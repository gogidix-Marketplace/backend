package com.gogidix.aiservices.aidatavalidation.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@DisplayName("ValidationResult Domain Entity Tests")
class ValidationResultTest {

    @Test
    @DisplayName("Should create valid validation result with all required fields")
    void shouldCreateValidationResult() {
        // Given
        String validationId = "val-123";
        boolean isValid = true;
        List<ValidationError> errors = List.of();
        List<ValidationWarning> warnings = List.of();
        ValidationStatistics statistics = new ValidationStatistics(100, 100, 0, 0);

        // When
        ValidationResult result = new ValidationResult(
            validationId,
            isValid,
            errors,
            warnings,
            statistics,
            LocalDateTime.now()
        );

        // Then
        assertThat(result.getValidationId()).isEqualTo(validationId);
        assertThat(result.isValid()).isTrue();
        assertThat(result.getErrors()).isEmpty();
        assertThat(result.getWarnings()).isEmpty();
        assertThat(result.getStatistics()).isNotNull();
    }

    @Test
    @DisplayName("Should mark validation as invalid when errors are present")
    void shouldMarkInvalidWhenErrorsPresent() {
        // Given
        List<ValidationError> errors = List.of(
            new ValidationError("field1", "REQUIRED", "Field is required"),
            new ValidationError("field2", "FORMAT", "Invalid format")
        );

        // When
        ValidationResult result = new ValidationResult(
            "val-456",
            false,
            errors,
            List.of(),
            new ValidationStatistics(50, 40, 5, 5),
            LocalDateTime.now()
        );

        // Then
        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrors()).hasSize(2);
    }

    @Test
    @DisplayName("Should calculate statistics correctly")
    void shouldCalculateStatistics() {
        // Given
        ValidationStatistics statistics = new ValidationStatistics(100, 85, 10, 5);

        // Then
        assertThat(statistics.getTotalRecords()).isEqualTo(100);
        assertThat(statistics.getValidRecords()).isEqualTo(85);
        assertThat(statistics.getInvalidRecords()).isEqualTo(10);
        assertThat(statistics.getSkippedRecords()).isEqualTo(5);
        assertThat(statistics.getValidityRate()).isEqualTo(0.85);
    }

    @Test
    @DisplayName("Should throw exception when validation ID is null")
    void shouldThrowExceptionWhenValidationIdIsNull() {
        // Then
        assertThatThrownBy(() -> new ValidationResult(
            null,
            true,
            List.of(),
            List.of(),
            new ValidationStatistics(0, 0, 0, 0),
            LocalDateTime.now()
        )).isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("validationId");
    }

    @Test
    @DisplayName("Should add error to validation result")
    void shouldAddErrorToResult() {
        // Given
        ValidationResult result = new ValidationResult(
            "val-789",
            true,
            List.of(),
            List.of(),
            new ValidationStatistics(10, 10, 0, 0),
            LocalDateTime.now()
        );
        ValidationError error = new ValidationError("email", "FORMAT", "Invalid email format");

        // When
        result.addError(error);

        // Then
        assertThat(result.getErrors()).hasSize(1);
        assertThat(result.isValid()).isFalse();
    }

    @Test
    @DisplayName("Should add warning to validation result")
    void shouldAddWarningToResult() {
        // Given
        ValidationResult result = new ValidationResult(
            "val-101",
            true,
            List.of(),
            List.of(),
            new ValidationStatistics(10, 10, 0, 0),
            LocalDateTime.now()
        );
        ValidationWarning warning = new ValidationWarning("age", "OUT_OF_RANGE", "Value exceeds expected range");

        // When
        result.addWarning(warning);

        // Then
        assertThat(result.getWarnings()).hasSize(1);
        assertThat(result.isValid()).isTrue(); // Warnings don't invalidate
    }
}
