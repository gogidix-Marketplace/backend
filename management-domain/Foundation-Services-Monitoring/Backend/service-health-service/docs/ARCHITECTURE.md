# Service Health Service - Architecture Documentation

## Overview

The Service Health Service is a core component of the Foundation-Services-Monitoring domain within the Gogidix Ecosystem. It provides comprehensive health status tracking, uptime monitoring, dependency mapping, and health score calculation for all 53 services.

## Table of Contents

1. [System Architecture](#system-architecture)
2. [Component Diagram](#component-diagram)
3. [Domain Model](#domain-model)
4. [Data Flow](#data-flow)
5. [Technology Stack](#technology-stack)
6. [Design Patterns](#design-patterns)

## System Architecture

The Service Health Service follows a hexagonal (ports and adapters) architecture pattern with scheduled health checks and event-driven updates.

```mermaid
graph TB
    subgraph "Interface Layer"
        SHC[ServiceHealthController]
        HC[HealthController]
    end

    subgraph "Application Layer"
        SHAS[ServiceHealthApplicationService]
        HCSS[HealthCheckSchedulerService]
        SHM[ServiceHealthMapper]
    end

    subgraph "Domain Layer"
        SHS[ServiceHealthStatus]
        SD[ServiceDependency]
        SU[ServiceUptime]
        ScoreComponents[ScoreComponents]
    end

    subgraph "Port Interfaces"
        SHSRP[ServiceHealthStatusRepositoryPort]
        SDRP[ServiceDependencyRepositoryPort]
        SURP[ServiceUptimeRepositoryPort]
        HCEP[HealthCheckEventPublisherPort]
    end

    subgraph "Infrastructure Adapters"
        SHA[HealthStatusAdapter]
        SDA[DependencyAdapter]
        SUA[UptimeAdapter]
        KAFKA[Kafka Publisher]
    end

    subgraph "Data Stores"
        MONGO[(MongoDB)]
        REDIS[(Redis Cache)]
    end

    subgraph "External Dependencies"
        SVC[Monitored Services]
    end

    SHC --> SHAS
    HCSS --> SHAS
    HCSS --> SVC
    SHAS --> SHM
    SHAS --> SHS
    SHAS --> SD
    SHAS --> SU
    SHAS --> SHSRP
    SHAS --> SDRP
    SHAS --> SURP
    SHSRP --> SHA
    SDRP --> SDA
    SURP --> SUA
    SHAS --> HCEP
    HCEP --> KAFKA
    SHA --> MONGO
    SDA --> MONGO
    SUA --> MONGO
    SHAS --> REDIS
```

## Component Diagram

```mermaid
graph LR
    subgraph "API Layer"
        HealthAPI[Health API]
    end

    subgraph "Services"
        HealthApp[Health Application Service]
        Scheduler[Health Check Scheduler]
        Calculator[Health Score Calculator]
    end

    subgraph "Domain"
        HealthStatus[Health Status]
        Dependency[Dependency]
        Uptime[Uptime]
    end

    subgraph "Infrastructure"
        Repositories[Repositories]
        EventPublisher[Event Publisher]
    end

    HealthAPI --> HealthApp
    Scheduler --> HealthApp
    HealthApp --> Calculator
    HealthApp --> HealthStatus
    HealthApp --> Dependency
    HealthApp --> Uptime
    HealthStatus --> Repositories
    Dependency --> Repositories
    Uptime --> Repositories
    HealthApp --> EventPublisher
```

## Domain Model

### Service Health Status

```mermaid
classDiagram
    class ServiceHealthStatus {
        +String id
        +String tenantId
        +String serviceName
        +ServiceType serviceType
        +HealthStatus status
        +Integer healthScore
        +ScoreComponents scoreComponents
        +Map~String,Object~ details
        +Instant lastCheckAt
        +Integer consecutiveFailures
        +Instant lastSuccessAt
        +Instant lastFailureAt
        +Double averageResponseTime
        +Double errorRate
        +Double uptimePercentage
        +String instanceId
        +String host
        +Instant createdAt
        +isHealthy() boolean
        +isDown() boolean
        +statusFromScore(Integer score) HealthStatus
    }

    class HealthStatus {
        <<enumeration>>
        HEALTHY
        DEGRADED
        UNHEALTHY
        DOWN
        UNKNOWN
    }

    class ScoreComponents {
        +Double uptimeScore
        +Double responseTimeScore
        +Double errorRateScore
        +Double dependencyScore
    }

    ServiceHealthStatus --> HealthStatus
    ServiceHealthStatus --> ScoreComponents
```

### Service Dependency

```mermaid
classDiagram
    class ServiceDependency {
        +String id
        +String tenantId
        +String serviceName
        +String dependsOnService
        +DependencyType dependencyType
        +Boolean isCritical
        +Double healthImpact
        +Instant lastVerifiedAt
        +DependencyStatus status
        +List~DependencyEndpoint~ endpoints
        +Instant createdAt
        +Instant updatedAt
    }

    class DependencyType {
        <<enumeration>>
        REST_API
        GRPC
        MESSAGE_QUEUE
        DATABASE
        CACHE
        EVENT_STREAM
        INTERNAL
    }

    class DependencyStatus {
        <<enumeration>>
        ACTIVE
        INACTIVE
        DEGRADED
        FAILED
        UNKNOWN
    }

    class DependencyEndpoint {
        +String url
        +String method
        +Double averageLatency
        +Double successRate
    }

    ServiceDependency --> DependencyType
    ServiceDependency --> DependencyStatus
    ServiceDependency --> DependencyEndpoint
```

### Service Uptime

```mermaid
classDiagram
    class ServiceUptime {
        +String id
        +String tenantId
        +String serviceName
        +String instanceId
        +Instant periodStart
        +Instant periodEnd
        +Long totalDuration
        +Long upDuration
        +Long downDuration
        +Double uptimePercentage
        +Integer upCount
        +Integer downCount
        +Instant createdAt
        +calculateUptime() Double
    }
```

## Data Flow

### Health Check Flow

```mermaid
sequenceDiagram
    participant Scheduler as Health Check Scheduler
    participant Service as Monitored Service
    participant AppService as Health Application Service
    participant Repository as Health Repository
    participant Publisher as Event Publisher

    Note over Scheduler: Periodic Execution
    Scheduler->>Service: HTTP GET /health
    Service-->>Scheduler: 200 OK / 503 Service Unavailable
    Scheduler->>AppService: updateServiceHealth()
    AppService->>Repository: findByTenantAndService()
    Repository-->>AppService: ServiceHealthStatus
    alt Service Exists
        AppService->>Repository: updateStatus()
        AppService->>Publisher: publishStatusChanged()
    else Service Not Found
        AppService->>Repository: save(new HealthStatus)
    end
    Publisher->>Kafka: HealthStatusChanged Event
```

### Dependency Health Impact Flow

```mermaid
sequenceDiagram
    participant DepService as Dependency Service
    participant Dependent as Dependent Service
    participant HealthService as Health Application Service
    participant Calculator as Health Score Calculator

    DepService->>HealthService: updateServiceHealth(DOWN)
    HealthService->>Repository: findDependents()
    Repository-->>HealthService: List of dependents
    loop For each dependent
        HealthService->>Calculator: recalculateHealth()
        Calculator->>Calculator: applyDependencyImpact()
        Calculator-->>HealthService: newHealthScore
        HealthService->>Repository: updateStatus()
    end
```

### Health Summary Flow

```mermaid
sequenceDiagram
    participant Client as Dashboard/User
    participant API as Health API
    participant Cache as Redis Cache
    participant Service as Health Application Service
    participant Repository as Health Repository

    Client->>API: GET /health/summary
    API->>Cache: Check cache
    alt Cache Hit
        Cache-->>API: Cached summary
        API-->>Client: Health Summary
    else Cache Miss
        API->>Service: getHealthSummary()
        Service->>Repository: findByTenantId()
        Repository-->>Service: All health statuses
        Service->>Service: Calculate aggregates
        Service->>Cache: Cache result
        Service-->>API: HealthSummaryDto
        API-->>Client: Health Summary
    end
```

## Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| Framework | Spring Boot | 3.1.5 |
| Language | Java | 17 |
| Database | MongoDB | Latest |
| Cache | Redis | Latest |
| Messaging | Apache Kafka | Latest |
| Scheduler | Quartz Scheduler | Latest |
| API Documentation | SpringDoc OpenAPI | 2.3.0 |
| Metrics | Micrometer | Latest |
| Build Tool | Maven | 3.11.0 |
| Testing | JUnit 5, Mockito, Testcontainers | Latest |

## Design Patterns

### Hexagonal Architecture (Ports and Adapters)

- **Ports**: Domain-defined interfaces (Repository ports, Event publisher ports)
- **Adapters**: Infrastructure implementations (MongoDB adapters, Kafka publishers)

### Scheduled Task Pattern

- Quartz scheduler for periodic health checks
- Configurable check intervals per service
- Automatic retry logic

### Observer Pattern

- Health status changes trigger dependency recalculation
- Event-driven updates to dependents

### Caching Pattern

- Redis caching for frequently accessed health summaries
- TTL-based cache invalidation

### Repository Pattern

- Abstraction over data persistence
- Domain-specific query methods

## Health Score Calculation

The health score (0-100) is calculated using multiple components:

1. **Uptime Score (40%)**: Based on uptime percentage
2. **Response Time Score (25%)**: Based on average response time
3. **Error Rate Score (20%)**: Based on request error rate
4. **Dependency Score (15%)**: Based on dependency health

**Health Status Mapping:**
- 90-100: HEALTHY
- 70-89: DEGRADED
- 50-69: UNHEALTHY
- 0-49: DOWN
- Null: UNKNOWN

## Key Features

1. **Real-Time Health Tracking**: Continuous health monitoring of all services
2. **Dependency Mapping**: Tracks service dependencies and their health impact
3. **Health Scoring**: Calculates composite health scores
4. **Uptime Tracking**: Monitors and records service uptime
5. **Multi-Tenancy**: Full tenant isolation
6. **Event-Driven**: Publishes health status change events
7. **Caching**: Redis caching for improved query performance
