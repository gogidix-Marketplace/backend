package com.gogidix.courier.etaservice.application.dto;

/**
 * Response DTO for ETA statistics.
 */
public record EtaStatisticsResponse(

        long totalCalculations,
        long activeDeliveries,
        long completedDeliveries,
        double averageEtaMinutes,
        double averageAccuracyPercentage,
        long onTimeDeliveries,
        long delayedDeliveries,
        long earlyDeliveries,
        String mostCommonTrafficLevel,
        TrafficLevelStats trafficLevelStats

) {

    /**
     * Traffic level statistics.
     */
    public record TrafficLevelStats(
            long lowCount,
            long mediumCount,
            long highCount,
            long severeCount
    ) {}
}
