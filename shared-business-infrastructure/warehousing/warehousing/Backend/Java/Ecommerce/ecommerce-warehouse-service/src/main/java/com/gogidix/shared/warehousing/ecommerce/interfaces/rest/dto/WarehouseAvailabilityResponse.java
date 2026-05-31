package com.gogidix.shared.warehousing.ecommerce.interfaces.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseAvailabilityResponse {

    private String sku;
    private Integer totalAvailable;
    private List<WarehouseStockInfo> warehouses;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WarehouseStockInfo {
        private String warehouseId;
        private String warehouseName;
        private String zoneId;
        private Integer available;
        private Integer reserved;
        private Integer incoming;
        private String estimatedPickTime;
    }
}
