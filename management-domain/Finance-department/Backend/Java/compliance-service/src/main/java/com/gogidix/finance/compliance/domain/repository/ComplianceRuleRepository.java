package com.gogidix.finance.compliance.domain.repository;

import com.gogidix.finance.compliance.domain.model.ComplianceRule;

import java.util.List;
import java.util.Optional;

/**
 * Compliance Rule Repository Interface (Port)
 * Defines the contract for compliance rule persistence operations
 */
public interface ComplianceRuleRepository {

    ComplianceRule save(ComplianceRule rule);

    List<ComplianceRule> saveAll(List<ComplianceRule> rules);

    Optional<ComplianceRule> findById(String id);

    Optional<ComplianceRule> findByRuleIdAndTenantId(String ruleId, String tenantId);

    List<ComplianceRule> findByTenantId(String tenantId);

    List<ComplianceRule> findByTenantIdAndStatus(String tenantId, ComplianceRule.RuleStatus status);

    List<ComplianceRule> findByTenantIdAndEnabled(String tenantId, Boolean enabled);

    List<ComplianceRule> findByTenantIdAndRuleType(String tenantId, ComplianceRule.RuleType ruleType);

    List<ComplianceRule> findByTenantIdAndCategory(String tenantId, ComplianceRule.RuleCategory category);

    List<ComplianceRule> findActiveByTenantId(String tenantId);

    List<ComplianceRule> findByTenantIdAndApplicableDepartmentsContaining(String tenantId, String department);

    List<ComplianceRule> findByTenantIdAndApplicableExpenseCategoriesContaining(String tenantId, String category);

    List<ComplianceRule> findByTenantIdAndTagsContaining(String tenantId, String tag);

    boolean existsByRuleIdAndTenantId(String ruleId, String tenantId);

    void deleteById(String id);

    void deleteByRuleIdAndTenantId(String ruleId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, ComplianceRule.RuleStatus status);

    List<ComplianceRule> searchByNameContaining(String tenantId, String searchTerm);
}
