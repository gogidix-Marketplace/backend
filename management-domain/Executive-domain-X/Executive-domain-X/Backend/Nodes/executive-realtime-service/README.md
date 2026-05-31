# Executive Real-time Service

Real-time WebSocket service for executive dashboards with live KPI updates.

## Features

- **WebSocket Server** for real-time bidirectional communication
- **Redis Pub/Sub** for distributed message broadcasting
- **JWT Authentication** for tenant isolation and security
- **Room-based Subscriptions** for filtered updates
- **Rate Limiting** for connection management
- **Health Monitoring** and metrics

## Technology Stack

- Node.js >= 18.0.0
- WebSocket (ws)
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
- `PORT` - Server port (default: 3003)
- `REDIS_URL` - Redis connection string
- `REDIS_DB` - Redis database number (default: 1)
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

## WebSocket Protocol

### Connection

Connect to: `ws://localhost:3003/ws`

### Authentication

Send authentication message after connection:

```json
{
  "type": "auth",
  "token": "your-jwt-token"
}
```

Response:
```json
{
  "type": "authenticated",
  "tenantId": "tenant-123",
  "userId": "user-456",
  "executiveLevel": "CEO"
}
```

### Subscribing to Rooms

```json
{
  "type": "subscribe",
  "rooms": [
    "tenant:{tenantId}",
    "tenant:{tenantId}:{category}",
    "tenant:{tenantId}:{executiveLevel}"
  ]
}
```

### Real-time Updates

KPI Update:
```json
{
  "type": "kpi_update",
  "data": {
    "kpiId": "kpi-123",
    "tenantId": "tenant-123",
    "value": 1000000,
    "category": "FINANCIAL"
  },
  "timestamp": 1641234567890
}
```

KPI Alert:
```json
{
  "type": "kpi_alert",
  "data": {
    "kpiId": "kpi-456",
    "level": "CRITICAL",
    "message": "Revenue dropped below threshold"
  },
  "timestamp": 1641234567890
}
```

## API Endpoints

### GET /health
Health check endpoint

### GET /metrics
Server metrics and statistics

### GET /api/status
WebSocket server status

### POST /api/broadcast
Broadcast message to clients (internal)

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

## Room Naming Convention

- `tenant:{tenantId}` - All updates for tenant
- `tenant:{tenantId}:{category}` - Category-specific updates (FINANCIAL, OPERATIONAL, etc.)
- `tenant:{tenantId}:{executiveLevel}` - Executive level updates (CEO, CFO, CTO, COO)
- `tenant:{tenantId}:dashboard:{type}` - Dashboard-specific updates

## Message Types

| Type | Direction | Description |
|------|-----------|-------------|
| welcome | Server → Client | Welcome message with client ID |
| auth | Client → Server | Authentication with JWT |
| authenticated | Server → Client | Authentication success |
| subscribe | Client → Server | Subscribe to rooms |
| unsubscribe | Client → Server | Unsubscribe from rooms |
| subscribed | Server → Client | Subscription confirmation |
| kpi_update | Server → Client | KPI value update |
| kpi_alert | Server → Client | KPI alert notification |
| dashboard_update | Server → Client | Dashboard data update |
| ping | Client → Server | Connection health check |
| pong | Server → Client | Pong response |
| error | Server → Client | Error notification |

## Security

- JWT authentication required for all operations
- Tenant isolation enforced at room level
- Rate limiting: 100 messages/minute per connection
- Connection timeout: 30 seconds for authentication

## Architecture

```
┌─────────────────┐     ┌──────────────────┐     ┌─────────────────┐
│  Executive      │────▶│  Executive       │────▶│  Redis Pub/Sub  │
│  Dashboard      │ WS  │  Real-time       │     │                 │
│  (Frontend)     │     │  Service         │     │                 │
└─────────────────┘     └──────────────────┘     └─────────────────┘
                                │
                                ▼
                         ┌──────────────────┐
                         │  Executive       │
                         │  Analytics       │
                         │  Service         │
                         └──────────────────┘
```

## License

PROPRIETARY - Gogidix
