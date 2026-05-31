package com.gogidix.aiservices.aipersonalizationservice.domain.port.out;

import com.gogidix.aiservices.aipersonalizationservice.domain.aggregate.UserProfile;
import com.gogidix.aiservices.aipersonalizationservice.domain.model.Segment;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserProfileRepository {
    UserProfile save(UserProfile profile);
    Optional<UserProfile> findByUserId(String userId);
    Optional<UserProfile> findById(String profileId);
    List<UserProfile> findAll();
    List<UserProfile> findBySegment(Segment segment);
    List<UserProfile> findByUpdatedAtAfter(Instant timestamp);
    void delete(String userId);
    void deleteById(String profileId);
    List<UserProfile> saveAll(List<UserProfile> profiles);
}
