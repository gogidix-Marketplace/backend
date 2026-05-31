package com.gogidix.aiservices.aipersonalizationservice.infrastructure.persistence;

import com.gogidix.aiservices.aipersonalizationservice.domain.model.EventType;
import com.gogidix.aiservices.aipersonalizationservice.domain.model.Segment;
import lombok.Data;

import java.time.Instant;
import java.util.*;

@Data
public class UserProfileEntity {
    private String profileId;
    private UUID userId;
    private Segment segment;
    private Map<String, Object> attributesMap;
    private Map<String, Double> affinityScores;
    private List<BehaviorEventEntity> behaviorEvents;
    private Instant createdAt;
    private Instant updatedAt;
}
