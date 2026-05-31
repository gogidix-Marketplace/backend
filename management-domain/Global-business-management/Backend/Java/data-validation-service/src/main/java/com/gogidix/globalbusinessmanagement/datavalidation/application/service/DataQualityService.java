package com.gogidix.globalbusinessmanagement.datavalidation.application.service;

import com.gogidix.globalbusinessmanagement.datavalidation.domain.dto.DataQualityReportDTO;
import com.gogidix.globalbusinessmanagement.datavalidation.domain.model.DataQualityReport;
import com.gogidix.globalbusinessmanagement.datavalidation.domain.model.ValidationResult;
import com.gogidix.globalbusinessmanagement.datavalidation.domain.repository.DataQualityReportRepository;
import com.gogidix.globalbusinessmanagement.datavalidation.domain.repository.ValidationResultRepository;
import com.gogidix.globalbusinessmanagement.datavalidation.infrastructure.config.ValidationConfig;
import com.gogidix.globalbusinessmanagement.datavalidation.infrastructure.mapper.DataQualityReportMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for generating and managing data quality reports
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DataQualityService {

    private final DataQualityReportRepository reportRepository;
    private final ValidationResultRepository resultRepository;
    private final DataQualityReportMapper reportMapper;
    private final ValidationConfig config;

    /**
     * Generate a data quality report for an entity
     */
    @Transactional
    public DataQualityReportDTO generateEntityReport(String entityType, String entityId,
                                                      String tenantId, String generatedBy) {
        log.info("Generating quality report for entity: {} - {}", entityType, entityId);

        String reportId = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        // Get validation results for the entity
        List<ValidationResult> results = resultRepository
                .findByEntityTypeAndEntityIdOrderByValidatedAtDesc(entityType, entityId);

        // Build the report
        DataQualityReport report = buildReport(reportId, entityType, entityId, tenantId,
                DataQualityReport.ReportType.ENTITY, results, now, generatedBy);

        DataQualityReport saved = reportRepository.save(report);
        log.info("Generated quality report {} with score: {}", reportId, saved.getQualityScore());

        return reportMapper.toDto(saved);
    }

    /**
     * Generate a data quality report for a dataset
     */
    @Transactional
    public DataQualityReportDTO generateDatasetReport(String dataSource, String dataSourceType,
                                                       LocalDateTime periodStart, LocalDateTime periodEnd,
                                                       String tenantId, String generatedBy) {
        log.info("Generating quality report for dataset: {}", dataSource);

        String reportId = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        // Get validation results for the period
        List<ValidationResult> results = resultRepository
                .findByValidatedAtBetweenOrderByValidatedAtDesc(periodStart, periodEnd);

        // Build the report
        DataQualityReport report = buildReport(reportId, dataSourceType, dataSource, tenantId,
                DataQualityReport.ReportType.DATA_SOURCE, results, now, generatedBy);
        report.setDataSource(dataSource);
        report.setDataSourceType(dataSourceType);
        report.setPeriodStart(periodStart);
        report.setPeriodEnd(periodEnd);

        DataQualityReport saved = reportRepository.save(report);
        log.info("Generated dataset quality report {} with score: {}", reportId, saved.getQualityScore());

        return reportMapper.toDto(saved);
    }

    /**
     * Generate a tenant-wide quality report
     */
    @Transactional
    public DataQualityReportDTO generateTenantReport(String tenantId, LocalDateTime periodStart,
                                                     LocalDateTime periodEnd, String generatedBy) {
        log.info("Generating tenant quality report for: {}", tenantId);

        String reportId = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        // Get all validation results for the tenant
        List<ValidationResult> results = resultRepository.findByTenantIdOrderByValidatedAtDesc(tenantId, null)
                .getContent()
                .stream()
                .filter(r -> !r.getValidatedAt().isBefore(periodStart) && !r.getValidatedAt().isAfter(periodEnd))
                .collect(Collectors.toList());

        // Build the report
        DataQualityReport report = buildReport(reportId, "TENANT", tenantId, tenantId,
                DataQualityReport.ReportType.TENANT, results, now, generatedBy);
        report.setPeriodStart(periodStart);
        report.setPeriodEnd(periodEnd);

        DataQualityReport saved = reportRepository.save(report);
        log.info("Generated tenant quality report {} with score: {}", reportId, saved.getQualityScore());

        return reportMapper.toDto(saved);
    }

    /**
     * Build a quality report from validation results
     */
    private DataQualityReport buildReport(String reportId, String entityType, String entityId,
                                         String tenantId, DataQualityReport.ReportType reportType,
                                         List<ValidationResult> results, LocalDateTime now, String generatedBy) {
        DataQualityReport.DataQualityReportBuilder builder = DataQualityReport.builder()
                .reportId(reportId)
                .entityType(entityType)
                .entityId(entityId)
                .tenantId(tenantId)
                .reportType(reportType)
                .reportDate(now)
                .generatedBy(generatedBy)
                .generatedAt(now);

        if (results.isEmpty()) {
            return builder
                    .totalRecords(0)
                    .validRecords(0)
                    .invalidRecords(0)
                    .pendingRecords(0)
                    .qualityScore(0.0)
                    .qualityLevel(DataQualityReport.QualityLevel.UNKNOWN)
                    .build();
        }

        // Calculate basic metrics
        long total = results.size();
        long passed = results.stream().filter(ValidationResult::isPassed).count();
        long failed = results.stream().filter(r -> !r.isPassed()).count();

        // Calculate quality score
        double score = (passed * 100.0) / total;

        builder.totalRecords((int) total)
                .validRecords((int) passed)
                .invalidRecords((int) failed)
                .qualityScore(score)
                .qualityLevel(DataQualityReport.QualityLevel.fromScore((int) score));

        // Build field metrics
        Map<String, DataQualityReport.FieldQualityMetrics> fieldMetrics = buildFieldMetrics(results);
        builder.fieldMetrics(fieldMetrics);

        // Build rule summaries
        List<DataQualityReport.RuleExecutionSummary> ruleSummaries = buildRuleSummaries(results);
        builder.ruleSummaries(ruleSummaries);

        // Build top issues
        List<DataQualityReport.QualityIssue> topIssues = buildTopIssues(results);
        builder.topIssues(topIssues);

        // Build error distribution
        Map<String, Integer> errorDistribution = buildErrorDistribution(results);
        builder.errorDistribution(errorDistribution);

        // Generate recommendations
        List<DataQualityReport.QualityRecommendation> recommendations = generateRecommendations(score, results);
        builder.recommendations(recommendations);

        return builder.build();
    }

    /**
     * Build field-level quality metrics
     */
    private Map<String, DataQualityReport.FieldQualityMetrics> buildFieldMetrics(List<ValidationResult> results) {
        Map<String, List<ValidationResult>> groupedByField = results.stream()
                .filter(r -> r.getFieldName() != null)
                .collect(Collectors.groupingBy(ValidationResult::getFieldName));

        Map<String, DataQualityReport.FieldQualityMetrics> metrics = new HashMap<>();

        for (Map.Entry<String, List<ValidationResult>> entry : groupedByField.entrySet()) {
            String field = entry.getKey();
            List<ValidationResult> fieldResults = entry.getValue();

            int total = fieldResults.size();
            int invalid = (int) fieldResults.stream().filter(r -> !r.isPassed()).count();
            int nullCount = (int) fieldResults.stream()
                    .filter(r -> r.getActualValue() == null || r.getActualValue().isEmpty()).count();

            double completeness = ((total - nullCount) * 100.0) / total;
            double validity = ((total - invalid) * 100.0) / total;

            metrics.put(field, DataQualityReport.FieldQualityMetrics.builder()
                    .fieldName(field)
                    .totalRecords(total)
                    .nullCount(nullCount)
                    .invalidCount(invalid)
                    .completeness(completeness)
                    .validity(validity)
                    .consistency(completeness * validity / 100)
                    .topErrors(fieldResults.stream()
                            .filter(r -> !r.isPassed())
                            .map(ValidationResult::getErrorMessage)
                            .limit(5)
                            .collect(Collectors.toList()))
                    .build());
        }

        return metrics;
    }

    /**
     * Build rule execution summaries
     */
    private List<DataQualityReport.RuleExecutionSummary> buildRuleSummaries(List<ValidationResult> results) {
        return results.stream()
                .collect(Collectors.groupingBy(ValidationResult::getRuleCode))
                .entrySet().stream()
                .map(entry -> {
                    String ruleCode = entry.getKey();
                    List<ValidationResult> ruleResults = entry.getValue();

                    int executed = ruleResults.size();
                    int passed = (int) ruleResults.stream().filter(ValidationResult::isPassed).count();
                    int failed = executed - passed;
                    double passRate = (passed * 100.0) / executed;

                    ValidationResult first = ruleResults.get(0);

                    return DataQualityReport.RuleExecutionSummary.builder()
                            .ruleCode(ruleCode)
                            .ruleName(first.getRuleName())
                            .executedCount(executed)
                            .passedCount(passed)
                            .failedCount(failed)
                            .passRate(passRate)
                            .severity(first.getSeverity())
                            .build();
                })
                .sorted(Comparator.comparingDouble(DataQualityReport.RuleExecutionSummary::getPassRate))
                .collect(Collectors.toList());
    }

    /**
     * Build top quality issues
     */
    private List<DataQualityReport.QualityIssue> buildTopIssues(List<ValidationResult> results) {
        Map<String, Long> errorCounts = results.stream()
                .filter(r -> !r.isPassed() && r.getErrorMessage() != null)
                .collect(Collectors.groupingBy(
                        r -> r.getFieldName() + "|" + r.getRuleCode(),
                        Collectors.counting()
                ));

        return errorCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .map(entry -> {
                    String[] parts = entry.getKey().split("\\|");
                    String fieldName = parts.length > 0 ? parts[0] : "unknown";
                    String ruleCode = parts.length > 1 ? parts[1] : "unknown";

                    List<ValidationResult> issueResults = results.stream()
                            .filter(r -> fieldName.equals(r.getFieldName()) && ruleCode.equals(r.getRuleCode()))
                            .filter(r -> !r.isPassed())
                            .toList();

                    long count = entry.getValue();
                    double percentage = (count * 100.0) / results.size();
                    ValidationResult first = issueResults.get(0);

                    return DataQualityReport.QualityIssue.builder()
                            .issueCode(ruleCode + "_" + fieldName)
                            .description(first.getErrorMessage())
                            .fieldName(fieldName)
                            .entityType(first.getEntityType())
                            .occurrenceCount((int) count)
                            .percentage(percentage)
                            .severity(first.getSeverity())
                            .recommendation(getRecommendationForIssue(first))
                            .build();
                })
                .collect(Collectors.toList());
    }

    /**
     * Build error distribution
     */
    private Map<String, Integer> buildErrorDistribution(List<ValidationResult> results) {
        Map<String, Integer> distribution = new HashMap<>();

        distribution.put("CRITICAL", (int) results.stream()
                .filter(r -> r.getSeverity() == com.gogidix.globalbusinessmanagement.datavalidation.domain.model.ValidationRule.SeverityLevel.CRITICAL)
                .filter(r -> !r.isPassed())
                .count());

        distribution.put("HIGH", (int) results.stream()
                .filter(r -> r.getSeverity() == com.gogidix.globalbusinessmanagement.datavalidation.domain.model.ValidationRule.SeverityLevel.HIGH)
                .filter(r -> !r.isPassed())
                .count());

        distribution.put("MEDIUM", (int) results.stream()
                .filter(r -> r.getSeverity() == com.gogidix.globalbusinessmanagement.datavalidation.domain.model.ValidationRule.SeverityLevel.MEDIUM)
                .filter(r -> !r.isPassed())
                .count());

        distribution.put("LOW", (int) results.stream()
                .filter(r -> r.getSeverity() == com.gogidix.globalbusinessmanagement.datavalidation.domain.model.ValidationRule.SeverityLevel.LOW)
                .filter(r -> !r.isPassed())
                .count());

        return distribution;
    }

    /**
     * Get recommendation for a specific issue
     */
    private String getRecommendationForIssue(ValidationResult result) {
        if (result.getActualValue() == null || result.getActualValue().isEmpty()) {
            return "Ensure this field is populated with valid data";
        }

        return switch (result.getSeverity()) {
            case CRITICAL -> "Immediate action required to fix data quality issue";
            case HIGH -> "High priority - address this issue soon";
            case MEDIUM -> "Plan to resolve this issue in the next iteration";
            case LOW -> "Low priority - consider fixing when convenient";
            default -> "Review and correct if necessary";
        };
    }

    /**
     * Generate recommendations based on quality score and issues
     */
    private List<DataQualityReport.QualityRecommendation> generateRecommendations(double score,
                                                                                  List<ValidationResult> results) {
        List<DataQualityReport.QualityRecommendation> recommendations = new ArrayList<>();

        if (score < config.getQuality().getAcceptableThreshold()) {
            recommendations.add(DataQualityReport.QualityRecommendation.builder()
                    .recommendationId(UUID.randomUUID().toString())
                    .title("Critical: Data Quality Below Acceptable Threshold")
                    .description("Overall data quality score is " + String.format("%.1f", score) +
                            "%, which is below the acceptable threshold of " + config.getQuality().getAcceptableThreshold() + "%")
                    .priority(DataQualityReport.RecommendationPriority.CRITICAL)
                    .category("QUALITY_SCORE")
                    .estimatedImpact((int) (100 - score))
                    .action("Review and fix all validation failures immediately")
                    .build());
        }

        // Check for high volume of critical errors
        long criticalCount = results.stream()
                .filter(r -> r.getSeverity() == com.gogidix.globalbusinessmanagement.datavalidation.domain.model.ValidationRule.SeverityLevel.CRITICAL)
                .filter(r -> !r.isPassed())
                .count();

        if (criticalCount > 0) {
            recommendations.add(DataQualityReport.QualityRecommendation.builder()
                    .recommendationId(UUID.randomUUID().toString())
                    .title("Address Critical Validation Failures")
                    .description("Found " + criticalCount + " critical validation failures that need immediate attention")
                    .priority(DataQualityReport.RecommendationPriority.CRITICAL)
                    .category("CRITICAL_ERRORS")
                    .estimatedImpact((int) criticalCount)
                    .action("Investigate and fix all critical validation failures")
                    .build());
        }

        // Check for data completeness issues
        long nullValueCount = results.stream()
                .filter(r -> r.getActualValue() == null || r.getActualValue().isEmpty())
                .filter(r -> !r.isPassed())
                .count();

        if (nullValueCount > results.size() * 0.1) { // More than 10%
            recommendations.add(DataQualityReport.QualityRecommendation.builder()
                    .recommendationId(UUID.randomUUID().toString())
                    .title("Improve Data Completeness")
                    .description("Significant number of null/empty values detected (" + nullValueCount + " records)")
                    .priority(DataQualityReport.RecommendationPriority.HIGH)
                    .category("COMPLETENESS")
                    .estimatedImpact((int) (nullValueCount * 100 / results.size()))
                    .action("Implement required field validations and data entry checks")
                    .build());
        }

        return recommendations;
    }

    /**
     * Get a report by ID
     */
    public DataQualityReportDTO getReportById(String id) {
        DataQualityReport report = reportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Report not found with ID: " + id));
        return reportMapper.toDto(report);
    }

    /**
     * Get a report by report ID
     */
    public DataQualityReportDTO getReportByReportId(String reportId) {
        DataQualityReport report = reportRepository.findByReportId(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Report not found with report ID: " + reportId));
        return reportMapper.toDto(report);
    }

    /**
     * Get reports for an entity
     */
    public List<DataQualityReportDTO> getReportsForEntity(String entityType, String entityId) {
        return reportMapper.toDtoList(
                reportRepository.findByEntityTypeAndEntityIdOrderByReportDateDesc(entityType, entityId));
    }

    /**
     * Get reports for tenant
     */
    public Page<DataQualityReportDTO> getReportsForTenant(String tenantId, Pageable pageable) {
        return reportRepository.findByTenantIdOrderByReportDateDesc(tenantId, pageable)
                .map(reportMapper::toDto);
    }

    /**
     * Get latest report for entity
     */
    public DataQualityReportDTO getLatestReportForEntity(String entityType, String entityId) {
        return reportRepository.findFirstByEntityTypeAndEntityIdOrderByReportDateDesc(entityType, entityId)
                .map(reportMapper::toDto)
                .orElse(null);
    }

    /**
     * Get reports by quality level
     */
    public List<DataQualityReportDTO> getReportsByQualityLevel(DataQualityReport.QualityLevel qualityLevel) {
        return reportMapper.toDtoList(
                reportRepository.findByQualityLevelOrderByReportDateDesc(qualityLevel));
    }

    /**
     * Archive a report
     */
    @Transactional
    public void archiveReport(String id) {
        DataQualityReport report = reportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Report not found with ID: " + id));

        report.setArchived(true);
        report.setArchivedAt(LocalDateTime.now());
        reportRepository.save(report);

        log.info("Archived report: {}", id);
    }

    /**
     * Get quality summary for dashboard
     */
    public QualitySummary getQualitySummary(String tenantId) {
        List<DataQualityReport> recentReports = reportRepository
                .findByTenantIdOrderByReportDateDesc(tenantId, null)
                .getContent()
                .stream()
                .filter(r -> !r.isArchived())
                .limit(100)
                .toList();

        if (recentReports.isEmpty()) {
            return new QualitySummary(0, 0, 0, 0, DataQualityReport.QualityLevel.UNKNOWN);
        }

        double avgScore = recentReports.stream()
                .mapToDouble(DataQualityReport::getQualityScore)
                .average()
                .orElse(0.0);

        long excellentCount = recentReports.stream()
                .filter(r -> r.getQualityLevel() == DataQualityReport.QualityLevel.EXCELLENT)
                .count();

        long poorCount = recentReports.stream()
                .filter(r -> r.getQualityLevel() == DataQualityReport.QualityLevel.POOR ||
                           r.getQualityLevel() == DataQualityReport.QualityLevel.CRITICAL)
                .count();

        return new QualitySummary(
                recentReports.size(),
                (int) excellentCount,
                (int) poorCount,
                avgScore,
                DataQualityReport.QualityLevel.fromScore((int) avgScore)
        );
    }

    /**
     * Quality summary record
     */
    public record QualitySummary(
        int totalReports,
        int excellentReports,
        int poorReports,
        double averageScore,
        DataQualityReport.QualityLevel overallLevel
    ) {}
}
