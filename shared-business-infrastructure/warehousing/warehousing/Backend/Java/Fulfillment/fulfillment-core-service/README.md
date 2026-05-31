# Fulfillment Core Service

Multi-tenant fulfillment order management service for the Gogidix ecosystem.

## Overview

The Fulfillment Core Service manages order fulfillment processes, including:
- Order creation and tracking
- Status management (PENDING, PICKING, PACKING, SHIPPED, DELIVERED, CANCELLED)
- Priority-based processing (LOW, NORMAL, HIGH, URGENT)
- Customer order history
- Real-time order status updates

## Migration Status

**MIGRATED FROM PostgreSQL TO MongoDB**

This service has been successfully migrated from JPA/PostgreSQL to MongoDB:
- Entities migrated from `@Entity` to `@Document`
- Repositories migrated from `JpaRepository` to `MongoRepository`
- Configuration updated to use MongoDB connection
- All unit and integration tests updated for MongoDB

## Technology Stack

- Java 17
- Spring Boot 3.1.5
- MongoDB 6.0
- Maven
- Testcontainers

## Database

MongoDB is used for persistence with the following collections:
- `fulfillment_orders` - Fulfillment orders with items and tracking

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
java -jar target/fulfillment-core-service-1.0.0.jar
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
  port: 8085
```

## API Endpoints

### Order Management

- `POST /api/v1/fulfillment/orders` - Create fulfillment order
- `GET /api/v1/fulfillment/orders?tenantId={id}` - List all orders
- `GET /api/v1/fulfillment/orders/{id}` - Get order details
- `GET /api/v1/fulfillment/orders/number/{orderNumber}` - Get order by number
- `PUT /api/v1/fulfillment/orders/{id}/status` - Update order status

### Order Queries

- `GET /api/v1/fulfillment/orders/pending?tenantId={id}` - Get pending orders
- `GET /api/v1/fulfillment/orders/ready-to-ship?tenantId={id}` - Get orders ready for shipping
- `GET /api/v1/fulfillment/orders/high-priority?tenantId={id}` - Get high priority orders
- `GET /api/v1/fulfillment/orders/urgent?tenantId={id}` - Get urgent orders

### Health Check

- `GET /health` - Service health status

## API Examples

### Create Fulfillment Order

```bash
curl -X POST http://localhost:8085/api/v1/fulfillment/orders \
  -H "Content-Type: application/json" \
  -d '{
    "tenantId": "tenant-123",
    "orderNumber": "ORDER-001",
    "customerId": "customer-001",
    "warehouseId": "warehouse-001",
    "items": [
      {
        "sku": "SKU-001",
        "productName": "Product 1",
        "quantity": 2,
        "weight": 1.5,
        "volume": 0.5,
        "unitPrice": 25.0
      }
    ],
    "shippingAddress": "123 Test St, City, Country",
    "shippingMethod": "STANDARD",
    "priority": "NORMAL"
  }'
```

### Update Order Status

```bash
curl -X PUT http://localhost:8085/api/v1/fulfillment/orders/{id}/status?updatedBy=user-001 \
  -H "Content-Type: application/json" \
  -d '{
    "status": "SHIPPED",
    "trackingNumber": "TRACK-12345",
    "carrier": "FedEx"
  }'
```

### Get Pending Orders

```bash
curl http://localhost:8085/api/v1/fulfillment/orders/pending?tenantId=tenant-123
```

## Test Coverage

Unit tests: >80% coverage
- Repository layer tests
- Service layer tests
- Integration tests with Testcontainers MongoDB

## Features

### Multi-Tenancy
All entities include `tenantId` field for data isolation.

### Order Status Workflow
Orders progress through: PENDING → PICKING → PACKING → SHIPPED → DELIVERED

### Priority Processing
Support for LOW, NORMAL, HIGH, and URGENT priority orders.

### Tracking Integration
Full tracking number and carrier support for shipped orders.

### Audit Trail
Tracks who picked, packed, and shipped each order.

