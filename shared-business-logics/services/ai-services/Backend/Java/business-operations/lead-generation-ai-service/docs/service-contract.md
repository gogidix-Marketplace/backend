# Lead Generation AI Service - Service Contract

## Service Responsibility
AI-powered lead generation and scoring for sales.

## Core Functionality
- Lead scoring
- Lead qualification
- Prospect identification
- Campaign optimization

## API Contracts

### 1. Score Lead
**Endpoint:** `POST /api/v1/leads/score`

**Input:**
```json
{
  "prospectId": "string",
  "attributes": {
    "company": "string",
    "industry": "string",
    "size": "string",
    "contacts": []
  },
  "behavior": []
}
```

**Output:**
```json
{
  "leadId": "string",
  "score": "integer (0-100)",
  "tier": "HOT|WARM|COLD",
  "likelihood": "float (0-1)",
  "nextActions": ["string"]
}
```

### 2. Get Lead Recommendations
**Endpoint:** `GET /api/v1/leads/recommendations`

## Business Rules
- Score range: 0-100
- Hot threshold: score >= 70
- Cold threshold: score <= 30
- Score refresh: daily

## Error Conditions
- Invalid prospect data (400)
