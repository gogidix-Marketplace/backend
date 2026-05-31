# Audit Trail Service - Architecture Documentation

## Overview

The Audit Trail Service is a hexagonal (ports and adapters) microservice that provides comprehensive audit logging capabilities for all transaction events within the Gogidix ecosystem. It serves as the centralized source of truth for tracking who did what, when, and how across all distributed transactions.

## Architecture Principles

### Hexagonal Architecture (Ports and Adapters)

The service follows hexagonal architecture principles to maintain a clean separation of concerns:

```
+--------------------------------------------------+
|                Interfaces Layer                  |
|  (REST Controllers, Event Listeners)             |
+--------------------------------------------------+
                       ^
                       |
                       v
+--------------------------------------------------+
|              Application Layer                   |
|  (Services, Mappers, DTOs)                       |
+--------------------------------------------------+
                       ^
                       |
                       v
+--------------------------------------------------+
|                Domain Layer                      |
|  (Entities, Domain Logic, Ports)                 |
+--------------------------------------------------+
                       ^
                       |
                       v
+--------------------------------------------------+
|           Infrastructure Layer                   |
|  (Repositories, Messaging, Config)               |
+--------------------------------------------------+
```

### Layer Responsibilities

#### 1. Domain Layer (`domain/`)
The core business logic, completely independent of external concerns.

- **Entities**: Business objects (`AuditLog`)
- **Ports**: Input and output contracts
  - **Input Ports**: `CreateAuditLogCommand`, `GetAuditLogQuery`, `SearchAuditLogsQuery`
  - **Output Ports**: `AuditLogRepositoryPort`
- **Value Objects**: Enums and domain-specific types
  - `ActorType`: USER, SYSTEM, SERVICE
  - `AuditAction`: CREATE, UPDATE, DELETE, EXECUTE
  - `AuditSeverity`: INFO, WARNING, ERROR, CRITICAL
  - `AuditStatus`: SUCCESS, FAILURE, PENDING

#### 2. Application Layer (`application/`)
Orchestrates business use cases and coordinates domain operations.

- **Services**: Orchestrators for commands and queries
  - `AuditLogCommandService`: Handles write operations
  - `AuditLogQueryService`: Handles read operations
- **DTOs**: Data transfer objects for API communication
  - `CreateAuditLogRequestDto`: Input for creating audit logs
  - `AuditLogResponseDto`: Output representation
  - `PagedAuditLogsResponseDto`: Paginated search results
- **Mappers**: Converts between DTOs and domain entities
  - `AuditLogMapper`: MapStruct-based mapping

#### 3. Infrastructure Layer (`infrastructure/`)
External concerns and technical implementations.

- **Persistence**: Database access implementations
  - `PostgresAuditLogRepository`: PostgreSQL implementation
  - `AuditJpaRepository`: Spring Data JPA interface
- **Messaging**: Event publishing
  - `AuditEventPublisher`: Kafka event publisher
  - `KafkaProducerConfig` / `KafkaConsumerConfig`: Kafka configuration
- **Configuration**: Application configuration
  - `ApplicationConfig`: General bean configurations
  - `PostgreSQLConfig`: Database-specific settings
  - `RedisConfig`: Caching configuration

#### 4. Interfaces Layer (`interfaces/`)
Entry points for external communication.

- **REST Controllers**: HTTP API endpoints
  - `AuditLogController`: Main REST API

## Domain Model

### AuditLog Aggregate Root

```java
@Entity
@Table(name = "audit_logs")
public class AuditLog extends BaseEntity {
    private UUID id;
    private String tenantId;
    private String entityType;
    private String entityId;
    private String action;
    private String actorId;
    private String actorType;
    private String ipAddress;
    private String userAgent;
    private String correlationId;
    private LocalDateTime timestamp;
    private String oldState;
    private String newState;
    private String changedFields;
    private String businessContext;
    private String severity;
    private String category;
    private String description;
    private String status;
    private String errorMessage;
    private String sessionId;
    private String requestId;
}
```

### BaseEntity Fields

All entities inherit common fields from `BaseEntity`:

- `tenantId`: Multi-tenancy isolation
- `createdAt` / `updatedAt`: Automatic timestamps
- `createdBy` / `updatedBy`: Audit tracking
- `version`: Optimistic locking
- `isActive` / `isDeleted`: Soft delete support

## Key Design Patterns

### 1. Repository Pattern

Domain ports define the contract, infrastructure provides the implementation:

```java
// Domain Port
public interface AuditLogRepository {
    AuditLog save(AuditLog auditLog);
    Optional<AuditLog> findById(UUID id);
    void delete(AuditLog auditLog);
}

// Infrastructure Implementation
@Repository
public class PostgresAuditLogRepository implements AuditLogRepository {
    private final AuditJpaRepository jpaRepository;
    // Implementation details
}
```

### 2. CQRS (Command Query Responsibility Segregation)

Separate services for writes and reads:

- **Command Service**: Handles `CREATE`, `DELETE` operations
- **Query Service**: Handles `SEARCH`, `GET` operations

### 3. DTO Pattern

Clear separation between API contracts and domain models:

- Request DTOs validate incoming data
- Response DTOs control output representation
- Mappers handle bidirectional conversion

### 4. Builder Pattern

All entities and DTOs use Lombok `@Builder` for:

- Immutable object creation
- Clear, readable instantiation
- Optional parameter handling

## Data Flow

### Creating an Audit Log

```
1. HTTP POST /api/v1/audit-logs
   |
   v
2. AuditLogController.createAuditLog()
   |
   v
3. AuditLogCommandService.createAuditLog()
   |
   v
4. AuditLogMapper.toEntity()
   |
   v
5. AuditLogRepository.save()
   |
   v
6. PostgresAuditLogRepository (Infrastructure)
   |
   v
7. Database (PostgreSQL)
```

### Searching Audit Logs

```
1. HTTP GET /api/v1/audit-logs?tenantId=xxx
   |
   v
2. AuditLogController.searchAuditLogs()
   |
   v
3. AuditLogQueryService.searchAuditLogs()
   |
   v
4. AuditLogRepositoryPort.searchAuditLogs()
   |
   v
5. PostgresAuditLogRepository (Infrastructure)
   |
   v
6. AuditJpaRepository.searchAuditLogs() (JPQL)
   |
   v
7. Database (PostgreSQL)
```

## Caching Strategy

Redis is used for caching frequently accessed audit logs:

- Cache TTL: 10 minutes
- Key format: `audit:{tenantId}:{entityType}:{entityId}`
- Null values are not cached

## Event Publishing

Critical audit events are published to Kafka:

- **Topic**: `audit-events`
- **Key**: Audit log ID (UUID)
- **Value**: Serialized AuditLog as JSON
- **Pattern**: Fire-and-forget (async)

## Multi-Tenancy

The service supports multi-tenancy through:

1. **Tenant ID Column**: Every audit log has a `tenantId`
2. **Isolation**: All queries filter by tenant
3. **Indexing**: `tenantId` is indexed for performance

## Security Considerations

1. **Input Validation**: All DTOs use Jakarta Validation
2. **SQL Injection**: JPA parameterized queries prevent injection
3. **Data Isolation**: Tenant-based data separation
4. **Audit Trails**: All changes tracked with actor information

## Scalability

### Horizontal Scaling

- **Stateless Services**: Application logic is stateless
- **Shared Database**: Multiple instances can share the same database
- **Connection Pooling**: Efficient database connection management

### Performance Optimization

1. **Database Indexing**: Strategic indexes on frequently queried columns
2. **Caching**: Redis for hot data
3. **Pagination**: All list queries support pagination
4. **Async Events**: Kafka publishing is non-blocking

## Technology Stack

| Component | Technology |
|-----------|-----------|
| Framework | Spring Boot 3.1.5 |
| Language | Java 17 |
| Database | PostgreSQL 16 |
| Cache | Redis |
| Messaging | Apache Kafka |
| ORM | Spring Data JPA (Hibernate) |
| API Documentation | OpenAPI 3.0 (SpringDoc) |
| Build Tool | Maven |
| Testing | JUnit 5, Mockito, TestContainers, ArchUnit |

## Dependency Diagram

```
Interfaces --> Application --> Domain
                      ^          ^
                      |          |
                      v          v
                 Infrastructure
```

## Deployment Architecture

```
+-------------------+     +-------------------+
|   Client Apps     |---->|   API Gateway     |
+-------------------+     +-------------------+
                                      |
                                      v
+-------------------+     +-------------------+
|  Audit Trail Svc  |<----|   Service Mesh    |
|  Instance 1       |     +-------------------+
+-------------------+
+-------------------+            |
|  Audit Trail Svc  |<-----------+
|  Instance 2       |
+-------------------+
          |
          v
+-------------------+
|   PostgreSQL      |
+-------------------+
+-------------------+
|      Redis        |
+-------------------+
+-------------------+
|      Kafka        |
+-------------------+
```

## Monitoring and Observability

### Actuator Endpoints

- `/actuator/health`: Health check
- `/actuator/info`: Application information
- `/actuator/metrics`: Prometheus metrics

### Key Metrics

- Request latency (p50, p95, p99)
- Request rate (per endpoint)
- Error rate
- Database connection pool usage
- Cache hit/miss ratio

## Error Handling

### Exception Hierarchy

```
IllegalArgumentException
    |
    +-- AuditLogNotFoundException
    +-- InvalidTenantException
```

### Error Response Format

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Audit log not found: 123e4567-e89b-12d3-a456-426614174000",
  "path": "/api/v1/audit-logs/123e4567-e89b-12d3-a456-426614174000"
}
```

## Future Enhancements

1. **Elasticsearch Integration**: Full-text search on audit logs
2. **Data Archival**: Automatic archival of old audit logs
3. **Export Capabilities**: CSV/PDF export for compliance
4. **Real-time Streaming**: WebSocket support for live audit feeds
5. **Analytics Dashboard**: Built-in analytics and reporting
6. **GDPR Compliance**: Right to be forgotten implementation
7. **Signature Verification**: Cryptographic signing of critical logs
