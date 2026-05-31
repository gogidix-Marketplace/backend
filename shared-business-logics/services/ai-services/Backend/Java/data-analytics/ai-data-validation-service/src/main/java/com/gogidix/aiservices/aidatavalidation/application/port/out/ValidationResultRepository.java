package com.gogidix.aiservices.aidatavalidation.application.port.out;

import com.gogidix.aiservices.aidatavalidation.domain.ValidationResult;

import java.util.Optional;

/**
 * Repository port for persisting validation results.
 */
public interface ValidationResultRepository {

    /**
     * Saves a validation result.
     */
    ValidationResult save(ValidationResult result);

    /**
     * Finds a validation result by ID.
     */
    Optional<ValidationResult> findById(String validationId);

    /**
     * Deletes a validation result by ID.
     */
    void deleteById(String validationId);
}
