# Platform Service

Core platform management service providing configuration management, feature flags, health monitoring, and platform announcements for the Gogidix platform.

## Features

- **Configuration Management**: Centralized platform configuration with versioning
- **Feature Flags**: Progressive rollout and A/B testing capabilities
- **Health Monitoring**: Real-time service health tracking
- **Platform Announcements**: System-wide notifications and announcements
- **Maintenance Windows**: Scheduled maintenance management

## Quick Start

### Prerequisites

- Java 17+
- Maven 3.9+
- PostgreSQL 14+
- Redis 7+
- Kafka 3+

### Local Development

1. Navigate to the service:
```bash
cd platform-service
```

2. Build the application:
```bash
mvn clean install
```

3. Run with Spring Boot:
```bash
mvn spring-boot:run
```

4. Or use Docker Compose:
```bash
docker-compose up -d
```

The service will be available at: `http://localhost:8401/platform`

## API Documentation

### Swagger UI

Interactive API documentation available at:
```
http://localhost:8401/platform/swagger-ui.html
```

## Architecture

### Technology Stack

- **Framework**: Spring Boot 3.1.5
- **Language**: Java 17
- **Database**: PostgreSQL 14 with Row-Level Security
- **Cache**: Redis 7
- **Messaging**: Kafka 3
- **Build**: Maven 3.9

### Domain Models

- **PlatformConfiguration**: Centralized configuration management
- **FeatureFlag**: Feature toggles with progressive rollout
- **ServiceHealthStatus**: Service health monitoring
- **PlatformAnnouncement**: Platform announcements
- **MaintenanceWindow**: Maintenance scheduling

## API Endpoints

### Configuration Management

- `POST /api/v1/platform/configurations` - Create configuration
- `GET /api/v1/platform/configurations` - List configurations
- `GET /api/v1/platform/configurations/{key}` - Get configuration by key

### Feature Flags

- `POST /api/v1/platform/feature-flags` - Create feature flag
- `GET /api/v1/platform/feature-flags` - List feature flags
- `GET /api/v1/platform/feature-flags/evaluate/{key}` - Evaluate flag for user
- `POST /api/v1/platform/feature-flags/{id}/enable` - Enable flag
- `POST /api/v1/platform/feature-flags/{id}/disable` - Disable flag

### Health Monitoring

- `POST /api/v1/platform/health/status` - Report health status
- `GET /api/v1/platform/health/status` - Get all service health
- `GET /api/v1/platform/health/summary` - Get health summary

### Platform Announcements

- `POST /api/v1/platform/announcements` - Create announcement
- `GET /api/v1/platform/announcements` - List active announcements
- `POST /api/v1/platform/announcements/{id}/dismiss` - Dismiss announcement

### Maintenance Windows

- `POST /api/v1/platform/maintenance-windows` - Create maintenance window
- `GET /api/v1/platform/maintenance-windows` - List maintenance windows
- `POST /api/v1/platform/maintenance-windows/{id}/start` - Start maintenance
- `POST /api/v1/platform/maintenance-windows/{id}/complete` - Complete maintenance

## Deployment

### Using Docker

```bash
# Build image
docker build -t platform-service:latest .

# Run container
docker run -p 8401:8401 \
  -e DATABASE_URL=jdbc:postgresql://localhost:5432/platform_service \
  -e DATABASE_USERNAME=postgres \
  -e DATABASE_PASSWORD=postgres \
  platform-service:latest
```

### Railway Deployment

```bash
# Install Railway CLI
npm install -g @railway/cli

# Login and deploy
railway login
railway init
railway up
```

## Testing

```bash
# Unit tests
mvn test

# Integration tests
mvn verify -P integration-test

# Coverage report
mvn jacoco:report
```

## Monitoring

### Health Check
```
GET /platform/actuator/health
```

### Metrics
```
GET /platform/actuator/metrics
GET /platform/actuator/prometheus
```

## Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DATABASE_URL` | PostgreSQL JDBC URL | `jdbc:postgresql://localhost:5432/platform_service` |
| `REDIS_HOST` | Redis host | `localhost` |
| `KAFKA_BOOTSTRAP_SERVERS` | Kafka servers | `localhost:9092` |
| `SERVER_PORT` | Service port | `8401` |

## Contributing

1. Create feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -am 'Add feature'`
3. Push branch: `git push origin feature/your-feature`
4. Create merge request

## License

Copyright © 2025 Gogidix Platform Team. All rights reserved.
