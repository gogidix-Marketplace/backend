package com.gogidix.shared.warehousing.warehouse.analytics.domain.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Performance Data Entity - Multi-tenant with MongoDB
 *
 * Tracks operational performance metrics over time
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "performance_data")
@CompoundIndex(def = "{'tenantId': 1, 'warehouseId': 1, 'timestamp': 1}", name = "idx_tenant_warehouse_timestamp")
public class PerformanceData {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String warehouseId;

    private String warehouseName;

    @Indexed
    private LocalDateTime timestamp;

    // Order processing performance
    private OrderPerformance orderPerformance;

    // Inventory performance
    private InventoryPerformance inventoryPerformance;

    // Labor performance
    private LaborPerformance laborPerformance;

    // Equipment performance
    private EquipmentPerformance equipmentPerformance;

    // Quality metrics
    private QualityMetrics qualityMetrics;

    // Performance score
    private Double overallScore;
    private PerformanceGrade grade;

    // Benchmark data
    private Double benchmarkScore;
    private Double varianceFromBenchmark;

    private Map<String, Object> attributes;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderPerformance {
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
    public static class InventoryPerformance {
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
    public static class LaborPerformance {
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
    public static class EquipmentPerformance {
        private Double uptimePercentage;
        private Integer maintenanceEvents;
        private Double meanTimeBetweenFailures;
        private Double utilizationRate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QualityMetrics {
        private Double pickAccuracy;
        private Double packAccuracy;
        private Double shippingAccuracy;
        private Double returnRate;
        private Integer defectCount;
    }

    public enum PerformanceGrade {
        EXCELLENT,
        GOOD,
        SATISFACTORY,
        NEEDS_IMPROVEMENT,
        CRITICAL
    }
}
