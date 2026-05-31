package com.gogidix.platform.realtime.infrastructure.messaging.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Event class for real-time platform events.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealTimeEvent {

    private String eventType;
    private String tenantId;
    private String channelId;
    private String userId;
    private Object payload;
    private Instant timestamp;
}
