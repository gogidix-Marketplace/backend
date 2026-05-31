package com.gogidix.hr.globalcompliance.shared.exception;

import java.time.Instant;

public class ComplianceCheckCompletedEvent {

    private String eventId;
    private String eventType;
    private String tenantId;
    private Instant occurredAt;

    public ComplianceCheckCompletedEvent() {}

    public ComplianceCheckCompletedEvent(String eventId, String eventType, String tenantId, Instant occurredAt) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.tenantId = tenantId;
        this.occurredAt = occurredAt;
    }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public Instant getOccurredAt() { return occurredAt; }
    public void setOccurredAt(Instant occurredAt) { this.occurredAt = occurredAt; }
}
