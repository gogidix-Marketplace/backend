package com.gogidix.platform.subscription.interfaces.rest;

import com.gogidix.platform.subscription.application.service.SubscriptionService;
import com.gogidix.platform.subscription.domain.model.Subscription;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for Subscription operations.
 */
@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Subscription", description = "Subscription management API")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    @Operation(summary = "Create a new subscription")
    public ResponseEntity<Subscription> createSubscription(@RequestBody Subscription subscription) {
        log.info("Creating subscription: {}", subscription.getId());
        return ResponseEntity.ok(subscriptionService.createSubscription(subscription));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get subscription by ID")
    public ResponseEntity<Subscription> getSubscription(
        @PathVariable UUID id,
        @RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(subscriptionService.getSubscription(id));
    }

    @GetMapping
    @Operation(summary = "Get all subscriptions for tenant")
    public ResponseEntity<List<Subscription>> getSubscriptions(
        @RequestHeader("X-Tenant-ID") String tenantId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionsByTenant(tenantId));
    }

    @PostMapping("/{id}/activate")
    @Operation(summary = "Activate subscription")
    public ResponseEntity<Void> activateSubscription(@PathVariable UUID id) {
        subscriptionService.activateSubscription(id);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel subscription")
    public ResponseEntity<Void> cancelSubscription(@PathVariable UUID id) {
        subscriptionService.cancelSubscription(id);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/health")
    @Operation(summary = "Health check")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Subscription Service is healthy");
    }
}
