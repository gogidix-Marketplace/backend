# Executive Query Service

Query service for executive operations - handles all read operations (CQRS pattern) for KPIs, metrics, and executive analytics.

## Features

- **REST API** for KPI and metric read operations
- **MongoDB** for data persistence
- **Redis** for caching layer
- **JWT Authentication** for tenant isolation
- **Query Optimization** with pagination and filtering
- **Aggregation Pipelines** for analytics
- **Real-time Cache Invalidation**

## Technology Stack

- Node.js >= 18.0.0
- Express.js
- MongoDB
- Redis (ioredis)
- JWT (jsonwebtoken)
- Winston (logging)
- Jest (testing)

## Installation

```bash
npm install
```

## Configuration

Copy `.env.example` to `.env` and configure:

```bash
cp .env.example .env
```

Environment variables:
- `NODE_ENV` - Environment (development/production)
- `PORT` - Server port (default: 3005)
- `MONGODB_URL` - MongoDB connection string
- `MONGODB_DB` - Database name (default: management_executive)
- `REDIS_URL` - Redis connection string
- `REDIS_DB` - Redis database number (default: 3)
- `JWT_SECRET` - JWT signing secret
- `CACHE_TTL` - Cache TTL in seconds (default: 300)
- `LOG_LEVEL` - Logging level (default: info)

## Running

### Development
```bash
npm run dev
```

### Production
```bash
npm start
```

## API Endpoints

### GET /api/kpi
Get all KPIs with pagination and filtering

**Query Parameters:**
- `page` - Page number (default: 1)
- `limit` - Items per page (default: 20)
- `category` - Filter by category
- `executiveLevel` - Filter by executive level
- `status` - Filter by status
- `period` - Filter by period

**Response:** 200 OK
```json
{
  "success": true,
  "data": [
    {
      "_id": "...",
      "tenantId": "tenant-123",
      "name": "Total Revenue",
      "category": "FINANCIAL",
      "value": 1000000,
      "percentChange": 11.11,
      "status": "ON_TRACK",
      "trend": "UP"
    }
  ],
  "pagination": {
    "page": 1,
    "limit": 20,
    "total": 45,
    "pages": 3
  }
}
```

### GET /api/kpi/:id
Get a single KPI by ID

### GET /api/kpi/summary
Get KPI summary by category

**Response:** 200 OK
```json
{
  "success": true,
  "data": {
    "FINANCIAL": {
      "total": 15,
      "onTrack": 10,
      "atRisk": 3,
      "behind": 2
    },
    "OPERATIONAL": {
      "total": 12,
      "onTrack": 8,
      "atRisk": 2,
      "behind": 2
    }
  }
}
```

### GET /api/kpi/dashboard/:level
Get dashboard KPIs for executive level

**Response:** 200 OK
```json
{
  "success": true,
  "data": {
    "kpi": [
      { "name": "Revenue", "value": 1000000, "status": "ON_TRACK" }
    ],
    "summary": {
      "total": 10,
      "ahead": 2,
      "onTrack": 5,
      "atRisk": 2,
      "behind": 1
    }
  }
}
```

### GET /api/metrics
Get all metrics with filtering

### GET /api/analytics/trend
Get KPI trends over time

**Query Parameters:**
- `kpiId` - KPI ID to analyze
- `periods` - Number of periods to analyze (default: 12)

## CQRS Pattern

This service is the **Query** side of CQRS:
- Handles all read operations (SELECT)
- Optimized for fast queries with caching
- Subscribes to events from Command service
- Denormalized data for read performance
- Separate from Executive Command Service for write optimization

## Cache Strategy

- KPIs are cached in Redis for 5 minutes (configurable)
- Cache is invalidated when updates occur
- Cache key format: `kpi:{tenantId}:{kpiId}`
- Summary queries are cached separately

## Testing

```bash
# Run tests with coverage
npm test

# Run tests in watch mode
npm run test:watch
```

## Coverage

Minimum coverage: 80%
- Branches: 80%
- Functions: 80%
- Lines: 80%
- Statements: 80%

## Architecture

```
┌─────────────┐     ┌──────────────┐     ┌─────────────┐
│  Executive  │────▶│  Executive   │────▶│   MongoDB   │
│  Dashboard  │     │  Query       │     │             │
│  (Frontend) │     │  Service     │     │             │
└─────────────┘     └──────────────┘     └─────────────┘
                           │
                           ▼
                    ┌──────────────┐
                    │    Redis     │
                    │    Cache     │
                    └──────────────┘
                           ▲
                           │
                    ┌──────────────┐
                    │  Executive   │
                    │  Command     │
                    │  Service     │
                    └──────────────┘
```

## License

PROPRIETARY - Gogidix
