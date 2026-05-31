# Time Series Forecasting Service - Service Contract

## Service Responsibility
Specialized service for time series forecasting and analysis.

## Core Functionality
- Time series forecasting
- Seasonality detection
- Anomaly detection in time series
- Forecast evaluation

## API Contracts

### 1. Create Forecast
**Endpoint:** `POST /api/v1/forecasting/forecasts`

**Input:**
```json
{
  "timeSeriesData": [{"timestamp": "datetime", "value": "number"}],
  "forecastHorizon": "integer",
  "frequency": "HOURLY|DAILY|WEEKLY|MONTHLY",
  "includeSeasonality": "boolean"
}
```

**Output:**
```json
{
  "forecastId": "string (UUID)",
  "forecasts": [{"timestamp": "datetime", "value": "number", "lowerBound": "number", "upperBound": "number"}],
  "accuracyMetrics": {}
}
```

### 2. Detect Anomalies
**Endpoint:** `POST /api/v1/forecasting/anomalies`

## Business Rules
- Min data points: 20
- Max forecast horizon: 1000 periods
- Default confidence: 95%

## Error Conditions
- Insufficient data points (400)
- Invalid frequency (400)
- Forecast timeout (408)
