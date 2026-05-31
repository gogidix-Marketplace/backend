package com.gogidix.management.executive.financial.domain.repository;

import com.gogidix.management.executive.financial.domain.model.FinancialData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository for FinancialData entities
 */
@Repository
public interface FinancialDataRepository extends MongoRepository<FinancialData, String> {

    /**
     * Find strategy by tenant ID
     */
    Optional<FinancialData> findByTenantId(String tenantId);

    /**
     * Find all financials by tenant ID
     */
    List<FinancialData> findAllByTenantId(String tenantId);

    /**
     * Find all financials by tenant ID and not deleted
     */
    List<FinancialData> findAllByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Find financials by tenant ID and not deleted
     */
    List<FinancialData> findByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Find financials by tenant ID with pagination
     */
    Page<FinancialData> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Find financials by tenant ID and not deleted with pagination
     */
    Page<FinancialData> findByTenantIdAndDeletedAtIsNull(String tenantId, Pageable pageable);

    /**
     * Check if strategy exists by tenant ID
     */
    boolean existsByTenantId(String tenantId);

    /**
     * Check if strategy exists by tenant ID and not deleted
     */
    boolean existsByTenantIdAndDeletedAtIsNull(String tenantId);

    /**
     * Check if strategy exists by ID and tenant ID and not deleted
     */
    boolean existsByIdAndTenantIdAndDeletedAtIsNull(String id, String tenantId);

    /**
     * Find strategy by ID and tenant ID
     */
    Optional<FinancialData> findByIdAndTenantId(String id, String tenantId);

    /**
     * Find strategy by ID and tenant ID and not deleted
     */
    Optional<FinancialData> findByIdAndTenantIdAndDeletedAtIsNull(String id, String tenantId);

    /**
     * Find strategy by owner ID
     */
    Optional<FinancialData> findByOwnerId(String ownerId);

    /**
     * Find strategy by owner ID and not deleted
     */
    Optional<FinancialData> findByOwnerIdAndDeletedAtIsNull(String ownerId);

    /**
     * Count financials by tenant
     */
    long countByTenantId(String tenantId);

    /**
     * Count financials by tenant and not deleted
     */
    long countByTenantIdAndDeletedAtIsNull(String tenantId);
}
