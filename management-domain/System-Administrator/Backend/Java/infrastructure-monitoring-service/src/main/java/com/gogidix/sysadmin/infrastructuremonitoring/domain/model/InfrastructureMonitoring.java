package com.gogidix.sysadmin.infrastructuremonitoring.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Document(collection = "infrastructure_monitoring")
public class InfrastructureMonitoring {

    @Id
    private String id;
    private String tenantId;
    private String name;
    private InfrastructureType type;
    private String host;
    private Integer port;
    private MonitoringStatus status;
    private HealthCheckConfiguration healthCheckConfig;
    private Map<String, String> tags;
    private List<MetricSnapshot> recentMetrics;
    private Instant lastCheckedAt;
    private Instant createdAt;
    private Instant updatedAt;
    private String region;
    private String environment;

    public enum InfrastructureType {
        SERVER, DATABASE, CACHE, MESSAGE_QUEUE, LOAD_BALANCER,
        CONTAINER, KUBERNETES_CLUSTER, STORAGE, CDN, API_GATEWAY
    }

    public enum MonitoringStatus {
        HEALTHY, DEGRADED, UNHEALTHY, UNKNOWN, MAINTENANCE
    }

    public static class HealthCheckConfiguration {
        private String protocol;
        private String path;
        private Integer intervalSeconds;
        private Integer timeoutSeconds;
        private Integer retryCount;
        private Map<String, String> headers;
        private String expectedStatusCode;

        public HealthCheckConfiguration() {}

        public HealthCheckConfiguration(String protocol, String path, Integer intervalSeconds,
                                       Integer timeoutSeconds, Integer retryCount) {
            this.protocol = protocol;
            this.path = path;
            this.intervalSeconds = intervalSeconds;
            this.timeoutSeconds = timeoutSeconds;
            this.retryCount = retryCount;
        }

        public String getProtocol() { return protocol; }
        public void setProtocol(String protocol) { this.protocol = protocol; }

        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }

        public Integer getIntervalSeconds() { return intervalSeconds; }
        public void setIntervalSeconds(Integer intervalSeconds) { this.intervalSeconds = intervalSeconds; }

        public Integer getTimeoutSeconds() { return timeoutSeconds; }
        public void setTimeoutSeconds(Integer timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }

        public Integer getRetryCount() { return retryCount; }
        public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }

        public Map<String, String> getHeaders() { return headers; }
        public void setHeaders(Map<String, String> headers) { this.headers = headers; }

        public String getExpectedStatusCode() { return expectedStatusCode; }
        public void setExpectedStatusCode(String expectedStatusCode) { this.expectedStatusCode = expectedStatusCode; }
    }

    public static class MetricSnapshot {
        private String metricName;
        private Double value;
        private String unit;
        private Instant timestamp;

        public MetricSnapshot() {}

        public MetricSnapshot(String metricName, Double value, String unit, Instant timestamp) {
            this.metricName = metricName;
            this.value = value;
            this.unit = unit;
            this.timestamp = timestamp;
        }

        public String getMetricName() { return metricName; }
        public void setMetricName(String metricName) { this.metricName = metricName; }

        public Double getValue() { return value; }
        public void setValue(Double value) { this.value = value; }

        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }

        public Instant getTimestamp() { return timestamp; }
        public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    }

    public InfrastructureMonitoring() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.recentMetrics = new ArrayList<>();
        this.status = MonitoringStatus.UNKNOWN;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public InfrastructureType getType() { return type; }
    public void setType(InfrastructureType type) { this.type = type; }

    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }

    public Integer getPort() { return port; }
    public void setPort(Integer port) { this.port = port; }

    public MonitoringStatus getStatus() { return status; }
    public void setStatus(MonitoringStatus status) { this.status = status; }

    public HealthCheckConfiguration getHealthCheckConfig() { return healthCheckConfig; }
    public void setHealthCheckConfig(HealthCheckConfiguration healthCheckConfig) { this.healthCheckConfig = healthCheckConfig; }

    public Map<String, String> getTags() { return tags; }
    public void setTags(Map<String, String> tags) { this.tags = tags; }

    public List<MetricSnapshot> getRecentMetrics() { return recentMetrics; }
    public void setRecentMetrics(List<MetricSnapshot> recentMetrics) { this.recentMetrics = recentMetrics; }

    public Instant getLastCheckedAt() { return lastCheckedAt; }
    public void setLastCheckedAt(Instant lastCheckedAt) { this.lastCheckedAt = lastCheckedAt; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InfrastructureMonitoring that = (InfrastructureMonitoring) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void addMetric(MetricSnapshot metric) {
        if (this.recentMetrics == null) {
            this.recentMetrics = new ArrayList<>();
        }
        this.recentMetrics.add(metric);
        if (this.recentMetrics.size() > 100) {
            this.recentMetrics.remove(0);
        }
    }

    public void updateStatus(MonitoringStatus newStatus) {
        this.status = newStatus;
        this.lastCheckedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final InfrastructureMonitoring instance = new InfrastructureMonitoring();

        public Builder id(String id) { instance.setId(id); return this; }
        public Builder tenantId(String tenantId) { instance.setTenantId(tenantId); return this; }
        public Builder name(String name) { instance.setName(name); return this; }
        public Builder type(InfrastructureType type) { instance.setType(type); return this; }
        public Builder host(String host) { instance.setHost(host); return this; }
        public Builder port(Integer port) { instance.setPort(port); return this; }
        public Builder status(MonitoringStatus status) { instance.setStatus(status); return this; }
        public Builder healthCheckConfig(HealthCheckConfiguration config) { instance.setHealthCheckConfig(config); return this; }
        public Builder tags(Map<String, String> tags) { instance.setTags(tags); return this; }
        public Builder region(String region) { instance.setRegion(region); return this; }
        public Builder environment(String environment) { instance.setEnvironment(environment); return this; }

        public InfrastructureMonitoring build() {
            return instance;
        }
    }
}
