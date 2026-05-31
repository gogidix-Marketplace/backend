package com.gogidix.shared.warehousing.warehouse.analytics.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Command to create warehouse metrics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMetricsCommand {

    @NotBlank(message = "Warehouse ID is required")
    private String warehouseId;

    private String warehouseName;

    @NotNull(message = "Metric date is required")
    private LocalDateTime metricDate;

    // Capacity metrics
    private Double totalCapacity;
    private Double usedCapacity;

    // Throughput metrics
    private Integer ordersProcessed;
    private Integer itemsReceived;
    private Integer itemsShipped;

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
}
