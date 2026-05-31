package com.gogidix.ecommerce.pricing.shared.constants;

public final class PricingConstants {

    private PricingConstants() {}

    public static final String SERVICE_NAME = "pricing-service";
    public static final String API_BASE_PATH = "/api/v1/pricing-rules";
    public static final String DEFAULT_CURRENCY = "USD";
    public static final int DEFAULT_PRIORITY = 0;

    public static final String HEADER_TENANT_ID = "X-Tenant-ID";
    public static final String HEADER_USER_ID = "X-User-ID";
    public static final String HEADER_CORRELATION_ID = "X-Correlation-ID";

    public static final String KAFKA_TOPIC_PRICING_EVENTS = "pricing-events";
    public static final String MONGO_COLLECTION_PRICING_RULES = "pricing_rules";
    public static final String MONGO_DATABASE = "gogidix_pricing";

    public static final String METRIC_RULES_CREATED = "pricing.rules.created";
    public static final String METRIC_RULES_UPDATED = "pricing.rules.updated";
    public static final String METRIC_RULES_DELETED = "pricing.rules.deleted";
    public static final String METRIC_CALCULATION_DURATION = "pricing.calculation.duration";
}
