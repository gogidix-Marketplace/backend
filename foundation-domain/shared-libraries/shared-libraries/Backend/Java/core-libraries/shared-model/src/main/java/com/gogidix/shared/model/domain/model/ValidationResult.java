package com.gogidix.shared.model.domain.model;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
    private boolean valid;
    private List<String> errors;

    public ValidationResult(boolean valid) {
        this.valid = valid;
        this.errors = new ArrayList<>();
    }

    public ValidationResult(boolean valid, List<String> errors) {
        this.valid = valid;
        this.errors = errors != null ? new ArrayList<>(errors) : new ArrayList<>();
    }

    public static ValidationResult success() {
        return new ValidationResult(true);
    }

    public static ValidationResult failure(String error) {
        ValidationResult result = new ValidationResult(false);
        result.addError(error);
        return result;
    }

    public static ValidationResult failure(List<String> errors) {
        return new ValidationResult(false, errors);
    }

    public static ValidationResult invalid(List<String> errors) {
        return new ValidationResult(false, errors);
    }

    public static ValidationResult withWarnings(List<String> warnings) {
        ValidationResult result = new ValidationResult(true);
        // Note: This implementation doesn't support warnings in the current structure
        // but provides method compatibility
        return result;
    }

    public static ValidationResult valid() {
        return new ValidationResult(true);
    }

    public boolean isValid() {
        return valid;
    }

    public boolean isInvalid() {
        return !valid;
    }

    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }

    public void addError(String error) {
        if (error != null && !error.trim().isEmpty()) {
            this.errors.add(error);
            this.valid = false;
        }
    }

    public void addErrors(List<String> errors) {
        if (errors != null) {
            for (String error : errors) {
                addError(error);
            }
        }
    }

    public String getErrorsAsString() {
        return String.join(", ", errors);
    }

    public int getErrorCount() {
        return errors.size();
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    @Override
    public String toString() {
        return "ValidationResult{" +
                "valid=" + valid +
                ", errors=" + errors +
                '}';
    }
}