package com.gogidix.courier.gpstrackingservice.domain.repository;

import com.gogidix.courier.gpstrackingservice.domain.entity.GpsLocation;
import com.gogidix.courier.gpstrackingservice.domain.entity.LocationHistory;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for GpsLocation aggregates.
 * Defines the contract for GPS location persistence operations.
 * All implementations MUST filter by tenantId for multi-tenancy.
 */
public interface GpsLocationRepository {

    /**
     * Save a GPS location.
     *
     * @param location the location to save
     * @return the saved location
     */
    GpsLocation save(GpsLocation location);

    /**
     * Find the most recent location for a driver.
     *
     * @param driverId the driver ID
     * @return the most recent location if found
     */
    Optional<GpsLocation> findMostRecentByDriverId(String driverId);

    /**
     * Find the most recent location for a driver within a tenant.
     *
     * @param tenantId the tenant ID
     * @param driverId the driver ID
     * @return the most recent location if found
     */
    Optional<GpsLocation> findMostRecentByDriverIdAndTenantId(String tenantId, String driverId);

    /**
     * Find location history for a driver within a time range.
     *
     * @param driverId the driver ID
     * @param startTime the start time
     * @param endTime the end time
     * @return list of locations
     */
    List<GpsLocation> findByDriverIdAndTimestampBetween(String driverId, Instant startTime, Instant endTime);

    /**
     * Find location history for a driver within a tenant and time range.
     *
     * @param tenantId the tenant ID
     * @param driverId the driver ID
     * @param startTime the start time
     * @param endTime the end time
     * @return list of locations
     */
    List<GpsLocation> findByTenantIdAndDriverIdAndTimestampBetween(String tenantId, String driverId, Instant startTime, Instant endTime);

    /**
     * Find locations for an order.
     *
     * @param orderId the order ID
     * @return list of locations
     */
    List<GpsLocation> findByOrderId(String orderId);

    /**
     * Find locations for an order within a tenant.
     *
     * @param tenantId the tenant ID
     * @param orderId the order ID
     * @return list of locations
     */
    List<GpsLocation> findByTenantIdAndOrderId(String tenantId, String orderId);

    /**
     * Find all locations for a driver paginated.
     *
     * @param driverId the driver ID
     * @param page the page number (0-indexed)
     * @param size the page size
     * @return list of locations
     */
    List<GpsLocation> findByDriverIdPaginated(String driverId, int page, int size);

    /**
     * Find locations near a given point.
     *
     * @param latitude the latitude
     * @param longitude the longitude
     * @param radiusMeters the search radius in meters
     * @return list of nearby locations
     */
    List<GpsLocation> findNearby(double latitude, double longitude, double radiusMeters);

    /**
     * Find locations near a given point within a tenant.
     *
     * @param tenantId the tenant ID
     * @param latitude the latitude
     * @param longitude the longitude
     * @param radiusMeters the search radius in meters
     * @return list of nearby locations
     */
    List<GpsLocation> findByTenantIdNearby(String tenantId, double latitude, double longitude, double radiusMeters);

    /**
     * Find recent locations (within specified minutes).
     *
     * @param driverId the driver ID
     * @param minutes the time window in minutes
     * @return list of recent locations
     */
    List<GpsLocation> findRecentByDriverId(String driverId, int minutes);

    /**
     * Find recent locations within a tenant.
     *
     * @param tenantId the tenant ID
     * @param driverId the driver ID
     * @param minutes the time window in minutes
     * @return list of recent locations
     */
    List<GpsLocation> findRecentByTenantIdAndDriverId(String tenantId, String driverId, int minutes);

    /**
     * Delete old locations older than specified date.
     *
     * @param before the cutoff date
     * @return the number of deleted locations
     */
    long deleteOlderThan(Instant before);

    /**
     * Delete old locations for a specific driver.
     *
     * @param driverId the driver ID
     * @param before the cutoff date
     * @return the number of deleted locations
     */
    long deleteByDriverIdOlderThan(String driverId, Instant before);

    /**
     * Count locations for a driver.
     *
     * @param driverId the driver ID
     * @return the count
     */
    long countByDriverId(String driverId);

    /**
     * Count locations for a driver within a tenant.
     *
     * @param tenantId the tenant ID
     * @param driverId the driver ID
     * @return the count
     */
    long countByTenantIdAndDriverId(String tenantId, String driverId);

    /**
     * Save or update location history.
     *
     * @param history the history to save
     * @return the saved history
     */
    LocationHistory saveHistory(LocationHistory history);

    /**
     * Find location history for a driver by date.
     *
     * @param driverId the driver ID
     * @param date the date (YYYY-MM-DD format)
     * @return the history if found
     */
    Optional<LocationHistory> findHistoryByDriverIdAndDate(String driverId, String date);

    /**
     * Find location history for a driver within a tenant by date.
     *
     * @param tenantId the tenant ID
     * @param driverId the driver ID
     * @param date the date (YYYY-MM-DD format)
     * @return the history if found
     */
    Optional<LocationHistory> findHistoryByTenantIdAndDriverIdAndDate(String tenantId, String driverId, String date);

    /**
     * Find location history for a driver within a date range.
     *
     * @param driverId the driver ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of histories
     */
    List<LocationHistory> findHistoryByDriverIdAndDateBetween(String driverId, String startDate, String endDate);

    /**
     * Find location history for an order.
     *
     * @param orderId the order ID
     * @return list of histories
     */
    List<LocationHistory> findHistoryByOrderId(String orderId);

    /**
     * Delete location history older than specified date.
     *
     * @param date the cutoff date (YYYY-MM-DD format)
     * @return the number of deleted records
     */
    long deleteHistoryOlderThan(String date);
}
