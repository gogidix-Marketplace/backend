# AI Search Service - Service Contract

## Service Responsibility
AI-powered semantic search across products and content.

## Core Functionality
- Semantic search
- Auto-complete
- Search analytics
- Query understanding

## API Contracts

### 1. Search
**Endpoint:** `POST /api/v1/search/query`

**Input:**
```json
{
  "query": "string",
  "filters": {"category": "string", "priceRange": {}, "attributes": {}},
  "sortBy": "RELEVANCE|PRICE|NEWEST",
  "limit": "integer"
}
```

**Output:**
```json
{
  "searchId": "string (UUID)",
  "results": [{
    "itemId": "string",
    "score": "float (0-1)",
    "highlights": ["string"]
  }],
  "totalCount": "integer",
  "suggestions": ["string"]
}
```

### 2. Get Suggestions
**Endpoint:** `GET /api/v1/search/suggest`

## Business Rules
- Max results: 100
- Query expansion: enabled
- Typo tolerance: 2 edits

## Error Conditions
- Invalid query (400)
