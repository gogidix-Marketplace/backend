# Analytics Service - API Documentation

## Base URL

```
Production: https://analytics.gogidix.com/api/v1
Development: http://localhost:8086/api/v1
```

## Authentication

All API requests require a valid JWT token in the Authorization header:

```
Authorization: Bearer <jwt-token>
```

The JWT token contains:
- User ID (`sub`)
- Tenant ID (`tenantId`)
- User roles (`roles`)
- Token expiration (`exp`)

---

## API Endpoints

### Health Check

#### GET /actuator/health

Check service health status.

**Response:**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "MongoDB",
        "validationQuery": "isOk()"
      }
    },
    "redis": {
      "status": "UP"
    }
  }
}
```

---

## Marketing Metrics

### GET /metrics

Retrieve marketing metrics with optional filtering.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| tenantId | string | Yes | Tenant ID (from JWT) |
| metricType | string | No | Filter by metric type (IMPRESSIONS, CLICKS, CONVERSIONS, etc.) |
| channelType | string | No | Filter by channel type (EMAIL, SOCIAL, SEARCH, etc.) |
| campaignId | string | No | Filter by campaign ID |
| startDate | string | No | Start date (ISO-8601 format) |
| endDate | string | No | End date (ISO-8601 format) |
| granularity | string | No | Data granularity (HOURLY, DAILY, WEEKLY, MONTHLY) |
| page | integer | No | Page number (default: 0) |
| size | integer | No | Page size (default: 20, max: 100) |
| sort | string | No | Sort field and direction (field,asc/desc) |

**Response:**
```json
{
  "content": [
    {
      "id": "metric-123",
      "tenantId": "tenant-abc",
      "metricType": "IMPRESSIONS",
      "channelType": "SOCIAL",
      "campaignId": "campaign-456",
      "value": 10000,
      "previousValue": 8500,
      "percentChange": 17.65,
      "timestamp": "2024-01-15T10:00:00Z",
      "granularity": "DAILY",
      "dataSource": "Facebook Ads",
      "qualityScore": 0.95,
      "verified": true,
      "currency": null,
      "unit": "count",
      "targetValue": 12000,
      "targetStatus": "ON_TRACK",
      "createdAt": "2024-01-15T10:00:00Z",
      "updatedAt": "2024-01-15T10:05:00Z"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "totalPages": 5,
    "totalElements": 100,
    "first": true,
    "last": false
  }
}
```

### GET /metrics/{id}

Retrieve a specific marketing metric by ID.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Metric ID |

**Response:**
```json
{
  "id": "metric-123",
  "tenantId": "tenant-abc",
  "metricType": "CLICKS",
  "channelType": "SOCIAL",
  "value": 500,
  "timestamp": "2024-01-15T10:00:00Z"
}
```

### POST /metrics

Create a new marketing metric.

**Request Body:**
```json
{
  "metricType": "IMPRESSIONS",
  "channelType": "SEARCH",
  "campaignId": "campaign-789",
  "value": 15000,
  "granularity": "DAILY",
  "dataSource": "Google Ads",
  "qualityScore": 0.92
}
```

**Response:**
```json
{
  "id": "metric-456",
  "tenantId": "tenant-abc",
  "metricType": "IMPRESSIONS",
  "channelType": "SEARCH",
  "value": 15000,
  "timestamp": "2024-01-15T10:00:00Z",
  "createdAt": "2024-01-15T10:00:00Z"
}
```

### PUT /metrics/{id}

Update an existing marketing metric.

**Request Body:**
```json
{
  "value": 16000,
  "verified": true
}
```

**Response:**
```json
{
  "id": "metric-456",
  "value": 16000,
  "verified": true,
  "updatedAt": "2024-01-15T11:00:00Z"
}
```

### DELETE /metrics/{id}

Delete a marketing metric.

**Response:** `204 No Content`

---

## Campaign Analytics

### GET /campaigns/analytics

Retrieve campaign analytics with filtering.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| status | string | No | Filter by status (ACTIVE, PAUSED, COMPLETED, CANCELLED) |
| campaignType | string | No | Filter by campaign type |
| startDate | string | No | Start date filter |
| endDate | string | No | End date filter |

**Response:**
```json
{
  "content": [
    {
      "id": "ca-123",
      "tenantId": "tenant-abc",
      "campaignId": "campaign-456",
      "campaignName": "Summer Sale 2024",
      "campaignType": "SOCIAL",
      "status": "ACTIVE",
      "startDate": "2024-01-01T00:00:00Z",
      "endDate": "2024-01-31T23:59:59Z",
      "impressions": 500000,
      "reach": 350000,
      "clicks": 25000,
      "ctr": 5.0,
      "conversions": 1250,
      "conversionRate": 5.0,
      "spend": 5000.00,
      "cpc": 0.20,
      "cpm": 10.00,
      "cpa": 4.00,
      "revenue": 15000.00,
      "roi": 200.0,
      "roas": 3.0,
      "targetBudget": 6000.00,
      "budgetUtilization": 83.33,
      "achievementStatus": "ON_TRACK"
    }
  ]
}
```

### GET /campaigns/{campaignId}/analytics

Retrieve analytics for a specific campaign.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| campaignId | string | Yes | Campaign ID |

**Response:** Same as GET /campaigns/analytics (single object)

### POST /campaigns/{campaignId}/analytics/recalculate

Trigger recalculation of campaign metrics.

**Response:**
```json
{
  "id": "ca-123",
  "campaignId": "campaign-456",
  "lastAnalyticsUpdate": "2024-01-15T12:00:00Z",
  "status": "recalculated"
}
```

---

## Channel Analytics

### GET /channels/analytics

Retrieve channel performance analytics.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| channelType | string | No | Filter by channel type |
| platform | string | No | Filter by platform (Facebook, Google, etc.) |
| period | string | No | Filter by period (2024-01, Q1-2024, etc.) |

**Response:**
```json
{
  "content": [
    {
      "id": "cha-123",
      "tenantId": "tenant-abc",
      "channelType": "SOCIAL",
      "platform": "Facebook",
      "period": "2024-01",
      "periodStart": "2024-01-01T00:00:00Z",
      "periodEnd": "2024-01-31T23:59:59Z",
      "impressions": 1000000,
      "reach": 750000,
      "clicks": 50000,
      "ctr": 5.0,
      "conversions": 2500,
      "conversionRate": 5.0,
      "spend": 10000.00,
      "cpc": 0.20,
      "cpm": 10.00,
      "cpa": 4.00,
      "revenue": 30000.00,
      "roi": 200.0,
      "roas": 3.0,
      "engagementScore": 75.0,
      "channelScore": 78.5,
      "performanceRating": "GOOD",
      "trendDirection": "UP",
      "percentChange": 15.5
    }
  ]
}
```

### GET /channels/compare

Compare performance across channels.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| channelTypes | string | Yes | Comma-separated channel types |
| period | string | Yes | Comparison period |
| metrics | string | No | Metrics to compare (default: all) |

**Response:**
```json
{
  "period": "2024-01",
  "channels": [
    {
      "channelType": "SOCIAL",
      "impressions": 1000000,
      "clicks": 50000,
      "conversions": 2500,
      "spend": 10000.00,
      "revenue": 30000.00,
      "roi": 200.0,
      "rank": 1
    },
    {
      "channelType": "SEARCH",
      "impressions": 500000,
      "clicks": 40000,
      "conversions": 3000,
      "spend": 12000.00,
      "revenue": 36000.00,
      "roi": 200.0,
      "rank": 2
    }
  ]
}
```

---

## Reports

### GET /reports

Retrieve analytics reports.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| reportType | string | No | Filter by report type |
| status | string | No | Filter by status |
| isTemplate | boolean | No | Filter templates |
| owner | string | No | Filter by owner |

**Response:**
```json
{
  "content": [
    {
      "id": "report-123",
      "tenantId": "tenant-abc",
      "name": "Monthly Performance Report",
      "description": "Summary of monthly marketing performance",
      "reportType": "CAMPAIGN_PERFORMANCE",
      "status": "COMPLETED",
      "format": "PDF",
      "owner": "user-456",
      "isTemplate": false,
      "scheduleEnabled": true,
      "scheduleType": "MONTHLY",
      "nextRunAt": "2024-02-01T00:00:00Z",
      "fileUrl": "https://storage.example.com/reports/monthly-2024-01.pdf",
      "createdAt": "2024-01-01T00:00:00Z",
      "updatedAt": "2024-01-31T23:59:59Z"
    }
  ]
}
```

### GET /reports/{id}

Retrieve a specific report.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Report ID |

**Response:**
```json
{
  "id": "report-123",
  "name": "Campaign Performance Report",
  "reportType": "CAMPAIGN_PERFORMANCE",
  "status": "COMPLETED",
  "startDate": "2024-01-01T00:00:00Z",
  "endDate": "2024-01-31T23:59:59Z",
  "dateRangeType": "LAST_30_DAYS",
  "campaignIds": ["campaign-1", "campaign-2"],
  "channelTypes": ["SOCIAL", "SEARCH"],
  "metrics": ["impressions", "clicks", "conversions", "roi"],
  "dimensions": ["date", "campaign", "channel"],
  "reportData": {
    "totalImpressions": 5000000,
    "totalClicks": 250000,
    "totalConversions": 12500,
    "totalSpend": 50000.00,
    "totalRevenue": 150000.00,
    "averageRoi": 200.0
  },
  "sections": [
    {
      "id": "section-1",
      "title": "Executive Summary",
      "type": "METRIC",
      "order": 1,
      "data": {
        "keyMetrics": [...]
      }
    }
  ],
  "insights": [
    "Social media campaigns outperformed search by 25%",
    "ROI increased by 15% compared to previous period"
  ],
  "recommendations": [
    "Increase budget for top-performing social campaigns",
    "A/B test new ad creatives for underperforming campaigns"
  ]
}
```

### POST /reports

Create a new report.

**Request Body:**
```json
{
  "name": "Weekly Performance Report",
  "description": "Weekly campaign performance summary",
  "reportType": "CAMPAIGN_PERFORMANCE",
  "format": "PDF",
  "startDate": "2024-01-01T00:00:00Z",
  "endDate": "2024-01-07T23:59:59Z",
  "dateRangeType": "LAST_7_DAYS",
  "campaignIds": ["campaign-1", "campaign-2"],
  "channelTypes": ["SOCIAL", "EMAIL"],
  "metrics": ["impressions", "clicks", "conversions"],
  "dimensions": ["date", "campaign"],
  "scheduleEnabled": true,
  "scheduleType": "WEEKLY"
}
```

**Response:**
```json
{
  "id": "report-456",
  "name": "Weekly Performance Report",
  "reportType": "CAMPAIGN_PERFORMANCE",
  "status": "DRAFT",
  "createdAt": "2024-01-08T00:00:00Z"
}
```

### PUT /reports/{id}

Update an existing report.

**Request Body:**
```json
{
  "name": "Updated Weekly Report",
  "description": "Updated description",
  "status": "SCHEDULED"
}
```

**Response:** Updated report object

### DELETE /reports/{id}

Delete a report.

**Response:** `204 No Content`

### POST /reports/{id}/generate

Generate a report immediately.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Report ID |

**Response:**
```json
{
  "id": "report-123",
  "status": "RUNNING",
  "lastRunAt": "2024-01-15T10:00:00Z"
}
```

### GET /reports/{id}/download

Download a generated report file.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | string | Yes | Report ID |

**Response:** File download (PDF, XLSX, CSV, etc.)

---

## Attribution

### GET /attribution

Retrieve attribution data.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| campaignId | string | No | Filter by campaign |
| conversionId | string | No | Filter by conversion |
| model | string | No | Attribution model (FIRST_TOUCH, LAST_TOUCH, LINEAR, etc.) |
| lookbackDays | integer | No | Lookback period (default: 30) |

**Response:**
```json
{
  "content": [
    {
      "id": "attr-123",
      "tenantId": "tenant-abc",
      "conversionId": "conv-456",
      "campaignId": "campaign-789",
      "attributionModel": "FIRST_TOUCH",
      "touchpoints": [
        {
          "channelType": "SOCIAL",
          "platform": "Facebook",
          "touchpointDate": "2024-01-01T10:00:00Z",
          "attributedValue": 100.00,
          "contributionPercent": 100.0
        }
      ],
      "totalValue": 100.00,
      "lookbackDays": 30
    }
  ]
}
```

---

## Aggregation

### POST /aggregate

Perform custom aggregation on metrics.

**Request Body:**
```json
{
  "metrics": ["impressions", "clicks", "conversions"],
  "dimensions": ["date", "channelType"],
  "aggregation": "SUM",
  "filters": {
    "startDate": "2024-01-01T00:00:00Z",
    "endDate": "2024-01-31T23:59:59Z",
    "channelTypes": ["SOCIAL", "SEARCH"]
  },
  "orderBy": ["date", "asc"],
  "limit": 1000
}
```

**Response:**
```json
{
  "results": [
    {
      "date": "2024-01-01",
      "channelType": "SOCIAL",
      "impressions": 50000,
      "clicks": 2500,
      "conversions": 125
    },
    {
      "date": "2024-01-01",
      "channelType": "SEARCH",
      "impressions": 30000,
      "clicks": 2000,
      "conversions": 150
    }
  ],
  "totalResults": 62,
  "aggregationTime": "2024-01-15T10:00:00Z"
}
```

---

## Error Responses

All endpoints may return the following error responses:

### 400 Bad Request
```json
{
  "timestamp": "2024-01-15T10:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed for field 'metricType'",
  "path": "/api/v1/metrics"
}
```

### 401 Unauthorized
```json
{
  "timestamp": "2024-01-15T10:00:00Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid or expired JWT token",
  "path": "/api/v1/metrics"
}
```

### 403 Forbidden
```json
{
  "timestamp": "2024-01-15T10:00:00Z",
  "status": 403,
  "error": "Forbidden",
  "message": "Access denied for this resource",
  "path": "/api/v1/metrics"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-15T10:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Metric not found with id: metric-123",
  "path": "/api/v1/metrics/metric-123"
}
```

### 409 Conflict
```json
{
  "timestamp": "2024-01-15T10:00:00Z",
  "status": 409,
  "error": "Conflict",
  "message": "Resource already exists",
  "path": "/api/v1/metrics"
}
```

### 500 Internal Server Error
```json
{
  "timestamp": "2024-01-15T10:00:00Z",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred",
  "path": "/api/v1/metrics"
}
```

---

## Rate Limiting

The API implements rate limiting per tenant:

- **Default Limit**: 1000 requests per hour
- **Burst Limit**: 100 requests per minute

Rate limit headers are included in responses:

```
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 950
X-RateLimit-Reset: 1705305600
```

---

## Pagination

List endpoints support pagination using the following query parameters:

- `page`: Page number (0-based)
- `size`: Page size (1-100)
- `sort`: Sort field and direction (field,asc/desc)

Example:
```
GET /metrics?page=0&size=20&sort=timestamp,desc
```

---

## API Versioning

The API uses URL path versioning. The current version is `v1`:

```
/api/v1/metrics
```

Previous versions may be maintained for backward compatibility:

```
/api/v1/metrics
/api/v2/metrics (future)
```

---

## Interactive Documentation

Interactive API documentation is available via Swagger UI:

- **Development**: http://localhost:8086/api/v1/swagger-ui.html
- **Production**: https://analytics.gogidix.com/api/v1/swagger-ui.html

API documentation in JSON format:

- **Development**: http://localhost:8086/api/v1/api-docs
- **Production**: https://analytics.gogidix.com/api/v1/api-docs
