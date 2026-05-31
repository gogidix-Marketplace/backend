# AGENT F1 - GLOBAL FINANCE DASHBOARD SERVICE

**Agent ID:** F1
**Domain:** Finance-Department
**Service:** global-finance-dashboard-service
**Priority:** P0 (CRITICAL)

---

## 🎯 SERVICE PURPOSE

Implement a global finance dashboard service that aggregates financial metrics across all countries/regions for CFO and executive visibility. This service handles:
- Global revenue aggregation
- Multi-currency consolidation
- Expense tracking globally
- Cash flow visibility
- Financial health metrics

---

## 📋 DOMAIN MODEL SPECIFICATION

### Entity 1: GlobalFinancialMetric
```java
@Document(collection = "global_financial_metrics")
public class GlobalFinancialMetric extends BaseEntity {
    private String metricName;             // e.g., "Total Revenue", "Net Income"
    private String metricCategory;         // REVENUE, EXPENSE, PROFIT, CASHFLOW
    private String executiveLevel;         // CFO, CEO, BOARD
    private Double value;                  // Current value (USD)
    private Double valueLocalCurrency;    // Original currency value
    private String currency;               // Original currency
    private Double previousValue;          // Previous period
    private Double targetValue;            // Budget/target
    private String period;                 // "2024-01", "Q1-2024"
    private MetricTrend trend;             // UP, DOWN, STABLE
    private Map<String, RegionalFinancialMetric> regionalBreakdown;
    private Map<String, Object> metadata;

    // Business logic
    public boolean isOnTrack();
    public double getVariancePercent();
    public double getAchievementPercent();
}
```

### Entity 2: RegionalFinancialMetric
```java
@Document(collection = "regional_financial_metrics")
public class RegionalFinancialMetric extends BaseEntity {
    private String regionCode;             // EUROPE, AFRICA, ASIA, AMERICAS
    private String regionName;
    private String metricName;
    private Double valueUSD;               // Converted to USD
    private Double valueLocal;             // Original currency value
    private String localCurrency;
    private Double exchangeRate;           // Rate used for conversion
    private Double targetValue;
    private String period;
    private List<CountryFinancialMetric> countries;

    // Business logic
    public Double convertToUSD(Double localValue, String currency);
}
```

### Entity 3: CountryFinancialMetric
```java
@Document(collection = "country_financial_metrics")
public class CountryFinancialMetric extends BaseEntity {
    private String countryCode;            // ISO country code
    private String countryName;
    private String metricName;
    private Double revenue;
    private Double expenses;
    private Double profit;
    private String currency;
    private Double exchangeRateToUSD;
    private Double revenueUSD;
    private Double profitMargin;
    private String period;

    // Business logic
    public Double calculateProfitMargin();
    public Double getRevenueInUSD();
}
```

---

## 🔌 API ENDPOINTS REQUIRED

### Global Metrics (18 endpoints)

#### GET /api/v1/global-financial-metrics
- Get all global financial metrics

#### GET /api/v1/global-financial-metrics/{category}
- Get metrics by category (REVENUE, EXPENSE, etc.)

#### GET /api/v1/global-financial-metrics/period/{period}
- Get metrics for period

#### GET /api/v1/global-financial-metrics/consolidated
- Get consolidated metrics (multi-currency to USD)

#### POST /api/v1/global-financial-metrics/refresh
- Trigger aggregation

### Regional Breakdown (10 endpoints)

#### GET /api/v1/regional-financial-metrics
- Get all regional metrics

#### GET /api/v1/regional-financial-metrics/{regionCode}
- Get metrics for specific region

#### GET /api/v1/regional-financial-metrics/top/{limit}
- Get top performing regions

### Revenue & Expenses (12 endpoints)

#### GET /api/v1/revenue/global
- Get global revenue

#### GET /api/v1/revenue/by-region/{regionCode}
- Get revenue by region

#### GET /api/v1/expenses/global
- Get global expenses

#### GET /api/v1/profit/global
- Get global profit

#### GET /api/v1/profit/by-country/{countryCode}
- Get profit by country

### Dashboard (8 endpoints)

#### GET /api/v1/dashboard/cfo
- CFO dashboard (all financial metrics)

#### GET /api/v1/dashboard/cashflow
- Cash flow dashboard

#### GET /api/v1/dashboard/budget-vs-actual
- Budget vs actual comparison

---

## ✅ ACCEPTANCE CRITERIA

1. **Multi-Currency:**
   - Convert all to USD for global view
   - Support 10+ currencies
   - Accurate exchange rates

2. **Aggregation:**
   - Countries → Regions → Global
   - Sums match at each level

3. **Multi-Tenancy:**
   - Each tenant's financial data isolated

4. **Accuracy:**
   - Penny-accurate calculations
   - Proper rounding for currency

---

## 🔧 CONFIGURATION

```yaml
spring:
  data:
    mongodb:
      database: management_finance
  redis:
    host: localhost
    port: 6379

# Finance Dashboard specific
finance:
  base-currency: USD
  supported-currencies: USD,EUR,GBP,NGN,KES,ZAR,CAD,AUD,INR,JPY
  aggregation-cache-minutes: 30
```

---

## 📁 FILE STRUCTURE TO CREATE

```
global-finance-dashboard-service/
├── pom.xml
├── src/main/java/
│   ├── GlobalFinanceDashboardServiceApplication.java
│   ├── domain/
│   │   ├── model/
│   │   │   ├── BaseEntity.java
│   │   │   ├── GlobalFinancialMetric.java
│   │   │   ├── RegionalFinancialMetric.java
│   │   │   ├── CountryFinancialMetric.java
│   │   │   └── CashFlowMetric.java
│   │   ├── repository/
│   │   │   ├── BaseRepository.java
│   │   │   ├── GlobalFinancialMetricRepository.java
│   │   │   ├── RegionalFinancialMetricRepository.java
│   │   │   └── CountryFinancialMetricRepository.java
│   │   └── enums/
│   │       ├── MetricCategory.java
│   │       └── MetricTrend.java
│   ├── application/
│   │   ├── command/
│   │   │   └── FinancialAggregationService.java
│   │   ├── query/
│   │   │   ├── GlobalFinanceQueryService.java
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
│   │       ├── GlobalFinanceController.java
│   │       ├── RegionalFinanceController.java
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

- GlobalFinancialMetricTest (15+ tests)
- RegionalFinancialMetricTest (12+ tests)
- FinancialAggregationServiceTest (15+ tests)
- CurrencyConversionServiceTest (12+ tests)
- DashboardControllerIntegrationTest (13+ tests)
- TenantIsolationTest (CRITICAL)
- HexagonalArchitectureTest (15 rules)

---

**Gold Standard Reference:** `../../Executive-domain/Backend/Java/executive-dashboard-service/Shared/executive-analytics-service`
**Base Path:** `Finance-department/Backend/Java/finance-service/Dashboard/global-finance-dashboard-service`

**READ THESE BEFORE STARTING:**
1. AGENT_INSTRUCTIONS.md
2. PRD_FINANCE_DEPARTMENT.md
3. Gold-standard service (executive-analytics-service)
