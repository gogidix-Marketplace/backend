package com.gogidix.dashboard.gateway.chart.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Entity representing a chart configuration.
 */
@Entity
@Table(name = "chart_configuration", indexes = {
    @Index(name = "idx_chart_id", columnList = "chart_id"),
    @Index(name = "idx_tenant_id", columnList = "tenant_id"),
    @Index(name = "idx_chart_type", columnList = "chart_type")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ChartConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chart_id", nullable = false, unique = true)
    private String chartId;

    @Column(name = "chart_name", nullable = false)
    private String chartName;

    @Column(name = "chart_type", nullable = false)
    private String chartType;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "description")
    private String description;

    @Column(name = "data_source")
    private String dataSource;

    @Column(name = "query")
    private String query;

    @Column(name = "config")
    @Builder.Default
    private Map<String, Object> config = Map.of();

    @Column(name = "refresh_interval_seconds")
    @Builder.Default
    private Integer refreshIntervalSeconds = 60;

    @Column(name = "enabled")
    @Builder.Default
    private Boolean enabled = true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
