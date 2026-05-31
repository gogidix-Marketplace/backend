package com.gogidix.ecommerce.realtime.interfaces.rest;

import com.gogidix.ecommerce.realtime.domain.model.RealtimeTracking;
import com.gogidix.ecommerce.realtime.domain.model.RealtimeTracking.Location;
import com.gogidix.ecommerce.realtime.domain.service.RealtimeTrackingService;
import com.gogidix.ecommerce.realtime.shared.requestcontext.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/realtime-tracking")
@RequiredArgsConstructor
@Tag(name = "Realtime Tracking", description = "APIs for managing realtime tracking information")
public class RealtimeTrackingController {

    private final RealtimeTrackingService trackingService;

    @GetMapping("/{trackingId}")
    @Operation(summary = "Get realtime tracking by ID", description = "Retrieve realtime tracking information by tracking ID")
    public ResponseEntity<RealtimeTracking> getTracking(
            @Parameter(description = "Tracking ID") @PathVariable String trackingId) {
        RealtimeTracking tracking = trackingService.getTracking(RequestContextHolder.getTenantId(), trackingId);
        return tracking != null ? ResponseEntity.ok(tracking) : ResponseEntity.notFound().build();
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get realtime tracking by order ID", description = "Retrieve realtime tracking information by order ID")
    public ResponseEntity<RealtimeTracking> getTrackingByOrderId(
            @Parameter(description = "Order ID") @PathVariable String orderId) {
        RealtimeTracking tracking = trackingService.getTrackingByOrderId(RequestContextHolder.getTenantId(), orderId);
        return tracking != null ? ResponseEntity.ok(tracking) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Create realtime tracking", description = "Create a new realtime tracking record")
    public ResponseEntity<RealtimeTracking> createTracking(
            @RequestBody RealtimeTracking tracking) {
        tracking.setTenantId(RequestContextHolder.getTenantId());
        return ResponseEntity.ok(trackingService.createTracking(tracking));
    }

    @PutMapping("/{trackingId}/location")
    @Operation(summary = "Update location", description = "Update the current location of a tracking record")
    public ResponseEntity<RealtimeTracking> updateLocation(
            @Parameter(description = "Tracking ID") @PathVariable String trackingId,
            @RequestBody Location location) {
        RealtimeTracking tracking = trackingService.updateLocation(RequestContextHolder.getTenantId(), trackingId, location);
        return tracking != null ? ResponseEntity.ok(tracking) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{trackingId}/metrics")
    @Operation(summary = "Update tracking metrics", description = "Update speed, heading, and altitude metrics")
    public ResponseEntity<RealtimeTracking> updateMetrics(
            @Parameter(description = "Tracking ID") @PathVariable String trackingId,
            @Parameter(description = "Current speed") @RequestParam(required = false) Double speed,
            @Parameter(description = "Current heading") @RequestParam(required = false) Double heading,
            @Parameter(description = "Current altitude") @RequestParam(required = false) Double altitude) {
        RealtimeTracking tracking = trackingService.updateMetrics(RequestContextHolder.getTenantId(), trackingId, speed, heading, altitude);
        return tracking != null ? ResponseEntity.ok(tracking) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{trackingId}/status")
    @Operation(summary = "Update device status", description = "Update battery and signal status of tracking device")
    public ResponseEntity<RealtimeTracking> updateDeviceStatus(
            @Parameter(description = "Tracking ID") @PathVariable String trackingId,
            @Parameter(description = "Battery status") @RequestParam RealtimeTracking.BatteryStatus batteryStatus,
            @Parameter(description = "Signal strength") @RequestParam RealtimeTracking.SignalStrength signalStrength) {
        RealtimeTracking tracking = trackingService.updateDeviceStatus(RequestContextHolder.getTenantId(), trackingId, batteryStatus, signalStrength);
        return tracking != null ? ResponseEntity.ok(tracking) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{trackingId}")
    @Operation(summary = "Deactivate tracking", description = "Deactivate a realtime tracking record")
    public ResponseEntity<Void> deactivateTracking(
            @Parameter(description = "Tracking ID") @PathVariable String trackingId) {
        trackingService.deactivateTracking(RequestContextHolder.getTenantId(), trackingId);
        return ResponseEntity.ok().build();
    }
}
