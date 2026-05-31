package com.gogidix.aiservices.researchintelligenceservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Research Intelligence Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class ResearchIntelligenceMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter analysisTotalCounter;
    private final Counter analysisSuccessCounter;
    private final Counter analysisFailureCounter;
    private final Counter projectCreatedCounter;
    private final Counter projectUpdatedCounter;
    private final Counter projectStartedCounter;
    private final Counter projectCompletedCounter;
    private final Counter projectCancelledCounter;
    private final Counter findingCreatedCounter;
    private final Counter publicationCreatedCounter;
    private final Counter datasetCreatedCounter;

    // Timers
    private final Timer analysisTimer;
    private final Timer projectCreationTimer;
    private final Timer projectUpdateTimer;
    private final Timer findingProcessingTimer;
    private final Timer publicationProcessingTimer;

    public ResearchIntelligenceMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.analysisTotalCounter = Counter.builder("research.analysis.total")
                .description("Total number of research analysis requests")
                .tag("service", "research-intelligence")
                .register(meterRegistry);

        this.analysisSuccessCounter = Counter.builder("research.analysis.success")
                .description("Number of successful research analysis requests")
                .tag("service", "research-intelligence")
                .register(meterRegistry);

        this.analysisFailureCounter = Counter.builder("research.analysis.failure")
                .description("Number of failed research analysis requests")
                .tag("service", "research-intelligence")
                .register(meterRegistry);

        this.projectCreatedCounter = Counter.builder("project.created")
                .description("Number of projects created")
                .tag("service", "research-intelligence")
                .register(meterRegistry);

        this.projectUpdatedCounter = Counter.builder("project.updated")
                .description("Number of projects updated")
                .tag("service", "research-intelligence")
                .register(meterRegistry);

        this.projectStartedCounter = Counter.builder("project.started")
                .description("Number of projects started")
                .tag("service", "research-intelligence")
                .register(meterRegistry);

        this.projectCompletedCounter = Counter.builder("project.completed")
                .description("Number of projects completed")
                .tag("service", "research-intelligence")
                .register(meterRegistry);

        this.projectCancelledCounter = Counter.builder("project.cancelled")
                .description("Number of projects cancelled")
                .tag("service", "research-intelligence")
                .register(meterRegistry);

        this.findingCreatedCounter = Counter.builder("finding.created")
                .description("Number of findings created")
                .tag("service", "research-intelligence")
                .register(meterRegistry);

        this.publicationCreatedCounter = Counter.builder("publication.created")
                .description("Number of publications created")
                .tag("service", "research-intelligence")
                .register(meterRegistry);

        this.datasetCreatedCounter = Counter.builder("dataset.created")
                .description("Number of datasets created")
                .tag("service", "research-intelligence")
                .register(meterRegistry);

        // Initialize timers
        this.analysisTimer = Timer.builder("research.analysis.duration")
                .description("Research analysis processing time")
                .tag("service", "research-intelligence")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.projectCreationTimer = Timer.builder("project.creation.duration")
                .description("Project creation processing time")
                .tag("service", "research-intelligence")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.projectUpdateTimer = Timer.builder("project.update.duration")
                .description("Project update processing time")
                .tag("service", "research-intelligence")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.findingProcessingTimer = Timer.builder("finding.processing.duration")
                .description("Finding processing time")
                .tag("service", "research-intelligence")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.publicationProcessingTimer = Timer.builder("publication.processing.duration")
                .description("Publication processing time")
                .tag("service", "research-intelligence")
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

    public void incrementProjectCreated() {
        projectCreatedCounter.increment();
    }

    public void incrementProjectUpdated() {
        projectUpdatedCounter.increment();
    }

    public void incrementProjectStarted() {
        projectStartedCounter.increment();
    }

    public void incrementProjectCompleted() {
        projectCompletedCounter.increment();
    }

    public void incrementProjectCancelled() {
        projectCancelledCounter.increment();
    }

    public void incrementFindingCreated() {
        findingCreatedCounter.increment();
    }

    public void incrementPublicationCreated() {
        publicationCreatedCounter.increment();
    }

    public void incrementDatasetCreated() {
        datasetCreatedCounter.increment();
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

    public Timer.Sample startProjectCreationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopProjectCreationTimer(Timer.Sample sample) {
        sample.stop(projectCreationTimer);
    }

    public Timer.Sample startProjectUpdateTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopProjectUpdateTimer(Timer.Sample sample) {
        sample.stop(projectUpdateTimer);
    }

    // SLO compliance methods
    public double getAnalysisLatencyP95() {
        return analysisTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getAnalysisLatencyP99() {
        return analysisTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getProjectCreationLatencyP95() {
        return projectCreationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
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
