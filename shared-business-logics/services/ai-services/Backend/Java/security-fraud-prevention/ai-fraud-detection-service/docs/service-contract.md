# AI Fraud Detection Service - Service Contract

## Service Responsibility
Detects and prevents fraudulent activities using ML models.

## Core Functionality
- Real-time fraud detection
- Pattern recognition
- Risk scoring
- Alert generation

## API Contracts

### 1. Analyze Transaction
**Endpoint:** `POST /api/v1/fraud/analyze`

**Input:**
```json
{
  "transactionId": "string",
  "userId": "string",
  "amount": "number",
  "merchant": "string",
  "timestamp": "datetime",
  "metadata": {}
}
```

**Output:**
```json
{
  "fraudScore": "float (0-1)",
  "riskLevel": "LOW|MEDIUM|HIGH",
  "reasons": ["string"],
  "action": "ALLOW|BLOCK|REVIEW"
}
```

### 2. Get Fraud Patterns
**Endpoint:** `GET /api/v1/fraud/patterns`

## Business Rules
- Threshold for blocking: score > 0.8
- Review threshold: score > 0.5
- Pattern retention: 180 days

## Error Conditions
- Invalid transaction (400)
- Analysis timeout (408)
