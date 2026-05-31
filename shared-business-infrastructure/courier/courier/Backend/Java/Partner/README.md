# Partner Portal Service

Partner driver and fleet management portal.

## Overview

The Partner Portal Service provides APIs and business logic for the partner web dashboard, enabling partner companies to manage their fleet of drivers and track earnings.

## Features

- Partner registration and onboarding
- Fleet management
- Driver management
- Earnings tracking
- Commission calculations
- Performance analytics
- Payout management

## API Endpoints

### Partner Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/partners/{id}` | Get partner details |
| PUT | `/api/v1/partners/{id}` | Update partner profile |
| POST | `/api/v1/partners` | Register new partner |
| GET | `/api/v1/partners/{id}/settings` | Get partner settings |

### Fleet Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/partners/{id}/drivers` | List partner drivers |
| POST | `/api/v1/partners/{id}/drivers` | Add driver to fleet |
| DELETE | `/api/v1/partners/{id}/drivers/{driverId}` | Remove driver |
| GET | `/api/v1/partners/{id}/vehicles` | List partner vehicles |

### Earnings

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/partners/{id}/earnings` | Get earnings summary |
| GET | `/api/v1/partners/{id}/earnings/history` | Get earnings history |
| POST | `/api/v1/partners/{id}/payouts` | Request payout |
| GET | `/api/v1/partners/{id}/payouts` | Get payout history |

### Commission

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/partners/{id}/commission` | Get commission structure |
| GET | `/api/v1/partners/{id}/commission/transactions` | Get commission transactions |

## Configuration

```yaml
spring:
  application:
    name: partner-portal-service
  data:
    mongodb:
      uri: ${MONGODB_URI}
      database: courier_core

server:
  port: 8116

courier:
  partner:
    commission-rate: 0.15
    payout-threshold: 100
    payout-frequency: WEEKLY
```

## Data Model

### Partner Schema

```json
{
  "id": "uuid",
  "tenantId": "uuid",
  "businessName": "Acme Delivery LLC",
  "contact": {
    "name": "John Smith",
    "email": "john@acmedelivery.com",
    "phone": "+1234567890"
  },
  "address": {
    "street": "123 Business Ave",
    "city": "New York",
    "state": "NY",
    "zip": "10001",
    "country": "USA"
  },
  "settings": {
    "commissionRate": 0.15,
    "payoutFrequency": "WEEKLY",
    "currency": "USD"
  },
  "status": "ACTIVE",
  "createdAt": "2026-01-01T00:00:00Z"
}
```

### Driver Fleet Item

```json
{
  "driverId": "uuid",
  "partnerId": "uuid",
  "status": "ACTIVE",
  "joinedAt": "2026-01-15T00:00:00Z",
  "performance": {
    "totalDeliveries": 245,
    "onTimeRate": 0.94,
    "averageRating": 4.7
  }
}
```

## Earnings Calculation

```
Commission = (Dispatch Price - Platform Fee) × Commission Rate
Partner Earnings = Sum of All Commissions
```

## Monitoring

Metrics at `/actuator/prometheus`:

- `partners_total`: Total registered partners
- `partner_drivers_total`: Total partner drivers
- `partner_earnings_total`: Total partner earnings
- `partner_payouts_total`: Total payouts processed

## Dependencies

- Spring Boot 3.x
- Spring Data MongoDB
- Commission Service
- Driver Pool Service
- Performance Service

## Related Services

- **Commission Service**: Commission calculations
- **Driver Pool Service**: Driver data
- **Performance Service**: Performance metrics
- **Partner Dashboard**: Frontend application

## Support

- GitHub: https://github.com/gogidix/courier-services/issues
- Documentation: https://docs.gogidix.com/courier/partner-portal
