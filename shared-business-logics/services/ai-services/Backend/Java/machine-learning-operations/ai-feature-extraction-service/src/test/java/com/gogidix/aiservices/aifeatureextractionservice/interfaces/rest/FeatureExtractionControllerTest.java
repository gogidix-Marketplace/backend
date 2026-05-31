package com.gogidix.aiservices.aifeatureextractionservice.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.aifeatureextractionservice.application.dto.ExtractFeaturesRequestDto;
import com.gogidix.aiservices.aifeatureextractionservice.application.dto.FeatureSetResponseDto;
import com.gogidix.aiservices.aifeatureextractionservice.domain.model.ExtractionMethod;
import com.gogidix.aiservices.aifeatureextractionservice.domain.model.FeatureExtractionStatus;
import com.gogidix.aiservices.aifeatureextractionservice.domain.model.FeatureValue;
import org.springframework.test.web.servlet.MockMvc;
import com.gogidix.aiservices.aifeatureextractionservice.domain.port.in.FeatureExtractionServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for FeatureExtractionController.
 */
@WebMvcTest(FeatureExtractionController.class)
@DisplayName("FeatureExtractionController Tests")
class FeatureExtractionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FeatureExtractionServicePort featureExtractionService;

    private FeatureSetResponseDto featureSetResponseDto;

    @BeforeEach
    void setUp() {
        featureSetResponseDto = new FeatureSetResponseDto(
                "fs-123",
                "data-source",
                FeatureExtractionStatus.COMPLETED,
                List.of(ExtractionMethod.TFIDF),
                List.of(FeatureValue.numeric("feature1", 1.0)),
                1,
                true,
                null,
                Instant.now(),
                Instant.now()
        );
    }

    @Nested
    @DisplayName("Extract Features Endpoint Tests")
    class ExtractFeaturesEndpointTests {

        @Test
        @DisplayName("Should return 201 when extracting features")
        void shouldReturn201WhenExtractingFeatures() throws Exception {
            ExtractFeaturesRequestDto request = new ExtractFeaturesRequestDto(
                    "data-source",
                    List.of("feature1"),
                    List.of(ExtractionMethod.TFIDF),
                    true
            );

            when(featureExtractionService.extractFeatures(any())).thenReturn(featureSetResponseDto);

            mockMvc.perform(post("/api/v1/features/extract")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("X-Tenant-ID", "tenant-123")
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.featureSetId").value("fs-123"))
                    .andExpect(jsonPath("$.status").value("COMPLETED"));
        }

        @Test
        @DisplayName("Should return 400 when request is invalid")
        void shouldReturn400WhenRequestIsInvalid() throws Exception {
            String invalidRequest = """
                    {
                        "dataSource": "",
                        "features": [],
                        "methods": []
                    }
                    """;

            mockMvc.perform(post("/api/v1/features/extract")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidRequest))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Get Feature Set Endpoint Tests")
    class GetFeatureSetEndpointTests {

        @Test
        @DisplayName("Should return 200 when getting feature set")
        void shouldReturn200WhenGettingFeatureSet() throws Exception {
            when(featureExtractionService.getFeatureSet(eq("fs-123"), eq("tenant-123")))
                    .thenReturn(featureSetResponseDto);

            mockMvc.perform(get("/api/v1/features/fs-123")
                            .header("X-Tenant-ID", "tenant-123"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.featureSetId").value("fs-123"));
        }

        @Test
        @DisplayName("Should return 404 when feature set not found")
        void shouldReturn404WhenFeatureSetNotFound() throws Exception {
            when(featureExtractionService.getFeatureSet(eq("invalid"), eq("tenant-123")))
                    .thenThrow(new com.gogidix.aiservices.aifeatureextractionservice.shared.exception.ValidationException("Not found"));

            mockMvc.perform(get("/api/v1/features/invalid")
                            .header("X-Tenant-ID", "tenant-123"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("List Feature Sets Endpoint Tests")
    class ListFeatureSetsEndpointTests {

        @Test
        @DisplayName("Should return 200 when listing feature sets")
        void shouldReturn200WhenListingFeatureSets() throws Exception {
            when(featureExtractionService.listFeatureSets(eq("tenant-123"), eq(null), eq(null), eq(0), eq(20)))
                    .thenReturn(List.of(featureSetResponseDto));

            mockMvc.perform(get("/api/v1/features")
                            .header("X-Tenant-ID", "tenant-123"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(1));
        }

        @Test
        @DisplayName("Should filter by data source")
        void shouldFilterByDataSource() throws Exception {
            when(featureExtractionService.listFeatureSets(eq("tenant-123"), eq("source-1"), eq(null), eq(0), eq(20)))
                    .thenReturn(List.of(featureSetResponseDto));

            mockMvc.perform(get("/api/v1/features?dataSource=source-1")
                            .header("X-Tenant-ID", "tenant-123"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Delete Feature Set Endpoint Tests")
    class DeleteFeatureSetEndpointTests {

        @Test
        @DisplayName("Should return 204 when deleting feature set")
        void shouldReturn204WhenDeletingFeatureSet() throws Exception {
            mockMvc.perform(delete("/api/v1/features/fs-123")
                            .header("X-Tenant-ID", "tenant-123"))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("Health Check Endpoint Tests")
    class HealthCheckEndpointTests {

        @Test
        @DisplayName("Should return 200 for health check")
        void shouldReturn200ForHealthCheck() throws Exception {
            mockMvc.perform(get("/api/v1/features/health"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Feature Extraction Service is running"));
        }
    }
}
