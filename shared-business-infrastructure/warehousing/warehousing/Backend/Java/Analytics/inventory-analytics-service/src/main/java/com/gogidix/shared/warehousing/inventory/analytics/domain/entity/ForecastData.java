package com.gogidix.shared.warehousing.inventory.analytics.domain.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "forecast_data")
@CompoundIndex(def = "{'tenantId': 1, 'sku': 1, 'forecastDate': 1}", name = "idx_tenant_sku_forecast")
public class ForecastData {

    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String sku;

    private String skuName;

    @Indexed
    private String warehouseId;

    private String warehouseName;

    @Indexed
    private LocalDateTime forecastDate;

    private LocalDateTime forecastGenerated;

    // Forecast horizon
    private Integer forecastHorizonDays;

    // Forecast values
    private Double predictedDemand;
    private Double minDemand;
    private Double maxDemand;

    // Confidence metrics
    private Double confidenceLevel;
    private ForecastAccuracy accuracy;

    // Seasonal adjustments
    private Double seasonalityFactor;
    private String seasonalityType;

    // Trend analysis
    private TrendDirection trendDirection;
    private Double trendStrength;

    // Historical data used
    private Integer historicalDataPoints;

    // Recommendations
    private String recommendedAction;
    private Integer suggestedReorderQuantity;

    // Additional forecast periods
    private List<ForecastPeriod> forecastPeriods;

    private Map<String, Object> attributes;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForecastPeriod {
        private LocalDateTime periodDate;
        private Double predictedDemand;
        private Double minDemand;
        private Double maxDemand;
        private Double confidenceLevel;
    }

    public enum ForecastAccuracy {
        HIGH,
        MEDIUM,
        LOW,
        UNKNOWN
    }

    public enum TrendDirection {
        INCREASING,
        STABLE,
        DECREASING,
        VOLATILE
    }
}
