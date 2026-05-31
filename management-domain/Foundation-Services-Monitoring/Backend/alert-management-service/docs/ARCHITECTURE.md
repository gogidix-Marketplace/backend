# Alert Management Service - Architecture Documentation

## Overview

The Alert Management Service is a core component of the Foundation-Services-Monitoring domain within the Gogidix Ecosystem. It provides comprehensive alert rule management, alert generation, and notification capabilities for monitoring system health and performance metrics.

## Table of Contents

1. [System Architecture](#system-architecture)
2. [Component Diagram](#component-diagram)
3. [Domain Model](#domain-model)
4. [Data Flow](#data-flow)
5. [Technology Stack](#technology-stack)
6. [Design Patterns](#design-patterns)
7. [Port and Adapter Architecture](#port-and-adapter-architecture)

## System Architecture

The Alert Management Service follows a hexagonal (ports and adapters) architecture pattern, separating domain logic from external concerns like databases, messaging systems, and web APIs.

```mermaid
graph TB
    subgraph "Interface Layer"
        A[AlertManagementController]
        H[HealthController]
    end

    subgraph "Application Layer"
        B[AlertManagementApplicationService]
        M[AlertMapper]
    end

    subgraph "Domain Layer"
        C[Alert Domain Model]
        D[AlertRule Domain Model]
        E[AlertHistory Domain Model]
        F[Domain Exceptions]
    end

    subgraph "Port Interfaces"
        G[AlertRepositoryPort]
        I[AlertRuleRepositoryPort]
        J[AlertHistoryRepositoryPort]
        K[AlertNotificationServicePort]
    end

    subgraph "Infrastructure Adapters"
        L[MongoDB Adapter]
        N[Kafka/Messaging Adapter]
        O[Email/Notification Adapter]
        P[WebSocket Adapter]
    end

    A --> B
    H --> B
    B --> M
    B --> C
    B --> D
    B --> E
    B --> G
    B --> I
    B --> J
    B --> K
    G --> L
    I --> L
    J --> L
    K --> N
    K --> O
    B --> P
```

## Component Diagram

### Core Components

```mermaid
classDiagram
    class AlertManagementController {
        +createAlertRule()
        +getAlertRules()
        +getAlertRule()
        +deleteAlertRule()
        +getAlerts()
        +getAlert()
        +acknowledgeAlert()
        +resolveAlert()
        +getAlertHistory()
    }

    class AlertManagementApplicationService {
        +createAlertRule()
        +getAlertRules()
        +getEnabledAlertRules()
        +getAlertRule()
        +deleteAlertRule()
        +getAlerts()
        +getAlert()
        +acknowledgeAlert()
        +resolveAlert()
        +getAlertHistory()
        +createAlertFromRule()
    }

    class AlertMapper {
        +toAlertRuleResponseDto()
        +toAlertResponseDto()
        +toAlertHistoryResponseDto()
    }

    class Alert {
        -String id
        -String tenantId
        -String ruleId
        -AlertStatus status
        -AlertSeverity severity
        +acknowledge()
        +resolve()
        +autoResolve()
        +isOpen()
        +isAcknowledged()
        +isResolved()
    }

    class AlertRule {
        -String id
        -String tenantId
        -String name
        -ConditionType conditionType
        -ComparisonOperator operator
        -Double threshold
        -AlertSeverity severity
        +evaluate(value)
        +isApplicableForService()
    }

    class AlertHistory {
        -String id
        -String alertId
        -StateChangeType stateChangeType
        -String previousState
        -String newState
    }

    AlertManagementController --> AlertManagementApplicationService
    AlertManagementApplicationService --> AlertMapper
    AlertManagementApplicationService --> Alert
    AlertManagementApplicationService --> AlertRule
    AlertManagementApplicationService --> AlertHistory
```

## Domain Model

### Alert

The `Alert` entity represents a generated alert from a rule trigger.

```mermaid
erDiagram
    Alert {
        string id PK
        string tenantId FK
        string ruleId FK
        string ruleName
        string serviceName
        string metricName
        AlertSeverity severity
        AlertStatus status
        string message
        double triggerValue
        double threshold
        instant triggeredAt
        instant acknowledgedAt
        string acknowledgedBy
        string acknowledgmentComment
        instant resolvedAt
        string resolvedBy
        string resolutionComment
        NotificationStatus notificationStatus
        int notificationAttempts
        json context
        json tags
        instant createdAt
        instant updatedAt
    }
```

### AlertRule

The `AlertRule` entity defines conditions under which alerts should be generated.

```mermaid
erDiagram
    AlertRule {
        string id PK
        string tenantId FK
        string name
        string description
        boolean enabled
        string serviceName
        string metricName
        ConditionType conditionType
        double threshold
        ComparisonOperator operator
        int durationSeconds
        AlertSeverity severity
        list notificationChannels
        list recipients
        int cooldownSeconds
        string messageTemplate
        json metadata
        json tags
        instant createdAt
        instant updatedAt
        string createdBy
        string updatedBy
    }
```

### AlertHistory

The `AlertHistory` entity records all state changes for an alert.

```mermaid
erDiagram
    AlertHistory {
        string id PK
        string alertId FK
        string tenantId FK
        StateChangeType stateChangeType
        string previousState
        string newState
        string changedBy
        string comment
        json context
        instant changedAt
    }
```

## Data Flow

### Alert Creation Flow

```mermaid
sequenceDiagram
    participant M as Monitoring Service
    participant AM as Alert Management Service
    participant DB as MongoDB
    participant N as Notification Service
    participant U as User

    M->>AM: Metric exceeds threshold
    AM->>DB: Query enabled alert rules
    DB-->>AM: Return matching rules
    AM->>AM: Evaluate rule conditions
    AM->>DB: Save new alert
    AM->>DB: Create alert history (CREATED)
    AM->>N: Send notifications
    N-->>U: Alert notification
    U->>AM: Acknowledge alert
    AM->>DB: Update alert status
    AM->>DB: Create alert history (ACKNOWLEDGED)
```

### Alert Resolution Flow

```mermaid
sequenceDiagram
    participant U as User
    participant C as Alert Controller
    participant S as Application Service
    participant DB as Repository
    participant H as History

    U->>C: POST /alerts/{id}/resolve
    C->>S: resolveAlert()
    S->>DB: findById(alertId)
    DB-->>S: Alert
    S->>S: alert.resolve(userId, comment)
    S->>DB: save(alert)
    S->>H: save(history)
    S-->>C: AlertResponseDto
    C-->>U: 200 OK
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

## Design Patterns

### 1. Hexagonal Architecture (Ports and Adapters)

The service implements hexagonal architecture with:
- **Domain**: Core business logic without external dependencies
- **Ports**: Interfaces for interacting with external systems
- **Adapters**: Implementation of ports for specific technologies

### 2. Domain-Driven Design (DDD)

- Bounded context for alert management
- Domain entities with rich behavior
- Value objects for DTOs
- Repository pattern for data access

### 3. CQRS (Command Query Responsibility Segregation)

- Separate DTOs for requests and responses
- Optimized read models via mappers
- Separate operations for commands (create, update) and queries (get)

### 4. Builder Pattern

All domain entities use Lombok's `@Builder` for fluent object construction.

## Port and Adapter Architecture

### Output Ports (Domain Interface)

```java
// Alert Repository Port
public interface AlertRepositoryPort {
    Alert save(Alert alert);
    Optional<Alert> findById(String id);
    List<Alert> findByTenantId(String tenantId);
    List<Alert> findByTenantIdAndStatus(String tenantId, AlertStatus status);
    List<Alert> findByTenantIdAndSeverity(String tenantId, AlertSeverity severity);
    void deleteById(String id);
    long deleteOlderThan(Instant timestamp);
}

// Alert Rule Repository Port
public interface AlertRuleRepositoryPort {
    AlertRule save(AlertRule rule);
    Optional<AlertRule> findById(String id);
    List<AlertRule> findByTenantId(String tenantId);
    List<AlertRule> findEnabledByTenantId(String tenantId);
    void deleteById(String id);
}

// Alert History Repository Port
public interface AlertHistoryRepositoryPort {
    AlertHistory save(AlertHistory history);
    List<AlertHistory> findByAlertId(String alertId);
}

// Alert Notification Service Port
public interface AlertNotificationServicePort {
    void sendNotification(Alert alert, NotificationChannel channel, List<String> recipients);
}
```

### REST API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/alerts/rules` | Create alert rule |
| GET | `/alerts/rules` | Get all alert rules |
| GET | `/alerts/rules/{ruleId}` | Get specific alert rule |
| DELETE | `/alerts/rules/{ruleId}` | Delete alert rule |
| GET | `/alerts` | Get alerts with pagination |
| GET | `/alerts/{alertId}` | Get specific alert |
| POST | `/alerts/{alertId}/acknowledge` | Acknowledge alert |
| POST | `/alerts/{alertId}/resolve` | Resolve alert |
| GET | `/alerts/{alertId}/history` | Get alert history |

## Enumerations

### AlertStatus
- `OPEN`: Alert is active and unacknowledged
- `ACKNOWLEDGED`: Alert has been acknowledged by a user
- `RESOLVED`: Alert has been resolved
- `SUPPRESSED`: Alert is suppressed
- `CLOSED`: Alert is closed

### AlertSeverity
- `CRITICAL`: Critical severity requiring immediate action
- `HIGH`: High severity
- `MEDIUM`: Medium severity
- `LOW`: Low severity
- `INFO`: Informational

### NotificationStatus
- `PENDING`: Notification pending
- `SENT`: Notification sent successfully
- `FAILED`: Notification failed
- `RETRYING`: Notification being retried

### ConditionType
- `THRESHOLD`: Simple threshold-based condition
- `ANOMALY_DETECTION`: Anomaly-based detection
- `RATE_OF_CHANGE`: Rate of change condition
- `MISSING_DATA`: Missing data condition
- `PREDICTIVE`: Predictive condition

### ComparisonOperator
- `GREATER_THAN`: Value greater than threshold
- `LESS_THAN`: Value less than threshold
- `EQUAL_TO`: Value equal to threshold
- `NOT_EQUAL_TO`: Value not equal to threshold
- `GREATER_THAN_OR_EQUAL`: Value greater than or equal to threshold
- `LESS_THAN_OR_EQUAL`: Value less than or equal to threshold

### NotificationChannel
- `EMAIL`: Email notification
- `SMS`: SMS notification
- `WEBHOOK`: Webhook notification
- `SLACK`: Slack notification
- `PAGERDUTY`: PagerDuty notification
- `INCIDENT_MANAGEMENT`: Incident management system

## Deployment Architecture

```mermaid
graph LR
    subgraph "Kubernetes Cluster"
        subgraph "Namespace: monitoring"
            A[Alert Management Pod]
            B[MongoDB]
            C[Redis]
        end
    end

    subgraph "External Services"
        D[Kafka]
        E[Email Service]
        F[Slack]
    end

    A --> B
    A --> C
    A --> D
    A --> E
    A --> F
```

## Security Considerations

1. **Multi-tenancy**: All operations are scoped to tenant ID
2. **Authentication**: JWT-based authentication via Spring Security
3. **Authorization**: Role-based access control
4. **Data Isolation**: Tenant data isolation at database level

## Monitoring and Observability

1. **Health Checks**: `/health`, `/health/liveness`, `/health/readiness`
2. **Metrics**: Prometheus metrics via Micrometer
3. **Logging**: Structured logging with correlation IDs
4. **Tracing**: Distributed tracing support
