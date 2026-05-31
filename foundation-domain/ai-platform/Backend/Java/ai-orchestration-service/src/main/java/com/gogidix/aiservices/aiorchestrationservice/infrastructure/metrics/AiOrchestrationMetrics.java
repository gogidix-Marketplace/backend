package com.gogidix.aiservices.aiorchestrationservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for AI Orchestration Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class AiOrchestrationMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter requestsTotalCounter;
    private final Counter requestsSuccessCounter;
    private final Counter requestsFailureCounter;
    private final Counter workflowsExecutedCounter;
    private final Counter workflowsFailedCounter;
    private final Counter stepsExecutedCounter;
    private final Counter stepsFailedCounter;

    // Timers
    private final Timer orchestrationLatencyTimer;
    private final Timer stepExecutionTimer;
    private final Timer workflowExecutionTimer;

    public AiOrchestrationMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.requestsTotalCounter = Counter.builder("ai.orchestration.requests.total")
                .description("Total number of orchestration requests")
                .tag("service", "ai-orchestration")
                .register(meterRegistry);

        this.requestsSuccessCounter = Counter.builder("ai.orchestration.requests.success")
                .description("Number of successful orchestration requests")
                .tag("service", "ai-orchestration")
                .register(meterRegistry);

        this.requestsFailureCounter = Counter.builder("ai.orchestration.requests.failure")
                .description("Number of failed orchestration requests")
                .tag("service", "ai-orchestration")
                .register(meterRegistry);

        this.workflowsExecutedCounter = Counter.builder("ai.orchestration.workflows.executed")
                .description("Number of workflows executed")
                .tag("service", "ai-orchestration")
                .register(meterRegistry);

        this.workflowsFailedCounter = Counter.builder("ai.orchestration.workflows.failed")
                .description("Number of workflows failed")
                .tag("service", "ai-orchestration")
                .register(meterRegistry);

        this.stepsExecutedCounter = Counter.builder("ai.orchestration.steps.executed")
                .description("Number of workflow steps executed")
                .tag("service", "ai-orchestration")
                .register(meterRegistry);

        this.stepsFailedCounter = Counter.builder("ai.orchestration.steps.failed")
                .description("Number of workflow steps failed")
                .tag("service", "ai-orchestration")
                .register(meterRegistry);

        // Initialize timers
        this.orchestrationLatencyTimer = Timer.builder("ai.orchestration.duration")
                .description("Orchestration request processing time")
                .tag("service", "ai-orchestration")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.stepExecutionTimer = Timer.builder("ai.orchestration.step.duration")
                .description("Workflow step execution time")
                .tag("service", "ai-orchestration")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.workflowExecutionTimer = Timer.builder("ai.orchestration.workflow.execution.duration")
                .description("Complete workflow execution time")
                .tag("service", "ai-orchestration")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementRequestsTotal() {
        requestsTotalCounter.increment();
    }

    public void incrementRequestsSuccess() {
        requestsSuccessCounter.increment();
    }

    public void incrementRequestsFailure() {
        requestsFailureCounter.increment();
    }

    public void incrementWorkflowsExecuted() {
        workflowsExecutedCounter.increment();
    }

    public void incrementWorkflowsFailed() {
        workflowsFailedCounter.increment();
    }

    public void incrementStepsExecuted() {
        stepsExecutedCounter.increment();
    }

    public void incrementStepsFailed() {
        stepsFailedCounter.increment();
    }

    // Timer methods
    public void recordOrchestrationTime(long durationMs) {
        orchestrationLatencyTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startOrchestrationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopOrchestrationTimer(Timer.Sample sample) {
        sample.stop(orchestrationLatencyTimer);
    }

    public Timer.Sample startStepExecutionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopStepExecutionTimer(Timer.Sample sample) {
        sample.stop(stepExecutionTimer);
    }

    public Timer.Sample startWorkflowExecutionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopWorkflowExecutionTimer(Timer.Sample sample) {
        sample.stop(workflowExecutionTimer);
    }

    // SLO compliance methods
    public double getOrchestrationLatencyP95() {
        return orchestrationLatencyTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getOrchestrationLatencyP99() {
        return orchestrationLatencyTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getStepExecutionLatencyP95() {
        return stepExecutionTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) requestsTotalCounter.count();
        long failures = (long) requestsFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
