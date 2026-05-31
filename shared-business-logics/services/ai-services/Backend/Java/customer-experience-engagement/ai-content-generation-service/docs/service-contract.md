# AI Content Generation Service - Service Contract

## Service Responsibility
Generates marketing, product, and communication content using AI.

## Core Functionality
- Text generation
- Image generation
- Content templates
- Brand voice adaptation

## API Contracts

### 1. Generate Content
**Endpoint:** `POST /api/v1/content/generate`

**Input:**
```json
{
  "contentType": "PRODUCT_DESCRIPTION|EMAIL|SOCIAL_POST|BLOG",
  "prompt": "string",
  "tone": "PROFESSIONAL|CASUAL|FRIENDLY",
  "length": "SHORT|MEDIUM|LONG",
  "keywords": ["string"]
}
```

**Output:**
```json
{
  "contentId": "string (UUID)",
  "generatedContent": "string",
  "wordCount": "integer",
  "qualityScore": "float (0-1)"
}
```

### 2. Get Content Templates
**Endpoint:** `GET /api/v1/content/templates`

## Business Rules
- Max generation length: 5000 words
- Min quality threshold: 0.7
- Content moderation: enabled

## Error Conditions
- Invalid prompt (400)
- Generation timeout (408)
