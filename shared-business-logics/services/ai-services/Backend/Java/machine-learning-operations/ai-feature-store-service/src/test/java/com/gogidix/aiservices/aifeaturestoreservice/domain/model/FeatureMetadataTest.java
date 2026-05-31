package com.gogidix.aiservices.aifeaturestoreservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("FeatureMetadata Record Tests")
class FeatureMetadataTest {

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create metadata with valid key and value")
        void shouldCreateWithValidKeyAndValue() {
            FeatureMetadata metadata = new FeatureMetadata("feature_key", "feature_value");

            assertThat(metadata.key()).isEqualTo("feature_key");
            assertThat(metadata.value()).isEqualTo("feature_value");
        }

        @Test
        @DisplayName("Should create metadata using factory method")
        void shouldCreateUsingFactoryMethod() {
            FeatureMetadata metadata = FeatureMetadata.of("feature_key", 42);

            assertThat(metadata.key()).isEqualTo("feature_key");
            assertThat(metadata.value()).isEqualTo(42);
        }

        @Test
        @DisplayName("Should accept numeric value")
        void shouldAcceptNumericValue() {
            FeatureMetadata metadata = FeatureMetadata.of("age", 25.5);

            assertThat(metadata.key()).isEqualTo("age");
            assertThat(metadata.value()).isEqualTo(25.5);
        }

        @Test
        @DisplayName("Should accept string value")
        void shouldAcceptStringValue() {
            FeatureMetadata metadata = FeatureMetadata.of("name", "John Doe");

            assertThat(metadata.key()).isEqualTo("name");
            assertThat(metadata.value()).isEqualTo("John Doe");
        }

        @Test
        @DisplayName("Should accept boolean value")
        void shouldAcceptBooleanValue() {
            FeatureMetadata metadata = FeatureMetadata.of("is_active", true);

            assertThat(metadata.key()).isEqualTo("is_active");
            assertThat(metadata.value()).isEqualTo(true);
        }

        @Test
        @DisplayName("Should accept null as value (Object type)")
        void shouldAcceptNullAsObjectValue() {
            FeatureMetadata metadata = new FeatureMetadata("key", null);

            assertThat(metadata.key()).isEqualTo("key");
            assertThat(metadata.value()).isNull();
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should throw when key is null")
        void shouldThrowWhenKeyIsNull() {
            assertThatThrownBy(() -> new FeatureMetadata(null, "value"))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("key");
        }

        @Test
        @DisplayName("Should throw when value is null")
        void shouldThrowWhenValueIsNull() {
            assertThatThrownBy(() -> new FeatureMetadata("key", null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("value");
        }
    }

    @Nested
    @DisplayName("Record Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal when keys and values match")
        void shouldBeEqualWhenKeysAndValuesMatch() {
            FeatureMetadata metadata1 = new FeatureMetadata("key", "value");
            FeatureMetadata metadata2 = new FeatureMetadata("key", "value");

            assertThat(metadata1).isEqualTo(metadata2);
            assertThat(metadata1.hashCode()).isEqualTo(metadata2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when keys differ")
        void shouldNotBeEqualWhenKeysDiffer() {
            FeatureMetadata metadata1 = new FeatureMetadata("key1", "value");
            FeatureMetadata metadata2 = new FeatureMetadata("key2", "value");

            assertThat(metadata1).isNotEqualTo(metadata2);
        }

        @Test
        @DisplayName("Should not be equal when values differ")
        void shouldNotBeEqualWhenValuesDiffer() {
            FeatureMetadata metadata1 = new FeatureMetadata("key", "value1");
            FeatureMetadata metadata2 = new FeatureMetadata("key", "value2");

            assertThat(metadata1).isNotEqualTo(metadata2);
        }

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            FeatureMetadata metadata = new FeatureMetadata("key", "value");

            assertThat(metadata).isEqualTo(metadata);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should contain key in toString")
        void shouldContainKeyInToString() {
            FeatureMetadata metadata = new FeatureMetadata("feature_key", "value");

            assertThat(metadata.toString()).contains("feature_key");
        }

        @Test
        @DisplayName("Should contain value in toString")
        void shouldContainValueInToString() {
            FeatureMetadata metadata = new FeatureMetadata("key", "feature_value");

            assertThat(metadata.toString()).contains("feature_value");
        }
    }

    @Nested
    @DisplayName("Component Access Tests")
    class ComponentAccessTests {

        @Test
        @DisplayName("Should access key component")
        void shouldAccessKeyComponent() {
            FeatureMetadata metadata = new FeatureMetadata("test_key", 123);

            assertThat(metadata.key()).isEqualTo("test_key");
        }

        @Test
        @DisplayName("Should access value component")
        void shouldAccessValueComponent() {
            FeatureMetadata metadata = new FeatureMetadata("key", "test_value");

            assertThat(metadata.value()).isEqualTo("test_value");
        }
    }

    @Nested
    @DisplayName("Value Type Tests")
    class ValueTypeTests {

        @Test
        @DisplayName("Should store integer value")
        void shouldStoreIntegerValue() {
            FeatureMetadata metadata = new FeatureMetadata("count", 42);

            assertThat(metadata.value()).isInstanceOf(Integer.class);
            assertThat(metadata.value()).isEqualTo(42);
        }

        @Test
        @DisplayName("Should store double value")
        void shouldStoreDoubleValue() {
            FeatureMetadata metadata = new FeatureMetadata("price", 19.99);

            assertThat(metadata.value()).isInstanceOf(Double.class);
            assertThat(metadata.value()).isEqualTo(19.99);
        }

        @Test
        @DisplayName("Should store long value")
        void shouldStoreLongValue() {
            FeatureMetadata metadata = new FeatureMetadata("timestamp", 1234567890L);

            assertThat(metadata.value()).isInstanceOf(Long.class);
            assertThat(metadata.value()).isEqualTo(1234567890L);
        }

        @Test
        @DisplayName("Should store string value")
        void shouldStoreStringValue() {
            FeatureMetadata metadata = new FeatureMetadata("description", "A feature");

            assertThat(metadata.value()).isInstanceOf(String.class);
            assertThat(metadata.value()).isEqualTo("A feature");
        }
    }
}
