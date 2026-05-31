package com.gogidix.shared.warehousing.storage.self.domain.repository;

import com.gogidix.shared.warehousing.storage.self.domain.entity.SelfStorageUnit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Self Storage Unit entity
 */
@Repository
public interface SelfStorageUnitRepository extends MongoRepository<SelfStorageUnit, String> {

    List<SelfStorageUnit> findByTenantId(String tenantId);

    List<SelfStorageUnit> findByTenantIdAndFacilityId(String tenantId, String facilityId);

    Optional<SelfStorageUnit> findByTenantIdAndUnitNumber(String tenantId, String unitNumber);

    List<SelfStorageUnit> findByTenantIdAndFacilityIdAndStatus(
            String tenantId, String facilityId, SelfStorageUnit.UnitStatus status);

    List<SelfStorageUnit> findByTenantIdAndFacilityIdAndUnitType(
            String tenantId, String facilityId, SelfStorageUnit.UnitType unitType);

    List<SelfStorageUnit> findByTenantIdAndFacilityIdAndClimateControlledTrue(
            String tenantId, String facilityId);

    @Query("{ 'tenantId': ?0, 'facilityId': ?1, 'pricingInfo.monthlyPrice': { $lte: ?2 } }")
    List<SelfStorageUnit> findByTenantIdAndFacilityIdAndMaxPrice(
            String tenantId, String facilityId, Double maxPrice);

    @Query("{ 'tenantId': ?0, 'facilityId': ?1, 'area': { $gte: ?2 } }")
    List<SelfStorageUnit> findByTenantIdAndFacilityIdAndMinimumArea(
            String tenantId, String facilityId, Double minArea);

    @Query("{ 'tenantId': ?0, 'facilityId': ?1, 'status': 'AVAILABLE' }")
    List<SelfStorageUnit> findAvailableUnits(String tenantId, String facilityId);

    boolean existsByTenantIdAndUnitNumber(String tenantId, String unitNumber);
}
