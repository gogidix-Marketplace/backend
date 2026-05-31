package com.gogidix.centralconfiguration.notificationservice.interfaces.rest;

import com.gogidix.centralconfiguration.notificationservice.application.service.NotificationService;
import com.gogidix.centralconfiguration.notificationservice.domain.model.Notification;
import com.gogidix.centralconfiguration.notificationservice.domain.model.NotificationChannel;
import com.gogidix.centralconfiguration.notificationservice.domain.model.NotificationType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for Notification operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "APIs for notification management")
public class NotificationController {

    private final NotificationService notificationService;

    private static final String DEFAULT_TENANT_HEADER = "X-Tenant-ID";

    @PostMapping(produces = "application/json")
    @Operation(summary = "Send notification", description = "Sends a notification through specified channel")
    public ResponseEntity<Notification> sendNotification(
            @RequestBody Map<String, Object> request,

            @Parameter(description = "Tenant ID")
            @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId) {

        String typeStr = (String) request.getOrDefault("type", "SYSTEM_ALERT");
        String channelStr = (String) request.getOrDefault("channel", "EMAIL");
        String recipient = (String) request.get("recipient");
        String subject = (String) request.get("subject");
        String message = (String) request.get("message");
        String metadata = (String) request.get("metadata");

        NotificationType type = NotificationType.valueOf(typeStr.toUpperCase());
        NotificationChannel channel = NotificationChannel.valueOf(channelStr.toUpperCase());

        Notification response = notificationService.sendNotification(tenantId, type, channel, recipient, subject, message, metadata);

        return ResponseEntity.ok(response);
    }

    @GetMapping(produces = "application/json")
    @Operation(summary = "Get all notifications", description = "Retrieves all notifications for the tenant")
    public ResponseEntity<List<Notification>> getNotifications(
            @Parameter(description = "Tenant ID")
            @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId) {

        List<Notification> response = notificationService.getNotifications(tenantId);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/retry", produces = "application/json")
    @Operation(summary = "Retry failed notifications", description = "Retries all failed notifications for the tenant")
    public ResponseEntity<String> retryFailedNotifications(
            @Parameter(description = "Tenant ID")
            @RequestHeader(value = DEFAULT_TENANT_HEADER, defaultValue = "default") String tenantId) {

        notificationService.retryFailedNotifications(tenantId);
        return ResponseEntity.ok("Retry initiated");
    }
}
