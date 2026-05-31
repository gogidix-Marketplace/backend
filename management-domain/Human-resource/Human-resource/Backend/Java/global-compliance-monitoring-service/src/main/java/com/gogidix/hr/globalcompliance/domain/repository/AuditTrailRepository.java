package com.gogidix.hr.globalcompliance.domain.repository;

import com.gogidix.hr.globalcompliance.domain.model.AuditTrail;

import java.time.LocalDate;
import java.util.List;

/**
 * Audit Trail Repository Interface (Port)
 * Defines the contract for audit trail persistence operations
 */
public interface AuditTrailRepository {

    AuditTrail save(AuditTrail auditTrail);

    List<AuditTrail> saveAll(List<AuditTrail> auditTrails);

    List<AuditTrail> findByTenantId(String tenantId);

    List<AuditTrail> findByTenantIdAndRequirementId(String tenantId, String requirementId);

    List<AuditTrail> findByTenantIdAndCheckId(String tenantId, String checkId);

    List<AuditTrail> findByTenantIdAndIssueId(String tenantId, String issueId);

    List<AuditTrail> findByTenantIdAndReportId(String tenantId, String reportId);

    List<AuditTrail> findByTenantIdAndAction(String tenantId, String action);

    List<AuditTrail> findByTenantIdAndActionedBy(String tenantId, String actionedBy);

    List<AuditTrail> findByTenantIdAndEntityTypeAndEntityId(String tenantId, String entityType, String entityId);

    List<AuditTrail> findByTenantIdAndActionDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<AuditTrail> findByTenantIdAndCountryCode(String tenantId, String countryCode);

    List<AuditTrail> findByTenantIdAndDepartment(String tenantId, String department);

    List<AuditTrail> findRecentAudits(String tenantId, int limit);

    List<AuditTrail> findCriticalAudits(String tenantId);

    List<AuditTrail> findByTenantIdAndIpAddress(String tenantId, String ipAddress);

    void deleteById(String id);

    void deleteAllByTenantId(String tenantId);

    void deleteByRequirementId(String requirementId);

    void deleteByCheckId(String checkId);

    void deleteByIssueId(String issueId);

    void deleteByReportId(String reportId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndAction(String tenantId, String action);

    long countByTenantIdAndActionedBy(String tenantId, String actionedBy);

    List<AuditTrail> findAuditsByEntity(String tenantId, String entityType, String entityId);

    List<AuditTrail> searchByReason(String tenantId, String searchTerm);
}
