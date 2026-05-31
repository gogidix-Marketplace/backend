package com.gogidix.shared.infrastructure.services.communication.eventbus.domain.port.out;
import com.gogidix.shared.infrastructure.services.communication.eventbus.domain.model.EventBridgeMessage;
/**
 * Output port for EventBridge message publishing
 */
public interface IEventBridgePublisher {
    void publish(String topic, EventBridgeMessage message);
    void publishToExchange(String exchange, String routingKey, EventBridgeMessage message);
    void sendToQueue(String queue, EventBridgeMessage message);
}
