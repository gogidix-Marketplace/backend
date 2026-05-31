package com.gogidix.courier.availabilityservice.infrastructure.persistence.adapter;

import com.gogidix.courier.availabilityservice.domain.entity.AvailabilitySlot;
import com.gogidix.courier.availabilityservice.domain.repository.AvailabilitySlotRepository;
import com.gogidix.courier.availabilityservice.infrastructure.persistence.repository.MongoAvailabilitySlotRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Adapter implementing AvailabilitySlotRepository using MongoDB.
 */
@Repository
public class AvailabilitySlotRepositoryAdapter implements AvailabilitySlotRepository {

    private final MongoAvailabilitySlotRepository mongoRepository;

    public AvailabilitySlotRepositoryAdapter(MongoAvailabilitySlotRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public AvailabilitySlot save(AvailabilitySlot slot) {
        return mongoRepository.save(slot);
    }

    @Override
    public Optional<AvailabilitySlot> findById(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public List<AvailabilitySlot> findByDriverIdAndDate(String driverId, LocalDate date) {
        return mongoRepository.findByDriverIdAndDate(driverId, date);
    }

    @Override
    public List<AvailabilitySlot> findAvailableSlotsByDriverIdAndDate(String driverId, LocalDate date) {
        return mongoRepository.findByDriverIdAndDateAndStatus(driverId, date,
                AvailabilitySlot.SlotStatus.AVAILABLE);
    }

    @Override
    public List<AvailabilitySlot> findByDriverIdAndDateAndTimeRange(String driverId, LocalDate date,
                                                                     LocalTime startTime, LocalTime endTime) {
        return mongoRepository.findOverlappingSlots(driverId, date, startTime, endTime);
    }

    @Override
    public List<AvailabilitySlot> findAvailableSlotsByZone(String tenantId, String zoneId, LocalDate date) {
        return mongoRepository.findAvailableByZone(tenantId, zoneId, date);
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public void deleteByDriverIdAndDate(String driverId, LocalDate date) {
        mongoRepository.deleteByDriverIdAndDate(driverId, date);
    }
}
