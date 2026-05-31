package com.gogidix.shared.warehousing.storage.application.service;

import com.gogidix.shared.warehousing.storage.application.dto.*;
import com.gogidix.shared.warehousing.storage.domain.entity.StorageSpace;
import com.gogidix.shared.warehousing.storage.domain.repository.StorageSpaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for managing storage spaces
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StorageSpaceService {

    private final StorageSpaceRepository repository;

    /**
     * Create a new storage space
     */
    @Transactional
    public StorageSpaceResponse createSpace(CreateSpaceRequest request) {
        log.info("Creating storage space for tenant: {}, code: {}", request.getTenantId(), request.getSpaceCode());

        // Check if space code already exists
        if (repository.existsByTenantIdAndSpaceCode(request.getTenantId(), request.getSpaceCode())) {
            throw new IllegalArgumentException("Space code already exists: " + request.getSpaceCode());
        }

        // Calculate total capacity
        double totalCapacity = request.getLengthMeters() * request.getWidthMeters() * request.getHeightMeters();

        StorageSpace space = StorageSpace.builder()
            .tenantId(request.getTenantId())
            .spaceCode(request.getSpaceCode())
            .spaceType(request.getSpaceType())
            .lengthMeters(request.getLengthMeters())
            .widthMeters(request.getWidthMeters())
            .heightMeters(request.getHeightMeters())
            .totalCapacityCubicMeters(totalCapacity)
            .availableCapacityCubicMeters(totalCapacity)
            .availableSlots(1)
            .basePricePerDay(request.getBasePricePerDay())
            .currency(request.getCurrency())
            .facilityZone(request.getFacilityZone())
            .shelfLevel(request.getShelfLevel())
            .binNumber(request.getBinNumber())
            .status("AVAILABLE")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        StorageSpace saved = repository.save(space);
        log.info("Storage space created with ID: {}", saved.getId());

        return StorageSpaceResponse.fromEntity(saved);
    }

    /**
     * Get all storage spaces for tenant
     */
    public List<StorageSpaceResponse> getSpaces(String tenantId) {
        log.debug("Fetching spaces for tenant: {}", tenantId);
        return repository.findByTenantId(tenantId).stream()
            .map(StorageSpaceResponse::fromEntity)
            .collect(Collectors.toList());
    }

    /**
     * Get storage space by ID
     */
    public StorageSpaceResponse getSpaceById(String id) {
        StorageSpace space = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Storage space not found: " + id));
        return StorageSpaceResponse.fromEntity(space);
    }

    /**
     * Get available spaces by type
     */
    public List<StorageSpaceResponse> getAvailableSpaces(String tenantId, String spaceType) {
        log.debug("Fetching available spaces for tenant: {}, type: {}", tenantId, spaceType);
        return repository.findByTenantIdAndStatusAndSpaceType(tenantId, "AVAILABLE", spaceType).stream()
            .map(StorageSpaceResponse::fromEntity)
            .sorted(Comparator.comparing(StorageSpaceResponse::getAvailableCapacityCubicMeters).reversed())
            .collect(Collectors.toList());
    }

    /**
     * Update storage space
     */
    @Transactional
    public StorageSpaceResponse updateSpace(String id, CreateSpaceRequest request) {
        log.info("Updating storage space: {}", id);

        StorageSpace space = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Storage space not found: " + id));

        // Update fields
        space.setSpaceType(request.getSpaceType());
        space.setLengthMeters(request.getLengthMeters());
        space.setWidthMeters(request.getWidthMeters());
        space.setHeightMeters(request.getHeightMeters());

        double totalCapacity = request.getLengthMeters() * request.getWidthMeters() * request.getHeightMeters();
        space.setTotalCapacityCubicMeters(totalCapacity);
        space.setBasePricePerDay(request.getBasePricePerDay());
        space.setCurrency(request.getCurrency());
        space.setFacilityZone(request.getFacilityZone());
        space.setShelfLevel(request.getShelfLevel());
        space.setBinNumber(request.getBinNumber());
        space.setUpdatedAt(LocalDateTime.now());

        StorageSpace updated = repository.save(space);
        log.info("Storage space updated: {}", id);

        return StorageSpaceResponse.fromEntity(updated);
    }

    /**
     * Allocate space to customer
     */
    @Transactional
    public StorageSpaceResponse allocateSpace(String id, AllocateSpaceRequest request) {
        log.info("Allocating space {} to customer: {}", id, request.getCustomerId());

        StorageSpace space = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Storage space not found: " + id));

        if (!space.isAvailable()) {
            throw new IllegalStateException("Space is not available for allocation");
        }

        double required = request.getRequiredCapacityCubicMeters();
        if (space.getAvailableCapacityCubicMeters() < required) {
            throw new IllegalStateException("Insufficient capacity. Available: " + space.getAvailableCapacityCubicMeters() + ", Required: " + required);
        }

        // Allocate capacity
        space.setAvailableCapacityCubicMeters(space.getAvailableCapacityCubicMeters() - required);

        // Update status if fully occupied
        if (space.getAvailableCapacityCubicMeters() == 0) {
            space.setStatus("OCCUPIED");
        }

        space.setUpdatedAt(LocalDateTime.now());

        StorageSpace updated = repository.save(space);
        log.info("Space allocated. Remaining capacity: {}", updated.getAvailableCapacityCubicMeters());

        return StorageSpaceResponse.fromEntity(updated);
    }

    /**
     * Optimize space utilization
     */
    @Transactional
    public void optimizeSpace(String id) {
        log.info("Optimizing space: {}", id);

        StorageSpace space = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Storage space not found: " + id));

        // Optimization algorithm: consolidate fragmented space
        if (space.getAvailableCapacityCubicMeters() > 0 && space.getAvailableCapacityCubicMeters() < space.getTotalCapacityCubicMeters()) {
            // If space is partially used, optimize slots
            space.setAvailableSlots(1);
            space.setUpdatedAt(LocalDateTime.now());
            repository.save(space);
            log.info("Space optimized: {}", id);
        }
    }

    /**
     * Get utilization report
     */
    public UtilizationReport getUtilizationReport(String tenantId) {
        log.debug("Generating utilization report for tenant: {}", tenantId);

        List<StorageSpace> spaces = repository.findByTenantId(tenantId);

        UtilizationReport report = new UtilizationReport();
        report.setTenantId(tenantId);
        report.setTotalSpaces(spaces.size());
        report.setAvailableSpaces((int) spaces.stream().filter(StorageSpace::isAvailable).count());
        report.setOccupiedSpaces((int) spaces.stream().filter(s -> "OCCUPIED".equals(s.getStatus())).count());

        double totalCapacity = spaces.stream()
            .mapToDouble(s -> s.getTotalCapacityCubicMeters() != null ? s.getTotalCapacityCubicMeters() : 0.0)
            .sum();
        report.setTotalCapacityCubicMeters(totalCapacity);

        double usedCapacity = spaces.stream()
            .mapToDouble(s -> {
                double available = s.getAvailableCapacityCubicMeters() != null ? s.getAvailableCapacityCubicMeters() : 0.0;
                double total = s.getTotalCapacityCubicMeters() != null ? s.getTotalCapacityCubicMeters() : 0.0;
                return total - available;
            })
            .sum();
        report.setUsedCapacityCubicMeters(usedCapacity);

        double avgUtilization = totalCapacity > 0 ? (usedCapacity / totalCapacity) * 100 : 0.0;
        report.setAverageUtilizationPercentage(avgUtilization);

        // Group by space type
        Map<String, UtilizationReport.TypeStatistics> statsByType = spaces.stream()
            .collect(Collectors.groupingBy(
                StorageSpace::getSpaceType,
                Collectors.collectingAndThen(
                    Collectors.toList(),
                    typeSpaces -> {
                        UtilizationReport.TypeStatistics stats = new UtilizationReport.TypeStatistics();
                        stats.setTotalCount(typeSpaces.size());
                        stats.setAvailableCount((int) typeSpaces.stream().filter(StorageSpace::isAvailable).count());

                        double typeTotal = typeSpaces.stream()
                            .mapToDouble(s -> s.getTotalCapacityCubicMeters() != null ? s.getTotalCapacityCubicMeters() : 0.0)
                            .sum();
                        stats.setTotalCapacity(typeTotal);

                        double typeUsed = typeSpaces.stream()
                            .mapToDouble(s -> {
                                double available = s.getAvailableCapacityCubicMeters() != null ? s.getAvailableCapacityCubicMeters() : 0.0;
                                double total = s.getTotalCapacityCubicMeters() != null ? s.getTotalCapacityCubicMeters() : 0.0;
                                return total - available;
                            })
                            .sum();
                        stats.setUsedCapacity(typeUsed);

                        stats.setUtilizationPercentage(typeTotal > 0 ? (typeUsed / typeTotal) * 100 : 0.0);
                        return stats;
                    }
                )
            ));

        report.setStatisticsByType(statsByType);

        return report;
    }
}
