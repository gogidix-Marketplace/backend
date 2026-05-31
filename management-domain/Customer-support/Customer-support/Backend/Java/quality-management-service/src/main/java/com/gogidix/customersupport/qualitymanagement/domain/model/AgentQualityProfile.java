package com.gogidix.customersupport.qualitymanagement.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AgentQualityProfile - Domain model representing an agent's quality profile
 *
 * Aggregates all QA review data for an agent to provide comprehensive
 * quality metrics, trends, and performance tracking.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AgentQualityProfile extends BaseEntity {

    // Agent Identification
    @Field("agent_id")
    private String agentId;

    @Field("agent_name")
    private String agentName;

    @Field("agent_email")
    private String agentEmail;

    @Field("team_id")
    private String teamId;

    @Field("team_name")
    private String teamName;

    @Field("manager_id")
    private String managerId;

    @Field("manager_name")
    private String managerName;

    // Overall Quality Score
    @Field("overall_quality_score")
    private Double overallQualityScore;

    @Field("overall_quality_rank")
    private QualityRank overallQualityRank;

    @Field("quality_trend")
    private QualityTrend qualityTrend;

    @Field("rank_in_team")
    private Integer rankInTeam;

    @Field("percentile_in_team")
    private Double percentileInTeam;

    // Review Statistics
    @Field("total_reviews")
    private Long totalReviews;

    @Field("passed_reviews")
    private Long passedReviews;

    @Field("failed_reviews")
    private Long failedReviews;

    @Field("pass_rate_percentage")
    private Double passRatePercentage;

    @Field("average_score")
    private Double averageScore;

    @Field("highest_score")
    private Double highestScore;

    @Field("lowest_score")
    private Double lowestScore;

    @Field("score_standard_deviation")
    private Double scoreStandardDeviation;

    // Recent Performance
    @Field("last_review_date")
    private Instant lastReviewDate;

    @Field("last_review_score")
    private Double lastReviewScore;

    @Field("last_review_passed")
    private Boolean lastReviewPassed;

    @Field("current_streak")
    private Integer currentStreak;

    @Field("current_streak_type")
    private StreakType currentStreakType;

    @Field("longest_passing_streak")
    private Integer longestPassingStreak;

    @Field("longest_failing_streak")
    private Integer longestFailingStreak;

    // Period Metrics
    @Field("monthly_average_score")
    private Double monthlyAverageScore;

    @Field("monthly_reviews_count")
    private Long monthlyReviewsCount;

    @Field("monthly_pass_rate")
    private Double monthlyPassRate;

    @Field("quarterly_average_score")
    private Double quarterlyAverageScore;

    @Field("quarterly_reviews_count")
    private Long quarterlyReviewsCount;

    @Field("quarterly_pass_rate")
    private Double quarterlyPassRate;

    @Field("yearly_average_score")
    private Double yearlyAverageScore;

    @Field("yearly_reviews_count")
    private Long yearlyReviewsCount;

    @Field("yearly_pass_rate")
    private Double yearlyPassRate;

    // Category Performance
    @Field("category_performance")
    private List<CategoryPerformance> categoryPerformance;

    @Field("strength_areas")
    private List<String> strengthAreas;

    @Field("improvement_areas")
    private List<String> improvementAreas;

    // Coaching and Development
    @Field("coaching_required")
    private Boolean coachingRequired;

    @Field("coaching_priority")
    private CoachingPriority coachingPriority;

    @Field("coaching_notes")
    private List<CoachingNote> coachingNotes;

    @Field("development_plan_id")
    private String developmentPlanId;

    @Field("certifications_earned")
    private List<String> certificationsEarned;

    // Calibration Stats
    @Field("calibration_sessions_participated")
    private Long calibrationSessionsParticipated;

    @Field("calibration_average_variance")
    private Double calibrationAverageVariance;

    @Field("calibration_compliance_score")
    private Double calibrationComplianceScore;

    // Channel Performance
    @Field("channel_performance")
    private Map<String, ChannelStats> channelPerformance;

    // Score History
    @Field("score_history")
    private List<ScoreSnapshot> scoreHistory;

    @Field("recent_review_ids")
    private List<String> recentReviewIds;

    // Goals
    @Field("quality_goal")
    private Double qualityGoal;

    @Field("goal_progress_percentage")
    private Double goalProgressPercentage;

    @Field("goal_target_date")
    private Instant goalTargetDate;

    @Field("goal_achieved")
    private Boolean goalAchieved;

    // Risk Assessment
    @Field("quality_risk_level")
    private RiskLevel qualityRiskLevel;

    @Field("risk_factors")
    private List<String> riskFactors;

    // Metadata
    @Field("profile_last_calculated")
    private Instant profileLastCalculated;

    @Field("data_as_of_date")
    private Instant dataAsOfDate;

    @Field("tags")
    private List<String> tags;

    /**
     * Enums for Quality Rank
     */
    public enum QualityRank {
        EXEMPLARY,
        PROFICIENT,
        COMPETENT,
        DEVELOPING,
        NEEDS_IMPROVEMENT
    }

    /**
     * Enums for Quality Trend
     */
    public enum QualityTrend {
        IMPROVING,
        STABLE,
        DECLINING,
        INSUFFICIENT_DATA
    }

    /**
     * Enums for Streak Type
     */
    public enum StreakType {
        PASSING,
        FAILING,
        NONE
    }

    /**
     * Enums for Coaching Priority
     */
    public enum CoachingPriority {
        CRITICAL,
        HIGH,
        MEDIUM,
        LOW,
        NONE
    }

    /**
     * Enums for Risk Level
     */
    public enum RiskLevel {
        CRITICAL,
        HIGH,
        MEDIUM,
        LOW,
        MINIMAL
    }

    /**
     * Inner class for Category Performance
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryPerformance {
        private String category;
        private Double averageScore;
        private Double maxScore;
        private Integer reviewCount;
        private QualityTrend trend;
        private Integer rank;
        private String performanceLevel;
    }

    /**
     * Inner class for Coaching Note
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CoachingNote {
        private String noteId;
        private String note;
        private String createdBy;
        private String createdByName;
        private Instant createdAt;
        private String category;
        private Boolean isActionable;
    }

    /**
     * Inner class for Channel Stats
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChannelStats {
        private String channel;
        private Long reviewCount;
        private Double averageScore;
        private Double passRate;
        private QualityTrend trend;
    }

    /**
     * Inner class for Score Snapshot
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScoreSnapshot {
        private String reviewId;
        private Double score;
        private Boolean passed;
        private String reviewType;
        private Instant reviewDate;
        private String channel;
    }

    /**
     * Constructor with tenant ID
     */
    public AgentQualityProfile(String tenantId) {
        super(tenantId);
        this.categoryPerformance = new ArrayList<>();
        this.strengthAreas = new ArrayList<>();
        this.improvementAreas = new ArrayList<>();
        this.coachingNotes = new ArrayList<>();
        this.certificationsEarned = new ArrayList<>();
        this.scoreHistory = new ArrayList<>();
        this.recentReviewIds = new ArrayList<>();
        this.riskFactors = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.currentStreak = 0;
        this.currentStreakType = StreakType.NONE;
        this.qualityTrend = QualityTrend.INSUFFICIENT_DATA;
        this.overallQualityRank = QualityRank.COMPETENT;
        this.coachingPriority = CoachingPriority.NONE;
        this.qualityRiskLevel = RiskLevel.LOW;
        this.profileLastCalculated = Instant.now();
        this.dataAsOfDate = Instant.now();
    }

    /**
     * Calculate pass rate percentage
     */
    public void calculatePassRate() {
        if (totalReviews != null && totalReviews > 0 && passedReviews != null) {
            this.passRatePercentage = (passedReviews.doubleValue() / totalReviews) * 100.0;
        }
    }

    /**
     * Determine quality rank based on score
     */
    public void determineQualityRank() {
        if (overallQualityScore == null) {
            this.overallQualityRank = QualityRank.COMPETENT;
            return;
        }

        if (overallQualityScore >= 95) {
            this.overallQualityRank = QualityRank.EXEMPLARY;
        } else if (overallQualityScore >= 85) {
            this.overallQualityRank = QualityRank.PROFICIENT;
        } else if (overallQualityScore >= 75) {
            this.overallQualityRank = QualityRank.COMPETENT;
        } else if (overallQualityScore >= 65) {
            this.overallQualityRank = QualityRank.DEVELOPING;
        } else {
            this.overallQualityRank = QualityRank.NEEDS_IMPROVEMENT;
        }
    }

    /**
     * Determine quality risk level
     */
    public void determineRiskLevel() {
        if (overallQualityScore == null || passRatePercentage == null) {
            this.qualityRiskLevel = RiskLevel.MEDIUM;
            return;
        }

        double score = overallQualityScore;
        double passRate = passRatePercentage;

        if (score < 60 || passRate < 60) {
            this.qualityRiskLevel = RiskLevel.CRITICAL;
        } else if (score < 70 || passRate < 70) {
            this.qualityRiskLevel = RiskLevel.HIGH;
        } else if (score < 80 || passRate < 80) {
            this.qualityRiskLevel = RiskLevel.MEDIUM;
        } else if (score < 90 || passRate < 90) {
            this.qualityRiskLevel = RiskLevel.LOW;
        } else {
            this.qualityRiskLevel = RiskLevel.MINIMAL;
        }
    }

    /**
     * Calculate goal progress
     */
    public void calculateGoalProgress() {
        if (qualityGoal != null && overallQualityScore != null) {
            this.goalProgressPercentage = (overallQualityScore / qualityGoal) * 100.0;
            this.goalAchieved = overallQualityScore >= qualityGoal;
        }
    }

    /**
     * Add coaching note
     */
    public void addCoachingNote(CoachingNote note) {
        if (this.coachingNotes == null) {
            this.coachingNotes = new ArrayList<>();
        }
        this.coachingNotes.add(note);
        updateTimestamp();
    }

    /**
     * Add score snapshot
     */
    public void addScoreSnapshot(ScoreSnapshot snapshot) {
        if (this.scoreHistory == null) {
            this.scoreHistory = new ArrayList<>();
        }
        this.scoreHistory.add(snapshot);

        // Keep only last 100 snapshots
        if (scoreHistory.size() > 100) {
            scoreHistory = scoreHistory.stream()
                    .sorted(Comparator.comparing(ScoreSnapshot::getReviewDate).reversed())
                    .limit(100)
                    .collect(Collectors.toList());
        }
        updateTimestamp();
    }

    /**
     * Get recent score history
     */
    public List<ScoreSnapshot> getRecentScoreHistory(int limit) {
        if (scoreHistory == null) {
            return new ArrayList<>();
        }
        return scoreHistory.stream()
                .sorted(Comparator.comparing(ScoreSnapshot::getReviewDate).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Add strength area
     */
    public void addStrengthArea(String area) {
        if (this.strengthAreas == null) {
            this.strengthAreas = new ArrayList<>();
        }
        if (!this.strengthAreas.contains(area)) {
            this.strengthAreas.add(area);
        }
        updateTimestamp();
    }

    /**
     * Add improvement area
     */
    public void addImprovementArea(String area) {
        if (this.improvementAreas == null) {
            this.improvementAreas = new ArrayList<>();
        }
        if (!this.improvementAreas.contains(area)) {
            this.improvementAreas.add(area);
        }
        updateTimestamp();
    }

    /**
     * Add risk factor
     */
    public void addRiskFactor(String factor) {
        if (this.riskFactors == null) {
            this.riskFactors = new ArrayList<>();
        }
        if (!this.riskFactors.contains(factor)) {
            this.riskFactors.add(factor);
        }
        updateTimestamp();
    }

    /**
     * Check if profile needs recalculation
     */
    public boolean needsRecalculation() {
        if (profileLastCalculated == null) {
            return true;
        }
        return Instant.now().minusSeconds(86400).isAfter(profileLastCalculated);
    }

    /**
     * Mark profile as recalculated
     */
    public void markAsRecalculated() {
        this.profileLastCalculated = Instant.now();
        this.dataAsOfDate = Instant.now();
        updateTimestamp();
    }
}
