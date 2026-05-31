package com.gogidix.sales.forecast.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.YearMonth;

/**
 * Forecast Line Item Domain Entity
 * Represents a single line item in a sales forecast
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "forecast_line_items")
public class ForecastLineItem {

    private String lineItemId;

    private String forecastId;

    private String tenantId;

    private String name;

    private String description;

    private ForecastCategory category;

    private LineItemType type;

    private String productId;

    private String productName;

    private String territoryId;

    private String territoryName;

    private String customerSegmentId;

    private String customerSegmentName;

    private String salesChannel;

    private BigDecimal bestCase;

    private BigDecimal likely;

    private BigDecimal worstCase;

    private String currency;

    private YearMonth period;

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal probability;

    private String notes;

    private String owner;

    private Integer sortOrder;

    private Boolean active;

    /**
     * Creates a new forecast line item
     */
    public static ForecastLineItem create(String forecastId, String tenantId,
                                           String name, ForecastCategory category,
                                           LineItemType type, BigDecimal bestCase,
                                           BigDecimal likely, BigDecimal worstCase,
                                           String currency) {
        String lineItemId = "LI-" + System.currentTimeMillis();

        return ForecastLineItem.builder()
            .lineItemId(lineItemId)
            .forecastId(forecastId)
            .tenantId(tenantId)
            .name(name)
            .category(category)
            .type(type)
            .bestCase(bestCase)
            .likely(likely)
            .worstCase(worstCase)
            .currency(currency)
            .probability(new BigDecimal("50"))
            .active(true)
            .build();
    }

    /**
     * Updates the line item values
     */
    public void updateValues(ForecastCategory category, BigDecimal bestCase,
                             BigDecimal likely, BigDecimal worstCase) {
        this.category = category;
        this.bestCase = bestCase;
        this.likely = likely;
        this.worstCase = worstCase;
    }

    /**
     * Calculates the weighted forecast value
     */
    public BigDecimal getWeightedForecast() {
        if (this.likely == null || this.probability == null) {
            return this.likely;
        }
        return this.likely.multiply(this.probability)
            .divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
    }

    /**
     * Gets the variance percentage between best and worst case
     */
    public BigDecimal getVariancePercentage() {
        if (this.worstCase == null || this.worstCase.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return this.bestCase.subtract(this.worstCase)
            .divide(this.worstCase, 4, java.math.RoundingMode.HALF_UP)
            .multiply(new BigDecimal("100"));
    }

    /**
     * Checks if the line item is within tolerance
     */
    public boolean isWithinTolerance(BigDecimal tolerancePercentage) {
        BigDecimal variance = getVariancePercentage();
        return variance.compareTo(tolerancePercentage) <= 0;
    }

    /**
     * Deactivates the line item
     */
    public void deactivate() {
        this.active = false;
    }

    /**
     * Activates the line item
     */
    public void activate() {
        this.active = true;
    }

    // Enum definitions
    public enum LineItemType {
        PRODUCT,
        TERRITORY,
        CUSTOMER_SEGMENT,
        SALES_CHANNEL,
        SERVICE,
        SUBSCRIPTION
    }

    public enum ForecastCategory {
        BEST_CASE,
        LIKELY,
        WORST_CASE
    }
}
