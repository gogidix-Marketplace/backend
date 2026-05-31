package com.gogidix.courier.etaservice.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain Entity representing the historical record of ETA calculations.
 * Tracks all changes and recalculations for a dispatch.
 */
@Document(collection = "eta_history")
@CompoundIndex(name = "idx_dispatch_timestamp", def = "{'dispatchId': 1, 'timestamp': -1}")
@CompoundIndex(name = "idx_tenant_timestamp", def = "{'tenantId': 1, 'timestamp': -1}")
public class EtaHistory {

    @Id
    private String id;

    @Indexed
    @Field("dispatch_id")
    private String dispatchId;

    @Indexed
    @Field("tenant_id")
    private String tenantId;

    @Field("timestamp")
    private Instant timestamp;

    @Field("eta_minutes")
    private Integer etaMinutes;

    @Field("previous_eta_minutes")
    private Integer previousEtaMinutes;

    @Field("eta_change_minutes")
    private Integer etaChangeMinutes;

    @Field("distance_km")
    private Double distanceKm;

    @Field("current_location")
    private EtaCalculation.Location currentLocation;

    @Field("traffic_level")
    private String trafficLevel;

    @Field("traffic_multiplier")
    private Double trafficMultiplier;

    @Field("vehicle_type")
    private String vehicleType;

    @Field("confidence_score")
    private Double confidenceScore;

    @Field("calculation_method")
    private String calculationMethod;

    @Field("change_reason")
    private String changeReason;

    @Field("change_type")
    private ChangeType changeType;

    @Field("calculated_by")
    private String calculatedBy;

    @Field("source")
    private String source;

    /**
     * Default constructor for persistence.
     */
    protected EtaHistory() {
    }

    /**
     * Create a new history entry.
     *
     * @param dispatchId    the dispatch ID
     * @param tenantId      the tenant ID
     * @param calculation   the current ETA calculation
     * @param changeType    the type of change
     * @param changeReason  the reason for the change
     */
    public EtaHistory(String dispatchId, String tenantId,
                      EtaCalculation calculation,
                      ChangeType changeType,
                      String changeReason) {
        this.id = java.util.UUID.randomUUID().toString();
        this.dispatchId = Objects.requireNonNull(dispatchId, "dispatchId is required");
        this.tenantId = Objects.requireNonNull(tenantId, "tenantId is required");
        this.timestamp = Instant.now();
        this.changeType = Objects.requireNonNull(changeType, "changeType is required");
        this.changeReason = changeReason;
        this.source = "ETA_SERVICE";

        if (calculation != null) {
            this.etaMinutes = calculation.getEtaMinutes();
            this.distanceKm = calculation.getDistanceKm();
            this.currentLocation = calculation.getCurrentLocation();
            this.trafficLevel = calculation.getTrafficLevel();
            this.trafficMultiplier = calculation.getTrafficMultiplier();
            this.vehicleType = calculation.getVehicleType();
            this.confidenceScore = calculation.getConfidenceScore();
            this.calculationMethod = calculation.getCalculationMethod();
        }
    }

    /**
     * Create a history entry with previous ETA comparison.
     *
     * @param dispatchId      the dispatch ID
     * @param tenantId        the tenant ID
     * @param etaMinutes      the new ETA
     * @param previousEtaMinutes the previous ETA
     * @param calculation     the ETA calculation
     * @param changeType      the type of change
     * @param changeReason    the reason for change
     */
    public EtaHistory(String dispatchId, String tenantId,
                      Integer etaMinutes, Integer previousEtaMinutes,
                      EtaCalculation calculation,
                      ChangeType changeType, String changeReason) {
        this(dispatchId, tenantId, calculation, changeType, changeReason);
        this.etaMinutes = etaMinutes;
        this.previousEtaMinutes = previousEtaMinutes;

        if (etaMinutes != null && previousEtaMinutes != null) {
            this.etaChangeMinutes = etaMinutes - previousEtaMinutes;
        }
    }

    /**
     * Check if this is a significant ETA change.
     *
     * @param thresholdPercentage the threshold percentage
     * @return true if significant change
     */
    public boolean isSignificantChange(double thresholdPercentage) {
        if (etaChangeMinutes == null || previousEtaMinutes == null || previousEtaMinutes == 0) {
            return false;
        }
        double changePercentage = Math.abs((double) etaChangeMinutes / previousEtaMinutes * 100);
        return changePercentage >= thresholdPercentage;
    }

    /**
     * Check if ETA increased (delay).
     *
     * @return true if ETA increased
     */
    public boolean isDelay() {
        return etaChangeMinutes != null && etaChangeMinutes > 0;
    }

    /**
     * Check if ETA decreased (improvement).
     *
     * @return true if ETA decreased
     */
    public boolean isImprovement() {
        return etaChangeMinutes != null && etaChangeMinutes < 0;
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

    public Instant getTimestamp() {
        return timestamp;
    }

    public Integer getEtaMinutes() {
        return etaMinutes;
    }

    public Integer getPreviousEtaMinutes() {
        return previousEtaMinutes;
    }

    public Integer getEtaChangeMinutes() {
        return etaChangeMinutes;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public EtaCalculation.Location getCurrentLocation() {
        return currentLocation;
    }

    public String getTrafficLevel() {
        return trafficLevel;
    }

    public Double getTrafficMultiplier() {
        return trafficMultiplier;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public Double getConfidenceScore() {
        return confidenceScore;
    }

    public String getCalculationMethod() {
        return calculationMethod;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public String getCalculatedBy() {
        return calculatedBy;
    }

    public String getSource() {
        return source;
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

    protected void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    protected void setEtaMinutes(Integer etaMinutes) {
        this.etaMinutes = etaMinutes;
    }

    protected void setPreviousEtaMinutes(Integer previousEtaMinutes) {
        this.previousEtaMinutes = previousEtaMinutes;
    }

    protected void setEtaChangeMinutes(Integer etaChangeMinutes) {
        this.etaChangeMinutes = etaChangeMinutes;
    }

    protected void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    protected void setCurrentLocation(EtaCalculation.Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    protected void setTrafficLevel(String trafficLevel) {
        this.trafficLevel = trafficLevel;
    }

    protected void setTrafficMultiplier(Double trafficMultiplier) {
        this.trafficMultiplier = trafficMultiplier;
    }

    protected void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    protected void setConfidenceScore(Double confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    protected void setCalculationMethod(String calculationMethod) {
        this.calculationMethod = calculationMethod;
    }

    protected void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    protected void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }

    protected void setCalculatedBy(String calculatedBy) {
        this.calculatedBy = calculatedBy;
    }

    protected void setSource(String source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EtaHistory that = (EtaHistory) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "EtaHistory{" +
                "id='" + id + '\'' +
                ", dispatchId='" + dispatchId + '\'' +
                ", timestamp=" + timestamp +
                ", etaMinutes=" + etaMinutes +
                ", changeType=" + changeType +
                '}';
    }

    /**
     * Change type enum.
     */
    public enum ChangeType {
        INITIAL_CALCULATION,
        RECALCULATION,
        LOCATION_UPDATE,
        TRAFFIC_UPDATE,
        MANUAL_ADJUSTMENT,
        AUTOMATIC_UPDATE,
        CANCELLED,
        DELIVERED
    }
}
