package com.gogidix.shared.infrastructure.services.infrastructure.messaging.domain.event;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain event published when a topic is created.
 */
public class TopicCreatedEvent {

    private final String topicName;
    private final int partitions;
    private final short replicationFactor;
    private final String type;
    private final Instant occurredAt;

    public TopicCreatedEvent(String topicName, int partitions, short replicationFactor, String type, Instant occurredAt) {
        this.topicName = topicName;
        this.partitions = partitions;
        this.replicationFactor = replicationFactor;
        this.type = type;
        this.occurredAt = occurredAt;
    }

    public String getTopicName() {
        return topicName;
    }

    public int getPartitions() {
        return partitions;
    }

    public short getReplicationFactor() {
        return replicationFactor;
    }

    public String getType() {
        return type;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicCreatedEvent that = (TopicCreatedEvent) o;
        return Objects.equals(topicName, that.topicName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topicName);
    }

    @Override
    public String toString() {
        return "TopicCreatedEvent{" +
            "topicName='" + topicName + '\'' +
            ", partitions=" + partitions +
            ", replicationFactor=" + replicationFactor +
            ", type='" + type + '\'' +
            ", occurredAt=" + occurredAt +
            '}';
    }
}
