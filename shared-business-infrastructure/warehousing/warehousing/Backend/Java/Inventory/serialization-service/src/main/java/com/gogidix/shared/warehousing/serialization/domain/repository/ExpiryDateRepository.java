package com.gogidix.shared.warehousing.serialization.domain.repository;

import com.gogidix.shared.warehousing.serialization.domain.entity.ExpiryDate;
import com.gogidix.shared.warehousing.serialization.domain.entity.ExpiryDate.AlertStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Expiry Date Repository with Multi-Tenant Support
 *
 * MongoDB-based repository for expiry date tracking
 */
@Repository
public interface ExpiryDateRepository extends MongoRepository<ExpiryDate, String> {

    /**
     * Find expiry dates by SKU and tenant
     */
    List<ExpiryDate> findByTenantIdAndSku(String tenantId, String sku);

    /**
     * Find expiry dates by alert status and tenant
     */
    List<ExpiryDate> findByTenantIdAndAlertStatus(String tenantId, AlertStatus alertStatus);

    /**
     * Find expiry dates by batch ID
     */
    List<ExpiryDate> findByBatchId(String batchId);

    /**
     * Find expiry dates by serialized item ID
     */
    List<ExpiryDate> findBySerializedItemId(String serializedItemId);

    /**
     * Find expiry dates expiring before a date
     */
    List<ExpiryDate> findByTenantIdAndExpiryDateBefore(String tenantId, LocalDate date);

    /**
     * Find expiry dates within a date range
     */
    List<ExpiryDate> findByTenantIdAndExpiryDateBetween(
        String tenantId, LocalDate startDate, LocalDate endDate);

    /**
     * Find expiry dates where notification not sent
     */
    List<ExpiryDate> findByTenantIdAndNotificationSentFalse(String tenantId);

    /**
     * Find all expiry dates for a tenant
     */
    List<ExpiryDate> findByTenantId(String tenantId);

    /**
     * Delete expiry dates by batch ID
     */
    void deleteByBatchId(String batchId);
}
