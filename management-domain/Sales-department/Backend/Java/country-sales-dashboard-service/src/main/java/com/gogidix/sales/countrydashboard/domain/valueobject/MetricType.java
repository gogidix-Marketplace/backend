package com.gogidix.sales.countrydashboard.domain.valueobject;

import lombok.Getter;

/**
 * Metric Type Enumeration
 * Defines the types of metrics tracked in country sales dashboards
 */
@Getter
public enum MetricType {

    // Revenue Metrics
    TOTAL_REVENUE("Total Revenue", "REVENUE", MetricCategory.FINANCIAL),
    RECURRING_REVENUE("Recurring Revenue", "REVENUE", MetricCategory.FINANCIAL),
    NEW_REVENUE("New Revenue", "REVENUE", MetricCategory.FINANCIAL),
    RENEWAL_REVENUE("Renewal Revenue", "REVENUE", MetricCategory.FINANCIAL),
    UPSSELL_REVENUE("Upsell Revenue", "REVENUE", MetricCategory.FINANCIAL),

    // Deal Metrics
    TOTAL_DEALS("Total Deals", "DEALS", MetricCategory.SALES),
    WON_DEALS("Won Deals", "DEALS", MetricCategory.SALES),
    LOST_DEALS("Lost Deals", "DEALS", MetricCategory.SALES),
    OPEN_DEALS("Open Deals", "DEALS", MetricCategory.SALES),
    NEW_DEALS("New Deals", "DEALS", MetricCategory.SALES),
    AVG_DEAL_SIZE("Average Deal Size", "DEALS", MetricCategory.SALES),

    // Pipeline Metrics
    PIPELINE_VALUE("Pipeline Value", "PIPELINE", MetricCategory.SALES),
    WEIGHTED_PIPELINE("Weighted Pipeline", "PIPELINE", MetricCategory.SALES),
    PIPELINE_VELOCITY("Pipeline Velocity", "PIPELINE", MetricCategory.SALES),
    STAGE_DISTRIBUTION("Stage Distribution", "PIPELINE", MetricCategory.SALES),

    // Performance Metrics
    WIN_RATE("Win Rate", "PERFORMANCE", MetricCategory.PERFORMANCE),
    CONVERSION_RATE("Conversion Rate", "PERFORMANCE", MetricCategory.PERFORMANCE),
    QUOTA_ATTAINMENT("Quota Attainment", "PERFORMANCE", MetricCategory.PERFORMANCE),
    ACTIVITY_RATE("Activity Rate", "PERFORMANCE", MetricCategory.PERFORMANCE),
    RESPONSE_TIME("Response Time", "PERFORMANCE", MetricCategory.PERFORMANCE),

    // Growth Metrics
    YOY_GROWTH("Year Over Year Growth", "GROWTH", MetricCategory.GROWTH),
    QOQ_GROWTH("Quarter Over Quarter Growth", "GROWTH", MetricCategory.GROWTH),
    MOM_GROWTH("Month Over Month Growth", "GROWTH", MetricCategory.GROWTH),

    // Customer Metrics
    NEW_CUSTOMERS("New Customers", "CUSTOMER", MetricCategory.CUSTOMER),
    CHURNED_CUSTOMERS("Churned Customers", "CUSTOMER", MetricCategory.CUSTOMER),
    RETENTION_RATE("Retention Rate", "CUSTOMER", MetricCategory.CUSTOMER),
    NPS_SCORE("NPS Score", "CUSTOMER", MetricCategory.CUSTOMER),
    CUSTOMER_SATISFACTION("Customer Satisfaction", "CUSTOMER", MetricCategory.CUSTOMER);

    private final String displayName;
    private final String category;
    private final MetricCategory metricCategory;

    MetricType(String displayName, String category, MetricCategory metricCategory) {
        this.displayName = displayName;
        this.category = category;
        this.metricCategory = metricCategory;
    }

    public enum MetricCategory {
        FINANCIAL, SALES, PERFORMANCE, GROWTH, CUSTOMER, PIPELINE
    }

    public static MetricType fromString(String text) {
        for (MetricType type : MetricType.values()) {
            if (type.name().equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No metric type with name " + text + " found");
    }
}
