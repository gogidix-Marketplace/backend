# Executive Analytics Service - Build and Deployment Guide

## Overview

This guide provides step-by-step instructions for building, testing, and deploying the **executive-analytics-service**.

## Prerequisites

### Required Software

| Software | Version | Purpose |
|----------|---------|---------|
| Java JDK | 17+ | Runtime environment |
| Maven | 3.9.12+ | Build tool |
| MongoDB | 6.0+ | Database |
| Redis | 7.0+ | Caching (optional for tests) |

### Verify Installation

```bash
# Check Java version
java -version
# Expected output: openjdk version "17.x.x" or higher

# Check Maven version
mvn -version
# Expected output: Apache Maven 3.9.12 or higher

# Check MongoDB connection
mongosh --eval "db.version()"
# Or: mongo --eval "db.version()"

# Check Redis connection
redis-cli ping
# Expected output: PONG
```

## Quick Start

### 1. Clone and Navigate

```bash
cd "C:\Users\TEMP.LAPTOP-1QDBFFCA\Desktop\Gogidix-ecosystem\x-gogidix-domain\Management-domain\Executive-domain\Backend\Java\executive-dashboard-service\Shared\executive-analytics-service"
```

### 2. Build the Service

```bash
# Clean compile (check for syntax errors)
mvn clean compile

# Run all tests
mvn test

# Build with tests
mvn clean build

# Build JAR (skip tests for faster build)
mvn clean package -DskipTests
```

### 3. Run the Service

```bash
# Using Maven
mvn spring-boot:run

# Using JAR
java -jar target/executive-analytics-service-1.0.0.jar
```

### 4. Verify Health

```bash
# Check health endpoint
curl http://localhost:8081/actuator/health

# Expected output:
# {
#   "status": "UP"
# }
```

## Build Phases

### Phase 1: Compile

**Purpose:** Check for compilation errors

```bash
mvn clean compile
```

**Success Indicators:**
- No compilation errors
- `BUILD SUCCESS` message
- `target/classes` directory created

**Common Issues:**
| Issue | Solution |
|-------|----------|
| `java: package org.springframework.boot does not exist` | Ensure dependencies are downloaded: `mvn dependency:resolve` |
| `java: cannot find symbol` | Check imports and ensure all files are saved |

### Phase 2: Test

**Purpose:** Run unit and integration tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=KPITest

# Run specific test method
mvn test -Dtest=KPITest#shouldCreateKPIWithTenantId

# Skip tests
mvn test -DskipTests
```

**Success Indicators:**
- All tests pass
- `Tests run: X, Failures: 0, Errors: 0`
- 75%+ code coverage (check `target/site/jacoco/index.html`)

**Common Issues:**
| Issue | Solution |
|-------|----------|
| `MongoTimeoutException` | Ensure MongoDB is running on localhost:27017 |
| `Tests run: 0` | Check test file naming (must end with `Test.java` or `Tests.java`) |

### Phase 3: Build JAR

**Purpose:** Create executable JAR file

```bash
mvn clean package
```

**Success Indicators:**
- `BUILD SUCCESS`
- `target/executive-analytics-service-1.0.0.jar` created
- JAR size > 50MB (includes dependencies)

### Phase 4: Verify Coverage

**Purpose:** Check test coverage meets 75% threshold

```bash
# Generate coverage report
mvn jacoco:report

# Open report
# Windows: start target/site/jacoco/index.html
# Mac/Linux: open target/site/jacoco/index.html
```

**Success Indicators:**
- Total coverage >= 75%
- All critical packages covered

## Smoke Tests

After deployment, run these smoke tests:

### 1. Health Check

```bash
curl http://localhost:8081/actuator/health
```

### 2. Create KPI

```bash
curl -X POST http://localhost:8081/api/v1/kpi \
  -H "Content-Type: application/json" \
  -H "X-Tenant-ID: tenant-001" \
  -d '{
    "name": "Total Revenue",
    "category": "FINANCIAL",
    "executiveLevel": "CEO",
    "value": 1500000,
    "unit": "$",
    "period": "2024-01",
    "target": 2000000
  }'
```

### 3. Get All KPIs

```bash
curl http://localhost:8081/api/v1/kpi \
  -H "X-Tenant-ID: tenant-001"
```

### 4. Get Dashboard Data

```bash
curl http://localhost:8081/api/v1/kpi/dashboard/CEO \
  -H "X-Tenant-ID: tenant-001"
```

## MongoDB Verification

### Check Collections

```bash
mongosh --eval "
use management_executive;
show collections;
db.kpi_metrics.countDocuments();
db.metrics.countDocuments();
"
```

### Verify Indexes

```bash
mongosh --eval "
use management_executive;
db.kpi_metrics.getIndexes();
db.metrics.getIndexes();
"
```

## Troubleshooting

### Port Already in Use

```bash
# Windows
netstat -ano | findstr :8081
taskkill /PID <PID> /F

# Mac/Linux
lsof -ti:8081 | xargs kill -9
```

### MongoDB Connection Failed

```bash
# Check MongoDB status
# Windows
sc query MongoDB

# Mac/Linux
brew services list | grep mongodb-community
# or
systemctl status mongod

# Start MongoDB
# Windows
net start MongoDB

# Mac/Linux
brew services start mongodb-community
# or
sudo systemctl start mongod
```

### Out of Memory

```bash
# Increase Maven memory
export MAVEN_OPTS="-Xmx2048m -XX:MaxPermSize=512m"

# Or use settings.xml
<settings>
  <profiles>
    <profile>
      <id>maven-compiler</id>
      <properties>
        <maven.compiler.maxHeapSize>2048m</maven.compiler.maxHeapSize>
      </properties>
    </profile>
  </profiles>
  <activeProfiles>
    <activeProfile>maven-compiler</activeProfile>
  </activeProfiles>
</settings>
```

## Production Build

```bash
# Build with production profile
mvn clean package -Pproduction -DskipTests

# Run production JAR
java -jar target/executive-analytics-service-1.0.0.jar --spring.profiles.active=production
```

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SERVICE_PORT` | Service port | 8081 |
| `MONGO_HOST` | MongoDB host | localhost |
| `MONGO_PORT` | MongoDB port | 27017 |
| `MONGO_DB` | MongoDB database | management_executive |
| `REDIS_HOST` | Redis host | localhost |
| `REDIS_PORT` | Redis port | 6379 |
| `KAFKA_SERVERS` | Kafka bootstrap servers | localhost:9092 |

## CI/CD Integration

### GitHub Actions Example

```yaml
name: Build and Test

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v3

    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
        cache: 'maven'

    - name: Start MongoDB
      uses: supercharge/mongodb-github-action@v1.10.0
      with:
        mongodb-version: '6.0'

    - name: Build with Maven
      run: mvn clean build

    - name: Generate Coverage Report
      run: mvn jacoco:report

    - name: Upload Coverage
      uses: codecov/codecov-action@v3
```

## Next Steps

1. ✅ Verify build succeeds
2. ✅ Verify all tests pass
3. ✅ Verify coverage >= 75%
4. ✅ Run smoke tests
5. ✅ Verify MongoDB collections created
6. ✅ Deploy to staging environment

---

**Last Updated:** 2026-01-31
**Status:** Ready for build verification
