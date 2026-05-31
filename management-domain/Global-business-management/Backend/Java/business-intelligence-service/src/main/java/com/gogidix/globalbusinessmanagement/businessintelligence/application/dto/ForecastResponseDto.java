package com.gogidix.globalbusinessmanagement.businessintelligence.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForecastResponseDto {
    private String id;
    private String forecastName;
    private String metricCode;
    private String metricName;
    private String entityCode;
    private String entityType;
    private String regionCode;
    private String forecastType;
    private String forecastMethod;
    private LocalDateTime forecastPeriodStart;
    private LocalDateTime forecastPeriodEnd;
    private String status;
    private BigDecimal confidenceLevel;
    private Instant generatedAt;
    private Instant validUntil;
    private String generatedBy;
    private String modelVersion;
    private Instant createdAt;
    private Instant updatedAt;
}
