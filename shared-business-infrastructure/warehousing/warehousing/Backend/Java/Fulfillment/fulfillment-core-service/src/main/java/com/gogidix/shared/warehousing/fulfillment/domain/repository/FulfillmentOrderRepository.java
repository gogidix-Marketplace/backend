package com.gogidix.shared.warehousing.fulfillment.domain.repository;

import com.gogidix.shared.warehousing.fulfillment.domain.entity.FulfillmentOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * MongoDB Repository for FulfillmentOrder entity
 */
@Repository
public interface FulfillmentOrderRepository extends MongoRepository<FulfillmentOrder, String> {

    /**
     * Find all orders by tenant ID
     */
    List<FulfillmentOrder> findByTenantId(String tenantId);

    /**
     * Find orders by tenant and customer
     */
    List<FulfillmentOrder> findByTenantIdAndCustomerId(String tenantId, String customerId);

    /**
     * Find orders by tenant and status
     */
    List<FulfillmentOrder> findByTenantIdAndStatus(String tenantId, String status);

    /**
     * Find order by tenant and order number
     */
    FulfillmentOrder findByTenantIdAndOrderNumber(String tenantId, String orderNumber);

    /**
     * Check if order number exists for tenant
     */
    boolean existsByTenantIdAndOrderNumber(String tenantId, String orderNumber);

    /**
     * Find high priority orders
     */
    List<FulfillmentOrder> findByTenantIdAndPriority(String tenantId, String priority);

    /**
     * Find orders pending pickup
     */
    @Query("{ 'tenantId': ?0, 'status': 'PENDING' }")
    List<FulfillmentOrder> findPendingOrders(String tenantId);

    /**
     * Find orders ready for shipping
     */
    @Query("{ 'tenantId': ?0, 'status': 'PACKING' }")
    List<FulfillmentOrder> findReadyForShipping(String tenantId);

    /**
     * Find orders by date range
     */
    List<FulfillmentOrder> findByTenantIdAndCreatedAtBetween(
        String tenantId,
        LocalDateTime startDate,
        LocalDateTime endDate
    );

    /**
     * Count orders by status
     */
    long countByTenantIdAndStatus(String tenantId, String status);
}
