# AGENT HR1 - GLOBAL HR DASHBOARD SERVICE

**Agent ID:** HR1
**Domain:** Human-Resource
**Service:** global-hr-dashboard-service
**Priority:** P0 (CRITICAL)

---

## 🎯 SERVICE PURPOSE

Implement a global HR dashboard service that aggregates workforce metrics across all countries/regions for executive visibility. This service handles:
- Global workforce metrics aggregation
- Country-level HR data rollup
- Headcount tracking globally
- Global compliance monitoring
- Multi-region HR analytics

---

## 📋 DOMAIN MODEL SPECIFICATION

### Entity 1: GlobalWorkforceMetric
```java
@Document(collection = "global_workforce_metrics")
public class GlobalWorkforceMetric extends BaseEntity {
    private String metricName;             // e.g., "Total Headcount", "Turnover Rate"
    private String metricCategory;         // HEADCOUNT, RETENTION, COMPLIANCE, PERFORMANCE
    private String executiveLevel;         // CHRO, CEO, CFO
    private Double value;                  // Current aggregated value
    private Double previousValue;          // Previous period value
    private Double targetValue;            // Target/KPI value
    private String period;                 // e.g., "2024-01", "Q1-2024"
    private MetricTrend trend;             // UP, DOWN, STABLE
    private String aggregationLevel;      // GLOBAL, REGION, COUNTRY
    private Map<String, RegionalMetric> regionalBreakdown;  // By region
    private Map<String, Object> metadata;

    // Business logic
    public boolean isOnTrack();
    public double getVariance();
    public MetricTrend calculateTrend();
    public void aggregateRegionalMetrics();
}
```

### Entity 2: RegionalMetric
```java
@Document(collection = "regional_metrics")
public class RegionalMetric extends BaseEntity {
    private String regionCode;             // e.g., "EUROPE", "AFRICA", "ASIA"
    private String regionName;
    private String metricName;
    private Double value;
    private String period;
    private List<CountryMetric> countries;  // Countries in region
    private Instant lastUpdated;

    // Business logic
    public void addCountryMetric(CountryMetric metric);
    public Double aggregateValue();
}
```

### Entity 3: CountryHeadcount
```java
@Document(collection = "country_headcount")
public class CountryHeadcount extends BaseEntity {
    private String countryCode;            // ISO country code
    private String countryName;
    private Integer totalHeadcount;
    private Integer permanentEmployees;
    private Integer contractors;
    private Integer interns;
    private String department;             // HR, SALES, ENGINEERING, etc.
    private String period;                 // "2024-01"
    private Integer yoyChange;             // Year-over-year change
    private Integer momChange;             // Month-over-month change

    // Business logic
    public Integer getTotalHeadcount();
    public Double getContractorPercentage();
    public boolean isGrowing();
}
```

---

## 🔌 API ENDPOINTS REQUIRED

### Global Metrics (20 endpoints)

#### GET /api/v1/global-metrics
- Get all global workforce metrics

#### GET /api/v1/global-metrics/{category}
- Get metrics by category (HEADCOUNT, RETENTION, etc.)

#### GET /api/v1/global-metrics/by-level/{executiveLevel}
- Get metrics for specific executive level

#### GET /api/v1/global-metrics/period/{period}
- Get metrics for specific period

#### GET /api/v1/global-metrics/aggregate
- Trigger manual aggregation

### Regional Breakdown (12 endpoints)

#### GET /api/v1/regional-metrics
- Get all regional metrics

#### GET /api/v1/regional-metrics/{regionCode}
- Get metrics for specific region

#### GET /api/v1/regional-metrics/{regionCode}/countries
- Get country breakdown for region

### Headcount Tracking (10 endpoints)

#### GET /api/v1/headcount/global
- Get global headcount

#### GET /api/v1/headcount/by-country/{countryCode}
- Get headcount by country

#### GET /api/v1/headcount/by-department/{department}
- Get headcount by department

#### GET /api/v1/headcount/trend
- Get headcount trend over time

### Dashboard (8 endpoints)

#### GET /api/v1/dashboard/chro
- CHRO dashboard (all HR metrics)

#### GET /api/v1/dashboard/ceo
- CEO dashboard (workforce summary)

#### GET /api/v1/dashboard/compliance
- Global compliance dashboard

#### POST /api/v1/dashboard/refresh
- Refresh all dashboard data

---

## ✅ ACCEPTANCE CRITERIA

1. **Aggregation:**
   - Correctly aggregate from country to region to global
   - Handle multiple currencies for compensation metrics
   - Real-time aggregation capability

2. **Multi-Tenancy:**
   - Each tenant has separate HR data
   - No cross-tenant data leakage

3. **Performance:**
   - Fast aggregation (<5 seconds for global)
   - Cached results for frequently accessed data

4. **Accuracy:**
   - Sum of country metrics equals regional
   - Sum of regional equals global

---

## 🔧 CONFIGURATION

```yaml
spring:
  data:
    mongodb:
      database: management_hr
  redis:
    host: localhost
    port: 6379

# HR Dashboard specific
hr:
  aggregation-cache-minutes: 15
  supported-regions: EUROPE,AFRICA,ASIA,NORTH_AMERICA,SOUTH_AMERICA
```

---

## 📁 FILE STRUCTURE TO CREATE

```
global-hr-dashboard-service/
├── pom.xml
├── src/main/java/
│   ├── GlobalHRDashboardServiceApplication.java
│   ├── domain/
│   │   ├── model/
│   │   │   ├── BaseEntity.java
│   │   │   ├── GlobalWorkforceMetric.java
│   │   │   ├── RegionalMetric.java
│   │   │   ├── CountryHeadcount.java
│   │   │   └── ComplianceMetric.java
│   │   ├── repository/
│   │   │   ├── BaseRepository.java
│   │   │   ├── GlobalWorkforceMetricRepository.java
│   │   │   ├── RegionalMetricRepository.java
│   │   │   └── CountryHeadcountRepository.java
│   │   └── enums/
│   │       ├── MetricCategory.java
│   │       └── MetricTrend.java
│   ├── application/
│   │   ├── command/
│   │   │   └── MetricAggregationService.java
│   │   ├── query/
│   │   │   ├── GlobalMetricQueryService.java
│   │   │   └── RegionalMetricQueryService.java
│   │   └── dto/
│   │       ├── DashboardDTO.java
│   │       └── MetricDTO.java
│   ├── infrastructure/
│   │   ├── config/
│   │   │   ├── MongoDBConfig.java
│   │   │   ├── RedisConfig.java
│   │   │   └── SecurityConfig.java
│   │   └── security/
│   │       └── TenantContextFilter.java
│   ├── interfaces/
│   │   └── rest/
│   │       ├── GlobalMetricController.java
│   │       ├── RegionalMetricController.java
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

- GlobalWorkforceMetricTest (15+ tests)
- RegionalMetricTest (12+ tests)
- CountryHeadcountTest (12+ tests)
- MetricAggregationServiceTest (15+ tests)
- DashboardControllerIntegrationTest (13+ tests)
- TenantIsolationTest (CRITICAL)
- HexagonalArchitectureTest (15 rules)

---

**Gold Standard Reference:** `../Executive-domain/Backend/Java/executive-dashboard-service/Shared/executive-analytics-service`
**Base Path:** `Human-resource/Backend/Java/human-resource-service/Global/global-hr-dashboard-service`

**READ THESE BEFORE STARTING:**
1. AGENT_INSTRUCTIONS.md
2. PRD_HUMAN_RESOURCE.md
3. Gold-standard service (executive-analytics-service)
