# AI Prediction Service - Service Contract

## Service Responsibility
Generates predictions using trained ML models for various use cases.

## Core Functionality
- Model-based predictions
- Batch prediction
- Real-time inference
- Prediction result caching

## API Contracts

### 1. Generate Prediction
**Endpoint:** `POST /api/v1/predictions/generate`

**Input:**
```json
{
  "modelId": "string",
  "modelVersion": "string",
  "inputData": {},
  "options": {}
}
```

**Output:**
```json
{
  "predictionId": "string (UUID)",
  "result": {},
  "confidence": "float (0-1)",
  "generatedAt": "datetime"
}
```

### 2. Batch Prediction
**Endpoint:** `POST /api/v1/predictions/batch`

## Business Rules
- Max prediction time: 30 seconds
- Cache results for 1 hour
- Minimum confidence threshold: 0.1

## Error Conditions
- Model not found (404)
- Invalid input format (400)
- Prediction timeout (408)
