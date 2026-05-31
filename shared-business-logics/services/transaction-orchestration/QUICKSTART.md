# Quick Start Guide - Local Development

## Prerequisites Check

Before starting, verify you have the required tools installed:

```bash
# Check Java version (should be 17+)
java -version

# Check Maven
mvn -version

# Check Node.js (should be 20+)
node -v
```

**Missing Maven?** Run `install-maven-only.bat` or see `INSTALL-GUIDE.md` for installation instructions.

**Note**: Java 17 is already installed on your system.

## Starting the System

### Option A: Start Everything at Once (Recommended)

Double-click: `start-all-services.bat`

This will start all 5 backend services and the React frontend.

### Option B: Start Services Individually

Use these batch scripts to start services one by one:

1. `start-audit-service.bat` - Audit Trail Service (Port 8081)
2. `start-onboarding-service.bat` - Onboarding Tracker (Port 8082)
3. `start-progress-service.bat` - Progress Step Service (Port 8083)
4. `start-status-service.bat` - Status Broadcast (Port 8084)
5. `start-monitoring-service.bat` - Transaction Monitoring (Port 8085)
6. `start-frontend.bat` - React Dashboard (Port 3000)

## Access the Application

Once all services are running (30-60 seconds):

**Web Dashboard**: http://localhost:3000

**API Endpoints**:
- Audit Trail API: http://localhost:8081
- Onboarding Tracker: http://localhost:8082
- Progress Step API: http://localhost:8083
- Status Broadcast: http://localhost:8084
- Transaction Monitoring: http://localhost:8085

**H2 Consoles** (for debugging):
- http://localhost:8081/h2-console
- http://localhost:8082/h2-console
- http://localhost:8083/h2-console
- http://localhost:8084/h2-console
- http://localhost:8085/h2-console

  - JDBC URL: `jdbc:h2:mem:<database_name>`
  - Username: `sa`
  - Password: (leave empty)

## Stopping the System

Double-click: `stop-all-services.bat`

Or press `Ctrl+C` in each service window.

## What's Changed from Docker Version?

### Simplified for Local Development:

✅ **Removed**:
- PostgreSQL → Now using H2 in-memory databases
- Apache Kafka → Event logging instead of messaging
- Docker dependency → Runs directly on your machine

✅ **Still Working**:
- All REST APIs fully functional
- Complete database operations with H2
- React dashboard with all features
- Service health monitoring

⚠️ **Limitations**:
- Data is NOT persisted (H2 is in-memory)
- No real-time WebSocket updates
- No inter-service event broadcasting
- Manual API calls needed for service coordination

## First Run

1. Open 6 terminal windows (or use the batch script)
2. Start all 5 backend services (wait for each to fully start)
3. Start the frontend
4. Open http://localhost:3000
5. You should see the dashboard with empty statistics

## Testing the System

Try these API calls to verify everything works:

```bash
# Create an onboarding
curl -X POST http://localhost:8082/api/v1/onboarding ^
  -H "Content-Type: application/json" ^
  -d "{\"entityType\":\"MERCHANT\",\"entityId\":\"merchant-001\",\"transactionId\":\"tx-001\"}"

# Create a progress step
curl -X POST http://localhost:8083/api/v1/progress-steps ^
  -H "Content-Type: application/json" ^
  -d "{\"transactionId\":\"tx-001\",\"stepName\":\"Verification\",\"stepType\":\"MANUAL\"}"

# Record a metric
curl -X POST http://localhost:8085/api/v1/monitoring/metrics ^
  -H "Content-Type: application/json" ^
  -d "{\"transactionId\":\"tx-001\",\"metricName\":\"processing_time\",\"metricValue\":150}"
```

## Troubleshooting

**Services won't start?**
- Make sure ports 8081-8085 are available
- Check that Java 21 and Maven are installed
- Look at error messages in service windows

**Frontend won't load?**
- Make sure backend services are running first
- Check that port 3000 is available
- Verify Node.js is version 20+

**Need to reset everything?**
1. Stop all services (Ctrl+C or use stop script)
2. Close all terminal windows
3. Start fresh with `start-all-services.bat`

## Need More Details?

See `LOCAL-SETUP.md` for comprehensive setup and troubleshooting information.

## Service Architecture

```
┌─────────────────┐
│  React App      │
│  (Port 3000)    │
└────────┬────────┘
         │
         ▼
┌─────────────────────────────────────────┐
│           REST API Calls                 │
├─────────┬─────────┬─────────┬──────────┤
│  8081   │  8082   │  8083   │  8084/5  │
│  Audit  │Onboard  │Progress │Status/   │
│  Trail  │Tracker  │ Steps   │Monitor   │
└─────────┴─────────┴─────────┴──────────┘
         │         │         │
         ▼         ▼         ▼
┌─────────────────────────────────────────┐
│         H2 In-Memory Databases          │
│    (separate DB per service)            │
└─────────────────────────────────────────┘
```

## Next Steps

1. ✅ Install Java 21 and Maven (if not already installed)
2. ✅ Run `start-all-services.bat`
3. ✅ Open http://localhost:3000
4. ✅ Explore the dashboard and test the APIs
5. 📖 Read `LOCAL-SETUP.md` for detailed documentation

Happy coding! 🚀
