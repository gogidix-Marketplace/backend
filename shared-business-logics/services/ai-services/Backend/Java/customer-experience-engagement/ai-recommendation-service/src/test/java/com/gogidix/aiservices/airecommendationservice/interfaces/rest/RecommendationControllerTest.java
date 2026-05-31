package com.gogidix.aiservices.airecommendationservice.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.airecommendationservice.application.service.RecommendationService;
import com.gogidix.aiservices.airecommendationservice.domain.model.*;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecommendationController.class)
@DisplayName("RecommendationController REST API Tests")
class RecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RecommendationService recommendationService;

    private RecommendationResult mockResult;
    private List<RecommendationItem> mockItems;

    @BeforeEach
    void setUp() {
        mockItems = List.of(
                RecommendationItem.builder()
                        .itemId("item-1")
                        .title("Test Product 1")
                        .category("electronics")
                        .score(0.9)
                        .reason(RecommendationReason.COLLABORATIVE)
                        .build(),
                RecommendationItem.builder()
                        .itemId("item-2")
                        .title("Test Product 2")
                        .category("books")
                        .score(0.85)
                        .reason(RecommendationReason.CONTENT_BASED)
                        .build()
        );

        mockResult = RecommendationResult.builder()
                .requestId("req-123")
                .userId("user-123")
                .type(RecommendationType.PERSONALIZED)
                .items(mockItems)
                .sessionId("session-456")
                .generatedAt(Instant.now())
                .algorithm("hybrid")
                .confidence(0.92)
                .build();
    }

    @Nested
    @DisplayName("GET /api/v1/recommendations/{userId} - Get Recommendations")
    class GetRecommendationsTests {

        @Test
        @DisplayName("Should return recommendations for valid user")
        void shouldReturnRecommendations() throws Exception {
            when(recommendationService.getRecommendations(
                    eq("user-123"),
                    eq(RecommendationType.PERSONALIZED),
                    eq("homepage"),
                    eq(10)
            )).thenReturn(mockResult);

            mockMvc.perform(get("/api/v1/recommendations/user-123")
                            .param("type", "PERSONALIZED")
                            .param("context", "homepage")
                            .param("limit", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.requestId").value("req-123"))
                    .andExpect(jsonPath("$.userId").value("user-123"))
                    .andExpect(jsonPath("$.items.length()").value(2))
                    .andExpect(jsonPath("$.items[0].itemId").value("item-1"))
                    .andExpect(jsonPath("$.items[0].score").value(0.9));

            verify(recommendationService).getRecommendations(
                    eq("user-123"),
                    eq(RecommendationType.PERSONALIZED),
                    eq("homepage"),
                    eq(10)
            );
        }

        @Test
        @DisplayName("Should use default type parameter")
        void shouldUseDefaultType() throws Exception {
            when(recommendationService.getRecommendations(
                    anyString(),
                    eq(RecommendationType.PERSONALIZED),
                    eq("homepage"),
                    eq(10)
            )).thenReturn(mockResult);

            mockMvc.perform(get("/api/v1/recommendations/user-123")
                            .param("context", "homepage"))
                    .andExpect(status().isOk());

            verify(recommendationService).getRecommendations(
                    anyString(),
                    eq(RecommendationType.PERSONALIZED),
                    eq("homepage"),
                    eq(10)
            );
        }

        @Test
        @DisplayName("Should use default context parameter")
        void shouldUseDefaultContext() throws Exception {
            when(recommendationService.getRecommendations(
                    anyString(),
                    any(RecommendationType.class),
                    eq("homepage"),
                    eq(10)
            )).thenReturn(mockResult);

            mockMvc.perform(get("/api/v1/recommendations/user-123"))
                    .andExpect(status().isOk());

            verify(recommendationService).getRecommendations(
                    anyString(),
                    any(RecommendationType.class),
                    eq("homepage"),
                    eq(10)
            );
        }

        @Test
        @DisplayName("Should use default limit parameter")
        void shouldUseDefaultLimit() throws Exception {
            when(recommendationService.getRecommendations(
                    anyString(),
                    any(RecommendationType.class),
                    anyString(),
                    eq(10)
            )).thenReturn(mockResult);

            mockMvc.perform(get("/api/v1/recommendations/user-123"))
                    .andExpect(status().isOk());

            verify(recommendationService).getRecommendations(
                    anyString(),
                    any(RecommendationType.class),
                    anyString(),
                    eq(10)
            );
        }

        @Test
        @DisplayName("Should handle custom limit parameter")
        void shouldHandleCustomLimit() throws Exception {
            when(recommendationService.getRecommendations(
                    anyString(),
                    any(RecommendationType.class),
                    anyString(),
                    eq(25)
            )).thenReturn(mockResult);

            mockMvc.perform(get("/api/v1/recommendations/user-123")
                            .param("limit", "25"))
                    .andExpect(status().isOk());

            verify(recommendationService).getRecommendations(
                    anyString(),
                    any(RecommendationType.class),
                    anyString(),
                    eq(25)
            );
        }
    }

    @Nested
    @DisplayName("GET /api/v1/recommendations/similar/{itemId} - Get Similar Items")
    class GetSimilarItemsTests {

        @Test
        @DisplayName("Should return similar items for valid item")
        void shouldReturnSimilarItems() throws Exception {
            when(recommendationService.getSimilarItems(eq("item-1"), eq(10)))
                    .thenReturn(mockItems);

            mockMvc.perform(get("/api/v1/recommendations/similar/item-1")
                            .param("limit", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].itemId").value("item-1"))
                    .andExpect(jsonPath("$[0].score").value(0.9))
                    .andExpect(jsonPath("$[1].itemId").value("item-2"))
                    .andExpect(jsonPath("$.length()").value(2));

            verify(recommendationService).getSimilarItems(eq("item-1"), eq(10));
        }

        @Test
        @DisplayName("Should use default limit for similar items")
        void shouldUseDefaultLimitForSimilar() throws Exception {
            when(recommendationService.getSimilarItems(anyString(), eq(10)))
                    .thenReturn(mockItems);

            mockMvc.perform(get("/api/v1/recommendations/similar/item-1"))
                    .andExpect(status().isOk());

            verify(recommendationService).getSimilarItems(anyString(), eq(10));
        }

        @Test
        @DisplayName("Should return empty list when no similar items found")
        void shouldReturnEmptyListWhenNoSimilarItems() throws Exception {
            when(recommendationService.getSimilarItems(anyString(), anyInt()))
                    .thenReturn(List.of());

            mockMvc.perform(get("/api/v1/recommendations/similar/item-999"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/recommendations/trending - Get Trending Items")
    class GetTrendingItemsTests {

        @Test
        @DisplayName("Should return trending items with category filter")
        void shouldReturnTrendingItemsWithCategory() throws Exception {
            when(recommendationService.getTrendingItems(eq("electronics"), eq(10)))
                    .thenReturn(mockItems);

            mockMvc.perform(get("/api/v1/recommendations/trending")
                            .param("category", "electronics")
                            .param("limit", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].category").value("electronics"))
                    .andExpect(jsonPath("$.length()").value(2));

            verify(recommendationService).getTrendingItems(eq("electronics"), eq(10));
        }

        @Test
        @DisplayName("Should return trending items without category filter")
        void shouldReturnTrendingItemsWithoutCategory() throws Exception {
            when(recommendationService.getTrendingItems(isNull(), eq(10)))
                    .thenReturn(mockItems);

            mockMvc.perform(get("/api/v1/recommendations/trending"))
                    .andExpect(status().isOk());

            verify(recommendationService).getTrendingItems(isNull(), eq(10));
        }

        @Test
        @DisplayName("Should use default limit for trending items")
        void shouldUseDefaultLimitForTrending() throws Exception {
            when(recommendationService.getTrendingItems(anyString(), eq(10)))
                    .thenReturn(mockItems);

            mockMvc.perform(get("/api/v1/recommendations/trending")
                            .param("category", "books"))
                    .andExpect(status().isOk());

            verify(recommendationService).getTrendingItems(anyString(), eq(10));
        }
    }

    @Nested
    @DisplayName("POST /api/v1/recommendations/interactions - Track Interaction")
    class TrackInteractionTests {

        @Test
        @DisplayName("Should track interaction successfully")
        void shouldTrackInteraction() throws Exception {
            doNothing().when(recommendationService).trackInteraction(
                    eq("user-123"),
                    eq("item-456"),
                    eq("click")
            );

            mockMvc.perform(post("/api/v1/recommendations/interactions")
                            .param("userId", "user-123")
                            .param("itemId", "item-456")
                            .param("interactionType", "click"))
                    .andExpect(status().isNoContent());

            verify(recommendationService).trackInteraction(
                    eq("user-123"),
                    eq("item-456"),
                    eq("click")
            );
        }

        @Test
        @DisplayName("Should handle view interaction type")
        void shouldHandleViewInteraction() throws Exception {
            doNothing().when(recommendationService).trackInteraction(
                    anyString(),
                    anyString(),
                    eq("view")
            );

            mockMvc.perform(post("/api/v1/recommendations/interactions")
                            .param("userId", "user-123")
                            .param("itemId", "item-789")
                            .param("interactionType", "view"))
                    .andExpect(status().isNoContent());

            verify(recommendationService).trackInteraction(
                    anyString(),
                    anyString(),
                    eq("view")
            );
        }

        @Test
        @DisplayName("Should handle purchase interaction type")
        void shouldHandlePurchaseInteraction() throws Exception {
            doNothing().when(recommendationService).trackInteraction(
                    anyString(),
                    anyString(),
                    eq("purchase")
            );

            mockMvc.perform(post("/api/v1/recommendations/interactions")
                            .param("userId", "user-123")
                            .param("itemId", "item-999")
                            .param("interactionType", "purchase"))
                    .andExpect(status().isNoContent());

            verify(recommendationService).trackInteraction(
                    anyString(),
                    anyString(),
                    eq("purchase")
            );
        }
    }

    @Nested
    @DisplayName("Response Format Tests")
    class ResponseFormatTests {

        @Test
        @DisplayName("Should return proper JSON structure")
        void shouldReturnProperJsonStructure() throws Exception {
            when(recommendationService.getRecommendations(
                    anyString(),
                    any(RecommendationType.class),
                    anyString(),
                    anyInt()
            )).thenReturn(mockResult);

            mockMvc.perform(get("/api/v1/recommendations/user-123")
                            .param("type", "PERSONALIZED"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.requestId").exists())
                    .andExpect(jsonPath("$.userId").exists())
                    .andExpect(jsonPath("$.type").exists())
                    .andExpect(jsonPath("$.items").isArray())
                    .andExpect(jsonPath("$.generatedAt").exists());
        }
    }
}
