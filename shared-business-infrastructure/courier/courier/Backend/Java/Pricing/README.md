# Pricing Engine Service

Core pricing calculation engine for courier services.

## Overview

The Pricing Engine Service calculates delivery prices based on multiple factors including distance, duration, vehicle type, time of day, and demand. It supports dynamic pricing, discounts, and surge multipliers.

## Features

- Base price calculation
- Distance-based pricing
- Time-based surcharges
- Vehicle type adjustments
- Dynamic surge pricing
- Discount application
- Multi-currency support
- Tax calculation

## API Endpoints

### Pricing Operations

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/pricing/calculate` | Calculate price |
| POST | `/api/v1/pricing/estimate` | Get quick estimate |
| GET | `/api/v1/pricing/tenants/{id}/config` | Get pricing config |
| PUT | `/api/v1/pricing/tenants/{id}/config` | Update pricing config |

### Price Components

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/pricing/components` | List price components |
| GET | `/api/v1/pricing/surcharges` | List available surcharges |

## Configuration

```yaml
spring:
  application:
    name: pricing-engine-service
  data:
    mongodb:
      uri: ${MONGODB_URI}
      database: courier_core

server:
  port: 8112

courier:
  pricing:
    default-currency: USD
    tax-rate: 0.08
    base-distance-km: 5
    base-price: 10.00
```

## Pricing Algorithm

### Base Formula

```
Base Price = Pickup Fee + (Distance × Distance Rate) + (Duration × Time Rate)
Total = (Base Price × Surge Multiplier) - Discounts + Taxes
```

### Price Components

| Component | Description |
|-----------|-------------|
| `pickupFee` | Fixed fee for pickup |
| `distanceFee` | Per-kilometer charge |
| `timeFee` | Per-minute charge |
| `serviceFee` | Platform service fee |
| `surgeMultiplier` | Demand-based multiplier |
| `discountAmount` | Applied discounts |
| `taxAmount` | Calculated taxes |

## Data Model

### Price Request

```json
{
  "tenantId": "uuid",
  "pickupLocation": {
    "latitude": 40.7128,
    "longitude": -74.0060,
    "address": "123 Main St"
  },
  "deliveryLocation": {
    "latitude": 40.7580,
    "longitude": -73.9855,
    "address": "456 Oak Ave"
  },
  "distance": 5240,
  "duration": 900,
  "vehicleType": "CAR",
  "scheduledFor": "2026-02-20T14:30:00Z",
  "items": [
    {
      "weight": 2.5,
      "dimensions": {"length": 30, "width": 20, "height": 10}
    }
  ]
}
```

### Price Response

```json
{
  "quoteId": "uuid",
  "basePrice": 15.00,
  "distanceFee": 5.24,
  "timeFee": 3.00,
  "serviceFee": 2.50,
  "subtotal": 25.74,
  "surgeMultiplier": 1.0,
  "surgeAmount": 0.00,
  "discounts": [
    {
      "code": "SAVE20",
      "amount": 5.15,
      "type": "PERCENTAGE"
    }
  ],
  "discountedAmount": 5.15,
  "taxAmount": 1.65,
  "total": 22.24,
  "currency": "USD",
  "validUntil": "2026-02-20T14:15:00Z",
  "breakdown": [...]
}
```

## Vehicle Type Pricing

| Type | Base Fee | Distance Rate | Time Rate | Capacity |
|------|----------|---------------|-----------|----------|
| `MOTORCYCLE` | $5.00 | $0.50/km | $0.10/min | 5kg |
| `CAR` | $8.00 | $0.80/km | $0.15/min | 50kg |
| `VAN` | $12.00 | $1.20/km | $0.20/min | 200kg |
| `TRUCK` | $20.00 | $2.00/km | $0.30/min | 1000kg |

## Surge Pricing

Surge multipliers based on demand:

| Demand Level | Multiplier | Trigger |
|-------------|------------|---------|
| `NORMAL` | 1.0x | Default |
| `ELEVATED` | 1.2x | Order/driver ratio > 2 |
| `HIGH` | 1.5x | Order/driver ratio > 4 |
| `EXTREME` | 2.0x+ | Order/driver ratio > 6 |

## Events

| Event | Topic | Description |
|-------|-------|-------------|
| `price.calculated` | `pricing.events` | Price calculated |
| `surge.activated` | `pricing.events` | Surge pricing activated |
| `surge.deactivated` | `pricing.events` | Surge pricing deactivated |

## Monitoring

Metrics at `/actuator/prometheus`:

- `price_calculations_total`: Total price calculations
- `price_calculation_duration_seconds`: Calculation time
- `surge_multiplier_current`: Current surge multiplier
- `discounts_applied_total`: Total discounts applied

## Dependencies

- Spring Boot 3.x
- Spring Data MongoDB
- Dynamic Pricing Service
- Discount Service
- Location Service (for distance)

## Related Services

- **Dynamic Pricing Service**: Surge pricing logic
- **Discount Service**: Promo code validation
- **Public Quote Service`: Public price quotes

## Support

- GitHub: https://github.com/gogidix/courier-services/issues
- Documentation: https://docs.gogidix.com/courier/pricing
