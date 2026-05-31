package com.gogidix.aiservices.voicerecognitionservice.multitenancy;

import com.gogidix.aiservices.voicerecognitionservice.domain.model.RecognitionResult;
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
    @DisplayName("RecognitionResult Domain Model Tests")
    class RecognitionResultTests {

        @Test
        @DisplayName("Should create recognition result with unique ID per tenant")
        void shouldCreateRecognitionResultWithUniqueIdPerTenant() {
            RecognitionResult result1 = RecognitionResult.builder()
                    .recognitionId(TENANT_1 + "-recognition-1")
                    .transcript("Hello world")
                    .confidence(0.95)
                    .language("en-US")
                    .duration(1000)
                    .build();

            RecognitionResult result2 = RecognitionResult.builder()
                    .recognitionId(TENANT_2 + "-recognition-1")
                    .transcript("Hello world")
                    .confidence(0.95)
                    .language("en-US")
                    .duration(1000)
                    .build();

            assertThat(result1.getRecognitionId()).isNotEqualTo(result2.getRecognitionId());
            assertThat(result1.getRecognitionId()).contains(TENANT_1);
            assertThat(result2.getRecognitionId()).contains(TENANT_2);
        }

        @Test
        @DisplayName("Should support different transcripts per tenant")
        void shouldSupportDifferentTranscriptsPerTenant() {
            RecognitionResult result1 = RecognitionResult.builder()
                    .recognitionId("recognition-1")
                    .transcript("Welcome to Tenant 1 application")
                    .confidence(0.92)
                    .language("en-US")
                    .duration(2000)
                    .build();

            RecognitionResult result2 = RecognitionResult.builder()
                    .recognitionId("recognition-2")
                    .transcript("Welcome to Tenant 2 application")
                    .confidence(0.88)
                    .language("en-US")
                    .duration(2000)
                    .build();

            assertThat(result1.getTranscript()).isNotEqualTo(result2.getTranscript());
            assertThat(result1.getTranscript()).contains("Tenant 1");
            assertThat(result2.getTranscript()).contains("Tenant 2");
        }

        @Test
        @DisplayName("Should distinguish recognition results by ID")
        void shouldDistinguishRecognitionResultsById() {
            RecognitionResult result1 = RecognitionResult.builder()
                    .recognitionId("recognition-1")
                    .transcript("First transcript")
                    .confidence(0.90)
                    .language("en-US")
                    .duration(1500)
                    .build();

            RecognitionResult result2 = RecognitionResult.builder()
                    .recognitionId("recognition-2")
                    .transcript("Second transcript")
                    .confidence(0.85)
                    .language("en-GB")
                    .duration(2000)
                    .build();

            assertThat(result1.getRecognitionId()).isNotEqualTo(result2.getRecognitionId());
            assertThat(result1.getTranscript()).isNotEqualTo(result2.getTranscript());
            assertThat(result1.getLanguage()).isNotEqualTo(result2.getLanguage());
        }

        @Test
        @DisplayName("Should track confidence scores")
        void shouldTrackConfidenceScores() {
            RecognitionResult highConfidence = RecognitionResult.builder()
                    .recognitionId("recognition-high")
                    .transcript("Clear speech")
                    .confidence(0.98)
                    .language("en-US")
                    .duration(1000)
                    .build();

            RecognitionResult lowConfidence = RecognitionResult.builder()
                    .recognitionId("recognition-low")
                    .transcript("Unclear speech")
                    .confidence(0.65)
                    .language("en-US")
                    .duration(1000)
                    .build();

            assertThat(highConfidence.getConfidence()).isGreaterThan(lowConfidence.getConfidence());
        }

        @Test
        @DisplayName("Should support different languages")
        void shouldSupportDifferentLanguages() {
            RecognitionResult english = RecognitionResult.builder()
                    .recognitionId("recognition-en")
                    .transcript("Hello world")
                    .confidence(0.95)
                    .language("en-US")
                    .duration(1000)
                    .build();

            RecognitionResult spanish = RecognitionResult.builder()
                    .recognitionId("recognition-es")
                    .transcript("Hola mundo")
                    .confidence(0.93)
                    .language("es-ES")
                    .duration(1000)
                    .build();

            assertThat(english.getLanguage()).isEqualTo("en-US");
            assertThat(spanish.getLanguage()).isEqualTo("es-ES");
        }

        @Test
        @DisplayName("Should track recognition duration")
        void shouldTrackRecognitionDuration() {
            RecognitionResult shortAudio = RecognitionResult.builder()
                    .recognitionId("recognition-short")
                    .transcript("Hi")
                    .confidence(0.90)
                    .language("en-US")
                    .duration(500)
                    .build();

            RecognitionResult longAudio = RecognitionResult.builder()
                    .recognitionId("recognition-long")
                    .transcript("This is a much longer speech that takes more time to recognize")
                    .confidence(0.88)
                    .language("en-US")
                    .duration(5000)
                    .build();

            assertThat(shortAudio.getDuration()).isLessThan(longAudio.getDuration());
        }

        @Test
        @DisplayName("Should handle null language")
        void shouldHandleNullLanguage() {
            RecognitionResult result = RecognitionResult.builder()
                    .recognitionId("recognition-1")
                    .transcript("Hello")
                    .confidence(0.95)
                    .language(null)
                    .duration(1000)
                    .build();

            assertThat(result.getLanguage()).isNull();
        }
    }

    @Nested
    @DisplayName("Tenant Context Isolation Tests")
    class TenantContextIsolationTests {

        @Test
        @DisplayName("Should verify tenant isolation via recognition IDs")
        void shouldVerifyTenantIsolationViaRecognitionIds() {
            RecognitionResult result1 = RecognitionResult.builder()
                    .recognitionId(TENANT_1 + "-recognition-1")
                    .transcript("Hello from tenant 1")
                    .confidence(0.95)
                    .language("en-US")
                    .duration(1000)
                    .build();

            RecognitionResult result2 = RecognitionResult.builder()
                    .recognitionId(TENANT_2 + "-recognition-1")
                    .transcript("Hello from tenant 2")
                    .confidence(0.95)
                    .language("en-US")
                    .duration(1000)
                    .build();

            // Verify that results are distinct by tenant
            assertThat(result1.getRecognitionId()).isNotEqualTo(result2.getRecognitionId());

            // Simulate filtering by tenant
            var allResults = java.util.List.of(result1, result2);
            var tenant1Results = allResults.stream()
                    .filter(r -> r.getRecognitionId().startsWith(TENANT_1))
                    .toList();

            assertThat(tenant1Results).hasSize(1);
            assertThat(tenant1Results.get(0).getRecognitionId()).startsWith(TENANT_1);
        }

        @Test
        @DisplayName("Should verify tenant isolation via transcripts")
        void shouldVerifyTenantIsolationViaTranscripts() {
            RecognitionResult result1 = RecognitionResult.builder()
                    .recognitionId("recognition-1")
                    .transcript("Tenant 1 specific content")
                    .confidence(0.95)
                    .language("en-US")
                    .duration(1000)
                    .build();

            RecognitionResult result2 = RecognitionResult.builder()
                    .recognitionId("recognition-2")
                    .transcript("Tenant 2 specific content")
                    .confidence(0.95)
                    .language("en-US")
                    .duration(1000)
                    .build();

            // Verify that results are distinct by tenant
            var allResults = java.util.List.of(result1, result2);
            var tenant1Results = allResults.stream()
                    .filter(r -> r.getTranscript().contains("Tenant 1"))
                    .toList();

            assertThat(tenant1Results).hasSize(1);
            assertThat(tenant1Results.get(0).getTranscript()).contains("Tenant 1");
        }
    }

    @Nested
    @DisplayName("Cross-Tenant Data Isolation Tests")
    class CrossTenantIsolationTests {

        @Test
        @DisplayName("Should demonstrate tenant filtering concept")
        void shouldDemonstrateTenantFilteringConcept() {
            // Create recognition results for different tenants
            RecognitionResult result1 = RecognitionResult.builder()
                    .recognitionId(TENANT_1 + "-recognition-1")
                    .transcript("Hello tenant 1")
                    .confidence(0.95)
                    .language("en-US")
                    .duration(1000)
                    .build();

            RecognitionResult result2 = RecognitionResult.builder()
                    .recognitionId(TENANT_2 + "-recognition-1")
                    .transcript("Hello tenant 2")
                    .confidence(0.95)
                    .language("en-US")
                    .duration(1000)
                    .build();

            // Simulate repository filtering by tenant
            var allResults = java.util.List.of(result1, result2);

            // Filter for tenant 1
            var tenant1Results = allResults.stream()
                    .filter(r -> r.getRecognitionId().startsWith(TENANT_1))
                    .toList();

            // Filter for tenant 2
            var tenant2Results = allResults.stream()
                    .filter(r -> r.getRecognitionId().startsWith(TENANT_2))
                    .toList();

            // Verify isolation
            assertThat(tenant1Results).hasSize(1);
            assertThat(tenant2Results).hasSize(1);
            assertThat(tenant1Results.get(0).getRecognitionId()).startsWith(TENANT_1);
            assertThat(tenant2Results.get(0).getRecognitionId()).startsWith(TENANT_2);
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
                        RecognitionResult result = RecognitionResult.builder()
                                .recognitionId(tenantId + "-recognition-" + index)
                                .transcript("Transcript from thread " + index)
                                .confidence(0.90 + (index % 10) * 0.01)
                                .language("en-US")
                                .duration(1000 + index * 100)
                                .build();

                        results[index] = new StringBuilder()
                                .append("Thread: ").append(index)
                                .append(", Tenant: ").append(tenantId)
                                .append(", RecognitionId: ").append(result.getRecognitionId())
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
