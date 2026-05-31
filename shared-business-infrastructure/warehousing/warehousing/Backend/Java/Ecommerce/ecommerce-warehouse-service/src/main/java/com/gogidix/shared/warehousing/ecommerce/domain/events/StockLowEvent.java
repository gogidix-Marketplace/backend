package com.gogidix.shared.warehousing.ecommerce.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockLowEvent {
    private String vendorId;
    private String warehouseId;
    private String sku;
    private Integer currentStock;
    private Integer reorderThreshold;
}
