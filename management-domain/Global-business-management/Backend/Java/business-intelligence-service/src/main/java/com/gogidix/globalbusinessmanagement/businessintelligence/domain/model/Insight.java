package com.gogidix.globalbusinessmanagement.businessintelligence.domain.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Domain model representing a business insight.
 * Stores AI-generated or manually created business insights.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "insights")
@CompoundIndex(name = "entity_period_type_idx", def = "{'entityCode': 1, 'periodId': 1, 'insightType': 1}")
public class Insight {

    @Id
    private String id;

    @NotBlank(message = "Insight title is required")
    @Indexed
    private String title;

    @NotBlank(message = "Insight summary is required")
    private String summary;

    private String description;

    @NotNull(message = "Insight type is required")
    @Indexed
    private InsightType insightType;

    @NotNull(message = "Impact level is required")
    @Indexed
    private ImpactLevel impactLevel;

    @NotNull(message = "Confidence score is required")
    @Indexed
    private BigDecimal confidenceScore;

    @NotNull(message = "Sentiment is required")
    private Sentiment sentiment;

    @Indexed
    private String entityCode;

    @Indexed
    private String entityType;

    @Indexed
    private String periodId;

    @Valid
    @NotEmpty(message = "At least one metric reference is required")
    private List<MetricReference> metricReferences;

    @Valid
    private List<ActionableRecommendation> recommendations;

    @Valid
    private InsightContext context;

    @Valid
    private InsightEvidence evidence;

    @Valid
    private InsightTrends trends;

    @NotNull(message = "Insight status is required")
    @Builder.Default
    private InsightStatus status = InsightStatus.ACTIVE;

    @Indexed
    private String source;

    @Indexed
    private String createdBy;

    @Indexed
    private Boolean isVerified;

    @Indexed
    private Boolean isAiGenerated;

    @Indexed
    private Instant validFrom;

    @Indexed
    private Instant validUntil;

    @Indexed
    private List<String> tags;

    private Map<String, Object> attributes;

    @Indexed
    private Instant detectedAt;

    private Instant createdAt;

    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetricReference {
        @NotBlank(message = "Metric name is required")
        private String metricName;

        private String metricCode;

        private BigDecimal currentValue;

        private BigDecimal previousValue;

        private BigDecimal targetValue;

        private BigDecimal variance;

        private String varianceDirection;

        private BigDecimal variancePercentage;

        private String trend;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActionableRecommendation {
        @NotBlank(message = "Recommendation title is required")
        private String title;

        private String description;

        private RecommendationPriority priority;

        private String category;

        private String assignedTo;

        private Integer estimatedEffort;

        private BigDecimal estimatedImpact;

        private String timeline;

        private List<String> actionItems;

        private RecommendationStatus status;

        private Instant dueBy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InsightContext {
        private String businessContext;

        private String marketContext;

        private String competitorContext;

        private String regulatoryContext;

        private Map<String, Object> environmentalFactors;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InsightEvidence {
        private List<String> dataSources;

        private List<String> supportingDocuments;

        private List<String> references;

        private BigDecimal evidenceScore;

        private String methodology;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InsightTrends {
        private String trendDirection;

        private BigDecimal trendStrength;

        private Integer trendDuration;

        private String trendPattern;

        private BigDecimal seasonalityIndex;

        private List<BigDecimal> historicalValues;
    }

    public enum InsightType {
        REVENUE_GROWTH,
        PROFITABILITY,
        COST_OPTIMIZATION,
        MARKET_EXPANSION,
        CUSTOMER_RETENTION,
        OPERATIONAL_EFFICIENCY,
        RISK_DETECTION,
        OPPORTUNITY,
        ANOMALY,
        FORECAST,
        CUSTOM
    }

    public enum ImpactLevel {
        CRITICAL,
        HIGH,
        MEDIUM,
        LOW,
        INFORMATIONAL
    }

    public enum Sentiment {
        POSITIVE,
        NEUTRAL,
        NEGATIVE
    }

    public enum InsightStatus {
        ACTIVE,
        ACKNOWLEDGED,
        IN_PROGRESS,
        RESOLVED,
        ARCHIVED,
        DISMISSED
    }

    public enum RecommendationPriority {
        URGENT,
        HIGH,
        MEDIUM,
        LOW
    }

    public enum RecommendationStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED,
        DEFERRED
    }

    public boolean isActive() {
        return InsightStatus.ACTIVE.equals(status);
    }

    public boolean isHighImpact() {
        return ImpactLevel.HIGH.equals(impactLevel) || ImpactLevel.CRITICAL.equals(impactLevel);
    }

    public boolean hasHighConfidence() {
        return confidenceScore != null && confidenceScore.compareTo(new BigDecimal("0.7")) >= 0;
    }

    public boolean isPositive() {
        return Sentiment.POSITIVE.equals(sentiment);
    }

    public boolean isStillValid(Instant asOfDate) {
        if (validUntil == null) {
            return true;
        }
        return asOfDate == null || !asOfDate.isAfter(validUntil);
    }

    public boolean requiresAction() {
        return isHighImpact() && hasHighConfidence() && isActive();
    }
}
