# Infrastructure DevTools Service - Architecture

## Overview

The Infrastructure DevTools Service is a comprehensive development platform that provides essential tools for developers working within the Gogidix ecosystem. It offers API testing, database query capabilities, logging/debugging utilities, deployment management, and documentation generation.

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                         Client Layer                             │
├─────────────────────────────────────────────────────────────────┤
│  React Dashboard │ API Clients │ CLI Tools │ IDE Plugins        │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         │ REST/GraphQL/WebSocket
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                      API Gateway Layer                           │
├─────────────────────────────────────────────────────────────────┤
│  Spring Boot Controllers │ Security │ Validation │ CORS        │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                       Service Layer                              │
├─────────────────────────────────────────────────────────────────┤
│  ApiTestingService │ DatabaseQueryService │ LoggingService     │
│  DeploymentService │ DocumentationService                       │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                     Repository Layer                             │
├─────────────────────────────────────────────────────────────────┤
│  JPA Repositories │ Query Methods │ Custom Implementations      │
└────────────────────────┬────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                    Data Layer                                    │
├─────────────────────────────────────────────────────────────────┤
│  PostgreSQL (Primary) │ Redis (Cache) │ Kafka (Events)         │
└─────────────────────────────────────────────────────────────────┘
```

## Component Overview

### 1. API Testing Module

**Purpose**: Enable automated testing and validation of REST APIs.

**Components**:
- `ApiTestingController`: REST endpoints for test management
- `ApiTestingService`: Business logic for test execution
- `ApiTestCase`: Test case entity with request/response validation
- `ApiTestExecution`: Historical execution records

**Key Features**:
- Create and manage reusable test cases
- Execute ad-hoc API requests
- Response validation with JavaScript expressions
- Batch test execution
- Execution history tracking

### 2. Database Query Module

**Purpose**: Safe database query execution and analysis tools.

**Components**:
- `DatabaseQueryController`: Query management endpoints
- `DatabaseQueryService`: Query execution with safeguards
- `DatabaseQuery`: Saved query definitions
- `DatabaseQueryExecution`: Execution records

**Key Features**:
- Save and organize frequently used queries
- Query validation before execution
- Result limiting to prevent large result sets
- Query history and statistics

### 3. Logging Module

**Purpose**: Centralized logging and debugging capabilities.

**Components**:
- `LoggingController`: Log query endpoints
- `LoggingService`: Log aggregation and search
- `DevToolLogEntry`: Log entry entity

**Key Features**:
- Real-time log streaming
- Log level filtering
- Session/request correlation
- Log export functionality
- Automatic log cleanup

### 4. Deployment Module

**Purpose**: Streamline deployment and rollback processes.

**Components**:
- `DeploymentController`: Deployment job management
- `DeploymentService`: Deployment orchestration
- `DeploymentJob`: Deployment configuration
- `DeploymentExecution`: Deployment history

**Key Features**:
- Script-based deployment jobs
- Pre/post deployment hooks
- Automatic rollback on failure
- Multiple environment support
- SSH/Docker/Kubernetes integration

### 5. Documentation Generator Module

**Purpose**: Automatic documentation generation from source code.

**Components**:
- `DocumentationController`: Documentation generation endpoints
- `DocumentationService`: Documentation parsing and generation
- `DocumentationProject`: Documentation configuration
- `DocumentationGeneration`: Generation records

**Key Features**:
- Source code scanning
- Markdown/HTML output
- API documentation extraction
- Database schema documentation
- Scheduled generation

## Technology Stack

### Backend
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: PostgreSQL 16
- **Cache**: Redis 7
- **Messaging**: Kafka 3.6
- **ORM**: Spring Data JPA / Hibernate
- **Migrations**: Flyway
- **API Documentation**: SpringDoc OpenAPI

### Frontend
- **Framework**: React 18
- **Build Tool**: Vite 5
- **UI Library**: Material-UI 5
- **State Management**: Zustand
- **Data Fetching**: TanStack Query
- **Code Editor**: Monaco Editor
- **Charts**: Recharts

## Design Patterns

### 1. Repository Pattern
Data access is abstracted behind repository interfaces, enabling easy mocking and testing.

### 2. DTO Pattern
Data Transfer Objects separate API contracts from domain entities.

### 3. Service Layer Pattern
Business logic is encapsulated in service classes, keeping controllers thin.

### 4. Async Execution
Long-running operations use `@Async` with `CompletableFuture` for non-blocking execution.

### 5. Cache-Aside Pattern
Frequently accessed data is cached in Redis with cache-aside loading strategy.

## Security Considerations

1. **Authentication**: HTTP Basic with pluggable JWT support
2. **Authorization**: Role-based access control (RBAC)
3. **Input Validation**: Jakarta Validation annotations
4. **SQL Injection**: Parameterized queries via JPA
5. **CORS**: Configurable allowed origins
6. **Query Safety**: Keyword detection for dangerous SQL commands

## Scalability Considerations

1. **Horizontal Scaling**: Stateless service design enables multiple instances
2. **Connection Pooling**: HikariCP for database connections
3. **Caching**: Redis for frequently accessed data
4. **Async Processing**: @Async for long-running operations
5. **Event Streaming**: Kafka for event-driven architecture

## Monitoring & Observability

1. **Health Checks**: Spring Boot Actuator endpoints
2. **Metrics**: Prometheus-compatible metrics export
3. **Logging**: Structured logging with correlation IDs
4. **Tracing**: OpenTelemetry support (ready for integration)
