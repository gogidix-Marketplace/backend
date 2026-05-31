# Transaction Orchestration System

A production-ready microservices-based transaction orchestration system built with Spring Boot, PostgreSQL, Apache Kafka, and React.

## Architecture

### Backend Services (Spring Boot 3.1.5 + Java 17)

1. **Audit Trail Service** (Port 8081)
   - Tracks all transaction events with comprehensive audit logging
   - Kafka consumer for real-time event ingestion
   - Full-text search and filtering capabilities
   - REST API for querying audit trails

2. **Onboarding Tracker Service** (Port 8082)
   - Manages merchant/user onboarding workflows
   - State machine implementation for onboarding stages
   - Idempotency handling for duplicate requests
   - Stage history tracking

3. **Progress Step Service** (Port 8083)
   - Manages transaction progress steps
   - Workflow engine with parallel execution support
   - Retry and compensation logic
   - Timeout detection

4. **Status Broadcast Service** (Port 8084)
   - WebSocket-based real-time status updates
   - Subscriber management
   - Event filtering and routing
   - Connection lifecycle management

5. **Transaction Monitoring Service** (Port 8085)
   - Metrics collection and aggregation
   - Alert engine with configurable thresholds
   - Dashboard data provider
   - Health monitoring

### Frontend (React 18 + TypeScript + Vite)

- Modern, responsive dashboard UI
- Real-time updates via WebSocket
- Transaction list with filtering and pagination
- Detailed transaction view with audit trail
- Onboarding progress visualization
- Monitoring dashboard with charts

### Infrastructure

- PostgreSQL 16 for data persistence
- Apache Kafka for event streaming
- Docker Compose for orchestration

## Quick Start

### Prerequisites

- Docker and Docker Compose installed
- Java 17+ (for local development)
- Maven 3.9+ (for local development)
- Node.js 20 (for frontend development)

### Running with Docker Compose

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

Services will be available at:
- Web Dashboard: http://localhost:3000
- Audit Trail API: http://localhost:8081
- Onboarding Tracker API: http://localhost:8082
- Progress Step API: http://localhost:8083
- Status Broadcast WebSocket: ws://localhost:8084/ws/status
- Transaction Monitoring API: http://localhost:8085
- PostgreSQL: localhost:5432
- Kafka: localhost:9092

## Local Development

For local development without Docker, see **README-LOCAL.md** for quick setup.

### Prerequisites Check

```bash
# Check Java version (17+)
java -version

# Check Maven (3.9+)
mvn -version

# Check Node.js (20+)
node -v
```

### Install Maven (if needed)

Run: `install-maven-only.bat` (as Administrator)

### Backend Services

```bash
# Start all services at once
start-all-services.bat

# Or start individually:
cd Backend/Java/<service-name>
mvn clean install
mvn spring-boot:run
```

### Frontend

```bash
cd Frontends/Web
npm install
npm run dev
```

## API Documentation

### Audit Trail Service

- `POST /api/v1/audit-logs` - Create audit log
- `GET /api/v1/audit-logs/{id}` - Get audit log by ID
- `GET /api/v1/audit-logs/transaction/{transactionId}` - Get logs by transaction
- `POST /api/v1/audit-logs/search` - Search audit logs
- `GET /api/v1/audit-logs/correlation/{correlationId}` - Get logs by correlation ID

### Onboarding Tracker Service

- `POST /api/v1/onboarding` - Create onboarding
- `GET /api/v1/onboarding/{id}` - Get onboarding by ID
- `POST /api/v1/onboarding/{id}/transition` - Transition to next stage
- `GET /api/v1/onboarding/{id}/history` - Get stage history
- `GET /api/v1/onboarding/pending` - Get pending onboardings

### Progress Step Service

- `POST /api/v1/progress-steps` - Create step
- `POST /api/v1/progress-steps/{id}/start` - Start step
- `POST /api/v1/progress-steps/{id}/complete` - Complete step
- `GET /api/v1/progress-steps/transaction/{transactionId}` - Get transaction steps
- `GET /api/v1/progress-steps/transaction/{transactionId}/summary` - Get execution summary

### Transaction Monitoring Service

- `POST /api/v1/monitoring/metrics` - Record metric
- `POST /api/v1/monitoring/alerts` - Create alert
- `GET /api/v1/monitoring/dashboard` - Get dashboard data
- `GET /api/v1/monitoring/alerts/open` - Get open alerts
- `PUT /api/v1/monitoring/alerts/{id}/acknowledge` - Acknowledge alert

## WebSocket Events

### Connection

```javascript
const ws = new WebSocket('ws://localhost:8084/ws/status');

// Subscribe to transaction updates
ws.send(JSON.stringify({
  action: 'SUBSCRIBE',
  transactionId: 'uuid-here'
}));
```

### Event Types

- `CONNECTED` - Connection established
- `STATUS_UPDATE` - Transaction status changed
- `STEP_COMPLETED` - Progress step completed
- `ALERT_CREATED` - New alert created

## Configuration

Environment variables can be set in `docker-compose.yml` or application `.yml` files:

- `DB_URL` - Database JDBC URL
- `DB_USERNAME` - Database username
- `DB_PASSWORD` - Database password
- `KAFKA_BOOTSTRAP_SERVERS` - Kafka broker addresses

## Production Considerations

1. **Security**: Enable TLS/SSL for all services
2. **Authentication**: Implement JWT/OAuth2
3. **Monitoring**: Add Prometheus/Grafana
4. **Logging**: Centralized logging (ELK stack)
5. **Scaling**: Use Kubernetes for orchestration
6. **Database**: Configure connection pooling and backups
7. **Kafka**: Configure replication and retention policies

## License

Copyright © 2025 Gogidix Ecosystem
