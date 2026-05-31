package com.gogidix.sales.onboarding.domain.repository;

import com.gogidix.sales.onboarding.domain.model.DocumentChecklist;

import java.util.List;
import java.util.Optional;

/**
 * Document Checklist Repository Interface (Port)
 * Defines the contract for document checklist persistence operations
 */
public interface DocumentChecklistRepository {

    DocumentChecklist save(DocumentChecklist checklist);

    List<DocumentChecklist> saveAll(List<DocumentChecklist> checklists);

    Optional<DocumentChecklist> findById(String id);

    Optional<DocumentChecklist> findByChecklistIdAndTenantId(String checklistId, String tenantId);

    Optional<DocumentChecklist> findByOnboardingId(String onboardingId);

    List<DocumentChecklist> findByTenantId(String tenantId);

    List<DocumentChecklist> findByTenantIdAndCustomerId(String tenantId, String customerId);

    List<DocumentChecklist> findByOnboardingIdAndTenantId(String onboardingId, String tenantId);

    List<DocumentChecklist> findPendingVerification(String tenantId);

    List<DocumentChecklist> findCompleted(String tenantId);

    boolean existsByChecklistIdAndTenantId(String checklistId, String tenantId);

    void deleteById(String id);

    void deleteByChecklistIdAndTenantId(String checklistId, String tenantId);

    void deleteByOnboardingId(String onboardingId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndCompleted(String tenantId, Boolean completed);
}
