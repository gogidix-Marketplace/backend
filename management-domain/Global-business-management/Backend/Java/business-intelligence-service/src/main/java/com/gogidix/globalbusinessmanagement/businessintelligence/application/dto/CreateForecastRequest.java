package com.gogidix.globalbusinessmanagement.businessintelligence.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateForecastRequest {
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
    private LocalDateTime historicalPeriodStart;
    private LocalDateTime historicalPeriodEnd;
    private String generatedBy;
    private String modelVersion;
    private Map<String, Object> parameters;
}
