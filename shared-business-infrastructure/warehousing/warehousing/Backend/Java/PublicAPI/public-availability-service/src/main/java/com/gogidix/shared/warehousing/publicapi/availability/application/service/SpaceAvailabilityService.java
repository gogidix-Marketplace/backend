package com.gogidix.shared.warehousing.publicapi.availability.application.service;

import com.gogidix.shared.warehousing.publicapi.availability.domain.entity.SpaceAvailability;
import com.gogidix.shared.warehousing.publicapi.availability.domain.repository.SpaceAvailabilityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Application Service for Public Space Availability
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SpaceAvailabilityService {

    private final SpaceAvailabilityRepository availabilityRepository;

    public SpaceAvailability createAvailability(SpaceAvailability availability) {
        log.info("Creating space availability for warehouse: {}", availability.getWarehouseId());

        availability.setId(UUID.randomUUID().toString());
        availability.setCreatedAt(LocalDateTime.now());
        availability.setUpdatedAt(LocalDateTime.now());
        availability.setLastVerifiedAt(LocalDateTime.now());

        return availabilityRepository.save(availability);
    }

    public List<SpaceAvailability> getAvailableSpaces(String tenantId, LocalDateTime date) {
        return availabilityRepository.findAvailableAtDate(tenantId, date != null ? date : LocalDateTime.now());
    }

    public List<SpaceAvailability> getClimateControlledSpaces(String tenantId) {
        return availabilityRepository.findClimateControlled(tenantId);
    }

    public List<SpaceAvailability> getSpacesByMaxPrice(String tenantId, Double maxPrice) {
        return availabilityRepository.findByMaxPrice(tenantId, maxPrice);
    }

    public List<SpaceAvailability> getSpacesByCity(String tenantId, String city) {
        return availabilityRepository.findByTenantIdAndCity(tenantId, city);
    }

    public List<SpaceAvailability> getSpacesByState(String tenantId, String state) {
        return availabilityRepository.findByTenantIdAndState(tenantId, state);
    }

    public List<SpaceAvailability> getAvailableByWarehouse(String tenantId, String warehouseId) {
        return availabilityRepository.findAvailableByWarehouse(tenantId, warehouseId);
    }

    public SpaceAvailability updateAvailability(String availabilityId, SpaceAvailability.AvailabilityStatus status) {
        SpaceAvailability availability = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new IllegalArgumentException("Availability not found: " + availabilityId));

        availability.setStatus(status);
        availability.setUpdatedAt(LocalDateTime.now());
        availability.setLastVerifiedAt(LocalDateTime.now());

        return availabilityRepository.save(availability);
    }
}
