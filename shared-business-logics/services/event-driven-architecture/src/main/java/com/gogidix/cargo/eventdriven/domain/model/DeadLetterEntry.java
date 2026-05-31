package com.gogidix.cargo.eventdriven.domain.model;

import java.time.Instant;

public class DeadLetterEntry {
    private String id;
    private String originalTopic;
    private String eventKey;
    private String eventPayload;
    private String errorMessage;
    private int retryCount;
    private Instant createdAt;
    private Instant lastRetryAt;
    private DeadLetterStatus status;

    public enum DeadLetterStatus { PENDING, RETRYING, EXHAUSTED, RESOLVED }

    public DeadLetterEntry() {
        this.createdAt = Instant.now();
        this.retryCount = 0;
        this.status = DeadLetterStatus.PENDING;
    }

    public void incrementRetry() { this.retryCount++; this.lastRetryAt = Instant.now(); this.status = DeadLetterStatus.RETRYING; }
    public void markExhausted() { this.status = DeadLetterStatus.EXHAUSTED; }
    public void markResolved() { this.status = DeadLetterStatus.RESOLVED; }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getOriginalTopic() { return originalTopic; }
    public void setOriginalTopic(String originalTopic) { this.originalTopic = originalTopic; }
    public String getEventKey() { return eventKey; }
    public void setEventKey(String eventKey) { this.eventKey = eventKey; }
    public String getEventPayload() { return eventPayload; }
    public void setEventPayload(String eventPayload) { this.eventPayload = eventPayload; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public int getRetryCount() { return retryCount; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getLastRetryAt() { return lastRetryAt; }
    public DeadLetterStatus getStatus() { return status; }
}
