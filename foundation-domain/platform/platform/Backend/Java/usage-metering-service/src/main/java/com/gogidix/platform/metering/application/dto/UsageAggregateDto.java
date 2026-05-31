package com.gogidix.platform.metering.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for usage aggregate.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsageAggregateDto {

    private String id;
    private String tenantId;
    private String metricName;
    private String aggregationType;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private BigDecimal totalQuantity;
    private BigDecimal avgQuantity;
    private BigDecimal maxQuantity;
    private BigDecimal minQuantity;
    private Integer count;
    private Map<String, Object> dimensions;
    private BigDecimal quotaLimit;
    private BigDecimal quotaUsagePercentage;
    private LocalDateTime createdAt;
}
