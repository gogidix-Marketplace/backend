# AI Monitoring Service - Service Contract

## Service Responsibility
Centralized monitoring and alerting for all AI microservices.

## Core Functionality
- Metrics collection
- Health monitoring
- Alert management
- Dashboard aggregation
- Performance tracking

## API Contracts

### 1. Create Alert Rule
**Endpoint:** `POST /api/v1/monitoring/alerts`

**Input:**
```json
{
  "name": "string",
  "metric": "string",
  "condition": "GREATER_THAN|LESS_THAN|EQUALS",
  "threshold": "number",
  "notificationChannels": ["string"]
}
```

**Output:**
```json
{
  "alertId": "string (UUID)",
  "status": "ACTIVE|INACTIVE",
  "createdAt": "datetime"
}
```

### 2. Get Service Health
**Endpoint:** `GET /api/v1/monitoring/health/{serviceName}`

## Business Rules
- Default alert threshold: configurable
- Metrics retained for 30 days
- Min alert interval: 1 minute

## Error Conditions
- Service not found (404)
- Invalid metric (400)
