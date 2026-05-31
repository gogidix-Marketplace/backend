package com.gogidix.shared.warehousing.storage.self.application.service;

import com.gogidix.shared.warehousing.storage.self.domain.entity.SelfStorageUnit;
import com.gogidix.shared.warehousing.storage.self.domain.repository.SelfStorageUnitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Application Service for Self Storage Units
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SelfStorageService {

    private final SelfStorageUnitRepository storageUnitRepository;

    public SelfStorageUnit createUnit(SelfStorageUnit unit) {
        log.info("Creating self storage unit: {}", unit.getUnitNumber());

        if (storageUnitRepository.existsByTenantIdAndUnitNumber(unit.getTenantId(), unit.getUnitNumber())) {
            throw new IllegalArgumentException("Storage unit number already exists: " + unit.getUnitNumber());
        }

        unit.setId(UUID.randomUUID().toString());
        unit.setStatus(SelfStorageUnit.UnitStatus.AVAILABLE);
        unit.setCreatedAt(LocalDateTime.now());
        unit.setUpdatedAt(LocalDateTime.now());

        return storageUnitRepository.save(unit);
    }

    public List<SelfStorageUnit> getAvailableUnits(String tenantId, String facilityId) {
        return storageUnitRepository.findAvailableUnits(tenantId, facilityId);
    }

    public List<SelfStorageUnit> getUnitsByType(String tenantId, String facilityId, String unitType) {
        return storageUnitRepository.findByTenantIdAndFacilityIdAndUnitType(
                tenantId, facilityId, SelfStorageUnit.UnitType.valueOf(unitType));
    }

    public List<SelfStorageUnit> getClimateControlledUnits(String tenantId, String facilityId) {
        return storageUnitRepository.findByTenantIdAndFacilityIdAndClimateControlledTrue(
                tenantId, facilityId);
    }

    public SelfStorageUnit rentUnit(String unitId, String rentalId, String tenantId, String tenantName, LocalDateTime startDate) {
        SelfStorageUnit unit = storageUnitRepository.findById(unitId)
                .orElseThrow(() -> new IllegalArgumentException("Storage unit not found: " + unitId));

        if (!unit.isAvailable()) {
            throw new IllegalStateException("Storage unit is not available: " + unitId);
        }

        unit.rentOut(rentalId, tenantId, tenantName, startDate);
        return storageUnitRepository.save(unit);
    }

    public SelfStorageUnit vacateUnit(String unitId) {
        SelfStorageUnit unit = storageUnitRepository.findById(unitId)
                .orElseThrow(() -> new IllegalArgumentException("Storage unit not found: " + unitId));

        unit.vacate();
        return storageUnitRepository.save(unit);
    }

    public SelfStorageUnit reserveUnit(String unitId, String tenantId) {
        SelfStorageUnit unit = storageUnitRepository.findById(unitId)
                .orElseThrow(() -> new IllegalArgumentException("Storage unit not found: " + unitId));

        unit.reserve(tenantId);
        return storageUnitRepository.save(unit);
    }
}
