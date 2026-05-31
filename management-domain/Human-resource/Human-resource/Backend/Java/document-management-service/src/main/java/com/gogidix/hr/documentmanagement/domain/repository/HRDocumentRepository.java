package com.gogidix.hr.documentmanagement.domain.repository;

import com.gogidix.hr.documentmanagement.domain.model.HRDocument;
import com.gogidix.hr.documentmanagement.domain.model.DocumentCategory;
import com.gogidix.hr.documentmanagement.domain.model.DocumentStatus;
import com.gogidix.hr.documentmanagement.domain.model.DocumentType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * HR Document Repository Interface (Port)
 * Defines the contract for document persistence operations
 */
public interface HRDocumentRepository {

    // Basic CRUD operations
    HRDocument save(HRDocument document);
    List<HRDocument> saveAll(List<HRDocument> documents);
    Optional<HRDocument> findById(String id);
    void deleteById(String id);
    void deleteByDocumentIdAndTenantId(String documentId, String tenantId);

    // Find by tenant and document ID
    Optional<HRDocument> findByDocumentIdAndTenantId(String documentId, String tenantId);
    boolean existsByDocumentIdAndTenantId(String documentId, String tenantId);

    // Find by various criteria
    List<HRDocument> findByTenantId(String tenantId);
    List<HRDocument> findByTenantIdAndStatus(String tenantId, DocumentStatus status);
    List<HRDocument> findByTenantIdAndDocumentType(String tenantId, DocumentType documentType);
    List<HRDocument> findByTenantIdAndCategory(String tenantId, DocumentCategory category);
    List<HRDocument> findByTenantIdAndCountryCode(String tenantId, String countryCode);
    List<HRDocument> findByTenantIdAndEmployeeId(String tenantId, String employeeId);
    List<HRDocument> findByTenantIdAndDepartment(String tenantId, String department);

    // Find by multiple criteria
    List<HRDocument> findByTenantIdAndDocumentTypeAndCategory(String tenantId, DocumentType documentType, DocumentCategory category);
    List<HRDocument> findByTenantIdAndStatusIn(String tenantId, List<DocumentStatus> statuses);

    // Search operations
    List<HRDocument> searchByDocumentName(String tenantId, String searchTerm);
    List<HRDocument> searchByDescription(String tenantId, String searchTerm);
    List<HRDocument> searchByEmployeeName(String tenantId, String employeeName);
    List<HRDocument> findByTenantIdAndTagsContaining(String tenantId, String tag);

    // Date-based queries
    List<HRDocument> findByTenantIdAndIssueDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);
    List<HRDocument> findByTenantIdAndExpiryDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    // Expiring documents
    List<HRDocument> findExpiringDocuments(String tenantId, LocalDate beforeDate);
    List<HRDocument> findExpiredDocuments(String tenantId);
    List<HRDocument> findExpiringWithinDays(String tenantId, int days);

    // User-specific queries
    List<HRDocument> findByTenantIdAndUploadedBy(String tenantId, String uploadedBy);
    List<HRDocument> findByTenantIdAndApprovedBy(String tenantId, String approvedBy);

    // Version queries
    List<HRDocument> findByTenantIdAndParentDocumentId(String tenantId, String parentDocumentId);
    Optional<HRDocument> findByTenantIdAndDocumentIdAndVersion(String tenantId, String documentId, Integer version);

    // Confidential documents
    List<HRDocument> findByTenantIdAndIsConfidential(String tenantId, Boolean isConfidential);

    // Count operations
    long countByTenantId(String tenantId);
    long countByTenantIdAndStatus(String tenantId, DocumentStatus status);
    long countByTenantIdAndDocumentType(String tenantId, DocumentType documentType);
    long countByTenantIdAndCategory(String tenantId, DocumentCategory category);
    long countByTenantIdAndEmployeeId(String tenantId, String employeeId);

    // Storage queries
    List<HRDocument> findByTenantIdAndStorageProvider(String tenantId, String storageProvider);

    // Bulk operations
    void deleteAllByTenantId(String tenantId);
    List<HRDocument> findAllByTenantId(String tenantId, int page, int size);
}
