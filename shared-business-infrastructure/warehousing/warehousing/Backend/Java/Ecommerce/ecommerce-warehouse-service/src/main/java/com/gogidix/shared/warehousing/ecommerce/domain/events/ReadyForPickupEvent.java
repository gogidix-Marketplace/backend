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
public class ReadyForPickupEvent {
    private String fulfillmentId;
    private String orderId;
    private String subOrderId;
    private String warehouseId;
    private String zoneId;
    private String warehouseAddress;
    private Double warehouseLatitude;
    private Double warehouseLongitude;
    private String contactName;
    private String contactPhone;
    private Integer packageCount;
    private LocalDateTime readyAt;
    private String deliveryType;
}
