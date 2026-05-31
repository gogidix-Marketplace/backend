# Financial Reporting Service - API Documentation

## Base URL
```
/api/v1
```

## Reports API

### Generate Report
```http
POST /reports/generate
Content-Type: application/json

{
  "reportType": "BALANCE_SHEET",
  "periodStart": "2024-01-01",
  "periodEnd": "2024-12-31",
  "currency": "USD",
  "comparisonPeriod": true,
  "includeBudgets": false
}
```

### Get Report
```http
GET /reports/{reportId}
```

### List Reports
```http
GET /reports?reportType=BALANCE_SHEET&page=0&size=20
```

### Download Report
```http
GET /reports/{reportId}/download?format=PDF
```

## Report Templates API

### Create Template
```http
POST /reports/templates
Content-Type: application/json

{
  "templateName": "Custom P&L",
  "reportType": "INCOME_STATEMENT",
  "rows": [...],
  "columns": [...]
}
```

## Schedules API

### Create Schedule
```http
POST /reports/schedules
Content-Type: application/json

{
  "reportType": "BALANCE_SHEET",
  "schedule": "MONTHLY",
  "recipients": ["email@example.com"],
  "format": "PDF"
}
```
