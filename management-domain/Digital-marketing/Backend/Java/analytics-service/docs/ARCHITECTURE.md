# Analytics Service - Architecture Documentation

## Overview

The Analytics Service is a core component of the Digital Marketing Domain within the Gogidix Ecosystem. It provides comprehensive tracking, analysis, and reporting capabilities for marketing metrics, campaign performance, channel attribution, and ROI calculations.

## Table of Contents

- [Service Purpose](#service-purpose)
- [Architecture Principles](#architecture-principles)
- [System Architecture](#system-architecture)
- [Domain Model](#domain-model)
- [Technology Stack](#technology-stack)
- [Multi-Tenancy](#multi-tenancy)
- [Data Flow](#data-flow)
- [Integration Points](#integration-points)
- [Security](#security)
- [Scalability Considerations](#scalability-considerations)

---

## Service Purpose

The Analytics Service is responsible for:

1. **Metric Collection**: Collecting and storing marketing metrics from various channels
2. **Campaign Analytics**: Tracking and analyzing campaign performance
3. **Channel Analytics**: Comparing performance across marketing channels
4. **Attribution Tracking**: Multi-touch attribution analysis
5. **ROI Calculation**: Return on investment and ROAS calculations
6. **Report Generation**: Creating scheduled and on-demand analytics reports

## Architecture Principles

The service follows these architectural principles:

- **Domain-Driven Design (DDD)**: Clear domain boundaries and ubiquitous language
- **Multi-Tenancy First**: All data isolated by tenant ID
- **Event-Driven**: Asynchronous processing for analytics calculations
- **API-First**: RESTful APIs with OpenAPI/Swagger documentation
- **Cloud-Native**: Designed for containerization and orchestration

## System Architecture

### High-Level Architecture

```mermaid
graph TB
    subgraph "Presentation Layer"
        API[REST API]
        Docs[Swagger UI]
    end

    subgraph "Application Layer"
        Service[Analytics Service]
        Report[Report Service]
        Attribution[Attribution Service]
    end

    subgraph "Domain Layer"
        Metric[Marketing Metric]
        Campaign[Campaign Analytics]
        Channel[Channel Analytics]
        ReportModel[Analytics Report]
        AttributionModel[Attribution Data]
    end

    subgraph "Infrastructure Layer"
        Mongo[MongoDB]
        Redis[Redis Cache]
        Kafka[Kafka Events]
    end

    subgraph "External Integrations"
        GA[Google Analytics]
        FB[Facebook Ads]
        Goo[Google Ads]
        CRM[CRM Systems]
    end

    API --> Service
    API --> Report
    Docs --> API

    Service --> Metric
    Service --> Campaign
    Service --> Channel
    Report --> ReportModel
    Attribution --> AttributionModel

    Metric --> Mongo
    Campaign --> Mongo
    Channel --> Mongo
    ReportModel --> Mongo
    AttributionModel --> Mongo

    Service --> Redis
    Service --> Kafka

    Kafka --> GA
    Kafka --> FB
    Kafka --> Goo
    Kafka --> CRM
```

### Component Architecture

```mermaid
graph LR
    subgraph "Analytics Service Components"
        Controller[API Controllers]
        Service[Domain Services]
        Repository[Repositories]
        Model[Domain Models]
    end

    subgraph "Cross-Cutting Concerns"
        Tenant[Tenant Filter]
        Cache[Caching Layer]
        Security[Security Layer]
        Events[Event Publisher]
    end

    Controller --> Security
    Security --> Tenant
    Tenant --> Service
    Service --> Repository
    Repository --> Model
    Service --> Cache
    Service --> Events
```

## Domain Model

### Core Entities

#### MarketingMetric
Represents individual marketing metric data points.

```mermaid
classDiagram
    class MarketingMetric {
        +String id
        +String tenantId
        +String metricType
        +String channelType
        +String campaignId
        +BigDecimal value
        +BigDecimal previousValue
        +BigDecimal percentChange
        +Instant timestamp
        +String granularity
        +String dataSource
        +BigDecimal qualityScore
        +Boolean verified
        +calculatePercentChange()
        +updateTargetStatus()
        +isOnTrack()
    }
```

#### CampaignAnalytics
Aggregates campaign performance metrics.

```mermaid
classDiagram
    class CampaignAnalytics {
        +String id
        +String tenantId
        +String campaignId
        +String campaignName
        +String campaignType
        +String status
        +BigDecimal impressions
        +BigDecimal clicks
        +BigDecimal ctr
        +BigDecimal conversions
        +BigDecimal conversionRate
        +BigDecimal spend
        +BigDecimal roi
        +BigDecimal roas
        +calculateAllMetrics()
        +isProfitable()
        +updateAchievementStatus()
    }
```

#### ChannelAnalytics
Aggregates channel performance metrics.

```mermaid
classDiagram
    class ChannelAnalytics {
        +String id
        +String tenantId
        +String channelType
        +String platform
        +String period
        +BigDecimal impressions
        +BigDecimal clicks
        +BigDecimal ctr
        +BigDecimal conversions
        +BigDecimal roi
        +BigDecimal channelScore
        +String performanceRating
        +calculateAllMetrics()
        +isProfitable()
        +isTrendingUp()
    }
```

#### AnalyticsReport
Represents generated analytics reports.

```mermaid
classDiagram
    class AnalyticsReport {
        +String id
        +String tenantId
        +String name
        +String reportType
        +String status
        +String format
        +Boolean scheduleEnabled
        +String scheduleType
        +Instant nextRunAt
        +List~String~ metrics
        +List~String~ dimensions
        +calculateNextRunTime()
        +isAccessibleBy(userId)
        +markAsCompleted()
    }
```

### Entity Relationships

```mermaid
erDiagram
    MARKETING_METRIC ||--o{ CAMPAIGN_ANALYTICS : feeds
    MARKETING_METRIC ||--o{ CHANNEL_ANALYTICS : aggregates
    CAMPAIGN_ANALYTICS ||--o{ ATTRIBUTION_DATA : tracks
    CHANNEL_ANALYTICS ||--o{ ATTRIBUTION_DATA : includes
    ANALYTICS_REPORT ||--o{ MARKETING_METRIC : includes
    ANALYTICS_REPORT ||--o{ CAMPAIGN_ANALYTICS : summarizes
    ANALYTICS_REPORT ||--o{ CHANNEL_ANALYTICS : compares
```

## Technology Stack

### Backend Framework
- **Spring Boot 3.1.5**: Main application framework
- **Spring Data MongoDB**: Database access layer
- **Spring Data Redis**: Caching layer
- **Spring Security**: Security framework

### Database
- **MongoDB**: Primary data store
- **Redis**: Caching and session management
- **Embedded MongoDB**: Test database

### Messaging
- **Apache Kafka**: Event streaming and integration

### API Documentation
- **SpringDoc OpenAPI 2.3.0**: API documentation
- **Swagger UI**: Interactive API console

### Build & Test
- **Maven**: Build tool
- **JUnit 5**: Testing framework
- **Mockito**: Mocking framework
- **Embedded MongoDB**: Integration testing

### Additional Libraries
- **MapStruct 1.5.5**: DTO mapping
- **Lombok 1.18.30**: Code generation
- **Resilience4j 2.1.0**: Circuit breaker and retry
- **Mongock 5.4.0**: Database migrations

## Multi-Tenancy

The service implements tenant isolation at the database level:

### Tenant Isolation Strategy
- **Collection-Level Isolation**: Each document includes a `tenantId` field
- **Query Filtering**: All queries automatically filter by tenant ID
- **Index Strategy**: Compound indexes include tenant ID for query optimization

### Tenant Context
- **RequestContextFilter**: Extracts tenant ID from JWT tokens
- **RequestContextHolder**: Thread-local tenant context
- **BaseRepository**: Automatic tenant filtering in all queries

```java
@Query("{ 'tenantId': ?0 }")
List<T> findAllByTenantId(String tenantId);
```

## Data Flow

### Metric Ingestion Flow

```mermaid
sequenceDiagram
    participant External as External System
    participant Kafka as Message Broker
    participant Service as Analytics Service
    participant Mongo as MongoDB
    participant Cache as Redis Cache

    External->>Kafka: Publish Metric Event
    Kafka->>Service: Consume Event
    Service->>Mongo: Store Metric
    Service->>Cache: Update Cache
    Service->>Service: Calculate Derivatives
    Service->>Mongo: Update Aggregates
```

### Report Generation Flow

```mermaid
sequenceDiagram
    participant User as User
    participant API as REST API
    participant Service as Report Service
    participant Mongo as MongoDB
    participant Storage as File Storage

    User->>API: POST /reports
    API->>Service: Create Report
    Service->>Mongo: Save Report (DRAFT)
    User->>API: PUT /reports/{id}/generate
    API->>Service: Generate Report
    Service->>Mongo: Query Metrics
    Service->>Service: Aggregate Data
    Service->>Storage: Export File
    Storage->>Service: File URL
    Service->>Mongo: Update Report (COMPLETED)
    Service->>User: Return Report
```

## Integration Points

### External Service Integrations

1. **Google Analytics API**
   - Web traffic metrics
   - User behavior data
   - Conversion tracking

2. **Facebook Ads API**
   - Ad performance metrics
   - Audience insights
   - Campaign data

3. **Google Ads API**
   - Search campaign metrics
   - Keyword performance
   - Budget utilization

4. **CRM Systems**
   - Lead conversion data
   - Customer lifetime value
   - Revenue attribution

### Internal Service Integrations

1. **Campaign Management Service**
   - Campaign configuration data
   - Budget information
   - Target settings

2. **Budget Management Service**
   - Budget allocation data
   - Spend tracking
   - Forecasting data

3. **Lead Generation Service**
   - Lead quality metrics
   - Conversion funnel data
   - Source attribution

## Security

### Authentication & Authorization
- **JWT Tokens**: Stateless authentication
- **Role-Based Access Control (RBAC)**: User roles and permissions
- **Tenant Isolation**: Automatic tenant filtering

### API Security
- **HTTPS Only**: All endpoints require TLS
- **Rate Limiting**: Request throttling per tenant
- **Input Validation**: Request validation at API boundaries

### Data Security
- **Encryption at Rest**: MongoDB encryption
- **Encryption in Transit**: TLS for all communications
- **Audit Logging**: All mutations logged with user context

## Scalability Considerations

### Horizontal Scaling
- **Stateless Services**: All services are stateless
- **Connection Pooling**: Efficient database connection management
- **Caching Layer**: Redis cache reduces database load

### Performance Optimization
- **Database Indexing**: Compound indexes for common queries
- **Query Optimization**: Efficient MongoDB queries
- **Async Processing**: Event-driven architecture for heavy computations

### Caching Strategy
- **Metric Cache**: Frequently accessed metrics cached in Redis
- **Aggregation Cache**: Pre-computed aggregations cached
- **Cache Invalidation**: Time-based and event-based invalidation

### Batch Processing
- **Metric Batching**: Bulk metric ingestion
- **Scheduled Jobs**: Report generation and aggregation updates
- **Partitioning**: Data partitioned by tenant and date

## Deployment Architecture

### Container Configuration
- **Base Image**: OpenJDK 17-slim
- **Port**: 8086 (configurable)
- **Health Check**: Actuator health endpoint
- **Resource Limits**: CPU and memory constraints

### Environment Variables
```bash
SPRING_DATA_MONGODB_HOST=mongodb
SPRING_DATA_MONGODB_PORT=27017
SPRING_DATA_MONGODB_DATABASE=digital_marketing_analytics
SPRING_REDIS_HOST=redis
SPRING_REDIS_PORT=6379
SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
```

### Kubernetes Considerations
- **Health Probes**: Liveness and readiness endpoints
- **Config Maps**: Externalized configuration
- **Secrets Management**: Sensitive data in Kubernetes secrets
- **Horizontal Pod Autoscaler**: Auto-scaling based on CPU/memory

---

## Version History

| Version | Date | Changes |
|---------|------|---------|
| 1.0.0 | 2024-01 | Initial release |
