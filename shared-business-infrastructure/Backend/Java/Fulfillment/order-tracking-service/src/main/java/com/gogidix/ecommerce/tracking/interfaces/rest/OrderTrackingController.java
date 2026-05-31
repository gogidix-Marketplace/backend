package com.gogidix.ecommerce.tracking.interfaces.rest;

import com.gogidix.ecommerce.tracking.domain.model.OrderTracking;
import com.gogidix.ecommerce.tracking.domain.model.OrderTracking.TrackingEvent;
import com.gogidix.ecommerce.tracking.domain.service.OrderTrackingService;
import com.gogidix.ecommerce.tracking.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tracking")
@RequiredArgsConstructor
@Tag(name = "Order Tracking", description = "APIs for managing order tracking information")
public class OrderTrackingController {

    private final OrderTrackingService trackingService;

    @GetMapping("/{trackingId}")
    @Operation(summary = "Get tracking by ID", description = "Retrieve order tracking information by tracking ID")
    public ResponseEntity<OrderTracking> getTracking(
            @Parameter(description = "Tracking ID") @PathVariable String trackingId) {
        OrderTracking tracking = trackingService.getTracking(RequestContextHolder.getTenantId(), trackingId);
        return tracking != null ? ResponseEntity.ok(tracking) : ResponseEntity.notFound().build();
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get tracking by order ID", description = "Retrieve order tracking information by order ID")
    public ResponseEntity<OrderTracking> getTrackingByOrderId(
            @Parameter(description = "Order ID") @PathVariable String orderId) {
        OrderTracking tracking = trackingService.getTrackingByOrderId(RequestContextHolder.getTenantId(), orderId);
        return tracking != null ? ResponseEntity.ok(tracking) : ResponseEntity.notFound().build();
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get tracking by status", description = "Retrieve order tracking information filtered by status")
    public ResponseEntity<List<OrderTracking>> getTrackingByStatus(
            @Parameter(description = "Tracking status") @PathVariable OrderTracking.TrackingStatus status) {
        return ResponseEntity.ok(trackingService.getTrackingByStatus(RequestContextHolder.getTenantId(), status));
    }

    @PostMapping
    @Operation(summary = "Create tracking", description = "Create a new order tracking record")
    public ResponseEntity<OrderTracking> createTracking(
            @RequestBody OrderTracking tracking) {
        tracking.setTenantId(RequestContextHolder.getTenantId());
        return ResponseEntity.ok(trackingService.createTracking(tracking));
    }

    @PostMapping("/{trackingId}/events")
    @Operation(summary = "Add tracking event", description = "Add a tracking event to an existing tracking record")
    public ResponseEntity<OrderTracking> addTrackingEvent(
            @Parameter(description = "Tracking ID") @PathVariable String trackingId,
            @RequestBody TrackingEvent event) {
        OrderTracking tracking = trackingService.addTrackingEvent(RequestContextHolder.getTenantId(), trackingId, event);
        return tracking != null ? ResponseEntity.ok(tracking) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{trackingId}/status")
    @Operation(summary = "Update tracking status", description = "Update the status of an order tracking record")
    public ResponseEntity<OrderTracking> updateStatus(
            @Parameter(description = "Tracking ID") @PathVariable String trackingId,
            @Parameter(description = "New tracking status") @RequestParam OrderTracking.TrackingStatus status) {
        OrderTracking tracking = trackingService.updateStatus(RequestContextHolder.getTenantId(), trackingId, status);
        return tracking != null ? ResponseEntity.ok(tracking) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{trackingId}/carrier")
    @Operation(summary = "Update carrier information", description = "Update carrier and tracking number information")
    public ResponseEntity<OrderTracking> updateCarrierInfo(
            @Parameter(description = "Tracking ID") @PathVariable String trackingId,
            @Parameter(description = "Carrier name") @RequestParam String carrier,
            @Parameter(description = "Carrier tracking number") @RequestParam String carrierTrackingNumber) {
        OrderTracking tracking = trackingService.updateCarrierInfo(RequestContextHolder.getTenantId(), trackingId, carrier, carrierTrackingNumber);
        return tracking != null ? ResponseEntity.ok(tracking) : ResponseEntity.notFound().build();
    }
}
