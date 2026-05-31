package com.gogidix.dashboard.shared.constants;

/**
 * Constants for dashboard services.
 */
public final class DashboardConstants {

    private DashboardConstants() {}

    // Tenant
    public static final String TENANT_HEADER = "X-Tenant-ID";
    public static final String TENANT_DEFAULT = "default";

    // Correlation
    public static final String CORRELATION_HEADER = "X-Correlation-ID";

    // KPI
    public static final String KPI_CODE_PREFIX = "KPI_";
    public static final int KPI_CODE_MAX_LENGTH = 50;

    // Cache
    public static final String CACHE_PREFIX_KPI = "kpi:";
    public static final String CACHE_PREFIX_DASHBOARD = "dashboard:";
    public static final long CACHE_TTL_SECONDS = 300L; // 5 minutes

    // Topics
    public static final String TOPIC_KPI_EVENTS = "dashboard.kpi.events";
    public static final String TOPIC_DATA_AGGREGATION = "dashboard.data.aggregation";
    public static final String TOPIC_WIDGET_EVENTS = "dashboard.widget.events";

    // Events
    public static final String EVENT_KPI_CREATED = "KPI_CREATED";
    public static final String EVENT_KPI_UPDATED = "KPI_UPDATED";
    public static final String EVENT_KPI_DELETED = "KPI_DELETED";
    public static final String EVENT_KPI_VALUE_UPDATED = "KPI_VALUE_UPDATED";
    public static final String EVENT_KPI_CALCULATED = "KPI_CALCULATED";

    // Source Domains
    public static final String SOURCE_COURIER_SERVICE = "COURIER_SERVICE";
    public static final String SOURCE_WAREHOUSE = "WAREHOUSE";
    public static final String SOURCE_SOCIAL_COMMERCE = "SOCIAL_COMMERCE";
    public static final String SOURCE_PAYMENT = "PAYMENT";
    public static final String SOURCE_USER_MANAGEMENT = "USER_MANAGEMENT";
    public static final String SOURCE_ANALYTICS = "ANALYTICS";
    public static final String SOURCE_OPERATIONS = "OPERATIONS";
    public static final String SOURCE_CUSTOMER_SERVICE = "CUSTOMER_SERVICE";
    public static final String SOURCE_SALES = "SALES";
    public static final String SOURCE_MARKETING = "MARKETING";
    public static final String SOURCE_FINANCE = "FINANCE";
    public static final String SOURCE_HR = "HR";
    public static final String SOURCE_LEGACY = "LEGACY";
    public static final String SOURCE_EXTERNAL = "EXTERNAL";
    public static final String SOURCE_AGGREGATED = "AGGREGATED";

    // KPI Categories
    public static final String CATEGORY_SALES = "Sales";
    public static final String CATEGORY_OPERATIONS = "Operations";
    public static final String CATEGORY_FINANCE = "Finance";
    public static final String CATEGORY_CUSTOMER = "Customer";
    public static final String CATEGORY_PERFORMANCE = "Performance";
    public static final String CATEGORY_QUALITY = "Quality";
    public static final String CATEGORY_ENGAGEMENT = "Engagement";
    public static final String CATEGORY_REVENUE = "Revenue";
    public static final String CATEGORY_METRICS = "Metrics";

    // Data Types
    public static final String TYPE_NUMERIC = "NUMERIC";
    public static final String TYPE_CURRENCY = "CURRENCY";
    public static final String TYPE_PERCENTAGE = "PERCENTAGE";
    public static final String TYPE_COUNT = "COUNT";
    public static final String TYPE_DURATION = "DURATION";
    public static final String TYPE_TEXT = "TEXT";
    public static final String TYPE_BOOLEAN = "BOOLEAN";

    // Aggregation Types
    public static final String AGG_SUM = "SUM";
    public static final String AGG_AVG = "AVG";
    public static final String AGG_COUNT = "COUNT";
    public static final String AGG_MIN = "MIN";
    public static final String AGG_MAX = "MAX";
    public static final String AGG_DISTINCT_COUNT = "DISTINCT_COUNT";

    // Error Codes
    public static final String ERROR_TENANT_NOT_FOUND = "TENANT_NOT_FOUND";
    public static final String ERROR_TENANT_INVALID = "TENANT_INVALID";
    public static final String ERROR_KPI_NOT_FOUND = "KPI_NOT_FOUND";
    public static final String ERROR_KPI_EXISTS = "KPI_EXISTS";
    public static final String ERROR_INVALID_INPUT = "INVALID_INPUT";
    public static final String ERROR_CALCULATION_FAILED = "CALCULATION_FAILED";
    public static final String ERROR_DATA_SOURCE_ERROR = "DATA_SOURCE_ERROR";
    public static final String ERROR_UNAUTHORIZED = "UNAUTHORIZED";
    public static final String ERROR_FORBIDDEN = "FORBIDDEN";

    // Pagination Defaults
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    public static final int DEFAULT_PAGE = 0;
}
