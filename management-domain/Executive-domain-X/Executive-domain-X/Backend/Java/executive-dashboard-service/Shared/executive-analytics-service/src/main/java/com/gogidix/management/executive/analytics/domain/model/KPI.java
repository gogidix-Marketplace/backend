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
import java.math.RoundingMode;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(collection = "kpi_metrics")
@TypeAlias("kpi")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "kpi_tenant_category_idx", def = "{'tenantId': 1, 'category': 1, 'period': -1}")
@CompoundIndex(name = "kpi_tenant_executive_idx", def = "{'tenantId': 1, 'executiveLevel': 1, 'category': 1, 'period': -1}")
public class KPI extends BaseEntity {

    @Indexed
    private String name;
    private String category;
    private String executiveLevel;
    private BigDecimal value;
    private String unit;
    private String period;
    private BigDecimal target;
    private BigDecimal previousValue;
    private BigDecimal percentChange;
    private String status;
    private String trend;
    private List<String> dataSources;
    private Map<String, Object> metadata;
    @Builder.Default
    private Boolean visible = true;
    private Instant lastCalculatedAt;
    private Boolean isCalculated = true;

    public KPI(String tenantId, String name, String category, BigDecimal value) {
        super(tenantId);
        this.name = name;
        this.category = category;
        this.value = value;
        this.lastCalculatedAt = Instant.now();
    }

    public KPI(String tenantId, String name, String category, String executiveLevel, BigDecimal value, String period) {
        super(tenantId);
        this.name = name;
        this.category = category;
        this.executiveLevel = executiveLevel;
        this.value = value;
        this.period = period;
        this.lastCalculatedAt = Instant.now();
    }

    public boolean isOnTrack() {
        return "ON_TRACK".equals(this.status) || "AHEAD".equals(this.status);
    }

    public boolean needsAttention() {
        return "AT_RISK".equals(this.status) || "BEHIND".equals(this.status);
    }

    public void calculatePercentChange() {
        if (previousValue != null && previousValue.compareTo(BigDecimal.ZERO) != 0 && value != null) {
            BigDecimal change = value.subtract(previousValue);
            this.percentChange = change.divide(previousValue, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
        }
    }

    public void updateStatus() {
        if (target != null && target.compareTo(BigDecimal.ZERO) != 0 && value != null) {
            BigDecimal ratio = value.divide(target, 2, RoundingMode.HALF_UP);
            if (ratio.compareTo(new BigDecimal("1.1")) >= 0) {
                this.status = "AHEAD";
                this.trend = "UP";
            } else if (ratio.compareTo(new BigDecimal("0.9")) >= 0) {
                this.status = "ON_TRACK";
                this.trend = "STABLE";
            } else if (ratio.compareTo(new BigDecimal("0.8")) >= 0) {
                this.status = "AT_RISK";
                this.trend = "DOWN";
            } else {
                this.status = "BEHIND";
                this.trend = "DOWN";
            }
        }
    }

    public void markAsRecalculated() {
        this.lastCalculatedAt = Instant.now();
        this.touch();
    }

    public void addDimension(String key, String value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put("dimension_" + key, value);
    }

    public String getDimension(String key) {
        if (this.metadata == null) {
            return null;
        }
        Object value = this.metadata.get("dimension_" + key);
        return value != null ? value.toString() : null;
    }

    public Map<String, Object> getDimensions() {
        if (this.metadata == null) {
            return new HashMap<>();
        }
        Map<String, Object> dimensions = new HashMap<>();
        for (Map.Entry<String, Object> entry : this.metadata.entrySet()) {
            if (entry.getKey().startsWith("dimension_")) {
                dimensions.put(entry.getKey().substring("dimension_".length()), entry.getValue());
            }
        }
        return dimensions;
    }

    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
    }

    public Object getMetadata(String key) {
        if (this.metadata == null) {
            return null;
        }
        return this.metadata.get(key);
    }
}
