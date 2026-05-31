# Country Support Dashboard Service - API Documentation

## Base URL

```
/api/v1/country-support-dashboard
```

## Authentication

All API requests require:
- Header `X-Tenant-ID`: Your tenant identifier
- Header `X-Correlation-ID`: Unique correlation ID for request tracing

## Country Metrics Endpoints

### Get All Country Metrics

Retrieves all country-specific metrics for the current tenant.

```http
GET /api/v1/country-support-dashboard/country-metrics
```

**Response:** `200 OK`

```json
[
  {
    "id": "metrics-123",
    "tenantId": "tenant-123",
    "countryCode": "US",
    "countryName": "United States",
    "metricDate": "2024-01-15",
    "totalTickets": 100,
    "openTickets": 25,
    "resolvedTickets": 70,
    "escalatedTickets": 5,
    "averageResolutionTimeMinutes": 120.5,
    "averageResponseTimeMinutes": 15.5,
    "customerSatisfactionScore": 4.2,
    "activeAgents": 10,
    "ticketVolumeByChannel": {
      "Email": 50,
      "Phone": 30,
      "Chat": 20
    },
    "ticketVolumeByPriority": {
      "High": 10,
      "Medium": 40,
      "Low": 50
    },
    "slaComplianceRate": 95.5,
    "firstContactResolutionRate": 78.5,
    "region": "North America",
    "language": "en",
    "timezone": "America/New_York",
    "businessHours": {
      "startTime": "09:00",
      "endTime": "17:00",
      "timezone": "America/New_York",
      "workingDays": ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"]
    },
    "createdAt": "2024-01-15T10:00:00.000Z",
    "updatedAt": "2024-01-15T10:00:00.000Z"
  }
]
```

---

### Get Country Metrics by ID

Retrieves specific country metrics by ID.

```http
GET /api/v1/country-support-dashboard/country-metrics/{id}
```

**Path Parameters:**
- `id` (string): The metrics ID

**Response:** `200 OK`

```json
{
  "id": "metrics-123",
  "countryCode": "US",
  "countryName": "United States",
  "metricDate": "2024-01-15",
  "totalTickets": 100
}
```

**Error Response:** `404 Not Found`

```json
{
  "message": "Country specific metrics not found with id: {id}"
}
```

---

### Get Metrics by Country Code

Retrieves metrics filtered by country code.

```http
GET /api/v1/country-support-dashboard/country-metrics/country/{countryCode}
```

**Path Parameters:**
- `countryCode` (string): ISO country code (e.g., US, UK, DE)

**Response:** `200 OK`

---

### Get Latest Metrics by Country Code

Retrieves the most recent metrics for a country.

```http
GET /api/v1/country-support-dashboard/country-metrics/country/{countryCode}/latest
```

**Path Parameters:**
- `countryCode` (string): ISO country code

**Response:** `200 OK`

---

### Get Metrics by Country Code and Date Range

Retrieves metrics for a country within a date range.

```http
GET /api/v1/country-support-dashboard/country-metrics/country/{countryCode}/daterange?startDate={startDate}&endDate={endDate}
```

**Path Parameters:**
- `countryCode` (string): ISO country code

**Query Parameters:**
- `startDate` (date, required): Start date (ISO format: yyyy-MM-dd)
- `endDate` (date, required): End date (ISO format: yyyy-MM-dd)

**Response:** `200 OK`

---

### Get Metrics by Region

Retrieves metrics filtered by region.

```http
GET /api/v1/country-support-dashboard/country-metrics/region/{region}
```

**Path Parameters:**
- `region` (string): Region name (e.g., North America, Europe)

**Response:** `200 OK`

---

### Create Country Metrics

Creates new country-specific metrics.

```http
POST /api/v1/country-support-dashboard/country-metrics
```

**Request Body:**

```json
{
  "countryCode": "US",
  "countryName": "United States",
  "metricDate": "2024-01-15",
  "totalTickets": 100,
  "openTickets": 25,
  "resolvedTickets": 70,
  "escalatedTickets": 5,
  "averageResolutionTimeMinutes": 120.5,
  "averageResponseTimeMinutes": 15.5,
  "customerSatisfactionScore": 4.2,
  "activeAgents": 10,
  "ticketVolumeByChannel": {
    "Email": 50,
    "Phone": 30,
    "Chat": 20
  },
  "ticketVolumeByPriority": {
    "High": 10,
    "Medium": 40,
    "Low": 50
  },
  "ticketVolumeByCategory": {
    "Technical": 40,
    "Billing": 30,
    "General": 30
  },
  "slaComplianceRate": 95.5,
  "firstContactResolutionRate": 78.5,
  "peakHours": {
    "09:00": 15,
    "10:00": 20,
    "14:00": 18
  },
  "region": "North America",
  "language": "en",
  "timezone": "America/New_York",
  "businessHours": {
    "startTime": "09:00",
    "endTime": "17:00",
    "timezone": "America/New_York",
    "workingDays": ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"]
  }
}
```

**Validation Rules:**
- `countryCode`: Required, non-blank
- `countryName`: Required, non-blank
- `metricDate`: Required, valid date

**Response:** `201 Created`

---

### Update Country Metrics

Updates existing country-specific metrics.

```http
PUT /api/v1/country-support-dashboard/country-metrics/{id}
```

**Path Parameters:**
- `id` (string): The metrics ID

**Request Body:** Same as Create

**Response:** `200 OK`

**Error Response:** `404 Not Found`

---

### Delete Country Metrics

Deletes country-specific metrics.

```http
DELETE /api/v1/country-support-dashboard/country-metrics/{id}
```

**Path Parameters:**
- `id` (string): The metrics ID

**Response:** `204 No Content`

**Error Response:** `404 Not Found`

---

## Country Metadata Endpoints

### Get Distinct Country Codes

Retrieves list of all country codes with metrics.

```http
GET /api/v1/country-support-dashboard/country-metadata/countries
```

**Response:** `200 OK`

```json
["US", "UK", "DE", "FR", "JP"]
```

---

### Get Distinct Regions

Retrieves list of all regions with metrics.

```http
GET /api/v1/country-support-dashboard/country-metadata/regions
```

**Response:** `200 OK`

```json
["North America", "Europe", "Asia Pacific", "Latin America"]
```

---

## Regional Statistics Endpoints

### Get All Regional Stats

Retrieves all regional ticket statistics.

```http
GET /api/v1/country-support-dashboard/regional-stats
```

**Response:** `200 OK`

```json
[
  {
    "id": "stats-123",
    "tenantId": "tenant-123",
    "regionName": "North America",
    "countryCodes": ["US", "CA", "MX"],
    "statDate": "2024-01-15",
    "totalTickets": 200,
    "newTickets": 50,
    "closedTickets": 140,
    "pendingTickets": 10,
    "ticketsByCountry": {
      "US": {
        "countryCode": "US",
        "countryName": "United States",
        "totalTickets": 100,
        "openTickets": 25,
        "resolvedTickets": 70,
        "satisfactionScore": 4.2
      },
      "CA": {
        "countryCode": "CA",
        "countryName": "Canada",
        "totalTickets": 50,
        "openTickets": 10,
        "resolvedTickets": 38,
        "satisfactionScore": 4.4
      }
    },
    "ticketsByStatus": {
      "Open": 35,
      "Closed": 140,
      "Pending": 10
    },
    "ticketsByPriority": {
      "High": 15,
      "Medium": 120,
      "Low": 50
    },
    "ticketsByChannel": {
      "Email": 90,
      "Phone": 60,
      "Chat": 50
    },
    "averageResolutionTimeMinutes": 115.0,
    "averageResponseTimeMinutes": 12.5,
    "customerSatisfactionScore": 4.3,
    "slaComplianceRate": 96.5,
    "changePercentage": 5.2,
    "activeAgents": 20,
    "createdAt": "2024-01-15T10:00:00.000Z",
    "updatedAt": "2024-01-15T10:00:00.000Z"
  }
]
```

---

### Get Regional Stats by ID

Retrieves specific regional statistics by ID.

```http
GET /api/v1/country-support-dashboard/regional-stats/{id}
```

**Path Parameters:**
- `id` (string): The stats ID

**Response:** `200 OK`

---

### Get Stats by Region

Retrieves statistics filtered by region name.

```http
GET /api/v1/country-support-dashboard/regional-stats/region/{regionName}
```

**Path Parameters:**
- `regionName` (string): Region name (e.g., North America)

**Response:** `200 OK`

---

### Get Latest Stats by Region

Retrieves the most recent statistics for a region.

```http
GET /api/v1/country-support-dashboard/regional-stats/region/{regionName}/latest
```

**Path Parameters:**
- `regionName` (string): Region name

**Response:** `200 OK`

---

### Get Stats by Date Range

Retrieves regional statistics within a date range.

```http
GET /api/v1/country-support-dashboard/regional-stats/daterange?startDate={startDate}&endDate={endDate}
```

**Query Parameters:**
- `startDate` (date, required): Start date (ISO format: yyyy-MM-dd)
- `endDate` (date, required): End date (ISO format: yyyy-MM-dd)

**Response:** `200 OK`

---

## Regional Metadata Endpoints

### Get Distinct Region Names

Retrieves list of all region names with statistics.

```http
GET /api/v1/country-support-dashboard/regional-metadata/regions
```

**Response:** `200 OK`

```json
["North America", "Europe", "Asia Pacific"]
```

---

## Error Responses

### 400 Bad Request

Invalid request data.

```json
{
  "timestamp": "2024-01-15T10:00:00.000Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed for object='countrySpecificMetricsRequestDto'. Error count: 2",
  "path": "/api/v1/country-support-dashboard/country-metrics"
}
```

### 404 Not Found

Resource not found.

```json
{
  "timestamp": "2024-01-15T10:00:00.000Z",
  "status": 404,
  "error": "Not Found",
  "message": "Country specific metrics not found with id: non-existent",
  "path": "/api/v1/country-support-dashboard/country-metrics/non-existent"
}
```

### 500 Internal Server Error

Server error.

```json
{
  "timestamp": "2024-01-15T10:00:00.000Z",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred",
  "path": "/api/v1/country-support-dashboard/country-metrics"
}
```

---

## Pagination

Currently, endpoints return all results. Future versions will support pagination:

```http
GET /api/v1/country-support-dashboard/country-metrics?page=0&size=20&sort=metricDate,desc
```

---

## OpenAPI Documentation

Interactive API documentation is available at:

```
/swagger-ui.html
```

or

```
/swagger-ui/index.html
```

OpenAPI JSON specification:

```
/v3/api-docs
```

---

## Rate Limiting

API rate limits (if configured):
- 1000 requests per hour per tenant
- 100 requests per minute per tenant

Rate limit headers are included in responses:
```
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 950
X-RateLimit-Reset: 1705305600
```

---

## CORS

Cross-Origin Resource Sharing is configured for allowed origins. Include the `Origin` header in preflight requests.
