package com.gogidix.courier.availabilityservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Domain Entity representing a specific Availability Slot.
 * Represents a time slot when a driver is available for assignments.
 */
@Document(collection = "availability_slots")
@CompoundIndex(name = "idx_slot_driver_date", def = "{'tenantId': 1, 'driverId': 1, 'date': 1, 'startTime': 1}")
public class AvailabilitySlot {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("driver_id")
    private String driverId;

    @Indexed
    @Field("date")
    private LocalDate date;

    @Field("start_time")
    private LocalTime startTime;

    @Field("end_time")
    private LocalTime endTime;

    @Field("status")
    private SlotStatus status;

    @Field("booking_id")
    private String bookingId;

    @Field("zone_id")
    private String zoneId;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    /**
     * Default constructor for persistence.
     */
    protected AvailabilitySlot() {
    }

    /**
     * Create a new AvailabilitySlot.
     *
     * @param tenantId the tenant identifier
     * @param driverId the driver identifier
     * @param date     the date
     * @param startTime the start time
     * @param endTime   the end time
     */
    public AvailabilitySlot(String tenantId, String driverId, LocalDate date,
                           LocalTime startTime, LocalTime endTime) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.driverId = Objects.requireNonNull(driverId, "driverId is required");
        this.date = Objects.requireNonNull(date, "date is required");
        this.startTime = Objects.requireNonNull(startTime, "startTime is required");
        this.endTime = Objects.requireNonNull(endTime, "endTime is required");
        this.status = SlotStatus.AVAILABLE;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();

        validate();
    }

    // Domain Logic Methods

    /**
     * Book this slot.
     *
     * @param bookingId the booking ID
     */
    public void book(String bookingId) {
        if (this.status != SlotStatus.AVAILABLE) {
            throw new IllegalStateException("Slot is not available for booking");
        }
        this.status = SlotStatus.BOOKED;
        this.bookingId = Objects.requireNonNull(bookingId, "bookingId is required");
        this.updatedAt = Instant.now();
    }

    /**
     * Release this slot.
     */
    public void release() {
        if (this.status != SlotStatus.BOOKED) {
            throw new IllegalStateException("Slot is not booked");
        }
        this.status = SlotStatus.AVAILABLE;
        this.bookingId = null;
        this.updatedAt = Instant.now();
    }

    /**
     * Mark slot as unavailable.
     */
    public void markUnavailable() {
        this.status = SlotStatus.UNAVAILABLE;
        this.updatedAt = Instant.now();
    }

    /**
     * Check if slot overlaps with another time range.
     *
     * @param otherStart other start time
     * @param otherEnd   other end time
     * @return true if overlaps
     */
    public boolean overlapsWith(LocalTime otherStart, LocalTime otherEnd) {
        return !(this.endTime.isBefore(otherStart) || this.startTime.isAfter(otherEnd));
    }

    /**
     * Validate the slot.
     */
    public void validate() {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start and end times are required");
        }
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        if (startTime.equals(endTime)) {
            throw new IllegalArgumentException("Start and end times cannot be equal");
        }
    }

    /**
     * Check if slot is available.
     *
     * @return true if available
     */
    public boolean isAvailable() {
        return this.status == SlotStatus.AVAILABLE;
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

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public SlotStatus getStatus() {
        return status;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getZoneId() {
        return zoneId;
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

    protected void setDate(LocalDate date) {
        this.date = date;
    }

    protected void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    protected void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    protected void setStatus(SlotStatus status) {
        this.status = status;
    }

    protected void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    protected void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    protected void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    protected void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Slot status enum.
     */
    public enum SlotStatus {
        AVAILABLE,
        BOOKED,
        UNAVAILABLE,
        CANCELLED
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvailabilitySlot that = (AvailabilitySlot) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AvailabilitySlot{" +
                "id='" + id + '\'' +
                ", driverId='" + driverId + '\'' +
                ", date=" + date +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status=" + status +
                '}';
    }
}
