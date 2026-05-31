package com.gogidix.finance.compliance.application.service;

import com.gogidix.finance.compliance.domain.model.ComplianceRule;
import com.gogidix.finance.compliance.domain.port.in.ComplianceRuleCommand;
import com.gogidix.finance.compliance.domain.port.out.EventPublisher;
import com.gogidix.finance.compliance.domain.repository.ComplianceRuleRepository;
import com.gogidix.finance.compliance.shared.exception.ConflictException;
import com.gogidix.finance.compliance.shared.exception.NotFoundException;
import com.gogidix.finance.compliance.shared.exception.ValidationException;
import com.gogidix.finance.compliance.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Compliance Rule Command Service
 * Handles all write operations for compliance rules
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ComplianceRuleService {

    private final ComplianceRuleRepository ruleRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public ComplianceRule create(ComplianceRuleCommand.CreateComplianceRuleCommand command) {
        log.info("Creating compliance rule: {} for tenant: {}", command.getName(), command.getTenantId());

        validateRuleName(command.getName(), command.getTenantId());

        ComplianceRule rule = ComplianceRule.create(
            command.getTenantId(),
            command.getName(),
            command.getDescription(),
            command.getRuleType(),
            command.getCategory(),
            command.getSeverity(),
            command.getCreatedByUserId()
        );

        // Set additional fields
        rule.setRuleId("CR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        rule.setParameters(command.getParameters());
        rule.setConditionExpression(command.getConditionExpression());

        if (command.getThresholdAmount() != null) {
            rule.setThresholdAmount(command.getThresholdAmount());
            rule.setThresholdCurrency(command.getThresholdCurrency());
        }

        if (command.getEffectiveFrom() != null) {
            rule.setEffectiveFrom(command.getEffectiveFrom());
        }

        if (command.getEffectiveTo() != null) {
            rule.setEffectiveTo(command.getEffectiveTo());
        }

        if (command.getApprovalRequiredBy() != null) {
            rule.setApprovalRequiredBy(command.getApprovalRequiredBy());
        }

        if (command.getAutoApproveThreshold() != null) {
            rule.setAutoApproveThreshold(command.getAutoApproveThreshold());
        }

        if (command.getPriority() != null) {
            rule.setPriority(command.getPriority());
        }

        // Set applicable scopes
        if (command.getApplicableDepartments() != null) {
            command.getApplicableDepartments().forEach(rule::addApplicableDepartment);
        }

        if (command.getApplicableExpenseCategories() != null) {
            command.getApplicableExpenseCategories().forEach(rule::addApplicableExpenseCategory);
        }

        if (command.getTags() != null) {
            command.getTags().forEach(rule::addTag);
        }

        rule.setNotes(command.getNotes());

        ComplianceRule savedRule = ruleRepository.save(rule);
        publishEvents(savedRule);

        log.info("Created compliance rule: {} for tenant: {}", savedRule.getRuleId(), command.getTenantId());
        return savedRule;
    }

    @Transactional
    public ComplianceRule update(ComplianceRuleCommand.UpdateComplianceRuleCommand command) {
        log.info("Updating compliance rule: {} for tenant: {}", command.getRuleId(), command.getTenantId());

        ComplianceRule rule = ruleRepository.findByRuleIdAndTenantId(
            command.getRuleId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("ComplianceRule", command.getRuleId()));

        if (rule.getStatus() == ComplianceRule.RuleStatus.ACTIVE) {
            throw new ValidationException("Cannot modify active rules. Deactivate first.");
        }

        // Update fields
        if (command.getName() != null && !command.getName().equals(rule.getName())) {
            validateRuleName(command.getName(), command.getTenantId());
            rule.setName(command.getName());
        }

        if (command.getDescription() != null) {
            rule.setDescription(command.getDescription());
        }

        if (command.getParameters() != null) {
            rule.updateParameters(command.getParameters(), command.getModifiedByUserId());
        }

        if (command.getThresholdAmount() != null) {
            rule.setThreshold(command.getThresholdAmount(),
                command.getThresholdCurrency() != null ? command.getThresholdCurrency() : rule.getThresholdCurrency(),
                command.getModifiedByUserId());
        }

        if (command.getConditionExpression() != null) {
            rule.setConditionExpression(command.getConditionExpression());
        }

        if (command.getEffectiveFrom() != null) {
            rule.setEffectiveFrom(command.getEffectiveFrom());
        }

        if (command.getEffectiveTo() != null) {
            rule.setEffectiveTo(command.getEffectiveTo());
        }

        if (command.getPriority() != null) {
            rule.setPriority(command.getPriority());
        }

        if (command.getTags() != null) {
            rule.setTags(command.getTags());
        }

        if (command.getNotes() != null) {
            rule.setNotes(command.getNotes());
        }

        rule.setLastModifiedByUserId(command.getModifiedByUserId());

        ComplianceRule savedRule = ruleRepository.save(rule);
        publishEvents(savedRule);

        log.info("Updated compliance rule: {}", command.getRuleId());
        return savedRule;
    }

    @Transactional
    public void activate(ComplianceRuleCommand.ActivateRuleCommand command) {
        log.info("Activating compliance rule: {} for tenant: {}", command.getRuleId(), command.getTenantId());

        ComplianceRule rule = ruleRepository.findByRuleIdAndTenantId(
            command.getRuleId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("ComplianceRule", command.getRuleId()));

        rule.activate(command.getActivatedByUserId());
        ruleRepository.save(rule);
        publishEvents(rule);

        log.info("Activated compliance rule: {}", command.getRuleId());
    }

    @Transactional
    public void deactivate(ComplianceRuleCommand.DeactivateRuleCommand command) {
        log.info("Deactivating compliance rule: {} for tenant: {}", command.getRuleId(), command.getTenantId());

        ComplianceRule rule = ruleRepository.findByRuleIdAndTenantId(
            command.getRuleId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("ComplianceRule", command.getRuleId()));

        rule.deactivate(command.getDeactivatedByUserId());
        ruleRepository.save(rule);
        publishEvents(rule);

        log.info("Deactivated compliance rule: {}", command.getRuleId());
    }

    @Transactional
    public void delete(ComplianceRuleCommand.DeleteRuleCommand command) {
        log.info("Deleting compliance rule: {} for tenant: {}", command.getRuleId(), command.getTenantId());

        ComplianceRule rule = ruleRepository.findByRuleIdAndTenantId(
            command.getRuleId(), command.getTenantId())
            .orElseThrow(() -> new NotFoundException("ComplianceRule", command.getRuleId()));

        if (rule.getStatus() == ComplianceRule.RuleStatus.ACTIVE) {
            throw new ValidationException("Cannot delete active rules. Deactivate first.");
        }

        ruleRepository.deleteByRuleIdAndTenantId(command.getRuleId(), command.getTenantId());

        log.info("Deleted compliance rule: {}", command.getRuleId());
    }

    private void validateRuleName(String name, String tenantId) {
        List<ComplianceRule> existingRules = ruleRepository.findByTenantId(tenantId);
        boolean nameExists = existingRules.stream()
            .anyMatch(r -> r.getName().equalsIgnoreCase(name));

        if (nameExists) {
            throw new ConflictException("ComplianceRule", "name: " + name);
        }
    }

    private void publishEvents(ComplianceRule rule) {
        if (!rule.getDomainEvents().isEmpty() && eventPublisher.isReady()) {
            eventPublisher.publishAll(new java.util.ArrayList<>(rule.getDomainEvents()));
            rule.clearDomainEvents();
        }
    }
}
