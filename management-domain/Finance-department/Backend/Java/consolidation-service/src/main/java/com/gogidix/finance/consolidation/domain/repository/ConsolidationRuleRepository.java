package com.gogidix.finance.consolidation.domain.repository;

import com.gogidix.finance.consolidation.domain.model.ConsolidationRule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Consolidation Rule Repository Interface (Port)
 * Defines the contract for consolidation rule persistence operations
 */
public interface ConsolidationRuleRepository {

    ConsolidationRule save(ConsolidationRule rule);

    List<ConsolidationRule> saveAll(List<ConsolidationRule> rules);

    Optional<ConsolidationRule> findById(String id);

    Optional<ConsolidationRule> findByRuleIdAndTenantId(String ruleId, String tenantId);

    List<ConsolidationRule> findByTenantId(String tenantId);

    List<ConsolidationRule> findByTenantIdAndActive(String tenantId, Boolean active);

    List<ConsolidationRule> findByTenantIdAndRuleType(String tenantId, ConsolidationRule.RuleType ruleType);

    List<ConsolidationRule> findByTenantIdAndRuleScope(String tenantId, ConsolidationRule.RuleScope ruleScope);

    List<ConsolidationRule> findActiveByTenantIdAndEffectiveDate(String tenantId, LocalDate effectiveDate);

    List<ConsolidationRule> findByTenantIdAndRuleTypeAndActive(String tenantId,
                                                                ConsolidationRule.RuleType ruleType,
                                                                Boolean active);

    List<ConsolidationRule> findByTenantIdAndApplicableDepartmentsContaining(String tenantId,
                                                                               String departmentId);

    List<ConsolidationRule> findByTenantIdAndApplicableCostCentersContaining(String tenantId,
                                                                              String costCenterId);

    List<ConsolidationRule> findByTenantIdAndApplicableSubsidiariesContaining(String tenantId,
                                                                                String subsidiaryId);

    boolean existsByRuleIdAndTenantId(String ruleId, String tenantId);

    void deleteById(String id);

    void deleteByRuleIdAndTenantId(String ruleId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndActive(String tenantId, Boolean active);

    List<ConsolidationRule> searchByTenantIdAndRuleNameContaining(String tenantId, String searchTerm);
}
