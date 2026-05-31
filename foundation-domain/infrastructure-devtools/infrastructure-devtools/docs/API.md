# Infrastructure DevTools API Reference

## Base URL

```
http://localhost:8080/api/devtools
```

## Authentication

All endpoints (except health/info) require authentication:

```http
Authorization: Basic <base64(username:password)>
# or
Authorization: Bearer <jwt_token>
```

## API Testing Endpoints

### List Test Cases

```http
GET /api-testing/test-cases?projectId={projectId}&page=0&size=20
```

**Response**:
```json
{
  "content": [
    {
      "uuid": "550e8400-e29b-41d4-a716-446655440000",
      "name": "User API Test",
      "method": "GET",
      "url": "https://api.example.com/users",
      "expectedStatusCode": 200,
      "enabled": true
    }
  ],
  "totalElements": 1,
  "totalPages": 1
}
```

### Create Test Case

```http
POST /api-testing/test-cases
Content-Type: application/json

{
  "name": "User API Test",
  "description": "Test user endpoint",
  "projectId": "default",
  "method": "GET",
  "url": "https://api.example.com/users",
  "expectedStatusCode": 200,
  "headers": {"Authorization": "Bearer token"},
  "enabled": true
}
```

### Execute Test Case

```http
POST /api-testing/test-cases/{id}/execute?executedBy=user@example.com
```

**Response** (async):
```http
HTTP/1.1 202 Accepted

{
  "testCaseUuid": "550e8400-e29b-41d4-a716-446655440000",
  "status": "PASSED",
  "statusCode": 200,
  "responseTime": 125
}
```

### Get Test Statistics

```http
GET /api-testing/statistics?projectId=default
```

**Response**:
```json
{
  "totalTests": 25,
  "enabledTests": 20,
  "disabledTests": 5,
  "executions": {
    "passed": 145,
    "failed": 12
  }
}
```

## Database Query Endpoints

### List Saved Queries

```http
GET /database/queries?projectId={projectId}&page=0&size=20
```

### Execute Saved Query

```http
POST /database/queries/{id}/execute
Content-Type: application/json

{
  "param1": "value1"
}
```

### Execute Ad-Hoc Query

```http
POST /database/execute?sql=SELECT+*+FROM+users&databaseName=default
```

### Validate Query

```http
POST /database/validate?sql=SELECT+*+FROM+users&databaseName=default
```

**Response**:
```json
{
  "valid": true,
  "queryType": "SELECT"
}
```

## Logging Endpoints

### Query Logs

```http
POST /logging/entries/query?page=0&size=50
Content-Type: application/json

{
  "levels": ["ERROR", "WARN"],
  "startDate": "2024-01-01T00:00:00Z",
  "endDate": "2024-01-02T00:00:00Z",
  "search": "error message"
}
```

### Get Log Statistics

```http
GET /logging/statistics?startDate=2024-01-01T00:00:00Z&endDate=2024-01-02T00:00:00Z
```

**Response**:
```json
{
  "startDate": "2024-01-01T00:00:00Z",
  "endDate": "2024-01-02T00:00:00Z",
  "totalCount": 1523,
  "errorCount": 42,
  "warnCount": 156,
  "infoCount": 1125,
  "debugCount": 200
}
```

### Get Logs by Session

```http
GET /logging/entries/session/{sessionId}
```

### Export Logs

```http
POST /logging/export
Content-Type: application/json

{
  "levels": ["ERROR"],
  "startDate": "2024-01-01T00:00:00Z",
  "endDate": "2024-01-02T00:00:00Z"
}
```

**Response**: `application/json` file download

## Deployment Endpoints

### List Deployment Jobs

```http
GET /deployment/jobs?projectId={projectId}&page=0&size=20
```

### Create Deployment Job

```http
POST /deployment/jobs
Content-Type: application/json

{
  "name": "Production Deploy",
  "description": "Deploy to production",
  "projectId": "default",
  "type": "docker",
  "targetEnvironment": "production",
  "deploymentScript": "docker-compose up -d",
  "rollbackScript": "docker-compose down",
  "timeout": 300000,
  "enabled": true
}
```

### Execute Deployment

```http
POST /deployment/jobs/{id}/execute
Content-Type: application/json

{
  "executedBy": "user@example.com",
  "version": "1.0.5",
  "commitSha": "abc123"
}
```

### Rollback Deployment

```http
POST /deployment/executions/{id}/rollback?executedBy=user@example.com
```

### Get Deployment Statistics

```http
GET /deployment/statistics?projectId=default
```

**Response**:
```json
{
  "totalJobs": 10,
  "enabledJobs": 8,
  "executions": {
    "success": 45,
    "failed": 3
  },
  "environments": {
    "production": 3,
    "staging": 3,
    "development": 4
  }
}
```

## Documentation Endpoints

### List Documentation Projects

```http
GET /documentation/projects?projectId={projectId}&page=0&size=20
```

### Create Documentation Project

```http
POST /documentation/projects
Content-Type: application/json

{
  "name": "API Documentation",
  "description": "REST API documentation",
  "projectId": "default",
  "sourcePath": "/path/to/source",
  "outputFormat": "markdown"
}
```

### Generate Documentation

```http
POST /documentation/projects/{id}/generate
Content-Type: application/json

{
  "generatedBy": "user@example.com",
  "formats": ["markdown", "html"]
}
```

### Preview Documentation

```http
POST /documentation/preview
Content-Type: text/plain

# My Documentation

This is markdown content...
```

**Response**: Rendered HTML

## System Endpoints

### Health Check

```http
GET /health
```

**Response**:
```json
{
  "status": "UP",
  "service": "infrastructure-devtools",
  "timestamp": 1704067200000
}
```

### Service Info

```http
GET /info
```

**Response**:
```json
{
  "service": "Infrastructure DevTools Service",
  "version": "1.0.0",
  "description": "Development tools and utilities for the Gogidix platform"
}
```

### List Available Tools

```http
GET /tools
```

**Response**:
```json
{
  "tools": [
    {
      "name": "API Testing",
      "description": "Test and validate REST APIs",
      "endpoint": "/api-testing",
      "enabled": true
    },
    {
      "name": "Database Query",
      "description": "Execute and analyze database queries",
      "endpoint": "/database",
      "enabled": true
    },
    {
      "name": "Logging & Debugging",
      "description": "View and analyze application logs",
      "endpoint": "/logging",
      "enabled": true
    },
    {
      "name": "Deployment",
      "description": "Manage deployments and rollbacks",
      "endpoint": "/deployment",
      "enabled": true
    },
    {
      "name": "Documentation Generator",
      "description": "Generate project documentation",
      "endpoint": "/documentation",
      "enabled": true
    }
  ]
}
```

## Error Responses

All endpoints may return error responses:

```json
{
  "timestamp": "2024-01-01T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/testing/test-cases"
}
```

## Common Status Codes

| Code | Description |
|------|-------------|
| 200 | Success |
| 201 | Created |
| 202 | Accepted (async operation started) |
| 400 | Bad Request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |
| 500 | Internal Server Error |

## OpenAPI/Swagger

Interactive API documentation available at:

```
http://localhost:8080/api/devtools/swagger-ui.html
```

OpenAPI JSON specification:

```
http://localhost:8080/api/devtools/api-docs
```
