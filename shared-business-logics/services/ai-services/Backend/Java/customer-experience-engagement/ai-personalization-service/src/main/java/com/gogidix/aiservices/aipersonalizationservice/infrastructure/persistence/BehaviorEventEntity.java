package com.gogidix.aiservices.aipersonalizationservice.infrastructure.persistence;

import com.gogidix.aiservices.aipersonalizationservice.domain.model.EventType;
import lombok.Data;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Data
public class BehaviorEventEntity {
    private String eventId;
    private EventType eventType;
    private String itemId;
    private Instant timestamp;
    private Map<String, Object> properties;
}
