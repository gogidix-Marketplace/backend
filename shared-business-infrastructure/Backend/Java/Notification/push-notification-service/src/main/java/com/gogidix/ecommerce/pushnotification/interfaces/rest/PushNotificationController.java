package com.gogidix.ecommerce.pushnotification.interfaces.rest;

import com.gogidix.ecommerce.pushnotification.application.dto.*;
import com.gogidix.ecommerce.pushnotification.application.service.PushNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/push-notifications")
@Tag(name = "PushNotification Service", description = "APIs for managing push-notifications")
public class PushNotificationController {

    private final PushNotificationService service;

    public PushNotificationController(PushNotificationService service) { this.service = service; }

    @GetMapping
    @Operation(summary = "Get all active push-notifications")
    public ResponseEntity<List<PushNotificationResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get push-notification by ID")
    public ResponseEntity<PushNotificationResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create push-notification")
    public ResponseEntity<PushNotificationResponse> create(@Valid @RequestBody CreatePushNotificationRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update push-notification")
    public ResponseEntity<PushNotificationResponse> update(@PathVariable String id, @Valid @RequestBody UpdatePushNotificationRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete push-notification")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
