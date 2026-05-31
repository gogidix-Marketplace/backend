# AI Recommendation Service - Service Contract

## Service Responsibility
AI-powered product and content recommendations.

## Core Functionality
- Product recommendations
- Content recommendations
- Collaborative filtering
- Real-time personalization

## API Contracts

### 1. Get Recommendations
**Endpoint:** `POST /api/v1/recommendations/generate`

**Input:**
```json
{
  "userId": "string",
  "context": "HOMEPAGE|PRODUCT_PAGE|CHECKOUT",
  "limit": "integer",
  "filters": {"category": "string", "priceRange": {"min": "number", "max": "number"}}
}
```

**Output:**
```json
{
  "recommendationId": "string (UUID)",
  "items": [{
    "itemId": "string",
    "score": "float (0-1)",
    "reason": "string",
    "position": "integer"
  }],
  "algorithm": "string"
}
```

### 2. Provide Feedback
**Endpoint:** `POST /api/v1/recommendations/feedback`

## Business Rules
- Min score: 0.3
- Max recommendations: 100
- Real-time updates: enabled

## Error Conditions
- User not found (404)
