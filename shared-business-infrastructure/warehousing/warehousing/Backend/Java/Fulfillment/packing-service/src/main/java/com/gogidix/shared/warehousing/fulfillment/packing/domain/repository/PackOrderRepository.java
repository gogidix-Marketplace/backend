package com.gogidix.shared.warehousing.fulfillment.packing.domain.repository;

import com.gogidix.shared.warehousing.fulfillment.packing.domain.entity.PackOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Pack Order entity
 */
@Repository
public interface PackOrderRepository extends MongoRepository<PackOrder, String> {

    List<PackOrder> findByTenantId(String tenantId);

    List<PackOrder> findByTenantIdAndWarehouseId(String tenantId, String warehouseId);

    Optional<PackOrder> findByTenantIdAndPackNumber(String tenantId, String packNumber);

    Optional<PackOrder> findByTenantIdAndPickOrderId(String tenantId, String pickOrderId);

    List<PackOrder> findByTenantIdAndStatus(String tenantId, PackOrder.PackStatus status);

    List<PackOrder> findByTenantIdAndPackerId(String tenantId, String packerId);

    List<PackOrder> findByTenantIdAndStatusAndDueDateBefore(
            String tenantId, PackOrder.PackStatus status, LocalDateTime dueDate);

    List<PackOrder> findByTenantIdAndTrackingNumber(String tenantId, String trackingNumber);

    @Query("{ 'tenantId': ?0, 'warehouseId': ?1, 'status': 'PENDING' }")
    List<PackOrder> findPendingOrdersByWarehouse(String tenantId, String warehouseId);

    List<PackOrder> findByTenantIdAndWarehouseIdAndDueDateBetween(
            String tenantId, String warehouseId, LocalDateTime start, LocalDateTime end);

    @Query("{ 'tenantId': ?0, 'status': 'COMPLETED', 'trackingNumber': null }")
    List<PackOrder> findCompletedWithoutTracking(String tenantId);

    boolean existsByTenantIdAndPackNumber(String tenantId, String packNumber);
}
