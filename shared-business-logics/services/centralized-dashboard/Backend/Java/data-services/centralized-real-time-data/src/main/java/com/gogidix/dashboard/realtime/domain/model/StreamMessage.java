package com.gogidix.dashboard.realtime.domain.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

/**
 * Stream Message Value Object
 */
public class StreamMessage {
    private final StreamId streamId;
    private final Map<String, Object> data;
    private final LocalDateTime timestamp;
    private final long sequenceNumber;
    private final long payloadSize;
    
    private StreamMessage(Builder builder) {
        this.streamId = Objects.requireNonNull(builder.streamId);
        this.data = Map.copyOf(builder.data);
        this.timestamp = Objects.requireNonNull(builder.timestamp);
        this.sequenceNumber = builder.sequenceNumber;
        this.payloadSize = calculatePayloadSize();
    }
    
    private long calculatePayloadSize() {
        return data.toString().getBytes().length;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public StreamId getStreamId() { return streamId; }
    public Map<String, Object> getData() { return data; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public long getSequenceNumber() { return sequenceNumber; }
    public long getPayloadSize() { return payloadSize; }
    
    public static class Builder {
        private StreamId streamId;
        private Map<String, Object> data = Map.of();
        private LocalDateTime timestamp = LocalDateTime.now();
        private long sequenceNumber;
        
        public Builder withStreamId(StreamId streamId) {
            this.streamId = streamId;
            return this;
        }
        
        public Builder withData(Map<String, Object> data) {
            this.data = data;
            return this;
        }
        
        public Builder withTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public Builder withSequenceNumber(long sequenceNumber) {
            this.sequenceNumber = sequenceNumber;
            return this;
        }
        
        public StreamMessage build() {
            return new StreamMessage(this);
        }
    }
}