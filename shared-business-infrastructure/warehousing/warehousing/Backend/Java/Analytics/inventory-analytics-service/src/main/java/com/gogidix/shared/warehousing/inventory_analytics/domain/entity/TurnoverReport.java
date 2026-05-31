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
 * Turnover Report Entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "turnover_reports")
@CompoundIndex(def = "{'tenantId': 1, 'warehouseId': 1, 'reportDate': 1}", name = "idx_tenant_warehouse_report_date")
public class TurnoverReport {

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

    // Overall turnover
    private Double inventoryTurnoverRatio; // times per period
    private Double daysSalesInventory; // days
    private Double turnoverRateChange; // percentage change

    // Cost metrics
    private Double costOfGoodsSold;
    private Double averageInventoryValue;
    private Double totalInventoryCost;

    // Category breakdown
    private List<CategoryTurnover> categoryTurnovers;

    // Performance indicators
    private PerformanceRating performanceRating;
    private Double industryBenchmark;
    private String performanceVsBenchmark; // ABOVE/BELOW/PAR

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
    public static class CategoryTurnover {
        private String category;
        private String categoryId;
        private Double turnoverRatio;
        private Double daysInInventory;
        private Double averageValue;
        private Double cogs;
    }

    public enum ReportPeriod {
        DAILY,
        WEEKLY,
        MONTHLY,
        QUARTERLY,
        YEARLY
    }

    public enum PerformanceRating {
        EXCELLENT,
        GOOD,
        AVERAGE,
        BELOW_AVERAGE,
        POOR
    }
}
