# Foundation Services Monitoring - Backend

This directory contains the backend services for the Foundation Services Monitoring system. The backend consists of three Spring Boot microservices that work together to monitor 53 services (48 AI services + 5 Orchestration services).

## Architecture

The backend follows hexagonal architecture with clean separation of concerns:

```
┌─────────────────────────────────────────────────────────────┐
│                     Interfaces Layer                        │
│  (REST Controllers, WebSocket Handlers, Event Handlers)     │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                   Application Layer                         │
│  (Application Services, DTOs, Mappers, Use Cases)           │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                     Domain Layer                            │
│  (Domain Models, Ports, Domain Services, Business Logic)    │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                  Infrastructure Layer                        │
│  (Repository Implementations, External Services, Adapters)  │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                   Shared Layer                              │
│  (Exceptions, Utilities, Common Classes)                    │
└─────────────────────────────────────────────────────────────┘
```

## Services

### 1. Monitoring Data Service (Port 8081)

**Purpose**: Collects, stores, and aggregates metrics from all 53 services.

**Key Features**:
- Time-series metrics collection and storage (MongoDB)
- Metric aggregation (minute, 5-minute, hourly, daily windows)
- Real-time metric updates via WebSocket
- Service registration for monitoring
- Threshold-based alerting

**API Endpoints**:
- `POST /api/v1/metrics` - Collect a single metric
- `POST /api/v1/metrics/batch` - Collect multiple metrics
- `POST /api/v1/metrics/query` - Query metrics with aggregation
- `GET /api/v1/metrics/services/{serviceName}` - Get metrics for a service
- `POST /api/v1/services` - Register a service for monitoring
- `GET /api/v1/services` - List all registered services

**WebSocket**:
- `/ws` - WebSocket endpoint for real-time metric updates
- Subscribe to `/topic/metrics/{tenant}/{service}` for service-specific updates

### 2. Service Health Service (Port 8082)

**Purpose**: Aggregates health status from all services, tracks uptime/downtime.

**Key Features**:
- Periodic health checks on all registered services
- Health score calculation (0-100)
- Uptime/downtime tracking
- Service dependency mapping
- Health status aggregation

**API Endpoints**:
- `GET /api/v1/health/summary` - Get overall health summary
- `GET /api/v1/health/services/{serviceName}` - Get service health
- `GET /api/v1/health/services` - List all service health
- `GET /api/v1/health/ai-services` - Get AI services health
- `GET /api/v1/health/orchestration-services` - Get Orchestration services health

### 3. Alert Management Service (Port 8083)

**Purpose**: Manages alert rules, generates alerts based on thresholds, handles notifications.

**Key Features**:
- Alert rule configuration
- Alert generation based on metric thresholds
- Alert history and acknowledgment
- Multi-channel notification routing (Email, Webhook, Slack, PagerDuty)
- Alert escalation

**API Endpoints**:
- `POST /api/v1/alerts/rules` - Create an alert rule
- `GET /api/v1/alerts/rules` - List all alert rules
- `DELETE /api/v1/alerts/rules/{ruleId}` - Delete an alert rule
- `GET /api/v1/alerts` - List alerts with pagination
- `POST /api/v1/alerts/{alertId}/acknowledge` - Acknowledge an alert
- `POST /api/v1/alerts/{alertId}/resolve` - Resolve an alert

## Technology Stack

- **Framework**: Spring Boot 3.1.5
- **Java Version**: 17
- **Build Tool**: Maven
- **Database**: MongoDB (time-series data storage)
- **Cache**: Redis
- **Message Broker**: Kafka
- **WebSocket**: Spring WebSocket with STOMP
- **API Documentation**: SpringDoc OpenAPI 3
- **Metrics**: Micrometer with Prometheus
- **Testing**: JUnit 5, Testcontainers

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- MongoDB 7+
- Redis 7+
- Kafka 3.6+

### Building

```bash
# Build all services
cd monitoring-data-service && mvn clean package
cd ../service-health-service && mvn clean package
cd ../alert-management-service && mvn clean package
```

### Running with Docker Compose

```bash
# Start all services including infrastructure
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

### Running Individual Services

```bash
# Monitoring Data Service
cd monitoring-data-service
mvn spring-boot:run

# Service Health Service
cd service-health-service
mvn spring-boot:run

# Alert Management Service
cd alert-management-service
mvn spring-boot:run
```

## Configuration

All services use Spring configuration profiles. Default profiles:
- `dev` - Development (default)
- `test` - Testing
- `prod` - Production

Configuration files are located in `src/main/resources/`:
- `application.yml` - Base configuration
- `application-dev.yml` - Development overrides
- `application-test.yml` - Testing overrides
- `application-prod.yml` - Production overrides

### Environment Variables

Common environment variables for all services:

| Variable | Description | Default |
|----------|-------------|---------|
| `SERVICE_PORT` | Service port | Varies |
| `MONGODB_HOST` | MongoDB host | localhost |
| `MONGODB_PORT` | MongoDB port | 27017 |
| `MONGODB_DB` | Database name | Service-specific |
| `REDIS_HOST` | Redis host | localhost |
| `REDIS_PORT` | Redis port | 6379 |
| `KAFKA_SERVERS` | Kafka bootstrap servers | localhost:9092 |
| `LOG_LEVEL` | Logging level | DEBUG |

## API Documentation

Each service provides Swagger UI documentation:

- Monitoring Data Service: http://localhost:8081/api/v1/swagger-ui.html
- Service Health Service: http://localhost:8082/api/v1/swagger-ui.html
- Alert Management Service: http://localhost:8083/api/v1/swagger-ui.html

## Monitoring the Services

All services expose Spring Boot Actuator endpoints:

- `GET /actuator/health` - Service health
- `GET /actuator/metrics` - Metrics
- `GET /actuator/prometheus` - Prometheus metrics
- `GET /actuator/info` - Service information

## Service Communication

The services communicate via:

1. **Synchronous REST** - For direct queries and updates
2. **WebSocket** - For real-time metric updates to the frontend
3. **Kafka Events** - For async event publishing and inter-service communication

## Data Retention

- **Raw Metrics**: 30 days
- **Aggregated Metrics**:
  - Minute/Hour: 7 days
  - Hour: 30 days
  - Day: 90 days
- **Alerts**: 90 days
- **Alert History**: 90 days
- **Uptime Records**: 1 year

## Testing

```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify

# Run with coverage
mvn test jacoco:report
```

## License

Copyright (c) 2024 Gogidix. All rights reserved.
