package com.gogidix.hr.globalcompliance.domain.repository;

import com.gogidix.hr.globalcompliance.domain.model.ComplianceCheck;

import java.time.LocalDate;
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

    Optional<ComplianceCheck> findByCheckNumberAndTenantId(String checkNumber, String tenantId);

    List<ComplianceCheck> findByTenantId(String tenantId);

    List<ComplianceCheck> findByTenantIdAndRequirementId(String tenantId, String requirementId);

    List<ComplianceCheck> findByTenantIdAndCountryCode(String tenantId, String countryCode);

    List<ComplianceCheck> findByTenantIdAndStatus(String tenantId, String status);

    List<ComplianceCheck> findByTenantIdAndResult(String tenantId, String result);

    List<ComplianceCheck> findByTenantIdAndCheckedBy(String tenantId, String checkedBy);

    List<ComplianceCheck> findByTenantIdAndScheduledDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<ComplianceCheck> findByTenantIdAndCompletedDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<ComplianceCheck> findByTenantIdAndScheduledDateBeforeAndStatus(String tenantId, LocalDate date, String status);

    List<ComplianceCheck> findOverdueChecks(String tenantId);

    List<ComplianceCheck> findUpcomingChecks(String tenantId, LocalDate fromDate, LocalDate toDate);

    List<ComplianceCheck> findPendingChecks(String tenantId);

    List<ComplianceCheck> findCompletedChecks(String tenantId);

    List<ComplianceCheck> findFailedChecks(String tenantId);

    List<ComplianceCheck> findChecksByRequirementAndDateRange(String tenantId, String requirementId, LocalDate startDate, LocalDate endDate);

    List<ComplianceCheck> findByTenantIdAndFrequency(String tenantId, String frequency);

    boolean existsByCheckNumberAndTenantId(String checkNumber, String tenantId);

    boolean existsByCheckIdAndTenantId(String checkId, String tenantId);

    void deleteById(String id);

    void deleteByCheckIdAndTenantId(String checkId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    void deleteByRequirementId(String requirementId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, String status);

    long countByTenantIdAndResult(String tenantId, String result);

    long countByTenantIdAndRequirementId(String tenantId, String requirementId);

    long countOverdueChecks(String tenantId);

    List<ComplianceCheck> searchByFindings(String tenantId, String searchTerm);
}
