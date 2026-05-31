package com.gogidix.shared.courier.pricing.domain.repository;

import com.gogidix.shared.courier.pricing.domain.entity.PricingRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Pricing Rule Entity
 * Provides data access for pricing rules
 */
@Repository
public interface PricingRuleRepository extends MongoRepository<PricingRule, String> {

    /**
     * Find pricing rule by tenant and rule ID
     */
    Optional<PricingRule> findByTenantIdAndRuleId(String tenantId, String ruleId);

    /**
     * Find all pricing rules by tenant ID
     */
    Page<PricingRule> findByTenantId(String tenantId, Pageable pageable);

    /**
     * Find all pricing rules by tenant ID (without pagination)
     */
    List<PricingRule> findByTenantId(String tenantId);

    /**
     * Find active pricing rules by tenant, service type, and vehicle type
     */
    List<PricingRule> findByTenantIdAndActiveTrueAndServiceTypeAndVehicleType(
            String tenantId, String serviceType, String vehicleType);

    /**
     * Find all active pricing rules for tenant
     */
    List<PricingRule> findByTenantIdAndActiveTrue(String tenantId);

    /**
     * Find pricing rules by rule type
     */
    List<PricingRule> findByTenantIdAndRuleType(String tenantId, String ruleType);

    /**
     * Find pricing rules by priority range
     */
    List<PricingRule> findByTenantIdAndPriorityBetweenOrderByPriorityAsc(
            String tenantId, Integer minPriority, Integer maxPriority);

    /**
     * Check if rule exists
     */
    boolean existsByTenantIdAndRuleId(String tenantId, String ruleId);
}
