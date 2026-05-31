package com.gogidix.shared.warehousing.spaceallocation.interfaces.rest;

import com.gogidix.shared.warehousing.spaceallocation.application.dto.*;
import com.gogidix.shared.warehousing.spaceallocation.application.service.SpaceAllocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Space Allocation REST Controller
 */
@RestController
@RequestMapping("/space-allocation")
@RequiredArgsConstructor
@Tag(name = "Space Allocation", description = "Dynamic space allocation and optimization APIs")
public class SpaceAllocationController {

    private final SpaceAllocationService allocationService;

    @PostMapping("/allocate")
    @Operation(summary = "Allocate space", description = "Allocate space for items in warehouse")
    public ResponseEntity<AllocationResultDTO> allocateSpace(
            @Valid @RequestBody AllocateSpaceCommand command) {
        AllocationResultDTO result = allocationService.allocateSpace(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/release")
    @Operation(summary = "Release space", description = "Release allocated space")
    public ResponseEntity<Void> releaseSpace(
            @Parameter(description = "Allocation ID") @RequestParam String allocationId,
            @Valid @RequestBody ReleaseSpaceCommand command) {
        allocationService.releaseSpace(allocationId, command);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/allocations/{allocationId}")
    @Operation(summary = "Get allocation", description = "Get space allocation by ID")
    public ResponseEntity<SpaceAllocationDTO> getAllocation(
            @Parameter(description = "Allocation ID") @PathVariable String allocationId) {
        SpaceAllocationDTO allocation = allocationService.getAllocation(allocationId);
        return ResponseEntity.ok(allocation);
    }

    @GetMapping("/warehouses/{warehouseId}/zones/{zoneId}/allocations")
    @Operation(summary = "Get zone allocations", description = "Get allocations for a specific zone")
    public ResponseEntity<List<SpaceAllocationDTO>> getZoneAllocations(
            @Parameter(description = "Warehouse ID") @PathVariable String warehouseId,
            @Parameter(description = "Zone ID") @PathVariable String zoneId) {
        List<SpaceAllocationDTO> allocations = allocationService.getZoneAllocations(warehouseId, zoneId);
        return ResponseEntity.ok(allocations);
    }

    @GetMapping("/warehouses/{warehouseId}/zones/capacity")
    @Operation(summary = "Get zone capacities", description = "Get capacity information for all zones")
    public ResponseEntity<List<ZoneCapacityDTO>> getZoneCapacities(
            @Parameter(description = "Warehouse ID") @PathVariable String warehouseId) {
        List<ZoneCapacityDTO> capacities = allocationService.getZoneCapacities(warehouseId);
        return ResponseEntity.ok(capacities);
    }

    @PostMapping("/warehouses/{warehouseId}/zones/{zoneId}/optimize")
    @Operation(summary = "Optimize allocation", description = "Optimize space allocation in zone")
    public ResponseEntity<SpaceAllocationService.OptimizationResultDTO> optimizeAllocation(
            @Parameter(description = "Warehouse ID") @PathVariable String warehouseId,
            @Parameter(description = "Zone ID") @PathVariable String zoneId) {
        SpaceAllocationService.OptimizationResultDTO result =
            allocationService.optimizeAllocation(warehouseId, zoneId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/zones/capacity")
    @Operation(summary = "Create zone capacity", description = "Create zone capacity record")
    public ResponseEntity<ZoneCapacityDTO> createZoneCapacity(
            @Valid @RequestBody SpaceAllocationService.CreateZoneCapacityCommand command) {
        ZoneCapacityDTO capacity = allocationService.createZoneCapacity(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(capacity);
    }
}
