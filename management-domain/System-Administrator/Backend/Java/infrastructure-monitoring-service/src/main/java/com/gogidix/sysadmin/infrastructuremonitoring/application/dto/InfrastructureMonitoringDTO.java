package com.gogidix.sysadmin.infrastructuremonitoring.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.sysadmin.infrastructuremonitoring.domain.model.InfrastructureMonitoring;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class InfrastructureMonitoringDTO {

    private String id;
    private String name;
    private InfrastructureMonitoring.InfrastructureType type;
    private String host;
    private Integer port;
    private InfrastructureMonitoring.MonitoringStatus status;
    private HealthCheckConfigDTO healthCheckConfig;
    private Map<String, String> tags;
    private List<MetricSnapshotDTO> recentMetrics;
    private String region;
    private String environment;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant lastCheckedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant createdAt;

    public static class HealthCheckConfigDTO {
        private String protocol;
        private String path;
        private Integer intervalSeconds;
        private Integer timeoutSeconds;
        private Integer retryCount;

        public HealthCheckConfigDTO() {}

        public static HealthCheckConfigDTO fromEntity(InfrastructureMonitoring.HealthCheckConfiguration config) {
            if (config == null) return null;
            HealthCheckConfigDTO dto = new HealthCheckConfigDTO();
            dto.protocol = config.getProtocol();
            dto.path = config.getPath();
            dto.intervalSeconds = config.getIntervalSeconds();
            dto.timeoutSeconds = config.getTimeoutSeconds();
            dto.retryCount = config.getRetryCount();
            return dto;
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
    }

    public static class MetricSnapshotDTO {
        private String metricName;
        private Double value;
        private String unit;
        private Instant timestamp;

        public MetricSnapshotDTO() {}

        public static MetricSnapshotDTO fromEntity(InfrastructureMonitoring.MetricSnapshot metric) {
            if (metric == null) return null;
            MetricSnapshotDTO dto = new MetricSnapshotDTO();
            dto.metricName = metric.getMetricName();
            dto.value = metric.getValue();
            dto.unit = metric.getUnit();
            dto.timestamp = metric.getTimestamp();
            return dto;
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

    public static InfrastructureMonitoringDTO fromEntity(InfrastructureMonitoring entity) {
        if (entity == null) return null;
        InfrastructureMonitoringDTO dto = new InfrastructureMonitoringDTO();
        dto.id = entity.getId();
        dto.name = entity.getName();
        dto.type = entity.getType();
        dto.host = entity.getHost();
        dto.port = entity.getPort();
        dto.status = entity.getStatus();
        dto.healthCheckConfig = HealthCheckConfigDTO.fromEntity(entity.getHealthCheckConfig());
        dto.tags = entity.getTags();
        dto.region = entity.getRegion();
        dto.environment = entity.getEnvironment();
        dto.lastCheckedAt = entity.getLastCheckedAt();
        dto.createdAt = entity.getCreatedAt();
        if (entity.getRecentMetrics() != null) {
            dto.recentMetrics = entity.getRecentMetrics().stream()
                    .map(MetricSnapshotDTO::fromEntity).toList();
        }
        return dto;
    }

    public InfrastructureMonitoring toEntity() {
        InfrastructureMonitoring entity = new InfrastructureMonitoring();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setType(this.type);
        entity.setHost(this.host);
        entity.setPort(this.port);
        entity.setStatus(this.status);
        entity.setTags(this.tags);
        entity.setRegion(this.region);
        entity.setEnvironment(this.environment);

        if (this.healthCheckConfig != null) {
            InfrastructureMonitoring.HealthCheckConfiguration config = new InfrastructureMonitoring.HealthCheckConfiguration();
            config.setProtocol(this.healthCheckConfig.getProtocol());
            config.setPath(this.healthCheckConfig.getPath());
            config.setIntervalSeconds(this.healthCheckConfig.getIntervalSeconds());
            config.setTimeoutSeconds(this.healthCheckConfig.getTimeoutSeconds());
            config.setRetryCount(this.healthCheckConfig.getRetryCount());
            entity.setHealthCheckConfig(config);
        }
        return entity;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public InfrastructureMonitoring.InfrastructureType getType() { return type; }
    public void setType(InfrastructureMonitoring.InfrastructureType type) { this.type = type; }

    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }

    public Integer getPort() { return port; }
    public void setPort(Integer port) { this.port = port; }

    public InfrastructureMonitoring.MonitoringStatus getStatus() { return status; }
    public void setStatus(InfrastructureMonitoring.MonitoringStatus status) { this.status = status; }

    public HealthCheckConfigDTO getHealthCheckConfig() { return healthCheckConfig; }
    public void setHealthCheckConfig(HealthCheckConfigDTO healthCheckConfig) { this.healthCheckConfig = healthCheckConfig; }

    public Map<String, String> getTags() { return tags; }
    public void setTags(Map<String, String> tags) { this.tags = tags; }

    public List<MetricSnapshotDTO> getRecentMetrics() { return recentMetrics; }
    public void setRecentMetrics(List<MetricSnapshotDTO> recentMetrics) { this.recentMetrics = recentMetrics; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }

    public Instant getLastCheckedAt() { return lastCheckedAt; }
    public void setLastCheckedAt(Instant lastCheckedAt) { this.lastCheckedAt = lastCheckedAt; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
