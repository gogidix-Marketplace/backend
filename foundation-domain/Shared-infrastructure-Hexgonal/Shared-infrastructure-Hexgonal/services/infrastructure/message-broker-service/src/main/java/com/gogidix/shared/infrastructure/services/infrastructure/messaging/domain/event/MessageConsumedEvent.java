package com.gogidix.shared.infrastructure.services.infrastructure.messaging.domain.event;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain event published when a message is consumed.
 */
public class MessageConsumedEvent {

    private final String messageId;
    private final String topic;
    private final String consumerGroup;
    private final Instant occurredAt;

    public MessageConsumedEvent(String messageId, String topic, String consumerGroup, Instant occurredAt) {
        this.messageId = messageId;
        this.topic = topic;
        this.consumerGroup = consumerGroup;
        this.occurredAt = occurredAt;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getTopic() {
        return topic;
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageConsumedEvent that = (MessageConsumedEvent) o;
        return Objects.equals(messageId, that.messageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId);
    }

    @Override
    public String toString() {
        return "MessageConsumedEvent{" +
            "messageId='" + messageId + '\'' +
            ", topic='" + topic + '\'' +
            ", consumerGroup='" + consumerGroup + '\'' +
            ", occurredAt=" + occurredAt +
            '}';
    }
}
