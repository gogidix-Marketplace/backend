package com.gogidix.cargo.eventdriven.infrastructure.messaging;

import com.gogidix.cargo.eventdriven.domain.port.EventSubscriberPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class KafkaEventSubscriberAdapter implements EventSubscriberPort {
    private static final Logger log = LoggerFactory.getLogger(KafkaEventSubscriberAdapter.class);
    private final Map<String, Consumer<String>> handlers = new ConcurrentHashMap<>();

    @Override
    public void subscribe(String topic, String groupId, Consumer<String> handler) {
        log.info("Registering handler for topic: {}, groupId: {}", topic, groupId);
        handlers.put(topic, handler);
    }

    @Override
    public void unsubscribe(String topic) {
        log.info("Unsubscribing from topic: {}", topic);
        handlers.remove(topic);
    }

    public void handleMessage(String topic, String payload) {
        Consumer<String> handler = handlers.get(topic);
        if (handler != null) {
            handler.accept(payload);
        } else {
            log.warn("No handler registered for topic: {}", topic);
        }
    }
}
