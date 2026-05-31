package com.gogidix.monitoring.servicehealthservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Domain model for service uptime tracking.
 * Records uptime/downtime events for SLA tracking.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceUptime {

    /**
     * Unique identifier.
     */
    private String id;

    /**
     * Tenant identifier.
     */
    private String tenantId;

    /**
     * Service name.
     */
    private String serviceName;

    /**
     * Incident type (UP or DOWN).
     */
    private IncidentType incidentType;

    /**
     * When this incident started.
     */
    private Instant startedAt;

    /**
     * When this incident ended (null for ongoing incidents).
     */
    private Instant endedAt;

    /**
     * Duration of the incident in seconds (calculated after it ends).
     */
    private Long durationSeconds;

    /**
     * Reason for the incident.
     */
    private String reason;

    /**
     * Additional details.
     */
    private String details;

    /**
     * Whether this was a scheduled maintenance.
     */
    private Boolean scheduledMaintenance;

    /**
     * Service instance (if applicable).
     */
    private String instanceId;

    /**
     * Timestamp when created.
     */
    private Instant createdAt;

    /**
     * Incident type enumeration.
     */
    public enum IncidentType {
        UP,
        DOWN,
        DEGRADED
    }

    /**
     * Calculates the duration if endedAt is set.
     */
    public long calculateDuration() {
        if (endedAt == null) {
            return Instant.now().getEpochSecond() - startedAt.getEpochSecond();
        }
        return endedAt.getEpochSecond() - startedAt.getEpochSecond();
    }

    /**
     * Checks if the incident is ongoing.
     */
    public boolean isOngoing() {
        return endedAt == null;
    }

    /**
     * Ends the incident.
     */
    public void endIncident() {
        this.endedAt = Instant.now();
        this.durationSeconds = calculateDuration();
    }
}
