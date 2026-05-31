# Export Service - API Documentation

## Base URL
```
http://localhost:8080/api/v1/export
```

## Create Export Job
```
POST /api/v1/export/jobs
```

**Request:**
```json
{
  "entityType": "CountryData",
  "format": "CSV",
  "filters": {"region": "Europe"},
  "fields": ["countryCode", "countryName", "population"]
}
```

## Get Export Status
```
GET /api/v1/export/jobs/{jobId}
```

## Download Export
```
GET /api/v1/export/jobs/{jobId}/download
```

## List Export Jobs
```
GET /api/v1/export/jobs?page=0&size=20
```
