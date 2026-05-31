# Driver Pool Service

Driver management and availability tracking service.

## Overview

The Driver Pool Service manages the fleet of drivers, tracking their availability, status, and performance metrics. It serves as the central registry for all driver-related operations.

## Features

- Driver registration and onboarding
- Real-time availability tracking
- Driver status management
- Performance metrics tracking
- Vehicle information management
- Working hours and schedules
- Driver ratings and feedback

## API Endpoints

### Driver Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/drivers` | List drivers (with filters) |
| GET | `/api/v1/drivers/{id}` | Get driver details |
| POST | `/api/v1/drivers` | Register new driver |
| PUT | `/api/v1/drivers/{id}` | Update driver profile |
| DELETE | `/api/v1/drivers/{id}` | Deactivate driver |

### Status Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| PUT | `/api/v1/drivers/{id}/status` | Update driver status |
| GET | `/api/v1/drivers/{id}/status/history` | Get status history |
| POST | `/api/v1/drivers/{id}/break` | Request break |
| PUT | `/api/v1/drivers/{id}/location` | Update driver location |

### Performance

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/drivers/{id}/performance` | Get performance metrics |
| GET | `/api/v1/drivers/{id}/earnings` | Get earnings summary |
| GET | `/api/v1/drivers/{id}/ratings` | Get driver ratings |

### Fleet Operations

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/fleets` | List fleets |
| GET | `/api/v1/fleets/{id}/drivers` | Get fleet drivers |
| POST | `/api/v1/fleets/{id}/drivers` | Add driver to fleet |

## Configuration

```yaml
spring:
  application:
    name: driver-pool-service
  data:
    mongodb:
      uri: ${MONGODB_URI}
      database: courier_core

server:
  port: 8105

courier:
  driver:
    status-expire-minutes: 15
    max-offline-hours: 24
    rating-decay-days: 30
```

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `MONGODB_URI` | MongoDB connection string | mongodb://localhost:27017 |
| `KAFKA_BOOTSTRAP_SERVERS` | Kafka brokers | localhost:9092 |
| `REDIS_HOST` | Redis host | localhost |
| `STATUS_EXPIRE_MINUTES` | Status expiration time | 15 |

## Driver Status

```
OFFLINE → AVAILABLE → BUSY → AVAILABLE
         ↓           ↓
      ON_BREAK    OFF_DUTY
```

### Status Descriptions

| Status | Description |
|--------|-------------|
| `AVAILABLE` | Driver is available for assignments |
| `BUSY` | Driver is on active dispatch |
| `OFFLINE` | Driver is offline/off duty |
| `ON_BREAK` | Driver is on scheduled break |
| `OFF_DUTY` | Driver is not working |

## Data Model

### Driver Schema

```json
{
  "id": "uuid",
  "tenantId": "uuid",
  "userId": "uuid",
  "status": "AVAILABLE",
  "profile": {
    "firstName": "John",
    "lastName": "Doe",
    "phone": "+1234567890",
    "email": "john@example.com",
    "photo": "https://...",
    "languages": ["en", "es"]
  },
  "vehicle": {
    "type": "CAR",
    "make": "Toyota",
    "model": "Camry",
    "year": 2020,
    "plateNumber": "ABC123",
    "color": "Blue",
    "capacity": {
      "weight": 100,
      "volume": 500
    }
  },
  "currentLocation": {
    "latitude": 40.7128,
    "longitude": -74.0060,
    "lastUpdate": "2026-02-20T14:00:00Z"
  },
  "currentDispatchId": null,
  "performance": {
    "totalDeliveries": 542,
    "onTimeRate": 0.94,
    "averageRating": 4.7,
    "totalEarnings": 5420.00
  },
  "workingHours": {
    "monday": {"start": "08:00", "end": "18:00"},
    "tuesday": {"start": "08:00", "end": "18:00"}
  },
  "createdAt": "2026-01-01T00:00:00Z",
  "updatedAt": "2026-02-20T14:00:00Z"
}
```

## Events

| Event | Topic | Description |
|-------|-------|-------------|
| `driver.registered` | `driver.events` | New driver registered |
| `driver.status.changed` | `driver.events` | Status updated |
| `driver.location.updated` | `driver.events` | Location changed |
| `driver.performance.updated` | `driver.events` | Performance metrics updated |

## Local Development

```bash
# Build and run
mvn clean install
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

## Monitoring

Metrics at `/actuator/prometheus`:

- `drivers_total`: Total registered drivers
- `drivers_by_status`: Current drivers per status
- `driver_location_updates_total`: Location update count
- `driver_status_changes_total`: Status change count

## Dependencies

- Spring Boot 3.x
- Spring Data MongoDB
- Spring Kafka
- Redis
- Location Service Client

## Related Services

- **Assignment Service**: Uses driver availability for assignments
- **GPS Tracking Service**: Receives location updates
- **Performance Service**: Aggregates driver performance
- **Availability Service**: Manages driver schedules

## Support

- GitHub: https://github.com/gogidix/courier-services/issues
- Documentation: https://docs.gogidix.com/courier/driver-pool
