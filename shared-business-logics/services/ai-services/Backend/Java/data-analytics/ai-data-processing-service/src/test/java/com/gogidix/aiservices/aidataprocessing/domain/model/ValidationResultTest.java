package com.gogidix.aiservices.aidataprocessing.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ValidationResult Domain Model Tests")
class ValidationResultTest {

    @Nested
    @DisplayName("Factory Methods Tests")
    class FactoryMethodsTests {

        @Test
        @DisplayName("Should create valid result")
        void shouldCreateValidResult() {
            ValidationResult result = ValidationResult.valid();

            assertThat(result.isValid()).isTrue();
            assertThat(result.getErrors()).isEmpty();
            assertThat(result.getWarnings()).isEmpty();
        }

        @Test
        @DisplayName("Should create invalid result with errors")
        void shouldCreateInvalidResultWithErrors() {
            List<ValidationResult.ValidationError> errors = List.of(
                    new ValidationResult.ValidationError("email", "Invalid format", "test@"),
                    new ValidationResult.ValidationError("age", "Must be positive", -5)
            );

            ValidationResult result = ValidationResult.invalid(errors);

            assertThat(result.isValid()).isFalse();
            assertThat(result.getErrors()).hasSize(2);
            assertThat(result.getWarnings()).isEmpty();
        }

        @Test
        @DisplayName("Should create result with warnings")
        void shouldCreateResultWithWarnings() {
            List<ValidationResult.ValidationWarning> warnings = List.of(
                    new ValidationResult.ValidationWarning("name", "Field is unusually short"),
                    new ValidationResult.ValidationWarning("phone", "Format may be invalid")
            );

            ValidationResult result = ValidationResult.withWarnings(warnings);

            assertThat(result.isValid()).isTrue();
            assertThat(result.getErrors()).isEmpty();
            assertThat(result.getWarnings()).hasSize(2);
        }

        @Test
        @DisplayName("Should handle empty errors list")
        void shouldHandleEmptyErrorsList() {
            ValidationResult result = ValidationResult.invalid(List.of());

            assertThat(result.isValid()).isFalse();
            assertThat(result.getErrors()).isEmpty();
        }

        @Test
        @DisplayName("Should handle empty warnings list")
        void shouldHandleEmptyWarningsList() {
            ValidationResult result = ValidationResult.withWarnings(List.of());

            assertThat(result.isValid()).isTrue();
            assertThat(result.getWarnings()).isEmpty();
        }
    }

    @Nested
    @DisplayName("ValidationError Tests")
    class ValidationErrorTests {

        @Test
        @DisplayName("Should create validation error with all fields")
        void shouldCreateValidationErrorWithAllFields() {
            ValidationResult.ValidationError error = new ValidationResult.ValidationError(
                    "email",
                    "Invalid email format",
                    "not-an-email"
            );

            assertThat(error.field()).isEqualTo("email");
            assertThat(error.message()).isEqualTo("Invalid email format");
            assertThat(error.invalidValue()).isEqualTo("not-an-email");
        }

        @Test
        @DisplayName("Should create error with null invalid value")
        void shouldCreateErrorWithNullInvalidValue() {
            ValidationResult.ValidationError error = new ValidationResult.ValidationError(
                    "requiredField",
                    "Field is required",
                    null
            );

            assertThat(error.invalidValue()).isNull();
        }

        @Test
        @DisplayName("Should create error with numeric invalid value")
        void shouldCreateErrorWithNumericInvalidValue() {
            ValidationResult.ValidationError error = new ValidationResult.ValidationError(
                    "age",
                    "Must be between 0 and 120",
                    150
            );

            assertThat(error.invalidValue()).isEqualTo(150);
        }

        @Test
        @DisplayName("Should create error with string invalid value")
        void shouldCreateErrorWithStringInvalidValue() {
            ValidationResult.ValidationError error = new ValidationResult.ValidationError(
                    "name",
                    "Contains special characters",
                    "John@#$"
            );

            assertThat(error.invalidValue()).isEqualTo("John@#$");
        }

        @Test
        @DisplayName("Should handle empty field name")
        void shouldHandleEmptyFieldName() {
            ValidationResult.ValidationError error = new ValidationResult.ValidationError(
                    "",
                    "Generic error",
                    null
            );

            assertThat(error.field()).isEmpty();
        }

        @Test
        @DisplayName("Should handle unicode in field name")
        void shouldHandleUnicodeInFieldName() {
            ValidationResult.ValidationError error = new ValidationResult.ValidationError(
                    "电子邮件",
                    "格式无效",
                    "invalid"
            );

            assertThat(error.field()).isEqualTo("电子邮件");
        }

        @Test
        @DisplayName("Should handle very long error message")
        void shouldHandleLongErrorMessage() {
            String longMessage = "Error: " + "detail".repeat(100);

            ValidationResult.ValidationError error = new ValidationResult.ValidationError(
                    "field",
                    longMessage,
                    null
            );

            assertThat(error.message()).hasSizeGreaterThan(500);
        }
    }

    @Nested
    @DisplayName("ValidationWarning Tests")
    class ValidationWarningTests {

        @Test
        @DisplayName("Should create validation warning with all fields")
        void shouldCreateValidationWarningWithAllFields() {
            ValidationResult.ValidationWarning warning = new ValidationResult.ValidationWarning(
                    "email",
                    "Email format may be incorrect"
            );

            assertThat(warning.field()).isEqualTo("email");
            assertThat(warning.message()).isEqualTo("Email format may be incorrect");
        }

        @Test
        @DisplayName("Should create warning with minimal fields")
        void shouldCreateWarningWithMinimalFields() {
            ValidationResult.ValidationWarning warning = new ValidationResult.ValidationWarning(
                    "field",
                    "Warning message"
            );

            assertThat(warning.field()).isEqualTo("field");
            assertThat(warning.message()).isEqualTo("Warning message");
        }

        @Test
        @DisplayName("Should handle empty field name")
        void shouldHandleEmptyFieldName() {
            ValidationResult.ValidationWarning warning = new ValidationResult.ValidationWarning(
                    "",
                    "Generic warning"
            );

            assertThat(warning.field()).isEmpty();
        }

        @Test
        @DisplayName("Should handle unicode in warning")
            void shouldHandleUnicodeInWarning() {
            ValidationResult.ValidationWarning warning = new ValidationResult.ValidationWarning(
                    "姓名",
                    "字段过短"
            );

            assertThat(warning.field()).isEqualTo("姓名");
            assertThat(warning.message()).isEqualTo("字段过短");
        }

        @Test
        @DisplayName("Should handle special characters in message")
        void shouldHandleSpecialCharactersInMessage() {
            ValidationResult.ValidationWarning warning = new ValidationResult.ValidationWarning(
                    "password",
                    "Contains special chars: !@#$%^&*()"
            );

            assertThat(warning.message()).contains("!@#$%^&*()");
        }
    }

    @Nested
    @DisplayName("Error Collection Tests")
    class ErrorCollectionTests {

        @Test
        @DisplayName("Should store multiple errors")
        void shouldStoreMultipleErrors() {
            List<ValidationResult.ValidationError> errors = List.of(
                    new ValidationResult.ValidationError("field1", "Error 1", null),
                    new ValidationResult.ValidationError("field2", "Error 2", null),
                    new ValidationResult.ValidationError("field3", "Error 3", null)
            );

            ValidationResult result = ValidationResult.invalid(errors);

            assertThat(result.getErrors()).hasSize(3);
            assertThat(result.getErrors().get(0).field()).isEqualTo("field1");
            assertThat(result.getErrors().get(1).field()).isEqualTo("field2");
            assertThat(result.getErrors().get(2).field()).isEqualTo("field3");
        }

        @Test
        @DisplayName("Should store multiple warnings")
        void shouldStoreMultipleWarnings() {
            List<ValidationResult.ValidationWarning> warnings = List.of(
                    new ValidationResult.ValidationWarning("field1", "Warning 1"),
                    new ValidationResult.ValidationWarning("field2", "Warning 2"),
                    new ValidationResult.ValidationWarning("field3", "Warning 3")
            );

            ValidationResult result = ValidationResult.withWarnings(warnings);

            assertThat(result.getWarnings()).hasSize(3);
        }

        @Test
        @DisplayName("Should handle same field with multiple errors")
        void shouldHandleSameFieldMultipleErrors() {
            List<ValidationResult.ValidationError> errors = List.of(
                    new ValidationResult.ValidationError("email", "Invalid format", "test"),
                    new ValidationResult.ValidationError("email", "Already exists", "test@test.com")
            );

            ValidationResult result = ValidationResult.invalid(errors);

            assertThat(result.getErrors()).hasSize(2);
            assertThat(result.getErrors().get(0).field()).isEqualTo("email");
            assertThat(result.getErrors().get(1).field()).isEqualTo("email");
        }
    }

    @Nested
    @DisplayName("Validation Scenarios Tests")
    class ValidationScenariosTests {

        @Test
        @DisplayName("Should represent required field validation error")
        void shouldRepresentRequiredFieldError() {
            ValidationResult.ValidationError error = new ValidationResult.ValidationError(
                    "email",
                    "Field is required",
                    null
            );

            assertThat(error.message()).contains("required");
            assertThat(error.invalidValue()).isNull();
        }

        @Test
        @DisplayName("Should represent type validation error")
        void shouldRepresentTypeError() {
            ValidationResult.ValidationError error = new ValidationResult.ValidationError(
                    "age",
                    "Expected type: integer, got: String",
                    "twenty-five"
            );

            assertThat(error.message()).contains("Expected type");
            assertThat(error.invalidValue()).isEqualTo("twenty-five");
        }

        @Test
        @DisplayName("Should represent range validation error")
        void shouldRepresentRangeError() {
            ValidationResult.ValidationError error = new ValidationResult.ValidationError(
                    "score",
                    "Value out of range [0, 100]",
                    150
            );

            assertThat(error.message()).contains("range");
            assertThat(error.invalidValue()).isEqualTo(150);
        }

        @Test
        @DisplayName("Should represent email validation error")
        void shouldRepresentEmailError() {
            ValidationResult.ValidationError error = new ValidationResult.ValidationError(
                    "email",
                    "Invalid email format",
                    "not-an-email"
            );

            assertThat(error.message()).contains("email");
        }

        @Test
        @DisplayName("Should represent pattern validation error")
        void shouldRepresentPatternError() {
            ValidationResult.ValidationError error = new ValidationResult.ValidationError(
                    "phone",
                    "Does not match required pattern",
                    "123"
            );

            assertThat(error.message()).contains("pattern");
        }
    }

    @Nested
    @DisplayName("Record Equality Tests")
    class RecordEqualityTests {

        @Test
        @DisplayName("Should validate ValidationError equality")
        void shouldValidateErrorEquality() {
            ValidationResult.ValidationError error1 = new ValidationResult.ValidationError(
                    "field",
                    "message",
                    null
            );

            ValidationResult.ValidationError error2 = new ValidationResult.ValidationError(
                    "field",
                    "message",
                    null
            );

            assertThat(error1).isEqualTo(error2);
            assertThat(error1.hashCode()).isEqualTo(error2.hashCode());
        }

        @Test
        @DisplayName("Should validate ValidationWarning equality")
        void shouldValidateWarningEquality() {
            ValidationResult.ValidationWarning warning1 = new ValidationResult.ValidationWarning(
                    "field",
                    "message"
            );

            ValidationResult.ValidationWarning warning2 = new ValidationResult.ValidationWarning(
                    "field",
                    "message"
            );

            assertThat(warning1).isEqualTo(warning2);
            assertThat(warning1.hashCode()).isEqualTo(warning2.hashCode());
        }

        @Test
        @DisplayName("Should not equal different ValidationErrors")
        void shouldNotEqualDifferentErrors() {
            ValidationResult.ValidationError error1 = new ValidationResult.ValidationError(
                    "field1",
                    "message1",
                    null
            );

            ValidationResult.ValidationError error2 = new ValidationResult.ValidationError(
                    "field2",
                    "message2",
                    null
            );

            assertThat(error1).isNotEqualTo(error2);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should have meaningful ValidationError toString")
        void shouldHaveMeaningfulErrorToString() {
            ValidationResult.ValidationError error = new ValidationResult.ValidationError(
                    "email",
                    "Invalid format",
                    "test"
            );

            String toString = error.toString();
            assertThat(toString).contains("email");
            assertThat(toString).contains("Invalid format");
        }

        @Test
        @DisplayName("Should have meaningful ValidationWarning toString")
        void shouldHaveMeaningfulWarningToString() {
            ValidationResult.ValidationWarning warning = new ValidationResult.ValidationWarning(
                    "field",
                    "Warning message"
            );

            String toString = warning.toString();
            assertThat(toString).contains("field");
            assertThat(toString).contains("Warning message");
        }
    }
}
