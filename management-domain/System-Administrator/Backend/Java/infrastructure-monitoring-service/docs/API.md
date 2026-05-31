# Infrastructure Monitoring Service - API Documentation

## Base URL
```
http://localhost:8080/api
```

## Headers
```
X-Tenant-Id: {tenant-id}
X-User-Id: {user-id}
X-Correlation-Id: {correlation-id}
```

---

## Infrastructure Monitoring Endpoints

### Get All Infrastructure Monitoring
```http
GET /infrastructure-monitoring?page=0&size=20&sortBy=name&sortDirection=asc
```

**Query Parameters:**
- `page` (optional, default: 0) - Page number
- `size` (optional, default: 20) - Page size
- `sortBy` (optional, default: name) - Sort field
- `sortDirection` (optional, default: asc) - Sort direction (asc/desc)

**Response:** Page<InfrastructureMonitoringDTO>

---

### Get Infrastructure Monitoring by ID
```http
GET /infrastructure-monitoring/{id}
```

**Path Parameters:**
- `id` - Infrastructure monitoring ID

**Response:** InfrastructureMonitoringDTO

**Example Response:**
```json
{
  "id": "507f1f77bcf86cd799439011",
  "name": "web-server-01",
  "type": "SERVER",
  "host": "web-server-01.example.com",
  "port": 443,
  "status": "HEALTHY",
  "healthCheckConfig": {
    "protocol": "HTTPS",
    "path": "/health",
    "intervalSeconds": 30,
    "timeoutSeconds": 5,
    "retryCount": 3
  },
  "tags": {
    "environment": "production",
    "team": "platform"
  },
  "region": "us-east-1",
  "environment": "production",
  "lastCheckedAt": "2026-02-23T10:30:00Z",
  "createdAt": "2026-02-01T10:00:00Z"
}
```

---

### Get by Status
```http
GET /infrastructure-monitoring/status/{status}
```

**Path Parameters:**
- `status` - Monitoring status (HEALTHY, DEGRADED, UNHEALTHY, UNKNOWN, MAINTENANCE)

**Response:** List<InfrastructureMonitoringDTO>

---

### Get by Type
```http
GET /infrastructure-monitoring/type/{type}
```

**Path Parameters:**
- `type` - Infrastructure type (SERVER, DATABASE, CACHE, MESSAGE_QUEUE, LOAD_BALANCER, CONTAINER, KUBERNETES_CLUSTER, STORAGE, CDN, API_GATEWAY)

**Response:** List<InfrastructureMonitoringDTO>

---

### Get by Environment
```http
GET /infrastructure-monitoring/environment/{environment}
```

**Path Parameters:**
- `environment` - Environment name (e.g., production, staging)

**Response:** List<InfrastructureMonitoringDTO>

---

### Search Infrastructure
```http
GET /infrastructure-monitoring/search?q={searchTerm}
```

**Query Parameters:**
- `q` - Search term (searches in name and host)

**Response:** List<InfrastructureMonitoringDTO>

---

### Get Stale Monitors
```http
GET /infrastructure-monitoring/stale?thresholdMinutes=5
```

**Query Parameters:**
- `thresholdMinutes` (optional, default: 5) - Minutes since last check

**Response:** List<InfrastructureMonitoringDTO>

---

### Create Infrastructure Monitoring
```http
POST /infrastructure-monitoring
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "web-server-02",
  "type": "SERVER",
  "host": "web-server-02.example.com",
  "port": 443,
  "healthCheckConfig": {
    "protocol": "HTTPS",
    "path": "/health",
    "intervalSeconds": 30,
    "timeoutSeconds": 5,
    "retryCount": 3
  },
  "tags": {
    "environment": "production",
    "team": "platform"
  },
  "region": "us-east-1",
  "environment": "production"
}
```

**Response:** InfrastructureMonitoringDTO
**Status:** 201 Created

---

### Update Infrastructure Monitoring
```http
PUT /infrastructure-monitoring/{id}
Content-Type: application/json
```

**Path Parameters:**
- `id` - Infrastructure monitoring ID

**Request Body:** InfrastructureMonitoringDTO (partial update supported)

**Response:** InfrastructureMonitoringDTO

---

### Update Status
```http
PATCH /infrastructure-monitoring/{id}/status?status={status}
```

**Path Parameters:**
- `id` - Infrastructure monitoring ID

**Query Parameters:**
- `status` - New status (HEALTHY, DEGRADED, UNHEALTHY, UNKNOWN, MAINTENANCE)

**Response:** InfrastructureMonitoringDTO

---

### Add Metric
```http
POST /infrastructure-monitoring/{id}/metrics
Content-Type: application/json
```

**Path Parameters:**
- `id` - Infrastructure monitoring ID

**Request Body:**
```json
{
  "metricName": "cpu_usage",
  "value": 75.5,
  "unit": "percent",
  "timestamp": "2026-02-23T10:30:00Z"
}
```

**Response:** InfrastructureMonitoringDTO

---

### Delete Infrastructure Monitoring
```http
DELETE /infrastructure-monitoring/{id}
```

**Path Parameters:**
- `id` - Infrastructure monitoring ID

**Response:** 204 No Content

---

### Get Statistics Summary
```http
GET /infrastructure-monitoring/stats/summary
```

**Response:** MonitoringStats
```json
{
  "total": 42,
  "healthy": 35,
  "degraded": 4,
  "unhealthy": 2,
  "unknown": 1,
  "maintenance": 0
}
```

---

## Monitoring Alert Endpoints

### Get All Alerts
```http
GET /monitoring-alerts?page=0&size=20&sortBy=createdAt&sortDirection=desc
```

**Query Parameters:**
- `page` (optional, default: 0) - Page number
- `size` (optional, default: 20) - Page size
- `sortBy` (optional, default: createdAt) - Sort field
- `sortDirection` (optional, default: desc) - Sort direction (asc/desc)

**Response:** Page<MonitoringAlertDTO>

---

### Get Alert by ID
```http
GET /monitoring-alerts/{id}
```

**Path Parameters:**
- `id` - Alert ID

**Response:** MonitoringAlertDTO

**Example Response:**
```json
{
  "id": "507f1f77bcf86cd799439012",
  "infrastructureId": "507f1f77bcf86cd799439011",
  "infrastructureName": "web-server-01",
  "severity": "CRITICAL",
  "type": "SERVICE_DOWN",
  "title": "Infrastructure Unhealthy: web-server-01",
  "description": "Infrastructure web-server-01 (SERVER) at web-server-01.example.com is reporting as UNHEALTHY.",
  "status": "OPEN",
  "occurrenceCount": 1,
  "firstOccurredAt": "2026-02-23T10:25:00Z",
  "lastOccurredAt": "2026-02-23T10:25:00Z",
  "createdAt": "2026-02-23T10:25:00Z"
}
```

---

### Get Alerts by Status
```http
GET /monitoring-alerts/status/{status}
```

**Path Parameters:**
- `status` - Alert status (OPEN, ACKNOWLEDGED, RESOLVED, CLOSED, SUPPRESSED)

**Response:** List<MonitoringAlertDTO>

---

### Get Alerts by Severity
```http
GET /monitoring-alerts/severity/{severity}
```

**Path Parameters:**
- `severity` - Alert severity (CRITICAL, HIGH, MEDIUM, LOW, INFO)

**Response:** List<MonitoringAlertDTO>

---

### Get Alerts by Infrastructure
```http
GET /monitoring-alerts/infrastructure/{infrastructureId}
```

**Path Parameters:**
- `infrastructureId` - Infrastructure ID

**Response:** List<MonitoringAlertDTO>

---

### Get Active Critical Alerts
```http
GET /monitoring-alerts/active
```

**Response:** List<MonitoringAlertDTO> (critical and high severity open alerts)

---

### Create Alert
```http
POST /monitoring-alerts
Content-Type: application/json
```

**Request Body:**
```json
{
  "infrastructureId": "507f1f77bcf86cd799439011",
  "infrastructureName": "web-server-01",
  "severity": "HIGH",
  "type": "HIGH_CPU",
  "title": "High CPU Usage",
  "description": "CPU usage is above 90% for more than 5 minutes",
  "details": {
    "currentValue": 95.5,
    "threshold": 90,
    "duration": "5m"
  }
}
```

**Response:** MonitoringAlertDTO
**Status:** 201 Created

---

### Acknowledge Alert
```http
POST /monitoring-alerts/{id}/acknowledge?acknowledgedBy={userId}
```

**Path Parameters:**
- `id` - Alert ID

**Query Parameters:**
- `acknowledgedBy` - User ID acknowledging the alert

**Response:** MonitoringAlertDTO

---

### Resolve Alert
```http
POST /monitoring-alerts/{id}/resolve?resolvedBy={userId}&resolutionNotes={notes}
```

**Path Parameters:**
- `id` - Alert ID

**Query Parameters:**
- `resolvedBy` - User ID resolving the alert
- `resolutionNotes` (optional) - Resolution notes

**Response:** MonitoringAlertDTO

---

### Delete Alert
```http
DELETE /monitoring-alerts/{id}
```

**Path Parameters:**
- `id` - Alert ID

**Response:** 204 No Content

---

### Cleanup Old Alerts
```http
POST /monitoring-alerts/cleanup?daysToKeep=30
```

**Query Parameters:**
- `daysToKeep` (optional, default: 30) - Days to keep resolved alerts

**Response:** 202 Accepted

---

### Get Alert Statistics
```http
GET /monitoring-alerts/stats/summary
```

**Response:** AlertStats
```json
{
  "total": 150,
  "open": 12,
  "acknowledged": 5,
  "resolved": 133,
  "critical": 2,
  "high": 8
}
```

---

## Health Check

```http
GET /actuator/health
```

**Response:**
```json
{
  "status": "UP"
}
```

---

## Error Responses

All endpoints may return the following error responses:

### 400 Bad Request
```json
{
  "timestamp": "2026-02-23T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Name is required",
  "path": "/api/infrastructure-monitoring"
}
```

### 404 Not Found
```json
{
  "timestamp": "2026-02-23T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Infrastructure monitoring not found with id: {id}",
  "path": "/api/infrastructure-monitoring/{id}"
}
```

### 500 Internal Server Error
```json
{
  "timestamp": "2026-02-23T10:30:00Z",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred",
  "path": "/api/infrastructure-monitoring"
}
```
