package com.gogidix.shared.warehousing.storage.application.dto;

import lombok.Data;

import java.util.Map;

/**
 * DTO for utilization report
 */
@Data
public class UtilizationReport {

    private String tenantId;
    private int totalSpaces;
    private int availableSpaces;
    private int occupiedSpaces;
    private double totalCapacityCubicMeters;
    private double usedCapacityCubicMeters;
    private double averageUtilizationPercentage;
    private Map<String, TypeStatistics> statisticsByType;

    @Data
    public static class TypeStatistics {
        private int totalCount;
        private int availableCount;
        private double totalCapacity;
        private double usedCapacity;
        private double utilizationPercentage;
    }
}
