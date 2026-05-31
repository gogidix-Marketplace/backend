package com.gogidix.courier.availabilityservice.infrastructure.persistence.adapter;

import com.gogidix.courier.availabilityservice.domain.entity.DriverAvailability;
import com.gogidix.courier.availabilityservice.domain.repository.DriverAvailabilityRepository;
import com.gogidix.courier.availabilityservice.infrastructure.persistence.repository.MongoDriverAvailabilityRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Adapter implementing DriverAvailabilityRepository using MongoDB.
 */
@Repository
public class DriverAvailabilityRepositoryAdapter implements DriverAvailabilityRepository {

    private final MongoDriverAvailabilityRepository mongoRepository;

    public DriverAvailabilityRepositoryAdapter(MongoDriverAvailabilityRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public DriverAvailability save(DriverAvailability availability) {
        return mongoRepository.save(availability);
    }

    @Override
    public Optional<DriverAvailability> findById(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public Optional<DriverAvailability> findByDriverIdAndDate(String driverId, LocalDate date) {
        return mongoRepository.findByDriverIdAndDate(driverId, date);
    }

    @Override
    public List<DriverAvailability> findByDriverId(String driverId) {
        return mongoRepository.findByDriverId(driverId);
    }

    @Override
    public List<DriverAvailability> findByDriverIdAndDateBetween(String driverId, LocalDate startDate, LocalDate endDate) {
        return mongoRepository.findByDriverIdAndDateBetween(driverId, startDate, endDate);
    }

    @Override
    public List<String> findAvailableDrivers(String tenantId, LocalDate date) {
        return mongoRepository.findAvailableDriverIds(tenantId, date);
    }

    @Override
    public List<String> findAvailableDriversByZone(String tenantId, String zoneId, LocalDate date) {
        return mongoRepository.findAvailableDriverIdsByZone(tenantId, zoneId, date);
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public long countByDriverId(String driverId) {
        return mongoRepository.countByDriverId(driverId);
    }
}
