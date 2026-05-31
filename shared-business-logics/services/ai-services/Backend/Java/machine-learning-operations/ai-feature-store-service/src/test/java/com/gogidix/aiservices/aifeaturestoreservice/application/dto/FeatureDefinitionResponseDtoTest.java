package com.gogidix.aiservices.aifeaturestoreservice.application.dto;

import com.gogidix.aiservices.aifeaturestoreservice.domain.model.FeatureMetadata;
import com.gogidix.aiservices.aifeaturestoreservice.domain.model.FeatureType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FeatureDefinitionResponseDto Tests")
class FeatureDefinitionResponseDtoTest {

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create with all fields")
        void shouldCreateWithAllFields() {
            Instant now = Instant.now();
            List<FeatureMetadata> metadata = List.of(
                    new FeatureMetadata("source", "user_input"),
                    new FeatureMetadata("tags", List.of("ml", "feature"))
            );

            FeatureDefinitionResponseDto dto = new FeatureDefinitionResponseDto(
                    "user_age",
                    FeatureType.NUMERIC,
                    "Age of the user",
                    "v1.0.0",
                    metadata,
                    now.minusSeconds(3600),
                    now
            );

            assertThat(dto.featureName()).isEqualTo("user_age");
            assertThat(dto.featureType()).isEqualTo(FeatureType.NUMERIC);
            assertThat(dto.description()).isEqualTo("Age of the user");
            assertThat(dto.version()).isEqualTo("v1.0.0");
            assertThat(dto.metadata()).hasSize(2);
            assertThat(dto.createdAt()).isEqualTo(now.minusSeconds(3600));
            assertThat(dto.updatedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should create with minimal required fields")
        void shouldCreateWithMinimalFields() {
            Instant now = Instant.now();

            FeatureDefinitionResponseDto dto = new FeatureDefinitionResponseDto(
                    "feature_name",
                    FeatureType.TEXT,
                    null,
                    "v1.0",
                    List.of(),
                    now,
                    now
            );

            assertThat(dto.featureName()).isEqualTo("feature_name");
            assertThat(dto.featureType()).isEqualTo(FeatureType.TEXT);
            assertThat(dto.description()).isNull();
            assertThat(dto.metadata()).isEmpty();
        }
    }

    @Nested
    @DisplayName("FeatureType Support Tests")
    class FeatureTypeSupportTests {

        @Test
        @DisplayName("Should represent NUMERIC feature definition")
        void shouldRepresentNumericFeatureDefinition() {
            FeatureDefinitionResponseDto dto = new FeatureDefinitionResponseDto(
                    "price",
                    FeatureType.NUMERIC,
                    "Product price",
                    "v1.0",
                    List.of(),
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.featureType()).isEqualTo(FeatureType.NUMERIC);
        }

        @Test
        @DisplayName("Should represent CATEGORICAL feature definition")
        void shouldRepresentCategoricalFeatureDefinition() {
            FeatureDefinitionResponseDto dto = new FeatureDefinitionResponseDto(
                    "category",
                    FeatureType.CATEGORICAL,
                    "Product category",
                    "v1.0",
                    List.of(),
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.featureType()).isEqualTo(FeatureType.CATEGORICAL);
        }

        @Test
        @DisplayName("Should represent TEXT feature definition")
        void shouldRepresentTextFeatureDefinition() {
            FeatureDefinitionResponseDto dto = new FeatureDefinitionResponseDto(
                    "description",
                    FeatureType.TEXT,
                    "Product description",
                    "v1.0",
                    List.of(),
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.featureType()).isEqualTo(FeatureType.TEXT);
        }

        @Test
        @DisplayName("Should represent BOOLEAN feature definition")
        void shouldRepresentBooleanFeatureDefinition() {
            FeatureDefinitionResponseDto dto = new FeatureDefinitionResponseDto(
                    "is_active",
                    FeatureType.BOOLEAN,
                    "Active status",
                    "v1.0",
                    List.of(),
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.featureType()).isEqualTo(FeatureType.BOOLEAN);
        }

        @Test
        @DisplayName("Should represent VECTOR feature definition")
        void shouldRepresentVectorFeatureDefinition() {
            FeatureDefinitionResponseDto dto = new FeatureDefinitionResponseDto(
                    "embedding",
                    FeatureType.VECTOR,
                    "Vector embedding",
                    "v1.0",
                    List.of(),
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.featureType()).isEqualTo(FeatureType.VECTOR);
        }
    }

    @Nested
    @DisplayName("Version Tests")
    class VersionTests {

        @Test
        @DisplayName("Should store semantic version")
        void shouldStoreSemanticVersion() {
            FeatureDefinitionResponseDto dto = new FeatureDefinitionResponseDto(
                    "feature",
                    FeatureType.NUMERIC,
                    null,
                    "1.2.3-beta",
                    List.of(),
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.version()).isEqualTo("1.2.3-beta");
        }

        @Test
        @DisplayName("Should store custom version format")
        void shouldStoreCustomVersionFormat() {
            FeatureDefinitionResponseDto dto = new FeatureDefinitionResponseDto(
                    "feature",
                    FeatureType.TEXT,
                    null,
                    "20240315-001",
                    List.of(),
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.version()).isEqualTo("20240315-001");
        }
    }

    @Nested
    @DisplayName("Timestamp Tests")
    class TimestampTests {

        @Test
        @DisplayName("Should store creation and update timestamps")
        void shouldStoreCreationAndUpdateTimestamps() {
            Instant created = Instant.now().minusSeconds(86400);
            Instant updated = Instant.now();

            FeatureDefinitionResponseDto dto = new FeatureDefinitionResponseDto(
                    "feature",
                    FeatureType.NUMERIC,
                    null,
                    "v1.0",
                    List.of(),
                    created,
                    updated
            );

            assertThat(dto.createdAt()).isEqualTo(created);
            assertThat(dto.updatedAt()).isEqualTo(updated);
        }

        @Test
        @DisplayName("Should allow same created and updated time")
        void shouldAllowSameCreatedAndUpdatedTime() {
            Instant now = Instant.now();

            FeatureDefinitionResponseDto dto = new FeatureDefinitionResponseDto(
                    "feature",
                    FeatureType.NUMERIC,
                    null,
                    "v1.0",
                    List.of(),
                    now,
                    now
            );

            assertThat(dto.createdAt()).isEqualTo(dto.updatedAt());
        }
    }

    @Nested
    @DisplayName("Metadata Tests")
    class MetadataTests {

        @Test
        @DisplayName("Should store feature metadata list")
        void shouldStoreFeatureMetadataList() {
            List<FeatureMetadata> metadata = List.of(
                    new FeatureMetadata("source", "database"),
                    new FeatureMetadata("owner", "data-team"),
                    new FeatureMetadata("pii", false)
            );

            FeatureDefinitionResponseDto dto = new FeatureDefinitionResponseDto(
                    "feature",
                    FeatureType.TEXT,
                    null,
                    "v1.0",
                    metadata,
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.metadata()).hasSize(3);
        }

        @Test
        @DisplayName("Should handle empty metadata")
        void shouldHandleEmptyMetadata() {
            FeatureDefinitionResponseDto dto = new FeatureDefinitionResponseDto(
                    "feature",
                    FeatureType.NUMERIC,
                    null,
                    "v1.0",
                    List.of(),
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.metadata()).isEmpty();
        }

        @Test
        @DisplayName("Should preserve metadata order")
        void shouldPreserveMetadataOrder() {
            List<FeatureMetadata> metadata = List.of(
                    new FeatureMetadata("first", 1),
                    new FeatureMetadata("second", 2),
                    new FeatureMetadata("third", 3)
            );

            FeatureDefinitionResponseDto dto = new FeatureDefinitionResponseDto(
                    "feature",
                    FeatureType.NUMERIC,
                    null,
                    "v1.0",
                    metadata,
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.metadata().get(0).key()).isEqualTo("first");
            assertThat(dto.metadata().get(1).key()).isEqualTo("second");
            assertThat(dto.metadata().get(2).key()).isEqualTo("third");
        }
    }

    @Nested
    @DisplayName("Description Tests")
    class DescriptionTests {

        @Test
        @DisplayName("Should store description")
        void shouldStoreDescription() {
            FeatureDefinitionResponseDto dto = new FeatureDefinitionResponseDto(
                    "feature",
                    FeatureType.TEXT,
                    "This is a detailed description of the feature",
                    "v1.0",
                    List.of(),
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.description()).isEqualTo("This is a detailed description of the feature");
        }

        @Test
        @DisplayName("Should handle null description")
        void shouldHandleNullDescription() {
            FeatureDefinitionResponseDto dto = new FeatureDefinitionResponseDto(
                    "feature",
                    FeatureType.TEXT,
                    null,
                    "v1.0",
                    List.of(),
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.description()).isNull();
        }

        @Test
        @DisplayName("Should handle empty description")
        void shouldHandleEmptyDescription() {
            FeatureDefinitionResponseDto dto = new FeatureDefinitionResponseDto(
                    "feature",
                    FeatureType.TEXT,
                    "",
                    "v1.0",
                    List.of(),
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.description()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Record Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should be equal when all fields match")
        void shouldBeEqualWhenAllFieldsMatch() {
            Instant now = Instant.now();

            FeatureDefinitionResponseDto dto1 = new FeatureDefinitionResponseDto(
                    "feature", FeatureType.NUMERIC, "desc", "v1.0",
                    List.of(new FeatureMetadata("k", "v")), now, now
            );
            FeatureDefinitionResponseDto dto2 = new FeatureDefinitionResponseDto(
                    "feature", FeatureType.NUMERIC, "desc", "v1.0",
                    List.of(new FeatureMetadata("k", "v")), now, now
            );

            assertThat(dto1).isEqualTo(dto2);
            assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when feature names differ")
        void shouldNotBeEqualWhenFeatureNamesDiffer() {
            Instant now = Instant.now();

            FeatureDefinitionResponseDto dto1 = new FeatureDefinitionResponseDto(
                    "feature1", FeatureType.NUMERIC, null, "v1.0",
                    List.of(), now, now
            );
            FeatureDefinitionResponseDto dto2 = new FeatureDefinitionResponseDto(
                    "feature2", FeatureType.NUMERIC, null, "v1.0",
                    List.of(), now, now
            );

            assertThat(dto1).isNotEqualTo(dto2);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle unicode in feature name")
        void shouldHandleUnicodeInFeatureName() {
            FeatureDefinitionResponseDto dto = new FeatureDefinitionResponseDto(
                    "特性-名",
                    FeatureType.TEXT,
                    null,
                    "v1.0",
                    List.of(),
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.featureName()).isEqualTo("特性-名");
        }

        @Test
        @DisplayName("Should handle special characters in feature name")
        void shouldHandleSpecialCharactersInFeatureName() {
            FeatureDefinitionResponseDto dto = new FeatureDefinitionResponseDto(
                    "feature.with-special_chars",
                    FeatureType.TEXT,
                    null,
                    "v1.0",
                    List.of(),
                    Instant.now(),
                    Instant.now()
            );

            assertThat(dto.featureName()).contains("._-");
        }
    }
}
