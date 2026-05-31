package com.gogidix.platform.realtime.domain.port.in;

import com.gogidix.platform.realtime.domain.model.ChannelType;
import com.gogidix.platform.realtime.domain.model.RealTimeChannel;

import java.util.List;

/**
 * Inbound port (use case) for channel management.
 */
public interface ChannelManagementUseCase {

    /**
     * Create a new channel.
     */
    RealTimeChannel createChannel(String tenantId, String name, String description, ChannelType type);

    /**
     * Get channel by ID.
     */
    RealTimeChannel getChannel(String channelId);

    /**
     * List channels for a tenant.
     */
    List<RealTimeChannel> listChannels(String tenantId);

    /**
     * Delete a channel.
     */
    void deleteChannel(String channelId);

    /**
     * Add subscriber to channel.
     */
    void addSubscriber(String channelId, String subscriberId);

    /**
     * Remove subscriber from channel.
     */
    void removeSubscriber(String channelId, String subscriberId);
}
