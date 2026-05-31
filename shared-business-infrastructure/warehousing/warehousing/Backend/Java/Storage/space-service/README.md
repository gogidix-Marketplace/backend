# Space Service

Multi-tenant storage space allocation service for the Gogidix ecosystem.

## Overview

The Space Service manages physical storage spaces within warehouse facilities, including:
- Storage space allocation by size, duration, and requirements
- Multiple storage types (general, climate-controlled, bonded, hazardous)
- Real-time availability tracking
- Space utilization optimization
- Availability calendar

## Technology Stack

- Java 17
- Spring Boot 3.1.5
- MongoDB 6.0
- Maven
- Testcontainers

## Database

MongoDB is used for persistence with the following collections:
- `storage_spaces` - Physical storage spaces with capacity tracking

## Build

```bash
mvn clean compile
```

## Run Tests

```bash
# Unit tests
mvn test

# With coverage
mvn test jacoco:report
```

## Package

```bash
mvn clean package
```

## Run Service

```bash
java -jar target/space-service-1.0.0.jar
```

## Configuration

Application configuration in `src/main/resources/application.yml`:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017
      database: shared_warehousing
      auto-index-creation: true

server:
  port: 8082
```

## API Endpoints

### Storage Space Management

- `POST /api/v1/storage/spaces` - Create storage space
- `GET /api/v1/storage/spaces?tenantId={id}` - List all spaces
- `GET /api/v1/storage/spaces/{id}` - Get space details
- `PUT /api/v1/storage/spaces/{id}` - Update space
- `POST /api/v1/storage/spaces/{id}/allocate` - Allocate space to customer
- `PUT /api/v1/storage/spaces/{id}/optimize` - Optimize space utilization

### Utilization Reports

- `GET /api/v1/storage/utilization?tenantId={id}` - Get utilization report

### Health Check

- `GET /health` - Service health status

## API Examples

### Create Storage Space

```bash
curl -X POST http://localhost:8082/api/v1/storage/spaces \
  -H "Content-Type: application/json" \
  -d '{
    "tenantId": "tenant-123",
    "spaceCode": "SPACE-001",
    "spaceType": "GENERAL",
    "lengthMeters": 5.0,
    "widthMeters": 3.0,
    "heightMeters": 2.5,
    "basePricePerDay": 50.00,
    "currency": "USD",
    "facilityZone": "ZONE-A",
    "shelfLevel": "LEVEL-1",
    "binNumber": "BIN-001"
  }'
```

### Allocate Space

```bash
curl -X POST http://localhost:8082/api/v1/storage/spaces/{id}/allocate \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "customer-001",
    "requiredCapacityCubicMeters": 10.0,
    "notes": "Store electronics"
  }'
```

### Get Utilization Report

```bash
curl http://localhost:8082/api/v1/storage/utilization?tenantId=tenant-123
```

## Test Coverage

Unit tests: >80% coverage
- Repository layer tests
- Service layer tests
- Integration tests with Testcontainers

## Features

### Multi-Tenancy
All entities include `tenantId` field for data isolation.

### Real-time Availability
Track available capacity and slots in real-time.

### Space Optimization
Automatic slot consolidation for efficient space utilization.

### Utilization Reports
Detailed statistics on space usage by type and zone.
