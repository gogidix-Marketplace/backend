# Batch Aggregation Service - API Documentation

## Base URL
```
http://localhost:8080/api/v1/batch-aggregation
```

## Overview
The Batch Aggregation Service provides REST APIs for triggering and monitoring batch aggregation jobs, as well as retrieving aggregated business metrics.

---

## Health Check

### Check Service Health
```http
GET /actuator/health
```

**Response:**
```json
{
  "status": "UP",
  "components": {
    "db": { "status": "UP" },
    "kafka": { "status": "UP" },
    "redis": { "status": "UP" }
  }
}
```

---

## Aggregation Jobs

### Trigger Manual Aggregation
Triggers an immediate aggregation job for specified scope.

```http
POST /api/v1/batch-aggregation/jobs/trigger
Content-Type: application/json
```

**Request Body:**
```json
{
  "scope": "REGIONAL",
  "regions": ["EUROPE", "ASIA"],
  "startDate": "2024-01-01",
  "endDate": "2024-01-31",
  "metrics": ["revenue", "orders", "users"]
}
```

**Response:**
```json
{
  "jobId": "JOB-20240123-143052-a1b2c3d4",
  "status": "SUBMITTED",
  "submittedAt": "2024-01-23T14:30:52Z",
  "estimatedCompletion": "2024-01-23T14:35:52Z"
}
```

### Get Job Status
Retrieves the current status of an aggregation job.

```http
GET /api/v1/batch-aggregation/jobs/{jobId}
```

**Path Parameters:**
- `jobId`: Unique job identifier

**Response:**
```json
{
  "jobId": "JOB-20240123-143052-a1b2c3d4",
  "status": "COMPLETED",
  "progress": 100,
  "submittedAt": "2024-01-23T14:30:52Z",
  "startedAt": "2024-01-23T14:31:00Z",
  "completedAt": "2024-01-23T14:34:45Z",
  "recordsProcessed": 15420,
  "recordsFailed": 3,
  "errorMessage": null
}
```

### List Recent Jobs
Retrieves a paginated list of recent aggregation jobs.

```http
GET /api/v1/batch-aggregation/jobs?page=0&size=20&sort=submittedAt,desc
```

**Query Parameters:**
- `page`: Page number (default: 0)
- `size`: Page size (default: 20)
- `sort`: Sort field and direction

**Response:**
```json
{
  "content": [
    {
      "jobId": "JOB-20240123-143052-a1b2c3d4",
      "status": "COMPLETED",
      "submittedAt": "2024-01-23T14:30:52Z",
      "recordsProcessed": 15420
    }
  ],
  "totalElements": 145,
  "totalPages": 8,
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20
  }
}
```

---

## Aggregated Metrics

### Get Global Metrics
Retrieves globally aggregated business metrics.

```http
GET /api/v1/batch-aggregation/metrics/global?from=2024-01-01&to=2024-01-31
```

**Query Parameters:**
- `from`: Start date (ISO format)
- `to`: End date (ISO format)
- `includeTrends`: Include trend indicators (default: true)

**Response:**
```json
{
  "period": {
    "from": "2024-01-01",
    "to": "2024-01-31"
  },
  "metrics": {
    "totalRevenue": {
      "value": 125000000.00,
      "currency": "USD",
      "change": 12.5,
      "trend": "UP"
    },
    "totalOrders": {
      "value": 45230,
      "change": 8.3,
      "trend": "UP"
    },
    "activeUsers": {
      "value": 125000,
      "change": 15.2,
      "trend": "UP"
    },
    "averageOrderValue": {
      "value": 2764.52,
      "currency": "USD",
      "change": -2.1,
      "trend": "DOWN"
    }
  },
  "topRegions": [
    {
      "region": "NORTH_AMERICA",
      "revenue": 45000000.00,
      "contribution": 36.0
    },
    {
      "region": "EUROPE",
      "revenue": 38000000.00,
      "contribution": 30.4
    }
  ],
  "lastUpdated": "2024-01-31T23:59:59Z"
}
```

### Get Regional Metrics
Retrieves aggregated metrics for a specific region.

```http
GET /api/v1/batch-aggregation/metrics/regions/{region}?from=2024-01-01&to=2024-01-31
```

**Path Parameters:**
- `region`: Region code (e.g., EUROPE, ASIA, NORTH_AMERICA)

**Response:**
```json
{
  "region": "EUROPE",
  "period": {
    "from": "2024-01-01",
    "to": "2024-01-31"
  },
  "metrics": {
    "totalRevenue": 38000000.00,
    "totalOrders": 12500,
    "activeUsers": 42000,
    "conversionRate": 3.2,
    "averageOrderValue": 3040.00
  },
  "topCountries": [
    {
      "countryCode": "DE",
      "countryName": "Germany",
      "revenue": 12500000.00,
      "orders": 4200
    },
    {
      "countryCode": "FR",
      "countryName": "France",
      "revenue": 9800000.00,
      "orders": 3500
    }
  ],
  "lastUpdated": "2024-01-31T23:59:59Z"
}
```

### List All Regional Metrics
Retrieves metrics summaries for all regions.

```http
GET /api/v1/batch-aggregation/metrics/regions?from=2024-01-01&to=2024-01-31
```

**Response:**
```json
{
  "period": {
    "from": "2024-01-01",
    "to": "2024-01-31"
  },
  "regions": [
    {
      "region": "EUROPE",
      "totalRevenue": 38000000.00,
      "totalOrders": 12500,
      "activeUsers": 42000
    },
    {
      "region": "ASIA",
      "totalRevenue": 32000000.00,
      "totalOrders": 15200,
      "activeUsers": 58000
    }
  ],
  "lastUpdated": "2024-01-31T23:59:59Z"
}
```

---

## Trend Analysis

### Get Trend Analysis
Retrieves trend analysis for specified metrics over time.

```http
GET /api/v1/batch-aggregation/trends?metric=revenue&period=DAILY&from=2024-01-01&to=2024-01-31
```

**Query Parameters:**
- `metric`: Metric name (revenue, orders, users)
- `period`: Aggregation period (DAILY, WEEKLY, MONTHLY)
- `from`: Start date
- `to`: End date
- `region`: Optional region filter

**Response:**
```json
{
  "metric": "revenue",
  "period": "DAILY",
  "aggregation": "SUM",
  "dataPoints": [
    {
      "date": "2024-01-01",
      "value": 1250000.00,
      "change": 5.2
    },
    {
      "date": "2024-01-02",
      "value": 1320000.00,
      "change": 5.6
    }
  ],
  "summary": {
    "total": 38000000.00,
    "average": 1225806.45,
    "min": 980000.00,
    "max": 1650000.00,
    "growthRate": 12.5
  }
}
```

---

## Error Responses

### Standard Error Format
```json
{
  "timestamp": "2024-01-23T14:30:52Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid date range specified",
  "path": "/api/v1/batch-aggregation/metrics/global"
}
```

### HTTP Status Codes
- `200 OK`: Successful request
- `201 Created`: Resource created successfully
- `400 Bad Request`: Invalid request parameters
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server error
- `503 Service Unavailable`: Service temporarily unavailable

---

## Rate Limiting

- Default rate limit: 100 requests per minute per IP
- Headers included:
  - `X-RateLimit-Limit`: Request limit
  - `X-RateLimit-Remaining`: Remaining requests
  - `X-RateLimit-Reset`: Reset time (Unix timestamp)

---

## Authentication

Include API key in request header:
```http
Authorization: Bearer {api-key}
```

Or use service-to-service authentication:
```http
X-Service-Auth: {jwt-token}
```
