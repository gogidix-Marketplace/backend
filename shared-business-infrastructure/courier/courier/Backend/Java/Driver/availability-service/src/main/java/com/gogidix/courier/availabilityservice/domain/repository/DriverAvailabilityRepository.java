package com.gogidix.courier.availabilityservice.domain.repository;

import com.gogidix.courier.availabilityservice.domain.entity.DriverAvailability;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for DriverAvailability aggregates.
 */
public interface DriverAvailabilityRepository {

    /**
     * Save a driver availability.
     *
     * @param availability the availability to save
     * @return the saved availability
     */
    DriverAvailability save(DriverAvailability availability);

    /**
     * Find a driver availability by ID.
     *
     * @param id the availability ID
     * @return the availability if found
     */
    Optional<DriverAvailability> findById(String id);

    /**
     * Find driver availability by driver ID and date.
     *
     * @param driverId the driver ID
     * @param date     the date
     * @return the availability if found
     */
    Optional<DriverAvailability> findByDriverIdAndDate(String driverId, LocalDate date);

    /**
     * Find all availability for a driver.
     *
     * @param driverId the driver ID
     * @return list of availability records
     */
    List<DriverAvailability> findByDriverId(String driverId);

    /**
     * Find availability for a driver within a date range.
     *
     * @param driverId the driver ID
     * @param startDate the start date
     * @param endDate   the end date
     * @return list of availability records
     */
    List<DriverAvailability> findByDriverIdAndDateBetween(String driverId, LocalDate startDate, LocalDate endDate);

    /**
     * Find available drivers on a specific date.
     *
     * @param tenantId the tenant ID
     * @param date     the date
     * @return list of available driver IDs
     */
    List<String> findAvailableDrivers(String tenantId, LocalDate date);

    /**
     * Find available drivers by zone.
     *
     * @param tenantId the tenant ID
     * @param zoneId   the zone ID
     * @param date     the date
     * @return list of available driver IDs
     */
    List<String> findAvailableDriversByZone(String tenantId, String zoneId, LocalDate date);

    /**
     * Delete a driver availability by ID.
     *
     * @param id the availability ID
     */
    void deleteById(String id);

    /**
     * Count all availability records for a driver.
     *
     * @param driverId the driver ID
     * @return the count
     */
    long countByDriverId(String driverId);
}
