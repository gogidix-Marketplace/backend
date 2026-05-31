package com.gogidix.shared.infrastructure.services.gateway.discovery.domain.event;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain event published when a service instance lease is renewed.
 */
public class ServiceRenewedEvent {

    private final String instanceId;
    private final String appName;
    private final Instant occurredAt;

    public ServiceRenewedEvent(String instanceId, String appName) {
        this.instanceId = instanceId;
        this.appName = appName;
        this.occurredAt = Instant.now();
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getAppName() {
        return appName;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceRenewedEvent that = (ServiceRenewedEvent) o;
        return Objects.equals(instanceId, that.instanceId) && Objects.equals(appName, that.appName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instanceId, appName);
    }

    @Override
    public String toString() {
        return "ServiceRenewedEvent{" +
                "instanceId='" + instanceId + '\'' +
                ", appName='" + appName + '\'' +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
