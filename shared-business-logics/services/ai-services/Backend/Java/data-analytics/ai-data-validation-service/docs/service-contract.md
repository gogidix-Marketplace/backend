# AI Data Validation Service - Service Contract

## Service Responsibility
Validates data quality, schema compliance, and business rules for AI/ML datasets.

## Core Functionality
- Schema validation
- Data quality checks
- Anomaly detection
- Validation rule management

## API Contracts

### 1. Validate Dataset
**Endpoint:** `POST /api/v1/validation/datasets/validate`

**Input:**
```json
{
  "dataSource": "string",
  "schema": {},
  "validationRules": ["string"]
}
```

**Output:**
```json
{
  "validationId": "string (UUID)",
  "isValid": "boolean",
  "errors": [],
  "warnings": [],
  "statistics": {}
}
```

### 2. Get Validation Result
**Endpoint:** `GET /api/v1/validation/results/{validationId}`

## Business Rules
- Maximum validation time: 1 hour
- Max file size: 1GB
- Schema validation is always performed
- Custom rules are applied after schema validation

## Error Conditions
- Invalid schema format (400)
- Data source not found (404)
- Validation timeout (408)
