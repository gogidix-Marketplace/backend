package com.gogidix.sysadmin.performancemetrics.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

@Document(collection = "performance_metrics")
public class PerformanceMetric {

    @Id
    private String id;
    @Indexed
    private String tenantId;
    @Indexed
    private String resourceId;
    private String resourceType;
    @Indexed
    private String metricName;
    private Double value;
    private String unit;
    private Map<String, Object> dimensions;
    @Indexed
    private Instant timestamp;
    private String source;
    private Instant createdAt;

    public enum ResourceType {
        SERVER, DATABASE, CACHE, MESSAGE_QUEUE, LOAD_BALANCER,
        CONTAINER, KUBERNETES_POD, KUBERNETES_NODE, STORAGE, API
    }

    public enum MetricName {
        CPU_USAGE, MEMORY_USAGE, DISK_USAGE, NETWORK_IN, NETWORK_OUT,
        RESPONSE_TIME, REQUEST_COUNT, ERROR_RATE, THROUGHPUT,
        QUEUE_DEPTH, CONNECTION_COUNT, THREAD_COUNT, GC_TIME
    }

    public PerformanceMetric() {
        this.createdAt = Instant.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getResourceId() { return resourceId; }
    public void setResourceId(String resourceId) { this.resourceId = resourceId; }

    public String getResourceType() { return resourceType; }
    public void setResourceType(String resourceType) { this.resourceType = resourceType; }

    public String getMetricName() { return metricName; }
    public void setMetricName(String metricName) { this.metricName = metricName; }

    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Map<String, Object> getDimensions() { return dimensions; }
    public void setDimensions(Map<String, Object> dimensions) { this.dimensions = dimensions; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PerformanceMetric that = (PerformanceMetric) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final PerformanceMetric instance = new PerformanceMetric();

        public Builder tenantId(String tenantId) { instance.setTenantId(tenantId); return this; }
        public Builder resourceId(String resourceId) { instance.setResourceId(resourceId); return this; }
        public Builder resourceType(String resourceType) { instance.setResourceType(resourceType); return this; }
        public Builder metricName(String metricName) { instance.setMetricName(metricName); return this; }
        public Builder value(Double value) { instance.setValue(value); return this; }
        public Builder unit(String unit) { instance.setUnit(unit); return this; }
        public Builder dimensions(Map<String, Object> dimensions) { instance.setDimensions(dimensions); return this; }
        public Builder timestamp(Instant timestamp) { instance.setTimestamp(timestamp); return this; }
        public Builder source(String source) { instance.setSource(source); return this; }

        public PerformanceMetric build() {
            if (instance.timestamp == null) {
                instance.setTimestamp(Instant.now());
            }
            return instance;
        }
    }
}
