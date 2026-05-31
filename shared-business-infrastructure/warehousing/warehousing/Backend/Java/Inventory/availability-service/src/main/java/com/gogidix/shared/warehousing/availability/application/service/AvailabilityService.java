package com.gogidix.shared.warehousing.availability.application.service;

import com.gogidix.shared.warehousing.availability.application.command.*;
import com.gogidix.shared.warehousing.availability.application.dto.*;
import com.gogidix.shared.warehousing.availability.application.mapper.AvailabilityMapper;
import com.gogidix.shared.warehousing.availability.domain.entity.AvailabilitySlot;
import com.gogidix.shared.warehousing.availability.domain.entity.CapacityPool;
import com.gogidix.shared.warehousing.availability.domain.entity.StorageAvailability;
import com.gogidix.shared.warehousing.availability.domain.exception.AvailabilityNotFoundException;
import com.gogidix.shared.warehousing.availability.domain.exception.InsufficientCapacityException;
import com.gogidix.shared.warehousing.availability.domain.repository.AvailabilitySlotRepository;
import com.gogidix.shared.warehousing.availability.domain.repository.CapacityPoolRepository;
import com.gogidix.shared.warehousing.availability.domain.repository.StorageAvailabilityRepository;
import com.gogidix.shared.warehousing.availability.infrastructure.messaging.AvailabilityEventPublisher;
import com.gogidix.shared.warehousing.availability.infrastructure.security.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Availability Application Service
 *
 * Handles storage availability tracking and capacity management
 * Multi-tenant with MongoDB support
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AvailabilityService {

    private final StorageAvailabilityRepository storageAvailabilityRepository;
    private final AvailabilitySlotRepository availabilitySlotRepository;
    private final CapacityPoolRepository capacityPoolRepository;
    private final AvailabilityMapper availabilityMapper;
    private final AvailabilityEventPublisher eventPublisher;

    private static final int DEFAULT_RESERVATION_MINUTES = 30;

    /**
     * Check availability for storage
     */
    @Transactional(readOnly = true)
    public AvailabilityCheckResultDTO checkAvailability(CheckAvailabilityCommand command) {
        log.info("Checking availability for warehouse: {}, quantity: {}", command.getWarehouseId(), command.getQuantity());

        String tenantId = TenantContext.getCurrentTenantId();

        // Check zone availability if zone specified
        if (command.getZoneId() != null) {
            Optional<StorageAvailability> zoneAvailability = storageAvailabilityRepository
                .findByTenantIdAndWarehouseIdAndZoneId(tenantId, command.getWarehouseId(), command.getZoneId());

            if (zoneAvailability.isPresent()) {
                StorageAvailability availability = zoneAvailability.get();
                boolean available = availability.getAvailableCapacity() >= command.getQuantity();

                return AvailabilityCheckResultDTO.builder()
                    .available(available)
                    .warehouseId(command.getWarehouseId())
                    .zoneId(command.getZoneId())
                    .requestedQuantity(command.getQuantity())
                    .availableQuantity(availability.getAvailableCapacity())
                    .message(available ? "Capacity available" : "Insufficient capacity in zone")
                    .details(buildAvailabilityDetails(availability))
                    .build();
            }
        }

        // Check pool availability if pool specified
        if (command.getPoolId() != null) {
            Optional<CapacityPool> pool = capacityPoolRepository.findById(command.getPoolId())
                .filter(p -> p.getTenantId().equals(tenantId));

            if (pool.isPresent()) {
                CapacityPool capacityPool = pool.get();
                boolean available = capacityPool.getAvailableCapacity() >= command.getQuantity();

                return AvailabilityCheckResultDTO.builder()
                    .available(available)
                    .warehouseId(command.getWarehouseId())
                    .poolId(command.getPoolId())
                    .requestedQuantity(command.getQuantity())
                    .availableQuantity(capacityPool.getAvailableCapacity())
                    .message(available ? "Pool capacity available" : "Insufficient capacity in pool")
                    .details(buildPoolDetails(capacityPool))
                    .build();
            }
        }

        // Check overall warehouse availability
        List<StorageAvailability> warehouseAvailability = storageAvailabilityRepository
            .findAvailableZones(tenantId, command.getWarehouseId());

        int totalAvailable = warehouseAvailability.stream()
            .mapToInt(StorageAvailability::getAvailableCapacity)
            .sum();

        boolean available = totalAvailable >= command.getQuantity();

        return AvailabilityCheckResultDTO.builder()
            .available(available)
            .warehouseId(command.getWarehouseId())
            .requestedQuantity(command.getQuantity())
            .availableQuantity(totalAvailable)
            .message(available ? "Warehouse capacity available" : "Insufficient warehouse capacity")
            .details(buildWarehouseDetails(warehouseAvailability))
            .build();
    }

    /**
     * Reserve storage capacity
     */
    public ReservationResultDTO reserveCapacity(ReserveCapacityCommand command) {
        log.info("Reserving capacity for warehouse: {}, quantity: {}", command.getWarehouseId(), command.getQuantity());

        String tenantId = TenantContext.getCurrentTenantId();
        String reservationId = command.getReservationId() != null ? command.getReservationId() : generateReservationId();

        // Try to reserve from pool if specified
        if (command.getPoolId() != null) {
            Optional<CapacityPool> poolOpt = capacityPoolRepository.findById(command.getPoolId())
                .filter(p -> p.getTenantId().equals(tenantId));

            if (poolOpt.isPresent()) {
                CapacityPool pool = poolOpt.get();
                if (pool.reserveCapacity(command.getQuantity())) {
                    capacityPoolRepository.save(pool);
                    eventPublisher.publishCapacityReserved(tenantId, command.getWarehouseId(),
                        command.getPoolId(), reservationId, command.getQuantity());

                    return ReservationResultDTO.builder()
                        .success(true)
                        .reservationId(reservationId)
                        .warehouseId(command.getWarehouseId())
                        .poolId(command.getPoolId())
                        .reservedQuantity(command.getQuantity())
                        .message("Capacity reserved successfully")
                        .reservedAt(LocalDateTime.now())
                        .expiresAt(LocalDateTime.now().plusMinutes(DEFAULT_RESERVATION_MINUTES))
                        .build();
                }
                throw new InsufficientCapacityException(command.getWarehouseId(), command.getQuantity(),
                    pool.getAvailableCapacity());
            }
        }

        // Try to reserve from zone if specified
        if (command.getZoneId() != null) {
            Optional<StorageAvailability> availabilityOpt = storageAvailabilityRepository
                .findByTenantIdAndWarehouseIdAndZoneId(tenantId, command.getWarehouseId(), command.getZoneId());

            if (availabilityOpt.isPresent()) {
                StorageAvailability availability = availabilityOpt.get();
                if (availability.getAvailableCapacity() >= command.getQuantity()) {
                    availability.setReservedCapacity(availability.getReservedCapacity() + command.getQuantity());
                    availability.calculateUtilization();
                    storageAvailabilityRepository.save(availability);

                    eventPublisher.publishCapacityReserved(tenantId, command.getWarehouseId(),
                        command.getZoneId(), reservationId, command.getQuantity());

                    return ReservationResultDTO.builder()
                        .success(true)
                        .reservationId(reservationId)
                        .warehouseId(command.getWarehouseId())
                        .zoneId(command.getZoneId())
                        .reservedQuantity(command.getQuantity())
                        .message("Zone capacity reserved successfully")
                        .reservedAt(LocalDateTime.now())
                        .expiresAt(LocalDateTime.now().plusMinutes(DEFAULT_RESERVATION_MINUTES))
                        .build();
                }
                throw new InsufficientCapacityException(command.getWarehouseId(), command.getQuantity(),
                    availability.getAvailableCapacity());
            }
        }

        // Find best available zone
        List<StorageAvailability> availableZones = storageAvailabilityRepository
            .findAvailableZones(tenantId, command.getWarehouseId());

        for (StorageAvailability availability : availableZones) {
            if (availability.getAvailableCapacity() >= command.getQuantity()) {
                availability.setReservedCapacity(availability.getReservedCapacity() + command.getQuantity());
                availability.calculateUtilization();
                storageAvailabilityRepository.save(availability);

                eventPublisher.publishCapacityReserved(tenantId, command.getWarehouseId(),
                    availability.getZoneId(), reservationId, command.getQuantity());

                return ReservationResultDTO.builder()
                    .success(true)
                    .reservationId(reservationId)
                    .warehouseId(command.getWarehouseId())
                    .zoneId(availability.getZoneId())
                    .reservedQuantity(command.getQuantity())
                    .message("Capacity reserved successfully in zone: " + availability.getZoneName())
                    .reservedAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusMinutes(DEFAULT_RESERVATION_MINUTES))
                    .build();
            }
        }

        throw new InsufficientCapacityException(command.getWarehouseId(), command.getQuantity(), 0);
    }

    /**
     * Release reserved capacity
     */
    public void releaseCapacity(ReleaseCapacityCommand command) {
        log.info("Releasing capacity for reservation: {}", command.getReservationId());

        String tenantId = TenantContext.getCurrentTenantId();

        // Release from pool if specified
        if (command.getPoolId() != null) {
            Optional<CapacityPool> poolOpt = capacityPoolRepository.findById(command.getPoolId())
                .filter(p -> p.getTenantId().equals(tenantId));

            if (poolOpt.isPresent()) {
                CapacityPool pool = poolOpt.get();
                pool.releaseCapacity(command.getQuantity());
                capacityPoolRepository.save(pool);

                eventPublisher.publishCapacityReleased(tenantId, command.getWarehouseId(),
                    command.getPoolId(), command.getReservationId(), command.getQuantity());
                return;
            }
        }

        // Release from zone if specified
        if (command.getZoneId() != null) {
            Optional<StorageAvailability> availabilityOpt = storageAvailabilityRepository
                .findByTenantIdAndWarehouseIdAndZoneId(tenantId, command.getWarehouseId(), command.getZoneId());

            if (availabilityOpt.isPresent()) {
                StorageAvailability availability = availabilityOpt.get();
                availability.setReservedCapacity(Math.max(0,
                    availability.getReservedCapacity() - command.getQuantity()));
                availability.calculateUtilization();
                storageAvailabilityRepository.save(availability);

                eventPublisher.publishCapacityReleased(tenantId, command.getWarehouseId(),
                    command.getZoneId(), command.getReservationId(), command.getQuantity());
                return;
            }
        }

        log.warn("No resource found to release capacity for reservation: {}", command.getReservationId());
    }

    /**
     * Get availability for warehouse
     */
    @Transactional(readOnly = true)
    public List<StorageAvailabilityDTO> getWarehouseAvailability(String warehouseId) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<StorageAvailability> availability = storageAvailabilityRepository
            .findByTenantIdAndWarehouseId(tenantId, warehouseId);
        return availabilityMapper.toStorageAvailabilityDTOList(availability);
    }

    /**
     * Get all availability for tenant
     */
    @Transactional(readOnly = true)
    public List<StorageAvailabilityDTO> getAllAvailability() {
        String tenantId = TenantContext.getCurrentTenantId();
        List<StorageAvailability> availability = storageAvailabilityRepository.findByTenantId(tenantId);
        return availabilityMapper.toStorageAvailabilityDTOList(availability);
    }

    /**
     * Get capacity pools for warehouse
     */
    @Transactional(readOnly = true)
    public List<CapacityPoolDTO> getCapacityPools(String warehouseId) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<CapacityPool> pools = capacityPoolRepository.findByTenantIdAndWarehouseId(tenantId, warehouseId);
        return availabilityMapper.toCapacityPoolDTOList(pools);
    }

    /**
     * Create capacity pool
     */
    public CapacityPoolDTO createCapacityPool(CreateCapacityPoolCommand command) {
        log.info("Creating capacity pool: {} for warehouse: {}", command.getPoolName(), command.getWarehouseId());

        String tenantId = TenantContext.getCurrentTenantId();

        CapacityPool pool = availabilityMapper.toEntity(command);
        pool.setTenantId(tenantId);
        pool.setAllocatedCapacity(0);
        pool.setReservedCapacity(0);
        pool.setAvailableCapacity(command.getTotalCapacity());

        CapacityPool savedPool = capacityPoolRepository.save(pool);

        log.info("Capacity pool created with ID: {}", savedPool.getId());
        return availabilityMapper.toCapacityPoolDTO(savedPool);
    }

    /**
     * Update storage availability
     */
    public StorageAvailabilityDTO updateAvailability(String availabilityId, Integer usedCapacity,
                                                     Integer reservedCapacity) {
        log.info("Updating availability: {}", availabilityId);

        String tenantId = TenantContext.getCurrentTenantId();

        StorageAvailability availability = storageAvailabilityRepository.findById(availabilityId)
            .filter(a -> a.getTenantId().equals(tenantId))
            .orElseThrow(() -> new AvailabilityNotFoundException(availabilityId, tenantId));

        availability.setUsedCapacity(usedCapacity);
        availability.setReservedCapacity(reservedCapacity);
        availability.calculateUtilization();
        availability.setLastCalculatedAt(LocalDateTime.now());

        StorageAvailability saved = storageAvailabilityRepository.save(availability);

        return availabilityMapper.toStorageAvailabilityDTO(saved);
    }

    /**
     * Get available slots for time range
     */
    @Transactional(readOnly = true)
    public List<AvailabilitySlotDTO> getAvailableSlots(String warehouseId, AvailabilitySlot.SlotType slotType,
                                                       LocalDateTime startTime, LocalDateTime endTime) {
        String tenantId = TenantContext.getCurrentTenantId();
        List<AvailabilitySlot> slots = availabilitySlotRepository.findAvailableSlots(
            tenantId, warehouseId, slotType, startTime, endTime);
        return availabilityMapper.toAvailabilitySlotDTOList(slots);
    }

    /**
     * Clean up expired reservations
     */
    @Transactional
    public void cleanupExpiredReservations() {
        log.info("Cleaning up expired reservations");
        String tenantId = TenantContext.getCurrentTenantId();
        LocalDateTime now = LocalDateTime.now();

        List<AvailabilitySlot> expiredSlots = availabilitySlotRepository.findExpiredSlots(tenantId, now);
        for (AvailabilitySlot slot : expiredSlots) {
            slot.setStatus(AvailabilitySlot.SlotStatus.EXPIRED);
            slot.setReserved(0);
            slot.updateAvailability();
        }
        availabilitySlotRepository.saveAll(expiredSlots);

        log.info("Cleaned up {} expired reservations", expiredSlots.size());
    }

    private Map<String, Object> buildAvailabilityDetails(StorageAvailability availability) {
        Map<String, Object> details = new HashMap<>();
        details.put("totalCapacity", availability.getTotalCapacity());
        details.put("usedCapacity", availability.getUsedCapacity());
        details.put("reservedCapacity", availability.getReservedCapacity());
        details.put("utilizationPercentage", availability.getUtilizationPercentage());
        details.put("status", availability.getStatus());
        return details;
    }

    private Map<String, Object> buildPoolDetails(CapacityPool pool) {
        Map<String, Object> details = new HashMap<>();
        details.put("totalCapacity", pool.getTotalCapacity());
        details.put("allocatedCapacity", pool.getAllocatedCapacity());
        details.put("reservedCapacity", pool.getReservedCapacity());
        details.put("utilizationPercentage", pool.calculateUtilization());
        details.put("overflowEnabled", pool.getOverflowEnabled());
        return details;
    }

    private Map<String, Object> buildWarehouseDetails(List<StorageAvailability> availabilityList) {
        Map<String, Object> details = new HashMap<>();
        details.put("zones", availabilityList.stream()
            .map(a -> Map.of(
                "zoneId", a.getZoneId(),
                "zoneName", a.getZoneName(),
                "availableCapacity", a.getAvailableCapacity(),
                "status", a.getStatus()
            ))
            .collect(Collectors.toList()));
        return details;
    }

    private String generateReservationId() {
        return "RSV-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
