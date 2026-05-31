# Gogidix WebSocket Service

Real-time WebSocket server for the Executive Dashboard domain, providing live updates for KPIs, dashboards, and notifications.

## Features

- **WebSocket Server**: Built with Socket.io for real-time bidirectional communication
- **Room/Channel Management**: Organize connections into rooms for targeted broadcasts
- **Connection Lifecycle Management**: Handle connection, disconnection, and reconnection
- **Authentication & Authorization**: JWT-based authentication with role-based access control
- **Message Broadcasting**: Send messages to rooms, users, or globally
- **Presence Management**: Track user online/offline status and typing indicators
- **Multi-Tenant Isolation**: Complete tenant separation for all operations
- **Kafka Integration**: Event broadcasting via Kafka for cross-service communication
- **Redis Pub/Sub**: Scalable message distribution across server instances

## Architecture

```
websocket-service/
├── src/
│   ├── config/           # Configuration (Redis, MongoDB, Kafka, Logger)
│   ├── middleware/       # Authentication, validation, rate limiting
│   ├── models/          # Mongoose models
│   ├── services/        # Business logic (Room, Presence, Broadcast, Kafka)
│   ├── controllers/     # HTTP endpoint handlers
│   ├── routes/          # HTTP route definitions
│   ├── websocket/       # Socket.IO server and handler
│   └── tests/           # Unit and integration tests
├── package.json
└── .env.example         # Environment variables template
```

## Installation

```bash
npm install
```

## Configuration

Copy `.env.example` to `.env` and configure:

```bash
cp .env.example .env
```

Key environment variables:

- `PORT` - HTTP server port (default: 4000)
- `WS_PORT` - WebSocket port (default: 4001)
- `REDIS_HOST` - Redis server host
- `MONGODB_URI` - MongoDB connection string
- `KAFKA_BROKERS` - Comma-separated Kafka broker addresses
- `JWT_SECRET` - Secret for JWT verification

## Running

### Development

```bash
npm run dev
```

### Production

```bash
npm start
```

### Testing

```bash
npm test              # Run tests once
npm run test:watch    # Run tests in watch mode
npm run lint          # Check code style
```

## API Reference

### Health & Metrics

- `GET /` - Service information
- `GET /health` - Health check status
- `GET /health/ready` - Readiness probe
- `GET /health/live` - Liveness probe
- `GET /metrics` - Service metrics
- `GET /connections` - Active connections

### Broadcast Endpoints

- `POST /broadcast/room` - Broadcast to a room
- `POST /broadcast/user` - Send to a specific user
- `POST /broadcast/tenant` - Broadcast to all tenant users
- `POST /broadcast/global` - Global broadcast
- `POST /broadcast/kpi` - Send KPI update
- `POST /broadcast/dashboard` - Send dashboard update
- `POST /broadcast/notification` - Send notification

### Room Management

- `POST /rooms` - Create a new room
- `GET /rooms` - List rooms
- `GET /rooms/:roomId` - Get room details
- `PUT /rooms/:roomId` - Update room metadata
- `DELETE /rooms/:roomId` - Delete a room
- `GET /rooms/:roomId/users` - Get room users
- `GET /rooms/:roomId/presence` - Get room presence

### Presence

- `GET /presence/:tenantId/:userId` - Get user presence
- `GET /presence/:tenantId/users/online` - Get online users
- `GET /presence/:tenantId/users/count` - Get online count
- `GET /presence/:tenantId/stats` - Get presence statistics

## WebSocket Events

### Client -> Server

- `room:join` - Join a room
- `room:leave` - Leave a room
- `room:create` - Create a new room
- `room:users` - Get users in a room
- `message:send` - Send a message
- `message:broadcast` - Broadcast a message
- `presence:update` - Update presence status
- `presence:typing` - Send typing indicator
- `dashboard:subscribe` - Subscribe to dashboard updates
- `dashboard:unsubscribe` - Unsubscribe from dashboard
- `kpi:subscribe` - Subscribe to KPI updates
- `kpi:unsubscribe` - Unsubscribe from KPI

### Server -> Client

- `connected` - Connection established
- `room:joined` - Successfully joined a room
- `room:left` - Successfully left a room
- `room:user_joined` - User joined a room
- `room:user_left` - User left a room
- `presence:change` - User presence changed
- `presence:typing` - Typing indicator
- `dashboard:update` - Dashboard data updated
- `kpi:update` - KPI data updated
- `notification:new` - New notification
- `error` - Error occurred

## Authentication

WebSocket connections require a JWT token. Include it in:

1. Handshake auth: `socket = io({ auth: { token } })`
2. Query string: `socket = io({ query: { token } })`
3. Authorization header: `socket = io({ transportOptions: { polling: { extraHeaders: { Authorization } } } })`

## Multi-Tenancy

All rooms and operations are tenant-isolated. Room names are prefixed with `tenant:{tenantId}:`.

Room patterns:
- `tenant:{tenantId}:user:{userId}` - User-specific room
- `tenant:{tenantId}:dashboard:{dashboardId}` - Dashboard room
- `tenant:{tenantId}:kpi:{kpiId}` - KPI room
- `tenant:{tenantId}:notifications:{userId}` - Notification room

## License

PROPRIETARY
