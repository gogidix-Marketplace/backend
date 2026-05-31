# Auto Scaling Service - API Documentation

## Base URL
```
http://localhost:3001/api/v1
```

## Authentication
All API requests require authentication via Bearer token:
```
Authorization: Bearer <token>
```

---

## Scaling Policies

### Create Scaling Policy
```http
POST /scaling-policies
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "web-server-scaling",
  "cloudProvider": "aws",
  "resourceId": "asg-web-servers",
  "minInstances": 2,
  "maxInstances": 10,
  "currentInstances": 4,
  "cooldownPeriod": 300000,
  "scaleOutRules": [
    {
      "metric": "cpu",
      "operator": "greater_than",
      "threshold": 80,
      "adjustmentType": "change_in_capacity",
      "adjustment": 2
    }
  ],
  "scaleInRules": [
    {
      "metric": "cpu",
      "operator": "less_than",
      "threshold": 30,
      "adjustmentType": "change_in_capacity",
      "adjustment": -1
    }
  ]
}
```

**Response:** `201 Created`
```json
{
  "success": true,
  "data": {
    "_id": "policy-123",
    "name": "web-server-scaling",
    "enabled": false,
    "createdAt": "2026-02-23T10:00:00Z"
  }
}
```

---

### Get All Scaling Policies
```http
GET /scaling-policies?enabled=true&cloudProvider=aws&limit=50&skip=0
```

**Query Parameters:**
- `enabled` (optional) - Filter by enabled status
- `cloudProvider` (optional) - Filter by cloud provider (aws, azure)
- `resourceId` (optional) - Filter by resource ID
- `limit` (optional, default: 50) - Page size
- `skip` (optional, default: 0) - Page offset

**Response:** `200 OK`
```json
{
  "success": true,
  "data": [
    {
      "_id": "policy-123",
      "name": "web-server-scaling",
      "cloudProvider": "aws",
      "resourceId": "asg-web-servers",
      "enabled": true,
      "minInstances": 2,
      "maxInstances": 10,
      "currentInstances": 4
    }
  ],
  "meta": {
    "total": 1,
    "limit": 50,
    "skip": 0
  }
}
```

---

### Get Scaling Policy by ID
```http
GET /scaling-policies/:id
```

**Response:** `200 OK`
```json
{
  "success": true,
  "data": {
    "_id": "policy-123",
    "name": "web-server-scaling",
    "cloudProvider": "aws",
    "resourceId": "asg-web-servers",
    "enabled": true,
    "scaleOutRules": [...],
    "scaleInRules": [...]
  }
}
```

---

### Update Scaling Policy
```http
PUT /scaling-policies/:id
Content-Type: application/json
```

**Request Body:** (Partial update supported)
```json
{
  "minInstances": 3,
  "maxInstances": 15
}
```

**Response:** `200 OK`

---

### Delete Scaling Policy
```http
DELETE /scaling-policies/:id
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Policy deleted successfully"
}
```

---

### Enable Scaling Policy
```http
POST /scaling-policies/:id/enable
```

**Response:** `200 OK`
```json
{
  "success": true,
  "data": {
    "_id": "policy-123",
    "enabled": true
  }
}
```

---

### Disable Scaling Policy
```http
POST /scaling-policies/:id/disable
```

**Response:** `200 OK`

---

## Scaling Events

### Get Policy Events
```http
GET /scaling-policies/:id/events?eventType=scale_out&status=completed&limit=50&skip=0
```

**Query Parameters:**
- `eventType` (optional) - Filter by event type (scale_out, scale_in, evaluation_passed, evaluation_failed)
- `status` (optional) - Filter by status (in_progress, completed, failed)
- `limit` (optional, default: 50) - Page size
- `skip` (optional, default: 0) - Page offset

**Response:** `200 OK`
```json
{
  "success": true,
  "data": [
    {
      "_id": "event-123",
      "policyId": "policy-123",
      "eventType": "scale_out",
      "previousCapacity": 4,
      "newCapacity": 6,
      "triggeredBy": "cpu greater_than 80%",
      "status": "completed",
      "startedAt": "2026-02-23T10:30:00Z",
      "completedAt": "2026-02-23T10:32:00Z"
    }
  ],
  "meta": {
    "total": 1
  }
}
```

---

## Metrics

### Get Current Metrics
```http
GET /metrics/:resourceId
```

**Response:** `200 OK`
```json
{
  "success": true,
  "data": {
    "resourceId": "asg-web-servers",
    "currentMetrics": {
      "cpuUtilization": 75.5,
      "memoryUtilization": 60.2,
      "diskUtilization": 45.0,
      "networkInBytes": 5000000,
      "networkOutBytes": 3000000,
      "requestCount": 1500,
      "timestamp": "2026-02-23T10:30:00Z"
    }
  }
}
```

---

### Get Metrics History
```http
GET /metrics/:resourceId/history?hours=24
```

**Query Parameters:**
- `hours` (optional, default: 24) - Hours of history to return

**Response:** `200 OK`
```json
{
  "success": true,
  "data": [
    {
      "resourceId": "asg-web-servers",
      "cloudProvider": "aws",
      "metrics": [...],
      "collectedAt": "2026-02-23T10:00:00Z"
    }
  ]
}
```

---

### Get Policy Metrics
```http
GET /scaling-policies/:id/metrics?hours=24
```

**Response:** `200 OK`
```json
{
  "success": true,
  "data": {
    "policyId": "policy-123",
    "resourceId": "asg-web-servers",
    "currentMetrics": {...},
    "historicalMetrics": [...]
  }
}
```

---

## Manual Scaling

### Trigger Manual Scaling
```http
POST /scaling-policies/:id/manual-scaling
Content-Type: application/json
```

**Request Body:**
```json
{
  "action": "scale_up",
  "instances": 2,
  "reason": "Preparing for high traffic event"
}
```

**Action Types:**
- `scale_up` - Increase capacity
- `scale_down` - Decrease capacity
- `exact` - Set exact capacity (requires `instances` parameter)

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Scaling action initiated: 4 -> 6"
}
```

---

## Health Check

```http
GET /health
```

**Response:** `200 OK`
```json
{
  "status": "healthy",
  "uptime": 3600,
  "timestamp": "2026-02-23T10:30:00Z",
  "services": {
    "mongodb": "connected",
    "redis": "connected",
    "aws": "configured",
    "azure": "configured"
  }
}
```

---

## Error Responses

All endpoints may return the following error responses:

### 400 Bad Request
```json
{
  "success": false,
  "error": "Validation error",
  "details": ["name is required", "minInstances must be greater than 0"]
}
```

### 404 Not Found
```json
{
  "success": false,
  "error": "Policy not found"
}
```

### 500 Internal Server Error
```json
{
  "success": false,
  "error": "Internal server error",
  "requestId": "req-123"
}
```

---

## Rate Limiting

API requests are rate limited:
- **100 requests per minute** per IP address
- Rate limit headers are included in responses:
  ```
  X-RateLimit-Limit: 100
  X-RateLimit-Remaining: 95
  X-RateLimit-Reset: 1614523456
  ```

---

## WebSocket Events

The service also supports WebSocket connections for real-time scaling events:

### Connect
```
ws://localhost:3001/ws
```

### Event Types
- `scaling:started` - Scaling operation started
- `scaling:completed` - Scaling operation completed
- `scaling:failed` - Scaling operation failed
- `policy:updated` - Policy configuration updated
- `metrics:updated` - New metrics collected
