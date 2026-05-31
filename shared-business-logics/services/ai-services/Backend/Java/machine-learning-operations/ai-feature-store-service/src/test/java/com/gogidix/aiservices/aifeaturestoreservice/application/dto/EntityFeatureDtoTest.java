package com.gogidix.aiservices.aifeaturestoreservice.application.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EntityFeatureDto Tests")
class EntityFeatureDtoTest {

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create with string value")
        void shouldCreateWithStringValue() {
            EntityFeatureDto dto = new EntityFeatureDto("entity-123", "feature_value");

            assertThat(dto.entityId()).isEqualTo("entity-123");
            assertThat(dto.value()).isEqualTo("feature_value");
        }

        @Test
        @DisplayName("Should create with numeric value")
        void shouldCreateWithNumericValue() {
            EntityFeatureDto dto = new EntityFeatureDto("entity-456", 42.5);

            assertThat(dto.entityId()).isEqualTo("entity-456");
            assertThat(dto.value()).isEqualTo(42.5);
        }

        @Test
        @DisplayName("Should create with boolean value")
        void shouldCreateWithBooleanValue() {
            EntityFeatureDto dto = new EntityFeatureDto("entity-789", true);

            assertThat(dto.entityId()).isEqualTo("entity-789");
            assertThat(dto.value()).isEqualTo(true);
        }

        @Test
        @DisplayName("Should create with null value")
        void shouldCreateWithNullValue() {
            EntityFeatureDto dto = new EntityFeatureDto("entity-null", null);

            assertThat(dto.entityId()).isEqualTo("entity-null");
            assertThat(dto.value()).isNull();
        }
    }

    @Nested
    @DisplayName("Value Type Tests")
    class ValueTypeTests {

        @Test
        @DisplayName("Should accept integer value")
        void shouldAcceptIntegerValue() {
            EntityFeatureDto dto = new EntityFeatureDto("e1", 123);

            assertThat(dto.value()).isInstanceOf(Integer.class);
            assertThat(dto.value()).isEqualTo(123);
        }

        @Test
        @DisplayName("Should accept long value")
        void shouldAcceptLongValue() {
            EntityFeatureDto dto = new EntityFeatureDto("e1", 1234567890L);

            assertThat(dto.value()).isInstanceOf(Long.class);
            assertThat(dto.value()).isEqualTo(1234567890L);
        }

        @Test
        @DisplayName("Should accept double value")
        void shouldAcceptDoubleValue() {
            EntityFeatureDto dto = new EntityFeatureDto("e1", 99.99);

            assertThat(dto.value()).isInstanceOf(Double.class);
            assertThat(dto.value()).isEqualTo(99.99);
        }

        @Test
        @DisplayName("Should accept list value (for vector)")
        void shouldAcceptListValue() {
            List<Double> vector = List.of(0.1, 0.2, 0.3);
            EntityFeatureDto dto = new EntityFeatureDto("e1", vector);

            assertThat(dto.value()).isInstanceOf(List.class);
            assertThat(dto.value()).isEqualTo(vector);
        }

        @Test
        @DisplayName("Should accept string value")
        void shouldAcceptStringValue() {
            EntityFeatureDto dto = new EntityFeatureDto("e1", "text_value");

            assertThat(dto.value()).isInstanceOf(String.class);
            assertThat(dto.value()).isEqualTo("text_value");
        }
    }

    @Nested
    @DisplayName("Entity ID Tests")
    class EntityIdTests {

        @ParameterizedTest
        @ValueSource(strings = {"entity-1", "user_123", "customer:456", "id.with.dots"})
        @DisplayName("Should accept various entity ID formats")
        void shouldAcceptVariousEntityIdFormats(String entityId) {
            EntityFeatureDto dto = new EntityFeatureDto(entityId, "value");

            assertThat(dto.entityId()).isEqualTo(entityId);
        }

        @Test
        @DisplayName("Should accept UUID as entity ID")
        void shouldAcceptUuidAsEntityId() {
            String uuid = java.util.UUID.randomUUID().toString();
            EntityFeatureDto dto = new EntityFeatureDto(uuid, "value");

            assertThat(dto.entityId()).isEqualTo(uuid);
        }

        @Test
        @DisplayName("Should accept numeric string as entity ID")
        void shouldAcceptNumericStringAsEntityId() {
            EntityFeatureDto dto = new EntityFeatureDto("12345", "value");

            assertThat(dto.entityId()).isEqualTo("12345");
        }
    }

    @Nested
    @DisplayName("Record Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal when all fields match")
        void shouldBeEqualWhenAllFieldsMatch() {
            EntityFeatureDto dto1 = new EntityFeatureDto("entity-1", "value");
            EntityFeatureDto dto2 = new EntityFeatureDto("entity-1", "value");

            assertThat(dto1).isEqualTo(dto2);
            assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when entity IDs differ")
        void shouldNotBeEqualWhenEntityIdsDiffer() {
            EntityFeatureDto dto1 = new EntityFeatureDto("entity-1", "value");
            EntityFeatureDto dto2 = new EntityFeatureDto("entity-2", "value");

            assertThat(dto1).isNotEqualTo(dto2);
        }

        @Test
        @DisplayName("Should not be equal when values differ")
        void shouldNotBeEqualWhenValuesDiffer() {
            EntityFeatureDto dto1 = new EntityFeatureDto("entity-1", "value1");
            EntityFeatureDto dto2 = new EntityFeatureDto("entity-1", "value2");

            assertThat(dto1).isNotEqualTo(dto2);
        }

        @Test
        @DisplayName("Should be equal when values are both null")
        void shouldBeEqualWhenValuesAreBothNull() {
            EntityFeatureDto dto1 = new EntityFeatureDto("entity-1", null);
            EntityFeatureDto dto2 = new EntityFeatureDto("entity-1", null);

            assertThat(dto1).isEqualTo(dto2);
        }

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            EntityFeatureDto dto = new EntityFeatureDto("entity-1", "value");

            assertThat(dto).isEqualTo(dto);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should contain entity ID in toString")
        void shouldContainEntityIdInToString() {
            EntityFeatureDto dto = new EntityFeatureDto("entity-123", "value");

            assertThat(dto.toString()).contains("entity-123");
        }

        @Test
        @DisplayName("Should contain value in toString")
        void shouldContainValueInToString() {
            EntityFeatureDto dto = new EntityFeatureDto("e1", "feature_value");

            assertThat(dto.toString()).contains("feature_value");
        }
    }

    @Nested
    @DisplayName("Component Access Tests")
    class ComponentAccessTests {

        @Test
        @DisplayName("Should access entityId component")
        void shouldAccessEntityIdComponent() {
            EntityFeatureDto dto = new EntityFeatureDto("test-entity", 123);

            assertThat(dto.entityId()).isEqualTo("test-entity");
        }

        @Test
        @DisplayName("Should access value component")
        void shouldAccessValueComponent() {
            EntityFeatureDto dto = new EntityFeatureDto("e1", 456);

            assertThat(dto.value()).isEqualTo(456);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle empty string entity ID")
        void shouldHandleEmptyStringEntityId() {
            EntityFeatureDto dto = new EntityFeatureDto("", "value");

            assertThat(dto.entityId()).isEmpty();
        }

        @Test
        @DisplayName("Should handle empty string value")
        void shouldHandleEmptyStringValue() {
            EntityFeatureDto dto = new EntityFeatureDto("e1", "");

            assertThat(dto.value()).isEqualTo("");
        }

        @Test
        @DisplayName("Should handle unicode in entity ID")
        void shouldHandleUnicodeInEntityId() {
            EntityFeatureDto dto = new EntityFeatureDto("实体-123", "value");

            assertThat(dto.entityId()).isEqualTo("实体-123");
        }

        @Test
        @DisplayName("Should handle unicode in value")
        void shouldHandleUnicodeInValue() {
            EntityFeatureDto dto = new EntityFeatureDto("e1", "値");

            assertThat(dto.value()).isEqualTo("値");
        }

        @Test
        @DisplayName("Should handle very long entity ID")
        void shouldHandleVeryLongEntityId() {
            String longId = "a".repeat(1000);
            EntityFeatureDto dto = new EntityFeatureDto(longId, "value");

            assertThat(dto.entityId()).hasSize(1000);
        }
    }

    @Nested
    @DisplayName("Feature Store Context Tests")
    class FeatureStoreContextTests {

        @Test
        @DisplayName("Should represent user feature")
        void shouldRepresentUserFeature() {
            EntityFeatureDto dto = new EntityFeatureDto("user-123", "premium_customer");

            assertThat(dto.entityId()).startsWith("user-");
        }

        @Test
        @DisplayName("Should represent product feature")
        void shouldRepresentProductFeature() {
            EntityFeatureDto dto = new EntityFeatureDto("product-456", 29.99);

            assertThat(dto.entityId()).startsWith("product-");
        }

        @Test
        @DisplayName("Should represent session feature")
        void shouldRepresentSessionFeature() {
            EntityFeatureDto dto = new EntityFeatureDto("session-789", true);

            assertThat(dto.entityId()).startsWith("session-");
        }

        @Test
        @DisplayName("Should represent vector embedding")
        void shouldRepresentVectorEmbedding() {
            List<Double> embedding = List.of(0.1, 0.2, 0.3, 0.4, 0.5);
            EntityFeatureDto dto = new EntityFeatureDto("item-123", embedding);

            assertThat(dto.value()).isInstanceOf(List.class);
            assertThat((List<?>) dto.value()).hasSize(5);
        }
    }
}
