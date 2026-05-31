package com.gogidix.aiservices.ainotificationservice.interfaces.rest;

import com.gogidix.aiservices.ainotificationservice.application.service.NotificationService;
import com.gogidix.aiservices.ainotificationservice.domain.model.Notification;
import com.gogidix.aiservices.ainotificationservice.domain.model.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<Notification> sendNotification(
            @RequestParam String recipientId,
            @RequestParam NotificationType type,
            @RequestParam String subject,
            @RequestParam String content) {
        Notification notification = notificationService.sendNotification(recipientId, type, subject, content);
        return ResponseEntity.ok(notification);
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<Notification> getNotification(@PathVariable String notificationId) {
        Notification notification = notificationService.getNotification(notificationId);
        return ResponseEntity.ok(notification);
    }

    @GetMapping("/recipient/{recipientId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable String recipientId) {
        List<Notification> notifications = notificationService.getUserNotifications(recipientId);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/{notificationId}/cancel")
    public ResponseEntity<Void> cancelNotification(@PathVariable String notificationId) {
        notificationService.cancelNotification(notificationId);
        return ResponseEntity.noContent().build();
    }
}
