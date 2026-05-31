package com.gogidix.shared.warehousing.fulfillment.picking.domain.repository;

import com.gogidix.shared.warehousing.fulfillment.picking.domain.entity.PickOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Pick Order entity
 */
@Repository
public interface PickOrderRepository extends MongoRepository<PickOrder, String> {

    List<PickOrder> findByTenantId(String tenantId);

    List<PickOrder> findByTenantIdAndWarehouseId(String tenantId, String warehouseId);

    Optional<PickOrder> findByTenantIdAndPickNumber(String tenantId, String pickNumber);

    Optional<PickOrder> findByTenantIdAndOrderNumber(String tenantId, String orderNumber);

    List<PickOrder> findByTenantIdAndStatus(String tenantId, PickOrder.PickStatus status);

    List<PickOrder> findByTenantIdAndPickerId(String tenantId, String pickerId);

    List<PickOrder> findByTenantIdAndStatusAndDueDateBefore(
            String tenantId, PickOrder.PickStatus status, LocalDateTime dueDate);

    @Query("{ 'tenantId': ?0, 'status': { $in: ['PENDING', 'ASSIGNED'] }, 'priority': { $gte: ?1 } }")
    List<PickOrder> findHighPriorityPendingOrders(String tenantId, Integer minPriority);

    @Query("{ 'tenantId': ?0, 'warehouseId': ?1, 'status': 'PENDING' }")
    List<PickOrder> findPendingOrdersByWarehouse(String tenantId, String warehouseId);

    List<PickOrder> findByTenantIdAndWarehouseIdAndDueDateBetween(
            String tenantId, String warehouseId, LocalDateTime start, LocalDateTime end);

    boolean existsByTenantIdAndPickNumber(String tenantId, String pickNumber);
}
