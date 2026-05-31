package com.gogidix.courier.etaservice.domain.event;

/**
 * Domain event raised when an ETA is calculated for a dispatch.
 */
public class EtaCalculatedEvent extends DomainEvent {

    private final String dispatchId;
    private final String tenantId;
    private final Integer etaMinutes;
    private final Double distanceKm;
    private final String trafficLevel;
    private final Double trafficMultiplier;
    private final Double confidenceScore;
    private final String vehicleType;

    public EtaCalculatedEvent(
            String dispatchId,
            String tenantId,
            Integer etaMinutes,
            Double distanceKm,
            String trafficLevel,
            Double trafficMultiplier,
            Double confidenceScore,
            String vehicleType,
            String correlationId) {
        super("EtaCalculatedEvent", correlationId);
        this.dispatchId = dispatchId;
        this.tenantId = tenantId;
        this.etaMinutes = etaMinutes;
        this.distanceKm = distanceKm;
        this.trafficLevel = trafficLevel;
        this.trafficMultiplier = trafficMultiplier;
        this.confidenceScore = confidenceScore;
        this.vehicleType = vehicleType;
    }

    public String getDispatchId() {
        return dispatchId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public Integer getEtaMinutes() {
        return etaMinutes;
    }

    public Double getDistanceKm() {
        return distanceKm;
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

    public String getVehicleType() {
        return vehicleType;
    }

    @Override
    public String toString() {
        return "EtaCalculatedEvent{" +
                "dispatchId='" + dispatchId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", etaMinutes=" + etaMinutes +
                ", distanceKm=" + distanceKm +
                ", trafficLevel='" + trafficLevel + '\'' +
                ", trafficMultiplier=" + trafficMultiplier +
                ", confidenceScore=" + confidenceScore +
                '}';
    }
}
