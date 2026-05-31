package com.gogidix.shared.warehousing.storage.space.application.service;

import com.gogidix.shared.warehousing.storage.space.domain.entity.StorageSpace;
import com.gogidix.shared.warehousing.storage.space.domain.repository.StorageSpaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Application Service for Storage Space Management
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StorageSpaceService {

    private final StorageSpaceRepository storageSpaceRepository;

    public StorageSpace createSpace(StorageSpace space) {
        log.info("Creating storage space: {}", space.getSpaceCode());

        if (storageSpaceRepository.existsByTenantIdAndSpaceCode(space.getTenantId(), space.getSpaceCode())) {
            throw new IllegalArgumentException("Storage space code already exists: " + space.getSpaceCode());
        }

        space.setId(UUID.randomUUID().toString());
        space.setStatus(StorageSpace.SpaceStatus.AVAILABLE);
        space.setCreatedAt(LocalDateTime.now());
        space.setUpdatedAt(LocalDateTime.now());

        if (space.getOccupancyInfo() == null) {
            space.setOccupancyInfo(StorageSpace.OccupancyInfo.builder()
                    .currentWeight(0.0)
                    .currentVolume(0.0)
                    .currentItemCount(0)
                    .utilizationPercentage(0.0)
                    .build());
        }

        return storageSpaceRepository.save(space);
    }

    public List<StorageSpace> getAvailableSpaces(String tenantId, String warehouseId) {
        return storageSpaceRepository.findAvailableSpaces(tenantId, warehouseId);
    }

    public List<StorageSpace> getSpacesByType(String tenantId, String warehouseId, String spaceType) {
        return storageSpaceRepository.findByTenantIdAndWarehouseIdAndSpaceType(
                tenantId, warehouseId, StorageSpace.SpaceType.valueOf(spaceType));
    }

    public List<StorageSpace> getSpacesByTemperatureZone(String tenantId, String warehouseId, String temperatureZone) {
        return storageSpaceRepository.findByTenantIdAndWarehouseIdAndTemperatureZone(
                tenantId, warehouseId, temperatureZone);
    }

    public StorageSpace occupySpace(String spaceId, String contentId, String contentType) {
        StorageSpace space = storageSpaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("Storage space not found: " + spaceId));

        if (!space.isAvailable()) {
            throw new IllegalStateException("Storage space is not available: " + spaceId);
        }

        space.occupy(contentId, contentType);
        return storageSpaceRepository.save(space);
    }

    public StorageSpace vacateSpace(String spaceId) {
        StorageSpace space = storageSpaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("Storage space not found: " + spaceId));

        space.vacate();
        return storageSpaceRepository.save(space);
    }

    public StorageSpace updateStatus(String spaceId, StorageSpace.SpaceStatus status) {
        StorageSpace space = storageSpaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("Storage space not found: " + spaceId));

        space.setStatus(status);
        space.setUpdatedAt(LocalDateTime.now());
        return storageSpaceRepository.save(space);
    }
}
