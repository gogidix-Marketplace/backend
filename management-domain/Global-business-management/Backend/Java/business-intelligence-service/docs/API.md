# Business Intelligence Service - API Documentation

## Base URL
```
http://localhost:8080/api/v1/bi
```

---

## Reports

### Generate Report
```http
POST /api/v1/bi/reports
Content-Type: application/json
```

**Request Body:**
```json
{
  "reportName": "Q1 2024 Executive Summary",
  "reportType": "EXECUTIVE_SUMMARY",
  "reportPeriod": "QUARTERLY",
  "startDate": "2024-01-01",
  "endDate": "2024-03-31",
  "regionCode": "GLOBAL",
  "businessUnit": "ALL",
  "includeCharts": true,
  "includeTables": true,
  "includeInsights": true
}
```

**Response:**
```json
{
  "id": "rep-123456",
  "reportName": "Q1 2024 Executive Summary",
  "status": "GENERATING",
  "createdBy": "user@example.com",
  "createdAt": "2024-01-15T10:00:00Z",
  "estimatedCompletion": "2024-01-15T10:05:00Z"
}
```

### Get Report
```http
GET /api/v1/bi/reports/{reportId}
```

**Response:**
```json
{
  "id": "rep-123456",
  "reportName": "Q1 2024 Executive Summary",
  "status": "COMPLETED",
  "reportType": "EXECUTIVE_SUMMARY",
  "sections": [
    {
      "sectionId": "summary",
      "title": "Executive Summary",
      "type": "SUMMARY",
      "content": {
        "text": "Overall business performance exceeded expectations...",
        "metrics": [...],
        "charts": [...]
      }
    }
  ],
  "summary": {
    "executiveSummary": "...",
    "keyFindings": ["..."],
    "recommendations": ["..."],
    "overallSentiment": "POSITIVE",
    "confidenceScore": 85
  }
}
```

### List Reports
```http
GET /api/v1/bi/reports?type=EXECUTIVE_SUMMARY&status=COMPLETED&page=0&size=20
```

---

## Insights

### Get Active Insights
```http
GET /api/v1/bi/insights?entityCode=EUROPE&impactLevel=HIGH&page=0&size=20
```

**Query Parameters:**
- `entityCode`: Filter by entity (region, country)
- `impactLevel`: Filter by impact (CRITICAL, HIGH, MEDIUM, LOW)
- `insightType`: Filter by type
- `sentiment`: Filter by sentiment (POSITIVE, NEGATIVE, NEUTRAL)
- `status`: Filter by status (default: ACTIVE)
- `requiresAction`: true/false

**Response:**
```json
{
  "content": [
    {
      "id": "ins-123456",
      "title": "Revenue Growth Opportunity in Germany",
      "summary": "German market showing 25% growth potential",
      "insightType": "REVENUE_GROWTH",
      "impactLevel": "HIGH",
      "confidenceScore": 0.85,
      "sentiment": "POSITIVE",
      "requiresAction": true,
      "recommendations": [
        {
          "title": "Increase marketing spend",
          "priority": "HIGH",
          "estimatedImpact": 150000,
          "timeline": "Q2 2024"
        }
      ],
      "detectedAt": "2024-01-15T10:00:00Z"
    }
  ],
  "totalElements": 45,
  "pageable": {...}
}
```

### Acknowledge Insight
```http
POST /api/v1/bi/insights/{insightId}/acknowledge
```

**Request Body:**
```json
{
  "acknowledgedBy": "user@example.com",
  "comments": "Will address in next planning cycle"
}
```

---

## Forecasts

### Create Forecast
```http
POST /api/v1/bi/forecasts
```

**Request Body:**
```json
{
  "metricCode": "REVENUE",
  "entityCode": "EUROPE",
  "forecastType": "QUARTERLY",
  "forecastMethod": "ARIMA",
  "forecastHorizon": 4,
  "historicalPeriodStart": "2023-01-01",
  "historicalPeriodEnd": "2024-01-01"
}
```

**Response:**
```json
{
  "id": "fcast-123456",
  "forecastName": "Europe Revenue Forecast",
  "metricCode": "REVENUE",
  "status": "ACTIVE",
  "confidenceLevel": 0.85,
  "forecastValues": [
    {
      "period": "2024-Q2",
      "forecastValue": 1500000,
      "lowerBound": 1400000,
      "upperBound": 1600000
    }
  ],
  "scenarios": {
    "baseline": {...},
    "optimistic": {...},
    "pessimistic": {...}
  }
}
```

### Get Forecast
```http
GET /api/v1/bi/forecasts/{forecastId}
```

### List Forecasts
```http
GET /api/v1/bi/forecasts?metricCode=REVENUE&entityCode=EUROPE&status=ACTIVE
```

---

## Trend Analysis

### Analyze Trend
```http
POST /api/v1/bi/trends/analyze
```

**Request Body:**
```json
{
  "metricCode": "REVENUE",
  "entityCode": "EUROPE",
  "periodStart": "2023-01-01",
  "periodEnd": "2024-01-01",
  "analysisType": "LINEAR"
}
```

**Response:**
```json
{
  "id": "trend-123456",
  "metricName": "Total Revenue",
  "trendDirection": "UPWARD",
  "trendStrength": 0.85,
  "trendPattern": "LINEAR_GROWTH",
  "statistics": {
    "mean": 1250000,
    "median": 1200000,
    "standardDeviation": 150000,
    "coefficientOfVariation": 0.12
  },
  "forecast": {
    "forecastMethod": "LINEAR_REGRESSION",
    "forecastPoints": [...],
    "confidenceInterval": 0.95
  },
  "anomalies": {
    "detected": [...],
    "totalCount": 3,
    "severity": "LOW"
  },
  "drivers": {
    "drivers": [
      {
        "name": "Marketing Campaign",
        "impact": 0.6,
        "direction": "POSITIVE"
      }
    ]
  }
}
```

---

## Health Check

```http
GET /actuator/health
```

**Response:**
```json
{
  "status": "UP",
  "components": {
    "db": {"status": "UP"},
    "cache": {"status": "UP"}
  }
}
```

---

## Error Responses

Standard error format:
```json
{
  "timestamp": "2024-01-15T10:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid date range",
  "path": "/api/v1/bi/reports"
}
```

**Status Codes:**
- 200: Success
- 201: Created
- 400: Bad Request
- 404: Not Found
- 500: Internal Server Error
