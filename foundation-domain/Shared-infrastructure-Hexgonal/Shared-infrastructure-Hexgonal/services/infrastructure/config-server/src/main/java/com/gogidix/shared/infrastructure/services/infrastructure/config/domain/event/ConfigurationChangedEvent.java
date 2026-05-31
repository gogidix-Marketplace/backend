package com.gogidix.shared.infrastructure.services.infrastructure.config.domain.event;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain event published when a configuration is changed.
 */
public class ConfigurationChangedEvent {

    private final String configId;
    private final String applicationName;
    private final String profile;
    private final String label;
    private final String version;
    private final String changeType;
    private final Instant occurredAt;

    public ConfigurationChangedEvent(String configId, String applicationName, String profile,
                                     String label, String version, String changeType, Instant occurredAt) {
        this.configId = configId;
        this.applicationName = applicationName;
        this.profile = profile;
        this.label = label;
        this.version = version;
        this.changeType = changeType;
        this.occurredAt = occurredAt;
    }

    public String getConfigId() {
        return configId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getProfile() {
        return profile;
    }

    public String getLabel() {
        return label;
    }

    public String getVersion() {
        return version;
    }

    public String getChangeType() {
        return changeType;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigurationChangedEvent that = (ConfigurationChangedEvent) o;
        return Objects.equals(configId, that.configId) &&
            Objects.equals(version, that.version) &&
            Objects.equals(changeType, that.changeType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configId, version, changeType);
    }

    @Override
    public String toString() {
        return "ConfigurationChangedEvent{" +
            "configId='" + configId + '\'' +
            ", applicationName='" + applicationName + '\'' +
            ", profile='" + profile + '\'' +
            ", version='" + version + '\'' +
            ", changeType='" + changeType + '\'' +
            ", occurredAt=" + occurredAt +
            '}';
    }
}
