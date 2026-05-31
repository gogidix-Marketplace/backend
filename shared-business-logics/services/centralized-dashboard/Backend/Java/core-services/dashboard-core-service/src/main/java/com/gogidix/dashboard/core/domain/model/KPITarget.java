package com.gogidix.dashboard.core.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * KPI target entity.
 * Defines target values for KPIs over specific time periods.
 */
@Entity
@Table(name = "kpi_targets", indexes = {
    @Index(name = "idx_kpi_target_kpi_id", columnList = "kpi_id"),
    @Index(name = "idx_kpi_target_period", columnList = "target_period"),
    @Index(name = "idx_kpi_target_date", columnList = "target_date")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KPITarget {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kpi_id", nullable = false)
    private DashboardKPI kpi;

    @Column(name = "target_value", nullable = false)
    private Double targetValue;

    @Column(name = "target_period", length = 50)
    private String targetPeriod;

    @Column(name = "target_date")
    private LocalDate targetDate;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "minimum_acceptable")
    private Double minimumAcceptable;

    @Column(name = "stretch_target")
    private Double stretchTarget;

    @Column(name = "owner", length = 100)
    private String owner;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Double getProgressPercentage(Double currentValue) {
        if (targetValue == null || targetValue == 0 || currentValue == null) {
            return null;
        }
        return (currentValue / targetValue) * 100;
    }

    public boolean isTargetMet(Double currentValue) {
        return currentValue != null && targetValue != null && currentValue >= targetValue;
    }

    public boolean isBelowMinimum(Double currentValue) {
        return minimumAcceptable != null && currentValue != null && currentValue < minimumAcceptable;
    }
}
