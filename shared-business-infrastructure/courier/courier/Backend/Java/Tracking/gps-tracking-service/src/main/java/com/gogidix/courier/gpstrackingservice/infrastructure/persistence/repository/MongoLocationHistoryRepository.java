package com.gogidix.courier.gpstrackingservice.infrastructure.persistence.repository;

import com.gogidix.courier.gpstrackingservice.domain.entity.LocationHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB repository for LocationHistory entities.
 */
@Repository
public interface MongoLocationHistoryRepository extends MongoRepository<LocationHistory, String> {

    /**
     * Find history by driver ID and date.
     */
    Optional<LocationHistory> findByDriverIdAndDate(String driverId, String date);

    /**
     * Find history by tenant, driver ID and date.
     */
    Optional<LocationHistory> findByTenantIdAndDriverIdAndDate(String tenantId, String driverId, String date);

    /**
     * Find history by driver ID in date range.
     */
    List<LocationHistory> findByDriverIdAndDateBetweenOrderByDateDesc(
            String driverId, String startDate, String endDate);

    /**
     * Find history by order ID.
     */
    List<LocationHistory> findByOrderId(String orderId);

    /**
     * Delete history older than specified date.
     */
    long deleteByDateBefore(String date);
}
