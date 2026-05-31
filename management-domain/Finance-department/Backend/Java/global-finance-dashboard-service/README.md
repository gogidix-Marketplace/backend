# Global Finance Dashboard Service

## Overview

The Global Finance Dashboard Service provides comprehensive financial analytics and oversight across all global operations. It aggregates financial data from multiple regions and presents unified dashboards for strategic decision-making.

## Features

- **Revenue Tracking**: Monitor revenue across all regions and countries
- **Expense Management**: Track and categorize expenses globally
- **Cash Flow Monitoring**: Real-time cash flow visibility
- **Financial Reporting**: Generate comprehensive financial reports
- **Multi-Currency Support**: Handle multiple currencies with automatic conversion
- **Budget Management**: Track budgets and variance analysis
- **Dashboard Views**: Customizable dashboard widgets and filters

## Technology Stack

- **Framework**: Spring Boot 3.1.5
- **Language**: Java 17
- **Build**: Maven 3.9.12+
- **Database**: MongoDB (primary), Redis (cache)
- **Message Queue**: Kafka
- **API Documentation**: OpenAPI 3.0 (Swagger UI)

## Hexagonal Architecture

This service follows the Hexagonal Architecture pattern (Ports and Adapters):

```
domain/          # Core business logic (no framework dependencies)
  ├─ model/       # Domain entities
  ├─ repository/  # Repository interfaces (ports)
  ├─ event/       # Domain events
  └─ policy/      # Business policies

application/     # Application services
  ├─ service/     # Service implementations
  ├─ dto/         # Data transfer objects
  └─ mapper/      # Object mappers

infrastructure/  # External concerns
  ├─ persistence/ # Database implementations
  ├─ messaging/   # Kafka publishers/consumers
  ├─ security/    # Authentication/authorization
  └─ config/      # Spring configuration

interfaces/      # External interfaces
  └─ rest/        # REST controllers

shared/          # Shared utilities
  ├─ requestcontext/  # Multi-tenant context
  ├─ exception/       # Custom exceptions
  └─ util/            # Utilities
```

## API Endpoints

### Financial Metrics

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/financial-dashboard/metrics` | Get all metrics |
| GET | `/api/v1/financial-dashboard/metrics/{id}` | Get metric by ID |
| GET | `/api/v1/financial-dashboard/metrics/type/{type}` | Get metrics by type |
| GET | `/api/v1/financial-dashboard/metrics/period/{period}` | Get metrics by period |
| POST | `/api/v1/financial-dashboard/metrics` | Create new metric |
| PUT | `/api/v1/financial-dashboard/metrics/{id}/amount` | Update metric amount |
| DELETE | `/api/v1/financial-dashboard/metrics/{id}` | Delete metric |

### Dashboard

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/financial-dashboard/summary` | Get dashboard summary |

## Multi-Tenancy

All requests must include the `X-Tenant-ID` header for tenant isolation:

```
X-Tenant-ID: tenant-001
X-User-ID: user-001
X-Correlation-ID: optional-correlation-id
```

## Running Locally

```bash
# Build the service
mvn clean package

# Run the service
java -jar target/global-finance-dashboard-service-*.jar

# Access Swagger UI
open http://localhost:8080/swagger-ui.html
```

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| SERVICE_PORT | Service port | 8080 |
| MONGODB_HOST | MongoDB host | localhost |
| MONGODB_PORT | MongoDB port | 27017 |
| MONGODB_DB | MongoDB database | global_finance_dashboard_db |
| REDIS_HOST | Redis host | localhost |
| REDIS_PORT | Redis port | 6379 |
| KAFKA_SERVERS | Kafka bootstrap servers | localhost:9092 |

## Testing

```bash
# Run all tests
mvn test

# Run with coverage
mvn test jacoco:report
```

## Deployment

### Docker

```bash
# Build image
docker build -t global-finance-dashboard-service .

# Run container
docker run -p 8080:8080 global-finance-dashboard-service
```

### Railway

Deploy using `railway.json` configuration.

## License

Copyright © 2026 Gogidix. All rights reserved.
