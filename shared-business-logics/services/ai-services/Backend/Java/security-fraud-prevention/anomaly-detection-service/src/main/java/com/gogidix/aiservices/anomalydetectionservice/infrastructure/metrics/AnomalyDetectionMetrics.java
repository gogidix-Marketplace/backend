package com.gogidix.aiservices.anomalydetectionservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Anomaly Detection Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class AnomalyDetectionMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter detectionTotalCounter;
    private final Counter detectionSuccessCounter;
    private final Counter detectionFailureCounter;
    private final Counter anomaliesFoundCounter;
    private final Counter criticalAnomaliesCounter;
    private final Counter highAnomaliesCounter;
    private final Counter mediumAnomaliesCounter;
    private final Counter lowAnomaliesCounter;

    // Timers
    private final Timer detectionTimer;
    private final Timer analysisTimer;
    private final Timer modelInferenceTimer;

    public AnomalyDetectionMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        this.detectionTotalCounter = Counter.builder("anomaly.detection.total")
                .description("Total number of anomaly detection requests")
                .tag("service", "anomaly-detection")
                .register(meterRegistry);

        this.detectionSuccessCounter = Counter.builder("anomaly.detection.success")
                .description("Number of successful detections")
                .tag("service", "anomaly-detection")
                .register(meterRegistry);

        this.detectionFailureCounter = Counter.builder("anomaly.detection.failure")
                .description("Number of failed detections")
                .tag("service", "anomaly-detection")
                .register(meterRegistry);

        this.anomaliesFoundCounter = Counter.builder("anomaly.found")
                .description("Total number of anomalies found")
                .tag("service", "anomaly-detection")
                .register(meterRegistry);

        this.criticalAnomaliesCounter = Counter.builder("anomaly.critical")
                .description("Number of critical anomalies")
                .tag("service", "anomaly-detection")
                .register(meterRegistry);

        this.highAnomaliesCounter = Counter.builder("anomaly.high")
                .description("Number of high severity anomalies")
                .tag("service", "anomaly-detection")
                .register(meterRegistry);

        this.mediumAnomaliesCounter = Counter.builder("anomaly.medium")
                .description("Number of medium severity anomalies")
                .tag("service", "anomaly-detection")
                .register(meterRegistry);

        this.lowAnomaliesCounter = Counter.builder("anomaly.low")
                .description("Number of low severity anomalies")
                .tag("service", "anomaly-detection")
                .register(meterRegistry);

        this.detectionTimer = Timer.builder("anomaly.detection.duration")
                .description("Full detection processing time")
                .tag("service", "anomaly-detection")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.analysisTimer = Timer.builder("anomaly.analysis.duration")
                .description("Data analysis time")
                .tag("service", "anomaly-detection")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.modelInferenceTimer = Timer.builder("anomaly.model.inference.duration")
                .description("ML model inference time")
                .tag("service", "anomaly-detection")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    public void incrementDetectionTotal() { detectionTotalCounter.increment(); }
    public void incrementDetectionSuccess() { detectionSuccessCounter.increment(); }
    public void incrementDetectionFailure() { detectionFailureCounter.increment(); }
    public void incrementAnomaliesFound() { anomaliesFoundCounter.increment(); }
    public void incrementCriticalAnomaly() { criticalAnomaliesCounter.increment(); }
    public void incrementHighAnomaly() { highAnomaliesCounter.increment(); }
    public void incrementMediumAnomaly() { mediumAnomaliesCounter.increment(); }
    public void incrementLowAnomaly() { lowAnomaliesCounter.increment(); }

    public void recordDetectionTime(long durationMs) { detectionTimer.record(durationMs, TimeUnit.MILLISECONDS); }
    public void recordAnalysisTime(long durationMs) { analysisTimer.record(durationMs, TimeUnit.MILLISECONDS); }
    public void recordModelInferenceTime(long durationMs) { modelInferenceTimer.record(durationMs, TimeUnit.MILLISECONDS); }

    public Timer.Sample startDetectionTimer() { return Timer.start(meterRegistry); }
    public void stopDetectionTimer(Timer.Sample sample) { sample.stop(detectionTimer); }
    public Timer.Sample startAnalysisTimer() { return Timer.start(meterRegistry); }
    public void stopAnalysisTimer(Timer.Sample sample) { sample.stop(analysisTimer); }
    public Timer.Sample startModelInferenceTimer() { return Timer.start(meterRegistry); }
    public void stopModelInferenceTimer(Timer.Sample sample) { sample.stop(modelInferenceTimer); }

    public double getDetectionLatencyP95() { return detectionTimer.percentile(0.95, TimeUnit.MILLISECONDS); }
    public double getModelInferenceLatencyP95() { return modelInferenceTimer.percentile(0.95, TimeUnit.MILLISECONDS); }
    public double getErrorRate() {
        long total = (long) detectionTotalCounter.count();
        long failures = (long) detectionFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() { return meterRegistry; }
}
