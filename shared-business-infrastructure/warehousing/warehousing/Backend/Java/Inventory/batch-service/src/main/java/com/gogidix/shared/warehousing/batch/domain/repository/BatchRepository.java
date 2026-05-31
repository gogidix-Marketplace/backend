package com.gogidix.shared.warehousing.batch.domain.repository;

import com.gogidix.shared.warehousing.batch.domain.entity.Batch;
import com.gogidix.shared.warehousing.batch.domain.entity.Batch.BatchStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Batch Repository with Multi-Tenant Support
 */
@Repository
public interface BatchRepository extends MongoRepository<Batch, String> {

    /**
     * Find batch by batch number and tenant
     */
    Optional<Batch> findByTenantIdAndBatchNumber(String tenantId, String batchNumber);

    /**
     * Find batches by SKU and tenant
     */
    List<Batch> findByTenantIdAndSku(String tenantId, String sku);

    /**
     * Find batches by status and tenant
     */
    List<Batch> findByTenantIdAndStatus(String tenantId, BatchStatus status);

    /**
     * Find batches by tenant and location
     */
    List<Batch> findByTenantIdAndLocationId(String tenantId, String locationId);

    /**
     * Find batches by supplier
     */
    List<Batch> findByTenantIdAndSupplierId(String tenantId, String supplierId);

    /**
     * Find all batches for a tenant
     */
    List<Batch> findByTenantId(String tenantId);

    /**
     * Check if batch number exists for tenant
     */
    boolean existsByTenantIdAndBatchNumber(String tenantId, String batchNumber);
}
