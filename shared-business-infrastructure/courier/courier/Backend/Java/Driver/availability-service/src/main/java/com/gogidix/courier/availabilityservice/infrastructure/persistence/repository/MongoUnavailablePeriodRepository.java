package com.gogidix.courier.availabilityservice.infrastructure.persistence.repository;

import com.gogidix.courier.availabilityservice.domain.entity.UnavailablePeriod;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data MongoDB repository for UnavailablePeriod.
 */
@Repository
public interface MongoUnavailablePeriodRepository extends MongoRepository<UnavailablePeriod, String> {

    /**
     * Find all by driver ID.
     */
    List<UnavailablePeriod> findByDriverId(String driverId);

    /**
     * Find by driver ID and time range overlap.
     */
    @Query("{ 'driverId': ?0, $or: [ " +
            "{ 'startTime': { $lte: ?2 }, 'endTime': { $gte: ?1 } }, " +
            "{ 'startTime': { $gte: ?1, $lte: ?2 } } " +
            "] }")
    List<UnavailablePeriod> findByDriverIdAndTimeOverlap(String driverId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * Find active periods for driver.
     */
    @Query("{ 'driverId': ?0, 'startTime': { $lte: ?1 }, 'endTime': { $gte: ?1 } }")
    List<UnavailablePeriod> findActiveByDriverId(String driverId, LocalDateTime now);

    /**
     * Find recurring periods.
     */
    List<UnavailablePeriod> findByDriverIdAndIsRecurringTrue(String driverId);

    /**
     * Delete expired periods.
     */
    void deleteByEndTimeBefore(LocalDateTime before);
}
