# Reconciliation Automation Service

Automated reconciliation service for financial transactions using hexagonal architecture pattern.

## Features

- Automated reconciliation between bank transactions and internal records
- Multiple matching algorithms (exact, fuzzy, AI-based)
- Difference detection and resolution workflow
- Reconciliation statuses: PENDING, RUNNING, COMPLETED, FAILED, PARTIAL
- Multi-tenant support
- Reconciliation rules configuration
- Integration with bank APIs
- Audit trail for all reconciliations
- Scheduled reconciliation jobs

## Architecture

This service follows hexagonal (ports and adapters) architecture:

- **Domain Layer**: Core business logic, entities, events, and interfaces
- **Application Layer**: Services that orchestrate business operations
- **Infrastructure Layer**: External integrations (MongoDB, Kafka, APIs)
- **Interface Layer**: HTTP controllers and DTOs

## Project Structure

```
reconciliation-automation-service/
├── src/
│   ├── domain/                 # Core domain logic
│   │   ├── models/            # Domain entities
│   │   ├── events/            # Domain events
│   │   ├── ports/             # Input/Output interfaces
│   │   └── repositories/      # Repository interfaces
│   ├── application/           # Application services
│   │   ├── services/          # Business logic
│   │   └── dto/               # Data transfer objects
│   ├── infrastructure/        # External integrations
│   │   ├── persistence/       # MongoDB
│   │   ├── messaging/         # Kafka
│   │   ├── datasources/       # API integrations
│   │   └── config/            # Configuration
│   ├── interfaces/            # HTTP controllers
│   └── shared/                # Shared utilities
└── package.json
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

## Running the Service

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
http://localhost:3003/api/docs
```

## API Endpoints

### Reconciliations

- `POST /api/v1/reconciliations` - Create a new reconciliation
- `GET /api/v1/reconciliations` - List reconciliations
- `GET /api/v1/reconciliations/:id` - Get reconciliation by ID
- `POST /api/v1/reconciliations/:id/cancel` - Cancel a reconciliation
- `GET /api/v1/reconciliations/:id/stats` - Get statistics
- `GET /api/v1/reconciliations/:id/matches` - Get matches
- `GET /api/v1/reconciliations/:id/differences` - Get differences
- `GET /api/v1/reconciliations/:id/audit-trail` - Get audit trail
- `POST /api/v1/reconciliations/schedule` - Schedule recurring reconciliation

### Matches

- `POST /api/v1/matches/:id/verify` - Verify a match
- `POST /api/v1/matches/:id/unverify` - Unverify a match

### Differences

- `GET /api/v1/differences/:id` - Get difference by ID
- `POST /api/v1/differences/:id/resolve` - Resolve a difference
- `POST /api/v1/differences/bulk-resolve` - Resolve multiple differences

### Rules

- `POST /api/v1/rules` - Create a reconciliation rule
- `GET /api/v1/rules` - List reconciliation rules
- `GET /api/v1/rules/active` - Get active rules
- `GET /api/v1/rules/:id` - Get rule by ID
- `PUT /api/v1/rules/:id` - Update a rule
- `POST /api/v1/rules/:id/enable` - Enable a rule
- `POST /api/v1/rules/:id/disable` - Disable a rule
- `DELETE /api/v1/rules/:id` - Delete a rule

## Reconciliation Status Flow

```
PENDING -> RUNNING -> COMPLETED (if no differences)
PENDING -> RUNNING -> PARTIAL (if differences exist)
PENDING -> RUNNING -> FAILED (if error occurs)
```

## Difference Resolution Workflow

1. **PENDING_REVIEW** - Initial state when difference is detected
2. **AUTO_RESOLVED** - Resolved by automated rules
3. **MANUALLY_RESOLVED** - Resolved by user
4. **IGNORED** - Marked as ignored
5. **ESCALATED** - Escalated for further review

## License

UNLICENSED
