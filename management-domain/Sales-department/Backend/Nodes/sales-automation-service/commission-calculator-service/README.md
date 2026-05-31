# Commission Calculator Service

A microservice for calculating and managing sales commissions with support for multiple commission plan types, tiered structures, accelerators, and multi-tenancy.

## Architecture

This service follows **Hexagonal Architecture** (Ports and Adapters) with clear separation of concerns:

### Domain Layer
Contains the business logic and domain models:
- **Entities**: `Commission`, `CommissionRule`, `CommissionPeriod`, `CommissionPlan`, `CommissionPayout`, `CommissionTier`
- **Value Objects**: `Money`, `CommissionSplit`
- **Enums**: `CommissionStatus`, `CommissionCalculationType`, `CommissionPeriodType`, `AcceleratorType`, etc.
- **Events**: `CommissionCalculatedEvent`, `PayoutApprovedEvent`, `PayoutPaidEvent`, `CommissionClawbackEvent`, `CommissionPeriodClosedEvent`
- **Ports (In)**: Service interfaces defining use cases
- **Ports (Out)**: Repository and event publisher interfaces

### Application Layer
Contains use case implementations and DTOs:
- **Services**: `CommissionService`, `CommissionRuleService`, `CommissionPeriodService`
- **DTOs**: Request/Response models for API operations

### Infrastructure Layer
Contains technical implementations:
- **MongoDB**: Repository implementations using Mongoose
- **Kafka**: Event publisher for domain events
- **Schemas**: Mongoose schemas for persistence

### Interface Layer
Contains REST API controllers and cross-cutting concerns:
- **Controllers**: REST endpoints for all operations
- **Guards**: `TenantGuard`, `RoleGuard`
- **Interceptors**: Logging, response transformation
- **Filters**: Global exception handling

## Features

### Commission Plan Types
- **Flat Rate**: Simple percentage-based commission
- **Tiered**: Progressive rates based on sales thresholds
- **Accelerator**: Increased rates when quota is exceeded
- **Matrix**: Product/customer/region-specific rates
- **Graduated**: Different rates for different sale portions

### Commission Rules Engine
- Product-specific commission rates
- Customer-specific commission rates
- Accelerator multipliers (Linear, Step Up, Retroactive)
- Commission caps (Amount or Percentage)
- Effective and expiration dates

### Calculation Features
- Quota-based calculations
- Team commission splits
- Override adjustments
- Clawback processing
- Multi-currency support

### Multi-Tenancy
- Tenant-scoped data isolation
- Organization-level filtering
- Request context propagation

## API Endpoints

### Commissions
- `POST /api/v1/commissions/calculate` - Calculate commission for a transaction
- `POST /api/v1/commissions` - Create a commission
- `GET /api/v1/commissions/:id` - Get commission by ID
- `GET /api/v1/commissions/sales-rep/:salesRepId` - Get commissions for sales rep
- `POST /api/v1/commissions/:id/submit` - Submit for approval
- `POST /api/v1/commissions/:id/approve` - Approve commission
- `POST /api/v1/commissions/:id/adjustment` - Apply adjustment
- `POST /api/v1/commissions/:id/payment` - Process payment
- `POST /api/v1/commissions/:id/clawback` - Clawback commission

### Commission Rules
- `POST /api/v1/commission-rules` - Create rule
- `GET /api/v1/commission-rules/:id` - Get rule by ID
- `GET /api/v1/commission-rules` - Get all rules
- `PUT /api/v1/commission-rules/:id` - Update rule
- `POST /api/v1/commission-rules/:id/tiers` - Add tier
- `POST /api/v1/commission-rules/:id/product-rates` - Set product rate

### Commission Periods
- `POST /api/v1/commission-periods` - Create period
- `GET /api/v1/commission-periods/:id` - Get period by ID
- `POST /api/v1/commission-periods/:id/close` - Close period
- `POST /api/v1/commission-periods/create/monthly` - Create monthly period

### Health
- `GET /health` - Health check
- `GET /health/liveness` - Liveness probe
- `GET /health/readiness` - Readiness probe

## Environment Variables

```bash
# Application
NODE_ENV=development
PORT=3002
SERVICE_NAME=commission-calculator-service

# CORS
CORS_ORIGIN=*

# MongoDB
MONGODB_URI=mongodb://localhost:27017/commission-calculator

# Kafka
KAFKA_BROKERS=localhost:9092
KAFKA_SSL=false
KAFKA_SASL_MECHANISM=
KAFKA_USERNAME=
KAFKA_PASSWORD=

# Commission Settings
DEFAULT_CURRENCY=USD
```

## Installation

```bash
npm install
```

## Running

```bash
# Development
npm run start:dev

# Production
npm run build
npm run start:prod
```

## Testing

```bash
# Unit tests
npm run test

# E2E tests
npm run test:e2e

# Coverage
npm run test:cov
```

## Headers

All requests must include tenant context:

```
x-tenant-id: {tenant-id}
x-user-id: {user-id}
x-organization-id: {organization-id} (optional)
x-correlation-id: {correlation-id} (optional)
```

## Domain Events

The service publishes the following events to Kafka:
- `commission.calculated` - When commission is calculated
- `payout.approved` - When payout is approved
- `payout.paid` - When payout is paid
- `commission.clawback` - When commission is clawed back
- `commission.period.closed` - When period is closed

## License

MIT
