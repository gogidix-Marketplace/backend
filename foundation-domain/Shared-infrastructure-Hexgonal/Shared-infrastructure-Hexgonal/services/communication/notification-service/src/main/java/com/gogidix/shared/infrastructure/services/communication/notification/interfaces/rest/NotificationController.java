package com.gogidix.shared.infrastructure.services.communication.notification.interfaces.rest;

import com.gogidix.shared.infrastructure.services.communication.notification.application.dto.request.SendNotificationRequestDto;
import com.gogidix.shared.infrastructure.services.communication.notification.application.dto.response.NotificationResponseDto;
import com.gogidix.shared.infrastructure.services.communication.notification.application.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for notifications.
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Notification management APIs")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @Operation(summary = "Send notification", description = "Send a new notification")
    public ResponseEntity<NotificationResponseDto> sendNotification(
            @Valid @RequestBody SendNotificationRequestDto request) {
        NotificationResponseDto response = notificationService.sendNotification(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping("/{notificationId}")
    @Operation(summary = "Get notification", description = "Get notification by ID")
    public ResponseEntity<NotificationResponseDto> getNotification(@PathVariable String notificationId) {
        NotificationResponseDto response = notificationService.getNotification(notificationId);
        return response != null ? ResponseEntity.ok(response)
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/{notificationId}/retry")
    @Operation(summary = "Retry notification", description = "Retry a failed notification")
    public ResponseEntity<NotificationResponseDto> retryNotification(@PathVariable String notificationId) {
        NotificationResponseDto response = notificationService.retryNotification(notificationId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{notificationId}")
    @Operation(summary = "Cancel notification", description = "Cancel a pending notification")
    public ResponseEntity<Void> cancelNotification(@PathVariable String notificationId) {
        notificationService.cancelNotification(notificationId);
        return ResponseEntity.noContent().build();
    }
}
