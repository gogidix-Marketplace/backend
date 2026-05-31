# Research Intelligence Service - Service Contract

## Service Responsibility
AI-powered market research and competitive intelligence.

## Core Functionality
- Market analysis
- Competitor tracking
- Trend identification
- Research reports

## API Contracts

### 1. Generate Market Report
**Endpoint:** `POST /api/v1/research/market-report`

**Input:**
```json
{
  "industry": "string",
  "region": "string",
  "timeframe": "string",
  "includeCompetitors": "boolean"
}
```

**Output:**
```json
{
  "reportId": "string (UUID)",
  "status": "GENERATING",
  "marketSize": "number",
  "growthRate": "float",
  "keyTrends": [],
  "competitors": []
}
```

### 2. Track Competitor
**Endpoint:** `POST /api/v1/research/competitors/track`

## Business Rules
- Report generation: up to 5 minutes
- Competitor data refresh: daily
- Market data retention: 1 year

## Error Conditions
- Invalid industry (400)
- Data unavailable (404)
