package com.gogidix.aiservices.aidatavalidation.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationResult {
    private static final int MAX_ERRORS = 1000;

    private final String validationId;
    private final String dataSource;
    private boolean valid;
    private final List<String> errors;
    private final List<String> warnings;
    private Map<String, Object> statistics;
    private Instant validatedAt;
    private boolean completed;

    private ValidationResult(String validationId, String dataSource) {
        if (validationId == null) {
            throw new IllegalArgumentException("Validation ID cannot be null");
        }
        if (dataSource == null || dataSource.trim().isEmpty()) {
            throw new IllegalArgumentException("Data source cannot be null or empty");
        }

        this.validationId = validationId;
        this.dataSource = dataSource;
        this.valid = true;
        this.errors = new ArrayList<>();
        this.warnings = new ArrayList<>();
        this.statistics = new HashMap<>();
        this.validatedAt = Instant.now();
        this.completed = false;
    }

    public static ValidationResult create(String validationId, String dataSource) {
        return new ValidationResult(validationId, dataSource);
    }

    public static ValidationResult restore(String validationId, String dataSource, boolean valid,
                                          List<String> errors, List<String> warnings,
                                          Map<String, Object> statistics, Instant validatedAt,
                                          boolean completed) {
        ValidationResult result = new ValidationResult(validationId, dataSource);
        result.valid = valid;
        result.errors.addAll(errors);
        result.warnings.addAll(warnings);
        result.statistics = statistics;
        result.validatedAt = validatedAt;
        result.completed = completed;
        return result;
    }

    public void addError(String errorMessage) {
        if (errorMessage == null) {
            throw new IllegalArgumentException("Error message cannot be null");
        }
        if (errorMessage.trim().isEmpty()) {
            throw new IllegalArgumentException("Error message cannot be empty");
        }
        if (errors.size() >= MAX_ERRORS) {
            throw new IllegalStateException("Cannot add more than " + MAX_ERRORS + " errors");
        }
        errors.add(errorMessage);
    }

    public void addWarning(String warningMessage) {
        if (warningMessage == null) {
            throw new IllegalArgumentException("Warning message cannot be null");
        }
        warnings.add(warningMessage);
    }

    public void setStatistics(Map<String, Object> statistics) {
        if (statistics == null) {
            throw new IllegalArgumentException("Statistics cannot be null");
        }
        this.statistics = statistics;
    }

    public void markCompleted() {
        this.completed = true;
    }

    public String getValidationId() { return validationId; }
    public String getDataSource() { return dataSource; }
    public boolean isValid() { return valid && errors.isEmpty(); }
    public List<String> getErrors() { return errors; }
    public List<String> getWarnings() { return warnings; }
    public Map<String, Object> getStatistics() { return statistics; }
    public Instant getValidatedAt() { return validatedAt; }
    public boolean isCompleted() { return completed; }
}
