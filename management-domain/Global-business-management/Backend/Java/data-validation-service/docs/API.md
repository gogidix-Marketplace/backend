# Data Validation Service - API Documentation

## Base URL
```
http://localhost:8080/api/v1/validation
```

---

## Validation Rules

### Create Rule
```http
POST /api/v1/validation/rules
```

**Request:**
```json
{
  "ruleName": "Valid Country Code",
  "ruleType": "FORMAT",
  "fieldName": "countryCode",
  "pattern": "^[A-Z]{2}$",
  "errorMessage": "Invalid country code format"
}
```

### List Rules
```http
GET /api/v1/validation/rules?entityType=CountryData&active=true
```

### Update Rule
```http
PUT /api/v1/validation/rules/{ruleId}
```

### Delete Rule
```http
DELETE /api/v1/validation/rules/{ruleId}
```

---

## Validation

### Validate Single Record
```http
POST /api/v1/validation/validate
```

**Request:**
```json
{
  "entityType": "CountryData",
  "data": {
    "countryCode": "DE",
    "countryName": "Germany",
    "population": 83200000
  }
}
```

**Response:**
```json
{
  "valid": true,
  "score": 100,
  "results": []
}
```

### Batch Validation
```http
POST /api/v1/validation/batch
```

**Request:**
```json
{
  "entityType": "CountryData",
  "records": [...],
  "stopOnError": false
}
```

---

## Data Quality

### Get Quality Report
```http
GET /api/v1/validation/quality/{entityType}
```

**Response:**
```json
{
  "entityType": "CountryData",
  "overallScore": 92,
  "dimensions": {
    "completeness": 95,
    "accuracy": 90,
    "consistency": 92,
    "timeliness": 88,
    "validity": 95
  },
  "issueCount": 15,
  "lastEvaluated": "2024-01-23T12:00:00Z"
}
```

---

## Health Check
```http
GET /actuator/health
```
