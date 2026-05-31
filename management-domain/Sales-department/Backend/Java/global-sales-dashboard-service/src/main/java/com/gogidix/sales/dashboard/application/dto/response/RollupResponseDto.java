package com.gogidix.sales.dashboard.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Rollup Response DTO
 * Used for sending rollup data to clients
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RollupResponseDto {

    private String id;
    private String rollupId;
    private String tenantId;
    private String rollupType;
    private String rollupKey;
    private String rollupName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String periodType;
    private Integer periodValue;
    private Integer year;
    private String quarter;

    // Metrics
    private MoneyDto revenue;
    private Integer deals;
    private Double winRate;
    private MoneyDto pipelineValue;
    private Integer opportunities;
    private MoneyDto averageDealSize;
    private Double growthRate;
    private Integer newCustomers;
    private MoneyDto margin;
    private Double marginPercentage;

    // Performance
    private String overallStatus;
    private Double score;
    private String trend;
    private List<String> strengths;
    private List<String> weaknesses;
    private String recommendation;
    private Integer riskLevel;

    // Targets
    private Map<String, TargetDto> targets;

    // Metadata
    private Boolean isRealtime;
    private Integer lagSeconds;
    private Instant rollupTime;
    private Integer dataVersion;
    private Instant createdAt;
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MoneyDto {
        private Double amount;
        private String currency;
        private String formatted;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TargetDto {
        private String targetType;
        private MoneyDto targetValue;
        private MoneyDto currentValue;
        private Double achievementPercentage;
        private MoneyDto remaining;
        private Boolean isOnTrack;
    }
}
