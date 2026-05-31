# Infrastructure Config Service - Architecture

## Overview

The Infrastructure Config Service provides centralized configuration management for the Gogidix ecosystem. It implements a hexagonal architecture with Domain-Driven Design (DDD) principles, ensuring clean separation of concerns and maintainability.

## Architecture Principles

### Hexagonal Architecture (Ports and Adapters)

```
┌─────────────────────────────────────────────────────────────┐
│                     Interfaces Layer                        │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────┐ │
│  │ REST Controllers│  │  Event Listeners│  │  Scheduler  │ │
│  └─────────────────┘  └─────────────────┘  └─────────────┘ │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                   Application Layer                         │
│  ┌──────────────────┐  ┌──────────────────┐  ┌───────────┐ │
│  │ ConfigService    │  │ FeatureFlagSvc   │  │ SecretSvc │ │
│  └──────────────────┘  └──────────────────┘  └───────────┘ │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                      Domain Layer                           │
│  ┌──────────────┐  ┌──────────────┐  ┌─────────────────┐  │
│  │ Config Model │  │ FeatureFlag  │  │    Secret       │  │
│  └──────────────┘  └──────────────┘  └─────────────────┘  │
│  ┌──────────────────────────────────────────────────────┐  │
│  │              Repository Interfaces                    │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                 Infrastructure Layer                        │
│  ┌──────────────┐  ┌──────────────┐  ┌─────────────────┐  │
│  │ MongoDB Repo │  │ Redis Cache  │  │  Kafka Producer │  │
│  └──────────────┘  └──────────────┘  └─────────────────┘  │
│  ┌──────────────────────────────────────────────────────┐  │
│  │              Encryption Service                        │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

## Domain Model

### ConfigurationProperty

Represents a configuration property with:
- **Tenant-scoped isolation**: Multi-tenancy support
- **Environment-specific values**: dev, staging, prod
- **Type-safe values**: STRING, INTEGER, BOOLEAN, DOUBLE, JSON
- **Validation rules**: Regex, range, length, allowed values
- **Versioning**: Complete audit trail

### FeatureFlag

Enables dynamic feature toggling with:
- **Multiple rollout strategies**: ALL_USERS, PERCENTAGE, WHITELIST, CONDITIONAL, GRADUAL, AB_TEST
- **Sticky assignments**: Consistent user experience
- **Condition-based evaluation**: User attributes, location, segments
- **Priority ordering**: Higher priority flags evaluated first
- **Expiration support**: Time-based flag lifecycle

### Secret

Secure storage for sensitive data with:
- **AES-256 encryption**: All values encrypted at rest
- **Access control lists**: User-level permissions
- **Automatic rotation**: Configurable rotation intervals
- **Audit logging**: Complete access tracking
- **Expiration handling**: Time-based secret lifecycle

### ConfigVersion

Version history for all configuration changes:
- **Complete audit trail**: Before/after values
- **Change tracking**: Created, Updated, Deleted, Enabled, Disabled, Rotated, Rolled Back
- **Rollback support**: Restore any previous version
- **Change attribution**: Who changed what and when

## Key Design Patterns

### 1. Repository Pattern

Abstracts data access behind interfaces:
```java
public interface ConfigurationPropertyRepository
    extends MongoRepository<ConfigurationProperty, String> {
    Optional<ConfigurationProperty> findByTenantIdAndKey(
        String tenantId, String key);
    // ... custom queries
}
```

### 2. CQRS (Command Query Responsibility Segregation)

Separate operations for:
- **Commands**: Create, Update, Delete, Toggle
- **Queries**: Get, Search, Evaluate

### 3. Event-Driven Architecture

Kafka events for:
- Configuration changes
- Feature flag toggles
- Secret rotations
- Version updates

### 4. Cache-Aside Pattern

Redis caching for:
- Configuration values (5-minute TTL)
- Feature flag evaluations (1-minute TTL)
- Secret values (2-minute TTL)

### 5. Strategy Pattern

Rollout strategies for feature flags:
- Each strategy encapsulates evaluation logic
- Easy to add new strategies
- Pluggable architecture

## Security Architecture

### Encryption

```
┌────────────────────────────────────────────────────────┐
│                    Application                          │
│  ┌─────────────────────────────────────────────────┐  │
│  │  ConfigurationProperty (plaintext)               │  │
│  └─────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────┘
                         │
                         ▼
┌────────────────────────────────────────────────────────┐
│              Encryption Service                        │
│  - AES-256/GCM encryption                              │
│  - SHA-256 key derivation                              │
│  - Random IV per encryption                            │
└────────────────────────────────────────────────────────┘
                         │
                         ▼
┌────────────────────────────────────────────────────────┐
│                  MongoDB                               │
│  Secret.encryptedValue (ciphertext)                    │
└────────────────────────────────────────────────────────┘
```

### Access Control

1. **Tenant Isolation**: All queries scoped to tenant ID
2. **ACL for Secrets**: User-level access control
3. **Header-based Auth**: X-Tenant-ID, X-User-ID headers

## Data Flow

### Configuration Read Flow

```
Client Request
    │
    ▼
Check Redis Cache ─────► Hit ─────► Return Cached Value
    │                                              │
    Miss                                           │
    │                                              │
    ▼                                              │
Query MongoDB                                     │
    │                                              │
    ▼                                              │
Apply Business Logic                               │
    │                                              │
    ▼                                              │
Encrypt if Sensitive                               │
    │                                              │
    ▼                                              │
Store in Redis                                     │
    │                                              │
    ▼                                              │
Return Result ◄─────────────────────────────────────┘
```

### Configuration Write Flow

```
Client Request
    │
    ▼
Validate Request
    │
    ▼
Create Version Record (Snapshot)
    │
    ▼
Encrypt if Sensitive
    │
    ▼
Save to MongoDB
    │
    ▼
Invalidate Redis Cache
    │
    ▼
Publish Kafka Event
    │
    ▼
Return Response
```

## Technology Stack

| Layer | Technology |
|-------|-----------|
| Application | Spring Boot 3.1.5 |
| Persistence | MongoDB (via Spring Data MongoDB) |
| Cache | Redis (via Spring Data Redis) |
| Events | Apache Kafka |
| Documentation | SpringDoc OpenAPI |
| Testing | JUnit 5, Mockito, Testcontainers |
| Build | Maven |
| Containerization | Docker |

## Multi-Tenancy

All domain models support tenant isolation:
- **Tenant ID**: Required field on all entities
- **Scoped Queries**: All repository queries filter by tenant
- **Indexing**: Tenant ID indexed for performance
- **Data Isolation**: Complete logical separation

## Configuration Versioning

Every change creates a version record:
```
ConfigVersion {
    configId: String,
    version: Integer,
    previousValue: String,
    newValue: String,
    changeType: ChangeType,
    changedBy: String,
    changedAt: LocalDateTime,
    snapshot: Map<String, Object>
}
```

This enables:
- **Full audit trail**: Complete change history
- **Rollback**: Restore any previous version
- **Diff visualization**: See what changed

## Performance Considerations

### Caching Strategy

| Data Type | TTL | Eviction |
|-----------|-----|----------|
| Configuration | 5 minutes | LRU |
| Feature Flags | 1 minute | LRU |
| Secrets | 2 minutes | LRU |

### Database Indexing

Indexed fields:
- tenantId (all collections)
- key/flagKey/secretKey (unique per tenant)
- environment
- isActive
- secretType
- expiresAt

### Query Optimization

- Covered queries where possible
- Projection for large documents
- Aggregation for complex searches
- Batch operations for bulk updates

## Scalability

### Horizontal Scaling

Stateless design enables horizontal scaling:
- Multiple instances behind load balancer
- Shared Redis cache
- Shared MongoDB cluster

### Database Sharding

MongoDB sharding strategies:
- Shard key: tenantId
- Even data distribution
- Tenant isolation at shard level

## Monitoring

### Actuator Endpoints

- `/actuator/health` - Health checks
- `/actuator/metrics` - Application metrics
- `/actuator/prometheus` - Prometheus scraping
- `/actuator/info` - Application info

### Key Metrics

- Configuration request rate
- Cache hit/miss ratio
- Feature flag evaluation latency
- Secret access frequency
- Database query performance

## Deployment Architecture

```
                    ┌─────────────────┐
                    │   Load Balancer │
                    └────────┬────────┘
                             │
            ┌────────────────┼────────────────┐
            │                │                │
    ┌───────▼──────┐ ┌──────▼──────┐ ┌──────▼──────┐
    │   Instance 1 │ │  Instance 2 │ │  Instance 3 │
    └───────┬──────┘ └──────┬──────┘ └──────┬──────┘
            │                │                │
            └────────────────┼────────────────┘
                             │
            ┌────────────────┼────────────────┐
            │                │                │
    ┌───────▼──────┐ ┌──────▼──────┐ ┌──────▼──────┐
    │    Redis     │ │   MongoDB   │ │   Kafka     │
    │    Cluster   │ │   Cluster   │ │   Cluster   │
    └──────────────┘ └─────────────┘ └─────────────┘
```
