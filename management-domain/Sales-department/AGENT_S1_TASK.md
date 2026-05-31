# AGENT S1 - GLOBAL SALES DASHBOARD SERVICE

**Agent ID:** S1
**Domain:** Sales-Departments
**Service:** global-sales-dashboard-service
**Priority:** P0 (CRITICAL)

---

## 🎯 SERVICE PURPOSE

Implement a global sales dashboard service that aggregates sales metrics across all countries/regions for executive visibility. This service handles:
- Global revenue tracking and aggregation
- Country-level sales rollup
- Sales performance metrics globally
- Regional sales comparison
- Multi-currency revenue consolidation

---

## 📋 DOMAIN MODEL SPECIFICATION

### Entity 1: GlobalSalesMetric
```java
@Document(collection = "global_sales_metrics")
public class GlobalSalesMetric extends BaseEntity {
    private String metricName;             // e.g., "Total Revenue", "New Customers"
    private String metricCategory;         // REVENUE, CUSTOMERS, ORDERS, FORECAST
    private String executiveLevel;         // CSO, CEO, CFO
    private Double value;                  // Current aggregated value
    private Double previousValue;          // Previous period
    private Double targetValue;            // Sales target
    private String period;                 // "2024-01", "Q1-2024"
    private MetricTrend trend;             // UP, DOWN, STABLE
    private String currency;               // Base currency (USD)
    private Map<String, RegionalSalesMetric> regionalBreakdown;
    private Map<String, Object> metadata;

    // Business logic
    public boolean isOnTrack();
    public double getAchievementPercentage();
    public MetricTrend calculateTrend();
}
```

### Entity 2: RegionalSalesMetric
```java
@Document(collection = "regional_sales_metrics")
public class RegionalSalesMetric extends BaseEntity {
    private String regionCode;             // EUROPE, AFRICA, ASIA, AMERICAS
    private String regionName;
    private String metricName;
    private Double value;
    private Double targetValue;
    private String currency;
    private Double achievementPercent;
    private String period;
    private List<CountrySalesMetric> countries;

    // Business logic
    public void addCountryMetric(CountrySalesMetric metric);
}
```

### Entity 3: CountrySalesMetric
```java
@Document(collection = "country_sales_metrics")
public class CountrySalesMetric extends BaseEntity {
    private String countryCode;            // ISO country code
    private String countryName;
    private String metricName;
    private Double revenue;
    private Double ordersCount;
    private Integer newCustomers;
    private String period;
    private String currency;
    private Double conversionRateToUSD;   // For consolidation
    private Double revenueUSD;            // Converted value

    // Business logic
    public Double getRevenueInBaseCurrency();
    public boolean isTargetAchieved();
}
```

---

## 🔌 API ENDPOINTS REQUIRED

### Global Metrics (18 endpoints)

#### GET /api/v1/global-sales-metrics
- Get all global sales metrics

#### GET /api/v1/global-sales-metrics/{category}
- Get metrics by category (REVENUE, CUSTOMERS, etc.)

#### GET /api/v1/global-sales-metrics/period/{period}
- Get metrics for specific period

#### GET /api/v1/global-sales-metrics/trend
- Get metric trends over time

#### POST /api/v1/global-sales-metrics/refresh
- Trigger aggregation refresh

### Regional Breakdown (10 endpoints)

#### GET /api/v1/regional-sales-metrics
- Get all regional metrics

#### GET /api/v1/regional-sales-metrics/{regionCode}
- Get metrics for specific region

#### GET /api/v1/regional-sales-metrics/top/{limit}
- Get top performing regions

### Revenue Tracking (12 endpoints)

#### GET /api/v1/revenue/global
- Get global revenue

#### GET /api/v1/revenue/by-region/{regionCode}
- Get revenue by region

#### GET /api/v1/revenue/by-country/{countryCode}
- Get revenue by country

#### GET /api/v1/revenue/consolidated
- Get consolidated revenue (all currencies to USD)

### Dashboard (8 endpoints)

#### GET /api/v1/dashboard/cso
- CSO dashboard (all sales metrics)

#### GET /api/v1/dashboard/ceo
- CEO dashboard (sales summary)

#### GET /api/v1/dashboard/forecast
- Sales forecast dashboard

---

## ✅ ACCEPTANCE CRITERIA

1. **Currency Conversion:**
   - Convert all country revenues to USD
   - Support multiple base currencies
   - Up-to-date exchange rates

2. **Aggregation:**
   - Sum of countries = regional
   - Sum of regions = global

3. **Multi-Tenancy:**
   - Each tenant's sales data isolated

4. **Performance:**
   - Fast aggregation (<5 seconds)
   - Cached results

---

## 🔧 CONFIGURATION

```yaml
spring:
  data:
    mongodb:
      database: management_sales
  redis:
    host: localhost
    port: 6379

# Sales Dashboard specific
sales:
  base-currency: USD
  supported-currencies: USD,EUR,GBP,NGN,KES,ZAR,CAD,AUD
  aggregation-cache-minutes: 15
```

---

## 📁 FILE STRUCTURE TO CREATE

```
global-sales-dashboard-service/
├── pom.xml
├── src/main/java/
│   ├── GlobalSalesDashboardServiceApplication.java
│   ├── domain/
│   │   ├── model/
│   │   │   ├── BaseEntity.java
│   │   │   ├── GlobalSalesMetric.java
│   │   │   ├── RegionalSalesMetric.java
│   │   │   ├── CountrySalesMetric.java
│   │   │   └── SalesForecast.java
│   │   ├── repository/
│   │   │   ├── BaseRepository.java
│   │   │   ├── GlobalSalesMetricRepository.java
│   │   │   ├── RegionalSalesMetricRepository.java
│   │   │   └── CountrySalesMetricRepository.java
│   │   └── enums/
│   │       ├── MetricCategory.java
│   │       └── MetricTrend.java
│   ├── application/
│   │   ├── command/
│   │   │   └── SalesAggregationService.java
│   │   ├── query/
│   │   │   ├── GlobalSalesQueryService.java
│   │   │   └── CurrencyConversionService.java
│   │   └── dto/
│   │       └── DashboardDTO.java
│   ├── infrastructure/
│   │   ├── config/
│   │   │   ├── MongoDBConfig.java
│   │   │   ├── RedisConfig.java
│   │   │   └── SecurityConfig.java
│   │   └── security/
│   │       └── TenantContextFilter.java
│   ├── interfaces/
│   │   └── rest/
│   │       ├── GlobalSalesController.java
│   │       ├── RegionalSalesController.java
│   │       └── DashboardController.java
│   └── shared/
│       └── requestcontext/
│           ├── RequestContext.java
│           └── RequestContextHolder.java
└── src/main/resources/
    └── application.yml
```

---

## 🧪 TESTS REQUIRED

- GlobalSalesMetricTest (15+ tests)
- RegionalSalesMetricTest (12+ tests)
- SalesAggregationServiceTest (15+ tests)
- CurrencyConversionServiceTest (12+ tests)
- DashboardControllerIntegrationTest (13+ tests)
- TenantIsolationTest (CRITICAL)
- HexagonalArchitectureTest (15 rules)

---

**Gold Standard Reference:** `../../Executive-domain/Backend/Java/executive-dashboard-service/Shared/executive-analytics-service`
**Base Path:** `Sales-Departments/Backend/Java/sales-service/Global/global-sales-dashboard-service`

**READ THESE BEFORE STARTING:**
1. AGENT_INSTRUCTIONS.md
2. PRD_SALES_DEPARTMENTS.md
3. Gold-standard service (executive-analytics-service)
