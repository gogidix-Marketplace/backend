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
public class VendorStockOverviewResponse {

    private String vendorId;
    private String sellingRadius;
    private Integer totalSKUs;
    private List<WarehouseStockSummary> warehouses;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WarehouseStockSummary {
        private String warehouseId;
        private String zoneId;
        private String warehouseName;
        private Integer skuCount;
        private Integer totalUnits;
    }
}
