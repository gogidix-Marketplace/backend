package com.gogidix.aiservices.aidocumentprocessingservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ValidationResult Domain Model Tests")
class ValidationResultTest {

    @Nested
    @DisplayName("Result Creation Tests")
    class ResultCreationTests {

        @Test
        @DisplayName("Should create success result")
        void shouldCreateSuccessResult() {
            ValidationResult result = ValidationResult.success();

            assertThat(result).isNotNull();
            assertThat(result.isValid()).isTrue();
            assertThat(result.getErrors()).isEmpty();
            assertThat(result.getWarnings()).isEmpty();
        }

        @Test
        @DisplayName("Should create failure result with single error")
        void shouldCreateFailureResultWithSingleError() {
            ValidationResult result = ValidationResult.failure("Invalid document type");

            assertThat(result).isNotNull();
            assertThat(result.isValid()).isFalse();
            assertThat(result.getErrors()).hasSize(1);
            assertThat(result.getErrors().get(0)).isEqualTo("Invalid document type");
        }

        @Test
        @DisplayName("Should create failure result with multiple errors")
        void shouldCreateFailureResultWithMultipleErrors() {
            List<String> errors = Arrays.asList("Error 1", "Error 2", "Error 3");

            ValidationResult result = ValidationResult.failure(errors);

            assertThat(result).isNotNull();
            assertThat(result.isValid()).isFalse();
            assertThat(result.getErrors()).hasSize(3);
            assertThat(result.getErrors()).containsExactlyElementsOf(errors);
        }

        @Test
        @DisplayName("Should create result using builder")
        void shouldCreateResultUsingBuilder() {
            ValidationResult result = ValidationResult.builder()
                    .valid(true)
                    .errors(Collections.emptyList())
                    .warnings(Arrays.asList("Warning 1"))
                    .build();

            assertThat(result.isValid()).isTrue();
            assertThat(result.getWarnings()).hasSize(1);
            assertThat(result.getWarnings().get(0)).isEqualTo("Warning 1");
        }

        @Test
        @DisplayName("Should be valid by default")
        void shouldBeValidByDefault() {
            ValidationResult result = ValidationResult.builder().build();

            assertThat(result.isValid()).isTrue();
        }
    }

    @Nested
    @DisplayName("Result Combination Tests")
    class ResultCombinationTests {

        @Test
        @DisplayName("Should add error to result")
        void shouldAddErrorToResult() {
            ValidationResult original = ValidationResult.success();
            ValidationResult withError = original.withError("New error");

            assertThat(original.isValid()).isTrue();
            assertThat(withError.isValid()).isFalse();
            assertThat(withError.getErrors()).containsExactly("New error");
        }

        @Test
        @DisplayName("Should add warning to result")
        void shouldAddWarningToResult() {
            ValidationResult original = ValidationResult.success();
            ValidationResult withWarning = original.withWarning("New warning");

            assertThat(original.getWarnings()).isEmpty();
            assertThat(withWarning.getWarnings()).containsExactly("New warning");
            assertThat(withWarning.isValid()).isTrue();
        }

        @Test
        @DisplayName("Should chain errors")
        void shouldChainErrors() {
            ValidationResult result = ValidationResult.success()
                    .withError("Error 1")
                    .withError("Error 2");

            assertThat(result.isValid()).isFalse();
            assertThat(result.getErrors()).containsExactly("Error 1", "Error 2");
        }

        @Test
        @DisplayName("Should chain warnings")
        void shouldChainWarnings() {
            ValidationResult result = ValidationResult.success()
                    .withWarning("Warning 1")
                    .withWarning("Warning 2");

            assertThat(result.isValid()).isTrue();
            assertThat(result.getWarnings()).containsExactly("Warning 1", "Warning 2");
        }

        @Test
        @DisplayName("Should chain errors and warnings")
        void shouldChainErrorsAndWarnings() {
            ValidationResult result = ValidationResult.success()
                    .withError("Error 1")
                    .withWarning("Warning 1")
                    .withError("Error 2")
                    .withWarning("Warning 2");

            assertThat(result.isValid()).isFalse();
            assertThat(result.getErrors()).containsExactly("Error 1", "Error 2");
            assertThat(result.getWarnings()).containsExactly("Warning 1", "Warning 2");
        }
    }

    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {

        @Test
        @DisplayName("Should return unmodifiable errors list")
        void shouldReturnUnmodifiableErrorsList() {
            ValidationResult result = ValidationResult.failure("Error 1");

            List<String> errors = result.getErrors();

            assertThatThrownBy(() -> errors.add("Error 2"))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("Should return unmodifiable warnings list")
        void shouldReturnUnmodifiableWarningsList() {
            ValidationResult result = ValidationResult.builder()
                    .warnings(Arrays.asList("Warning 1"))
                    .build();

            List<String> warnings = result.getWarnings();

            assertThatThrownBy(() -> warnings.add("Warning 2"))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("Should not mutate original when adding error")
        void shouldNotMutateOriginalWhenAddingError() {
            ValidationResult original = ValidationResult.success();
            original.withError("Error");

            assertThat(original.isValid()).isTrue();
        }

        @Test
        @DisplayName("Should not mutate original when adding warning")
        void shouldNotMutateOriginalWhenAddingWarning() {
            ValidationResult original = ValidationResult.success();
            original.withWarning("Warning");

            assertThat(original.getWarnings()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Validation Logic Tests")
    class ValidationLogicTests {

        @Test
        @DisplayName("Should be invalid when has errors")
        void shouldBeInvalidWhenHasErrors() {
            ValidationResult result = ValidationResult.builder()
                    .valid(true)
                    .errors(Arrays.asList("Error 1"))
                    .build();

            assertThat(result.isValid()).isFalse();
        }

        @Test
        @DisplayName("Should be valid when only warnings")
        void shouldBeValidWhenOnlyWarnings() {
            ValidationResult result = ValidationResult.builder()
                    .valid(true)
                    .warnings(Arrays.asList("Warning 1", "Warning 2"))
                    .build();

            assertThat(result.isValid()).isTrue();
        }

        @Test
        @DisplayName("Should be invalid when explicitly marked invalid")
        void shouldBeInvalidWhenExplicitlyMarkedInvalid() {
            ValidationResult result = ValidationResult.builder()
                    .valid(false)
                    .errors(Collections.emptyList())
                    .build();

            assertThat(result.isValid()).isFalse();
        }
    }

    @Nested
    @DisplayName("Practical Usage Tests")
    class PracticalUsageTests {

        @Test
        @DisplayName("Should represent validation of invoice")
        void shouldRepresentValidationOfInvoice() {
            ValidationResult result = ValidationResult.builder()
                    .warnings(Arrays.asList(
                            "Field 'tax_amount' has low confidence: 0.65",
                            "Field 'vendor_address' has low confidence: 0.68"
                    ))
                    .build();

            assertThat(result.isValid()).isTrue();
            assertThat(result.getWarnings()).hasSize(2);
        }

        @Test
        @DisplayName("Should represent validation failure")
        void shouldRepresentValidationFailure() {
            ValidationResult result = ValidationResult.builder()
                    .valid(false)
                    .errors(Arrays.asList(
                            "Missing required field: invoice_number",
                            "Missing required field: amount"
                    ))
                    .build();

            assertThat(result.isValid()).isFalse();
            assertThat(result.getErrors()).hasSize(2);
        }

        @Test
        @DisplayName("Should build complex validation result")
        void shouldBuildComplexValidationResult() {
            ValidationResult result = ValidationResult.success()
                    .withWarning("Low confidence on field1")
                    .withWarning("Low confidence on field2")
                    .withError("Missing required field3")
                    .withWarning("Font size irregular");

            assertThat(result.isValid()).isFalse();
            assertThat(result.getErrors()).hasSize(1);
            assertThat(result.getWarnings()).hasSize(3);
        }
    }
}
