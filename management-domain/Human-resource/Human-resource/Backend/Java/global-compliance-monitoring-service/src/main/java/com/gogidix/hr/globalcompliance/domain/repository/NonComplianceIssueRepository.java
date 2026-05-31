package com.gogidix.hr.globalcompliance.domain.repository;

import com.gogidix.hr.globalcompliance.domain.model.NonComplianceIssue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Non-Compliance Issue Repository Interface (Port)
 * Defines the contract for non-compliance issue persistence operations
 */
public interface NonComplianceIssueRepository {

    NonComplianceIssue save(NonComplianceIssue issue);

    List<NonComplianceIssue> saveAll(List<NonComplianceIssue> issues);

    Optional<NonComplianceIssue> findById(String id);

    Optional<NonComplianceIssue> findByIssueIdAndTenantId(String issueId, String tenantId);

    Optional<NonComplianceIssue> findByIssueNumberAndTenantId(String issueNumber, String tenantId);

    List<NonComplianceIssue> findByTenantId(String tenantId);

    List<NonComplianceIssue> findByTenantIdAndRequirementId(String tenantId, String requirementId);

    List<NonComplianceIssue> findByTenantIdAndCheckId(String tenantId, String checkId);

    List<NonComplianceIssue> findByTenantIdAndCountryCode(String tenantId, String countryCode);

    List<NonComplianceIssue> findByTenantIdAndSeverity(String tenantId, String severity);

    List<NonComplianceIssue> findByTenantIdAndStatus(String tenantId, String status);

    List<NonComplianceIssue> findByTenantIdAndAssignedTo(String tenantId, String assignedTo);

    List<NonComplianceIssue> findByTenantIdAndDepartment(String tenantId, String department);

    List<NonComplianceIssue> findByTenantIdAndIdentifiedBy(String tenantId, String identifiedBy);

    List<NonComplianceIssue> findByTenantIdAndIdentifiedDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<NonComplianceIssue> findByTenantIdAndDueDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<NonComplianceIssue> findByTenantIdAndDueDateBeforeAndStatusNotIn(String tenantId, LocalDate date, List<String> statuses);

    List<NonComplianceIssue> findOpenIssues(String tenantId);

    List<NonComplianceIssue> findOverdueIssues(String tenantId);

    List<NonComplianceIssue> findCriticalIssues(String tenantId);

    List<NonComplianceIssue> findHighPriorityIssues(String tenantId);

    List<NonComplianceIssue> findIssuesByAffectedEmployee(String tenantId, String employeeId);

    List<NonComplianceIssue> findResolvedIssuesBetweenDates(String tenantId, LocalDate startDate, LocalDate endDate);

    List<NonComplianceIssue> findByTenantIdAndLocation(String tenantId, String location);

    List<NonComplianceIssue> searchByTitleOrDescription(String tenantId, String searchTerm);

    boolean existsByIssueNumberAndTenantId(String issueNumber, String tenantId);

    boolean existsByIssueIdAndTenantId(String issueId, String tenantId);

    void deleteById(String id);

    void deleteByIssueIdAndTenantId(String issueId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    void deleteByRequirementId(String requirementId);

    void deleteByCheckId(String checkId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, String status);

    long countByTenantIdAndSeverity(String tenantId, String severity);

    long countOpenIssues(String tenantId);

    long countOverdueIssues(String tenantId);

    List<NonComplianceIssue> findIssuesRequiringAttention(String tenantId);
}
