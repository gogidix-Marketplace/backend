package com.gogidix.courier.gpstrackingservice.infrastructure.persistence.repository;

import com.gogidix.courier.gpstrackingservice.domain.entity.GpsLocation;
import com.gogidix.courier.gpstrackingservice.domain.entity.LocationHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * MongoDB repository for GpsLocation entities.
 */
@Repository
public interface MongoGpsLocationRepository extends MongoRepository<GpsLocation, String> {

    /**
     * Find most recent location by driver ID.
     */
    GpsLocation findFirstByDriverIdOrderByTimestampDesc(String driverId);

    /**
     * Find most recent location by tenant and driver ID.
     */
    GpsLocation findFirstByTenantIdAndDriverIdOrderByTimestampDesc(String tenantId, String driverId);

    /**
     * Find locations by driver ID within time range.
     */
    List<GpsLocation> findByDriverIdAndTimestampBetweenOrderByTimestampDesc(
            String driverId, Instant startTime, Instant endTime);

    /**
     * Find locations by tenant, driver ID and time range.
     */
    List<GpsLocation> findByTenantIdAndDriverIdAndTimestampBetweenOrderByTimestampDesc(
            String tenantId, String driverId, Instant startTime, Instant endTime);

    /**
     * Find locations by order ID.
     */
    List<GpsLocation> findByOrderIdOrderByTimestampDesc(String orderId);

    /**
     * Find locations by tenant and order ID.
     */
    List<GpsLocation> findByTenantIdAndOrderIdOrderByTimestampDesc(String tenantId, String orderId);

    /**
     * Find locations for a driver paginated.
     */
    List<GpsLocation> findByDriverIdOrderByTimestampDesc(String driverId, Pageable pageable);

    /**
     * Find recent locations for a driver.
     */
    List<GpsLocation> findByDriverIdAndTimestampAfterOrderByTimestampDesc(
            String driverId, Instant since);

    /**
     * Find recent locations for a driver within tenant.
     */
    List<GpsLocation> findByTenantIdAndDriverIdAndTimestampAfterOrderByTimestampDesc(
            String tenantId, String driverId, Instant since);

    /**
     * Find locations near a point using geospatial query.
     */
    @Query("{ 'location': { $near: { $geometry: { type: 'Point', coordinates: [ ?1, ?0 ] }, $maxDistance: ?2 } } }")
    List<GpsLocation> findNearby(double longitude, double latitude, double radiusMeters);

    /**
     * Find locations near a point within tenant.
     */
    @Query("{ 'tenantId': ?3, 'location': { $near: { $geometry: { type: 'Point', coordinates: [ ?1, ?0 ] }, $maxDistance: ?2 } } }")
    List<GpsLocation> findByTenantIdNearby(double longitude, double latitude, double radiusMeters, String tenantId);

    /**
     * Count locations by driver ID.
     */
    long countByDriverId(String driverId);

    /**
     * Count locations by tenant and driver ID.
     */
    long countByTenantIdAndDriverId(String tenantId, String driverId);

    /**
     * Delete locations older than specified date.
     */
    long deleteByTimestampBefore(Instant before);

    /**
     * Delete locations for driver older than specified date.
     */
    long deleteByDriverIdAndTimestampBefore(String driverId, Instant before);
}
