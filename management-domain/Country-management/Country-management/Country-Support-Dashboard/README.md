# Country Support Dashboard

Country-level customer support dashboard service for the Gogidix ecosystem. This service manages customer support operations at the country level and reports to the Customer-support HQ.

## Overview

The Country Support Dashboard handles:
- Ticket Management - Country support tickets with full lifecycle management
- SLA Tracking - Country SLA compliance monitoring and reporting
- Team Management - Country support team performance tracking
- Customer Feedback - Country customer satisfaction measurement
- Knowledge Base - Country-specific knowledge base
- Escalation - Escalate critical issues to HQ when needed
- HQ Support Client - Call Customer-support HQ APIs
- Kafka Integration - Send support metrics to HQ

## Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                    Country Support Dashboard                    │
├─────────────────────────────────────────────────────────────────┤
│                                                                   │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐        │
│  │   Ticket     │  │     SLA      │  │  Knowledge   │        │
│  │  Management  │  │   Tracking   │  │    Base      │        │
│  └──────────────┘  └──────────────┘  └──────────────┘        │
│                                                                   │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐        │
│  │    Team      │  │  Feedback    │  │ Escalation   │        │
│  │  Management  │  │  System      │  │   to HQ      │        │
│  └──────────────┘  └──────────────┘  └──────────────┘        │
│                                                                   │
└─────────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                      HQ Support Service                          │
│                   (Customer-support HQ)                         │
└─────────────────────────────────────────────────────────────────┘
```

## Project Structure

```
Country-Support-Dashboard/
├── backend/
│   └── country-support-dashboard-service/
│       ├── src/main/java/com/gogidix/countrysupport/
│       │   ├── application/
│       │   │   ├── config/          # Configuration classes
│       │   │   ├── dto/             # Data Transfer Objects
│       │   │   ├── exception/       # Exception handlers
│       │   │   └── mapper/          # MapStruct mappers
│       │   ├── domain/
│       │   │   ├── enums/           # Domain enumerations
│       │   │   ├── model/           # JPA entities
│       │   │   ├── repository/      # JPA repositories
│       │   │   └── service/         # Business logic
│       │   ├── infrastructure/
│       │   │   ├── messaging/       # Kafka producers/consumers
│       │   │   └── rest/            # External API clients
│       │   └── interfaces/
│       │       └── rest/            # REST controllers
│       ├── src/main/resources/
│       │   └── application.yml      # Configuration
│       └── Dockerfile
├── Frontends/
│   └── country-support-dashboard-nextjs/
│       ├── src/
│       │   ├── app/                 # Next.js app directory
│       │   ├── dash/                # Dashboard pages
│       │   ├── components/          # React components
│       │   ├── lib/                 # Utilities and API client
│       │   └── types/               # TypeScript types
│       ├── Dockerfile
│       └── package.json
├── docker-compose.yml
└── README.md
```

## Quick Start

### Prerequisites

- Java 17+
- Node.js 18+
- Docker & Docker Compose
- PostgreSQL 14+
- Kafka 3.6+
- Redis 7+

### Using Docker Compose

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f backend

# Stop services
docker-compose down
```

### Backend Setup

```bash
cd backend/country-support-dashboard-service

# Build with Maven
./mvnw clean package

# Run with Spring Boot
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080/api/v1`

### Frontend Setup

```bash
cd Frontends/country-support-dashboard-nextjs

# Install dependencies
npm install

# Run development server
npm run dev

# Build for production
npm run build
npm start
```

The dashboard will be available at `http://localhost:3000`

## API Documentation

Once the backend is running, visit:
- Swagger UI: `http://localhost:8080/api/v1/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api/v1/api-docs`

### Key Endpoints

#### Tickets
- `GET /tickets` - List all tickets with pagination
- `POST /tickets` - Create a new ticket
- `GET /tickets/{id}` - Get ticket by ID
- `PUT /tickets/{id}` - Update ticket
- `POST /tickets/{id}/assign` - Assign ticket to agent
- `POST /tickets/{id}/close` - Close ticket
- `POST /tickets/{id}/escalate` - Escalate to HQ

#### Dashboard
- `GET /dashboard/metrics` - Get dashboard metrics
- `GET /dashboard/sla` - Get SLA compliance data
- `GET /dashboard/team-performance` - Get team performance

#### Agents
- `GET /agents` - List all agents
- `GET /agents/{id}` - Get agent by ID
- `GET /agents/available` - Get available agents
- `PUT /agents/{id}/status` - Update agent status

#### Escalations
- `GET /escalations` - List all escalations
- `POST /escalations/{id}/resolve` - Resolve escalation
- `POST /escalations/{id}/cancel` - Cancel escalation

#### Knowledge Base
- `GET /knowledge-base/search` - Search articles
- `GET /knowledge-base/articles/{id}` - Get article by ID
- `POST /knowledge-base/articles` - Create article

## Configuration

### Environment Variables

#### Backend
```bash
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=jdbc:postgresql://localhost:5432/country_support_db
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=postgres
REDIS_HOST=localhost
REDIS_PORT=6379
KAFKA_BOOTSTRAP_SERVERS=localhost:9092
SUPPORT_COUNTRY_CODE=US
SUPPORT_COUNTRY_NAME="United States"
HQ_SUPPORT_URL=http://localhost:8081/api/v1
```

#### Frontend
```bash
NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1
```

## Kafka Topics

The service produces events to the following topics:
- `country-support.metrics` - Dashboard metrics
- `country-support.tickets` - Ticket lifecycle events
- `country-support.sla` - SLA compliance data
- `country-support.escalations` - Escalation events

## Testing

### Backend Tests

```bash
cd backend/country-support-dashboard-service

# Run all tests with coverage
./mvnw clean test

# Generate coverage report
./mvnw jacoco:report
```

### Frontend Tests

```bash
cd Frontends/country-support-dashboard-nextjs

# Run tests
npm test

# Run tests with coverage
npm run test:coverage
```

## HQ Integration

### Reporting to HQ

The service automatically reports the following to HQ:
- Ticket metrics (created, resolved, escalated)
- SLA compliance data
- Escalation requests and status updates
- Customer satisfaction scores

### HQ APIs Called

- `POST /hq-support/escalations` - Create escalation at HQ
- `GET /hq-support/escalations/{number}/status` - Get escalation status
- `GET /hq-support/policies` - Get support policies
- `GET /hq-support/sla/targets` - Get SLA targets
- `GET /hq-support/knowledge-base/articles` - Get KB articles from HQ

## Development

### Code Style

- Java: Follow Google Java Style Guide
- TypeScript: Follow Airbnb Style Guide
- Use meaningful variable and function names
- Add JSDoc/JavaDoc comments for public APIs

### Commit Messages

Follow conventional commits:
```
feat: add ticket escalation feature
fix: resolve SLA calculation bug
docs: update API documentation
test: add unit tests for ticket service
```

## Production Deployment

### Build Docker Images

```bash
# Backend
docker build -t country-support-backend:latest ./backend/country-support-dashboard-service

# Frontend
docker build -t country-support-frontend:latest ./Frontends/country-support-dashboard-nextjs
```

### Environment Setup

Ensure the following environment variables are set:

1. Database connection (PostgreSQL)
2. Redis connection
3. Kafka bootstrap servers
4. HQ Support service URL
5. Country-specific settings

### Health Checks

- Backend: `/api/v1/actuator/health`
- Frontend: `/api/health`

## License

Proprietary - Gogidix Internal

## Support

For support, contact the Gogidix Platform Team at platform@gogidix.com
