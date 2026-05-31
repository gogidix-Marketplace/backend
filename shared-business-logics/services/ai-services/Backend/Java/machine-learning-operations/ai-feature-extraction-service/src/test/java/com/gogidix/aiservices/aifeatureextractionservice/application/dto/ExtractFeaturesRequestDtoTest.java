package com.gogidix.aiservices.aifeatureextractionservice.application.dto;

import com.gogidix.aiservices.aifeatureextractionservice.domain.model.ExtractionMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ExtractFeaturesRequestDto.
 */
@DisplayName("ExtractFeaturesRequestDto Tests")
class ExtractFeaturesRequestDtoTest {

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {

        @Test
        @DisplayName("Should create request DTO with valid parameters")
        void shouldCreateRequestDtoWithValidParameters() {
            ExtractFeaturesRequestDto dto = new ExtractFeaturesRequestDto(
                    "data-source",
                    List.of("feature1", "feature2"),
                    List.of(ExtractionMethod.TFIDF),
                    true
            );

            assertEquals("data-source", dto.dataSource());
            assertEquals(2, dto.features().size());
            assertEquals(ExtractionMethod.TFIDF, dto.methods().get(0));
            assertTrue(dto.normalize());
        }

        @Test
        @DisplayName("Should support multiple extraction methods")
        void shouldSupportMultipleExtractionMethods() {
            ExtractFeaturesRequestDto dto = new ExtractFeaturesRequestDto(
                    "data-source",
                    List.of("feature1"),
                    List.of(ExtractionMethod.TFIDF, ExtractionMethod.PCA),
                    false
            );

            assertEquals(2, dto.methods().size());
            assertFalse(dto.normalize());
        }
    }

    @Nested
    @DisplayName("Record Tests")
    class RecordTests {

        @Test
        @DisplayName("Should implement equals correctly")
        void shouldImplementEqualsCorrectly() {
            ExtractFeaturesRequestDto dto1 = new ExtractFeaturesRequestDto(
                    "data-source",
                    List.of("feature1"),
                    List.of(ExtractionMethod.TFIDF),
                    true
            );
            ExtractFeaturesRequestDto dto2 = new ExtractFeaturesRequestDto(
                    "data-source",
                    List.of("feature1"),
                    List.of(ExtractionMethod.TFIDF),
                    true
            );

            assertEquals(dto1, dto2);
            assertEquals(dto1.hashCode(), dto2.hashCode());
        }

        @Test
        @DisplayName("Should implement toString")
        void shouldImplementToString() {
            ExtractFeaturesRequestDto dto = new ExtractFeaturesRequestDto(
                    "data-source",
                    List.of("feature1"),
                    List.of(ExtractionMethod.TFIDF),
                    true
            );

            String toString = dto.toString();
            assertTrue(toString.contains("data-source"));
            assertTrue(toString.contains("feature1"));
        }
    }
}
