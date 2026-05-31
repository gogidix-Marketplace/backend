package com.gogidix.shared.warehousing.order.application.command;

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
public class CreateOrderCommand {
    private String customerId;
    private String customerName;
    private String shippingAddress;
    private String carrierCode;
    private String serviceCode;
    private LocalDateTime requestedShipDate;
    private List<OrderLineCommand> lines;
}
