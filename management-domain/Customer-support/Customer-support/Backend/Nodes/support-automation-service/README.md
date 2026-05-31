# Gogidix Customer Support Automation Services

A suite of production-ready Node.js microservices for intelligent customer support automation.

## Services Overview

### 1. Ticket Routing Service (P0) - Port 3001
Intelligent ticket routing with multiple strategies:
- **Round-robin**: Distributes tickets evenly among agents
- **Least-busy**: Routes to agents with lowest current load
- **Skills-based**: Matches tickets to agents based on required skills
- **Priority-based**: Considers both ticket priority and agent availability

Features:
- Agent availability tracking with heartbeat monitoring
- Priority-based queue management
- Ticket reassignment capabilities
- Agent statistics and performance tracking

### 2. Chatbot Service (P1) - Port 3002
AI-powered chatbot for customer support:
- Intent recognition using NLP
- Context-aware multi-turn conversations
- Multi-language support
- Knowledge base integration
- Smart handoff to human agents

Features:
- 12 supported intents (greeting, goodbye, FAQ, billing, technical, etc.)
- Entity extraction (order numbers, emails, phone numbers)
- Conversation context management
- Suggested responses

### 3. Sentiment Analysis Service (P1) - Port 3003
Real-time sentiment and emotion analysis:
- Sentiment detection (positive, negative, neutral)
- Emotion detection (joy, sadness, anger, fear, disgust, surprise)
- Multi-language support
- Batch processing capabilities
- Result caching

## Quick Start

### Prerequisites
- Docker and Docker Compose
- Node.js 18 LTS (for local development)

### Using Docker Compose (Recommended)

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

### Local Development

Each service can be run independently:

```bash
# Ticket Routing Service
cd ticket-routing-service
npm install
npm run dev

# Chatbot Service
cd chatbot-service
npm install
npm run dev

# Sentiment Analysis Service
cd sentiment-analysis-service
npm install
npm run dev
```

## API Endpoints

### Ticket Routing Service (http://localhost:3001)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/agents` | Create a new agent |
| GET | `/api/agents` | Get all agents |
| GET | `/api/agents/available` | Get available agents |
| POST | `/api/tickets` | Create a new ticket |
| POST | `/api/tickets/:id/route` | Route ticket to agent |
| POST | `/api/tickets/:id/reassign` | Reassign ticket |
| GET | `/api/queue` | Get queued tickets |
| POST | `/api/queue/process` | Process queue |
| GET | `/api/routing/stats` | Get routing statistics |

### Chatbot Service (http://localhost:3002)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/chatbot/conversations` | Start conversation |
| POST | `/api/chatbot/conversations/:id/message` | Send message |
| GET | `/api/chatbot/conversations/:id/history` | Get history |
| POST | `/api/knowledge/search` | Search knowledge base |
| GET | `/api/knowledge/categories` | Get KB categories |
| GET | `/api/chatbot/stats` | Get statistics |

### Sentiment Analysis Service (http://localhost:3003)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/sentiment/analyze` | Analyze sentiment + emotions |
| POST | `/api/sentiment/sentiment-only` | Sentiment only |
| POST | `/api/sentiment/emotions-only` | Emotions only |
| POST | `/api/sentiment/batch` | Batch analysis |
| DELETE | `/api/sentiment/cache` | Clear cache |
| GET | `/api/sentiment/stats` | Get statistics |

## Architecture

```
+-------------------+     +-------------------+     +---------------------+
| Ticket Routing    |<--->|                   |<--->| Chatbot Service     |
| Service (3001)    |     |   Redis (6379)    |     | (3002)              |
+-------------------+     |                   |     +---------------------+
                          +-------------------+
                                     ^
                                     |
                          +---------------------+
                          | Sentiment Analysis  |
                          | Service (3003)      |
                          +---------------------+
```

## Configuration

Each service has its own `.env` configuration:

- `ticket-routing-service/.env.example`
- `chatbot-service/.env.example`
- `sentiment-analysis-service/.env.example`

Copy the example file to `.env` and adjust values as needed.

## Technology Stack

- **Runtime**: Node.js 18 LTS
- **Framework**: Express.js
- **Language**: TypeScript
- **Database**: Redis
- **NLP**: Natural, node-nlp, Sentiment
- **Logging**: Winston
- **Validation**: Joi
- **Container**: Docker

## Development

### Build for Production

```bash
cd ticket-routing-service
npm run build
npm start
```

### Running Tests

```bash
cd ticket-routing-service
npm test
```

### Code Structure

```
support-automation-service/
├── ticket-routing-service/
│   ├── src/
│   │   ├── config/         # Configuration (logger, redis)
│   │   ├── middleware/     # Express middleware
│   │   ├── routes/         # API routes
│   │   ├── services/       # Business logic
│   │   ├── types/          # TypeScript types
│   │   └── index.ts        # Entry point
│   ├── package.json
│   ├── tsconfig.json
│   ├── Dockerfile
│   └── README.md
├── chatbot-service/
│   └── (similar structure)
├── sentiment-analysis-service/
│   └── (similar structure)
└── docker-compose.yml
```

## Monitoring

Each service exposes a `/health` endpoint:

```bash
curl http://localhost:3001/health
curl http://localhost:3002/health
curl http://localhost:3003/health
```

## License

MIT
