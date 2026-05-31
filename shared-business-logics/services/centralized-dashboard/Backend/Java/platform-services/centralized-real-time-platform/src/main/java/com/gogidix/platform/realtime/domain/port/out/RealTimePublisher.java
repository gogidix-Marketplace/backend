package com.gogidix.platform.realtime.domain.port.out;

import com.gogidix.platform.realtime.domain.model.RealTimeMessage;

/**
 * Outbound port for publishing real-time events.
 */
public interface RealTimePublisher {

    /**
     * Publish message to Redis channel.
     */
    void publish(String channelId, RealTimeMessage message);

    /**
     * Publish event to Kafka.
     */
    void publishEvent(String eventType, Object payload);
}
