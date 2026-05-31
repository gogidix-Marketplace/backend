# Pricing Engine Service

Multi-tenant pricing calculation engine with support for distance-based, weight-based, and express delivery pricing.

## Overview

This service manages pricing rules and calculates delivery quotes based on various factors including distance, weight, and service type.

## Features

- **Multi-tenant Architecture**: Complete tenant isolation with tenantId-based data segregation
- **Flexible Pricing Models**: Support for multiple pricing strategies (distance-based, weight-based, flat-rate, dynamic, tiered)
- **Express Multiplier**: Automatic pricing adjustment for express deliveries
- **Price Breakdown**: Detailed breakdown of pricing components
- **Time-based Rules**: Support for effective date ranges on pricing rules
- **Quote Management**: Generate quotes with validity periods

## Technology Stack

- **Java 17**
- **Spring Boot 3.1.5**
- **Spring Data MongoDB**
- **Lombok** for reducing boilerplate
- **Testcontainers** for integration testing

## Database Schema

### PricingRule Document

```java
@Document(collection = "pricing_rules")
@CompoundIndex(def = "{'tenantId': 1, 'ruleId': 1}", unique = true)
public class PricingRule {
    @Id
    private String id;

    @Indexed
    private String tenantId;

    @Indexed
    private String ruleId;

    private String serviceType; // STANDARD, EXPRESS, SAME_DAY
    private PricingModel pricingModel;

    // Distance-based pricing
    private Double basePrice;
    private Double pricePerKm;

    // Weight-based pricing
    private Double pricePerKg;

    // Speed-based pricing
    private Double expressMultiplier;

    private String currency;
    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

## Pricing Calculation Logic

### Standard Delivery
```
Total Price = Base Price + (Distance × Price per Km) + (Weight × Price per Kg)
```

### Express Delivery
```
Total Price = (Base Price + Distance Price + Weight Price) × Express Multiplier
```

### Example Calculation
For a 10km, 5kg delivery with standard pricing:
- Base Price: $10.00
- Distance: 10km × $1.50/km = $15.00
- Weight: 5kg × $0.50/kg = $2.50
- **Standard Total**: $27.50

For express delivery (1.5x multiplier):
- **Express Total**: $27.50 × 1.5 = $41.25

## API Endpoints

### Pricing Rule Management

#### Create Pricing Rule
```http
POST /api/v1/pricing/rules
X-Tenant-ID: tenant-001
Content-Type: application/json

{
  "serviceType": "STANDARD",
  "pricingModel": "DISTANCE_BASED",
  "basePrice": 10.0,
  "pricePerKm": 1.5,
  "pricePerKg": 0.5,
  "expressMultiplier": 1.5,
  "currency": "USD",
  "effectiveFrom": "2024-01-01T00:00:00",
  "effectiveTo": "2024-12-31T23:59:59"
}
```

#### Get All Pricing Rules
```http
GET /api/v1/pricing/rules
X-Tenant-ID: tenant-001
```

#### Get Specific Pricing Rule
```http
GET /api/v1/pricing/rules/{ruleId}
X-Tenant-ID: tenant-001
```

#### Update Pricing Rule
```http
PUT /api/v1/pricing/rules/{ruleId}
X-Tenant-ID: tenant-001
Content-Type: application/json

{
  "basePrice": 15.0,
  "pricePerKm": 2.0
}
```

#### Delete Pricing Rule
```http
DELETE /api/v1/pricing/rules/{ruleId}
X-Tenant-ID: tenant-001
```

### Quote Calculation

#### Calculate Price Quote
```http
POST /api/v1/pricing/calculate
X-Tenant-ID: tenant-001
Content-Type: application/json

{
  "serviceType": "STANDARD",
  "distanceKm": 10.0,
  "weightKg": 5.0
}
```

Response:
```json
{
  "quoteId": "quote-uuid",
  "tenantId": "tenant-001",
  "serviceType": "STANDARD",
  "totalPrice": 27.50,
  "currency": "USD",
  "breakdown": {
    "base": 10.0,
    "distance": 15.0,
    "weight": 2.5
  },
  "validUntil": "2024-01-01T01:00:00"
}
```

## Configuration

### application.yml

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017
      database: shared_courier
      auto-index-creation: true

server:
  port: 8085
```

## Running Tests

### Unit Tests
```bash
mvn test
```

### Integration Tests (with Testcontainers)
```bash
mvn verify
```

## Building

```bash
mvn clean package
```

This creates an executable JAR file at:
```
target/pricing-engine-service-1.0.0.jar
```

## Health Check

```bash
curl http://localhost:8085/actuator/health
```

Response:
```json
{
  "status": "UP"
}
```

## Service Types

The service supports multiple delivery service types:

- **STANDARD**: Regular delivery with standard pricing
- **EXPRESS**: Fast delivery with express multiplier applied
- **SAME_DAY**: Same-day delivery with premium pricing

## Pricing Models

- **DISTANCE_BASED**: Pricing primarily based on distance traveled
- **WEIGHT_BASED**: Pricing primarily based on package weight
- **FLAT_RATE**: Fixed pricing regardless of distance/weight
- **DYNAMIC**: Pricing adjusted based on demand and other factors
- **TIERED**: Different pricing for different distance/weight tiers

## Multi-Tenancy

All operations are scoped to a specific tenant using the `X-Tenant-ID` header:
- All database queries include tenantId filtering
- Compound indexes ensure tenant-rule uniqueness
- Complete data isolation between tenants
