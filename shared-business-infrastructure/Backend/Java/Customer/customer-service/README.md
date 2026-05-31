# Customer Service

## Description

The Customer Service manages customer profiles, authentication, and account-related operations in the Gogidix E-commerce ecosystem.

## Features

- Customer Registration and Onboarding
- Profile Management
- Address Book Management
- Customer Authentication
- Password Reset and Recovery
- Email Verification
- Customer Preferences
- Account Activity Tracking
- GDPR Compliance (Data Export/Delete)

## Technology Stack

- Java 17+
- Spring Boot 3.2+
- Spring Security
- Spring Data JPA
- PostgreSQL (primary database)
- MongoDB (activity logs)
- Kafka (event publishing)
- Micrometer (metrics)

## API Endpoints

### Registration

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/v1/customers/registration` | Register new customer |
| POST | `/v1/customers/registration/verify-email` | Verify email address |
| POST | `/v1/customers/registration/resend-verification` | Resend verification email |

### Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/v1/auth/login` | Customer login |
| POST | `/v1/auth/logout` | Customer logout |
| POST | `/v1/auth/refresh` | Refresh access token |
| POST | `/v1/auth/forgot-password` | Request password reset |
| POST | `/v1/auth/reset-password` | Reset password |

### Customer Profile

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/v1/customers/{id}` | Get customer profile |
| PUT | `/v1/customers/{id}` | Update customer profile |
| PATCH | `/v1/customers/{id}` | Partial update customer profile |
| DELETE | `/v1/customers/{id}` | Delete customer account |
| POST | `/v1/customers/{id}/avatar` | Upload avatar image |
| DELETE | `/v1/customers/{id}/avatar` | Remove avatar image |

### Address Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/v1/customers/{id}/addresses` | List customer addresses |
| POST | `/v1/customers/{id}/addresses` | Add new address |
| PUT | `/v1/customers/{id}/addresses/{addressId}` | Update address |
| DELETE | `/v1/customers/{id}/addresses/{addressId}` | Delete address |
| PATCH | `/v1/customers/{id}/addresses/{addressId}/default` | Set default address |

### Customer Preferences

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/v1/customers/{id}/preferences` | Get customer preferences |
| PUT | `/v1/customers/{id}/preferences` | Update preferences |
| PATCH | `/v1/customers/{id}/preferences/notifications` | Update notification settings |
| PATCH | `/v1/customers/{id}/preferences/privacy` | Update privacy settings |

### GDPR

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/v1/customers/{id}/export` | Export customer data (GDPR) |
| POST | `/v1/customers/{id}/delete-request` | Request account deletion (GDPR) |

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
      uri: mongodb://${MONGO_HOST:localhost}:${MONGO_PORT:27017}/customer_activity
  kafka:
    bootstrap-servers: ${KAFKA_BROKERS:localhost:9092}
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: ${AUTH_ISSUER_URI}

customer:
  registration:
    email-verification-required: true
    welcome-email-enabled: true
  password:
    min-length: 8
    require-uppercase: true
    require-lowercase: true
    require-digit: true
    require-special-char: true
  session:
    timeout: 3600  # 1 hour
  avatar:
    max-size: 5242880  # 5MB in bytes
    allowed-formats: jpg,jpeg,png,webp
```

## Running Locally

### Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL 15+
- MongoDB 6+
- Kafka 3.x
- SMTP server (for emails)

### Start Dependencies

```bash
# Using Docker Compose
docker-compose -f docker-compose-dev.yml up -d postgres mongodb kafka
```

### Run Service

```bash
# Using Maven
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Or using JAR
mvn clean package
java -jar target/customer-service-1.0.0.jar --spring.profiles.active=local
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
docker build -t gogidix/customer-service:latest .

# Run container
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/ecommerce \
  -e SPRING_DATA_MONGODB_URI=mongodb://mongodb:27017/customer_activity \
  gogidix/customer-service:latest
```

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DB_URL` | Database JDBC URL | jdbc:postgresql://localhost:5432/ecommerce |
| `DB_USERNAME` | Database username | customer_user |
| `DB_PASSWORD` | Database password | - |
| `MONGO_HOST` | MongoDB host | localhost |
| `MONGO_PORT` | MongoDB port | 27017 |
| `KAFKA_BROKERS` | Kafka bootstrap servers | localhost:9092 |
| `AUTH_ISSUER_URI` | OAuth2 issuer URI | - |
| `SMTP_HOST` | SMTP host for emails | localhost |
| `SPRING_PROFILES_ACTIVE` | Active profile | local |

## Customer Data Structure

```json
{
  "id": "cust_12345",
  "email": "john.doe@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phone": "+1234567890",
  "avatar": "https://cdn.gogidix.com/avatars/cust_12345.jpg",
  "emailVerified": true,
  "status": "ACTIVE",
  "preferences": {
    "language": "en",
    "currency": "USD",
    "notifications": {
      "email": true,
      "sms": false,
      "push": true
    },
    "marketing": {
      "email": true,
      "sms": false
    }
  },
  "addresses": [
    {
      "id": "addr_1",
      "type": "SHIPPING",
      "default": true,
      "firstName": "John",
      "lastName": "Doe",
      "addressLine1": "123 Main St",
      "city": "New York",
      "state": "NY",
      "postalCode": "10001",
      "country": "US",
      "phone": "+1234567890"
    }
  ],
  "createdAt": "2024-01-01T00:00:00Z",
  "updatedAt": "2024-01-01T00:00:00Z",
  "lastLoginAt": "2024-01-15T10:30:00Z"
}
```

## Metrics

The service exposes the following custom metrics:

- `customers_registered_total` - Total customer registrations
- `customers_active_total` - Total active customers
- `customer_login_attempts_total` - Total login attempts
- `customer_login_failures_total` - Total failed logins
- `password_reset_requests_total` - Total password reset requests

Access metrics at `/actuator/prometheus` or `/actuator/metrics`.

## Events Published

| Event | Topic | Description |
|-------|-------|-------------|
| CustomerRegistered | customer.registered | New customer registered |
| CustomerVerified | customer.verified | Email verified |
| CustomerUpdated | customer.updated | Profile updated |
| CustomerDeleted | customer.deleted | Account deleted |
| CustomerLoggedIn | customer.logged_in | Customer logged in |
| PasswordChanged | customer.password_changed | Password changed |

## Security

### Password Policy

- Minimum 8 characters
- At least one uppercase letter
- At least one lowercase letter
- At least one digit
- At least one special character
- Cannot reuse last 5 passwords

### Authentication

- JWT tokens for API authentication
- Refresh token rotation
- Secure password hashing (bcrypt)
- Account lockout after 5 failed attempts
- Email verification required

### GDPR Compliance

- Data export endpoint for customer data requests
- Account deletion request endpoint
- 30-day retention period for deleted accounts
- Audit logging for all data access

## Development

### Project Structure

```
customer-service/
├── src/main/java/com/gogidix/customer/
│   ├── controller/              # REST controllers
│   ├── service/                 # Business logic
│   ├── repository/              # Data access
│   ├── model/                   # Domain models
│   ├── dto/                     # Data transfer objects
│   ├── mapper/                  # Entity-DTO mappers
│   ├── config/                  # Configuration
│   ├── exception/               # Custom exceptions
│   └── security/                # Security components
├── src/main/resources/
│   ├── db/migration/            # Database migrations
│   ├── templates/               # Email templates
│   └── application.yml          # Configuration
└── src/test/                    # Tests
```

## Troubleshooting

### Common Issues

**Issue**: Email verification not sending
- **Solution**: Check SMTP configuration and email service

**Issue**: Login failing after password reset
- **Solution**: Verify password hashing consistency

**Issue**: Profile update not persisting
- **Solution**: Check database connection and transaction management

## Contributing

1. Create a feature branch
2. Make your changes
3. Add/update tests
4. Ensure all tests pass
5. Submit a pull request

## License

Copyright (c) 2026 Gogidix. All rights reserved.
