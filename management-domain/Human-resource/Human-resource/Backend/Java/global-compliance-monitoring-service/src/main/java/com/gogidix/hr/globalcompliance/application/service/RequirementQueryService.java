package com.gogidix.hr.globalcompliance.application.service;

import com.gogidix.hr.globalcompliance.domain.model.ComplianceRequirement;
import com.gogidix.hr.globalcompliance.domain.repository.ComplianceRequirementRepository;
import com.gogidix.hr.globalcompliance.shared.exception.NotFoundException;
import com.gogidix.hr.globalcompliance.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Requirement Query Service
 * Handles all read operations for compliance requirements
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RequirementQueryService {

    private final ComplianceRequirementRepository requirementRepository;

    public ComplianceRequirement getById(String requirementId) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching requirement: {} for tenant: {}", requirementId, tenantId);

        return requirementRepository.findByRequirementIdAndTenantId(requirementId, tenantId)
                .orElseThrow(() -> new NotFoundException("ComplianceRequirement", requirementId));
    }

    public ComplianceRequirement getByCode(String requirementCode) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching requirement by code: {} for tenant: {}", requirementCode, tenantId);

        return requirementRepository.findByRequirementCodeAndTenantId(requirementCode, tenantId)
                .orElseThrow(() -> new NotFoundException("ComplianceRequirement", requirementCode));
    }

    public Page<ComplianceRequirement> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching all requirements for tenant: {}", tenantId);

        List<ComplianceRequirement> requirements = requirementRepository.findByTenantId(tenantId);
        return new PageImpl<>(requirements, PageRequest.of(0, requirements.size()), requirements.size());
    }

    public Page<ComplianceRequirement> getByCountryCode(String countryCode, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching requirements for country: {} in tenant: {}", countryCode, tenantId);

        List<ComplianceRequirement> requirements = requirementRepository
                .findByTenantIdAndCountryCode(tenantId, countryCode);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(requirements, pageRequest, requirements.size());
    }

    public Page<ComplianceRequirement> getByCategory(String category, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching requirements by category: {} for tenant: {}", category, tenantId);

        List<ComplianceRequirement> requirements = requirementRepository
                .findByTenantIdAndCategory(tenantId, category);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(requirements, pageRequest, requirements.size());
    }

    public Page<ComplianceRequirement> getActiveRequirements(int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching active requirements for tenant: {}", tenantId);

        List<ComplianceRequirement> requirements = requirementRepository.findActiveRequirements(tenantId);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(requirements, pageRequest, requirements.size());
    }

    public Page<ComplianceRequirement> getByType(String type, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching requirements by type: {} for tenant: {}", type, tenantId);

        List<ComplianceRequirement> requirements = requirementRepository
                .findByTenantIdAndType(tenantId, type);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(requirements, pageRequest, requirements.size());
    }

    public Page<ComplianceRequirement> getBySeverity(String severity, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching requirements by severity: {} for tenant: {}", severity, tenantId);

        List<ComplianceRequirement> requirements = requirementRepository
                .findByTenantIdAndSeverity(tenantId, severity);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(requirements, pageRequest, requirements.size());
    }

    public Page<ComplianceRequirement> getByOwnerDepartment(String ownerDepartment, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching requirements for department: {} in tenant: {}", ownerDepartment, tenantId);

        List<ComplianceRequirement> requirements = requirementRepository
                .findByTenantIdAndOwnerDepartment(tenantId, ownerDepartment);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(requirements, pageRequest, requirements.size());
    }

    public Page<ComplianceRequirement> getByOwnerId(String ownerId, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching requirements for owner: {} in tenant: {}", ownerId, tenantId);

        List<ComplianceRequirement> requirements = requirementRepository
                .findByTenantIdAndOwnerId(tenantId, ownerId);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(requirements, pageRequest, requirements.size());
    }

    public Page<ComplianceRequirement> getDueForReview(int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching requirements due for review in tenant: {}", tenantId);

        List<ComplianceRequirement> requirements = requirementRepository.findDueForReview(tenantId);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(requirements, pageRequest, requirements.size());
    }

    public Page<ComplianceRequirement> getByEffectiveDateRange(LocalDate startDate, LocalDate endDate, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching requirements for date range: {} to {} in tenant: {}", startDate, endDate, tenantId);

        List<ComplianceRequirement> requirements = requirementRepository
                .findByTenantIdAndEffectiveFromBetween(tenantId, startDate, endDate);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(requirements, pageRequest, requirements.size());
    }

    public Page<ComplianceRequirement> search(String searchTerm, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Searching requirements for tenant: {} with term: {}", tenantId, searchTerm);

        List<ComplianceRequirement> requirements = requirementRepository.searchByDescription(tenantId, searchTerm);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(requirements, pageRequest, requirements.size());
    }

    public Page<ComplianceRequirement> getByAuthority(String authority, int page, int size) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching requirements by authority: {} in tenant: {}", authority, tenantId);

        List<ComplianceRequirement> requirements = requirementRepository
                .findByTenantIdAndAuthority(tenantId, authority);
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(requirements, pageRequest, requirements.size());
    }

    public long countByTenant() {
        String tenantId = RequestContextHolder.getTenantId();
        return requirementRepository.countByTenantId(tenantId);
    }

    public long countByActive(Boolean active) {
        String tenantId = RequestContextHolder.getTenantId();
        return requirementRepository.countByTenantIdAndActive(tenantId, active);
    }

    public long countByCategory(String category) {
        String tenantId = RequestContextHolder.getTenantId();
        return requirementRepository.countByTenantIdAndCategory(tenantId, category);
    }

    public long countByCountryCode(String countryCode) {
        String tenantId = RequestContextHolder.getTenantId();
        return requirementRepository.countByTenantIdAndCountryCode(tenantId, countryCode);
    }

    public List<ComplianceRequirement> getByRelatedRequirements(String requirementId) {
        String tenantId = RequestContextHolder.getTenantId();
        return requirementRepository.findByTenantIdAndRelatedRequirementsContaining(tenantId, requirementId);
    }
}
