# Payment Automation Service

A Node.js/TypeScript service implementing payment automation with hexagonal architecture pattern, built with NestJS framework.

## Features

1. **Payment Processing**
   - Multiple payment gateway support (Stripe, TransferWise)
   - Payment statuses: PENDING, SCHEDULED, PROCESSING, COMPLETED, FAILED, CANCELLED
   - Payment retry mechanism with configurable max retries
   - Payment metadata tracking

2. **Payment Batching**
   - Bulk payment processing
   - Batch statuses: DRAFT, PENDING, PROCESSING, PARTIALLY_COMPLETED, COMPLETED, FAILED, CANCELLED
   - Configurable priority and concurrency
   - Batch scheduling

3. **Payment Automation**
   - Scheduled payment processing (cron jobs)
   - Automated batch processing
   - Failed payment retry automation
   - Payment reconciliation hooks

4. **Vendor Management**
   - Vendor payment tracking
   - Bank details management
   - Payment settings configuration
   - Credit score and payment rating

5. **Multi-Tenant Support**
   - Tenant isolation
   - Context propagation

6. **Domain Events**
   - Kafka integration for event publishing
   - Payment lifecycle events

## Architecture

This service follows **Hexagonal Architecture** (Ports and Adapters):

```
src/
├── domain/                 # Core business logic
│   ├── models/            # Domain entities
│   ├── events/            # Domain events
│   ├── ports/             # Input/Output ports
│   └── repositories/      # Repository interfaces
├── application/           # Application services
│   ├── services/          # Use cases
│   └── dto/               # Data transfer objects
├── infrastructure/        # External implementations
│   ├── persistence/       # MongoDB repositories
│   ├── messaging/         # Kafka messaging
│   ├── gateway/           # Payment gateway adapters
│   └── config/            # Configuration
└── interfaces/            # External interfaces
    └── http/              # REST controllers
```

## Installation

```bash
npm install
```

## Configuration

Copy `.env.example` to `.env` and configure:

```bash
cp .env.example .env
```

### Environment Variables

- `PORT` - Service port (default: 3001)
- `MONGODB_URI` - MongoDB connection string
- `KAFKA_BROKERS` - Comma-separated Kafka broker addresses
- `STRIPE_API_KEY` - Stripe API key
- `TRANSFERWISE_API_KEY` - TransferWise API key

## Running the Application

```bash
# Development mode
npm run start:dev

# Production mode
npm run build
npm run start:prod
```

## API Documentation

### Payments

- `POST /api/v1/payments` - Create a new payment
- `GET /api/v1/payments/:id` - Get payment by ID
- `PUT /api/v1/payments/:id` - Update payment
- `POST /api/v1/payments/:id/process` - Process a payment
- `POST /api/v1/payments/:id/schedule` - Schedule a payment
- `POST /api/v1/payments/:id/cancel` - Cancel a payment
- `POST /api/v1/payments/:id/retry` - Retry a failed payment
- `GET /api/v1/payments` - List payments with filters

### Batches

- `POST /api/v1/batches` - Create a new batch
- `GET /api/v1/batches/:id` - Get batch by ID
- `POST /api/v1/batches/:id/payments` - Add payment to batch
- `POST /api/v1/batches/:id/process` - Process a batch
- `POST /api/v1/batches/:id/schedule` - Schedule a batch
- `DELETE /api/v1/batches/:id` - Cancel a batch
- `GET /api/v1/batches` - List batches

## Development

### Running Tests

```bash
npm test
npm run test:cov
```

### Code Style

```bash
npm run lint
```

## Payment Status Flow

```
PENDING -> SCHEDULED -> PROCESSING -> COMPLETED
                 \           \
                  \           -> FAILED -> (RETRY) -> PROCESSING
                   \
                    -> CANCELLED
```

## Scheduled Tasks

- **Every Hour**: Process scheduled payments
- **Every 5 Minutes**: Process scheduled batches
- **Every Day at Midnight**: Retry failed payments
- **Every Day at 1 AM**: Cleanup old payments

## License

Proprietary - Gogidix Ecosystem
