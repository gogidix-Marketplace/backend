package com.gogidix.aiservices.aipersonalizationservice.infrastructure.persistence;

import com.gogidix.aiservices.aipersonalizationservice.domain.model.Segment;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryUserProfileDataSource implements UserProfileDataSource {

    private final Map<String, UserProfileEntity> byUserId = new ConcurrentHashMap<>();
    private final Map<String, UserProfileEntity> byProfileId = new ConcurrentHashMap<>();

    @Override
    public UserProfileEntity save(UserProfileEntity entity) {
        byUserId.put(entity.getUserId().toString(), entity);
        byProfileId.put(entity.getProfileId(), entity);
        entity.setUpdatedAt(Instant.now());
        return entity;
    }

    @Override
    public Optional<UserProfileEntity> findByUserId(String userId) {
        return Optional.ofNullable(byUserId.get(userId));
    }

    @Override
    public Optional<UserProfileEntity> findById(String profileId) {
        return Optional.ofNullable(byProfileId.get(profileId));
    }

    @Override
    public List<UserProfileEntity> findAll() {
        return new ArrayList<>(byUserId.values());
    }

    @Override
    public List<UserProfileEntity> findBySegment(Segment segment) {
        return byUserId.values().stream()
                .filter(e -> e.getSegment() == segment)
                .toList();
    }

    @Override
    public List<UserProfileEntity> findByUpdatedAtAfter(Instant timestamp) {
        return byUserId.values().stream()
                .filter(e -> e.getUpdatedAt().isAfter(timestamp))
                .toList();
    }

    @Override
    public void deleteByUserId(String userId) {
        UserProfileEntity entity = byUserId.remove(userId);
        if (entity != null) {
            byProfileId.remove(entity.getProfileId());
        }
    }

    @Override
    public void deleteById(String profileId) {
        UserProfileEntity entity = byProfileId.remove(profileId);
        if (entity != null) {
            byUserId.remove(entity.getUserId().toString());
        }
    }

    @Override
    public List<UserProfileEntity> saveAll(List<UserProfileEntity> entities) {
        List<UserProfileEntity> saved = new ArrayList<>();
        for (UserProfileEntity entity : entities) {
            saved.add(save(entity));
        }
        return saved;
    }
}
