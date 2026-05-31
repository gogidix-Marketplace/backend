package com.gogidix.aiservices.aitestingservice.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Document(collection = "test_suites")
@CompoundIndex(name = "idx_suite_tenant", def = "{'tenantId': 1, 'suiteId': 1}")
public class TestSuite {
    @Id private String id;
    @Indexed private String suiteId;
    @Indexed private String tenantId;
    private String name;
    private String description;
    private List<TestCase> testCases;
    private TestStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant lastExecutedAt;
    private Integer passRate;

    private TestSuite() { this.testCases = new ArrayList<>(); }

    public TestSuite(String tenantId, String name) {
        this();
        this.id = java.util.UUID.randomUUID().toString();
        this.suiteId = "suite_" + java.util.UUID.randomUUID().toString().substring(0, 8);
        this.tenantId = Objects.requireNonNull(tenantId);
        this.name = Objects.requireNonNull(name);
        this.status = TestStatus.PENDING;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void addTestCase(TestCase testCase) {
        Objects.requireNonNull(testCase);
        if (!this.testCases.contains(testCase)) this.testCases.add(testCase);
        this.updatedAt = Instant.now();
    }

    public void startExecution() { this.status = TestStatus.RUNNING; this.updatedAt = Instant.now(); }
    public void complete() { this.status = TestStatus.PASSED; this.lastExecutedAt = Instant.now(); this.updatedAt = Instant.now(); }
    public void fail() { this.status = TestStatus.FAILED; this.lastExecutedAt = Instant.now(); this.updatedAt = Instant.now(); }

    public String getId() { return id; }
    public String getSuiteId() { return suiteId; }
    public String getTenantId() { return tenantId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public List<TestCase> getTestCases() { return Collections.unmodifiableList(testCases); }
    public TestStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Instant getLastExecutedAt() { return lastExecutedAt; }
    public Integer getPassRate() { return passRate; }

    protected void setId(String id) { this.id = id; }
    protected void setSuiteId(String suiteId) { this.suiteId = suiteId; }
    protected void setTenantId(String tenantId) { this.tenantId = tenantId; }
    protected void setName(String name) { this.name = name; }
    protected void setDescription(String description) { this.description = description; }
    protected void setTestCases(List<TestCase> testCases) { this.testCases = testCases; }
    protected void setStatus(TestStatus status) { this.status = status; }
    protected void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    protected void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    protected void setLastExecutedAt(Instant lastExecutedAt) { this.lastExecutedAt = lastExecutedAt; }
    protected void setPassRate(Integer passRate) { this.passRate = passRate; }
}
