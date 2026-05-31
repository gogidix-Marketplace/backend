package com.gogidix.aiservices.aidatavalidation.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ValidationResult Domain Model Tests")
class ValidationResultTest {

    private static final String VALIDATION_ID = "val-123";
    private static final String DATA_SOURCE = "s3://bucket/data.csv";

    @Nested
    @DisplayName("ValidationResult Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create validation result with valid parameters")
        void shouldCreateWithValidParameters() {
            ValidationResult result = ValidationResult.create(VALIDATION_ID, DATA_SOURCE);

            assertThat(result).isNotNull();
            assertThat(result.getValidationId()).isEqualTo(VALIDATION_ID);
            assertThat(result.getDataSource()).isEqualTo(DATA_SOURCE);
            assertThat(result.isValid()).isTrue();
            assertThat(result.getErrors()).isEmpty();
            assertThat(result.getWarnings()).isEmpty();
            assertThat(result.getValidatedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should reject null validation ID")
        void shouldRejectNullValidationId() {
            assertThatThrownBy(() -> ValidationResult.create(null, DATA_SOURCE))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Validation ID cannot be null");
        }

        @Test
        @DisplayName("Should reject null data source")
        void shouldRejectNullDataSource() {
            assertThatThrownBy(() -> ValidationResult.create(VALIDATION_ID, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Data source cannot be null");
        }

        @Test
        @DisplayName("Should reject empty data source")
        void shouldRejectEmptyDataSource() {
            assertThatThrownBy(() -> ValidationResult.create(VALIDATION_ID, ""))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Data source cannot be empty");
        }
    }

    @Nested
    @DisplayName("Error Management Tests")
    class ErrorManagementTests {

        @Test
        @DisplayName("Should add error successfully")
        void shouldAddError() {
            ValidationResult result = ValidationResult.create(VALIDATION_ID, DATA_SOURCE);

            result.addError("Missing required field: email");

            assertThat(result.isValid()).isFalse();
            assertThat(result.getErrors()).hasSize(1);
            assertThat(result.getErrors().get(0)).isEqualTo("Missing required field: email");
        }

        @Test
        @DisplayName("Should add multiple errors")
        void shouldAddMultipleErrors() {
            ValidationResult result = ValidationResult.create(VALIDATION_ID, DATA_SOURCE);

            result.addError("Error 1");
            result.addError("Error 2");
            result.addError("Error 3");

            assertThat(result.getErrors()).hasSize(3);
        }

        @Test
        @DisplayName("Should reject null error message")
        void shouldRejectNullError() {
            ValidationResult result = ValidationResult.create(VALIDATION_ID, DATA_SOURCE);

            assertThatThrownBy(() -> result.addError(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Error message cannot be null");
        }

        @Test
        @DisplayName("Should reject empty error message")
        void shouldRejectEmptyError() {
            ValidationResult result = ValidationResult.create(VALIDATION_ID, DATA_SOURCE);

            assertThatThrownBy(() -> result.addError(""))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Error message cannot be empty");
        }

        @Test
        @DisplayName("Should enforce maximum errors limit")
        void shouldEnforceMaxErrors() {
            ValidationResult result = ValidationResult.create(VALIDATION_ID, DATA_SOURCE);

            for (int i = 0; i < 1000; i++) {
                result.addError("Error " + i);
            }

            assertThatThrownBy(() -> result.addError("Error 1001"))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("maximum");
        }
    }

    @Nested
    @DisplayName("Warning Management Tests")
    class WarningManagementTests {

        @Test
        @DisplayName("Should add warning successfully")
        void shouldAddWarning() {
            ValidationResult result = ValidationResult.create(VALIDATION_ID, DATA_SOURCE);

            result.addWarning("Duplicate values detected");

            assertThat(result.getWarnings()).hasSize(1);
            assertThat(result.getWarnings().get(0)).isEqualTo("Duplicate values detected");
        }

        @Test
        @DisplayName("Should add multiple warnings")
        void shouldAddMultipleWarnings() {
            ValidationResult result = ValidationResult.create(VALIDATION_ID, DATA_SOURCE);

            result.addWarning("Warning 1");
            result.addWarning("Warning 2");

            assertThat(result.getWarnings()).hasSize(2);
        }

        @Test
        @DisplayName("Should reject null warning message")
        void shouldRejectNullWarning() {
            ValidationResult result = ValidationResult.create(VALIDATION_ID, DATA_SOURCE);

            assertThatThrownBy(() -> result.addWarning(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Warning message cannot be null");
        }
    }

    @Nested
    @DisplayName("Statistics Tests")
    class StatisticsTests {

        @Test
        @DisplayName("Should set statistics")
        void shouldSetStatistics() {
            ValidationResult result = ValidationResult.create(VALIDATION_ID, DATA_SOURCE);
            Map<String, Object> stats = Map.of(
                    "totalRows", 1000,
                    "validRows", 950,
                    "invalidRows", 50
            );

            result.setStatistics(stats);

            assertThat(result.getStatistics()).isEqualTo(stats);
        }

        @Test
        @DisplayName("Should reject null statistics")
        void shouldRejectNullStatistics() {
            ValidationResult result = ValidationResult.create(VALIDATION_ID, DATA_SOURCE);

            assertThatThrownBy(() -> result.setStatistics(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Statistics cannot be null");
        }
    }

    @Nested
    @DisplayName("Status Tests")
    class StatusTests {

        @Test
        @DisplayName("Should be valid when no errors")
        void shouldBeValidWhenNoErrors() {
            ValidationResult result = ValidationResult.create(VALIDATION_ID, DATA_SOURCE);
            result.addWarning("Some warning");

            assertThat(result.isValid()).isTrue();
        }

        @Test
        @DisplayName("Should be invalid when errors exist")
        void shouldBeInvalidWhenErrorsExist() {
            ValidationResult result = ValidationResult.create(VALIDATION_ID, DATA_SOURCE);
            result.addError("Some error");

            assertThat(result.isValid()).isFalse();
        }

        @Test
        @DisplayName("Should track completion status")
        void shouldTrackCompletionStatus() {
            ValidationResult result = ValidationResult.create(VALIDATION_ID, DATA_SOURCE);

            assertThat(result.isCompleted()).isFalse();

            result.markCompleted();

            assertThat(result.isCompleted()).isTrue();
        }
    }

    @Nested
    @DisplayName("Restore Tests")
    class RestoreTests {

        @Test
        @DisplayName("Should restore existing validation result")
        void shouldRestoreExistingResult() {
            List<String> errors = List.of("Error 1", "Error 2");
            List<String> warnings = List.of("Warning 1");
            Map<String, Object> stats = Map.of("totalRows", 100);
            Instant validatedAt = Instant.now().minusSeconds(60);

            ValidationResult result = ValidationResult.restore(
                    VALIDATION_ID,
                    DATA_SOURCE,
                    false,
                    errors,
                    warnings,
                    stats,
                    validatedAt,
                    true
            );

            assertThat(result.getValidationId()).isEqualTo(VALIDATION_ID);
            assertThat(result.getDataSource()).isEqualTo(DATA_SOURCE);
            assertThat(result.isValid()).isFalse();
            assertThat(result.getErrors()).isEqualTo(errors);
            assertThat(result.getWarnings()).isEqualTo(warnings);
            assertThat(result.getStatistics()).isEqualTo(stats);
            assertThat(result.isCompleted()).isTrue();
        }
    }
}
