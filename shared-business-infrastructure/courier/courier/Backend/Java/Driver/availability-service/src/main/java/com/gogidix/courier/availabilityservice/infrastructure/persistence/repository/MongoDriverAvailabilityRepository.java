package com.gogidix.courier.availabilityservice.infrastructure.persistence.repository;

import com.gogidix.courier.availabilityservice.domain.entity.DriverAvailability;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data MongoDB repository for DriverAvailability.
 */
@Repository
public interface MongoDriverAvailabilityRepository extends MongoRepository<DriverAvailability, String> {

    /**
     * Find by driver ID and date.
     */
    Optional<DriverAvailability> findByDriverIdAndDate(String driverId, LocalDate date);

    /**
     * Find all by driver ID.
     */
    List<DriverAvailability> findByDriverId(String driverId);

    /**
     * Find by driver ID and date between.
     */
    List<DriverAvailability> findByDriverIdAndDateBetween(String driverId, LocalDate startDate, LocalDate endDate);

    /**
     * Find available drivers by tenant and date.
     */
    @Query("{ 'tenantId': ?0, 'date': ?1, 'status': 'AVAILABLE', 'currentLoad': { $lt: '$maxCapacity' } }")
    List<String> findAvailableDriverIds(String tenantId, LocalDate date);

    /**
     * Find available drivers by tenant, zone, and date.
     */
    @Query("{ 'tenantId': ?0, 'preferredZones': ?1, 'date': ?2, 'status': 'AVAILABLE', 'currentLoad': { $lt: '$maxCapacity' } }")
    List<String> findAvailableDriverIdsByZone(String tenantId, String zoneId, LocalDate date);

    /**
     * Count by driver ID.
     */
    long countByDriverId(String driverId);
}
