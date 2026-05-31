package com.gogidix.hr.globalcompliance.application.service;

import com.gogidix.hr.globalcompliance.domain.model.AuditTrail;
import com.gogidix.hr.globalcompliance.domain.model.ComplianceCheck;
import com.gogidix.hr.globalcompliance.domain.model.NonComplianceIssue;
import com.gogidix.hr.globalcompliance.domain.port.in.IssueCommand;
import com.gogidix.hr.globalcompliance.domain.port.out.EventPublisher;
import com.gogidix.hr.globalcompliance.domain.repository.AuditTrailRepository;
import com.gogidix.hr.globalcompliance.domain.repository.ComplianceCheckRepository;
import com.gogidix.hr.globalcompliance.domain.repository.NonComplianceIssueRepository;
import com.gogidix.hr.globalcompliance.shared.exception.NotFoundException;
import com.gogidix.hr.globalcompliance.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Issue Command Service
 * Handles all write operations for non-compliance issues
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class IssueCommandService {

    private final NonComplianceIssueRepository issueRepository;
    private final ComplianceCheckRepository checkRepository;
    private final AuditTrailRepository auditTrailRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public NonComplianceIssue create(IssueCommand.CreateIssueCommand command) {
        log.info("Creating issue: {} for tenant: {}", command.getTitle(), command.getTenantId());

        NonComplianceIssue issue = NonComplianceIssue.create(
                command.getTenantId(),
                command.getRequirementId(),
                command.getCheckId(),
                command.getCountryCode(),
                command.getTitle(),
                command.getDescription(),
                command.getSeverity(),
                command.getIdentifiedBy(),
                command.getIdentifiedByName(),
                command.getDepartment()
        );

        if (command.getAffectedEmployees() != null) {
            command.getAffectedEmployees().forEach(issue::addAffectedEmployee);
        }

        if (command.getFinancialImpact() != null) {
            issue.setFinancialImpactDetails(command.getFinancialImpact(), command.getCurrency());
        }

        if (command.getLocation() != null) {
            issue.setLocation(command.getLocation());
        }

        NonComplianceIssue savedIssue = issueRepository.save(issue);

        // Add issue reference to check
        if (command.getCheckId() != null) {
            checkRepository.findByCheckIdAndTenantId(command.getCheckId(), command.getTenantId())
                    .ifPresent(check -> {
                        check.addIssue(savedIssue.getIssueId(), savedIssue.getIssueNumber(),
                                savedIssue.getSeverity());
                        checkRepository.save(check);
                    });
        }

        publishIssueEvents(savedIssue);
        createAuditTrail(savedIssue.getTenantId(), savedIssue.getIssueId(),
                "NonComplianceIssue", savedIssue.getIssueId(),
                "CREATED", null, "Issue created", command.getIdentifiedBy());

        log.info("Created issue: {}", savedIssue.getIssueId());
        return savedIssue;
    }

    @Transactional
    public void assign(IssueCommand.AssignIssueCommand command) {
        log.info("Assigning issue: {} to {}", command.getIssueId(), command.getAssignedTo());

        NonComplianceIssue issue = issueRepository.findByIssueIdAndTenantId(
                command.getIssueId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("NonComplianceIssue", command.getIssueId()));

        String previousAssignee = issue.getAssignedTo();
        issue.assignTo(command.getAssignedTo(), command.getAssignedToName());
        issueRepository.save(issue);

        createAuditTrail(issue.getTenantId(), issue.getIssueId(),
                "NonComplianceIssue", issue.getIssueId(),
                "UPDATED", "assignedTo=" + previousAssignee,
                "assignedTo=" + command.getAssignedTo(), RequestContextHolder.getUserId());

        log.info("Assigned issue: {}", command.getIssueId());
    }

    @Transactional
    public void startProgress(IssueCommand.StartProgressCommand command) {
        log.info("Starting progress on issue: {}", command.getIssueId());

        NonComplianceIssue issue = issueRepository.findByIssueIdAndTenantId(
                command.getIssueId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("NonComplianceIssue", command.getIssueId()));

        issue.startProgress();
        issueRepository.save(issue);

        createAuditTrail(issue.getTenantId(), issue.getIssueId(),
                "NonComplianceIssue", issue.getIssueId(),
                "UPDATED", "status=OPEN", "status=IN_PROGRESS", RequestContextHolder.getUserId());

        log.info("Started progress on issue: {}", command.getIssueId());
    }

    @Transactional
    public void resolve(IssueCommand.ResolveIssueCommand command) {
        log.info("Resolving issue: {}", command.getIssueId());

        NonComplianceIssue issue = issueRepository.findByIssueIdAndTenantId(
                command.getIssueId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("NonComplianceIssue", command.getIssueId()));

        issue.resolve(command.getResolution(), command.getRootCause(), command.getResolvedBy());
        issueRepository.save(issue);

        createAuditTrail(issue.getTenantId(), issue.getIssueId(),
                "NonComplianceIssue", issue.getIssueId(),
                "RESOLVED", null, "resolution=" + command.getResolution(), command.getResolvedBy());

        publishIssueEvents(issue);
        log.info("Resolved issue: {}", command.getIssueId());
    }

    @Transactional
    public void close(IssueCommand.CloseIssueCommand command) {
        log.info("Closing issue: {}", command.getIssueId());

        NonComplianceIssue issue = issueRepository.findByIssueIdAndTenantId(
                command.getIssueId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("NonComplianceIssue", command.getIssueId()));

        issue.close(command.getClosedBy());
        issueRepository.save(issue);

        createAuditTrail(issue.getTenantId(), issue.getIssueId(),
                "NonComplianceIssue", issue.getIssueId(),
                "CLOSED", "status=RESOLVED", "status=CLOSED", command.getClosedBy());

        log.info("Closed issue: {}", command.getIssueId());
    }

    @Transactional
    public void escalate(IssueCommand.EscalateIssueCommand command) {
        log.info("Escalating issue: {}", command.getIssueId());

        NonComplianceIssue issue = issueRepository.findByIssueIdAndTenantId(
                command.getIssueId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("NonComplianceIssue", command.getIssueId()));

        String previousStatus = issue.getStatus();
        issue.escalate(command.getReason(), command.getEscalatedBy());
        issueRepository.save(issue);

        createAuditTrail(issue.getTenantId(), issue.getIssueId(),
                "NonComplianceIssue", issue.getIssueId(),
                "ESCALATED", "status=" + previousStatus,
                "status=ESCALATED", command.getEscalatedBy());

        log.info("Escalated issue: {}", command.getIssueId());
    }

    @Transactional
    public void reopen(IssueCommand.ReopenIssueCommand command) {
        log.info("Reopening issue: {}", command.getIssueId());

        NonComplianceIssue issue = issueRepository.findByIssueIdAndTenantId(
                command.getIssueId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("NonComplianceIssue", command.getIssueId()));

        String previousStatus = issue.getStatus();
        issue.reopen(command.getReason());
        issueRepository.save(issue);

        createAuditTrail(issue.getTenantId(), issue.getIssueId(),
                "NonComplianceIssue", issue.getIssueId(),
                "UPDATED", "status=" + previousStatus,
                "status=OPEN", RequestContextHolder.getUserId());

        log.info("Reopened issue: {}", command.getIssueId());
    }

    @Transactional
    public void updateSeverity(IssueCommand.UpdateSeverityCommand command) {
        log.info("Updating severity for issue: {} to {}", command.getIssueId(), command.getNewSeverity());

        NonComplianceIssue issue = issueRepository.findByIssueIdAndTenantId(
                command.getIssueId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("NonComplianceIssue", command.getIssueId()));

        String previousSeverity = issue.getSeverity();
        issue.updateSeverity(command.getNewSeverity(), command.getReason());
        issueRepository.save(issue);

        createAuditTrail(issue.getTenantId(), issue.getIssueId(),
                "NonComplianceIssue", issue.getIssueId(),
                "UPDATED", "severity=" + previousSeverity,
                "severity=" + command.getNewSeverity(), RequestContextHolder.getUserId());

        log.info("Updated severity for issue: {}", command.getIssueId());
    }

    @Transactional
    public void updateDueDate(IssueCommand.UpdateDueDateCommand command) {
        log.info("Updating due date for issue: {}", command.getIssueId());

        NonComplianceIssue issue = issueRepository.findByIssueIdAndTenantId(
                command.getIssueId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("NonComplianceIssue", command.getIssueId()));

        LocalDate previousDueDate = issue.getDueDate();
        issue.updateDueDate(command.getNewDueDate(), command.getReason());
        issueRepository.save(issue);

        createAuditTrail(issue.getTenantId(), issue.getIssueId(),
                "NonComplianceIssue", issue.getIssueId(),
                "UPDATED", "dueDate=" + previousDueDate,
                "dueDate=" + command.getNewDueDate(), RequestContextHolder.getUserId());

        log.info("Updated due date for issue: {}", command.getIssueId());
    }

    @Transactional
    public void addAffectedEmployee(IssueCommand.AddAffectedEmployeeCommand command) {
        log.info("Adding affected employee: {} to issue: {}", command.getEmployeeId(), command.getIssueId());

        NonComplianceIssue issue = issueRepository.findByIssueIdAndTenantId(
                command.getIssueId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("NonComplianceIssue", command.getIssueId()));

        issue.addAffectedEmployee(command.getEmployeeId());
        issueRepository.save(issue);

        log.info("Added affected employee to issue: {}", command.getIssueId());
    }

    @Transactional
    public void setFinancialImpact(IssueCommand.SetFinancialImpactCommand command) {
        log.info("Setting financial impact for issue: {}", command.getIssueId());

        NonComplianceIssue issue = issueRepository.findByIssueIdAndTenantId(
                command.getIssueId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("NonComplianceIssue", command.getIssueId()));

        issue.setFinancialImpactDetails(command.getFinancialImpact(), command.getCurrency());
        issueRepository.save(issue);

        createAuditTrail(issue.getTenantId(), issue.getIssueId(),
                "NonComplianceIssue", issue.getIssueId(),
                "UPDATED", null, "financialImpact=" + command.getFinancialImpact(),
                RequestContextHolder.getUserId());

        log.info("Set financial impact for issue: {}", command.getIssueId());
    }

    @Transactional
    public void addAction(IssueCommand.AddActionCommand command) {
        log.info("Adding action to issue: {}", command.getIssueId());

        NonComplianceIssue issue = issueRepository.findByIssueIdAndTenantId(
                command.getIssueId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("NonComplianceIssue", command.getIssueId()));

        issue.addAction(command.getAction());
        issueRepository.save(issue);

        log.info("Added action to issue: {}", command.getIssueId());
    }

    @Transactional
    public void delete(IssueCommand.DeleteIssueCommand command) {
        log.info("Deleting issue: {} for tenant: {}", command.getIssueId(), command.getTenantId());

        NonComplianceIssue issue = issueRepository.findByIssueIdAndTenantId(
                command.getIssueId(), command.getTenantId())
                .orElseThrow(() -> new NotFoundException("NonComplianceIssue", command.getIssueId()));

        String issueId = issue.getIssueId();
        String tenantId = issue.getTenantId();

        issueRepository.deleteByIssueIdAndTenantId(command.getIssueId(), command.getTenantId());
        auditTrailRepository.deleteByIssueId(issueId);

        createAuditTrail(tenantId, issueId, "NonComplianceIssue", issueId,
                "DELETED", null, null, RequestContextHolder.getUserId());

        log.info("Deleted issue: {}", command.getIssueId());
    }

    private void publishIssueEvents(NonComplianceIssue issue) {
        if (!issue.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            for (var event : issue.getDomainEvents()) {
                if (event instanceof com.gogidix.hr.globalcompliance.domain.event.NonComplianceIssueResolvedEvent) {
                    eventPublisher.publishIssueResolvedEvent(
                            (com.gogidix.hr.globalcompliance.domain.event.NonComplianceIssueResolvedEvent) event);
                } else {
                    eventPublisher.publishIssueCreatedEvent(
                            (com.gogidix.hr.globalcompliance.domain.event.NonComplianceIssueCreatedEvent) event);
                }
            }
            issue.clearDomainEvents();
        }
    }

    private void createAuditTrail(String tenantId, String entityId, String entityType,
                                   String issueId, String action, String previousValue,
                                   String newValue, String actionedBy) {
        AuditTrail audit = AuditTrail.forIssue(tenantId, issueId, action,
                actionedBy, actionedBy, previousValue, newValue, null,
                RequestContextHolder.getOptional().map(ctx -> ctx.getIpAddress()).orElse(null),
                RequestContextHolder.getOptional().map(ctx -> ctx.getCountryCode()).orElse(null));

        auditTrailRepository.save(audit);
    }
}
