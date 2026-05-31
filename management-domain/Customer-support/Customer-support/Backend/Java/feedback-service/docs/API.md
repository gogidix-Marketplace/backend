# Feedback Service - API Documentation

## Base URL

```
/api/v1/feedback
```

## Authentication

Headers required:
- `X-Tenant-ID`: Your tenant identifier
- `X-Correlation-ID`: Unique correlation ID

---

## Feedback Endpoints

### Submit Feedback

```http
POST /api/v1/feedback
```

**Request Body:**
```json
{
  "customerId": "customer-123",
  "ticketId": "ticket-456",
  "rating": 4,
  "comment": "Great support!",
  "category": "COMPLIMENT",
  "channel": "EMAIL"
}
```

**Response:** `201 Created`

### Get Feedback Analytics

```http
GET /api/v1/feedback/analytics?startDate=2024-01-01&endDate=2024-01-31
```

**Response:** `200 OK`
```json
{
  "averageRating": 4.2,
  "totalFeedback": 150,
  "byCategory": {
    "COMPLIMENT": 80,
    "COMPLAINT": 20,
    "SUGGESTION": 50
  },
  "byChannel": {
    "EMAIL": 60,
    "CHAT": 50,
    "PHONE": 40
  }
}
```

---

## CSAT Survey Endpoints

### Create CSAT Survey

```http
POST /api/v1/csatsurveys
```

**Request Body:**
```json
{
  "ticketId": "ticket-789",
  "customerId": "customer-123",
  "questions": [
    {
      "question": "How satisfied were you with our support?",
      "type": "RATING",
      "required": true
    }
  ]
}
```

**Response:** `201 Created`

### Submit Survey Response

```http
POST /api/v1/csatsurveys/{surveyId}/respond
```

**Request Body:**
```json
{
  "responses": [
    {
      "questionId": "q1",
      "rating": 5
    }
  ]
}
```

---

## NPS Endpoints

### Record NPS Score

```http
POST /api/v1/nps
```

**Request Body:**
```json
{
  "customerId": "customer-123",
  "score": 9,
  "reason": "Excellent service"
}
```

**Response:** `201 Created`

### Get Current NPS

```http
GET /api/v1/nps/score
```

**Response:** `200 OK`
```json
{
  "npsScore": 42,
  "promoters": 60,
  "passives": 20,
  "detractors": 20,
  "totalResponses": 100
}
```

---

## Error Responses

All errors follow this format:

```json
{
  "timestamp": "2024-01-15T10:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/feedback"
}
```
