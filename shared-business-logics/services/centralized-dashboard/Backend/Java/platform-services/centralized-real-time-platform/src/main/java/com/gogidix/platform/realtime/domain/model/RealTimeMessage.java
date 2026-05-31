package com.gogidix.platform.realtime.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * Domain entity representing a real-time message.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealTimeMessage {

    private String id;
    private String tenantId;
    private String channelId;
    private String userId;
    private String type;
    private String payload;
    private Map<String, String> metadata;
    private Instant timestamp;

    /**
     * Create a new real-time message.
     */
    public static RealTimeMessage create(String tenantId, String channelId, String userId,
                                         String type, String payload, Map<String, String> metadata) {
        return RealTimeMessage.builder()
                .tenantId(tenantId)
                .channelId(channelId)
                .userId(userId)
                .type(type)
                .payload(payload)
                .metadata(metadata)
                .timestamp(Instant.now())
                .build();
    }
}
