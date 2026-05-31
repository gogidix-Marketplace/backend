package com.gogidix.ecommerce.notification.interfaces.rest;

import com.gogidix.ecommerce.notification.application.dto.*;
import com.gogidix.ecommerce.notification.application.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@Tag(name = "Notification Service", description = "APIs for managing notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Get all active notifications")
    public ResponseEntity<List<NotificationResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get notification by ID")
    public ResponseEntity<NotificationResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create notification")
    public ResponseEntity<NotificationResponse> create(@Valid @RequestBody CreateNotificationRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update notification")
    public ResponseEntity<NotificationResponse> update(@PathVariable String id, @Valid @RequestBody UpdateNotificationRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete notification")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
