package com.gogidix.cargo.eventdriven.domain.event;

import java.time.Instant;
import java.util.UUID;

public abstract class BaseDomainEvent {
    private final String eventId;
    private final Instant timestamp;
    private final String eventType;
    private String tenantId;
    private String correlationId;
    private String source;

    protected BaseDomainEvent(String eventType) {
        this.eventId = UUID.randomUUID().toString();
        this.timestamp = Instant.now();
        this.eventType = eventType;
    }

    protected BaseDomainEvent(String eventType, String tenantId, String correlationId) {
        this(eventType);
        this.tenantId = tenantId;
        this.correlationId = correlationId;
    }

    public String getEventId() { return eventId; }
    public Instant getTimestamp() { return timestamp; }
    public String getEventType() { return eventType; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public abstract String getAggregateType();
    public abstract String getAggregateId();
}
