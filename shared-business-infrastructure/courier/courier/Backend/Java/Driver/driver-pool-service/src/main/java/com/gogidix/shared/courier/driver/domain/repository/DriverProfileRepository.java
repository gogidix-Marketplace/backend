package com.gogidix.shared.courier.driver.domain.repository;

import com.gogidix.shared.courier.driver.domain.entity.DriverProfile;
import com.gogidix.shared.courier.driver.domain.entity.DriverStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverProfileRepository extends MongoRepository<DriverProfile, String> {

    Optional<DriverProfile> findByTenantIdAndDriverId(String tenantId, String driverId);

    List<DriverProfile> findByTenantIdAndStatus(String tenantId, DriverStatus status);

    @Query("{ 'tenantId': ?0, 'status': ?1, 'currentLocation': { $near: { $geometry: ?2, $maxDistance: ?3 } } }")
    List<DriverProfile> findNearbyAvailableDrivers(String tenantId, DriverStatus status, org.springframework.data.mongodb.core.geo.GeoJsonPoint location, double maxDistanceMeters);

    List<DriverProfile> findByTenantIdAndVehicleType(String tenantId, String vehicleType);

    List<DriverProfile> findByTenantIdAndRatingGreaterThanEqual(String tenantId, Double minRating);
}
