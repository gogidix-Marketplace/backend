package com.gogidix.customersupport.notification.interfaces.rest;

import com.gogidix.customersupport.notification.application.dto.NotificationDto;
import com.gogidix.customersupport.notification.application.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

/**
 * REST Controller for Notification operations
 */
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Notifications", description = "Notification Management API")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Send notification
     */
    @PostMapping("/send")
    @Operation(summary = "Send notification", description = "Send a new notification to a recipient")
    public ResponseEntity<NotificationDto> sendNotification(
            @Valid @RequestBody NotificationDto.SendNotificationRequest request) {

        log.info("POST /api/v1/notifications/send - type: {}, recipient: {}",
                request.getType(), request.getRecipientId());

        NotificationDto sent = notificationService.sendNotification(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(sent);
    }

    /**
     * Send bulk notifications
     */
    @PostMapping("/bulk")
    @Operation(summary = "Send bulk notifications", description = "Send notifications to multiple recipients")
    public ResponseEntity<List<NotificationDto>> sendBulkNotifications(
            @Valid @RequestBody NotificationDto.BulkNotificationRequest request) {

        log.info("POST /api/v1/notifications/bulk - recipients: {}", request.getRecipientIds().size());

        List<NotificationDto> sent = notificationService.sendBulkNotifications(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(sent);
    }

    /**
     * Get notification by ID
     */
    @GetMapping("/{notificationId}")
    @Operation(summary = "Get notification", description = "Retrieve a notification by ID")
    public ResponseEntity<NotificationDto> getNotification(
            @Parameter(description = "Notification ID")
            @PathVariable String notificationId) {

        log.info("GET /api/v1/notifications/{}", notificationId);
        NotificationDto notification = notificationService.getNotificationById(notificationId);
        return ResponseEntity.ok(notification);
    }

    /**
     * Get notifications for recipient
     */
    @GetMapping("/recipient/{recipientId}")
    @Operation(summary = "Get recipient notifications", description = "Retrieve all notifications for a recipient")
    public ResponseEntity<List<NotificationDto>> getRecipientNotifications(
            @Parameter(description = "Recipient ID")
            @PathVariable String recipientId,
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId,
            @Parameter(description = "Status filter")
            @RequestParam(required = false) String status,
            @Parameter(description = "Channel filter")
            @RequestParam(required = false) String channel) {

        log.info("GET /api/v1/notifications/recipient/{}", recipientId);
        List<NotificationDto> notifications = notificationService.getNotificationsByRecipient(
                tenantId, recipientId, status, channel);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Get notifications by status
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Get notifications by status", description = "Retrieve notifications by status")
    public ResponseEntity<List<NotificationDto>> getNotificationsByStatus(
            @Parameter(description = "Notification Status (PENDING, SENDING, SENT, DELIVERED, READ, FAILED, CANCELLED)")
            @PathVariable String status,
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("GET /api/v1/notifications/status/{}", status);
        List<NotificationDto> notifications = notificationService.getNotificationsByStatus(tenantId, status);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Get pending notifications
     */
    @GetMapping("/pending")
    @Operation(summary = "Get pending notifications", description = "Retrieve all pending notifications")
    public ResponseEntity<List<NotificationDto>> getPendingNotifications(
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("GET /api/v1/notifications/pending");
        List<NotificationDto> notifications = notificationService.getPendingNotifications(tenantId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Get failed notifications
     */
    @GetMapping("/failed")
    @Operation(summary = "Get failed notifications", description = "Retrieve all failed notifications")
    public ResponseEntity<List<NotificationDto>> getFailedNotifications(
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId) {

        log.info("GET /api/v1/notifications/failed");
        List<NotificationDto> notifications = notificationService.getFailedNotifications(tenantId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Get notifications by date range
     */
    @GetMapping("/date-range")
    @Operation(summary = "Get notifications by date range", description = "Retrieve notifications within a date range")
    public ResponseEntity<List<NotificationDto>> getNotificationsByDateRange(
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId,
            @Parameter(description = "Start Date (ISO format)")
            @RequestParam String startDate,
            @Parameter(description = "End Date (ISO format)")
            @RequestParam String endDate) {

        log.info("GET /api/v1/notifications/date-range");

        Instant start = Instant.parse(startDate);
        Instant end = Instant.parse(endDate);

        List<NotificationDto> notifications = notificationService.getNotificationsByDateRange(tenantId, start, end);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Mark notification as read
     */
    @PostMapping("/{notificationId}/read")
    @Operation(summary = "Mark as read", description = "Mark a notification as read")
    public ResponseEntity<NotificationDto> markAsRead(
            @Parameter(description = "Notification ID")
            @PathVariable String notificationId) {

        log.info("POST /api/v1/notifications/{}/read", notificationId);
        NotificationDto updated = notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(updated);
    }

    /**
     * Mark notification as delivered
     */
    @PostMapping("/{notificationId}/delivered")
    @Operation(summary = "Mark as delivered", description = "Mark a notification as delivered")
    public ResponseEntity<NotificationDto> markAsDelivered(
            @Parameter(description = "Notification ID")
            @PathVariable String notificationId) {

        log.info("POST /api/v1/notifications/{}/delivered", notificationId);
        NotificationDto updated = notificationService.markAsDelivered(notificationId);
        return ResponseEntity.ok(updated);
    }

    /**
     * Retry failed notification
     */
    @PostMapping("/{notificationId}/retry")
    @Operation(summary = "Retry notification", description = "Retry a failed notification")
    public ResponseEntity<NotificationDto> retryNotification(
            @Parameter(description = "Notification ID")
            @PathVariable String notificationId) {

        log.info("POST /api/v1/notifications/{}/retry", notificationId);
        NotificationDto retried = notificationService.retryNotification(notificationId);
        return ResponseEntity.ok(retried);
    }

    /**
     * Cancel notification
     */
    @PostMapping("/{notificationId}/cancel")
    @Operation(summary = "Cancel notification", description = "Cancel a pending notification")
    public ResponseEntity<NotificationDto> cancelNotification(
            @Parameter(description = "Notification ID")
            @PathVariable String notificationId) {

        log.info("POST /api/v1/notifications/{}/cancel", notificationId);
        NotificationDto cancelled = notificationService.cancelNotification(notificationId);
        return ResponseEntity.ok(cancelled);
    }

    /**
     * Delete notification
     */
    @DeleteMapping("/{notificationId}")
    @Operation(summary = "Delete notification", description = "Delete a notification")
    public ResponseEntity<Void> deleteNotification(
            @Parameter(description = "Notification ID")
            @PathVariable String notificationId) {

        log.info("DELETE /api/v1/notifications/{}", notificationId);
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get notification statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get notification statistics", description = "Retrieve notification statistics for a tenant")
    public ResponseEntity<NotificationService.NotificationStatisticsDto> getNotificationStatistics(
            @Parameter(description = "Tenant ID")
            @RequestParam(required = false) String tenantId,
            @Parameter(description = "Start Date (ISO format)")
            @RequestParam String startDate,
            @Parameter(description = "End Date (ISO format)")
            @RequestParam String endDate) {

        log.info("GET /api/v1/notifications/statistics");

        Instant start = Instant.parse(startDate);
        Instant end = Instant.parse(endDate);

        NotificationService.NotificationStatisticsDto stats = notificationService.getNotificationStatistics(tenantId, start, end);
        return ResponseEntity.ok(stats);
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the notification service is healthy")
    public ResponseEntity<HealthResponse> health() {
        return ResponseEntity.ok(
                new HealthResponse("UP", "Notification Service is running")
        );
    }

    /**
     * Exception handler for IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        log.error("Illegal argument: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("BAD_REQUEST", ex.getMessage()));
    }

    /**
     * Exception handler for general exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred"));
    }

    /**
     * Health response record
     */
    private record HealthResponse(String status, String message) {}

    /**
     * Error response record
     */
    private record ErrorResponse(String code, String message) {}
}
