# Service Health Service - API Documentation

## Base URL

```
http://localhost:8082
```

## Overview

The Service Health Service provides REST APIs for tracking service health status, dependencies, and uptime metrics for all services in the Gogidix ecosystem.

## Authentication

All endpoints require JWT authentication:

```
Authorization: Bearer <jwt-token>
```

## API Endpoints

### Service Health

#### Get Service Health

Retrieves the current health status of a specific service.

```http
GET /health/services/{serviceName}?tenantId={tenantId}
```

**Path Parameters:**
- `serviceName` (required): Service name

**Query Parameters:**
- `tenantId` (optional): Tenant ID. Defaults to "default"

**Response:** `200 OK`

```json
{
  "id": "health-123",
  "tenantId": "tenant-1",
  "serviceName": "order-service",
  "serviceType": "AI_SERVICE",
  "status": "HEALTHY",
  "healthScore": 95,
  "scoreComponents": {
    "uptimeScore": 98.0,
    "responseTimeScore": 90.0,
    "errorRateScore": 95.0,
    "dependencyScore": 92.0
  },
  "details": {
    "message": "All systems operational"
  },
  "lastCheckAt": "2024-01-15T10:00:00Z",
  "consecutiveFailures": 0,
  "lastSuccessAt": "2024-01-15T10:00:00Z",
  "averageResponseTime": 150.0,
  "errorRate": 0.01,
  "uptimePercentage": 99.9,
  "instanceId": "order-service-1",
  "host": "server-1",
  "createdAt": "2024-01-01T00:00:00Z"
}
```

---

#### Get All Service Health

Retrieves health status for all services in a tenant.

```http
GET /health/services?tenantId={tenantId}
```

**Response:** `200 OK`

```json
[
  {
    "id": "health-123",
    "serviceName": "order-service",
    "status": "HEALTHY",
    "healthScore": 95
  },
  {
    "id": "health-124",
    "serviceName": "payment-service",
    "status": "DEGRADED",
    "healthScore": 75
  }
]
```

---

#### Get Health Summary

Retrieves a summary of health statistics for a tenant.

```http
GET /health/summary?tenantId={tenantId}
```

**Response:** `200 OK`

```json
{
  "totalServices": 53,
  "healthyServices": 45,
  "degradedServices": 5,
  "unhealthyServices": 2,
  "downServices": 1,
  "unknownServices": 0,
  "averageHealthScore": 82.5,
  "statusBreakdown": {
    "HEALTHY": 45,
    "DEGRADED": 5,
    "UNHEALTHY": 2,
    "DOWN": 1,
    "UNKNOWN": 0
  },
  "serviceTypeBreakdown": {
    "AI_SERVICE": 28,
    "ORCHESTRATION_SERVICE": 25
  }
}
```

---

#### Get Services by Status

Retrieves services filtered by health status.

```http
GET /health/services/status/{status}?tenantId={tenantId}
```

**Path Parameters:**
- `status` (required): Health status (HEALTHY, DEGRADED, UNHEALTHY, DOWN, UNKNOWN)

**Response:** `200 OK`

```json
[
  {
    "id": "health-123",
    "serviceName": "order-service",
    "status": "HEALTHY",
    "healthScore": 95
  }
]
```

---

#### Get Services by Type

Retrieves services filtered by service type.

```http
GET /health/services/type/{serviceType}?tenantId={tenantId}
```

**Path Parameters:**
- `serviceType` (required): Service type (AI_SERVICE, ORCHESTRATION_SERVICE)

**Response:** `200 OK`

```json
[
  {
    "id": "health-123",
    "serviceName": "order-service",
    "serviceType": "AI_SERVICE",
    "status": "HEALTHY"
  }
]
```

---

### Service Dependencies

#### Get Service Dependencies

Retrieves the dependencies for a specific service.

```http
GET /health/dependencies/{serviceName}?tenantId={tenantId}
```

**Path Parameters:**
- `serviceName` (required): Service name

**Response:** `200 OK`

```json
[
  {
    "id": "dep-123",
    "serviceName": "order-service",
    "dependsOnService": "database-service",
    "dependencyType": "DATABASE",
    "isCritical": true,
    "healthImpact": 0.9,
    "status": "ACTIVE",
    "lastVerifiedAt": "2024-01-15T10:00:00Z",
    "endpoints": [
      {
        "url": "jdbc:postgresql://db:5432/orders",
        "method": "TCP",
        "averageLatency": 5.0,
        "successRate": 0.999
      }
    ]
  }
]
```

---

#### Get Service Dependents

Retrieves services that depend on a specific service.

```http
GET /health/dependents/{serviceName}?tenantId={tenantId}
```

**Path Parameters:**
- `serviceName` (required): Service name

**Response:** `200 OK`

```json
[
  {
    "id": "dep-124",
    "serviceName": "order-service",
    "dependsOnService": "database-service",
    "dependencyType": "DATABASE"
  }
]
```

---

#### Register Dependency

Registers a service dependency relationship.

```http
POST /health/dependencies?tenantId={tenantId}
Content-Type: application/json
```

**Request Body:**

```json
{
  "serviceName": "order-service",
  "dependsOnService": "database-service",
  "dependencyType": "DATABASE",
  "isCritical": true,
  "healthImpact": 0.9,
  "endpoints": [
    {
      "url": "jdbc:postgresql://db:5432/orders",
      "method": "TCP"
    }
  ]
}
```

**Response:** `201 Created`

```json
{
  "id": "dep-123",
  "serviceName": "order-service",
  "dependsOnService": "database-service",
  "status": "ACTIVE"
}
```

---

### Health Check

#### Service Health

```http
GET /health
```

**Response:** `200 OK`

```json
{
  "status": "UP",
  "service": "service-health-service",
  "timestamp": 1705339200000
}
```

#### Liveness Probe

```http
GET /health/liveness
```

#### Readiness Probe

```http
GET /health/readiness
```

---

## Data Types

### Health Status Enum

| Value | Description |
|-------|-------------|
| HEALTHY | Service is fully operational (score: 90-100) |
| DEGRADED | Service has minor issues (score: 70-89) |
| UNHEALTHY | Service has significant issues (score: 50-69) |
| DOWN | Service is not operational (score: 0-49) |
| UNKNOWN | Service status cannot be determined (score: null) |

### Dependency Type Enum

| Value | Description |
|-------|-------------|
| REST_API | RESTful API dependency |
| GRPC | gRPC dependency |
| MESSAGE_QUEUE | Message queue dependency (Kafka, RabbitMQ) |
| DATABASE | Database dependency |
| CACHE | Cache dependency (Redis, Memcached) |
| EVENT_STREAM | Event stream dependency |
| INTERNAL | Internal module dependency |

### Dependency Status Enum

| Value | Description |
|-------|-------------|
| ACTIVE | Dependency is actively used |
| INACTIVE | Dependency is not currently used |
| DEGRADED | Dependency has degraded performance |
| FAILED | Dependency is not accessible |
| UNKNOWN | Dependency status unknown |

---

## Error Responses

| Status Code | Description |
|-------------|-------------|
| 400 | Bad Request - Invalid input |
| 401 | Unauthorized - Missing or invalid token |
| 403 | Forbidden - Insufficient permissions |
| 404 | Not Found - Service not found |
| 422 | Unprocessable Entity - Validation failed |
| 500 | Internal Server Error |

**Error Response Format:**

```json
{
  "errorCode": "NOT_FOUND",
  "message": "Service 'order-service' not found",
  "timestamp": "2024-01-15T10:00:00Z"
}
```

---

## Rate Limiting

| Endpoint | Rate Limit |
|----------|-----------|
| GET /health/services | 100 requests/minute per tenant |
| GET /health/summary | 60 requests/minute per tenant |
| POST /health/dependencies | 20 requests/minute per tenant |

---

## WebSocket Support

Health status updates can be streamed via WebSocket:

```
ws://localhost:8082/ws/health-updates
```

**Message Format:**

```json
{
  "type": "HEALTH_STATUS_CHANGED",
  "data": {
    "serviceName": "order-service",
    "oldStatus": "HEALTHY",
    "newStatus": "DEGRADED",
    "healthScore": 72,
    "timestamp": "2024-01-15T10:00:00Z"
  }
}
```

---

## Health Score Calculation

The health score is computed as a weighted average:

| Component | Weight | Description |
|-----------|--------|-------------|
| Uptime Score | 40% | Based on uptime percentage over time window |
| Response Time Score | 25% | Based on average response time vs SLA |
| Error Rate Score | 20% | Based on error rate vs threshold |
| Dependency Score | 15% | Based on health of critical dependencies |

**Score to Status Mapping:**
- 90-100: HEALTHY
- 70-89: DEGRADED
- 50-69: UNHEALTHY
- 0-49: DOWN
- null: UNKNOWN
