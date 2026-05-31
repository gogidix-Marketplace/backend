package com.gogidix.shared.warehousing.stock.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockAllocationDTO {
    private String allocationId;
    private String orderId;
    private String sku;
    private Integer quantity;
    private String locationId;
    private String warehouseId;
    private String batchNumber;
}
