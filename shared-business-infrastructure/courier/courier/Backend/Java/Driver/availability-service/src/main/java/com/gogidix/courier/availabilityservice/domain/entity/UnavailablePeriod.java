package com.gogidix.courier.availabilityservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Domain Entity representing an Unavailable Period for a driver.
 * Records when a driver is not available for assignments.
 */
@Document(collection = "unavailable_periods")
@CompoundIndex(name = "idx_unavailable_driver_time", def = "{'tenantId': 1, 'driverId': 1, 'startTime': 1}")
public class UnavailablePeriod {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("driver_id")
    private String driverId;

    @Field("start_time")
    private LocalDateTime startTime;

    @Field("end_time")
    private LocalDateTime endTime;

    @Field("reason")
    private String reason;

    @Field("reason_type")
    private UnavailabilityReasonType reasonType;

    @Field("is_recurring")
    private Boolean isRecurring;

    @Field("recurrence_pattern")
    private String recurrencePattern;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    /**
     * Default constructor for persistence.
     */
    protected UnavailablePeriod() {
    }

    /**
     * Create a new UnavailablePeriod.
     *
     * @param tenantId the tenant identifier
     * @param driverId the driver identifier
     * @param startTime the start time
     * @param endTime   the end time
     * @param reason    the reason
     */
    public UnavailablePeriod(String tenantId, String driverId, LocalDateTime startTime,
                            LocalDateTime endTime, String reason) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.driverId = Objects.requireNonNull(driverId, "driverId is required");
        this.startTime = Objects.requireNonNull(startTime, "startTime is required");
        this.endTime = Objects.requireNonNull(endTime, "endTime is required");
        this.reason = reason;
        this.reasonType = UnavailabilityReasonType.OTHER;
        this.isRecurring = false;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();

        validate();
    }

    /**
     * Create a new UnavailablePeriod with reason type.
     *
     * @param tenantId   the tenant identifier
     * @param driverId   the driver identifier
     * @param startTime  the start time
     * @param endTime    the end time
     * @param reason     the reason
     * @param reasonType the reason type
     */
    public UnavailablePeriod(String tenantId, String driverId, LocalDateTime startTime,
                            LocalDateTime endTime, String reason, UnavailabilityReasonType reasonType) {
        this(tenantId, driverId, startTime, endTime, reason);
        this.reasonType = Objects.requireNonNull(reasonType, "reasonType is required");
    }

    // Domain Logic Methods

    /**
     * Validate the unavailability period.
     */
    public void validate() {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start and end times are required");
        }
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        if (startTime.isEqual(endTime)) {
            throw new IllegalArgumentException("Start and end times cannot be equal");
        }
    }

    /**
     * Check if this period overlaps with another time range.
     *
     * @param otherStart other start time
     * @param otherEnd   other end time
     * @return true if overlaps
     */
    public boolean overlapsWith(LocalDateTime otherStart, LocalDateTime otherEnd) {
        return !(this.endTime.isBefore(otherStart) || this.startTime.isAfter(otherEnd));
    }

    /**
     * Check if this period contains a specific time.
     *
     * @param time the time to check
     * @return true if contains
     */
    public boolean contains(LocalDateTime time) {
        return !time.isBefore(startTime) && !time.isAfter(endTime);
    }

    /**
     * Extend the end time.
     *
     * @param newEndTime the new end time
     */
    public void extendEndTime(LocalDateTime newEndTime) {
        if (newEndTime.isBefore(this.endTime)) {
            throw new IllegalArgumentException("New end time must be after current end time");
        }
        this.endTime = newEndTime;
        this.updatedAt = Instant.now();
    }

    /**
     * Set recurrence pattern.
     *
     * @param pattern the recurrence pattern (cron-like)
     */
    public void setRecurring(String pattern) {
        this.isRecurring = true;
        this.recurrencePattern = pattern;
        this.updatedAt = Instant.now();
    }

    /**
     * Cancel recurrence.
     */
    public void cancelRecurrence() {
        this.isRecurring = false;
        this.recurrencePattern = null;
        this.updatedAt = Instant.now();
    }

    /**
     * Check if period is currently active.
     *
     * @return true if active now
     */
    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return contains(now);
    }

    /**
     * Get duration in minutes.
     *
     * @return duration in minutes
     */
    public long getDurationMinutes() {
        return java.time.Duration.between(startTime, endTime).toMinutes();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getDriverId() {
        return driverId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public String getReason() {
        return reason;
    }

    public UnavailabilityReasonType getReasonType() {
        return reasonType;
    }

    public Boolean getIsRecurring() {
        return isRecurring;
    }

    public String getRecurrencePattern() {
        return recurrencePattern;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    protected void setId(String id) {
        this.id = id;
    }

    protected void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    protected void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    protected void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    protected void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    protected void setReason(String reason) {
        this.reason = reason;
    }

    protected void setReasonType(UnavailabilityReasonType reasonType) {
        this.reasonType = reasonType;
    }

    protected void setIsRecurring(Boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    protected void setRecurrencePattern(String recurrencePattern) {
        this.recurrencePattern = recurrencePattern;
    }

    protected void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    protected void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Unavailability reason type enum.
     */
    public enum UnavailabilityReasonType {
        VACATION,
        SICK_LEAVE,
        PERSONAL,
        TRAINING,
        MAINTENANCE,
        BREAK,
        OFF_DUTY,
        OTHER
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnavailablePeriod that = (UnavailablePeriod) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UnavailablePeriod{" +
                "id='" + id + '\'' +
                ", driverId='" + driverId + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", reason='" + reason + '\'' +
                ", reasonType=" + reasonType +
                '}';
    }
}
