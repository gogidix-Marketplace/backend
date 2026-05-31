package com.gogidix.aiservices.leadgenerationaiservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Lead Generation AI Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class LeadGenerationMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter leadScoreTotalCounter;
    private final Counter leadScoreSuccessCounter;
    private final Counter leadScoreFailureCounter;
    private final Counter leadCreatedCounter;
    private final Counter leadConvertedCounter;
    private final Counter highQualityLeadCounter;

    // Timers
    private final Timer leadScoringTimer;
    private final Timer aiPredictionTimer;
    private final Timer leadEnrichmentTimer;

    public LeadGenerationMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters with ai.lead.generation prefix
        this.leadScoreTotalCounter = Counter.builder("ai.lead.generation.score.total")
                .description("Total number of lead scoring requests")
                .tag("service", "lead-generation-ai")
                .register(meterRegistry);

        this.leadScoreSuccessCounter = Counter.builder("ai.lead.generation.score.success")
                .description("Number of successful lead scoring requests")
                .tag("service", "lead-generation-ai")
                .register(meterRegistry);

        this.leadScoreFailureCounter = Counter.builder("ai.lead.generation.score.failure")
                .description("Number of failed lead scoring requests")
                .tag("service", "lead-generation-ai")
                .register(meterRegistry);

        this.leadCreatedCounter = Counter.builder("ai.lead.generation.created")
                .description("Number of leads created")
                .tag("service", "lead-generation-ai")
                .register(meterRegistry);

        this.leadConvertedCounter = Counter.builder("ai.lead.generation.converted")
                .description("Number of leads converted")
                .tag("service", "lead-generation-ai")
                .register(meterRegistry);

        this.highQualityLeadCounter = Counter.builder("ai.lead.generation.high.quality")
                .description("Number of high quality leads identified")
                .tag("service", "lead-generation-ai")
                .register(meterRegistry);

        // Initialize timers
        this.leadScoringTimer = Timer.builder("ai.lead.generation.scoring.duration")
                .description("Lead scoring processing time")
                .tag("service", "lead-generation-ai")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.aiPredictionTimer = Timer.builder("ai.lead.generation.prediction.duration")
                .description("AI model prediction time for lead scoring")
                .tag("service", "lead-generation-ai")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.leadEnrichmentTimer = Timer.builder("ai.lead.generation.enrichment.duration")
                .description("Lead enrichment operation time")
                .tag("service", "lead-generation-ai")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementLeadScoreTotal() {
        leadScoreTotalCounter.increment();
    }

    public void incrementLeadScoreSuccess() {
        leadScoreSuccessCounter.increment();
    }

    public void incrementLeadScoreFailure() {
        leadScoreFailureCounter.increment();
    }

    public void incrementLeadCreated() {
        leadCreatedCounter.increment();
    }

    public void incrementLeadConverted() {
        leadConvertedCounter.increment();
    }

    public void incrementHighQualityLead() {
        highQualityLeadCounter.increment();
    }

    // Timer methods
    public void recordLeadScoringTime(long durationMs) {
        leadScoringTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startLeadScoringTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopLeadScoringTimer(Timer.Sample sample) {
        sample.stop(leadScoringTimer);
    }

    public Timer.Sample startAiPredictionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopAiPredictionTimer(Timer.Sample sample) {
        sample.stop(aiPredictionTimer);
    }

    public void recordLeadEnrichmentTime(long durationMs) {
        leadEnrichmentTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startLeadEnrichmentTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopLeadEnrichmentTimer(Timer.Sample sample) {
        sample.stop(leadEnrichmentTimer);
    }

    // SLO compliance methods
    public double getLeadScoringLatencyP95() {
        return leadScoringTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getLeadScoringLatencyP99() {
        return leadScoringTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getAiPredictionLatencyP95() {
        return aiPredictionTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) leadScoreTotalCounter.count();
        long failures = (long) leadScoreFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
