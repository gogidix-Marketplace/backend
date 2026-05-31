package com.gogidix.aiservices.aidocumentprocessingservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ExtractedField Domain Model Tests")
class ExtractedFieldTest {

    private static final String VALID_FIELD_NAME = "invoice_number";
    private static final String VALID_VALUE = "INV-2024-001";
    private static final double VALID_CONFIDENCE = 0.95;

    @Nested
    @DisplayName("Field Creation Tests")
    class FieldCreationTests {

        @Test
        @DisplayName("Should create field with valid parameters")
        void shouldCreateFieldWithValidParameters() {
            ExtractedField field = ExtractedField.builder()
                    .name(VALID_FIELD_NAME)
                    .value(VALID_VALUE)
                    .confidence(VALID_CONFIDENCE)
                    .build();

            assertThat(field).isNotNull();
            assertThat(field.getName()).isEqualTo(VALID_FIELD_NAME);
            assertThat(field.getValue()).isEqualTo(VALID_VALUE);
            assertThat(field.getConfidence()).isEqualTo(VALID_CONFIDENCE);
        }

        @Test
        @DisplayName("Should reject null field name")
        void shouldRejectNullFieldName() {
            assertThatThrownBy(() -> ExtractedField.builder()
                    .name(null)
                    .value(VALID_VALUE)
                    .confidence(VALID_CONFIDENCE)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Field name cannot be null");
        }

        @Test
        @DisplayName("Should reject empty field name")
        void shouldRejectEmptyFieldName() {
            assertThatThrownBy(() -> ExtractedField.builder()
                    .name("")
                    .value(VALID_VALUE)
                    .confidence(VALID_CONFIDENCE)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Field name cannot be empty");
        }

        @ParameterizedTest
        @ValueSource(doubles = {-0.1, 1.1, 2.0})
        @DisplayName("Should reject confidence out of range")
        void shouldRejectConfidenceOutOfRange(double confidence) {
            assertThatThrownBy(() -> ExtractedField.builder()
                    .name(VALID_FIELD_NAME)
                    .value(VALID_VALUE)
                    .confidence(confidence)
                    .build())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Confidence must be between 0 and 1");
        }

        @Test
        @DisplayName("Should allow field with null value")
        void shouldAllowNullValue() {
            ExtractedField field = ExtractedField.builder()
                    .name(VALID_FIELD_NAME)
                    .value(null)
                    .confidence(VALID_CONFIDENCE)
                    .build();

            assertThat(field.getValue()).isNull();
        }
    }

    @Nested
    @DisplayName("Field Validation Tests")
    class FieldValidationTests {

        @Test
        @DisplayName("Should check if field meets confidence threshold")
        void shouldCheckConfidenceThreshold() {
            ExtractedField highConfidence = ExtractedField.builder()
                    .name("field1")
                    .value("value1")
                    .confidence(0.8)
                    .build();

            ExtractedField lowConfidence = ExtractedField.builder()
                    .name("field2")
                    .value("value2")
                    .confidence(0.5)
                    .build();

            assertThat(highConfidence.meetsThreshold(0.7)).isTrue();
            assertThat(lowConfidence.meetsThreshold(0.7)).isFalse();
        }

        @Test
        @DisplayName("Should validate required field")
        void shouldValidateRequiredField() {
            ExtractedField presentField = ExtractedField.builder()
                    .name("field1")
                    .value("value1")
                    .confidence(0.9)
                    .build();

            ExtractedField emptyField = ExtractedField.builder()
                    .name("field2")
                    .value("")
                    .confidence(0.9)
                    .build();

            assertThat(presentField.isValidRequired()).isTrue();
            assertThat(emptyField.isValidRequired()).isFalse();
        }

        @Test
        @DisplayName("Should check if field is valid")
        void shouldCheckIfValid() {
            ExtractedField validField = ExtractedField.builder()
                    .name("field1")
                    .value("value1")
                    .confidence(0.75)
                    .build();

            assertThat(validField.isValid(0.7)).isTrue();
            assertThat(validField.isValid(0.8)).isFalse();
        }
    }

    @Nested
    @DisplayName("Field Metadata Tests")
    class FieldMetadataTests {

        @Test
        @DisplayName("Should store and retrieve metadata")
        void shouldStoreMetadata() {
            Map<String, Object> metadata = Map.of(
                    "position", Map.of("x", 100, "y", 200),
                    "page", 1
            );

            ExtractedField field = ExtractedField.builder()
                    .name("field1")
                    .value("value1")
                    .confidence(0.9)
                    .metadata(metadata)
                    .build();

            assertThat(field.getMetadata()).isNotNull();
            assertThat(field.getMetadata()).containsKey("position");
        }

        @Test
        @DisplayName("Should get bounding box from metadata")
        void shouldGetBoundingBox() {
            Map<String, Object> position = Map.of("x", 100, "y", 200, "width", 50, "height", 20);
            Map<String, Object> metadata = Map.of("position", position);

            ExtractedField field = ExtractedField.builder()
                    .name("field1")
                    .value("value1")
                    .confidence(0.9)
                    .metadata(metadata)
                    .build();

            assertThat(field.getBoundingBox()).isNotNull();
            assertThat(field.getBoundingBox().get("x")).isEqualTo(100);
        }

        @Test
        @DisplayName("Should get page number from metadata")
        void shouldGetPageNumber() {
            Map<String, Object> metadata = Map.of("page", 2);

            ExtractedField field = ExtractedField.builder()
                    .name("field1")
                    .value("value1")
                    .confidence(0.9)
                    .metadata(metadata)
                    .build();

            assertThat(field.getPageNumber()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should return default page number when not in metadata")
        void shouldReturnDefaultPageNumber() {
            ExtractedField field = ExtractedField.builder()
                    .name("field1")
                    .value("value1")
                    .confidence(0.9)
                    .build();

            assertThat(field.getPageNumber()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Field Type Tests")
    class FieldTypeTests {

        @Test
        @DisplayName("Should identify numeric field")
        void shouldIdentifyNumericField() {
            ExtractedField numericField = ExtractedField.builder()
                    .name("amount")
                    .value("123.45")
                    .confidence(0.9)
                    .build();

            assertThat(numericField.isNumeric()).isTrue();
        }

        @Test
        @DisplayName("Should identify date field")
        void shouldIdentifyDateField() {
            ExtractedField dateField = ExtractedField.builder()
                    .name("date")
                    .value("2024-01-15")
                    .confidence(0.9)
                    .build();

            assertThat(dateField.isDate()).isTrue();
        }

        @Test
        @DisplayName("Should get numeric value")
        void shouldGetNumericValue() {
            ExtractedField numericField = ExtractedField.builder()
                    .name("amount")
                    .value("123.45")
                    .confidence(0.9)
                    .build();

            assertThat(numericField.getAsNumeric()).isEqualTo(123.45);
        }

        @Test
        @DisplayName("Should return null for non-numeric value")
        void shouldReturnNullForNonNumeric() {
            ExtractedField textField = ExtractedField.builder()
                    .name("name")
                    .value("John Doe")
                    .confidence(0.9)
                    .build();

            assertThat(textField.getAsNumeric()).isNull();
        }
    }
}
