package com.gogidix.aiservices.aicustomerfeedbackservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class CustomerFeedbackMetrics {

    private final MeterRegistry meterRegistry;

    private final Counter feedbackTotalCounter;
    private final Counter feedbackSuccessCounter;
    private final Counter feedbackFailureCounter;
    private final Counter feedbackProcessedCounter;

    private final Timer feedbackTimer;
    private final Timer nlpProcessingTimer;

    public CustomerFeedbackMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.feedbackTotalCounter = Counter.builder("ai.customer.feedback.total")
                .description("Total number of feedback processing requests")
                .tag("service", "ai-customer-feedback")
                .register(meterRegistry);

        this.feedbackSuccessCounter = Counter.builder("ai.customer.feedback.success")
                .description("Number of successful feedback processing requests")
                .tag("service", "ai-customer-feedback")
                .register(meterRegistry);

        this.feedbackFailureCounter = Counter.builder("ai.customer.feedback.failure")
                .description("Number of failed feedback processing requests")
                .tag("service", "ai-customer-feedback")
                .register(meterRegistry);

        this.feedbackProcessedCounter = Counter.builder("ai.customer.feedback.processed")
                .description("Number of feedback items processed")
                .tag("service", "ai-customer-feedback")
                .register(meterRegistry);

        this.feedbackTimer = Timer.builder("ai.customer.feedback.duration")
                .description("Feedback processing time")
                .tag("service", "ai-customer-feedback")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.nlpProcessingTimer = Timer.builder("ai.customer.feedback.nlp.processing.duration")
                .description("NLP processing time")
                .tag("service", "ai-customer-feedback")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    public void incrementFeedbackTotal() {
        feedbackTotalCounter.increment();
    }

    public void incrementFeedbackSuccess() {
        feedbackSuccessCounter.increment();
    }

    public void incrementFeedbackFailure() {
        feedbackFailureCounter.increment();
    }

    public void incrementFeedbackProcessed() {
        feedbackProcessedCounter.increment();
    }

    public void recordFeedbackTime(long durationMs) {
        feedbackTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startFeedbackTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopFeedbackTimer(Timer.Sample sample) {
        sample.stop(feedbackTimer);
    }

    public double getFeedbackLatencyP95() {
        return feedbackTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getFeedbackLatencyP99() {
        return feedbackTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getNlpProcessingLatencyP95() {
        return nlpProcessingTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) feedbackTotalCounter.count();
        long failures = (long) feedbackFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
