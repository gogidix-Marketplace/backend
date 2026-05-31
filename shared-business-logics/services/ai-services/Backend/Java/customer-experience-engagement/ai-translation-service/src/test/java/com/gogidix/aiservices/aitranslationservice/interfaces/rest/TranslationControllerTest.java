package com.gogidix.aiservices.aitranslationservice.interfaces.rest;

import com.gogidix.aiservices.aitranslationservice.application.service.TranslationService;
import com.gogidix.aiservices.aitranslationservice.domain.model.Language;
import com.gogidix.aiservices.aitranslationservice.domain.model.TranslationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TranslationController.class)
@DisplayName("TranslationController REST API Tests")
class TranslationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TranslationService translationService;

    private TranslationResult mockResult;

    @BeforeEach
    void setUp() {
        mockResult = TranslationResult.builder()
                .sourceText("Hello")
                .translatedText("Hola")
                .sourceLanguage(Language.ENGLISH)
                .targetLanguage(Language.SPANISH)
                .confidence(0.95)
                .build();
    }

    @Nested
    @DisplayName("POST /api/v1/translation/translate - Translation with target only")
    class TranslateWithTargetTests {

        @Test
        @DisplayName("Should translate text to target language")
        void shouldTranslateToTarget() throws Exception {
            when(translationService.translate(eq("Hello"), eq(Language.SPANISH)))
                    .thenReturn(mockResult);

            mockMvc.perform(post("/api/v1/translation/translate")
                            .param("text", "Hello")
                            .param("target", "SPANISH"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.sourceText").value("Hello"))
                    .andExpect(jsonPath("$.translatedText").value("Hola"))
                    .andExpect(jsonPath("$.sourceLanguage").value("ENGLISH"))
                    .andExpect(jsonPath("$.targetLanguage").value("SPANISH"));

            verify(translationService).translate("Hello", Language.SPANISH);
        }

        @Test
        @DisplayName("Should translate to French")
        void shouldTranslateToFrench() throws Exception {
            TranslationResult frenchResult = TranslationResult.builder()
                    .sourceText("Hello")
                    .translatedText("Bonjour")
                    .sourceLanguage(Language.ENGLISH)
                    .targetLanguage(Language.FRENCH)
                    .confidence(0.98)
                    .build();

            when(translationService.translate(eq("Hello"), eq(Language.FRENCH)))
                    .thenReturn(frenchResult);

            mockMvc.perform(post("/api/v1/translation/translate")
                            .param("text", "Hello")
                            .param("target", "FRENCH"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.translatedText").value("Bonjour"))
                    .andExpect(jsonPath("$.targetLanguage").value("FRENCH"));
        }

        @Test
        @DisplayName("Should translate to German")
        void shouldTranslateToGerman() throws Exception {
            TranslationResult germanResult = TranslationResult.builder()
                    .sourceText("Hello")
                    .translatedText("Hallo")
                    .sourceLanguage(Language.ENGLISH)
                    .targetLanguage(Language.GERMAN)
                    .confidence(0.95)
                    .build();

            when(translationService.translate(eq("Hello"), eq(Language.GERMAN)))
                    .thenReturn(germanResult);

            mockMvc.perform(post("/api/v1/translation/translate")
                            .param("text", "Hello")
                            .param("target", "GERMAN"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.targetLanguage").value("GERMAN"));
        }
    }

    @Nested
    @DisplayName("POST /api/v1/translation/translate-with-source - Translation with source and target")
    class TranslateWithSourceAndTargetTests {

        @Test
        @DisplayName("Should translate with source and target specified")
        void shouldTranslateWithSourceAndTarget() throws Exception {
            when(translationService.translate(eq("Hello"), eq(Language.ENGLISH), eq(Language.SPANISH)))
                    .thenReturn(mockResult);

            mockMvc.perform(post("/api/v1/translation/translate-with-source")
                            .param("text", "Hello")
                            .param("source", "ENGLISH")
                            .param("target", "SPANISH"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.translatedText").exists());

            verify(translationService).translate("Hello", Language.ENGLISH, Language.SPANISH);
        }

        @Test
        @DisplayName("Should handle English to Spanish translation")
        void shouldHandleEnglishToSpanish() throws Exception {
            when(translationService.translate(anyString(), eq(Language.ENGLISH), eq(Language.SPANISH)))
                    .thenReturn(mockResult);

            mockMvc.perform(post("/api/v1/translation/translate-with-source")
                            .param("text", "Good morning")
                            .param("source", "ENGLISH")
                            .param("target", "SPANISH"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should handle Spanish to English translation")
        void shouldHandleSpanishToEnglish() throws Exception {
            TranslationResult reverseResult = TranslationResult.builder()
                    .sourceText("Hola")
                    .translatedText("Hello")
                    .sourceLanguage(Language.SPANISH)
                    .targetLanguage(Language.ENGLISH)
                    .confidence(0.92)
                    .build();

            when(translationService.translate(eq("Hola"), eq(Language.SPANISH), eq(Language.ENGLISH)))
                    .thenReturn(reverseResult);

            mockMvc.perform(post("/api/v1/translation/translate-with-source")
                            .param("text", "Hola")
                            .param("source", "SPANISH")
                            .param("target", "ENGLISH"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.translatedText").value("Hello"));
        }
    }

    @Nested
    @DisplayName("POST /api/v1/translation/detect - Language Detection")
    class DetectLanguageTests {

        @Test
        @DisplayName("Should detect English text")
        void shouldDetectEnglish() throws Exception {
            when(translationService.detectLanguage("Hello world"))
                    .thenReturn(Language.ENGLISH);

            mockMvc.perform(post("/api/v1/translation/detect")
                            .param("text", "Hello world"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value("ENGLISH"));

            verify(translationService).detectLanguage("Hello world");
        }

        @Test
        @DisplayName("Should detect Spanish text")
        void shouldDetectSpanish() throws Exception {
            when(translationService.detectLanguage("Hola mundo"))
                    .thenReturn(Language.SPANISH);

            mockMvc.perform(post("/api/v1/translation/detect")
                            .param("text", "Hola mundo"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value("SPANISH"));
        }

        @Test
        @DisplayName("Should detect French text")
        void shouldDetectFrench() throws Exception {
            when(translationService.detectLanguage("Bonjour"))
                    .thenReturn(Language.FRENCH);

            mockMvc.perform(post("/api/v1/translation/detect")
                            .param("text", "Bonjour"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value("FRENCH"));
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle empty text")
        void shouldHandleEmptyText() throws Exception {
            when(translationService.translate(eq(""), any(Language.class)))
                    .thenReturn(mockResult);

            mockMvc.perform(post("/api/v1/translation/translate")
                            .param("text", "")
                            .param("target", "SPANISH"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should handle special characters")
        void shouldHandleSpecialCharacters() throws Exception {
            String specialText = "Hello! @#$%^&*()";

            when(translationService.translate(eq(specialText), any(Language.class)))
                    .thenReturn(mockResult);

            mockMvc.perform(post("/api/v1/translation/translate")
                            .param("text", specialText)
                            .param("target", "SPANISH"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should handle very long text")
        void shouldHandleLongText() throws Exception {
            String longText = "A".repeat(1000);

            when(translationService.translate(eq(longText), any(Language.class)))
                    .thenReturn(mockResult);

            mockMvc.perform(post("/api/v1/translation/translate")
                            .param("text", longText)
                            .param("target", "SPANISH"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should handle unicode characters")
        void shouldHandleUnicode() throws Exception {
            String unicodeText = "Hello 世界 🌍";

            when(translationService.translate(eq(unicodeText), any(Language.class)))
                    .thenReturn(mockResult);

            mockMvc.perform(post("/api/v1/translation/translate")
                            .param("text", unicodeText)
                            .param("target", "SPANISH"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Response Format Tests")
    class ResponseFormatTests {

        @Test
        @DisplayName("Should return proper JSON structure for translation result")
        void shouldReturnProperJsonStructure() throws Exception {
            when(translationService.translate(anyString(), any(Language.class)))
                    .thenReturn(mockResult);

            mockMvc.perform(post("/api/v1/translation/translate")
                            .param("text", "Hello")
                            .param("target", "SPANISH"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.sourceText").exists())
                    .andExpect(jsonPath("$.translatedText").exists())
                    .andExpect(jsonPath("$.sourceLanguage").exists())
                    .andExpect(jsonPath("$.targetLanguage").exists())
                    .andExpect(jsonPath("$.confidence").exists());
        }

        @Test
        @DisplayName("Should include confidence score in response")
        void shouldIncludeConfidenceScore() throws Exception {
            when(translationService.translate(anyString(), any(Language.class)))
                    .thenReturn(mockResult);

            mockMvc.perform(post("/api/v1/translation/translate")
                            .param("text", "Hello")
                            .param("target", "SPANISH"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.confidence").value(0.95));
        }
    }
}
