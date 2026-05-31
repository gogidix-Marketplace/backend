# Infrastructure Monitoring Service - Architecture

## Overview

The Infrastructure Monitoring Service is a Spring Boot microservice responsible for monitoring the health and performance of IT infrastructure components across the Gogidix ecosystem. It provides real-time visibility into servers, databases, caches, and other infrastructure elements.

## Architecture Diagram

```mermaid
graph TB
    subgraph "Presentation Layer"
        API[REST Controllers]
        FILTER[Request Context Filter]
    end

    subgraph "Application Layer"
        SERVICE[Infrastructure Monitoring Service]
        ALERT_SERVICE[Alert Service]
        DTO[Data Transfer Objects]
    end

    subgraph "Domain Layer"
        MODEL[Domain Models]
        REPO[Repositories]
    end

    subgraph "Infrastructure Layer"
        MONGO[(MongoDB)]
        CONFIG[Configuration]
        CACHE[Cache]
    end

    API --> FILTER
    FILTER --> SERVICE
    API --> ALERT_SERVICE
    SERVICE --> REPO
    ALERT_SERVICE --> REPO
    REPO --> MONGO
    SERVICE --> CACHE
    CONFIG --> MONGO
```

## Layer Responsibilities

### Presentation Layer
- **InfrastructureMonitoringController**: REST endpoints for infrastructure monitoring CRUD operations
- **MonitoringAlertController**: REST endpoints for alert management
- **RequestContextFilter**: Extracts tenant/user context from HTTP headers

### Application Layer
- **InfrastructureMonitoringService**: Business logic for infrastructure monitoring
- **MonitoringAlertService**: Business logic for alert management
- **DTOs**: Data transfer objects for API communication

### Domain Layer
- **InfrastructureMonitoring**: Core domain model for monitored infrastructure
- **MonitoringAlert**: Core domain model for alerts
- **Repositories**: Data access interfaces

### Infrastructure Layer
- **MongoDB**: Document database for persistence
- **Cache**: Spring Cache abstraction
- **Configuration**: Spring Boot configuration

## Domain Model

### InfrastructureMonitoring

```mermaid
classDiagram
    class InfrastructureMonitoring {
        String id
        String tenantId
        String name
        InfrastructureType type
        String host
        Integer port
        MonitoringStatus status
        HealthCheckConfiguration healthCheckConfig
        Map~String,String~ tags
        List~MetricSnapshot~ recentMetrics
        Instant lastCheckedAt
        String region
        String environment
    }

    class InfrastructureType {
        <<enumeration>>
        SERVER
        DATABASE
        CACHE
        MESSAGE_QUEUE
        LOAD_BALANCER
        CONTAINER
        KUBERNETES_CLUSTER
        STORAGE
        CDN
        API_GATEWAY
    }

    class MonitoringStatus {
        <<enumeration>>
        HEALTHY
        DEGRADED
        UNHEALTHY
        UNKNOWN
        MAINTENANCE
    }

    class HealthCheckConfiguration {
        String protocol
        String path
        Integer intervalSeconds
        Integer timeoutSeconds
        Integer retryCount
    }

    class MetricSnapshot {
        String metricName
        Double value
        String unit
        Instant timestamp
    }

    InfrastructureMonitoring --> InfrastructureType
    InfrastructureMonitoring --> MonitoringStatus
    InfrastructureMonitoring *-- HealthCheckConfiguration
    InfrastructureMonitoring *-- "0..*" MetricSnapshot
```

### MonitoringAlert

```mermaid
classDiagram
    class MonitoringAlert {
        String id
        String tenantId
        String infrastructureId
        String infrastructureName
        AlertSeverity severity
        AlertType type
        String title
        String description
        AlertStatus status
        String acknowledgedBy
        String resolvedBy
        String resolutionNotes
        Integer occurrenceCount
        Instant firstOccurredAt
        Instant lastOccurredAt
    }

    class AlertSeverity {
        <<enumeration>>
        CRITICAL
        HIGH
        MEDIUM
        LOW
        INFO
    }

    class AlertType {
        <<enumeration>>
        SERVICE_DOWN
        HIGH_CPU
        HIGH_MEMORY
        HIGH_DISK
        NETWORK_ISSUE
        DATABASE_CONNECTION_ERROR
        SLOW_RESPONSE
        CERTIFICATE_EXPIRING
        REPLATION_LAG
        QUEUE_BUILDUP
        CUSTOM
    }

    class AlertStatus {
        <<enumeration>>
        OPEN
        ACKNOWLEDGED
        RESOLVED
        CLOSED
        SUPPRESSED
    }

    MonitoringAlert --> AlertSeverity
    MonitoringAlert --> AlertType
    MonitoringAlert --> AlertStatus
```

## Key Patterns

### Hexagonal Architecture
The service follows hexagonal architecture principles with clear separation between:
- **Domain**: Core business logic and models
- **Application**: Use cases and orchestration
- **Infrastructure**: External concerns (database, cache)
- **Interface**: External communication (REST API)

### Multi-Tenancy
The service supports multi-tenancy through:
- Tenant-scoped queries in repositories
- RequestContext propagation via ThreadLocal
- Tenant isolation filters

### Builder Pattern
Domain models use the Builder pattern for flexible object creation.

## Technology Stack

- **Framework**: Spring Boot 3.x
- **Language**: Java 17
- **Database**: MongoDB
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito
- **Container**: Docker

## Deployment

The service is deployed as a Docker container with:
- Multi-stage build for optimized image size
- Health check endpoint for orchestration
- Actuator endpoints for monitoring
