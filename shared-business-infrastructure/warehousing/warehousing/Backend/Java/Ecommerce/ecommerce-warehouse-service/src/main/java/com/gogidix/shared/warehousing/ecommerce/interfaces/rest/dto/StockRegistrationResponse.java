package com.gogidix.shared.warehousing.ecommerce.interfaces.rest.dto;

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
public class StockRegistrationResponse {

    private String registrationId;
    private String vendorId;
    private String warehouseId;
    private String zoneId;
    private List<RegisteredItem> items;
    private LocalDateTime estimatedReceivingDate;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisteredItem {
        private String sku;
        private String allocatedLocation;
        private Integer quantity;
        private String status;
    }
}
