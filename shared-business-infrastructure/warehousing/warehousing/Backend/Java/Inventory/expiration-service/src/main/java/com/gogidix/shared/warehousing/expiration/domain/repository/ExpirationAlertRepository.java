package com.gogidix.shared.warehousing.expiration.domain.repository;

import com.gogidix.shared.warehousing.expiration.domain.entity.ExpirationAlert;
import com.gogidix.shared.warehousing.expiration.domain.entity.ExpirationAlert.AlertSeverity;
import com.gogidix.shared.warehousing.expiration.domain.entity.ExpirationAlert.AlertStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Expiration Alert Repository with Multi-Tenant Support
 */
@Repository
public interface ExpirationAlertRepository extends MongoRepository<ExpirationAlert, String> {

    /**
     * Find alerts by SKU and tenant
     */
    List<ExpirationAlert> findByTenantIdAndSku(String tenantId, String sku);

    /**
     * Find alerts by severity and tenant
     */
    List<ExpirationAlert> findByTenantIdAndSeverity(String tenantId, AlertSeverity severity);

    /**
     * Find alerts by status and tenant
     */
    List<ExpirationAlert> findByTenantIdAndStatus(String tenantId, AlertStatus status);

    /**
     * Find alerts by expiry date range
     */
    List<ExpirationAlert> findByTenantIdAndExpiryDateBetween(
        String tenantId, LocalDate startDate, LocalDate endDate);

    /**
     * Find alerts expiring before date
     */
    List<ExpirationAlert> findByTenantIdAndExpiryDateBefore(String tenantId, LocalDate date);

    /**
     * Find alerts where notification not sent
     */
    List<ExpirationAlert> findByTenantIdAndNotificationSentFalse(String tenantId);

    /**
     * Find alerts by batch/lot ID
     */
    List<ExpirationAlert> findByBatchLotId(String batchLotId);

    /**
     * Find all alerts for a tenant
     */
    List<ExpirationAlert> findByTenantId(String tenantId);

    /**
     * Delete alerts by batch/lot ID
    */
    void deleteByBatchLotId(String batchLotId);

    /**
     * Count alerts by severity and tenant
     */
    long countByTenantIdAndSeverity(String tenantId, AlertSeverity severity);
}
