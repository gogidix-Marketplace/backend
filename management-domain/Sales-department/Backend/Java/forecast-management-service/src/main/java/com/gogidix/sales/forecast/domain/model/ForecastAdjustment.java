package com.gogidix.sales.forecast.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Forecast Adjustment Domain Entity
 * Records adjustments made to forecasts
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "forecast_adjustments")
public class ForecastAdjustment {

    private String adjustmentId;

    private String forecastId;

    private String tenantId;

    private String adjustedBy;

    private String adjusterName;

    private Instant adjustedAt;

    private AdjustmentType type;

    private String reason;

    private String lineItemId;

    private String lineItemName;

    private BigDecimal previousAmount;

    private BigDecimal newAmount;

    private BigDecimal difference;

    private String currency;

    private String comments;

    private String previousVersion;

    private String newVersion;

    public enum AdjustmentType {
        LINE_ITEM_UPDATE,
        FORECAST_REVISION,
        CATEGORY_CHANGE,
        PERIOD_CHANGE,
        CORRECTION,
        REFORECAST
    }

    /**
     * Creates a forecast adjustment record
     */
    public static ForecastAdjustment create(String forecastId, String tenantId,
                                             String adjustedBy, AdjustmentType type,
                                             String reason, BigDecimal previousAmount,
                                             BigDecimal newAmount, String currency) {
        String adjustmentId = "ADJ-" + System.currentTimeMillis();

        BigDecimal difference = newAmount.subtract(previousAmount);

        return ForecastAdjustment.builder()
            .adjustmentId(adjustmentId)
            .forecastId(forecastId)
            .tenantId(tenantId)
            .adjustedBy(adjustedBy)
            .adjustedAt(Instant.now())
            .type(type)
            .reason(reason)
            .previousAmount(previousAmount)
            .newAmount(newAmount)
            .difference(difference)
            .currency(currency)
            .build();
    }

    /**
     * Calculates the percentage change
     */
    public BigDecimal getPercentageChange() {
        if (previousAmount == null || previousAmount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return difference.divide(previousAmount, 4, java.math.RoundingMode.HALF_UP)
            .multiply(new BigDecimal("100"));
    }

    /**
     * Checks if this is an increase
     */
    public boolean isIncrease() {
        return difference != null && difference.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Checks if this is a decrease
     */
    public boolean isDecrease() {
        return difference != null && difference.compareTo(BigDecimal.ZERO) < 0;
    }
}
