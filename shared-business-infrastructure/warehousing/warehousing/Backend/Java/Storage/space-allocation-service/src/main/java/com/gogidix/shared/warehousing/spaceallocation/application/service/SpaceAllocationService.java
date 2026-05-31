package com.gogidix.shared.warehousing.spaceallocation.application.service;

import com.gogidix.shared.warehousing.spaceallocation.application.dto.*;
import com.gogidix.shared.warehousing.spaceallocation.domain.entity.*;
import com.gogidix.shared.warehousing.spaceallocation.domain.exception.InsufficientSpaceException;
import com.gogidix.shared.warehousing.spaceallocation.domain.repository.AllocationRuleRepository;
import com.gogidix.shared.warehousing.spaceallocation.domain.repository.SpaceAllocationRepository;
import com.gogidix.shared.warehousing.spaceallocation.domain.repository.ZoneCapacityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Space Allocation Service
 *
 * Handles dynamic space allocation in warehouses
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SpaceAllocationService {

    private final SpaceAllocationRepository allocationRepository;
    private final ZoneCapacityRepository zoneCapacityRepository;
    private final AllocationRuleRepository ruleRepository;

    private static final BigDecimal DEFAULT_VOLUME_THRESHOLD = new BigDecimal("0.1");

    /**
     * Allocate space for item
     */
    public AllocationResultDTO allocateSpace(AllocateSpaceCommand command) {
        log.info("Allocating space for item: {} in warehouse: {}", command.getItemId(), command.getWarehouseId());

        String tenantId = "current-tenant";

        // Find suitable zone
        String targetZoneId = findSuitableZone(tenantId, command.getWarehouseId(), command);

        if (targetZoneId == null) {
            throw new InsufficientSpaceException("No suitable zone found with available capacity");
        }

        // Check zone capacity
        Optional<ZoneCapacity> zoneCapacityOpt = zoneCapacityRepository
            .findByTenantIdAndWarehouseIdAndZoneId(tenantId, command.getWarehouseId(), targetZoneId);

        if (zoneCapacityOpt.isPresent()) {
            ZoneCapacity zoneCapacity = zoneCapacityOpt.get();
            if (!zoneCapacity.hasCapacityFor(command.getVolume(),
                command.getWeight() != null ? command.getWeight() : BigDecimal.ZERO)) {
                throw new InsufficientSpaceException("Insufficient capacity in zone: " + targetZoneId);
            }
        }

        // Generate location
        SpaceAllocation.Location location = generateLocation(targetZoneId);

        // Create allocation
        SpaceAllocation allocation = SpaceAllocation.builder()
            .tenantId(tenantId)
            .warehouseId(command.getWarehouseId())
            .zoneId(targetZoneId)
            .allocationId(generateAllocationId())
            .itemId(command.getItemId())
            .itemType(command.getItemType())
            .sku(command.getSku())
            .status(SpaceAllocation.AllocationStatus.PENDING)
            .spaceType(command.getSpaceType())
            .volume(command.getVolume())
            .weight(command.getWeight())
            .quantity(command.getQuantity())
            .unit(command.getUnit())
            .location(location)
            .allocatedAt(LocalDateTime.now())
            .expiresAt(command.getExpiresAt())
            .priority(command.getPriority() != null ? command.getPriority() : 0)
            .allocationType(command.getAllocationType())
            .referenceId(command.getReferenceId())
            .referenceType(command.getReferenceType())
            .assignedTo(command.getAssignedTo())
            .optimized(false)
            .build();

        allocation.markAllocated();
        SpaceAllocation saved = allocationRepository.save(allocation);

        // Update zone capacity
        zoneCapacityOpt.ifPresent(capacity -> {
            capacity.allocateCapacity(command.getVolume(),
                command.getWeight() != null ? command.getWeight() : BigDecimal.ZERO);
            zoneCapacityRepository.save(capacity);
        });

        log.info("Space allocated with ID: {} in zone: {}", saved.getAllocationId(), targetZoneId);

        return AllocationResultDTO.builder()
            .success(true)
            .allocationId(saved.getAllocationId())
            .warehouseId(saved.getWarehouseId())
            .zoneId(saved.getZoneId())
            .location(saved.getLocation())
            .message("Space allocated successfully")
            .build();
    }

    /**
     * Release space allocation
     */
    public void releaseSpace(String allocationId, ReleaseSpaceCommand command) {
        log.info("Releasing space allocation: {}", allocationId);

        SpaceAllocation allocation = allocationRepository.findById(allocationId)
            .orElseThrow(() -> new IllegalArgumentException("Allocation not found: " + allocationId));

        allocation.release();
        allocationRepository.save(allocation);

        // Update zone capacity
        Optional<ZoneCapacity> zoneCapacityOpt = zoneCapacityRepository
            .findByTenantIdAndWarehouseIdAndZoneId(
                allocation.getTenantId(), allocation.getWarehouseId(), allocation.getZoneId());

        zoneCapacityOpt.ifPresent(capacity -> {
            capacity.releaseCapacity(allocation.getVolume(),
                allocation.getWeight() != null ? allocation.getWeight() : BigDecimal.ZERO);
            zoneCapacityRepository.save(capacity);
        });

        log.info("Space released: {}", allocationId);
    }

    /**
     * Get allocation by ID
     */
    @Transactional(readOnly = true)
    public SpaceAllocationDTO getAllocation(String allocationId) {
        SpaceAllocation allocation = allocationRepository.findById(allocationId)
            .orElseThrow(() -> new IllegalArgumentException("Allocation not found: " + allocationId));
        return toDTO(allocation);
    }

    /**
     * Get allocations by zone
     */
    @Transactional(readOnly = true)
    public List<SpaceAllocationDTO> getZoneAllocations(String warehouseId, String zoneId) {
        String tenantId = "current-tenant";
        return allocationRepository.findByTenantIdAndWarehouseIdAndZoneId(tenantId, warehouseId, zoneId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get zone capacities
     */
    @Transactional(readOnly = true)
    public List<ZoneCapacityDTO> getZoneCapacities(String warehouseId) {
        String tenantId = "current-tenant";
        return zoneCapacityRepository.findByTenantIdAndWarehouseId(tenantId, warehouseId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Optimize allocation in zone
     */
    @Transactional
    public OptimizationResultDTO optimizeAllocation(String warehouseId, String zoneId) {
        log.info("Optimizing allocations in warehouse: {}, zone: {}", warehouseId, zoneId);

        String tenantId = "current-tenant";

        List<SpaceAllocation> allocations = allocationRepository.findActiveAllocationsInZone(
            tenantId, warehouseId, zoneId);

        // Mark allocations as optimized
        allocations.forEach(a -> a.setOptimized(true));
        allocationRepository.saveAll(allocations);

        return OptimizationResultDTO.builder()
            .zoneId(zoneId)
            .allocationsOptimized(allocations.size())
            .spaceSaved(BigDecimal.ZERO)
            .message("Optimization completed successfully")
            .build();
    }

    /**
     * Create zone capacity
     */
    public ZoneCapacityDTO createZoneCapacity(CreateZoneCapacityCommand command) {
        log.info("Creating zone capacity for zone: {}", command.getZoneId());

        String tenantId = "current-tenant";

        ZoneCapacity capacity = ZoneCapacity.builder()
            .tenantId(tenantId)
            .warehouseId(command.getWarehouseId())
            .zoneId(command.getZoneId())
            .zoneName(command.getZoneName())
            .zoneType(command.getZoneType())
            .totalVolume(command.getTotalVolume())
            .usedVolume(BigDecimal.ZERO)
            .availableVolume(command.getTotalVolume())
            .totalPositions(command.getTotalPositions())
            .usedPositions(0)
            .availablePositions(command.getTotalPositions())
            .maxWeight(command.getMaxWeight())
            .usedWeight(BigDecimal.ZERO)
            .availableWeight(command.getMaxWeight())
            .utilizationPercentage(0.0)
            .status(ZoneCapacity.CapacityStatus.AVAILABLE)
            .build();

        ZoneCapacity saved = zoneCapacityRepository.save(capacity);
        return toDTO(saved);
    }

    private String findSuitableZone(String tenantId, String warehouseId, AllocateSpaceCommand command) {
        // If zone specified, check if it has capacity
        if (command.getZoneId() != null) {
            Optional<ZoneCapacity> zoneOpt = zoneCapacityRepository
                .findByTenantIdAndWarehouseIdAndZoneId(tenantId, warehouseId, command.getZoneId());

            if (zoneOpt.isPresent() && zoneOpt.get().hasCapacityFor(command.getVolume(),
                command.getWeight() != null ? command.getWeight() : BigDecimal.ZERO)) {
                return command.getZoneId();
            }
        }

        // Find zone with available capacity
        List<ZoneCapacity> zones = zoneCapacityRepository.findZonesWithCapacity(tenantId, warehouseId);

        for (ZoneCapacity zone : zones) {
            if (zone.hasCapacityFor(command.getVolume(),
                command.getWeight() != null ? command.getWeight() : BigDecimal.ZERO)) {
                return zone.getZoneId();
            }
        }

        return null;
    }

    private SpaceAllocation.Location generateLocation(String zoneId) {
        return SpaceAllocation.Location.builder()
            .zone(zoneId)
            .aisle("A" + (int) (Math.random() * 10 + 1))
            .bay("B" + (int) (Math.random() * 20 + 1))
            .level("L" + (int) (Math.random() * 5 + 1))
            .position("P" + (int) (Math.random() * 4 + 1))
            .barcode(generateBarcode(zoneId))
            .build();
    }

    private String generateAllocationId() {
        return "SA-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String generateBarcode(String zoneId) {
        return "ZONE-" + zoneId + "-" + System.currentTimeMillis();
    }

    private SpaceAllocationDTO toDTO(SpaceAllocation allocation) {
        return SpaceAllocationDTO.builder()
            .id(allocation.getId())
            .tenantId(allocation.getTenantId())
            .warehouseId(allocation.getWarehouseId())
            .zoneId(allocation.getZoneId())
            .allocationId(allocation.getAllocationId())
            .itemId(allocation.getItemId())
            .itemType(allocation.getItemType())
            .sku(allocation.getSku())
            .status(allocation.getStatus())
            .spaceType(allocation.getSpaceType())
            .volume(allocation.getVolume())
            .weight(allocation.getWeight())
            .quantity(allocation.getQuantity())
            .unit(allocation.getUnit())
            .location(allocation.getLocation())
            .assignedTo(allocation.getAssignedTo())
            .allocatedAt(allocation.getAllocatedAt())
            .expiresAt(allocation.getExpiresAt())
            .priority(allocation.getPriority())
            .allocationType(allocation.getAllocationType())
            .referenceId(allocation.getReferenceId())
            .referenceType(allocation.getReferenceType())
            .utilizationPercentage(allocation.getUtilizationPercentage())
            .optimized(allocation.getOptimized())
            .createdAt(allocation.getCreatedAt())
            .updatedAt(allocation.getUpdatedAt())
            .build();
    }

    private ZoneCapacityDTO toDTO(ZoneCapacity capacity) {
        return ZoneCapacityDTO.builder()
            .id(capacity.getId())
            .tenantId(capacity.getTenantId())
            .warehouseId(capacity.getWarehouseId())
            .zoneId(capacity.getZoneId())
            .zoneName(capacity.getZoneName())
            .zoneType(capacity.getZoneType())
            .totalVolume(capacity.getTotalVolume())
            .usedVolume(capacity.getUsedVolume())
            .availableVolume(capacity.getAvailableVolume())
            .totalPositions(capacity.getTotalPositions())
            .usedPositions(capacity.getUsedPositions())
            .availablePositions(capacity.getAvailablePositions())
            .maxWeight(capacity.getMaxWeight())
            .usedWeight(capacity.getUsedWeight())
            .availableWeight(capacity.getAvailableWeight())
            .utilizationPercentage(capacity.getUtilizationPercentage())
            .status(capacity.getStatus())
            .createdAt(capacity.getCreatedAt())
            .updatedAt(capacity.getUpdatedAt())
            .build();
    }

    /**
     * DTO for optimization result
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class OptimizationResultDTO {
        private String zoneId;
        private Integer allocationsOptimized;
        private java.math.BigDecimal spaceSaved;
        private String message;
    }

    /**
     * Command to create zone capacity
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class CreateZoneCapacityCommand {
        private String warehouseId;
        private String zoneId;
        private String zoneName;
        private ZoneCapacity.ZoneType zoneType;
        private java.math.BigDecimal totalVolume;
        private Integer totalPositions;
        private java.math.BigDecimal maxWeight;
    }
}
