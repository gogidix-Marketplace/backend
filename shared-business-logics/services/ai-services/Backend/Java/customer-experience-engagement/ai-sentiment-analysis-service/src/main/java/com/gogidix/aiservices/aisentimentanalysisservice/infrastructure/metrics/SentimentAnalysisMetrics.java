package com.gogidix.aiservices.aisentimentanalysisservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class SentimentAnalysisMetrics {

    private final MeterRegistry meterRegistry;

    private final Counter analysisTotalCounter;
    private final Counter analysisSuccessCounter;
    private final Counter analysisFailureCounter;
    private final Counter sentimentsAnalyzedCounter;

    private final Timer analysisTimer;
    private final Timer aiInferenceTimer;

    public SentimentAnalysisMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.analysisTotalCounter = Counter.builder("ai.sentiment.analysis.total")
                .description("Total number of sentiment analysis requests")
                .tag("service", "ai-sentiment-analysis")
                .register(meterRegistry);

        this.analysisSuccessCounter = Counter.builder("ai.sentiment.analysis.success")
                .description("Number of successful sentiment analysis requests")
                .tag("service", "ai-sentiment-analysis")
                .register(meterRegistry);

        this.analysisFailureCounter = Counter.builder("ai.sentiment.analysis.failure")
                .description("Number of failed sentiment analysis requests")
                .tag("service", "ai-sentiment-analysis")
                .register(meterRegistry);

        this.sentimentsAnalyzedCounter = Counter.builder("ai.sentiment.analysis.analyzed")
                .description("Number of sentiments analyzed")
                .tag("service", "ai-sentiment-analysis")
                .register(meterRegistry);

        this.analysisTimer = Timer.builder("ai.sentiment.analysis.duration")
                .description("Sentiment analysis processing time")
                .tag("service", "ai-sentiment-analysis")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.aiInferenceTimer = Timer.builder("ai.sentiment.analysis.ai.inference.duration")
                .description("AI inference time")
                .tag("service", "ai-sentiment-analysis")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    public void incrementAnalysisTotal() {
        analysisTotalCounter.increment();
    }

    public void incrementAnalysisSuccess() {
        analysisSuccessCounter.increment();
    }

    public void incrementAnalysisFailure() {
        analysisFailureCounter.increment();
    }

    public void incrementSentimentsAnalyzed() {
        sentimentsAnalyzedCounter.increment();
    }

    public void recordAnalysisTime(long durationMs) {
        analysisTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startAnalysisTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopAnalysisTimer(Timer.Sample sample) {
        sample.stop(analysisTimer);
    }

    public double getAnalysisLatencyP95() {
        return analysisTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getAnalysisLatencyP99() {
        return analysisTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getAiInferenceLatencyP95() {
        return aiInferenceTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) analysisTotalCounter.count();
        long failures = (long) analysisFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
