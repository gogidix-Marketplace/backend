package com.gogidix.shared.warehousing.serialization.domain.repository;

import com.gogidix.shared.warehousing.serialization.domain.entity.SerializedItem;
import com.gogidix.shared.warehousing.serialization.domain.entity.SerializedItem.SerializedItemStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Serialized Item Repository with Multi-Tenant Support
 *
 * MongoDB-based repository for serialized item management
 */
@Repository
public interface SerializedItemRepository extends MongoRepository<SerializedItem, String> {

    /**
     * Find serialized item by serial number and tenant
     */
    Optional<SerializedItem> findByTenantIdAndSerialNumber(String tenantId, String serialNumber);

    /**
     * Find serialized items by SKU and tenant
     */
    List<SerializedItem> findByTenantIdAndSku(String tenantId, String sku);

    /**
     * Find serialized items by status and tenant
     */
    List<SerializedItem> findByTenantIdAndStatus(String tenantId, SerializedItemStatus status);

    /**
     * Find serialized items by batch ID
     */
    List<SerializedItem> findByBatchId(String batchId);

    /**
     * Find serialized items by tenant and location
     */
    List<SerializedItem> findByTenantIdAndLocationId(String tenantId, String locationId);

    /**
     * Find serialized items by SKU, status, and tenant
     */
    List<SerializedItem> findByTenantIdAndSkuAndStatus(String tenantId, String sku, SerializedItemStatus status);

    /**
     * Find serialized items expiring before a date
     */
    List<SerializedItem> findByTenantIdAndExpiryDateBefore(String tenantId, LocalDate date);

    /**
     * Find serialized items with expiry dates within a range
     */
    List<SerializedItem> findByTenantIdAndExpiryDateBetween(
        String tenantId, LocalDate startDate, LocalDate endDate);

    /**
     * Count serialized items by batch ID
     */
    long countByBatchId(String batchId);

    /**
     * Find all serialized items for a tenant
     */
    List<SerializedItem> findByTenantId(String tenantId);

    /**
     * Check if serial number exists for tenant
     */
    boolean existsByTenantIdAndSerialNumber(String tenantId, String serialNumber);
}
