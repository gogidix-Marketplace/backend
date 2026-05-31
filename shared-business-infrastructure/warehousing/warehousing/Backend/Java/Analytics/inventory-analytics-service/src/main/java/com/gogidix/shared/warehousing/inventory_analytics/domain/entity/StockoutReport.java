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
import java.util.List;
import java.util.Map;

/**
 * Stockout Report Entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "stockout_reports")
@CompoundIndex(def = "{'tenantId': 1, 'warehouseId': 1, 'reportDate': 1}", name = "idx_tenant_warehouse_stockout_date")
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

    private ReportPeriod reportPeriod;

    // Overall stockout metrics
    private Integer totalStockouts;
    private Integer uniqueSkusAffected;
    private Double stockoutRate; // percentage
    private Double stockoutFrequency; // occurrences per period

    // Impact metrics
    private Double lostRevenue;
    private Integer lostOrders;
    private Double averageStockoutDuration; // hours

    // Category impact
    private List<CategoryStockout> categoryStockouts;

    // Top stockout items
    private List<StockoutItem> topStockoutItems;

    // Predictive indicators
    private List<String> highRiskSkus;
    private Integer predictedStockouts;

    // Recommendations
    private List<String> recommendations;

    private Map<String, Object> attributes;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryStockout {
        private String category;
        private String categoryId;
        private Integer stockoutCount;
        private Double stockoutRate;
        private Double impactValue;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockoutItem {
        private String sku;
        private String productName;
        private Integer stockoutCount;
        private Double stockoutDuration;
        private Double estimatedLostRevenue;
        private String reason;
    }

    public enum ReportPeriod {
        DAILY,
        WEEKLY,
        MONTHLY,
        QUARTERLY
    }
}
