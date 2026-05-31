package com.gogidix.platform.realtime.domain.port.in;

import com.gogidix.platform.realtime.domain.model.RealTimeMessage;

import java.util.List;

/**
 * Inbound port (use case) for message broadcasting.
 */
public interface MessageBroadcastUseCase {

    /**
     * Broadcast message to a channel.
     */
    void broadcast(String channelId, RealTimeMessage message);

    /**
     * Send direct message to a specific user.
     */
    void sendDirectMessage(String userId, RealTimeMessage message);

    /**
     * Broadcast to multiple channels.
     */
    void broadcastToChannels(List<String> channelIds, RealTimeMessage message);
}
