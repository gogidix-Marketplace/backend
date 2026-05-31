# Platform API Documentation

## Base URLs

- **Platform Service:** `https://api.gogidix.com/platform/v1`
- **Subscription Service:** `https://api.gogidix.com/subscription/v1`
- **Tenant Registry:** `https://api.gogidix.com/tenant/v1`
- **Usage Metering:** `https://api.gogidix.com/metering/v1`

## Authentication

All API requests require authentication:

```http
Authorization: Bearer <jwt_token>
X-Tenant-ID: <tenant_id>
```

Or API key for service-to-service:

```http
X-API-Key: <api_key>
```

## Platform Service APIs

### Feature Flags

#### Create Feature Flag
```http
POST /api/v1/platform/feature-flags
Content-Type: application/json

{
  "featureKey": "new_dashboard_v2",
  "featureName": "New Dashboard V2",
  "description": "New dashboard interface",
  "featureType": "FEATURE",
  "enabled": true,
  "allowedTenants": ["tenant1", "tenant2"],
  "deniedTenants": [],
  "userSegments": ["beta", "internal"],
  "rolloutRules": {"percentage": 50},
  "requiresOptIn": false
}
```

**Response:** `201 Created`

```json
{
  "id": "flag-123",
  "tenantId": "tenant-123",
  "featureKey": "new_dashboard_v2",
  "featureName": "New Dashboard V2",
  "enabled": true,
  "rolloutPercentage": 100
}
```

#### Get Feature Flag
```http
GET /api/v1/platform/feature-flags/{key}
```

**Response:** `200 OK`

#### Check Feature Enabled
```http
GET /api/v1/platform/feature-flags/{key}/enabled?userId={userId}
```

**Response:** `200 OK`
```json
true
```

#### List All Feature Flags
```http
GET /api/v1/platform/feature-flags
```

#### Toggle Feature Flag
```http
PUT /api/v1/platform/feature-flags/{id}/toggle?enabled=true
```

#### Delete Feature Flag
```http
DELETE /api/v1/platform/feature-flags/{id}
```

**Response:** `204 No Content`

### Platform Configuration

#### Create Configuration
```http
POST /api/v1/platform/configurations
Content-Type: application/json

{
  "configKey": "max_upload_size",
  "configValue": "100MB",
  "configType": "STRING",
  "description": "Maximum file upload size",
  "isSensitive": false,
  "isEncrypted": false,
  "environment": "PRODUCTION",
  "tags": ["upload", "storage"]
}
```

#### Get Configuration
```http
GET /api/v1/platform/configurations/{key}
```

#### Update Configuration
```http
PUT /api/v1/platform/configurations/{id}
Content-Type: application/json

{
  "configValue": "200MB",
  "description": "Updated description"
}
```

#### List Configurations
```http
GET /api/v1/platform/configurations
```

#### Delete Configuration
```http
DELETE /api/v1/platform/configurations/{id}
```

## Subscription Service APIs

### Subscriptions

#### Create Subscription
```http
POST /api/v1/subscriptions
Content-Type: application/json

{
  "customerId": "customer-123",
  "planId": "plan-pro",
  "billingCycle": "MONTHLY",
  "autoRenew": true
}
```

**Response:** `201 Created`

```json
{
  "id": "sub-123",
  "subscriptionNumber": "SUB-1234567890-123",
  "status": "PENDING",
  "billingCycle": "MONTHLY",
  "basePrice": 99.00,
  "totalAmount": 108.90,
  "currency": "USD"
}
```

#### Get Subscription
```http
GET /api/v1/subscriptions/{id}
```

#### List Subscriptions
```http
GET /api/v1/subscriptions?tenantId={tenantId}
```

#### Activate Subscription
```http
POST /api/v1/subscriptions/{id}/activate
```

#### Cancel Subscription
```http
POST /api/v1/subscriptions/{id}/cancel
Content-Type: application/json

{
  "reason": "Downgrading to basic plan"
}
```

#### Update Subscription
```http
PUT /api/v1/subscriptions/{id}
Content-Type: application/json

{
  "planId": "plan-enterprise",
  "billingCycle": "ANNUAL"
}
```

### Subscription Plans

#### List Plans
```http
GET /api/v1/plans
```

**Response:** `200 OK`

```json
[
  {
    "id": "plan-basic",
    "name": "Basic Plan",
    "description": "Essential features for small teams",
    "price": 29.00,
    "currency": "USD",
    "billingCycle": "MONTHLY",
    "features": [
      "Up to 10 users",
      "5GB storage",
      "Email support"
    ],
    "limits": {
      "maxUsers": 10,
      "maxStorageGb": 5,
      "maxApiCallsPerDay": 1000
    }
  }
]
```

### Invoices

#### List Invoices
```http
GET /api/v1/invoices?subscriptionId={subscriptionId}
```

#### Get Invoice
```http
GET /api/v1/invoices/{id}
```

## Tenant Registry APIs

### Tenants

#### Create Tenant
```http
POST /api/v1/tenants
Content-Type: application/json

{
  "tenantName": "Acme Corporation",
  "tenantSlug": "acme-corp",
  "companyName": "Acme Corp Inc.",
  "contactEmail": "admin@acme.com",
  "contactPhone": "+1-555-0100",
  "contactPerson": "John Doe",
  "tier": "TRIAL",
  "address": {
    "street": "123 Main St",
    "city": "San Francisco",
    "state": "CA",
    "country": "US",
    "postalCode": "94105"
  }
}
```

**Response:** `201 Created`

```json
{
  "id": "tenant-internal-123",
  "tenantId": "tenant-external-123",
  "tenantName": "Acme Corporation",
  "tenantSlug": "acme-corp",
  "status": "PENDING",
  "tier": "TRIAL",
  "createdAt": "2024-01-15T10:30:00Z"
}
```

#### Get Tenant
```http
GET /api/v1/tenants/{tenantId}
```

#### List Tenants
```http
GET /api/v1/tenants?status={status}&tier={tier}
```

#### Activate Tenant
```http
POST /api/v1/tenants/{tenantId}/activate
```

#### Suspend Tenant
```http
POST /api/v1/tenants/{tenantId}/suspend
Content-Type: application/json

{
  "reason": "Payment overdue"
}
```

#### Reactivate Tenant
```http
POST /api/v1/tenants/{tenantId}/reactivate
```

#### Update Tenant
```http
PUT /api/v1/tenants/{tenantId}
Content-Type: application/json

{
  "companyName": "Acme Corporation LLC",
  "contactEmail": "newadmin@acme.com",
  "maxUsers": 50,
  "maxStorageGb": 100
}
```

### API Keys

#### Create API Key
```http
POST /api/v1/tenants/{tenantId}/api-keys
Content-Type: application/json

{
  "name": "Production API Key",
  "scopes": ["read", "write"],
  "expiresAt": "2024-12-31T23:59:59Z"
}
```

**Response:** `201 Created`

```json
{
  "id": "key-123",
  "apiKey": "gogidix_live_1234567890abcdef",
  "name": "Production API Key",
  "scopes": ["read", "write"],
  "status": "ACTIVE",
  "createdAt": "2024-01-15T10:30:00Z"
}
```

#### List API Keys
```http
GET /api/v1/tenants/{tenantId}/api-keys
```

#### Revoke API Key
```http
DELETE /api/v1/tenants/{tenantId}/api-keys/{keyId}
```

## Usage Metering APIs

### Usage Records

#### Record Usage
```http
POST /api/v1/usage/records
Content-Type: application/json

{
  "metricName": "api_requests",
  "metricType": "COUNTER",
  "quantity": 1,
  "unit": "requests",
  "eventTime": "2024-01-15T10:30:00Z",
  "dimensions": {
    "method": "GET",
    "endpoint": "/api/v1/users",
    "statusCode": 200
  },
  "serviceName": "api-gateway",
  "userId": "user-123",
  "resourceId": "resource-123"
}
```

**Response:** `202 Accepted`

#### Record Batch Usage
```http
POST /api/v1/usage/records/batch
Content-Type: application/json

{
  "records": [
    {
      "metricName": "api_requests",
      "quantity": 1,
      "eventTime": "2024-01-15T10:30:00Z"
    },
    {
      "metricName": "storage_bytes",
      "quantity": 1024,
      "eventTime": "2024-01-15T10:30:00Z"
    }
  ]
}
```

#### Query Usage
```http
GET /api/v1/usage/records?metricName={metricName}&startDate={start}&endDate={end}
```

### Usage Aggregates

#### Get Aggregated Usage
```http
GET /api/v1/usage/aggregates?tenantId={tenantId}&metricName={metric}&period={day|week|month}
```

**Response:** `200 OK`

```json
[
  {
    "metricName": "api_requests",
    "period": "2024-01-15",
    "total": 15432,
    "unit": "requests",
    "aggregationType": "SUM"
  }
]
```

### Quotas

#### Check Quota
```http
GET /api/v1/quotas/{quotaId}/usage
```

**Response:** `200 OK`

```json
{
  "quotaId": "quota-123",
  "metricName": "api_requests",
  "limit": 10000,
  "used": 5432,
  "remaining": 4568,
  "percentageUsed": 54.32,
  "resetsAt": "2024-01-16T00:00:00Z"
}
```

#### List Quotas
```http
GET /api/v1/quotas?tenantId={tenantId}
```

### Metric Definitions

#### Create Metric Definition
```http
POST /api/v1/metrics/definitions
Content-Type: application/json

{
  "metricName": "api_requests",
  "displayName": "API Requests",
  "description": "Number of API requests made",
  "metricType": "COUNTER",
  "unit": "requests",
  "aggregationType": "SUM",
  "retentionDays": 90
}
```

#### List Metric Definitions
```http
GET /api/v1/metrics/definitions
```

## Error Responses

All endpoints may return standard error responses:

### 400 Bad Request
```json
{
  "error": "VALIDATION_ERROR",
  "message": "Invalid request data",
  "details": [
    {
      "field": "email",
      "message": "Must be a valid email address"
    }
  ]
}
```

### 401 Unauthorized
```json
{
  "error": "UNAUTHORIZED",
  "message": "Authentication required"
}
```

### 403 Forbidden
```json
{
  "error": "FORBIDDEN",
  "message": "Insufficient permissions"
}
```

### 404 Not Found
```json
{
  "error": "NOT_FOUND",
  "message": "Resource not found"
}
```

### 409 Conflict
```json
{
  "error": "CONFLICT",
  "message": "Resource already exists"
}
```

### 429 Rate Limit Exceeded
```json
{
  "error": "RATE_LIMIT_EXCEEDED",
  "message": "Too many requests",
  "retryAfter": 60
}
```

### 500 Internal Server Error
```json
{
  "error": "INTERNAL_ERROR",
  "message": "An unexpected error occurred",
  "requestId": "req-abc-123"
}
```

## Pagination

List endpoints support pagination:

```http
GET /api/v1/subscriptions?page=0&size=20&sort=createdAt,desc
```

**Response Headers:**
```
X-Total-Count: 150
X-Total-Pages: 8
```

## Rate Limiting

API rate limits by tier:

| Tier | Requests/Minute | Requests/Day |
|------|----------------|--------------|
| Trial | 60 | 1,000 |
| Starter | 100 | 10,000 |
| Standard | 300 | 50,000 |
| Premium | 600 | 100,000 |
| Enterprise | Unlimited | Unlimited |

Rate limit headers are included in responses:
```
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 95
X-RateLimit-Reset: 1642252800
```

## Webhooks

Services can send webhook notifications for events:

### Configure Webhook
```http
POST /api/v1/webhooks
Content-Type: application/json

{
  "url": "https://your-app.com/webhooks",
  "events": ["subscription.created", "subscription.cancelled"],
  "secret": "webhook_secret_key"
}
```

### Webhook Payload
```json
{
  "eventId": "evt-123",
  "eventType": "subscription.created",
  "timestamp": "2024-01-15T10:30:00Z",
  "data": {
    "subscriptionId": "sub-123",
    "customerId": "customer-123"
  }
}
```

## SDKs

Official SDKs available:

- **Java:** `com.gogidix:platform-client:1.0.0`
- **Python:** `gogidix-platform`
- **JavaScript:** `@gogidix/platform-client`

For more details, see the SDK documentation.
