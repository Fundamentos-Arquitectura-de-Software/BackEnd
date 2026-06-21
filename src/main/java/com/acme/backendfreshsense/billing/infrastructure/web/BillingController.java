package com.acme.backendfreshsense.billing.infrastructure.web;

import com.acme.backendfreshsense.billing.application.dto.PlanResponse;
import com.acme.backendfreshsense.billing.application.dto.SubscribeRequest;
import com.acme.backendfreshsense.billing.application.dto.SubscriptionResponse;
import com.acme.backendfreshsense.billing.application.service.BillingService;
import com.acme.backendfreshsense.shared.infrastructure.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing")
@Tag(name = "Billing", description = "Gestión de planes y suscripciones")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @GetMapping("/plans")
    @Operation(summary = "Listar planes disponibles (público)")
    public ResponseEntity<List<PlanResponse>> getPlans() {
        return ResponseEntity.ok(billingService.getPlans());
    }

    @PostMapping("/subscribe")
    @Operation(summary = "Suscribirse a un plan premium")
    public ResponseEntity<SubscriptionResponse> subscribe(@Valid @RequestBody SubscribeRequest request) {
        return ResponseEntity.ok(billingService.subscribe(CurrentUser.id(), request));
    }

    @DeleteMapping("/cancel")
    @Operation(summary = "Cancelar suscripción activa")
    public ResponseEntity<Void> cancel() {
        billingService.cancel(CurrentUser.id());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/subscription")
    @Operation(summary = "Ver suscripción activa del usuario")
    public ResponseEntity<SubscriptionResponse> getSubscription() {
        return ResponseEntity.ok(billingService.getActiveSubscription(CurrentUser.id()));
    }
}
