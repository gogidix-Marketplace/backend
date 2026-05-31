package com.gogidix.courier.availabilityservice.domain.repository;

import com.gogidix.courier.availabilityservice.domain.entity.UnavailablePeriod;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for UnavailablePeriod aggregates.
 */
public interface UnavailablePeriodRepository {

    /**
     * Save an unavailable period.
     *
     * @param period the period to save
     * @return the saved period
     */
    UnavailablePeriod save(UnavailablePeriod period);

    /**
     * Find a period by ID.
     *
     * @param id the period ID
     * @return the period if found
     */
    Optional<UnavailablePeriod> findById(String id);

    /**
     * Find all unavailable periods for a driver.
     *
     * @param driverId the driver ID
     * @return list of periods
     */
    List<UnavailablePeriod> findByDriverId(String driverId);

    /**
     * Find unavailable periods for a driver within a time range.
     *
     * @param driverId the driver ID
     * @param startTime the start time
     * @param endTime   the end time
     * @return list of periods
     */
    List<UnavailablePeriod> findByDriverIdAndTimeRange(String driverId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * Find active unavailable periods for a driver.
     *
     * @param driverId the driver ID
     * @return list of active periods
     */
    List<UnavailablePeriod> findActiveByDriverId(String driverId);

    /**
     * Find recurring unavailable periods for a driver.
     *
     * @param driverId the driver ID
     * @return list of recurring periods
     */
    List<UnavailablePeriod> findRecurringByDriverId(String driverId);

    /**
     * Delete a period by ID.
     *
     * @param id the period ID
     */
    void deleteById(String id);

    /**
     * Delete expired periods before a date.
     *
     * @param before the date
     */
    void deleteExpiredBefore(LocalDateTime before);
}
