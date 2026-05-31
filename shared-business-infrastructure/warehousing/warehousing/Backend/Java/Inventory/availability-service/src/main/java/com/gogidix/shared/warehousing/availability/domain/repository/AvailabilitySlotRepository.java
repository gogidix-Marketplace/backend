package com.gogidix.shared.warehousing.availability.domain.repository;

import com.gogidix.shared.warehousing.availability.domain.entity.AvailabilitySlot;
import com.gogidix.shared.warehousing.availability.domain.entity.AvailabilitySlot.SlotStatus;
import com.gogidix.shared.warehousing.availability.domain.entity.AvailabilitySlot.SlotType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Availability Slot Repository
 *
 * MongoDB repository for availability slots
 */
@Repository
public interface AvailabilitySlotRepository extends MongoRepository<AvailabilitySlot, String> {

    /**
     * Find available slots for warehouse and time range
     */
    @Query("{'tenantId': ?0, 'warehouseId': ?1, 'slotType': ?2, 'startTime': {'$gte': ?3}, 'endTime': {'$lte': ?4}, 'status': 'AVAILABLE'}")
    List<AvailabilitySlot> findAvailableSlots(String tenantId, String warehouseId, SlotType slotType,
                                              LocalDateTime startTime, LocalDateTime endTime);

    /**
     * Find slots by status
     */
    List<AvailabilitySlot> findByTenantIdAndWarehouseIdAndStatus(
            String tenantId, String warehouseId, SlotStatus status);

    /**
     * Find slots by reservation ID
     */
    List<AvailabilitySlot> findByTenantIdAndReservationId(String tenantId, String reservationId);

    /**
     * Find expired slots (end time in past, still reserved)
     */
    @Query("{'tenantId': ?0, 'endTime': {'$lt': ?1}, 'status': 'RESERVED'}")
    List<AvailabilitySlot> findExpiredSlots(String tenantId, LocalDateTime now);

    /**
     * Find slots by type and date range
     */
    List<AvailabilitySlot> findByTenantIdAndWarehouseIdAndSlotTypeAndStartTimeBetween(
            String tenantId, String warehouseId, SlotType slotType, LocalDateTime start, LocalDateTime end);
}
