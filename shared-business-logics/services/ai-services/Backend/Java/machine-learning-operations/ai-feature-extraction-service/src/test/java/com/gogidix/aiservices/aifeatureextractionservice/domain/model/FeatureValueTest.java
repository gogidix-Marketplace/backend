package com.gogidix.aiservices.aifeatureextractionservice.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FeatureValue value object.
 */
@DisplayName("FeatureValue Tests")
class FeatureValueTest {

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create feature value with valid parameters")
        void shouldCreateFeatureValueWithValidParameters() {
            FeatureValue featureValue = new FeatureValue("feature1", 100.0);

            assertEquals("feature1", featureValue.name());
            assertEquals(100.0, featureValue.value());
        }

        @Test
        @DisplayName("Should throw exception when name is null")
        void shouldThrowWhenNameIsNull() {
            assertThrows(NullPointerException.class,
                    () -> new FeatureValue(null, 100.0));
        }

        @Test
        @DisplayName("Should throw exception when value is null")
        void shouldThrowWhenValueIsNull() {
            assertThrows(NullPointerException.class,
                    () -> new FeatureValue("feature1", null));
        }

        @Test
        @DisplayName("Should throw exception when name is blank")
        void shouldThrowWhenNameIsBlank() {
            assertThrows(IllegalArgumentException.class,
                    () -> new FeatureValue("  ", 100.0));
        }
    }

    @Nested
    @DisplayName("Factory Methods Tests")
    class FactoryMethodsTests {

        @Test
        @DisplayName("Should create feature value using of method")
        void shouldCreateUsingOfMethod() {
            FeatureValue featureValue = FeatureValue.of("feature1", "value1");

            assertEquals("feature1", featureValue.name());
            assertEquals("value1", featureValue.value());
        }

        @Test
        @DisplayName("Should create numeric feature value")
        void shouldCreateNumericFeatureValue() {
            FeatureValue featureValue = FeatureValue.numeric("age", 25);

            assertEquals("age", featureValue.name());
            assertEquals(25.0, featureValue.value());
        }

        @Test
        @DisplayName("Should create categorical feature value")
        void shouldCreateCategoricalFeatureValue() {
            FeatureValue featureValue = FeatureValue.categorical("category", "A");

            assertEquals("category", featureValue.name());
            assertEquals("A", featureValue.value());
        }

        @Test
        @DisplayName("Should create text feature value")
        void shouldCreateTextFeatureValue() {
            FeatureValue featureValue = FeatureValue.text("description", "Some text");

            assertEquals("description", featureValue.name());
            assertEquals("Some text", featureValue.value());
        }
    }
}
