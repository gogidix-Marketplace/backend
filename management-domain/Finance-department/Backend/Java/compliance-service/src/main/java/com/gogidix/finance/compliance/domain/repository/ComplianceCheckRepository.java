package com.gogidix.finance.compliance.domain.repository;

import com.gogidix.finance.compliance.domain.model.ComplianceCheck;

import java.time.LocalDate;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Compliance Check Repository Interface (Port)
 * Defines the contract for compliance check persistence operations
 */
public interface ComplianceCheckRepository {

    ComplianceCheck save(ComplianceCheck check);

    List<ComplianceCheck> saveAll(List<ComplianceCheck> checks);

    Optional<ComplianceCheck> findById(String id);

    Optional<ComplianceCheck> findByCheckIdAndTenantId(String checkId, String tenantId);

    List<ComplianceCheck> findByTenantId(String tenantId);

    List<ComplianceCheck> findByTenantIdAndRuleId(String tenantId, String ruleId);

    List<ComplianceCheck> findByTenantIdAndEntityTypeAndEntityId(String tenantId, String entityType, String entityId);

    List<ComplianceCheck> findByTenantIdAndResult(String tenantId, ComplianceCheck.CheckResult result);

    List<ComplianceCheck> findByTenantIdAndSeverity(String tenantId, ComplianceCheck.SeverityLevel severity);

    List<ComplianceCheck> findByTenantIdAndStatus(String tenantId, ComplianceCheck.CheckStatus status);

    List<ComplianceCheck> findByTenantIdAndEvaluatedAtBetween(String tenantId, Instant startDate, Instant endDate);

    List<ComplianceCheck> findViolationsByTenantId(String tenantId);

    List<ComplianceCheck> findViolationsByTenantIdAndSeverityGreaterThanEqual(String tenantId, ComplianceCheck.SeverityLevel severity);

    List<ComplianceCheck> findPendingRemediationByTenantId(String tenantId);

    List<ComplianceCheck> findPendingRemediationByTenantIdAndAssignedTo(String tenantId, String assignedTo);

    List<ComplianceCheck> findByTenantIdAndDepartment(String tenantId, String department);

    List<ComplianceCheck> findByTenantIdAndCostCenter(String tenantId, String costCenter);

    boolean existsByCheckIdAndTenantId(String checkId, String tenantId);

    void deleteById(String id);

    void deleteByCheckIdAndTenantId(String checkId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    void deleteByTenantIdAndEvaluatedAtBefore(String tenantId, Instant cutoffDate);

    long countByTenantId(String tenantId);

    long countByTenantIdAndResult(String tenantId, ComplianceCheck.CheckResult result);

    long countByTenantIdAndSeverity(String tenantId, ComplianceCheck.SeverityLevel severity);

    long countViolationsByTenantId(String tenantId);

    long countPendingRemediationByTenantId(String tenantId);
}
