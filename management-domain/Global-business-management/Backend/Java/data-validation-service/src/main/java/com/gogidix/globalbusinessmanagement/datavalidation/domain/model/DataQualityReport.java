package com.gogidix.globalbusinessmanagement.datavalidation.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Domain model representing a data quality report for an entity or dataset.
 * Aggregates validation results and provides quality metrics.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "data_quality_reports")
public class DataQualityReport {

    @Id
    private String id;

    @Indexed
    private String reportId;

    @Indexed
    private String entityType;

    @Indexed
    private String entityId;

    private String entityName;

    @Indexed
    private String tenantId;

    @Indexed
    private ReportType reportType;

    private String dataSource;

    private String dataSourceType;

    @Indexed
    private LocalDateTime reportDate;

    @Indexed
    private LocalDateTime periodStart;

    @Indexed
    private LocalDateTime periodEnd;

    // Quality Metrics
    @Builder.Default
    private int totalRecords = 0;

    @Builder.Default
    private int validRecords = 0;

    @Builder.Default
    private int invalidRecords = 0;

    @Builder.Default
    private int pendingRecords = 0;

    @Builder.Default
    private double qualityScore = 0.0;

    @Builder.Default
    private QualityLevel qualityLevel = QualityLevel.UNKNOWN;

    // Detailed Metrics
    private Map<String, FieldQualityMetrics> fieldMetrics;

    private List<RuleExecutionSummary> ruleSummaries;

    private List<QualityIssue> topIssues;

    private Map<String, Integer> errorDistribution;

    // Trend Data
    private QualityTrend trend;

    // Recommendations
    private List<QualityRecommendation> recommendations;

    // Metadata
    private String generatedBy;

    private LocalDateTime generatedAt;

    private String batchId;

    private Map<String, Object> metadata;

    @Indexed
    @Builder.Default
    private boolean archived = false;

    private LocalDateTime archivedAt;

    /**
     * Type of quality report
     */
    public enum ReportType {
        ENTITY,
        DATASET,
        DATA_SOURCE,
        TENANT,
        GLOBAL,
        FIELD_LEVEL,
        RULE_LEVEL
    }

    /**
     * Quality levels based on score
     */
    public enum QualityLevel {
        EXCELLENT(95, 100),
        GOOD(80, 94),
        ACCEPTABLE(60, 79),
        POOR(40, 59),
        CRITICAL(0, 39),
        UNKNOWN(0, 0);

        private final int minScore;
        private final int maxScore;

        QualityLevel(int minScore, int maxScore) {
            this.minScore = minScore;
            this.maxScore = maxScore;
        }

        public static QualityLevel fromScore(double score) {
            if (score >= EXCELLENT.minScore) return EXCELLENT;
            if (score >= GOOD.minScore) return GOOD;
            if (score >= ACCEPTABLE.minScore) return ACCEPTABLE;
            if (score >= POOR.minScore) return POOR;
            return CRITICAL;
        }

        public int getMinScore() { return minScore; }
        public int getMaxScore() { return maxScore; }
    }

    /**
     * Metrics for a specific field
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldQualityMetrics {
        private String fieldName;
        private int totalRecords;
        private int nullCount;
        private int invalidCount;
        private double completeness;
        private double validity;
        private double consistency;
        private List<String> topErrors;
    }

    /**
     * Summary of rule execution
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RuleExecutionSummary {
        private String ruleCode;
        private String ruleName;
        private int executedCount;
        private int passedCount;
        private int failedCount;
        private double passRate;
        private ValidationRule.SeverityLevel severity;
    }

    /**
     * Quality issue found
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QualityIssue {
        private String issueCode;
        private String description;
        private String fieldName;
        private String entityType;
        private int occurrenceCount;
        private double percentage;
        private ValidationRule.SeverityLevel severity;
        private String recommendation;
    }

    /**
     * Quality trend data
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QualityTrend {
        private double currentScore;
        private double previousScore;
        private double changePercent;
        private TrendDirection direction;
        private List<TrendDataPoint> dataPoints;

        public enum TrendDirection {
            IMPROVING,
            DECLINING,
            STABLE
        }

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class TrendDataPoint {
            private LocalDateTime timestamp;
            private double score;
        }
    }

    /**
     * Quality improvement recommendation
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QualityRecommendation {
        private String recommendationId;
        private String title;
        private String description;
        private RecommendationPriority priority;
        private String category;
        private int estimatedImpact;
        private String action;
    }

    /**
     * Priority level for recommendations
     */
    public enum RecommendationPriority {
        CRITICAL,
        HIGH,
        MEDIUM,
        LOW
    }

    /**
     * Calculates the quality score based on valid vs total records
     */
    public void calculateQualityScore() {
        if (totalRecords == 0) {
            this.qualityScore = 0.0;
            this.qualityLevel = QualityLevel.UNKNOWN;
            return;
        }
        this.qualityScore = (validRecords * 100.0) / totalRecords;
        this.qualityLevel = QualityLevel.fromScore((int) qualityScore);
    }

    /**
     * Calculates the completion rate
     */
    public double getCompletionRate() {
        if (totalRecords == 0) {
            return 0.0;
        }
        return ((validRecords + invalidRecords) * 100.0) / totalRecords;
    }

    /**
     * Checks if quality is acceptable
     */
    public boolean isQualityAcceptable() {
        return qualityLevel == QualityLevel.EXCELLENT ||
               qualityLevel == QualityLevel.GOOD ||
               qualityLevel == QualityLevel.ACCEPTABLE;
    }

    /**
     * Gets the percentage of invalid records
     */
    public double getInvalidPercentage() {
        if (totalRecords == 0) {
            return 0.0;
        }
        return (invalidRecords * 100.0) / totalRecords;
    }

    /**
     * Adds a field metric
     */
    public void addFieldMetric(String fieldName, FieldQualityMetrics metric) {
        if (this.fieldMetrics == null) {
            this.fieldMetrics = new java.util.HashMap<>();
        }
        this.fieldMetrics.put(fieldName, metric);
    }

    /**
     * Adds a rule summary
     */
    public void addRuleSummary(RuleExecutionSummary summary) {
        if (this.ruleSummaries == null) {
            this.ruleSummaries = new java.util.ArrayList<>();
        }
        this.ruleSummaries.add(summary);
    }

    /**
     * Adds a quality issue
     */
    public void addQualityIssue(QualityIssue issue) {
        if (this.topIssues == null) {
            this.topIssues = new java.util.ArrayList<>();
        }
        this.topIssues.add(issue);
    }

    /**
     * Adds a recommendation
     */
    public void addRecommendation(QualityRecommendation recommendation) {
        if (this.recommendations == null) {
            this.recommendations = new java.util.ArrayList<>();
        }
        this.recommendations.add(recommendation);
    }
}
