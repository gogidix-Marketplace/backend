package com.gogidix.shared.warehousing.availability.interfaces.rest;

import com.gogidix.shared.warehousing.availability.application.command.*;
import com.gogidix.shared.warehousing.availability.application.dto.*;
import com.gogidix.shared.warehousing.availability.application.service.AvailabilityService;
import com.gogidix.shared.warehousing.availability.domain.entity.AvailabilitySlot;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Availability REST Controller
 */
@RestController
@RequestMapping("/availability")
@RequiredArgsConstructor
@Tag(name = "Availability", description = "Storage availability and capacity management APIs")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @PostMapping("/check")
    @Operation(summary = "Check availability", description = "Check storage availability for given parameters")
    public ResponseEntity<AvailabilityCheckResultDTO> checkAvailability(
            @Valid @RequestBody CheckAvailabilityCommand command) {
        AvailabilityCheckResultDTO result = availabilityService.checkAvailability(command);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/reserve")
    @Operation(summary = "Reserve capacity", description = "Reserve storage capacity")
    public ResponseEntity<ReservationResultDTO> reserveCapacity(
            @Valid @RequestBody ReserveCapacityCommand command) {
        ReservationResultDTO result = availabilityService.reserveCapacity(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/release")
    @Operation(summary = "Release capacity", description = "Release reserved storage capacity")
    public ResponseEntity<Void> releaseCapacity(
            @Valid @RequestBody ReleaseCapacityCommand command) {
        availabilityService.releaseCapacity(command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/warehouses/{warehouseId}")
    @Operation(summary = "Get warehouse availability", description = "Get storage availability for a warehouse")
    public ResponseEntity<List<StorageAvailabilityDTO>> getWarehouseAvailability(
            @Parameter(description = "Warehouse ID") @PathVariable String warehouseId) {
        List<StorageAvailabilityDTO> availability = availabilityService.getWarehouseAvailability(warehouseId);
        return ResponseEntity.ok(availability);
    }

    @GetMapping
    @Operation(summary = "Get all availability", description = "Get all storage availability for current tenant")
    public ResponseEntity<List<StorageAvailabilityDTO>> getAllAvailability() {
        List<StorageAvailabilityDTO> availability = availabilityService.getAllAvailability();
        return ResponseEntity.ok(availability);
    }

    @GetMapping("/warehouses/{warehouseId}/pools")
    @Operation(summary = "Get capacity pools", description = "Get capacity pools for a warehouse")
    public ResponseEntity<List<CapacityPoolDTO>> getCapacityPools(
            @Parameter(description = "Warehouse ID") @PathVariable String warehouseId) {
        List<CapacityPoolDTO> pools = availabilityService.getCapacityPools(warehouseId);
        return ResponseEntity.ok(pools);
    }

    @PostMapping("/pools")
    @Operation(summary = "Create capacity pool", description = "Create a new capacity pool")
    public ResponseEntity<CapacityPoolDTO> createCapacityPool(
            @Valid @RequestBody CreateCapacityPoolCommand command) {
        CapacityPoolDTO pool = availabilityService.createCapacityPool(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(pool);
    }

    @PutMapping("/availability/{id}")
    @Operation(summary = "Update availability", description = "Update storage availability metrics")
    public ResponseEntity<StorageAvailabilityDTO> updateAvailability(
            @Parameter(description = "Availability ID") @PathVariable String id,
            @RequestParam Integer usedCapacity,
            @RequestParam Integer reservedCapacity) {
        StorageAvailabilityDTO availability = availabilityService.updateAvailability(id, usedCapacity, reservedCapacity);
        return ResponseEntity.ok(availability);
    }

    @GetMapping("/warehouses/{warehouseId}/slots")
    @Operation(summary = "Get available slots", description = "Get available time slots for operations")
    public ResponseEntity<List<AvailabilitySlotDTO>> getAvailableSlots(
            @Parameter(description = "Warehouse ID") @PathVariable String warehouseId,
            @Parameter(description = "Slot type") @RequestParam AvailabilitySlot.SlotType slotType,
            @Parameter(description = "Start time") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "End time") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<AvailabilitySlotDTO> slots = availabilityService.getAvailableSlots(warehouseId, slotType, startTime, endTime);
        return ResponseEntity.ok(slots);
    }

    @PostMapping("/cleanup")
    @Operation(summary = "Cleanup expired reservations", description = "Clean up expired reservations")
    public ResponseEntity<Void> cleanupExpiredReservations() {
        availabilityService.cleanupExpiredReservations();
        return ResponseEntity.ok().build();
    }
}
