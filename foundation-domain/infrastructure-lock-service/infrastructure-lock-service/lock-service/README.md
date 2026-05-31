# Infrastructure Lock Service

A production-ready distributed locking service built with Spring Boot and Redis, providing multi-tenant resource locking capabilities for the Gogidix ecosystem.

## Features

- **Distributed Locking**: Redis-based distributed locks with Lua scripts for atomicity
- **Multi-Tenant Support**: Complete tenant isolation for SaaS environments
- **Lock Types**: Exclusive, Shared, Read, and Write lock modes
- **Retry Mechanisms**: Configurable retry policies with Resilience4j
- **Lock Statistics**: Real-time metrics and monitoring
- **REST API**: Comprehensive RESTful API with OpenAPI documentation
- **Auto Cleanup**: Scheduled cleanup of expired locks
- **Production Ready**: Docker images, Kubernetes manifests, CI/CD pipelines

## Quick Start

### Prerequisites

- Java 17+
- Redis 6.x+
- Maven 3.8+

### Run Locally

```bash
# Clone the repository
git clone <repository-url>
cd infrastructure-lock-service/lock-service

# Start Redis (using Docker)
docker run -d -p 6379:6379 redis:7-alpine

# Run the service
./mvnw spring-boot:run
```

The service will be available at `http://localhost:8080`

### Docker

```bash
# Build the image
docker build -t gogidix/lock-service:latest .

# Run with Redis
docker network create lock-network
docker run -d --name redis --network lock-network redis:7-alpine
docker run -d --name lock-service --network lock-network \
  -p 8080:8080 \
  -e REDIS_HOST=redis \
  gogidix/lock-service:latest
```

## Usage

### Acquire a Lock

```bash
curl -X POST http://localhost:8080/api/v1/locks/acquire \
  -H "X-Tenant-ID: tenant-123" \
  -H "Content-Type: application/json" \
  -d '{
    "tenantId": "tenant-123",
    "resourceKey": "order:456",
    "holderId": "service-instance-1",
    "lockType": "EXCLUSIVE",
    "ttlSeconds": 300
  }'
```

### Release a Lock

```bash
curl -X DELETE "http://localhost:8080/api/v1/locks/order:456/release" \
  -H "X-Tenant-ID: tenant-123" \
  -G -d "holderId=service-instance-1" \
  -d "lockId=<lock-id-from-acquire>"
```

### Check Lock Status

```bash
curl http://localhost:8080/api/v1/locks/order:456/status \
  -H "X-Tenant-ID: tenant-123"
```

## API Documentation

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

## Configuration

### Application Properties

```yaml
# Redis Configuration
spring.data.redis.host: localhost
spring.data.redis.port: 6379

# Lock Defaults
lock.default-ttl-seconds: 300
lock.default-wait-time-seconds: 30
lock.default-max-retries: 3

# Cleanup Configuration
lock.cleanup.enabled: true
lock.cleanup.cron: "0 0 * * * ?"
```

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| REDIS_HOST | Redis server host | localhost |
| REDIS_PORT | Redis server port | 6379 |
| REDIS_PASSWORD | Redis password | (none) |
| SERVER_PORT | Service port | 8080 |

## Project Structure

```
lock-service/
├── src/
│   ├── main/
│   │   ├── java/com/gogidix/infrastructure/lockservice/
│   │   │   ├── domain/                 # Domain models and repositories
│   │   │   ├── application/            # Application services and DTOs
│   │   │   ├── infrastructure/         # Infrastructure implementations
│   │   │   ├── interfaces/             # REST controllers
│   │   │   └── LockServiceApplication.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── application-dev.yml
│   └── test/                           # Unit and integration tests
├── docs/                               # Documentation
│   ├── ARCHITECTURE.md
│   ├── API.md
│   └── BUSINESS_USE_CASES.md
├── .github/workflows/                  # CI/CD pipelines
├── Dockerfile
├── pom.xml
├── CERTIFICATION.md
└── README.md
```

## Development

### Build

```bash
./mvnw clean install
```

### Run Tests

```bash
./mvnw test
```

### Check Coverage

```bash
./mvnw jacoco:report
```

View report at `target/site/jacoco/index.html`

## Deployment

### Kubernetes

```bash
kubectl apply -f k8s/
```

### Docker Compose

```bash
docker-compose up -d
```

## Monitoring

The service exposes metrics at `/actuator/prometheus` for Prometheus scraping.

Key metrics:
- `lock_active_locks`: Current active lock count
- `lock_failed_attempts`: Total failed acquisition attempts
- `lock_acquisition_time_ms`: Lock acquisition time

## Documentation

- [Architecture](docs/ARCHITECTURE.md) - System architecture and design
- [API Documentation](docs/API.md) - Complete API reference
- [Business Use Cases](docs/BUSINESS_USE_CASES.md) - Usage examples and patterns
- [Certification](CERTIFICATION.md) - Production readiness checklist

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## License

Apache License 2.0

## Support

For issues and questions:
- GitHub Issues: [Create an issue](../../issues)
- Documentation: [docs/](docs/)
- Email: support@gogidix.com
