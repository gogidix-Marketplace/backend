# Sales Analytics Service

A comprehensive sales analytics service for the Sales Department, built with **Java 17** and **Spring Boot 3.1.5** following **Hexagonal Architecture** (Ports and Adapters pattern).

## Features

### Core Analytics Capabilities
- **Sales Performance Analytics**: Track revenue, quota achievement, and deal metrics
- **Team and Individual Metrics**: Performance tracking for sales reps and teams
- **Conversion Rate Analysis**: Lead-to-opportunity and opportunity-to-win rates
- **Sales Cycle Analytics**: Deal duration, stage velocity, and response times
- **Pipeline Velocity Tracking**: Pipeline health, coverage, and movement metrics
- **Win/Loss Analysis**: Detailed win/loss reasons, competitor analysis, and trends
- **Revenue Forecasting**: Predictive analytics and forecast accuracy tracking
- **Custom Report Generation**: On-demand reports in PDF, Excel, CSV, JSON formats
- **Dashboard Widgets**: Configurable, real-time dashboard components
- **Multi-Tenancy**: Full tenant isolation with context propagation

## Architecture

```
sales-analytics-service/
├── domain/                          # Core Business Logic
│   ├── model/                       # Domain Entities
│   │   ├── Metric.java              # General metric entity
│   │   ├── PerformanceMetric.java   # Sales performance metrics
│   │   ├── SalesCycleMetric.java    # Sales cycle metrics
│   │   ├── PipelineMetric.java      # Pipeline metrics
│   │   ├── WinLossMetric.java       # Win/loss analysis metrics
│   │   ├── AnalyticsReport.java     # Report entity
│   │   └── DashboardWidget.java     # Dashboard widget entity
│   ├── event/                       # Domain Events
│   │   ├── MetricUpdatedEvent.java
│   │   ├── ReportGeneratedEvent.java
│   │   ├── SalesPerformanceUpdatedEvent.java
│   │   ├── ReportScheduledEvent.java
│   │   ├── PipelineUpdatedEvent.java
│   │   └── WinLossAnalysisEvent.java
│   └── repository/                  # Repository Ports (Interfaces)
│       ├── MetricRepository.java
│       ├── PerformanceMetricRepository.java
│       ├── SalesCycleMetricRepository.java
│       ├── PipelineMetricRepository.java
│       ├── WinLossMetricRepository.java
│       ├── AnalyticsReportRepository.java
│       └── DashboardWidgetRepository.java
├── application/                     # Application Services
│   └── service/
│       ├── MetricsCommandService.java    # Write operations
│       ├── MetricsQueryService.java      # Read operations
│       ├── ReportService.java            # Report generation
│       └── DashboardService.java         # Dashboard management
├── infrastructure/                  # External Dependencies
│   ├── config/
│   │   ├── MongoDBConfig.java           # MongoDB configuration
│   │   └── WebConfig.java               # Web MVC configuration
│   ├── messaging/
│   │   ├── KafkaConfig.java             # Kafka configuration
│   │   └── KafkaEventPublisher.java     # Event publishing
│   ├── persistence/
│   │   └── mongo/                       # MongoDB Adapters
│   │       ├── MongoMetricRepository.java
│   │       ├── MongoPerformanceMetricRepository.java
│   │       ├── MongoSalesCycleMetricRepository.java
│   │       ├── MongoPipelineMetricRepository.java
│   │       ├── MongoWinLossMetricRepository.java
│   │       ├── MongoAnalyticsReportRepository.java
│   │       └── MongoDashboardWidgetRepository.java
│   └── security/
│       └── TenantInterceptor.java       # Multi-tenancy interceptor
├── interfaces/                       # External Interfaces
│   └── rest/
│       ├── MetricsController.java       # Metrics API
│       ├── ReportsController.java       # Reports API
│       ├── DashboardController.java     # Dashboard API
│       └── GlobalExceptionHandler.java  # Error handling
└── shared/                          # Shared Utilities
    ├── base/
    │   └── BaseEntity.java             # Base entity with auditing
    ├── requestcontext/
    │   ├── RequestContext.java          # Request context DTO
    │   └── RequestContextHolder.java    # Thread-local context holder
    └── exception/
        ├── NotFoundException.java
        ├── ValidationException.java
        └── ConflictException.java
```

## API Endpoints

### Metrics
- `POST /api/v1/metrics` - Create a new metric
- `GET /api/v1/metrics/{metricId}` - Get metric by ID
- `GET /api/v1/metrics/entity/{entityType}/{entityId}` - Get metrics by entity
- `PUT /api/v1/metrics/{metricId}/value` - Update metric value
- `DELETE /api/v1/metrics/{metricId}` - Delete metric

### Performance Metrics
- `POST /api/v1/metrics/performance` - Create performance metric
- `GET /api/v1/metrics/performance/{metricId}` - Get performance metric
- `GET /api/v1/metrics/performance/top/{entityType}` - Get top performers
- `PUT /api/v1/metrics/performance/{metricId}/revenue` - Update revenue metrics
- `PUT /api/v1/metrics/performance/{metricId}/deals` - Update deal metrics

### Pipeline Metrics
- `POST /api/v1/metrics/pipeline` - Create pipeline metric
- `GET /api/v1/metrics/pipeline/at-risk` - Get at-risk pipelines
- `PUT /api/v1/metrics/pipeline/{metricId}/value` - Update pipeline value
- `PUT /api/v1/metrics/pipeline/{metricId}/health` - Update health score

### Win/Loss Metrics
- `POST /api/v1/metrics/win-loss` - Create win/loss metric
- `GET /api/v1/metrics/win-loss/summary` - Get win/loss summary
- `PUT /api/v1/metrics/win-loss/{metricId}/counts` - Update deal counts

### Reports
- `POST /api/v1/reports` - Create new report
- `GET /api/v1/reports/{reportId}` - Get report by ID
- `GET /api/v1/reports/status/{status}` - Get reports by status
- `POST /api/v1/reports/{reportId}/share` - Share report

### Dashboard
- `POST /api/v1/dashboards/{dashboardId}/widgets` - Create widget
- `GET /api/v1/dashboards/{dashboardId}/widgets` - Get dashboard widgets
- `PUT /api/v1/dashboards/widgets/{widgetId}/data` - Update widget data
- `POST /api/v1/dashboards/widgets/{widgetId}/refresh` - Refresh widget

## Configuration

### Required Headers
All requests must include:
- `X-Tenant-ID`: The tenant identifier (required)
- `X-User-ID`: The user identifier (optional)
- `X-Correlation-ID`: Correlation ID for tracing (auto-generated if not provided)

### Environment Variables
- `SERVICE_PORT`: Server port (default: 8090)
- `MONGODB_HOST`: MongoDB host (default: localhost)
- `MONGODB_PORT`: MongoDB port (default: 27017)
- `MONGODB_DB`: Database name (default: sales-analytics-service_db)
- `KAFKA_SERVERS`: Kafka bootstrap servers (default: localhost:9092)

## Building and Running

### Build
```bash
mvn clean package
```

### Run
```bash
mvn spring-boot:run
```

### Run Tests
```bash
mvn test
```

## Domain Events

The service publishes the following domain events to Kafka:

1. **MetricUpdatedEvent**: Published when a metric is created or updated
2. **ReportGeneratedEvent**: Published when a report is generated
3. **SalesPerformanceUpdatedEvent**: Published when performance metrics change
4. **ReportScheduledEvent**: Published when a report is scheduled
5. **PipelineUpdatedEvent**: Published when pipeline metrics change
6. **WinLossAnalysisEvent**: Published when win/loss analysis completes

## License

Copyright (c) Gogidix Ecosystem. All rights reserved.
