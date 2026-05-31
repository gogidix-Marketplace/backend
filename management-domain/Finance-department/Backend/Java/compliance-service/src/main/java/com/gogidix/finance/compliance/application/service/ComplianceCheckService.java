package com.gogidix.finance.compliance.application.service;

import com.gogidix.finance.compliance.domain.event.ComplianceViolationEvent;
import com.gogidix.finance.compliance.domain.model.ComplianceCheck;
import com.gogidix.finance.compliance.domain.model.ComplianceRule;
import com.gogidix.finance.compliance.domain.port.in.ComplianceCheckCommand;
import com.gogidix.finance.compliance.domain.port.out.EventPublisher;
import com.gogidix.finance.compliance.domain.repository.ComplianceCheckRepository;
import com.gogidix.finance.compliance.domain.repository.ComplianceRuleRepository;
import com.gogidix.finance.compliance.shared.exception.NotFoundException;
import com.gogidix.finance.compliance.shared.exception.ValidationException;
import com.gogidix.finance.compliance.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * Compliance Check Command Service
 * Handles all write operations for compliance checks
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ComplianceCheckService {

    private final ComplianceCheckRepository checkRepository;
    private final ComplianceRuleRepository ruleRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public ComplianceCheck create(ComplianceCheckCommand.CreateCheckCommand command) {
        log.info("Creating compliance check for rule: {} and entity: {}",
            command.getRuleId(), command.getEntityId());

        ComplianceRule rule = ruleRepository.findByRuleIdAndTenantId(
            command.getRuleId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("ComplianceRule", command.getRuleId()));

        ComplianceCheck check = ComplianceCheck.create(
            command.getTenantId(),
            command.getRuleId(),
            command.getRuleName(),
            command.getEntityType(),
            command.getEntityId(),
            command.getEvaluatedByUserId()
        );

        check.setCheckId("CC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        check.setReferenceNumber(command.getReferenceNumber());
        check.setEvaluatedContext(command.getContext());
        check.setEvaluatedAmount(command.getEvaluatedAmount());
        check.setEvaluatedCurrency(command.getEvaluatedCurrency());
        check.setThresholdAmount(command.getThresholdAmount());
        check.setThresholdCurrency(command.getThresholdCurrency());
        check.setDepartment(command.getDepartment());
        check.setCostCenter(command.getCostCenter());
        check.setExpenseCategory(command.getExpenseCategory());
        check.setCorrelationId(command.getCorrelationId());

        // Set severity from rule
        check.setSeverity(ComplianceCheck.SeverityLevel.valueOf(rule.getSeverity().name()));

        ComplianceCheck savedCheck = checkRepository.save(check);
        publishEvents(savedCheck);

        log.info("Created compliance check: {}", savedCheck.getCheckId());
        return savedCheck;
    }

    @Transactional
    public ComplianceCheck executeCheck(ComplianceCheckCommand.ExecuteCheckCommand command) {
        log.info("Executing compliance check: {} for tenant: {}", command.getCheckId(), command.getTenantId());

        ComplianceCheck check = checkRepository.findByCheckIdAndTenantId(
            command.getCheckId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("ComplianceCheck", command.getCheckId()));

        check.startEvaluation();

        // Set evaluated context
        if (command.getContext() != null) {
            check.setEvaluatedContext(command.getContext());
        }

        if (command.getVariance() != null) {
            check.setVariance(command.getVariance());
        }

        // Process result
        switch (command.getResult()) {
            case COMPLIANT -> {
                check.markAsCompliant(command.getEvaluatedBy(), command.getContext());
            }
            case NON_COMPLIANT -> {
                ComplianceCheck.SeverityLevel severity = command.getSeverity() != null
                    ? ComplianceCheck.SeverityLevel.valueOf(command.getSeverity().name())
                    : ComplianceCheck.SeverityLevel.ERROR;
                check.markAsNonCompliant(
                    command.getEvaluatedBy(),
                    command.getViolationDescription(),
                    severity,
                    command.getContext()
                );
            }
            case WARNING -> {
                check.markAsWarning(command.getEvaluatedBy(), command.getViolationDescription(), command.getContext());
            }
            case NOT_APPLICABLE -> {
                check.markAsNotApplicable(command.getEvaluatedBy(), "Not applicable");
            }
            default -> {
                check.markAsFailed(command.getEvaluatedBy(), "Unknown result type");
            }
        }

        ComplianceCheck savedCheck = checkRepository.save(check);
        publishEvents(savedCheck);

        log.info("Executed compliance check: {} with result: {}", command.getCheckId(), command.getResult());
        return savedCheck;
    }

    @Transactional
    public void approve(ComplianceCheckCommand.ApproveCheckCommand command) {
        log.info("Approving compliance check: {} for tenant: {}", command.getCheckId(), command.getTenantId());

        ComplianceCheck check = checkRepository.findByCheckIdAndTenantId(
            command.getCheckId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("ComplianceCheck", command.getCheckId()));

        check.approve(command.getApprovedBy(), command.getNotes());
        checkRepository.save(check);

        log.info("Approved compliance check: {}", command.getCheckId());
    }

    @Transactional
    public void waiveViolation(ComplianceCheckCommand.WaiveViolationCommand command) {
        log.info("Waiving violation for check: {} for tenant: {}", command.getCheckId(), command.getTenantId());

        ComplianceCheck check = checkRepository.findByCheckIdAndTenantId(
            command.getCheckId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("ComplianceCheck", command.getCheckId()));

        check.waive(command.getWaivedBy(), command.getReason());
        checkRepository.save(check);
        publishEvents(check);

        log.info("Waived violation for check: {}", command.getCheckId());
    }

    @Transactional
    public void assignRemediation(ComplianceCheckCommand.AssignRemediationCommand command) {
        log.info("Assigning remediation for check: {} for tenant: {}", command.getCheckId(), command.getTenantId());

        ComplianceCheck check = checkRepository.findByCheckIdAndTenantId(
            command.getCheckId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("ComplianceCheck", command.getCheckId()));

        check.assignRemediation(command.getAssignedTo(), command.getAction(), command.getDueDate());
        checkRepository.save(check);

        log.info("Assigned remediation for check: {}", command.getCheckId());
    }

    @Transactional
    public void completeRemediation(ComplianceCheckCommand.CompleteRemediationCommand command) {
        log.info("Completing remediation for check: {} for tenant: {}", command.getCheckId(), command.getTenantId());

        ComplianceCheck check = checkRepository.findByCheckIdAndTenantId(
            command.getCheckId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("ComplianceCheck", command.getCheckId()));

        check.completeRemediation(command.getCompletedBy());
        checkRepository.save(check);

        log.info("Completed remediation for check: {}", command.getCheckId());
    }

    @Transactional
    public void delete(ComplianceCheckCommand.DeleteCheckCommand command) {
        log.info("Deleting compliance check: {} for tenant: {}", command.getCheckId(), command.getTenantId());

        ComplianceCheck check = checkRepository.findByCheckIdAndTenantId(
            command.getCheckId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("ComplianceCheck", command.getCheckId()));

        checkRepository.deleteByCheckIdAndTenantId(command.getCheckId(), command.getTenantId());

        log.info("Deleted compliance check: {}", command.getCheckId());
    }

    /**
     * Automatically evaluates an entity against active rules
     */
    @Transactional
    public ComplianceCheck evaluateAgainstRule(String tenantId, String ruleId, String entityType,
                                                 String entityId, Map<String, Object> context,
                                                 String evaluatedByUserId) {
        log.info("Evaluating entity: {} against rule: {}", entityId, ruleId);

        ComplianceRule rule = ruleRepository.findByRuleIdAndTenantId(ruleId, tenantId)
            .orElseThrow(() -> new NotFoundException("ComplianceRule", ruleId));

        if (!rule.isCurrentlyEffective()) {
            throw new ValidationException("Rule is not currently effective");
        }

        // Create check
        ComplianceCheck check = ComplianceCheck.create(
            tenantId, ruleId, rule.getName(), entityType, entityId, evaluatedByUserId
        );
        check.setCheckId("CC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        // Evaluate based on rule type
        evaluateCheck(check, rule, context);

        return checkRepository.save(check);
    }

    private void evaluateCheck(ComplianceCheck check, ComplianceRule rule, Map<String, Object> context) {
        check.startEvaluation();
        check.setEvaluatedContext(context);

        boolean isCompliant = switch (rule.getRuleType()) {
            case AMOUNT_THRESHOLD -> evaluateAmountThreshold(check, rule, context);
            case CATEGORY_RESTRICTION -> evaluateCategoryRestriction(check, rule, context);
            case DOCUMENT_REQUIRED -> evaluateDocumentRequired(check, rule, context);
            case TIME_LIMIT -> evaluateTimeLimit(check, rule, context);
            case FREQUENCY_LIMIT -> evaluateFrequencyLimit(check, rule, context);
            case CUSTOM_EXPRESSION -> evaluateCustomExpression(check, rule, context);
            default -> true;
        };

        if (isCompliant) {
            check.markAsCompliant(check.getEvaluatedByUserId() != null ?
                check.getEvaluatedByUserId() : RequestContextHolder.getUserId().orElse("system"), context);
        } else {
            check.markAsNonCompliant(check.getEvaluatedByUserId() != null ?
                check.getEvaluatedByUserId() : RequestContextHolder.getUserId().orElse("system"),
                "Rule violation detected", ComplianceCheck.SeverityLevel.valueOf(rule.getSeverity().name()), context);
        }
    }

    private boolean evaluateAmountThreshold(ComplianceCheck check, ComplianceRule rule, Map<String, Object> context) {
        Object amountObj = context.get("amount");
        if (amountObj instanceof Number) {
            BigDecimal amount = new BigDecimal(amountObj.toString());
            check.setEvaluatedAmount(amount);
            check.setThresholdAmount(rule.getThresholdAmount());
            check.calculateVariance();

            return amount.compareTo(rule.getThresholdAmount()) <= 0;
        }
        return true;
    }

    private boolean evaluateCategoryRestriction(ComplianceCheck check, ComplianceRule rule, Map<String, Object> context) {
        Object categoryObj = context.get("category");
        if (categoryObj != null) {
            String category = categoryObj.toString();
            return rule.getApplicableExpenseCategories() == null ||
                   rule.getApplicableExpenseCategories().isEmpty() ||
                   rule.getApplicableExpenseCategories().contains(category);
        }
        return true;
    }

    private boolean evaluateDocumentRequired(ComplianceCheck check, ComplianceRule rule, Map<String, Object> context) {
        Object hasDocObj = context.get("hasDocument");
        return hasDocObj instanceof Boolean && (Boolean) hasDocObj;
    }

    private boolean evaluateTimeLimit(ComplianceCheck check, ComplianceRule rule, Map<String, Object> context) {
        // Implementation would check time constraints
        return true;
    }

    private boolean evaluateFrequencyLimit(ComplianceCheck check, ComplianceRule rule, Map<String, Object> context) {
        // Implementation would check frequency constraints
        return true;
    }

    private boolean evaluateCustomExpression(ComplianceCheck check, ComplianceRule rule, Map<String, Object> context) {
        // Implementation would evaluate custom expression
        return true;
    }

    private void publishEvents(ComplianceCheck check) {
        if (!check.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            eventPublisher.publishAll(new java.util.ArrayList<>(check.getDomainEvents()));
            check.clearDomainEvents();
        }
    }
}
