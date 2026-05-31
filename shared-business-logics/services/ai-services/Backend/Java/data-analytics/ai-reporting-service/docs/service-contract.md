# AI Reporting Service - Service Contract

## Service Responsibility
Generates analytical reports from processed data and metrics.

## Core Functionality
- Report generation
- Report templates
- Scheduled reports
- Export capabilities

## API Contracts

### 1. Generate Report
**Endpoint:** `POST /api/v1/reports/generate`

**Input:**
```json
{
  "type": "SUMMARY|DETAILED|CUSTOM",
  "dateRange": {
    "start": "datetime",
    "end": "datetime"
  },
  "includeMetrics": ["string"],
  "format": "PDF|CSV|JSON"
}
```

**Output:**
```json
{
  "reportId": "string (UUID)",
  "status": "PROCESSING|COMPLETED",
  "downloadUrl": "string",
  "expiresAt": "datetime"
}
```

### 2. Get Report Status
**Endpoint:** `GET /api/v1/reports/{reportId}/status`

## Business Rules
- Report generation timeout: 5 minutes
- Max report size: 10MB
- Reports retained for 30 days

## Error Conditions
- Invalid report type (400)
- Report not found (404)
- Generation timeout (408)
