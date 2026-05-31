# Universal Tracking Service - Architecture Documentation

## Overview

The Universal Tracking Service is a hexagonal architecture (ports and adapters) SaaS microservice designed for universal event tracking and analytics. It provides multi-tenant event ingestion, storage, and real-time processing capabilities.

## Architectural Style

### Hexagonal Architecture (Ports and Adapters)

The service follows the hexagonal architecture pattern, separating the domain logic from external concerns:

```
+--------------------------------------------------+
|                  Interfaces Layer                |
|  +----------------+      +---------------------+ |
|  | REST Controllers |      |  Event Listeners  | |
|  +----------------+      +---------------------+ |
+--------------------------------------------------+
                      |
                      v
+--------------------------------------------------+
|               Application Layer                   |
|  +----------------+      +---------------------+ |
|  | Command Services|      |  Query Services   | |
|  +----------------+      +---------------------+ |
|  +-------------------------------------------+  |
|  |            DTOs & Mappers                  |  |
|  +-------------------------------------------+  |
+--------------------------------------------------+
                      |
                      v
+--------------------------------------------------+
|                 Domain Layer                      |
|  +----------------+      +---------------------+ |
|  |   Domain Models |      |  Ports (In/Out)   | |
|  +----------------+      +---------------------+ |
+--------------------------------------------------+
                      |
                      v
+--------------------------------------------------+
|              Infrastructure Layer                 |
|  +----------------+   +----------------------+  |
|  |  Repositories  |   |  Messaging/Kafka     |  |
|  +----------------+   +----------------------+  |
|  +----------------+   +----------------------+  |
|  | Redis Cache    |   |  Security/Interceptors| |
|  +----------------+   +----------------------+  |
+--------------------------------------------------+
```

## Layer Descriptions

### 1. Domain Layer

**Location:** `src/main/java/com/gogidix/universal/tracking/domain/`

The domain layer contains the core business logic and is independent of any external concerns.

#### Domain Models
- **TrackingEvent**: Represents a single tracked event with all its properties
- **TrackingSession**: Groups related events into user sessions
- **TrackingMetric**: Stores pre-aggregated metrics for reporting

#### Ports (Inbound/Outbound)
- **Inbound Ports (Use Cases):**
  - `CreateEventCommand` - Create a new tracking event
  - `CreateSessionCommand` - Create a new tracking session
  - `UpdateSessionCommand` - Update an existing session
  - `GetEventsQuery` - Search for events
  - `GetSessionQuery` - Get session details
  - `GetMetricsQuery` - Get metrics data

- **Outbound Ports (Repository Interfaces):**
  - `TrackingEventRepositoryPort` - Event persistence
  - `TrackingSessionRepositoryPort` - Session persistence
  - `TrackingMetricRepositoryPort` - Metric persistence

### 2. Application Layer

**Location:** `src/main/java/com/gogidix/universal/tracking/application/`

Contains the application services that orchestrate business operations.

#### CQRS Pattern
The service implements Command Query Responsibility Segregation:

- **Command Services:** Write operations (create, update)
  - `TrackingCommandService` - Handles all write operations

- **Query Services:** Read operations (search, get)
  - `TrackingQueryService` - Handles all read operations

#### DTOs and Mappers
- **Request DTOs:** Input validation and transformation
- **Response DTOs:** Output formatting
- **TrackingMapper:** MapStruct-based mapping between layers

### 3. Infrastructure Layer

**Location:** `src/main/java/com/gogidix/universal/tracking/infrastructure/`

Implements the ports defined in the domain layer.

#### Persistence (PostgreSQL)
- `TrackingEventRepository` - JPA repository for events
- `TrackingSessionRepository` - JPA repository for sessions
- `TrackingMetricRepository` - JPA repository for metrics

#### Caching (Redis)
- `TrackingCacheService` - Redis-based caching for frequently accessed data

#### Messaging (Kafka)
- `KafkaProducerConfig` / `KafkaConsumerConfig` - Kafka configuration
- `TrackingEventPublisher` - Publishes events to Kafka topics

#### Security
- `TenantInterceptor` - Multi-tenant request interceptor
- `WebConfig` - Web security configuration

### 4. Interfaces Layer

**Location:** `src/main/java/com/gogidix/universal/tracking/interfaces/rest/`

REST API controllers that handle HTTP requests and responses.

#### Controllers
- `TrackingController` - Main tracking API endpoints
- `HealthController` - Health check endpoints

## Technology Stack

| Component | Technology | Version |
|-----------|------------|---------|
| Language | Java | 17 |
| Framework | Spring Boot | 3.1.5 |
| Database | PostgreSQL | Latest |
| Cache | Redis | Latest |
| Messaging | Apache Kafka | Latest |
| API Docs | OpenAPI (Springdoc) | 2.3.0 |
| Mapping | MapStruct | 1.5.5.Final |
| Build Tool | Maven | - |
| Testing | JUnit 5, Mockito | - |

## Multi-Tenancy

The service implements **tenant isolation at the database level**:

1. Every entity includes a `tenantId` field
2. The `TenantInterceptor` extracts tenant ID from request headers
3. `RequestContext` stores tenant ID for the duration of the request
4. All queries automatically filter by tenant ID
5. Cross-tenant access is prevented by validation

### Tenant Context Flow
```
HTTP Request (X-Tenant-ID header)
    |
    v
TenantInterceptor
    |
    v
RequestContext.setTenantId()
    |
    v
Business Logic (accesses tenant via RequestContext)
    |
    v
Database Query (filtered by tenantId)
```

## Event Flow

### Event Ingestion Flow
```
Client POST /api/v1/tracking/events
    |
    v
TrackingController
    |
    v
CreateEventRequestDto (validation)
    |
    v
TrackingCommandService.createEvent()
    |
    +--> Save to PostgreSQL
    +--> Update Session (if sessionId provided)
    +--> Publish to Kafka (tracking-events topic)
    +--> Audit Log
    |
    v
TrackingEventResponseDto (response)
```

### Session Creation Flow
```
Client POST /api/v1/tracking/sessions
    |
    v
TrackingController
    |
    v
CreateSessionRequestDto (validation)
    |
    v
TrackingCommandService.createSession()
    |
    +--> Check for duplicate sessionId
    +--> Save to PostgreSQL
    +--> Publish to Kafka (tracking-sessions topic)
    +--> Audit Log
    |
    v
TrackingSessionResponseDto (response)
```

## Database Schema

### tracking_events Table
```sql
CREATE TABLE tracking_events (
    id UUID PRIMARY KEY,
    event_type VARCHAR(100) NOT NULL,
    session_id VARCHAR(100),
    user_id VARCHAR(100),
    source VARCHAR(50),
    timestamp TIMESTAMP NOT NULL,
    event_name VARCHAR(255),
    description TEXT,
    properties TEXT,
    metadata TEXT,
    ip_address VARCHAR(50),
    user_agent VARCHAR(500),
    referrer VARCHAR(500),
    page_url VARCHAR(1000),
    page_title VARCHAR(255),
    tenant_id VARCHAR(50) NOT NULL,
    correlation_id VARCHAR(100),
    priority INTEGER DEFAULT 5,
    processed BOOLEAN DEFAULT FALSE,
    processed_at TIMESTAMP,
    error_message TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- Indexes
CREATE INDEX idx_event_tenant_id ON tracking_events(tenant_id);
CREATE INDEX idx_event_session_id ON tracking_events(session_id);
CREATE INDEX idx_event_type ON tracking_events(event_type);
CREATE INDEX idx_event_timestamp ON tracking_events(timestamp);
CREATE INDEX idx_event_source ON tracking_events(source);
```

### tracking_sessions Table
```sql
CREATE TABLE tracking_sessions (
    id UUID PRIMARY KEY,
    session_id VARCHAR(100) UNIQUE NOT NULL,
    user_id VARCHAR(100),
    tenant_id VARCHAR(50) NOT NULL,
    source VARCHAR(50),
    ip_address VARCHAR(50),
    user_agent VARCHAR(500),
    device_type VARCHAR(50),
    browser VARCHAR(100),
    os VARCHAR(100),
    country VARCHAR(10),
    city VARCHAR(100),
    referrer VARCHAR(500),
    landing_page VARCHAR(1000),
    campaign VARCHAR(100),
    started_at TIMESTAMP NOT NULL,
    last_activity_at TIMESTAMP NOT NULL,
    ended_at TIMESTAMP,
    duration_seconds INTEGER,
    event_count INTEGER DEFAULT 0,
    page_view_count INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    metadata TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- Indexes
CREATE INDEX idx_session_tenant_id ON tracking_sessions(tenant_id);
CREATE INDEX idx_session_user_id ON tracking_sessions(user_id);
CREATE INDEX idx_session_started_at ON tracking_sessions(started_at);
CREATE INDEX idx_session_source ON tracking_sessions(source);
```

### tracking_metrics Table
```sql
CREATE TABLE tracking_metrics (
    id UUID PRIMARY KEY,
    metric_name VARCHAR(100) NOT NULL,
    metric_type VARCHAR(20) NOT NULL,
    metric_value DOUBLE NOT NULL,
    metric_date DATE NOT NULL,
    metric_hour INTEGER,
    dimensions TEXT,
    tenant_id VARCHAR(50) NOT NULL,
    event_type VARCHAR(100),
    source VARCHAR(50),
    count BIGINT DEFAULT 0,
    last_updated_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- Indexes
CREATE INDEX idx_metric_tenant_id ON tracking_metrics(tenant_id);
CREATE INDEX idx_metric_name ON tracking_metrics(metric_name);
CREATE INDEX idx_metric_date ON tracking_metrics(metric_date);
CREATE INDEX idx_metric_type ON tracking_metrics(metric_type);
```

## Kafka Topics

### tracking-events
**Purpose:** Real-time event streaming
**Partitions:** Configurable (default: 3)
**Replication Factor:** 3
**Retention:** 7 days

**Message Format:**
```json
{
  "eventId": "uuid",
  "eventType": "PAGE_VIEW",
  "sessionId": "session-id",
  "userId": "user-id",
  "source": "WEB",
  "tenantId": "tenant-id",
  "eventAction": "EVENT_CREATED",
  "timestamp": "2024-01-01T12:00:00",
  "correlationId": "correlation-id"
}
```

### tracking-sessions
**Purpose:** Session lifecycle events
**Partitions:** Configurable (default: 3)
**Replication Factor:** 3
**Retention:** 7 days

## Security

### Multi-Tenant Isolation
- Tenant ID extracted from `X-Tenant-ID` header
- All database queries filtered by tenant ID
- Cross-tenant access prevented at service layer

### Input Validation
- Jakarta Bean Validation on all DTOs
- Custom validators for business rules

### Audit Logging
- All write operations audited
- Audit includes: action, entity type, entity ID, description, timestamp, tenant

## Performance Considerations

### Caching Strategy
- Redis caching for frequently accessed sessions and events
- 30-minute default TTL
- Cache invalidation on updates

### Database Optimization
- Composite indexes on tenant_id + frequently queried fields
- Partitioning by tenant_id (recommended for large scale)
- Connection pooling (HikariCP)

### Batch Processing
- Bulk event insertion support
- Async event publishing to Kafka

## Scalability

### Horizontal Scaling
- Stateless service design
- Redis for shared cache
- Kafka for distributed messaging

### Vertical Scaling
- Configurable thread pools
- Memory settings tunable

### Deployment Options
- Kubernetes (recommended)
- Docker Swarm
- Traditional VM

## Monitoring

### Health Endpoints
- `/actuator/health` - Health check
- `/actuator/metrics` - Metrics
- `/actuator/prometheus` - Prometheus metrics

### Logging
- Structured logging (JSON format recommended)
- Correlation ID for request tracing
- Tenant-aware logging

## Configuration

### Application Properties
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/tracking_db
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  redis:
    host: ${REDIS_HOST}
    port: 6379
  kafka:
    bootstrap-servers: ${KAFKA_SERVERS}
    producer:
      topic:
        tracking-events: tracking-events
        tracking-sessions: tracking-sessions
```

## Testing Strategy

### Unit Tests
- JUnit 5 + Mockito
- >80% code coverage target
- Test each layer independently

### Integration Tests
- Spring Boot Test
- TestContainers for PostgreSQL and Kafka
- API endpoint testing

### Architecture Tests
- ArchUnit for architecture compliance
- Ensures layer separation rules
