package com.gogidix.shared.warehousing.order.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineCommand {
    private String productId;
    private String productCode;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
}
