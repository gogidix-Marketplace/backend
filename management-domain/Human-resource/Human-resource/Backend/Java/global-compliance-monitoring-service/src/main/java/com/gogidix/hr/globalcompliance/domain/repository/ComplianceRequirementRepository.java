package com.gogidix.hr.globalcompliance.domain.repository;

import com.gogidix.hr.globalcompliance.domain.model.ComplianceRequirement;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Compliance Requirement Repository Interface (Port)
 * Defines the contract for compliance requirement persistence operations
 */
public interface ComplianceRequirementRepository {

    ComplianceRequirement save(ComplianceRequirement requirement);

    List<ComplianceRequirement> saveAll(List<ComplianceRequirement> requirements);

    Optional<ComplianceRequirement> findById(String id);

    Optional<ComplianceRequirement> findByRequirementIdAndTenantId(String requirementId, String tenantId);

    Optional<ComplianceRequirement> findByRequirementCodeAndTenantId(String requirementCode, String tenantId);

    List<ComplianceRequirement> findByTenantId(String tenantId);

    List<ComplianceRequirement> findByTenantIdAndCountryCode(String tenantId, String countryCode);

    List<ComplianceRequirement> findByTenantIdAndCategory(String tenantId, String category);

    List<ComplianceRequirement> findByTenantIdAndActive(String tenantId, Boolean active);

    List<ComplianceRequirement> findByTenantIdAndType(String tenantId, String type);

    List<ComplianceRequirement> findByTenantIdAndSeverity(String tenantId, String severity);

    List<ComplianceRequirement> findByTenantIdAndOwnerDepartment(String tenantId, String ownerDepartment);

    List<ComplianceRequirement> findByTenantIdAndOwnerId(String tenantId, String ownerId);

    List<ComplianceRequirement> findByTenantIdAndEffectiveFromBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<ComplianceRequirement> findByTenantIdAndReviewDateBefore(String tenantId, LocalDate reviewDate);

    List<ComplianceRequirement> findDueForReview(String tenantId);

    List<ComplianceRequirement> findActiveRequirements(String tenantId);

    List<ComplianceRequirement> findByTenantIdAndCountryCodeAndActive(String tenantId, String countryCode, Boolean active);

    List<ComplianceRequirement> searchByDescription(String tenantId, String searchTerm);

    List<ComplianceRequirement> findByTenantIdAndAuthority(String tenantId, String authority);

    boolean existsByRequirementCodeAndTenantId(String requirementCode, String tenantId);

    boolean existsByRequirementIdAndTenantId(String requirementId, String tenantId);

    void deleteById(String id);

    void deleteByRequirementIdAndTenantId(String requirementId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndActive(String tenantId, Boolean active);

    long countByTenantIdAndCategory(String tenantId, String category);

    long countByTenantIdAndCountryCode(String tenantId, String countryCode);

    List<ComplianceRequirement> findByTenantIdAndRelatedRequirementsContaining(String tenantId, String requirementId);
}
