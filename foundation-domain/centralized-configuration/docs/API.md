# API Documentation

## Base URLs

| Service | Base URL | Swagger UI |
|---------|----------|------------|
| Config Server | `http://localhost:8888` | `/swagger-ui.html` |
| Feature Flag Service | `http://localhost:8889` | `/swagger-ui.html` |
| Environment Service | `http://localhost:8890` | `/swagger-ui.html` |
| Notification Service | `http://localhost:8891` | `/swagger-ui.html` |
| Config Audit Service | `http://localhost:8892` | `/swagger-ui.html` |

## Common Headers

### Required Headers

| Header | Type | Required | Default | Description |
|--------|------|----------|---------|-------------|
| `X-Tenant-ID` | String | No | `default` | Tenant identifier for multi-tenancy |
| `X-User-ID` | String | No | `system` | User identifier for audit trail |

### Example

```bash
curl -H "X-Tenant-ID: tenant-1" -H "X-User-ID: user-123" \
  http://localhost:8888/api/v1/configs
```

## Config Server API

### Configuration Endpoints

#### Create Configuration

```http
POST /api/v1/configs
Content-Type: application/json
X-Tenant-ID: tenant-1
X-User-ID: admin

{
  "applicationName": "payment-service",
  "profile": "prod",
  "configKey": "database.timeout",
  "configValue": "30000",
  "isEncrypted": false,
  "description": "Database connection timeout in milliseconds"
}
```

**Response**: `201 Created`

```json
{
  "id": 1,
  "tenantId": "tenant-1",
  "applicationName": "payment-service",
  "profile": "prod",
  "configKey": "database.timeout",
  "configValue": "30000",
  "isEncrypted": false,
  "version": 1,
  "isActive": true,
  "description": "Database connection timeout in milliseconds",
  "createdBy": "admin",
  "createdAt": "2024-01-15T10:30:00"
}
```

**Error Responses**:
- `400 Bad Request`: Invalid input data
- `409 Conflict`: Configuration already exists

---

#### Get Configuration by ID

```http
GET /api/v1/configs/{configId}
```

**Response**: `200 OK`

```json
{
  "id": 1,
  "tenantId": "tenant-1",
  "applicationName": "payment-service",
  "profile": "prod",
  "configKey": "database.timeout",
  "configValue": "30000",
  "isEncrypted": false,
  "version": 1,
  "isActive": true,
  "description": "Database connection timeout"
}
```

**Error Responses**:
- `404 Not Found`: Configuration not found

---

#### Get Configuration by Key

```http
GET /api/v1/configs/by-key?application=payment-service&profile=prod&key=database.timeout
X-Tenant-ID: tenant-1
```

**Query Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `application` | String | Yes | Application name |
| `profile` | String | Yes | Environment profile |
| `key` | String | Yes | Configuration key |

---

#### Get All Configurations for Application

```http
GET /api/v1/configs/application/{application}/profile/{profile}
X-Tenant-ID: tenant-1
```

**Response**: `200 OK`

```json
[
  {
    "id": 1,
    "applicationName": "payment-service",
    "profile": "prod",
    "configKey": "database.timeout",
    "configValue": "30000"
  },
  {
    "id": 2,
    "applicationName": "payment-service",
    "profile": "prod",
    "configKey": "database.pool.size",
    "configValue": "50"
  }
]
```

---

#### Search Configurations

```http
GET /api/v1/configs?application=payment-service&profile=prod&isActive=true&page=0&size=20
X-Tenant-ID: tenant-1
```

**Query Parameters**:
| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `application` | String | No | - | Filter by application |
| `profile` | String | No | - | Filter by profile |
| `isActive` | Boolean | No | - | Filter by active status |
| `page` | Integer | No | `0` | Page number (0-indexed) |
| `size` | Integer | No | `20` | Page size |

**Response**: `200 OK`

```json
{
  "items": [
    {
      "id": 1,
      "applicationName": "payment-service",
      "configKey": "database.timeout",
      "configValue": "30000"
    }
  ],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1,
  "isFirst": true,
  "isLast": true
}
```

---

#### Update Configuration

```http
PUT /api/v1/configs/{configId}
Content-Type: application/json
X-Tenant-ID: tenant-1
X-User-ID: admin

{
  "configValue": "60000",
  "isEncrypted": false,
  "description": "Updated timeout",
  "changeReason": "Performance optimization"
}
```

**Response**: `200 OK`

```json
{
  "id": 1,
  "configKey": "database.timeout",
  "configValue": "60000",
  "version": 2
}
```

---

#### Delete Configuration

```http
DELETE /api/v1/configs/{configId}
X-Tenant-ID: tenant-1
X-User-ID: admin
```

**Response**: `204 No Content`

---

#### Get Configuration History

```http
GET /api/v1/configs/{configId}/history
```

**Response**: `200 OK`

```json
[
  {
    "id": 1,
    "configurationId": 1,
    "changeType": "CREATE",
    "oldValue": null,
    "newValue": "30000",
    "version": 1,
    "changedBy": "admin",
    "createdAt": "2024-01-15T10:30:00"
  },
  {
    "id": 2,
    "configurationId": 1,
    "changeType": "UPDATE",
    "oldValue": "30000",
    "newValue": "60000",
    "version": 2,
    "changedBy": "admin",
    "changeReason": "Performance optimization",
    "createdAt": "2024-01-15T11:00:00"
  }
]
```

---

## Config Audit Service API

### Audit Log Endpoints

#### Get All Audit Logs

```http
GET /api/v1/audit-logs
X-Tenant-ID: tenant-1
```

**Response**: `200 OK`

```json
[
  {
    "id": 1,
    "tenantId": "tenant-1",
    "entityType": "CONFIGURATION",
    "entityId": "config-123",
    "action": "UPDATE",
    "oldValue": "old-value",
    "newValue": "new-value",
    "changedBy": "admin",
    "userId": "user-1",
    "userName": "Admin User",
    "userEmail": "admin@example.com",
    "ipAddress": "192.168.1.1",
    "userAgent": "Mozilla/5.0",
    "changeReason": "Configuration update",
    "createdAt": "2024-01-15T10:30:00"
  }
]
```

---

#### Get Audit Logs by Entity Type

```http
GET /api/v1/audit-logs/by-entity-type?entityType=CONFIGURATION
X-Tenant-ID: tenant-1
```

**Query Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `entityType` | String | Yes | Entity type (CONFIGURATION, FEATURE_FLAG, ENVIRONMENT) |

**Supported Entity Types**:
- `CONFIGURATION`
- `FEATURE_FLAG`
- `ENVIRONMENT`

---

#### Get Audit Logs by Entity

```http
GET /api/v1/audit-logs/by-entity?entityType=CONFIGURATION&entityId=config-123
X-Tenant-ID: tenant-1
```

**Query Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `entityType` | String | Yes | Entity type |
| `entityId` | String | Yes | Entity identifier |

---

#### Get Audit Logs by Date Range

```http
GET /api/v1/audit-logs/by-date-range?startDate=2024-01-01T00:00:00&endDate=2024-01-31T23:59:59
X-Tenant-ID: tenant-1
```

**Query Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `startDate` | DateTime | Yes | Start date (ISO 8601) |
| `endDate` | DateTime | Yes | End date (ISO 8601) |

---

#### Get Audit Logs by User

```http
GET /api/v1/audit-logs/by-user?userId=admin
```

**Query Parameters**:
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `userId` | String | Yes | User who made the changes |

---

## Audit Actions

| Action | Code | Description |
|--------|------|-------------|
| CREATE | `create` | Entity created |
| UPDATE | `update` | Entity updated |
| DELETE | `delete` | Entity deleted |
| READ | `read` | Entity read |
| ENABLE | `enable` | Entity enabled |
| DISABLE | `disable` | Entity disabled |
| ROLLOUT | `rollout` | Feature flag rolled out |
| ROLLBACK | `rollback` | Feature flag rolled back |
| EXPORT | `export` | Data exported |
| IMPORT | `import` | Data imported |

## Health Check Endpoints

Each service exposes a health check endpoint:

```http
GET /actuator/health
```

**Response**: `200 OK`

```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "PostgreSQL",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 500000000000,
        "free": 250000000000,
        "threshold": 10485760,
        "path": "/app",
        "exists": true
      }
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

## Error Response Format

All error responses follow this format:

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed for request",
  "path": "/api/v1/configs",
  "details": [
    {
      "field": "applicationName",
      "message": "Application name is required"
    }
  ]
}
```

## Common HTTP Status Codes

| Code | Description |
|------|-------------|
| `200 OK` | Request successful |
| `201 Created` | Resource created successfully |
| `204 No Content` | Request successful, no content returned |
| `400 Bad Request` | Invalid request data |
| `404 Not Found` | Resource not found |
| `409 Conflict` | Resource already exists |
| `500 Internal Server Error` | Server error |

## Pagination

Pagination is supported on list endpoints using `page` and `size` parameters:

- `page`: Zero-based page index (default: 0)
- `size`: Number of items per page (default: 20)

The response includes pagination metadata:

```json
{
  "items": [...],
  "page": 0,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5,
  "isFirst": true,
  "isLast": false
}
```

## Examples

### Complete Workflow Example

```bash
# 1. Create a configuration
curl -X POST http://localhost:8888/api/v1/configs \
  -H "Content-Type: application/json" \
  -H "X-Tenant-ID: my-app" \
  -H "X-User-ID: admin" \
  -d '{
    "applicationName": "user-service",
    "profile": "production",
    "configKey": "email.smtp.host",
    "configValue": "smtp.example.com",
    "description": "SMTP server for email notifications"
  }'

# 2. Get the configuration by key
curl http://localhost:8888/api/v1/configs/by-key \
  -H "X-Tenant-ID: my-app" \
  -d "application=user-service" \
  -d "profile=production" \
  -d "key=email.smtp.host"

# 3. Update the configuration
curl -X PUT http://localhost:8888/api/v1/configs/1 \
  -H "Content-Type: application/json" \
  -H "X-Tenant-ID: my-app" \
  -H "X-User-ID: admin" \
  -d '{
    "configValue": "smtp2.example.com",
    "changeReason": "Server migration"
  }'

# 4. Get the configuration history
curl http://localhost:8888/api/v1/configs/1/history

# 5. Get the audit logs
curl http://localhost:8892/api/v1/audit-logs/by-entity \
  -H "X-Tenant-ID: my-app" \
  -d "entityType=CONFIGURATION" \
  -d "entityId=1"

# 6. Delete the configuration
curl -X DELETE http://localhost:8888/api/v1/configs/1 \
  -H "X-Tenant-ID: my-app" \
  -H "X-User-ID: admin"
```

## OpenAPI Specification

Each service exposes the OpenAPI specification at:

```
http://localhost:{port}/v3/api-docs
```

Download the specification:

```bash
curl http://localhost:8888/v3/api-docs -o openapi.json
```
