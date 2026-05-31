package com.gogidix.aiservices.nlpprocessingservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("LanguageCode Domain Model Tests")
class LanguageCodeTest {

    @Nested
    @DisplayName("Language Detection Tests")
    class LanguageDetectionTests {

        @ParameterizedTest
        @EnumSource(LanguageCode.class)
        @DisplayName("Should have valid language codes")
        void shouldHaveValidLanguageCodes(LanguageCode code) {
            assertThat(code).isNotNull();
            assertThat(code.getIsoCode()).isNotNull();
        }

        @Test
        @DisplayName("Should parse from ISO code")
        void shouldParseFromIsoCode() {
            assertThat(LanguageCode.fromIsoCode("en")).isEqualTo(LanguageCode.EN);
            assertThat(LanguageCode.fromIsoCode("es")).isEqualTo(LanguageCode.ES);
            assertThat(LanguageCode.fromIsoCode("fr")).isEqualTo(LanguageCode.FR);
            assertThat(LanguageCode.fromIsoCode("de")).isEqualTo(LanguageCode.DE);
            assertThat(LanguageCode.fromIsoCode("auto")).isEqualTo(LanguageCode.AUTO);
        }

        @Test
        @DisplayName("Should return UNKNOWN for invalid code")
        void shouldReturnUnknownForInvalid() {
            assertThat(LanguageCode.fromIsoCode("invalid")).isEqualTo(LanguageCode.UNKNOWN);
            assertThat(LanguageCode.fromIsoCode(null)).isEqualTo(LanguageCode.UNKNOWN);
        }

        @Test
        @DisplayName("Should get language name")
        void shouldGetLanguageName() {
            assertThat(LanguageCode.EN.getName()).isEqualTo("English");
            assertThat(LanguageCode.ES.getName()).isEqualTo("Spanish");
            assertThat(LanguageCode.FR.getName()).isEqualTo("French");
            assertThat(LanguageCode.DE.getName()).isEqualTo("German");
        }
    }

    @Nested
    @DisplayName("Text Analysis Tests")
    class TextAnalysisTests {

        @Test
        @DisplayName("Should check if language is supported")
        void shouldCheckIfSupported() {
            assertThat(LanguageCode.EN.isSupported()).isTrue();
            assertThat(LanguageCode.ES.isSupported()).isTrue();
            assertThat(LanguageCode.FR.isSupported()).isTrue();
            assertThat(LanguageCode.DE.isSupported()).isTrue();
        }

        @Test
        @DisplayName("Should get supported languages count")
        void shouldGetSupportedCount() {
            assertThat(LanguageCode.getSupportedCount()).isGreaterThan(50);
        }
    }
}
