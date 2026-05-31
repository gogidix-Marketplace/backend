# Audit Trail Service

> Comprehensive audit logging for all transaction events in the Gogidix ecosystem.

[![Build Status](https://img.shields.io/github/actions/workflow/status/gogidix/audit-trail-service/ci.yml?branch=main)](https://github.com/gogidix/audit-trail-service/actions/workflows/ci.yml)
[![Coverage](https://img.shields.io/codecov/c/github/gogidix/audit-trail-service?token=xxx)](https://codecov.io/gh/gogidix/audit-trail-service)
[![Version](https://img.shields.io/github/v/release/gogidix/audit-trail-service)](https://github.com/gogidix/audit-trail-service/releases)
[![License](https://img.shields.io/github/license/gogidix/audit-trail-service)](LICENSE)

## Overview

The Audit Trail Service is a hexagonal microservice that provides comprehensive audit logging capabilities for all transaction events. It ensures regulatory compliance (SOX, PCI DSS, GDPR), enables security incident investigation, and supports operational monitoring.

## Features

- **Comprehensive Logging**: Track all transaction events with full context
- **Multi-Tenancy**: Complete tenant isolation with scoping on all queries
- **Distributed Tracing**: Correlation ID support across services
- **High Performance**: Optimized for high-throughput environments (2000+ req/s)
- **Search & Filter**: Powerful search capabilities with multiple filters
- **Event Publishing**: Kafka integration for real-time event streaming
- **Caching**: Redis integration for improved read performance
- **Security**: Input validation, SQL injection prevention, secure defaults

## Quick Start

### Prerequisites

- Java 17+
- Maven 3.9+
- PostgreSQL 16+
- Redis 7+
- Kafka 3.x

### Running Locally

```bash
# Clone the repository
git clone https://github.com/gogidix/audit-trail-service.git
cd audit-trail-service

# Build the project
mvn clean install

# Run the service
mvn spring-boot:run
```

### Using Docker

```bash
# Build the image
docker build -t audit-trail-service .

# Run with Docker Compose (includes dependencies)
docker-compose up -d
```

### Using Kubernetes

```bash
# Deploy to Kubernetes
kubectl apply -f k8s/

# Verify deployment
kubectl get pods -l app=audit-trail-service
```

## API Documentation

Interactive API documentation is available at:

- **Swagger UI**: http://localhost:8081/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8081/v3/api-docs

### Example: Create an Audit Log

```bash
curl -X POST http://localhost:8081/api/v1/audit-logs \
  -H "Content-Type: application/json" \
  -d '{
    "tenantId": "tenant-001",
    "entityType": "Transaction",
    "entityId": "txn-12345",
    "action": "CREATE",
    "actorId": "user-001",
    "actorType": "USER",
    "severity": "INFO",
    "status": "SUCCESS",
    "description": "Transaction created successfully"
  }'
```

### Example: Search Audit Logs

```bash
curl "http://localhost:8081/api/v1/audit-logs?tenantId=tenant-001&entityType=Transaction&page=0&size=20"
```

## Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_DATASOURCE_URL` | PostgreSQL JDBC URL | - |
| `SPRING_DATASOURCE_USERNAME` | Database username | - |
| `SPRING_DATASOURCE_PASSWORD` | Database password | - |
| `SPRING_DATA_REDIS_HOST` | Redis host | localhost |
| `SPRING_DATA_REDIS_PORT` | Redis port | 6379 |
| `SPRING_KAFKA_BOOTSTRAP_SERVERS` | Kafka brokers | localhost:9092 |

### Spring Profiles

- `dev`: Development configuration
- `test`: Test configuration with H2 database
- `prod`: Production configuration

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Architecture

The service follows hexagonal architecture (ports and adapters):

```
┌─────────────────────────────────────────────────┐
│              Interfaces Layer                   │
│         (REST Controllers)                       │
├─────────────────────────────────────────────────┤
│              Application Layer                  │
│    (Services, Mappers, DTOs)                    │
├─────────────────────────────────────────────────┤
│                Domain Layer                     │
│      (Entities, Domain Logic, Ports)            │
├─────────────────────────────────────────────────┤
│           Infrastructure Layer                   │
│   (Repositories, Messaging, Config)             │
└─────────────────────────────────────────────────┘
```

For detailed architecture documentation, see [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md).

## Development

### Running Tests

```bash
# Unit tests
mvn test -Dtest='**.unit.**'

# Integration tests
mvn test -Dtest='**.integration.**'

# Architecture tests
mvn test -Dtest='**.arch.**'

# All tests with coverage
mvn test jacoco:report
```

### Code Quality

```bash
# Checkstyle
mvn checkstyle:check

# SpotBugs
mvn spotbugs:check

# OWASP dependency check
mvn org.owasp:dependency-check-maven:check
```

### Project Structure

```
src/main/java/com/gogidix/transaction/audit/
├── application/           # Application layer
│   ├── dto/              # Data transfer objects
│   ├── mapper/           # MapStruct mappers
│   └── service/          # Application services
├── domain/               # Domain layer
│   ├── model/            # Domain entities
│   ├── port/             # Input/output ports
│   └── repository/       # Repository interfaces
├── infrastructure/       # Infrastructure layer
│   ├── config/          # Configuration classes
│   ├── messaging/       # Kafka messaging
│   └── persistence/     # Database adapters
└── interfaces/          # Interfaces layer
    └── rest/            # REST controllers
```

## Documentation

- [Architecture Documentation](docs/ARCHITECTURE.md)
- [API Documentation](docs/API.md)
- [Business Use Cases](docs/BUSINESS_USE_CASES.md)
- [Production Certification](CERTIFICATION.md)

## Monitoring

### Health Endpoints

- `/actuator/health` - Application health
- `/actuator/health/readiness` - Readiness probe
- `/actuator/health/liveness` - Liveness probe
- `/actuator/metrics` - Prometheus metrics

### Key Metrics

- `audit_log_create_total` - Total audit logs created
- `audit_log_query_duration_seconds` - Query duration
- `http_server_requests_seconds` - HTTP request duration

## Deployment

### Production Build

```bash
# Build production JAR
mvn clean package -DskipTests -Pprod

# Build Docker image
docker build -t gogidix/audit-trail-service:latest .
```

### Kubernetes Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: audit-trail-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: audit-trail-service
  template:
    metadata:
      labels:
        app: audit-trail-service
    spec:
      containers:
      - name: audit-trail-service
        image: gogidix/audit-trail-service:latest
        ports:
        - containerPort: 8081
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        resources:
          requests:
            memory: "256Mi"
            cpu: "250m"
          limits:
            memory: "512Mi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /actuator/health/liveness
            port: 8081
          initialDelaySeconds: 60
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /actuator/health/readiness
            port: 8081
          initialDelaySeconds: 30
          periodSeconds: 5
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines

- Follow hexagonal architecture principles
- Write tests for all new functionality (target: 85%+ coverage)
- Run `mvn test` before committing
- Update documentation for API changes
- Follow existing code style and patterns

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Support

- **Documentation**: https://docs.gogidix.com/services/audit-trail
- **Issues**: https://github.com/gogidix/audit-trail-service/issues
- **Email**: platform@gogidix.com

## Changelog

See [CHANGELOG.md](CHANGELOG.md) for version history.

---

**Built with ❤️ by the Gogidix Platform Team**
