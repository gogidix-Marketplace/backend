package com.gogidix.platform.metering.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for metric definition.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricDefinitionDto {

    private String id;
    private String metricName;
    private String metricDisplayName;
    private String description;
    private String metricType;
    private String metricCategory;
    private String unit;
    private String aggregationType;
    private Integer retentionDays;
    private boolean billable;
    private BigDecimal unitPrice;
    private String[] dimensions;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
