package com.gogidix.shared.warehousing.inventory.analytics.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for inventory turnover metrics
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryTurnoverDTO {

    private String productId;
    private String productName;
    private String tenantId;
    private BigDecimal turnoverRatio;
    private Integer daysToSell;
    private BigDecimal averageInventoryValue;
    private BigDecimal costOfGoodsSold;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private String category;
}
