# AI Model Training Service - Service Contract

## Service Responsibility
Handles training of ML models with hyperparameter tuning.

## Core Functionality
- Model training
- Hyperparameter optimization
- Training job management
- Model evaluation

## API Contracts

### 1. Start Training Job
**Endpoint:** `POST /api/v1/training/jobs`

**Input:**
```json
{
  "modelType": "string",
  "trainingData": "string (URL)",
  "hyperparameters": {},
  "algorithm": "RANDOM_FOREST|NEURAL_NETWORK|XGBOOST"
}
```

**Output:**
```json
{
  "trainingJobId": "string (UUID)",
  "status": "QUEUED|RUNNING|COMPLETED|FAILED",
  "startedAt": "datetime"
}
```

### 2. Get Training Status
**Endpoint:** `GET /api/v1/training/jobs/{jobId}/status`

## Business Rules
- Max training time: 24 hours
- Max concurrent jobs: 10 per tenant
- Auto-retry on failure: 3 attempts

## Error Conditions
- Training data not found (404)
- Invalid hyperparameters (400)
- Training timeout (408)
