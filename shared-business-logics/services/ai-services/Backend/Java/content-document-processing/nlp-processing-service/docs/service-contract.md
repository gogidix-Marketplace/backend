# NLP Processing Service - Service Contract

## Service Responsibility
Natural Language Processing for text analysis and understanding.

## Core Functionality
- Text analysis
- Entity recognition
- Text classification
- Language detection
- Summarization

## API Contracts

### 1. Analyze Text
**Endpoint:** `POST /api/v1/nlp/analyze`

**Input:**
```json
{
  "text": "string",
  "language": "AUTO|EN|ES|FR|DE",
  "features": {
    "entities": "boolean",
    "sentiment": "boolean",
    "categories": "boolean",
    "keywords": "boolean"
  }
}
```

**Output:**
```json
{
  "analysisId": "string (UUID)",
  "language": "string",
  "entities": [{"text": "string", "type": "PERSON|ORG|LOCATION", "confidence": "float"}],
  "sentiment": {"score": "float", "label": "POSITIVE|NEGATIVE|NEUTRAL"},
  "categories": [{"label": "string", "confidence": "float"}],
  "keywords": [{"text": "string", "relevance": "float"}]
}
```

### 2. Summarize Text
**Endpoint:** `POST /api/v1/nlp/summarize`

## Business Rules
- Max text length: 100,000 characters
- Min summary length: 10% of original
- Supported languages: 50+
- Processing timeout: 30 seconds

## Error Conditions
- Text too long (400)
- Unsupported language (400)
