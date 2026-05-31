package com.gogidix.shared.warehousing.warehouse.analytics.application.dto;

import com.gogidix.shared.warehousing.warehouse.analytics.domain.entity.WarehouseMetrics;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Warehouse Metrics Data Transfer Object
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseMetricsDTO {

    private String id;
    private String tenantId;
    private String warehouseId;
    private String warehouseName;
    private LocalDateTime metricDate;

    // Capacity metrics
    private Double totalCapacity;
    private Double usedCapacity;
    private Double availableCapacity;
    private Double utilizationPercentage;

    // Throughput metrics
    private Integer ordersProcessed;
    private Integer itemsReceived;
    private Integer itemsShipped;
    private Double ordersPerHour;

    // Efficiency metrics
    private Double laborEfficiency;
    private Double spaceEfficiency;
    private Double equipmentUtilization;

    // Quality metrics
    private Integer pickAccuracy;
    private Integer packAccuracy;
    private Integer inventoryAccuracy;

    // Time metrics
    private Double averagePickTime;
    private Double averagePackTime;
    private Double averageProcessingTime;

    private Map<String, Object> attributes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
