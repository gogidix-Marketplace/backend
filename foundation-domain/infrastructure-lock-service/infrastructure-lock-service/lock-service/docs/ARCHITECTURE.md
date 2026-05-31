# Infrastructure Lock Service - Architecture

## Overview

The Infrastructure Lock Service is a distributed locking solution built with Spring Boot and Redis, providing multi-tenant resource locking capabilities for the Gogidix ecosystem. It follows clean architecture principles with hexagonal (ports and adapters) design patterns.

## System Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                          API Layer                                    │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │              REST Controllers (Interfaces)                   │    │
│  │  - LockController                                           │    │
│  │  - DTOs & Request/Response Mappers                          │    │
│  └─────────────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│                       Application Layer                              │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │                     Services                                 │    │
│  │  - DistributedLockService (Core business logic)              │    │
│  │  - LockCleanupService (Scheduled cleanup)                    │    │
│  └─────────────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│                         Domain Layer                                 │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │                      Models                                   │    │
│  │  - Lock, LockRequest, LockStatus, LockType                   │    │
│  │  - LockAcquisitionResult, LockReleaseResult                  │    │
│  │  - LockStatistics                                            │    │
│  └─────────────────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │                  Repository Interfaces                       │    │
│  │  - LockRepository                                           │    │
│  │  - LockStatisticsRepository                                 │    │
│  └─────────────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    Infrastructure Layer                              │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │                    Redis Adapters                            │    │
│  │  - RedisLockRepository (Lua scripts for atomicity)          │    │
│  │  - RedisLockStatisticsRepository                             │    │
│  └─────────────────────────────────────────────────────────────┘    │
│  ┌─────────────────────────────────────────────────────────────┐    │
│  │                   Configuration                              │    │
│  │  - RedisConfiguration, ResilienceConfiguration              │    │
│  │  - OpenApiConfiguration                                     │    │
│  └─────────────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────────────┘
```

## Key Design Patterns

### 1. Hexagonal Architecture (Ports and Adapters)

- **Ports**: Repository interfaces define contracts for external dependencies
- **Adapters**: Redis implementations provide concrete persistence
- **Benefits**: Testability, replaceable infrastructure, clean separation of concerns

### 2. Repository Pattern

Domain repositories define the contract for lock operations without exposing implementation details:

```java
public interface LockRepository {
    LockAcquisitionResult acquireLock(LockRequest request);
    LockReleaseResult releaseLock(String tenantId, String resourceKey, ...);
    boolean extendLock(...);
    Optional<Lock> findLock(...);
    // ...
}
```

### 3. Builder Pattern

Complex objects use builders for fluent construction:

```java
Lock lock = Lock.builder(tenantId, resourceKey, holderId)
    .lockType(LockType.EXCLUSIVE)
    .ttlSeconds(300L)
    .build();
```

### 4. Result Objects

Operations return rich result objects instead of throwing exceptions:

```java
LockAcquisitionResult result = lockService.acquireLock(...);
if (result.isAcquired()) {
    // Success handling
} else {
    // Error handling via result.getErrorMessage()
}
```

## Distributed Lock Implementation

### Redis-based Locking

The service uses Redis with Lua scripts for atomic lock operations:

1. **Acquire Lock**: Atomic SET NX EX operation via Lua script
2. **Release Lock**: Verify holder then delete atomically
3. **Extend TTL**: Verify holder then extend atomically

### Multi-Tenant Isolation

Locks are isolated by tenant using Redis key prefixes:

```
lock:{tenantId}:{resourceKey}
```

Each tenant has separate lock namespaces, ensuring complete isolation.

### Lock Types

| Type | Description | Concurrency |
|------|-------------|-------------|
| EXCLUSIVE | Only one holder allowed | No |
| SHARED | Multiple readers allowed | Yes |
| WRITE | Single writer, no concurrent readers/writers | No |
| READ | Multiple concurrent reads allowed | Yes |

## Retry Mechanism

The service uses Resilience4j for configurable retry:

- Configurable max attempts
- Exponential backoff support
- Retry on specific exceptions
- Wait timeout for blocking acquisition

### Retry Flow

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│ Acquire     │───▶│ Failed?     │───▶│ Retry       │
│ Lock        │    │ Wait & Retry│    │ (max N)     │
└─────────────┘    └─────────────┘    └─────────────┘
       │                                      │
       ▼                                      │
  ┌─────────────┐                            │
  │ Success     │◀───────────────────────────┘
  └─────────────┘
```

## Scalability

### Horizontal Scaling

- **Stateless Service**: Application instances are stateless
- **Redis Cluster**: Redis can be scaled using cluster mode
- **Connection Pooling**: Lettuce connection pool for Redis connections

### Performance Optimizations

1. **Lua Scripts**: Reduce network round-trips for atomic operations
2. **Connection Pooling**: Reuse Redis connections
3. **Async Operations**: Support for non-blocking operations
4. **Statistics Caching**: In-memory caching for frequently accessed metrics

## Reliability

### High Availability

- **Redis Sentinel/Cluster**: Automatic failover
- **Graceful Shutdown**: Proper resource cleanup on shutdown
- **Health Checks**: Actuator endpoints for health monitoring

### Data Consistency

- **Atomic Operations**: Lua scripts ensure atomicity
- **Version Fields**: Optimistic locking support
- **Expiration Handling**: Automatic TTL management

## Security

### Multi-Tenant Security

- **Tenant Isolation**: Complete data separation by tenant
- **Header-based Tenancy**: X-Tenant-ID header for tenant context
- **Resource-scoped Operations**: All operations scoped to tenant

### Redis Security

- **Password Authentication**: Redis password support
- **TLS/SSL**: Encrypted connections to Redis
- **Network Isolation**: Private network deployment

## Monitoring

### Metrics

- **Active Locks**: Current active lock count per tenant
- **Failed Attempts**: Lock acquisition failures
- **Average Times**: Acquisition and hold times
- **Peak Concurrency**: Maximum concurrent locks

### Health Checks

- **Liveness**: Service is running
- **Readiness**: Redis connection is available
- **Custom Metrics**: Lock-specific metrics via Micrometer

## Deployment

### Container Deployment

```yaml
# Docker Compose example
services:
  lock-service:
    image: gogidix/lock-service:latest
    ports:
      - "8080:8080"
    environment:
      - REDIS_HOST=redis
      - REDIS_PORT=6379
    depends_on:
      - redis

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
```

### Kubernetes Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: lock-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: lock-service
  template:
    metadata:
      labels:
        app: lock-service
    spec:
      containers:
      - name: lock-service
        image: gogidix/lock-service:latest
        ports:
        - containerPort: 8080
        env:
        - name: REDIS_HOST
          value: "redis-service"
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
        readinessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
```

## Technology Stack

| Component | Technology |
|-----------|------------|
| Framework | Spring Boot 3.1.5 |
| Language | Java 17 |
| Build Tool | Maven |
| Database | Redis 7.x |
| API Docs | SpringDoc OpenAPI |
| Monitoring | Spring Boot Actuator |
| Retry | Resilience4j |
| Testing | JUnit 5, Mockito, Testcontainers |

## Future Enhancements

1. **Lock Leasing**: Automatic lock renewal for long-running operations
2. **Lock Hierarchies**: Parent-child lock relationships
3. **Deadlock Detection**: Automatic deadlock detection and resolution
4. **Event Streaming**: Kafka integration for lock events
5. **Multi-Region**: Cross-region lock coordination
6. **GraphQL API**: Alternative to REST API
