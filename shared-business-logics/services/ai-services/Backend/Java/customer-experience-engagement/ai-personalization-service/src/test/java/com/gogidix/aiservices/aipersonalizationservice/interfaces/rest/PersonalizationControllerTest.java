package com.gogidix.aiservices.aipersonalizationservice.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.aipersonalizationservice.application.dto.request.CreateProfileRequest;
import com.gogidix.aiservices.aipersonalizationservice.application.dto.request.TrackBehaviorRequest;
import com.gogidix.aiservices.aipersonalizationservice.application.dto.request.UpdateAttributesRequest;
import com.gogidix.aiservices.aipersonalizationservice.application.dto.response.ProfileResponse;
import com.gogidix.aiservices.aipersonalizationservice.application.dto.response.RecommendationResponse;
import com.gogidix.aiservices.aipersonalizationservice.application.dto.response.BehaviorTrackingResponse;
import com.gogidix.aiservices.aipersonalizationservice.application.service.PersonalizationService;
import com.gogidix.aiservices.aipersonalizationservice.domain.model.*;
import com.gogidix.aiservices.aipersonalizationservice.shared.exception.PersonalizationException;
import com.gogidix.aiservices.aipersonalizationservice.shared.exception.ProfileNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonalizationController.class)
@DisplayName("Personalization Controller Interface Tests")
class PersonalizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonalizationService personalizationService;

    private static final String USER_ID = "550e8400-e29b-41d4-a716-446655440000";
    private static final String PROFILE_ID = "660e8400-e29b-41d4-a716-446655440000";

    @Nested
    @DisplayName("Profile Management Endpoints")
    class ProfileEndpoints {

        @Test
        @DisplayName("POST /api/v1/personalization/profiles - Should create profile")
        void shouldCreateProfile() throws Exception {
            CreateProfileRequest request = CreateProfileRequest.builder()
                    .userId(USER_ID)
                    .attributes(createValidAttributes())
                    .build();

            ProfileResponse response = ProfileResponse.builder()
                    .profileId(UUID.randomUUID().toString())
                    .userId(USER_ID)
                    .segment(Segment.NEW_USER)
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();

            when(personalizationService.createProfile(any(CreateProfileRequest.class)))
                    .thenReturn(response);

            mockMvc.perform(post("/api/v1/personalization/profiles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.userId").value(USER_ID))
                    .andExpect(jsonPath("$.segment").value("NEW_USER"))
                    .andExpect(jsonPath("$.profileId").exists())
                    .andExpect(jsonPath("$.createdAt").exists());

            verify(personalizationService).createProfile(any(CreateProfileRequest.class));
        }

        @Test
        @DisplayName("POST /api/v1/personalization/profiles - Should reject invalid user ID")
        void shouldRejectInvalidUserId() throws Exception {
            CreateProfileRequest request = CreateProfileRequest.builder()
                    .userId("invalid-uuid")
                    .attributes(createValidAttributes())
                    .build();

            mockMvc.perform(post("/api/v1/personalization/profiles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verify(personalizationService, never()).createProfile(any());
        }

        @Test
        @DisplayName("POST /api/v1/personalization/profiles - Should reject missing user ID")
        void shouldRejectMissingUserId() throws Exception {
            Map<String, Object> request = new HashMap<>();
            request.put("attributes", createValidAttributes());

            mockMvc.perform(post("/api/v1/personalization/profiles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("GET /api/v1/personalization/profiles/{userId} - Should get profile")
        void shouldGetProfile() throws Exception {
            ProfileResponse response = ProfileResponse.builder()
                    .profileId(PROFILE_ID)
                    .userId(USER_ID)
                    .segment(Segment.ACTIVE)
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();

            when(personalizationService.getProfile(USER_ID)).thenReturn(response);

            mockMvc.perform(get("/api/v1/personalization/profiles/{userId}", USER_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").value(USER_ID))
                    .andExpect(jsonPath("$.segment").value("ACTIVE"));

            verify(personalizationService).getProfile(USER_ID);
        }

        @Test
        @DisplayName("GET /api/v1/personalization/profiles/{userId} - Should return 404 when not found")
        void shouldReturn404WhenProfileNotFound() throws Exception {
            when(personalizationService.getProfile(USER_ID))
                    .thenThrow(new ProfileNotFoundException("Profile not found"));

            mockMvc.perform(get("/api/v1/personalization/profiles/{userId}", USER_ID))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").exists());

            verify(personalizationService).getProfile(USER_ID);
        }

        @Test
        @DisplayName("PUT /api/v1/personalization/profiles/{userId} - Should update attributes")
        void shouldUpdateAttributes() throws Exception {
            UpdateAttributesRequest request = UpdateAttributesRequest.builder()
                    .userId(USER_ID)
                    .attributes(createValidAttributes())
                    .build();

            ProfileResponse response = ProfileResponse.builder()
                    .profileId(PROFILE_ID)
                    .userId(USER_ID)
                    .segment(Segment.ACTIVE)
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();

            when(personalizationService.updateAttributes(any(UpdateAttributesRequest.class)))
                    .thenReturn(response);

            mockMvc.perform(put("/api/v1/personalization/profiles/{userId}", USER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").value(USER_ID));

            verify(personalizationService).updateAttributes(any(UpdateAttributesRequest.class));
        }

        @Test
        @DisplayName("DELETE /api/v1/personalization/profiles/{userId} - Should delete profile")
        void shouldDeleteProfile() throws Exception {
            doNothing().when(personalizationService).deleteProfile(USER_ID);

            mockMvc.perform(delete("/api/v1/personalization/profiles/{userId}", USER_ID))
                    .andExpect(status().isNoContent());

            verify(personalizationService).deleteProfile(USER_ID);
        }
    }

    @Nested
    @DisplayName("Recommendation Endpoints")
    class RecommendationEndpoints {

        @Test
        @DisplayName("GET /api/v1/personalization/recommendations/{userId} - Should get recommendations")
        void shouldGetRecommendations() throws Exception {
            List<RecommendationResponse.ItemRecommendation> recs = Arrays.asList(
                    new RecommendationResponse.ItemRecommendation("item-1", 0.9, "Based on your browsing behavior", "electronics"),
                    new RecommendationResponse.ItemRecommendation("item-2", 0.8, "Users like you also liked this", "books")
            );

            RecommendationResponse response = RecommendationResponse.builder()
                    .userId(USER_ID)
                    .recommendations(recs)
                    .metadata(new RecommendationResponse.Metadata("collaborative_filtering", Instant.now()))
                    .build();

            when(personalizationService.getRecommendations(eq(USER_ID), eq(10), eq("content"), eq("homepage")))
                    .thenReturn(response);

            mockMvc.perform(get("/api/v1/personalization/recommendations/{userId}", USER_ID)
                            .param("limit", "10")
                            .param("type", "content")
                            .param("context", "homepage"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").value(USER_ID))
                    .andExpect(jsonPath("$.recommendations").isArray())
                    .andExpect(jsonPath("$.recommendations[0].itemId").value("item-1"))
                    .andExpect(jsonPath("$.recommendations[0].score").value(0.9))
                    .andExpect(jsonPath("$.metadata.algorithm").value("collaborative_filtering"));

            verify(personalizationService).getRecommendations(eq(USER_ID), eq(10), eq("content"), eq("homepage"));
        }

        @Test
        @DisplayName("GET /api/v1/personalization/recommendations/{userId} - Should use default limit")
        void shouldUseDefaultLimit() throws Exception {
            RecommendationResponse response = RecommendationResponse.builder()
                    .userId(USER_ID)
                    .recommendations(Collections.emptyList())
                    .metadata(new RecommendationResponse.Metadata("default", Instant.now()))
                    .build();

            when(personalizationService.getRecommendations(eq(USER_ID), eq(10), eq("content"), eq("homepage")))
                    .thenReturn(response);

            mockMvc.perform(get("/api/v1/personalization/recommendations/{userId}", USER_ID))
                    .andExpect(status().isOk());

            verify(personalizationService).getRecommendations(eq(USER_ID), eq(10), eq("content"), eq("homepage"));
        }

        @Test
        @DisplayName("GET /api/v1/personalization/recommendations/{userId} - Should reject invalid limit")
        void shouldRejectInvalidLimit() throws Exception {
            mockMvc.perform(get("/api/v1/personalization/recommendations/{userId}", USER_ID)
                            .param("limit", "150"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").exists());

            verify(personalizationService, never()).getRecommendations(any(), anyInt(), any(), any());
        }

        @Test
        @DisplayName("GET /api/v1/personalization/recommendations/{userId} - Should return 400 when not eligible")
        void shouldReturn400WhenNotEligible() throws Exception {
            when(personalizationService.getRecommendations(eq(USER_ID), eq(10), eq("content"), eq("homepage")))
                    .thenThrow(new PersonalizationException("User not eligible for recommendations"));

            mockMvc.perform(get("/api/v1/personalization/recommendations/{userId}", USER_ID))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").exists());
        }

        @Test
        @DisplayName("GET /api/v1/personalization/recommendations/{userId} - Should validate type parameter")
        void shouldValidateTypeParameter() throws Exception {
            mockMvc.perform(get("/api/v1/personalization/recommendations/{userId}", USER_ID)
                            .param("type", "invalid_type"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Behavior Tracking Endpoints")
    class BehaviorEndpoints {

        @Test
        @DisplayName("POST /api/v1/personalization/behaviors - Should track behaviors")
        void shouldTrackBehaviors() throws Exception {
            TrackBehaviorRequest request = TrackBehaviorRequest.builder()
                    .userId(USER_ID)
                    .sessionId("session-123")
                    .events(Arrays.asList(
                            createEventRequest(EventType.VIEW, "item-1"),
                            createEventRequest(EventType.CLICK, "item-2")
                    ))
                    .build();

            BehaviorTrackingResponse response = BehaviorTrackingResponse.builder()
                    .eventsProcessed(2)
                    .profileUpdated(true)
                    .build();

            when(personalizationService.trackBehaviors(any(TrackBehaviorRequest.class)))
                    .thenReturn(response);

            mockMvc.perform(post("/api/v1/personalization/behaviors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$.eventsProcessed").value(2))
                    .andExpect(jsonPath("$.profileUpdated").value(true));

            verify(personalizationService).trackBehaviors(any(TrackBehaviorRequest.class));
        }

        @Test
        @DisplayName("POST /api/v1/personalization/behaviors - Should reject empty events")
        void shouldRejectEmptyEvents() throws Exception {
            TrackBehaviorRequest request = TrackBehaviorRequest.builder()
                    .userId(USER_ID)
                    .sessionId("session-123")
                    .events(Collections.emptyList())
                    .build();

            mockMvc.perform(post("/api/v1/personalization/behaviors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verify(personalizationService, never()).trackBehaviors(any());
        }

        @Test
        @DisplayName("POST /api/v1/personalization/behaviors - Should reject missing session ID")
        void shouldRejectMissingSessionId() throws Exception {
            Map<String, Object> request = new HashMap<>();
            request.put("userId", USER_ID);
            request.put("events", Arrays.asList(createEventRequest(EventType.VIEW, "item-1")));

            mockMvc.perform(post("/api/v1/personalization/behaviors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("POST /api/v1/personalization/behaviors - Should validate event type")
        void shouldValidateEventType() throws Exception {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", "INVALID_TYPE");
            event.put("itemId", "item-1");
            event.put("timestamp", Instant.now().toString());

            Map<String, Object> request = new HashMap<>();
            request.put("userId", USER_ID);
            request.put("sessionId", "session-123");
            request.put("events", Collections.singletonList(event));

            mockMvc.perform(post("/api/v1/personalization/behaviors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("POST /api/v1/personalization/behaviors - Should reject oversized batch")
        void shouldRejectOversizedBatch() throws Exception {
            List<Map<String, Object>> events = new ArrayList<>();
            for (int i = 0; i < 1001; i++) {
                events.add(Map.of(
                        "eventType", "VIEW",
                        "itemId", "item-" + i,
                        "timestamp", Instant.now().toString()
                ));
            }

            Map<String, Object> request = new HashMap<>();
            request.put("userId", USER_ID);
            request.put("sessionId", "session-123");
            request.put("events", events);

            mockMvc.perform(post("/api/v1/personalization/behaviors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle invalid UUID format")
        void shouldHandleInvalidUuid() throws Exception {
            mockMvc.perform(get("/api/v1/personalization/profiles/{userId}", "not-a-uuid"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should handle malformed JSON")
        void shouldHandleMalformedJson() throws Exception {
            mockMvc.perform(post("/api/v1/personalization/profiles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{invalid json"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should handle unsupported media type")
        void shouldHandleUnsupportedMediaType() throws Exception {
            mockMvc.perform(post("/api/v1/personalization/profiles")
                            .contentType(MediaType.TEXT_PLAIN)
                            .content("some text"))
                    .andExpect(status().isUnsupportedMediaType());
        }

        @Test
        @DisplayName("Should return standard error format")
        void shouldReturnStandardErrorFormat() throws Exception {
            when(personalizationService.getProfile(USER_ID))
                    .thenThrow(new PersonalizationException("Service error"));

            mockMvc.perform(get("/api/v1/personalization/profiles/{userId}", USER_ID))
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
        @DisplayName("Should validate demographics age range")
        void shouldValidateAgeRange() throws Exception {
            Map<String, Object> demographics = new HashMap<>();
            demographics.put("age", -1);
            demographics.put("gender", "male");
            demographics.put("location", "US");

            Map<String, Object> attributes = new HashMap<>();
            attributes.put("demographics", demographics);

            CreateProfileRequest request = CreateProfileRequest.builder()
                    .userId(USER_ID)
                    .attributes(UserAttributes.builder()
                            .demographics(Demographics.builder()
                                    .age(-1)
                                    .gender("male")
                                    .location("US")
                                    .build())
                            .build())
                    .build();

            mockMvc.perform(post("/api/v1/personalization/profiles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should validate required fields")
        void shouldValidateRequiredFields() throws Exception {
            Map<String, Object> request = new HashMap<>();
            // Missing userId

            mockMvc.perform(post("/api/v1/personalization/profiles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Response Mapping Tests")
    class ResponseMappingTests {

        @Test
        @DisplayName("Should map profile response correctly")
        void shouldMapProfileResponse() throws Exception {
            ProfileResponse response = ProfileResponse.builder()
                    .profileId(PROFILE_ID)
                    .userId(USER_ID)
                    .segment(Segment.VIP)
                    .affinityScores(Map.of("electronics", 0.9, "books", 0.7))
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();

            when(personalizationService.getProfile(USER_ID)).thenReturn(response);

            mockMvc.perform(get("/api/v1/personalization/profiles/{userId}", USER_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.profileId").value(PROFILE_ID))
                    .andExpect(jsonPath("$.userId").value(USER_ID))
                    .andExpect(jsonPath("$.segment").value("VIP"))
                    .andExpect(jsonPath("$.affinityScores.electronics").value(0.9))
                    .andExpect(jsonPath("$.affinityScores.books").value(0.7));
        }

        @Test
        @DisplayName("Should map recommendation response correctly")
        void shouldMapRecommendationResponse() throws Exception {
            List<RecommendationResponse.ItemRecommendation> recs = Arrays.asList(
                    new RecommendationResponse.ItemRecommendation("item-1", 0.95, "Based on your browsing behavior", "electronics"),
                    new RecommendationResponse.ItemRecommendation("item-2", 0.85, "Users like you also liked this", "books")
            );

            RecommendationResponse response = RecommendationResponse.builder()
                    .userId(USER_ID)
                    .recommendations(recs)
                    .metadata(new RecommendationResponse.Metadata("hybrid", Instant.now()))
                    .build();

            when(personalizationService.getRecommendations(eq(USER_ID), eq(10), eq("content"), eq("homepage")))
                    .thenReturn(response);

            mockMvc.perform(get("/api/v1/personalization/recommendations/{userId}", USER_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.recommendations[0].itemId").value("item-1"))
                    .andExpect(jsonPath("$.recommendations[0].score").value(0.95))
                    .andExpect(jsonPath("$.recommendations[0].reason").value("Based on your browsing behavior"))
                    .andExpect(jsonPath("$.recommendations[0].category").value("electronics"))
                    .andExpect(jsonPath("$.metadata.algorithm").value("hybrid"));
        }
    }

    // Helper methods

    private UserAttributes createValidAttributes() {
        return UserAttributes.builder()
                .demographics(Demographics.builder()
                        .age(30)
                        .gender("male")
                        .location("US")
                        .build())
                .interests(Arrays.asList("technology", "sports"))
                .preferences(Preferences.builder()
                        .categories(Arrays.asList("electronics", "books"))
                        .brands(Arrays.asList("Apple", "Nike"))
                        .build())
                .build();
    }

    private TrackBehaviorRequest.BehaviorEventRequest createEventRequest(EventType type, String itemId) {
        return TrackBehaviorRequest.BehaviorEventRequest.builder()
                .eventType(type)
                .itemId(itemId)
                .timestamp(Instant.now())
                .properties(new HashMap<>())
                .build();
    }
}
