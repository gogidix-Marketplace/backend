# Forecast Management Service - Architecture Documentation

## Overview

The Forecast Management Service provides sales forecasting capabilities with multiple forecasting methods and accuracy tracking.

## Domain Model

### Forecast Entity

- `forecastId`: Unique identifier
- `tenantId`: Multi-tenant isolation
- `period`: Forecast period
- `forecastType`: Type (PIPELINE, HISTORICAL, AI_ENHANCED)
- `totalAmount`: Forecasted amount
- `currency`: Currency code
- `confidence`: Confidence level
- `createdBy': User who created forecast
- `approvedBy': User who approved forecast
- `status`: Status (DRAFT, SUBMITTED, APPROVED, REJECTED)
- `version`: Forecast version for tracking changes

### ForecastAdjustment Entity

- `adjustmentId`: Unique identifier
- `forecastId`: Parent forecast
- `userId': User making adjustment
- `originalAmount`: Original forecasted amount
- `adjustedAmount`: New amount
- `reason`: Reason for adjustment
- `timestamp`: When adjustment was made

## Application Services

### ForecastCommandService

- `createForecast()`: Create new forecast
- `submitForecast()`: Submit for approval
- `approveForecast()`: Approve forecast
- `rejectForecast()`: Reject forecast
- `adjustForecast()`: Make adjustment to forecast

### ForecastQueryService

- `getForecast()`: Get forecast by period
- `getForecastHistory()`: Get forecast versions
- `getForecastAccuracy()`: Compare forecast vs actual
- `getRollingForecast()`: Get rolling forecast

## Technology Stack

- **Language**: Java 17
- **Framework**: Spring Boot 3.1.5
- **Database**: MongoDB
- **Messaging**: Apache Kafka
