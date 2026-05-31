package com.gogidix.shared.warehousing.stock.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Stock Level Data Transfer Object
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockLevelDTO {

    private String id;
    private String tenantId;
    private String sku;
    private String locationId;
    private Integer availableQuantity;
    private Integer reservedQuantity;
    private Integer allocatedQuantity;
    private Integer totalQuantity;
    private Integer reorderPoint;
    private Integer reorderQuantity;
    private Boolean belowReorderPoint;
    private LocalDateTime lastMovementAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
