# Sales Analytics Service - Architecture Documentation

## Overview

The Sales Analytics Service provides comprehensive analytics and reporting capabilities for the Sales Department, including dashboards, metrics calculation, and performance tracking.

## Architecture Principles

The service follows **Hexagonal Architecture** (Ports and Adapters) with clear separation of concerns:

1. **Domain Layer**: Core analytics entities and metrics
2. **Application Layer**: Analytics calculation and report generation
3. **Infrastructure Layer**: MongoDB storage, Kafka event consumption
4. **Interface Layer**: REST API for dashboard data

## Domain Model

### Metric Entity

Base metric entity with:
- `metricId`: Unique identifier
- `tenantId`: Multi-tenant isolation
- `metricName`: Name of the metric
- `metricType`: Type (COUNTER, GAUGE, HISTOGRAM)
- `value`: Metric value
- `timestamp`: When metric was recorded

### PerformanceMetric Entity

Sales performance metrics:
- `userId`: Sales representative
- `period`: Time period (DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY)
- `revenueGenerated`: Total revenue
- `dealsWon`: Number of won deals
- `dealsLost`: Number of lost deals
- `dealsClosed`: Total closed deals
- `winRate`: Win rate percentage
- `averageDealSize`: Average deal value
- `pipelineGenerated`: Pipeline value created
- `activitiesCompleted`: Number of activities

### PipelineMetric Entity

Pipeline analysis metrics:
- `stage`: Deal stage
- `dealCount`: Number of deals in stage
- `totalValue`: Total amount in stage
- `averageValue`: Average deal value
- `agedDeals`: Number of stalled deals
- `conversionRate`: Stage conversion rate

### WinLossMetric Entity

Win/loss analysis:
- `period`: Analysis period
- `reason`: Loss reason
- `competitor`: Competitor if applicable
- `dealCount`: Number of deals
- `totalValue`: Total value of deals
- `percentage`: Percentage of total

### AnalyticsReport Entity

Report definitions:
- `reportId`: Unique identifier
- `reportName`: Report name
- `reportType`: Type (PIPELINE, PERFORMANCE, FORECAST, ACTIVITY)
- `schedule`: Report schedule (DAILY, WEEKLY, MONTHLY)
- `recipients`: Report recipients
- `filters`: Report filters
- `lastGenerated`: Last generation timestamp
- `nextScheduled`: Next scheduled generation

## Application Services

### DashboardService

- `getDashboardData()`: Get widget data for dashboard
- `getPersonalizedDashboard()`: User-specific dashboard
- `getTeamDashboard()`: Team performance dashboard

### MetricsCommandService

- `recordMetric()`: Record a new metric
- `batchRecordMetrics()`: Record multiple metrics
- `calculatePerformanceMetrics()`: Calculate performance metrics
- `calculatePipelineMetrics()`: Calculate pipeline metrics
- `calculateWinLossMetrics()`: Calculate win/loss analysis

### MetricsQueryService

- `getMetrics()`: Get metrics by type and period
- `getPerformanceTrend()`: Get performance trend over time
- `getPipelineSnapshot()`: Get current pipeline snapshot
- `getWinLossAnalysis()`: Get win/loss analysis

### ReportService

- `generateReport()`: Generate report on demand
- `scheduleReport()`: Schedule recurring report
- `getReportHistory()`: Get historical reports
- `getAvailableReports()`: List available report types

## Infrastructure Components

### Persistence Layer

**MongoDB Collections:**
- `metrics`: Base metrics
- `performance_metrics`: Sales performance data
- `pipeline_metrics`: Pipeline analysis
- `win_loss_metrics`: Win/loss analysis
- `analytics_reports`: Report definitions
- `dashboard_widgets`: Dashboard configurations

### Event Processing

**Kafka Events Consumed:**
- Deal events for pipeline updates
- Lead events for conversion tracking
- Activity events for engagement metrics

## Technology Stack

- **Language**: Java 17
- **Framework**: Spring Boot 3.1.5
- **Database**: MongoDB
- **Messaging**: Apache Kafka
- **API Documentation**: SpringDoc OpenAPI
- **Testing**: JUnit 5, Mockito
