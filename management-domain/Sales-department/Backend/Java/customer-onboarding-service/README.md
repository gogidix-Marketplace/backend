# Customer Onboarding Service

Hexagonal SaaS multi-tenant service for customer onboarding workflow management in the Sales Department.

## Architecture

This service follows hexagonal architecture (ports and adapters) with clear separation of concerns:

### Domain Layer
- **Entities**: `Onboarding`, `OnboardingStep`, `OnboardingTemplate`, `DocumentChecklist`
- **Events**: `OnboardingStartedEvent`, `OnboardingCompletedEvent`, `OnboardingStepCompletedEvent`, `DocumentUploadedEvent`
- **Ports**:
  - Input: `OnboardingCommand`, `OnboardingQuery`, `OnboardingTemplateCommand`
  - Output: `EventPublisher`, `EmailService`
- **Value Objects**: `CustomerType`, `OnboardingStatus`, `StepStatus`, `Priority`, `StepType`
- **Repository Interfaces**: `OnboardingRepository`, `OnboardingTemplateRepository`, `DocumentChecklistRepository`

### Application Layer
- **Services**: `OnboardingCommandService`, `OnboardingQueryService`
- **DTOs**: Request/Response DTOs for all operations

### Infrastructure Layer
- **Persistence**: MongoDB repository implementations with tenant filtering
- **Messaging**: Kafka event publishing for domain events
- **Email**: Email service for welcome/completion notifications
- **Configuration**: MongoDB, Kafka, Web configuration

### Interface Layer
- **REST Controllers**: Full CRUD operations for onboarding
- **Exception Handling**: Global exception handler with proper error responses

## Features

- Customer onboarding workflow management
- Onboarding step tracking with dependencies
- Onboarding templates for different customer types (Individual, Small Business, Enterprise, Partner, Reseller)
- Document checklist management with verification
- Onboarding progress tracking
- Automated task assignments
- Welcome email sequences
- Multi-tenancy with RequestContextHolder
- Domain events for integration

## API Endpoints

### Onboarding Operations
- `POST /api/v1/onboardings` - Create new onboarding
- `GET /api/v1/onboardings/{onboardingId}` - Get onboarding by ID
- `GET /api/v1/onboardings` - Get all onboardings for tenant
- `GET /api/v1/onboardings/customer/{customerId}` - Get by customer
- `POST /api/v1/onboardings/{onboardingId}/start` - Start onboarding
- `POST /api/v1/onboardings/{onboardingId}/assign` - Assign to user
- `POST /api/v1/onboardings/{onboardingId}/steps/{stepId}` - Update step
- `POST /api/v1/onboardings/{onboardingId}/complete` - Complete onboarding
- `POST /api/v1/onboardings/{onboardingId}/documents` - Upload document
- `GET /api/v1/onboardings/summary` - Get onboarding summary

### Template Operations
- `GET /api/v1/onboardings/templates` - Get all templates
- `POST /api/v1/onboardings/templates` - Create template
- `PUT /api/v1/onboardings/templates/{templateId}` - Update template
- `POST /api/v1/onboardings/templates/{templateId}/activate` - Activate template

## Configuration

### Environment Variables
- `MONGODB_HOST` - MongoDB host (default: localhost)
- `MONGODB_PORT` - MongoDB port (default: 27017)
- `MONGODB_DB` - Database name (default: customer-onboarding-service_db)
- `KAFKA_SERVERS` - Kafka bootstrap servers (default: localhost:9092)
- `REDIS_HOST` - Redis host (default: localhost)
- `REDIS_PORT` - Redis port (default: 6379)
- `SERVICE_PORT` - Service port (default: 8085)

### Required Headers
All requests must include:
- `X-Tenant-ID` - Tenant identifier
- `X-User-ID` - User identifier
- `X-Correlation-ID` - Correlation ID (optional, auto-generated if not provided)

## Building and Running

### Build
```bash
mvn clean package
```

### Run
```bash
java -jar target/customer-onboarding-service-1.0.0.jar
```

### Docker
```bash
docker build -t customer-onboarding-service .
docker run -p 8085:8085 customer-onboarding-service
```

## Testing

```bash
mvn test
```

## API Documentation

Swagger UI is available at: `http://localhost:8085/api/v1/swagger-ui.html`

## License

Copyright (c) Gogidix Ecosystem
