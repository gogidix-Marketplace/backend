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
 * Platform announcements for system-wide notifications.
 *
 * Supports:
 * - Different announcement types (info, warning, maintenance, critical)
 * - Priority levels
 * - Target audience filtering
 * - Scheduling (from/to dates)
 * - Dismissal tracking
 * - Analytics (views, clicks)
 */
@Entity
@Table(name = "platform_announcements", indexes = {
    @Index(name = "idx_announcements_tenant", columnList = "tenant_id"),
    @Index(name = "idx_announcements_type", columnList = "announcement_type"),
    @Index(name = "idx_announcements_priority", columnList = "priority"),
    @Index(name = "idx_announcements_schedule", columnList = "scheduled_from, scheduled_until"),
    @Index(name = "idx_announcements_status", columnList = "status")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatformAnnouncement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "tenant_id", nullable = false, length = 100)
    private String tenantId;

    /**
     * Announcement title
     */
    @Column(name = "title", nullable = false, length = 500)
    private String title;

    /**
     * Announcement content (can be HTML or markdown)
     */
    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    /**
     * Type of announcement
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "announcement_type", nullable = false, length = 50)
    private AnnouncementType announcementType;

    /**
     * Priority level
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", length = 50)
    @Builder.Default
    private Priority priority = Priority.NORMAL;

    /**
     * Target audience (e.g., ["ALL"], ["ADMINS"], ["USERS"])
     */
    @Convert(converter = com.gogidix.platform.platform.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "target_audience")
    private String[] targetAudience;

    /**
     * Whether announcement is pinned to top
     */
    @Column(name = "is_pinned", nullable = false)
    @Builder.Default
    private boolean isPinned = false;

    /**
     * Whether users can dismiss the announcement
     */
    @Column(name = "is_dismissible", nullable = false)
    @Builder.Default
    private boolean isDismissible = true;

    /**
     * When announcement becomes visible
     */
    @Column(name = "scheduled_from", nullable = false)
    private LocalDateTime scheduledFrom;

    /**
     * When announcement stops being visible
     */
    @Convert(converter = com.gogidix.platform.platform.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "scheduled_until")
    private LocalDateTime scheduledUntil;

    /**
     * Number of times announcement was dismissed
     */
    @Convert(converter = com.gogidix.platform.platform.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "dismiss_count")
    @Builder.Default
    private Integer dismissCount = 0;

    /**
     * Number of times announcement was viewed
     */
    @Convert(converter = com.gogidix.platform.platform.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "view_count")
    @Builder.Default
    private Integer viewCount = 0;

    /**
     * Number of clicks on announcement links
     */
    @Convert(converter = com.gogidix.platform.platform.infrastructure.persistence.JsonMapConverter.class)
    @Column(name = "click_count")
    @Builder.Default
    private Integer clickCount = 0;

    /**
     * Additional metadata
     */
    @Column(name = "metadata", columnDefinition = "JSONB")
    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    /**
     * Announcement status
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    @Builder.Default
    private AnnouncementStatus status = AnnouncementStatus.ACTIVE;

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
     * Check if announcement is currently visible
     */
    public boolean isVisible() {
        if (status != AnnouncementStatus.ACTIVE) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(scheduledFrom)) {
            return false;
        }

        if (scheduledUntil != null && now.isAfter(scheduledUntil)) {
            return false;
        }

        return true;
    }

    /**
     * Record a view
     */
    public void recordView() {
        this.viewCount++;
    }

    /**
     * Record a click
     */
    public void recordClick() {
        this.clickCount++;
    }

    /**
     * Record a dismissal
     */
    public void recordDismissal() {
        if (isDismissible) {
            this.dismissCount++;
        }
    }

    /**
     * Archive announcement
     */
    public void archive() {
        this.status = AnnouncementStatus.ARCHIVED;
    }

    public enum AnnouncementType {
        INFO,
        WARNING,
        MAINTENANCE,
        CRITICAL
    }

    public enum Priority {
        LOW,
        NORMAL,
        HIGH,
        URGENT
    }

    public enum AnnouncementStatus {
        ACTIVE,
        SCHEDULED,
        ARCHIVED
    }
}
