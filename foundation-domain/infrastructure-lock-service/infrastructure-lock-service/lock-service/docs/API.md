# Infrastructure Lock Service - API Documentation

## Overview

The Infrastructure Lock Service provides RESTful APIs for distributed resource locking with multi-tenant support. All APIs follow REST conventions and return JSON responses.

**Base URL**: `http://localhost:8080/api/v1/locks`

**API Version**: v1

**Authentication**: Uses `X-Tenant-ID` header for tenant identification

## Common Headers

| Header | Required | Description | Example |
|--------|----------|-------------|---------|
| X-Tenant-ID | Yes | Tenant identifier for multi-tenancy | `tenant-123` |
| Content-Type | Yes (for POST/PUT) | Media type | `application/json` |

## Common Response Codes

| Code | Description |
|------|-------------|
| 200 | Success |
| 400 | Bad Request - Invalid input |
| 403 | Forbidden - Lock held by different holder |
| 404 | Not Found - Lock not found |
| 409 | Conflict - Lock already held |
| 500 | Internal Server Error |

## API Endpoints

### 1. Acquire Lock

Acquire a distributed lock on a resource.

**Endpoint**: `POST /api/v1/locks/acquire`

**Request Body**:
```json
{
  "tenantId": "tenant-123",
  "resourceKey": "order:456",
  "holderId": "service-instance-1",
  "holderName": "OrderProcessingService",
  "lockType": "EXCLUSIVE",
  "ttlSeconds": 300,
  "waitTimeSeconds": 30,
  "maxRetries": 3,
  "retryIntervalMs": 100,
  "metadata": "{\"transactionId\": \"tx-789\"}",
  "waitForLock": false
}
```

**Response** (Success - 200):
```json
{
  "acquired": true,
  "lock": {
    "lockId": "550e8400-e29b-41d4-a716-446655440000",
    "tenantId": "tenant-123",
    "resourceKey": "order:456",
    "lockType": "EXCLUSIVE",
    "status": "LOCKED",
    "holderId": "service-instance-1",
    "holderName": "OrderProcessingService",
    "acquiredAt": "2024-01-15T10:30:00",
    "expiresAt": "2024-01-15T10:35:00",
    "remainingTtlSeconds": 300,
    "retryCount": 0,
    "metadata": "{\"transactionId\": \"tx-789\"}"
  },
  "errorMessage": null,
  "attemptedAt": "2024-01-15T10:30:00",
  "acquiredAt": "2024-01-15T10:30:00",
  "retryCount": 0,
  "timedOut": false
}
```

**Response** (Failure - 409):
```json
{
  "acquired": false,
  "lock": null,
  "errorMessage": "Resource already locked",
  "attemptedAt": "2024-01-15T10:30:00",
  "acquiredAt": null,
  "retryCount": 3,
  "timedOut": true
}
```

### 2. Release Lock

Release a previously acquired lock.

**Endpoint**: `DELETE /api/v1/locks/{resourceKey}/release`

**Query Parameters**:
| Parameter | Required | Type | Description |
|-----------|----------|------|-------------|
| holderId | Yes | String | ID of the lock holder |
| lockId | Yes | UUID | Unique lock identifier |

**Example**:
```
DELETE /api/v1/locks/order:456/release?holderId=service-instance-1&lockId=550e8400-e29b-41d4-a716-446655440000
```

**Response** (Success - 200):
```json
{
  "success": true,
  "releasedLock": {
    "lockId": "550e8400-e29b-41d4-a716-446655440000",
    "tenantId": "tenant-123",
    "resourceKey": "order:456",
    "lockType": "EXCLUSIVE",
    "status": "RELEASED",
    "holderId": "service-instance-1"
  },
  "errorMessage": null,
  "releasedAt": "2024-01-15T10:35:00",
  "wasExpired": false
}
```

### 3. Extend Lock TTL

Extend the time-to-live of an existing lock.

**Endpoint**: `PUT /api/v1/locks/{resourceKey}/extend`

**Query Parameters**:
| Parameter | Required | Type | Description |
|-----------|----------|------|-------------|
| holderId | Yes | String | ID of the lock holder |
| lockId | Yes | UUID | Unique lock identifier |
| additionalTtlSeconds | Yes | Long | Additional TTL in seconds |

**Example**:
```
PUT /api/v1/locks/order:456/extend?holderId=service-instance-1&lockId=550e8400-e29b-41d4-a716-446655440000&additionalTtlSeconds=300
```

**Response** (200):
```json
{
  "extended": true,
  "message": "Lock extended successfully"
}
```

### 4. Check Lock Status

Check if a resource is currently locked.

**Endpoint**: `GET /api/v1/locks/{resourceKey}/status`

**Example**:
```
GET /api/v1/locks/order:456/status
```

**Response** (200):
```json
{
  "locked": true,
  "resourceKey": "order:456",
  "tenantId": "tenant-123"
}
```

### 5. Get Lock Details

Get detailed information about a specific lock.

**Endpoint**: `GET /api/v1/locks/{resourceKey}`

**Example**:
```
GET /api/v1/locks/order:456
```

**Response** (200):
```json
{
  "lockId": "550e8400-e29b-41d4-a716-446655440000",
  "tenantId": "tenant-123",
  "resourceKey": "order:456",
  "lockType": "EXCLUSIVE",
  "status": "LOCKED",
  "holderId": "service-instance-1",
  "holderName": "OrderProcessingService",
  "acquiredAt": "2024-01-15T10:30:00",
  "expiresAt": "2024-01-15T10:35:00",
  "remainingTtlSeconds": 240,
  "retryCount": 0,
  "metadata": "{\"transactionId\": \"tx-789\"}"
}
```

**Response** (404 - Not Found):
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Lock not found for resource: order:456",
  "path": "/api/v1/locks/order:456"
}
```

### 6. Get All Active Locks

Get all active locks for a tenant.

**Endpoint**: `GET /api/v1/locks`

**Example**:
```
GET /api/v1/locks
```

**Response** (200):
```json
[
  {
    "lockId": "550e8400-e29b-41d4-a716-446655440000",
    "tenantId": "tenant-123",
    "resourceKey": "order:456",
    "lockType": "EXCLUSIVE",
    "status": "LOCKED",
    "holderId": "service-instance-1",
    "remainingTtlSeconds": 240
  },
  {
    "lockId": "660e8400-e29b-41d4-a716-446655440001",
    "tenantId": "tenant-123",
    "resourceKey": "user:789:profile",
    "lockType": "SHARED",
    "status": "LOCKED",
    "holderId": "service-instance-2",
    "remainingTtlSeconds": 180
  }
]
```

### 7. Get Locks by Holder

Get all locks held by a specific holder.

**Endpoint**: `GET /api/v1/locks/by-holder/{holderId}`

**Example**:
```
GET /api/v1/locks/by-holder/service-instance-1
```

**Response** (200):
```json
[
  {
    "lockId": "550e8400-e29b-41d4-a716-446655440000",
    "tenantId": "tenant-123",
    "resourceKey": "order:456",
    "lockType": "EXCLUSIVE",
    "status": "LOCKED",
    "holderId": "service-instance-1",
    "remainingTtlSeconds": 240
  }
]
```

### 8. Force Unlock (Admin)

Forcefully release a lock (admin operation).

**Endpoint**: `DELETE /api/v1/locks/{resourceKey}/force-unlock`

**Example**:
```
DELETE /api/v1/locks/order:456/force-unlock
```

**Response** (200):
```json
{
  "unlocked": true,
  "resourceKey": "order:456",
  "tenantId": "tenant-123"
}
```

### 9. Get Statistics

Get lock statistics for a tenant.

**Endpoint**: `GET /api/v1/locks/statistics`

**Example**:
```
GET /api/v1/locks/statistics
```

**Response** (200):
```json
{
  "tenantId": "tenant-123",
  "activeLocks": 15,
  "expiredLocks": 42,
  "releasedLocks": 156,
  "failedAttempts": 8,
  "averageAcquisitionTimeMs": 45.5,
  "averageHoldTimeMs": 12500.0,
  "locksByType": {
    "EXCLUSIVE": 10,
    "SHARED": 5,
    "WRITE": 0,
    "READ": 0
  },
  "calculatedAt": "2024-01-15T10:30:00",
  "totalOperations": 221,
  "peakConcurrentLocks": 25,
  "waitingRequests": 2
}
```

### 10. Get Global Statistics

Get aggregated statistics across all tenants.

**Endpoint**: `GET /api/v1/locks/statistics/global`

**Example**:
```
GET /api/v1/locks/statistics/global
```

**Response** (200):
```json
{
  "tenantId": "global",
  "activeLocks": 156,
  "expiredLocks": 384,
  "releasedLocks": 1523,
  "failedAttempts": 67,
  "totalOperations": 2130,
  "calculatedAt": "2024-01-15T10:30:00"
}
```

### 11. Get Lock Counts by Tenant

Get active lock counts for each tenant.

**Endpoint**: `GET /api/v1/locks/statistics/by-tenant`

**Example**:
```
GET /api/v1/locks/statistics/by-tenant
```

**Response** (200):
```json
{
  "tenant-123": 15,
  "tenant-456": 32,
  "tenant-789": 8
}
```

### 12. Trigger Cleanup

Manually trigger cleanup of expired locks.

**Endpoint**: `POST /api/v1/locks/cleanup`

**Query Parameters**:
| Parameter | Required | Type | Description |
|-----------|----------|------|-------------|
| tenantId | No | String | Tenant ID (null for all tenants) |

**Example** (all tenants):
```
POST /api/v1/locks/cleanup
```

**Example** (specific tenant):
```
POST /api/v1/locks/cleanup?tenantId=tenant-123
```

**Response** (200):
```json
{
  "cleanedUp": 12,
  "tenantId": "tenant-123"
}
```

## Lock Types

| Type | Code | Description | Concurrent |
|------|------|-------------|------------|
| EXCLUSIVE | EXCLUSIVE | Only one holder allowed | No |
| SHARED | SHARED | Multiple readers allowed | Yes |
| WRITE | WRITE | Single writer, no concurrent readers/writers | No |
| READ | READ | Multiple concurrent reads allowed | Yes |

## Error Responses

All errors follow this format:

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Tenant ID is required",
  "path": "/api/v1/locks/acquire"
}
```

## Rate Limiting

Currently, rate limiting is not enforced. Consider implementing for production:

| Tier | Requests per Minute |
|------|---------------------|
| Free | 100 |
| Standard | 1000 |
| Premium | 10000 |

## Pagination

Pagination is not currently implemented for list endpoints. For large result sets, use filtering by holder or specific resource keys.

## SDK Examples

### Java

```java
RestTemplate restTemplate = new RestTemplate();
HttpHeaders headers = new HttpHeaders();
headers.set("X-Tenant-ID", "tenant-123");
headers.setContentType(MediaType.APPLICATION_JSON);

LockRequestDto request = LockRequestDto.builder()
    .tenantId("tenant-123")
    .resourceKey("order:456")
    .holderId("service-instance-1")
    .lockType(LockType.EXCLUSIVE)
    .ttlSeconds(300L)
    .build();

HttpEntity<LockRequestDto> entity = new HttpEntity<>(request, headers);
ResponseEntity<LockAcquisitionResponseDto> response = restTemplate.postForEntity(
    "http://localhost:8080/api/v1/locks/acquire",
    entity,
    LockAcquisitionResponseDto.class
);
```

### Python

```python
import requests

headers = {
    "X-Tenant-ID": "tenant-123",
    "Content-Type": "application/json"
}

data = {
    "tenantId": "tenant-123",
    "resourceKey": "order:456",
    "holderId": "service-instance-1",
    "lockType": "EXCLUSIVE",
    "ttlSeconds": 300
}

response = requests.post(
    "http://localhost:8080/api/v1/locks/acquire",
    json=data,
    headers=headers
)

result = response.json()
if result["acquired"]:
    print(f"Lock acquired: {result['lock']['lockId']}")
```

### cURL

```bash
curl -X POST http://localhost:8080/api/v1/locks/acquire \
  -H "X-Tenant-ID: tenant-123" \
  -H "Content-Type: application/json" \
  -d '{
    "tenantId": "tenant-123",
    "resourceKey": "order:456",
    "holderId": "service-instance-1",
    "lockType": "EXCLUSIVE",
    "ttlSeconds": 300
  }'
```

## Interactive Documentation

Interactive API documentation is available via Swagger UI:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

## Health Check

Monitor service health:

**Endpoint**: `GET /actuator/health`

**Response**:
```json
{
  "status": "UP",
  "components": {
    "redis": {
      "status": "UP",
      "details": {
        "version": "7.0.0"
      }
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

## Metrics

Prometheus metrics are available at:

**Endpoint**: `GET /actuator/metrics`

**Prometheus endpoint**: `GET /actuator/prometheus`
