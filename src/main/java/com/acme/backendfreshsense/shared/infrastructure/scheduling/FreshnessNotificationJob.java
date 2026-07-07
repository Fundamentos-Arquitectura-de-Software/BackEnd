package com.acme.backendfreshsense.shared.infrastructure.scheduling;

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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Job diario que revisa el inventario de cada usuario y publica
 * {@link ExpirationAlertRaisedEvent} cuando un producto está vencido, próximo a vencer
 * (≤ {@value #WARN_DAYS} días) o su frescura según el sensor del Edge es YELLOW/RED.
 * El módulo notifications escucha el evento y lo entrega por los canales activos
 * del usuario (bandeja in-app).
 *
 * <p>Cron configurable con {@code freshsense.freshness.cron} (default: 8:00 a.m. hora de Lima).</p>
 */
@Component
public class FreshnessNotificationJob {

    private static final Logger log = LoggerFactory.getLogger(FreshnessNotificationJob.class);
    private static final int WARN_DAYS = 2;

    private final ProductJpaRepository productRepo;
    private final MonitoringJpaRepository monitoringRepo;
    private final ApplicationEventPublisher events;

    public FreshnessNotificationJob(ProductJpaRepository productRepo,
                                    MonitoringJpaRepository monitoringRepo,
                                    ApplicationEventPublisher events) {
        this.productRepo = productRepo;
        this.monitoringRepo = monitoringRepo;
        this.events = events;
    }

    @Scheduled(cron = "${freshsense.freshness.cron:0 0 8 * * *}", zone = "America/Lima")
    public void notifyFreshness() {
        Map<Long, List<ProductEntity>> byUser = productRepo.findAll().stream()
                .collect(Collectors.groupingBy(ProductEntity::getUserId));

        int sent = 0;
        for (Map.Entry<Long, List<ProductEntity>> entry : byUser.entrySet()) {
            Long userId = entry.getKey();
            MonitoringReadingEntity latest = monitoringRepo
                    .findTopByUserIdOrderByRecordedAtDesc(userId).orElse(null);
            String sensorStatus   = latest != null ? latest.getStatus() : null;
            String sensorCategory = latest != null ? latest.getCategory() : null;

            for (ProductEntity product : entry.getValue()) {
                String severity = buildSeverity(product, sensorStatus, sensorCategory);
                if (severity != null) {
                    events.publishEvent(new ExpirationAlertRaisedEvent(userId, product.getName(), severity));
                    sent++;
                }
            }
        }
        log.info("Freshness job: {} notificaciones publicadas para {} usuarios.", sent, byUser.size());
    }

    /**
     * Combina vencimiento y sensor: manda el peor de los dos. Devuelve el texto de la
     * severidad para el usuario, o null si el producto está bien (no se notifica).
     */
    private String buildSeverity(ProductEntity product, String sensorStatus, String sensorCategory) {
        List<String> reasons = new ArrayList<>();

        if (product.getExpirationDate() != null) {
            long days = ChronoUnit.DAYS.between(LocalDate.now(), product.getExpirationDate());
            if (days < 0)       reasons.add(days == -1 ? "vencido desde ayer" : "vencido hace " + (-days) + " días");
            else if (days == 0) reasons.add("vence hoy");
            else if (days == 1) reasons.add("vence mañana");
            else if (days <= WARN_DAYS) reasons.add("vence en " + days + " días");
        }

        boolean sensorApplies = sensorStatus != null && sensorCategory != null
                && sensorCategory.equalsIgnoreCase(product.getCategory());
        if (sensorApplies) {
            if ("RED".equals(sensorStatus))    reasons.add("frescura crítica según el sensor");
            else if ("YELLOW".equals(sensorStatus)) reasons.add("frescura en riesgo según el sensor");
        }

        return reasons.isEmpty() ? null : String.join(" y ", reasons);
    }
}
