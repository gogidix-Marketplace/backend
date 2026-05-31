# Tenant Management Service

## Description
Multi-tenant management service for the Gogidix platform. Handles tenant CRUD operations, configuration, and multi-tenant data isolation enforcement.

## Features
- Tenant CRUD operations
- Multi-tenant data isolation at the database level
- Tenant-specific configuration and feature flags
- Event publishing for tenant lifecycle changes
- Tenant quota management (users, storage)
- Integration with authentication service

## Architecture
This service follows hexagonal architecture with clear separation of concerns:

- **Domain Layer**: Tenant entity, repository ports, domain events
- **Application Layer**: CQRS commands/queries, DTOs, mappers, services
- **Infrastructure Layer**: MongoDB repository, Kafka event publisher, configuration
- **Interface Layer**: REST controllers, API DTOs
- **Shared Layer**: Exception handling, tenant context propagation

## Multi-Tenancy
This service enforces multi-tenant data isolation:
- All operations require `X-Tenant-ID` header
- Database queries are automatically filtered by tenant
- Cross-tenant access attempts are blocked
- Tenant context is propagated to all layers

## API Documentation
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Spec**: `http://localhost:8080/v3/api-docs`

## Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| SERVICE_NAME | Service name | tenant-management-service |
| SPRING_DATA_MONGODB_URI | MongoDB URI | mongodb://localhost:27017/tenant_db |
| KAFKA_BOOTSTRAP_SERVERS | Kafka brokers | localhost:9092 |
| EUREKA_URL | Eureka server URL | http://localhost:8761/eureka/ |
| SERVER_PORT | HTTP port | 8080 |
| kafka.topic.prefix | Kafka topic prefix | tenant |

### Profiles
- `dev`: Local development with debug logging
- `prod`: Production environment with optimized settings

## Running Locally
```bash
mvn spring-boot:run
```

## Building
```bash
mvn clean package
```

## Docker
```bash
# Build image
docker build -t gogidix/tenant-management-service:1.0.0 .

# Run container
docker run -p 8080:8080 \
  -e SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/tenant_db \
  -e KAFKA_BOOTSTRAP_SERVERS=localhost:9092 \
  gogidix/tenant-management-service:1.0.0
```

## Kubernetes
```bash
# Apply manifests
kubectl apply -f k8s/

# Check deployment
kubectl rollout status deployment/tenant-management-service
```

## Testing
```bash
# Unit tests
mvn test

# Coverage report
mvn jacoco:report

# Full certification
mvn verify -Pcertification
```

## Health Check
```bash
# Health endpoint
curl http://localhost:8080/actuator/health

# Readiness check
curl http://localhost:8080/actuator/health/readiness
```

## API Endpoints

### Create Tenant
```bash
POST /api/v1/tenants
Headers:
  X-Tenant-ID: default
  Content-Type: application/json

Body:
{
  "name": "Acme Corp",
  "domain": "acme.com",
  "primaryContactEmail": "admin@acme.com",
  "primaryContactName": "John Doe",
  "maxUsers": 100,
  "maxStorageGB": 100
}
```

### Get Tenant
```bash
GET /api/v1/tenants/{id}
Headers:
  X-Tenant-ID: default
```

### Update Tenant
```bash
PUT /api/v1/tenants/{id}
Headers:
  X-Tenant-ID: default
  Content-Type: application/json
```

### Delete Tenant
```bash
DELETE /api/v1/tenants/{id}
Headers:
  X-Tenant-ID: default
```

### List Tenants
```bash
GET /api/v1/tenants?page=0&size=20
Headers:
  X-Tenant-ID: default
```

## Events Published

| Event | Description | Topic |
|-------|-------------|-------|
| TenantCreated | New tenant created | tenant.{tenantId}.events |
| TenantUpdated | Tenant updated | tenant.{tenantId}.events |
| TenantDeleted | Tenant deleted | tenant.{tenantId}.events |

## Events Consumed
None (this service only publishes events)

## Tenant Context Propagation

All requests must include the following headers:

```
X-Tenant-ID: {tenant-id}          # Required
X-User-ID: {user-id}                # Optional
X-Correlation-ID: {correlation-id}  # Optional
X-Roles: ROLE1,ROLE2                # Optional
X-Username: {username}              # Optional
X-Email: {email}                    # Optional
```

## Database Schema

**Collection:** `tenants`

```javascript
{
  "_id": ObjectId("..."),
  "tenantId": "acme-corp",          // Unique tenant identifier
  "name": "Acme Corporation",
  "domain": "acme.com",
  "logoUrl": "https://s3.../logo.png",
  "status": "ACTIVE",                // ACTIVE, INACTIVE, SUSPENDED, TRIAL, PENDING_VERIFICATION
  "plan": "ENTERPRISE",              // FREE, STARTER, PROFESSIONAL, ENTERPRISE, CUSTOM
  "trialEndsAt": ISODate("2024-12-31"),
  "settings": {
    "featureX": true,
    "featureY": false
  },
  "features": {
    "maxUsers": 100,
    "maxStorageGB": 100
  },
  "primaryContactEmail": "admin@acme.com",
  "primaryContactName": "John Doe",
  "maxUsers": 100,
  "maxStorageGB": 100,
  "createdAt": ISODate("2024-01-01"),
  "updatedAt": ISODate("2024-01-01")
}
```

## Security
- Multi-tenant data isolation enforced at MongoDB query level
- All database queries include tenantId filter
- Cross-tenant data access returns 404 (tenant not found in current tenant context)

## Monitoring

Metrics exposed at:
- Prometheus: `http://localhost:8080/actuator/prometheus`
- Health: `http://localhost:8080/actuator/health`

## Troubleshooting

### Issue: "TenantContext not set"
**Solution**: Ensure `X-Tenant-ID` header is included in all requests

### Issue: "Cross-tenant access blocked"
**Solution**: Verify the tenant ID matches the requesting user's tenant

### Issue: "Connection to MongoDB refused"
**Solution**: Ensure MongoDB is running and accessible

## Team
- Platform Team
- Security Team

## Version
1.0.0
