# Centralized Dashboard - Architecture Documentation

## Executive Summary

The Centralized Dashboard is a comprehensive microservices-based monitoring and analytics platform built for the Gogidix ecosystem. It provides real-time visibility into system performance, business metrics, and operational health across all domain services.

**Key Metrics:**
- **Services:** 8 Spring Boot microservices, 1 Node.js reporting service
- **Frontend:** React-based unified admin dashboard with TypeScript
- **Total Files:** 293+
- **Test Coverage Target:** 80%+
- **Technology Stack:** Java 17, Spring Boot 3.1.5, Node.js 18, React 18

---

## System Architecture

### High-Level Overview

```
┌─────────────────────────────────────────────────────────────────────┐
│                         CLIENT LAYER                                 │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │           Unified Admin Dashboard (React/TypeScript)         │   │
│  │  - Dashboard, Charts, Real-time, Settings pages              │   │
│  │  - WebSocket for live updates                                │   │
│  └────────────────────┬────────────────────────────────────────┘   │
└───────────────────────┼─────────────────────────────────────────────┘
                        │ HTTP/WebSocket
                        ▼
┌─────────────────────────────────────────────────────────────────────┐
│                         GATEWAY LAYER                                │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                    API Gateway Service                        │   │
│  │  - Request routing, authentication, rate limiting            │   │
│  │  - CORS handling, request/response transformation           │   │
│  └─────────────────────────────────────────────────────────────┘   │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                  WebSocket Gateway Service                    │   │
│  │  - Real-time bi-directional communication                    │   │
│  │  - Topic-based pub/sub, tenant isolation                     │   │
│  └─────────────────────────────────────────────────────────────┘   │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                      Chart Service                           │   │
│  │  - Chart data aggregation and formatting                     │   │
│  │  - Multi-source data composition                             │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
                        │
                        ▼
┌─────────────────────────────────────────────────────────────────────┐
│                         SERVICE LAYER                                │
│  ┌──────────────────────────┬──────────────────────────────────┐   │
│  │   ANALYTICS SERVICES     │      CORE SERVICES               │   │
│  ├──────────────────────────┼──────────────────────────────────┤   │
│  │ - Metrics Aggregation    │ - Dashboard Core                 │   │
│  │ - Business Intelligence  │ - Data Aggregation               │   │
│  │ - Analytics Data         │ - Shared Components              │   │
│  └──────────────────────────┴──────────────────────────────────┘   │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │                   DATA SERVICES                               │  │
│  │ - Centralized Data Aggregation                                │  │
│  │ - Real-time Data Streams                                      │  │
│  └──────────────────────────────────────────────────────────────┘  │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │                 REPORTING SERVICES                            │  │
│  │ - Centralized Reporting (Node.js)                             │  │
│  │ - CSV/PDF Generation                                          │  │
│  └──────────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────┘
                        │
                        ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    MONITORING LAYER                                  │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │           Performance Metrics Service                         │   │
│  │  - Service health monitoring                                  │   │
│  │  - Latency, throughput, error rate tracking                  │   │
│  └─────────────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Service Directory Structure

```
Backend/Java/
├── analytics-services/
│   ├── metrics-aggregation-service/      # Metrics collection and aggregation
│   ├── business-intelligence-service/    # BI and reporting queries
│   └── analytics-data-service/           # Analytics data API
├── core-services/
│   ├── dashboard-core-service/           # Core dashboard logic
│   ├── data-aggregation-service/         # Cross-service data aggregation
│   └── dashboard-shared-service/         # Shared utilities
├── gateway-services/
│   ├── api-gateway-service/              # API Gateway
│   ├── websocket-gateway-service/        # WebSocket Gateway
│   └── chart-service/                    # Chart data service
├── data-services/
│   ├── centralized-data-aggregation/     # Data aggregation
│   └── centralized-real-time-data/       # Real-time data streams
├── monitoring-services/
│   └── centralized-performance-metrics/   # Performance monitoring
├── platform-services/
│   └── ...                               # Platform-level services
└── reporting-services/
    ├── centralized-reporting/            # Java reporting service
    └── centralized-reporting-service/    # Node.js reporting service

Frontends/
├── unified-admin-dashboard/              # Main React admin UI
├── Web/
│   ├── centralized-analytics-dashboard/  # Analytics UI
│   └── dashboard-frontend/               # Additional frontend
└── Mobile/                               # Mobile apps (placeholder)
```

---

## Technology Stack

### Backend Services (Java)

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Primary language |
| Spring Boot | 3.1.5 | Application framework |
| Maven | 3.9.12 | Build tool |
| MapStruct | 1.5.5.Final | DTO mapping |
| Lombok | 1.18.30 | Code generation |
| SpringDoc | 2.3.0 | OpenAPI documentation |

### Backend Services (Node.js)

| Technology | Version | Purpose |
|------------|---------|---------|
| Node.js | 18+ | Runtime |
| Express | 4.18.2 | Web framework |
| node-fetch | 2.6.7 | HTTP client |
| @json2csv/plainjs | 7.0.6 | CSV generation |
| Jest | 29.5.0 | Testing framework |
| Supertest | 6.3.3 | API testing |

### Frontend

| Technology | Version | Purpose |
|------------|---------|---------|
| React | 18.2.0 | UI framework |
| TypeScript | 5.3.3 | Type safety |
| Vite | 5.0.11 | Build tool |
| React Router | 6.21.1 | Routing |
| TanStack Query | 5.17.9 | Data fetching |
| Recharts | 2.10.3 | Charts |
| Zustand | 4.4.7 | State management |
| Tailwind CSS | 3.4.1 | Styling |

---

## Cross-Cutting Concerns

### Multi-Tenancy

All services support multi-tenant architecture via:
- **X-Tenant-ID** header for API requests
- Tenant-scoped data isolation
- Separate WebSocket topics per tenant

### Resilience Patterns

- **Circuit Breaker:** Prevents cascading failures
- **Retry Logic:** Automatic retry with exponential backoff
- **Timeouts:** Configurable per service
- **Health Checks:** `/health` endpoint on all services

### Security

- **Authentication:** JWT-based authentication
- **Authorization:** Role-based access control (RBAC)
- **CORS:** Configured for trusted origins
- **Secret Management:** Environment-based configuration

---

## Communication Patterns

### Synchronous Communication (REST API)

```
Client -> API Gateway -> Service A -> Service B
         <- Response   <- Response   <- Response
```

### Asynchronous Communication (WebSocket)

```
Client <---> WebSocket Gateway <---> Service Bus <---> Services
           (bi-directional)    (pub/sub)
```

### Event-Driven Communication

Services emit events for:
- Metrics updates
- Health status changes
- Data availability
- Alert conditions

---

## Data Flow

### Dashboard Data Flow

1. Frontend requests dashboard data via API Gateway
2. Gateway authenticates and identifies tenant
3. Gateway requests data from relevant services
4. Services respond with tenant-scoped data
5. Gateway aggregates and returns to frontend
6. Frontend renders visualizations

### Real-time Updates Flow

1. Service generates event (metric update, alert)
2. Service publishes to WebSocket Gateway
3. Gateway routes to appropriate tenant topics
4. Connected clients receive update
5. Frontend updates UI reactively

---

## Deployment Architecture

### Container Strategy

Each service runs in its own container with:
- Multi-stage Docker builds
- Non-root user execution
- Health check endpoints
- Resource limits (CPU, memory)

### Orchestration

Supported deployment options:
- **Docker Compose:** Local development
- **Kubernetes:** Production (manifests in `k8s/`)
- **Cloud Build:** Google Cloud Platform support

### Environments

- **Development:** Local Docker Compose
- **Staging:** Kubernetes cluster
- **Production:** Kubernetes with autoscaling

---

## Monitoring and Observability

### Metrics Collection

- Service-level metrics (latency, throughput, errors)
- Business metrics (sales, inventory, performance)
- Custom metrics per domain

### Logging

- Structured JSON logging
- Correlation IDs for request tracing
- Centralized log aggregation

### Health Monitoring

- `/health` endpoint on all services
- Dependency health checks
- Circuit breaker status

---

## Extensibility Points

### Adding a New Service

1. Create service in appropriate domain folder
2. Implement standard health endpoint
3. Add to API Gateway routing
4. Update docker-compose.yml
5. Add CI/CD pipeline steps

### Adding a New Chart Type

1. Implement in Chart Service
2. Add frontend component in `src/components/`
3. Register in chart configuration
4. Update documentation

### Adding a New Metric

1. Define in Metrics Aggregation Service
2. Add to data models
3. Configure collection interval
4. Add to dashboard if needed

---

## Performance Considerations

### Caching Strategy

- API response caching (TTL-based)
- Dashboard data caching (5-minute default)
- Static asset caching (immutable hashes)

### Scalability

- Stateless services for horizontal scaling
- Connection pooling for database access
- Async processing for long-running operations

### Optimization

- Database query optimization
- Index strategy
- Pagination for large datasets
- Lazy loading for charts

---

## Architecture Decision Records (ADRs)

### ADR-001: Multi-Service Architecture

**Decision:** Use separate microservices instead of monolith

**Rationale:**
- Independent deployment
- Technology diversity
- Fault isolation
- Team autonomy

### ADR-002: WebSocket for Real-Time

**Decision:** Use WebSocket instead of polling

**Rationale:**
- Lower latency
- Reduced server load
- Better user experience
- Bi-directional communication

### ADR-003: TypeScript for Frontend

**Decision:** Use TypeScript instead of JavaScript

**Rationale:**
- Type safety
- Better IDE support
- Catch errors at compile time
- Improved maintainability

---

## Future Enhancements

1. **GraphQL API:** Alternative to REST for complex queries
2. **Event Sourcing:** Audit trail and temporal queries
3. **CQRS:** Separate read/write models
4. **Service Mesh:** Istio for service-to-service communication
5. **Distributed Tracing:** OpenTelemetry integration
6. **Advanced Analytics:** Machine learning pipeline integration

---

## References

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React Documentation](https://react.dev)
- [Microservices Patterns](https://microservices.io/patterns/)
- [Twelve-Factor App](https://12factor.net/)
