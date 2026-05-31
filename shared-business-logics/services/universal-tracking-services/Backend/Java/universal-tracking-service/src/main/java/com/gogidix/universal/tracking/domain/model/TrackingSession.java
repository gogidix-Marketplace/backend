package com.gogidix.universal.tracking.domain.model;

import com.gogidix.shared.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Tracking Session entity representing a user session.
 * Groups related events together for session-based analytics.
 *
 * Multi-tenancy: Inherits tenantId from BaseEntity for tenant isolation.
 */
@Entity
@Table(name = "tracking_sessions", indexes = {
    @Index(name = "idx_session_tenant_id", columnList = "tenant_id"),
    @Index(name = "idx_session_user_id", columnList = "user_id"),
    @Index(name = "idx_session_started_at", columnList = "started_at"),
    @Index(name = "idx_session_source", columnList = "source")
})
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackingSession extends BaseEntity {

    /**
     * Unique identifier for this session
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Session ID (external identifier)
     */
    @Column(name = "session_id", nullable = false, unique = true, length = 100)
    private String sessionId;

    /**
     * User ID associated with this session
     */
    @Column(name = "user_id", length = 100)
    private String userId;

    /**
     * Tenant ID for multi-tenancy isolation
     */
    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;

    /**
     * Source of the session (e.g., "WEB", "MOBILE", "API")
     */
    @Column(name = "source", length = 50)
    @Builder.Default
    private String source = "WEB";

    /**
     * IP address of the user
     */
    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    /**
     * User agent string
     */
    @Column(name = "user_agent", length = 500)
    private String userAgent;

    /**
     * Device type (e.g., "DESKTOP", "MOBILE", "TABLET")
     */
    @Column(name = "device_type", length = 50)
    private String deviceType;

    /**
     * Browser name
     */
    @Column(name = "browser", length = 100)
    private String browser;

    /**
     * Operating system
     */
    @Column(name = "os", length = 100)
    private String os;

    /**
     * Country code
     */
    @Column(name = "country", length = 10)
    private String country;

    /**
     * City
     */
    @Column(name = "city", length = 100)
    private String city;

    /**
     * Referrer URL
     */
    @Column(name = "referrer", length = 500)
    private String referrer;

    /**
     * Landing page URL
     */
    @Column(name = "landing_page", length = 1000)
    private String landingPage;

    /**
     * Campaign identifier
     */
    @Column(name = "campaign", length = 100)
    private String campaign;

    /**
     * Session start timestamp
     */
    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    /**
     * Last activity timestamp
     */
    @Column(name = "last_activity_at", nullable = false)
    private LocalDateTime lastActivityAt;

    /**
     * Session end timestamp (null if active)
     */
    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    /**
     * Session duration in seconds
     */
    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    /**
     * Total number of events in this session
     */
    @Column(name = "event_count", nullable = false)
    @Builder.Default
    private Integer eventCount = 0;

    /**
     * Total number of page views in this session
     */
    @Column(name = "page_view_count", nullable = false)
    @Builder.Default
    private Integer pageViewCount = 0;

    /**
     * Whether the session is active
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    /**
     * Session metadata as JSON
     */
    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata;

    /**
     * Check if session is active
     */
    public boolean isActiveSession() {
        return isActive != null && isActive && endedAt == null;
    }

    /**
     * Check if session has timed out
     */
    public boolean isTimedOut(int timeoutMinutes) {
        if (!isActiveSession()) {
            return false;
        }
        LocalDateTime timeoutThreshold = LocalDateTime.now().minusMinutes(timeoutMinutes);
        return lastActivityAt.isBefore(timeoutThreshold);
    }

    /**
     * End the session
     */
    public void endSession() {
        this.isActive = false;
        this.endedAt = LocalDateTime.now();
        if (startedAt != null) {
            this.durationSeconds = (int) java.time.Duration.between(startedAt, endedAt).getSeconds();
        }
    }

    /**
     * Update last activity timestamp
     */
    public void updateActivity() {
        this.lastActivityAt = LocalDateTime.now();
    }

    /**
     * Increment event count
     */
    public void incrementEventCount() {
        this.eventCount++;
    }

    /**
     * Increment page view count
     */
    public void incrementPageViewCount() {
        this.pageViewCount++;
    }
}
