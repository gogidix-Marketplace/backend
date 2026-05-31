package com.gogidix.aiservices.aifrauddetectionservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Fraud Detection Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class FraudDetectionMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter analysisTotalCounter;
    private final Counter analysisSuccessCounter;
    private final Counter analysisFailureCounter;
    private final Counter fraudDetectedCounter;
    private final Counter highRiskTransactionsCounter;
    private final Counter blockedTransactionsCounter;

    // Timers
    private final Timer analysisTimer;
    private final Timer mlPredictionTimer;
    private final Timer databaseSaveTimer;

    public FraudDetectionMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.analysisTotalCounter = Counter.builder("fraud.analysis.total")
                .description("Total number of fraud analysis requests")
                .tag("service", "ai-fraud-detection")
                .register(meterRegistry);

        this.analysisSuccessCounter = Counter.builder("fraud.analysis.success")
                .description("Number of successful fraud analysis requests")
                .tag("service", "ai-fraud-detection")
                .register(meterRegistry);

        this.analysisFailureCounter = Counter.builder("fraud.analysis.failure")
                .description("Number of failed fraud analysis requests")
                .tag("service", "ai-fraud-detection")
                .register(meterRegistry);

        this.fraudDetectedCounter = Counter.builder("fraud.detected")
                .description("Number of fraud cases detected")
                .tag("service", "ai-fraud-detection")
                .register(meterRegistry);

        this.highRiskTransactionsCounter = Counter.builder("fraud.high.risk")
                .description("Number of high-risk transactions")
                .tag("service", "ai-fraud-detection")
                .register(meterRegistry);

        this.blockedTransactionsCounter = Counter.builder("fraud.blocked")
                .description("Number of transactions blocked")
                .tag("service", "ai-fraud-detection")
                .register(meterRegistry);

        // Initialize timers
        this.analysisTimer = Timer.builder("fraud.analysis.duration")
                .description("Fraud analysis processing time")
                .tag("service", "ai-fraud-detection")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.mlPredictionTimer = Timer.builder("fraud.ml.prediction.duration")
                .description("ML model prediction time")
                .tag("service", "ai-fraud-detection")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.databaseSaveTimer = Timer.builder("fraud.database.save.duration")
                .description("Database save operation time")
                .tag("service", "ai-fraud-detection")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementAnalysisTotal() {
        analysisTotalCounter.increment();
    }

    public void incrementAnalysisSuccess() {
        analysisSuccessCounter.increment();
    }

    public void incrementAnalysisFailure() {
        analysisFailureCounter.increment();
    }

    public void incrementFraudDetected() {
        fraudDetectedCounter.increment();
    }

    public void incrementHighRisk() {
        highRiskTransactionsCounter.increment();
    }

    public void incrementBlocked() {
        blockedTransactionsCounter.increment();
    }

    // Timer methods
    public void recordAnalysisTime(long durationMs) {
        analysisTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startAnalysisTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopAnalysisTimer(Timer.Sample sample) {
        sample.stop(analysisTimer);
    }

    public Timer.Sample startMlPredictionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopMlPredictionTimer(Timer.Sample sample) {
        sample.stop(mlPredictionTimer);
    }

    public void recordDatabaseSaveTime(long durationMs) {
        databaseSaveTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startDatabaseSaveTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopDatabaseSaveTimer(Timer.Sample sample) {
        sample.stop(databaseSaveTimer);
    }

    // SLO compliance methods
    public double getAnalysisLatencyP95() {
        return analysisTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getAnalysisLatencyP99() {
        return analysisTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getMlPredictionLatencyP95() {
        return mlPredictionTimer.percentile(0.95, TimeUnit.MILLISECONDS);
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
