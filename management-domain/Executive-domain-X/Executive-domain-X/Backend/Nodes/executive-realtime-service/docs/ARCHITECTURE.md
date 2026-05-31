# Executive Realtime Service - Architecture Documentation

## Overview
The Executive Realtime Service provides real-time data streaming and updates for executive dashboards using WebSockets and server-sent events.

## Technology Stack
- **Runtime**: Node.js 18+
- **Framework**: Express.js
- **WebSocket**: Socket.io
- **Message Queue**: Kafka consumer
- **Database**: MongoDB

## Architecture

```
WebSocket Clients <---> Socket.io Server <---> Kafka Topics <---> Domain Events
```

## Key Features
- Real-time KPI updates
- Live dashboard notifications
- Event streaming from Kafka
- Connection management
- Reconnection logic

## API Endpoints
- GET /health - Health check
- GET /api/v1/realtime/status - Connection status
- WS /socket.io/ - WebSocket endpoint
