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
import java.util.Map;

/**
 * Warehouse Metrics Entity - Multi-tenant with MongoDB
 *
 * Tracks key performance indicators for warehouse operations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "warehouse_metrics")
@CompoundIndex(def = "{'tenantId': 1, 'warehouseId': 1, 'metricDate': 1}", name = "idx_tenant_warehouse_date")
public class WarehouseMetrics {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String warehouseId;

    private String warehouseName;

    @Indexed
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

    // Additional metadata
    private Map<String, Object> attributes;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public enum MetricType {
        CAPACITY,
        THROUGHPUT,
        EFFICIENCY,
        QUALITY,
        TIMING
    }
}
