package com.gogidix.platform.realtime.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

/**
 * Domain entity representing a real-time channel.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealTimeChannel {

    private String id;
    private String tenantId;
    private String name;
    private String description;
    private ChannelType type;
    private Set<String> subscriberIds;
    private ChannelStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    /**
     * Create a new channel.
     */
    public static RealTimeChannel create(String tenantId, String name, String description, ChannelType type) {
        return RealTimeChannel.builder()
                .tenantId(tenantId)
                .name(name)
                .description(description)
                .type(type)
                .status(ChannelStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    /**
     * Add subscriber to channel.
     */
    public void addSubscriber(String subscriberId) {
        this.subscriberIds.add(subscriberId);
        this.updatedAt = Instant.now();
    }

    /**
     * Remove subscriber from channel.
     */
    public void removeSubscriber(String subscriberId) {
        this.subscriberIds.remove(subscriberId);
        this.updatedAt = Instant.now();
    }
}
