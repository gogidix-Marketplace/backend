package com.gogidix.shared.warehousing.inventory.analytics.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for inventory forecast data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForecastDataDTO {

    private String forecastId;
    private String productId;
    private String productName;
    private String tenantId;

    private LocalDate forecastDate;
    private BigDecimal predictedDemand;
    private BigDecimal confidenceLevel;
    private String trend; // INCREASING, STABLE, DECREASING

    private List<ForecastPoint> historicalData;
    private List<ForecastPoint> forecastPoints;

    private LocalDateTime createdAt;
    private String createdBy;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForecastPoint {
        private LocalDate date;
        private BigDecimal value;
        private BigDecimal actualValue;
    }
}
