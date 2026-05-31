package com.gogidix.aiservices.aitestingservice.infrastructure.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Micrometer metrics for AI Testing Service.
 * Tracks key performance indicators for SLO monitoring.
 */
@Component
public class AiTestingMetrics {

    private final MeterRegistry meterRegistry;

    // Counters
    private final Counter requestsTotalCounter;
    private final Counter requestsSuccessCounter;
    private final Counter requestsFailureCounter;
    private final Counter testsExecutedCounter;
    private final Counter testsPassedCounter;
    private final Counter testsFailedCounter;
    private final Counter testSuitesExecutedCounter;

    // Timers
    private final Timer testExecutionLatencyTimer;
    private final Timer testSuiteExecutionTimer;
    private final Timer testAnalysisTimer;

    public AiTestingMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;

        // Initialize counters
        this.requestsTotalCounter = Counter.builder("ai.testing.requests.total")
                .description("Total number of testing service requests")
                .tag("service", "ai-testing")
                .register(meterRegistry);

        this.requestsSuccessCounter = Counter.builder("ai.testing.requests.success")
                .description("Number of successful testing service requests")
                .tag("service", "ai-testing")
                .register(meterRegistry);

        this.requestsFailureCounter = Counter.builder("ai.testing.requests.failure")
                .description("Number of failed testing service requests")
                .tag("service", "ai-testing")
                .register(meterRegistry);

        this.testsExecutedCounter = Counter.builder("ai.testing.tests.executed")
                .description("Number of tests executed")
                .tag("service", "ai-testing")
                .register(meterRegistry);

        this.testsPassedCounter = Counter.builder("ai.testing.tests.passed")
                .description("Number of tests passed")
                .tag("service", "ai-testing")
                .register(meterRegistry);

        this.testsFailedCounter = Counter.builder("ai.testing.tests.failed")
                .description("Number of tests failed")
                .tag("service", "ai-testing")
                .register(meterRegistry);

        this.testSuitesExecutedCounter = Counter.builder("ai.testing.testsuites.executed")
                .description("Number of test suites executed")
                .tag("service", "ai-testing")
                .register(meterRegistry);

        // Initialize timers
        this.testExecutionLatencyTimer = Timer.builder("ai.testing.test.execution.duration")
                .description("Test execution processing time")
                .tag("service", "ai-testing")
                .publishPercentiles(0.5, 0.95, 0.99)
                .publishPercentileHistogram()
                .register(meterRegistry);

        this.testSuiteExecutionTimer = Timer.builder("ai.testing.testsuite.execution.duration")
                .description("Test suite execution time")
                .tag("service", "ai-testing")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);

        this.testAnalysisTimer = Timer.builder("ai.testing.analysis.duration")
                .description("Test result analysis time")
                .tag("service", "ai-testing")
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

    public void incrementTestsExecuted() {
        testsExecutedCounter.increment();
    }

    public void incrementTestsPassed() {
        testsPassedCounter.increment();
    }

    public void incrementTestsFailed() {
        testsFailedCounter.increment();
    }

    public void incrementTestSuitesExecuted() {
        testSuitesExecutedCounter.increment();
    }

    // Timer methods
    public void recordTestExecutionTime(long durationMs) {
        testExecutionLatencyTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }

    public Timer.Sample startTestExecutionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopTestExecutionTimer(Timer.Sample sample) {
        sample.stop(testExecutionLatencyTimer);
    }

    public Timer.Sample startTestSuiteExecutionTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopTestSuiteExecutionTimer(Timer.Sample sample) {
        sample.stop(testSuiteExecutionTimer);
    }

    public Timer.Sample startTestAnalysisTimer() {
        return Timer.start(meterRegistry);
    }

    public void stopTestAnalysisTimer(Timer.Sample sample) {
        sample.stop(testAnalysisTimer);
    }

    // SLO compliance methods
    public double getTestExecutionLatencyP95() {
        return testExecutionLatencyTimer.percentile(0.95, TimeUnit.MILLISECONDS);
    }

    public double getTestExecutionLatencyP99() {
        return testExecutionLatencyTimer.percentile(0.99, TimeUnit.MILLISECONDS);
    }

    public double getTestSuiteExecutionLatencyP95() {
        return testSuiteExecutionTimer.percentile(0.95, TimeUnit.MILLISECONDS);
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
