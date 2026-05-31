package com.gogidix.aiservices.aifrauddetectionservice.domain.aggregate;

import com.gogidix.aiservices.aifrauddetectionservice.domain.event.FraudAnalysisCompletedEvent;
import com.gogidix.aiservices.aifrauddetectionservice.domain.event.FraudDetectedEvent;
import com.gogidix.aiservices.aifrauddetectionservice.domain.event.PatternAddedEvent;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAction;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudAnalysisResult;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.FraudPattern;
import com.gogidix.aiservices.aifrauddetectionservice.domain.model.RiskLevel;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Domain Aggregate for Fraud Analysis.
 * This is the aggregate root for all fraud analysis operations.
 *
 * Enforces invariants:
 * - Analysis must have a valid transaction and tenant
 * - Risk scores must be between 0 and 1
 * - All changes emit domain events
 */
@Aggregate(
    value = "Fraud Analysis Aggregate Root",
    type = "FraudAnalysis"
)
@Getter
public class FraudAnalysisAggregate {

    private String analysisId;
    private String transactionId;
    private String userId;
    private String tenantId;
    private double fraudScore;
    private RiskLevel riskLevel;
    private FraudAction recommendedAction;
    private List<String> reasons;
    private Instant timestamp;
    private String modelVersion;
    private List<FraudPattern> matchedPatterns;
    private List<DomainEventWrapper> domainEvents;
    private AnalysisStatus status;

    /**
     * Analysis status tracking.
     */
    public enum AnalysisStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        FAILED,
        CANCELLED
    }

    /**
     * Wrapper for domain events until they are dispatched.
     */
    @Getter
    @lombok.AllArgsConstructor
    public static class DomainEventWrapper {
        private final String eventId;
        private final Object event;
        private final Instant occurredAt;
        private final String aggregateId;

        public static DomainEventWrapper create(Object event, String aggregateId) {
            return new DomainEventWrapper(
                    UUID.randomUUID().toString(),
                    event,
                    Instant.now(),
                    aggregateId
            );
        }
    }

    /**
     * Creates a new fraud analysis for a transaction.
     */
    public static FraudAnalysisAggregate create(String transactionId, String userId, String tenantId, String modelVersion) {
        FraudAnalysisAggregate aggregate = new FraudAnalysisAggregate();
        aggregate.analysisId = UUID.randomUUID().toString();
        aggregate.transactionId = transactionId;
        aggregate.userId = userId;
        aggregate.tenantId = tenantId;
        aggregate.fraudScore = 0.0;
        aggregate.riskLevel = RiskLevel.LOW;
        aggregate.recommendedAction = FraudAction.ALLOW;
        aggregate.reasons = new ArrayList<>();
        aggregate.timestamp = Instant.now();
        aggregate.modelVersion = modelVersion;
        aggregate.matchedPatterns = new ArrayList<>();
        aggregate.domainEvents = new ArrayList<>();
        aggregate.status = AnalysisStatus.PENDING;
        return aggregate;
    }

    /**
     * Analyzes transaction data and updates fraud score.
     * Emits FraudDetectedEvent if fraud is detected.
     *
     * @param detectedPatterns List of fraud patterns matched
     * @param score Calculated fraud score (0-1)
     * @param analysisReasons Reasons for the score
     */
    public void analyze(List<FraudPattern> detectedPatterns, double score, List<String> analysisReasons) {
        this.status = AnalysisStatus.IN_PROGRESS;
        this.matchedPatterns.clear();
        this.matchedPatterns.addAll(detectedPatterns);
        this.reasons.clear();
        this.reasons.addAll(analysisReasons);

        updateFraudScore(score);

        if (score > 0.5) {
            FraudDetectedEvent event = FraudDetectedEvent.builder()
                    .eventId(UUID.randomUUID().toString())
                    .analysisId(this.analysisId)
                    .transactionId(this.transactionId)
                    .riskLevel(this.riskLevel)
                    .fraudScore(score)
                    .occurredAt(Instant.now())
                    .aggregateId(this.analysisId)
                    .build();

            addDomainEvent(DomainEventWrapper.create(event, this.analysisId));
        }
    }

    /**
     * Adds a new fraud pattern to this analysis.
     * Emits PatternAddedEvent.
     *
     * @param pattern The pattern to add
     * @param addedBy Who added the pattern
     */
    public void addPattern(FraudPattern pattern, String addedBy) {
        if (!this.matchedPatterns.contains(pattern)) {
            this.matchedPatterns.add(pattern);

            PatternAddedEvent event = PatternAddedEvent.builder()
                    .eventId(UUID.randomUUID().toString())
                    .patternId(pattern.getPatternId())
                    .patternType(pattern.getPatternName())
                    .description("Pattern matched during analysis")
                    .addedBy(addedBy)
                    .analysisId(this.analysisId)
                    .occurredAt(Instant.now())
                    .aggregateId(this.analysisId)
                    .build();

            addDomainEvent(DomainEventWrapper.create(event, this.analysisId));

            // Recalculate fraud score with new pattern
            recalculateScore();
        }
    }

    /**
     * Marks the analysis as completed.
     * Emits FraudAnalysisCompletedEvent.
     *
     * @param finalResult Final analysis result
     */
    public void completeAnalysis(String finalResult) {
        this.status = AnalysisStatus.COMPLETED;

        FraudAnalysisCompletedEvent event = FraudAnalysisCompletedEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .analysisId(this.analysisId)
                .result(finalResult)
                .score(this.fraudScore)
                .timestamp(Instant.now())
                .aggregateId(this.analysisId)
                .riskLevel(this.riskLevel)
                .build();

        addDomainEvent(DomainEventWrapper.create(event, this.analysisId));
    }

    /**
     * Updates the fraud score and adjusts risk level and recommended action accordingly.
     *
     * @param score New fraud score (0-1)
     * @throws IllegalArgumentException if score is not between 0 and 1
     */
    private void updateFraudScore(double score) {
        if (score < 0.0 || score > 1.0) {
            throw new IllegalArgumentException("Fraud score must be between 0 and 1");
        }

        this.fraudScore = score;
        this.riskLevel = determineRiskLevel(score);
        this.recommendedAction = determineRecommendedAction(score);
    }

    /**
     * Determines risk level based on fraud score.
     */
    private RiskLevel determineRiskLevel(double score) {
        if (score >= 0.8) return RiskLevel.HIGH;
        if (score >= 0.5) return RiskLevel.MEDIUM;
        return RiskLevel.LOW;
    }

    /**
     * Determines recommended action based on fraud score.
     */
    private FraudAction determineRecommendedAction(double score) {
        if (score >= 0.8) return FraudAction.BLOCK;
        if (score >= 0.5) return FraudAction.REVIEW;
        return FraudAction.ALLOW;
    }

    /**
     * Recalculates fraud score based on matched patterns.
     */
    private void recalculateScore() {
        if (this.matchedPatterns.isEmpty()) {
            updateFraudScore(0.0);
            return;
        }

        double totalConfidence = this.matchedPatterns.stream()
                .mapToDouble(FraudPattern::getConfidenceScore)
                .sum();

        double averageScore = Math.min(totalConfidence / this.matchedPatterns.size(), 1.0);
        updateFraudScore(averageScore);
    }

    /**
     * Adds a domain event to the list of uncommitted events.
     */
    private void addDomainEvent(DomainEventWrapper event) {
        this.domainEvents.add(event);
    }

    /**
     * Returns and clears all uncommitted domain events.
     */
    public List<DomainEventWrapper> getAndClearDomainEvents() {
        List<DomainEventWrapper> events = new ArrayList<>(this.domainEvents);
        this.domainEvents.clear();
        return events;
    }

    /**
     * Returns uncommitted domain events without clearing them.
     */
    public List<DomainEventWrapper> getUncommittedDomainEvents() {
        return Collections.unmodifiableList(this.domainEvents);
    }

    /**
     * Converts this aggregate to a FraudAnalysisResult value object.
     */
    public FraudAnalysisResult toResult() {
        return FraudAnalysisResult.builder()
                .analysisId(this.analysisId)
                .transactionId(this.transactionId)
                .userId(this.userId)
                .tenantId(this.tenantId)
                .fraudScore(this.fraudScore)
                .riskLevel(this.riskLevel)
                .recommendedAction(this.recommendedAction)
                .reasons(new ArrayList<>(this.reasons))
                .timestamp(this.timestamp)
                .modelVersion(this.modelVersion)
                .build();
    }

    /**
     * Checks if the analysis indicates fraud.
     */
    public boolean isFraudulent() {
        return this.fraudScore > 0.8;
    }

    /**
     * Checks if the analysis requires manual review.
     */
    public boolean requiresReview() {
        return this.fraudScore > 0.5 && this.fraudScore <= 0.8;
    }

    /**
     * Checks if the transaction should be allowed.
     */
    public boolean shouldAllow() {
        return this.fraudScore <= 0.5;
    }

    /**
     * Marks the analysis as failed.
     */
    public void markAsFailed(String errorMessage) {
        this.status = AnalysisStatus.FAILED;
        this.reasons.add("Analysis failed: " + errorMessage);
    }

    /**
     * Cancels the analysis.
     */
    public void cancel() {
        this.status = AnalysisStatus.CANCELLED;
    }
}
