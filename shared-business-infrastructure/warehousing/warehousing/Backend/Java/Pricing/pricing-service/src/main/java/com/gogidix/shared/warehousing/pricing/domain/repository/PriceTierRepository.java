package com.gogidix.shared.warehousing.pricing.domain.repository;

import com.gogidix.shared.warehousing.pricing.domain.entity.PriceTier;
import com.gogidix.shared.warehousing.pricing.domain.entity.PriceTier.TierType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Price Tier Repository
 *
 * MongoDB repository for price tiers with multi-tenant support
 */
@Repository
public interface PriceTierRepository extends MongoRepository<PriceTier, String> {

    /**
     * Find all active tiers for tenant and pricing rule
     */
    List<PriceTier> findByTenantIdAndPricingRuleIdAndActiveTrueOrderByPriorityAsc(
            String tenantId, String pricingRuleId);

    /**
     * Find applicable tier for quantity
     */
    @Query("{'tenantId': ?0, 'pricingRuleId': ?1, 'active': true, 'minQuantity': {'$lte': ?2}, '$or': [{'maxQuantity': null}, {'maxQuantity': {'$gte': ?2}}]}")
    List<PriceTier> findApplicableTiersForQuantity(
            String tenantId, String pricingRuleId, Integer quantity);

    /**
     * Find tiers by tenant and tier type
     */
    List<PriceTier> findByTenantIdAndTierTypeAndActiveTrueOrderByMinQuantityAsc(
            String tenantId, TierType tierType);

    /**
     * Find all active tiers for tenant
     */
    List<PriceTier> findByTenantIdAndActiveTrueOrderByPriorityAsc(String tenantId);
}
