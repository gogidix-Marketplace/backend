package com.gogidix.aiservices.aifeatureextractionservice.application.dto;

import com.gogidix.aiservices.aifeatureextractionservice.domain.model.ExtractionMethod;
import com.gogidix.aiservices.aifeatureextractionservice.domain.model.FeatureExtractionStatus;
import com.gogidix.aiservices.aifeatureextractionservice.domain.model.FeatureValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for FeatureSetResponseDto.
 */
@DisplayName("FeatureSetResponseDto Tests")
class FeatureSetResponseDtoTest {

    private static final String FEATURE_SET_ID = "fs-123";
    private static final String DATA_SOURCE = "user-data";
    private static final List<ExtractionMethod> METHODS = List.of(ExtractionMethod.TFIDF);
    private static final List<FeatureValue> FEATURES = List.of(
            FeatureValue.numeric("feature1", 1.0),
            FeatureValue.numeric("feature2", 2.0)
    );

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create response DTO with all parameters")
        void shouldCreateResponseDtoWithAllParameters() {
            Instant now = Instant.now();
            FeatureSetResponseDto dto = new FeatureSetResponseDto(
                    FEATURE_SET_ID,
                    DATA_SOURCE,
                    FeatureExtractionStatus.COMPLETED,
                    METHODS,
                    FEATURES,
                    2,
                    true,
                    null,
                    now,
                    now
            );

            assertEquals(FEATURE_SET_ID, dto.featureSetId());
            assertEquals(DATA_SOURCE, dto.dataSource());
            assertEquals(FeatureExtractionStatus.COMPLETED, dto.status());
            assertEquals(1, dto.extractionMethods().size());
            assertEquals(2, dto.features().size());
            assertEquals(2, dto.featureCount());
            assertTrue(dto.normalized());
            assertNull(dto.errorMessage());
        }

        @Test
        @DisplayName("Should create response DTO with error status")
        void shouldCreateResponseDtoWithErrorStatus() {
            FeatureSetResponseDto dto = new FeatureSetResponseDto(
                    FEATURE_SET_ID,
                    DATA_SOURCE,
                    FeatureExtractionStatus.FAILED,
                    METHODS,
                    List.of(),
                    0,
                    true,
                    "Extraction failed",
                    Instant.now(),
                    Instant.now()
            );

            assertEquals(FeatureExtractionStatus.FAILED, dto.status());
            assertEquals("Extraction failed", dto.errorMessage());
            assertEquals(0, dto.features().size());
        }
    }

    @Nested
    @DisplayName("Record Tests")
    class RecordTests {

        @Test
        @DisplayName("Should implement equals correctly")
        void shouldImplementEqualsCorrectly() {
            Instant now = Instant.now();
            FeatureSetResponseDto dto1 = new FeatureSetResponseDto(
                    FEATURE_SET_ID,
                    DATA_SOURCE,
                    FeatureExtractionStatus.COMPLETED,
                    METHODS,
                    FEATURES,
                    2,
                    true,
                    null,
                    now,
                    now
            );
            FeatureSetResponseDto dto2 = new FeatureSetResponseDto(
                    FEATURE_SET_ID,
                    DATA_SOURCE,
                    FeatureExtractionStatus.COMPLETED,
                    METHODS,
                    FEATURES,
                    2,
                    true,
                    null,
                    now,
                    now
            );

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Should not be equal with different status")
        void shouldNotBeEqualWithDifferentStatus() {
            Instant now = Instant.now();
            FeatureSetResponseDto dto1 = new FeatureSetResponseDto(
                    FEATURE_SET_ID,
                    DATA_SOURCE,
                    FeatureExtractionStatus.COMPLETED,
                    METHODS,
                    FEATURES,
                    2,
                    true,
                    null,
                    now,
                    now
            );
            FeatureSetResponseDto dto2 = new FeatureSetResponseDto(
                    FEATURE_SET_ID,
                    DATA_SOURCE,
                    FeatureExtractionStatus.PENDING,
                    METHODS,
                    FEATURES,
                    2,
                    true,
                    null,
                    now,
                    now
            );

            assertNotEquals(dto1, dto2);
        }
    }
}
