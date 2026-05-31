package com.gogidix.aiservices.aicustomerengagementservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for AI Customer Engagement Service.
 * Tracks key performance indicators for SLO monitoring.
 *
 * SLO Thresholds for Customer Engagement:
 * - P95 Latency: < 200ms
 * - P99 Latency: < 400ms
 * - Error Rate: < 1%
 */
@Component
public class CustomerEngagementMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter engagementTotalCounter;
    private final Counter engagementSuccessCounter;
    private final Counter engagementFailureCounter;
    private final Counter campaignsCreatedCounter;
    private final Counter messagesSentCounter;
    private final Counter interactionsRecordedCounter;

    // Timers
    private final Timer engagementTimer;
    private final Timer campaignCreationTimer;
    private final Timer messageDeliveryTimer;
    private final Timer mlPredictionTimer;

    public CustomerEngagementMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.engagementTotalCounter = Counter.builder("engagement.analysis.total")
                .description("Total number of customer engagement requests")
                .tag("service", "ai-customer-engagement")
                .register(meterRegistry);

        this.engagementSuccessCounter = Counter.builder("engagement.analysis.success")
                .description("Number of successful customer engagement requests")
                .tag("service", "ai-customer-engagement")
                .register(meterRegistry);

        this.engagementFailureCounter = Counter.builder("engagement.analysis.failure")
                .description("Number of failed customer engagement requests")
                .tag("service", "ai-customer-engagement")
                .register(meterRegistry);

        this.campaignsCreatedCounter = Counter.builder("engagement.campaigns.created")
                .description("Number of engagement campaigns created")
                .tag("service", "ai-customer-engagement")
                .register(meterRegistry);

        this.messagesSentCounter = Counter.builder("engagement.messages.sent")
                .description("Number of engagement messages sent")
                .tag("service", "ai-customer-engagement")
                .register(meterRegistry);

        this.interactionsRecordedCounter = Counter.builder("engagement.interactions.recorded")
                .description("Number of customer interactions recorded")
                .tag("service", "ai-customer-engagement")
                .register(meterRegistry);

        // Initialize timers
        this.engagementTimer = Timer.builder("engagement.analysis.duration")
                .description("Customer engagement processing time")
                .tag("service", "ai-customer-engagement")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.campaignCreationTimer = Timer.builder("engagement.campaign.creation.duration")
                .description("Campaign creation time")
                .tag("service", "ai-customer-engagement")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.messageDeliveryTimer = Timer.builder("engagement.message.delivery.duration")
                .description("Message delivery time")
                .tag("service", "ai-customer-engagement")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.mlPredictionTimer = Timer.builder("engagement.ml.prediction.duration")
                .description("ML model prediction time for engagement")
                .tag("service", "ai-customer-engagement")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementEngagementTotal() {
        engagementTotalCounter.increment();
    }

    public void incrementEngagementSuccess() {
        engagementSuccessCounter.increment();
    }

    public void incrementEngagementFailure() {
        engagementFailureCounter.increment();
    }

    public void incrementCampaignsCreated() {
        campaignsCreatedCounter.increment();
    }

    public void incrementMessagesSent() {
        messagesSentCounter.increment();
    }

    public void incrementInteractionsRecorded() {
        interactionsRecordedCounter.increment();
    }

    // Timer methods
    public void recordEngagementTime(long durationMs) {
        engagementTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startEngagementTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopEngagementTimer(Timer.Sample sample) {
        sample.stop(engagementTimer);
    }

    public Timer.Sample startCampaignCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopCampaignCreationTimer(Timer.Sample sample) {
        sample.stop(campaignCreationTimer);
    }

    public Timer.Sample startMessageDeliveryTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopMessageDeliveryTimer(Timer.Sample sample) {
        sample.stop(messageDeliveryTimer);
    }

    public Timer.Sample startMlPredictionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopMlPredictionTimer(Timer.Sample sample) {
        sample.stop(mlPredictionTimer);
    }

    // SLO compliance methods
    public double getEngagementLatencyP95() {
        return engagementTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getEngagementLatencyP99() {
        return engagementTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getMlPredictionLatencyP95() {
        return mlPredictionTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) engagementTotalCounter.count();
        long failures = (long) engagementFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
