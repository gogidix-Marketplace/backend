# Driver Pool Service

Multi-tenant driver pool management service with real-time geospatial tracking capabilities.

## Overview

This service manages driver profiles, locations, and availability status with MongoDB geospatial indexing for efficient nearby driver searches. It provides a comprehensive REST API for driver lifecycle management and is fully containerized for production deployment.

## Features

- **Multi-tenant Architecture**: Complete tenant isolation with tenantId-based data segregation
- **Geospatial Indexing**: MongoDB 2dsphere indexing for real-time location queries
- **Driver Management**: Create, update, and manage driver profiles
- **Real-time Location Tracking**: Update and query driver locations
- **Status Management**: Track driver availability (ONLINE, OFFLINE, BUSY, ON_BREAK)
- **Performance Metrics**: Track driver ratings and delivery counts
- **Nearby Driver Search**: Find available drivers within specified radius
- **OpenAPI Documentation**: Interactive API documentation at `/swagger-ui.html`
- **Kubernetes Ready**: Liveness and readiness probes for orchestration

## Technology Stack

- **Java 17** - Modern Java with enhanced performance and security
- **Spring Boot 3.1.5** - Enterprise application framework
- **Spring Data MongoDB** - NoSQL data persistence with geospatial support
- **SpringDoc OpenAPI 2.2.0** - API documentation generation
- **Lombok** - Boilerplate reduction
- **Testcontainers** - Integration testing with real containers
- **Maven** - Build and dependency management

## Table of Contents

- [Quick Start](#quick-start)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
- [Database Schema](#database-schema)
- [Running Locally](#running-locally)
- [Testing](#testing)
- [Building](#building)
- [Docker Deployment](#docker-deployment)
- [Health Checks](#health-checks)
- [Kubernetes Deployment](#kubernetes-deployment)
- [Multi-Tenancy](#multi-tenancy)

## Quick Start

### Prerequisites

- Java 17 or later
- Maven 3.9+
- MongoDB 6.0+
- Kafka (optional, for event publishing)

### Running the Service

```bash
# Clone the repository
cd driver-pool-service

# Run with default configuration (MongoDB on localhost:27017)
mvn spring-boot:run

# Or build and run the JAR
mvn clean package
java -jar target/driver-pool-service-1.0.0.jar
```

The service will start on port 8084.

## Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SERVER_PORT` | Service port | 8084 |
| `MONGODB_URI` | MongoDB connection URI | mongodb://localhost:27017 |
| `MONGODB_DATABASE` | Database name | shared_courier |
| `KAFKA_BROKERS` | Kafka bootstrap servers | localhost:9092 |

### application.yml

```yaml
spring:
  application:
    name: driver-pool-service
  data:
    mongodb:
      uri: ${MONGODB_URI:mongodb://localhost:27017}
      database: ${MONGODB_DATABASE:shared_courier}
      auto-index-creation: true
  kafka:
    bootstrap-servers: ${KAFKA_BROKERS:localhost:9092}
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer

server:
  port: ${SERVER_PORT:8084}

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: always

# SpringDoc OpenAPI Configuration
springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
    tags-sorter: alpha
    operations-sorter: alpha

logging:
  level:
    root: INFO
    com.gogidix.shared.courier.driver: DEBUG
```

## API Endpoints

### Base URL
```
http://localhost:8084/api/v1/drivers
```

### Authentication Header
All endpoints require the tenant identifier header:
```
X-Tenant-ID: tenant-001
```

### Driver Management

#### Create Driver
```http
POST /api/v1/drivers
X-Tenant-ID: tenant-001
Content-Type: application/json

{
  "driverId": "driver-001",
  "fullName": "John Doe",
  "email": "john@example.com",
  "phone": "+1234567890",
  "vehicleType": "CAR",
  "licensePlate": "ABC-123"
}
```

**Response:** `201 Created`
```json
{
  "id": "507f1f77bcf86cd799439011",
  "tenantId": "tenant-001",
  "driverId": "driver-001",
  "fullName": "John Doe",
  "email": "john@example.com",
  "phone": "+1234567890",
  "status": "OFFLINE",
  "currentLocation": null,
  "vehicleType": "CAR",
  "licensePlate": "ABC-123",
  "rating": 0.0,
  "totalDeliveries": 0,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

#### Get Driver
```http
GET /api/v1/drivers/{driverId}
X-Tenant-ID: tenant-001
```

#### Update Driver Location
```http
PUT /api/v1/drivers/{driverId}/location
X-Tenant-ID: tenant-001
Content-Type: application/json

{
  "latitude": 40.7484,
  "longitude": -73.9857
}
```

#### Update Driver Status
```http
PUT /api/v1/drivers/{driverId}/status
X-Tenant-ID: tenant-001
Content-Type: application/json

{
  "status": "ONLINE"
}
```

**Valid statuses:** `ONLINE`, `OFFLINE`, `BUSY`, `ON_BREAK`

#### Find Nearby Drivers
```http
GET /api/v1/drivers/nearby?latitude=40.7484&longitude=-73.9857&radiusKm=5.0
X-Tenant-ID: tenant-001
```

#### Find Drivers by Status
```http
GET /api/v1/drivers/status/ONLINE
X-Tenant-ID: tenant-001
```

#### Update Driver Rating
```http
POST /api/v1/drivers/{driverId}/rating?rating=5.0
X-Tenant-ID: tenant-001
```

#### Delete Driver
```http
DELETE /api/v1/drivers/{driverId}
X-Tenant-ID: tenant-001
```

**Response:** `204 No Content`

### Health Endpoints

#### General Health Check
```http
GET /health
```

#### Liveness Probe (Kubernetes)
```http
GET /health/liveness
```

#### Readiness Probe (Kubernetes)
```http
GET /health/readiness
```

### API Documentation
Interactive API documentation available at:
- Swagger UI: http://localhost:8084/swagger-ui.html
- OpenAPI JSON: http://localhost:8084/v3/api-docs

## Database Schema

### DriverProfile Document

```java
@Document(collection = "driver_profiles")
@CompoundIndex(def = "{'tenantId': 1, 'driverId': 1}", unique = true)
public class DriverProfile {
    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String driverId;

    private String fullName;
    private String email;
    private String phone;

    private DriverStatus status; // ONLINE, OFFLINE, BUSY, ON_BREAK

    // GEOSPATIAL INDEX for real-time location tracking
    @GeoSpatialIndexed(type = GeoJsonIndexType.GEO_2DSPHERE)
    private GeoJsonPoint currentLocation;

    private String vehicleType;
    private String licensePlate;

    // Performance metrics
    private Double rating;
    private Integer totalDeliveries;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

### Indexes

1. **Compound Index**: `{tenantId: 1, driverId: 1}` (unique)
2. **Tenant Index**: `{tenantId: 1}`
3. **Status Index**: `{tenantId: 1, status: 1}`
4. **Geospatial Index**: `currentLocation` (2dsphere)

## Running Locally

### Using Maven

```bash
mvn spring-boot:run
```

### Using Docker Compose

```bash
docker-compose up -d
```

### Accessing the Service

- Service URL: http://localhost:8084
- Swagger UI: http://localhost:8084/swagger-ui.html
- Actuator Health: http://localhost:8084/actuator/health

## Testing

### Unit Tests

```bash
mvn test
```

### Integration Tests (with Testcontainers)

```bash
mvn verify
```

### Running Specific Tests

```bash
mvn test -Dtest=DriverPoolServiceTest
mvn test -Dtest=DriverControllerTest
mvn test -Dtest=DriverPoolServiceIntegrationTest
```

### Test Coverage

- **Unit Tests**: Service layer, repository layer
- **Integration Tests**: Full stack with Testcontainers (MongoDB)
- **Controller Tests**: REST endpoint validation

## Building

### Standard Build

```bash
mvn clean package
```

### Build Without Tests

```bash
mvn clean package -DskipTests
```

### Build Output

The executable JAR is created at:
```
target/driver-pool-service-1.0.0.jar
```

## Docker Deployment

### Build Image

```bash
docker build -t driver-pool-service:1.0.0 .
```

### Run Container

```bash
docker run -d \
  -p 8084:8084 \
  -e MONGODB_URI=mongodb://host.docker.internal:27017 \
  -e MONGODB_DATABASE=shared_courier \
  driver-pool-service:1.0.0
```

### Docker Compose Example

```yaml
version: '3.8'
services:
  driver-pool-service:
    build: .
    ports:
      - "8084:8084"
    environment:
      - MONGODB_URI=mongodb://mongodb:27017
      - MONGODB_DATABASE=shared_courier
      - KAFKA_BROKERS=kafka:9092
    depends_on:
      - mongodb
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8084/health/liveness"]
      interval: 30s
      timeout: 5s
      retries: 3

  mongodb:
    image: mongo:6.0
    ports:
      - "27017:27017"
```

## Health Checks

The service provides three health endpoints:

### 1. General Health Check

```bash
curl http://localhost:8084/health
```

Response:
```json
{
  "status": "UP",
  "service": "driver-pool-service",
  "timestamp": "2024-01-15T10:30:00",
  "version": "1.0.0"
}
```

### 2. Liveness Probe (Kubernetes)

```bash
curl http://localhost:8084/health/liveness
```

Response:
```json
{
  "status": "UP",
  "probe": "liveness"
}
```

### 3. Readiness Probe (Kubernetes)

```bash
curl http://localhost:8084/health/readiness
```

Response:
```json
{
  "status": "UP",
  "probe": "readiness",
  "database": "connected"
}
```

### Kubernetes Probe Configuration

```yaml
livenessProbe:
  httpGet:
    path: /health/liveness
    port: 8084
  initialDelaySeconds: 60
  periodSeconds: 30

readinessProbe:
  httpGet:
    path: /health/readiness
    port: 8084
  initialDelaySeconds: 30
  periodSeconds: 10
```

## Kubernetes Deployment

### Deployment Manifest

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: driver-pool-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: driver-pool-service
  template:
    metadata:
      labels:
        app: driver-pool-service
    spec:
      containers:
      - name: driver-pool-service
        image: driver-pool-service:1.0.0
        ports:
        - containerPort: 8084
        env:
        - name: MONGODB_URI
          valueFrom:
            configMapKeyRef:
              name: mongodb-config
              key: uri
        - name: MONGODB_DATABASE
          value: "shared_courier"
        resources:
          requests:
            memory: "512Mi"
            cpu: "250m"
          limits:
            memory: "1Gi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /health/liveness
            port: 8084
          initialDelaySeconds: 60
          periodSeconds: 30
        readinessProbe:
          httpGet:
            path: /health/readiness
            port: 8084
          initialDelaySeconds: 30
          periodSeconds: 10
---
apiVersion: v1
kind: Service
metadata:
  name: driver-pool-service
spec:
  selector:
    app: driver-pool-service
  ports:
  - port: 8084
    targetPort: 8084
  type: ClusterIP
```

## Multi-Tenancy

All operations are scoped to a specific tenant using the `X-Tenant-ID` header:

### Tenant Isolation Features

1. **Database-level Isolation**: All queries include tenantId filtering
2. **Compound Indexes**: Ensure tenant-driver uniqueness
3. **Complete Data Segregation**: No cross-tenant data access

### Example Usage

```bash
# Create a driver for tenant-001
curl -X POST http://localhost:8084/api/v1/drivers \
  -H "X-Tenant-ID: tenant-001" \
  -H "Content-Type: application/json" \
  -d '{"driverId": "driver-001", "fullName": "John Doe", ...}'

# The same driverId can exist for different tenants
curl -X POST http://localhost:8084/api/v1/drivers \
  -H "X-Tenant-ID: tenant-002" \
  -H "Content-Type: application/json" \
  -d '{"driverId": "driver-001", "fullName": "Jane Smith", ...}'
```

## Error Handling

The service uses standardized error responses:

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Invalid request parameters",
  "fieldErrors": {
    "email": "Email must be valid",
    "phone": "Phone must be valid"
  },
  "path": "/api/v1/drivers"
}
```

### Common HTTP Status Codes

| Code | Description |
|------|-------------|
| 200 | Success |
| 201 | Created |
| 204 | No Content |
| 400 | Bad Request |
| 404 | Not Found |
| 409 | Conflict (duplicate) |
| 500 | Internal Server Error |

## Geospatial Queries

The service uses MongoDB's geospatial indexing for efficient nearby driver searches:

```java
@Query("{ 'tenantId': ?0, 'status': ?1, 'currentLocation': { $near: { $geometry: ?2, $maxDistance: ?3 } } }")
List<DriverProfile> findNearbyAvailableDrivers(
    String tenantId,
    DriverStatus status,
    GeoJsonPoint location,
    double maxDistanceMeters
);
```

This enables finding all available drivers within a specified radius from a given point efficiently.

## Monitoring

### Actuator Endpoints

```bash
# Health
curl http://localhost:8084/actuator/health

# Metrics
curl http://localhost:8084/actuator/metrics

# Info
curl http://localhost:8084/actuator/info
```

### Application Logs

```yaml
logging:
  level:
    root: INFO
    com.gogidix.shared.courier.driver: DEBUG
    org.springframework.data.mongodb: DEBUG
```

## License

Copyright (c) 2024 Gogidix. All rights reserved.
