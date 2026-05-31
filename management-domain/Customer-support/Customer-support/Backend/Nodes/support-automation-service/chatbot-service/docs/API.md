# Chatbot Service - API Documentation

## Base URL

```
/api/chatbot
```

## Authentication

Headers required:
- `X-Tenant-ID`: Your tenant identifier (optional for anonymous chats)
- `X-Correlation-ID`: Unique correlation ID for tracing

---

## Conversation Endpoints

### Start New Conversation

```http
POST /api/chatbot/conversations
```

**Request Body:**
```json
{
  "customerId": "customer-123",
  "language": "en"
}
```

**Response:** `201 Created`
```json
{
  "success": true,
  "data": {
    "sessionId": "session-abc123",
    "message": "Hello! How can I help you today?"
  }
}
```

### Send Message

```http
POST /api/chatbot/conversations/:sessionId/message
```

**Path Parameters:**
- `sessionId` - The conversation session ID

**Request Body:**
```json
{
  "message": "I need help with my order",
  "customerId": "customer-123",
  "language": "en"
}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "data": {
    "message": "I can help you with your order. Could you please provide your order number?",
    "intent": "ORDER_STATUS",
    "confidence": 0.92,
    "suggestedResponses": [
      "Track my order",
      "Cancel my order",
      "Return an item"
    ]
  }
}
```

**Response with Handoff:**
```json
{
  "success": true,
  "data": {
    "message": "I'm connecting you with a human agent who can better assist you.",
    "intent": "COMPLAINT",
    "confidence": 0.85,
    "handoff": {
      "recommended": true,
      "reason": "COMPLEX_QUERY"
    }
  }
}
```

### Get Conversation History

```http
GET /api/chatbot/conversations/:sessionId/history
```

**Path Parameters:**
- `sessionId` - The conversation session ID

**Response:** `200 OK`
```json
{
  "success": true,
  "data": [
    {
      "sessionId": "session-abc123",
      "type": "USER",
      "content": "I need help with my order",
      "timestamp": "2024-01-15T10:00:00Z"
    },
    {
      "sessionId": "session-abc123",
      "type": "BOT",
      "content": "I can help you with your order...",
      "intent": "ORDER_STATUS",
      "confidence": 0.92,
      "timestamp": "2024-01-15T10:00:01Z"
    }
  ],
  "count": 2
}
```

### End Conversation

```http
DELETE /api/chatbot/conversations/:sessionId
```

**Path Parameters:**
- `sessionId` - The conversation session ID

**Response:** `200 OK`
```json
{
  "success": true,
  "data": {
    "success": true,
    "summary": {
      "sessionId": "session-abc123",
      "messageCount": 5,
      "intents": ["GREETING", "ORDER_STATUS", "GOODBYE"]
    }
  }
}
```

### Get Conversation Summary

```http
GET /api/chatbot/conversations/:sessionId/summary
```

**Path Parameters:**
- `sessionId` - The conversation session ID

**Response:** `200 OK`
```json
{
  "success": true,
  "data": {
    "sessionId": "session-abc123",
    "customerId": "customer-123",
    "language": "en",
    "messageCount": 5,
    "intents": ["GREETING", "ORDER_STATUS", "GOODBYE"],
    "entities": {
      "orderNumber": "ORD-12345"
    },
    "duration": 180,
    "handoffInitiated": false
  }
}
```

---

## Statistics Endpoint

### Get Chatbot Statistics

```http
GET /api/chatbot/stats
```

**Response:** `200 OK`
```json
{
  "success": true,
  "data": {
    "activeConversations": 42,
    "pendingHandoffs": 3,
    "totalMessages": 1520,
    "averageMessagesPerSession": 4.5
  }
}
```

---

## Health Check

### Health Check

```http
GET /api/chatbot/health
```

**Response:** `200 OK`
```json
{
  "success": true,
  "service": "chatbot-service",
  "status": "healthy",
  "timestamp": "2024-01-15T10:00:00Z"
}
```

---

## Error Responses

### 400 Bad Request

```json
{
  "success": false,
  "error": "Validation Error",
  "message": "message is required",
  "details": ["message must not be empty"]
}
```

### 404 Not Found

```json
{
  "success": false,
  "error": "Not Found",
  "message": "Conversation not found"
}
```

### 500 Internal Server Error

```json
{
  "success": false,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred"
}
```

---

## Intent Types

| Intent | Description | Example |
|--------|-------------|---------|
| GREETING | User greeting | "Hello", "Hi there" |
| FAQ | General questions | "What are your hours?" |
| SUPPORT_REQUEST | Help requests | "I need help" |
| BILLING_INQUIRY | Billing questions | "Check my invoice" |
| TECHNICAL_ISSUE | Technical problems | "App not working" |
| ORDER_STATUS | Order tracking | "Where is my order?" |
| REFUND_REQUEST | Refund requests | "I want a refund" |
| FEEDBACK | Customer feedback | "I'd like to give feedback" |
| COMPLAINT | Complaints | "I'm very unhappy" |
| ACCOUNT_ACCESS | Account access | "Access my account" |
| GOODBYE | Conversation ending | "Thanks, bye" |
| UNKNOWN | Unrecognized | [Other inputs] |

---

## Handoff Reasons

| Reason | Description |
|--------|-------------|
| LOW_CONFIDENCE | Intent recognition confidence below threshold |
| COMPLEX_QUERY | Query requires human intervention |
| ESCALATION_REQUEST | User explicitly requested human agent |
| AUTHENTICATION_REQUIRED | Account access needs verification |
| NEGATIVE_SENTIMENT | Negative sentiment detected |

---

## Rate Limiting

- **Limit**: 100 requests per minute per IP
- **Headers Returned**:
  - `X-RateLimit-Limit`: 100
  - `X-RateLimit-Remaining`: Remaining requests
  - `X-RateLimit-Reset`: Reset timestamp
