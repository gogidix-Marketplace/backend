package com.gogidix.aiservices.aidatavalidation.domain.aggregate;

import com.gogidix.aiservices.aidatavalidation.domain.model.ValidationResult;
import com.gogidix.aiservices.aidatavalidation.domain.model.ValidationRule;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ValidationExecution {
    private static final int MAX_RULES = 50;
    private static final int MIN_TIMEOUT = 60;
    private static final int MAX_TIMEOUT = 3600;

    private String executionId;
    private final String dataSource;
    private final String schema;
    private Status status;
    private Instant createdAt;
    private Instant startedAt;
    private Instant completedAt;
    private final List<ValidationRule> rules;
    private int timeout;
    private int progress;
    private ValidationResult result;
    private String errorMessage;

    public enum Status {
        PENDING, RUNNING, COMPLETED, FAILED
    }

    private ValidationExecution(String dataSource, String schema) {
        if (dataSource == null || dataSource.trim().isEmpty()) {
            throw new IllegalArgumentException("Data source cannot be null or empty");
        }
        if (schema == null) {
            throw new IllegalArgumentException("Schema cannot be null");
        }

        this.executionId = UUID.randomUUID().toString();
        this.dataSource = dataSource;
        this.schema = schema;
        this.status = Status.PENDING;
        this.createdAt = Instant.now();
        this.rules = new ArrayList<>();
        this.timeout = 3600;
        this.progress = 0;
    }

    public static ValidationExecution create(String dataSource, String schema) {
        return new ValidationExecution(dataSource, schema);
    }

    public static ValidationExecution restore(String executionId, String dataSource, String schema,
                                             Status status, Instant createdAt, Instant startedAt,
                                             Instant completedAt, List<ValidationRule> rules,
                                             int timeout, int progress, ValidationResult result,
                                             String errorMessage) {
        ValidationExecution execution = new ValidationExecution(dataSource, schema);
        execution.executionId = executionId;
        execution.status = status;
        execution.createdAt = createdAt;
        execution.startedAt = startedAt;
        execution.completedAt = completedAt;
        execution.rules.addAll(rules);
        execution.timeout = timeout;
        execution.progress = progress;
        execution.result = result;
        execution.errorMessage = errorMessage;
        return execution;
    }

    public void addRule(ValidationRule rule) {
        if (rule == null) {
            throw new IllegalArgumentException("Rule cannot be null");
        }
        if (rules.size() >= MAX_RULES) {
            throw new IllegalStateException("Cannot add more than " + MAX_RULES + " rules");
        }
        rules.add(rule);
    }

    public void removeRule(String ruleId) {
        rules.removeIf(r -> r.getRuleId().equals(ruleId));
    }

    public void start() {
        if (status != Status.PENDING) {
            throw new IllegalStateException("Can only start pending validation");
        }
        this.status = Status.RUNNING;
        this.startedAt = Instant.now();
    }

    public void complete(ValidationResult result) {
        if (result == null) {
            throw new IllegalArgumentException("Result cannot be null");
        }
        if (status != Status.RUNNING) {
            throw new IllegalStateException("Can only complete running validation");
        }
        this.status = Status.COMPLETED;
        this.completedAt = Instant.now();
        this.result = result;
    }

    public void fail(String errorMessage) {
        if (errorMessage == null || errorMessage.trim().isEmpty()) {
            throw new IllegalArgumentException("Error message cannot be empty");
        }
        this.status = Status.FAILED;
        this.completedAt = Instant.now();
        this.errorMessage = errorMessage;
    }

    public void setTimeout(int timeout) {
        if (timeout < MIN_TIMEOUT) {
            throw new IllegalArgumentException("Timeout must be at least " + MIN_TIMEOUT + " seconds");
        }
        if (timeout > MAX_TIMEOUT) {
            throw new IllegalArgumentException("Timeout cannot exceed " + MAX_TIMEOUT + " seconds");
        }
        this.timeout = timeout;
    }

    public boolean isTimeoutExceeded() {
        if (startedAt == null) {
            return false;
        }
        return Instant.now().isAfter(startedAt.plusSeconds(timeout));
    }

    public void setProgress(int progress) {
        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("Progress must be between 0 and 100");
        }
        this.progress = progress;
    }

    public String getExecutionId() { return executionId; }
    public String getDataSource() { return dataSource; }
    public String getSchema() { return schema; }
    public Status getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getStartedAt() { return startedAt; }
    public Instant getCompletedAt() { return completedAt; }
    public List<ValidationRule> getRules() { return rules; }
    public int getTimeout() { return timeout; }
    public int getProgress() { return progress; }
    public ValidationResult getResult() { return result; }
    public String getErrorMessage() { return errorMessage; }
}
