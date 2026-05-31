package com.gogidix.aiservices.aitestingservice.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TestRun {
    private final String runId;
    private final String suiteId;
    private final String tenantId;
    private final Instant startedAt;
    private Instant completedAt;
    private TestRunStatus status;
    private final List<TestCaseResult> results;

    public enum TestRunStatus { RUNNING, PASSED, FAILED, TIMEOUT }

    public TestRun(String suiteId, String tenantId) {
        this.runId = "run_" + java.util.UUID.randomUUID().toString().substring(0, 8);
        this.suiteId = Objects.requireNonNull(suiteId);
        this.tenantId = Objects.requireNonNull(tenantId);
        this.startedAt = Instant.now();
        this.status = TestRunStatus.RUNNING;
        this.results = new ArrayList<>();
    }

    public void addResult(TestCaseResult result) { this.results.add(result); }
    public void complete(TestRunStatus status) { this.status = status; this.completedAt = Instant.now(); }
    public String getRunId() { return runId; }
    public String getSuiteId() { return suiteId; }
    public String getTenantId() { return tenantId; }
    public Instant getStartedAt() { return startedAt; }
    public Instant getCompletedAt() { return completedAt; }
    public TestRunStatus getStatus() { return status; }
    public List<TestCaseResult> getResults() { return Collections.unmodifiableList(results); }
}
