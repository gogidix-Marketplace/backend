package com.gogidix.courier.availabilityservice.infrastructure.persistence.adapter;

import com.gogidix.courier.availabilityservice.domain.entity.UnavailablePeriod;
import com.gogidix.courier.availabilityservice.domain.repository.UnavailablePeriodRepository;
import com.gogidix.courier.availabilityservice.infrastructure.persistence.repository.MongoUnavailablePeriodRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Adapter implementing UnavailablePeriodRepository using MongoDB.
 */
@Repository
public class UnavailablePeriodRepositoryAdapter implements UnavailablePeriodRepository {

    private final MongoUnavailablePeriodRepository mongoRepository;

    public UnavailablePeriodRepositoryAdapter(MongoUnavailablePeriodRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public UnavailablePeriod save(UnavailablePeriod period) {
        return mongoRepository.save(period);
    }

    @Override
    public Optional<UnavailablePeriod> findById(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public List<UnavailablePeriod> findByDriverId(String driverId) {
        return mongoRepository.findByDriverId(driverId);
    }

    @Override
    public List<UnavailablePeriod> findByDriverIdAndTimeRange(String driverId, LocalDateTime startTime, LocalDateTime endTime) {
        return mongoRepository.findByDriverIdAndTimeOverlap(driverId, startTime, endTime);
    }

    @Override
    public List<UnavailablePeriod> findActiveByDriverId(String driverId) {
        return mongoRepository.findActiveByDriverId(driverId, LocalDateTime.now());
    }

    @Override
    public List<UnavailablePeriod> findRecurringByDriverId(String driverId) {
        return mongoRepository.findByDriverIdAndIsRecurringTrue(driverId);
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public void deleteExpiredBefore(LocalDateTime before) {
        mongoRepository.deleteByEndTimeBefore(before);
    }
}
