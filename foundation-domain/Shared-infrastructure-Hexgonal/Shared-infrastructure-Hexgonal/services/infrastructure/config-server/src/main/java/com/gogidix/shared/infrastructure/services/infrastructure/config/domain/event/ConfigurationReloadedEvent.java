package com.gogidix.shared.infrastructure.services.infrastructure.config.domain.event;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain event published when configurations are reloaded.
 */
public class ConfigurationReloadedEvent {

    private final String scope;
    private final Instant occurredAt;
    private final int configurationCount;

    public ConfigurationReloadedEvent(String scope, Instant occurredAt, int configurationCount) {
        this.scope = scope;
        this.occurredAt = occurredAt;
        this.configurationCount = configurationCount;
    }

    public String getScope() {
        return scope;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public int getConfigurationCount() {
        return configurationCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigurationReloadedEvent that = (ConfigurationReloadedEvent) o;
        return configurationCount == that.configurationCount &&
            Objects.equals(scope, that.scope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scope, configurationCount);
    }

    @Override
    public String toString() {
        return "ConfigurationReloadedEvent{" +
            "scope='" + scope + '\'' +
            ", configurationCount=" + configurationCount +
            ", occurredAt=" + occurredAt +
            '}';
    }
}
