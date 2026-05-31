package com.gogidix.courier.availabilityservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Domain Entity representing a Driver's Availability.
 * Manages driver availability slots and unavailable periods.
 */
@Document(collection = "driver_availability")
@CompoundIndex(name = "idx_driver_tenant", def = "{'tenantId': 1, 'driverId': 1, 'date': 1}")
public class DriverAvailability {

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

    @Field("status")
    private AvailabilityStatus status;

    @Field("slots")
    private List<AvailabilitySlot> slots;

    @Field("unavailable_periods")
    private List<UnavailablePeriod> unavailablePeriods;

    @Field("current_location")
    private LocationInfo currentLocation;

    @Field("preferred_zones")
    private List<String> preferredZones;

    @Field("max_capacity")
    private Integer maxCapacity;

    @Field("current_load")
    private Integer currentLoad;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    @Field("version")
    private Long version;

    /**
     * Default constructor for persistence.
     */
    protected DriverAvailability() {
    }

    /**
     * Create a new DriverAvailability.
     *
     * @param tenantId the tenant identifier
     * @param driverId the driver identifier
     * @param date     the date for availability
     */
    public DriverAvailability(String tenantId, String driverId, LocalDate date) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.driverId = Objects.requireNonNull(driverId, "driverId is required");
        this.date = Objects.requireNonNull(date, "date is required");
        this.status = AvailabilityStatus.UNKNOWN;
        this.slots = new ArrayList<>();
        this.unavailablePeriods = new ArrayList<>();
        this.preferredZones = new ArrayList<>();
        this.maxCapacity = 10;
        this.currentLoad = 0;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.version = 0L;
    }

    // Domain Logic Methods

    /**
     * Mark the driver as available for the entire day.
     */
    public void markAvailable() {
        this.status = AvailabilityStatus.AVAILABLE;
        this.updatedAt = Instant.now();
        this.version++;
    }

    /**
     * Mark the driver as unavailable for the entire day.
     *
     * @param reason the reason for unavailability
     */
    public void markUnavailable(String reason) {
        this.status = AvailabilityStatus.UNAVAILABLE;
        this.unavailablePeriods.add(new UnavailablePeriod(
                date.atStartOfDay(),
                date.atTime(23, 59, 59),
                reason
        ));
        this.updatedAt = Instant.now();
        this.version++;
    }

    /**
     * Add an availability slot.
     *
     * @param startTime the start time
     * @param endTime   the end time
     */
    public void addAvailabilitySlot(java.time.LocalTime startTime, java.time.LocalTime endTime) {
        validateSlotTime(startTime, endTime);

        AvailabilitySlot slot = new AvailabilitySlot(
                date.atTime(startTime),
                date.atTime(endTime),
                SlotStatus.AVAILABLE
        );

        this.slots.add(slot);
        this.status = AvailabilityStatus.AVAILABLE;
        this.updatedAt = Instant.now();
        this.version++;
    }

    /**
     * Add an unavailable period.
     *
     * @param startTime the start time
     * @param endTime   the end time
     * @param reason    the reason
     */
    public void addUnavailablePeriod(java.time.LocalTime startTime, java.time.LocalTime endTime, String reason) {
        validateSlotTime(startTime, endTime);

        UnavailablePeriod period = new UnavailablePeriod(
                date.atTime(startTime),
                date.atTime(endTime),
                reason
        );

        this.unavailablePeriods.add(period);
        this.updatedAt = Instant.now();
        this.version++;
    }

    /**
     * Update current location.
     *
     * @param latitude  the latitude
     * @param longitude the longitude
     */
    public void updateLocation(Double latitude, Double longitude) {
        this.currentLocation = new LocationInfo(latitude, longitude, Instant.now());
        this.updatedAt = Instant.now();
    }

    /**
     * Set preferred zones.
     *
     * @param zones the list of zone IDs
     */
    public void setPreferredZones(List<String> zones) {
        this.preferredZones = Objects.requireNonNullElse(zones, new ArrayList<>());
        this.updatedAt = Instant.now();
    }

    /**
     * Increment current load.
     */
    public void incrementLoad() {
        if (this.currentLoad < this.maxCapacity) {
            this.currentLoad++;
            this.updatedAt = Instant.now();
        }
    }

    /**
     * Decrement current load.
     */
    public void decrementLoad() {
        if (this.currentLoad > 0) {
            this.currentLoad--;
            this.updatedAt = Instant.now();
        }
    }

    /**
     * Check if driver is at maximum capacity.
     *
     * @return true if at capacity
     */
    public boolean isAtCapacity() {
        return this.currentLoad >= this.maxCapacity;
    }

    /**
     * Check if driver is available at a specific time.
     *
     * @param time the time to check
     * @return true if available
     */
    public boolean isAvailableAt(java.time.LocalDateTime time) {
        if (this.status == AvailabilityStatus.UNAVAILABLE) {
            return false;
        }

        // Check unavailable periods
        for (UnavailablePeriod period : unavailablePeriods) {
            if (!time.isBefore(period.getStartTime()) && !time.isAfter(period.getEndTime())) {
                return false;
            }
        }

        // Check if within available slots
        if (slots.isEmpty()) {
            return this.status == AvailabilityStatus.AVAILABLE;
        }

        for (AvailabilitySlot slot : slots) {
            if (!time.isBefore(slot.getStartTime()) && !time.isAfter(slot.getEndTime())) {
                return slot.getStatus() == SlotStatus.AVAILABLE;
            }
        }

        return false;
    }

    /**
     * Validate slot times.
     */
    private void validateSlotTime(java.time.LocalTime start, java.time.LocalTime end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end times are required");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
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

    public AvailabilityStatus getStatus() {
        return status;
    }

    public List<AvailabilitySlot> getSlots() {
        return slots;
    }

    public List<UnavailablePeriod> getUnavailablePeriods() {
        return unavailablePeriods;
    }

    public LocationInfo getCurrentLocation() {
        return currentLocation;
    }

    public List<String> getPreferredZones() {
        return preferredZones;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public Integer getCurrentLoad() {
        return currentLoad;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Long getVersion() {
        return version;
    }

    // Setters for persistence
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

    protected void setStatus(AvailabilityStatus status) {
        this.status = status;
    }

    protected void setSlots(List<AvailabilitySlot> slots) {
        this.slots = slots;
    }

    protected void setUnavailablePeriods(List<UnavailablePeriod> unavailablePeriods) {
        this.unavailablePeriods = unavailablePeriods;
    }

    protected void setCurrentLocation(LocationInfo currentLocation) {
        this.currentLocation = currentLocation;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    protected void setCurrentLoad(Integer currentLoad) {
        this.currentLoad = currentLoad;
    }

    protected void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    protected void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    protected void setVersion(Long version) {
        this.version = version;
    }

    /**
     * Availability status enum.
     */
    public enum AvailabilityStatus {
        UNKNOWN,
        AVAILABLE,
        UNAVAILABLE,
        BUSY,
        OFF_DUTY
    }

    /**
     * Slot status enum.
     */
    public enum SlotStatus {
        AVAILABLE,
        BOOKED,
        UNAVAILABLE
    }

    /**
     * Inner class for availability slots.
     */
    public static class AvailabilitySlot {
        @Field("start_time")
        private java.time.LocalDateTime startTime;

        @Field("end_time")
        private java.time.LocalDateTime endTime;

        @Field("status")
        private SlotStatus status;

        public AvailabilitySlot() {
        }

        public AvailabilitySlot(java.time.LocalDateTime startTime, java.time.LocalDateTime endTime, SlotStatus status) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.status = status;
        }

        public java.time.LocalDateTime getStartTime() {
            return startTime;
        }

        public void setStartTime(java.time.LocalDateTime startTime) {
            this.startTime = startTime;
        }

        public java.time.LocalDateTime getEndTime() {
            return endTime;
        }

        public void setEndTime(java.time.LocalDateTime endTime) {
            this.endTime = endTime;
        }

        public SlotStatus getStatus() {
            return status;
        }

        public void setStatus(SlotStatus status) {
            this.status = status;
        }
    }

    /**
     * Inner class for unavailable periods.
     */
    public static class UnavailablePeriod {
        @Field("start_time")
        private java.time.LocalDateTime startTime;

        @Field("end_time")
        private java.time.LocalDateTime endTime;

        @Field("reason")
        private String reason;

        public UnavailablePeriod() {
        }

        public UnavailablePeriod(java.time.LocalDateTime startTime, java.time.LocalDateTime endTime, String reason) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.reason = reason;
        }

        public java.time.LocalDateTime getStartTime() {
            return startTime;
        }

        public void setStartTime(java.time.LocalDateTime startTime) {
            this.startTime = startTime;
        }

        public java.time.LocalDateTime getEndTime() {
            return endTime;
        }

        public void setEndTime(java.time.LocalDateTime endTime) {
            this.endTime = endTime;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }

    /**
     * Inner class for location info.
     */
    public static class LocationInfo {
        @Field("latitude")
        private Double latitude;

        @Field("longitude")
        private Double longitude;

        @Field("updated_at")
        private Instant updatedAt;

        public LocationInfo() {
        }

        public LocationInfo(Double latitude, Double longitude, Instant updatedAt) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.updatedAt = updatedAt;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public Instant getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}
