# Public API Services

Public-facing APIs for customers and partners.

## Overview

This directory contains services that expose public APIs for external integration:

- **Public Booking Service** (Port 8117): Customer booking API
- **Public Quote Service** (Port 8118): Price quote API
- **Public Tracking Service** (Port 8119): Package tracking API

## Services

### Public Booking Service

Customer-facing booking and order management.

**Port**: 8117

**Features**:
- Create delivery bookings
- Manage existing bookings
- Booking cancellation
- Real-time booking status

**Key Endpoints**:
- `POST /api/v1/booking` - Create booking
- `GET /api/v1/booking/{id}` - Get booking details
- `PUT /api/v1/booking/{id}/cancel` - Cancel booking
- `GET /api/v1/booking/{id}/status` - Get booking status

### Public Quote Service

Real-time price quotes for customers.

**Port**: 8118

**Features**:
- Instant price quotes
- Multiple vehicle types
- Promo code support
- Quote validation

**Key Endpoints**:
- `POST /api/v1/quote` - Get price quote
- `GET /api/v1/quote/{id}` - Get existing quote
- `POST /api/v1/quote/{id}/validate` - Validate quote

### Public Tracking Service

Package tracking for customers.

**Port**: 8119

**Features**:
- Real-time tracking
- Driver information
- Delivery proof
- Notifications

**Key Endpoints**:
- `GET /api/v1/tracking/{number}` - Track package
- `POST /api/v1/tracking/lookup` - Lookup by reference
- `GET /api/v1/tracking/{id}/history` - Get tracking history

## Local Development

### Running All Services

```bash
# Navigate to PublicAPI directory
cd Backend/Java/PublicAPI

# Build all services
mvn clean install

# Run specific service
cd PublicBookingService
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Or use Docker Compose
docker-compose -f docker-compose-public-api.yml up
```

### Docker Compose

```yaml
version: '3.8'
services:
  booking-service:
    build: ./PublicBookingService
    ports:
      - "8117:8117"
    environment:
      - MONGODB_URI=mongodb://mongodb:27017
      - KAFKA_BOOTSTRAP_SERVERS=kafka:9092

  quote-service:
    build: ./PublicQuoteService
    ports:
      - "8118:8118"
    environment:
      - MONGODB_URI=mongodb://mongodb:27017

  tracking-service:
    build: ./PublicTrackingService
    ports:
      - "8119:8119"
    environment:
      - MONGODB_URI=mongodb://mongodb:27017
```

## API Documentation

Full API documentation is available at:
- Swagger UI: http://api.courier.gogidix.com/docs
- OpenAPI Spec: /api/v1/openapi.yaml

## Authentication

Public APIs use API key authentication:

```
Authorization: Bearer <public_api_key>
```

Rate limits:
- 100 requests/minute per IP
- 1000 requests/minute per API key

## Monitoring

Each service exposes metrics at `/actuator/prometheus`.

## Related Services

- **Dispatch Core Service**: Backend dispatch management
- **Pricing Engine Service**: Price calculations
- **GPS Tracking Service**: Real-time location

## Support

- Developer Portal: https://developers.gogidix.com
- API Status: https://status.gogidix.com
- Documentation: https://docs.gogidix.com/courier/public-api
