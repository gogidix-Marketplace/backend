package com.gogidix.shared.warehousing.ecommerce.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockDepletedEvent {
    private String vendorId;
    private String warehouseId;
    private String sku;
    private LocalDateTime depletedAt;
}
