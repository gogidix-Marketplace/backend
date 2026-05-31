package com.gogidix.hr.documentmanagement.domain.repository;

import com.gogidix.hr.documentmanagement.domain.model.DocumentTemplate;
import com.gogidix.hr.documentmanagement.domain.model.DocumentCategory;
import com.gogidix.hr.documentmanagement.domain.model.DocumentType;

import java.util.List;
import java.util.Optional;

/**
 * Document Template Repository Interface (Port)
 * Defines the contract for template persistence operations
 */
public interface DocumentTemplateRepository {

    // Basic CRUD operations
    DocumentTemplate save(DocumentTemplate template);
    List<DocumentTemplate> saveAll(List<DocumentTemplate> templates);
    Optional<DocumentTemplate> findById(String id);
    void deleteById(String id);
    void deleteByTemplateIdAndTenantId(String templateId, String tenantId);

    // Find by tenant and template ID
    Optional<DocumentTemplate> findByTemplateIdAndTenantId(String templateId, String tenantId);
    boolean existsByTemplateIdAndTenantId(String templateId, String tenantId);

    // Find by template code
    Optional<DocumentTemplate> findByTemplateCodeAndTenantId(String templateCode, String tenantId);
    boolean existsByTemplateCodeAndTenantId(String templateCode, String tenantId);

    // Find by various criteria
    List<DocumentTemplate> findByTenantId(String tenantId);
    List<DocumentTemplate> findByTenantIdAndActive(String tenantId, Boolean active);
    List<DocumentTemplate> findByTenantIdAndDocumentType(String tenantId, DocumentType documentType);
    List<DocumentTemplate> findByTenantIdAndCategory(String tenantId, DocumentCategory category);
    List<DocumentTemplate> findByTenantIdAndCountryCode(String tenantId, String countryCode);

    // Find by multiple criteria for template selection
    List<DocumentTemplate> findByTenantIdAndDocumentTypeAndCategoryAndCountryCodeAndActive(
            String tenantId, DocumentType documentType, DocumentCategory category, String countryCode, Boolean active);

    // Find active and effective templates
    List<DocumentTemplate> findActiveTemplates(String tenantId);
    List<DocumentTemplate> findEffectiveTemplates(String tenantId);

    // Find by version
    List<DocumentTemplate> findByTenantIdAndTemplateCodeOrderByVersionDesc(String tenantId, String templateCode);
    Optional<DocumentTemplate> findLatestVersionByTemplateCodeAndTenantId(String templateCode, String tenantId);

    // Count operations
    long countByTenantId(String tenantId);
    long countByTenantIdAndActive(String tenantId, Boolean active);
    long countByTenantIdAndDocumentType(String tenantId, DocumentType documentType);

    // Bulk operations
    void deleteAllByTenantId(String tenantId);
    List<DocumentTemplate> findAllByTenantId(String tenantId, int page, int size);

    // Search operations
    List<DocumentTemplate> searchByTemplateName(String tenantId, String searchTerm);
    List<DocumentTemplate> searchByDescription(String tenantId, String searchTerm);
}
