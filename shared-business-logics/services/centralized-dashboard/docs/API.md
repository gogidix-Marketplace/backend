# Centralized Dashboard - API Documentation

## Overview

The Centralized Dashboard exposes a comprehensive REST API for accessing metrics, charts, real-time data, and reports. All APIs support multi-tenancy via the `X-Tenant-ID` header.

**Base URL:** `https://api.gogidix.com/api/v1`

**Authentication:** Bearer token required (except where noted)

---

## Table of Contents

1. [Gateway APIs](#gateway-apis)
2. [Chart APIs](#chart-apis)
3. [WebSocket APIs](#websocket-apis)
4. [Analytics APIs](#analytics-apis)
5. [Reporting APIs](#reporting-apis)
6. [Health Check APIs](#health-check-apis)
7. [Error Responses](#error-responses)
8. [Rate Limiting](#rate-limiting)

---

## Gateway APIs

### Get Dashboard Data

Retrieves comprehensive dashboard data including saga statistics, chart data, monitoring data, and service health.

```http
GET /api/v1/gateway/dashboard
```

**Headers:**
```
Authorization: Bearer <token>
X-Tenant-ID: default
Content-Type: application/json
```

**Response (200 OK):**
```json
{
  "sagaStatistics": {
    "running": 15,
    "completed": 1250,
    "failed": 3,
    "total": 1268
  },
  "chartData": {
    "revenue": { /* chart-specific data */ },
    "users": { /* chart-specific data */ }
  },
  "monitoringData": {
    "cpu": 65.5,
    "memory": 78.2,
    "disk": 45.1
  },
  "serviceHealth": {
    "api-gateway": {
      "serviceName": "api-gateway",
      "status": "UP",
      "baseUrl": "http://api-gateway:8080",
      "lastChecked": "2024-02-25T10:30:00Z",
      "responseTimeMs": 15
    }
  },
  "timestamp": "2024-02-25T10:30:00Z",
  "tenantId": "default"
}
```

**Error Responses:**
- `401 Unauthorized` - Invalid or missing token
- `403 Forbidden` - Insufficient permissions
- `503 Service Unavailable` - Upstream services down

---

### Get Service Health

Retrieves health status of all backend services.

```http
GET /api/v1/gateway/health/services
```

**Headers:**
```
Authorization: Bearer <token>
X-Tenant-ID: default
```

**Response (200 OK):**
```json
{
  "api-gateway": {
    "serviceName": "api-gateway",
    "status": "UP",
    "baseUrl": "http://api-gateway:8080",
    "lastChecked": "2024-02-25T10:30:00Z",
    "responseTimeMs": 15,
    "details": {
      "version": "1.0.0",
      "uptime": 86400
    }
  },
  "metrics-aggregation": {
    "serviceName": "metrics-aggregation",
    "status": "UP",
    "baseUrl": "http://metrics-aggregation:8080",
    "lastChecked": "2024-02-25T10:30:00Z",
    "responseTimeMs": 22
  }
}
```

---

### Get Saga Statistics

Retrieves statistics about saga orchestrations.

```http
GET /api/v1/gateway/sagas/statistics
```

**Response (200 OK):**
```json
{
  "running": 15,
  "completed": 1250,
  "failed": 3,
  "total": 1268,
  "byType": {
    "order-processing": { "running": 5, "completed": 500, "failed": 1 },
    "payment-processing": { "running": 3, "completed": 750, "failed": 2 }
  }
}
```

---

## Chart APIs

### Get Chart Summary

Retrieves summary statistics for all charts.

```http
GET /api/v1/charts/summary
```

**Response (200 OK):**
```json
{
  "totalCharts": 25,
  "activeCharts": 22,
  "totalDataPoints": 15420,
  "chartsByType": {
    "line": 10,
    "bar": 8,
    "pie": 4,
    "area": 3
  },
  "timestamp": "2024-02-25T10:30:00Z"
}
```

---

### Get Chart Data

Retrieves data for a specific chart.

```http
GET /api/v1/charts/{chartId}/data
```

**Path Parameters:**
- `chartId` (string) - Unique chart identifier

**Query Parameters:**
- `startTime` (string, optional) - ISO 8601 start timestamp
- `endTime` (string, optional) - ISO 8601 end timestamp

**Example:**
```http
GET /api/v1/charts/revenue-trend/data?startTime=2024-02-01T00:00:00Z&endTime=2024-02-25T23:59:59Z
```

**Response (200 OK):**
```json
{
  "chartId": "revenue-trend",
  "chartName": "Revenue Trend",
  "chartType": "line",
  "data": [
    {
      "label": "2024-02-01",
      "timestamp": "2024-02-01T00:00:00Z",
      "value": 15420.50,
      "metadata": {
        "currency": "USD",
        "region": "US"
      }
    }
  ],
  "metadata": {
    "unit": "currency",
    "aggregation": "daily"
  },
  "generatedAt": "2024-02-25T10:30:00Z",
  "tenantId": "default"
}
```

---

### Get Chart Metrics

Retrieves performance metrics for chart generation.

```http
GET /api/v1/charts/metrics
```

**Response (200 OK):**
```json
{
  "averageGenerationTime": 125,
  "requestCount": 1542,
  "cacheHitRate": 0.85,
  "popularCharts": ["revenue-trend", "user-growth", "conversion-rate"]
}
```

---

## WebSocket APIs

### Connect to WebSocket

Establishes a real-time connection for live updates.

**WebSocket URL:**
```
wss://api.gogidix.com/api/v1/websocket?tenantId=default&token=<jwt>
```

**Query Parameters:**
- `tenantId` (string) - Tenant identifier
- `token` (string) - Valid JWT token

**Connection Message (Client -> Server):**
```json
{
  "type": "subscribe",
  "topics": ["metrics", "alerts", "dashboard-updates"]
}
```

**Available Topics:**
- `metrics` - Real-time metric updates
- `alerts` - System alerts and notifications
- `dashboard-updates` - Dashboard data changes
- `saga-events` - Saga lifecycle events
- `service-health` - Service health status changes

**Update Message (Server -> Client):**
```json
{
  "topic": "metrics",
  "data": {
    "metricName": "revenue",
    "value": 15420.50,
    "timestamp": "2024-02-25T10:30:00Z",
    "changePercent": 5.2
  },
  "tenantId": "default"
}
```

**Heartbeat:**
```json
{
  "type": "ping",
  "timestamp": "2024-02-25T10:30:00Z"
}
```

---

### Get WebSocket Statistics

Retrieves statistics about WebSocket connections.

```http
GET /api/v1/websocket/statistics
```

**Response (200 OK):**
```json
{
  "totalConnections": 152,
  "tenants": 5,
  "topics": 8,
  "messagesPerSecond": 45,
  "timestamp": "2024-02-25T10:30:00Z"
}
```

---

### Broadcast Message

Broadcasts a message to all connected clients (admin only).

```http
POST /api/v1/websocket/broadcast?tenantId=all
```

**Headers:**
```
Authorization: Bearer <admin-token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "type": "notification",
  "title": "System Maintenance",
  "message": "Scheduled maintenance in 30 minutes",
  "severity": "warning"
}
```

**Response (200 OK):**
```json
{
  "status": "broadcasted",
  "recipientCount": 152,
  "timestamp": "2024-02-25T10:30:00Z"
}
```

---

## Analytics APIs

### Get Metrics

Retrieves aggregated metrics.

```http
GET /api/v1/analytics/metrics
```

**Query Parameters:**
- `metric` (string) - Metric name (e.g., sales, inventory, performance)
- `aggregation` (string) - Aggregation level: daily, weekly, monthly
- `from` (string) - Start date (ISO 8601)
- `to` (string) - End date (ISO 8601)

**Example:**
```http
GET /api/v1/analytics/metrics?metric=sales&aggregation=daily&from=2024-02-01&to=2024-02-25
```

**Response (200 OK):**
```json
{
  "metric": "sales",
  "aggregation": "daily",
  "data": [
    {
      "date": "2024-02-01",
      "value": 15420.50
    },
    {
      "date": "2024-02-02",
      "value": 16875.25
    }
  ],
  "summary": {
    "total": 345678.90,
    "average": 13827.16,
    "max": 16875.25,
    "min": 12345.00
  }
}
```

---

### Execute Query

Executes a custom analytics query.

```http
POST /api/v1/analytics/queries
```

**Request Body:**
```json
{
  "name": "User Growth Query",
  "query": {
    "dataSource": "users",
    "filters": {
      "registrationDate": {
        "from": "2024-01-01",
        "to": "2024-02-25"
      }
    },
    "groupBy": ["registrationDate"],
    "aggregations": {
      "count": "userId"
    }
  },
  "format": "json"
}
```

**Response (200 OK):**
```json
{
  "queryId": "q-123456",
  "status": "completed",
  "results": [
    {
      "registrationDate": "2024-01-01",
      "count": 152
    }
  ],
  "executionTimeMs": 125,
  "completedAt": "2024-02-25T10:30:00Z"
}
```

---

### Export Data

Exports analytics data in various formats.

```http
POST /api/v1/analytics/export
```

**Request Body:**
```json
{
  "query": {
    "dataSource": "sales",
    "filters": {}
  },
  "format": "csv",
  "includeHeaders": true
}
```

**Response (200 OK):**
CSV file download

---

## Reporting APIs

### Generate Sales Report

Generates a CSV sales report for a date range.

```http
GET /reports/sales
```

**Query Parameters:**
- `from` (string, optional) - Start date (YYYY-MM-DD)
- `to` (string, optional) - End date (YYYY-MM-DD)

**Response (200 OK):**
```
Content-Type: text/csv
Content-Disposition: attachment; filename="sales_report.csv"

date,amount
2024-02-01,15420.50
2024-02-02,16875.25
```

---

### Generate Inventory Report

Generates a CSV inventory report.

```http
GET /reports/inventory
```

**Response (200 OK):**
```
Content-Type: text/csv
Content-Disposition: attachment; filename="inventory_report.csv"

productId,quantity
PROD-001,150
PROD-002,75
```

---

### Generate Performance Report

Generates a CSV performance report.

```http
GET /reports/performance
```

**Response (200 OK):**
```
Content-Type: text/csv
Content-Disposition: attachment; filename="performance_report.csv"

service,avgLatency,avgErrorRate,avgThroughput
api-gateway,45.2,0.01,1250
analytics-service,120.5,0.05,450
```

---

## Health Check APIs

### Service Health

Standard health check endpoint (no authentication required).

```http
GET /health
```

**Response (200 OK):**
```json
{
  "status": "UP",
  "service": "centralized-reporting"
}
```

**Response (503 Service Unavailable):**
```json
{
  "status": "DOWN",
  "service": "centralized-reporting",
  "error": "Database connection failed"
}
```

---

### Liveness Probe

Kubernetes liveness probe.

```http
GET /actuator/health/liveness
```

**Response (200 OK):**
```json
{
  "status": "UP"
}
```

---

### Readiness Probe

Kubernetes readiness probe.

```http
GET /actuator/health/readiness
```

**Response (200 OK):**
```json
{
  "status": "UP",
  "components": {
    "db": { "status": "UP" },
    "disk": { "status": "UP" }
  }
}
```

---

## Error Responses

All error responses follow a consistent format:

```json
{
  "timestamp": "2024-02-25T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid date format",
  "path": "/api/v1/analytics/metrics",
  "requestId": "req-123456"
}
```

### HTTP Status Codes

| Code | Description |
|------|-------------|
| 200 | Success |
| 201 | Created |
| 204 | No Content |
| 400 | Bad Request - Invalid input |
| 401 | Unauthorized - Missing or invalid token |
| 403 | Forbidden - Insufficient permissions |
| 404 | Not Found - Resource doesn't exist |
| 409 | Conflict - Resource already exists |
| 422 | Unprocessable Entity - Validation failed |
| 429 | Too Many Requests - Rate limit exceeded |
| 500 | Internal Server Error |
| 502 | Bad Gateway - Upstream service error |
| 503 | Service Unavailable - Service temporarily down |
| 504 | Gateway Timeout - Upstream timeout |

---

## Rate Limiting

API requests are rate limited per tenant:

| Plan | Requests | Window |
|------|----------|--------|
| Free | 1,000 | 1 hour |
| Standard | 10,000 | 1 hour |
| Enterprise | Unlimited | - |

**Rate Limit Headers:**
```
X-RateLimit-Limit: 10000
X-RateLimit-Remaining: 8542
X-RateLimit-Reset: 1708857600
```

**Rate Limit Exceeded Response (429):**
```json
{
  "timestamp": "2024-02-25T10:30:00Z",
  "status": 429,
  "error": "Too Many Requests",
  "message": "Rate limit exceeded. Try again in 300 seconds.",
  "retryAfter": 300
}
```

---

## Pagination

List endpoints support pagination via query parameters:

```
?page=0&size=20&sort=createdAt,desc
```

**Pagination Response:**
```json
{
  "content": [ /* items */ ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "totalPages": 5,
    "totalElements": 100
  },
  "first": true,
  "last": false
}
```

---

## SDKs and Libraries

Official SDKs:

- **JavaScript/TypeScript:** `@gogidix/dashboard-sdk`
- **Python:** `gogidix-dashboard-python`
- **Java:** `com.gogidix:dashboard-client`

Example (JavaScript):
```javascript
import { DashboardClient } from '@gogidix/dashboard-sdk';

const client = new DashboardClient({
  apiKey: 'your-api-key',
  tenantId: 'default'
});

const data = await client.gateway.getDashboardData();
```

---

## API Versioning

The API uses URL-based versioning. Current version: `v1`

Deprecated versions will be supported for at least 6 months after deprecation notice.

---

## Changelog

### v1.2.0 (2024-02-25)
- Added WebSocket statistics endpoint
- Enhanced reporting with performance metrics
- Added pagination support

### v1.1.0 (2024-01-15)
- Added analytics export endpoint
- Enhanced error responses
- Added rate limiting headers

### v1.0.0 (2024-01-01)
- Initial API release
