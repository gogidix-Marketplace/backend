# Universal Tracking Service - API Documentation

## Base URL

```
Production: https://api.gogidix.com/tracking/v1
Development: http://localhost:8080/api/v1/tracking
```

## Authentication

All requests require:
- **Header:** `X-Tenant-ID: {your-tenant-id}`
- **Header:** `Authorization: Bearer {api-key}` (optional, for future use)

## Common Headers

| Header | Required | Description |
|--------|----------|-------------|
| X-Tenant-ID | Yes | Tenant identifier for multi-tenancy |
| Content-Type | Yes | application/json |
| Accept | Optional | application/json |
| X-Correlation-ID | Optional | Request correlation ID for tracing |

## Common Response Codes

| Code | Description |
|------|-------------|
| 200 | Success |
| 201 | Created |
| 204 | No Content |
| 400 | Bad Request - Validation error |
| 401 | Unauthorized |
| 403 | Forbidden - Tenant access denied |
| 404 | Not Found |
| 409 | Conflict - Resource already exists |
| 422 | Unprocessable Entity |
| 500 | Internal Server Error |

## API Endpoints

### Sessions

#### Create Session

Creates a new tracking session for grouping related events.

**Endpoint:** `POST /sessions`

**Request Body:**
```json
{
  "sessionId": "session-abc-123",
  "userId": "user-xyz-789",
  "source": "WEB",
  "ipAddress": "192.168.1.1",
  "userAgent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36",
  "deviceType": "DESKTOP",
  "browser": "Chrome",
  "os": "Windows",
  "country": "US",
  "city": "New York",
  "referrer": "https://google.com",
  "landingPage": "/home",
  "campaign": "spring_sale_2024"
}
```

**Response (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "sessionId": "session-abc-123",
  "userId": "user-xyz-789",
  "tenantId": "tenant-001",
  "source": "WEB",
  "ipAddress": "192.168.1.1",
  "deviceType": "DESKTOP",
  "browser": "Chrome",
  "os": "Windows",
  "country": "US",
  "city": "New York",
  "referrer": "https://google.com",
  "landingPage": "/home",
  "campaign": "spring_sale_2024",
  "startedAt": "2024-01-15T10:30:00Z",
  "lastActivityAt": "2024-01-15T10:30:00Z",
  "endedAt": null,
  "durationSeconds": null,
  "eventCount": 0,
  "pageViewCount": 0,
  "isActive": true,
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T10:30:00Z"
}
```

**Field Descriptions:**

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| sessionId | string | Yes | Unique session identifier |
| userId | string | No | User identifier |
| source | string | No | Source channel (WEB, MOBILE, API). Default: WEB |
| ipAddress | string | No | Client IP address |
| userAgent | string | No | Client user agent string |
| deviceType | string | No | Device type (DESKTOP, MOBILE, TABLET) |
| browser | string | No | Browser name |
| os | string | No | Operating system |
| country | string | No | ISO country code |
| city | string | No | City name |
| referrer | string | No | Referrer URL |
| landingPage | string | No | Initial landing page |
| campaign | string | No | Marketing campaign identifier |

#### Get Session

Retrieves a tracking session by ID.

**Endpoint:** `GET /sessions/{sessionId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| sessionId | string | Session identifier |

**Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "sessionId": "session-abc-123",
  "userId": "user-xyz-789",
  "tenantId": "tenant-001",
  "source": "WEB",
  "startedAt": "2024-01-15T10:30:00Z",
  "lastActivityAt": "2024-01-15T10:35:00Z",
  "endedAt": null,
  "durationSeconds": null,
  "eventCount": 5,
  "pageViewCount": 3,
  "isActive": true
}
```

#### Update Session

Updates a tracking session or ends it.

**Endpoint:** `PUT /sessions/{sessionId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| sessionId | string | Session identifier |

**Request Body:**
```json
{
  "referrer": "https://new-referrer.com",
  "landingPage": "/new-page",
  "campaign": "updated_campaign",
  "endSession": false
}
```

**Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "sessionId": "session-abc-123",
  "referrer": "https://new-referrer.com",
  "landingPage": "/new-page",
  "campaign": "updated_campaign",
  "isActive": true
}
```

#### Get Session Events

Retrieves all events for a specific session.

**Endpoint:** `GET /sessions/{sessionId}/events`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| sessionId | string | Session identifier |

**Response (200 OK):**
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "eventType": "PAGE_VIEW",
    "sessionId": "session-abc-123",
    "timestamp": "2024-01-15T10:31:00Z",
    "pageUrl": "/home",
    "pageTitle": "Home Page"
  },
  {
    "id": "550e8400-e29b-41d4-a716-446655440002",
    "eventType": "CLICK",
    "sessionId": "session-abc-123",
    "timestamp": "2024-01-15T10:32:00Z"
  }
]
```

### Events

#### Create Event

Creates a new tracking event.

**Endpoint:** `POST /events`

**Request Body:**
```json
{
  "eventType": "PAGE_VIEW",
  "sessionId": "session-abc-123",
  "userId": "user-xyz-789",
  "source": "WEB",
  "timestamp": "2024-01-15T10:31:00Z",
  "eventName": "Homepage Visit",
  "description": "User visited the homepage",
  "properties": {
    "category": "navigation",
    "value": 100
  },
  "metadata": {
    "experiment": "exp_a",
    "variant": "control"
  },
  "ipAddress": "192.168.1.1",
  "userAgent": "Mozilla/5.0...",
  "referrer": "https://google.com",
  "pageUrl": "/home",
  "pageTitle": "Home Page",
  "correlationId": "corr-xyz-123",
  "priority": 5
}
```

**Response (201 Created):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440001",
  "eventType": "PAGE_VIEW",
  "sessionId": "session-abc-123",
  "userId": "user-xyz-789",
  "tenantId": "tenant-001",
  "source": "WEB",
  "timestamp": "2024-01-15T10:31:00Z",
  "eventName": "Homepage Visit",
  "description": "User visited the homepage",
  "ipAddress": "192.168.1.1",
  "referrer": "https://google.com",
  "pageUrl": "/home",
  "pageTitle": "Home Page",
  "correlationId": "corr-xyz-123",
  "priority": 5,
  "processed": false,
  "createdAt": "2024-01-15T10:31:00Z"
}
```

**Field Descriptions:**

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| eventType | string | Yes | Event type/category (PAGE_VIEW, CLICK, PURCHASE, etc.) |
| sessionId | string | No | Associated session ID |
| userId | string | No | User identifier |
| source | string | No | Source channel. Default: WEB |
| timestamp | string | Yes | ISO 8601 timestamp |
| eventName | string | No | Human-readable event name |
| description | string | No | Event description |
| properties | object | No | Event-specific properties (JSON) |
| metadata | object | No | Additional metadata (JSON) |
| ipAddress | string | No | Client IP address |
| userAgent | string | No | Client user agent |
| referrer | string | No | Referrer URL |
| pageUrl | string | No | Current page URL |
| pageTitle | string | No | Current page title |
| correlationId | string | No | Correlation ID for distributed tracing |
| priority | integer | No | Event priority (1-10). Default: 5 |

#### Get Event

Retrieves a specific event by ID.

**Endpoint:** `GET /events/{eventId}`

**Path Parameters:**
| Parameter | Type | Description |
|-----------|------|-------------|
| eventId | string | Event UUID |

**Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440001",
  "eventType": "PAGE_VIEW",
  "sessionId": "session-abc-123",
  "userId": "user-xyz-789",
  "tenantId": "tenant-001",
  "timestamp": "2024-01-15T10:31:00Z",
  "eventName": "Homepage Visit",
  "processed": false
}
```

#### Search Events

Searches for events with filters and pagination.

**Endpoint:** `GET /events`

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| eventType | string | No | Filter by event type |
| sessionId | string | No | Filter by session ID |
| userId | string | No | Filter by user ID |
| source | string | No | Filter by source |
| startDate | string | No | Filter by start date (ISO 8601) |
| endDate | string | No | Filter by end date (ISO 8601) |
| processed | boolean | No | Filter by processed status |
| page | integer | No | Page number (0-indexed). Default: 0 |
| size | integer | No | Page size. Default: 20 |
| sortBy | string | No | Sort field. Default: timestamp |
| sortDirection | string | No | Sort direction (ASC/DESC). Default: DESC |

**Example Request:**
```
GET /events?eventType=PAGE_VIEW&startDate=2024-01-01T00:00:00Z&endDate=2024-01-31T23:59:59Z&page=0&size=10
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440001",
      "eventType": "PAGE_VIEW",
      "sessionId": "session-abc-123",
      "timestamp": "2024-01-15T10:31:00Z"
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 150,
  "totalPages": 15,
  "first": true,
  "last": false
}
```

### Statistics

#### Get Statistics

Retrieves aggregate statistics for the current tenant.

**Endpoint:** `GET /statistics`

**Response (200 OK):**
```json
{
  "totalEvents": 125000,
  "activeSessions": 45,
  "todayEvents": 3200,
  "timestamp": "2024-01-15T10:30:00Z"
}
```

## Common Event Types

| Event Type | Description | Typical Properties |
|------------|-------------|-------------------|
| PAGE_VIEW | User viewed a page | url, title, referrer |
| CLICK | User clicked an element | elementId, elementClass, targetUrl |
| FORM_SUBMIT | User submitted a form | formId, formName |
| PURCHASE | User completed a purchase | orderId, amount, currency |
| SIGN_UP | User registered | userId, method |
| LOGIN | User logged in | userId, method |
| LOGOUT | User logged out | userId |
| SEARCH | User performed a search | query, resultsCount |
| DOWNLOAD | User downloaded a file | fileId, fileName |
| SHARE | User shared content | platform, contentType |
| ERROR | An error occurred | errorCode, errorMessage |

## Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed: sessionId is required",
  "path": "/api/v1/tracking/sessions"
}
```

### 409 Conflict
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 409,
  "error": "Conflict",
  "message": "Session with ID already exists: session-abc-123",
  "path": "/api/v1/tracking/sessions"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Session not found: session-xyz-999",
  "path": "/api/v1/tracking/sessions/session-xyz-999"
}
```

## Rate Limits

| Tier | Requests per Minute | Requests per Day |
|------|--------------------|------------------|
| Free | 100 | 10,000 |
| Basic | 1,000 | 100,000 |
| Pro | 10,000 | 1,000,000 |
| Enterprise | Unlimited | Unlimited |

Rate limit headers are included in responses:
```
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 950
X-RateLimit-Reset: 1705305600
```

## Webhooks

### Webhook Events

The service sends webhook notifications for the following events:

1. **session.created** - New session created
2. **session.ended** - Session ended
3. **event.batch_processed** - Batch of events processed
4. **threshold.reached** - Metric threshold reached

### Webhook Payload Example
```json
{
  "eventId": "wh-evt-123",
  "eventType": "session.created",
  "tenantId": "tenant-001",
  "timestamp": "2024-01-15T10:30:00Z",
  "data": {
    "sessionId": "session-abc-123",
    "userId": "user-xyz-789"
  }
}
```

## SDKs

Official SDKs are available for:
- JavaScript/TypeScript
- Python
- Java
- Go
- PHP

See the [SDK Documentation](https://docs.gogidix.com/sdk) for more details.
