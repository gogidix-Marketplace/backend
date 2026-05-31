# Dispatch Core Service

Central dispatch management service for the Courier Services platform.

## Overview

The Dispatch Core Service is the heart of the courier platform, managing the entire lifecycle of delivery dispatches from creation to completion. It orchestrates assignments, tracking, and status updates.

## Features

- Dispatch creation and management
- Real-time status tracking
- Driver assignment orchestration
- Route optimization integration
- ETA calculation coordination
- Multi-stop dispatch support
- Dispatch history and analytics

## API Endpoints

### Dispatch Operations

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/dispatches` | Create new dispatch |
| GET | `/api/v1/dispatches/{id}` | Get dispatch details |
| PUT | `/api/v1/dispatches/{id}` | Update dispatch |
| DELETE | `/api/v1/dispatches/{id}` | Cancel dispatch |
| GET | `/api/v1/dispatches` | List dispatches (with filters) |

### Dispatch Actions

| Method | Endpoint | Description |
|--------|----------|-------------|
| PUT | `/api/v1/dispatches/{id}/assign` | Assign driver |
| PUT | `/api/v1/dispatches/{id}/status` | Update status |
| POST | `/api/v1/dispatches/{id}/reroute` | Request re-route |
| GET | `/api/v1/dispatches/{id}/timeline` | Get dispatch timeline |

### Bulk Operations

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/dispatches/bulk` | Create multiple dispatches |
| PUT | `/api/v1/dispatches/bulk/status` | Update multiple statuses |
| GET | `/api/v1/dispatches/bulk/summary` | Get batch summary |

## Configuration

### Application Properties

```yaml
spring:
  application:
    name: dispatch-core-service
  data:
    mongodb:
      uri: ${MONGODB_URI:mongodb://localhost:27017}
      database: ${MONGODB_DATABASE:courier_core}

server:
  port: 8101

courier:
  dispatch:
    auto-assign: true
    max-pending-time: 600
    status-check-interval: 30
```

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `MONGODB_URI` | MongoDB connection string | mongodb://localhost:27017 |
| `KAFKA_BOOTSTRAP_SERVERS` | Kafka brokers | localhost:9092 |
| `REDIS_HOST` | Redis host | localhost |
| `ASSIGNMENT_ENABLED` | Enable auto-assignment | true |
| `ROUTING_ENGINE` | Routing service URL | http://localhost:8104 |

## Local Development

### Running Locally

```bash
# Build the service
mvn clean install

# Run the service
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### Docker

```bash
# Build image
docker build -t courier/dispatch-core-service:latest .

# Run container
docker run -p 8101:8101 \
  -e MONGODB_URI=mongodb://host.docker.internal:27017 \
  courier/dispatch-core-service:latest
```

## Testing

```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify -P integration-tests
```

## Dispatch States

```
PENDING → ASSIGNED → PICKED_UP → IN_TRANSIT → DELIVERED
              ↓
           CANCELLED
```

### State Descriptions

| State | Description |
|-------|-------------|
| `PENDING` | Dispatch created, awaiting driver assignment |
| `ASSIGNED` | Driver assigned, awaiting pickup |
| `PICKED_UP` | Package picked up by driver |
| `IN_TRANSIT` | Package en route to destination |
| `DELIVERED` | Delivery completed successfully |
| `CANCELLED` | Dispatch cancelled |

## Data Model

### Dispatch Schema

```json
{
  "id": "uuid",
  "tenantId": "uuid",
  "status": "PENDING",
  "pickupLocation": {
    "address": "123 Main St",
    "latitude": 40.7128,
    "longitude": -74.0060,
    "contactName": "John Doe",
    "contactPhone": "+1234567890",
    "instructions": "Ring doorbell"
  },
  "deliveryLocation": {
    "address": "456 Oak Ave",
    "latitude": 40.7580,
    "longitude": -73.9855,
    "contactName": "Jane Smith",
    "contactPhone": "+0987654321",
    "instructions": "Leave at door"
  },
  "assignedDriverId": "uuid",
  "route": {
    "waypoints": [...],
    "distance": 5240,
    "estimatedDuration": 1800
  },
  "items": [
    {
      "description": "Small package",
      "weight": 2.5,
      "dimensions": {"length": 30, "width": 20, "height": 10}
    }
  ],
  "quote": {
    "price": 25.50,
    "currency": "USD"
  },
  "estimatedArrival": "2026-02-20T15:30:00Z",
  "actualArrival": null,
  "proofOfDelivery": null,
  "createdAt": "2026-02-20T14:00:00Z",
  "updatedAt": "2026-02-20T14:00:00Z"
}
```

## Events

The service publishes the following Kafka events:

| Event | Topic | Description |
|-------|-------|-------------|
| `dispatch.created` | `dispatch.events` | New dispatch created |
| `dispatch.assigned` | `dispatch.events` | Driver assigned |
| `dispatch.status.changed` | `dispatch.events` | Status updated |
| `dispatch.completed` | `dispatch.events` | Delivery completed |
| `dispatch.cancelled` | `dispatch.events` | Dispatch cancelled |

## Monitoring

Metrics exposed at `/actuator/prometheus`:

- `dispatches_created_total`: Total dispatches created
- `dispatches_by_status`: Current dispatches per status
- `dispatch_processing_duration_seconds`: Processing time
- `dispatch_assignment_duration_seconds`: Time to assign driver

## Dependencies

- Spring Boot 3.x
- Spring Data MongoDB
- Spring Kafka
- Assignment Service Client
- Routing Service Client
- ETA Service Client
- Redis

## Related Services

- **Assignment Service**: Driver assignment optimization
- **Routing Service**: Route calculation
- **ETA Service**: ETA predictions
- **GPS Tracking Service**: Real-time location updates
- **Notification Service**: Status notifications

## Support

For issues and questions:
- GitHub: https://github.com/gogidix/courier-services/issues
- Documentation: https://docs.gogidix.com/courier/dispatch
