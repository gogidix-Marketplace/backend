# Tenant Config Service

Multi-tenant configuration and management service for the Courier Services platform.

## Overview

The Tenant Config Service provides centralized configuration management for multi-tenant courier operations. Each tenant represents an independent courier company with their own settings, branding, and business rules.

## Features

- Multi-tenant configuration management
- Tenant-specific feature flags
- White-label branding configuration
- Rate limiting per tenant
- Tenant analytics and metrics
- Dynamic configuration updates

## API Endpoints

### Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/tenants/{tenantId}` | Get tenant configuration |
| PUT | `/api/v1/tenants/{tenantId}` | Update tenant configuration |
| POST | `/api/v1/tenants` | Create new tenant |
| DELETE | `/api/v1/tenants/{tenantId}` | Delete tenant |

### Configuration

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/tenants/{tenantId}/settings` | Get tenant settings |
| PUT | `/api/v1/tenants/{tenantId}/settings` | Update tenant settings |
| GET | `/api/v1/tenants/{tenantId}/features` | Get feature flags |
| PUT | `/api/v1/tenants/{tenantId}/features` | Update feature flags |

## Configuration

### Application Properties

```yaml
spring:
  application:
    name: tenant-config-service
  data:
    mongodb:
      uri: ${MONGODB_URI:mongodb://localhost:27017}
      database: ${MONGODB_DATABASE:courier_core}

server:
  port: 8100
```

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `MONGODB_URI` | MongoDB connection string | mongodb://localhost:27017 |
| `MONGODB_DATABASE` | Database name | courier_core |
| `KAFKA_BOOTSTRAP_SERVERS` | Kafka brokers | localhost:9092 |
| `REDIS_HOST` | Redis host | localhost |
| `REDIS_PORT` | Redis port | 6379 |

## Local Development

### Prerequisites

- Java 17+
- Maven 3.9+
- MongoDB 6.x
- Redis 7.x

### Running Locally

```bash
# Build the service
mvn clean install

# Run the service
mvn spring-boot:run -Dspring-boot.run.profiles=local

# With custom port
mvn spring-boot:run -Dspring-boot.run.profiles=local -Dserver.port=8100
```

### Docker

```bash
# Build image
docker build -t courier/tenant-config-service:latest .

# Run container
docker run -p 8100:8100 \
  -e MONGODB_URI=mongodb://host.docker.internal:27017 \
  courier/tenant-config-service:latest
```

## Testing

```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify -P integration-tests

# Run with coverage
mvn test jacoco:report
```

## Service Configuration

The service uses the following configuration structure:

### Tenant Configuration Schema

```json
{
  "id": "uuid",
  "name": "Acme Couriers",
  "domain": "acme.gogidix.com",
  "logo": "https://s3.amazonaws.com/logos/acme.png",
  "settings": {
    "currency": "USD",
    "timezone": "America/New_York",
    "supportedLanguages": ["en", "es"],
    "dateFormat": "MM/DD/YYYY"
  },
  "features": {
    "surgePricing": true,
    "scheduledDispatches": true,
    "realTimeTracking": true,
    "signatureRequired": false
  },
  "limits": {
    "maxDispatchesPerDay": 1000,
    "maxDriversPerFleet": 100,
    "apiRateLimit": 1000
  },
  "branding": {
    "primaryColor": "#007bff",
    "secondaryColor": "#6c757d",
    "customDomain": "dispatch.acme.com"
  }
}
```

## Monitoring

The service exposes metrics at `/actuator/prometheus`:

- `tenant_requests_total`: Total requests per tenant
- `tenant_config_updates_total`: Configuration update count
- `tenant_cache_hits_total`: Cache hit rate
- `tenant_api_latency_seconds`: API response times

## Dependencies

- Spring Boot 3.x
- Spring Data MongoDB
- Spring Kafka
- Redis (for caching)
- Micrometer (metrics)

## Health Checks

```bash
# Liveness probe
curl http://localhost:8100/actuator/health/liveness

# Readiness probe
curl http://localhost:8100/actuator/health/readiness

# Health check
curl http://localhost:8100/actuator/health
```

## Related Services

- **Dispatch Core Service**: Uses tenant configuration for dispatch operations
- **Pricing Engine**: Applies tenant-specific pricing rules
- **Notification Service**: Uses tenant branding for notifications

## Support

For issues and questions:
- GitHub: https://github.com/gogidix/courier-services/issues
- Documentation: https://docs.gogidix.com/courier/tenant-config
