package com.gogidix.shared.courier.notification.interfaces.rest;

import com.gogidix.shared.courier.notification.application.command.MarkAsReadCommand;
import com.gogidix.shared.courier.notification.application.command.SendNotificationCommand;
import com.gogidix.shared.courier.notification.application.dto.NotificationDTO;
import com.gogidix.shared.courier.notification.application.query.NotificationQuery;
import com.gogidix.shared.courier.notification.application.service.NotificationApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Notification Operations
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationApplicationService notificationService;

    /**
     * Send a notification
     * POST /api/v1/notifications
     */
    @PostMapping
    public ResponseEntity<NotificationDTO> sendNotification(
            @Valid @RequestBody SendNotificationCommand command) {

        log.info("Sending notification to recipient: {}", command.getRecipientId());
        NotificationDTO notification = notificationService.sendNotification(command);
        return ResponseEntity.ok(notification);
    }

    /**
     * Send bulk notifications
     * POST /api/v1/notifications/bulk
     */
    @PostMapping("/bulk")
    public ResponseEntity<List<NotificationDTO>> sendBulkNotifications(
            @Valid @RequestBody List<SendNotificationCommand> commands) {

        log.info("Sending bulk notifications: {} recipients", commands.size());
        List<NotificationDTO> notifications = notificationService.sendBulkNotifications(commands);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Get notifications for recipient
     * GET /api/v1/notifications
     */
    @GetMapping
    public ResponseEntity<Page<NotificationDTO>> getNotifications(
            @RequestParam String tenantId,
            @RequestParam String recipientId,
            @RequestParam(required = false) String recipientType,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "50") Integer size,
            @RequestParam(required = false, defaultValue = "sentAt") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDirection) {

        NotificationQuery query = NotificationQuery.builder()
                .tenantId(tenantId)
                .recipientId(recipientId)
                .recipientType(recipientType)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        Page<NotificationDTO> results = notificationService.getNotifications(query);
        return ResponseEntity.ok(results);
    }

    /**
     * Get notification by ID
     * GET /api/v1/notifications/{notificationId}
     */
    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationDTO> getNotification(
            @PathVariable String notificationId,
            @RequestHeader(value = "X-Tenant-ID", required = true) String tenantId) {

        return notificationService.getNotification(tenantId, notificationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Mark notification as read
     * POST /api/v1/notifications/{notificationId}/read
     */
    @PostMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable String notificationId,
            @RequestHeader(value = "X-Tenant-ID", required = true) String tenantId) {

        MarkAsReadCommand command = MarkAsReadCommand.builder()
                .tenantId(tenantId)
                .notificationId(notificationId)
                .build();

        notificationService.markAsRead(command);
        return ResponseEntity.noContent().build();
    }

    /**
     * Mark all notifications as read for recipient
     * POST /api/v1/notifications/read-all
     */
    @PostMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(
            @RequestParam String tenantId,
            @RequestParam String recipientId) {

        notificationService.markAllAsRead(tenantId, recipientId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get unread count for recipient
     * GET /api/v1/notifications/unread-count
     */
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(
            @RequestParam String tenantId,
            @RequestParam String recipientId) {

        long count = notificationService.getUnreadCount(tenantId, recipientId);
        return ResponseEntity.ok(count);
    }
}
