# Infrastructure DevTools Service

A comprehensive development platform providing essential tools for developers working within the Gogidix ecosystem.

## Features

- **Developer Portal/Dashboard** - Centralized dashboard for all development tools
- **API Testing Tools** - Create, manage, and execute REST API tests
- **Database Query Tools** - Safe database query execution with saved queries library
- **Logging and Debugging** - Centralized log aggregation and search
- **Deployment Tools** - Streamlined deployment management with rollback support
- **Documentation Generator** - Automatic documentation generation from source code

## Quick Start

### Prerequisites

- Java 17+
- Node.js 18+
- PostgreSQL 16+
- Redis 7+
- Docker (optional)

### Using Docker Compose

```bash
docker-compose up -d
```

Access the dashboard at http://localhost:3000
API available at http://localhost:8080/api/devtools

### Manual Setup

#### Backend

```bash
cd Backend/Java/infrastructure-devtools-service
mvn spring-boot:run
```

#### Frontend

```bash
cd Frontends/Web/infrastructure-devtools-frontend
npm install
npm run dev
```

## Documentation

- [Architecture](docs/ARCHITECTURE.md) - System architecture and design
- [API Reference](docs/API.md) - Complete API documentation
- [Business Use Cases](docs/BUSINESS_USE_CASES.md) - Use cases and business value
- [Certification](docs/CERTIFICATION.md) - Production readiness certification

## API Endpoints

### Health Check
```bash
curl http://localhost:8080/api/devtools/health
```

### Available Tools
```bash
curl http://localhost:8080/api/devtools/tools
```

### API Testing
```bash
# List test cases
curl http://localhost:8080/api/devtools/api-testing/test-cases?projectId=default

# Create test case
curl -X POST http://localhost:8080/api/devtools/api-testing/test-cases \
  -H "Content-Type: application/json" \
  -d '{"name":"My Test","method":"GET","url":"https://api.example.com/test","expectedStatusCode":200,"projectId":"default"}'
```

## Development

### Running Tests

**Backend:**
```bash
cd Backend/Java/infrastructure-devtools-service
mvn test
```

**Frontend:**
```bash
cd Frontends/Web/infrastructure-devtools-frontend
npm test
```

### Building

**Backend:**
```bash
mvn package
```

**Frontend:**
```bash
npm run build
```

## Configuration

Key configuration options in `application.yml`:

```yaml
devtools:
  api-testing:
    timeout: 30000
    max-concurrent: 10
  database:
    query-timeout: 60000
    max-rows: 1000
  logging:
    retention-days: 7
  deployment:
    timeout: 300000
```

## Technology Stack

**Backend:**
- Spring Boot 3.2.0
- Java 17
- PostgreSQL 16
- Redis 7
- Kafka 3.6

**Frontend:**
- React 18
- Material-UI 5
- Vite 5
- TanStack Query

## Contributing

1. Create a feature branch
2. Make your changes
3. Add tests (80%+ coverage required)
4. Submit a pull request

## License

MIT License - see LICENSE file for details
