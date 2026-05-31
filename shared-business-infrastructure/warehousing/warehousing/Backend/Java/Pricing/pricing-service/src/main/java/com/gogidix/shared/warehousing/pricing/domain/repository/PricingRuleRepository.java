package com.gogidix.shared.warehousing.pricing.domain.repository;

import com.gogidix.shared.warehousing.pricing.domain.entity.PricingRule;
import com.gogidix.shared.warehousing.pricing.domain.entity.PricingRule.ServiceType;
import com.gogidix.shared.warehousing.pricing.domain.entity.PricingRule.StorageType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Pricing Rule Repository
 *
 * MongoDB repository for pricing rules with multi-tenant support
 */
@Repository
public interface PricingRuleRepository extends MongoRepository<PricingRule, String> {

    /**
     * Find all active pricing rules for tenant
     */
    List<PricingRule> findByTenantIdAndActiveTrue(String tenantId);

    /**
     * Find pricing rules by tenant, service type, and storage type
     */
    List<PricingRule> findByTenantIdAndServiceTypeAndStorageTypeAndActiveTrue(
            String tenantId, ServiceType serviceType, StorageType storageType);

    /**
     * Find pricing rules by tenant and warehouse
     */
    List<PricingRule> findByTenantIdAndWarehouseIdAndActiveTrue(
            String tenantId, String warehouseId);

    /**
     * Find pricing rules by tenant, service type
     */
    List<PricingRule> findByTenantIdAndServiceTypeAndActiveTrue(
            String tenantId, ServiceType serviceType);

    /**
     * Find default pricing rules (no specific warehouse) for tenant
     */
    @Query("{'tenantId': ?0, 'warehouseId': null, 'active': true}")
    List<PricingRule> findDefaultRulesForTenant(String tenantId);

    /**
     * Find specific rule for tenant by service and storage type
     */
    Optional<PricingRule> findByTenantIdAndServiceTypeAndStorageTypeAndWarehouseIdIsNullAndActiveTrue(
            String tenantId, ServiceType serviceType, StorageType storageType);

    /**
     * Count active pricing rules for tenant
     */
    long countByTenantIdAndActiveTrue(String tenantId);
}
