package com.gogidix.aiservices.aibusinessautomationservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for Business Automation Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class BusinessAutomationMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter automationTotalCounter;
    private final Counter automationSuccessCounter;
    private final Counter automationFailureCounter;
    private final Counter workflowsExecutedCounter;
    private final Counter approvalsProcessedCounter;
    private final Counter documentsProcessedCounter;
    private final Counter notificationsSentCounter;

    // Timers
    private final Timer automationTimer;
    private final Timer workflowExecutionTimer;
    private final Timer approvalProcessingTimer;
    private final Timer documentProcessingTimer;
    private final Timer aiDecisionTimer;

    public BusinessAutomationMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.automationTotalCounter = Counter.builder("ai.business.automation.total")
                .description("Total number of business automation requests")
                .tag("service", "ai-business-automation")
                .register(meterRegistry);

        this.automationSuccessCounter = Counter.builder("ai.business.automation.success")
                .description("Number of successful business automation requests")
                .tag("service", "ai-business-automation")
                .register(meterRegistry);

        this.automationFailureCounter = Counter.builder("ai.business.automation.failure")
                .description("Number of failed business automation requests")
                .tag("service", "ai-business-automation")
                .register(meterRegistry);

        this.workflowsExecutedCounter = Counter.builder("ai.business.automation.workflow.executed")
                .description("Number of workflows executed")
                .tag("service", "ai-business-automation")
                .register(meterRegistry);

        this.approvalsProcessedCounter = Counter.builder("ai.business.automation.approval.processed")
                .description("Number of approvals processed")
                .tag("service", "ai-business-automation")
                .register(meterRegistry);

        this.documentsProcessedCounter = Counter.builder("ai.business.automation.document.processed")
                .description("Number of documents processed")
                .tag("service", "ai-business-automation")
                .register(meterRegistry);

        this.notificationsSentCounter = Counter.builder("ai.business.automation.notification.sent")
                .description("Number of notifications sent")
                .tag("service", "ai-business-automation")
                .register(meterRegistry);

        // Initialize timers
        this.automationTimer = Timer.builder("ai.business.automation.duration")
                .description("Business automation processing time")
                .tag("service", "ai-business-automation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.workflowExecutionTimer = Timer.builder("ai.business.automation.workflow.duration")
                .description("Workflow execution time")
                .tag("service", "ai-business-automation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.approvalProcessingTimer = Timer.builder("ai.business.automation.approval.duration")
                .description("Approval processing time")
                .tag("service", "ai-business-automation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.documentProcessingTimer = Timer.builder("ai.business.automation.document.duration")
                .description("Document processing time")
                .tag("service", "ai-business-automation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.aiDecisionTimer = Timer.builder("ai.business.automation.decision.duration")
                .description("AI decision making time")
                .tag("service", "ai-business-automation")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }

    // Counter methods
    public void incrementAutomationTotal() {
        automationTotalCounter.increment();
    }

    public void incrementAutomationSuccess() {
        automationSuccessCounter.increment();
    }

    public void incrementAutomationFailure() {
        automationFailureCounter.increment();
    }

    public void incrementWorkflowsExecuted() {
        workflowsExecutedCounter.increment();
    }

    public void incrementApprovalsProcessed() {
        approvalsProcessedCounter.increment();
    }

    public void incrementDocumentsProcessed() {
        documentsProcessedCounter.increment();
    }

    public void incrementNotificationsSent() {
        notificationsSentCounter.increment();
    }

    // Timer methods
    public void recordAutomationTime(long durationMs) {
        automationTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startAutomationTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopAutomationTimer(Timer.Sample sample) {
        sample.stop(automationTimer);
    }

    public void recordWorkflowExecutionTime(long durationMs) {
        workflowExecutionTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startWorkflowExecutionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopWorkflowExecutionTimer(Timer.Sample sample) {
        sample.stop(workflowExecutionTimer);
    }

    public void recordApprovalProcessingTime(long durationMs) {
        approvalProcessingTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startApprovalProcessingTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopApprovalProcessingTimer(Timer.Sample sample) {
        sample.stop(approvalProcessingTimer);
    }

    public void recordDocumentProcessingTime(long durationMs) {
        documentProcessingTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startDocumentProcessingTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopDocumentProcessingTimer(Timer.Sample sample) {
        sample.stop(documentProcessingTimer);
    }

    public void recordAiDecisionTime(long durationMs) {
        aiDecisionTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startAiDecisionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopAiDecisionTimer(Timer.Sample sample) {
        sample.stop(aiDecisionTimer);
    }

    // SLO compliance methods
    public double getAutomationLatencyP95() {
        return automationTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getAutomationLatencyP99() {
        return automationTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getWorkflowExecutionLatencyP95() {
        return workflowExecutionTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getAiDecisionLatencyP95() {
        return aiDecisionTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getErrorRate() {
        long total = (long) automationTotalCounter.count();
        long failures = (long) automationFailureCounter.count();
        return total > 0 ? (double) failures / total : 0.0;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }
}
