package com.gogidix.shared.infrastructure.services.infrastructure.messaging.domain.model;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Topic domain model.
 * Represents a topic/channel in the messaging system.
 */
public class Topic {

    private final String topicName;
    private final int partitions;
    private final short replicationFactor;
    private final TopicType type;
    private final Map<String, String> config;
    private final Instant createdAt;
    private final String description;

    private Topic(Builder builder) {
        this.topicName = builder.topicName;
        this.partitions = builder.partitions;
        this.replicationFactor = builder.replicationFactor;
        this.type = builder.type;
        this.config = builder.config;
        this.createdAt = builder.createdAt;
        this.description = builder.description;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public String getTopicName() { return topicName; }
    public int getPartitions() { return partitions; }
    public short getReplicationFactor() { return replicationFactor; }
    public TopicType getType() { return type; }
    public Map<String, String> getConfig() { return config; }
    public Instant getCreatedAt() { return createdAt; }
    public String getDescription() { return description; }

    public boolean isDurable() {
        return type == TopicType.DURABLE || type == TopicType.STREAM;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Topic topic = (Topic) o;
        return Objects.equals(topicName, topic.topicName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topicName);
    }

    @Override
    public String toString() {
        return "Topic{" +
            "topicName='" + topicName + '\'' +
            ", partitions=" + partitions +
            ", replicationFactor=" + replicationFactor +
            ", type=" + type +
            '}';
    }

    public enum TopicType {
        STANDARD,
        DURABLE,
        STREAM,
        TEMPORARY
    }

    public static class Builder {
        private String topicName;
        private int partitions = 3;
        private short replicationFactor = 2;
        private TopicType type = TopicType.STANDARD;
        private Map<String, String> config;
        private Instant createdAt;
        private String description;

        public Builder topicName(String topicName) {
            this.topicName = topicName;
            return this;
        }

        public Builder partitions(int partitions) {
            this.partitions = partitions;
            return this;
        }

        public Builder replicationFactor(short replicationFactor) {
            this.replicationFactor = replicationFactor;
            return this;
        }

        public Builder type(TopicType type) {
            this.type = type;
            return this;
        }

        public Builder config(Map<String, String> config) {
            this.config = config;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Topic build() {
            return new Topic(this);
        }
    }
}
