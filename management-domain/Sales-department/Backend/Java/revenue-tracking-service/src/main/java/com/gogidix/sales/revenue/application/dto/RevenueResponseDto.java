package com.gogidix.sales.revenue.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevenueResponseDto {
    private String id;
    private String revenueId;
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
    private Instant createdAt;
    private Instant updatedAt;
}