package com.gogidix.universal.tracking.domain.model;

import com.gogidix.shared.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Tracking Event entity representing a single tracked event.
 * Stores all event data with tenant isolation for multi-tenancy.
 *
 * Multi-tenancy: Inherits tenantId from BaseEntity for tenant isolation.
 */
@Entity
@Table(name = "tracking_events", indexes = {
    @Index(name = "idx_event_tenant_id", columnList = "tenant_id"),
    @Index(name = "idx_event_session_id", columnList = "session_id"),
    @Index(name = "idx_event_type", columnList = "event_type"),
    @Index(name = "idx_event_timestamp", columnList = "timestamp"),
    @Index(name = "idx_event_source", columnList = "source")
})
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrackingEvent extends BaseEntity {

    /**
     * Unique identifier for this event
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    /**
     * Event type/category (e.g., "PAGE_VIEW", "CLICK", "PURCHASE")
     */
    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    /**
     * Session ID this event belongs to
     */
    @Column(name = "session_id", length = 100)
    private String sessionId;

    /**
     * User ID who triggered this event
     */
    @Column(name = "user_id", length = 100)
    private String userId;

    /**
     * Source of the event (e.g., "WEB", "MOBILE", "API")
     */
    @Column(name = "source", length = 50)
    @Builder.Default
    private String source = "WEB";

    /**
     * Timestamp when the event occurred
     */
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    /**
     * Event name/title
     */
    @Column(name = "event_name", length = 255)
    private String eventName;

    /**
     * Event description
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Event properties as JSON
     */
    @Column(name = "properties", columnDefinition = "TEXT")
    private String properties;

    /**
     * Additional metadata as JSON
     */
    @Column(name = "metadata", columnDefinition = "TEXT")
    private String metadata;

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
     * Referrer URL
     */
    @Column(name = "referrer", length = 500)
    private String referrer;

    /**
     * Page URL where event occurred
     */
    @Column(name = "page_url", length = 1000)
    private String pageUrl;

    /**
     * Page title
     */
    @Column(name = "page_title", length = 255)
    private String pageTitle;

    /**
     * Tenant ID for multi-tenancy isolation
     */
    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;

    /**
     * Correlation ID for distributed tracing
     */
    @Column(name = "correlation_id", length = 100)
    private String correlationId;

    /**
     * Priority of the event (1-10, higher = more important)
     */
    @Column(name = "priority", nullable = false)
    @Builder.Default
    private Integer priority = 5;

    /**
     * Whether this event has been processed
     */
    @Column(name = "processed", nullable = false)
    @Builder.Default
    private Boolean processed = false;

    /**
     * Timestamp when the event was processed
     */
    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    /**
     * Error message if processing failed
     */
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    /**
     * Check if event is processed
     */
    public boolean isProcessed() {
        return processed != null && processed;
    }

    /**
     * Mark event as processed
     */
    public void markAsProcessed() {
        this.processed = true;
        this.processedAt = LocalDateTime.now();
    }

    /**
     * Mark event as failed with error message
     */
    public void markAsFailed(String errorMessage) {
        this.processed = false;
        this.errorMessage = errorMessage;
    }
}
