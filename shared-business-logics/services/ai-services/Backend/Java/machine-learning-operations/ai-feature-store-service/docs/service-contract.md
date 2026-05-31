# AI Feature Store Service - Service Contract

## Service Responsibility
Centralized storage and management of ML features.

## Core Functionality
- Feature storage
- Feature retrieval
- Feature versioning
- Feature lineage

## API Contracts

### 1. Store Features
**Endpoint:** `POST /api/v1/feature-store/features`

**Input:**
```json
{
  "featureName": "string",
  "featureType": "NUMERIC|CATEGORICAL|TEXT",
  "values": [{"entityId": "string", "value": "object"}],
  "metadata": {}
}
```

**Output:**
```json
{
  "featureVersion": "string",
  "storedCount": "integer",
  "timestamp": "datetime"
}
```

### 2. Get Features
**Endpoint:** `GET /api/v1/feature-store/features/{featureName}/entities/{entityId}`

## Business Rules
- Max feature versions: 100
- Feature retention: 90 days
- Max value size: 1MB

## Error Conditions
- Feature not found (404)
- Invalid feature type (400)
