# Country Support Dashboard Service - Architecture Documentation

## Overview

The Country Support Dashboard Service is a Spring Boot microservice that provides country-specific support metrics and regional statistics for the Customer Support domain. It enables monitoring and analytics of support operations across different countries and regions.

## Technology Stack

- **Framework**: Spring Boot 3.2.0
- **Java Version**: 17
- **Database**: MongoDB
- **Build Tool**: Maven
- **API Documentation**: OpenAPI 3.0 (SpringDoc)
- **Testing**: JUnit 5, Mockito, Spring Boot Test

## Architecture Pattern

The service follows **Hexagonal Architecture** (Ports and Adapters) with clear separation of concerns:

```mermaid
graph TB
    subgraph "Interface Layer"
        Controller[CountrySupportDashboardController]
    end

    subgraph "Application Layer"
        Service[CountrySupportDashboardService]
        Mapper1[CountrySpecificMetricsMapper]
        Mapper2[RegionalTicketStatsMapper]
    end

    subgraph "Domain Layer"
        Model1[CountrySpecificMetrics]
        Model2[RegionalTicketStats]
        Repo1[CountrySpecificMetricsRepository]
        Repo2[RegionalTicketStatsRepository]
    end

    subgraph "Infrastructure Layer"
        MongoConfig[MongoConfig]
        WebConfig[WebConfig]
        OpenApiConfig[OpenApiConfig]
    end

    subgraph "Shared Layer"
        RequestContext[RequestContext]
        RequestContextHolder[RequestContextHolder]
    end

    Controller --> Service
    Service --> Mapper1
    Service --> Mapper2
    Service --> Repo1
    Service --> Repo2
    Mapper1 --> Model1
    Mapper2 --> Model2
    Controller --> RequestContext
    Service --> RequestContextHolder
```

## Layer Structure

### 1. Interface Layer (`interfaces.rest`)

**CountrySupportDashboardController**
- REST API endpoints for country-specific metrics and regional statistics
- Request validation using `@Valid`
- OpenAPI documentation annotations

### 2. Application Layer (`application`)

#### Service (`application.service`)
- **CountrySupportDashboardService**: Business logic for managing metrics and statistics

#### Mapper (`application.mapper`)
- **CountrySpecificMetricsMapper**: DTO to Entity mapping for country metrics
- **RegionalTicketStatsMapper**: DTO to Entity mapping for regional stats

#### DTO (`application.dto`)
- **Request DTOs**: Data transfer objects for incoming requests
- **Response DTOs**: Data transfer objects for API responses

### 3. Domain Layer (`domain`)

#### Models (`domain.model`)
- **CountrySpecificMetrics**: Entity representing country-level support metrics
- **RegionalTicketStats**: Entity representing regional ticket statistics
- **BaseEntity**: Abstract base entity with common fields

#### Repositories (`domain.repository`)
- **CountrySpecificMetricsRepository**: MongoDB repository for country metrics
- **RegionalTicketStatsRepository**: MongoDB repository for regional stats

### 4. Infrastructure Layer (`infrastructure`)

#### Configuration (`infrastructure.config`)
- **MongoConfig**: MongoDB configuration with auditing
- **WebConfig**: Web MVC configuration
- **OpenApiConfig**: OpenAPI/Swagger configuration

### 5. Shared Layer (`shared`)

- **RequestContext**: Tenant-scoped request context
- **RequestContextHolder**: Thread-local context holder

## Data Model

### CountrySpecificMetrics

```mermaid
erDiagram
    CountrySpecificMetrics ||--o| BusinessHours : contains
    CountrySpecificMetrics {
        string id PK
        string tenantId FK
        string countryCode
        string countryName
        date metricDate
        int totalTickets
        int openTickets
        int resolvedTickets
        int escalatedTickets
        double averageResolutionTimeMinutes
        double averageResponseTimeMinutes
        double customerSatisfactionScore
        int activeAgents
        map ticketVolumeByChannel
        map ticketVolumeByPriority
        map ticketVolumeByCategory
        double slaComplianceRate
        double firstContactResolutionRate
        map peakHours
        string region
        string language
        string timezone
        instant createdAt
        instant updatedAt
    }

    BusinessHours {
        string startTime
        string endTime
        string timezone
        list workingDays
    }
```

### RegionalTicketStats

```mermaid
erDiagram
    RegionalTicketStats ||--o| CountryTicketSummary : contains
    RegionalTicketStats {
        string id PK
        string tenantId FK
        string regionName
        list countryCodes
        date statDate
        int totalTickets
        int newTickets
        int closedTickets
        int pendingTickets
        map ticketsByCountry
        map ticketsByStatus
        map ticketsByPriority
        map ticketsByChannel
        double averageResolutionTimeMinutes
        double averageResponseTimeMinutes
        double customerSatisfactionScore
        double slaComplianceRate
        double changePercentage
        int activeAgents
        instant createdAt
        instant updatedAt
    }

    CountryTicketSummary {
        string countryCode
        string countryName
        int totalTickets
        int openTickets
        int resolvedTickets
        double satisfactionScore
    }
```

## Multi-Tenancy

The service supports multi-tenancy through tenant-scoped data isolation:

1. **Tenant Context**: `RequestContextHolder` maintains the current tenant ID in a ThreadLocal
2. **Data Isolation**: All database queries filter by tenant ID
3. **Security**: Cross-tenant data access is prevented at the repository level

## API Flow

```mermaid
sequenceDiagram
    participant Client
    participant Controller
    participant Filter
    participant Service
    participant Repository
    participant MongoDB

    Client->>Filter: HTTP Request
    Filter->>Filter: Extract tenant from header
    Filter->>RequestContextHolder: Set tenant context
    Filter->>Controller: Forward request

    Controller->>Service: Business method call
    Service->>RequestContextHolder: Get tenant ID
    Service->>Repository: Query with tenant filter
    Repository->>MongoDB: MongoDB query
    MongoDB-->>Repository: Result
    Repository-->>Service: Domain entities
    Service->>Service: Map to DTOs
    Service-->>Controller: Response DTOs
    Controller-->>Client: HTTP Response

    Filter->>RequestContextHolder: Clear context
```

## Caching Strategy

The service uses Spring Cache for optimizing frequently accessed data:

- Cache configuration is defined in `CacheConfig`
- Dashboard summaries are cached with TTL
- Cache is invalidated on data updates

## Monitoring & Observability

### Metrics
- **Micrometer** with Prometheus registry
- Exposes metrics at `/actuator/prometheus`
- Tracks request counts, response times, error rates

### Health Checks
- Spring Boot Actuator health endpoints
- MongoDB connection health verification

### Logging
- Structured logging with SLF4J
- Request correlation IDs for traceability

## Security Considerations

1. **Tenant Isolation**: All queries scoped to tenant ID
2. **Input Validation**: Bean validation on all request DTOs
3. **MongoDB Injection Prevention**: Using repository abstraction
4. **CORS**: Configured for allowed origins

## Deployment Architecture

```mermaid
graph LR
    subgraph "Kubernetes Cluster"
        LB[Load Balancer]
        Pod1[Pod 1]
        Pod2[Pod 2]
        Pod3[Pod N]
    end

    subgraph "Infrastructure"
        MongoDB[(MongoDB Cluster)]
        Config[Config Server]
        Registry[Service Registry]
    end

    LB --> Pod1
    LB --> Pod2
    LB --> Pod3

    Pod1 --> MongoDB
    Pod2 --> MongoDB
    Pod3 --> MongoDB

    Pod1 --> Config
    Pod2 --> Config
    Pod3 --> Config

    Pod1 --> Registry
    Pod2 --> Registry
    Pod3 --> Registry
```

## Scaling Strategy

### Horizontal Scaling
- Stateless service design enables horizontal scaling
- Multiple instances behind load balancer
- Sticky sessions not required

### Database Scaling
- MongoDB sharding for large datasets
- Read replicas for read-heavy workloads
- Index optimization for query performance

## Configuration Management

- **External Configuration**: Spring Cloud Config
- **Profile-based**: dev, test, staging, production profiles
- **Environment Variables**: Override sensitive values
- **Configuration Properties**: Type-safe configuration

## Error Handling

```mermaid
graph TD
    Request[Incoming Request] --> Controller
    Controller -->|Valid| Service
    Controller -->|Invalid| BadRequest[400 Bad Request]

    Service -->|Found| Repository
    Service -->|Not Found| NotFound[404 Not Found]
    Service -->|Business Error| BusinessError[400 Business Error]

    Repository -->|Success| Response[200 OK]
    Repository -->|Database Error| DBError[500 Internal Server Error]
```

## Testing Strategy

### Unit Tests
- Service layer tests with mocked repositories
- Mapper tests for DTO conversions
- Domain model tests for business logic

### Integration Tests
- Repository tests with Testcontainers MongoDB
- Controller tests with MockMvc
- End-to-end API tests

### Test Coverage Goal
- Minimum 80% code coverage
- 100% coverage for critical business logic

## Future Enhancements

1. **Real-time Updates**: WebSocket support for live dashboard updates
2. **Advanced Analytics**: Integration with analytics engines
3. **Data Export**: CSV/PDF export capabilities
4. **Custom Dashboards**: User-configurable dashboard layouts
5. **Machine Learning**: Predictive analytics for ticket volumes
