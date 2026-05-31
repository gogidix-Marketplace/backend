# Public Tracking Service

Customer-facing tracking service for package visibility.

## Overview

The Public Tracking Service provides customers with real-time visibility into their delivery status. It offers both web and mobile interfaces for tracking packages.

## Features

- Real-time package tracking
- Tracking number lookup
- Delivery notifications
- Estimated arrival time
- Driver information
- Delivery proof
- Tracking history
- Multi-language support

## API Endpoints

### Tracking

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/tracking/{trackingNumber}` | Track by tracking number |
| POST | `/api/v1/tracking/lookup` | Lookup by reference |
| GET | `/api/v1/tracking/{id}/history` | Get tracking history |
| GET | `/api/v1/tracking/{id}/proof` | Get proof of delivery |

### Notifications

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/tracking/{id}/notifications` | Subscribe to updates |
| DELETE | `/api/v1/tracking/{id}/notifications` | Unsubscribe |

## Configuration

```yaml
spring:
  application:
    name: public-tracking-service
  data:
    mongodb:
      uri: ${MONGODB_URI}
      database: courier_core

server:
  port: 8119

courier:
  tracking:
    history-retention-days: 90
    cache-ttl-minutes: 5
```

## Tracking Response

```json
{
  "trackingNumber": "GOG-ABC123",
  "reference": "ORDER-12345",
  "status": "IN_TRANSIT",
  "estimatedArrival": "2026-02-20T15:30:00Z",
  "estimatedArrivalWindow": {
    "start": "2026-02-20T15:15:00Z",
    "end": "2026-02-20T15:45:00Z"
  },
  "driver": {
    "name": "John D.",
    "photoUrl": "https://...",
    "rating": 4.8,
    "vehicle": "Toyota Camry - Blue",
    "vehiclePlate": "ABC123"
  },
  "currentLocation": {
    "latitude": 40.7350,
    "longitude": -73.9950,
    "address": "Approaching 400 Main St",
    "lastUpdate": "2026-02-20T14:25:00Z"
  },
  "route": {
    "polyline": "encoded_polyline",
    "remainingDistance": 2500,
    "remainingDuration": 1800,
    "waypoints": [
      {
        "location": "400 Main St",
        "status": "COMPLETED",
        "timestamp": "2026-02-20T14:00:00Z"
      },
      {
        "location": "456 Oak Ave",
        "status": "PENDING",
        "estimatedArrival": "2026-02-20T15:30:00Z"
      }
    ]
  },
  "timeline": [
    {
      "status": "PENDING",
      "timestamp": "2026-02-20T14:00:00Z",
      "description": "Order placed",
      "location": "Online"
    },
    {
      "status": "CONFIRMED",
      "timestamp": "2026-02-20T14:01:00Z",
      "description": "Order confirmed",
      "location": "Distribution Center"
    },
    {
      "status": "ASSIGNED",
      "timestamp": "2026-02-20T14:05:00Z",
      "description": "Driver assigned - John D.",
      "location": "Distribution Center"
    },
    {
      "status": "PICKED_UP",
      "timestamp": "2026-02-20T14:10:00Z",
      "description": "Package picked up",
      "location": "Distribution Center"
    },
    {
      "status": "IN_TRANSIT",
      "timestamp": "2026-02-20T14:15:00Z",
      "description": "On the way to delivery address",
      "location": "En route"
    }
  ],
  "delivery": {
    "requiresSignature": true,
    "instructions": "Leave at door if no answer",
    "contactInfo": {
      "name": "Jane Smith",
      "phone": "+0987654321"
    }
  },
  "links": {
    "shareUrl": "https://track.gogidix.com/GOG-ABC123",
    "mapUrl": "https://maps.google.com/?api=1&destination=..."
  }
}
```

## Tracking Status Flow

```
PENDING → CONFIRMED → ASSIGNED → PICKED_UP → IN_TRANSIT → DELIVERED
                                                    ↓
                                                 FAILED_ATTEMPT
```

### Status Descriptions

| Status | Description | Icon |
|--------|-------------|------|
| `PENDING` | Order placed, awaiting confirmation | clock |
| `CONFIRMED` | Order confirmed by system | check-circle |
| `ASSIGNED` | Driver assigned to delivery | user |
| `PICKED_UP` | Package picked up by driver | box |
| `IN_TRANSIT` | Package en route to destination | truck |
| `DELIVERED` | Delivery completed | flag-checkered |
| `FAILED_ATTEMPT` | Delivery attempt failed | exclamation-circle |

## Proof of Delivery

```json
{
  "deliveredAt": "2026-02-20T15:28:00Z",
  "signature": {
    "image": "base64_encoded_image",
    "name": "J. Smith"
  },
  "photo": "base64_encoded_photo",
  "location": {
    "latitude": 40.7580,
    "longitude": -73.9855,
    "accuracy": 10
  },
  "notes": "Left at front door as requested"
}
```

## Monitoring

Metrics at `/actuator/prometheus`:

- `tracking_requests_total`: Total tracking requests
- `tracking_cache_hits_total`: Cache hit rate
- `tracking_ws_connections_total`: WebSocket connections

## Dependencies

- Spring Boot 3.x
- Spring Data MongoDB
- Spring WebSocket
- Redis (caching)
- GPS Tracking Service

## Related Services

- **Dispatch Core Service**: Dispatch status
- **GPS Tracking Service**: Real-time location
- **ETA Service**: ETA calculations
- **Notification Service**: Status notifications

## Support

- GitHub: https://github.com/gogidix/courier-services/issues
- Public URL: https://track.gogidix.com
- Documentation: https://docs.gogidix.com/courier/tracking
