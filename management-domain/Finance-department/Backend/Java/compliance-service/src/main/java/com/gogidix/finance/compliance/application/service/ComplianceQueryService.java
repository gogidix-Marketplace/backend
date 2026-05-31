package com.gogidix.finance.compliance.application.service;

import com.gogidix.finance.compliance.domain.model.ComplianceCheck;
import com.gogidix.finance.compliance.domain.model.ComplianceReport;
import com.gogidix.finance.compliance.domain.model.ComplianceRule;
import com.gogidix.finance.compliance.domain.port.in.ComplianceQuery;
import com.gogidix.finance.compliance.domain.repository.ComplianceCheckRepository;
import com.gogidix.finance.compliance.domain.repository.ComplianceRuleRepository;
import com.gogidix.finance.compliance.shared.exception.NotFoundException;
import com.gogidix.finance.compliance.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Compliance Query Service
 * Handles all read operations for compliance data
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ComplianceQueryService {

    private final ComplianceRuleRepository ruleRepository;
    private final ComplianceCheckRepository checkRepository;

    // Rule Queries

    public ComplianceRule getRuleById(String ruleId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching compliance rule: {} for tenant: {}", ruleId, tenantId);

        return ruleRepository.findByRuleIdAndTenantId(ruleId, tenantId)
            .orElseThrow(() -> new NotFoundException("ComplianceRule", ruleId));
    }

    public Page<ComplianceRule> getRules(ComplianceRule.RuleType ruleType, ComplianceRule.RuleCategory category,
                                          ComplianceRule.RuleStatus status, Boolean enabled,
                                          String department, int page, int size,
                                          String sortBy, String sortDirection) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching compliance rules for tenant: {}", tenantId);

        List<ComplianceRule> rules;

        if (department != null && !department.isBlank()) {
            rules = ruleRepository.findByTenantIdAndApplicableDepartmentsContaining(tenantId, department);
        } else if (ruleType != null) {
            rules = ruleRepository.findByTenantIdAndRuleType(tenantId, ruleType);
        } else if (category != null) {
            rules = ruleRepository.findByTenantIdAndCategory(tenantId, category);
        } else if (status != null) {
            rules = ruleRepository.findByTenantIdAndStatus(tenantId, status);
        } else if (enabled != null) {
            rules = ruleRepository.findByTenantIdAndEnabled(tenantId, enabled);
        } else {
            rules = ruleRepository.findByTenantId(tenantId);
        }

        // Apply additional filters
        if (category != null && ruleType == null) {
            rules = rules.stream()
                .filter(r -> r.getCategory() == category)
                .collect(Collectors.toList());
        }

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return new PageImpl<>(rules, pageRequest, rules.size());
    }

    public List<ComplianceRule> getActiveRules() {
        String tenantId = RequestContextHolder.getTenantId();
        return ruleRepository.findActiveByTenantId(tenantId).stream()
            .filter(ComplianceRule::isCurrentlyEffective)
            .collect(Collectors.toList());
    }

    public List<ComplianceRule> getRulesByType(ComplianceRule.RuleType ruleType) {
        String tenantId = RequestContextHolder.getTenantId();
        return ruleRepository.findByTenantIdAndRuleType(tenantId, ruleType);
    }

    public List<ComplianceRule> searchRules(String searchTerm) {
        String tenantId = RequestContextHolder.getTenantId();
        return ruleRepository.searchByNameContaining(tenantId, searchTerm);
    }

    // Check Queries

    public ComplianceCheck getCheckById(String checkId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching compliance check: {} for tenant: {}", checkId, tenantId);

        return checkRepository.findByCheckIdAndTenantId(checkId, tenantId)
            .orElseThrow(() -> new NotFoundException("ComplianceCheck", checkId));
    }

    public Page<ComplianceCheck> getChecks(String ruleId, String entityType, String entityId,
                                            ComplianceCheck.CheckResult result,
                                            ComplianceCheck.SeverityLevel severity,
                                            Boolean requiresAction,
                                            LocalDate startDate, LocalDate endDate,
                                            int page, int size, String sortBy, String sortDirection) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching compliance checks for tenant: {}", tenantId);

        List<ComplianceCheck> checks;

        if (entityId != null && entityType != null) {
            checks = checkRepository.findByTenantIdAndEntityTypeAndEntityId(tenantId, entityType, entityId);
        } else if (ruleId != null) {
            checks = checkRepository.findByTenantIdAndRuleId(tenantId, ruleId);
        } else if (result != null) {
            checks = checkRepository.findByTenantIdAndResult(tenantId, result);
        } else if (severity != null) {
            checks = checkRepository.findByTenantIdAndSeverity(tenantId, severity);
        } else {
            checks = checkRepository.findByTenantId(tenantId);
        }

        // Apply date range filter
        if (startDate != null && endDate != null) {
            java.time.Instant start = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
            java.time.Instant end = endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();
            checks = checks.stream()
                .filter(c -> c.getEvaluatedAt() != null &&
                           !c.getEvaluatedAt().isBefore(start) &&
                           !c.getEvaluatedAt().isAfter(end))
                .collect(Collectors.toList());
        }

        // Apply requires action filter
        if (Boolean.TRUE.equals(requiresAction)) {
            checks = checks.stream()
                .filter(ComplianceCheck::requiresAction)
                .collect(Collectors.toList());
        }

        // Apply additional result filter if not already applied
        if (result != null && ruleId != null) {
            final ComplianceCheck.CheckResult finalResult = result;
            checks = checks.stream()
                .filter(c -> c.getResult() == finalResult)
                .collect(Collectors.toList());
        }

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return new PageImpl<>(checks, pageRequest, checks.size());
    }

    public List<ComplianceCheck> getViolations(ComplianceCheck.SeverityLevel minSeverity,
                                                 Boolean includeWaived, Boolean includeRemediated) {
        String tenantId = RequestContextHolder.getTenantId();

        List<ComplianceCheck> violations = minSeverity != null
            ? checkRepository.findViolationsByTenantIdAndSeverityGreaterThanEqual(tenantId, minSeverity)
            : checkRepository.findViolationsByTenantId(tenantId);

        if (Boolean.FALSE.equals(includeWaived)) {
            violations = violations.stream()
                .filter(c -> !Boolean.TRUE.equals(c.getWaived()))
                .collect(Collectors.toList());
        }

        if (Boolean.FALSE.equals(includeRemediated)) {
            violations = violations.stream()
                .filter(c -> c.getRemediationCompletedAt() == null)
                .collect(Collectors.toList());
        }

        return violations;
    }

    public List<ComplianceCheck> getPendingRemediation(String assignedTo, java.time.LocalDate dueBefore) {
        String tenantId = RequestContextHolder.getTenantId();

        List<ComplianceCheck> checks = assignedTo != null
            ? checkRepository.findPendingRemediationByTenantIdAndAssignedTo(tenantId, assignedTo)
            : checkRepository.findPendingRemediationByTenantId(tenantId);

        if (dueBefore != null) {
            java.time.Instant dueBeforeInstant = dueBefore.atTime(23, 59, 59)
                .atZone(ZoneId.systemDefault()).toInstant();
            checks = checks.stream()
                .filter(c -> c.getRemediationDueDate() != null &&
                           !c.getRemediationDueDate().isAfter(dueBeforeInstant))
                .collect(Collectors.toList());
        }

        return checks;
    }

    public List<ComplianceCheck> getChecksByEntity(String entityType, String entityId) {
        String tenantId = RequestContextHolder.getTenantId();
        return checkRepository.findByTenantIdAndEntityTypeAndEntityId(tenantId, entityType, entityId);
    }

    // Summary Queries

    public ComplianceSummary getSummary(LocalDate startDate, LocalDate endDate, String department, String costCenter) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching compliance summary for tenant: {}", tenantId);

        List<ComplianceCheck> checks;

        if (startDate != null && endDate != null) {
            java.time.Instant start = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
            java.time.Instant end = endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();
            checks = checkRepository.findByTenantIdAndEvaluatedAtBetween(tenantId, start, end);
        } else {
            checks = checkRepository.findByTenantId(tenantId);
        }

        // Apply filters
        if (department != null) {
            checks = checks.stream()
                .filter(c -> department.equals(c.getDepartment()))
                .collect(Collectors.toList());
        }

        if (costCenter != null) {
            checks = checks.stream()
                .filter(c -> costCenter.equals(c.getCostCenter()))
                .collect(Collectors.toList());
        }

        long total = checks.size();
        long compliant = checks.stream().filter(c -> c.getResult() == ComplianceCheck.CheckResult.COMPLIANT).count();
        long nonCompliant = checks.stream().filter(c -> c.getResult() == ComplianceCheck.CheckResult.NON_COMPLIANT).count();
        long warnings = checks.stream().filter(c -> c.getResult() == ComplianceCheck.CheckResult.WARNING).count();
        long notApplicable = checks.stream().filter(c -> c.getResult() == ComplianceCheck.CheckResult.NOT_APPLICABLE).count();

        long activeViolations = checks.stream()
            .filter(c -> (c.getResult() == ComplianceCheck.CheckResult.NON_COMPLIANT ||
                         c.getResult() == ComplianceCheck.CheckResult.WARNING) &&
                         !Boolean.TRUE.equals(c.getWaived()) &&
                         c.getRemediationCompletedAt() == null)
            .count();

        long pendingRemediation = checks.stream()
            .filter(c -> Boolean.TRUE.equals(c.getRemediationRequired()) &&
                         c.getRemediationCompletedAt() == null)
            .count();

        double compliancePercentage = total > 0 ? (double) compliant / (total - notApplicable) * 100 : 0;

        return new ComplianceSummary(
            total,
            compliant,
            nonCompliant,
            warnings,
            notApplicable,
            activeViolations,
            pendingRemediation,
            compliancePercentage
        );
    }

    public long countByStatus(ComplianceRule.RuleStatus status) {
        String tenantId = RequestContextHolder.getTenantId();
        return ruleRepository.countByTenantIdAndStatus(tenantId, status);
    }

    public long countByResult(ComplianceCheck.CheckResult result) {
        String tenantId = RequestContextHolder.getTenantId();
        return checkRepository.countByTenantIdAndResult(tenantId, result);
    }

    public record ComplianceSummary(
        long totalChecks,
        long compliantCount,
        long nonCompliantCount,
        long warningCount,
        long notApplicableCount,
        long activeViolations,
        long pendingRemediation,
        double compliancePercentage
    ) {}
}
