package com.gogidix.customersupport.qualitymanagement.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * DTO for Agent Quality Profile operations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgentQualityProfileDto {

    private String id;
    private String tenantId;

    // Agent Info
    private String agentId;
    private String agentName;
    private String agentEmail;
    private String teamId;
    private String teamName;
    private String managerId;
    private String managerName;

    // Overall Score
    private Double overallQualityScore;
    private String overallQualityRank;
    private String qualityTrend;
    private Integer rankInTeam;
    private Double percentileInTeam;

    // Review Stats
    private Long totalReviews;
    private Long passedReviews;
    private Long failedReviews;
    private Double passRatePercentage;
    private Double averageScore;
    private Double highestScore;
    private Double lowestScore;
    private Double scoreStandardDeviation;

    // Recent Performance
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant lastReviewDate;

    private Double lastReviewScore;
    private Boolean lastReviewPassed;
    private Integer currentStreak;
    private String currentStreakType;
    private Integer longestPassingStreak;
    private Integer longestFailingStreak;

    // Period Metrics
    private Double monthlyAverageScore;
    private Long monthlyReviewsCount;
    private Double monthlyPassRate;
    private Double quarterlyAverageScore;
    private Long quarterlyReviewsCount;
    private Double quarterlyPassRate;
    private Double yearlyAverageScore;
    private Long yearlyReviewsCount;
    private Double yearlyPassRate;

    // Categories
    private List<CategoryPerformanceDto> categoryPerformance;
    private List<String> strengthAreas;
    private List<String> improvementAreas;

    // Coaching
    private Boolean coachingRequired;
    private String coachingPriority;
    private List<CoachingNoteDto> coachingNotes;
    private String developmentPlanId;
    private List<String> certificationsEarned;

    // Calibration
    private Long calibrationSessionsParticipated;
    private Double calibrationAverageVariance;
    private Double calibrationComplianceScore;

    // Channel Performance
    private Map<String, ChannelStatsDto> channelPerformance;

    // Score History
    private List<ScoreSnapshotDto> scoreHistory;

    // Goals
    private Double qualityGoal;
    private Double goalProgressPercentage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant goalTargetDate;

    private Boolean goalAchieved;

    // Risk
    private String qualityRiskLevel;
    private List<String> riskFactors;

    // Metadata
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant profileLastCalculated;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;

    /**
     * DTO for Category Performance
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CategoryPerformanceDto {
        private String category;
        private Double averageScore;
        private Double maxScore;
        private Integer reviewCount;
        private String trend;
        private Integer rank;
        private String performanceLevel;
    }

    /**
     * DTO for Coaching Note
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CoachingNoteDto {
        private String noteId;
        private String note;
        private String createdBy;
        private String createdByName;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant createdAt;

        private String category;
        private Boolean isActionable;
    }

    /**
     * DTO for Channel Stats
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ChannelStatsDto {
        private String channel;
        private Long reviewCount;
        private Double averageScore;
        private Double passRate;
        private String trend;
    }

    /**
     * DTO for Score Snapshot
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ScoreSnapshotDto {
        private String reviewId;
        private Double score;
        private Boolean passed;
        private String reviewType;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant reviewDate;

        private String channel;
    }

    /**
     * Response DTO for Agent Quality Profile summary
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgentQualityProfileSummaryDto {
        private String agentId;
        private String agentName;
        private String teamName;
        private Double overallQualityScore;
        private String overallQualityRank;
        private String qualityTrend;
        private Long totalReviews;
        private Double passRatePercentage;
        private String qualityRiskLevel;
        private Boolean coachingRequired;
        private String coachingPriority;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant lastReviewDate;
    }

    /**
     * Response DTO for Quality Leaderboard
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QualityLeaderboardDto {
        private String agentId;
        private String agentName;
        private String teamName;
        private Double overallQualityScore;
        private String overallQualityRank;
        private Long totalReviews;
        private Double passRatePercentage;
        private Integer rank;
    }

    /**
     * Response DTO for Quality Metrics
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QualityMetricsDto {
        private Long totalAgents;
        private Long agentsReviewed;
        private Double averageQualityScore;
        private Double overallPassRate;
        private Long exemplaryAgents;
        private Long agentsNeedingImprovement;
        private Long criticalRiskAgents;
        private Long coachingRequiredAgents;
        private Double averageCalibrationScore;
        private Long totalCalibrationSessions;
    }
}
