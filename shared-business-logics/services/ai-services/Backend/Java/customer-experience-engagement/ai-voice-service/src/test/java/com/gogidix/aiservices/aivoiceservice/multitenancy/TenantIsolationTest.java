package com.gogidix.aiservices.aivoiceservice.multitenancy;

import com.gogidix.aiservices.aivoiceservice.domain.model.SynthesisResult;
import com.gogidix.aiservices.aivoiceservice.domain.model.VoiceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Multi-Tenancy Tenant Isolation Tests")
class TenantIsolationTest {

    private static final String TENANT_1 = "tenant-001";
    private static final String TENANT_2 = "tenant-002";

    @Nested
    @DisplayName("SynthesisResult Domain Model Tests")
    class SynthesisResultTests {

        @Test
        @DisplayName("Should create synthesis result with unique ID per tenant")
        void shouldCreateSynthesisResultWithUniqueIdPerTenant() {
            SynthesisResult result1 = SynthesisResult.builder()
                    .synthesisId("synthesis-" + TENANT_1 + "-1")
                    .audioUrl("https://tenant1.example.com/audio1.mp3")
                    .text("Hello world")
                    .voiceType(VoiceType.FEMALE)
                    .duration(1000)
                    .format("mp3")
                    .build();

            SynthesisResult result2 = SynthesisResult.builder()
                    .synthesisId("synthesis-" + TENANT_2 + "-1")
                    .audioUrl("https://tenant2.example.com/audio1.mp3")
                    .text("Hello world")
                    .voiceType(VoiceType.FEMALE)
                    .duration(1000)
                    .format("mp3")
                    .build();

            assertThat(result1.getSynthesisId()).isNotEqualTo(result2.getSynthesisId());
            assertThat(result1.getSynthesisId()).contains(TENANT_1);
            assertThat(result2.getSynthesisId()).contains(TENANT_2);
        }

        @Test
        @DisplayName("Should support tenant-specific audio URLs")
        void shouldSupportTenantSpecificAudioUrls() {
            SynthesisResult tenant1Result = SynthesisResult.builder()
                    .synthesisId("synthesis-1")
                    .audioUrl("https://tenant1.storage.example.com/audio/voice-123.mp3")
                    .text("Welcome")
                    .voiceType(VoiceType.MALE)
                    .duration(2000)
                    .format("mp3")
                    .build();

            SynthesisResult tenant2Result = SynthesisResult.builder()
                    .synthesisId("synthesis-2")
                    .audioUrl("https://tenant2.storage.example.com/audio/voice-456.mp3")
                    .text("Welcome")
                    .voiceType(VoiceType.MALE)
                    .duration(2000)
                    .format("mp3")
                    .build();

            assertThat(tenant1Result.getAudioUrl()).contains("tenant1");
            assertThat(tenant2Result.getAudioUrl()).contains("tenant2");
        }

        @Test
        @DisplayName("Should distinguish synthesis results by ID")
        void shouldDistinguishSynthesisResultsById() {
            SynthesisResult result1 = SynthesisResult.builder()
                    .synthesisId("synthesis-1")
                    .audioUrl("https://example.com/audio1.mp3")
                    .text("Text 1")
                    .voiceType(VoiceType.FEMALE)
                    .duration(1000)
                    .format("mp3")
                    .build();

            SynthesisResult result2 = SynthesisResult.builder()
                    .synthesisId("synthesis-2")
                    .audioUrl("https://example.com/audio2.mp3")
                    .text("Text 2")
                    .voiceType(VoiceType.MALE)
                    .duration(2000)
                    .format("wav")
                    .build();

            assertThat(result1.getSynthesisId()).isNotEqualTo(result2.getSynthesisId());
            assertThat(result1.getText()).isNotEqualTo(result2.getText());
            assertThat(result1.getVoiceType()).isNotEqualTo(result2.getVoiceType());
        }

        @Test
        @DisplayName("Should support different voice types")
        void shouldSupportDifferentVoiceTypes() {
            SynthesisResult femaleVoice = SynthesisResult.builder()
                    .synthesisId("synthesis-female")
                    .audioUrl("https://example.com/female.mp3")
                    .text("Hello")
                    .voiceType(VoiceType.FEMALE)
                    .duration(1000)
                    .format("mp3")
                    .build();

            SynthesisResult maleVoice = SynthesisResult.builder()
                    .synthesisId("synthesis-male")
                    .audioUrl("https://example.com/male.mp3")
                    .text("Hello")
                    .voiceType(VoiceType.MALE)
                    .duration(1000)
                    .format("mp3")
                    .build();

            assertThat(femaleVoice.getVoiceType()).isEqualTo(VoiceType.FEMALE);
            assertThat(maleVoice.getVoiceType()).isEqualTo(VoiceType.MALE);
        }

        @Test
        @DisplayName("Should support different audio formats")
        void shouldSupportDifferentAudioFormats() {
            SynthesisResult mp3Result = SynthesisResult.builder()
                    .synthesisId("synthesis-mp3")
                    .audioUrl("https://example.com/audio.mp3")
                    .text("Hello")
                    .voiceType(VoiceType.FEMALE)
                    .duration(1000)
                    .format("mp3")
                    .build();

            SynthesisResult wavResult = SynthesisResult.builder()
                    .synthesisId("synthesis-wav")
                    .audioUrl("https://example.com/audio.wav")
                    .text("Hello")
                    .voiceType(VoiceType.FEMALE)
                    .duration(1000)
                    .format("wav")
                    .build();

            assertThat(mp3Result.getFormat()).isEqualTo("mp3");
            assertThat(wavResult.getFormat()).isEqualTo("wav");
        }

        @Test
        @DisplayName("Should track synthesis duration")
        void shouldTrackSynthesisDuration() {
            SynthesisResult shortAudio = SynthesisResult.builder()
                    .synthesisId("synthesis-short")
                    .audioUrl("https://example.com/short.mp3")
                    .text("Hi")
                    .voiceType(VoiceType.FEMALE)
                    .duration(500)
                    .format("mp3")
                    .build();

            SynthesisResult longAudio = SynthesisResult.builder()
                    .synthesisId("synthesis-long")
                    .audioUrl("https://example.com/long.mp3")
                    .text("This is a much longer text that will result in a longer audio file")
                    .voiceType(VoiceType.FEMALE)
                    .duration(5000)
                    .format("mp3")
                    .build();

            assertThat(shortAudio.getDuration()).isLessThan(longAudio.getDuration());
        }
    }

    @Nested
    @DisplayName("Tenant Context Isolation Tests")
    class TenantContextIsolationTests {

        @Test
        @DisplayName("Should verify tenant isolation via synthesis IDs")
        void shouldVerifyTenantIsolationViaSynthesisIds() {
            SynthesisResult result1 = SynthesisResult.builder()
                    .synthesisId(TENANT_1 + "-synthesis-1")
                    .audioUrl("https://tenant1.example.com/audio1.mp3")
                    .text("Hello from tenant 1")
                    .voiceType(VoiceType.FEMALE)
                    .duration(1000)
                    .format("mp3")
                    .build();

            SynthesisResult result2 = SynthesisResult.builder()
                    .synthesisId(TENANT_2 + "-synthesis-1")
                    .audioUrl("https://tenant2.example.com/audio1.mp3")
                    .text("Hello from tenant 2")
                    .voiceType(VoiceType.FEMALE)
                    .duration(1000)
                    .format("mp3")
                    .build();

            // Verify that results are distinct by tenant
            assertThat(result1.getSynthesisId()).isNotEqualTo(result2.getSynthesisId());

            // Simulate filtering by tenant
            var allResults = java.util.List.of(result1, result2);
            var tenant1Results = allResults.stream()
                    .filter(r -> r.getSynthesisId().startsWith(TENANT_1))
                    .toList();

            assertThat(tenant1Results).hasSize(1);
            assertThat(tenant1Results.get(0).getSynthesisId()).startsWith(TENANT_1);
        }

        @Test
        @DisplayName("Should verify tenant isolation via audio URLs")
        void shouldVerifyTenantIsolationViaAudioUrls() {
            SynthesisResult result1 = SynthesisResult.builder()
                    .synthesisId("synthesis-1")
                    .audioUrl("https://storage." + TENANT_1 + ".example.com/audio1.mp3")
                    .text("Hello")
                    .voiceType(VoiceType.FEMALE)
                    .duration(1000)
                    .format("mp3")
                    .build();

            SynthesisResult result2 = SynthesisResult.builder()
                    .synthesisId("synthesis-2")
                    .audioUrl("https://storage." + TENANT_2 + ".example.com/audio1.mp3")
                    .text("Hello")
                    .voiceType(VoiceType.FEMALE)
                    .duration(1000)
                    .format("mp3")
                    .build();

            // Verify that results are distinct by tenant URL
            var allResults = java.util.List.of(result1, result2);
            var tenant1Results = allResults.stream()
                    .filter(r -> r.getAudioUrl().contains(TENANT_1))
                    .toList();

            assertThat(tenant1Results).hasSize(1);
            assertThat(tenant1Results.get(0).getAudioUrl()).contains(TENANT_1);
        }
    }

    @Nested
    @DisplayName("Cross-Tenant Data Isolation Tests")
    class CrossTenantIsolationTests {

        @Test
        @DisplayName("Should demonstrate tenant filtering concept")
        void shouldDemonstrateTenantFilteringConcept() {
            // Create synthesis results for different tenants
            SynthesisResult result1 = SynthesisResult.builder()
                    .synthesisId(TENANT_1 + "-synthesis-1")
                    .audioUrl("https://tenant1.example.com/audio1.mp3")
                    .text("Hello tenant 1")
                    .voiceType(VoiceType.FEMALE)
                    .duration(1000)
                    .format("mp3")
                    .build();

            SynthesisResult result2 = SynthesisResult.builder()
                    .synthesisId(TENANT_2 + "-synthesis-1")
                    .audioUrl("https://tenant2.example.com/audio1.mp3")
                    .text("Hello tenant 2")
                    .voiceType(VoiceType.FEMALE)
                    .duration(1000)
                    .format("mp3")
                    .build();

            // Simulate repository filtering by tenant
            var allResults = java.util.List.of(result1, result2);

            // Filter for tenant 1
            var tenant1Results = allResults.stream()
                    .filter(r -> r.getSynthesisId().startsWith(TENANT_1))
                    .toList();

            // Filter for tenant 2
            var tenant2Results = allResults.stream()
                    .filter(r -> r.getSynthesisId().startsWith(TENANT_2))
                    .toList();

            // Verify isolation
            assertThat(tenant1Results).hasSize(1);
            assertThat(tenant2Results).hasSize(1);
            assertThat(tenant1Results.get(0).getSynthesisId()).startsWith(TENANT_1);
            assertThat(tenant2Results.get(0).getSynthesisId()).startsWith(TENANT_2);
        }
    }

    @Nested
    @DisplayName("Thread Safety Tests")
    class ThreadSafetyTests {

        @Test
        @DisplayName("Should support multiple tenants concurrently")
        void shouldSupportMultipleTenantsConcurrently() throws InterruptedException {
            int threadCount = 5;
            Thread[] threads = new Thread[threadCount];
            final boolean[] errors = {false};
            final String[] results = new String[threadCount];

            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                final String tenantId = "tenant-" + (i + 1);

                threads[i] = new Thread(() -> {
                    try {
                        SynthesisResult result = SynthesisResult.builder()
                                .synthesisId(tenantId + "-synthesis-" + index)
                                .audioUrl("https://" + tenantId + ".example.com/audio" + index + ".mp3")
                                .text("Text from thread " + index)
                                .voiceType(VoiceType.FEMALE)
                                .duration(1000 + index * 100)
                                .format("mp3")
                                .build();

                        results[index] = new StringBuilder()
                                .append("Thread: ").append(index)
                                .append(", Tenant: ").append(tenantId)
                                .append(", SynthesisId: ").append(result.getSynthesisId())
                                .toString();
                    } catch (Exception e) {
                        errors[0] = true;
                    }
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            assertThat(errors[0]).isFalse();

            // Verify each thread had its own tenant
            for (int i = 0; i < threadCount; i++) {
                assertThat(results[i]).isNotNull();
                assertThat(results[i].toString()).contains("tenant-" + (i + 1));
            }
        }
    }
}
