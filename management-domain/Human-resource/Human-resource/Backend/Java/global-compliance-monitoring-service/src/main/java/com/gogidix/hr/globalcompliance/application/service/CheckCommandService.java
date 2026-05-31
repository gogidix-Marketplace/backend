package com.gogidix.hr.globalcompliance.application.service;

import com.gogidix.hr.globalcompliance.domain.model.AuditTrail;
import com.gogidix.hr.globalcompliance.domain.model.ComplianceCheck;
import com.gogidix.hr.globalcompliance.domain.model.ComplianceRequirement;
import com.gogidix.hr.globalcompliance.domain.port.in.CheckCommand;
import com.gogidix.hr.globalcompliance.domain.port.out.EventPublisher;
import com.gogidix.hr.globalcompliance.domain.repository.AuditTrailRepository;
import com.gogidix.hr.globalcompliance.domain.repository.ComplianceCheckRepository;
import com.gogidix.hr.globalcompliance.domain.repository.ComplianceRequirementRepository;
import com.gogidix.hr.globalcompliance.shared.exception.NotFoundException;
import com.gogidix.hr.globalcompliance.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Check Command Service
 * Handles all write operations for compliance checks
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CheckCommandService {

    private final ComplianceCheckRepository checkRepository;
    private final ComplianceRequirementRepository requirementRepository;
    private final AuditTrailRepository auditTrailRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public ComplianceCheck create(CheckCommand.CreateCheckCommand command) {
        log.info("Creating check for requirement: {} in tenant: {}", command.getRequirementId(), command.getTenantId());

        ComplianceRequirement requirement = requirementRepository.findByRequirementIdAndTenantId(
                command.getRequirementId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceRequirement", command.getRequirementId()));

        ComplianceCheck check = ComplianceCheck.create(
                command.getTenantId(),
                command.getRequirementId(),
                command.getRequirementName(),
                command.getCountryCode(),
                command.getScheduledDate(),
                command.getFrequency(),
                RequestContextHolder.getUserId()
        );

        ComplianceCheck savedCheck = checkRepository.save(check);

        // Schedule check in requirement
        requirement.scheduleCheck(savedCheck.getCheckId(), command.getScheduledDate());
        requirementRepository.save(requirement);

        publishCheckEvents(savedCheck);
        createAuditTrail(savedCheck.getTenantId(), savedCheck.getCheckId(),
                "ComplianceCheck", savedCheck.getCheckId(),
                "CREATED", null, "Check created", RequestContextHolder.getUserId());

        log.info("Created check: {}", savedCheck.getCheckId());
        return savedCheck;
    }

    @Transactional
    public void start(CheckCommand.StartCheckCommand command) {
        log.info("Starting check: {} for tenant: {}", command.getCheckId(), command.getTenantId());

        ComplianceCheck check = checkRepository.findByCheckIdAndTenantId(
                command.getCheckId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceCheck", command.getCheckId()));

        check.start(command.getCheckedBy(), command.getCheckedByName());
        checkRepository.save(check);

        // Update requirement
        updateRequirementCheckStatus(command.getTenantId(), check.getRequirementId(),
                check.getCheckId(), "IN_PROGRESS");

        createAuditTrail(check.getTenantId(), check.getCheckId(),
                "ComplianceCheck", check.getCheckId(),
                "UPDATED", "status=PENDING", "status=IN_PROGRESS", command.getCheckedBy());

        log.info("Started check: {}", command.getCheckId());
    }

    @Transactional
    public void complete(CheckCommand.CompleteCheckCommand command) {
        log.info("Completing check: {} for tenant: {}", command.getCheckId(), command.getTenantId());

        ComplianceCheck check = checkRepository.findByCheckIdAndTenantId(
                command.getCheckId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceCheck", command.getCheckId()));

        check.complete(command.getResult(), command.getFindings(), command.getCorrectiveAction(),
                command.getTargetCompletionDate(), command.getSupportingDocuments());
        checkRepository.save(check);

        updateRequirementCheckStatus(command.getTenantId(), check.getRequirementId(),
                check.getCheckId(), check.getStatus());

        createAuditTrail(check.getTenantId(), check.getCheckId(),
                "ComplianceCheck", check.getCheckId(),
                "COMPLETED", null, "result=" + command.getResult(), RequestContextHolder.getUserId());

        publishCheckEvents(check);
        log.info("Completed check: {}", command.getCheckId());
    }

    @Transactional
    public void pass(CheckCommand.PassCheckCommand command) {
        log.info("Marking check as passed: {}", command.getCheckId());

        ComplianceCheck check = checkRepository.findByCheckIdAndTenantId(
                command.getCheckId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceCheck", command.getCheckId()));

        check.markAsPassed(command.getFindings(), command.getSupportingDocuments());
        checkRepository.save(check);

        updateRequirementCheckStatus(command.getTenantId(), check.getRequirementId(),
                check.getCheckId(), check.getStatus());

        createAuditTrail(check.getTenantId(), check.getCheckId(),
                "ComplianceCheck", check.getCheckId(),
                "COMPLETED", null, "result=COMPLIANT", RequestContextHolder.getUserId());

        publishCheckEvents(check);
        log.info("Marked check as passed: {}", command.getCheckId());
    }

    @Transactional
    public void fail(CheckCommand.FailCheckCommand command) {
        log.info("Marking check as failed: {}", command.getCheckId());

        ComplianceCheck check = checkRepository.findByCheckIdAndTenantId(
                command.getCheckId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceCheck", command.getCheckId()));

        check.markAsFailed(command.getFindings(), command.getCorrectiveAction(),
                command.getTargetCompletionDate(), command.getSupportingDocuments());
        checkRepository.save(check);

        updateRequirementCheckStatus(command.getTenantId(), check.getRequirementId(),
                check.getCheckId(), check.getStatus());

        createAuditTrail(check.getTenantId(), check.getCheckId(),
                "ComplianceCheck", check.getCheckId(),
                "COMPLETED", null, "result=NON_COMPLIANT", RequestContextHolder.getUserId());

        publishCheckEvents(check);
        log.info("Marked check as failed: {}", command.getCheckId());
    }

    @Transactional
    public void waive(CheckCommand.WaiveCheckCommand command) {
        log.info("Waiving check: {}", command.getCheckId());

        ComplianceCheck check = checkRepository.findByCheckIdAndTenantId(
                command.getCheckId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceCheck", command.getCheckId()));

        check.waive(command.getReason(), command.getWaivedBy());
        checkRepository.save(check);

        createAuditTrail(check.getTenantId(), check.getCheckId(),
                "ComplianceCheck", check.getCheckId(),
                "UPDATED", null, "status=WAIVED, reason=" + command.getReason(), command.getWaivedBy());

        log.info("Waived check: {}", command.getCheckId());
    }

    @Transactional
    public void markAsNotApplicable(CheckCommand.MarkAsNotApplicableCommand command) {
        log.info("Marking check as not applicable: {}", command.getCheckId());

        ComplianceCheck check = checkRepository.findByCheckIdAndTenantId(
                command.getCheckId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceCheck", command.getCheckId()));

        check.markAsNotApplicable(command.getReason());
        checkRepository.save(check);

        createAuditTrail(check.getTenantId(), check.getCheckId(),
                "ComplianceCheck", check.getCheckId(),
                "UPDATED", null, "status=NOT_APPLICABLE", RequestContextHolder.getUserId());

        log.info("Marked check as not applicable: {}", command.getCheckId());
    }

    @Transactional
    public void reschedule(CheckCommand.RescheduleCheckCommand command) {
        log.info("Rescheduling check: {}", command.getCheckId());

        ComplianceCheck check = checkRepository.findByCheckIdAndTenantId(
                command.getCheckId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceCheck", command.getCheckId()));

        LocalDate previousDate = check.getScheduledDate();
        check.reschedule(command.getNewScheduledDate(), command.getReason());
        checkRepository.save(check);

        createAuditTrail(check.getTenantId(), check.getCheckId(),
                "ComplianceCheck", check.getCheckId(),
                "UPDATED", "scheduledDate=" + previousDate,
                "scheduledDate=" + command.getNewScheduledDate(), RequestContextHolder.getUserId());

        log.info("Rescheduled check: {}", command.getCheckId());
    }

    @Transactional
    public void updateCorrectiveAction(CheckCommand.UpdateCorrectiveActionCommand command) {
        log.info("Updating corrective action for check: {}", command.getCheckId());

        ComplianceCheck check = checkRepository.findByCheckIdAndTenantId(
                command.getCheckId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceCheck", command.getCheckId()));

        check.updateCorrectiveAction(command.getCorrectiveAction(), command.getActualCompletionDate());
        checkRepository.save(check);

        createAuditTrail(check.getTenantId(), check.getCheckId(),
                "ComplianceCheck", check.getCheckId(),
                "UPDATED", null, "correctiveAction updated", RequestContextHolder.getUserId());

        log.info("Updated corrective action for check: {}", command.getCheckId());
    }

    @Transactional
    public void addComment(CheckCommand.AddCommentCommand command) {
        log.info("Adding comment to check: {}", command.getCheckId());

        ComplianceCheck check = checkRepository.findByCheckIdAndTenantId(
                command.getCheckId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceCheck", command.getCheckId()));

        check.addComment(command.getComment());
        checkRepository.save(check);

        log.info("Added comment to check: {}", command.getCheckId());
    }

    @Transactional
    public void delete(CheckCommand.DeleteCheckCommand command) {
        log.info("Deleting check: {} for tenant: {}", command.getCheckId(), command.getTenantId());

        ComplianceCheck check = checkRepository.findByCheckIdAndTenantId(
                command.getCheckId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("ComplianceCheck", command.getCheckId()));

        String checkId = check.getCheckId();
        String tenantId = check.getTenantId();

        checkRepository.deleteByCheckIdAndTenantId(command.getCheckId(), command.getTenantId());
        auditTrailRepository.deleteByCheckId(checkId);

        createAuditTrail(tenantId, checkId, "ComplianceCheck", checkId,
                "DELETED", null, null, RequestContextHolder.getUserId());

        log.info("Deleted check: {}", command.getCheckId());
    }

    private void publishCheckEvents(ComplianceCheck check) {
        if (!check.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            for (var event : check.getDomainEvents()) {
                eventPublisher.publishCheckEvent(event);
            }
            check.clearDomainEvents();
        }
    }

    private void createAuditTrail(String tenantId, String entityId, String entityType,
                                   String checkId, String action, String previousValue,
                                   String newValue, String actionedBy) {
        AuditTrail audit = AuditTrail.forCheck(tenantId, checkId, action,
                actionedBy, actionedBy, previousValue, newValue, null,
                RequestContextHolder.getOptional().map(ctx -> ctx.getIpAddress()).orElse(null),
                RequestContextHolder.getOptional().map(ctx -> ctx.getCountryCode()).orElse(null));

        auditTrailRepository.save(audit);
    }

    private void updateRequirementCheckStatus(String tenantId, String requirementId,
                                              String checkId, String status) {
        requirementRepository.findByRequirementIdAndTenantId(requirementId, tenantId)
                .ifPresent(requirement -> {
                    requirement.updateCheckStatus(checkId, status);
                    requirementRepository.save(requirement);
                });
    }
}
