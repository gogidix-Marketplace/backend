# AI User Profileation Service

A production-ready Spring Boot microservice for managing customer segments using Hexagonal Architecture.

## Overview

This service provides REST APIs for:
- Creating and managing customer segments
- Adding/removing customers from segments
- Analyzing segment performance
- Multi-tenant data isolation

## Tech Stack

- Java 17+
- Spring Boot 3.1.5
- Maven 3.9+
- MongoDB (primary database)
- Redis (caching)
- Kafka (messaging)
- MapStruct 1.5.5
- Testcontainers
- JaCoCo (70% coverage)

## Local Development

### Prerequisites

- Java 17+
- Maven 3.9+
- Docker (for MongoDB, Redis, Kafka)

### Running Services

```bash
# Start MongoDB, Redis, Kafka
docker-compose -f docker-compose.yml up -d

# Run application
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Build

```bash
# Build with tests
mvn clean install

# Build without tests
mvn clean install -DskipTests

# Package JAR
mvn clean package -Pprod
```

### Test

```bash
# Unit tests only
mvn test

# Integration tests
mvn verify -Pintegration

# Coverage report
mvn jacoco:report
```

## API Documentation

- Swagger UI: http://localhost:8080/api/v1/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api/v1/api-docs

## Configuration

Key configuration properties in `application.yml`:

```yaml
segment:
  max:
    segments-per-tenant: 100
    customers-per-segment: 1000000
  analysis:
    batch-size: 1000
    timeout-seconds: 300
  cache:
    ttl-seconds: 3600
```

## Health Checks

- Liveness: http://localhost:8080/api/v1/health/liveness
- Readiness: http://localhost:8080/api/v1/health/readiness
- Actuator: http://localhost:8080/api/v1/actuator/health

## Certification Checklist

- [x] Code compiles without errors
- [x] All tests pass (70%+ coverage)
- [x] Docker image builds successfully
- [x] Kubernetes manifests are valid
- [x] CI/CD pipeline configured
- [x] API documentation complete
- [x] Multi-tenant data isolation
- [x] Security configuration (JWT)
- [x] Hexagonal architecture compliance

## License

Copyright (c) 2024 Gogidix. All rights reserved.
