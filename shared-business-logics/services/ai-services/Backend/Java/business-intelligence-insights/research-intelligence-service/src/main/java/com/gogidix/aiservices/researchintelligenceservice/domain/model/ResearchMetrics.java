package com.gogidix.aiservices.researchintelligenceservice.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class ResearchMetrics {
    private final String metricId;
    private final String projectId;
    private final int totalPublications;
    private final int totalCitations;
    private final double hIndex;
    private final double impactFactor;
    private final int collaborationCount;
    private final int grantFundingAmount;
    private final int patentCount;
    private final LocalDateTime calculatedAt;
    private final MetricsPeriod period;

    public ResearchMetrics(String metricId, String projectId, MetricsPeriod period) {
        this.metricId = Objects.requireNonNull(metricId, "metricId cannot be null");
        this.projectId = Objects.requireNonNull(projectId, "projectId cannot be null");
        this.period = Objects.requireNonNull(period, "period cannot be null");
        this.totalPublications = 0;
        this.totalCitations = 0;
        this.hIndex = 0.0;
        this.impactFactor = 0.0;
        this.collaborationCount = 0;
        this.grantFundingAmount = 0;
        this.patentCount = 0;
        this.calculatedAt = LocalDateTime.now();
    }

    public String getMetricId() {
        return metricId;
    }

    public String getProjectId() {
        return projectId;
    }

    public int getTotalPublications() {
        return totalPublications;
    }

    public int getTotalCitations() {
        return totalCitations;
    }

    public double getHIndex() {
        return hIndex;
    }

    public double getImpactFactor() {
        return impactFactor;
    }

    public int getCollaborationCount() {
        return collaborationCount;
    }

    public int getGrantFundingAmount() {
        return grantFundingAmount;
    }

    public int getPatentCount() {
        return patentCount;
    }

    public LocalDateTime getCalculatedAt() {
        return calculatedAt;
    }

    public MetricsPeriod getPeriod() {
        return period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResearchMetrics that = (ResearchMetrics) o;
        return Objects.equals(metricId, that.metricId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metricId);
    }

    @Override
    public String toString() {
        return "ResearchMetrics{" +
                "metricId='" + metricId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", totalPublications=" + totalPublications +
                ", totalCitations=" + totalCitations +
                ", hIndex=" + hIndex +
                ", impactFactor=" + impactFactor +
                ", collaborationCount=" + collaborationCount +
                ", patentCount=" + patentCount +
                ", period=" + period +
                '}';
    }

    public enum MetricsPeriod {
        DAILY,
        WEEKLY,
        MONTHLY,
        QUARTERLY,
        YEARLY,
        ALL_TIME
    }
}
