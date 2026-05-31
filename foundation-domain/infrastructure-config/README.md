# Infrastructure Config Service

Centralized configuration management for the Gogidix ecosystem.

## Features

- **Configuration Properties**: Environment-specific configuration with type-safe values
- **Feature Flags**: Dynamic feature toggling with multiple rollout strategies
- **Secret Management**: Encrypted storage for sensitive credentials
- **Version History**: Complete audit trail and rollback capability
- **Multi-Tenancy**: Full tenant isolation
- **High Performance**: Redis caching for fast access
- **Event-Driven**: Kafka integration for change notifications

## Quick Start

### Prerequisites

- Java 17+
- Maven 3.8+
- MongoDB 6.0+
- Redis 7.0+
- Kafka 3.0+

### Local Development

```bash
# Clone the repository
git clone <repository-url>
cd infrastructure-config

# Build the project
mvn clean install

# Run the service
mvn spring-boot:run
```

### Docker

```bash
# Build the image
docker build -t infrastructure-config:latest .

# Run with Docker Compose
docker-compose up -d
```

## API Documentation

Interactive API documentation available at:
- Swagger UI: http://localhost:8090/swagger-ui.html
- OpenAPI JSON: http://localhost:8090/api-docs

## Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `MONGODB_URI` | MongoDB connection string | `mongodb://localhost:27017` |
| `MONGODB_DATABASE` | MongoDB database name | `infrastructure_config` |
| `REDIS_HOST` | Redis host | `localhost` |
| `REDIS_PORT` | Redis port | `6379` |
| `KAFKA_BOOTSTRAP_SERVERS` | Kafka brokers | `localhost:9092` |
| `ENCRYPTION_KEY` | Secret encryption key | (must be set) |
| `SERVER_PORT` | Server port | `8090` |

### Profiles

- `dev`: Development with debug logging
- `staging`: Staging environment
- `prod`: Production with optimized settings

```bash
# Run with profile
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## Project Structure

```
infrastructure-config/
├── src/
│   ├── main/
│   │   ├── java/com/gogidix/infrastructure/config/
│   │   │   ├── application/          # Application services
│   │   │   ├── domain/               # Domain models and repositories
│   │   │   ├── infrastructure/       # Infrastructure layer
│   │   │   ├── interfaces/           # REST controllers
│   │   │   └── InfrastructureConfigServiceApplication.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/                         # Test suite
├── docs/                             # Documentation
│   ├── ARCHITECTURE.md
│   ├── API.md
│   └── BUSINESS_USE_CASES.md
├── .github/workflows/                # CI/CD pipelines
├── Dockerfile
├── pom.xml
└── README.md
```

## Development

### Running Tests

```bash
# Unit tests
mvn test

# Integration tests
mvn verify -Dspring.profiles.active=integration-test

# With coverage
mvn test jacoco:report
```

### Code Quality

```bash
# Checkstyle
mvn checkstyle:check

# SpotBugs
mvn spotbugs:check

# PMD
mvn pmd:check
```

## Deployment

### Kubernetes

```bash
# Apply manifests
kubectl apply -f k8s/

# Check status
kubectl get pods -l app=infrastructure-config
```

### Monitoring

Health check: http://localhost:8090/actuator/health

Metrics: http://localhost:8090/actuator/metrics

## Documentation

- [Architecture](docs/ARCHITECTURE.md)
- [API Reference](docs/API.md)
- [Business Use Cases](docs/BUSINESS_USE_CASES.md)
- [Production Certification](CERTIFICATION.md)

## License

Copyright (c) 2025 Gogidix. All rights reserved.
