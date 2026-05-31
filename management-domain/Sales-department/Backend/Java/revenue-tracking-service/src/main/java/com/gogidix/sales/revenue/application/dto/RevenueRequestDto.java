package com.gogidix.sales.revenue.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevenueRequestDto {
    private String tenantId;
    private String contractId;
    private String customerId;
    private String customerName;
    private String productId;
    private String productName;
    private String territory;
    private String region;
    private String revenueType;
    private BigDecimal totalAmount;
}