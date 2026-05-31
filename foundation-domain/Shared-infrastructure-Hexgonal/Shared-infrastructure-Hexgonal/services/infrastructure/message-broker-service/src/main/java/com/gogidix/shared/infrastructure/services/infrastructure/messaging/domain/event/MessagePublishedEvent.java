package com.gogidix.shared.infrastructure.services.infrastructure.messaging.domain.event;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain event published when a message is published.
 */
public class MessagePublishedEvent {

    private final String messageId;
    private final String topic;
    private final String key;
    private final Instant occurredAt;

    public MessagePublishedEvent(String messageId, String topic, String key, Instant occurredAt) {
        this.messageId = messageId;
        this.topic = topic;
        this.key = key;
        this.occurredAt = occurredAt;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getTopic() {
        return topic;
    }

    public String getKey() {
        return key;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessagePublishedEvent that = (MessagePublishedEvent) o;
        return Objects.equals(messageId, that.messageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId);
    }

    @Override
    public String toString() {
        return "MessagePublishedEvent{" +
            "messageId='" + messageId + '\'' +
            ", topic='" + topic + '\'' +
            ", key='" + key + '\'' +
            ", occurredAt=" + occurredAt +
            '}';
    }
}
