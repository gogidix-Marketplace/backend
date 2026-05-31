package com.gogidix.shared.courier.dispatch.domain.repository;

import com.gogidix.shared.courier.dispatch.domain.entity.DispatchOrder;
import com.gogidix.shared.courier.dispatch.domain.entity.DispatchStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for DispatchOrder with geospatial queries
 */
@Repository
public interface DispatchOrderRepository extends MongoRepository<DispatchOrder, String> {

    List<DispatchOrder> findByTenantId(String tenantId);

    Optional<DispatchOrder> findByTenantIdAndDispatchId(String tenantId, String dispatchId);

    List<DispatchOrder> findByTenantIdAndStatus(String tenantId, DispatchStatus status);

    List<DispatchOrder> findByTenantIdAndAssignedDriverId(String tenantId, String driverId);

    List<DispatchOrder> findByTenantIdAndCustomerId(String tenantId, String customerId);

    // GEOSPATIAL QUERIES
    @Query("{ 'tenantId': ?0, 'pickupLocation': { $near: { $geometry: ?1, $maxDistance: ?2 } } }")
    List<DispatchOrder> findNearbyPickups(String tenantId, org.springframework.data.mongodb.core.geo.GeoJsonPoint location, double maxDistanceMeters);

    @Query("{ 'tenantId': ?0, 'deliveryLocation': { $near: { $geometry: ?1, $maxDistance: ?2 } } }")
    List<DispatchOrder> findNearbyDeliveries(String tenantId, org.springframework.data.mongodb.core.geo.GeoJsonPoint location, double maxDistanceMeters);

    @Query("{ 'tenantId': ?0, 'status': ?1, 'pickupLocation': { $near: { $geometry: ?2, $maxDistance: ?3 } } }")
    List<DispatchOrder> findNearbyPickupsByStatus(String tenantId, DispatchStatus status, org.springframework.data.mongodb.core.geo.GeoJsonPoint location, double maxDistanceMeters);

    List<DispatchOrder> findByTenantIdAndEstimatedPickupTimeBetween(
            String tenantId, LocalDateTime startTime, LocalDateTime endTime);

    List<DispatchOrder> findByTenantIdAndPriorityGreaterThanEqualOrderByPriorityDesc(
            String tenantId, Integer priority);

    boolean existsByTenantIdAndDispatchId(String tenantId, String dispatchId);

    void deleteByTenantIdAndDispatchId(String tenantId, String dispatchId);
}
