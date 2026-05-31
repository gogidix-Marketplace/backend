package com.gogidix.aiservices.aiworkflowautomationservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for AI Workflow Automation Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class AiWorkflowAutomationMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter requestsTotalCounter;
    private final Counter requestsSuccessCounter;
    private final Counter requestsFailureCounter;
    private final Counter automationsExecutedCounter;
    private final Counter automationsFailedCounter;
    private final Counter automationsTriggeredCounter;

    // Timers
    private final Timer workflowExecutionTimer;
    private final Timer triggerLatencyTimer;
    private final Timer actionExecutionTimer;

    public AiWorkflowAutomationMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.requestsTotalCounter = Counter.builder("ai.workflow.automation.requests.total")
                .description("Total number of workflow automation requests")
                .tag("service", "ai-workflow-automation")
                .register(meterRegistry);

        this.requestsSuccessCounter = Counter.builder("ai.workflow.automation.requests.success")
                .description("Number of successful workflow automation requests")
                .tag("service", "ai-workflow-automation")
                .register(meterRegistry);

        this.requestsFailureCounter = Counter.builder("ai.workflow.automation.requests.failure")
                .description("Number of failed workflow automation requests")
                .tag("service", "ai-workflow-automation")
                .register(meterRegistry);

        this.automationsExecutedCounter = Counter.builder("ai.workflow.automation.executions.total")
                .description("Number of workflow automations executed")
                .tag("service", "ai-workflow-automation")
                .register(meterRegistry);

        this.automationsFailedCounter = Counter.builder("ai.workflow.automation.executions.failed")
                .description("Number of workflow automations failed")
                .tag("service", "ai-workflow-automation")
                .register(meterRegistry);

        this.automationsTriggeredCounter = Counter.builder("ai.workflow.automation.triggered.total")
                .description("Number of workflow automations triggered")
                .tag("service", "ai-workflow-automation")
                .register(meterRegistry);

        // Initialize timers
        this.workflowExecutionTimer = Timer.builder("ai.workflow.automation.execution.duration")
                .description("Workflow automation execution time")
                .tag("service", "ai-workflow-automation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.triggerLatencyTimer = Timer.builder("ai.workflow.automation.trigger.duration")
                .description("Automation trigger latency")
                .tag("service", "ai-workflow-automation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.actionExecutionTimer = Timer.builder("ai.workflow.automation.action.duration")
                .description("Action execution time")
                .tag("service", "ai-workflow-automation")
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

    public void incrementAutomationsExecuted() {
        automationsExecutedCounter.increment();
    }

    public void incrementAutomationsFailed() {
        automationsFailedCounter.increment();
    }

    public void incrementAutomationsTriggered() {
        automationsTriggeredCounter.increment();
    }

    // Timer methods
    public void recordWorkflowExecutionTime(long durationMs) {
        workflowExecutionTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startWorkflowExecutionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopWorkflowExecutionTimer(Timer.Sample sample) {
        sample.stop(workflowExecutionTimer);
    }

    public Timer.Sample startTriggerLatencyTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopTriggerLatencyTimer(Timer.Sample sample) {
        sample.stop(triggerLatencyTimer);
    }

    public Timer.Sample startActionExecutionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopActionExecutionTimer(Timer.Sample sample) {
        sample.stop(actionExecutionTimer);
    }

    // SLO compliance methods
    public double getWorkflowExecutionLatencyP95() {
        return workflowExecutionTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getWorkflowExecutionLatencyP99() {
        return workflowExecutionTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getTriggerLatencyP95() {
        return triggerLatencyTimer.percentile(0.95, TimeUnit.MILLISECONDS);
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
