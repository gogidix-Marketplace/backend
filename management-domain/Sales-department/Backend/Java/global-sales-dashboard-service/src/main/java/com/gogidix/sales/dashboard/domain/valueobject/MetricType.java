package com.gogidix.sales.dashboard.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Value Object for metric types
 * Defines the types of metrics available for dashboard reporting
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricType {

    private String code;
    private String name;
    private String description;
    private MetricCategory category;
    private DataType dataType;
    private String unit;
    private String aggregationType;
    private Boolean isCurrency;
    private String defaultCurrency;
    private Integer decimalPlaces;
    private String formatPattern;
    private List<String> tags;

    public enum MetricCategory {
        REVENUE, DEALS, PIPELINE, ACTIVITY, PERFORMANCE, FORECAST,
        CUSTOMER, PRODUCT, REGION, EFFICIENCY, SATISFACTION, CUSTOM
    }

    public enum DataType {
        MONEY, NUMBER, PERCENTAGE, COUNT, RATING, DURATION, BOOLEAN, STRING
    }

    // Predefined metric types
    public static final MetricType TOTAL_REVENUE = MetricType.builder()
            .code("TOTAL_REVENUE")
            .name("Total Revenue")
            .description("Total revenue generated in the period")
            .category(MetricCategory.REVENUE)
            .dataType(DataType.MONEY)
            .unit("currency")
            .aggregationType("SUM")
            .isCurrency(true)
            .defaultCurrency("USD")
            .decimalPlaces(2)
            .formatPattern("$#,##0.00")
            .build();

    public static final MetricType DEALS_COUNT = MetricType.builder()
            .code("DEALS_COUNT")
            .name("Number of Deals")
            .description("Total number of deals")
            .category(MetricCategory.DEALS)
            .dataType(DataType.COUNT)
            .unit("count")
            .aggregationType("COUNT")
            .isCurrency(false)
            .decimalPlaces(0)
            .formatPattern("#,##0")
            .build();

    public static final MetricType WIN_RATE = MetricType.builder()
            .code("WIN_RATE")
            .name("Win Rate")
            .description("Percentage of deals won")
            .category(MetricCategory.PERFORMANCE)
            .dataType(DataType.PERCENTAGE)
            .unit("%")
            .aggregationType("AVG")
            .isCurrency(false)
            .decimalPlaces(2)
            .formatPattern("0.00%")
            .build();

    public static final MetricType PIPELINE_VALUE = MetricType.builder()
            .code("PIPELINE_VALUE")
            .name("Pipeline Value")
            .description("Total value of open opportunities")
            .category(MetricCategory.PIPELINE)
            .dataType(DataType.MONEY)
            .unit("currency")
            .aggregationType("SUM")
            .isCurrency(true)
            .defaultCurrency("USD")
            .decimalPlaces(2)
            .formatPattern("$#,##0.00")
            .build();

    public static final MetricType AVERAGE_DEAL_SIZE = MetricType.builder()
            .code("AVERAGE_DEAL_SIZE")
            .name("Average Deal Size")
            .description("Average value of closed deals")
            .category(MetricCategory.REVENUE)
            .dataType(DataType.MONEY)
            .unit("currency")
            .aggregationType("AVG")
            .isCurrency(true)
            .defaultCurrency("USD")
            .decimalPlaces(2)
            .formatPattern("$#,##0.00")
            .build();

    public static final MetricType CONVERSION_RATE = MetricType.builder()
            .code("CONVERSION_RATE")
            .name("Conversion Rate")
            .description("Lead to opportunity conversion rate")
            .category(MetricCategory.PERFORMANCE)
            .dataType(DataType.PERCENTAGE)
            .unit("%")
            .aggregationType("AVG")
            .isCurrency(false)
            .decimalPlaces(2)
            .formatPattern("0.00%")
            .build();

    public static final MetricType SALES_ACTIVITIES = MetricType.builder()
            .code("SALES_ACTIVITIES")
            .name("Sales Activities")
            .description("Number of sales activities completed")
            .category(MetricCategory.ACTIVITY)
            .dataType(DataType.COUNT)
            .unit("count")
            .aggregationType("COUNT")
            .isCurrency(false)
            .decimalPlaces(0)
            .formatPattern("#,##0")
            .build();

    public static final MetricType FORECAST_ACCURACY = MetricType.builder()
            .code("FORECAST_ACCURACY")
            .name("Forecast Accuracy")
            .description("Accuracy of sales forecasts")
            .category(MetricCategory.FORECAST)
            .dataType(DataType.PERCENTAGE)
            .unit("%")
            .aggregationType("AVG")
            .isCurrency(false)
            .decimalPlaces(2)
            .formatPattern("0.00%")
            .build();

    public static final MetricType CUSTOMER_SATISFACTION = MetricType.builder()
            .code("CUSTOMER_SATISFACTION")
            .name("Customer Satisfaction")
            .description("Customer satisfaction score")
            .category(MetricCategory.SATISFACTION)
            .dataType(DataType.RATING)
            .unit("score")
            .aggregationType("AVG")
            .isCurrency(false)
            .decimalPlaces(1)
            .formatPattern("0.0")
            .build();

    public static final MetricType MARGIN_PERCENTAGE = MetricType.builder()
            .code("MARGIN_PERCENTAGE")
            .name("Margin Percentage")
            .description("Gross margin percentage")
            .category(MetricCategory.EFFICIENCY)
            .dataType(DataType.PERCENTAGE)
            .unit("%")
            .aggregationType("AVG")
            .isCurrency(false)
            .decimalPlaces(2)
            .formatPattern("0.00%")
            .build();

    /**
     * Formats a value according to metric type
     */
    public String formatValue(BigDecimal value) {
        if (value == null) {
            return "-";
        }

        switch (this.dataType) {
            case MONEY:
                return String.format("$%,.2f", value);
            case PERCENTAGE:
                return String.format("%.2f%%", value);
            case COUNT:
                return String.format("%,d", value.intValue());
            case RATING:
                return String.format("%.1f", value);
            default:
                return value.setScale(this.decimalPlaces, java.math.RoundingMode.HALF_UP).toString();
        }
    }

    /**
     * Gets metric type by code
     */
    public static MetricType fromCode(String code) {
        switch (code) {
            case "TOTAL_REVENUE": return TOTAL_REVENUE;
            case "DEALS_COUNT": return DEALS_COUNT;
            case "WIN_RATE": return WIN_RATE;
            case "PIPELINE_VALUE": return PIPELINE_VALUE;
            case "AVERAGE_DEAL_SIZE": return AVERAGE_DEAL_SIZE;
            case "CONVERSION_RATE": return CONVERSION_RATE;
            case "SALES_ACTIVITIES": return SALES_ACTIVITIES;
            case "FORECAST_ACCURACY": return FORECAST_ACCURACY;
            case "CUSTOMER_SATISFACTION": return CUSTOMER_SATISFACTION;
            case "MARGIN_PERCENTAGE": return MARGIN_PERCENTAGE;
            default:
                return createCustomMetric(code);
        }
    }

    private static MetricType createCustomMetric(String code) {
        return MetricType.builder()
                .code(code)
                .name(code)
                .description("Custom metric")
                .category(MetricCategory.CUSTOM)
                .dataType(DataType.NUMBER)
                .unit("custom")
                .aggregationType("SUM")
                .isCurrency(false)
                .decimalPlaces(2)
                .build();
    }
}
