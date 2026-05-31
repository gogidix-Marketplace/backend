package com.gogidix.aiservices.aicontentgenerationservice.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.aicontentgenerationservice.application.dto.request.GenerateContentRequest;
import com.gogidix.aiservices.aicontentgenerationservice.application.dto.request.OptimizeContentRequest;
import com.gogidix.aiservices.aicontentgenerationservice.application.dto.response.ContentResponse;
import com.gogidix.aiservices.aicontentgenerationservice.application.dto.response.OptimizationResponse;
import com.gogidix.aiservices.aicontentgenerationservice.application.service.ContentGenerationService;
import com.gogidix.aiservices.aicontentgenerationservice.domain.model.*;
import com.gogidix.aiservices.aicontentgenerationservice.shared.exception.ContentGenerationException;
import com.gogidix.aiservices.aicontentgenerationservice.shared.exception.ContentNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContentGenerationController.class)
@DisplayName("Content Generation Controller Interface Tests")
class ContentGenerationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ContentGenerationService contentGenerationService;

    private static final String CONTENT_ID = "content-123";
    private static final String USER_ID = "user-456";

    @Nested
    @DisplayName("Content Generation Endpoints")
    class GenerationEndpoints {

        @Test
        @DisplayName("POST /api/v1/content-generation/generate - Should generate content")
        void shouldGenerateContent() throws Exception {
            GenerateContentRequest request = GenerateContentRequest.builder()
                    .userId(USER_ID)
                    .prompt("Generate a product description for premium wireless headphones")
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .tone(ContentTone.PROFESSIONAL)
                    .language("en")
                    .build();

            ContentResponse response = ContentResponse.builder()
                    .contentId(CONTENT_ID)
                    .content("Premium wireless headphones with exceptional sound quality")
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .tone(ContentTone.PROFESSIONAL)
                    .status(GenerationStatus.COMPLETED)
                    .wordCount(8)
                    .relevanceScore(0.9)
                    .build();

            when(contentGenerationService.generateContent(any(GenerateContentRequest.class)))
                    .thenReturn(response);

            mockMvc.perform(post("/api/v1/content-generation/generate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.contentId").value(CONTENT_ID))
                    .andExpect(jsonPath("$.content").value("Premium wireless headphones with exceptional sound quality"))
                    .andExpect(jsonPath("$.contentType").value("PRODUCT_DESCRIPTION"))
                    .andExpect(jsonPath("$.status").value("COMPLETED"));

            verify(contentGenerationService).generateContent(any(GenerateContentRequest.class));
        }

        @Test
        @DisplayName("POST /api/v1/content-generation/generate - Should reject short prompt")
        void shouldRejectShortPrompt() throws Exception {
            GenerateContentRequest request = GenerateContentRequest.builder()
                    .userId(USER_ID)
                    .prompt("Short")
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .build();

            mockMvc.perform(post("/api/v1/content-generation/generate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verify(contentGenerationService, never()).generateContent(any());
        }

        @Test
        @DisplayName("POST /api/v1/content-generation/generate - Should reject missing user ID")
        void shouldRejectMissingUserId() throws Exception {
            GenerateContentRequest request = GenerateContentRequest.builder()
                    .prompt("Generate a product description for premium wireless headphones")
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .build();

            mockMvc.perform(post("/api/v1/content-generation/generate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("POST /api/v1/content-generation/generate-variations - Should generate variations")
        void shouldGenerateVariations() throws Exception {
            GenerateContentRequest request = GenerateContentRequest.builder()
                    .userId(USER_ID)
                    .prompt("Generate product descriptions")
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .variations(3)
                    .build();

            ContentResponse response = ContentResponse.builder()
                    .contentId(CONTENT_ID)
                    .content("Primary variation")
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .status(GenerationStatus.COMPLETED)
                    .build();

            when(contentGenerationService.generateVariations(any(GenerateContentRequest.class)))
                    .thenReturn(response);

            mockMvc.perform(post("/api/v1/content-generation/generate-variations")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.contentId").exists());

            verify(contentGenerationService).generateVariations(any(GenerateContentRequest.class));
        }
    }

    @Nested
    @DisplayName("Content Retrieval Endpoints")
    class RetrievalEndpoints {

        @Test
        @DisplayName("GET /api/v1/content-generation/content/{contentId} - Should get content")
        void shouldGetContent() throws Exception {
            ContentResponse response = ContentResponse.builder()
                    .contentId(CONTENT_ID)
                    .content("Generated content")
                    .contentType(ContentType.BLOG_POST)
                    .status(GenerationStatus.COMPLETED)
                    .build();

            when(contentGenerationService.getContent(CONTENT_ID)).thenReturn(response);

            mockMvc.perform(get("/api/v1/content-generation/content/{contentId}", CONTENT_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.contentId").value(CONTENT_ID))
                    .andExpect(jsonPath("$.content").value("Generated content"));

            verify(contentGenerationService).getContent(CONTENT_ID);
        }

        @Test
        @DisplayName("GET /api/v1/content-generation/content/{contentId} - Should return 404 when not found")
        void shouldReturn404WhenNotFound() throws Exception {
            when(contentGenerationService.getContent(CONTENT_ID))
                    .thenThrow(new ContentNotFoundException("Content not found"));

            mockMvc.perform(get("/api/v1/content-generation/content/{contentId}", CONTENT_ID))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").exists());

            verify(contentGenerationService).getContent(CONTENT_ID);
        }

        @Test
        @DisplayName("GET /api/v1/content-generation/content/user/{userId} - Should get user content")
        void shouldGetUserContent() throws Exception {
            List<ContentResponse> responses = List.of(
                    ContentResponse.builder()
                            .contentId(CONTENT_ID)
                            .content("Content 1")
                            .contentType(ContentType.PRODUCT_DESCRIPTION)
                            .build(),
                    ContentResponse.builder()
                            .contentId(CONTENT_ID + "2")
                            .content("Content 2")
                            .contentType(ContentType.BLOG_POST)
                            .build()
            );

            when(contentGenerationService.getUserContent(USER_ID)).thenReturn(responses);

            mockMvc.perform(get("/api/v1/content-generation/content/user/{userId}", USER_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].contentId").value(CONTENT_ID))
                    .andExpect(jsonPath("$[1].contentId").value(CONTENT_ID + "2"));

            verify(contentGenerationService).getUserContent(USER_ID);
        }

        @Test
        @DisplayName("GET /api/v1/content-generation/content/user/{userId} - Should return empty list")
        void shouldReturnEmptyList() throws Exception {
            when(contentGenerationService.getUserContent(USER_ID)).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/api/v1/content-generation/content/user/{userId}", USER_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").isEmpty());
        }

        @Test
        @DisplayName("DELETE /api/v1/content-generation/content/{contentId} - Should delete content")
        void shouldDeleteContent() throws Exception {
            doNothing().when(contentGenerationService).deleteContent(CONTENT_ID);

            mockMvc.perform(delete("/api/v1/content-generation/content/{contentId}", CONTENT_ID))
                    .andExpect(status().isNoContent());

            verify(contentGenerationService).deleteContent(CONTENT_ID);
        }
    }

    @Nested
    @DisplayName("Content Optimization Endpoints")
    class OptimizationEndpoints {

        @Test
        @DisplayName("POST /api/v1/content-generation/optimize - Should optimize content")
        void shouldOptimizeContent() throws Exception {
            OptimizeContentRequest request = OptimizeContentRequest.builder()
                    .content("Original content that needs optimization")
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .optimizationGoal("seo")
                    .build();

            OptimizationResponse response = OptimizationResponse.builder()
                    .originalContent("Original content that needs optimization")
                    .optimizedContent("Optimized content with improved SEO")
                    .improvements("SEO optimization: keywords added, meta tags improved")
                    .originalWordCount(6)
                    .optimizedWordCount(6)
                    .build();

            when(contentGenerationService.optimizeContent(any(OptimizeContentRequest.class)))
                    .thenReturn(response);

            mockMvc.perform(post("/api/v1/content-generation/optimize")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.optimizedContent").value("Optimized content with improved SEO"))
                    .andExpect(jsonPath("$.improvements").exists());

            verify(contentGenerationService).optimizeContent(any(OptimizeContentRequest.class));
        }

        @Test
        @DisplayName("POST /api/v1/content-generation/optimize - Should reject empty content")
        void shouldRejectEmptyContent() throws Exception {
            OptimizeContentRequest request = OptimizeContentRequest.builder()
                    .content("")
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .build();

            when(contentGenerationService.optimizeContent(any(OptimizeContentRequest.class)))
                    .thenThrow(new IllegalArgumentException("Content to optimize cannot be empty"));

            mockMvc.perform(post("/api/v1/content-generation/optimize")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").exists());
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle content generation exception")
        void shouldHandleContentGenerationException() throws Exception {
            GenerateContentRequest request = GenerateContentRequest.builder()
                    .userId(USER_ID)
                    .prompt("Generate a product description")
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .build();

            when(contentGenerationService.generateContent(any()))
                    .thenThrow(new ContentGenerationException("AI service unavailable"));

            mockMvc.perform(post("/api/v1/content-generation/generate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.error").exists());
        }

        @Test
        @DisplayName("Should handle malformed JSON")
        void shouldHandleMalformedJson() throws Exception {
            mockMvc.perform(post("/api/v1/content-generation/generate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{invalid json"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return standard error format")
        void shouldReturnStandardErrorFormat() throws Exception {
            when(contentGenerationService.getContent(CONTENT_ID))
                    .thenThrow(new ContentGenerationException("Service error"));

            mockMvc.perform(get("/api/v1/content-generation/content/{contentId}", CONTENT_ID))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.error").exists())
                    .andExpect(jsonPath("$.message").exists())
                    .andExpect(jsonPath("$.timestamp").exists());
        }
    }

    @Nested
    @DisplayName("Request Validation Tests")
    class ValidationTests {

        @Test
        @DisplayName("Should validate content type is present")
        void shouldValidateContentType() throws Exception {
            GenerateContentRequest request = new GenerateContentRequest();

            mockMvc.perform(post("/api/v1/content-generation/generate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should validate prompt length")
        void shouldValidatePromptLength() throws Exception {
            GenerateContentRequest request = GenerateContentRequest.builder()
                    .userId(USER_ID)
                    .prompt("A".repeat(5001))
                    .contentType(ContentType.PRODUCT_DESCRIPTION)
                    .build();

            mockMvc.perform(post("/api/v1/content-generation/generate")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }
}
