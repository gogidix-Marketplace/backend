# Infrastructure Database Service - Architecture

## Overview

The Infrastructure Database Service is a Spring Boot-based microservice that provides comprehensive database infrastructure capabilities for the Gogidix ecosystem. It implements hexagonal architecture with Domain-Driven Design (DDD) principles.

## Architecture

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                    REST API Layer                                │
│                   (Controllers + DTOs)                           │
└───────────────────────────┬─────────────────────────────────────┘
                            │
┌───────────────────────────┴─────────────────────────────────────┐
│                    Application Layer                              │
│                      (Services)                                   │
│  ┌───────────────┐  ┌──────────────┐  ┌─────────────────────┐ │
│  │ Connection    │  │    Migration │  │   Tenant Routing   │ │
│  │ Pool Service  │  │    Service   │  │      Service       │ │
│  └───────────────┘  └──────────────┘  └─────────────────────┘ │
│  ┌───────────────┐  ┌──────────────┐                           │
│  │   Query       │  │   Backup     │                           │
│  │ Monitoring    │  │   Service    │                           │
│  └───────────────┘  └──────────────┘                           │
└───────────────────────────┬─────────────────────────────────────┘
                            │
┌───────────────────────────┴─────────────────────────────────────┐
│                      Domain Layer                                 │
│  ┌───────────────┐  ┌──────────────┐  ┌─────────────────────┐ │
│  │ Domain Models │  │ Repositories │  │   Domain Events     │ │
│  └───────────────┘  └──────────────┘  └─────────────────────┘ │
└───────────────────────────┬─────────────────────────────────────┘
                            │
┌───────────────────────────┴─────────────────────────────────────┐
│                   Infrastructure Layer                            │
│  ┌───────────────┐  ┌──────────────┐  ┌─────────────────────┐ │
│  │   MongoDB     │  │    Redis     │  │      Kafka          │ │
│  │   Storage     │  │    Cache     │  │   Messaging         │ │
│  └───────────────┘  └──────────────┘  └─────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

## Domain Models

### ConnectionPoolConfiguration
Manages database connection pool settings with HikariCP integration.

**Key Features:**
- Multi-database support (PostgreSQL, MySQL, Oracle, MSSQL, H2)
- Configurable pool settings (min/max size, timeouts)
- Health check configuration
- Metrics collection

### DatabaseMigration
Tracks database migrations across different tools.

**Key Features:**
- Flyway and Liquibase support
- Custom migration scripts
- Rollback capabilities
- Migration dependencies

### TenantDatabaseConfiguration
Manages multi-tenant database configurations.

**Key Features:**
- Multiple tenancy strategies (database-per-tenant, schema-per-tenant, shared)
- Resource quotas per tenant
- Tenant tiers (Basic, Standard, Premium, Enterprise)
- Automatic provisioning/deprovisioning

### QueryPerformanceMetric
Tracks query execution metrics for optimization.

**Key Features:**
- Query execution time tracking
- Slow query detection
- Full table scan detection
- Optimization suggestions

### DatabaseBackup
Manages database backup operations.

**Key Features:**
- Full, incremental, and differential backups
- Compression and encryption
- Retention policies
- Restore capabilities

### DistributedTransaction
Coordinates distributed transactions across databases.

**Key Features:**
- Two-Phase Commit (2PC) protocol
- Saga pattern for long-running transactions
- Automatic timeout handling
- Compensation logic

## Multi-Tenancy Strategies

### Database Per Tenant
Each tenant gets their own database instance.
- **Pros:** Complete isolation, independent scaling
- **Cons:** Higher resource usage
- **Use Case:** Premium/Enterprise tier

### Schema Per Tenant
Each tenant gets their own schema in a shared database.
- **Pros:** Good isolation, moderate resource usage
- **Cons:** Shared database resources
- **Use Case:** Standard tier

### Shared Database
All tenants share the same database with discriminator columns.
- **Pros:** Minimal resource usage
- **Cons:** Limited isolation, data security concerns
- **Use Case:** Basic tier

## Caching Strategy

### Redis Caching
- Connection pool configurations
- Tenant database configurations
- Query statistics (aggregated)

### Cache Invalidation
- Time-based TTL (5 minutes default)
- Write-through pattern
- Manual invalidation on updates

## Event-Driven Architecture

### Kafka Events

**Topics:**
- `infrastructure.database.connection-pool.events`
- `infrastructure.database.migration.events`
- `infrastructure.database.tenant.events`
- `infrastructure.database.query-metrics.events`
- `infrastructure.database.backup.events`
- `infrastructure.database.transaction.events`

**Event Types:**
- Created/Updated/Deleted
- Status changes
- Alerts (slow queries, failed migrations)
- Health check results

## Security Considerations

1. **Password Encryption**: Database passwords are encrypted at rest
2. **Tenant Isolation**: All tenant-specific operations validate tenant context
3. **Audit Logging**: All configuration changes are logged with user context
4. **Connection Pooling**: Prevents connection leaks and resource exhaustion

## Scalability

### Horizontal Scaling
- Stateless service design
- Redis-backed session state
- Kafka for distributed events

### Database Connections
- Connection pooling limits per tenant
- Automatic connection recycling
- Health check based connection validation

## Monitoring

### Metrics
- Connection pool utilization
- Query execution times
- Migration success rates
- Backup completion status
- Transaction success/failure rates

### Health Checks
- Database connectivity
- Connection pool status
- Cache availability
- Kafka connectivity

## Deployment

### Containerization
- Docker multi-stage build
- Alpine-based runtime image
- Health check endpoints

### Configuration
- Environment-specific profiles (dev, staging, prod)
- Externalized configuration
- Feature flags for experimental features
