package com.acme.backendfreshsense.notifications.infrastructure.web;

import com.acme.backendfreshsense.notifications.application.dto.NotificationPreferenceRequest;
import com.acme.backendfreshsense.notifications.application.dto.NotificationResponse;
import com.acme.backendfreshsense.notifications.application.dto.SendNotificationRequest;
import com.acme.backendfreshsense.notifications.application.service.NotificationService;
import com.acme.backendfreshsense.notifications.domain.model.InAppNotification;
import com.acme.backendfreshsense.notifications.domain.model.NotificationPayload;
import com.acme.backendfreshsense.notifications.domain.model.NotificationPreference;
import com.acme.backendfreshsense.notifications.domain.repository.InAppNotificationRepository;
import com.acme.backendfreshsense.shared.infrastructure.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications", description = "Envío y gestión de notificaciones (TS32)")
public class NotificationController {

    private final NotificationService notificationService;
    private final InAppNotificationRepository inAppNotificationRepository;

    public NotificationController(NotificationService notificationService,
                                   InAppNotificationRepository inAppNotificationRepository) {
        this.notificationService = notificationService;
        this.inAppNotificationRepository = inAppNotificationRepository;
    }

    /** TS32 — Enviar notificación a un usuario. Requiere rol ADMIN. */
    @PostMapping("/send")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "TS32: Enviar notificación a un usuario")
    public ResponseEntity<Map<String, String>> send(@Valid @RequestBody SendNotificationRequest request) {
        NotificationPayload payload = new NotificationPayload(
                request.getUserId(),
                request.getTitle(),
                request.getMessage(),
                request.getAlertType(),
                request.getChannelType()
        );
        notificationService.send(payload);
        return ResponseEntity.ok(Map.of("status", "sent"));
    }

    /** GET /api/notifications — bandeja in-app del usuario autenticado. */
    @GetMapping
    @Operation(summary = "Obtener bandeja de notificaciones in-app")
    public ResponseEntity<List<NotificationResponse>> getInbox() {
        Long userId = CurrentUser.id();
        List<NotificationResponse> result = inAppNotificationRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(result);
    }

    /** PATCH /api/notifications/{id}/read — marca una notificación como leída. */
    @PatchMapping("/{id}/read")
    @Operation(summary = "Marcar notificación como leída")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        CurrentUser.id();
        inAppNotificationRepository.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    /** GET /api/notifications/preferences */
    @GetMapping("/preferences")
    @Operation(summary = "Obtener preferencias de notificación")
    public ResponseEntity<NotificationPreference> getPreferences() {
        return ResponseEntity.ok(notificationService.getPreferences(CurrentUser.id()));
    }

    /** PUT /api/notifications/preferences */
    @PutMapping("/preferences")
    @Operation(summary = "Actualizar preferencias de notificación (US09/US24)")
    public ResponseEntity<NotificationPreference> updatePreferences(
            @Valid @RequestBody NotificationPreferenceRequest request) {
        Long userId = CurrentUser.id();
        NotificationPreference updated = new NotificationPreference(userId);
        updated.setInAppEnabled(request.isInAppEnabled());
        updated.setEmailEnabled(request.isEmailEnabled());
        updated.setPushEnabled(request.isPushEnabled());
        updated.setQuietStart(request.getQuietStart());
        updated.setQuietEnd(request.getQuietEnd());
        return ResponseEntity.ok(notificationService.updatePreferences(userId, updated));
    }

    private NotificationResponse toResponse(InAppNotification n) {
        return new NotificationResponse(n.getId(), n.getTitle(), n.getMessage(), n.getAlertType(), n.isRead(), n.getCreatedAt());
    }
}
