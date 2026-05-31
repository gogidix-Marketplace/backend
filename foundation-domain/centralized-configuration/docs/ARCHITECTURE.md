# Architecture Documentation

## Overview

The Centralized Configuration domain implements a hexagonal architecture (Ports and Adapters pattern) to provide a robust, scalable configuration management system for the Gogidix Ecosystem.

## Architectural Principles

### Hexagonal Architecture

The system is organized into four distinct layers:

```
+-------------------------------------------+
|          Interface Layer (REST)           |
+-------------------------------------------+
                    |
                    v
+-------------------------------------------+
|          Application Layer                |
|  (Services, DTOs, Mappers, Use Cases)     |
+-------------------------------------------+
                    |
                    v
+-------------------------------------------+
|             Domain Layer                  |
|  (Entities, Value Objects, Ports)         |
+-------------------------------------------+
                    |
                    v
+-------------------------------------------+
|        Infrastructure Layer               |
|  (Persistence, Messaging, External APIs)  |
+-------------------------------------------+
```

### Layer Responsibilities

#### 1. Domain Layer
- **Purpose**: Core business logic and rules
- **Components**:
  - Entities: `Configuration`, `ConfigurationHistory`, `ConfigAuditLog`
  - Value Objects: `ConfigEnvironment`, `AuditAction`
  - Ports (Interfaces): `CreateConfigCommand`, `UpdateConfigCommand`, `GetConfigQuery`
  - Repository Interfaces
- **Dependencies**: None (pure Java)

#### 2. Application Layer
- **Purpose**: Orchestrates business use cases
- **Components**:
  - Services: `ConfigCommandService`, `ConfigQueryService`, `ConfigAuditService`
  - DTOs: Request/Response objects
  - Mappers: `ConfigMapper` (MapStruct)
- **Dependencies**: Domain Layer only

#### 3. Interface Layer
- **Purpose**: External communication
- **Components**:
  - REST Controllers: `ConfigController`, `ConfigAuditController`, `HealthController`
  - OpenAPI/Swagger documentation
- **Dependencies**: Application Layer

#### 4. Infrastructure Layer
- **Purpose**: Technical implementation details
- **Components**:
  - Persistence: PostgreSQL repositories
  - Messaging: Kafka publishers/consumers
  - Configuration: Spring Boot configs
- **Dependencies**: Domain Layer (implements interfaces)

## Service Architecture

### Config Server (Port 8888)

**Responsibility**: Centralized configuration storage and management

```
ConfigController
       |
       v
ConfigCommandService     ConfigQueryService
       |                         |
       v                         v
ConfigEventPublisher   ConfigurationRepository
       |
       v
   Kafka (config-events topic)
```

**Key Features**:
- CQRS pattern for command/query separation
- Version-controlled configuration history
- Encryption support for sensitive values
- Multi-tenant isolation

### Config Audit Service (Port 8892)

**Responsibility**: Complete audit trail for all configuration changes

```
ConfigAuditController
       |
       v
ConfigAuditService
       |
       v
ConfigAuditLogRepository
```

**Key Features**:
- Complete audit trail with user attribution
- IP address and user agent tracking
- Date range queries
- Multi-entity type support (Configuration, FeatureFlag, Environment)

## Data Model

### Configuration Entity

```java
Configuration {
    Long id;
    String tenantId;           // Multi-tenant isolation
    String applicationName;    // Application identifier
    String profile;            // Environment profile
    String configKey;          // Configuration key
    String configValue;        // Configuration value (TEXT)
    Boolean isEncrypted;       // Encryption flag
    Integer version;           // Optimistic locking
    Boolean isActive;          // Soft delete support
    String description;        // Human-readable description
    String createdBy;          // User attribution
    String updatedBy;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
```

### ConfigurationHistory Entity

```java
ConfigurationHistory {
    Long id;
    Long configurationId;      // Reference to Configuration
    String tenantId;
    String applicationName;
    String profile;
    String configKey;
    String oldValue;           // Previous value
    String newValue;           // New value
    Integer version;
    String changeType;         // CREATE, UPDATE, DELETE
    String changedBy;
    String changeReason;
    LocalDateTime createdAt;
}
```

### ConfigAuditLog Entity

```java
ConfigAuditLog {
    Long id;
    String tenantId;
    String entityType;         // CONFIGURATION, FEATURE_FLAG, ENVIRONMENT
    String entityId;
    AuditAction action;        // Enum: CREATE, UPDATE, DELETE, etc.
    String oldValue;
    String newValue;
    String changedBy;
    String userId;
    String userName;
    String userEmail;
    String ipAddress;          // Tracking
    String userAgent;          // Browser/client info
    String changeReason;
    String metadata;           // JSON metadata
    LocalDateTime createdAt;
}
```

## Multi-Tenancy

All services support multi-tenant SaaS architecture:

- **Tenant Isolation**: Every entity includes a `tenantId` field
- **Header-based**: Clients pass `X-Tenant-ID` header
- **Default Tenant**: Falls back to "default" when not specified
- **Access Control**: Services verify tenant ownership before operations

## Event-Driven Architecture

### Kafka Topics

**config-events**:
- Published on configuration CRUD operations
- Payload includes: eventType, tenantId, applicationName, profile, configKey, oldValue, newValue, version
- Key: `{tenantId}-{configKey}` for partitioning
- Consumers: Audit Service, Notification Service, Cache invalidation

### Event Types

| Event Type | Trigger | Purpose |
|------------|---------|---------|
| CONFIG_CREATED | New configuration | Notify dependent services |
| CONFIG_UPDATED | Configuration value changed | Cache invalidation |
| CONFIG_DELETED | Configuration removed | Cleanup dependent resources |

## CQRS Implementation

The Config Server implements Command Query Responsibility Segregation:

### Command Side (Writes)
- `ConfigCommandService`: Handles create, update, delete
- Transactional operations
- Event publishing after state change
- History tracking

### Query Side (Reads)
- `ConfigQueryService`: Handles all queries
- Optimized for read performance
- Pagination support
- No side effects

## Database Design

### Indexes

**configurations table**:
- `idx_config_tenant_id` on `tenant_id`
- `idx_config_application` on `application_name`
- `idx_config_profile` on `profile`
- `idx_config_key` on `config_key`
- `idx_config_created_at` on `created_at`
- Composite index on `(tenant_id, application_name, profile, config_key)`

**config_audit_logs table**:
- `idx_audit_tenant_id` on `tenant_id`
- `idx_audit_type` on `entity_type`
- `idx_audit_action` on `action`
- `idx_audit_created_at` on `created_at`

### Data Types

- **TEXT**: Used for `configValue`, `oldValue`, `newValue`, `metadata` to support large content
- **TIMESTAMP**: `created_at`, `updated_at` with database defaults
- **BOOLEAN**: `isEncrypted`, `isActive` for flags

## Security Architecture

### Encryption
- Application-level encryption for sensitive values
- Configurable via `isEncrypted` flag
- Masked in API responses when encrypted

### Access Control
- Tenant-based isolation
- User attribution for all changes
- IP address tracking

### API Security
- OpenAPI documentation with security schemes
- Tenant ID validation
- Input validation via Jakarta Bean Validation

## Scalability Considerations

### Horizontal Scaling
- Stateless services
- Shared database with connection pooling
- Kafka for distributed events

### Performance Optimizations
- Database indexes on frequently queried columns
- Pagination for large result sets
- Async event publishing

### Caching Strategy
- Redis for feature flag evaluation
- Cache invalidation via Kafka events

## Technology Stack

| Component | Technology | Version |
|-----------|------------|---------|
| Language | Java | 17 |
| Framework | Spring Boot | 3.1.5 |
| Cloud | Spring Cloud | 2022.0.4 |
| Database | PostgreSQL | 15+ |
| Messaging | Apache Kafka | 3.x |
| Caching | Redis | 7.x |
| Mapping | MapStruct | 1.5.5 |
| API Docs | SpringDoc OpenAPI | 2.3.0 |
| Build | Maven | 3.9+ |

## Deployment Architecture

```
                    +------------------+
                    |   API Gateway    |
                    +------------------+
                            |
        +-----------+-------+-------+-----------+
        |           |       |       |           |
+-------v---+ +----v-----+ +v------+ +v---------+ +v----------+
|  Config   | | Feature | |  Env  | | Notification| |   Audit   |
|  Server   | |  Flag   | |Service| |  Service    | |  Service  |
|  :8888    | | :8889   | | :8890 | |    :8891    | |   :8892   |
+-----------+ +---------+ +-------+ +------------+ +-----------+
        |           |       |       |           |
        +-----------+-------+-------+-----------+
                    |
        +-----------+-----------+-----------+
        |           |           |           |
+-------v---+ +----v-----+ +----v-----+ +---v------+
| PostgreSQL| |  Kafka   | |  Redis   | | Docker   |
|           | |          | |          | | Network  |
+-----------+ +----------+ +----------+ +----------+
```

## Testing Strategy

### Unit Tests
- JUnit 5 for test framework
- Mockito for mocking dependencies
- 80%+ code coverage target
- Layer isolation (mock dependencies)

### Integration Tests
- TestContainers for PostgreSQL
- Embedded Kafka for messaging tests
- Spring Boot Test annotations

### Test Organization
```
src/test/java/
├── application/service/    # Service layer tests
├── domain/model/          # Entity tests
├── infrastructure/        # Adapter tests
└── interfaces/rest/       # Controller tests
```

## Monitoring and Observability

### Actuator Endpoints
- `/actuator/health` - Service health
- `/actuator/metrics` - Application metrics
- `/actuator/info` - Application info

### Logging
- Structured logging with SLF4J
- Request/Response logging
- Audit event logging

### Metrics
- Configuration creation/update/delete counts
- Query response times
- Error rates by endpoint
