# Cart Service

## Description

The Cart Service manages shopping cart functionality in the Gogidix E-commerce ecosystem. It handles cart creation, item management, and cart-to-order conversion.

## Features

- Anonymous and Authenticated Cart Support
- Cart Item Management (Add, Update, Remove)
- Cart Persistence
- Cart Merge (Anonymous to Authenticated)
- Cart Validation
- Cart Expiration
- Real-time Cart Updates
- Cart Analytics

## Technology Stack

- Java 17+
- Spring Boot 3.2+
- Spring Data Redis (cart storage)
- PostgreSQL (persistent data)
- Kafka (event publishing)
- Micrometer (metrics)

## API Endpoints

### Cart Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/v1/cart/{id}` | Get cart by ID |
| POST | `/v1/cart` | Create new cart |
| DELETE | `/v1/cart/{id}` | Delete cart |
| POST | `/v1/cart/{id}/merge` | Merge carts |

### Cart Items

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/v1/cart/{id}/items` | Get cart items |
| POST | `/v1/cart/{id}/items` | Add item to cart |
| PUT | `/v1/cart/{id}/items/{itemId}` | Update cart item |
| DELETE | `/v1/cart/{id}/items/{itemId}` | Remove cart item |
| DELETE | `/v1/cart/{id}/items` | Clear cart |

### Cart Validation

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/v1/cart/{id}/validate` | Validate cart contents |
| GET | `/v1/cart/{id}/summary` | Get cart summary |

## Configuration

### Application Properties

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ecommerce
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  redis:
    host: ${REDIS_HOST:localhost}
    port: ${REDIS_PORT:6379}
    timeout: 2000ms
  kafka:
    bootstrap-servers: ${KAFKA_BROKERS:localhost:9092}

cart:
  anonymous:
    ttl: 604800  # 7 days in seconds
  authenticated:
    ttl: 2592000  # 30 days in seconds
  validation:
    check-stock: true
    check-price: true
  merge:
    strategy: QUANTITY_SUM
```

## Running Locally

### Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL 15+
- Redis 7+
- Kafka 3.x

### Start Dependencies

```bash
# Using Docker Compose
docker-compose -f docker-compose-dev.yml up -d postgres redis kafka
```

### Run Service

```bash
# Using Maven
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Or using JAR
mvn clean package
java -jar target/cart-service-1.0.0.jar --spring.profiles.active=local
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
docker build -t gogidix/cart-service:latest .

# Run container
docker run -p 8080:8080 \
  -e SPRING_REDIS_HOST=redis \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/ecommerce \
  gogidix/cart-service:latest
```

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DB_URL` | Database JDBC URL | jdbc:postgresql://localhost:5432/ecommerce |
| `DB_USERNAME` | Database username | cart_user |
| `DB_PASSWORD` | Database password | - |
| `REDIS_HOST` | Redis host | localhost |
| `REDIS_PORT` | Redis port | 6379 |
| `KAFKA_BROKERS` | Kafka bootstrap servers | localhost:9092 |
| `SPRING_PROFILES_ACTIVE` | Active profile | local |

## Cart Data Structure

```json
{
  "id": "cart_12345",
  "customerId": "cust_67890",
  "items": [
    {
      "id": "item_1",
      "productId": "prod_123",
      "name": "Product Name",
      "price": 29.99,
      "quantity": 2,
      "variant": {
        "size": "M",
        "color": "Red"
      },
      "image": "https://cdn.gogidix.com/..."
    }
  ],
  "totals": {
    "subtotal": 59.98,
    "tax": 4.80,
    "discount": 0,
    "total": 64.78
  },
  "expiresAt": "2024-01-08T00:00:00Z",
  "createdAt": "2024-01-01T00:00:00Z",
  "updatedAt": "2024-01-01T01:00:00Z"
}
```

## Metrics

The service exposes the following custom metrics:

- `carts_active_total` - Total active carts
- `carts_created_total` - Total carts created
- `carts_abandoned_total` - Total abandoned carts
- `cart_items_added_total` - Total items added to carts
- `cart_items_removed_total` - Total items removed from carts

Access metrics at `/actuator/prometheus` or `/actuator/metrics`.

## Events Published

| Event | Topic | Description |
|-------|-------|-------------|
| CartCreated | cart.created | New cart created |
| CartUpdated | cart.updated | Cart updated |
| CartDeleted | cart.deleted | Cart deleted |
| CartItemAdded | cart.item.added | Item added to cart |
| CartItemRemoved | cart.item.removed | Item removed from cart |
| CartAbandoned | cart.abandoned | Cart abandoned (expired) |

## Cart Merge Strategy

When an anonymous user logs in, their cart is merged with their existing cart:

```
Anonymous Cart Items + Authenticated Cart Items → Merged Cart
- Same products: quantities summed
- Different products: all items included
- Maximum quantity per item: configurable (default: 10)
```

## Development

### Project Structure

```
cart-service/
├── src/main/java/com/gogidix/cart/
│   ├── controller/              # REST controllers
│   ├── service/                 # Business logic
│   ├── repository/              # Data access
│   ├── model/                   # Domain models
│   ├── dto/                     # Data transfer objects
│   ├── mapper/                  # Entity-DTO mappers
│   ├── config/                  # Configuration
│   ├── exception/               # Custom exceptions
│   └── cache/                   # Cache layer
├── src/main/resources/
│   ├── db/migration/            # Database migrations
│   └── application.yml          # Configuration
└── src/test/                    # Tests
```

## Troubleshooting

### Common Issues

**Issue**: Cart items disappearing
- **Solution**: Check Redis TTL configuration and expiration settings

**Issue**: Cart merge not working correctly
- **Solution**: Verify merge strategy configuration

**Issue**: Slow cart performance
- **Solution**: Ensure Redis is properly configured and cache is being used

## Contributing

1. Create a feature branch
2. Make your changes
3. Add/update tests
4. Ensure all tests pass
5. Submit a pull request

## License

Copyright (c) 2026 Gogidix. All rights reserved.
