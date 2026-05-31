package com.gogidix.courier.etaservice.domain.entity;

import com.gogidix.courier.etaservice.shared.util.VehicleType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain Entity representing an ETA calculation for a dispatch.
 * Contains all information needed to calculate and track estimated arrival times.
 */
@Document(collection = "eta_calculations")
@CompoundIndex(name = "idx_dispatch_tenant", def = "{'dispatchId': 1, 'tenantId': 1}")
public class EtaCalculation {

    @Id
    private String id;

    @Indexed
    @Field("dispatch_id")
    private String dispatchId;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("pickup_location")
    private Location pickupLocation;

    @Field("dropoff_location")
    private Location dropoffLocation;

    @Field("current_location")
    private Location currentLocation;

    @Field("vehicle_type")
    private String vehicleType;

    @Field("distance_km")
    private Double distanceKm;

    @Field("eta_minutes")
    private Integer etaMinutes;

    @Field("estimated_arrival")
    private Instant estimatedArrival;

    @Field("traffic_level")
    private String trafficLevel;

    @Field("traffic_multiplier")
    private Double trafficMultiplier;

    @Field("confidence_score")
    private Double confidenceScore;

    @Field("calculation_method")
    private String calculationMethod;

    @Field("status")
    private EtaStatus status;

    @Field("created_at")
    private Instant createdAt;

    @Field("updated_at")
    private Instant updatedAt;

    @Field("recalculated_at")
    private Instant recalculatedAt;

    @Field("recalculation_count")
    private Integer recalculationCount;

    /**
     * Default constructor for persistence.
     */
    protected EtaCalculation() {
    }

    /**
     * Create a new ETA calculation.
     *
     * @param dispatchId      the dispatch ID
     * @param tenantId        the tenant ID
     * @param pickupLocation  the pickup location
     * @param dropoffLocation the dropoff location
     * @param vehicleType     the vehicle type
     */
    public EtaCalculation(String dispatchId, String tenantId,
                          Location pickupLocation, Location dropoffLocation,
                          String vehicleType) {
        this.id = java.util.UUID.randomUUID().toString();
        this.dispatchId = Objects.requireNonNull(dispatchId, "dispatchId is required");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.pickupLocation = Objects.requireNonNull(pickupLocation, "pickupLocation is required");
        this.dropoffLocation = Objects.requireNonNull(dropoffLocation, "dropoffLocation is required");
        this.vehicleType = vehicleType != null ? vehicleType : VehicleType.CAR.getCode();
        this.currentLocation = pickupLocation;
        this.status = EtaStatus.PENDING;
        this.trafficLevel = "LOW";
        this.trafficMultiplier = 1.0;
        this.confidenceScore = 0.8;
        this.calculationMethod = "HAVERSINE";
        this.recalculationCount = 0;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // Domain Logic Methods

    /**
     * Update ETA calculation with new values.
     *
     * @param etaMinutes       the ETA in minutes
     * @param distanceKm       the distance in km
     * @param trafficLevel     the traffic level
     * @param trafficMultiplier the traffic multiplier
     * @param confidenceScore  the confidence score
     */
    public void updateEta(Integer etaMinutes, Double distanceKm,
                          String trafficLevel, Double trafficMultiplier,
                          Double confidenceScore) {
        this.etaMinutes = etaMinutes;
        this.distanceKm = distanceKm;
        this.trafficLevel = trafficLevel;
        this.trafficMultiplier = trafficMultiplier;
        this.confidenceScore = confidenceScore;

        // Calculate estimated arrival time
        if (etaMinutes != null) {
            this.estimatedArrival = Instant.now().plusSeconds(etaMinutes * 60L);
        }

        this.status = EtaStatus.CALCULATED;
        this.updatedAt = Instant.now();
    }

    /**
     * Update current location (for in-transit updates).
     *
     * @param currentLocation the new current location
     */
    public void updateCurrentLocation(Location currentLocation) {
        this.currentLocation = Objects.requireNonNull(currentLocation, "currentLocation is required");
        this.updatedAt = Instant.now();
    }

    /**
     * Mark ETA as recalculated.
     */
    public void markAsRecalculated() {
        this.recalculationCount = this.recalculationCount != null ? this.recalculationCount + 1 : 1;
        this.recalculatedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * Mark ETA as in-transit.
     */
    public void markAsInTransit() {
        if (this.status != EtaStatus.CALCULATED && this.status != EtaStatus.RECALCULATED) {
            throw new IllegalStateException("Cannot mark as in-transit: ETA must be calculated first");
        }
        this.status = EtaStatus.IN_TRANSIT;
        this.updatedAt = Instant.now();
    }

    /**
     * Mark ETA as delivered.
     */
    public void markAsDelivered() {
        this.status = EtaStatus.DELIVERED;
        this.updatedAt = Instant.now();
    }

    /**
     * Mark ETA as cancelled.
     */
    public void markAsCancelled() {
        this.status = EtaStatus.CANCELLED;
        this.updatedAt = Instant.now();
    }

    /**
     * Check if ETA can be recalculated.
     *
     * @return true if recalculation is allowed
     */
    public boolean canRecalculate() {
        return status == EtaStatus.CALCULATED
                || status == EtaStatus.RECALCULATED
                || status == EtaStatus.IN_TRANSIT;
    }

    /**
     * Check if ETA is in active state (not delivered or cancelled).
     *
     * @return true if active
     */
    public boolean isActive() {
        return status != EtaStatus.DELIVERED && status != EtaStatus.CANCELLED;
    }

    /**
     * Get remaining distance from current location to dropoff.
     *
     * @return remaining distance in km, or null if not calculated
     */
    public Double getRemainingDistanceKm() {
        if (currentLocation == null || dropoffLocation == null) {
            return null;
        }
        return calculateDistance(currentLocation, dropoffLocation);
    }

    /**
     * Validate the ETA calculation.
     *
     * @throws IllegalArgumentException if validation fails
     */
    public void validate() {
        if (dispatchId == null || dispatchId.isBlank()) {
            throw new IllegalArgumentException("dispatchId is required");
        }
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
        if (pickupLocation == null) {
            throw new IllegalArgumentException("pickupLocation is required");
        }
        if (dropoffLocation == null) {
            throw new IllegalArgumentException("dropoffLocation is required");
        }
        pickupLocation.validate();
        dropoffLocation.validate();
    }

    private Double calculateDistance(Location from, Location to) {
        return calculateHaversineDistance(
                from.getLatitude(), from.getLongitude(),
                to.getLatitude(), to.getLongitude()
        );
    }

    private Double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371.0; // Earth radius in km

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getDispatchId() {
        return dispatchId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public Location getPickupLocation() {
        return pickupLocation;
    }

    public Location getDropoffLocation() {
        return dropoffLocation;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public Integer getEtaMinutes() {
        return etaMinutes;
    }

    public Instant getEstimatedArrival() {
        return estimatedArrival;
    }

    public String getTrafficLevel() {
        return trafficLevel;
    }

    public Double getTrafficMultiplier() {
        return trafficMultiplier;
    }

    public Double getConfidenceScore() {
        return confidenceScore;
    }

    public String getCalculationMethod() {
        return calculationMethod;
    }

    public EtaStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getRecalculatedAt() {
        return recalculatedAt;
    }

    public Integer getRecalculationCount() {
        return recalculationCount;
    }

    // Setters for persistence
    protected void setId(String id) {
        this.id = id;
    }

    protected void setDispatchId(String dispatchId) {
        this.dispatchId = dispatchId;
    }

    protected void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    protected void setPickupLocation(Location pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    protected void setDropoffLocation(Location dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    protected void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    protected void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    protected void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    protected void setEtaMinutes(Integer etaMinutes) {
        this.etaMinutes = etaMinutes;
    }

    protected void setEstimatedArrival(Instant estimatedArrival) {
        this.estimatedArrival = estimatedArrival;
    }

    protected void setTrafficLevel(String trafficLevel) {
        this.trafficLevel = trafficLevel;
    }

    protected void setTrafficMultiplier(Double trafficMultiplier) {
        this.trafficMultiplier = trafficMultiplier;
    }

    protected void setConfidenceScore(Double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    protected void setCalculationMethod(String calculationMethod) {
        this.calculationMethod = calculationMethod;
    }

    protected void setStatus(EtaStatus status) {
        this.status = status;
    }

    protected void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    protected void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    protected void setRecalculatedAt(Instant recalculatedAt) {
        this.recalculatedAt = recalculatedAt;
    }

    protected void setRecalculationCount(Integer recalculationCount) {
        this.recalculationCount = recalculationCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EtaCalculation that = (EtaCalculation) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(dispatchId, that.dispatchId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dispatchId);
    }

    @Override
    public String toString() {
        return "EtaCalculation{" +
                "id='" + id + '\'' +
                ", dispatchId='" + dispatchId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", etaMinutes=" + etaMinutes +
                ", status=" + status +
                ", trafficLevel='" + trafficLevel + '\'' +
                '}';
    }

    /**
     * Location value object.
     */
    public static class Location {
        private Double latitude;
        private Double longitude;
        private String address;
        private String city;
        private String country;

        public Location() {
        }

        public Location(Double latitude, Double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public Location(Double latitude, Double longitude, String address, String city, String country) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.address = address;
            this.city = city;
            this.country = country;
        }

        public void validate() {
            if (latitude == null || latitude < -90 || latitude > 90) {
                throw new IllegalArgumentException("Invalid latitude: " + latitude);
            }
            if (longitude == null || longitude < -180 || longitude > 180) {
                throw new IllegalArgumentException("Invalid longitude: " + longitude);
            }
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Location location = (Location) o;
            return Objects.equals(latitude, location.latitude) &&
                    Objects.equals(longitude, location.longitude);
        }

        @Override
        public int hashCode() {
            return Objects.hash(latitude, longitude);
        }

        @Override
        public String toString() {
            return "Location{" +
                    "latitude=" + latitude +
                    ", longitude=" + longitude +
                    ", address='" + address + '\'' +
                    ", city='" + city + '\'' +
                    '}';
        }
    }

    /**
     * ETA status enum.
     */
    public enum EtaStatus {
        PENDING,
        CALCULATED,
        RECALCULATED,
        IN_TRANSIT,
        DELIVERED,
        CANCELLED,
        FAILED
    }
}
