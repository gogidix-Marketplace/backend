# AI Training Service - Service Contract

## Service Responsibility
Additional training capabilities for fine-tuning and retraining.

## Core Functionality
- Model fine-tuning
- Transfer learning
- Retraining schedules
- Training data management

## API Contracts

### 1. Fine-tune Model
**Endpoint:** `POST /api/v1/training/fine-tune`

**Input:**
```json
{
  "baseModel": "string (modelId)",
  "trainingData": "string (URL)",
  "epochs": "integer",
  "learningRate": "float"
}
```

**Output:**
```json
{
  "fineTuningJobId": "string (UUID)",
  "status": "RUNNING",
  "estimatedCompletion": "datetime"
}
```

### 2. Schedule Retraining
**Endpoint:** `POST /api/v1/training/schedule`

## Business Rules
- Max epochs: 1000
- Min learning rate: 0.0001
- Retraining frequency: min 1 day

## Error Conditions
- Base model not found (404)
- Invalid training data (400)
