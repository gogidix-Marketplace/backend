package com.gogidix.hr.documentmanagement.domain.repository;

import com.gogidix.hr.documentmanagement.domain.model.DocumentRetention;
import com.gogidix.hr.documentmanagement.domain.model.DocumentCategory;
import com.gogidix.hr.documentmanagement.domain.model.DocumentType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Document Retention Repository Interface (Port)
 * Defines the contract for retention policy persistence operations
 */
public interface DocumentRetentionRepository {

    // Basic CRUD operations
    DocumentRetention save(DocumentRetention retention);
    List<DocumentRetention> saveAll(List<DocumentRetention> retentions);
    Optional<DocumentRetention> findById(String id);
    void deleteById(String id);
    void deleteByRetentionIdAndTenantId(String retentionId, String tenantId);

    // Find by tenant and retention ID
    Optional<DocumentRetention> findByRetentionIdAndTenantId(String retentionId, String tenantId);
    boolean existsByRetentionIdAndTenantId(String retentionId, String tenantId);

    // Find by various criteria
    List<DocumentRetention> findByTenantId(String tenantId);
    List<DocumentRetention> findByTenantIdAndActive(String tenantId, Boolean active);
    List<DocumentRetention> findByTenantIdAndDocumentType(String tenantId, DocumentType documentType);
    List<DocumentRetention> findByTenantIdAndCategory(String tenantId, DocumentCategory category);
    List<DocumentRetention> findByTenantIdAndCountryCode(String tenantId, String countryCode);

    // Find matching policies for document
    List<DocumentRetention> findMatchingPolicies(String tenantId, DocumentType documentType,
                                                DocumentCategory category, String countryCode);

    // Find active and effective policies
    List<DocumentRetention> findActivePolicies(String tenantId);
    List<DocumentRetention> findEffectivePolicies(String tenantId);

    // Find policies with legal holds
    List<DocumentRetention> findByTenantIdAndLegalHoldIsNotNull(String tenantId);

    // Find policies requiring review
    List<DocumentRetention> findByTenantIdAndLastReviewDateBefore(String tenantId, LocalDate date);
    List<DocumentRetention> findPoliciesRequiringReview(String tenantId);

    // Find by retention action
    List<DocumentRetention> findByTenantIdAndAction(String tenantId, String action);

    // Find by retention period
    List<DocumentRetention> findByTenantIdAndRetentionPeriodYears(String tenantId, Integer years);

    // Count operations
    long countByTenantId(String tenantId);
    long countByTenantIdAndActive(String tenantId, Boolean active);
    long countByTenantIdAndDocumentType(String tenantId, DocumentType documentType);
    long countByTenantIdAndLegalHoldIsNotNull(String tenantId);

    // Bulk operations
    void deleteAllByTenantId(String tenantId);
    List<DocumentRetention> findAllByTenantId(String tenantId, int page, int size);

    // Search operations
    List<DocumentRetention> searchByComplianceRequirement(String tenantId, String searchTerm);
}
