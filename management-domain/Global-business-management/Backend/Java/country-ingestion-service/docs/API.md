# Country Ingestion Service - API Documentation

## Base URL
```
http://localhost:8080/api/v1/country-ingestion
```

---

## Ingestion Batches

### Create Batch
```http
POST /api/v1/country-ingestion/batches
Content-Type: application/json
```

**Request Body:**
```json
{
  "source": "CSV_UPLOAD",
  "format": "csv",
  "fileSizeBytes": 1024000,
  "fileName": "countries.csv",
  "schemaId": "country-schema-v1",
  "createdBy": "admin@example.com"
}
```

**Response:**
```json
{
  "batchId": "BATCH-20240123-120000-abc123",
  "status": "PENDING",
  "createdDate": "2024-01-23T12:00:00Z",
  "totalRecords": 0
}
```

### Upload and Ingest File
```http
POST /api/v1/country-ingestion/batches/{batchId}/upload
Content-Type: multipart/form-data
```

**Form Data:**
- `file`: The file to ingest
- `columnMappings`: JSON string of column mappings
- `validateOnly`: boolean flag

**Response:**
```json
{
  "batchId": "BATCH-20240123-120000-abc123",
  "status": "COMPLETED",
  "totalRecords": 250,
  "processedRecords": 250,
  "successfulRecords": 245,
  "failedRecords": 5,
  "progressPercentage": 100,
  "validationErrors": [...]
}
```

### Get Batch Status
```http
GET /api/v1/country-ingestion/batches/{batchId}
```

**Response:**
```json
{
  "batchId": "BATCH-20240123-120000-abc123",
  "status": "IN_PROGRESS",
  "progressPercentage": 45,
  "totalRecords": 1000,
  "processedRecords": 450,
  "successfulRecords": 440,
  "failedRecords": 10,
  "startTime": "2024-01-23T12:00:00Z",
  "processingTimeMs": 125000
}
```

### List Batches
```http
GET /api/v1/country-ingestion/batches?status=IN_PROGRESS&page=0&size=20
```

---

## Country Data

### Get Country by Code
```http
GET /api/v1/country-ingestion/countries/{countryCode}
```

**Response:**
```json
{
  "countryCode": "DE",
  "countryName": "Germany",
  "region": "Europe",
  "population": 83200000,
  "gdpUsd": 4250000000000,
  "currencyCode": "EUR",
  "dataQualityScore": 95
}
```

### Search Countries
```http
GET /api/v1/country-ingestion/countries/search?q=German&page=0&size=20
```

### Get Countries by Region
```http
GET /api/v1/country-ingestion/countries/region/{region}
```

### Update Country Data
```http
PUT /api/v1/country-ingestion/countries/{id}
Content-Type: application/json
```

### Delete Country Data
```http
DELETE /api/v1/country-ingestion/countries/{id}
```

---

## Validation Errors

### Get Batch Validation Errors
```http
GET /api/v1/country-ingestion/batches/{batchId}/errors
```

**Response:**
```json
{
  "content": [
    {
      "errorId": "ERR-001",
      "fieldName": "population",
      "errorMessage": "Population must be a positive number",
      "errorLevel": "HIGH",
      "rowNumber": 15,
      "status": "OPEN"
    }
  ],
  "totalElements": 5
}
```

### Resolve Error
```http
POST /api/v1/country-ingestion/errors/{errorId}/resolve
```

**Request Body:**
```json
{
  "correctedValue": "83200000",
  "resolutionNotes": "Updated with official census data"
}
```

### Suppress Error
```http
POST /api/v1/country-ingestion/errors/{errorId}/suppress
```

**Request Body:**
```json
{
  "reason": "False positive - data is valid"
}
```

---

## Health Check

```http
GET /actuator/health
```

---

## Error Responses

```json
{
  "timestamp": "2024-01-23T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid file format",
  "path": "/api/v1/country-ingestion/batches"
}
```
