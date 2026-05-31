# Infrastructure Config Service - API Documentation

## Base URL

```
Development: http://localhost:8090
Production:  https://config.api.gogidix.com
```

## Authentication

All API requests require:

| Header | Description | Example |
|--------|-------------|---------|
| X-Tenant-ID | Tenant identifier | `tenant-123` |
| X-User-ID | User identifier | `user-456` |

## Response Format

All responses follow this structure:

```json
{
  "data": { ... },
  "error": null,
  "timestamp": "2025-02-25T10:00:00Z"
}
```

Error responses:

```json
{
  "timestamp": "2025-02-25T10:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "details": { ... },
  "path": "/api/v1/configurations"
}
```

---

## Configuration Properties API

### Create Configuration

```http
POST /api/v1/configurations
Content-Type: application/json
X-Tenant-ID: tenant-123
X-User-ID: user-456
```

Request Body:
```json
{
  "tenantId": "tenant-123",
  "environment": "DEV",
  "key": "feature.max-users",
  "name": "Max Users",
  "description": "Maximum number of users allowed",
  "value": "1000",
  "defaultValue": "100",
  "valueType": "INTEGER",
  "isActive": true,
  "isSensitive": false,
  "isRequired": true,
  "category": "limits",
  "owner": "platform-team",
  "changeReason": "Increase limit for growth"
}
```

Response: `201 Created`

### Get Configuration

```http
GET /api/v1/configurations/{tenantId}/{key}
```

Response: `200 OK`
```json
{
  "id": "config-123",
  "tenantId": "tenant-123",
  "environment": "DEV",
  "key": "feature.max-users",
  "name": "Max Users",
  "value": "1000",
  "valueType": "INTEGER",
  "isActive": true,
  "createdAt": "2025-02-25T10:00:00Z",
  "updatedAt": "2025-02-25T10:00:00Z"
}
```

### Update Configuration

```http
PUT /api/v1/configurations/{tenantId}/{key}
Content-Type: application/json
```

### Get Configuration Value (Typed)

```http
GET /api/v1/configurations/{tenantId}/{key}/value?type=INTEGER
```

Response: `200 OK`
```json
{
  "key": "feature.max-users",
  "value": 1000,
  "type": "INTEGER"
}
```

### Batch Get Configuration Values

```http
POST /api/v1/configurations/batch
Content-Type: application/json
```

Request Body:
```json
{
  "keys": ["feature.max-users", "feature.timeout", "api.endpoint"]
}
```

### Search Configurations

```http
GET /api/v1/configurations/search?pattern=feature.*
```

### Rollback Configuration

```http
POST /api/v1/configurations/{id}/rollback/{version}
```

### Delete Configuration

```http
DELETE /api/v1/configurations/{tenantId}/{key}
```

---

## Feature Flags API

### Create Feature Flag

```http
POST /api/v1/feature-flags
Content-Type: application/json
```

Request Body:
```json
{
  "tenantId": "tenant-123",
  "flagKey": "new-dashboard",
  "name": "New Dashboard",
  "description": "Enable new dashboard UI",
  "isEnabled": true,
  "rolloutStrategy": "PERCENTAGE",
  "rolloutPercentage": 50,
  "isSticky": true,
  "priority": 100
}
```

### Evaluate Feature Flag

```http
POST /api/v1/feature-flags/evaluate
Content-Type: application/json
```

Request Body:
```json
{
  "flagKey": "new-dashboard",
  "userId": "user-789",
  "context": {
    "role": "admin",
    "tier": "premium",
    "country": "US"
  }
}
```

Response: `200 OK`
```json
{
  "flagKey": "new-dashboard",
  "enabled": true,
  "reason": "FLAG_ENABLED",
  "rolloutStrategy": "PERCENTAGE",
  "isSticky": true,
  "userId": "user-789",
  "evaluatedAt": "2025-02-25T10:00:00Z"
}
```

### Batch Evaluate Feature Flags

```http
POST /api/v1/feature-flags/evaluate/batch
Content-Type: application/json
```

Request Body:
```json
{
  "flagKeys": ["flag1", "flag2", "flag3"],
  "userId": "user-789",
  "context": {}
}
```

Response: `200 OK`
```json
{
  "flag1": true,
  "flag2": false,
  "flag3": true
}
```

### Get All Feature Flags

```http
GET /api/v1/feature-flags?activeOnly=true
```

### Toggle Feature Flag

```http
PUT /api/v1/feature-flags/{tenantId}/{flagKey}/toggle?enabled=true
```

### Get Feature Flag

```http
GET /api/v1/feature-flags/{id}
```

### Delete Feature Flag

```http
DELETE /api/v1/feature-flags/{tenantId}/{flagKey}
```

---

## Secrets API

### Create Secret

```http
POST /api/v1/secrets
Content-Type: application/json
```

Request Body:
```json
{
  "tenantId": "tenant-123",
  "secretKey": "stripe-api-key",
  "name": "Stripe API Key",
  "description": "Stripe payment processing API key",
  "secretValue": "sk_live_xxxxx",
  "secretType": "API_KEY",
  "category": "payments",
  "owner": "finance-team",
  "rotationIntervalDays": 90,
  "accessControlList": ["admin", "devops"]
}
```

### Get Secret Metadata

```http
GET /api/v1/secrets/{tenantId}/{secretKey}
```

Note: This returns metadata only, not the decrypted value.

### Get Secret Value

```http
GET /api/v1/secrets/{tenantId}/{secretKey}/value
```

Response: `200 OK`
```json
{
  "secretKey": "stripe-api-key",
  "value": "sk_live_xxxxx"
}
```

### Rotate Secret

```http
POST /api/v1/secrets/{tenantId}/{secretKey}/rotate
Content-Type: application/json
```

Request Body:
```json
{
  "newSecretValue": "sk_live_yyyyy",
  "changeReason": "Quarterly rotation"
}
```

### Get Secrets Needing Rotation

```http
GET /api/v1/secrets/rotation-needed
```

### Get All Secrets

```http
GET /api/v1/secrets
```

### Delete Secret

```http
DELETE /api/v1/secrets/{tenantId}/{secretKey}
```

---

## Version History API

### Get Configuration Versions

```http
GET /api/v1/configurations/{id}/versions
```

Response: `200 OK`
```json
[
  {
    "id": "version-123",
    "configId": "config-123",
    "configType": "CONFIGURATION_PROPERTY",
    "configKey": "feature.max-users",
    "version": 3,
    "previousValue": "500",
    "newValue": "1000",
    "changeType": "UPDATED",
    "changedBy": "user-456",
    "changeReason": "Increase limit for growth",
    "changedAt": "2025-02-25T10:00:00Z",
    "canRollback": true
  }
]
```

### Get Feature Flag Versions

```http
GET /api/v1/feature-flags/{id}/versions
```

### Get Secret Versions

```http
GET /api/v1/secrets/{id}/versions
```

---

## Data Types

### ValueType

| Value | Description |
|-------|-------------|
| STRING | Text string |
| INTEGER | Whole number |
| LONG | Large whole number |
| DOUBLE | Decimal number |
| BOOLEAN | True/False |
| JSON | JSON object/string |
| DATE | Date/Time |
| ENCRYPTED_STRING | Encrypted text |

### Environment

| Value | Description |
|-------|-------------|
| DEV | Development |
| STAGING | Staging/Pre-production |
| PROD | Production |
| TEST | Testing |

### RolloutStrategy

| Value | Description |
|-------|-------------|
| ALL_USERS | All users get same value |
| PERCENTAGE | Percentage-based rollout |
| WHITELIST | Whitelisted users only |
| GRADUAL | Gradual rollout over time |
| CONDITIONAL | Condition-based evaluation |
| AB_TEST | A/B testing variant |

### SecretType

| Value | Description |
|-------|-------------|
| API_KEY | External API key |
| DATABASE_PASSWORD | Database credentials |
| OAUTH_TOKEN | OAuth access token |
| CERTIFICATE | SSL/TLS certificate |
| SSH_KEY | SSH private key |
| ENCRYPTION_KEY | Encryption key |
| WEBHOOK_SECRET | Webhook signature key |
| JWT_SECRET | JWT signing key |
| THIRD_PARTY_KEY | Third-party service key |
| GENERIC_PASSWORD | Generic password |
| SERVICE_ACCOUNT_KEY | Service account credentials |
| ACCESS_TOKEN | Access token |
| REFRESH_TOKEN | Refresh token |

### ChangeType

| Value | Description |
|-------|-------------|
| CREATED | New resource created |
| UPDATED | Resource updated |
| DELETED | Resource deleted |
| ENABLED | Resource enabled |
| DISABLED | Resource disabled |
| ROTATED | Secret rotated |
| ROLLED_BACK | Rolled back to previous version |

---

## Validation Rules

### ConfigurationProperty ValidationRule

```json
{
  "regex": "^[A-Za-z0-9+_.-]+@(.+)$",
  "minValue": 0,
  "maxValue": 100,
  "minLength": 1,
  "maxLength": 255,
  "allowedValues": ["option1", "option2"]
}
```

### FeatureFlagCondition

```json
{
  "type": "ATTRIBUTE",
  "attribute": "role",
  "operator": "EQUALS",
  "value": "admin",
  "negate": false
}
```

Operators:
- EQUALS, NOT_EQUALS
- GREATER_THAN, GREATER_THAN_OR_EQUAL
- LESS_THAN, LESS_THAN_OR_EQUAL
- CONTAINS, NOT_CONTAINS
- STARTS_WITH, ENDS_WITH
- IN, NOT_IN
- BETWEEN
- REGEX
- IS_NULL, IS_NOT_NULL

---

## OpenAPI/Swagger

Interactive API documentation available at:

```
http://localhost:8090/swagger-ui.html
```

OpenAPI JSON:

```
http://localhost:8090/api-docs
```

---

## Error Codes

| Status | Code | Description |
|--------|------|-------------|
| 400 | VALIDATION_ERROR | Request validation failed |
| 401 | UNAUTHORIZED | Missing or invalid credentials |
| 403 | FORBIDDEN | Access denied |
| 404 | NOT_FOUND | Resource not found |
| 409 | CONFLICT | Resource already exists |
| 422 | UNPROCESSABLE_ENTITY | Cannot process request |
| 500 | INTERNAL_ERROR | Server error |

---

## Rate Limiting

| Tier | Requests | Window |
|------|----------|--------|
| Default | 1000 | 1 hour |
| Premium | 10000 | 1 hour |
| Enterprise | Unlimited | - |

Rate limit headers included in all responses:
```
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 999
X-RateLimit-Reset: 1740469200
```
