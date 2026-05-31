package com.gogidix.aiservices.aifeaturestoreservice.application.dto;

import com.gogidix.aiservices.aifeaturestoreservice.domain.model.FeatureType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("StoreFeaturesRequestDto Tests")
class StoreFeaturesRequestDtoTest {

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create request with all fields")
        void shouldCreateWithAllFields() {
            StoreFeaturesRequestDto.EntityFeatureValue value =
                    new StoreFeaturesRequestDto.EntityFeatureValue("entity-123", 42.0);

            StoreFeaturesRequestDto request = new StoreFeaturesRequestDto(
                    "user_age",
                    FeatureType.NUMERIC,
                    List.of(value),
                    "User age feature",
                    Map.of("source", "profile")
            );

            assertThat(request.featureName()).isEqualTo("user_age");
            assertThat(request.featureType()).isEqualTo(FeatureType.NUMERIC);
            assertThat(request.values()).hasSize(1);
            assertThat(request.description()).isEqualTo("User age feature");
            assertThat(request.metadata()).hasSize(1);
        }

        @Test
        @DisplayName("Should create request with minimal required fields")
        void shouldCreateWithMinimalFields() {
            StoreFeaturesRequestDto.EntityFeatureValue value =
                    new StoreFeaturesRequestDto.EntityFeatureValue("entity-123", "value");

            StoreFeaturesRequestDto request = new StoreFeaturesRequestDto(
                    "feature_name",
                    FeatureType.TEXT,
                    List.of(value),
                    null,
                    null
            );

            assertThat(request.featureName()).isEqualTo("feature_name");
            assertThat(request.featureType()).isEqualTo(FeatureType.TEXT);
            assertThat(request.values()).hasSize(1);
            assertThat(request.description()).isNull();
            assertThat(request.metadata()).isNull();
        }
    }

    @Nested
    @DisplayName("EntityFeatureValue Tests")
    class EntityFeatureValueTests {

        @Test
        @DisplayName("Should create entity feature value")
        void shouldCreateEntityFeatureValue() {
            StoreFeaturesRequestDto.EntityFeatureValue value =
                    new StoreFeaturesRequestDto.EntityFeatureValue("entity-123", 100);

            assertThat(value.entityId()).isEqualTo("entity-123");
            assertThat(value.value()).isEqualTo(100);
        }

        @Test
        @DisplayName("Should accept numeric value")
        void shouldAcceptNumericValue() {
            StoreFeaturesRequestDto.EntityFeatureValue value =
                    new StoreFeaturesRequestDto.EntityFeatureValue("entity-1", 99.5);

            assertThat(value.value()).isEqualTo(99.5);
        }

        @Test
        @DisplayName("Should accept string value")
        void shouldAcceptStringValue() {
            StoreFeaturesRequestDto.EntityFeatureValue value =
                    new StoreFeaturesRequestDto.EntityFeatureValue("entity-2", "text_value");

            assertThat(value.value()).isEqualTo("text_value");
        }

        @Test
        @DisplayName("Should accept boolean value")
        void shouldAcceptBooleanValue() {
            StoreFeaturesRequestDto.EntityFeatureValue value =
                    new StoreFeaturesRequestDto.EntityFeatureValue("entity-3", true);

            assertThat(value.value()).isEqualTo(true);
        }

        @Test
        @DisplayName("Should accept null value")
        void shouldAcceptNullValue() {
            StoreFeaturesRequestDto.EntityFeatureValue value =
                    new StoreFeaturesRequestDto.EntityFeatureValue("entity-4", null);

            assertThat(value.value()).isNull();
        }
    }

    @Nested
    @DisplayName("FeatureType Support Tests")
    class FeatureTypeSupportTests {

        @Test
        @DisplayName("Should accept NUMERIC feature type")
        void shouldAcceptNumericFeatureType() {
            StoreFeaturesRequestDto request = new StoreFeaturesRequestDto(
                    "numeric_feature",
                    FeatureType.NUMERIC,
                    List.of(new StoreFeaturesRequestDto.EntityFeatureValue("e1", 123.45)),
                    null,
                    null
            );

            assertThat(request.featureType()).isEqualTo(FeatureType.NUMERIC);
        }

        @Test
        @DisplayName("Should accept CATEGORICAL feature type")
        void shouldAcceptCategoricalFeatureType() {
            StoreFeaturesRequestDto request = new StoreFeaturesRequestDto(
                    "category_feature",
                    FeatureType.CATEGORICAL,
                    List.of(new StoreFeaturesRequestDto.EntityFeatureValue("e1", "premium")),
                    null,
                    null
            );

            assertThat(request.featureType()).isEqualTo(FeatureType.CATEGORICAL);
        }

        @Test
        @DisplayName("Should accept TEXT feature type")
        void shouldAcceptTextFeatureType() {
            StoreFeaturesRequestDto request = new StoreFeaturesRequestDto(
                    "text_feature",
                    FeatureType.TEXT,
                    List.of(new StoreFeaturesRequestDto.EntityFeatureValue("e1", "Some text content")),
                    null,
                    null
            );

            assertThat(request.featureType()).isEqualTo(FeatureType.TEXT);
        }

        @Test
        @DisplayName("Should accept BOOLEAN feature type")
        void shouldAcceptBooleanFeatureType() {
            StoreFeaturesRequestDto request = new StoreFeaturesRequestDto(
                    "boolean_feature",
                    FeatureType.BOOLEAN,
                    List.of(new StoreFeaturesRequestDto.EntityFeatureValue("e1", true)),
                    null,
                    null
            );

            assertThat(request.featureType()).isEqualTo(FeatureType.BOOLEAN);
        }

        @Test
        @DisplayName("Should accept VECTOR feature type")
        void shouldAcceptVectorFeatureType() {
            List<Double> vector = List.of(0.1, 0.2, 0.3, 0.4);
            StoreFeaturesRequestDto request = new StoreFeaturesRequestDto(
                    "embedding",
                    FeatureType.VECTOR,
                    List.of(new StoreFeaturesRequestDto.EntityFeatureValue("e1", vector)),
                    null,
                    null
            );

            assertThat(request.featureType()).isEqualTo(FeatureType.VECTOR);
        }
    }

    @Nested
    @DisplayName("Metadata Tests")
    class MetadataTests {

        @Test
        @DisplayName("Should store metadata map")
        void shouldStoreMetadataMap() {
            Map<String, Object> metadata = Map.of(
                    "source", "user_input",
                    "version", "1.0",
                    "tags", List.of("ml", "feature")
            );

            StoreFeaturesRequestDto request = new StoreFeaturesRequestDto(
                    "feature",
                    FeatureType.NUMERIC,
                    List.of(new StoreFeaturesRequestDto.EntityFeatureValue("e1", 1)),
                    null,
                    metadata
            );

            assertThat(request.metadata()).isEqualTo(metadata);
            assertThat(request.metadata()).hasSize(3);
        }

        @Test
        @DisplayName("Should handle empty metadata")
        void shouldHandleEmptyMetadata() {
            StoreFeaturesRequestDto request = new StoreFeaturesRequestDto(
                    "feature",
                    FeatureType.TEXT,
                    List.of(new StoreFeaturesRequestDto.EntityFeatureValue("e1", "val")),
                    null,
                    Map.of()
            );

            assertThat(request.metadata()).isEmpty();
        }

        @Test
        @DisplayName("Should handle null metadata")
        void shouldHandleNullMetadata() {
            StoreFeaturesRequestDto request = new StoreFeaturesRequestDto(
                    "feature",
                    FeatureType.BOOLEAN,
                    List.of(new StoreFeaturesRequestDto.EntityFeatureValue("e1", true)),
                    null,
                    null
            );

            assertThat(request.metadata()).isNull();
        }
    }

    @Nested
    @DisplayName("Multiple Values Tests")
    class MultipleValuesTests {

        @Test
        @DisplayName("Should store multiple entity values")
        void shouldStoreMultipleEntityValues() {
            List<StoreFeaturesRequestDto.EntityFeatureValue> values = List.of(
                    new StoreFeaturesRequestDto.EntityFeatureValue("entity-1", 10.0),
                    new StoreFeaturesRequestDto.EntityFeatureValue("entity-2", 20.0),
                    new StoreFeaturesRequestDto.EntityFeatureValue("entity-3", 30.0)
            );

            StoreFeaturesRequestDto request = new StoreFeaturesRequestDto(
                    "multi_entity_feature",
                    FeatureType.NUMERIC,
                    values,
                    null,
                    null
            );

            assertThat(request.values()).hasSize(3);
        }

        @Test
        @DisplayName("Should preserve entity value order")
        void shouldPreserveEntityValueOrder() {
            List<StoreFeaturesRequestDto.EntityFeatureValue> values = List.of(
                    new StoreFeaturesRequestDto.EntityFeatureValue("entity-1", "first"),
                    new StoreFeaturesRequestDto.EntityFeatureValue("entity-2", "second"),
                    new StoreFeaturesRequestDto.EntityFeatureValue("entity-3", "third")
            );

            StoreFeaturesRequestDto request = new StoreFeaturesRequestDto(
                    "ordered_feature",
                    FeatureType.TEXT,
                    values,
                    null,
                    null
            );

            assertThat(request.values().get(0).entityId()).isEqualTo("entity-1");
            assertThat(request.values().get(1).entityId()).isEqualTo("entity-2");
            assertThat(request.values().get(2).entityId()).isEqualTo("entity-3");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle special characters in feature name")
        void shouldHandleSpecialCharactersInFeatureName() {
            StoreFeaturesRequestDto request = new StoreFeaturesRequestDto(
                    "feature-with_special.chars",
                    FeatureType.TEXT,
                    List.of(new StoreFeaturesRequestDto.EntityFeatureValue("e1", "val")),
                    null,
                    null
            );

            assertThat(request.featureName()).contains("-_");
        }

        @Test
        @DisplayName("Should handle unicode in feature name")
        void shouldHandleUnicodeInFeatureName() {
            StoreFeaturesRequestDto request = new StoreFeaturesRequestDto(
                    "特性-名称",
                    FeatureType.TEXT,
                    List.of(new StoreFeaturesRequestDto.EntityFeatureValue("e1", "val")),
                    null,
                    null
            );

            assertThat(request.featureName()).isEqualTo("特性-名称");
        }

        @Test
        @DisplayName("Should handle long description")
        void shouldHandleLongDescription() {
            String longDescription = "a".repeat(500);

            StoreFeaturesRequestDto request = new StoreFeaturesRequestDto(
                    "feature",
                    FeatureType.TEXT,
                    List.of(new StoreFeaturesRequestDto.EntityFeatureValue("e1", "val")),
                    longDescription,
                    null
            );

            assertThat(request.description()).hasSize(500);
        }
    }

    @Nested
    @DisplayName("Record Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal when all fields match")
        void shouldBeEqualWhenAllFieldsMatch() {
            StoreFeaturesRequestDto.EntityFeatureValue value =
                    new StoreFeaturesRequestDto.EntityFeatureValue("e1", 1);

            StoreFeaturesRequestDto request1 = new StoreFeaturesRequestDto(
                    "feature", FeatureType.NUMERIC, List.of(value), "desc", Map.of("k", "v")
            );
            StoreFeaturesRequestDto request2 = new StoreFeaturesRequestDto(
                    "feature", FeatureType.NUMERIC, List.of(value), "desc", Map.of("k", "v")
            );

            assertThat(request1).isEqualTo(request2);
            assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when feature names differ")
        void shouldNotBeEqualWhenFeatureNamesDiffer() {
            StoreFeaturesRequestDto.EntityFeatureValue value =
                    new StoreFeaturesRequestDto.EntityFeatureValue("e1", 1);

            StoreFeaturesRequestDto request1 = new StoreFeaturesRequestDto(
                    "feature1", FeatureType.NUMERIC, List.of(value), null, null
            );
            StoreFeaturesRequestDto request2 = new StoreFeaturesRequestDto(
                    "feature2", FeatureType.NUMERIC, List.of(value), null, null
            );

            assertThat(request1).isNotEqualTo(request2);
        }
    }
}
