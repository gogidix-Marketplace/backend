package com.gogidix.hr.documentmanagement.domain.repository;

import com.gogidix.hr.documentmanagement.domain.model.DocumentAccessLog;
import com.gogidix.hr.documentmanagement.domain.model.AccessAction;

import java.time.LocalDate;
import java.util.List;

/**
 * Document Access Log Repository Interface (Port)
 * Defines the contract for access log persistence operations
 */
public interface DocumentAccessLogRepository {

    // Basic CRUD operations
    DocumentAccessLog save(DocumentAccessLog log);
    List<DocumentAccessLog> saveAll(List<DocumentAccessLog> logs);
    void deleteById(String id);
    void deleteByLogIdAndTenantId(String logId, String tenantId);

    // Find by tenant and log ID
    DocumentAccessLog findByLogIdAndTenantId(String logId, String tenantId);

    // Find by document
    List<DocumentAccessLog> findByTenantIdAndDocumentId(String tenantId, String documentId);
    List<DocumentAccessLog> findByTenantIdAndDocumentIdOrderByAccessTimestampDesc(String tenantId, String documentId);

    // Find by user
    List<DocumentAccessLog> findByTenantIdAndAccessedBy(String tenantId, String accessedBy);
    List<DocumentAccessLog> findByTenantIdAndAccessedByOrderByAccessTimestampDesc(String tenantId, String accessedBy);

    // Find by action
    List<DocumentAccessLog> findByTenantIdAndAction(String tenantId, AccessAction action);

    // Find by date range
    List<DocumentAccessLog> findByTenantIdAndAccessDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    // Find unauthorized access attempts
    List<DocumentAccessLog> findByTenantIdAndAuthorized(String tenantId, Boolean authorized);

    // Find by IP address
    List<DocumentAccessLog> findByTenantIdAndIpAddress(String tenantId, String ipAddress);

    // Find sensitive actions (delete, share, download)
    List<DocumentAccessLog> findSensitiveActionsByTenantId(String tenantId);

    // Count operations
    long countByTenantId(String tenantId);
    long countByTenantIdAndDocumentId(String tenantId, String documentId);
    long countByTenantIdAndAccessedBy(String tenantId, String accessedBy);
    long countByTenantIdAndAction(String tenantId, AccessAction action);
    long countByTenantIdAndAccessDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    // Aggregate operations
    long countUniqueDocumentsAccessed(String tenantId, LocalDate startDate, LocalDate endDate);
    long countUniqueUsersAccessedDocuments(String tenantId, LocalDate startDate, LocalDate endDate);

    // Bulk operations
    void deleteAllByTenantId(String tenantId);
    List<DocumentAccessLog> findAllByTenantId(String tenantId, int page, int size);

    // Cleanup operations
    void deleteOlderThan(String tenantId, LocalDate date);
    void deleteByDocumentIdAndTenantId(String documentId, String tenantId);
}
