package com.gogidix.shared.warehousing.inventory.analytics.domain.entity;

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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "stockout_reports")
@CompoundIndex(def = "{'tenantId': 1, 'warehouseId': 1, 'reportDate': 1}", name = "idx_tenant_warehouse_report")
public class StockoutReport {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String warehouseId;

    private String warehouseName;

    @Indexed
    private LocalDateTime reportDate;

    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;

    // Summary metrics
    private Integer totalStockouts;
    private Integer uniqueSkusAffected;
    private Double totalRevenueImpact;

    // Stockout items
    private List<StockoutItem> stockoutItems;

    // Analysis
    private StockoutTrend trend;
    private String topReason;

    private Map<String, Object> attributes;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockoutItem {
        private String sku;
        private String skuName;
        private Integer stockoutCount;
        private LocalDateTime lastStockoutDate;
        private Double revenueImpact;
        private ImpactLevel impactLevel;
        private Integer recommendedReorderLevel;
        private Integer currentStockLevel;
    }

    public enum StockoutTrend {
        IMPROVING,
        STABLE,
        DECLINING
    }

    public enum ImpactLevel {
        CRITICAL,
        HIGH,
        MEDIUM,
        LOW
    }
}
