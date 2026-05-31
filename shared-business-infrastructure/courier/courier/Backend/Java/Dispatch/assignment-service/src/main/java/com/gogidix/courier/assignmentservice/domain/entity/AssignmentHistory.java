package com.gogidix.courier.assignmentservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain Entity representing the history of driver assignments.
 * Tracks all assignment lifecycle events for analytics and auditing.
 */
@Document(collection = "assignment_history")
@CompoundIndex(name = "idx_assignment_tenant", def = "{'assignmentId': 1, 'tenantId': 1}")
@CompoundIndex(name = "idx_driver_timestamp", def = "{'driverId': 1, 'timestamp': -1}")
@CompoundIndex(name = "idx_dispatch_timestamp", def = "{'dispatchId': 1, 'timestamp': -1}")
public class AssignmentHistory {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("assignment_id")
    private String assignmentId;

    @Indexed
    @Field("driver_id")
    private String driverId;

    @Indexed
    @Field("dispatch_id")
    private String dispatchId;

    @Field("event_type")
    private HistoryEventType eventType;

    @Field("previous_status")
    private DriverAssignment.AssignmentStatus previousStatus;

    @Field("new_status")
    private DriverAssignment.AssignmentStatus newStatus;

    @Field("timestamp")
    private Instant timestamp;

    @Field("triggered_by")
    private String triggeredBy;

    @Field("triggered_by_type")
    private TriggeredByType triggeredByType;

    @Field("reason")
    private String reason;

    @Field("metadata")
    private HistoryMetadata metadata;

    /**
     * Default constructor for persistence.
     */
    protected AssignmentHistory() {
    }

    /**
     * Create a new assignment history record.
     *
     * @param tenantId     the tenant identifier
     * @param assignmentId the assignment identifier
     * @param driverId     the driver identifier
     * @param dispatchId   the dispatch identifier
     * @param eventType    the type of event
     */
    public AssignmentHistory(
            String tenantId,
            String assignmentId,
            String driverId,
            String dispatchId,
            HistoryEventType eventType) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.assignmentId = Objects.requireNonNull(assignmentId, "assignmentId is required");
        this.driverId = Objects.requireNonNull(driverId, "driverId is required");
        this.dispatchId = Objects.requireNonNull(dispatchId, "dispatchId is required");
        this.eventType = Objects.requireNonNull(eventType, "eventType is required");
        this.timestamp = Instant.now();
        this.triggeredByType = TriggeredByType.SYSTEM;
    }

    /**
     * Create history entry for status change.
     */
    public static AssignmentHistory forStatusChange(
            String tenantId,
            String assignmentId,
            String driverId,
            String dispatchId,
            DriverAssignment.AssignmentStatus previousStatus,
            DriverAssignment.AssignmentStatus newStatus,
            String triggeredBy) {
        AssignmentHistory history = new AssignmentHistory(
                tenantId, assignmentId, driverId, dispatchId, HistoryEventType.STATUS_CHANGED);
        history.previousStatus = previousStatus;
        history.newStatus = newStatus;
        history.triggeredBy = triggeredBy;
        history.triggeredByType = TriggeredByType.USER;
        return history;
    }

    /**
     * Create history entry for assignment created.
     */
    public static AssignmentHistory forAssignmentCreated(
            String tenantId,
            String assignmentId,
            String driverId,
            String dispatchId,
            String assignedBy) {
        AssignmentHistory history = new AssignmentHistory(
                tenantId, assignmentId, driverId, dispatchId, HistoryEventType.ASSIGNMENT_CREATED);
        history.newStatus = DriverAssignment.AssignmentStatus.PENDING;
        history.triggeredBy = assignedBy;
        history.triggeredByType = TriggeredByType.SYSTEM;
        return history;
    }

    /**
     * Create history entry for driver reassignment.
     */
    public static AssignmentHistory forReassignment(
            String tenantId,
            String assignmentId,
            String previousDriverId,
            String newDriverId,
            String dispatchId,
            String reason) {
        AssignmentHistory history = new AssignmentHistory(
                tenantId, assignmentId, newDriverId, dispatchId, HistoryEventType.DRIVER_REASSIGNED);
        history.triggeredBy = "SYSTEM";
        history.triggeredByType = TriggeredByType.SYSTEM;
        history.reason = reason;
        if (history.metadata == null) {
            history.metadata = new HistoryMetadata();
        }
        history.metadata.previousDriverId = previousDriverId;
        return history;
    }

    /**
     * Create history entry for cancellation.
     */
    public static AssignmentHistory forCancellation(
            String tenantId,
            String assignmentId,
            String driverId,
            String dispatchId,
            String reason,
            String triggeredBy) {
        AssignmentHistory history = new AssignmentHistory(
                tenantId, assignmentId, driverId, dispatchId, HistoryEventType.ASSIGNMENT_CANCELLED);
        history.triggeredBy = triggeredBy;
        history.triggeredByType = TriggeredByType.USER;
        history.reason = reason;
        return history;
    }

    /**
     * Create history entry for completion.
     */
    public static AssignmentHistory forCompletion(
            String tenantId,
            String assignmentId,
            String driverId,
            String dispatchId,
            Double actualDistanceKm,
            Integer actualDurationMinutes) {
        AssignmentHistory history = new AssignmentHistory(
                tenantId, assignmentId, driverId, dispatchId, HistoryEventType.ASSIGNMENT_COMPLETED);
        history.newStatus = DriverAssignment.AssignmentStatus.COMPLETED;
        if (history.metadata == null) {
            history.metadata = new HistoryMetadata();
        }
        history.metadata.actualDistanceKm = actualDistanceKm;
        history.metadata.actualDurationMinutes = actualDurationMinutes;
        return history;
    }

    /**
     * Create history entry for failed assignment.
     */
    public static AssignmentHistory forFailure(
            String tenantId,
            String assignmentId,
            String driverId,
            String dispatchId,
            String reason) {
        AssignmentHistory history = new AssignmentHistory(
                tenantId, assignmentId, driverId, dispatchId, HistoryEventType.ASSIGNMENT_FAILED);
        history.newStatus = DriverAssignment.AssignmentStatus.FAILED;
        history.reason = reason;
        return history;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getDispatchId() {
        return dispatchId;
    }

    public HistoryEventType getEventType() {
        return eventType;
    }

    public DriverAssignment.AssignmentStatus getPreviousStatus() {
        return previousStatus;
    }

    public DriverAssignment.AssignmentStatus getNewStatus() {
        return newStatus;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getTriggeredBy() {
        return triggeredBy;
    }

    public TriggeredByType getTriggeredByType() {
        return triggeredByType;
    }

    public String getReason() {
        return reason;
    }

    public HistoryMetadata getMetadata() {
        return metadata;
    }

    // Setters for persistence
    protected void setId(String id) {
        this.id = id;
    }

    protected void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    protected void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    protected void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    protected void setDispatchId(String dispatchId) {
        this.dispatchId = dispatchId;
    }

    protected void setEventType(HistoryEventType eventType) {
        this.eventType = eventType;
    }

    protected void setPreviousStatus(DriverAssignment.AssignmentStatus previousStatus) {
        this.previousStatus = previousStatus;
    }

    protected void setNewStatus(DriverAssignment.AssignmentStatus newStatus) {
        this.newStatus = newStatus;
    }

    protected void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    protected void setTriggeredBy(String triggeredBy) {
        this.triggeredBy = triggeredBy;
    }

    protected void setTriggeredByType(TriggeredByType triggeredByType) {
        this.triggeredByType = triggeredByType;
    }

    protected void setReason(String reason) {
        this.reason = reason;
    }

    protected void setMetadata(HistoryMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssignmentHistory that = (AssignmentHistory) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AssignmentHistory{" +
                "id='" + id + '\'' +
                ", assignmentId='" + assignmentId + '\'' +
                ", driverId='" + driverId + '\'' +
                ", eventType=" + eventType +
                ", timestamp=" + timestamp +
                '}';
    }

    /**
     * History event type enum.
     */
    public enum HistoryEventType {
        ASSIGNMENT_CREATED,
        STATUS_CHANGED,
        DRIVER_REASSIGNED,
        ASSIGNMENT_ACCEPTED,
        ASSIGNMENT_STARTED,
        ASSIGNMENT_COMPLETED,
        ASSIGNMENT_CANCELLED,
        ASSIGNMENT_FAILED,
        ASSIGNMENT_EXPIRED,
        ASSIGNMENT_TIMEOUT
    }

    /**
     * Triggered by type enum.
     */
    public enum TriggeredByType {
        SYSTEM,
        USER,
        DRIVER,
        AUTOMATED,
        WEBHOOK
    }

    /**
     * History metadata value object.
     */
    public static class HistoryMetadata {
        private String previousDriverId;
        private Double actualDistanceKm;
        private Integer actualDurationMinutes;
        private String ipAddress;
        private String userAgent;
        private String additionalInfo;

        public String getPreviousDriverId() {
            return previousDriverId;
        }

        public void setPreviousDriverId(String previousDriverId) {
            this.previousDriverId = previousDriverId;
        }

        public Double getActualDistanceKm() {
            return actualDistanceKm;
        }

        public void setActualDistanceKm(Double actualDistanceKm) {
            this.actualDistanceKm = actualDistanceKm;
        }

        public Integer getActualDurationMinutes() {
            return actualDurationMinutes;
        }

        public void setActualDurationMinutes(Integer actualDurationMinutes) {
            this.actualDurationMinutes = actualDurationMinutes;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        public String getUserAgent() {
            return userAgent;
        }

        public void setUserAgent(String userAgent) {
            this.userAgent = userAgent;
        }

        public String getAdditionalInfo() {
            return additionalInfo;
        }

        public void setAdditionalInfo(String additionalInfo) {
            this.additionalInfo = additionalInfo;
        }
    }
}
