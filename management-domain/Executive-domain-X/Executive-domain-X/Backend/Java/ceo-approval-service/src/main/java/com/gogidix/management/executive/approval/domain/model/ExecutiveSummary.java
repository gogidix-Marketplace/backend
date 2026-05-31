package com.gogidix.management.executive.approval.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Executive Summary domain model
 * Represents a generated executive summary with key metrics and insights
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "executive_summaries")
public class ExecutiveSummary extends BaseEntity {

    private String title;
    private String description;
    private SummaryPeriod period;
    private Instant generatedAt;
    private String generatedBy;

    private Map<String, Object> keyMetrics;
    private List<Insight> insights;
    private List<String> dashboardIds;
    private SummaryStatus status;

    public enum SummaryPeriod {
        DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY
    }

    public enum SummaryStatus {
        DRAFT, PUBLISHED, ARCHIVED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Insight {
        private String id;
        private String category;
        private String title;
        private String description;
        private ImpactLevel impact;
        private String recommendation;
    }

    public enum ImpactLevel {
        HIGH, MEDIUM, LOW
    }
}
