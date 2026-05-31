# Executive Command Service

Command service for executive operations - handles all write operations (CQRS pattern) for KPIs, metrics, and executive configurations.

## Features

- **REST API** for KPI and metric write operations
- **MongoDB** for data persistence
- **Redis** for caching and event publishing
- **JWT Authentication** for tenant isolation
- **Command Validation** with express-validator
- **Event Publishing** for real-time updates
- **Batch Operations** support

## Technology Stack

- Node.js >= 18.0.0
- Express.js
- MongoDB
- Redis (ioredis)
- JWT (jsonwebtoken)
- Joi/express-validator (validation)
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
- `PORT` - Server port (default: 3004)
- `MONGODB_URL` - MongoDB connection string
- `MONGODB_DB` - Database name (default: management_executive)
- `REDIS_URL` - Redis connection string
- `REDIS_DB` - Redis database number (default: 2)
- `JWT_SECRET` - JWT signing secret
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

### POST /api/kpi
Create a new KPI

**Request:**
```json
{
  "name": "Total Revenue",
  "category": "FINANCIAL",
  "executiveLevel": "CEO",
  "value": 1000000,
  "unit": "$",
  "period": "2024-01",
  "target": 1200000,
  "previousValue": 900000
}
```

**Response:** 201 Created
```json
{
  "success": true,
  "data": {
    "_id": "...",
    "tenantId": "tenant-123",
    "name": "Total Revenue",
    "category": "FINANCIAL",
    "value": 1000000,
    "percentChange": 11.11,
    "status": "ON_TRACK",
    "trend": "STABLE"
  }
}
```

### PUT /api/kpi/:id
Update an existing KPI

### DELETE /api/kpi/:id
Delete a KPI

### POST /api/kpi/batch
Create multiple KPIs in batch

**Request:**
```json
{
  "kpis": [
    { "name": "Revenue", "category": "FINANCIAL", "value": 100, "unit": "$", "period": "2024-01" },
    { "name": "Users", "category": "CUSTOMER", "value": 500, "unit": "count", "period": "2024-01" }
  ]
}
```

### POST /api/kpi/:id/recalculate
Recalculate KPI value with automatic percent change and status update

**Request:**
```json
{
  "value": 1500000,
  "metadata": {
    "source": "external",
    "calculatedAt": "2024-01-15T10:00:00Z"
  }
}
```

## KPI Categories

- `FINANCIAL` - Revenue, profit, costs
- `OPERATIONAL` - Efficiency, productivity
- `CUSTOMER` - Satisfaction, retention
- `EMPLOYEE` - Engagement, turnover

## Executive Levels

- `CEO` - Chief Executive Officer (all KPIs)
- `CFO` - Chief Financial Officer (financial KPIs)
- `CTO` - Chief Technology Officer (technical KPIs)
- `COO` - Chief Operating Officer (operational KPIs)
- `ALL` - All levels

## KPI Status

- `AHEAD` - Above target (>= 110%)
- `ON_TRACK` - Within range (90-110%)
- `AT_RISK` - Below target (80-90%)
- `BEHIND` - Significantly below (< 80%)

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

## Events Published

The service publishes events to Redis channels:

- `kpi:created` - New KPI created
- `kpi:updated` - KPI updated
- `kpi:deleted` - KPI deleted

Subscribers (like the real-time service) can listen to these events for live updates.

## Architecture

```
┌─────────────┐     ┌──────────────┐     ┌─────────────┐
│  Executive  │────▶│  Executive   │────▶│   MongoDB   │
│  Dashboard  │     │  Command     │     │             │
│  (Frontend) │     │  Service     │     │             │
└─────────────┘     └──────────────┘     └─────────────┘
                           │
                           ▼
                    ┌──────────────┐
                    │    Redis     │
                    │   Pub/Sub    │
                    └──────────────┘
                           │
                           ▼
                    ┌──────────────┐
                    │  Executive   │
                    │  Real-time   │
                    │   Service    │
                    └──────────────┘
```

## CQRS Pattern

This service is the **Command** side of CQRS:
- Handles all write operations (CREATE, UPDATE, DELETE)
- Validates commands
- Publishes events for the Query side to consume
- Separate from Executive Query Service for read optimization

## Rate Limiting

- API requests: 100 requests/minute
- Write operations: 20 requests/minute

## Security

- JWT authentication required for all API endpoints
- Tenant isolation enforced at service level
- Input validation on all endpoints
- Rate limiting per tenant

## License

PROPRIETARY - Gogidix
