# Order Service

## Description

The Order Service manages the complete order lifecycle in the Gogidix E-commerce ecosystem. It handles order creation, status updates, payment integration, and coordination with fulfillment services.

## Features

- Order Creation and Management
- Order Status Workflow
- Cart to Order Conversion
- Payment Integration
- Order History and Tracking
- Order Modification and Cancellation
- Refund Processing
- Order Analytics and Reporting

## Technology Stack

- Java 17+
- Spring Boot 3.2+
- Spring Data JPA
- PostgreSQL (primary database)
- MongoDB (order history)
- Redis (session/cart data)
- Kafka (event-driven communication)
- Micrometer (metrics)

## API Endpoints

### Orders

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/v1/orders` | List orders (with filters) |
| GET | `/v1/orders/{id}` | Get order by ID |
| POST | `/v1/orders` | Create new order |
| PUT | `/v1/orders/{id}` | Update order |
| DELETE | `/v1/orders/{id}` | Cancel order |
| GET | `/v1/orders/{id}/items` | Get order items |
| PATCH | `/v1/orders/{id}/status` | Update order status |

### Customer Orders

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/v1/customers/{customerId}/orders` | Get customer orders |
| GET | `/v1/customers/{customerId}/orders/{orderId}` | Get specific customer order |

### Order Actions

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/v1/orders/{id}/pay` | Process payment for order |
| POST | `/v1/orders/{id}/refund` | Refund order |
| POST | `/v1/orders/{id}/ship` | Mark order as shipped |

## Configuration

### Application Properties

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ecommerce
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  data:
    mongodb:
      uri: mongodb://${MONGO_HOST:localhost}:${MONGO_PORT:27017}/order_history
  kafka:
    bootstrap-servers: ${KAFKA_BROKERS:localhost:9092}
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
    consumer:
      group-id: order-service
      auto-offset-reset: earliest

order:
  payment:
    timeout: 30m
    retry-attempts: 3
  cancellation:
    allowed-hours: 1
  inventory:
    reservation-timeout: 15m
```

## Running Locally

### Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL 15+
- MongoDB 6+
- Redis 7+
- Kafka 3.x

### Start Dependencies

```bash
# Using Docker Compose
docker-compose -f docker-compose-dev.yml up -d postgres mongodb redis kafka
```

### Run Service

```bash
# Using Maven
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Or using JAR
mvn clean package
java -jar target/order-service-1.0.0.jar --spring.profiles.active=local
```

### Access Service

- API: http://localhost:8080
- Actuator Health: http://localhost:8080/actuator/health
- Swagger UI: http://localhost:8080/swagger-ui.html

## Testing

### Unit Tests

```bash
mvn test
```

### Integration Tests

```bash
mvn verify -P integration-test
```

### Test Coverage

```bash
mvn test jacoco:report
```

## Docker Build

```bash
# Build image
docker build -t gogidix/order-service:latest .

# Run container
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/ecommerce \
  -e SPRING_DATA_MONGODB_URI=mongodb://mongodb:27017/order_history \
  gogidix/order-service:latest
```

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DB_URL` | Database JDBC URL | jdbc:postgresql://localhost:5432/ecommerce |
| `DB_USERNAME` | Database username | order_user |
| `DB_PASSWORD` | Database password | - |
| `MONGO_HOST` | MongoDB host | localhost |
| `MONGO_PORT` | MongoDB port | 27017 |
| `REDIS_HOST` | Redis host | localhost |
| `REDIS_PORT` | Redis port | 6379 |
| `KAFKA_BROKERS` | Kafka bootstrap servers | localhost:9092 |
| `SPRING_PROFILES_ACTIVE` | Active profile | local |

## Order Status Flow

```
PENDING → CONFIRMED → PROCESSING → SHIPPED → DELIVERED
                    ↓
                  CANCELLED
                    ↓
                  REFUNDED
```

## Metrics

The service exposes the following custom metrics:

- `orders_created_total` - Total orders created
- `orders_confirmed_total` - Total orders confirmed
- `orders_cancelled_total` - Total orders cancelled
- `order_processing_duration_seconds` - Order processing time
- `order_value_total` - Total order value

Access metrics at `/actuator/prometheus` or `/actuator/metrics`.

## Events Published

| Event | Topic | Description |
|-------|-------|-------------|
| OrderCreated | order.created | New order created |
| OrderConfirmed | order.confirmed | Order confirmed |
| OrderCancelled | order.cancelled | Order cancelled |
| OrderShipped | order.shipped | Order shipped |
| OrderDelivered | order.delivered | Order delivered |

## Events Consumed

| Event | Topic | Description |
|-------|-------|-------------|
| PaymentCompleted | payment.completed | Payment successfully processed |
| PaymentFailed | payment.failed | Payment failed |
| InventoryReserved | inventory.reserved | Inventory reservation confirmed |
| ShipmentDelivered | shipment.delivered | Shipment marked delivered |

## Development

### Project Structure

```
order-service/
├── src/main/java/com/gogidix/order/
│   ├── controller/              # REST controllers
│   ├── service/                 # Business logic
│   ├── repository/              # Data access
│   ├── model/                   # Domain models
│   ├── dto/                     # Data transfer objects
│   ├── mapper/                  # Entity-DTO mappers
│   ├── config/                  # Configuration
│   ├── exception/               # Custom exceptions
│   └── workflow/                # Order workflow logic
├── src/main/resources/
│   ├── db/migration/            # Database migrations
│   └── application.yml          # Configuration
└── src/test/                    # Tests
```

### Order Creation Flow

```
1. Validate cart and customer
2. Reserve inventory
3. Calculate totals
4. Create order record
5. Initiate payment
6. Update order status based on payment
7. Publish events
8. Return order confirmation
```

## Troubleshooting

### Common Issues

**Issue**: Order creation fails with inventory error
- **Solution**: Check inventory service availability and stock levels

**Issue**: Order status not updating after payment
- **Solution**: Verify Kafka consumer is processing payment events

**Issue**: Slow order listing
- **Solution**: Ensure database indexes are created on customer_id and created_at

## Contributing

1. Create a feature branch
2. Make your changes
3. Add/update tests
4. Ensure all tests pass
5. Submit a pull request

## License

Copyright (c) 2026 Gogidix. All rights reserved.
