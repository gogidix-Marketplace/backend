package com.gogidix.aiservices.aireporting.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for AI Reporting Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class ReportingMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter reportGenerationTotalCounter;
    private final Counter reportGenerationSuccessCounter;
    private final Counter reportGenerationFailureCounter;
    private final Counter reportDownloadCounter;
    private final Counter dataExtractionCounter;

    // Timers
    private final Timer reportGenerationTimer;
    private final Timer dataExtractionTimer;
    private final Timer reportFormattingTimer;

    public ReportingMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.reportGenerationTotalCounter = Counter.builder("report.generation.total")
                .description("Total number of report generation requests")
                .tag("service", "ai-reporting")
                .register(meterRegistry);

        this.reportGenerationSuccessCounter = Counter.builder("report.generation.success")
                .description("Number of successful report generation requests")
                .tag("service", "ai-reporting")
                .register(meterRegistry);

        this.reportGenerationFailureCounter = Counter.builder("report.generation.failure")
                .description("Number of failed report generation requests")
                .tag("service", "ai-reporting")
                .register(meterRegistry);

        this.reportDownloadCounter = Counter.builder("report.download")
                .description("Number of report downloads")
                .tag("service", "ai-reporting")
                .register(meterRegistry);

        this.dataExtractionCounter = Counter.builder("report.data.extraction")
                .description("Number of data extraction operations")
                .tag("service", "ai-reporting")
                .register(meterRegistry);

        // Initialize timers
        this.reportGenerationTimer = Timer.builder("report.generation.duration")
                .description("Report generation processing time")
                .tag("service", "ai-reporting")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.dataExtractionTimer = Timer.builder("report.data.extraction.duration")
                .description("Data extraction processing time")
                .tag("service", "ai-reporting")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.reportFormattingTimer = Timer.builder("report.formatting.duration")
                .description("Report formatting processing time")
                .tag("service", "ai-reporting")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementReportGenerationTotal() {
        reportGenerationTotalCounter.increment();
    }

    public void incrementReportGenerationSuccess() {
        reportGenerationSuccessCounter.increment();
    }

    public void incrementReportGenerationFailure() {
        reportGenerationFailureCounter.increment();
    }

    public void incrementReportDownload() {
        reportDownloadCounter.increment();
    }

    public void incrementDataExtraction() {
        dataExtractionCounter.increment();
    }

    // Timer methods
    public void recordReportGenerationTime(long durationMs) {
        reportGenerationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startReportGenerationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopReportGenerationTimer(Timer.Sample sample) {
        sample.stop(reportGenerationTimer);
    }

    public void recordDataExtractionTime(long durationMs) {
        dataExtractionTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startDataExtractionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopDataExtractionTimer(Timer.Sample sample) {
        sample.stop(dataExtractionTimer);
    }

    public void recordReportFormattingTime(long durationMs) {
        reportFormattingTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startReportFormattingTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopReportFormattingTimer(Timer.Sample sample) {
        sample.stop(reportFormattingTimer);
    }

    // SLO compliance methods
    public double getReportGenerationLatencyP95() {
        return reportGenerationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getReportGenerationLatencyP99() {
        return reportGenerationTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getDataExtractionLatencyP95() {
        return dataExtractionTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) reportGenerationTotalCounter.count();
        long failures = (long) reportGenerationFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
