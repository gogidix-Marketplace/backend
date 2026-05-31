package com.gogidix.hr.globalcompliance.application.service;

import com.gogidix.hr.globalcompliance.domain.model.AuditTrail;
import com.gogidix.hr.globalcompliance.domain.model.ComplianceRequirement;
import com.gogidix.hr.globalcompliance.domain.port.in.RequirementCommand;
import com.gogidix.hr.globalcompliance.domain.port.out.EventPublisher;
import com.gogidix.hr.globalcompliance.domain.repository.AuditTrailRepository;
import com.gogidix.hr.globalcompliance.domain.repository.ComplianceRequirementRepository;
import com.gogidix.hr.globalcompliance.shared.exception.ConflictException;
import com.gogidix.hr.globalcompliance.shared.exception.NotFoundException;
import com.gogidix.hr.globalcompliance.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Requirement Command Service
 * Handles all write operations for compliance requirements
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RequirementCommandService {

    private final ComplianceRequirementRepository requirementRepository;
    private final AuditTrailRepository auditTrailRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public ComplianceRequirement create(RequirementCommand.CreateRequirementCommand command) {
        log.info("Creating requirement: {} for tenant: {}", command.getRequirementCode(), command.getTenantId());

        // Check if requirement code already exists
        if (requirementRepository.existsByRequirementCodeAndTenantId(command.getRequirementCode(), command.getTenantId())) {
            throw new ConflictException("ComplianceRequirement", command.getRequirementCode());
        }

        ComplianceRequirement requirement = ComplianceRequirement.create(
                command.getTenantId(),
                command.getRequirementCode(),
                command.getRequirementName(),
                command.getCategory(),
                command.getCountryCode(),
                command.getDescription(),
                command.getAuthority(),
                command.getType(),
                command.getEffectiveFrom(),
                command.getFrequency(),
                command.getSeverity(),
                command.getOwnerDepartment(),
                command.getOwnerId(),
                command.getCreatedBy()
        );

        if (command.getEffectiveTo() != null) {
            requirement.setEffectiveTo(command.getEffectiveTo());
        }

        ComplianceRequirement savedRequirement = requirementRepository.save(requirement);
        publishRequirementEvents(savedRequirement);
        createAuditTrail(savedRequirement.getTenantId(), savedRequirement.getRequirementId(),
                "ComplianceRequirement", savedRequirement.getRequirementId(),
                "CREATED", null, "Requirement created", command.getCreatedBy());

        log.info("Created requirement: {}", savedRequirement.getRequirementId());
        return savedRequirement;
    }

    @Transactional
    public ComplianceRequirement update(RequirementCommand.UpdateRequirementCommand command) {
        log.info("Updating requirement: {} for tenant: {}", command.getRequirementId(), command.getTenantId());

        ComplianceRequirement requirement = requirementRepository.findByRequirementIdAndTenantId(
                command.getRequirementId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceRequirement", command.getRequirementId()));

        String previousValues = capturePreviousValues(requirement);

        requirement.updateDetails(
                command.getRequirementName(),
                command.getDescription(),
                command.getAuthority(),
                command.getOwnerDepartment(),
                command.getOwnerId(),
                command.getEffectiveTo()
        );

        ComplianceRequirement savedRequirement = requirementRepository.save(requirement);

        String newValues = captureNewValues(savedRequirement);
        createAuditTrail(savedRequirement.getTenantId(), savedRequirement.getRequirementId(),
                "ComplianceRequirement", savedRequirement.getRequirementId(),
                "UPDATED", previousValues, newValues, RequestContextHolder.getUserId());

        log.info("Updated requirement: {}", command.getRequirementId());
        return savedRequirement;
    }

    @Transactional
    public void activate(RequirementCommand.ActivateRequirementCommand command) {
        log.info("Activating requirement: {} for tenant: {}", command.getRequirementId(), command.getTenantId());

        ComplianceRequirement requirement = requirementRepository.findByRequirementIdAndTenantId(
                command.getRequirementId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceRequirement", command.getRequirementId()));

        requirement.activate();
        requirementRepository.save(requirement);

        createAuditTrail(requirement.getTenantId(), requirement.getRequirementId(),
                "ComplianceRequirement", requirement.getRequirementId(),
                "UPDATED", "active=false", "active=true", RequestContextHolder.getUserId());

        log.info("Activated requirement: {}", command.getRequirementId());
    }

    @Transactional
    public void deactivate(RequirementCommand.DeactivateRequirementCommand command) {
        log.info("Deactivating requirement: {} for tenant: {}", command.getRequirementId(), command.getTenantId());

        ComplianceRequirement requirement = requirementRepository.findByRequirementIdAndTenantId(
                command.getRequirementId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceRequirement", command.getRequirementId()));

        requirement.deactivate(command.getReason());
        requirementRepository.save(requirement);

        createAuditTrail(requirement.getTenantId(), requirement.getRequirementId(),
                "ComplianceRequirement", requirement.getRequirementId(),
                "UPDATED", "active=true", "active=false, reason=" + command.getReason(),
                RequestContextHolder.getUserId());

        log.info("Deactivated requirement: {}", command.getRequirementId());
    }

    @Transactional
    public void delete(RequirementCommand.DeleteRequirementCommand command) {
        log.info("Deleting requirement: {} for tenant: {}", command.getRequirementId(), command.getTenantId());

        ComplianceRequirement requirement = requirementRepository.findByRequirementIdAndTenantId(
                command.getRequirementId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceRequirement", command.getRequirementId()));

        String requirementId = requirement.getRequirementId();
        String tenantId = requirement.getTenantId();

        requirementRepository.deleteByRequirementIdAndTenantId(command.getRequirementId(), command.getTenantId());
        auditTrailRepository.deleteByRequirementId(requirementId);

        createAuditTrail(tenantId, requirementId, "ComplianceRequirement", requirementId,
                "DELETED", capturePreviousValues(requirement), null, RequestContextHolder.getUserId());

        log.info("Deleted requirement: {}", command.getRequirementId());
    }

    @Transactional
    public void addRelatedRequirement(RequirementCommand.AddRelatedRequirementCommand command) {
        log.info("Adding related requirement: {} to {}", command.getRelatedRequirementId(), command.getRequirementId());

        ComplianceRequirement requirement = requirementRepository.findByRequirementIdAndTenantId(
                command.getRequirementId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceRequirement", command.getRequirementId()));

        requirement.addRelatedRequirement(command.getRelatedRequirementId());
        requirementRepository.save(requirement);

        createAuditTrail(requirement.getTenantId(), requirement.getRequirementId(),
                "ComplianceRequirement", requirement.getRequirementId(),
                "UPDATED", null, "Added related requirement: " + command.getRelatedRequirementId(),
                RequestContextHolder.getUserId());

        log.info("Added related requirement to: {}", command.getRequirementId());
    }

    @Transactional
    public void updateReviewDate(RequirementCommand.UpdateReviewDateCommand command) {
        log.info("Updating review date for requirement: {}", command.getRequirementId());

        ComplianceRequirement requirement = requirementRepository.findByRequirementIdAndTenantId(
                command.getRequirementId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceRequirement", command.getRequirementId()));

        LocalDate previousReviewDate = requirement.getReviewDate();
        requirement.updateReviewDate();
        requirementRepository.save(requirement);

        createAuditTrail(requirement.getTenantId(), requirement.getRequirementId(),
                "ComplianceRequirement", requirement.getRequirementId(),
                "UPDATED", "reviewDate=" + previousReviewDate,
                "reviewDate=" + requirement.getReviewDate(), RequestContextHolder.getUserId());

        log.info("Updated review date for requirement: {}", command.getRequirementId());
    }

    private void publishRequirementEvents(ComplianceRequirement requirement) {
        if (!requirement.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            for (var event : requirement.getDomainEvents()) {
                eventPublisher.publishRequirementEvent(event);
            }
            requirement.clearDomainEvents();
        }
    }

    private void createAuditTrail(String tenantId, String entityId, String entityType,
                                   String requirementId, String action, String previousValue,
                                   String newValue, String actionedBy) {
        AuditTrail audit = AuditTrail.forRequirement(tenantId, requirementId, action,
                actionedBy, actionedBy, previousValue, newValue, null,
                RequestContextHolder.getOptional().map(ctx -> ctx.getIpAddress()).orElse(null),
                RequestContextHolder.getOptional().map(ctx -> ctx.getCountryCode()).orElse(null));

        auditTrailRepository.save(audit);
    }

    private String capturePreviousValues(ComplianceRequirement requirement) {
        return String.format("name=%s, category=%s, type=%s, active=%s",
                requirement.getRequirementName(), requirement.getCategory(),
                requirement.getType(), requirement.getActive());
    }

    private String captureNewValues(ComplianceRequirement requirement) {
        return capturePreviousValues(requirement);
    }
}
