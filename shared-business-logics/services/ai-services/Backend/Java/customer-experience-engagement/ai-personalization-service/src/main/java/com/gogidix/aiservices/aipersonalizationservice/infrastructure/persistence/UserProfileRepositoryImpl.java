package com.gogidix.aiservices.aipersonalizationservice.infrastructure.persistence;

import com.gogidix.aiservices.aipersonalizationservice.domain.aggregate.UserProfile;
import com.gogidix.aiservices.aipersonalizationservice.domain.event.BehaviorEvent;
import com.gogidix.aiservices.aipersonalizationservice.domain.model.*;
import com.gogidix.aiservices.aipersonalizationservice.domain.port.out.UserProfileRepository;
import com.gogidix.aiservices.aipersonalizationservice.shared.exception.ProfileNotFoundException;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@org.springframework.stereotype.Repository
@RequiredArgsConstructor
public class UserProfileRepositoryImpl implements UserProfileRepository {

    private final UserProfileDataSource dataSource;

    @Override
    public UserProfile save(UserProfile profile) {
        UserProfileEntity entity = toEntity(profile);
        UserProfileEntity saved = dataSource.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<UserProfile> findByUserId(String userId) {
        return dataSource.findByUserId(userId)
                .map(this::toDomain);
    }

    @Override
    public Optional<UserProfile> findById(String profileId) {
        return dataSource.findById(profileId)
                .map(this::toDomain);
    }

    @Override
    public List<UserProfile> findAll() {
        return dataSource.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserProfile> findBySegment(Segment segment) {
        return dataSource.findBySegment(segment).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserProfile> findByUpdatedAtAfter(Instant timestamp) {
        return dataSource.findByUpdatedAtAfter(timestamp).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String userId) {
        dataSource.deleteByUserId(userId);
    }

    @Override
    public void deleteById(String profileId) {
        dataSource.deleteById(profileId);
    }

    @Override
    public List<UserProfile> saveAll(List<UserProfile> profiles) {
        List<UserProfileEntity> entities = profiles.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
        List<UserProfileEntity> saved = dataSource.saveAll(entities);
        return saved.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private UserProfileEntity toEntity(UserProfile profile) {
        UserProfileEntity entity = new UserProfileEntity();
        entity.setProfileId(profile.getProfileId().toString());
        entity.setUserId(profile.getUserId());
        entity.setSegment(profile.getSegment());
        entity.setAttributesMap(convertAttributesToMap(profile.getAttributes()));
        entity.setAffinityScores(new HashMap<>(profile.getAffinityScores()));
        entity.setBehaviorEvents(profile.getBehaviorHistory().stream()
                .map(this::toEventEntity)
                .collect(Collectors.toList()));
        entity.setCreatedAt(profile.getCreatedAt());
        entity.setUpdatedAt(profile.getUpdatedAt());
        return entity;
    }

    private BehaviorEventEntity toEventEntity(BehaviorEvent event) {
        BehaviorEventEntity entity = new BehaviorEventEntity();
        entity.setEventId(event.getEventId().toString());
        entity.setEventType(event.getEventType());
        entity.setItemId(event.getItemId());
        entity.setTimestamp(event.getTimestamp());
        entity.setProperties(new HashMap<>(event.getProperties()));
        return entity;
    }

    private UserProfile toDomain(UserProfileEntity entity) {
        UserProfile profile = UserProfile.create(entity.getUserId().toString());

        entity.getBehaviorEvents().stream()
                .sorted(Comparator.comparing(BehaviorEventEntity::getTimestamp))
                .forEach(eventEntity -> {
                    BehaviorEvent event = BehaviorEvent.builder()
                            .userId(entity.getUserId())
                            .eventType(eventEntity.getEventType())
                            .itemId(eventEntity.getItemId())
                            .timestamp(eventEntity.getTimestamp())
                            .properties(eventEntity.getProperties() != null
                                    ? eventEntity.getProperties() : new HashMap<>())
                            .build();
                    profile.addBehaviorEvent(event);
                });

        if (entity.getAttributesMap() != null && !entity.getAttributesMap().isEmpty()) {
            profile.updateAttributes(convertMapToAttributes(entity.getAttributesMap()));
        }

        profile.recalculateSegment();

        entity.getAffinityScores().forEach((key, value) ->
                profile.getAffinityScores().put(key, value));

        return profile;
    }

    private Map<String, Object> convertAttributesToMap(UserAttributes attributes) {
        if (attributes == null) {
            return Map.of();
        }

        Map<String, Object> map = new HashMap<>();

        if (attributes.getDemographics() != null) {
            Map<String, Object> demographics = new HashMap<>();
            demographics.put("age", attributes.getDemographics().getAge());
            demographics.put("gender", attributes.getDemographics().getGender());
            demographics.put("location", attributes.getDemographics().getLocation());
            map.put("demographics", demographics);
        }

        map.put("interests", attributes.getInterests() != null ? attributes.getInterests() : List.of());

        if (attributes.getPreferences() != null) {
            Map<String, Object> preferences = new HashMap<>();
            preferences.put("categories", attributes.getPreferences().getCategories() != null
                    ? attributes.getPreferences().getCategories() : List.of());
            preferences.put("brands", attributes.getPreferences().getBrands() != null
                    ? attributes.getPreferences().getBrands() : List.of());
            map.put("preferences", preferences);
        }

        return map;
    }

    @SuppressWarnings("unchecked")
    private UserAttributes convertMapToAttributes(Map<String, Object> map) {
        UserAttributes.UserAttributesBuilder builder = UserAttributes.builder();

        if (map.containsKey("demographics")) {
            Map<String, Object> demoMap = (Map<String, Object>) map.get("demographics");
            builder.demographics(Demographics.builder()
                    .age((Integer) demoMap.get("age"))
                    .gender((String) demoMap.get("gender"))
                    .location((String) demoMap.get("location"))
                    .build());
        }

        if (map.containsKey("interests")) {
            builder.interests((List<String>) map.get("interests"));
        }

        if (map.containsKey("preferences")) {
            Map<String, Object> prefMap = (Map<String, Object>) map.get("preferences");
            builder.preferences(Preferences.builder()
                    .categories((List<String>) prefMap.get("categories"))
                    .brands((List<String>) prefMap.get("brands"))
                    .build());
        }

        return builder.build();
    }
}
