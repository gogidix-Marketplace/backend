# Global Business Dashboard Service - API Documentation

## Base URL
```
http://localhost:8080/api/v1/global-metrics
```

## Authentication
All endpoints require Bearer token authentication (if configured).

## Endpoints

### Global Business Metrics

#### Create Metrics
```http
POST /api/v1/global-metrics
Content-Type: application/json

{
  "periodId": "2024-Q1",
  "startDate": "2024-01-01T00:00:00",
  "endDate": "2024-03-31T23:59:59",
  "totalRevenue": 100000.00,
  "totalExpenses": 60000.00,
  "totalOrders": 500,
  "activeCustomers": 100,
  "newCustomers": 20,
  "churnedCustomers": 5,
  "baseCurrency": "USD",
  "status": "PUBLISHED"
}
```

**Response**: `201 Created`
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "periodId": "2024-Q1",
  "totalRevenue": 100000.00,
  "status": "PUBLISHED",
  "calculatedMetrics": {
    "customerGrowthRate": 20.00,
    "customerChurnRate": 5.00,
    "revenuePerCustomer": 1000.00,
    "netGrowthRate": 15.00
  }
}
```

#### Update Metrics
```http
PUT /api/v1/global-metrics/{id}
Content-Type: application/json
```

#### Get Metrics by ID
```http
GET /api/v1/global-metrics/{id}
```

#### Get Metrics by Period
```http
GET /api/v1/global-metrics/period/{periodId}
```

#### Get Latest Published Metrics
```http
GET /api/v1/global-metrics/latest
```

#### Get All Metrics (Paginated)
```http
GET /api/v1/global-metrics?page=0&size=20&sort=endDate&direction=desc
```

#### Get Metrics by Date Range
```http
GET /api/v1/global-metrics/date-range?startDate=2024-01-01T00:00:00&endDate=2024-03-31T23:59:59
```

#### Get Metrics by Status
```http
GET /api/v1/global-metrics/status/{status}
```

**Status values**: `DRAFT`, `PENDING_REVIEW`, `PUBLISHED`, `ARCHIVED`

#### Get Top Performing Regions
```http
GET /api/v1/global-metrics/period/{periodId}/top-regions?limit=5
```

#### Get Financial Summary
```http
GET /api/v1/global-metrics/period/{periodId}/financial-summary
```

**Response**:
```json
{
  "totalRevenue": 100000.00,
  "totalExpenses": 60000.00,
  "grossProfit": 40000.00,
  "netProfit": 40000.00,
  "profitMargin": 40.00,
  "averageOrderValue": 200.00,
  "revenuePerCustomer": 1000.00,
  "currency": "USD"
}
```

#### Publish Metrics
```http
POST /api/v1/global-metrics/{id}/publish
```

#### Delete Metrics
```http
DELETE /api/v1/global-metrics/{id}
```

#### Check if Metrics Exist by Period
```http
GET /api/v1/global-metrics/exists/period/{periodId}
```

#### Count Metrics by Status
```http
GET /api/v1/global-metrics/count/status/{status}
```

## Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2024-01-01T00:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed for field 'periodId'",
  "path": "/api/v1/global-metrics"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-01T00:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "GlobalBusinessMetrics not found with id: {id}",
  "path": "/api/v1/global-metrics/{id}"
}
```

## Data Types

### BigDecimal
All monetary values use `BigDecimal` with 2 decimal places for precision.

### LocalDateTime
All datetime fields use ISO 8601 format: `yyyy-MM-dd'T'HH:mm:ss`

### Enums
- **MetricsStatus**: `DRAFT`, `PENDING_REVIEW`, `PUBLISHED`, `ARCHIVED`
- **SummaryStatus**: `ACTIVE`, `INACTIVE`, `ARCHIVED`

## Pagination
- **page**: Page number (0-based, default: 0)
- **size**: Items per page (default: 20)
- **sort**: Sort field (default: endDate)
- **direction**: Sort direction - `asc` or `desc` (default: desc)
