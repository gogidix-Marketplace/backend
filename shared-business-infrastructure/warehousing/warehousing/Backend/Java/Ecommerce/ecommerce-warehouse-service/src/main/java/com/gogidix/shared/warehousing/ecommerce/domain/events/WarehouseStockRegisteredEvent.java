package com.gogidix.shared.warehousing.ecommerce.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseStockRegisteredEvent {
    private String vendorId;
    private String warehouseId;
    private String zoneId;
    private List<StockItemInfo> items;
    private LocalDateTime registeredAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockItemInfo {
        private String sku;
        private Integer quantity;
        private String location;
    }
}
