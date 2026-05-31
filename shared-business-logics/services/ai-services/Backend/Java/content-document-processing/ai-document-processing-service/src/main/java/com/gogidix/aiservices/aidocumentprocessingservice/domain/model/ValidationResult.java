package com.gogidix.aiservices.aidocumentprocessingservice.domain.model;

import lombok.Builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Builder
public class ValidationResult {
    @Builder.Default
    private final boolean valid = true;
    @Builder.Default
    private final List<String> errors = new ArrayList<>();
    @Builder.Default
    private final List<String> warnings = new ArrayList<>();

    public boolean isValid() {
        return valid && errors.isEmpty();
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }

    public List<String> getWarnings() {
        return Collections.unmodifiableList(warnings);
    }

    public static ValidationResult success() {
        return ValidationResult.builder().build();
    }

    public static ValidationResult failure(String error) {
        return ValidationResult.builder()
                .valid(false)
                .errors(List.of(error))
                .build();
    }

    public static ValidationResult failure(List<String> errors) {
        return ValidationResult.builder()
                .valid(false)
                .errors(errors)
                .build();
    }

    public ValidationResult withError(String error) {
        List<String> newErrors = new ArrayList<>(this.errors);
        newErrors.add(error);
        return ValidationResult.builder()
                .valid(false)
                .errors(newErrors)
                .warnings(this.warnings)
                .build();
    }

    public ValidationResult withWarning(String warning) {
        List<String> newWarnings = new ArrayList<>(this.warnings);
        newWarnings.add(warning);
        return ValidationResult.builder()
                .valid(this.valid)
                .errors(this.errors)
                .warnings(newWarnings)
                .build();
    }
}
