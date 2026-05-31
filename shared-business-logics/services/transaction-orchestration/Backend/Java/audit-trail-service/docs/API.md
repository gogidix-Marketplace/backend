# Audit Trail Service - API Documentation

## Base URL

```
http://localhost:8081/api/v1/audit-logs
```

## Authentication

All API endpoints require authentication via JWT tokens passed in the Authorization header:

```
Authorization: Bearer <token>
```

## API Endpoints

### 1. Create Audit Log

Creates a new audit log entry.

#### Endpoint

```
POST /api/v1/audit-logs
```

#### Request Headers

```
Content-Type: application/json
```

#### Request Body

```json
{
  "tenantId": "tenant-001",
  "entityType": "Transaction",
  "entityId": "txn-12345",
  "action": "CREATE",
  "actorId": "user-001",
  "actorType": "USER",
  "ipAddress": "192.168.1.100",
  "userAgent": "Mozilla/5.0...",
  "correlationId": "corr-abc123",
  "oldState": "{\"status\":\"PENDING\"}",
  "newState": "{\"status\":\"ACTIVE\"}",
  "changedFields": "[\"status\"]",
  "metadata": "{\"source\":\"web\"}",
  "businessContext": "{\"region\":\"US\"}",
  "severity": "INFO",
  "category": "BUSINESS",
  "description": "Transaction created successfully",
  "status": "SUCCESS",
  "errorMessage": null,
  "sessionId": "sess-xyz789",
  "requestId": "req-123456"
}
```

#### Field Descriptions

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| tenantId | String | Yes | Tenant identifier for multi-tenancy |
| entityType | String | Yes | Type of entity being audited (e.g., Transaction, Payment) |
| entityId | String | Yes | Unique identifier of the entity |
| action | String | Yes | Action performed (CREATE, UPDATE, DELETE, EXECUTE) |
| actorId | String | No | ID of the user/service performing the action |
| actorType | String | No | Type of actor (USER, SYSTEM, SERVICE) |
| ipAddress | String | No | IP address of the actor |
| userAgent | String | No | User agent string of the client |
| correlationId | String | No | Correlation ID for distributed tracing |
| oldState | String | No | Previous state as JSON |
| newState | String | No | New state as JSON |
| changedFields | String | No | Array of changed field names as JSON |
| metadata | String | No | Additional metadata as JSON |
| businessContext | String | No | Business context information |
| severity | String | No | Severity level (INFO, WARNING, ERROR, CRITICAL) |
| category | String | No | Event category (e.g., AUTHENTICATION, BUSINESS, SYSTEM) |
| description | String | No | Human-readable description |
| status | String | No | Action status (SUCCESS, FAILURE, PENDING) |
| errorMessage | String | No | Error message if status is FAILURE |
| sessionId | String | No | Session identifier |
| requestId | String | No | Request identifier |

#### Response

**Status Code**: 201 Created

```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "tenantId": "tenant-001",
  "entityType": "Transaction",
  "entityId": "txn-12345",
  "action": "CREATE",
  "actorId": "user-001",
  "actorType": "USER",
  "ipAddress": "192.168.1.100",
  "userAgent": "Mozilla/5.0...",
  "correlationId": "corr-abc123",
  "timestamp": "2024-01-15T10:30:00",
  "oldState": "{\"status\":\"PENDING\"}",
  "newState": "{\"status\":\"ACTIVE\"}",
  "changedFields": "[\"status\"]",
  "metadata": "{\"source\":\"web\"}",
  "businessContext": "{\"region\":\"US\"}",
  "severity": "INFO",
  "category": "BUSINESS",
  "description": "Transaction created successfully",
  "status": "SUCCESS",
  "errorMessage": null,
  "sessionId": "sess-xyz789",
  "requestId": "req-123456",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

#### Error Responses

**400 Bad Request** - Invalid input data

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Tenant ID is required",
  "path": "/api/v1/audit-logs"
}
```

---

### 2. Get Audit Log by ID

Retrieves a specific audit log entry by its ID.

#### Endpoint

```
GET /api/v1/audit-logs/{auditLogId}
```

#### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| auditLogId | UUID | Yes | Unique identifier of the audit log |

#### Response

**Status Code**: 200 OK

```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "tenantId": "tenant-001",
  "entityType": "Transaction",
  "entityId": "txn-12345",
  "action": "CREATE",
  "actorId": "user-001",
  "actorType": "USER",
  "timestamp": "2024-01-15T10:30:00",
  "severity": "INFO",
  "status": "SUCCESS",
  "description": "Transaction created successfully",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

#### Error Responses

**404 Not Found** - Audit log not found

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Audit log not found: 123e4567-e89b-12d3-a456-426614174000",
  "path": "/api/v1/audit-logs/123e4567-e89b-12d3-a456-426614174000"
}
```

---

### 3. Search Audit Logs

Searches for audit logs with various filters and pagination.

#### Endpoint

```
GET /api/v1/audit-logs
```

#### Query Parameters

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| tenantId | String | No | - | Filter by tenant ID |
| entityType | String | No | - | Filter by entity type |
| entityId | String | No | - | Filter by entity ID |
| action | String | No | - | Filter by action |
| actorId | String | No | - | Filter by actor ID |
| severity | String | No | - | Filter by severity |
| category | String | No | - | Filter by category |
| status | String | No | - | Filter by status |
| correlationId | String | No | - | Filter by correlation ID |
| startDate | DateTime | No | - | Filter by start date (ISO 8601) |
| endDate | DateTime | No | - | Filter by end date (ISO 8601) |
| page | Integer | No | 0 | Page number (0-indexed) |
| size | Integer | No | 20 | Page size |
| sortBy | String | No | timestamp | Sort field |
| sortDirection | String | No | DESC | Sort direction (ASC/DESC) |

#### Example Requests

```
GET /api/v1/audit-logs?tenantId=tenant-001&page=0&size=20
GET /api/v1/audit-logs?entityType=Transaction&action=CREATE
GET /api/v1/audit-logs?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59
GET /api/v1/audit-logs?severity=ERROR&status=FAILURE
```

#### Response

**Status Code**: 200 OK

```json
{
  "auditLogs": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "tenantId": "tenant-001",
      "entityType": "Transaction",
      "entityId": "txn-12345",
      "action": "CREATE",
      "actorId": "user-001",
      "actorType": "USER",
      "timestamp": "2024-01-15T10:30:00",
      "severity": "INFO",
      "status": "SUCCESS",
      "description": "Transaction created successfully",
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 150,
  "totalPages": 8,
  "hasNext": true,
  "hasPrevious": false
}
```

---

### 4. Get Audit Logs by Entity

Retrieves all audit logs for a specific entity.

#### Endpoint

```
GET /api/v1/audit-logs/by-entity/{entityType}/{entityId}
```

#### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| entityType | String | Yes | Type of entity |
| entityId | String | Yes | ID of the entity |

#### Example Request

```
GET /api/v1/audit-logs/by-entity/Transaction/txn-12345
```

#### Response

**Status Code**: 200 OK

```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "tenantId": "tenant-001",
    "entityType": "Transaction",
    "entityId": "txn-12345",
    "action": "CREATE",
    "timestamp": "2024-01-15T10:30:00"
  },
  {
    "id": "223e4567-e89b-12d3-a456-426614174001",
    "tenantId": "tenant-001",
    "entityType": "Transaction",
    "entityId": "txn-12345",
    "action": "UPDATE",
    "timestamp": "2024-01-15T10:35:00"
  }
]
```

---

### 5. Get Audit Logs by Tenant and Entity

Retrieves audit logs for a specific tenant's entity.

#### Endpoint

```
GET /api/v1/audit-logs/by-tenant/{tenantId}/{entityType}/{entityId}
```

#### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| tenantId | String | Yes | Tenant identifier |
| entityType | String | Yes | Type of entity |
| entityId | String | Yes | ID of the entity |

#### Example Request

```
GET /api/v1/audit-logs/by-tenant/tenant-001/Transaction/txn-12345
```

#### Response

**Status Code**: 200 OK

```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "tenantId": "tenant-001",
    "entityType": "Transaction",
    "entityId": "txn-12345",
    "action": "CREATE",
    "timestamp": "2024-01-15T10:30:00"
  }
]
```

---

### 6. Get Audit Logs by Correlation ID

Retrieves all audit logs for a specific correlation ID (distributed transaction tracking).

#### Endpoint

```
GET /api/v1/audit-logs/by-correlation/{correlationId}
```

#### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| correlationId | String | Yes | Correlation identifier |

#### Example Request

```
GET /api/v1/audit-logs/by-correlation/corr-abc123
```

#### Response

**Status Code**: 200 OK

```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "tenantId": "tenant-001",
    "entityType": "Transaction",
    "entityId": "txn-12345",
    "action": "CREATE",
    "correlationId": "corr-abc123",
    "timestamp": "2024-01-15T10:30:00"
  },
  {
    "id": "223e4567-e89b-12d3-a456-426614174001",
    "tenantId": "tenant-001",
    "entityType": "Payment",
    "entityId": "pay-001",
    "action": "PROCESS",
    "correlationId": "corr-abc123",
    "timestamp": "2024-01-15T10:31:00"
  }
]
```

---

### 7. Get Audit Logs by Actor

Retrieves all audit logs performed by a specific actor.

#### Endpoint

```
GET /api/v1/audit-logs/by-actor/{actorId}
```

#### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| actorId | String | Yes | Actor identifier |

#### Example Request

```
GET /api/v1/audit-logs/by-actor/user-001
```

#### Response

**Status Code**: 200 OK

```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "tenantId": "tenant-001",
    "entityType": "Transaction",
    "entityId": "txn-12345",
    "actorId": "user-001",
    "action": "CREATE",
    "timestamp": "2024-01-15T10:30:00"
  }
]
```

---

### 8. Delete Audit Log

Deletes a specific audit log entry. Use with caution.

#### Endpoint

```
DELETE /api/v1/audit-logs/{auditLogId}
```

#### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| auditLogId | UUID | Yes | Unique identifier of the audit log |

#### Response

**Status Code**: 204 No Content

#### Error Responses

**404 Not Found** - Audit log not found

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Audit log not found: 123e4567-e89b-12d3-a456-426614174000",
  "path": "/api/v1/audit-logs/123e4567-e89b-12d3-a456-426614174000"
}
```

---

## Enums and Constants

### Action Types

| Value | Description |
|-------|-------------|
| CREATE | Entity creation |
| UPDATE | Entity modification |
| DELETE | Entity deletion |
| EXECUTE | Action execution |
| READ | Data access |

### Actor Types

| Value | Description |
|-------|-------------|
| USER | Human user |
| SYSTEM | System process |
| SERVICE | Microservice |

### Severity Levels

| Value | Description |
|-------|-------------|
| INFO | Informational |
| WARNING | Warning condition |
| ERROR | Error occurred |
| CRITICAL | Critical failure |

### Status Values

| Value | Description |
|-------|-------------|
| SUCCESS | Operation completed successfully |
| FAILURE | Operation failed |
| PENDING | Operation in progress |

### Categories

| Value | Description |
|-------|-------------|
| AUTHENTICATION | Auth-related events |
| AUTHORIZATION | Permission checks |
| BUSINESS | Business operations |
| SYSTEM | System events |
| DATA | Data operations |

---

## OpenAPI Documentation

Interactive API documentation is available via Swagger UI:

```
http://localhost:8081/swagger-ui.html
```

OpenAPI JSON specification:

```
http://localhost:8081/v3/api-docs
```

---

## Rate Limiting

API calls are rate-limited per tenant:

- **Default**: 1000 requests per minute
- **Burst**: 100 requests per second

Rate limit headers are included in responses:

```
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 999
X-RateLimit-Reset: 1705300200
```

---

## Pagination

All list endpoints support pagination using page-based pagination:

| Parameter | Description | Default | Max |
|-----------|-------------|---------|-----|
| page | Page number (0-indexed) | 0 | - |
| size | Items per page | 20 | 100 |

---

## Error Codes

| Code | Description |
|------|-------------|
| 200 | Success |
| 201 | Created |
| 204 | No Content |
| 400 | Bad Request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |
| 409 | Conflict |
| 429 | Too Many Requests |
| 500 | Internal Server Error |
| 503 | Service Unavailable |
