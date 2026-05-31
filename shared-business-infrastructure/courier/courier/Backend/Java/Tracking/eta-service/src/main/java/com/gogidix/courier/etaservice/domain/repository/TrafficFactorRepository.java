package com.gogidix.courier.etaservice.domain.repository;

import com.gogidix.courier.etaservice.domain.entity.TrafficFactor;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for TrafficFactor aggregates.
 * Defines the contract for traffic factor persistence operations.
 */
public interface TrafficFactorRepository {

    /**
     * Save a traffic factor.
     *
     * @param factor the factor to save
     * @return the saved factor
     */
    TrafficFactor save(TrafficFactor factor);

    /**
     * Save all traffic factors.
     *
     * @param factors the factors to save
     * @return the saved factors
     */
    List<TrafficFactor> saveAll(List<TrafficFactor> factors);

    /**
     * Find a traffic factor by ID.
     *
     * @param id the factor ID
     * @return the factor if found
     */
    Optional<TrafficFactor> findById(String id);

    /**
     * Find traffic factors by area code.
     *
     * @param areaCode the area code
     * @return list of factors
     */
    List<TrafficFactor> findByAreaCode(String areaCode);

    /**
     * Find traffic factors by area code and tenant.
     *
     * @param areaCode the area code
     * @param tenantId the tenant ID
     * @return list of factors
     */
    List<TrafficFactor> findByAreaCodeAndTenantId(String areaCode, String tenantId);

    /**
     * Find traffic factor by area, day of week, and hour.
     *
     * @param areaCode  the area code
     * @param dayOfWeek the day of week (1-7)
     * @param hourOfDay the hour of day (0-23)
     * @return the factor if found
     */
    Optional<TrafficFactor> findByAreaCodeAndDayOfWeekAndHourOfDay(
            String areaCode, int dayOfWeek, int hourOfDay);

    /**
     * Find traffic factor by area, day of week, hour, and tenant.
     *
     * @param areaCode  the area code
     * @param dayOfWeek the day of week (1-7)
     * @param hourOfDay the hour of day (0-23)
     * @param tenantId  the tenant ID
     * @return the factor if found
     */
    Optional<TrafficFactor> findByAreaCodeAndTenantIdAndDayOfWeekAndHourOfDay(
            String areaCode, String tenantId, int dayOfWeek, int hourOfDay);

    /**
     * Find traffic factors by day of week and hour.
     *
     * @param dayOfWeek the day of week (1-7)
     * @param hourOfDay the hour of day (0-23)
     * @return list of factors
     */
    List<TrafficFactor> findByDayOfWeekAndHourOfDay(int dayOfWeek, int hourOfDay);

    /**
     * Find traffic factors near a location.
     *
     * @param latitude  the latitude
     * @param longitude the longitude
     * @param radiusKm  the search radius in km
     * @return list of factors
     */
    List<TrafficFactor> findNearLocation(Double latitude, Double longitude, Double radiusKm);

    /**
     * Find traffic factors by tenant.
     *
     * @param tenantId the tenant ID
     * @return list of factors
     */
    List<TrafficFactor> findByTenantId(String tenantId);

    /**
     * Find all traffic factors.
     *
     * @return list of all factors
     */
    List<TrafficFactor> findAll();

    /**
     * Find peak hour factors for a tenant.
     *
     * @param tenantId the tenant ID
     * @return list of peak hour factors
     */
    List<TrafficFactor> findPeakHoursByTenantId(String tenantId);

    /**
     * Check if a factor exists for the given parameters.
     *
     * @param areaCode  the area code
     * @param dayOfWeek the day of week (1-7)
     * @param hourOfDay the hour of day (0-23)
     * @param tenantId  the tenant ID
     * @return true if exists, false otherwise
     */
    boolean existsByAreaCodeAndTenantIdAndDayOfWeekAndHourOfDay(
            String areaCode, String tenantId, int dayOfWeek, int hourOfDay);

    /**
     * Delete a traffic factor by ID.
     *
     * @param id the factor ID
     */
    void deleteById(String id);

    /**
     * Count all traffic factors.
     *
     * @return the count
     */
    long count();

    /**
     * Count traffic factors by tenant.
     *
     * @param tenantId the tenant ID
     * @return the count
     */
    long countByTenantId(String tenantId);

    /**
     * Delete old traffic factors.
     *
     * @param before    the timestamp threshold
     * @param tenantId  the tenant ID
     * @return the number of deleted factors
     */
    long deleteByLastObservedAtBeforeAndTenantId(java.time.Instant before, String tenantId);
}
