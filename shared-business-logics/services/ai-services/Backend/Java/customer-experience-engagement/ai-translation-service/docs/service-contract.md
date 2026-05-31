# AI Translation Service - Service Contract

## Service Responsibility
AI-powered translation and localization.

## Core Functionality
- Text translation
- Context-aware translation
- Localization
- Translation memory

## API Contracts

### 1. Translate Text
**Endpoint:** `POST /api/v1/translation/translate`

**Input:**
```json
{
  "text": "string",
  "sourceLanguage": "AUTO|EN|ES|FR|DE|ZH",
  "targetLanguage": "EN|ES|FR|DE|ZH",
  "context": "string",
  "preserveFormat": "boolean"
}
```

**Output:**
```json
{
  "translationId": "string (UUID)",
  "translatedText": "string",
  "detectedLanguage": "string",
  "confidence": "float (0-1)"
}
```

### 2. Batch Translation
**Endpoint:** `POST /api/v1/translation/batch`

## Business Rules
- Max text length: 10,000 characters
- Batch size: 100 texts
- Supported languages: 100+

## Error Conditions
- Text too long (400)
- Unsupported language (400)
