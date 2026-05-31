package com.gogidix.courier.availabilityservice.domain.repository;

import com.gogidix.courier.availabilityservice.domain.entity.AvailabilitySlot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for AvailabilitySlot aggregates.
 */
public interface AvailabilitySlotRepository {

    /**
     * Save a slot.
     *
     * @param slot the slot to save
     * @return the saved slot
     */
    AvailabilitySlot save(AvailabilitySlot slot);

    /**
     * Find a slot by ID.
     *
     * @param id the slot ID
     * @return the slot if found
     */
    Optional<AvailabilitySlot> findById(String id);

    /**
     * Find all slots for a driver on a specific date.
     *
     * @param driverId the driver ID
     * @param date     the date
     * @return list of slots
     */
    List<AvailabilitySlot> findByDriverIdAndDate(String driverId, LocalDate date);

    /**
     * Find available slots for a driver on a specific date.
     *
     * @param driverId the driver ID
     * @param date     the date
     * @return list of available slots
     */
    List<AvailabilitySlot> findAvailableSlotsByDriverIdAndDate(String driverId, LocalDate date);

    /**
     * Find slots within a time range.
     *
     * @param driverId  the driver ID
     * @param date      the date
     * @param startTime the start time
     * @param endTime   the end time
     * @return list of overlapping slots
     */
    List<AvailabilitySlot> findByDriverIdAndDateAndTimeRange(String driverId, LocalDate date,
                                                             LocalTime startTime, LocalTime endTime);

    /**
     * Find available slots by zone.
     *
     * @param tenantId the tenant ID
     * @param zoneId   the zone ID
     * @param date     the date
     * @return list of available slots
     */
    List<AvailabilitySlot> findAvailableSlotsByZone(String tenantId, String zoneId, LocalDate date);

    /**
     * Delete a slot by ID.
     *
     * @param id the slot ID
     */
    void deleteById(String id);

    /**
     * Delete slots for a driver on a specific date.
     *
     * @param driverId the driver ID
     * @param date     the date
     */
    void deleteByDriverIdAndDate(String driverId, LocalDate date);
}
