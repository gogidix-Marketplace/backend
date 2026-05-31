package com.gogidix.shared.warehousing.inventory.analytics.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for stockout reports
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockoutReportDTO {

    private String reportId;
    private String tenantId;
    private LocalDateTime reportDate;
    private String warehouseId;

    private List<StockoutItem> stockoutItems;
    private Integer totalStockouts;
    private Integer criticalItems;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockoutItem {
        private String productId;
        private String productName;
        private String sku;
        private Integer quantity;
        private LocalDateTime stockoutDate;
        private String category;
        private Boolean isCritical;
    }
}
