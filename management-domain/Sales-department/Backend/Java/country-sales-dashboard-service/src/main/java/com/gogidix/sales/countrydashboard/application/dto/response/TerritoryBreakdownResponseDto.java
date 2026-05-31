package com.gogidix.sales.countrydashboard.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

/**
 * Territory Breakdown Response DTO
 * Used for API responses with territory performance data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TerritoryBreakdownResponseDto {

    private String territoryId;
    private String territoryName;
    private String territoryCode;
    private BigDecimal revenue;
    private String currency;
    private BigDecimal quota;
    private BigDecimal achievementPercentage;
    private Integer deals;
    private Integer wonDeals;
    private BigDecimal winRate;
    private BigDecimal averageDealSize;
    private Integer rank;
    private Integer previousRank;
    private BigDecimal growthRate;
    private String topPerformer;
    private String performanceIndicator;
    private Instant lastUpdated;
    private Map<String, Object> attributes;
}
