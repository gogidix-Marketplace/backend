package com.gogidix.platform.platform.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Maintenance window for planned system maintenance.
 *
 * Manages:
 * - Scheduled maintenance windows
 * - Affected services
 * - Impact levels
 * - Notifications
 * - Maintenance summaries
 */
@Entity
@Table(name = "maintenance_windows", indexes = {
    @Index(name = "idx_maintenance_tenant", columnList = "tenant_id"),
    @Index(name = "idx_maintenance_schedule", columnList = "scheduled_start, scheduled_end"),
    @Index(name = "idx_maintenance_status", columnList = "status"),
    @Index(name = "idx_maintenance_impact", columnList = "impact_level")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceWindow {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;

    /**
     * Maintenance window name
     */
    @Column(name = "window_name", nullable = false, length = 255)
    private String windowName;

    /**
     * Maintenance description
     */
    @Lob
    @Convert(converter = com.gogidix.platform.platform.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "description")
    private String description;

    /**
     * List of affected service names
     */
    @Convert(converter = com.gogidix.platform.platform.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "affected_services")
    private String[] affectedServices;

    /**
     * Scheduled start time
     */
    @Column(name = "scheduled_start", nullable = false)
    private LocalDateTime scheduledStart;

    /**
     * Scheduled end time
     */
    @Column(name = "scheduled_end", nullable = false)
    private LocalDateTime scheduledEnd;

    /**
     * Actual start time (when maintenance actually started)
     */
    @Convert(converter = com.gogidix.platform.platform.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "actual_start")
    private LocalDateTime actualStart;

    /**
     * Actual end time (when maintenance actually ended)
     */
    @Convert(converter = com.gogidix.platform.platform.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "actual_end")
    private LocalDateTime actualEnd;

    /**
     * Current maintenance status
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    @Builder.Default
    private MaintenanceStatus status = MaintenanceStatus.SCHEDULED;

    /**
     * Impact level
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "impact_level", length = 50)
    @Builder.Default
    private ImpactLevel impactLevel = ImpactLevel.MEDIUM;

    /**
     * Whether notification has been sent
     */
    @Column(name = "notification_sent", nullable = false)
    @Builder.Default
    private boolean notificationSent = false;

    /**
     * Notification lead time in minutes
     */
    @Convert(converter = com.gogidix.platform.platform.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "notification_lead_time_minutes")
    @Builder.Default
    private Integer notificationLeadTimeMinutes = 60;

    /**
     * Post-maintenance summary
     */
    @Lob
    @Convert(converter = com.gogidix.platform.platform.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "post_maintenance_summary")
    private String postMaintenanceSummary;

    /**
     * Additional metadata
     */
    @Column(name = "metadata", columnDefinition = "JSONB")
    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 255)
    private String createdBy;

    @Column(name = "updated_by", length = 255)
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Start maintenance
     */
    public void start() {
        if (status != MaintenanceStatus.SCHEDULED) {
            throw new IllegalStateException("Can only start scheduled maintenance");
        }
        this.status = MaintenanceStatus.IN_PROGRESS;
        this.actualStart = LocalDateTime.now();
    }

    /**
     * Complete maintenance
     */
    public void complete() {
        if (status != MaintenanceStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only complete in-progress maintenance");
        }
        this.status = MaintenanceStatus.COMPLETED;
        this.actualEnd = LocalDateTime.now();
    }

    /**
     * Cancel maintenance
     */
    public void cancel() {
        if (status == MaintenanceStatus.COMPLETED || status == MaintenanceStatus.CANCELLED) {
            throw new IllegalStateException("Cannot cancel maintenance that is " + status);
        }
        this.status = MaintenanceStatus.CANCELLED;
    }

    /**
     * Check if maintenance is currently active
     */
    public boolean isCurrentlyActive() {
        if (status != MaintenanceStatus.IN_PROGRESS) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        return !now.isBefore(actualStart) &&
               (actualEnd == null || now.isBefore(actualEnd));
    }

    /**
     * Check if maintenance is upcoming
     */
    public boolean isUpcoming() {
        if (status != MaintenanceStatus.SCHEDULED) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        return now.isBefore(scheduledStart);
    }

    /**
     * Get maintenance duration in minutes
     */
    public long getDurationMinutes() {
        if (actualStart != null && actualEnd != null) {
            return java.time.Duration.between(actualStart, actualEnd).toMinutes();
        }
        return java.time.Duration.between(scheduledStart, scheduledEnd).toMinutes();
    }

    public enum MaintenanceStatus {
        SCHEDULED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED
    }

    public enum ImpactLevel {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }

    public enum MaintenanceType {
        SCHEDULED,
        EMERGENCY,
        ROLLING,
        PATCH,
        UPGRADE
    }
}
