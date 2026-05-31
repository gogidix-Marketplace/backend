# Universal Tracking Service

A hexagonal SaaS service for universal event tracking and analytics with multi-tenant architecture.

## Overview

The Universal Tracking Service provides comprehensive event tracking and analytics capabilities for the Gogidix ecosystem. It implements a clean hexagonal architecture with Domain-Driven Design (DDD) principles.

## Features

- **Hexagonal Architecture**: Clean separation of concerns with ports and adapters
- **Multi-tenancy**: Complete tenant isolation for SaaS deployment
- **Event Tracking**: Capture and store any type of tracking event
- **Session Management**: Group related events into user sessions
- **Metrics Collection**: Pre-aggregated metrics for efficient reporting
- **Event-Driven**: Kafka integration for async event processing
- **Redis Caching**: Performance optimization with Redis cache
- **REST API**: OpenAPI/Swagger documentation included

## Architecture

### Domain Layer
- **Models**: `TrackingEvent`, `TrackingSession`, `TrackingMetric`
- **Ports (In)**: Commands and Queries for use cases
- **Ports (Out)**: Repository interfaces for persistence

### Application Layer
- **Services**: Command and Query handlers (CQRS pattern)
- **DTOs**: Request/Response objects
- **Mappers**: MapStruct for entity-DTO conversion

### Infrastructure Layer
- **Persistence**: PostgreSQL with JPA
- **Caching**: Redis for performance
- **Messaging**: Kafka for event publishing
- **Security**: Tenant context management

### Interfaces Layer
- **REST Controllers**: API endpoints with OpenAPI docs

## API Endpoints

### Sessions
- `POST /api/v1/tracking/sessions` - Create new session
- `GET /api/v1/tracking/sessions/{sessionId}` - Get session details
- `PUT /api/v1/tracking/sessions/{sessionId}` - Update session
- `GET /api/v1/tracking/sessions/{sessionId}/events` - Get session events

### Events
- `POST /api/v1/tracking/events` - Create tracking event
- `GET /api/v1/tracking/events/{eventId}` - Get event details
- `GET /api/v1/tracking/events` - Search events with filters

### Health
- `GET /api/v1/health` - Health check
- `GET /api/v1/ready` - Readiness probe
- `GET /api/v1/live` - Liveness probe

### Statistics
- `GET /api/v1/tracking/statistics` - Get tracking statistics

## Configuration

### Application Properties
```yaml
spring:
  application:
    name: universal-tracking-service
  datasource:
    url: jdbc:postgresql://localhost:5432/universal_tracking
    username: postgres
    password: postgres
  redis:
    host: localhost
    port: 6379
  kafka:
    bootstrap-servers: localhost:9092

tracking:
  event:
    retention:
      days: 90
    batch:
      size: 100
  session:
    timeout:
      minutes: 30
  metric:
    aggregation:
      enabled: true
```

## Building

### Prerequisites
- Java 17
- Maven 3.9+
- PostgreSQL 14+
- Redis 7+
- Kafka 3.x

### Compile
```bash
mvn clean compile
```

### Build JAR
```bash
mvn clean package
```

### Run Tests
```bash
mvn test
```

### Run Application
```bash
mvn spring-boot:run
```

## Database Schema

### tracking_events
- Stores all tracking events with full metadata
- Indexed on tenant_id, session_id, event_type, timestamp
- Supports efficient querying and aggregation

### tracking_sessions
- Manages user sessions with activity tracking
- Indexed on tenant_id, user_id, started_at
- Automatic timeout detection

### tracking_metrics
- Pre-aggregated metrics for fast reporting
- Supports daily and hourly aggregation
- Dimension-based filtering

## Multi-Tenancy

All entities include `tenant_id` for complete data isolation:
- Tenant context extracted from `X-Tenant-ID` header
- Automatic filtering in all queries
- Tenant-specific caching in Redis

## Event Publishing

Events are published to Kafka topics:
- `tracking-events`: New tracking events
- `tracking-sessions`: Session lifecycle events

## Monitoring

Actuator endpoints available:
- `/actuator/health` - Application health
- `/actuator/metrics` - Application metrics
- `/actuator/prometheus` - Prometheus metrics

## API Documentation

Swagger UI available at:
- `http://localhost:8080/swagger-ui.html`

## Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn verify
```

## Docker

Build image:
```bash
docker build -t universal-tracking-service:1.0.0 .
```

Run container:
```bash
docker run -p 8080:8080 \
  -e DATABASE_URL=jdbc:postgresql://host:5432/universal_tracking \
  -e REDIS_HOST=host \
  -e KAFKA_BOOTSTRAP_SERVERS=host:9092 \
  universal-tracking-service:1.0.0
```

## License

Copyright © 2025 Gogidix. All rights reserved.
