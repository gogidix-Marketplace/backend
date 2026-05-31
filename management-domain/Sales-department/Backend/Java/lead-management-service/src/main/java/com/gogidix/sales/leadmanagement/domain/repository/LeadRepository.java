package com.gogidix.sales.leadmanagement.domain.repository;

import com.gogidix.sales.leadmanagement.domain.model.Lead;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Lead Repository Interface
 * Defines the contract for lead persistence operations
 */
public interface LeadRepository {

    Lead save(Lead lead);

    Optional<Lead> findById(String id);

    Optional<Lead> findByLeadIdAndTenantId(String leadId, String tenantId);

    List<Lead> findAllByTenantId(String tenantId);

    List<Lead> findByTenantIdAndStatus(String tenantId, Lead.LeadStatus status);

    List<Lead> findByTenantIdAndStage(String tenantId, Lead.LeadStage stage);

    List<Lead> findByOwnerIdAndTenantId(String ownerId, String tenantId);

    List<Lead> findByTenantIdAndSource(String tenantId, Lead.LeadSource source);

    List<Lead> findByTenantIdAndQuality(String tenantId, Lead.LeadQuality quality);

    List<Lead> findByTenantIdAndScoreGreaterThanEqual(String tenantId, Integer minScore);

    List<Lead> findByTenantIdAndEmail(String tenantId, String email);

    List<Lead> findByTenantIdAndPhone(String tenantId, String phone);

    List<Lead> findByTenantIdAndCompany(String tenantId, String company);

    List<Lead> findByTenantIdAndLastActivityDateAfter(String tenantId, LocalDate date);

    List<Lead> findDuplicateLeads(String tenantId, String email, String phone, String firstName, String lastName);

    void deleteById(String id);

    void deleteByLeadIdAndTenantId(String leadId, String tenantId);

    boolean existsByLeadIdAndTenantId(String leadId, String tenantId);

    long countByTenantIdAndStatus(String tenantId, Lead.LeadStatus status);

    long countByTenantIdAndStage(String tenantId, Lead.LeadStage stage);

    long countByOwnerIdAndTenantId(String ownerId, String tenantId);

    /**
     * Find leads that need follow-up (no recent activity)
     */
    List<Lead> findLeadsNeedingFollowUp(String tenantId, LocalDate since);

    /**
     * Search leads by term
     */
    List<Lead> searchLeads(String tenantId, String searchTerm);

    /**
     * Get leads by tag
     */
    List<Lead> findByTenantIdAndTagListContaining(String tenantId, String tag);

    /**
     * Get stale leads (not contacted in X days)
     */
    List<Lead> findStaleLeads(String tenantId, int staleDays);
}
