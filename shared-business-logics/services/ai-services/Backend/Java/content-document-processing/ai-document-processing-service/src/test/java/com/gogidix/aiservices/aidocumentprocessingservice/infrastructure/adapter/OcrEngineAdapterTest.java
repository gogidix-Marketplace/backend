package com.gogidix.aiservices.aidocumentprocessingservice.infrastructure.adapter;

import com.gogidix.aiservices.aidocumentprocessingservice.domain.model.ExtractedField;
import com.gogidix.aiservices.aidocumentprocessingservice.domain.model.ExtractionConfig;
import com.gogidix.aiservices.aidocumentprocessingservice.infrastructure.config.OcrServiceProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OCR Engine Adapter Infrastructure Tests")
class OcrEngineAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private OcrServiceProperties properties;

    private OcrEngineAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new OcrEngineAdapter(restTemplate, properties);
    }

    @Nested
    @DisplayName("OCR Processing Tests")
    class OcrProcessingTests {

        @Test
        @DisplayName("Should process document and extract fields")
        void shouldProcessDocument() {
            String documentUrl = "https://example.com/invoice.pdf";
            ExtractionConfig config = ExtractionConfig.builder()
                    .fields(Arrays.asList("invoice_number", "amount"))
                    .extractTables(true)
                    .extractImages(false)
                    .build();

            when(properties.getServiceUrl()).thenReturn("http://ocr-service:8080");
            when(properties.getTimeoutMs()).thenReturn(30000);

            Map<String, Object> mockResponse = Map.of(
                    "fields", Arrays.asList(
                            Map.of("name", "invoice_number", "value", "INV-001", "confidence", 0.95),
                            Map.of("name", "amount", "value", "100.00", "confidence", 0.98)
                    ),
                    "pagesProcessed", 1,
                    "overallConfidence", 0.92
            );

            when(restTemplate.postForObject(any(), any(), eq(Map.class))).thenReturn(mockResponse);

            List<ExtractedField> result = adapter.processDocument(documentUrl, config);

            assertThat(result).isNotNull();
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getName()).isEqualTo("invoice_number");
            assertThat(result.get(0).getValue()).isEqualTo("INV-001");
            assertThat(result.get(0).getConfidence()).isEqualTo(0.95);

            verify(restTemplate).postForObject(any(), any(), eq(Map.class));
        }

        @Test
        @DisplayName("Should handle OCR service error")
        void shouldHandleOcrError() {
            String documentUrl = "https://example.com/invoice.pdf";
            ExtractionConfig config = ExtractionConfig.builder()
                    .fields(Arrays.asList("invoice_number"))
                    .build();

            when(properties.getServiceUrl()).thenReturn("http://ocr-service:8080");
            when(restTemplate.postForObject(any(), any(), eq(Map.class)))
                    .thenThrow(new RuntimeException("OCR service unavailable"));

            assertThatThrownBy(() -> adapter.processDocument(documentUrl, config))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("OCR service unavailable");
        }

        @Test
        @DisplayName("Should include extraction config in request")
        void shouldIncludeConfigInRequest() {
            String documentUrl = "https://example.com/invoice.pdf";
            ExtractionConfig config = ExtractionConfig.builder()
                    .fields(Arrays.asList("invoice_number", "amount", "date"))
                    .extractTables(true)
                    .extractImages(true)
                    .build();

            when(properties.getServiceUrl()).thenReturn("http://ocr-service:8080");

            Map<String, Object> mockResponse = Map.of("fields", List.of(), "pagesProcessed", 0);
            when(restTemplate.postForObject(any(), any(), eq(Map.class))).thenReturn(mockResponse);

            adapter.processDocument(documentUrl, config);

            verify(restTemplate).postForObject(any(), any(), eq(Map.class));
        }
    }

    @Nested
    @DisplayName("Health Check Tests")
    class HealthCheckTests {

        @Test
        @DisplayName("Should check OCR service health")
        void shouldCheckHealth() {
            when(properties.getServiceUrl()).thenReturn("http://ocr-service:8080");
            when(properties.getHealthEndpoint()).thenReturn("/health");

            Map<String, Object> healthResponse = Map.of("status", "UP");
            when(restTemplate.getForObject(any(), eq(Map.class))).thenReturn(healthResponse);

            boolean isHealthy = adapter.isHealthy();

            assertThat(isHealthy).isTrue();
            verify(restTemplate).getForObject(any(), eq(Map.class));
        }

        @Test
        @DisplayName("Should return false when service is down")
        void shouldReturnFalseWhenServiceDown() {
            when(properties.getServiceUrl()).thenReturn("http://ocr-service:8080");
            when(restTemplate.getForObject(any(), eq(Map.class)))
                    .thenThrow(new RuntimeException("Service unavailable"));

            boolean isHealthy = adapter.isHealthy();

            assertThat(isHealthy).isFalse();
        }
    }

    @Nested
    @DisplayName("Request Mapping Tests")
    class RequestMappingTests {

        @Test
        @DisplayName("Should build correct OCR request")
        void shouldBuildOcrRequest() {
            String documentUrl = "https://example.com/invoice.pdf";
            ExtractionConfig config = ExtractionConfig.builder()
                    .fields(Arrays.asList("invoice_number", "amount"))
                    .extractTables(true)
                    .extractImages(false)
                    .build();

            when(properties.getServiceUrl()).thenReturn("http://ocr-service:8080");
            when(properties.getTimeoutMs()).thenReturn(30000);

            Map<String, Object> mockResponse = Map.of("fields", List.of(), "pagesProcessed", 0);
            when(restTemplate.postForObject(any(), any(), eq(Map.class))).thenReturn(mockResponse);

            adapter.processDocument(documentUrl, config);

            verify(restTemplate).postForObject(eq("http://ocr-service:8080/extract"), any(), eq(Map.class));
        }
    }

    @Nested
    @DisplayName("Response Mapping Tests")
    class ResponseMappingTests {

        @Test
        @DisplayName("Should map OCR response to extracted fields")
        void shouldMapResponseToFields() {
            String documentUrl = "https://example.com/invoice.pdf";
            ExtractionConfig config = ExtractionConfig.builder().build();

            when(properties.getServiceUrl()).thenReturn("http://ocr-service:8080");

            Map<String, Object> mockResponse = Map.of(
                    "fields", Arrays.asList(
                            Map.of(
                                    "name", "invoice_number",
                                    "value", "INV-001",
                                    "confidence", 0.95,
                                    "boundingBox", Map.of("x", 100, "y", 200, "width", 150, "height", 30),
                                    "page", 1
                            )
                    ),
                    "pagesProcessed", 2,
                    "overallConfidence", 0.92
            );

            when(restTemplate.postForObject(any(), any(), eq(Map.class))).thenReturn(mockResponse);

            List<ExtractedField> result = adapter.processDocument(documentUrl, config);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("invoice_number");
            assertThat(result.get(0).getValue()).isEqualTo("INV-001");
            assertThat(result.get(0).getConfidence()).isEqualTo(0.95);
            assertThat(result.get(0).getMetadata()).isNotNull();
            assertThat(result.get(0).getPageNumber()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should handle empty response")
        void shouldHandleEmptyResponse() {
            String documentUrl = "https://example.com/invoice.pdf";
            ExtractionConfig config = ExtractionConfig.builder().build();

            when(properties.getServiceUrl()).thenReturn("http://ocr-service:8080");

            Map<String, Object> mockResponse = Map.of("fields", List.of(), "pagesProcessed", 0);
            when(restTemplate.postForObject(any(), any(), eq(Map.class))).thenReturn(mockResponse);

            List<ExtractedField> result = adapter.processDocument(documentUrl, config);

            assertThat(result).isEmpty();
        }
    }
}
