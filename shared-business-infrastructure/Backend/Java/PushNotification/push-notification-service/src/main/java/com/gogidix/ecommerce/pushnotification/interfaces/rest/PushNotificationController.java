package com.gogidix.ecommerce.pushnotification.interfaces.rest;

import com.gogidix.ecommerce.pushnotification.application.dto.CreatePushNotificationRequest;
import com.gogidix.ecommerce.pushnotification.application.dto.UpdatePushNotificationRequest;
import com.gogidix.ecommerce.pushnotification.application.dto.PushNotificationResponse;
import com.gogidix.ecommerce.pushnotification.application.service.PushNotificationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/push-notifications")
public class PushNotificationController {

    private final PushNotificationService service;

    public PushNotificationController(PushNotificationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PushNotificationResponse> create(@Valid @RequestBody CreatePushNotificationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PushNotificationResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<PushNotificationResponse>> getList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(service.getList(page, size));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PushNotificationResponse> update(@PathVariable String id, @RequestBody UpdatePushNotificationRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}