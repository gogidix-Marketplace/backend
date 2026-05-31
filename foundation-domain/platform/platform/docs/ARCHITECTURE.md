# Platform Architecture Documentation

## Overview

The Platform domain provides core infrastructure services for the Gogidix ecosystem. It implements multi-tenancy, subscription management, usage metering, and platform configuration capabilities following Domain-Driven Design (DDD) principles.

## Services

### 1. Platform Service
**Path:** `Backend/Java/platform-service`

**Responsibilities:**
- Feature flag management for controlled feature rollouts
- Platform configuration management
- Maintenance window scheduling
- Platform announcements
- Service health monitoring

**Key Entities:**
- `FeatureFlag` - Progressive rollout, A/B testing, segment targeting
- `PlatformConfiguration` - Tenant-specific configuration storage
- `MaintenanceWindow` - Scheduled downtime management
- `PlatformAnnouncement` - System-wide notifications
- `ServiceHealthStatus` - Health monitoring for all services

### 2. Subscription Service
**Path:** `Backend/Java/subscription-service`

**Responsibilities:**
- Subscription lifecycle management
- Plan management and billing cycles
- Invoice generation
- Payment transaction tracking
- Usage-based billing

**Key Entities:**
- `Subscription` - Customer subscriptions with trial, active, suspended states
- `SubscriptionPlan` - Available subscription tiers
- `Invoice` - Billing invoices
- `PaymentTransaction` - Payment records
- `PaymentMethod` - Customer payment methods
- `UsageRecord` - Usage-based billing data

### 3. Tenant Registry Service
**Path:** `Backend/Java/tenant-registry-service`

**Responsibilities:**
- Multi-tenant organization management
- Tenant provisioning and lifecycle
- API key management
- Tenant resource allocation
- Custom domain and branding

**Key Entities:**
- `Tenant` - Multi-tenant organization root entity
- `TenantApiKey` - API authentication keys
- `TenantDatabase` - Database isolation per tenant
- `TenantStorage` - Storage allocation tracking
- `TenantActivity` - Tenant activity logs
- `TenantResourceUsage` - Resource consumption metrics

### 4. Usage Metering Service
**Path:** `Backend/Java/usage-metering-service`

**Responsibilities:**
- Real-time usage tracking
- Metric aggregation
- Quota enforcement
- Usage alerting
- Metric definition management

**Key Entities:**
- `UsageRecord` - Raw usage events
- `UsageAggregate` - Aggregated usage data
- `MetricDefinition` - Metric schema definitions
- `QuotaDefinition` - Per-tenant quota limits
- `QuotaUsage` - Current quota consumption
- `QuotaAlert` - Threshold-based alerts

## Architectural Patterns

### Domain-Driven Design (DDD)

Each service follows DDD principles with clear layer separation:

```
interfaces/rest/     - REST controllers (input adapters)
application/         - Application services and DTOs
domain/              - Domain models and business logic
  - model/           - Domain entities
  - port/in/         - Input ports (commands)
  - port/out/        - Output ports (repositories)
infrastructure/      - External concerns (persistence, messaging)
```

### Hexagonal Architecture

Services use hexagonal (ports and adapters) architecture:

- **Input Ports:** Commands (CreateFeatureFlagCommand, etc.)
- **Output Ports:** Repository interfaces
- **Adapters:** REST controllers, JPA repositories

### Multi-Tenancy

Data isolation achieved through:
1. **Tenant ID** column on all entities
2. **Row-Level Security (RLS)** in PostgreSQL
3. **Request context** injection for tenant identification
4. **Tenant-scoped caching**

### Event-Driven Communication

Services communicate asynchronously via Kafka:

- `tenant-created` - New tenant provisioning
- `subscription-activated` - Subscription changes
- `usage-recorded` - Usage events
- `quota-exceeded` - Alert notifications

## Technology Stack

### Backend
- **Java 17** - Runtime platform
- **Spring Boot 3.1.5** - Application framework
- **Spring Data JPA** - ORM
- **PostgreSQL** - Primary database
- **Redis** - Caching layer
- **Kafka** - Message broker

### Shared Libraries
- `shared-security` - Authentication/authorization
- `shared-model` - Common domain models
- `shared-exceptions` - Standard exception handling
- `shared-validation` - Bean validation extensions
- `shared-audit` - Audit logging
- `shared-messaging` - Event publishing
- `shared-utilities` - Common utilities
- `shared-testing` - Test fixtures

### Build & Quality
- **Maven** - Dependency management
- **MapStruct 1.5.5** - DTO mapping
- **Lombok** - Code generation
- **JUnit 5** - Unit testing
- **Mockito** - Test mocking
- **ArchUnit** - Architecture testing
- **Testcontainers** - Integration testing

## Deployment

### Container Strategy
Each service packages as a Docker container:
- Multi-stage build for optimized image size
- JAR瘦身通过分层依赖
- Non-root user execution
- Health check endpoints

### Database Schema
- Flyway migrations in `src/main/resources/db/migration`
- Versioned migration scripts
- Tenant-specific schemas with RLS policies

### Configuration
- Externalized via Spring Cloud Config or environment variables
- Tenant-specific overrides supported
- Feature flags for runtime behavior control

## Security

### Authentication
- JWT tokens via shared-security library
- API key authentication for service-to-service
- OAuth 2.0 integration ready

### Authorization
- Role-based access control (RBAC)
- Tenant-scoped permissions
- Resource-level authorization

### Data Protection
- Sensitive configuration encrypted at rest
- TLS for all inter-service communication
- Audit logging for all mutations

## Scaling

### Horizontal Scaling
- Stateless service instances
- Sticky sessions not required
- Distributed caching via Redis

### Database Scaling
- Read replicas for queries
- Connection pooling per tenant
- Partitioning for large tables (usage_records)

### Caching Strategy
- Feature flags cached with TTL
- Configuration cached with invalidation
- Aggregate usage cached pre-computed

## Monitoring

### Metrics
- Micrometer integration
- Prometheus-compatible metrics
- Per-tenant resource usage

### Logging
- Structured JSON logging
- Correlation ID propagation
- Tenant context injection

### Tracing
- OpenTelemetry integration
- Distributed tracing across services
- Performance bottleneck identification

## Development Guidelines

### Adding New Features
1. Define domain model in `domain/model/`
2. Create input port in `domain/port/in/`
3. Define repository interface in `domain/repository/`
4. Implement application service in `application/service/`
5. Create REST controller in `interfaces/rest/`
6. Add comprehensive tests (80%+ coverage)

### Testing
- Unit tests for domain logic
- Integration tests for repositories
- Controller tests for REST endpoints
- Architecture tests for layer compliance

### Code Quality
- JaCoCo for coverage reporting (80% minimum)
- Checkstyle for code standards
- SpotBugs for static analysis
- OWASP dependency check
