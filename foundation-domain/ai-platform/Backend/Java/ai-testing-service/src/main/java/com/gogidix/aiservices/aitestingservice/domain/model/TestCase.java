package com.gogidix.aiservices.aitestingservice.domain.model;

import java.util.Map;
import java.util.Objects;

public class TestCase {
    private final String caseId;
    private final String name;
    private final String testType;
    private final Map<String, Object> parameters;
    private TestStatus status;
    private String result;
    private long executionTimeMs;

    public TestCase(String caseId, String name, String testType, Map<String, Object> parameters) {
        this.caseId = Objects.requireNonNull(caseId);
        this.name = Objects.requireNonNull(name);
        this.testType = Objects.requireNonNull(testType);
        this.parameters = parameters;
        this.status = TestStatus.PENDING;
    }

    public String getCaseId() { return caseId; }
    public String getName() { return name; }
    public String getTestType() { return testType; }
    public Map<String, Object> getParameters() { return parameters; }
    public TestStatus getStatus() { return status; }
    public String getResult() { return result; }
    public long getExecutionTimeMs() { return executionTimeMs; }

    public void pass(String result) { this.status = TestStatus.PASSED; this.result = result; }
    public void fail(String result) { this.status = TestStatus.FAILED; this.result = result; }
    public void setExecutionTimeMs(long executionTimeMs) { this.executionTimeMs = executionTimeMs; }
}
