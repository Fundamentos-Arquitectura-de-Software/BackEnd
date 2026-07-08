package com.acme.backendfreshsense.shared.infrastructure.scheduling;

import com.acme.backendfreshsense.alerts.application.dto.AlertRequest;
import com.acme.backendfreshsense.alerts.application.dto.AlertResponse;
import com.acme.backendfreshsense.alerts.infrastructure.feign.AlertsFeignClient;
import com.acme.backendfreshsense.inventory.infrastructure.persistence.jpa.ProductEntity;
import com.acme.backendfreshsense.inventory.infrastructure.persistence.jpa.ProductJpaRepository;
import com.acme.backendfreshsense.monitoring.infrastructure.persistence.jpa.MonitoringReadingEntity;
import com.acme.backendfreshsense.monitoring.infrastructure.persistence.jpa.MonitoringJpaRepository;
import com.acme.backendfreshsense.shared.domain.event.ExpirationAlertRaisedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Job periódico que revisa el inventario de cada usuario y, por cada producto vencido,
 * próximo a vencer (≤ {@value #WARN_DAYS} días) o con frescura YELLOW/RED según el
 * sensor del Edge:
 * <ul>
 *   <li>Publica {@link ExpirationAlertRaisedEvent} → el módulo notifications lo entrega
 *       por los canales activos (máx. 1 notificación por producto/motivo POR DÍA — el
 *       dedup vive en NotificationService).</li>
 *   <li>Crea una alerta dinámica en el alerts-service (máx. 1 alerta por título — si ya
 *       existe, sea activa, silenciada o resuelta, no se duplica).</li>
 * </ul>
 *
 * <p>Cron configurable con {@code freshsense.freshness.cron} (default: 8:00 a.m. hora de Lima).</p>
 */
@Component
public class FreshnessNotificationJob {

    private static final Logger log = LoggerFactory.getLogger(FreshnessNotificationJob.class);
    private static final int WARN_DAYS = 2;
    private static final ZoneId LIMA = ZoneId.of("America/Lima");
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /** Un motivo de deterioro detectado: texto para el usuario + severidad + metadata de alerta. */
    private record Reason(String text, String severity, String source, String alertTitle) {}

    private final ProductJpaRepository productRepo;
    private final MonitoringJpaRepository monitoringRepo;
    private final ApplicationEventPublisher events;
    private final AlertsFeignClient alertsFeignClient;

    public FreshnessNotificationJob(ProductJpaRepository productRepo,
                                    MonitoringJpaRepository monitoringRepo,
                                    ApplicationEventPublisher events,
                                    AlertsFeignClient alertsFeignClient) {
        this.productRepo = productRepo;
        this.monitoringRepo = monitoringRepo;
        this.events = events;
        this.alertsFeignClient = alertsFeignClient;
    }

    @Scheduled(cron = "${freshsense.freshness.cron:0 0 8 * * *}", zone = "America/Lima")
    public void notifyFreshness() {
        Map<Long, List<ProductEntity>> byUser = productRepo.findAll().stream()
                .collect(Collectors.groupingBy(ProductEntity::getUserId));

        int notified = 0;
        int alertsCreated = 0;
        for (Map.Entry<Long, List<ProductEntity>> entry : byUser.entrySet()) {
            Long userId = entry.getKey();
            MonitoringReadingEntity latest = monitoringRepo
                    .findTopByUserIdOrderByRecordedAtDesc(userId).orElse(null);
            String sensorStatus   = latest != null ? latest.getStatus() : null;
            String sensorCategory = latest != null ? latest.getCategory() : null;

            Set<String> existingAlertTitles = fetchAlertTitles(userId);

            for (ProductEntity product : entry.getValue()) {
                List<Reason> reasons = buildReasons(product, sensorStatus, sensorCategory);
                if (reasons.isEmpty()) continue;

                // Notificación (bandeja): un solo aviso combinado por producto.
                String combined = reasons.stream().map(Reason::text).collect(Collectors.joining(" y "));
                events.publishEvent(new ExpirationAlertRaisedEvent(userId, product.getName(), combined));
                notified++;

                // Alertas dinámicas: una por motivo, sin duplicar títulos existentes.
                // (existingAlertTitles null = alerts-service no disponible; se omite sin romper.)
                if (existingAlertTitles == null) continue;
                for (Reason reason : reasons) {
                    if (!existingAlertTitles.add(reason.alertTitle())) continue;
                    if (createAlert(userId, product.getName(), reason)) alertsCreated++;
                }
            }
        }
        log.info("Freshness job: {} notificaciones publicadas y {} alertas creadas para {} usuarios.",
                notified, alertsCreated, byUser.size());
    }

    /** Títulos de las alertas ya existentes del usuario (para no duplicar). */
    private Set<String> fetchAlertTitles(Long userId) {
        try {
            return alertsFeignClient.getAll(userId).stream()
                    .map(AlertResponse::title)
                    .collect(Collectors.toCollection(HashSet::new));
        } catch (Exception e) {
            // alerts-service caído/no sincronizado: no bloquear las notificaciones.
            log.warn("No se pudieron leer alertas del usuario {}: {}", userId, e.getMessage());
            return null;
        }
    }

    private boolean createAlert(Long userId, String productName, Reason reason) {
        try {
            alertsFeignClient.create(userId, new AlertRequest(
                    reason.alertTitle(),
                    "Tu producto '" + productName + "' " + reason.text() + ".",
                    reason.severity(),
                    reason.source(),
                    "active",
                    LocalDate.now(LIMA).format(DATE_FMT)
            ));
            return true;
        } catch (Exception e) {
            log.warn("No se pudo crear la alerta '{}' del usuario {}: {}",
                    reason.alertTitle(), userId, e.getMessage());
            return false;
        }
    }

    /**
     * Motivos de deterioro del producto, combinando vencimiento y sensor (manda el peor).
     * Lista vacía si el producto está bien.
     */
    private List<Reason> buildReasons(ProductEntity product, String sensorStatus, String sensorCategory) {
        List<Reason> reasons = new ArrayList<>();

        if (product.getExpirationDate() != null) {
            long days = ChronoUnit.DAYS.between(LocalDate.now(LIMA), product.getExpirationDate());
            String name = product.getName();
            if (days < 0) {
                String text = days == -1 ? "vencido desde ayer" : "vencido hace " + (-days) + " días";
                reasons.add(new Reason(text, "critical", "Expiration", name + " vencido"));
            } else if (days == 0) {
                reasons.add(new Reason("vence hoy", "warning", "Expiration", name + " por vencer"));
            } else if (days == 1) {
                reasons.add(new Reason("vence mañana", "warning", "Expiration", name + " por vencer"));
            } else if (days <= WARN_DAYS) {
                reasons.add(new Reason("vence en " + days + " días", "warning", "Expiration", name + " por vencer"));
            }
        }

        boolean sensorApplies = sensorStatus != null && sensorCategory != null
                && sensorCategory.equalsIgnoreCase(product.getCategory());
        if (sensorApplies) {
            if ("RED".equals(sensorStatus)) {
                reasons.add(new Reason("frescura crítica según el sensor", "critical", "Sensor",
                        "Frescura crítica: " + product.getName()));
            } else if ("YELLOW".equals(sensorStatus)) {
                reasons.add(new Reason("frescura en riesgo según el sensor", "warning", "Sensor",
                        "Frescura en riesgo: " + product.getName()));
            }
        }

        return reasons;
    }
}
