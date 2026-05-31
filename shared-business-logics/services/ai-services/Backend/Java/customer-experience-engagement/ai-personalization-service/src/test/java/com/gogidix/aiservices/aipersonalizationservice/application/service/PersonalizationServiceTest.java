package com.gogidix.aiservices.aipersonalizationservice.application.service;

import com.gogidix.aiservices.aipersonalizationservice.application.dto.request.CreateProfileRequest;
import com.gogidix.aiservices.aipersonalizationservice.application.dto.request.TrackBehaviorRequest;
import com.gogidix.aiservices.aipersonalizationservice.application.dto.request.UpdateAttributesRequest;
import com.gogidix.aiservices.aipersonalizationservice.application.dto.response.ProfileResponse;
import com.gogidix.aiservices.aipersonalizationservice.application.dto.response.RecommendationResponse;
import com.gogidix.aiservices.aipersonalizationservice.application.dto.response.BehaviorTrackingResponse;
import com.gogidix.aiservices.aipersonalizationservice.domain.aggregate.UserProfile;
import com.gogidix.aiservices.aipersonalizationservice.domain.event.BehaviorEvent;
import com.gogidix.aiservices.aipersonalizationservice.domain.model.*;
import com.gogidix.aiservices.aipersonalizationservice.domain.port.out.UserProfileRepository;
import com.gogidix.aiservices.aipersonalizationservice.domain.port.out.RecommendationEnginePort;
import com.gogidix.aiservices.aipersonalizationservice.domain.port.out.EventPublisherPort;
import com.gogidix.aiservices.aipersonalizationservice.shared.exception.PersonalizationException;
import com.gogidix.aiservices.aipersonalizationservice.shared.exception.ProfileNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Personalization Service Application Tests")
class PersonalizationServiceTest {

    @Mock
    private UserProfileRepository profileRepository;

    @Mock
    private RecommendationEnginePort recommendationEngine;

    @Mock
    private EventPublisherPort eventPublisher;

    @InjectMocks
    private PersonalizationService personalizationService;

    private static final String USER_ID = "550e8400-e29b-41d4-a716-446655440000";
    private static final String PROFILE_ID = "660e8400-e29b-41d4-a716-446655440000";

    @Nested
    @DisplayName("Profile Management Tests")
    class ProfileManagementTests {

        @Test
        @DisplayName("Should create new profile successfully")
        void shouldCreateNewProfile() {
            CreateProfileRequest request = CreateProfileRequest.builder()
                    .userId(USER_ID)
                    .attributes(createValidAttributes())
                    .build();

            UserProfile profile = UserProfile.create(USER_ID);
            profile.updateAttributes(createValidAttributes());

            when(profileRepository.save(any(UserProfile.class))).thenReturn(profile);

            ProfileResponse response = personalizationService.createProfile(request);

            assertThat(response).isNotNull();
            assertThat(response.userId()).isEqualTo(USER_ID);
            assertThat(response.segment()).isEqualTo(Segment.NEW_USER);
            verify(profileRepository).save(any(UserProfile.class));
            verify(eventPublisher).publish(any(), any());
        }

        @Test
        @DisplayName("Should update existing profile")
        void shouldUpdateExistingProfile() {
            UpdateAttributesRequest request = UpdateAttributesRequest.builder()
                    .userId(USER_ID)
                    .attributes(createValidAttributes())
                    .build();

            UserProfile existingProfile = UserProfile.create(USER_ID);
            when(profileRepository.findByUserId(USER_ID)).thenReturn(Optional.of(existingProfile));
            when(profileRepository.save(any(UserProfile.class))).thenReturn(existingProfile);

            ProfileResponse response = personalizationService.updateAttributes(request);

            assertThat(response).isNotNull();
            verify(profileRepository).save(any(UserProfile.class));
        }

        @Test
        @DisplayName("Should throw exception when profile not found for update")
        void shouldThrowWhenProfileNotFound() {
            UpdateAttributesRequest request = UpdateAttributesRequest.builder()
                    .userId(USER_ID)
                    .attributes(createValidAttributes())
                    .build();

            when(profileRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> personalizationService.updateAttributes(request))
                    .isInstanceOf(ProfileNotFoundException.class)
                    .hasMessageContaining("Profile not found");
        }

        @Test
        @DisplayName("Should get profile by user ID")
        void shouldGetProfile() {
            UserProfile profile = UserProfile.create(USER_ID);
            when(profileRepository.findByUserId(USER_ID)).thenReturn(Optional.of(profile));

            ProfileResponse response = personalizationService.getProfile(USER_ID);

            assertThat(response).isNotNull();
            assertThat(response.userId()).isEqualTo(USER_ID);
            verify(profileRepository).findByUserId(USER_ID);
        }

        @Test
        @DisplayName("Should throw exception when profile not found")
        void shouldThrowWhenGettingNonExistentProfile() {
            when(profileRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> personalizationService.getProfile(USER_ID))
                    .isInstanceOf(ProfileNotFoundException.class);
        }

        @Test
        @DisplayName("Should delete profile")
        void shouldDeleteProfile() {
            UserProfile profile = UserProfile.create(USER_ID);
            when(profileRepository.findByUserId(USER_ID)).thenReturn(Optional.of(profile));
            doNothing().when(profileRepository).delete(USER_ID);

            personalizationService.deleteProfile(USER_ID);

            verify(profileRepository).delete(USER_ID);
            verify(eventPublisher).publish(eq("profile.deleted"), any());
        }
    }

    @Nested
    @DisplayName("Recommendation Tests")
    class RecommendationTests {

        @Test
        @DisplayName("Should get recommendations for eligible user")
        void shouldGetRecommendations() {
            UserProfile profile = createEligibleProfile();
            when(profileRepository.findByUserId(USER_ID)).thenReturn(Optional.of(profile));

            List<Recommendation> mockRecommendations = Arrays.asList(
                    Recommendation.builder()
                            .itemId("item-1")
                            .score(0.9)
                            .reason(RecommendationReason.BEHAVIORAL)
                            .category("electronics")
                            .build(),
                    Recommendation.builder()
                            .itemId("item-2")
                            .score(0.8)
                            .reason(RecommendationReason.COLLABORATIVE)
                            .category("books")
                            .build()
            );

            when(recommendationEngine.generateRecommendations(eq(profile), any())).thenReturn(mockRecommendations);

            RecommendationResponse response = personalizationService.getRecommendations(USER_ID, 10, "content", "homepage");

            assertThat(response).isNotNull();
            assertThat(response.userId()).isEqualTo(USER_ID);
            assertThat(response.recommendations()).hasSize(2);
            assertThat(response.recommendations().get(0).itemId()).isEqualTo("item-1");
            assertThat(response.metadata().algorithm()).isNotNull();
            verify(recommendationEngine).generateRecommendations(eq(profile), any());
        }

        @Test
        @DisplayName("Should throw exception for ineligible user")
        void shouldThrowForIneligibleUser() {
            UserProfile profile = UserProfile.create(USER_ID);
            when(profileRepository.findByUserId(USER_ID)).thenReturn(Optional.of(profile));

            assertThatThrownBy(() -> personalizationService.getRecommendations(USER_ID, 10, "content", "homepage"))
                    .isInstanceOf(PersonalizationException.class)
                    .hasMessageContaining("not eligible for recommendations");
        }

        @Test
        @DisplayName("Should limit recommendations to requested count")
        void shouldLimitRecommendations() {
            UserProfile profile = createEligibleProfile();
            when(profileRepository.findByUserId(USER_ID)).thenReturn(Optional.of(profile));

            List<Recommendation> mockRecommendations = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                mockRecommendations.add(Recommendation.builder()
                        .itemId("item-" + i)
                        .score(0.9 - (i * 0.01))
                        .reason(RecommendationReason.BEHAVIORAL)
                        .build());
            }

            when(recommendationEngine.generateRecommendations(eq(profile), any())).thenReturn(mockRecommendations);

            RecommendationResponse response = personalizationService.getRecommendations(USER_ID, 10, "content", "homepage");

            assertThat(response.recommendations()).hasSize(10);
        }

        @Test
        @DisplayName("Should enforce maximum limit")
        void shouldEnforceMaxLimit() {
            UserProfile profile = createEligibleProfile();
            when(profileRepository.findByUserId(USER_ID)).thenReturn(Optional.of(profile));

            List<Recommendation> mockRecommendations = new ArrayList<>();
            for (int i = 0; i < 150; i++) {
                mockRecommendations.add(Recommendation.builder()
                        .itemId("item-" + i)
                        .score(0.9)
                        .reason(RecommendationReason.BEHAVIORAL)
                        .build());
            }

            when(recommendationEngine.generateRecommendations(eq(profile), any())).thenReturn(mockRecommendations);

            RecommendationResponse response = personalizationService.getRecommendations(USER_ID, 150, "content", "homepage");

            assertThat(response.recommendations()).hasSizeLessThanOrEqualTo(100);
        }

        @Test
        @DisplayName("Should filter recommendations below threshold")
        void shouldFilterBelowThreshold() {
            UserProfile profile = createEligibleProfile();
            when(profileRepository.findByUserId(USER_ID)).thenReturn(Optional.of(profile));

            List<Recommendation> mockRecommendations = Arrays.asList(
                    Recommendation.builder().itemId("item-1").score(0.9).reason(RecommendationReason.BEHAVIORAL).build(),
                    Recommendation.builder().itemId("item-2").score(0.2).reason(RecommendationReason.BEHAVIORAL).build(),
                    Recommendation.builder().itemId("item-3").score(0.5).reason(RecommendationReason.BEHAVIORAL).build()
            );

            when(recommendationEngine.generateRecommendations(eq(profile), any())).thenReturn(mockRecommendations);

            RecommendationResponse response = personalizationService.getRecommendations(USER_ID, 10, "content", "homepage");

            assertThat(response.recommendations()).allMatch(r -> r.score() >= 0.3);
        }
    }

    @Nested
    @DisplayName("Behavior Tracking Tests")
    class BehaviorTrackingTests {

        @Test
        @DisplayName("Should track behavior events successfully")
        void shouldTrackBehaviors() {
            TrackBehaviorRequest request = TrackBehaviorRequest.builder()
                    .userId(USER_ID)
                    .sessionId("session-123")
                    .events(Arrays.asList(
                            createBehaviorEventRequest(EventType.VIEW, "item-1"),
                            createBehaviorEventRequest(EventType.CLICK, "item-2"),
                            createBehaviorEventRequest(EventType.LIKE, "item-3")
                    ))
                    .build();

            UserProfile profile = UserProfile.create(USER_ID);
            when(profileRepository.findByUserId(USER_ID)).thenReturn(Optional.of(profile));
            when(profileRepository.save(any(UserProfile.class))).thenReturn(profile);

            BehaviorTrackingResponse response = personalizationService.trackBehaviors(request);

            assertThat(response).isNotNull();
            assertThat(response.eventsProcessed()).isEqualTo(3);
            assertThat(response.profileUpdated()).isTrue();
            verify(profileRepository).save(any(UserProfile.class));
        }

        @Test
        @DisplayName("Should create profile if not exists when tracking")
        void shouldCreateProfileWhenTracking() {
            TrackBehaviorRequest request = TrackBehaviorRequest.builder()
                    .userId(USER_ID)
                    .sessionId("session-123")
                    .events(Arrays.asList(
                            createBehaviorEventRequest(EventType.VIEW, "item-1")
                    ))
                    .build();

            when(profileRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());
            when(profileRepository.save(any(UserProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

            BehaviorTrackingResponse response = personalizationService.trackBehaviors(request);

            assertThat(response.eventsProcessed()).isEqualTo(1);
            verify(profileRepository).save(any(UserProfile.class));
        }

        @Test
        @DisplayName("Should reject empty events list")
        void shouldRejectEmptyEvents() {
            TrackBehaviorRequest request = TrackBehaviorRequest.builder()
                    .userId(USER_ID)
                    .sessionId("session-123")
                    .events(Collections.emptyList())
                    .build();

            assertThatThrownBy(() -> personalizationService.trackBehaviors(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("events cannot be empty");
        }

        @Test
        @DisplayName("Should limit events per batch")
        void shouldLimitEventsPerBatch() {
            List<TrackBehaviorRequest.BehaviorEventRequest> events = new ArrayList<>();
            for (int i = 0; i < 1001; i++) {
                events.add(createBehaviorEventRequest(EventType.VIEW, "item-" + i));
            }

            TrackBehaviorRequest request = TrackBehaviorRequest.builder()
                    .userId(USER_ID)
                    .sessionId("session-123")
                    .events(events)
                    .build();

            assertThatThrownBy(() -> personalizationService.trackBehaviors(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("exceeds maximum");
        }

        @Test
        @DisplayName("Should deduplicate identical events")
        void shouldDeduplicateEvents() {
            Instant timestamp = Instant.now();
            TrackBehaviorRequest request = TrackBehaviorRequest.builder()
                    .userId(USER_ID)
                    .sessionId("session-123")
                    .events(Arrays.asList(
                            createBehaviorEventRequestWithTimestamp(EventType.VIEW, "item-1", timestamp),
                            createBehaviorEventRequestWithTimestamp(EventType.VIEW, "item-1", timestamp)
                    ))
                    .build();

            UserProfile profile = UserProfile.create(USER_ID);
            when(profileRepository.findByUserId(USER_ID)).thenReturn(Optional.of(profile));
            when(profileRepository.save(any(UserProfile.class))).thenReturn(profile);

            BehaviorTrackingResponse response = personalizationService.trackBehaviors(request);

            assertThat(response.eventsProcessed()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should publish behavior events to message broker")
        void shouldPublishBehaviorEvents() {
            TrackBehaviorRequest request = TrackBehaviorRequest.builder()
                    .userId(USER_ID)
                    .sessionId("session-123")
                    .events(Arrays.asList(
                            createBehaviorEventRequest(EventType.VIEW, "item-1")
                    ))
                    .build();

            UserProfile profile = UserProfile.create(USER_ID);
            when(profileRepository.findByUserId(USER_ID)).thenReturn(Optional.of(profile));
            when(profileRepository.save(any(UserProfile.class))).thenReturn(profile);

            personalizationService.trackBehaviors(request);

            verify(eventPublisher, atLeastOnce()).publish(eq("behavior.tracked"), any());
        }
    }

    @Nested
    @DisplayName("Segment Recalculation Tests")
    class SegmentRecalculationTests {

        @Test
        @DisplayName("Should recalculate segment on interaction threshold")
        void shouldRecalculateSegment() {
            UserProfile profile = UserProfile.create(USER_ID);

            for (int i = 0; i < 50; i++) {
                profile.addBehaviorEvent(BehaviorEvent.builder()
                        .eventType(EventType.VIEW)
                        .itemId("item-" + i)
                        .timestamp(Instant.now())
                        .build());
            }

            when(profileRepository.findByUserId(USER_ID)).thenReturn(Optional.of(profile));
            when(profileRepository.save(any(UserProfile.class))).thenReturn(profile);

            personalizationService.recalculateSegment(USER_ID);

            assertThat(profile.getSegment()).isEqualTo(Segment.ACTIVE);
            verify(profileRepository).save(any(UserProfile.class));
        }

        @Test
        @DisplayName("Should trigger affinity recalculation with behaviors")
        void shouldRecalculateAffinity() {
            UserProfile profile = createEligibleProfile();
            when(profileRepository.findByUserId(USER_ID)).thenReturn(Optional.of(profile));
            when(profileRepository.save(any(UserProfile.class))).thenReturn(profile);

            TrackBehaviorRequest request = TrackBehaviorRequest.builder()
                    .userId(USER_ID)
                    .sessionId("session-123")
                    .events(Arrays.asList(
                            createBehaviorEventRequest(EventType.PURCHASE, "product-1")
                    ))
                    .build();

            personalizationService.trackBehaviors(request);

            verify(profileRepository).save(argThat(p -> !p.getAffinityScores().isEmpty()));
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle full workflow: create profile, track behaviors, get recommendations")
        void shouldHandleFullWorkflow() {
            // Create profile
            CreateProfileRequest createRequest = CreateProfileRequest.builder()
                    .userId(USER_ID)
                    .attributes(createValidAttributes())
                    .build();

            UserProfile profile = UserProfile.create(USER_ID);
            profile.updateAttributes(createValidAttributes());
            when(profileRepository.save(any(UserProfile.class))).thenReturn(profile);

            ProfileResponse profileResponse = personalizationService.createProfile(createRequest);
            assertThat(profileResponse).isNotNull();

            // Track behaviors
            TrackBehaviorRequest trackRequest = TrackBehaviorRequest.builder()
                    .userId(USER_ID)
                    .sessionId("session-123")
                    .events(Arrays.asList(
                            createBehaviorEventRequest(EventType.VIEW, "item-1"),
                            createBehaviorEventRequest(EventType.VIEW, "item-2"),
                            createBehaviorEventRequest(EventType.VIEW, "item-3"),
                            createBehaviorEventRequest(EventType.VIEW, "item-4"),
                            createBehaviorEventRequest(EventType.VIEW, "item-5")
                    ))
                    .build();

            when(profileRepository.findByUserId(USER_ID)).thenReturn(Optional.of(profile));

            BehaviorTrackingResponse trackingResponse = personalizationService.trackBehaviors(trackRequest);
            assertThat(trackingResponse.eventsProcessed()).isEqualTo(5);

            // Get recommendations
            List<Recommendation> mockRecommendations = Arrays.asList(
                    Recommendation.builder()
                            .itemId("item-1")
                            .score(0.9)
                            .reason(RecommendationReason.BEHAVIORAL)
                            .build()
            );

            when(recommendationEngine.generateRecommendations(eq(profile), any())).thenReturn(mockRecommendations);

            RecommendationResponse recResponse = personalizationService.getRecommendations(USER_ID, 10, "content", "homepage");
            assertThat(recResponse.recommendations()).isNotEmpty();
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

    private UserProfile createEligibleProfile() {
        UserProfile profile = UserProfile.create(USER_ID);
        for (int i = 0; i < 10; i++) {
            BehaviorEvent event = BehaviorEvent.builder()
                    .eventType(EventType.VIEW)
                    .itemId("item-" + i)
                    .timestamp(Instant.now())
                    .build();
            profile.addBehaviorEvent(event);
        }
        return profile;
    }

    private TrackBehaviorRequest.BehaviorEventRequest createBehaviorEventRequest(EventType type, String itemId) {
        return createBehaviorEventRequestWithTimestamp(type, itemId, Instant.now());
    }

    private TrackBehaviorRequest.BehaviorEventRequest createBehaviorEventRequestWithTimestamp(
            EventType type, String itemId, Instant timestamp) {
        return TrackBehaviorRequest.BehaviorEventRequest.builder()
                .eventType(type)
                .itemId(itemId)
                .timestamp(timestamp)
                .properties(new HashMap<>())
                .build();
    }
}
