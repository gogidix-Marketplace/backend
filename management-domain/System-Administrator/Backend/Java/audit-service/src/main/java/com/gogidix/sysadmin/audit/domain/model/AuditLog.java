package com.gogidix.sysadmin.audit.domain.model;

import lombok.Builder;
import lombok.AllArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

@Document(collection = "audit_logs")
@Builder
@AllArgsConstructor
public class AuditLog {

    @Id
    private String id;
    @Indexed
    private String tenantId;
    private String eventId;
    private String eventType;
    private String eventCategory;
    private String actor;
    private String actorType;
    private String action;
    private String resourceType;
    private String resourceId;
    private String resourceName;
    private String outcome;
    private String ipAddress;
    private String userAgent;
    private Map<String, Object> requestDetails;
    private Map<String, Object> responseDetails;
    private String errorMessage;
    private String correlationId;
    private String sessionId;
    @Indexed
    private Instant timestamp;
    private Map<String, String> metadata;

    public enum EventCategory {
        AUTHENTICATION, AUTHORIZATION, DATA_ACCESS, CONFIGURATION_CHANGE,
        USER_MANAGEMENT, RESOURCE_MANAGEMENT, SYSTEM, SECURITY
    }

    public enum Outcome {
        SUCCESS, FAILURE, PARTIAL_SUCCESS
    }

    public AuditLog() {
        this.timestamp = Instant.now();
        this.outcome = Outcome.SUCCESS.name();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getEventCategory() { return eventCategory; }
    public void setEventCategory(String eventCategory) { this.eventCategory = eventCategory; }

    public String getActor() { return actor; }
    public void setActor(String actor) { this.actor = actor; }

    public String getActorType() { return actorType; }
    public void setActorType(String actorType) { this.actorType = actorType; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }

    public String getResourceId() { return resourceId; }
    public void setResourceId(String resourceId) { this.resourceId = resourceId; }

    public String getResourceName() { return resourceName; }
    public void setResourceName(String resourceName) { this.resourceName = resourceName; }

    public String getOutcome() { return outcome; }
    public void setOutcome(String outcome) { this.outcome = outcome; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    public Map<String, Object> getRequestDetails() { return requestDetails; }
    public void setRequestDetails(Map<String, Object> requestDetails) { this.requestDetails = requestDetails; }

    public Map<String, Object> getResponseDetails() { return responseDetails; }
    public void setResponseDetails(Map<String, Object> responseDetails) { this.responseDetails = responseDetails; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public Map<String, String> getMetadata() { return metadata; }
    public void setMetadata(Map<String, String> metadata) { this.metadata = metadata; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditLog auditLog = (AuditLog) o;
        return Objects.equals(id, auditLog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final AuditLog instance = new AuditLog();

        public Builder tenantId(String tenantId) { instance.setTenantId(tenantId); return this; }
        public Builder eventType(String eventType) { instance.setEventType(eventType); return this; }
        public Builder eventCategory(String eventCategory) { instance.setEventCategory(eventCategory); return this; }
        public Builder actor(String actor) { instance.setActor(actor); return this; }
        public Builder action(String action) { instance.setAction(action); return this; }
        public Builder resourceType(String resourceType) { instance.setResourceType(resourceType); return this; }
        public Builder resourceId(String resourceId) { instance.setResourceId(resourceId); return this; }
        public Builder outcome(String outcome) { instance.setOutcome(outcome); return this; }
        public Builder ipAddress(String ipAddress) { instance.setIpAddress(ipAddress); return this; }
        public Builder correlationId(String correlationId) { instance.setCorrelationId(correlationId); return this; }

        public AuditLog build() {
            return instance;
        }
    }
}
