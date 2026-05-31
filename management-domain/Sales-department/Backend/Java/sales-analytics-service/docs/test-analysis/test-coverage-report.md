# Test Coverage Report - sales-analytics-service

## Overview
- **Service**: sales-analytics-service
- **Sub-Domain**: Sales-department
- **Analysis Date**: 2026-03-06T10:24:22Z
- **Total Source Files**: 49
- **Total Test Files**: 7
- **Source Classes (significant)**: 44
- **Test Classes**: 7
- **Test Methods**: 68
- **Test Coverage**: 15%

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

### SalesAnalyticsServiceTest
- **Path**: `src/test/java/com/gogidix/sales/analytics/application/service/SalesAnalyticsServiceTest.java`
- **Test Methods**: 3

### PerformanceMetricTest
- **Path**: `src/test/java/com/gogidix/sales/analytics/domain/model/PerformanceMetricTest.java`
- **Test Methods**: 7
- **Method Names**: testCalculateAverageDealSize, testCalculateAverageDealSizeWithNoDeals, testCalculateWinRate, testCalculateWinRateWithNoClosedDeals, testCreatePerformanceMetric, testMetricIsComparable, testPeriodTypes

### MetricsQueryServiceIntegrationTest
- **Path**: `src/test/java/com/gogidix/sales/analytics/integration/MetricsQueryServiceIntegrationTest.java`
- **Test Methods**: 9

### MetricTest
- **Path**: `src/test/java/com/gogidix/sales/analytics/unit/domain/MetricTest.java`
- **Test Methods**: 11

### PerformanceMetricTest
- **Path**: `src/test/java/com/gogidix/sales/analytics/unit/domain/PerformanceMetricTest.java`
- **Test Methods**: 11

### PipelineMetricTest
- **Path**: `src/test/java/com/gogidix/sales/analytics/unit/domain/PipelineMetricTest.java`
- **Test Methods**: 12

### WinLossMetricTest
- **Path**: `src/test/java/com/gogidix/sales/analytics/unit/domain/WinLossMetricTest.java`
- **Test Methods**: 15

## Source Classes (Significant)

- `DashboardService` - **Tested**: No
- `MetricsCommandService` - **Tested**: No
- `MetricsQueryService` - **Tested**: No
- `ReportService` - **Tested**: No
- `MetricUpdatedEvent` - **Tested**: No
- `PipelineUpdatedEvent` - **Tested**: No
- `ReportGeneratedEvent` - **Tested**: No
- `ReportScheduledEvent` - **Tested**: No
- `SalesPerformanceUpdatedEvent` - **Tested**: No
- `WinLossAnalysisEvent` - **Tested**: No
- `AnalyticsReport` - **Tested**: No
- `BaseEntity` - **Tested**: No
- `DashboardWidget` - **Tested**: No
- `Metric` - **Tested**: Yes
- `PerformanceMetric` - **Tested**: Yes
- `PipelineMetric` - **Tested**: Yes
- `SalesCycleMetric` - **Tested**: No
- `WinLossMetric` - **Tested**: Yes
- `AnalyticsReportRepository` - **Tested**: No
- `DashboardWidgetRepository` - **Tested**: No
- `MetricRepository` - **Tested**: No
- `PerformanceMetricRepository` - **Tested**: No
- `PipelineMetricRepository` - **Tested**: No
- `SalesCycleMetricRepository` - **Tested**: No
- `WinLossMetricRepository` - **Tested**: No
- `KafkaConfig` - **Tested**: No
- `KafkaEventPublisher` - **Tested**: No
- `MongoAnalyticsReportRepository` - **Tested**: No
- `MongoDashboardWidgetRepository` - **Tested**: No
- `MongoMetricRepository` - **Tested**: No
- `MongoPerformanceMetricRepository` - **Tested**: No
- `MongoPipelineMetricRepository` - **Tested**: No
- `MongoSalesCycleMetricRepository` - **Tested**: No
- `MongoWinLossMetricRepository` - **Tested**: No
- `SecurityConfig` - **Tested**: No
- `TenantInterceptor` - **Tested**: No
- `DashboardController` - **Tested**: No
- `GlobalExceptionHandler` - **Tested**: No
- `MetricsController` - **Tested**: No
- `ReportsController` - **Tested**: No
- `SalesAnalyticsApplication` - **Tested**: No
- `BaseEntity` - **Tested**: No
- `RequestContext` - **Tested**: No
- `RequestContextHolder` - **Tested**: No
