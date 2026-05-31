# Monitoring Data Service - Architecture Documentation

## Overview

The Monitoring Data Service is a core component of the Foundation-Services-Monitoring domain. It provides comprehensive metrics collection, storage, aggregation, and querying capabilities for the Gogidix Ecosystem.

## Table of Contents

1. [System Architecture](#system-architecture)
2. [Component Diagram](#component-diagram)
3. [Domain Model](#domain-model)
4. [Data Flow](#data-flow)
5. [Technology Stack](#technology-stack)
6. [Design Patterns](#design-patterns)

## System Architecture

The Monitoring Data Service follows a hexagonal (ports and adapters) architecture pattern:

```mermaid
graph TB
    subgraph "Interface Layer"
        A[MetricsController]
        B[ServiceRegistrationController]
        H[HealthController]
    end

    subgraph "Application Layer"
        C[MetricCollectionService]
        D[ServiceRegistrationApplicationService]
        E[MetricAggregationService]
        F[MetricDataPointMapper]
        G[ServiceRegistrationMapper]
    end

    subgraph "Domain Layer"
        I[MetricDataPoint]
        J[MetricAggregation]
        K[ServiceRegistration]
        L[Domain Exceptions]
    end

    subgraph "Port Interfaces"
        M[MetricDataPointRepositoryPort]
        N[MetricAggregationRepositoryPort]
        O[ServiceRegistrationRepositoryPort]
        P[MetricEventPublisherPort]
    end

    subgraph "Infrastructure Adapters"
        Q[MongoDB Adapter]
        R[Kafka Event Publisher]
        S[Redis Cache]
    end

    A --> C
    B --> D
    C --> F
    D --> G
    C --> I
    D --> K
    C --> M
    C --> N
    D --> O
    C --> P
    M --> Q
    N --> R
```

## Component Diagram

### Core Components

```mermaid
classDiagram
    class MetricsController {
        +collectMetric()
        +collectBatchMetrics()
        +queryMetrics()
        +getMetricsByService()
        +getAggregatedMetrics()
        +getMetricCount()
    }

    class ServiceRegistrationController {
        +registerService()
        +getService()
        +getServices()
        +updateHeartbeat()
        +unregisterService()
    }

    class MetricCollectionService {
        +collectMetric()
        +collectBatchMetrics()
        +queryMetrics()
        +getAggregatedMetrics()
        +getMetricCount()
        -checkThreshold()
        -calculateStatistics()
    }

    class ServiceRegistrationApplicationService {
        +registerService()
        +getService()
        +getServicesByTenant()
        +updateHeartbeat()
        +unregisterService()
    }

    class MetricAggregationService {
        +computeAggregations()
        +computeWindowAggregations()
    }

    class MetricDataPoint {
        -String id
        -String tenantId
        -String serviceName
        -ServiceType serviceType
        -String metricName
        -double value
        -String unit
        -MetricType metricType
        -Instant timestamp
        +isValid()
        +getMetricKey()
    }

    class MetricAggregation {
        -String id
        -String tenantId
        -String serviceName
        -String metricName
        -AggregationWindow window
        -long count
        -Double min
        -Double max
        -Double avg
        +isValid()
        +getDurationSeconds()
    }

    class ServiceRegistration {
        -String serviceId
        -String tenantId
        -String serviceName
        -ServiceType serviceType
        -ServiceStatus status
        -Boolean enabled
        +isActive()
        +updateHeartbeat()
        +getHealthCheckUrl()
    }

    MetricsController --> MetricCollectionService
    ServiceRegistrationController --> ServiceRegistrationApplicationService
    MetricCollectionService --> MetricAggregationService
    MetricCollectionService --> MetricDataPoint
    MetricAggregationService --> MetricAggregation
    ServiceRegistrationApplicationService --> ServiceRegistration
```

## Domain Model

### MetricDataPoint

The `MetricDataPoint` entity represents a single metric data point:

```mermaid
erDiagram
    MetricDataPoint {
        string id PK
        string tenantId FK
        string serviceName
        ServiceType serviceType
        string metricName
        double value
        string unit
        MetricType metricType
        json tags
        instant timestamp
        string host
        string instanceId
        string correlationId
        string category
        instant createdAt
    }
```

### MetricAggregation

The `MetricAggregation` entity stores pre-aggregated metrics:

```mermaid
erDiagram
    MetricAggregation {
        string id PK
        string tenantId FK
        string serviceName
        string metricName
        AggregationWindow window
        instant windowStart
        instant windowEnd
        long count
        double min
        double max
        double avg
        double sum
        double p50
        double p95
        double p99
        double stdDev
        json tags
        instant computedAt
    }
```

### ServiceRegistration

The `ServiceRegistration` entity tracks services registered for monitoring:

```mermaid
erDiagram
    ServiceRegistration {
        string serviceId PK
        string tenantId FK
        string serviceName
        ServiceType serviceType
        string category
        string version
        string baseUrl
        string healthEndpoint
        string metricsEndpoint
        int collectionInterval
        boolean enabled
        ServiceStatus status
        instant registeredAt
        instant lastHeartbeat
    }
```

## Data Flow

### Metric Collection Flow

```mermaid
sequenceDiagram
    participant S as Service
    participant M as Metrics Controller
    participant C as Collection Service
    participant DB as MongoDB
    participant E as Event Publisher
    participant A as Alert Management

    S->>M: POST /metrics
    M->>C: collectMetric()
    C->>C: Validate metric
    C->>DB: Save metric data point
    C->>E: Publish metric event
    E->>A: Metric available for evaluation
    C->>C: Check threshold
    alt Threshold exceeded
        C->>E: Publish threshold exceeded event
        E->>A: Trigger alert evaluation
    end
    C-->>M: MetricResponseDto
    M-->>S: 201 Created
```

### Metric Query Flow

```mermaid
sequenceDiagram
    participant D as Dashboard
    participant M as Metrics Controller
    participant C as Collection Service
    participant R as Redis Cache
    participant DB as MongoDB

    D->>M: GET /metrics/query
    M->>C: queryMetrics()
    C->>R: Check cache
    alt Cache hit
        R-->>C: Cached results
    else Cache miss
        C->>DB: Query raw metrics
        DB-->>C: Metric data points
        C->>C: Calculate statistics
        C->>R: Cache results
    end
    C-->>M: MetricQueryResponseDto
    M-->>D: Query results
```

### Service Registration Flow

```mermaid
sequenceDiagram
    participant S as Service
    participant R as Registration Controller
    participant A as Registration Service
    participant DB as MongoDB
    participant H as Health Check

    S->>R: POST /services (register)
    R->>A: registerService()
    A->>A: Check for existing registration
    A->>DB: Save service registration
    A->>H: Start health monitoring
    A-->>R: ServiceRegistrationResponseDto
    R-->>S: 201 Created

    loop Every collection interval
        H->>S: GET health endpoint
        S-->>H: Health status
        H->>A: Update heartbeat
        A->>DB: Update lastHeartbeat
    end
```

## Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| Language | Java | 17 |
| Framework | Spring Boot | 3.1.5 |
| Build Tool | Maven | - |
| Database | MongoDB | - |
| Cache | Redis | - |
| Messaging | Kafka | - |
| API Documentation | SpringDoc OpenAPI | 2.3.0 |
| Testing | JUnit 5, Mockito | - |
| Testcontainers | MongoDB Testcontainers | 1.19.3 |
| Metrics | Micrometer | - |

## Design Patterns

### 1. Hexagonal Architecture (Ports and Adapters)

- Domain layer with no external dependencies
- Port interfaces for repository and event publishing
- Adapter implementations for MongoDB, Kafka, Redis

### 2. Domain-Driven Design (DDD)

- Bounded context for metrics collection
- Aggregates for MetricDataPoint and MetricAggregation
- Value objects for DTOs

### 3. CQRS (Command Query Responsibility Segregation)

- Separate DTOs for commands and queries
- Optimized read models via aggregation
- Separate write and read operations

### 4. Cache-Aside Pattern

- Redis caching for query results
- Cache invalidation on new metrics
- TTL-based cache expiration

### 5. Event-Driven Architecture

- Metric events published to Kafka
- Threshold exceeded events
- Async event processing

## Enumerations

### ServiceType
- `AI_SERVICE`: AI/ML services
- `ORCHESTRATION_SERVICE`: Workflow orchestration services

### MetricType
- `GAUGE`: Current value (can go up or down)
- `COUNTER`: Cumulative value (only increases)
- `HISTOGRAM`: Distribution of values
- `SUMMARY`: Percentiles
- `RATE`: Rate per time interval

### AggregationWindow
- `MINUTE`: 60 seconds
- `FIVE_MINUTES`: 300 seconds
- `FIFTEEN_MINUTES`: 900 seconds
- `HOUR`: 3600 seconds
- `SIX_HOURS`: 21600 seconds
- `DAY`: 86400 seconds
- `WEEK`: 604800 seconds

### ServiceStatus
- `REGISTERED`: Service registered but not active
- `ACTIVE`: Service is active and reporting
- `INACTIVE`: Service is not responding
- `DEGRADED`: Service is degraded
- `UNREGISTERED`: Service has been unregistered

## REST API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/metrics` | Collect single metric |
| POST | `/metrics/batch` | Collect batch metrics |
| POST | `/metrics/query` | Query metrics |
| GET | `/metrics/services/{serviceName}` | Get metrics for service |
| GET | `/metrics/services/{serviceName}/aggregated` | Get aggregated metrics |
| GET | `/metrics/count` | Get metric count |
| POST | `/services` | Register service |
| GET | `/services/{serviceId}` | Get service registration |
| GET | `/services` | List services |
| PUT | `/services/{serviceId}/heartbeat` | Update heartbeat |
| DELETE | `/services/{serviceId}` | Unregister service |
| GET | `/health` | Health check |

## Deployment Architecture

```mermaid
graph LR
    subgraph "Kubernetes Cluster"
        subgraph "Namespace: monitoring"
            A[Monitoring Data Service]
            B[MongoDB]
            C[Redis]
        end
    end

    subgraph "External Services"
        D[Kafka]
    end

    subgraph "Producers"
        E[AI Services]
        F[Orchestration Services]
    end

    E --> A
    F --> A
    A --> B
    A --> C
    A --> D
```

## Performance Considerations

1. **Batch Collection**: Collect multiple metrics in a single request
2. **Caching**: Redis cache for frequently queried metrics
3. **Aggregation**: Pre-computed aggregations for faster queries
4. **Time-series Optimization**: MongoDB time-series collections
5. **Async Processing**: Event publishing is asynchronous

## Scaling Strategy

1. **Horizontal Scaling**: Stateless service instances
2. **Database Sharding**: MongoDB sharding by tenant
3. **Cache Distribution**: Redis Cluster
4. **Message Partitioning**: Kafka partitioning by service

## Security Considerations

1. **Multi-tenancy**: All operations scoped to tenant ID
2. **Authentication**: JWT-based authentication
3. **Authorization**: Role-based access control
4. **Rate Limiting**: API rate limiting per tenant
5. **Data Isolation**: Tenant data isolation at database level

## Monitoring and Observability

1. **Health Checks**: `/health`, `/health/liveness`, `/health/readiness`
2. **Metrics**: Prometheus metrics via Micrometer
3. **Logging**: Structured logging with correlation IDs
4. **Tracing**: Distributed tracing support
5. **Alerts**: Integration with Alert Management Service
