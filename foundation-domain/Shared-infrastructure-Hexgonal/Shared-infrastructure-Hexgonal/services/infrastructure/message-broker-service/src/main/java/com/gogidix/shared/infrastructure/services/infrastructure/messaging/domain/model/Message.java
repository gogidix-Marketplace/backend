package com.gogidix.shared.infrastructure.services.infrastructure.messaging.domain.model;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Message domain model.
 * Represents a message in the messaging system.
 */
public class Message {

    private final String messageId;
    private final String topic;
    private final String key;
    private final Object payload;
    private final Map<String, Object> headers;
    private final String contentType;
    private final Instant timestamp;
    private final Integer partition;
    private final Long offset;
    private final MessageStatus status;

    private Message(Builder builder) {
        this.messageId = builder.messageId;
        this.topic = builder.topic;
        this.key = builder.key;
        this.payload = builder.payload;
        this.headers = builder.headers;
        this.contentType = builder.contentType;
        this.timestamp = builder.timestamp;
        this.partition = builder.partition;
        this.offset = builder.offset;
        this.status = builder.status;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public String getMessageId() { return messageId; }
    public String getTopic() { return topic; }
    public String getKey() { return key; }
    public Object getPayload() { return payload; }
    public Map<String, Object> getHeaders() { return headers; }
    public String getContentType() { return contentType; }
    public Instant getTimestamp() { return timestamp; }
    public Integer getPartition() { return partition; }
    public Long getOffset() { return offset; }
    public MessageStatus getStatus() { return status; }

    public boolean isValid() {
        return topic != null && !topic.isBlank() && payload != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(messageId, message.messageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId);
    }

    @Override
    public String toString() {
        return "Message{" +
            "messageId='" + messageId + '\'' +
            ", topic='" + topic + '\'' +
            ", key='" + key + '\'' +
            ", timestamp=" + timestamp +
            ", status=" + status +
            '}';
    }

    public enum MessageStatus {
        PENDING,
        SENT,
        ACKNOWLEDGED,
        FAILED,
        RETRYING
    }

    public static class Builder {
        private String messageId;
        private String topic;
        private String key;
        private Object payload;
        private Map<String, Object> headers;
        private String contentType = "application/json";
        private Instant timestamp;
        private Integer partition;
        private Long offset;
        private MessageStatus status = MessageStatus.PENDING;

        public Builder messageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        public Builder topic(String topic) {
            this.topic = topic;
            return this;
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder payload(Object payload) {
            this.payload = payload;
            return this;
        }

        public Builder headers(Map<String, Object> headers) {
            this.headers = headers;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder partition(Integer partition) {
            this.partition = partition;
            return this;
        }

        public Builder offset(Long offset) {
            this.offset = offset;
            return this;
        }

        public Builder status(MessageStatus status) {
            this.status = status;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }
}
