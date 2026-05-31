package com.gogidix.aiservices.aitranslationservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Translation Domain Model Tests")
class LanguageTest {

    @Nested
    @DisplayName("Language Enum Tests")
    class LanguageTests {

        @ParameterizedTest
        @EnumSource(Language.class)
        @DisplayName("Should have valid language codes")
        void shouldHaveValidCodes(Language language) {
            assertThat(language.getCode()).isNotNull();
            assertThat(language.getName()).isNotNull();
        }

        @Test
        @DisplayName("Should find language by code")
        void shouldFindLanguageByCode() {
            assertThat(Language.fromCode("en")).isEqualTo(Language.ENGLISH);
            assertThat(Language.fromCode("es")).isEqualTo(Language.SPANISH);
            assertThat(Language.fromCode("fr")).isEqualTo(Language.FRENCH);
        }

        @Test
        @DisplayName("Should throw exception for invalid code")
        void shouldThrowForInvalidCode() {
            assertThatThrownBy(() -> Language.fromCode("xx"))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("TranslationResult Tests")
    class ResultTests {

        @Test
        @DisplayName("Should create translation result")
        void shouldCreateResult() {
            TranslationResult result = TranslationResult.builder()
                    .translationId("trans-1")
                    .sourceText("Hello")
                    .translatedText("Hola")
                    .sourceLanguage(Language.ENGLISH)
                    .targetLanguage(Language.SPANISH)
                    .confidence(0.95)
                    .createdAt(Instant.now())
                    .characterCount(5)
                    .build();

            assertThat(result).isNotNull();
            assertThat(result.getSourceText()).isEqualTo("Hello");
            assertThat(result.getTranslatedText()).isEqualTo("Hola");
        }
    }
}
