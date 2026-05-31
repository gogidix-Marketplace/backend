# Fulfillment Service

## Description

The Fulfillment Service manages shipping, delivery, and warehouse operations in the Gogidix E-commerce ecosystem. It coordinates with shipping carriers and tracks order fulfillment.

## Features

- Shipping Method Management
- Carrier Integration (UPS, FedEx, DHL, USPS)
- Shipment Creation and Tracking
- Warehouse Management
- Delivery Status Updates
- Shipping Label Generation
- Rate Calculation
- Return Management

## Technology Stack

- Java 17+
- Spring Boot 3.2+
- Spring Data JPA
- PostgreSQL (primary database)
- Kafka (event communication)
- Micrometer (metrics)

## API Endpoints

### Shipping Methods

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/v1/fulfillment/shipping-methods` | Get available shipping methods |
| GET | `/v1/fulfillment/shipping-methods/{id}` | Get shipping method details |
| POST | `/v1/fulfillment/shipping-methods` | Create shipping method (admin) |
| PUT | `/v1/fulfillment/shipping-methods/{id}` | Update shipping method (admin) |

### Shipments

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/v1/fulfillment/shipments` | Create shipment |
| GET | `/v1/fulfillment/shipments` | List shipments |
| GET | `/v1/fulfillment/shipments/{id}` | Get shipment details |
| GET | `/v1/fulfillment/shipments/{trackingNumber}/track` | Track shipment |
| POST | `/v1/fulfillment/shipments/{id}/labels` | Generate shipping label |
| PATCH | `/v1/fulfillment/shipments/{id}/status` | Update shipment status |

### Orders

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/v1/fulfillment/orders/{orderId}/shipping-methods` | Get shipping methods for order |
| POST | `/v1/fulfillment/orders/{orderId}/ship` | Ship order |
| GET | `/v1/fulfillment/orders/{orderId}/tracking` | Get order tracking info |

### Warehouses

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/v1/fulfillment/warehouses` | List warehouses |
| GET | `/v1/fulfillment/warehouses/{id}` | Get warehouse details |
| POST | `/v1/fulfillment/warehouses` | Create warehouse (admin) |
| PUT | `/v1/fulfillment/warehouses/{id}` | Update warehouse (admin) |

### Returns

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/v1/fulfillment/returns` | List returns |
| POST | `/v1/fulfillment/returns` | Create return request |
| GET | `/v1/fulfillment/returns/{id}` | Get return details |
| PATCH | `/v1/fulfillment/returns/{id}/status` | Update return status |
| POST | `/v1/fulfillment/returns/{id}/labels` | Generate return label |

## Configuration

### Application Properties

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ecommerce
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  kafka:
    bootstrap-servers: ${KAFKA_BROKERS:localhost:9092}

fulfillment:
  carriers:
    ups:
      enabled: true
      client-id: ${UPS_CLIENT_ID}
      client-secret: ${UPS_CLIENT_SECRET}
      test-mode: true
    fedex:
      enabled: true
      api-key: ${FEDEX_API_KEY}
      secret-key: ${FEDEX_SECRET_KEY}
      test-mode: true
    dhl:
      enabled: true
      client-id: ${DHL_CLIENT_ID}
      client-secret: ${DHL_CLIENT_SECRET}
      test-mode: true
  label:
    format: PDF
    size: 6x4
  tracking:
    update-interval: 1h
```

## Running Locally

### Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL 15+
- Kafka 3.x
- Carrier API credentials (test)

### Start Dependencies

```bash
# Using Docker Compose
docker-compose -f docker-compose-dev.yml up -d postgres kafka
```

### Run Service

```bash
# Using Maven
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Or using JAR
mvn clean package
java -jar target/fulfillment-service-1.0.0.jar --spring.profiles.active=local
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
docker build -t gogidix/fulfillment-service:latest .

# Run container
docker run -p 8080:8080 \
  -e UPS_CLIENT_ID=your-client-id \
  -e FEDEX_API_KEY=your-api-key \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/ecommerce \
  gogidix/fulfillment-service:latest
```

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DB_URL` | Database JDBC URL | jdbc:postgresql://localhost:5432/ecommerce |
| `DB_USERNAME` | Database username | fulfillment_user |
| `DB_PASSWORD` | Database password | - |
| `KAFKA_BROKERS` | Kafka bootstrap servers | localhost:9092 |
| `UPS_CLIENT_ID` | UPS API client ID | - |
| `FEDEX_API_KEY` | FedEx API key | - |
| `DHL_CLIENT_ID` | DHL API client ID | - |
| `SPRING_PROFILES_ACTIVE` | Active profile | local |

## Shipment Status Flow

```
CREATED → LABEL_GENERATED → PICKED_UP → IN_TRANSIT → OUT_FOR_DELIVERY → DELIVERED
                                                     ↓
                                                EXCEPTION
                                                     ↓
                                                RETURNED
```

## Metrics

The service exposes the following custom metrics:

- `shipments_created_total` - Total shipments created
- `shipments_delivered_total` - Total shipments delivered
- `shipments_in_transit_total` - Total shipments in transit
- `fulfillment_processing_duration_seconds` - Fulfillment processing time
- `tracking_updates_total` - Total tracking updates

Access metrics at `/actuator/prometheus` or `/actuator/metrics`.

## Events Published

| Event | Topic | Description |
|-------|-------|-------------|
| ShipmentCreated | fulfillment.shipment.created | Shipment created |
| ShipmentPickedUp | fulfillment.shipment.picked_up | Package picked up |
| ShipmentInTransit | fulfillment.shipment.in_transit | Package in transit |
| ShipmentDelivered | fulfillment.shipment.delivered | Package delivered |
| ReturnCreated | fulfillment.return.created | Return request created |
| ReturnReceived | fulfillment.return.received | Return received at warehouse |

## Events Consumed

| Event | Topic | Description |
|-------|-------|-------------|
| OrderConfirmed | order.confirmed | Order confirmed, ready to ship |
| PaymentCompleted | payment.completed | Payment completed, can ship |

## Carrier Integration

### Supported Carriers

- **UPS**: Ground, Next Day Air, 2nd Day Air
- **FedEx**: Ground, Express, Priority Overnight
- **DHL**: Express, Economy Select
- **USPS**: Priority Mail, First Class Package

### Rate Calculation

```java
// Request shipping rates
ShippingRateRequest request = ShippingRateRequest.builder()
    .origin(originAddress)
    .destination(destinationAddress)
    .weight(new Weight(1.5, WeightUnit.LB))
    .dimensions(new Dimensions(12, 10, 5, DimensionUnit.INCH))
    .serviceType(ServiceType.STANDARD)
    .build();

List<ShippingRate> rates = fulfillmentService.getShippingRates(request);
```

## Development

### Project Structure

```
fulfillment-service/
├── src/main/java/com/gogidix/fulfillment/
│   ├── controller/              # REST controllers
│   ├── service/                 # Business logic
│   ├── carrier/                 # Carrier integrations
│   ├── repository/              # Data access
│   ├── model/                   # Domain models
│   ├── dto/                     # Data transfer objects
│   ├── mapper/                  # Entity-DTO mappers
│   ├── config/                  # Configuration
│   ├── exception/               # Custom exceptions
│   └── tracking/                # Tracking services
├── src/main/resources/
│   ├── db/migration/            # Database migrations
│   └── application.yml          # Configuration
└── src/test/                    # Tests
```

## Troubleshooting

### Common Issues

**Issue**: Shipping rates not returning
- **Solution**: Verify carrier API credentials and test mode settings

**Issue**: Tracking not updating
- **Solution**: Check tracking update job and carrier API status

**Issue**: Label generation failing
- **Solution**: Verify label format settings and carrier API limits

## Contributing

1. Create a feature branch
2. Make your changes
3. Add/update tests
4. Ensure all tests pass
5. Submit a pull request

## License

Copyright (c) 2026 Gogidix. All rights reserved.
