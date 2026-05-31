package com.gogidix.aiservices.aidataprocessing.domain.model;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
    private final boolean valid;
    private final List<ValidationError> errors;
    private final List<ValidationWarning> warnings;

    private ValidationResult(boolean valid, List<ValidationError> errors, List<ValidationWarning> warnings) {
        this.valid = valid;
        this.errors = errors != null ? errors : new ArrayList<>();
        this.warnings = warnings != null ? warnings : new ArrayList<>();
    }

    public static ValidationResult valid() {
        return new ValidationResult(true, List.of(), List.of());
    }

    public static ValidationResult invalid(List<ValidationError> errors) {
        return new ValidationResult(false, errors, List.of());
    }

    public static ValidationResult withWarnings(List<ValidationWarning> warnings) {
        return new ValidationResult(true, List.of(), warnings);
    }

    public boolean isValid() { return valid; }
    public List<ValidationError> getErrors() { return errors; }
    public List<ValidationWarning> getWarnings() { return warnings; }

    public record ValidationError(String field, String message, Object invalidValue) {}
    public record ValidationWarning(String field, String message) {}
}
