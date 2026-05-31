# Payment Service

## Description

The Payment Service handles all payment processing operations in the Gogidix E-commerce ecosystem. It integrates with multiple payment gateways, manages payment methods, and processes refunds.

## Features

- Multiple Payment Gateway Support (Stripe, PayPal, Square)
- Payment Method Management
- Payment Intent Creation and Processing
- Refund Processing
- Payment Webhook Handling
- Payment History and Audit
- PCI DSS Compliance
- 3D Secure Authentication

## Technology Stack

- Java 17+
- Spring Boot 3.2+
- Spring Data JPA
- PostgreSQL (primary database)
- Kafka (event communication)
- Micrometer (metrics)

## API Endpoints

### Payment Intents

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/v1/payments/intents` | Create payment intent |
| GET | `/v1/payments/intents/{id}` | Get payment intent |
| POST | `/v1/payments/intents/{id}/confirm` | Confirm payment intent |
| POST | `/v1/payments/intents/{id}/cancel` | Cancel payment intent |

### Payment Methods

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/v1/payment-methods` | List payment methods |
| POST | `/v1/payment-methods` | Add payment method |
| DELETE | `/v1/payment-methods/{id}` | Delete payment method |
| GET | `/v1/payment-methods/{id}` | Get payment method details |

### Payments

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/v1/payments` | List payments (with filters) |
| GET | `/v1/payments/{id}` | Get payment by ID |
| POST | `/v1/payments/{id}/refund` | Process refund |
| GET | `/v1/payments/{id}/refunds` | Get payment refunds |

### Webhooks

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/v1/webhooks/stripe` | Stripe webhook handler |
| POST | `/v1/webhooks/paypal` | PayPal webhook handler |
| POST | `/v1/webhooks/square` | Square webhook handler |

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

payment:
  gateways:
    stripe:
      enabled: true
      api-key: ${STRIPE_API_KEY}
      webhook-secret: ${STRIPE_WEBHOOK_SECRET}
    paypal:
      enabled: true
      client-id: ${PAYPAL_CLIENT_ID}
      client-secret: ${PAYPAL_CLIENT_SECRET}
      webhook-id: ${PAYPAL_WEBHOOK_ID}
    square:
      enabled: true
      access-token: ${SQUARE_ACCESS_TOKEN}
      location-id: ${SQUARE_LOCATION_ID}

security:
  pci:
    enabled: true
    encryption-key: ${ENCRYPTION_KEY}
```

## Running Locally

### Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL 15+
- Kafka 3.x
- Stripe/PayPal test account

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
java -jar target/payment-service-1.0.0.jar --spring.profiles.active=local
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
docker build -t gogidix/payment-service:latest .

# Run container
docker run -p 8080:8080 \
  -e STRIPE_API_KEY=sk_test_... \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/ecommerce \
  gogidix/payment-service:latest
```

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DB_URL` | Database JDBC URL | jdbc:postgresql://localhost:5432/ecommerce |
| `DB_USERNAME` | Database username | payment_user |
| `DB_PASSWORD` | Database password | - |
| `KAFKA_BROKERS` | Kafka bootstrap servers | localhost:9092 |
| `STRIPE_API_KEY` | Stripe API key | - |
| `PAYPAL_CLIENT_ID` | PayPal client ID | - |
| `SQUARE_ACCESS_TOKEN` | Square access token | - |
| `SPRING_PROFILES_ACTIVE` | Active profile | local |

## Payment Flow

```
1. Create Payment Intent
   → Validate order and amount
   → Select payment gateway
   → Create intent with gateway

2. Confirm Payment
   → Process payment with gateway
   → Handle 3D Secure if required
   → Update payment status

3. Handle Webhook
   → Receive gateway webhook
   → Verify signature
   → Update payment record
   → Publish events

4. Process Refund (if needed)
   → Create refund with gateway
   → Update payment record
   → Publish refund event
```

## Metrics

The service exposes the following custom metrics:

- `payment_initiated_total` - Total payment initiations
- `payment_completed_total` - Total successful payments
- `payment_failed_total` - Total failed payments
- `payment_refunded_total` - Total refunds
- `payment_processing_duration_seconds` - Payment processing time

Access metrics at `/actuator/prometheus` or `/actuator/metrics`.

## Events Published

| Event | Topic | Description |
|-------|-------|-------------|
| PaymentInitiated | payment.initiated | Payment process started |
| PaymentCompleted | payment.completed | Payment successful |
| PaymentFailed | payment.failed | Payment failed |
| RefundProcessed | payment.refunded | Refund processed |

## Events Consumed

| Event | Topic | Description |
|-------|-------|-------------|
| OrderCreated | order.created | New order to process payment |

## Security

### PCI DSS Compliance

- Never store full card details
- Use tokenization from payment gateways
- Encrypt sensitive data at rest
- Use TLS for all communications
- Implement webhook signature verification

### Webhook Signature Verification

```java
@Component
public class WebhookSecurity {

    public boolean verifyStripeSignature(String payload, String signature, String secret) {
        // Stripe signature verification
        return Stripe.webhook()
            .constructEvent(payload, signature, secret) != null;
    }
}
```

## Development

### Project Structure

```
payment-service/
├── src/main/java/com/gogidix/payment/
│   ├── controller/              # REST controllers
│   ├── service/                 # Business logic
│   ├── gateway/                 # Payment gateway integrations
│   ├── repository/              # Data access
│   ├── model/                   # Domain models
│   ├── dto/                     # Data transfer objects
│   ├── config/                  # Configuration
│   ├── exception/               # Custom exceptions
│   └── webhook/                 # Webhook handlers
├── src/main/resources/
│   ├── db/migration/            # Database migrations
│   └── application.yml          # Configuration
└── src/test/                    # Tests
```

## Troubleshooting

### Common Issues

**Issue**: Payment fails with gateway error
- **Solution**: Verify API keys and gateway credentials

**Issue**: Webhook not processing
- **Solution**: Check webhook signature verification and endpoint URL

**Issue**: Payment status not updating
- **Solution**: Verify Kafka connectivity and event publishing

## Contributing

1. Create a feature branch
2. Make your changes
3. Add/update tests
4. Ensure all tests pass
5. Submit a pull request

## License

Copyright (c) 2026 Gogidix. All rights reserved.
