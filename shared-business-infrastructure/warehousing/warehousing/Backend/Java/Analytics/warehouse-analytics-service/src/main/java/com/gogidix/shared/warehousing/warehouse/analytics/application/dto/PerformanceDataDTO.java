package com.gogidix.shared.warehousing.warehouse.analytics.application.dto;

import com.gogidix.shared.warehousing.warehouse.analytics.domain.entity.PerformanceData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Performance Data Transfer Object
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceDataDTO {

    private String id;
    private String tenantId;
    private String warehouseId;
    private String warehouseName;
    private LocalDateTime timestamp;

    // Order processing performance
    private OrderPerformanceDTO orderPerformance;

    // Inventory performance
    private InventoryPerformanceDTO inventoryPerformance;

    // Labor performance
    private LaborPerformanceDTO laborPerformance;

    // Equipment performance
    private EquipmentPerformanceDTO equipmentPerformance;

    // Quality metrics
    private QualityMetricsDTO qualityMetrics;

    // Performance score
    private Double overallScore;
    private String grade;

    // Benchmark data
    private Double benchmarkScore;
    private Double varianceFromBenchmark;

    private Map<String, Object> attributes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderPerformanceDTO {
        private Integer totalOrders;
        private Integer ordersOnTime;
        private Integer ordersLate;
        private Double onTimeDeliveryRate;
        private Double averageProcessingTime;
        private Double averageCycleTime;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InventoryPerformanceDTO {
        private Integer inventoryTurns;
        private Double daysInInventory;
        private Double stockoutRate;
        private Double accuracyRate;
        private Integer cycleCountAccuracy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LaborPerformanceDTO {
        private Double productivityRate;
        private Integer ordersPerPerson;
        private Double utilizationRate;
        private Double overtimePercentage;
        private Double attendanceRate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EquipmentPerformanceDTO {
        private Double uptimePercentage;
        private Integer maintenanceEvents;
        private Double meanTimeBetweenFailures;
        private Double utilizationRate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QualityMetricsDTO {
        private Double pickAccuracy;
        private Double packAccuracy;
        private Double shippingAccuracy;
        private Double returnRate;
        private Integer defectCount;
    }
}
