package com.gogidix.shared.warehousing.reorder.domain.repository;

import com.gogidix.shared.warehousing.reorder.domain.entity.ReorderPoint;
import com.gogidix.shared.warehousing.reorder.domain.entity.ReorderPoint.ReorderStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Reorder Point Repository with Multi-Tenant Support
 */
@Repository
public interface ReorderPointRepository extends MongoRepository<ReorderPoint, String> {

    /**
     * Find reorder point by SKU and tenant
     */
    Optional<ReorderPoint> findByTenantIdAndSku(String tenantId, String sku);

    /**
     * Find reorder points by status and tenant
     */
    List<ReorderPoint> findByTenantIdAndStatus(String tenantId, ReorderStatus status);

    /**
     * Find reorder points by supplier
     */
    List<ReorderPoint> findByTenantIdAndSupplierId(String tenantId, String supplierId);

    /**
     * Find all reorder points for a tenant
     */
    List<ReorderPoint> findByTenantId(String tenantId);

    /**
     * Find reorder points that need reordering (below reorder point)
     */
    List<ReorderPoint> findByTenantIdAndStatusAndAutoReorderEnabledTrue(
        String tenantId, ReorderStatus status);

    /**
     * Check if reorder point exists for SKU
     */
    boolean existsByTenantIdAndSku(String tenantId, String sku);
}
