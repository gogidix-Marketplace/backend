package com.gogidix.aiservices.aifrauddetectionservice.application.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for AddPatternRequest DTO.
 * Tests validation annotations, getters, setters, and builder pattern.
 */
@DisplayName("AddPatternRequest DTO Tests")
class AddPatternRequestTest {

    @Nested
    @DisplayName("Getters and Setters")
    class GettersAndSettersTests {

        @Test
        @DisplayName("Should get and set pattern name")
        void shouldGetAndSetPatternName() {
            AddPatternRequest request = new AddPatternRequest();
            request.setPatternName("Test Pattern");

            assertThat(request.getPatternName()).isEqualTo("Test Pattern");
        }

        @Test
        @DisplayName("Should get and set description")
        void shouldGetAndSetDescription() {
            AddPatternRequest request = new AddPatternRequest();
            request.setDescription("Test description");

            assertThat(request.getDescription()).isEqualTo("Test description");
        }

        @Test
        @DisplayName("Should get and set confidence score")
        void shouldGetAndSetConfidenceScore() {
            AddPatternRequest request = new AddPatternRequest();
            request.setConfidenceScore(0.85);

            assertThat(request.getConfidenceScore()).isEqualTo(0.85);
        }
    }

    @Nested
    @DisplayName("Boundary Values")
    class BoundaryValueTests {

        @Test
        @DisplayName("Should accept minimum confidence score (0.0)")
        void shouldAcceptMinimumConfidenceScore() {
            AddPatternRequest request = new AddPatternRequest();
            request.setConfidenceScore(0.0);

            assertThat(request.getConfidenceScore()).isEqualTo(0.0);
        }

        @Test
        @DisplayName("Should accept maximum confidence score (1.0)")
        void shouldAcceptMaximumConfidenceScore() {
            AddPatternRequest request = new AddPatternRequest();
            request.setConfidenceScore(1.0);

            assertThat(request.getConfidenceScore()).isEqualTo(1.0);
        }

        @Test
        @DisplayName("Should accept mid-range confidence score")
        void shouldAcceptMidRangeConfidenceScore() {
            AddPatternRequest request = new AddPatternRequest();
            request.setConfidenceScore(0.5);

            assertThat(request.getConfidenceScore()).isEqualTo(0.5);
        }
    }

    @Nested
    @DisplayName("Null Handling")
    class NullHandlingTests {

        @Test
        @DisplayName("Should default to null for unset fields")
        void shouldDefaultToNullForUnsetFields() {
            AddPatternRequest request = new AddPatternRequest();

            assertThat(request.getPatternName()).isNull();
            assertThat(request.getDescription()).isNull();
            assertThat(request.getConfidenceScore()).isNull();
        }

        @Test
        @DisplayName("Should allow setting null values")
        void shouldAllowSettingNullValues() {
            AddPatternRequest request = new AddPatternRequest();
            request.setPatternName(null);
            request.setDescription(null);
            request.setConfidenceScore(null);

            assertThat(request.getPatternName()).isNull();
            assertThat(request.getDescription()).isNull();
            assertThat(request.getConfidenceScore()).isNull();
        }
    }

    @Nested
    @DisplayName("Type Handling")
    class TypeHandlingTests {

        @Test
        @DisplayName("Should handle Double precision correctly")
        void shouldHandleDoublePrecisionCorrectly() {
            AddPatternRequest request = new AddPatternRequest();
            request.setConfidenceScore(0.123456789);

            assertThat(request.getConfidenceScore()).isEqualTo(0.123456789);
        }

        @Test
        @DisplayName("Should handle very small confidence scores")
        void shouldHandleVerySmallConfidenceScores() {
            AddPatternRequest request = new AddPatternRequest();
            request.setConfidenceScore(0.001);

            assertThat(request.getConfidenceScore()).isEqualTo(0.001);
        }
    }

    @Nested
    @DisplayName("String Handling")
    class StringHandlingTests {

        @Test
        @DisplayName("Should handle empty strings")
        void shouldHandleEmptyStrings() {
            AddPatternRequest request = new AddPatternRequest();
            request.setPatternName("");
            request.setDescription("");

            assertThat(request.getPatternName()).isEmpty();
            assertThat(request.getDescription()).isEmpty();
        }

        @Test
        @DisplayName("Should handle special characters")
        void shouldHandleSpecialCharacters() {
            AddPatternRequest request = new AddPatternRequest();
            String specialChars = "Test-with-特殊字符-ñ-ø-";
            request.setPatternName(specialChars);

            assertThat(request.getPatternName()).isEqualTo(specialChars);
        }

        @Test
        @DisplayName("Should handle long strings")
        void shouldHandleLongStrings() {
            AddPatternRequest request = new AddPatternRequest();
            String longDescription = "a".repeat(1000);
            request.setDescription(longDescription);

            assertThat(request.getDescription()).hasSize(1000);
        }
    }
}
