package com.gogidix.shared.warehousing.inventory_analytics.domain.entity;

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
 * Inventory Metrics Entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "inventory_metrics")
@CompoundIndex(def = "{'tenantId': 1, 'warehouseId': 1, 'metricDate': 1}", name = "idx_tenant_warehouse_metric_date")
public class InventoryMetrics {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String warehouseId;

    private String warehouseName;

    @Indexed
    private LocalDateTime metricDate;

    // Overall inventory metrics
    private Integer totalSkus;
    private Integer totalUnits;
    private Double totalInventoryValue;

    // Category metrics
    private Integer activeSkus;
    private Integer obsoleteSkus;
    private Integer slowMovingSkus;

    // Stock metrics
    private Integer inStockItems;
    private Integer outOfStockItems;
    private Integer lowStockItems;

    // Value metrics
    private Double averageUnitValue;
    private Double inventoryCarryingCost;
    private Double agingInventoryValue;

    // Movement metrics
    private Double averageDailySales;
    private Double averageDailyReceipts;
    private Double inventoryTurnoverRate;

    // Accuracy metrics
    private Double inventoryAccuracy; // percentage
    private Integer cycleCountVariance;

    private Map<String, Object> attributes;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
