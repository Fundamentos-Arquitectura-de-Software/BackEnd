package com.acme.backendfreshsense.billing.infrastructure.persistence.config;

import com.acme.backendfreshsense.billing.application.service.BillingService;
import com.acme.backendfreshsense.billing.domain.model.Plan;
import com.acme.backendfreshsense.billing.domain.model.PlanType;
import com.acme.backendfreshsense.billing.domain.repository.PlanRepository;
import com.acme.backendfreshsense.billing.domain.repository.SubscriptionRepository;
import com.acme.backendfreshsense.billing.infrastructure.persistence.adapter.PlanRepositoryAdapter;
import com.acme.backendfreshsense.billing.infrastructure.persistence.adapter.SubscriptionRepositoryAdapter;
import com.acme.backendfreshsense.billing.infrastructure.persistence.jpa.PlanJpaRepository;
import com.acme.backendfreshsense.billing.infrastructure.persistence.jpa.SubscriptionJpaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class BillingPersistenceConfig {

    @Bean
    public PlanRepository planRepository(PlanJpaRepository jpa) {
        return new PlanRepositoryAdapter(jpa);
    }

    @Bean
    public SubscriptionRepository subscriptionRepository(SubscriptionJpaRepository jpa) {
        return new SubscriptionRepositoryAdapter(jpa);
    }

    @Bean
    public BillingService billingService(PlanRepository planRepository,
                                          SubscriptionRepository subscriptionRepository,
                                          ApplicationEventPublisher eventPublisher) {
        return new BillingService(planRepository, subscriptionRepository, eventPublisher);
    }

    /** Inicializa los planes por defecto si no existen. */
    @Bean
    public PlanDataInitializer planDataInitializer(PlanRepository planRepository) {
        return new PlanDataInitializer(planRepository);
    }

    public static class PlanDataInitializer {

        private final PlanRepository planRepository;

        public PlanDataInitializer(PlanRepository planRepository) {
            this.planRepository = planRepository;
        }

        @PostConstruct
        public void init() {
            if (planRepository.findAll().isEmpty()) {
                planRepository.save(new Plan(
                        "Basic",
                        PlanType.BASIC,
                        BigDecimal.ZERO,
                        BigDecimal.ZERO,
                        List.of("Inventario hasta 50 productos", "Alertas básicas", "Recetas estándar")
                ));
                planRepository.save(new Plan(
                        "Premium",
                        PlanType.PREMIUM,
                        new BigDecimal("15.00"),
                        new BigDecimal("150.00"),
                        List.of("Inventario ilimitado", "Alertas avanzadas", "Recetas exclusivas", "Análisis detallado", "Reporte de impacto ambiental")
                ));
            }
        }
    }
}
