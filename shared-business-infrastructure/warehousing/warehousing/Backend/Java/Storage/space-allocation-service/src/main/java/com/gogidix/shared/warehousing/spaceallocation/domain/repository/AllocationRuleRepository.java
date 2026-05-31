package com.gogidix.shared.warehousing.spaceallocation.domain.repository;

import com.gogidix.shared.warehousing.spaceallocation.domain.entity.AllocationRule;
import com.gogidix.shared.warehousing.spaceallocation.domain.entity.AllocationRule.RuleType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Allocation Rule Repository
 */
@Repository
public interface AllocationRuleRepository extends MongoRepository<AllocationRule, String> {

    /**
     * Find active rules by tenant and warehouse
     */
    List<AllocationRule> findByTenantIdAndWarehouseIdAndActiveTrueOrderByPriorityDesc(
            String tenantId, String warehouseId);

    /**
     * Find rules by type
     */
    List<AllocationRule> findByTenantIdAndRuleTypeAndActiveTrueOrderByPriorityDesc(
            String tenantId, RuleType ruleType);

    /**
     * Find default rules for tenant
     */
    List<AllocationRule> findByTenantIdAndWarehouseIdIsNullAndActiveTrueOrderByPriorityDesc(
            String tenantId);

    /**
     * Find rules by item type
     */
    List<AllocationRule> findByTenantIdAndAppliesToItemTypeAndActiveTrue(
            String tenantId, String itemType);
}
