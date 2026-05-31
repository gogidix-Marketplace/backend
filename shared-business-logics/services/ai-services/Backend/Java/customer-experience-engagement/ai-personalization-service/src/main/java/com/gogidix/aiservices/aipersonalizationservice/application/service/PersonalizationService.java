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
import com.gogidix.aiservices.aipersonalizationservice.domain.policy.PersonalizationPolicy;
import com.gogidix.aiservices.aipersonalizationservice.shared.exception.ProfileNotFoundException;
import com.gogidix.aiservices.aipersonalizationservice.shared.exception.PersonalizationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonalizationService {
    private final UserProfileRepository profileRepository;
    private final RecommendationEnginePort recommendationEngine;
    private final EventPublisherPort eventPublisher;
    private final PersonalizationPolicy policy;

    private static final int DEFAULT_RECOMMENDATION_LIMIT = 10;
    private static final int MAX_RECOMMENDATION_LIMIT = 100;
    private static final double MIN_RECOMMENDATION_SCORE = 0.3;

    public ProfileResponse createProfile(CreateProfileRequest request) {
        UserProfile profile = UserProfile.create(request.getUserId());

        if (request.getAttributes() != null) {
            profile.updateAttributes(request.getAttributes());
        }

        UserProfile saved = profileRepository.save(profile);

        eventPublisher.publish("profile.created", Map.of(
                "profileId", saved.getProfileId().toString(),
                "userId", saved.getUserId().toString()
        ));

        return toProfileResponse(saved);
    }

    public ProfileResponse updateAttributes(UpdateAttributesRequest request) {
        UserProfile profile = profileRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> ProfileNotFoundException.forUser(request.getUserId()));

        profile.updateAttributes(request.getAttributes());

        UserProfile saved = profileRepository.save(profile);

        return toProfileResponse(saved);
    }

    public ProfileResponse getProfile(String userId) {
        UserProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> ProfileNotFoundException.forUser(userId));

        return toProfileResponse(profile);
    }

    public void deleteProfile(String userId) {
        UserProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> ProfileNotFoundException.forUser(userId));

        profileRepository.delete(userId);

        eventPublisher.publish("profile.deleted", Map.of(
                "profileId", profile.getProfileId().toString(),
                "userId", userId
        ));
    }

    public RecommendationResponse getRecommendations(String userId, Integer limit, String type, String context) {
        UserProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> ProfileNotFoundException.forUser(userId));

        if (!policy.isEligibleForRecommendations(profile)) {
            throw new PersonalizationException("User not eligible for recommendations. Need more interaction data.");
        }

        int actualLimit = Math.min(limit != null ? limit : DEFAULT_RECOMMENDATION_LIMIT, MAX_RECOMMENDATION_LIMIT);

        RecommendationEnginePort.RecommendationContext recContext =
                new RecommendationEnginePort.RecommendationContext(actualLimit, type, context);

        List<Recommendation> recommendations = recommendationEngine.generateRecommendations(profile, recContext);

        // Filter by score threshold
        recommendations = recommendations.stream()
                .filter(r -> r.getScore() >= MIN_RECOMMENDATION_SCORE)
                .collect(Collectors.toList());

        // Filter by user preferences
        recommendations = policy.filterByUserPreferences(recommendations, profile);

        // Sort and limit
        recommendations = policy.limitRecommendations(recommendations, actualLimit);

        // Apply diversity
        recommendations = policy.applyDiversity(recommendations, 3);

        return toRecommendationResponse(userId, recommendations, recContext);
    }

    public BehaviorTrackingResponse trackBehaviors(TrackBehaviorRequest request) {
        if (request.getEvents().isEmpty()) {
            throw new IllegalArgumentException("Events cannot be empty");
        }
        if (request.getEvents().size() > 1000) {
            throw new IllegalArgumentException("Events batch exceeds maximum of 1000");
        }

        UserProfile profile = profileRepository.findByUserId(request.getUserId())
                .orElseGet(() -> {
                    UserProfile newProfile = UserProfile.create(request.getUserId());
                    return profileRepository.save(newProfile);
                });

        Set<String> processedEvents = new HashSet<>();
        int eventsProcessed = 0;

        for (var eventRequest : request.getEvents()) {
            String eventKey = request.getUserId() + eventRequest.getItemId() + eventRequest.getTimestamp().toString();

            if (!processedEvents.contains(eventKey)) {
                BehaviorEvent event = BehaviorEvent.builder()
                        .userId(UUID.fromString(request.getUserId()))
                        .eventType(eventRequest.getEventType())
                        .itemId(eventRequest.getItemId())
                        .timestamp(eventRequest.getTimestamp())
                        .properties(eventRequest.getProperties() != null ? eventRequest.getProperties() : new HashMap<>())
                        .build();

                profile.addBehaviorEvent(event);
                eventPublisher.publish("behavior.tracked", Map.of(
                        "userId", request.getUserId(),
                        "eventType", eventRequest.getEventType().toString(),
                        "itemId", eventRequest.getItemId()
                ));

                processedEvents.add(eventKey);
                eventsProcessed++;
            }
        }

        // Recalculate segment and affinity
        profile.recalculateSegment();
        profile.recalculateAffinityScores();

        profileRepository.save(profile);

        return BehaviorTrackingResponse.builder()
                .eventsProcessed(eventsProcessed)
                .profileUpdated(true)
                .build();
    }

    public void recalculateSegment(String userId) {
        UserProfile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> ProfileNotFoundException.forUser(userId));

        profile.recalculateSegment();
        profileRepository.save(profile);
    }

    private ProfileResponse toProfileResponse(UserProfile profile) {
        return ProfileResponse.builder()
                .profileId(profile.getProfileId().toString())
                .userId(profile.getUserId().toString())
                .segment(profile.getSegment())
                .attributes(convertAttributes(profile.getAttributes()))
                .affinityScores(new HashMap<>(profile.getAffinityScores()))
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }

    private RecommendationResponse toRecommendationResponse(String userId, List<Recommendation> recommendations,
                                                            RecommendationEnginePort.RecommendationContext context) {
        List<RecommendationResponse.ItemRecommendation> items = recommendations.stream()
                .map(r -> new RecommendationResponse.ItemRecommendation(
                        r.getItemId(),
                        r.getScore(),
                        r.getReason().getDescription(),
                        r.getCategory()
                ))
                .toList();

        return RecommendationResponse.builder()
                .userId(userId)
                .recommendations(items)
                .metadata(new RecommendationResponse.Metadata(
                        "hybrid_collaborative",
                        Instant.now()
                ))
                .build();
    }

    private Map<String, Object> convertAttributes(UserAttributes attributes) {
        if (attributes == null) {
            return Map.of();
        }

        Map<String, Object> result = new HashMap<>();

        if (attributes.getDemographics() != null) {
            Map<String, Object> demographics = new HashMap<>();
            demographics.put("age", attributes.getDemographics().getAge());
            demographics.put("gender", attributes.getDemographics().getGender());
            demographics.put("location", attributes.getDemographics().getLocation());
            result.put("demographics", demographics);
        }

        result.put("interests", attributes.getInterests() != null ? attributes.getInterests() : List.of());

        if (attributes.getPreferences() != null) {
            Map<String, Object> preferences = new HashMap<>();
            preferences.put("categories", attributes.getPreferences().getCategories() != null
                    ? attributes.getPreferences().getCategories() : List.of());
            preferences.put("brands", attributes.getPreferences().getBrands() != null
                    ? attributes.getPreferences().getBrands() : List.of());
            result.put("preferences", preferences);
        }

        return result;
    }
}
