# AI Feature Extraction Service - Service Contract

## Service Responsibility
Extracts features from raw data for ML model consumption.

## Core Functionality
- Feature extraction
- Feature transformation
- Feature selection
- Feature store integration

## API Contracts

### 1. Extract Features
**Endpoint:** `POST /api/v1/features/extract`

**Input:**
```json
{
  "dataSource": "string",
  "extractionConfig": {
    "features": ["string"],
    "methods": ["TFIDF|PCA|AUTOENCODER"]
  }
}
```

**Output:**
```json
{
  "featureSetId": "string (UUID)",
  "features": [{"name": "string", "value": "number"}],
  "extractedAt": "datetime"
}
```

### 2. Get Feature Schema
**Endpoint:** `GET /api/v1/features/schema/{featureSetId}`

## Business Rules
- Max features per extraction: 1000
- Feature normalization: enabled by default
- Cache extracted features: 1 hour

## Error Conditions
- Invalid data source (400)
- Extraction timeout (408)
