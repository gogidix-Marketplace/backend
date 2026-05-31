package com.gogidix.aiservices.aisecurityanalysisservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Security Analysis Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class SecurityAnalysisMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter scanTotalCounter;
    private final Counter scanCompletedCounter;
    private final Counter scanFailedCounter;
    private final Counter vulnerabilitiesFoundCounter;
    private final Counter criticalVulnerabilitiesCounter;
    private final Counter highVulnerabilitiesCounter;
    private final Counter mediumVulnerabilitiesCounter;
    private final Counter lowVulnerabilitiesCounter;
    private final Counter reportsGeneratedCounter;

    // Timers
    private final Timer scanDurationTimer;
    private final Timer vulnerabilityDetectionTimer;
    private final Timer reportGenerationTimer;
    private final Timer databaseOperationTimer;

    public SecurityAnalysisMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.scanTotalCounter = Counter.builder("security.scan.total")
                .description("Total number of security scans initiated")
                .tag("service", "ai-security-analysis")
                .register(meterRegistry);

        this.scanCompletedCounter = Counter.builder("security.scan.completed")
                .description("Number of scans completed successfully")
                .tag("service", "ai-security-analysis")
                .register(meterRegistry);

        this.scanFailedCounter = Counter.builder("security.scan.failed")
                .description("Number of scans that failed")
                .tag("service", "ai-security-analysis")
                .register(meterRegistry);

        this.vulnerabilitiesFoundCounter = Counter.builder("security.vulnerability.found")
                .description("Total number of vulnerabilities found")
                .tag("service", "ai-security-analysis")
                .register(meterRegistry);

        this.criticalVulnerabilitiesCounter = Counter.builder("security.vulnerability.critical")
                .description("Number of critical severity vulnerabilities")
                .tag("service", "ai-security-analysis")
                .register(meterRegistry);

        this.highVulnerabilitiesCounter = Counter.builder("security.vulnerability.high")
                .description("Number of high severity vulnerabilities")
                .tag("service", "ai-security-analysis")
                .register(meterRegistry);

        this.mediumVulnerabilitiesCounter = Counter.builder("security.vulnerability.medium")
                .description("Number of medium severity vulnerabilities")
                .tag("service", "ai-security-analysis")
                .register(meterRegistry);

        this.lowVulnerabilitiesCounter = Counter.builder("security.vulnerability.low")
                .description("Number of low severity vulnerabilities")
                .tag("service", "ai-security-analysis")
                .register(meterRegistry);

        this.reportsGeneratedCounter = Counter.builder("security.report.generated")
                .description("Number of security reports generated")
                .tag("service", "ai-security-analysis")
                .register(meterRegistry);

        // Initialize timers
        this.scanDurationTimer = Timer.builder("security.scan.duration")
                .description("Full security scan duration")
                .tag("service", "ai-security-analysis")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.vulnerabilityDetectionTimer = Timer.builder("security.vulnerability.detection.duration")
                .description("Vulnerability detection processing time")
                .tag("service", "ai-security-analysis")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.reportGenerationTimer = Timer.builder("security.report.generation.duration")
                .description("Security report generation time")
                .tag("service", "ai-security-analysis")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.databaseOperationTimer = Timer.builder("security.database.operation.duration")
                .description("Database operation time")
                .tag("service", "ai-security-analysis")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementScanTotal() {
        scanTotalCounter.increment();
    }

    public void incrementScanCompleted() {
        scanCompletedCounter.increment();
    }

    public void incrementScanFailed() {
        scanFailedCounter.increment();
    }

    public void incrementVulnerabilitiesFound() {
        vulnerabilitiesFoundCounter.increment();
    }

    public void incrementCriticalVulnerability() {
        criticalVulnerabilitiesCounter.increment();
    }

    public void incrementHighVulnerability() {
        highVulnerabilitiesCounter.increment();
    }

    public void incrementMediumVulnerability() {
        mediumVulnerabilitiesCounter.increment();
    }

    public void incrementLowVulnerability() {
        lowVulnerabilitiesCounter.increment();
    }

    public void incrementReportsGenerated() {
        reportsGeneratedCounter.increment();
    }

    // Timer methods
    public void recordScanTime(long durationMs) {
        scanDurationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startScanTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopScanTimer(Timer.Sample sample) {
        sample.stop(scanDurationTimer);
    }

    public Timer.Sample startVulnerabilityDetectionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopVulnerabilityDetectionTimer(Timer.Sample sample) {
        sample.stop(vulnerabilityDetectionTimer);
    }

    public Timer.Sample startReportGenerationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopReportGenerationTimer(Timer.Sample sample) {
        sample.stop(reportGenerationTimer);
    }

    public Timer.Sample startDatabaseOperationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopDatabaseOperationTimer(Timer.Sample sample) {
        sample.stop(databaseOperationTimer);
    }

    // SLO compliance methods
    public double getScanLatencyP95() {
        return scanDurationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getScanLatencyP99() {
        return scanDurationTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getVulnerabilityDetectionLatencyP95() {
        return vulnerabilityDetectionTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) scanTotalCounter.count();
        long failures = (long) scanFailedCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public double getSuccessRate() {
        long total = (long) scanTotalCounter.count();
        long successes = (long) scanCompletedCounter.count();
        return total > 0 ? (double) successes / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
