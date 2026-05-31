# Courier Domain — Product Requirements Document

## 1. Domain Overview

**Domain:** Courier Services
**Business Group:** E-commerce-business-group
**Package:** `com.gogidix.cargo.courier`
**API Gateway Port:** 8092
**Repository:** `cargoNexus-africa/CargoNexus-Courier-Domain`

CargoNexus Courier provides last-mile delivery services for e-commerce orders. Partners (courier companies) operate networks of riders/drivers who pick up from vendors and deliver to customers.

## 2. Actors & User Roles

| Actor | Frontend | Description |
|-------|----------|-------------|
| Courier Partner Admin | partner-dashboard (Web) | Manages riders, dispatch, pricing, commission, analytics |
| Courier Agent | agent-portal (Web) | Walk-in package intake, tracking, customer service |
| Rider/Driver | rider-app (Mobile) | Picks up and delivers packages, navigation, proof of delivery |

## 3. Frontend Applications (3)

| App | Package | Type | Pages/Screens |
|-----|---------|------|---------------|
| partner-dashboard | @cargonexus/partner-dashboard | Web (Next.js) | 9 pages |
| agent-portal | @cargonexus/agent-portal | Web (Next.js) | 9 pages |
| rider-app | @cargonexus/rider-app | Mobile (Expo) | 8 screens |

## 4. Backend Services (20)

### 4.1 Dispatch (4 services)
- `dispatch-core-service` — Dispatch engine core
- `assignment-service` — Order-to-rider assignment algorithm
- `routing-service` — Route optimization
- `load-balancing-service` — Rider load distribution

### 4.2 Driver/Rider (3 services)
- `driver-pool-service` — Rider registration and pool management
- `availability-service` — Real-time rider availability (online/offline/busy)
- `performance-service` — Rider performance metrics (rating, delivery time, completion rate)

### 4.3 Tracking (4 services)
- `gps-tracking-service` — Real-time GPS tracking
- `location-service` — Location history and geofencing
- `eta-service` — Estimated time of arrival calculation
- `notification-service` — Delivery status notifications

### 4.4 Pricing (3 services)
- `pricing-engine-service` — Dynamic pricing based on distance, weight, zone, time
- `dynamic-pricing-service` — Surge pricing during peak hours
- `discount-service` — Courier discount codes and promotions

### 4.5 Partner (2 services)
- `partner-portal-service` — Partner dashboard data aggregation
- `commission-service` — Partner commission calculation and settlement

### 4.6 Public API (3 services)
- `public-booking-service` — Public delivery booking
- `public-quote-service` — Delivery price quotes
- `public-tracking-service` — Public package tracking

### 4.7 Ecommerce Integration (1 service)
- `ecommerce-integration-service` — Bidirectional sync with ecommerce domain

### 4.8 Tenant (1 service)
- `tenant-config-service` — Multi-tenant configuration

## 5. Revenue Streams

| Channel | Model | Target |
|---------|-------|--------|
| Delivery fee per order | Dynamic pricing (distance, weight, zone) | Customers |
| Partner commission | 15-25% of delivery fee | Courier partners |
| Surge pricing premium | 1.5-3x during peak | Customers |
| Express delivery premium | Fixed premium for <2h delivery | Customers |

## 6. Cross-Domain Dependencies

| Dependency | Domain | Via |
|-----------|--------|-----|
| Order details | Ecommerce | Kafka (`cargo.ecommerce.courier.requested`) |
| Delivery address | Ecommerce | REST (gateway) |
| Payment settlement | Banking | Kafka (`cargo.banking.settlement.processed`) |
| Rider verification | Admin (shared) | JWT + REST |
| IoT tracking | IoTMonitoring (shared) | Kafka |

## 7. Kafka Topics

| Topic | Producer | Consumer |
|-------|----------|----------|
| `cargo.courier.dispatch.assigned` | assignment-service | rider-app, ecommerce |
| `cargo.courier.delivery.picked-up` | rider-app | ecommerce, partner-dashboard |
| `cargo.courier.delivery.completed` | rider-app | ecommerce, banking, partner-dashboard |
| `cargo.courier.delivery.failed` | rider-app | ecommerce, partner-dashboard |
| `cargo.courier.rider.location-updated` | gps-tracking-service | ecommerce (realtime tracking), location-service |
| `cargo.courier.pricing.quote-generated` | pricing-engine-service | public-quote-service |

## 8. Multi-Tenancy

- `tenantId` on every entity
- Partner-level isolation (each courier partner sees only their data)
- Country-level data residency (NG, GH, KE)
- HQ aggregation across all partners and countries

## 9. Success Metrics

| Metric | Target |
|--------|--------|
| Partner onboarding | 20 partners in 6 months |
| Active riders | 500 riders in 6 months |
| On-time delivery rate | >90% |
| Average delivery time | <4 hours (same-city) |
| POD capture rate | >98% |