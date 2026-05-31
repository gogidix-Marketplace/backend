package com.gogidix.shared.infrastructure.services.gateway.discovery.domain.event;

import com.gogidix.shared.infrastructure.services.gateway.discovery.domain.model.ServiceInstance;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain event published when a service instance is registered.
 */
public class ServiceRegisteredEvent {

    private final String instanceId;
    private final String appName;
    private final String hostName;
    private final int port;
    private final String zone;
    private final Instant occurredAt;

    public ServiceRegisteredEvent(ServiceInstance instance) {
        this.instanceId = instance.getInstanceId();
        this.appName = instance.getAppName();
        this.hostName = instance.getHostName();
        this.port = instance.getPort();
        this.zone = instance.getZone();
        this.occurredAt = Instant.now();
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getAppName() {
        return appName;
    }

    public String getHostName() {
        return hostName;
    }

    public int getPort() {
        return port;
    }

    public String getZone() {
        return zone;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceRegisteredEvent that = (ServiceRegisteredEvent) o;
        return Objects.equals(instanceId, that.instanceId) && Objects.equals(appName, that.appName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(instanceId, appName);
    }

    @Override
    public String toString() {
        return "ServiceRegisteredEvent{" +
                "instanceId='" + instanceId + '\'' +
                ", appName='" + appName + '\'' +
                ", occurredAt=" + occurredAt +
                '}';
    }
}
