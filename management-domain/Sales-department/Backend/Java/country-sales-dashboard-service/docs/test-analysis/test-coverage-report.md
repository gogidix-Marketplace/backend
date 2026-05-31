# Test Coverage Report - country-sales-dashboard-service

## Overview
- **Service**: country-sales-dashboard-service
- **Sub-Domain**: Sales-department
- **Analysis Date**: 2026-03-06T10:24:22Z
- **Total Source Files**: 41
- **Total Test Files**: 3
- **Source Classes (significant)**: 25
- **Test Classes**: 2
- **Test Methods**: 27
- **Test Coverage**: 8%

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

### CountrySalesDashboardApplicationTests
- **Path**: `src/test/java/com/gogidix/sales/countrydashboard/CountrySalesDashboardApplicationTests.java`
- **Test Methods**: 1

### CountrySalesDashboardTest
- **Path**: `src/test/java/com/gogidix/sales/countrydashboard/domain/model/CountrySalesDashboardTest.java`
- **Test Methods**: 13
- **Method Names**: testAddKPI, testArchive, testCalculateYoYComparison, testConvertToBaseCurrency, testCreateDashboard, testGetTerritory, testGetTopPerformingTerritories, testPublish, testPublishNonDraftThrowsException, testTimePeriodCreation, testUpdateMetrics, testUpdateQuota, testUpdateTerritoryMetric

### MoneyTest
- **Path**: `src/test/java/com/gogidix/sales/countrydashboard/domain/valueobject/MoneyTest.java`
- **Test Methods**: 13
- **Method Names**: testAddDifferentCurrenciesThrowsException, testAddMoney, testConvert, testCreateMoney, testCreateZeroMoney, testDivideMoney, testEquality, testHashCode, testIsGreaterThan, testIsLessThan, testMultiplyMoney, testSubtractMoney, testToString

## Source Classes (Significant)

- `CountryDashboardMapper` - **Tested**: No
- `CountryDashboardCommandService` - **Tested**: No
- `CountryDashboardQueryService` - **Tested**: No
- `CountrySalesDashboardApplication` - **Tested**: No
- `CountryComparisonGeneratedEvent` - **Tested**: No
- `CountryDashboardCreatedEvent` - **Tested**: No
- `CountryMetricUpdatedEvent` - **Tested**: No
- `CountryQuotaAdjustedEvent` - **Tested**: No
- `CurrencyConversionAppliedEvent` - **Tested**: No
- `TerritoryPerformanceUpdatedEvent` - **Tested**: No
- `BaseEntity` - **Tested**: No
- `CountrySalesDashboard` - **Tested**: Yes
- `CountryDashboardCommand` - **Tested**: No
- `CountryDashboardQuery` - **Tested**: No
- `EventPublisher` - **Tested**: No
- `CountrySalesDashboardRepository` - **Tested**: No
- `KafkaEventPublisher` - **Tested**: No
- `MongoCountrySalesDashboardRepository` - **Tested**: No
- `DashboardRefreshScheduler` - **Tested**: No
- `SecurityConfig` - **Tested**: No
- `CountryDashboardController` - **Tested**: No
- `GlobalExceptionHandler` - **Tested**: No
- `BaseEntity` - **Tested**: No
- `RequestContext` - **Tested**: No
- `RequestContextHolder` - **Tested**: No
