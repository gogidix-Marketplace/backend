package com.gogidix.shared.infrastructure.services.gateway.discovery.domain.event;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain event published when a service instance is deregistered or evicted.
 */
public class ServiceDeregisteredEvent {

    private final String instanceId;
    private final String appName;
    private final String reason;
    private final Instant occurredAt;

    public ServiceDeregisteredEvent(String instanceId, String appName, String reason) {
        this.instanceId = instanceId;
        this.appName = appName;
        this.reason = reason;
        this.occurredAt = Instant.now();
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getAppName() {
        return appName;
    }

    public String getReason() {
        return reason;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceDeregisteredEvent that = (ServiceDeregisteredEvent) o;
        return Objects.equals(instanceId, that.instanceId) && Objects.equals(appName, that.appName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instanceId, appName);
    }

    @Override
    public String toString() {
        return "ServiceDeregisteredEvent{" +
                "instanceId='" + instanceId + '\'' +
                ", appName='" + appName + '\'' +
                ", reason='" + reason + '\'' +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
