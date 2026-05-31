# Anomaly Detection Service - Service Contract

## Service Responsibility
Detects anomalies in data, behavior, and system metrics.

## Core Functionality
- Real-time anomaly detection
- Pattern deviation analysis
- Alert generation
- Threshold management

## API Contracts

### 1. Detect Anomalies
**Endpoint:** `POST /api/v1/anomalies/detect`

**Input:**
```json
{
  "dataSource": "string",
  "timeRange": {"start": "datetime", "end": "datetime"},
  "sensitivity": "LOW|MEDIUM|HIGH",
  "algorithms": ["ISOLATION_FOREST|AUTOENCODER|Z_SCORE"]
}
```

**Output:**
```json
{
  "analysisId": "string (UUID)",
  "anomalies": [{
    "timestamp": "datetime",
    "score": "float (0-1)",
    "description": "string"
  }],
  "summary": {}
}
```

### 2. Get Anomaly History
**Endpoint:** `GET /api/v1/anomalies/history`

## Business Rules
- Min anomaly score: 0.7
- Data retention: 90 days
- Max analysis window: 30 days

## Error Conditions
- Invalid data source (400)
- Analysis timeout (408)
