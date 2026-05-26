package com.acme.backendfreshsense.billing.infrastructure.web;

import com.acme.backendfreshsense.billing.application.dto.PlanResponse;
import com.acme.backendfreshsense.billing.application.dto.SubscribeRequest;
import com.acme.backendfreshsense.billing.application.dto.SubscriptionResponse;
import com.acme.backendfreshsense.billing.application.service.BillingService;
import com.acme.backendfreshsense.shared.infrastructure.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing")
@Tag(name = "Billing", description = "Gestión de planes y suscripciones")
public class BillingController {

    private final BillingService billingService;
    private final JwtService jwtService;

    public BillingController(BillingService billingService, JwtService jwtService) {
        this.billingService = billingService;
        this.jwtService = jwtService;
    }

    @GetMapping("/plans")
    @Operation(summary = "Listar planes disponibles (público)")
    public ResponseEntity<List<PlanResponse>> getPlans() {
        return ResponseEntity.ok(billingService.getPlans());
    }

    @PostMapping("/subscribe")
    @Operation(summary = "Suscribirse a un plan premium")
    public ResponseEntity<SubscriptionResponse> subscribe(@Valid @RequestBody SubscribeRequest request,
                                                           HttpServletRequest httpRequest) {
        Long userId = extractUserId(httpRequest);
        return ResponseEntity.ok(billingService.subscribe(userId, request));
    }

    @DeleteMapping("/cancel")
    @Operation(summary = "Cancelar suscripción activa")
    public ResponseEntity<Void> cancel(HttpServletRequest httpRequest) {
        Long userId = extractUserId(httpRequest);
        billingService.cancel(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/subscription")
    @Operation(summary = "Ver suscripción activa del usuario")
    public ResponseEntity<SubscriptionResponse> getSubscription(HttpServletRequest httpRequest) {
        Long userId = extractUserId(httpRequest);
        return ResponseEntity.ok(billingService.getActiveSubscription(userId));
    }

    private Long extractUserId(HttpServletRequest request) {
        String token = null;
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("authToken".equals(c.getName())) { token = c.getValue(); break; }
            }
        }
        if (token == null) {
            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) token = header.substring(7);
        }
        return token != null ? jwtService.extractUserId(token) : null;
    }
}
