# Alert Management Service - API Documentation

## Base URL

```
http://localhost:8080
```

## Overview

The Alert Management Service provides REST APIs for managing alert rules, alerts, and alert history. All endpoints support multi-tenancy via the `tenantId` parameter.

## Authentication

All endpoints require JWT authentication. Include the bearer token in the Authorization header:

```
Authorization: Bearer <jwt-token>
```

## API Endpoints

### Alert Rules

#### Create Alert Rule

Creates a new alert rule for monitoring metrics.

```http
POST /alerts/rules?tenantId={tenantId}&userId={userId}
Content-Type: application/json
```

**Path Parameters:**
- `tenantId` (optional, query): Tenant ID. Defaults to "default"
- `userId` (optional, query): User ID creating the rule. Defaults to "system"

**Request Body:**

```json
{
  "name": "High CPU Usage",
  "description": "Alert when CPU usage exceeds 80%",
  "enabled": true,
  "serviceName": "order-service",
  "metricName": "cpu.usage",
  "conditionType": "THRESHOLD",
  "threshold": 80.0,
  "operator": "GREATER_THAN",
  "durationSeconds": 300,
  "severity": "HIGH",
  "notificationChannels": ["EMAIL", "SLACK"],
  "recipients": ["admin@example.com", "#alerts"],
  "cooldownSeconds": 600,
  "messageTemplate": "CPU usage is {value}% on {service}",
  "metadata": {
    "team": "platform"
  },
  "tags": {
    "environment": "production"
  }
}
```

**Response:** `201 Created`

```json
{
  "id": "rule-123",
  "tenantId": "tenant-1",
  "name": "High CPU Usage",
  "description": "Alert when CPU usage exceeds 80%",
  "enabled": true,
  "serviceName": "order-service",
  "metricName": "cpu.usage",
  "conditionType": "THRESHOLD",
  "threshold": 80.0,
  "operator": "GREATER_THAN",
  "durationSeconds": 300,
  "severity": "HIGH",
  "notificationChannels": ["EMAIL", "SLACK"],
  "recipients": ["admin@example.com", "#alerts"],
  "cooldownSeconds": 600,
  "messageTemplate": "CPU usage is {value}% on {service}",
  "metadata": {
    "team": "platform"
  },
  "tags": {
    "environment": "production"
  },
  "createdAt": "2024-01-15T10:30:00Z",
  "createdBy": "user-1"
}
```

---

#### List Alert Rules

Returns all alert rules for a tenant.

```http
GET /alerts/rules?tenantId={tenantId}
```

**Path Parameters:**
- `tenantId` (optional, query): Tenant ID. Defaults to "default"

**Response:** `200 OK`

```json
[
  {
    "id": "rule-123",
    "tenantId": "tenant-1",
    "name": "High CPU Usage",
    "enabled": true,
    "severity": "HIGH",
    "metricName": "cpu.usage"
  }
]
```

---

#### Get Alert Rule

Returns a specific alert rule by ID.

```http
GET /alerts/rules/{ruleId}?tenantId={tenantId}
```

**Path Parameters:**
- `ruleId` (required, path): Alert rule ID
- `tenantId` (optional, query): Tenant ID. Defaults to "default"

**Response:** `200 OK`

```json
{
  "id": "rule-123",
  "tenantId": "tenant-1",
  "name": "High CPU Usage",
  "description": "Alert when CPU exceeds 80%",
  "enabled": true,
  "serviceName": "order-service",
  "metricName": "cpu.usage",
  "conditionType": "THRESHOLD",
  "threshold": 80.0,
  "operator": "GREATER_THAN",
  "severity": "HIGH"
}
```

**Error Response:** `404 Not Found`

```json
{
  "errorCode": "NOT_FOUND",
  "message": "Alert rule not found: rule-123"
}
```

---

#### Delete Alert Rule

Deletes an alert rule by ID.

```http
DELETE /alerts/rules/{ruleId}?tenantId={tenantId}
```

**Path Parameters:**
- `ruleId` (required, path): Alert rule ID
- `tenantId` (optional, query): Tenant ID. Defaults to "default"

**Response:** `204 No Content`

---

### Alerts

#### List Alerts

Returns alerts for a tenant with pagination and filtering.

```http
GET /alerts?tenantId={tenantId}&status={status}&severity={severity}&page={page}&size={size}
```

**Query Parameters:**
- `tenantId` (optional): Tenant ID. Defaults to "default"
- `status` (optional): Filter by status (OPEN, ACKNOWLEDGED, RESOLVED, SUPPRESSED, CLOSED)
- `severity` (optional): Filter by severity (CRITICAL, HIGH, MEDIUM, LOW, INFO)
- `page` (optional): Page number (0-based). Defaults to 0
- `size` (optional): Page size. Defaults to 20

**Response:** `200 OK`

```json
{
  "content": [
    {
      "id": "alert-123",
      "tenantId": "tenant-1",
      "ruleId": "rule-456",
      "ruleName": "High CPU Usage",
      "serviceName": "order-service",
      "metricName": "cpu.usage",
      "severity": "HIGH",
      "status": "OPEN",
      "message": "CPU usage is high",
      "triggerValue": 85.5,
      "threshold": 80.0,
      "triggeredAt": "2024-01-15T10:30:00Z",
      "acknowledgedAt": null,
      "acknowledgedBy": null,
      "acknowledgmentComment": null,
      "resolvedAt": null,
      "resolvedBy": null,
      "resolutionComment": null,
      "notificationStatus": "SENT",
      "notificationAttempts": 1,
      "context": {
        "host": "server-1"
      },
      "tags": {
        "environment": "production"
      },
      "createdAt": "2024-01-15T10:30:00Z",
      "updatedAt": "2024-01-15T10:30:00Z"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 20,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    }
  },
  "totalPages": 5,
  "totalElements": 100,
  "last": false,
  "first": true,
  "size": 20,
  "number": 0,
  "sort": {
    "sorted": true,
    "unsorted": false,
    "empty": false
  },
  "numberOfElements": 20,
  "empty": false
}
```

---

#### Get Alert

Returns a specific alert by ID.

```http
GET /alerts/{alertId}?tenantId={tenantId}
```

**Path Parameters:**
- `alertId` (required, path): Alert ID
- `tenantId` (optional, query): Tenant ID. Defaults to "default"

**Response:** `200 OK`

```json
{
  "id": "alert-123",
  "tenantId": "tenant-1",
  "ruleId": "rule-456",
  "ruleName": "High CPU Usage",
  "serviceName": "order-service",
  "metricName": "cpu.usage",
  "severity": "HIGH",
  "status": "OPEN",
  "message": "CPU usage is high",
  "triggerValue": 85.5,
  "threshold": 80.0,
  "triggeredAt": "2024-01-15T10:30:00Z",
  "notificationStatus": "SENT",
  "createdAt": "2024-01-15T10:30:00Z"
}
```

---

#### Acknowledge Alert

Acknowledges an alert to indicate that it is being investigated.

```http
POST /alerts/{alertId}/acknowledge?tenantId={tenantId}
Content-Type: application/json
```

**Path Parameters:**
- `alertId` (required, path): Alert ID
- `tenantId` (optional, query): Tenant ID. Defaults to "default"

**Request Body:**

```json
{
  "userId": "user-123",
  "comment": "Investigating the issue"
}
```

**Response:** `200 OK`

```json
{
  "id": "alert-123",
  "status": "ACKNOWLEDGED",
  "acknowledgedBy": "user-123",
  "acknowledgmentComment": "Investigating the issue",
  "acknowledgedAt": "2024-01-15T10:35:00Z",
  "updatedAt": "2024-01-15T10:35:00Z"
}
```

**Error Response:** `400 Bad Request`

```json
{
  "errorCode": "VALIDATION",
  "message": "Alert is already acknowledged"
}
```

---

#### Resolve Alert

Resolves an alert after the issue has been fixed.

```http
POST /alerts/{alertId}/resolve?tenantId={tenantId}
Content-Type: application/json
```

**Path Parameters:**
- `alertId` (required, path): Alert ID
- `tenantId` (optional, query): Tenant ID. Defaults to "default"

**Request Body:**

```json
{
  "userId": "user-123",
  "comment": "Fixed by restarting the service"
}
```

**Response:** `200 OK`

```json
{
  "id": "alert-123",
  "status": "RESOLVED",
  "resolvedBy": "user-123",
  "resolutionComment": "Fixed by restarting the service",
  "resolvedAt": "2024-01-15T11:00:00Z",
  "updatedAt": "2024-01-15T11:00:00Z"
}
```

**Error Response:** `400 Bad Request`

```json
{
  "errorCode": "VALIDATION",
  "message": "Alert is already resolved"
}
```

---

#### Get Alert History

Returns the history of state changes for an alert.

```http
GET /alerts/{alertId}/history?tenantId={tenantId}
```

**Path Parameters:**
- `alertId` (required, path): Alert ID
- `tenantId` (optional, query): Tenant ID. Defaults to "default"

**Response:** `200 OK`

```json
[
  {
    "id": "history-1",
    "alertId": "alert-123",
    "tenantId": "tenant-1",
    "stateChangeType": "CREATED",
    "previousState": null,
    "newState": "OPEN",
    "changedBy": "system",
    "comment": null,
    "context": null,
    "changedAt": "2024-01-15T10:30:00Z"
  },
  {
    "id": "history-2",
    "alertId": "alert-123",
    "tenantId": "tenant-1",
    "stateChangeType": "ACKNOWLEDGED",
    "previousState": "OPEN",
    "newState": "ACKNOWLEDGED",
    "changedBy": "user-123",
    "comment": "Investigating the issue",
    "context": null,
    "changedAt": "2024-01-15T10:35:00Z"
  },
  {
    "id": "history-3",
    "alertId": "alert-123",
    "tenantId": "tenant-1",
    "stateChangeType": "RESOLVED",
    "previousState": "ACKNOWLEDGED",
    "newState": "RESOLVED",
    "changedBy": "user-123",
    "comment": "Fixed by restarting the service",
    "context": null,
    "changedAt": "2024-01-15T11:00:00Z"
  }
]
```

---

### Health Check

#### Health Check

Returns the overall health status of the service.

```http
GET /health
```

**Response:** `200 OK`

```json
{
  "status": "UP",
  "service": "alert-management-service",
  "timestamp": 1705316700000
}
```

#### Liveness Probe

Kubernetes liveness probe endpoint.

```http
GET /health/liveness
```

**Response:** `200 OK`

```json
{
  "status": "alive"
}
```

#### Readiness Probe

Kubernetes readiness probe endpoint.

```http
GET /health/readiness
```

**Response:** `200 OK`

```json
{
  "status": "ready"
}
```

---

## Enums Reference

### AlertStatus
- `OPEN` - Alert is active and unacknowledged
- `ACKNOWLEDGED` - Alert has been acknowledged
- `RESOLVED` - Alert has been resolved
- `SUPPRESSED` - Alert is suppressed
- `CLOSED` - Alert is closed

### AlertSeverity
- `CRITICAL` - Critical severity
- `HIGH` - High severity
- `MEDIUM` - Medium severity
- `LOW` - Low severity
- `INFO` - Informational

### ConditionType
- `THRESHOLD` - Threshold-based condition
- `ANOMALY_DETECTION` - Anomaly detection
- `RATE_OF_CHANGE` - Rate of change
- `MISSING_DATA` - Missing data
- `PREDICTIVE` - Predictive

### ComparisonOperator
- `GREATER_THAN` - Greater than
- `LESS_THAN` - Less than
- `EQUAL_TO` - Equal to
- `NOT_EQUAL_TO` - Not equal to
- `GREATER_THAN_OR_EQUAL` - Greater than or equal
- `LESS_THAN_OR_EQUAL` - Less than or equal

### NotificationChannel
- `EMAIL` - Email
- `SMS` - SMS
- `WEBHOOK` - Webhook
- `SLACK` - Slack
- `PAGERDUTY` - PagerDuty
- `INCIDENT_MANAGEMENT` - Incident Management

### StateChangeType
- `CREATED` - Alert created
- `ACKNOWLEDGED` - Alert acknowledged
- `RESOLVED` - Alert resolved
- `CLOSED` - Alert closed
- `REOPENED` - Alert reopened
- `COMMENT_ADDED` - Comment added
- `NOTIFICATION_SENT` - Notification sent
- `NOTIFICATION_FAILED` - Notification failed
- `ESCALATED` - Alert escalated

---

## Error Responses

All errors follow this format:

```json
{
  "errorCode": "ERROR_CODE",
  "message": "Human readable error message",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

### Common Error Codes

| Status Code | Error Code | Description |
|-------------|------------|-------------|
| 400 | VALIDATION | Validation error |
| 404 | NOT_FOUND | Resource not found |
| 500 | INTERNAL_ERROR | Internal server error |

---

## OpenAPI/Swagger

Interactive API documentation is available at:

```
http://localhost:8080/swagger-ui.html
```

OpenAPI specification:

```
http://localhost:8080/v3/api-docs
```
