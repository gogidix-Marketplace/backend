# AI Inference Service - Service Contract

## Service Responsibility
Executes ML models for real-time predictions and batch inference.

## Core Functionality
- Model inference
- Batch prediction
- Model loading/unloading
- Inference optimization

## API Contracts

### 1. Run Inference
**Endpoint:** `POST /api/v1/inference/predict`

**Input:**
```json
{
  "modelId": "string (UUID)",
  "inputData": {},
  "options": {
    "batch": "boolean",
    "timeout": "integer"
  }
}
```

**Output:**
```json
{
  "inferenceId": "string (UUID)",
  "predictions": [{}],
  "executedAt": "datetime",
  "latency": "integer (ms)"
}
```

### 2. Get Model Status
**Endpoint:** `GET /api/v1/inference/models/{modelId}/status`

## Business Rules
- Max inference timeout: 30 seconds
- Max batch size: 1000
- Model cache timeout: 1 hour

## Error Conditions
- Model not loaded (404)
- Invalid input data (400)
- Inference timeout (408)
