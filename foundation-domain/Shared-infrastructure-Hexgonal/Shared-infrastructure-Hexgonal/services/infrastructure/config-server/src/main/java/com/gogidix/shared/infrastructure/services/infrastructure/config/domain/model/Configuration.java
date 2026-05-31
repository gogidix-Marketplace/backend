package com.gogidix.shared.infrastructure.services.infrastructure.config.domain.model;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Configuration domain model.
 * Represents a configuration entity with version tracking.
 */
public class Configuration {

    private final String configId;
    private final String applicationName;
    private final String profile;
    private final String label;
    private final Map<String, Object> properties;
    private final String version;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final String createdBy;
    private final String updatedBy;

    private Configuration(Builder builder) {
        this.configId = builder.configId;
        this.applicationName = builder.applicationName;
        this.profile = builder.profile;
        this.label = builder.label;
        this.properties = builder.properties;
        this.version = builder.version;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.createdBy = builder.createdBy;
        this.updatedBy = builder.updatedBy;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public String getConfigId() { return configId; }
    public String getApplicationName() { return applicationName; }
    public String getProfile() { return profile; }
    public String getLabel() { return label; }
    public Map<String, Object> getProperties() { return properties; }
    public String getVersion() { return version; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public String getCreatedBy() { return createdBy; }
    public String getUpdatedBy() { return updatedBy; }

    public String getCompositeKey() {
        return applicationName + "-" + profile + "-" + label;
    }

    public boolean isValid() {
        return applicationName != null && !applicationName.isBlank()
            && properties != null && !properties.isEmpty();
    }

    public boolean isEncrypted(String key) {
        Object value = properties.get(key);
        return value != null && value.toString().startsWith("{cipher}");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration that = (Configuration) o;
        return Objects.equals(configId, that.configId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configId);
    }

    @Override
    public String toString() {
        return "Configuration{" +
            "configId='" + configId + '\'' +
            ", applicationName='" + applicationName + '\'' +
            ", profile='" + profile + '\'' +
            ", label='" + label + '\'' +
            ", version='" + version + '\'' +
            ", createdAt=" + createdAt +
            '}';
    }

    public static class Builder {
        private String configId;
        private String applicationName;
        private String profile;
        private String label;
        private Map<String, Object> properties;
        private String version;
        private Instant createdAt;
        private Instant updatedAt;
        private String createdBy;
        private String updatedBy;

        public Builder configId(String configId) {
            this.configId = configId;
            return this;
        }

        public Builder applicationName(String applicationName) {
            this.applicationName = applicationName;
            return this;
        }

        public Builder profile(String profile) {
            this.profile = profile;
            return this;
        }

        public Builder label(String label) {
            this.label = label;
            return this;
        }

        public Builder properties(Map<String, Object> properties) {
            this.properties = properties;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public Configuration build() {
            return new Configuration(this);
        }
    }
}
