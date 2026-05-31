package com.gogidix.aiservices.multimodalprocessingservice.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.multimodalprocessingservice.application.dto.request.ProcessMultimodalRequest;
import com.gogidix.aiservices.multimodalprocessingservice.application.dto.request.SearchSimilarRequest;
import com.gogidix.aiservices.multimodalprocessingservice.application.dto.response.MultimodalProcessingResponse;
import com.gogidix.aiservices.multimodalprocessingservice.application.dto.response.SimilarContentResponse;
import com.gogidix.aiservices.multimodalprocessingservice.application.service.MultimodalProcessingService;
import com.gogidix.aiservices.multimodalprocessingservice.domain.model.ContentModality;
import com.gogidix.aiservices.multimodalprocessingservice.domain.model.OutputFormat;
import com.gogidix.aiservices.multimodalprocessingservice.shared.exception.MultimodalProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MultimodalProcessingController.class)
@DisplayName("Multimodal Processing Controller Interface Tests")
class MultimodalProcessingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MultimodalProcessingService multimodalProcessingService;

    private static final String USER_ID = "user-123";

    @Nested
    @DisplayName("Content Processing Endpoints")
    class ProcessingEndpoints {

        @Test
        @DisplayName("POST /api/v1/multimodal/process - Should process content")
        void shouldProcessContent() throws Exception {
            Map<String, Object> item = Map.of(
                    "modality", "TEXT",
                    "url", "https://example.com/document.txt",
                    "metadata", Map.of()
            );

            Map<String, Object> request = Map.of(
                    "contentItems", List.of(item),
                    "outputFormat", "EMBEDDING"
            );

            float[] embedding = new float[768];
            Arrays.fill(embedding, 0.5f);

            MultimodalProcessingResponse response = new MultimodalProcessingResponse(
                    UUID.randomUUID().toString(),
                    Map.of("TEXT", Arrays.asList(0.5)),
                    Arrays.asList(0.5),
                    null,
                    null
            );

            when(multimodalProcessingService.processMultimodal(any(), eq(USER_ID)))
                    .thenReturn(response);

            mockMvc.perform(post("/api/v1/multimodal/process")
                            .header("X-User-Id", USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$.processingId").exists())
                    .andExpect(jsonPath("$.embeddings").exists());

            verify(multimodalProcessingService).processMultimodal(any(), eq(USER_ID));
        }

        @Test
        @DisplayName("POST /api/v1/multimodal/process - Should reject invalid modality")
        void shouldRejectInvalidModality() throws Exception {
            Map<String, Object> item = Map.of(
                    "modality", "INVALID",
                    "url", "https://example.com/document.txt"
            );

            Map<String, Object> request = Map.of(
                    "contentItems", List.of(item)
            );

            mockMvc.perform(post("/api/v1/multimodal/process")
                            .header("X-User-Id", USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("POST /api/v1/multimodal/process - Should process multimodal content")
        void shouldProcessMultimodalContent() throws Exception {
            List<Map<String, Object>> items = Arrays.asList(
                    Map.of("modality", "TEXT", "url", "https://example.com/doc.txt"),
                    Map.of("modality", "IMAGE", "url", "https://example.com/img.png")
            );

            Map<String, Object> request = Map.of(
                    "contentItems", items,
                    "outputFormat", "FULL"
            );

            MultimodalProcessingResponse response = new MultimodalProcessingResponse(
                    UUID.randomUUID().toString(),
                    Map.of(),
                    Arrays.asList(0.5),
                    "Summary",
                    List.of("tag1", "tag2")
            );

            when(multimodalProcessingService.processMultimodal(any(), eq(USER_ID)))
                    .thenReturn(response);

            mockMvc.perform(post("/api/v1/multimodal/process")
                            .header("X-User-Id", USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$.summary").value("Summary"))
                    .andExpect(jsonPath("$.tags").isArray());
        }
    }

    @Nested
    @DisplayName("Similarity Search Endpoints")
    class SearchEndpoints {

        @Test
        @DisplayName("POST /api/v1/multimodal/search - Should search similar content")
        void shouldSearchSimilarContent() throws Exception {
            float[] embedding = new float[768];
            Arrays.fill(embedding, 0.5f);

            Map<String, Object> request = Map.of(
                    "queryEmbedding", embedding,
                    "threshold", 0.7,
                    "limit", 10
            );

            SimilarContentResponse response = new SimilarContentResponse(
                    List.of(
                            new SimilarContentResponse.SimilarItem("content-1", 0.85),
                            new SimilarContentResponse.SimilarItem("content-2", 0.78)
                    )
            );

            when(multimodalProcessingService.searchSimilar(any())).thenReturn(response);

            mockMvc.perform(post("/api/v1/multimodal/search")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.similarItems").isArray())
                    .andExpect(jsonPath("$.similarItems.length()").value(2));

            verify(multimodalProcessingService).searchSimilar(any());
        }

        @Test
        @DisplayName("POST /api/v1/multimodal/search - Should reject invalid embedding dimension")
        void shouldRejectInvalidEmbeddingDimension() throws Exception {
            float[] wrongEmbedding = new float[512];

            Map<String, Object> request = Map.of(
                    "queryEmbedding", wrongEmbedding,
                    "threshold", 0.7,
                    "limit", 10
            );

            when(multimodalProcessingService.searchSimilar(any()))
                    .thenThrow(new MultimodalProcessingException("Invalid query embedding"));

            mockMvc.perform(post("/api/v1/multimodal/search")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").exists());
        }

        @Test
        @DisplayName("POST /api/v1/multimodal/search - Should handle empty results")
        void shouldHandleEmptyResults() throws Exception {
            float[] embedding = new float[768];
            Arrays.fill(embedding, 0.5f);

            Map<String, Object> request = Map.of(
                    "queryEmbedding", embedding,
                    "threshold", 0.9,
                    "limit", 10
            );

            SimilarContentResponse response = new SimilarContentResponse(Collections.emptyList());

            when(multimodalProcessingService.searchSimilar(any())).thenReturn(response);

            mockMvc.perform(post("/api/v1/multimodal/search")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.similarItems").isArray())
                    .andExpect(jsonPath("$.similarItems.length()").value(0));
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle processing exception")
        void shouldHandleProcessingException() throws Exception {
            Map<String, Object> request = Map.of(
                    "contentItems", List.of(
                            Map.of("modality", "TEXT", "url", "invalid-url")
                    )
            );

            when(multimodalProcessingService.processMultimodal(any(), any()))
                    .thenThrow(new MultimodalProcessingException("Invalid URL"));

            mockMvc.perform(post("/api/v1/multimodal/process")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.message").exists());
        }

        @Test
        @DisplayName("Should handle generic exception")
        void shouldHandleGenericException() throws Exception {
            Map<String, Object> request = Map.of(
                    "queryEmbedding", new float[768],
                    "threshold", 0.7,
                    "limit", 10
            );

            when(multimodalProcessingService.searchSimilar(any()))
                    .thenThrow(new RuntimeException("Unexpected error"));

            mockMvc.perform(post("/api/v1/multimodal/search")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.error").exists());
        }

        @Test
        @DisplayName("Should return standard error format")
        void shouldReturnStandardErrorFormat() throws Exception {
            Map<String, Object> request = Map.of(
                    "contentItems", List.of()
            );

            when(multimodalProcessingService.processMultimodal(any(), any()))
                    .thenThrow(new IllegalArgumentException("Invalid request"));

            mockMvc.perform(post("/api/v1/multimodal/process")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.message").exists())
                    .andExpect(jsonPath("$.timestamp").exists());
        }
    }

    @Nested
    @DisplayName("Request Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should validate content items are present")
        void shouldValidateContentItemsPresent() throws Exception {
            Map<String, Object> request = Map.of(
                    "outputFormat", "EMBEDDING"
            );

            mockMvc.perform(post("/api/v1/multimodal/process")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should validate threshold range")
        void shouldValidateThresholdRange() throws Exception {
            float[] embedding = new float[768];

            Map<String, Object> request = Map.of(
                    "queryEmbedding", embedding,
                    "threshold", 1.5,
                    "limit", 10
            );

            mockMvc.perform(post("/api/v1/multimodal/search")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should validate limit range")
        void shouldValidateLimitRange() throws Exception {
            float[] embedding = new float[768];

            Map<String, Object> request = Map.of(
                    "queryEmbedding", embedding,
                    "threshold", 0.7,
                    "limit", 0
            );

            mockMvc.perform(post("/api/v1/multimodal/search")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Response Mapping Tests")
    class ResponseMappingTests {

        @Test
        @DisplayName("Should map processing response correctly")
        void shouldMapProcessingResponse() throws Exception {
            Map<String, Object> item = Map.of(
                    "modality", "TEXT",
                    "url", "https://example.com/doc.txt"
            );

            Map<String, Object> request = Map.of(
                    "contentItems", List.of(item),
                    "outputFormat", "FULL"
            );

            MultimodalProcessingResponse serviceResponse = new MultimodalProcessingResponse(
                    UUID.randomUUID().toString(),
                    Map.of("TEXT", Arrays.asList(0.1, 0.2, 0.3)),
                    Arrays.asList(0.2),
                    "Generated summary",
                    List.of("ai", "multimodal", "processed")
            );

            when(multimodalProcessingService.processMultimodal(any(), any()))
                    .thenReturn(serviceResponse);

            mockMvc.perform(post("/api/v1/multimodal/process")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$.processingId").exists())
                    .andExpect(jsonPath("$.embeddings.TEXT").isArray())
                    .andExpect(jsonPath("$.summary").value("Generated summary"))
                    .andExpect(jsonPath("$.tags").isArray());
        }

        @Test
        @DisplayName("Should map search response correctly")
        void shouldMapSearchResponse() throws Exception {
            float[] embedding = new float[768];

            Map<String, Object> request = Map.of(
                    "queryEmbedding", embedding,
                    "threshold", 0.7,
                    "limit", 5
            );

            SimilarContentResponse serviceResponse = new SimilarContentResponse(
                    List.of(
                            new SimilarContentResponse.SimilarItem("id-1", 0.95),
                            new SimilarContentResponse.SimilarItem("id-2", 0.88),
                            new SimilarContentResponse.SimilarItem("id-3", 0.75)
                    )
            );

            when(multimodalProcessingService.searchSimilar(any())).thenReturn(serviceResponse);

            mockMvc.perform(post("/api/v1/multimodal/search")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.similarItems").isArray())
                    .andExpect(jsonPath("$.similarItems.length()").value(3))
                    .andExpect(jsonPath("$.similarItems[0].contentId").value("id-1"))
                    .andExpect(jsonPath("$.similarItems[0].similarity").value(0.95));
        }
    }
}
