package com.gogidix.aiservices.researchintelligenceservice.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class ResearchFinding {
    private final String findingId;
    private final String projectId;
    private final String title;
    private final String description;
    private final FindingType type;
    private final double significanceScore;
    private final String discoveredBy;
    private final LocalDateTime discoveredAt;
    private final LocalDateTime recordedAt;
    private final FindingStatus status;
    private final String peerReviewNotes;
    private final int citationCount;

    public ResearchFinding(String findingId, String projectId, String title, String description,
                          FindingType type, double significanceScore, String discoveredBy) {
        this.findingId = Objects.requireNonNull(findingId, "findingId cannot be null");
        this.projectId = Objects.requireNonNull(projectId, "projectId cannot be null");
        this.title = Objects.requireNonNull(title, "title cannot be null");
        this.description = description;
        this.type = Objects.requireNonNull(type, "type cannot be null");
        this.significanceScore = Math.min(1.0, Math.max(0.0, significanceScore));
        this.discoveredBy = discoveredBy;
        this.discoveredAt = LocalDateTime.now();
        this.recordedAt = LocalDateTime.now();
        this.status = FindingStatus.PENDING_REVIEW;
        this.peerReviewNotes = "";
        this.citationCount = 0;
    }

    public String getFindingId() {
        return findingId;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public FindingType getType() {
        return type;
    }

    public double getSignificanceScore() {
        return significanceScore;
    }

    public String getDiscoveredBy() {
        return discoveredBy;
    }

    public LocalDateTime getDiscoveredAt() {
        return discoveredAt;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public FindingStatus getStatus() {
        return status;
    }

    public String getPeerReviewNotes() {
        return peerReviewNotes;
    }

    public int getCitationCount() {
        return citationCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResearchFinding that = (ResearchFinding) o;
        return Objects.equals(findingId, that.findingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(findingId);
    }

    @Override
    public String toString() {
        return "ResearchFinding{" +
                "findingId='" + findingId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", significanceScore=" + significanceScore +
                ", status=" + status +
                ", citationCount=" + citationCount +
                '}';
    }

    public enum FindingType {
        HYPOTHESIS_CONFIRMED,
        HYPOTHESIS_REJECTED,
        NOVEL_DISCOVERY,
        METHOD_IMPROVEMENT,
        DATA_INSIGHT,
        STATISTICAL_CORRELATION,
        PATTERN_RECOGNITION,
        ANOMALY_DETECTED
    }

    public enum FindingStatus {
        PENDING_REVIEW,
        UNDER_REVIEW,
        APPROVED,
        REJECTED,
        PUBLISHED,
        ARCHIVED
    }
}
