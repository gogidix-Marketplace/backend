package com.gogidix.shared.warehousing.order.application.dto;

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
public class OutboundOrderDTO {
    private String id;
    private String tenantId;
    private String orderNumber;
    private String status;
    private String customerId;
    private String customerName;
    private String shippingAddress;
    private String carrierCode;
    private String serviceCode;
    private LocalDateTime requestedShipDate;
    private LocalDateTime actualShipDate;
    private List<OrderLineDTO> lines;
}
