package com.gogidix.cargo.eventdriven.domain.port;

import java.util.function.Consumer;

public interface EventSubscriberPort {
    void subscribe(String topic, String groupId, Consumer<String> handler);
    void unsubscribe(String topic);
}
