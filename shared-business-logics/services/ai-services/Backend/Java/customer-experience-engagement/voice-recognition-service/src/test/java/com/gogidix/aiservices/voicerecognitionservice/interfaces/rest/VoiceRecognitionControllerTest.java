package com.gogidix.aiservices.voicerecognitionservice.interfaces.rest;

import com.gogidix.aiservices.voicerecognitionservice.application.service.VoiceRecognitionService;
import com.gogidix.aiservices.voicerecognitionservice.domain.model.RecognitionResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VoiceRecognitionController.class)
@DisplayName("VoiceRecognitionController REST API Tests")
class VoiceRecognitionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VoiceRecognitionService voiceRecognitionService;

    private RecognitionResult recognitionResult;

    @BeforeEach
    void setUp() {
        recognitionResult = RecognitionResult.builder()
                .recognitionId("rec-123")
                .transcript("Hello, world!")
                .confidence(0.95)
                .language("en")
                .duration(1500)
                .build();
    }

    @Nested
    @DisplayName("POST /api/v1/voice-recognition/recognize - Recognize Audio")
    class RecognizeAudioTests {

        @Test
        @DisplayName("Should recognize audio successfully")
        void shouldRecognizeAudioSuccessfully() throws Exception {
            when(voiceRecognitionService.recognize(any(byte[].class), eq("en"), eq("audio/wav")))
                    .thenReturn(recognitionResult);

            MockMultipartFile audioFile = new MockMultipartFile(
                    "audio",
                    "test.wav",
                    "audio/wav",
                    "fake audio data".getBytes(StandardCharsets.UTF_8)
            );

            mockMvc.perform(multipart("/api/v1/voice-recognition/recognize")
                            .file(audioFile)
                            .param("language", "en"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.recognitionId").exists())
                    .andExpect(jsonPath("$.transcript").value("Hello, world!"))
                    .andExpect(jsonPath("$.confidence").value(0.95))
                    .andExpect(jsonPath("$.language").value("en"))
                    .andExpect(jsonPath("$.duration").value(1500));

            verify(voiceRecognitionService).recognize(any(byte[].class), eq("en"), eq("audio/wav"));
        }

        @Test
        @DisplayName("Should use default language when not specified")
        void shouldUseDefaultLanguage() throws Exception {
            when(voiceRecognitionService.recognize(any(byte[].class), eq("en"), anyString()))
                    .thenReturn(recognitionResult);

            MockMultipartFile audioFile = new MockMultipartFile(
                    "audio",
                    "test.wav",
                    "audio/wav",
                    "audio data".getBytes(StandardCharsets.UTF_8)
            );

            mockMvc.perform(multipart("/api/v1/voice-recognition/recognize")
                            .file(audioFile))
                    .andExpect(status().isOk());

            verify(voiceRecognitionService).recognize(any(byte[].class), eq("en"), anyString());
        }

        @Test
        @DisplayName("Should accept different languages")
        void shouldAcceptDifferentLanguages() throws Exception {
            String[] languages = {"en", "es", "fr", "de", "ja", "zh"};

            for (String language : languages) {
                when(voiceRecognitionService.recognize(any(byte[].class), eq(language), anyString()))
                        .thenReturn(recognitionResult);

                MockMultipartFile audioFile = new MockMultipartFile(
                        "audio",
                        "test.wav",
                        "audio/wav",
                        "data".getBytes(StandardCharsets.UTF_8)
                );

                mockMvc.perform(multipart("/api/v1/voice-recognition/recognize")
                                .file(audioFile)
                                .param("language", language))
                        .andExpect(status().isOk());
            }
        }

        @Test
        @DisplayName("Should accept WAV format")
        void shouldAcceptWavFormat() throws Exception {
            when(voiceRecognitionService.recognize(any(byte[].class), anyString(), eq("audio/wav")))
                    .thenReturn(recognitionResult);

            MockMultipartFile audioFile = new MockMultipartFile(
                    "audio",
                    "test.wav",
                    "audio/wav",
                    "wav data".getBytes(StandardCharsets.UTF_8)
            );

            mockMvc.perform(multipart("/api/v1/voice-recognition/recognize")
                            .file(audioFile))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should accept MP3 format")
        void shouldAcceptMp3Format() throws Exception {
            when(voiceRecognitionService.recognize(any(byte[].class), anyString(), eq("audio/mpeg")))
                    .thenReturn(recognitionResult);

            MockMultipartFile audioFile = new MockMultipartFile(
                    "audio",
                    "test.mp3",
                    "audio/mpeg",
                    "mp3 data".getBytes(StandardCharsets.UTF_8)
            );

            mockMvc.perform(multipart("/api/v1/voice-recognition/recognize")
                            .file(audioFile))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should accept FLAC format")
        void shouldAcceptFlacFormat() throws Exception {
            when(voiceRecognitionService.recognize(any(byte[].class), anyString(), eq("audio/flac")))
                    .thenReturn(recognitionResult);

            MockMultipartFile audioFile = new MockMultipartFile(
                    "audio",
                    "test.flac",
                    "audio/flac",
                    "flac data".getBytes(StandardCharsets.UTF_8)
            );

            mockMvc.perform(multipart("/api/v1/voice-recognition/recognize")
                            .file(audioFile))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should return internal server error on exception")
        void shouldReturnInternalServerErrorOnException() throws Exception {
            when(voiceRecognitionService.recognize(any(byte[].class), anyString(), anyString()))
                    .thenThrow(new RuntimeException("Processing error"));

            MockMultipartFile audioFile = new MockMultipartFile(
                    "audio",
                    "test.wav",
                    "audio/wav",
                    "data".getBytes(StandardCharsets.UTF_8)
            );

            mockMvc.perform(multipart("/api/v1/voice-recognition/recognize")
                            .file(audioFile))
                    .andExpect(status().isInternalServerError());
        }

        @Test
        @DisplayName("Should handle empty audio file")
        void shouldHandleEmptyAudioFile() throws Exception {
            when(voiceRecognitionService.recognize(any(byte[].class), anyString(), anyString()))
                    .thenReturn(recognitionResult);

            MockMultipartFile audioFile = new MockMultipartFile(
                    "audio",
                    "empty.wav",
                    "audio/wav",
                    new byte[0]
            );

            mockMvc.perform(multipart("/api/v1/voice-recognition/recognize")
                            .file(audioFile))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/voice-recognition/recognize-url - Recognize from URL")
    class RecognizeFromUrlTests {

        @Test
        @DisplayName("Should recognize audio from URL successfully")
        void shouldRecognizeFromUrlSuccessfully() throws Exception {
            when(voiceRecognitionService.recognizeFromUrl(eq("https://example.com/audio.wav"), eq("en")))
                    .thenReturn(recognitionResult);

            mockMvc.perform(post("/api/v1/voice-recognition/recognize-url")
                            .param("audioUrl", "https://example.com/audio.wav")
                            .param("language", "en"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.recognitionId").exists())
                    .andExpect(jsonPath("$.transcript").exists())
                    .andExpect(jsonPath("$.confidence").exists());

            verify(voiceRecognitionService).recognizeFromUrl("https://example.com/audio.wav", "en");
        }

        @Test
        @DisplayName("Should use default language for URL recognition")
        void shouldUseDefaultLanguageForUrlRecognition() throws Exception {
            when(voiceRecognitionService.recognizeFromUrl(anyString(), eq("en")))
                    .thenReturn(recognitionResult);

            mockMvc.perform(post("/api/v1/voice-recognition/recognize-url")
                            .param("audioUrl", "https://example.com/audio.wav"))
                    .andExpect(status().isOk());

            verify(voiceRecognitionService).recognizeFromUrl(anyString(), eq("en"));
        }

        @Test
        @DisplayName("Should accept S3 URLs")
        void shouldAcceptS3Urls() throws Exception {
            when(voiceRecognitionService.recognizeFromUrl(eq("s3://bucket/audio.wav"), anyString()))
                    .thenReturn(recognitionResult);

            mockMvc.perform(post("/api/v1/voice-recognition/recognize-url")
                            .param("audioUrl", "s3://bucket/audio.wav"))
                    .andExpect(status().isOk());

            verify(voiceRecognitionService).recognizeFromUrl("s3://bucket/audio.wav", "en");
        }

        @Test
        @DisplayName("Should accept GCS URLs")
        void shouldAcceptGcsUrls() throws Exception {
            when(voiceRecognitionService.recognizeFromUrl(eq("gs://bucket/audio.wav"), anyString()))
                    .thenReturn(recognitionResult);

            mockMvc.perform(post("/api/v1/voice-recognition/recognize-url")
                            .param("audioUrl", "gs://bucket/audio.wav"))
                    .andExpect(status().isOk());

            verify(voiceRecognitionService).recognizeFromUrl("gs://bucket/audio.wav", "en");
        }

        @Test
        @DisplayName("Should accept different languages for URL recognition")
        void shouldAcceptDifferentLanguagesForUrlRecognition() throws Exception {
            String[] languages = {"en", "es", "fr", "de"};

            for (String language : languages) {
                when(voiceRecognitionService.recognizeFromUrl(anyString(), eq(language)))
                        .thenReturn(recognitionResult);

                mockMvc.perform(post("/api/v1/voice-recognition/recognize-url")
                                .param("audioUrl", "https://example.com/audio.wav")
                                .param("language", language))
                        .andExpect(status().isOk());
            }
        }

        @Test
        @DisplayName("Should handle long URLs")
        void shouldHandleLongUrls() throws Exception {
            String longUrl = "https://example.com/" + "a".repeat(200) + "/audio.wav";

            when(voiceRecognitionService.recognizeFromUrl(eq(longUrl), anyString()))
                    .thenReturn(recognitionResult);

            mockMvc.perform(post("/api/v1/voice-recognition/recognize-url")
                            .param("audioUrl", longUrl))
                    .andExpect(status().isOk());

            verify(voiceRecognitionService).recognizeFromUrl(longUrl, "en");
        }
    }

    @Nested
    @DisplayName("RecognitionResult Tests")
    class RecognitionResultTests {

        @Test
        @DisplayName("Should return recognition with all fields")
        void shouldReturnRecognitionWithAllFields() throws Exception {
            RecognitionResult result = RecognitionResult.builder()
                    .recognitionId("rec-xyz-789")
                    .transcript("This is a test transcript")
                    .confidence(0.98)
                    .language("en")
                    .duration(5000)
                    .build();

            when(voiceRecognitionService.recognizeFromUrl(anyString(), anyString()))
                    .thenReturn(result);

            mockMvc.perform(post("/api/v1/voice-recognition/recognize-url")
                            .param("audioUrl", "https://example.com/audio.wav"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.recognitionId").value("rec-xyz-789"))
                    .andExpect(jsonPath("$.transcript").value("This is a test transcript"))
                    .andExpect(jsonPath("$.confidence").value(0.98))
                    .andExpect(jsonPath("$.language").value("en"))
                    .andExpect(jsonPath("$.duration").value(5000));
        }

        @Test
        @DisplayName("Should return high confidence result")
        void shouldReturnHighConfidenceResult() throws Exception {
            RecognitionResult result = RecognitionResult.builder()
                    .recognitionId("rec-123")
                    .transcript("Hello")
                    .confidence(0.99)
                    .language("en")
                    .duration(1000)
                    .build();

            when(voiceRecognitionService.recognize(any(byte[].class), anyString(), anyString()))
                    .thenReturn(result);

            MockMultipartFile audioFile = new MockMultipartFile(
                    "audio", "test.wav", "audio/wav", "data".getBytes()
            );

            mockMvc.perform(multipart("/api/v1/voice-recognition/recognize")
                            .file(audioFile))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.confidence").value(0.99));
        }

        @Test
        @DisplayName("Should return low confidence result")
        void shouldReturnLowConfidenceResult() throws Exception {
            RecognitionResult result = RecognitionResult.builder()
                    .recognitionId("rec-123")
                    .transcript("?")
                    .confidence(0.45)
                    .language("en")
                    .duration(500)
                    .build();

            when(voiceRecognitionService.recognize(any(byte[].class), anyString(), anyString()))
                    .thenReturn(result);

            MockMultipartFile audioFile = new MockMultipartFile(
                    "audio", "test.wav", "audio/wav", "data".getBytes()
            );

            mockMvc.perform(multipart("/api/v1/voice-recognition/recognize")
                            .file(audioFile))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.confidence").value(0.45));
        }

        @Test
        @DisplayName("Should handle very short duration")
        void shouldHandleVeryShortDuration() throws Exception {
            RecognitionResult result = RecognitionResult.builder()
                    .recognitionId("rec-123")
                    .transcript("Hi")
                    .confidence(0.92)
                    .language("en")
                    .duration(100)
                    .build();

            when(voiceRecognitionService.recognize(any(byte[].class), anyString(), anyString()))
                    .thenReturn(result);

            MockMultipartFile audioFile = new MockMultipartFile(
                    "audio", "test.wav", "audio/wav", "data".getBytes()
            );

            mockMvc.perform(multipart("/api/v1/voice-recognition/recognize")
                            .file(audioFile))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.duration").value(100));
        }

        @Test
        @DisplayName("Should handle long duration")
        void shouldHandleLongDuration() throws Exception {
            RecognitionResult result = RecognitionResult.builder()
                    .recognitionId("rec-123")
                    .transcript("Long speech...")
                    .confidence(0.95)
                    .language("en")
                    .duration(300000)
                    .build();

            when(voiceRecognitionService.recognize(any(byte[].class), anyString(), anyString()))
                    .thenReturn(result);

            MockMultipartFile audioFile = new MockMultipartFile(
                    "audio", "test.wav", "audio/wav", "data".getBytes()
            );

            mockMvc.perform(multipart("/api/v1/voice-recognition/recognize")
                            .file(audioFile))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.duration").value(300000));
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle unicode transcript")
        void shouldHandleUnicodeTranscript() throws Exception {
            RecognitionResult result = RecognitionResult.builder()
                    .recognitionId("rec-123")
                    .transcript("こんにちは世界")
                    .confidence(0.95)
                    .language("ja")
                    .duration(2000)
                    .build();

            when(voiceRecognitionService.recognize(any(byte[].class), eq("ja"), anyString()))
                    .thenReturn(result);

            MockMultipartFile audioFile = new MockMultipartFile(
                    "audio", "test.wav", "audio/wav", "data".getBytes()
            );

            mockMvc.perform(multipart("/api/v1/voice-recognition/recognize")
                            .file(audioFile)
                            .param("language", "ja"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.transcript").value("こんにちは世界"));
        }

        @Test
        @DisplayName("Should handle special characters in URL")
        void shouldHandleSpecialCharactersInUrl() throws Exception {
            String url = "https://example.com/audio file-with spaces.wav";

            when(voiceRecognitionService.recognizeFromUrl(eq(url), anyString()))
                    .thenReturn(recognitionResult);

            mockMvc.perform(post("/api/v1/voice-recognition/recognize-url")
                            .param("audioUrl", url))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should handle very long filename")
        void shouldHandleVeryLongFilename() throws Exception {
            String longFilename = "a".repeat(200) + ".wav";

            when(voiceRecognitionService.recognize(any(byte[].class), anyString(), anyString()))
                    .thenReturn(recognitionResult);

            MockMultipartFile audioFile = new MockMultipartFile(
                    "audio",
                    longFilename,
                    "audio/wav",
                    "data".getBytes()
            );

            mockMvc.perform(multipart("/api/v1/voice-recognition/recognize")
                            .file(audioFile))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Language Support Tests")
    class LanguageSupportTests {

        @Test
        @DisplayName("Should support English language")
        void shouldSupportEnglishLanguage() throws Exception {
            when(voiceRecognitionService.recognize(any(byte[].class), eq("en"), anyString()))
                    .thenReturn(recognitionResult);

            MockMultipartFile audioFile = new MockMultipartFile(
                    "audio", "en.wav", "audio/wav", "data".getBytes()
            );

            mockMvc.perform(multipart("/api/v1/voice-recognition/recognize")
                            .file(audioFile)
                            .param("language", "en"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.language").value("en"));
        }

        @Test
        @DisplayName("Should support Spanish language")
        void shouldSupportSpanishLanguage() throws Exception {
            RecognitionResult esResult = RecognitionResult.builder()
                    .recognitionId("rec-123")
                    .transcript("Hola")
                    .confidence(0.95)
                    .language("es")
                    .duration(1000)
                    .build();

            when(voiceRecognitionService.recognize(any(byte[].class), eq("es"), anyString()))
                    .thenReturn(esResult);

            MockMultipartFile audioFile = new MockMultipartFile(
                    "audio", "es.wav", "audio/wav", "data".getBytes()
            );

            mockMvc.perform(multipart("/api/v1/voice-recognition/recognize")
                            .file(audioFile)
                            .param("language", "es"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.language").value("es"));
        }

        @Test
        @DisplayName("Should support French language")
        void shouldSupportFrenchLanguage() throws Exception {
            RecognitionResult frResult = RecognitionResult.builder()
                    .recognitionId("rec-123")
                    .transcript("Bonjour")
                    .confidence(0.95)
                    .language("fr")
                    .duration(1000)
                    .build();

            when(voiceRecognitionService.recognize(any(byte[].class), eq("fr"), anyString()))
                    .thenReturn(frResult);

            MockMultipartFile audioFile = new MockMultipartFile(
                    "audio", "fr.wav", "audio/wav", "data".getBytes()
            );

            mockMvc.perform(multipart("/api/v1/voice-recognition/recognize")
                            .file(audioFile)
                            .param("language", "fr"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.language").value("fr"));
        }

        @Test
        @DisplayName("Should support German language")
        void shouldSupportGermanLanguage() throws Exception {
            RecognitionResult deResult = RecognitionResult.builder()
                    .recognitionId("rec-123")
                    .transcript("Guten Tag")
                    .confidence(0.95)
                    .language("de")
                    .duration(1000)
                    .build();

            when(voiceRecognitionService.recognize(any(byte[].class), eq("de"), anyString()))
                    .thenReturn(deResult);

            MockMultipartFile audioFile = new MockMultipartFile(
                    "audio", "de.wav", "audio/wav", "data".getBytes()
            );

            mockMvc.perform(multipart("/api/v1/voice-recognition/recognize")
                            .file(audioFile)
                            .param("language", "de"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.language").value("de"));
        }

        @Test
        @DisplayName("Should support Japanese language")
        void shouldSupportJapaneseLanguage() throws Exception {
            RecognitionResult jaResult = RecognitionResult.builder()
                    .recognitionId("rec-123")
                    .transcript("こんにちは")
                    .confidence(0.90)
                    .language("ja")
                    .duration(2000)
                    .build();

            when(voiceRecognitionService.recognize(any(byte[].class), eq("ja"), anyString()))
                    .thenReturn(jaResult);

            MockMultipartFile audioFile = new MockMultipartFile(
                    "audio", "ja.wav", "audio/wav", "data".getBytes()
            );

            mockMvc.perform(multipart("/api/v1/voice-recognition/recognize")
                            .file(audioFile)
                            .param("language", "ja"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.language").value("ja"));
        }
    }
}
