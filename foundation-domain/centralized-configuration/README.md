# Centralized Configuration Domain

A comprehensive configuration management system built with hexagonal architecture, providing centralized configuration, feature flag management, environment configuration, notifications, and audit trails.

## Architecture

This domain follows the **Hexagonal Architecture** (Ports and Adapters) pattern with:
- **Domain Layer**: Core business logic with entities, value objects, and ports
- **Application Layer**: Use cases, DTOs, mappers, and services
- **Infrastructure Layer**: External concerns (persistence, messaging, configs)
- **Interface Layer**: REST API controllers

## Services

| Service | Port | Description |
|---------|------|-------------|
| **config-server** | 8888 | Spring Cloud Config Server for centralized configuration management |
| **feature-flag-service** | 8889 | Feature toggle management with rollout strategies |
| **environment-service** | 8890 | Environment configuration management |
| **notification-service** | 8891 | Config change alerts via email, webhooks, Slack |
| **config-audit-service** | 8892 | Complete audit trail for all configuration changes |

## Technology Stack

- **Java 17**
- **Spring Boot 3.1.5**
- **PostgreSQL** - Primary database
- **Kafka** - Event-driven messaging
- **Redis** - Caching (feature flags)
- **MapStruct** - DTO mapping
- **Lombok** - Boilerplate reduction
- **OpenAPI/Swagger** - API documentation
- **Actuator** - Metrics and health checks

## Getting Started

### Prerequisites

- JDK 17
- Maven 3.9+
- Docker & Docker Compose
- PostgreSQL 15+
- Kafka 3.x

### Quick Start with Docker Compose

```bash
# Start all services
docker-compose up -d

# Check service health
curl http://localhost:8888/actuator/health
curl http://localhost:8889/actuator/health
curl http://localhost:8890/actuator/health
curl http://localhost:8891/actuator/health
curl http://localhost:8892/actuator/health
```

### Building Individual Services

```bash
# Build config-server
cd config-server
mvn clean package

# Run the service
mvn spring-boot:run
```

### Running Services

Each service can be run independently:

```bash
# Config Server
cd config-server && mvn spring-boot:run

# Feature Flag Service
cd feature-flag-service && mvn spring-boot:run

# Environment Service
cd environment-service && mvn spring-boot:run

# Notification Service
cd notification-service && mvn spring-boot:run

# Config Audit Service
cd config-audit-service && mvn spring-boot:run
```

## API Documentation

Each service exposes Swagger UI at:
- Config Server: http://localhost:8888/swagger-ui.html
- Feature Flags: http://localhost:8889/swagger-ui.html
- Environments: http://localhost:8890/swagger-ui.html
- Notifications: http://localhost:8891/swagger-ui.html
- Audit Logs: http://localhost:8892/swagger-ui.html

## Multi-Tenancy

All services support multi-tenant SaaS architecture. Include the `X-Tenant-ID` header in your requests:

```bash
curl -H "X-Tenant-ID: tenant-1" http://localhost:8888/api/v1/configs
```

## Project Structure

```
centralized-configuration/
├── .github/workflows/          # CI/CD pipelines
│   ├── build.yml
│   ├── test.yml
│   ├── deploy-development.yml
│   ├── security-scan.yml
│   └── code-quality.yml
├── config-server/              # Configuration management service
├── feature-flag-service/       # Feature toggle service
├── environment-service/        # Environment management service
├── notification-service/       # Notification service
├── config-audit-service/       # Audit trail service
├── docker-compose.yml          # Local development setup
└── README.md
```

## Service Details

### Config Server (Port 8888)
Centralized configuration management with:
- Version-controlled configuration storage
- Encryption support for sensitive values
- Configuration history tracking
- Multi-environment support

### Feature Flag Service (Port 8889)
Dynamic feature toggle management with:
- Percentage-based rollout
- Whitelist/blacklist support
- Consistent hashing for user experience
- Redis caching for fast evaluation

### Environment Service (Port 8890)
Environment configuration management with:
- Development, Staging, QA, UAT, Production, DR environments
- Environment-specific variables
- Priority-based ordering

### Notification Service (Port 8891)
Config change alerts with:
- Email notifications
- Webhook support
- Slack integration (extensible)
- Retry mechanism for failed notifications

### Config Audit Service (Port 8892)
Complete audit trail with:
- All configuration changes tracked
- User attribution
- IP address and user agent logging
- Date range queries

## CI/CD

GitHub Actions workflows provide:
- **Build**: Compiles all services
- **Test**: Runs unit tests with coverage
- **Deploy**: Deploys to development environment
- **Security Scan**: Trivy and OWASP dependency checks
- **Code Quality**: SonarCloud analysis

## Contributing

1. Follow the hexagonal architecture pattern
2. Ensure all tests pass before committing
3. Add API documentation for new endpoints
4. Follow the existing code style

## License

Copyright (c) Gogidix Ecosystem. All rights reserved.
