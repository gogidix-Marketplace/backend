# Infrastructure Database Service - API Documentation

## Base URL

```
http://localhost:8091/api/v1
```

## Authentication

All API requests require a valid JWT token in the Authorization header:

```
Authorization: Bearer <token>
```

## Response Format

All responses follow this structure:

```json
{
  "success": true,
  "message": "Operation successful",
  "data": { ... },
  "timestamp": "2024-01-01T00:00:00"
}
```

## Connection Pools API

### Create Connection Pool

```http
POST /connection-pools
Content-Type: application/json

{
  "tenantId": "tenant1",
  "environment": "PROD",
  "poolName": "main-pool",
  "description": "Primary database pool",
  "databaseType": "POSTGRESQL",
  "jdbcUrl": "jdbc:postgresql://localhost:5432/mydb",
  "username": "dbuser",
  "password": "dbpass",
  "minimumIdle": 2,
  "maximumPoolSize": 10,
  "connectionTimeout": 30000,
  "idleTimeout": 600000,
  "maxLifetime": 1800000,
  "isActive": true
}
```

### Get Connection Pool

```http
GET /connection-pools/{tenantId}/{poolName}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": "pool123",
    "tenantId": "tenant1",
    "poolName": "main-pool",
    "databaseType": "POSTGRESQL",
    "jdbcUrl": "jdbc:postgresql://localhost:5432/mydb",
    "status": "ACTIVE",
    "minimumIdle": 2,
    "maximumPoolSize": 10
  }
}
```

### Update Connection Pool

```http
PUT /connection-pools/{id}
Content-Type: application/json

{
  "maximumPoolSize": 20,
  "description": "Updated description"
}
```

### Delete Connection Pool

```http
DELETE /connection-pools/{tenantId}/{poolName}
```

### Activate Connection Pool

```http
POST /connection-pools/{tenantId}/{poolName}/activate
```

### Deactivate Connection Pool

```http
POST /connection-pools/{tenantId}/{poolName}/deactivate
```

### Get Pool Statistics

```http
GET /connection-pools/{tenantId}/{poolName}/statistics
```

**Response:**
```json
{
  "success": true,
  "data": {
    "activeConnections": 3,
    "idleConnections": 2,
    "totalConnections": 5,
    "threadsAwaitingConnection": 0
  }
}
```

### Health Check

```http
GET /connection-pools/health
```

## Tenant Databases API

### Create Tenant Database

```http
POST /tenant-databases
Content-Type: application/json

{
  "tenantId": "tenant1",
  "tenantName": "Acme Corporation",
  "environment": "PROD",
  "strategy": "DATABASE_PER_TENANT",
  "databaseType": "POSTGRESQL",
  "databaseName": "tenant1_db",
  "tier": "STANDARD",
  "isActive": true
}
```

### Get Tenant Database

```http
GET /tenant-databases/{tenantId}
```

### Get All Tenants

```http
GET /tenant-databases
```

### Update Tenant Database

```http
PUT /tenant-databases/{id}
Content-Type: application/json

{
  "tier": "PREMIUM",
  "description": "Updated tenant configuration"
}
```

### Activate Tenant

```http
POST /tenant-databases/{tenantId}/activate
```

### Deactivate Tenant

```http
POST /tenant-databases/{tenantId}/deactivate
```

### Deprovision Tenant

```http
DELETE /tenant-databases/{tenantId}
```

### Get Tenant Statistics

```http
GET /tenant-databases/{tenantId}/statistics
```

**Response:**
```json
{
  "success": true,
  "data": {
    "tenantId": "tenant1",
    "status": "ACTIVE",
    "strategy": "DATABASE_PER_TENANT",
    "tier": "STANDARD",
    "currentConnections": 5,
    "maxConnections": 10
  }
}
```

### Upgrade Tenant Tier

```http
POST /tenant-databases/{tenantId}/tier?tier=PREMIUM
```

### Check Connection Availability

```http
GET /tenant-databases/{tenantId}/can-connect
```

## Database Migrations API

### Create Migration

```http
POST /migrations
Content-Type: application/json

{
  "tenantId": "tenant1",
  "environment": "PROD",
  "targetDatabase": "mydb",
  "migrationType": "FLYWAY",
  "version": "1.0.0",
  "description": "Initial schema",
  "scriptName": "V1.0.0__initial_schema.sql",
  "scriptContent": "CREATE TABLE users (id INT PRIMARY KEY);",
  "rollbackEnabled": true
}
```

### Execute Migration

```http
POST /migrations/{id}/execute
```

### Get Migration Status

```http
GET /migrations/{id}
```

### Get Pending Migrations

```http
GET /migrations/pending/{tenantId}/{targetDatabase}
```

### Rollback Migration

```http
POST /migrations/{id}/rollback
```

### Approve Migration

```http
POST /migrations/{id}/approve?approvedBy=admin
```

### Schedule Migration

```http
POST /migrations/{id}/schedule?scheduledFor=2024-01-01T02:00:00
```

## Query Monitoring API

### Record Query Metric

```http
POST /query-metrics
Content-Type: application/json

{
  "tenantId": "tenant1",
  "databaseName": "mydb",
  "queryType": "SELECT",
  "queryText": "SELECT * FROM users WHERE id = ?",
  "normalizedQuery": "SELECT * FROM users WHERE id = ?",
  "queryHash": "abc123",
  "executedAt": "2024-01-01T00:00:00",
  "executionDurationMs": 150,
  "rowsAffected": 1,
  "status": "SUCCESS"
}
```

### Get Slow Queries

```http
GET /query-metrics/slow/{tenantId}?threshold=1000
```

### Get Query Statistics

```http
GET /query-metrics/statistics/{tenantId}/{databaseName}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "totalQueries": 1000,
    "avgDuration": 45.5,
    "slowQueries": 25,
    "byType": {
      "SELECT": 700,
      "INSERT": 200,
      "UPDATE": 100
    }
  }
}
```

### Get Optimization Suggestions

```http
GET /query-metrics/suggestions/{tenantId}
```

## Backup API

### Create Backup

```http
POST /backups
Content-Type: application/json

{
  "tenantId": "tenant1",
  "environment": "PROD",
  "targetDatabase": "mydb",
  "backupType": "FULL",
  "backupMethod": "LOGICAL",
  "backupName": "daily-backup-2024-01-01",
  "retentionDays": 30,
  "compressed": true
}
```

### Execute Backup

```http
POST /backups/{id}/execute
```

### Restore from Backup

```http
POST /backups/{id}/restore
```

### Get Latest Backup

```http
GET /backups/latest/{tenantId}/{targetDatabase}
```

### Validate Backup

```http
POST /backups/{id}/validate
```

### Get Backup Statistics

```http
GET /backups/statistics/{tenantId}
```

## Distributed Transactions API

### Create Transaction

```http
POST /transactions
Content-Type: application/json

{
  "tenantId": "tenant1",
  "transactionName": "order-processing",
  "transactionType": "TWO_PHASE_COMMIT",
  "participants": [
    {
      "participantId": "order-db",
      "databaseName": "orders",
      "endpoint": "jdbc:postgresql://localhost:5432/orders"
    },
    {
      "participantId": "inventory-db",
      "databaseName": "inventory",
      "endpoint": "jdbc:postgresql://localhost:5432/inventory"
    }
  ],
  "timeoutMs": 60000
}
```

### Execute Transaction

```http
POST /transactions/{id}/execute
```

### Rollback Transaction

```http
POST /transactions/{id}/rollback
```

### Get Transaction Status

```http
GET /transactions/{id}
```

## Health Check API

### Service Health

```http
GET /actuator/health
```

**Response:**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "MongoDB",
        "validationQuery": "db.stats()"
      }
    },
    "redis": {
      "status": "UP"
    }
  }
}
```

## Error Codes

| Code | Description |
|------|-------------|
| 400 | Bad Request - Invalid input |
| 401 | Unauthorized - Missing or invalid token |
| 403 | Forbidden - Insufficient permissions |
| 404 | Not Found - Resource not found |
| 409 | Conflict - Resource already exists |
| 422 | Unprocessable Entity - Validation failed |
| 500 | Internal Server Error |
| 503 | Service Unavailable - Maintenance mode |
