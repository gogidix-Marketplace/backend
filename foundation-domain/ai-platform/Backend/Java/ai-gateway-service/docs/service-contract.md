# AI Gateway Service - Service Contract

## Service Responsibility
API Gateway for routing, rate limiting, and security across all AI microservices.

## Core Functionality
- Request routing
- Load balancing
- Rate limiting
- Authentication/authorization
- Request/response transformation
- Circuit breaker

## API Contracts

### 1. Route Configuration
**Endpoint:** `POST /api/v1/gateway/routes`

**Input:**
```json
{
  "routeId": "string",
  "path": "string",
  "targetService": "string",
  "filters": ["string"],
  "rateLimit": "integer"
}
```

**Output:**
```json
{
  "routeId": "string",
  "status": "ACTIVE|INACTIVE",
  "createdAt": "datetime"
}
```

### 2. Health Check
**Endpoint:** `GET /api/v1/gateway/health`

## Business Rules
- Default rate limit: 1000 req/min
- Circuit breaker opens after 5 failures
- Request timeout: 30 seconds

## Error Conditions
- Service unavailable (503)
- Rate limit exceeded (429)
- Invalid route (400)
