package com.gogidix.aiservices.aibusinessautomationservice.infrastructure.metrics;
import org.junit.jupiter.api.*;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import static org.assertj.core.api.Assertions.*;

class BusinessAutomationMetricsTest {
    private BusinessAutomationMetrics m;
    @BeforeEach
    void setup() { m = new BusinessAutomationMetrics(new SimpleMeterRegistry()); }

    @Test void counters() {
        m.incrementAutomationTotal();
        m.incrementAutomationSuccess();
        m.incrementAutomationFailure();
        m.incrementWorkflowsExecuted();
        m.incrementApprovalsProcessed();
        m.incrementDocumentsProcessed();
        m.incrementNotificationsSent();
    }
    @Test void timers() {
        m.recordAutomationTime(100);
        m.recordWorkflowExecutionTime(50);
        m.recordApprovalProcessingTime(30);
        m.recordDocumentProcessingTime(20);
        m.recordAiDecisionTime(10);
    }
    @Test void timerSamples() {
        var s1 = m.startAutomationTimer(); m.stopAutomationTimer(s1);
        var s2 = m.startWorkflowExecutionTimer(); m.stopWorkflowExecutionTimer(s2);
        var s3 = m.startApprovalProcessingTimer(); m.stopApprovalProcessingTimer(s3);
        var s4 = m.startDocumentProcessingTimer(); m.stopDocumentProcessingTimer(s4);
        var s5 = m.startAiDecisionTimer(); m.stopAiDecisionTimer(s5);
    }
    @Test void sloMethods() {
        assertThat(m.getAutomationLatencyP95()).isGreaterThanOrEqualTo(0);
        assertThat(m.getAutomationLatencyP99()).isGreaterThanOrEqualTo(0);
        assertThat(m.getWorkflowExecutionLatencyP95()).isGreaterThanOrEqualTo(0);
        assertThat(m.getAiDecisionLatencyP95()).isGreaterThanOrEqualTo(0);
        assertThat(m.getErrorRate()).isGreaterThanOrEqualTo(0);
    }
    @Test void registry() { assertThat(m.getMeterRegistry()).isNotNull(); }
}
