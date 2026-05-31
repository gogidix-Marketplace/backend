package com.gogidix.courier.etaservice.domain.event;

/**
 * Domain event raised when an ETA is recalculated or updated.
 */
public class EtaUpdatedEvent extends DomainEvent {

    private final String dispatchId;
    private final String tenantId;
    private final Integer previousEtaMinutes;
    private final Integer newEtaMinutes;
    private final Integer etaChangeMinutes;
    private final String changeReason;
    private final String trafficLevel;
    private final Double trafficMultiplier;
    private final Double confidenceScore;
    private final Integer recalculationCount;

    public EtaUpdatedEvent(
            String dispatchId,
            String tenantId,
            Integer previousEtaMinutes,
            Integer newEtaMinutes,
            Integer etaChangeMinutes,
            String changeReason,
            String trafficLevel,
            Double trafficMultiplier,
            Double confidenceScore,
            Integer recalculationCount,
            String correlationId) {
        super("EtaUpdatedEvent", correlationId);
        this.dispatchId = dispatchId;
        this.tenantId = tenantId;
        this.previousEtaMinutes = previousEtaMinutes;
        this.newEtaMinutes = newEtaMinutes;
        this.etaChangeMinutes = etaChangeMinutes;
        this.changeReason = changeReason;
        this.trafficLevel = trafficLevel;
        this.trafficMultiplier = trafficMultiplier;
        this.confidenceScore = confidenceScore;
        this.recalculationCount = recalculationCount;
    }

    public String getDispatchId() {
        return dispatchId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public Integer getPreviousEtaMinutes() {
        return previousEtaMinutes;
    }

    public Integer getNewEtaMinutes() {
        return newEtaMinutes;
    }

    public Integer getEtaChangeMinutes() {
        return etaChangeMinutes;
    }

    public String getChangeReason() {
        return changeReason;
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

    public Integer getRecalculationCount() {
        return recalculationCount;
    }

    /**
     * Check if this is a significant ETA change.
     *
     * @param thresholdPercentage the threshold percentage (e.g., 10 for 10%)
     * @return true if significant change
     */
    public boolean isSignificantChange(double thresholdPercentage) {
        if (previousEtaMinutes == null || previousEtaMinutes == 0) {
            return false;
        }
        double changePercentage = Math.abs((double) etaChangeMinutes / previousEtaMinutes * 100);
        return changePercentage >= thresholdPercentage;
    }

    /**
     * Check if this update represents a delay.
     *
     * @return true if ETA increased
     */
    public boolean isDelay() {
        return etaChangeMinutes != null && etaChangeMinutes > 0;
    }

    /**
     * Check if this update represents an improvement.
     *
     * @return true if ETA decreased
     */
    public boolean isImprovement() {
        return etaChangeMinutes != null && etaChangeMinutes < 0;
    }

    @Override
    public String toString() {
        return "EtaUpdatedEvent{" +
                "dispatchId='" + dispatchId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", previousEtaMinutes=" + previousEtaMinutes +
                ", newEtaMinutes=" + newEtaMinutes +
                ", etaChangeMinutes=" + etaChangeMinutes +
                ", changeReason='" + changeReason + '\'' +
                ", trafficLevel='" + trafficLevel + '\'' +
                '}';
    }
}
