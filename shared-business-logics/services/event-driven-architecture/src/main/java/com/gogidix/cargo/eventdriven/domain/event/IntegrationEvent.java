package com.gogidix.cargo.eventdriven.domain.event;

public class IntegrationEvent extends BaseDomainEvent {
    private String targetDomain;
    private String payload;
    private String schemaVersion;

    public IntegrationEvent(String eventType, String tenantId, String correlationId) {
        super(eventType, tenantId, correlationId);
    }

    public String getTargetDomain() { return targetDomain; }
    public void setTargetDomain(String targetDomain) { this.targetDomain = targetDomain; }
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }
    public String getSchemaVersion() { return schemaVersion; }
    public void setSchemaVersion(String schemaVersion) { this.schemaVersion = schemaVersion; }
    @Override public String getAggregateType() { return "INTEGRATION"; }
    @Override public String getAggregateId() { return getEventId(); }
}
