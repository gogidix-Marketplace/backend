# Chatbot Service

AI-powered customer support chatbot with natural language processing capabilities.

## Features

- **Natural Language Processing**: Intent detection and entity extraction using machine learning
- **Multi-language Support**: Support for 8+ languages with automatic detection
- **Conversation Context Management**: Maintains context across conversation turns
- **Knowledge Base Integration**: Fetches relevant articles from knowledge base
- **Human Handoff**: Seamless transfer to human agents when needed
- **Sentiment Analysis**: Detects customer mood for better response
- **WebSocket Support**: Real-time bidirectional messaging
- **Comprehensive Analytics**: Track sessions, intents, resolution rates

## Technology Stack

- **Runtime**: Node.js 18+
- **Framework**: Express.js
- **Language**: TypeScript
- **Database**: MongoDB with Mongoose ODM
- **Cache**: Redis
- **NLP**: Natural.js, OpenAI GPT
- **Testing**: Jest, Supertest
- **Containerization**: Docker

## Quick Start

### Prerequisites

- Node.js 18+
- MongoDB 6+
- Redis 7+
- OpenAI API key (for GPT features)

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd chatbot-service
```

2. Install dependencies:
```bash
npm install
```

3. Copy environment variables:
```bash
cp .env.example .env
```

4. Configure `.env` file:
```env
PORT=8112
MONGODB_URI=mongodb://localhost:27017/gogidix-chatbot
REDIS_HOST=localhost
REDIS_PORT=6379
JWT_SECRET=your-secret-key
OPENAI_API_KEY=your-openai-api-key
```

5. Start the service:
```bash
npm run dev
```

The service will be available at `http://localhost:8112`

### Docker Deployment

```bash
docker-compose up -d
```

## API Documentation

### Base URL
```
http://localhost:8112/api/v1
```

### Health Check

```http
GET /health
```

### Create Session

```http
POST /chat/sessions
Content-Type: application/json

{
  "customerId": "customer123",
  "language": "en",
  "metadata": {}
}
```

### Send Message

```http
POST /chat/send
Content-Type: application/json

{
  "sessionId": "session_abc123",
  "message": "Where is my order?",
  "language": "en"
}
```

### Get Session Details

```http
GET /chat/sessions/:sessionId
Authorization: Bearer <token>
```

### Request Handoff

```http
POST /chat/sessions/:sessionId/handoff
Authorization: Bearer <token>
Content-Type: application/json

{
  "reason": "Customer needs refund assistance",
  "priority": "high"
}
```

### Detect Intent

```http
POST /intents/detect
Authorization: Bearer <token>
Content-Type: application/json

{
  "message": "I want to return my order",
  "language": "en"
}
```

### Get Analytics

```http
GET /analytics/dashboard
Authorization: Bearer <token>
```

## WebSocket Connection

```javascript
const io = require('socket.io-client');

const socket = io('http://localhost:8112', {
  path: '/socket.io'
});

socket.on('connect', () => {
  console.log('Connected to chatbot service');

  // Join session
  socket.emit('join-session', 'session_abc123');
});

socket.on('message', (data) => {
  console.log('Received:', data);
});

socket.on('typing', (data) => {
  console.log('Typing indicator:', data);
});
```

## Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `PORT` | Service port | `8112` |
| `MONGODB_URI` | MongoDB connection string | - |
| `REDIS_HOST` | Redis host | `localhost` |
| `REDIS_PORT` | Redis port | `6379` |
| `JWT_SECRET` | JWT secret key | - |
| `OPENAI_API_KEY` | OpenAI API key | - |
| `SESSION_TIMEOUT_MS` | Session timeout | `1800000` |
| `HANDOFF_THRESHOLD` | Handoff confidence threshold | `0.3` |

### Intent Categories

- `greeting` - User greetings
- `faq` - Frequently asked questions
- `order_status` - Order tracking
- `product_info` - Product information
- `support` - General support
- `complaint` - Customer complaints
- `refund` - Refund requests
- `billing` - Billing inquiries
- `technical` - Technical support
- `shipping` - Shipping questions
- `returns` - Return requests
- `account` - Account management
- `payment` - Payment issues
- `general` - General queries
- `escalation` - Escalation triggers

## Development

### Available Scripts

```bash
npm run dev          # Start development server
npm run build        # Build TypeScript
npm run test         # Run tests
npm run test:watch   # Watch mode tests
npm run lint         # Lint code
npm run format       # Format code
```

### Project Structure

```
chatbot-service/
├── src/
│   ├── config/         # Configuration files
│   ├── controllers/    # Request handlers
│   ├── middleware/     # Express middleware
│   ├── models/         # Mongoose models
│   ├── routes/         # API routes
│   ├── services/       # Business logic
│   ├── types/          # TypeScript types
│   ├── utils/          # Utility functions
│   └── index.ts        # Entry point
├── tests/              # Test files
├── dist/               # Compiled output
└── package.json
```

## Testing

```bash
# Run all tests
npm test

# Run with coverage
npm test -- --coverage

# Run specific test file
npm test -- chat.test
```

## Monitoring

### Health Endpoints

- `/health` - Basic health check
- `/status` - Detailed system status
- `/readiness` - Kubernetes readiness probe
- `/liveness` - Kubernetes liveness probe

### Metrics

The service exposes metrics for:
- Active sessions
- Message throughput
- Intent detection accuracy
- Handoff rates
- Response times
- Error rates

## License

MIT

## Support

For issues and questions, please contact the Gogidix development team.
