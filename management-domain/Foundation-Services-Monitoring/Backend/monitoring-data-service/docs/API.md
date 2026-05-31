# Monitoring Data Service - API Documentation

## Base URL

```
http://localhost:8081
```

## Overview

The Monitoring Data Service provides REST APIs and WebSocket endpoints for collecting, querying, and aggregating time-series metrics from all services in the Gogidix ecosystem.

## Authentication

All endpoints require JWT authentication:

```
Authorization: Bearer <jwt-token>
```

## API Endpoints

### Metrics Collection

#### Collect Single Metric

Submits a single metric data point.

```http
POST /metrics?tenantId={tenantId}
Content-Type: application/json
```

**Query Parameters:**
- `tenantId` (optional): Tenant ID. Defaults to "default"

**Request Body:**

```json
{
  "serviceName": "order-service",
  "serviceType": "AI_SERVICE",
  "metricName": "cpu.usage",
  "value": 75.5,
  "unit": "percent",
  "metricType": "GAUGE",
  "timestamp": 1705339200000,
  "tags": {
    "host": "server-1",
    "region": "us-east-1"
  },
  "host": "server-1",
  "instanceId": "order-service-1",
  "correlationId": "req-123",
  "category": "performance"
}
```

**Response:** `201 Created`

```json
{
  "id": "metric-123",
  "tenantId": "tenant-1",
  "serviceName": "order-service",
  "serviceType": "AI_SERVICE",
  "metricName": "cpu.usage",
  "value": 75.5,
  "unit": "percent",
  "metricType": "GAUGE",
  "tags": {
    "host": "server-1",
    "region": "us-east-1"
  },
  "timestamp": "2024-01-15T10:00:00Z",
  "createdAt": "2024-01-15T10:00:00Z"
}
```

---

#### Collect Batch Metrics

Submits multiple metric data points in a single request.

```http
POST /metrics/batch?tenantId={tenantId}
Content-Type: application/json
```

**Request Body:**

```json
{
  "metrics": [
    {
      "serviceName": "order-service",
      "metricName": "cpu.usage",
      "value": 75.5,
      "unit": "percent"
    },
    {
      "serviceName": "order-service",
      "metricName": "memory.usage",
      "value": 60.0,
      "unit": "percent"
    }
  ]
}
```

**Response:** `201 Created`

```json
[
  {
    "id": "metric-123",
    "serviceName": "order-service",
    "metricName": "cpu.usage",
    "value": 75.5
  },
  {
    "id": "metric-124",
    "serviceName": "order-service",
    "metricName": "memory.usage",
    "value": 60.0
  }
]
```

---

### Metrics Query

#### Query Metrics

Queries metrics with time range and optional aggregation.

```http
POST /metrics/query?tenantId={tenantId}
Content-Type: application/json
```

**Request Body:**

```json
{
  "serviceName": "order-service",
  "metricName": "cpu.usage",
  "startTime": "2024-01-15T09:00:00Z",
  "endTime": "2024-01-15T10:00:00Z"
}
```

**Response:** `200 OK`

```json
{
  "serviceName": "order-service",
  "metricName": "cpu.usage",
  "startTime": "2024-01-15T09:00:00Z",
  "endTime": "2024-01-15T10:00:00Z",
  "dataPointCount": 60,
  "statistics": {
    "min": 45.2,
    "max": 89.7,
    "avg": 68.5,
    "sum": 4110.0,
    "p50": 67.8,
    "p95": 85.2,
    "p99": 88.9
  },
  "dataPoints": [
    {
      "timestamp": "2024-01-15T09:00:00Z",
      "value": 65.3
    },
    {
      "timestamp": "2024-01-15T09:01:00Z",
      "value": 67.8
    }
  ]
}
```

---

#### Get Metrics by Service

Retrieves metrics for a specific service.

```http
GET /metrics/services/{serviceName}?tenantId={tenantId}&metricName={metricName}&startTime={startTime}&endTime={endTime}
```

**Path Parameters:**
- `serviceName` (required): Service name

**Query Parameters:**
- `tenantId` (optional): Tenant ID. Defaults to "default"
- `metricName` (optional): Filter by metric name
- `startTime` (required): Start time in epoch milliseconds
- `endTime` (required): End time in epoch milliseconds

**Example:**

```
GET /metrics/services/order-service?tenantId=tenant-1&metricName=cpu.usage&startTime=1705339200000&endTime=1705342800000
```

---

#### Get Aggregated Metrics

Retrieves pre-aggregated metrics for a time window.

```http
GET /metrics/services/{serviceName}/aggregated?tenantId={tenantId}&metricName={metricName}&window={window}&startTime={startTime}&endTime={endTime}
```

**Path Parameters:**
- `serviceName` (required): Service name
- `metricName` (required): Metric name

**Query Parameters:**
- `tenantId` (optional): Tenant ID
- `window` (required): Aggregation window (ONE_MINUTE, FIVE_MINUTES, FIFTEEN_MINUTES, ONE_HOUR, SIX_HOURS, ONE_DAY)
- `startTime` (required): Start time in epoch milliseconds
- `endTime` (required): End time in epoch milliseconds

**Example:**

```
GET /metrics/services/order-service/aggregated?window=FIVE_MINUTES&startTime=1705339200000&endTime=1705342800000
```

---

#### Get Metric Count

Returns the total count of metrics for a tenant.

```http
GET /metrics/count?tenantId={tenantId}
```

**Response:** `200 OK`

```json
1000000
```

---

### Service Registration

#### Register Service

Registers a service for monitoring.

```http
POST /services/register?tenantId={tenantId}
Content-Type: application/json
```

**Request Body:**

```json
{
  "serviceName": "order-service",
  "serviceType": "AI_SERVICE",
  "category": "ecommerce",
  "version": "1.0.0",
  "description": "Order processing service",
  "baseUrl": "http://order-service:8080",
  "healthEndpoint": "/actuator/health",
  "metricsEndpoint": "/actuator/metrics",
  "collectionInterval": 60,
  "enabled": true,
  "tags": {
    "team": "orders",
    "environment": "production"
  },
  "metadata": {
    "owner": "orders-team",
    "costCenter": "cc-123"
  }
}
```

**Response:** `201 Created`

```json
{
  "serviceId": "svc-123",
  "tenantId": "tenant-1",
  "serviceName": "order-service",
  "serviceType": "AI_SERVICE",
  "status": "REGISTERED",
  "registeredAt": "2024-01-15T10:00:00Z"
}
```

---

#### Get Service

Retrieves a service registration by ID.

```http
GET /services/{serviceId}?tenantId={tenantId}
```

**Response:** `200 OK`

```json
{
  "serviceId": "svc-123",
  "tenantId": "tenant-1",
  "serviceName": "order-service",
  "serviceType": "AI_SERVICE",
  "category": "ecommerce",
  "version": "1.0.0",
  "description": "Order processing service",
  "baseUrl": "http://order-service:8080",
  "healthEndpoint": "/actuator/health",
  "metricsEndpoint": "/actuator/metrics",
  "collectionInterval": 60,
  "enabled": true,
  "status": "ACTIVE",
  "registeredAt": "2024-01-15T10:00:00Z",
  "lastHeartbeat": "2024-01-15T10:05:00Z"
}
```

---

#### List Services

Retrieves all services for a tenant.

```http
GET /services?tenantId={tenantId}
```

---

#### Update Heartbeat

Updates the heartbeat timestamp for a service.

```http
POST /services/{serviceId}/heartbeat?tenantId={tenantId}
```

---

#### Unregister Service

Removes a service from monitoring.

```http
DELETE /services/{serviceId}?tenantId={tenantId}
```

**Response:** `204 No Content`

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
  "service": "monitoring-data-service",
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

## WebSocket API

### Connect to Metric Stream

Connect to receive real-time metric updates.

```
ws://localhost:8081/ws/metrics
```

**Message Format:**

```json
{
  "type": "METRIC_UPDATE",
  "data": {
    "serviceName": "order-service",
    "metricName": "cpu.usage",
    "value": 75.5,
    "timestamp": "2024-01-15T10:00:00Z"
  }
}
```

---

## Error Responses

| Status Code | Description |
|-------------|-------------|
| 400 | Bad Request - Invalid input |
| 401 | Unauthorized - Missing or invalid token |
| 403 | Forbidden - Insufficient permissions |
| 404 | Not Found - Resource not found |
| 409 | Conflict - Resource already exists |
| 422 | Unprocessable Entity - Validation failed |
| 500 | Internal Server Error |

**Error Response Format:**

```json
{
  "errorCode": "VALIDATION_ERROR",
  "message": "Invalid metric data point",
  "timestamp": "2024-01-15T10:00:00Z"
}
```

---

## Rate Limiting

- Single metric: 10,000 requests/minute per tenant
- Batch metrics: 1,000 requests/minute per tenant
- Query metrics: 100 requests/minute per tenant

---

## Pagination

For list endpoints, use the following parameters:

- `page`: Page number (0-based, default: 0)
- `size`: Page size (default: 20, max: 100)

**Response Headers:**
- `X-Total-Count`: Total number of items
- `X-Page-Number`: Current page number
- X-Page-Size`: Items per page
