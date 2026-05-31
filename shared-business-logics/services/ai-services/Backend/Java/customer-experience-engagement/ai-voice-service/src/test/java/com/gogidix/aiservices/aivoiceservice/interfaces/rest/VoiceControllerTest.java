package com.gogidix.aiservices.aivoiceservice.interfaces.rest;

import com.gogidix.aiservices.aivoiceservice.application.service.VoiceService;
import com.gogidix.aiservices.aivoiceservice.domain.model.SynthesisResult;
import com.gogidix.aiservices.aivoiceservice.domain.model.VoiceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VoiceController.class)
@DisplayName("VoiceController REST API Tests")
class VoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VoiceService voiceService;

    private SynthesisResult synthesisResult;

    @BeforeEach
    void setUp() {
        synthesisResult = SynthesisResult.builder()
                .synthesisId("syn-123")
                .audioUrl("https://storage.example.com/audio/syn-123.mp3")
                .text("Hello, world!")
                .voiceType(VoiceType.NEUTRAL)
                .duration(1500)
                .format("mp3")
                .build();
    }

    @Nested
    @DisplayName("POST /api/v1/voice/synthesize - Synthesize Speech")
    class SynthesizeTests {

        @Test
        @DisplayName("Should synthesize speech successfully")
        void shouldSynthesizeSpeechSuccessfully() throws Exception {
            when(voiceService.synthesize(eq("Hello, world!"), eq(VoiceType.NEUTRAL), eq("mp3")))
                    .thenReturn(synthesisResult);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", "Hello, world!")
                            .param("voiceType", "NEUTRAL")
                            .param("format", "mp3"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.synthesisId").exists())
                    .andExpect(jsonPath("$.audioUrl").exists())
                    .andExpect(jsonPath("$.text").value("Hello, world!"))
                    .andExpect(jsonPath("$.voiceType").value("NEUTRAL"))
                    .andExpect(jsonPath("$.duration").value(1500))
                    .andExpect(jsonPath("$.format").value("mp3"));

            verify(voiceService).synthesize("Hello, world!", VoiceType.NEUTRAL, "mp3");
        }

        @Test
        @DisplayName("Should use default NEUTRAL voice type")
        void shouldUseDefaultNeutralVoiceType() throws Exception {
            when(voiceService.synthesize(eq("Hello"), eq(VoiceType.NEUTRAL), eq("mp3")))
                    .thenReturn(synthesisResult);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", "Hello"))
                    .andExpect(status().isOk());

            verify(voiceService).synthesize("Hello", VoiceType.NEUTRAL, "mp3");
        }

        @Test
        @DisplayName("Should use default mp3 format")
        void shouldUseDefaultMp3Format() throws Exception {
            when(voiceService.synthesize(anyString(), any(VoiceType.class), eq("mp3")))
                    .thenReturn(synthesisResult);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", "Hello"))
                    .andExpect(status().isOk());

            verify(voiceService).synthesize(anyString(), any(VoiceType.class), eq("mp3"));
        }

        @Test
        @DisplayName("Should accept empty text")
        void shouldAcceptEmptyText() throws Exception {
            SynthesisResult emptyResult = SynthesisResult.builder()
                    .synthesisId("syn-456")
                    .audioUrl("https://storage.example.com/audio/syn-456.mp3")
                    .text("")
                    .voiceType(VoiceType.NEUTRAL)
                    .duration(0)
                    .format("mp3")
                    .build();

            when(voiceService.synthesize(eq(""), any(VoiceType.class), anyString()))
                    .thenReturn(emptyResult);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", ""))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.text").value(""));
        }
    }

    @Nested
    @DisplayName("VoiceType Tests")
    class VoiceTypeTests {

        @ParameterizedTest
        @EnumSource(VoiceType.class)
        @DisplayName("Should accept all VoiceType values")
        void shouldAcceptAllVoiceTypes(VoiceType voiceType) throws Exception {
            when(voiceService.synthesize(anyString(), eq(voiceType), anyString()))
                    .thenReturn(synthesisResult);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", "Hello")
                            .param("voiceType", voiceType.name()))
                    .andExpect(status().isOk());

            verify(voiceService).synthesize("Hello", voiceType, "mp3");
        }

        @Test
        @DisplayName("Should return result with specified voice type")
        void shouldReturnResultWithSpecifiedVoiceType() throws Exception {
            VoiceType[] voiceTypes = {
                    VoiceType.NEUTRAL,
                    VoiceType.FEMALE,
                    VoiceType.MALE,
                    VoiceType.ROBOTIC
            };

            for (VoiceType voiceType : voiceTypes) {
                SynthesisResult result = SynthesisResult.builder()
                        .synthesisId("syn-123")
                        .audioUrl("https://storage.example.com/audio.mp3")
                        .text("Hello")
                        .voiceType(voiceType)
                        .duration(1000)
                        .format("mp3")
                        .build();

                when(voiceService.synthesize(eq("Hello"), eq(voiceType), eq("mp3")))
                        .thenReturn(result);

                mockMvc.perform(post("/api/v1/voice/synthesize")
                                .param("text", "Hello")
                                .param("voiceType", voiceType.name()))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.voiceType").value(voiceType.name()));
            }
        }
    }

    @Nested
    @DisplayName("Audio Format Tests")
    class AudioFormatTests {

        @Test
        @DisplayName("Should accept MP3 format")
        void shouldAcceptMp3Format() throws Exception {
            when(voiceService.synthesize(anyString(), any(VoiceType.class), eq("mp3")))
                    .thenReturn(synthesisResult);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", "Hello")
                            .param("format", "mp3"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.format").value("mp3"));
        }

        @Test
        @DisplayName("Should accept WAV format")
        void shouldAcceptWavFormat() throws Exception {
            SynthesisResult wavResult = SynthesisResult.builder()
                    .synthesisId("syn-123")
                    .audioUrl("https://storage.example.com/audio/syn-123.wav")
                    .text("Hello")
                    .voiceType(VoiceType.NEUTRAL)
                    .duration(1500)
                    .format("wav")
                    .build();

            when(voiceService.synthesize(anyString(), any(VoiceType.class), eq("wav")))
                    .thenReturn(wavResult);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", "Hello")
                            .param("format", "wav"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.format").value("wav"));
        }

        @Test
        @DisplayName("Should accept OGG format")
        void shouldAcceptOggFormat() throws Exception {
            SynthesisResult oggResult = SynthesisResult.builder()
                    .synthesisId("syn-123")
                    .audioUrl("https://storage.example.com/audio/syn-123.ogg")
                    .text("Hello")
                    .voiceType(VoiceType.NEUTRAL)
                    .duration(1500)
                    .format("ogg")
                    .build();

            when(voiceService.synthesize(anyString(), any(VoiceType.class), eq("ogg")))
                    .thenReturn(oggResult);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", "Hello")
                            .param("format", "ogg"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.format").value("ogg"));
        }
    }

    @Nested
    @DisplayName("Text Input Tests")
    class TextInputTests {

        @Test
        @DisplayName("Should accept short text")
        void shouldAcceptShortText() throws Exception {
            when(voiceService.synthesize(eq("Hi"), any(VoiceType.class), anyString()))
                    .thenReturn(synthesisResult);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", "Hi"))
                    .andExpect(status().isOk());

            verify(voiceService).synthesize("Hi", VoiceType.NEUTRAL, "mp3");
        }

        @Test
        @DisplayName("Should accept long text")
        void shouldAcceptLongText() throws Exception {
            String longText = "This is a very long text for speech synthesis. ".repeat(10);

            when(voiceService.synthesize(eq(longText), any(VoiceType.class), anyString()))
                    .thenReturn(synthesisResult);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", longText))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should accept text with punctuation")
        void shouldAcceptTextWithPunctuation() throws Exception {
            String textWithPunctuation = "Hello, world! How are you today?";

            when(voiceService.synthesize(eq(textWithPunctuation), any(VoiceType.class), anyString()))
                    .thenReturn(synthesisResult);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", textWithPunctuation))
                    .andExpect(status().isOk());

            verify(voiceService).synthesize(textWithPunctuation, VoiceType.NEUTRAL, "mp3");
        }

        @Test
        @DisplayName("Should accept text with numbers")
        void shouldAcceptTextWithNumbers() throws Exception {
            String textWithNumbers = "The price is 99 dollars and 99 cents.";

            when(voiceService.synthesize(eq(textWithNumbers), any(VoiceType.class), anyString()))
                    .thenReturn(synthesisResult);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", textWithNumbers))
                    .andExpect(status().isOk());

            verify(voiceService).synthesize(textWithNumbers, VoiceType.NEUTRAL, "mp3");
        }

        @Test
        @DisplayName("Should accept unicode text")
        void shouldAcceptUnicodeText() throws Exception {
            String unicodeText = "こんにちは世界";

            SynthesisResult unicodeResult = SynthesisResult.builder()
                    .synthesisId("syn-123")
                    .audioUrl("https://storage.example.com/audio/syn-123.mp3")
                    .text(unicodeText)
                    .voiceType(VoiceType.NEUTRAL)
                    .duration(2000)
                    .format("mp3")
                    .build();

            when(voiceService.synthesize(eq(unicodeText), any(VoiceType.class), anyString()))
                    .thenReturn(unicodeResult);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", unicodeText))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.text").value(unicodeText));
        }
    }

    @Nested
    @DisplayName("SynthesisResult Tests")
    class SynthesisResultTests {

        @Test
        @DisplayName("Should return synthesis with all fields")
        void shouldReturnSynthesisWithAllFields() throws Exception {
            when(voiceService.synthesize(anyString(), any(VoiceType.class), anyString()))
                    .thenReturn(synthesisResult);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", "Hello"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.synthesisId").exists())
                    .andExpect(jsonPath("$.audioUrl").exists())
                    .andExpect(jsonPath("$.text").exists())
                    .andExpect(jsonPath("$.voiceType").exists())
                    .andExpect(jsonPath("$.duration").exists())
                    .andExpect(jsonPath("$.format").exists());
        }

        @Test
        @DisplayName("Should return S3 audio URL")
        void shouldReturnS3AudioUrl() throws Exception {
            when(voiceService.synthesize(anyString(), any(VoiceType.class), anyString()))
                    .thenReturn(synthesisResult);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", "Hello"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.audioUrl").value("https://storage.example.com/audio/syn-123.mp3"));
        }

        @Test
        @DisplayName("Should return synthesis ID")
        void shouldReturnSynthesisId() throws Exception {
            when(voiceService.synthesize(anyString(), any(VoiceType.class), anyString()))
                    .thenReturn(synthesisResult);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", "Hello"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.synthesisId").value("syn-123"));
        }

        @Test
        @DisplayName("Should return duration")
        void shouldReturnDuration() throws Exception {
            when(voiceService.synthesize(anyString(), any(VoiceType.class), anyString()))
                    .thenReturn(synthesisResult);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", "Hello"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.duration").value(1500));
        }

        @Test
        @DisplayName("Should return short duration for short text")
        void shouldReturnShortDurationForShortText() throws Exception {
            SynthesisResult shortResult = SynthesisResult.builder()
                    .synthesisId("syn-123")
                    .audioUrl("https://storage.example.com/audio/syn-123.mp3")
                    .text("Hi")
                    .voiceType(VoiceType.NEUTRAL)
                    .duration(200)
                    .format("mp3")
                    .build();

            when(voiceService.synthesize(eq("Hi"), any(VoiceType.class), anyString()))
                    .thenReturn(shortResult);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", "Hi"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.duration").value(200));
        }

        @Test
        @DisplayName("Should return long duration for long text")
        void shouldReturnLongDurationForLongText() throws Exception {
            SynthesisResult longResult = SynthesisResult.builder()
                    .synthesisId("syn-123")
                    .audioUrl("https://storage.example.com/audio/syn-123.mp3")
                    .text("Long text...")
                    .voiceType(VoiceType.NEUTRAL)
                    .duration(10000)
                    .format("mp3")
                    .build();

            when(voiceService.synthesize(anyString(), any(VoiceType.class), anyString()))
                    .thenReturn(longResult);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", "Long text..."))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.duration").value(10000));
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle special characters in text")
        void shouldHandleSpecialCharactersInText() throws Exception {
            String textWithSpecialChars = "Hello! @#$%^&*() World";

            when(voiceService.synthesize(eq(textWithSpecialChars), any(VoiceType.class), anyString()))
                    .thenReturn(synthesisResult);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", textWithSpecialChars))
                    .andExpect(status().isOk());

            verify(voiceService).synthesize(textWithSpecialChars, VoiceType.NEUTRAL, "mp3");
        }

        @Test
        @DisplayName("Should handle newlines in text")
        void shouldHandleNewlinesInText() throws Exception {
            String textWithNewlines = "Line 1\nLine 2\nLine 3";

            when(voiceService.synthesize(eq(textWithNewlines), any(VoiceType.class), anyString()))
                    .thenReturn(synthesisResult);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", textWithNewlines))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should handle very long text")
        void shouldHandleVeryLongText() throws Exception {
            String veryLongText = "a".repeat(1000);

            when(voiceService.synthesize(eq(veryLongText), any(VoiceType.class), anyString()))
                    .thenReturn(synthesisResult);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", veryLongText))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should handle text with emojis")
        void shouldHandleTextWithEmojis() throws Exception {
            String textWithEmojis = "Hello! 😀 🎉 🎊";

            when(voiceService.synthesize(eq(textWithEmojis), any(VoiceType.class), anyString()))
                    .thenReturn(synthesisResult);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", textWithEmojis))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Voice Characteristics Tests")
    class VoiceCharacteristicsTests {

        @Test
        @DisplayName("Should synthesize with NEUTRAL voice")
        void shouldSynthesizeWithNeutralVoice() throws Exception {
            when(voiceService.synthesize(anyString(), eq(VoiceType.NEUTRAL), anyString()))
                    .thenReturn(synthesisResult);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", "Hello")
                            .param("voiceType", "NEUTRAL"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.voiceType").value("NEUTRAL"));
        }

        @Test
        @DisplayName("Should synthesize with FEMALE voice")
        void shouldSynthesizeWithFemaleVoice() throws Exception {
            SynthesisResult femaleResult = SynthesisResult.builder()
                    .synthesisId("syn-123")
                    .audioUrl("https://storage.example.com/audio/syn-123.mp3")
                    .text("Hello")
                    .voiceType(VoiceType.FEMALE)
                    .duration(1000)
                    .format("mp3")
                    .build();

            when(voiceService.synthesize(anyString(), eq(VoiceType.FEMALE), anyString()))
                    .thenReturn(femaleResult);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", "Hello")
                            .param("voiceType", "FEMALE"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.voiceType").value("FEMALE"));
        }

        @Test
        @DisplayName("Should synthesize with MALE voice")
        void shouldSynthesizeWithMaleVoice() throws Exception {
            SynthesisResult maleResult = SynthesisResult.builder()
                    .synthesisId("syn-123")
                    .audioUrl("https://storage.example.com/audio/syn-123.mp3")
                    .text("Hello")
                    .voiceType(VoiceType.MALE)
                    .duration(1000)
                    .format("mp3")
                    .build();

            when(voiceService.synthesize(anyString(), eq(VoiceType.MALE), anyString()))
                    .thenReturn(maleResult);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", "Hello")
                            .param("voiceType", "MALE"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.voiceType").value("MALE"));
        }

        @Test
        @DisplayName("Should synthesize with ROBOTIC voice")
        void shouldSynthesizeWithRoboticVoice() throws Exception {
            SynthesisResult roboticResult = SynthesisResult.builder()
                    .synthesisId("syn-123")
                    .audioUrl("https://storage.example.com/audio/syn-123.mp3")
                    .text("Hello")
                    .voiceType(VoiceType.ROBOTIC)
                    .duration(1000)
                    .format("mp3")
                    .build();

            when(voiceService.synthesize(anyString(), eq(VoiceType.ROBOTIC), anyString()))
                    .thenReturn(roboticResult);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", "Hello")
                            .param("voiceType", "ROBOTIC"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.voiceType").value("ROBOTIC"));
        }
    }

    @Nested
    @DisplayName("Parameter Combination Tests")
    class ParameterCombinationTests {

        @Test
        @DisplayName("Should combine FEMALE voice with WAV format")
        void shouldCombineFemaleVoiceWithWavFormat() throws Exception {
            SynthesisResult result = SynthesisResult.builder()
                    .synthesisId("syn-123")
                    .audioUrl("https://storage.example.com/audio/syn-123.wav")
                    .text("Hello")
                    .voiceType(VoiceType.FEMALE)
                    .duration(1000)
                    .format("wav")
                    .build();

            when(voiceService.synthesize(anyString(), eq(VoiceType.FEMALE), eq("wav")))
                    .thenReturn(result);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", "Hello")
                            .param("voiceType", "FEMALE")
                            .param("format", "wav"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.voiceType").value("FEMALE"))
                    .andExpect(jsonPath("$.format").value("wav"));
        }

        @Test
        @DisplayName("Should combine MALE voice with OGG format")
        void shouldCombineMaleVoiceWithOggFormat() throws Exception {
            SynthesisResult result = SynthesisResult.builder()
                    .synthesisId("syn-123")
                    .audioUrl("https://storage.example.com/audio/syn-123.ogg")
                    .text("Hello")
                    .voiceType(VoiceType.MALE)
                    .duration(1000)
                    .format("ogg")
                    .build();

            when(voiceService.synthesize(anyString(), eq(VoiceType.MALE), eq("ogg")))
                    .thenReturn(result);

            mockMvc.perform(post("/api/v1/voice/synthesize")
                            .param("text", "Hello")
                            .param("voiceType", "MALE")
                            .param("format", "ogg"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.voiceType").value("MALE"))
                    .andExpect(jsonPath("$.format").value("ogg"));
        }
    }
}
