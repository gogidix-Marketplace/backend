# Sales Forecasting Service

A NestJS microservice for sales forecasting as part of the Gogidix Ecosystem.

## Architecture

This service follows **Hexagonal Architecture** (Ports and Adapters pattern):

```
src/
├── domain/                    # Core business logic
│   ├── entities/             # Business entities (Forecast, ForecastModel, ForecastAccuracy)
│   ├── events/               # Domain events (ForecastGeneratedEvent, ForecastAdjustedEvent)
│   ├── ports/
│   │   ├── in/               # Input ports (Use Cases)
│   │   └── out/              # Output ports (Repositories, Event Publishers)
│   └── value-objects/        # Value objects
├── application/              # Application services
│   ├── commands/             # Command handlers
│   ├── queries/              # Query handlers
│   ├── dto/                  # Data Transfer Objects
│   ├── use-cases/            # Use case implementations
│   └── services/             # Application services (ForecastCalculator)
├── infrastructure/           # External implementations
│   ├── config/               # Configuration (MongoDB, Kafka, Redis)
│   ├── persistence/
│   │   └── mongo/            # MongoDB repositories and schemas
│   ├── messaging/
│   │   └── kafka/            # Kafka event publishers
│   ├── cache/                # Cache implementations
│   └── scheduler/            # Scheduled tasks
├── interfaces/               # External interfaces
│   └── rest/                 # REST controllers
└── shared/                   # Shared utilities
    ├── constants.ts
    ├── errors.ts
    └── request-context.ts    # Multi-tenancy context
```

## Features

- **Multiple Forecasting Models**:
  - Weighted Pipeline (based on deal stages and probabilities)
  - Historical (based on historical sales data)
  - AI/ML (machine learning enhanced)
  - Hybrid (combination of multiple models)

- **Forecast Periods**: Monthly, Quarterly, Annual

- **Forecast Granularity**:
  - Global (entire organization)
  - Territory level
  - Team level
  - Representative level

- **Forecast Accuracy Tracking**: MAPE, MAE, RMSE, Bias metrics

- **Rolling Forecasts**: Automatic monthly forecast updates

- **Multi-tenancy**: Tenant-aware data isolation

## Installation

```bash
npm install
```

## Configuration

Create a `.env` file based on `.env.example`:

```bash
cp .env.example .env
```

## Running the Application

```bash
# Development
npm run start:dev

# Production
npm run build
npm run start:prod
```

## API Documentation

Once running, access Swagger documentation at:
```
http://localhost:3004/api/docs
```

## API Endpoints

### Generate Forecast
```http
POST /api/v1/forecasts
```

### Get Forecast
```http
GET /api/v1/forecasts/:id
```

### List Forecasts
```http
GET /api/v1/forecasts
```

### Adjust Forecast
```http
PUT /api/v1/forecasts/:id/adjust
```

### Calculate Accuracy
```http
POST /api/v1/forecasts/:id/accuracy
```

### Trigger Rolling Forecast
```http
POST /api/v1/forecasts/rolling/trigger
```

## Domain Events

The service publishes the following events to Kafka:

- `ForecastGeneratedEvent`: When a new forecast is generated
- `ForecastAdjustedEvent`: When a forecast is manually adjusted
- `ForecastAccuracyCalculatedEvent`: When forecast accuracy is calculated
- `RollingForecastTriggeredEvent`: When a rolling forecast is triggered

## Testing

```bash
# Unit tests
npm run test

# E2E tests
npm run test:e2e

# Coverage
npm run test:cov
```

## Database Schema

### Forecast
- Stores forecast metadata, data points, and breakdowns
- Supports multiple models and granularities
- Tracks forecast versions and adjustments

### ForecastAccuracy
- Stores accuracy metrics for forecasts
- Tracks trends over time
- Supports alerts for low accuracy

### ForecastPeriod
- Stores period-level forecast data
- Tracks actuals vs forecasted amounts

## License

Proprietary - Gogidix Ecosystem
