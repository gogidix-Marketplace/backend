package com.gogidix.hr.globalcompliance.application.service;

import com.gogidix.hr.globalcompliance.application.dto.response.*;
import com.gogidix.hr.globalcompliance.domain.repository.*;
import com.gogidix.hr.globalcompliance.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dashboard Service
 * Handles dashboard and analytics operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final ComplianceRequirementRepository requirementRepository;
    private final ComplianceCheckRepository checkRepository;
    private final NonComplianceIssueRepository issueRepository;
    private final ComplianceReportRepository reportRepository;
    private final AuditTrailRepository auditTrailRepository;

    /**
     * Get compliance dashboard summary
     */
    public DashboardSummaryDto getSummary() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching dashboard summary for tenant: {}", tenantId);

        long totalRequirements = requirementRepository.countByTenantId(tenantId);
        long activeRequirements = requirementRepository.countByTenantIdAndActive(tenantId, true);
        long totalChecks = checkRepository.countByTenantId(tenantId);
        long pendingChecks = checkRepository.countByTenantIdAndStatus(tenantId, "PENDING");
        long overdueChecks = checkRepository.countOverdueChecks(tenantId);
        long totalIssues = issueRepository.countByTenantId(tenantId);
        long openIssues = issueRepository.countOpenIssues(tenantId);
        long criticalIssues = issueRepository.countByTenantIdAndSeverity(tenantId, "CRITICAL");
        long overdueIssues = issueRepository.countOverdueIssues(tenantId);
        Double averageScore = reportRepository.getAverageComplianceScore(tenantId);

        return DashboardSummaryDto.builder()
                .totalRequirements(totalRequirements)
                .activeRequirements(activeRequirements)
                .totalChecks(totalChecks)
                .pendingChecks(pendingChecks)
                .overdueChecks(overdueChecks)
                .totalIssues(totalIssues)
                .openIssues(openIssues)
                .criticalIssues(criticalIssues)
                .overdueIssues(overdueIssues)
                .averageComplianceScore(averageScore != null ? averageScore : 0.0)
                .build();
    }

    /**
     * Get compliance dashboard by country
     */
    public CountryDashboardDto getCountryDashboard(String countryCode) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching country dashboard for: {} in tenant: {}", countryCode, tenantId);

        long totalRequirements = requirementRepository.countByTenantIdAndCountryCode(tenantId, countryCode);
        long activeRequirements = requirementRepository.findByTenantIdAndCountryCodeAndActive(tenantId, countryCode, true).size();
        List<com.gogidix.hr.globalcompliance.domain.model.ComplianceCheck> checks =
                checkRepository.findByTenantIdAndCountryCode(tenantId, countryCode);
        List<com.gogidix.hr.globalcompliance.domain.model.NonComplianceIssue> issues =
                issueRepository.findByTenantIdAndCountryCode(tenantId, countryCode);
        List<com.gogidix.hr.globalcompliance.domain.model.ComplianceReport> reports =
                reportRepository.findByTenantIdAndCountryCode(tenantId, countryCode);

        long totalChecks = checks.size();
        long pendingChecks = checks.stream().filter(c -> "PENDING".equals(c.getStatus())).count();
        long overdueChecks = checks.stream().filter(c -> {
            if (c.getScheduledDate() == null) return false;
            return LocalDate.now().isAfter(c.getScheduledDate()) &&
                   !"PASSED".equals(c.getStatus()) && !"FAILED".equals(c.getStatus()) &&
                   !"WAIVED".equals(c.getStatus()) && !"NOT_APPLICABLE".equals(c.getStatus());
        }).count();

        long totalIssues = issues.size();
        long openIssues = issues.stream().filter(i -> "OPEN".equals(i.getStatus()) ||
                                                       "IN_PROGRESS".equals(i.getStatus()) ||
                                                       "ESCALATED".equals(i.getStatus())).count();
        long criticalIssues = issues.stream().filter(i -> "CRITICAL".equals(i.getSeverity()) &&
                                                         !"RESOLVED".equals(i.getStatus()) &&
                                                         !"CLOSED".equals(i.getStatus())).count();

        Double averageScore = reports.stream()
                .filter(r -> r.getComplianceScore() != null)
                .mapToDouble(r -> r.getComplianceScore() / Math.max(1, reports.size()))
                .sum();

        return CountryDashboardDto.builder()
                .countryCode(countryCode)
                .totalRequirements(totalRequirements)
                .activeRequirements(activeRequirements)
                .totalChecks(totalChecks)
                .pendingChecks(pendingChecks)
                .overdueChecks(overdueChecks)
                .totalIssues(totalIssues)
                .openIssues(openIssues)
                .criticalIssues(criticalIssues)
                .averageComplianceScore(averageScore != null ? averageScore : 0.0)
                .build();
    }

    /**
     * Get critical issues overview
     */
    public List<CriticalIssueDto> getCriticalIssues() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching critical issues for tenant: {}", tenantId);

        List<com.gogidix.hr.globalcompliance.domain.model.NonComplianceIssue> criticalIssues =
                issueRepository.findCriticalIssues(tenantId);

        return criticalIssues.stream()
                .map(issue -> CriticalIssueDto.builder()
                        .issueId(issue.getIssueId())
                        .issueNumber(issue.getIssueNumber())
                        .title(issue.getTitle())
                        .severity(issue.getSeverity())
                        .status(issue.getStatus())
                        .countryCode(issue.getCountryCode())
                        .identifiedDate(issue.getIdentifiedDate())
                        .dueDate(issue.getDueDate())
                        .assignedTo(issue.getAssignedTo())
                        .assignedToName(issue.getAssignedToName())
                        .daysUntilDue(issue.getDaysUntilDue())
                        .requiresImmediateAttention(issue.requiresImmediateAttention())
                        .build())
                .toList();
    }

    /**
     * Get upcoming compliance checks
     */
    public List<UpcomingCheckDto> getUpcomingChecks() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching upcoming checks for tenant: {}", tenantId);

        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusWeeks(1);

        List<com.gogidix.hr.globalcompliance.domain.model.ComplianceCheck> upcomingChecks =
                checkRepository.findUpcomingChecks(tenantId, today, nextWeek);

        return upcomingChecks.stream()
                .map(check -> UpcomingCheckDto.builder()
                        .checkId(check.getCheckId())
                        .checkNumber(check.getCheckNumber())
                        .requirementId(check.getRequirementId())
                        .requirementName(check.getRequirementName())
                        .scheduledDate(check.getScheduledDate())
                        .status(check.getStatus())
                        .countryCode(check.getCountryCode())
                        .daysUntilScheduled(check.getDaysUntilScheduled())
                        .build())
                .toList();
    }

    /**
     * Get overall compliance score
     */
    public ComplianceScoreDto getComplianceScore() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching compliance score for tenant: {}", tenantId);

        Double averageScore = reportRepository.getAverageComplianceScore(tenantId);

        String rating;
        if (averageScore == null) {
            rating = "NOT_RATED";
        } else if (averageScore >= 95) {
            rating = "EXCELLENT";
        } else if (averageScore >= 85) {
            rating = "GOOD";
        } else if (averageScore >= 70) {
            rating = "SATISFACTORY";
        } else if (averageScore >= 50) {
            rating = "NEEDS_IMPROVEMENT";
        } else {
            rating = "POOR";
        }

        long passedChecks = checkRepository.countByTenantIdAndResult(tenantId, "COMPLIANT");
        long failedChecks = checkRepository.countByTenantIdAndResult(tenantId, "NON_COMPLIANT");
        long partialChecks = checkRepository.countByTenantIdAndResult(tenantId, "PARTIALLY_COMPLIANT");
        long totalChecksCount = passedChecks + failedChecks + partialChecks;

        return ComplianceScoreDto.builder()
                .overallScore(averageScore != null ? averageScore : 0.0)
                .rating(rating)
                .passedChecks(passedChecks)
                .failedChecks(failedChecks)
                .partialChecks(partialChecks)
                .totalChecks(totalChecksCount)
                .compliancePercentage(totalChecksCount > 0 ? (passedChecks * 100.0 / totalChecksCount) : 0.0)
                .build();
    }

    /**
     * Get compliance by category breakdown
     */
    public Map<String, CategoryComplianceDto> getComplianceByCategory() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching compliance by category for tenant: {}", tenantId);

        Map<String, CategoryComplianceDto> result = new HashMap<>();

        String[] categories = {"LABOR_LAW", "DATA_PRIVACY", "HEALTH_SAFETY", "IMMIGRATION",
                              "EQUALITY", "ANTI_CORRUPTION", "FINANCIAL", "ENVIRONMENTAL"};

        for (String category : categories) {
            long totalRequirements = requirementRepository.countByTenantIdAndCategory(tenantId, category);
            List<com.gogidix.hr.globalcompliance.domain.model.ComplianceRequirement> requirements =
                    requirementRepository.findByTenantIdAndCategory(tenantId, category);

            long activeRequirements = requirements.stream()
                    .filter(com.gogidix.hr.globalcompliance.domain.model.ComplianceRequirement::isCurrentlyActive)
                    .count();

            List<com.gogidix.hr.globalcompliance.domain.model.ComplianceCheck> categoryChecks = requirements.stream()
                    .flatMap(r -> checkRepository.findByTenantIdAndRequirementId(tenantId, r.getRequirementId()).stream())
                    .toList();

            long passedChecks = categoryChecks.stream()
                    .filter(c -> "PASSED".equals(c.getStatus()) || "COMPLIANT".equals(c.getResult()))
                    .count();

            long totalChecks = categoryChecks.size();

            Double complianceScore = totalChecks > 0 ? (passedChecks * 100.0 / totalChecks) : 0.0;

            result.put(category, CategoryComplianceDto.builder()
                    .category(category)
                    .totalRequirements(totalRequirements)
                    .activeRequirements(activeRequirements)
                    .totalChecks(totalChecks)
                    .passedChecks(passedChecks)
                    .complianceScore(complianceScore)
                    .build());
        }

        return result;
    }

    /**
     * Get trends data
     */
    public Map<String, Object> getTrends(LocalDate startDate, LocalDate endDate) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching trends from {} to {} for tenant: {}", startDate, endDate, tenantId);

        Map<String, Object> trends = new HashMap<>();

        List<com.gogidix.hr.globalcompliance.domain.model.ComplianceCheck> checksInPeriod =
                checkRepository.findByTenantIdAndCompletedDateBetween(tenantId, startDate, endDate);

        long passedInPeriod = checksInPeriod.stream()
                .filter(c -> "COMPLIANT".equals(c.getResult()))
                .count();
        long failedInPeriod = checksInPeriod.stream()
                .filter(c -> "NON_COMPLIANT".equals(c.getResult()))
                .count();

        trends.put("periodChecks", checksInPeriod.size());
        trends.put("passedInPeriod", passedInPeriod);
        trends.put("failedInPeriod", failedInPeriod);

        List<com.gogidix.hr.globalcompliance.domain.model.NonComplianceIssue> resolvedIssues =
                issueRepository.findResolvedIssuesBetweenDates(tenantId, startDate, endDate);

        trends.put("resolvedIssues", resolvedIssues.size());
        trends.put("averageResolutionTime", resolvedIssues.stream()
                .mapToDouble(com.gogidix.hr.globalcompliance.domain.model.NonComplianceIssue::getDaysToResolve)
                .average()
                .orElse(0.0));

        List<com.gogidix.hr.globalcompliance.domain.model.AuditTrail> audits =
                auditTrailRepository.findByTenantIdAndActionDateBetween(tenantId, startDate, endDate);

        Map<String, Long> actionsByType = new HashMap<>();
        actionsByType.put("CREATED", audits.stream().filter(a -> "CREATED".equals(a.getAction())).count());
        actionsByType.put("UPDATED", audits.stream().filter(a -> "UPDATED".equals(a.getAction())).count());
        actionsByType.put("DELETED", audits.stream().filter(a -> "DELETED".equals(a.getAction())).count());
        actionsByType.put("RESOLVED", audits.stream().filter(a -> "RESOLVED".equals(a.getAction())).count());

        trends.put("actionsByType", actionsByType);

        return trends;
    }
}
