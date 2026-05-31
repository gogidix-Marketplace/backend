package com.gogidix.management.executive.analytics.domain.model;

import com.gogidix.management.shared.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Document(collection = "metrics")
@TypeAlias("metric")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "metric_tenant_source_idx", def = "{'tenantId': 1, 'sourceDomain': 1, 'metricName': 1, 'timestamp': -1}")
@CompoundIndex(name = "metric_tenant_timestamp_idx", def = "{'tenantId': 1, 'timestamp': -1}")
public class Metric extends BaseEntity {

    @Indexed
    private String metricName;
    private String sourceDomain;
    private String sourceService;
    private BigDecimal value;
    private String unit;
    private Instant timestamp;
    private String granularity;
    private Map<String, String> dimensions;
    private Map<String, Object> metadata;
    private BigDecimal qualityScore;

    @Builder.Default
    private Boolean isAggregate = false;

    private String parentMetricId;

    public Metric(String tenantId, String metricName, String sourceDomain, BigDecimal value, Instant timestamp) {
        super(tenantId);
        this.metricName = metricName;
        this.sourceDomain = sourceDomain;
        this.value = value;
        this.timestamp = timestamp != null ? timestamp : Instant.now();
        this.qualityScore = BigDecimal.ONE;
    }

    public Metric(String tenantId, String metricName, String sourceDomain, BigDecimal value, String unit, Instant timestamp) {
        this(tenantId, metricName, sourceDomain, value, timestamp);
        this.unit = unit;
    }

    public String getDimension(String key) {
        return dimensions != null ? dimensions.get(key) : null;
    }

    public void addDimension(String key, String value) {
        if (this.dimensions == null) {
            this.dimensions = new java.util.HashMap<>();
        }
        this.dimensions.put(key, value);
    }

    public boolean hasHighQuality() {
        return qualityScore != null && qualityScore.compareTo(new BigDecimal("0.9")) >= 0;
    }

    public boolean isRecent() {
        Instant yesterday = Instant.now().minusSeconds(86400);
        return timestamp != null && timestamp.isAfter(yesterday);
    }

    public Object getMetadata(String key) {
        return metadata != null ? metadata.get(key) : null;
    }

    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new java.util.HashMap<>();
        }
        this.metadata.put(key, value);
    }

    public void markAsAggregate() {
        this.isAggregate = true;
        this.touch();
    }

    public void updateQualityScore(BigDecimal qualityScore) {
        if (qualityScore != null) {
            if (qualityScore.compareTo(BigDecimal.ZERO) < 0) {
                this.qualityScore = BigDecimal.ZERO;
            } else if (qualityScore.compareTo(BigDecimal.ONE) > 0) {
                this.qualityScore = BigDecimal.ONE;
            } else {
                this.qualityScore = qualityScore;
            }
        }
    }
}
