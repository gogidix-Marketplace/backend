package com.gogidix.courier.availabilityservice.infrastructure.persistence.repository;

import com.gogidix.courier.availabilityservice.domain.entity.AvailabilitySlot;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Spring Data MongoDB repository for AvailabilitySlot.
 */
@Repository
public interface MongoAvailabilitySlotRepository extends MongoRepository<AvailabilitySlot, String> {

    /**
     * Find by driver ID and date.
     */
    List<AvailabilitySlot> findByDriverIdAndDate(String driverId, LocalDate date);

    /**
     * Find available slots by driver ID and date.
     */
    List<AvailabilitySlot> findByDriverIdAndDateAndStatus(String driverId, LocalDate date,
                                                           AvailabilitySlot.SlotStatus status);

    /**
     * Find overlapping slots.
     */
    @Query("{ 'driverId': ?0, 'date': ?1, $or: [ " +
            "{ 'startTime': { $lt: ?3 }, 'endTime': { $gt: ?2 } }, " +
            "{ 'startTime': { $lt: ?3 }, 'endTime': { $gte: ?3 } }, " +
            "{ 'startTime': { $lte: ?2 }, 'endTime': { $gt: ?2 } } " +
            "] }")
    List<AvailabilitySlot> findOverlappingSlots(String driverId, LocalDate date,
                                                LocalTime startTime, LocalTime endTime);

    /**
     * Find available slots by zone.
     */
    @Query("{ 'tenantId': ?0, 'zoneId': ?1, 'date': ?2, 'status': 'AVAILABLE' }")
    List<AvailabilitySlot> findAvailableByZone(String tenantId, String zoneId, LocalDate date);

    /**
     * Delete by driver ID and date.
     */
    void deleteByDriverIdAndDate(String driverId, LocalDate date);
}
