package com.gogidix.aiservices.multimodalprocessingservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("OutputFormat Domain Model Tests")
class OutputFormatTest {

    @Nested
    @DisplayName("Format Type Tests")
    class FormatTypeTests {

        @ParameterizedTest
        @EnumSource(OutputFormat.class)
        @DisplayName("Should have valid format types")
        void shouldHaveValidFormatTypes(OutputFormat format) {
            assertThat(format).isNotNull();
            assertThat(format.name()).isIn("EMBEDDING", "SUMMARY", "TAGS", "FULL");
        }

        @Test
        @DisplayName("Should define EMBEDDING format")
        void shouldDefineEmbeddingFormat() {
            assertThat(OutputFormat.EMBEDDING).isNotNull();
            assertThat(OutputFormat.EMBEDDING.name()).isEqualTo("EMBEDDING");
        }

        @Test
        @DisplayName("Should define SUMMARY format")
        void shouldDefineSummaryFormat() {
            assertThat(OutputFormat.SUMMARY).isNotNull();
            assertThat(OutputFormat.SUMMARY.name()).isEqualTo("SUMMARY");
        }

        @Test
        @DisplayName("Should define TAGS format")
        void shouldDefineTagsFormat() {
            assertThat(OutputFormat.TAGS).isNotNull();
            assertThat(OutputFormat.TAGS.name()).isEqualTo("TAGS");
        }

        @Test
        @DisplayName("Should define FULL format")
        void shouldDefineFullFormat() {
            assertThat(OutputFormat.FULL).isNotNull();
            assertThat(OutputFormat.FULL.name()).isEqualTo("FULL");
        }
    }

    @Nested
    @DisplayName("Format Parsing Tests")
    class FormatParsingTests {

        @Test
        @DisplayName("Should parse EMBEDDING from string")
        void shouldParseEmbeddingFromString() {
            assertThat(OutputFormat.fromString("EMBEDDING")).isEqualTo(OutputFormat.EMBEDDING);
            assertThat(OutputFormat.fromString("embedding")).isEqualTo(OutputFormat.EMBEDDING);
            assertThat(OutputFormat.fromString("Embedding")).isEqualTo(OutputFormat.EMBEDDING);
        }

        @Test
        @DisplayName("Should parse SUMMARY from string")
        void shouldParseSummaryFromString() {
            assertThat(OutputFormat.fromString("SUMMARY")).isEqualTo(OutputFormat.SUMMARY);
            assertThat(OutputFormat.fromString("summary")).isEqualTo(OutputFormat.SUMMARY);
        }

        @Test
        @DisplayName("Should parse TAGS from string")
        void shouldParseTagsFromString() {
            assertThat(OutputFormat.fromString("TAGS")).isEqualTo(OutputFormat.TAGS);
            assertThat(OutputFormat.fromString("tags")).isEqualTo(OutputFormat.TAGS);
        }

        @Test
        @DisplayName("Should parse FULL from string")
        void shouldParseFullFromString() {
            assertThat(OutputFormat.fromString("FULL")).isEqualTo(OutputFormat.FULL);
            assertThat(OutputFormat.fromString("full")).isEqualTo(OutputFormat.FULL);
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "   ", "invalid", "unknown", "JSON", "XML"})
        @DisplayName("Should return EMBEDDING for invalid string")
        void shouldReturnEmbeddingForInvalidString(String value) {
            assertThat(OutputFormat.fromString(value)).isEqualTo(OutputFormat.EMBEDDING);
        }

        @Test
        @DisplayName("Should return EMBEDDING for null")
        void shouldReturnEmbeddingForNull() {
            assertThat(OutputFormat.fromString(null)).isEqualTo(OutputFormat.EMBEDDING);
        }

        @Test
        @DisplayName("Should be case insensitive")
        void shouldBeCaseInsensitive() {
            assertThat(OutputFormat.fromString("embedding")).isEqualTo(OutputFormat.EMBEDDING);
            assertThat(OutputFormat.fromString("EMBEDDING")).isEqualTo(OutputFormat.EMBEDDING);
            assertThat(OutputFormat.fromString("EmBedDiNg")).isEqualTo(OutputFormat.EMBEDDING);
        }

        @Test
        @DisplayName("Should handle whitespace")
        void shouldHandleWhitespace() {
            assertThat(OutputFormat.fromString("  embedding  ")).isEqualTo(OutputFormat.EMBEDDING);
            assertThat(OutputFormat.fromString("\tSUMMARY\n")).isEqualTo(OutputFormat.SUMMARY);
        }
    }

    @Nested
    @DisplayName("Format Semantics Tests")
    class FormatSemanticsTests {

        @Test
        @DisplayName("EMBEDDING should represent vector output")
        void embeddingShouldRepresentVectorOutput() {
            OutputFormat format = OutputFormat.EMBEDDING;

            assertThat(format.name()).isEqualTo("EMBEDDING");
            assertThat(format).isNotNull();
        }

        @Test
        @DisplayName("SUMMARY should represent text summary")
        void summaryShouldRepresentTextSummary() {
            OutputFormat format = OutputFormat.SUMMARY;

            assertThat(format.name()).isEqualTo("SUMMARY");
        }

        @Test
        @DisplayName("TAGS should represent keyword extraction")
        void tagsShouldRepresentKeywordExtraction() {
            OutputFormat format = OutputFormat.TAGS;

            assertThat(format.name()).isEqualTo("TAGS");
        }

        @Test
        @DisplayName("FULL should represent comprehensive output")
        void fullShouldRepresentComprehensiveOutput() {
            OutputFormat format = OutputFormat.FULL;

            assertThat(format.name()).isEqualTo("FULL");
        }
    }

    @Nested
    @DisplayName("Format Comparison Tests")
    class FormatComparisonTests {

        @Test
        @DisplayName("Should be equal when same format")
        void shouldBeEqualWhenSameFormat() {
            assertThat(OutputFormat.EMBEDDING).isEqualTo(OutputFormat.EMBEDDING);
            assertThat(OutputFormat.SUMMARY).isEqualTo(OutputFormat.SUMMARY);
        }

        @Test
        @DisplayName("Should not be equal when different format")
        void shouldNotBeEqualWhenDifferentFormat() {
            assertThat(OutputFormat.EMBEDDING).isNotEqualTo(OutputFormat.SUMMARY);
            assertThat(OutputFormat.TAGS).isNotEqualTo(OutputFormat.FULL);
        }

        @Test
        @DisplayName("Should have consistent ordinal values")
        void shouldHaveConsistentOrdinalValues() {
            OutputFormat[] values = OutputFormat.values();

            assertThat(values).hasSize(4);
            assertThat(values[0].ordinal()).isLessThan(values[1].ordinal());
            assertThat(values[1].ordinal()).isLessThan(values[2].ordinal());
            assertThat(values[2].ordinal()).isLessThan(values[3].ordinal());
        }
    }
}
