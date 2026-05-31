package com.gogidix.dashboard.core.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Dashboard KPI aggregate root.
 * Manages Key Performance Indicators for dashboard visualization.
 *
 * Multi-tenancy: Uses tenantId for tenant isolation.
 */
@Entity
@Table(name = "dashboard_kpis", indexes = {
    @Index(name = "idx_kpi_tenant_id", columnList = "tenant_id"),
    @Index(name = "idx_kpi_category", columnList = "category"),
    @Index(name = "idx_kpi_source_domain", columnList = "source_domain"),
    @Index(name = "idx_kpi_is_active", columnList = "is_active")
})
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardKPI {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "code", nullable = false, unique = true, length = 100)
    private String code;

    @Column(name = "category", nullable = false, length = 100)
    private String category;

    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_domain", nullable = false, length = 50)
    private SourceDomain sourceDomain;

    @Column(name = "unit", length = 50)
    private String unit;

    @Column(name = "data_type", nullable = false, length = 50)
    private String dataType;

    @Column(name = "aggregation_type", length = 50)
    private String aggregationType;

    @Column(name = "formula", columnDefinition = "TEXT")
    private String formula;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "is_real_time", nullable = false)
    @Builder.Default
    private Boolean isRealTime = false;

    @Column(name = "refresh_interval_seconds")
    private Integer refreshIntervalSeconds;

    @Column(name = "threshold_warning")
    private Double thresholdWarning;

    @Column(name = "threshold_critical", nullable = false)
    private Double thresholdCritical;

    @Column(name = "target_value")
    private Double targetValue;

    @Column(name = "current_value")
    private Double currentValue;

    @Column(name = "previous_value")
    private Double previousValue;

    @Column(name = "trend", length = 20)
    private String trend;

    @Column(name = "last_calculated_at")
    private LocalDateTime lastCalculatedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @OneToMany(mappedBy = "kpi", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<KPIValue> historicalValues = new ArrayList<>();

    @OneToMany(mappedBy = "kpi", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<KPITarget> targets = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void updateValue(Double newValue) {
        if (this.currentValue != null) {
            this.previousValue = this.currentValue;
        }
        this.currentValue = newValue;
        this.lastCalculatedAt = LocalDateTime.now();

        if (this.previousValue != null) {
            if (newValue > this.previousValue) {
                this.trend = "UP";
            } else if (newValue < this.previousValue) {
                this.trend = "DOWN";
            } else {
                this.trend = "STABLE";
            }
        }
    }

    public boolean isAboveThreshold() {
        return thresholdCritical != null && currentValue != null && currentValue > thresholdCritical;
    }

    public boolean isBelowThreshold() {
        return thresholdCritical != null && currentValue != null && currentValue < thresholdCritical;
    }

    public boolean isAtWarningLevel() {
        return thresholdWarning != null && currentValue != null && currentValue >= thresholdWarning;
    }

    public Double calculateProgress() {
        if (targetValue == null || currentValue == null || targetValue == 0) {
            return null;
        }
        return (currentValue / targetValue) * 100;
    }

    public void addHistoricalValue(KPIValue value) {
        value.setKpi(this);
        this.historicalValues.add(value);
    }

    public void addTarget(KPITarget target) {
        target.setKpi(this);
        this.targets.add(target);
    }
}
