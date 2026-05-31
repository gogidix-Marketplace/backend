package com.gogidix.shared.warehousing.reorder.domain.repository;

import com.gogidix.shared.warehousing.reorder.domain.entity.PurchaseOrder;
import com.gogidix.shared.warehousing.reorder.domain.entity.PurchaseOrder.OrderStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Purchase Order Repository with Multi-Tenant Support
 */
@Repository
public interface PurchaseOrderRepository extends MongoRepository<PurchaseOrder, String> {

    /**
     * Find purchase order by order number and tenant
     */
    Optional<PurchaseOrder> findByTenantIdAndOrderNumber(String tenantId, String orderNumber);

    /**
     * Find purchase orders by status and tenant
     */
    List<PurchaseOrder> findByTenantIdAndStatus(String tenantId, OrderStatus status);

    /**
     * Find purchase orders by supplier
     */
    List<PurchaseOrder> findByTenantIdAndSupplierId(String tenantId, String supplierId);

    /**
     * Find all purchase orders for a tenant
     */
    List<PurchaseOrder> findByTenantId(String tenantId);

    /**
     * Check if order number exists for tenant
     */
    boolean existsByTenantIdAndOrderNumber(String tenantId, String orderNumber);
}
