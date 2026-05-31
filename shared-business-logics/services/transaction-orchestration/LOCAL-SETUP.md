# Local Development Setup Guide

This guide will help you run the Transaction Orchestration System locally without Docker.

## Prerequisites

Before running the services, you need to install the following tools:

### 1. Java Development Kit (JDK) 17+

**Already installed on your system!**

**Verify installation**:
```bash
java -version
```

Expected output: `java version "17.x.x"`

### 2. Maven 3.9+

**Quick Install**: Run `install-maven-only.bat` (right-click, Run as Administrator)

**Manual Install**:
1. Download: https://maven.apache.org/download.cgi
2. Download `apache-maven-3.9.6-bin.zip`
3. Extract to: `C:\Program Files\apache-maven-3.9.6`
4. Add `C:\Program Files\apache-maven-3.9.6\bin` to PATH
5. Set MAVEN_HOME environment variable

**Verify installation**:
```bash
mvn -version
```

### 3. Node.js 20+

Already installed on your system (v25.4.0 detected).

## Starting the Services

### Option 1: Start All Services (Windows)

Run the provided batch script:
```bash
start-all-services.bat
```

This will start all 5 Spring Boot services and the React frontend.

### Option 2: Start Services Individually

#### Backend Services

Open 5 separate terminal windows and run:

**Terminal 1 - Audit Trail Service (Port 8081)**:
```bash
cd Backend\Java\audit-trail-service
mvn clean install
mvn spring-boot:run
```

**Terminal 2 - Onboarding Tracker Service (Port 8082)**:
```bash
cd Backend\Java\onboarding-tracker-service
mvn clean install
mvn spring-boot:run
```

**Terminal 3 - Progress Step Service (Port 8083)**:
```bash
cd Backend\Java\progress-step-service
mvn clean install
mvn spring-boot:run
```

**Terminal 4 - Status Broadcast Service (Port 8084)**:
```bash
cd Backend\Java\status-broadcast-service
mvn clean install
mvn spring-boot:run
```

**Terminal 5 - Transaction Monitoring Service (Port 8085)**:
```bash
cd Backend\Java\transaction-monitoring-service
mvn clean install
mvn spring-boot:run
```

#### Frontend (Terminal 6)

```bash
cd Frontends\Web
npm install
npm run dev
```

## Accessing the Services

Once all services are running:

- **Web Dashboard**: http://localhost:3000
- **Audit Trail API**: http://localhost:8081
- **Onboarding Tracker API**: http://localhost:8082
- **Progress Step API**: http://localhost:8083
- **Status Broadcast API**: http://localhost:8084
- **Transaction Monitoring API**: http://localhost:8085
- **H2 Console (Audit)**: http://localhost:8081/h2-console
- **H2 Console (Onboarding)**: http://localhost:8082/h2-console
- **H2 Console (Progress)**: http://localhost:8083/h2-console
- **H2 Console (Status)**: http://localhost:8084/h2-console
- **H2 Console (Monitoring)**: http://localhost:8085/h2-console

## H2 Database Console Access

Each service has an H2 console available at `/h2-console`:

- **JDBC URL**: `jdbc:h2:mem:<database_name>`
- **Username**: `sa`
- **Password**: (leave empty)

Example for Audit Trail Service:
- JDBC URL: `jdbc:h2:mem:audit_trail_db`

## Testing the Services

### Test Audit Trail Service

```bash
curl -X POST http://localhost:8081/api/v1/audit-logs \
  -H "Content-Type: application/json" \
  -d '{
    "transactionId": "test-tx-001",
    "eventType": "TRANSACTION_INITIATED",
    "eventCategory": "TRANSACTION",
    "userId": "user-001"
  }'
```

### Test Onboarding Tracker Service

```bash
curl -X POST http://localhost:8082/api/v1/onboarding \
  -H "Content-Type: application/json" \
  -d '{
    "entityType": "MERCHANT",
    "entityId": "merchant-001",
    "transactionId": "tx-001"
  }'
```

### Test Progress Step Service

```bash
curl -X POST http://localhost:8083/api/v1/progress-steps \
  -H "Content-Type: application/json" \
  -d '{
    "transactionId": "tx-001",
    "stepName": "Identity Verification",
    "stepType": "MANUAL"
  }'
```

### Test Transaction Monitoring Service

```bash
curl -X POST http://localhost:8085/api/v1/monitoring/metrics \
  -H "Content-Type: application/json" \
  -d '{
    "transactionId": "tx-001",
    "metricName": "processing_time",
    "metricValue": 150
  }'
```

## Troubleshooting

### Port Already in Use

If you get "Port already in use" errors:

**Windows (PowerShell)**:
```powershell
# Find process using port 8081
netstat -ano | findstr :8081

# Kill the process (replace PID with actual process ID)
taskkill /PID <PID> /F
```

### Maven Build Fails

- Make sure Java 21 is installed: `java -version`
- Make sure JAVA_HOME is set correctly
- Try cleaning Maven cache: `mvn clean`

### Services Not Starting

- Check that all 5 services can start (look for "Started Application" in logs)
- Each service should start within 30-60 seconds on first run
- H2 databases are in-memory and will be empty on restart

### Frontend Build Errors

- Delete `node_modules` and `package-lock.json`
- Run `npm install` again
- Make sure Node.js version is 20 or higher

## Architecture Notes for Local Development

### Database

- **H2 in-memory databases** are used instead of PostgreSQL
- Each service has its own separate H2 database
- Data is **not persisted** between restarts
- H2 Console is available for each service to browse data

### Message Queue

- **Kafka has been removed** for local development
- Services log events instead of publishing to Kafka
- Real-time status updates via WebSocket are disabled
- All functionality works via REST APIs only

### Inter-Service Communication

- Services communicate via REST APIs in local mode
- No automatic event broadcasting between services
- Manual API calls are needed to trigger dependent actions

## Stopping All Services

Press `Ctrl+C` in each terminal window to stop the services.

Or use the provided script:
```bash
stop-all-services.bat
```

## Next Steps

Once all services are running:

1. Open http://localhost:3000 in your browser
2. The dashboard should show statistics and recent activity
3. Use the navigation to explore:
   - Dashboard - Overview and stats
   - Transactions - List and manage transactions
   - Onboarding - Track onboarding progress
   - Monitoring - View metrics and alerts

## Support

For issues or questions:
- Check the logs in each service's terminal window
- Verify all services are running using the Actuator health endpoint: `http://localhost:<port>/actuator/health`
- Check H2 console to verify database state
