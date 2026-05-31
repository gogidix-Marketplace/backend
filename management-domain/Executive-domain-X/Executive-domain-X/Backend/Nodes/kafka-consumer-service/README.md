# Kafka Consumer Service

A Node.js service that consumes and processes events from Kafka for the executive domain.

## Features

- Consumes events from Kafka topics
- Processes executive domain events (executives, KPIs, strategies, approvals, decisions)
- Caches processed data in Redis
- Stores event logs in MongoDB
- REST API for monitoring and management
- Comprehensive error handling and retry mechanism

## Tech Stack

- Node.js 18+
- Express.js for REST APIs
- MongoDB for persistence
- Redis for caching
- Kafka for messaging
- Winston for logging
- Jest for testing

## Getting Started

### Prerequisites

- Node.js 18 or higher
- MongoDB running on port 27017
- Redis running on port 6379
- Kafka broker running on port 9092

### Installation

1. Clone the repository
```bash
git clone <repository-url>
cd kafka-consumer-service
```

2. Install dependencies
```bash
npm install
```

3. Set up environment variables
```bash
cp .env.example .env
# Edit .env with your configuration
```

4. Start the service
```bash
npm start
```

The service will start on port 3003 by default.

### Development

```bash
npm run dev  # Start with nodemon for auto-reload
npm test     # Run tests
npm run test:watch  # Run tests in watch mode
```

## API Endpoints

### Health Check
- `GET /api/health` - Check service health status

### Event Management
- `GET /api/events/stats` - Get event processing statistics
- `POST /api/events/register` - Register event handler (for testing)

## Event Types

The service processes the following event types:

- `EXECUTIVE_CREATED` - When a new executive is created
- `EXECUTIVE_UPDATED` - When executive information is updated
- `KPI_UPDATED` - When KPI values are updated
- `STRATEGY_UPDATED` - When executive strategies are updated
- `APPROVAL_CREATED` - When new approval requests are created
- `DECISION_MADE` - When decisions are made on approvals

## Configuration

Environment variables (see .env.example):

- `PORT` - Service port (default: 3003)
- `MONGODB_URI` - MongoDB connection string
- `REDIS_HOST` - Redis host (default: localhost)
- `KAFKA_BROKERS` - Kafka brokers comma-separated list
- `LOG_LEVEL` - Logging level (default: info)

## Architecture

```
src/
├── config/           # Configuration modules
│   ├── logger.js    # Winston logger configuration
│   ├── mongodb.js   # MongoDB connection
│   ├── redis.js     # Redis connection
│   └── kafka.js     # Kafka consumer configuration
├── models/          # Data models
│   └── EventLog.js  # Event log model
├── controllers/     # Request handlers
│   └── EventController.js
├── services/        # Business logic
│   └── EventProcessor.js
├── routes/          # API routes
│   └── index.js
├── middleware/     # Express middleware
│   ├── errorHandler.js
│   └── requestLogger.js
└── server.js        # Main server file
```

## Error Handling

The service implements comprehensive error handling:

- Validates event types and data
- Retries failed events
- Logs all errors with stack traces
- Provides graceful shutdown

## Testing

Run tests with:
```bash
npm test
```

Tests cover:
- Configuration loading
- Event processing
- API endpoints
- Error scenarios
- Integration with mock databases

## Monitoring

The service provides metrics for:
- Event processing statistics
- Success/failure rates
- Recent events processed

## Troubleshooting

### Common Issues

1. **Kafka connection failed**
   - Check if Kafka broker is running
   - Verify KAFKA_BROKERS configuration
   - Check network connectivity

2. **MongoDB connection failed**
   - Ensure MongoDB is running
   - Verify MONGODB_URI
   - Check authentication credentials

3. **Redis connection failed**
   - Verify Redis is running
   - Check REDIS_HOST and REDIS_PORT

### Logs

Logs are written to:
- `logs/combined.log` - All logs
- `logs/error.log` - Error logs only

## License

Proprietary - Gogidix