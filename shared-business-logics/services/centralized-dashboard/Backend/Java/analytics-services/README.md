# Analytics Services

Analytics subdomain for the centralized dashboard, providing metrics aggregation, business intelligence, and data export capabilities.

## Services

### 1. Metrics Aggregation Service (Port 8901)

Aggregates platform metrics across all services in the ecosystem.

**Features:**
- Real-time metric ingestion
- Time-series aggregations
- Metric alerting with thresholds
- Multi-dimensional metrics
- Tenant isolation

**API Endpoints:**
- `POST /api/v1/metrics/ingest` - Ingest a metric
- `GET /api/v1/metrics` - Query metrics with filters
- `GET /api/v1/metrics/current/{metricName}` - Get current metric value
- `GET /api/v1/metrics/statistics/{metricName}` - Get metric statistics
- `POST /api/v1/metrics/alerts` - Create a metric alert
- `GET /api/v1/metrics/alerts` - Get all alerts

### 2. Business Intelligence Service (Port 8902)

Provides BI reporting, dashboards, and analytics insights.

**Features:**
- Interactive dashboards with customizable widgets
- Scheduled and ad-hoc reports
- Multiple output formats (PDF, Excel, CSV)
- Report notifications
- Dashboard sharing and favorites

**API Endpoints:**
- `POST /api/v1/dashboards` - Create a dashboard
- `GET /api/v1/dashboards/{id}` - Get dashboard details
- `POST /api/v1/dashboards/{id}/widgets` - Add widget to dashboard
- `POST /api/v1/reports` - Create report definition
- `POST /api/v1/reports/{id}/execute` - Execute a report

### 3. Analytics Data Service (Port 8903)

Provides data API for analytics queries and exports.

**Features:**
- Saved query management
- Query execution with parameters
- Async data export (CSV, Excel, JSON, PDF)
- Dataset management
- Export file storage and retrieval

**API Endpoints:**
- `POST /api/v1/data/queries` - Create saved query
- `POST /api/v1/data/execute` - Execute a query
- `POST /api/v1/data/exports` - Create data export
- `GET /api/v1/data/datasets` - Get all datasets

## Architecture

All services follow **Hexagonal Architecture** with:
- **Domain Layer**: Business logic, aggregates, repositories, ports
- **Application Layer**: CQRS command/query handlers, DTOs, mappers
- **Infrastructure Layer**: Database, cache, messaging implementations
- **Interface Layer**: REST controllers, API docs

## Technology Stack

- **Framework**: Spring Boot 3.1.5
- **Language**: Java 17
- **Database**: PostgreSQL 15
- **Cache**: Redis 7
- **Messaging**: Apache Kafka
- **API Docs**: OpenAPI 3.0
- **Build**: Maven 3.9+

## Running Locally

### Prerequisites
- Java 17+
- Maven 3.9+
- Docker & Docker Compose (for infrastructure)

### Start Infrastructure

```bash
docker-compose up -d
```

### Build & Run Services

```bash
# Build all services
mvn clean install

# Run Metrics Aggregation Service
cd metrics-aggregation-service
mvn spring-boot:run

# Run Business Intelligence Service
cd business-intelligence-service
mvn spring-boot:run

# Run Analytics Data Service
cd analytics-data-service
mvn spring-boot:run
```

## API Documentation

Once running, access Swagger UI at:
- Metrics: http://localhost:8901/swagger-ui.html
- BI: http://localhost:8902/swagger-ui.html
- Data: http://localhost:8903/swagger-ui.html

## Database Schema

### Metrics Aggregation
- `metric_data_points` - Raw metric data
- `metric_aggregations` - Pre-aggregated metrics
- `metric_alerts` - Alert definitions

### Business Intelligence
- `dashboards` - Dashboard definitions
- `dashboard_widgets` - Dashboard widgets
- `report_definitions` - Report templates
- `report_executions` - Report execution history

### Analytics Data
- `data_queries` - Saved query definitions
- `data_exports` - Export requests and results
- `analytics_datasets` - Dataset definitions

## Multi-Tenancy

All services support SaaS multi-tenancy:
- Tenant isolation at database level
- Tenant context from request headers
- Tenant-aware caching and queries

## Development

### Run Tests
```bash
mvn test
```

### Code Coverage
```bash
mvn clean test jacoco:report
```

### Build Docker Images
```bash
mvn clean package docker:build
```

## License

Copyright (c) 2025 Gogidix. All rights reserved.
