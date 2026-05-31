package com.gogidix.aiservices.aipersonalizationservice.infrastructure.persistence;

import com.gogidix.aiservices.aipersonalizationservice.domain.model.Segment;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserProfileDataSource {
    UserProfileEntity save(UserProfileEntity entity);
    Optional<UserProfileEntity> findByUserId(String userId);
    Optional<UserProfileEntity> findById(String profileId);
    List<UserProfileEntity> findAll();
    List<UserProfileEntity> findBySegment(Segment segment);
    List<UserProfileEntity> findByUpdatedAtAfter(Instant timestamp);
    void deleteByUserId(String userId);
    void deleteById(String profileId);
    List<UserProfileEntity> saveAll(List<UserProfileEntity> entities);
}
