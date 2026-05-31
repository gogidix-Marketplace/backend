package com.gogidix.courier.etaservice.domain.event;

import java.time.Instant;

/**
 * Domain event raised when a dispatch is marked as delivered.
 */
public class EtaDeliveredEvent extends DomainEvent {

    private final String dispatchId;
    private final String tenantId;
    private final Instant estimatedArrival;
    private final Instant actualArrival;
    private final Integer estimatedEtaMinutes;
    private final Integer actualEtaMinutes;
    private final Integer etaDeviationMinutes;
    private final Double accuracyPercentage;

    public EtaDeliveredEvent(
            String dispatchId,
            String tenantId,
            Instant estimatedArrival,
            Instant actualArrival,
            Integer estimatedEtaMinutes,
            Integer actualEtaMinutes,
            String correlationId) {
        super("EtaDeliveredEvent", correlationId);
        this.dispatchId = dispatchId;
        this.tenantId = tenantId;
        this.estimatedArrival = estimatedArrival;
        this.actualArrival = actualArrival != null ? actualArrival : Instant.now();
        this.estimatedEtaMinutes = estimatedEtaMinutes;
        this.actualEtaMinutes = actualEtaMinutes;

        if (estimatedEtaMinutes != null && actualEtaMinutes != null) {
            this.etaDeviationMinutes = actualEtaMinutes - estimatedEtaMinutes;
            this.accuracyPercentage = calculateAccuracy(estimatedEtaMinutes, actualEtaMinutes);
        } else {
            this.etaDeviationMinutes = null;
            this.accuracyPercentage = null;
        }
    }

    public String getDispatchId() {
        return dispatchId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public Instant getEstimatedArrival() {
        return estimatedArrival;
    }

    public Instant getActualArrival() {
        return actualArrival;
    }

    public Integer getEstimatedEtaMinutes() {
        return estimatedEtaMinutes;
    }

    public Integer getActualEtaMinutes() {
        return actualEtaMinutes;
    }

    public Integer getEtaDeviationMinutes() {
        return etaDeviationMinutes;
    }

    public Double getAccuracyPercentage() {
        return accuracyPercentage;
    }

    /**
     * Check if the delivery was on time (within 5 minutes of ETA).
     *
     * @return true if on time
     */
    public boolean isOnTime() {
        return etaDeviationMinutes != null && Math.abs(etaDeviationMinutes) <= 5;
    }

    /**
     * Check if the delivery was early.
     *
     * @return true if early
     */
    public boolean isEarly() {
        return etaDeviationMinutes != null && etaDeviationMinutes < 0;
    }

    /**
     * Check if the delivery was late.
     *
     * @return true if late
     */
    public boolean isLate() {
        return etaDeviationMinutes != null && etaDeviationMinutes > 0;
    }

    private Double calculateAccuracy(Integer estimated, Integer actual) {
        if (estimated == null || actual == null || estimated == 0) {
            return null;
        }
        double accuracy = 1.0 - Math.abs((double) (actual - estimated) / estimated);
        return Math.max(0.0, Math.min(1.0, accuracy)) * 100;
    }

    @Override
    public String toString() {
        return "EtaDeliveredEvent{" +
                "dispatchId='" + dispatchId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", estimatedEtaMinutes=" + estimatedEtaMinutes +
                ", actualEtaMinutes=" + actualEtaMinutes +
                ", etaDeviationMinutes=" + etaDeviationMinutes +
                ", accuracyPercentage=" + accuracyPercentage +
                '}';
    }
}
