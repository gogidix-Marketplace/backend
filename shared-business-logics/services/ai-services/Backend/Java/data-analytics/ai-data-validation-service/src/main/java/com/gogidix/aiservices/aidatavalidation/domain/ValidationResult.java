package com.gogidix.aiservices.aidatavalidation.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.NonFinal;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Domain entity representing a data validation result.
 * Contains validation outcomes including errors, warnings, and statistics.
 */
@Getter
public class ValidationResult {

    @NonNull
    private final String validationId;

    @NonFinal
    private boolean valid;

    @NonNull
    private List<ValidationError> errors;

    @NonNull
    private List<ValidationWarning> warnings;

    @NonNull
    private final ValidationStatistics statistics;

    @NonNull
    private final LocalDateTime validatedAt;

    /**
     * Creates a new ValidationResult with validation.
     */
    public ValidationResult(@NonNull String validationId, boolean valid,
                           List<ValidationError> errors, List<ValidationWarning> warnings,
                           @NonNull ValidationStatistics statistics, @NonNull LocalDateTime validatedAt) {
        if (validationId == null || validationId.trim().isEmpty()) {
            throw new IllegalArgumentException("validationId cannot be null or empty");
        }
        if (statistics == null) {
            throw new IllegalArgumentException("statistics cannot be null");
        }
        if (validatedAt == null) {
            throw new IllegalArgumentException("validatedAt cannot be null");
        }

        this.validationId = validationId;
        this.valid = valid;
        this.errors = errors != null ? new ArrayList<>(errors) : new ArrayList<>();
        this.warnings = warnings != null ? new ArrayList<>(warnings) : new ArrayList<>();
        this.statistics = statistics;
        this.validatedAt = validatedAt;
    }

    /**
     * Static builder method for creating ValidationResult instances.
     */
    public static ValidationResultBuilder builder() {
        return new ValidationResultBuilder();
    }

    /**
     * Adds an error to this validation result.
     */
    public void addError(ValidationError error) {
        if (error != null) {
            this.errors.add(error);
            this.valid = false;
        }
    }

    /**
     * Adds a warning to this validation result.
     */
    public void addWarning(ValidationWarning warning) {
        if (warning != null) {
            this.warnings.add(warning);
        }
    }

    /**
     * Returns the total number of issues (errors + warnings).
     */
    public int getTotalIssues() {
        return errors.size() + warnings.size();
    }

    /**
     * Returns true if there are no errors.
     */
    public boolean hasNoErrors() {
        return errors.isEmpty();
    }

    /**
     * Returns true if there are warnings.
     */
    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }

    /**
     * Builder class for ValidationResult.
     */
    public static class ValidationResultBuilder {
        private String validationId;
        private boolean valid;
        private List<ValidationError> errors = new ArrayList<>();
        private List<ValidationWarning> warnings = new ArrayList<>();
        private ValidationStatistics statistics;
        private LocalDateTime validatedAt;

        public ValidationResultBuilder validationId(String validationId) {
            this.validationId = validationId;
            return this;
        }

        public ValidationResultBuilder valid(boolean valid) {
            this.valid = valid;
            return this;
        }

        public ValidationResultBuilder errors(List<ValidationError> errors) {
            this.errors = errors;
            return this;
        }

        public ValidationResultBuilder warnings(List<ValidationWarning> warnings) {
            this.warnings = warnings;
            return this;
        }

        public ValidationResultBuilder statistics(ValidationStatistics statistics) {
            this.statistics = statistics;
            return this;
        }

        public ValidationResultBuilder validatedAt(LocalDateTime validatedAt) {
            this.validatedAt = validatedAt;
            return this;
        }

        public ValidationResult build() {
            return new ValidationResult(validationId, valid, errors, warnings, statistics, validatedAt);
        }
    }
}
