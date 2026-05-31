package com.gogidix.aiservices.aidatavalidation.application.port.in;

import com.gogidix.aiservices.aidatavalidation.domain.ValidationResult;

import java.util.List;
import java.util.Optional;

/**
 * Use case interface for dataset validation.
 */
public interface ValidateDatasetUseCase {

    /**
     * Validates a dataset according to schema and rules.
     */
    ValidationResult validateDataset(ValidateDatasetCommand command);

    /**
     * Retrieves a validation result by ID.
     */
    Optional<ValidationResult> getValidationResult(String validationId);

    /**
     * Applies custom validation rules.
     */
    boolean applyCustomRules(List<String> customRules);
}
