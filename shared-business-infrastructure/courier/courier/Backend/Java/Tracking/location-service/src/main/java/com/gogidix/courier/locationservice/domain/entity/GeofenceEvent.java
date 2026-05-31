package com.gogidix.courier.locationservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Domain Entity representing a Geofence Event.
 * Tracks geofence entry and exit events.
 */
@Document(collection = "geofence_events")
@CompoundIndex(name = "idx_geofence_driver_time", def = "{'tenantId': 1, 'driverId': 1, 'timestamp': -1}")
public class GeofenceEvent {

    @Id
    private String id;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Indexed
    @Field("driver_id")
    private String driverId;

    @Indexed
    @Field("geofence_id")
    private String geofenceId;

    @Field("geofence_name")
    private String geofenceName;

    @Field("event_type")
    private GeofenceEventType eventType;

    @Field("timestamp")
    private Instant timestamp;

    @Field("location")
    private LocationEvent.GeoLocation location;

    @Field("attributes")
    private Map<String, Object> attributes;

    @Field("created_at")
    private Instant createdAt;

    /**
     * Default constructor for persistence.
     */
    protected GeofenceEvent() {
    }

    /**
     * Create a new GeofenceEvent.
     *
     * @param tenantId   the tenant identifier
     * @param driverId   the driver identifier
     * @param geofenceId the geofence identifier
     * @param eventType  the event type
     * @param location   the location where event occurred
     */
    public GeofenceEvent(String tenantId, String driverId, String geofenceId,
                        GeofenceEventType eventType, LocationEvent.GeoLocation location) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.driverId = Objects.requireNonNull(driverId, "driverId is required");
        this.geofenceId = Objects.requireNonNull(geofenceId, "geofenceId is required");
        this.eventType = Objects.requireNonNull(eventType, "eventType is required");
        this.location = Objects.requireNonNull(location, "location is required");
        this.timestamp = Instant.now();
        this.createdAt = Instant.now();
    }

    /**
     * Create an entry event.
     */
    public static GeofenceEvent createEntry(String tenantId, String driverId,
                                            String geofenceId, String geofenceName,
                                            LocationEvent.GeoLocation location) {
        GeofenceEvent event = new GeofenceEvent(tenantId, driverId, geofenceId,
                GeofenceEventType.ENTERED, location);
        event.geofenceName = geofenceName;
        return event;
    }

    /**
     * Create an exit event.
     */
    public static GeofenceEvent createExit(String tenantId, String driverId,
                                          String geofenceId, String geofenceName,
                                          LocationEvent.GeoLocation location) {
        GeofenceEvent event = new GeofenceEvent(tenantId, driverId, geofenceId,
                GeofenceEventType.EXITED, location);
        event.geofenceName = geofenceName;
        return event;
    }

    /**
     * Create a dwell event (stayed inside too long).
     */
    public static GeofenceEvent createDwell(String tenantId, String driverId,
                                           String geofenceId, String geofenceName,
                                           LocationEvent.GeoLocation location,
                                           Long dwellTimeMs) {
        GeofenceEvent event = new GeofenceEvent(tenantId, driverId, geofenceId,
                GeofenceEventType.DWELL, location);
        event.geofenceName = geofenceName;
        event.setAttribute("dwellTimeMs", dwellTimeMs);
        return event;
    }

    /**
     * Set an attribute.
     *
     * @param key   the attribute key
     * @param value the attribute value
     */
    public void setAttribute(String key, Object value) {
        this.attributes = Map.of(key, value);
    }

    /**
     * Check if this is an entry event.
     *
     * @return true if entry
     */
    public boolean isEntry() {
        return eventType == GeofenceEventType.ENTERED;
    }

    /**
     * Check if this is an exit event.
     *
     * @return true if exit
     */
    public boolean isExit() {
        return eventType == GeofenceEventType.EXITED;
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

    public String getGeofenceId() {
        return geofenceId;
    }

    public String getGeofenceName() {
        return geofenceName;
    }

    public GeofenceEventType getEventType() {
        return eventType;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public LocationEvent.GeoLocation getLocation() {
        return location;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public Instant getCreatedAt() {
        return createdAt;
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

    protected void setGeofenceId(String geofenceId) {
        this.geofenceId = geofenceId;
    }

    protected void setGeofenceName(String geofenceName) {
        this.geofenceName = geofenceName;
    }

    protected void setEventType(GeofenceEventType eventType) {
        this.eventType = eventType;
    }

    protected void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    protected void setLocation(LocationEvent.GeoLocation location) {
        this.location = location;
    }

    protected void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    protected void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Geofence event type enum.
     */
    public enum GeofenceEventType {
        ENTERED,
        EXITED,
        DWELL,
        TRANSITION
    }
}
