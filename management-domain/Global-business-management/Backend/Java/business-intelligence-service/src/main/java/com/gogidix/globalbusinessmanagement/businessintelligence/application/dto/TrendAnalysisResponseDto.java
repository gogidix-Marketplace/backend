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
public class TrendAnalysisResponseDto {
    private String id;
    private String analysisName;
    private String metricName;
    private String metricCode;
    private String entityCode;
    private String entityType;
    private String regionCode;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private String analysisType;
    private String trendDirection;
    private BigDecimal trendStrength;
    private String trendPattern;
    private Instant analyzedAt;
    private String analysisVersion;
    private Instant createdAt;
    private Instant updatedAt;
}
