# Chatbot Service

An AI-powered chatbot service for customer support with intent recognition, context-aware responses, and seamless handoff to human agents.

## Features

- **Intent Recognition**: NLP-based classification of user messages into predefined intents
- **Context Management**: Maintains conversation context across multiple turns
- **Knowledge Base Integration**: Searches and retrieves relevant articles
- **Multi-language Support**: Built-in support for multiple languages
- **Smart Handoff**: Recognizes when human intervention is needed
- **Entity Extraction**: Extracts key information like order numbers, emails, phone numbers
- **Conversation History**: Tracks full conversation for context and analytics

## Supported Intents

- Greeting & Goodbye
- FAQ (frequently asked questions)
- Support Requests
- Billing Inquiries
- Technical Issues
- Account Access
- Order Status
- Refund Requests
- Complaints
- Feedback

## Installation

```bash
npm install
```

## Configuration

Create a `.env` file based on `.env.example`:

```bash
cp .env.example .env
```

## Running the Service

### Development

```bash
npm run dev
```

### Production

```bash
npm run build
npm start
```

### Docker

```bash
docker build -t chatbot-service .
docker run -p 3002:3002 chatbot-service
```

## API Endpoints

### Conversations
- `POST /api/chatbot/conversations` - Start a new conversation
- `POST /api/chatbot/conversations/:sessionId/message` - Send a message
- `GET /api/chatbot/conversations/:sessionId/history` - Get conversation history
- `GET /api/chatbot/conversations/:sessionId/summary` - Get conversation summary
- `DELETE /api/chatbot/conversations/:sessionId` - End conversation

### Knowledge Base
- `POST /api/knowledge/search` - Search knowledge base
- `GET /api/knowledge/articles/:id` - Get article by ID
- `GET /api/knowledge/articles?category=X` - Get articles by category
- `GET /api/knowledge/categories` - Get all categories
- `GET /api/knowledge/stats` - Get KB statistics

### General
- `GET /api/chatbot/stats` - Get chatbot statistics
- `GET /health` - Health check

## Example Usage

### Start a Conversation

```bash
curl -X POST http://localhost:3002/api/chatbot/conversations \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "cust_123",
    "language": "en"
  }'
```

### Send a Message

```bash
curl -X POST http://localhost:3002/api/chatbot/conversations/{sessionId}/message \
  -H "Content-Type: application/json" \
  -d '{
    "message": "I need help with my order",
    "language": "en"
  }'
```

### Search Knowledge Base

```bash
curl -X POST http://localhost:3002/api/knowledge/search \
  -H "Content-Type: application/json" \
  -d '{
    "query": "return policy",
    "language": "en"
  }'
```

## Handoff Triggers

The chatbot will automatically trigger a handoff to a human agent when:

1. **Low Confidence**: Intent confidence falls below threshold (default: 0.3)
2. **Escalation Keywords**: User mentions manager, supervisor, etc.
3. **Complex Queries**: Refund requests, complaints, etc.
4. **Authentication Required**: Account access attempts
5. **Negative Sentiment**: Detected negative sentiment (requires integration)

## Technologies

- Node.js 18 LTS
- Express.js
- TypeScript
- Natural (NLP library)
- Redis (for context storage)
- Winston (logging)
