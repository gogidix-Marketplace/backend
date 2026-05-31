package com.gogidix.shared.warehouse.inventory.location.application.service;

import com.gogidix.shared.warehouse.inventory.location.application.command.CreateLocationCommand;
import com.gogidix.shared.warehouse.inventory.location.application.command.UpdateLocationCommand;
import com.gogidix.shared.warehouse.inventory.location.application.dto.LocationDTO;
import com.gogidix.shared.warehouse.inventory.location.application.mapper.LocationDtoMapper;
import com.gogidix.shared.warehouse.inventory.location.domain.entity.StorageLocation;
import com.gogidix.shared.warehouse.inventory.location.domain.events.LocationCreatedEvent;
import com.gogidix.shared.warehouse.inventory.location.domain.events.LocationUpdatedEvent;
import com.gogidix.shared.warehouse.inventory.location.domain.repository.StorageLocationRepository;
import com.gogidix.shared.warehouse.inventory.location.infrastructure.messaging.LocationEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Application Service for Storage Location Operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LocationApplicationService {

    private final StorageLocationRepository locationRepository;
    private final LocationDtoMapper dtoMapper;
    private final LocationEventPublisher eventPublisher;

    @Transactional
    public LocationDTO createLocation(CreateLocationCommand command) {
        log.info("Creating storage location: {} for warehouse: {}",
                command.getLocationCode(), command.getWarehouseId());

        locationRepository.findByTenantIdAndLocationCode(
                command.getTenantId(), command.getLocationCode())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException(
                            "Location already exists: " + command.getLocationCode());
                });

        StorageLocation location = dtoMapper.toEntity(command);
        location = locationRepository.save(location);

        LocationCreatedEvent event = LocationCreatedEvent.builder()
                .tenantId(location.getTenantId())
                .locationId(location.getLocationId())
                .warehouseId(location.getWarehouseId())
                .locationCode(location.getLocationCode())
                .locationType(location.getLocationType())
                .build();
        eventPublisher.publishLocationCreated(event);

        return dtoMapper.toDTO(location);
    }

    @Transactional
    public LocationDTO updateLocation(UpdateLocationCommand command) {
        log.info("Updating storage location: {}", command.getLocationId());

        StorageLocation location = locationRepository.findByTenantIdAndLocationId(
                command.getTenantId(), command.getLocationId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Location not found: " + command.getLocationId()));

        dtoMapper.updateEntity(location, command);
        location = locationRepository.save(location);

        LocationUpdatedEvent event = LocationUpdatedEvent.builder()
                .tenantId(location.getTenantId())
                .locationId(location.getLocationId())
                .warehouseId(location.getWarehouseId())
                .build();
        eventPublisher.publishLocationUpdated(event);

        return dtoMapper.toDTO(location);
    }

    public Optional<LocationDTO> getLocation(String tenantId, String locationId) {
        return locationRepository.findByTenantIdAndLocationId(tenantId, locationId)
                .map(dtoMapper::toDTO);
    }

    public List<LocationDTO> getWarehouseLocations(String tenantId, String warehouseId) {
        return locationRepository.findByTenantIdAndWarehouseId(tenantId, warehouseId)
                .stream()
                .map(dtoMapper::toDTO)
                .toList();
    }

    public List<LocationDTO> getLocationsByType(String tenantId, String locationType) {
        return locationRepository.findByTenantIdAndLocationType(tenantId, locationType)
                .stream()
                .map(dtoMapper::toDTO)
                .toList();
    }

    public List<LocationDTO> getAvailableLocations(String tenantId, String warehouseId) {
        return locationRepository.findByTenantIdAndWarehouseIdAndOccupiedLessThanCapacity(
                tenantId, warehouseId)
                .stream()
                .map(dtoMapper::toDTO)
                .toList();
    }
}
