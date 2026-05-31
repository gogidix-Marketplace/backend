# Subscription Service

Subscription lifecycle and billing management service with Stripe integration for the Gogidix platform.

## Features

- **Subscription Plans**: Multiple tiers (Free, Basic, Pro, Enterprise) with flexible pricing
- **Subscription Management**: Create, upgrade, downgrade, cancel, and renew subscriptions
- **Trial Management**: Automated trial periods with conversion tracking
- **Invoice Generation**: Automated billing with PDF invoice generation
- **Payment Processing**: Stripe integration for credit cards and other payment methods
- **Usage-Based Billing**: Track and bill for resource usage
- **Payment Reminders**: Automated dunning and payment reminders
- **Multi-Tenancy**: Complete tenant isolation with Row-Level Security

## Quick Start

### Prerequisites

- Java 17+
- Maven 3.9+
- PostgreSQL 14+
- Redis 7+
- Kafka 3+
- Stripe Account (for payments)

### Local Development

1. Navigate to the service:
```bash
cd subscription-service
```

2. Build the application:
```bash
mvn clean install
```

3. Set Stripe API key:
```bash
export STRIPE_API_KEY=sk_test_your_key_here
```

4. Run with Spring Boot:
```bash
mvn spring-boot:run
```

5. Or use Docker Compose:
```bash
docker-compose up -d
```

The service will be available at: `http://localhost:8402/subscriptions`

## API Documentation

### Swagger UI

Interactive API documentation available at:
```
http://localhost:8402/subscriptions/swagger-ui.html
```

## Architecture

### Technology Stack

- **Framework**: Spring Boot 3.1.5
- **Language**: Java 17
- **Database**: PostgreSQL 14 with RLS
- **Cache**: Redis 7
- **Messaging**: Kafka 3
- **Payment**: Stripe SDK
- **PDF**: iText 7
- **Build**: Maven 3.9

### Domain Models

- **SubscriptionPlan**: Plan tiers and pricing
- **Subscription**: Customer subscriptions
- **Invoice**: Billing invoices
- **PaymentMethod**: Payment method management
- **PaymentTransaction**: Transaction history
- **UsageRecord**: Usage tracking for billing

## API Endpoints

### Subscription Plans

- `POST /api/v1/subscriptions/plans` - Create plan
- `GET /api/v1/subscriptions/plans` - List plans
- `GET /api/v1/subscriptions/plans/{id}` - Get plan by ID
- `PUT /api/v1/subscriptions/plans/{id}` - Update plan

### Subscriptions

- `POST /api/v1/subscriptions` - Create subscription
- `GET /api/v1/subscriptions` - List subscriptions
- `GET /api/v1/subscriptions/{id}` - Get subscription by ID
- `POST /api/v1/subscriptions/{id}/upgrade` - Upgrade plan
- `POST /api/v1/subscriptions/{id}/downgrade` - Downgrade plan
- `POST /api/v1/subscriptions/{id}/cancel` - Cancel subscription
- `POST /api/v1/subscriptions/{id}/renew` - Renew subscription

### Invoices

- `POST /api/v1/subscriptions/invoices` - Generate invoice
- `GET /api/v1/subscriptions/invoices` - List invoices
- `GET /api/v1/subscriptions/invoices/{id}` - Get invoice by ID
- `POST /api/v1/subscriptions/invoices/{id}/pay` - Pay invoice
- `GET /api/v1/subscriptions/invoices/{id}/pdf` - Download invoice PDF

### Payment Methods

- `POST /api/v1/subscriptions/payment-methods` - Add payment method
- `GET /api/v1/subscriptions/payment-methods` - List payment methods
- `POST /api/v1/subscriptions/payment-methods/{id}/set-default` - Set as default

### Stripe Webhooks

- `POST /api/v1/subscriptions/webhooks/stripe` - Stripe webhook handler

## Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DATABASE_URL` | PostgreSQL JDBC URL | `jdbc:postgresql://localhost:5432/subscription_service` |
| `REDIS_HOST` | Redis host | `localhost` |
| `KAFKA_BOOTSTRAP_SERVERS` | Kafka servers | `localhost:9092` |
| `STRIPE_API_KEY` | Stripe secret key | Required |
| `STRIPE_WEBHOOK_SECRET` | Stripe webhook secret | Required |
| `SERVER_PORT` | Service port | `8402` |

### Stripe Setup

1. Create a Stripe account: https://stripe.com
2. Get your API keys from the Stripe Dashboard
3. Set up webhook endpoints for:
   - `invoice.payment_succeeded`
   - `invoice.payment_failed`
   - `customer.subscription.deleted`
   - `invoice.upcoming`

## Deployment

### Using Docker

```bash
# Build image
docker build -t subscription-service:latest .

# Run container
docker run -p 8402:8402 \
  -e DATABASE_URL=jdbc:postgresql://localhost:5432/subscription_service \
  -e STRIPE_API_KEY=sk_test_xxx \
  subscription-service:latest
```

### Railway Deployment

```bash
# Install Railway CLI
npm install -g @railway/cli

# Login and deploy
railway login
railway init
railway up
```

## Testing

```bash
# Unit tests
mvn test

# Integration tests (requires Stripe test key)
mvn verify -P integration-test

# Coverage report
mvn jacoco:report
```

## Monitoring

### Health Check
```
GET /subscriptions/actuator/health
```

### Metrics
```
GET /subscriptions/actuator/metrics
GET /subscriptions/actuator/prometheus
```

## Billing Workflow

1. **Create Subscription Plan**: Define pricing and features
2. **Create Subscription**: Subscribe customer to a plan
3. **Generate Invoice**: Automatic invoice generation at period end
4. **Process Payment**: Charge payment method via Stripe
5. **Handle Events**: Process Stripe webhook events
6. **Track Usage**: Record usage for usage-based billing

## Contributing

1. Create feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -am 'Add feature'`
3. Push branch: `git push origin feature/your-feature`
4. Create merge request

## License

Copyright © 2025 Gogidix Platform Team. All rights reserved.
