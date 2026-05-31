# Wave 3 Services Implementation Summary

## Overview

This document summarizes the implementation of 4 Wave 3 microservices for the Gogidix Courier Services ecosystem. All services follow a complete Domain-Driven Design (DDD) architecture with distinct layers: Domain, Application, Infrastructure, and Interface.

## Services Implemented

### 1. Availability Service (Port 8106)
**Location:** `Driver/availability-service`

**Purpose:** Manages driver availability, slots, and unavailable periods.

**Key Entities:**
- `DriverAvailability` - Main aggregate for driver availability
- `AvailabilitySlot` - Individual time slots for availability
- `UnavailablePeriod` - Records when drivers are unavailable

**Key Endpoints:**
- `POST /availability` - Create driver availability
- `GET /availability/{id}` - Get availability by ID
- `GET /availability/driver/{driverId}/date/{date}` - Get by driver and date
- `GET /availability/available` - Find available drivers
- `POST /availability/slots` - Add availability slot
- `POST /availability/slots/{slotId}/book` - Book a slot
- `POST /availability/slots/{slotId}/release` - Release a slot
- `POST /availability/unavailable-periods` - Add unavailable period
- `POST /availability/drivers/{driverId}/location` - Update driver location

**Key Files:**
- Domain: `DriverAvailability.java`, `AvailabilitySlot.java`, `UnavailablePeriod.java`
- Application: `AvailabilityApplicationService.java`, `AvailabilityMapper.java`
- Infrastructure: MongoDB repositories, Kafka config
- Interface: `AvailabilityController.java`, `HealthController.java`

---

### 2. Performance Service (Port 8107)
**Location:** `Driver/performance-service`

**Purpose:** Tracks driver performance metrics and scores.

**Key Entities:**
- `DriverPerformance` - Main performance aggregate
- `PerformanceMetric` - Individual metric values
- `PerformanceReview` - Driver reviews

**Key Endpoints:**
- `POST /performance` - Create/update performance record
- `GET /performance/{id}` - Get performance by ID
- `GET /performance/driver/{driverId}` - Get driver performance
- `GET /performance/driver/{driverId}/metrics` - Get metrics
- `POST /performance/calculate` - Calculate performance score
- `POST /performance/reviews` - Add performance review
- `GET /performance/top` - Get top performers

**Key Features:**
- Performance scoring with configurable weights
- Performance tiers (Excellent, Good, Average, Poor, Unsatisfactory)
- Track metrics: on-time rate, completion rate, ratings, route efficiency
- Reviews from customers, admins, dispatchers

**Key Files:**
- Domain: `DriverPerformance.java`, `PerformanceMetric.java`, `PerformanceReview.java`

---

### 3. Load Balancing Service (Port 8103)
**Location:** `Dispatch/load-balancing-service`

**Purpose:** Distributes work among drivers using various strategies.

**Key Entities:**
- `LoadBalancingRule` - Rules for load distribution
- `DriverCapacity` - Tracks driver capacity and load
- `WorkDistribution` - Records of distributed work

**Key Endpoints:**
- `POST /balancing/rules` - Create load balancing rule
- `GET /balancing/rules` - List rules
- `POST /balancing/distribute` - Distribute work
- `GET /balancing/status` - Get balancing status
- `GET /balancing/capacity/{zoneId}` - Get zone capacity
- `POST /balancing/capacity/drivers/{driverId}` - Update driver capacity

**Balancing Strategies:**
- ROUND_ROBIN
- LEAST_LOADED
- WEIGHTED
- GEOGRAPHIC
- PERFORMANCE_BASED
- CUSTOM

**Rule Types:**
- ZONE_BASED
- CAPACITY_BASED
- PERFORMANCE_BASED
- PROXIMITY_BASED
- TIME_BASED
- CUSTOM

**Key Files:**
- Domain: `LoadBalancingRule.java`, `DriverCapacity.java`, `WorkDistribution.java`

---

### 4. Location Service (Port 8110)
**Location:** `Tracking/location-service`

**Purpose:** Tracks driver location updates and geofence events.

**Key Entities:**
- `LocationEvent` - Individual location updates
- `LocationAggregate` - Aggregated location data
- `GeofenceEvent` - Geofence entry/exit events

**Key Endpoints:**
- `POST /locations/batch` - Batch location updates
- `GET /locations/drivers/{driverId}/history` - Get location history
- `GET /locations/drivers/{driverId}/current` - Get current location
- `GET /locations/nearby` - Find nearby drivers
- `GET /geofence/events` - Get geofence events
- `GET /geofence/drivers/{driverId}/events` - Get driver geofence events

**Location Features:**
- Batch location update support
- Location history with retention
- Geofence entry/exit/dwell detection
- Distance calculations
- Speed and heading tracking

**Key Files:**
- Domain: `LocationEvent.java`, `LocationAggregate.java`, `GeofenceEvent.java`

---

## Common Architecture Patterns

### Domain Layer
- Entities with business logic methods
- Value objects for nested data
- Domain events for state changes
- Repository interfaces

### Application Layer
- Commands for write operations
- Queries for read operations
- DTOs for request/response
- Mappers for entity-DTO conversion
- Application services orchestrating use cases

### Infrastructure Layer
- MongoDB repository implementations
- Kafka configuration for events
- Caching configuration
- Web configuration

### Interface Layer
- REST controllers with OpenAPI annotations
- Global exception handling
- Health check endpoints
- Request context handling

---

## Configuration Files

Each service includes:
1. **pom.xml** - Maven configuration with dependencies
2. **application.yml** - Spring Boot configuration
3. **Dockerfile** - Container build instructions

### Default Ports
- availability-service: 8106
- performance-service: 8107
- load-balancing-service: 8103
- location-service: 8110

### Dependencies
- Spring Boot 3.1.5
- Spring Data MongoDB
- Spring Kafka
- OpenAPI (SpringDoc)
- Lombok
- Testcontainers for testing

---

## Database Collections

### availability-service
- `driver_availability`
- `availability_slots`
- `unavailable_periods`

### performance-service
- `driver_performance`
- `performance_metrics`
- `performance_reviews`

### load-balancing-service
- `load_balancing_rules`
- `driver_capacity`
- `work_distribution`

### location-service
- `location_events`
- `location_aggregates`
- `geofence_events`

---

## Kafka Topics

- `availability-events` - Availability state changes
- `performance-events` - Performance updates
- `load-balance-events` - Load balancing events
- `location-events` - Location updates

---

## Testing

Each service includes:
1. Unit tests for domain entities
2. Unit tests for application services
3. Integration tests with Testcontainers
4. Test coverage with JaCoCo

---

## Deployment

### Docker Build
```bash
# For each service
mvn clean package -DskipTests
docker build -t gogidix/{service-name}:latest .
```

### Environment Variables Required
- `MONGODB_HOST`
- `MONGODB_PORT`
- `MONGODB_DB`
- `MONGODB_USER`
- `MONGODB_PASSWORD`
- `KAFKA_SERVERS`
- `SERVICE_PORT` (optional, has default)

---

## API Documentation

Once running, Swagger UI is available at:
- http://localhost:8106/api/v1/swagger-ui.html (Availability)
- http://localhost:8107/api/v1/swagger-ui.html (Performance)
- http://localhost:8103/api/v1/swagger-ui.html (Load Balancing)
- http://localhost:8110/api/v1/swagger-ui.html (Location)

---

## File Count Summary

Total files created across all 4 services:
- **60 Java source files**
- **4 pom.xml files**
- **4 application.yml files**
- **4 Dockerfile files**

Total: **72 files** implementing complete DDD architecture for all 4 Wave 3 services.
