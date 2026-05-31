# Test Coverage Report - global-sales-dashboard-service

## Overview
- **Service**: global-sales-dashboard-service
- **Sub-Domain**: Sales-department
- **Analysis Date**: 2026-03-06T10:24:22Z
- **Total Source Files**: 60
- **Total Test Files**: 5
- **Source Classes (significant)**: 43
- **Test Classes**: 4
- **Test Methods**: 42
- **Test Coverage**: 9%

## Implementation Gaps Summary
- **Total Gaps**: 0
- **Files with Gaps**: 0
- **TODO**: 0
- **FIXME**: 0
- **XXX**: 0
- **HACK**: 0
- **STUB**: 0
- **PLACEHOLDER**: 0
- **NotImplementedError**: 0
- **UnsupportedOperationException**: 0

## Test Classes Details

### DashboardCommandServiceTest
- **Path**: `src/test/java/com/gogidix/sales/dashboard/application/service/DashboardCommandServiceTest.java`
- **Test Methods**: 6
- **Method Names**: testArchiveDashboard, testCreateDashboard, testDeleteDashboard, testPublishDashboard, testRefreshDashboard, testUpdateGlobalMetrics

### DashboardQueryServiceTest
- **Path**: `src/test/java/com/gogidix/sales/dashboard/application/service/DashboardQueryServiceTest.java`
- **Test Methods**: 9
- **Method Names**: testCountByStatus, testCountByTenant, testGetAllDashboardsDto, testGetById, testGetByIdNotFound, testGetByStatus, testGetByType, testGetTopPerformingRegions, testGetTrendData

### GlobalSalesDashboardTest
- **Path**: `src/test/java/com/gogidix/sales/dashboard/domain/model/GlobalSalesDashboardTest.java`
- **Test Methods**: 15
- **Method Names**: testAddTrendData, testAddWidget, testArchive, testClearDomainEvents, testConvertSameCurrency, testConvertToBaseCurrency, testCreateDashboard, testGetRegionalMetric, testGetTopPerformingRegions, testPublish, testPublishFailsForNonDraft, testRefresh, testUpdateExecutiveSummary, testUpdateGlobalMetrics, testUpdateRegionalMetric

### CurrencyConversionTest
- **Path**: `src/test/java/com/gogidix/sales/dashboard/domain/valueobject/CurrencyConversionTest.java`
- **Test Methods**: 11
- **Method Names**: testConvertSameCurrency, testConvertToBase, testConvertToDifferentCurrency, testCreateCurrencyConversion, testGetExchangeRate, testGetExchangeRateSameCurrency, testGetSupportedCurrencies, testIsValidCurrency, testMoneyOf, testMoneyZero, testUpdateRate

### GlobalSalesDashboardApplicationTests
- **Path**: `src/test/java/com/gogidix/sales/dashboard/GlobalSalesDashboardApplicationTests.java`
- **Test Methods**: 1

## Source Classes (Significant)

- `AggregationCommandService` - **Tested**: No
- `AggregationQueryService` - **Tested**: No
- `DashboardCommandService` - **Tested**: Yes
- `DashboardQueryService` - **Tested**: Yes
- `RollupCommandService` - **Tested**: No
- `RollupQueryService` - **Tested**: No
- `WidgetCommandService` - **Tested**: No
- `WidgetQueryService` - **Tested**: No
- `AggregationCompletedEvent` - **Tested**: No
- `DashboardRefreshedEvent` - **Tested**: No
- `MetricUpdatedEvent` - **Tested**: No
- `RollupCompletedEvent` - **Tested**: No
- `WidgetUpdatedEvent` - **Tested**: No
- `BaseEntity` - **Tested**: No
- `GlobalSalesDashboard` - **Tested**: Yes
- `KPIWidget` - **Tested**: No
- `MetricRollup` - **Tested**: No
- `SalesAggregation` - **Tested**: No
- `DashboardCommand` - **Tested**: No
- `DashboardQuery` - **Tested**: No
- `EventPublisher` - **Tested**: No
- `GlobalSalesDashboardRepository` - **Tested**: No
- `KPIWidgetRepository` - **Tested**: No
- `MetricRollupRepository` - **Tested**: No
- `SalesAggregationRepository` - **Tested**: No
- `GlobalSalesDashboardApplication` - **Tested**: No
- `CurrencyConverter` - **Tested**: No
- `KafkaEventPublisher` - **Tested**: No
- `MongoGlobalSalesDashboardRepository` - **Tested**: No
- `MongoKPIWidgetRepository` - **Tested**: No
- `MongoMetricRollupRepository` - **Tested**: No
- `MongoSalesAggregationRepository` - **Tested**: No
- `DashboardRefreshScheduler` - **Tested**: No
- `SecurityConfig` - **Tested**: No
- `TenantInterceptor` - **Tested**: No
- `AggregationController` - **Tested**: No
- `DashboardController` - **Tested**: No
- `GlobalExceptionHandler` - **Tested**: No
- `RollupController` - **Tested**: No
- `WidgetController` - **Tested**: No
- `BaseEntity` - **Tested**: No
- `RequestContext` - **Tested**: No
- `RequestContextHolder` - **Tested**: No
