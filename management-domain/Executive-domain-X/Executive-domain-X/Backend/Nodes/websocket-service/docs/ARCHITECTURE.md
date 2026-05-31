# WebSocket Service - Architecture Documentation

## Overview
The WebSocket Service manages persistent WebSocket connections for real-time executive notifications.

## Technology Stack
- **Runtime**: Node.js 18+
- **WebSocket**: Native WebSocket API
- **State Management**: In-memory + Redis pub/sub

## Architecture

```
Clients <---> WebSocket Server <---> Redis Pub/Sub <---> Event Publishers
```

## Key Features
- Persistent connections
- Room-based broadcasting
- User presence tracking
- Message acknowledgments
