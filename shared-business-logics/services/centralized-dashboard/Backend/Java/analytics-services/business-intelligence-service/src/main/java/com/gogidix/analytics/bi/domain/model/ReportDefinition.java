package com.gogidix.analytics.bi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "report_definitions", indexes = {
    @Index(name = "idx_report_tenant_id", columnList = "tenant_id"),
    @Index(name = "idx_report_owner", columnList = "owner_id"),
    @Index(name = "idx_report_schedule", columnList = "schedule_type"),
    @Index(name = "idx_report_enabled", columnList = "enabled")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "report_name", nullable = false, length = 255)
    private String reportName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false, length = 50)
    private ReportType reportType;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_type", length = 50)
    private ScheduleType scheduleType;

    @Column(name = "schedule_config", columnDefinition = "JSONB")
    private String scheduleConfig;

    @Column(name = "data_source", columnDefinition = "JSONB")
    private String dataSource;

    @Column(name = "query_definition", columnDefinition = "TEXT")
    private String queryDefinition;

    @Enumerated(EnumType.STRING)
    @Column(name = "output_format", length = 20)
    @Builder.Default
    private OutputFormat outputFormat = OutputFormat.PDF;

    @Column(name = "template_config", columnDefinition = "JSONB")
    private String templateConfig;

    @Column(name = "recipients", columnDefinition = "TEXT")
    private String recipients;

    @Column(name = "owner_id", nullable = false, length = 100)
    private String ownerId;

    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;

    @Column(name = "enabled", nullable = false)
    @Builder.Default
    private Boolean enabled = true;

    @Column(name = "last_run_at")
    private LocalDateTime lastRunAt;

    @Column(name = "next_run_at")
    private LocalDateTime nextRunAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "reportDefinition", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ReportExecution> executions = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (id == null) {
            id = java.util.UUID.randomUUID().toString();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum ReportType {
        SUMMARY,
        DETAILED,
        TREND,
        COMPARISON,
        CUSTOM
    }

    public enum ScheduleType {
        MANUAL,
        HOURLY,
        DAILY,
        WEEKLY,
        MONTHLY,
        QUARTERLY,
        YEARLY,
        CUSTOM
    }

    public enum OutputFormat {
        PDF,
        EXCEL,
        CSV,
        HTML,
        JSON
    }
}
