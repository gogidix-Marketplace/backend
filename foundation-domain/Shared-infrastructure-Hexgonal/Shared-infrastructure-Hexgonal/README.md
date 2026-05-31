# Shared-Infrastructure-Hexgonal

Multi-tenant SaaS infrastructure services with clean Hexagonal Architecture.

## Overview

This domain provides essential infrastructure services for the Gogidix ecosystem. All services follow Hexagonal Architecture with built-in multi-tenant support.

## Architecture

### Hexagonal Architecture Layers

Each service is organized into four layers:

```
[service-name]/
├── domain/          # Business logic, entities, ports
├── application/     # DTOs, mappers, use case implementations
├── infrastructure/  # External adapters, repositories, configs
└── interfaces/      # REST controllers
```

### Multi-Tenant Support

All services include:
- **TenantId** - Value object for tenant identification
- **TenantContextHolder** - Thread-local tenant context
- **TenantInterceptor** - HTTP header extraction (X-Tenant-ID, X-Tenant)
- **Tenant-scoped queries** - Automatic data isolation

## Services

| Service | Port | Description |
|---------|------|-------------|
| core-tenancy | - | Multi-tenant framework |
| auth-service | 8081 | JWT/OAuth2 authentication |
| user-management-service | 8082 | User lifecycle management |
| api-gateway-service | 8080 | API routing and discovery |
| config-server | 8888 | Centralized configuration |
| discovery-service | 8761 | Eureka service registry |
| notification-service | 8083 | Email/SMS notifications |
| file-storage-service | 8084 | S3 file storage |
| audit-service | 8085 | Activity logging |
| rate-limiting-service | 8086 | API rate limiting |
| tenant-management-service | 8087 | Multi-tenant administration |

## Package Structure

```
com.gogidix.shared.infrastructure
├── core
│   └── tenancy                    # Multi-tenant framework
└── services
    ├── security.[servicename]    # Security services
    ├── gateway.[servicename]     # Gateway services
    ├── communication.[servicename] # Communication services
    ├── storage.[servicename]      # Storage services
    └── observability.[servicename] # Observability services
```

## Setup

### Prerequisites

- Java 17+
- Maven 3.8+
- MongoDB 4.4+
- Redis 6+ (for rate limiting)
- AWS S3 credentials (for file storage)

### Build

```bash
cd Shared-infrastructure-Hexgonal
mvn clean install
```

### Run Individual Services

```bash
# Discovery Service (start first)
java -jar services/gateway/discovery-service/target/discovery-service-1.0.0.jar

# Config Server
java -jar services/config/config-server/target/config-server-1.0.0.jar

# API Gateway
java -jar services/gateway/api-gateway-service/target/api-gateway-service-1.0.0.jar

# Auth Service
java -jar services/security/auth-service/target/auth-service-1.0.0.jar

# Other services...
```

## Usage

### Authentication

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -H "X-Tenant-ID: tenant-123" \
  -d '{
    "username": "admin",
    "password": "password"
  }'
```

### Create User

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -H "X-Tenant-ID: tenant-123" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "userId": "user-123",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com"
  }'
```

### Create Tenant

```bash
curl -X POST http://localhost:8087/api/tenants \
  -H "Content-Type: application/json" \
  -d '{
    "tenantId": "new-tenant",
    "name": "New Tenant Inc.",
    "domain": "newtenant.com",
    "plan": "STARTER"
  }'
```

## Headers

| Header | Purpose |
|--------|---------|
| X-Tenant-ID | Primary tenant identifier |
| X-Tenant | Fallback tenant identifier |
| Authorization | Bearer token for auth |
| X-User-Id | User context for services |

## Configuration

Each service uses `application.yml` for configuration. Environment-specific configs can be provided:

```bash
java -jar service.jar --spring.profiles.active=prod
```

## Documentation

API documentation available at:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## License

Copyright © 2024 Gogidix. All rights reserved.
# Build Timestamp: 2026-03-29T23:12:43Z
All 26 services deployed and validated.
