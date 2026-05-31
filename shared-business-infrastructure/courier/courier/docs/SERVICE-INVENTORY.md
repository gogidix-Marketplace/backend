# Courier Domain — Service Inventory

## Service Count: 20 microservices + 1 API gateway + 3 frontends

---

## API Gateway

| Name | Port | Tech |
|------|------|------|
| courier-api-gateway | 8092 | Spring Cloud Gateway, WebFlux, Redis, JWT |

---

## Backend Services (by group)

### Dispatch (4)
| Service | Package | Description | Status |
|---------|---------|-------------|--------|
| dispatch-core-service | com.gogidix.cargo.courier.dispatch | Dispatch engine core | Scaffolded |
| assignment-service | com.gogidix.cargo.courier.dispatch | Order-to-rider matching | Scaffolded |
| routing-service | com.gogidix.cargo.courier.dispatch | Route optimization | Scaffolded |
| load-balancing-service | com.gogidix.cargo.courier.dispatch | Rider load distribution | Scaffolded |

### Driver/Rider (3)
| Service | Package | Description | Status |
|---------|---------|-------------|--------|
| driver-pool-service | com.gogidix.cargo.courier.driver | Rider registration and management | Scaffolded |
| availability-service | com.gogidix.cargo.courier.driver | Real-time online/offline/busy status | Scaffolded |
| performance-service | com.gogidix.cargo.courier.driver | Rider performance metrics | Scaffolded |

### Tracking (4)
| Service | Package | Description | Status |
|---------|---------|-------------|--------|
| gps-tracking-service | com.gogidix.cargo.courier.tracking | Real-time GPS position | Scaffolded |
| location-service | com.gogidix.cargo.courier.tracking | Location history, geofencing | Scaffolded |
| eta-service | com.gogidix.cargo.courier.tracking | ETA calculation | Scaffolded |
| notification-service | com.gogidix.cargo.courier.tracking | Delivery status notifications | Scaffolded |

### Pricing (3)
| Service | Package | Description | Status |
|---------|---------|-------------|--------|
| pricing-engine-service | com.gogidix.cargo.courier.pricing | Dynamic pricing | Scaffolded |
| dynamic-pricing-service | com.gogidix.cargo.courier.pricing | Surge pricing | Scaffolded |
| discount-service | com.gogidix.cargo.courier.pricing | Courier discounts | Scaffolded |

### Partner (2)
| Service | Package | Description | Status |
|---------|---------|-------------|--------|
| partner-portal-service | com.gogidix.cargo.courier.partner | Partner dashboard data | Scaffolded |
| commission-service | com.gogidix.cargo.courier.partner | Commission calculation | Scaffolded |

### Public API (3)
| Service | Package | Description | Status |
|---------|---------|-------------|--------|
| public-booking-service | com.gogidix.cargo.courier.publicapi | Public delivery booking | Scaffolded |
| public-quote-service | com.gogidix.cargo.courier.publicapi | Delivery price quotes | Scaffolded |
| public-tracking-service | com.gogidix.cargo.courier.publicapi | Public package tracking | Scaffolded |

### Ecommerce Integration (1)
| Service | Package | Description | Status |
|---------|---------|-------------|--------|
| ecommerce-integration-service | com.gogidix.cargo.courier.ecommerce | Ecommerce domain sync | Scaffolded |

### Tenant (1)
| Service | Package | Description | Status |
|---------|---------|-------------|--------|
| tenant-config-service | com.gogidix.cargo.courier.tenant | Multi-tenant config | Scaffolded |

---

## Frontend Applications (3)

| App | Package | Type | Framework |
|-----|---------|------|-----------|
| partner-dashboard | @cargonexus/partner-dashboard | Web | Next.js 14.2.3 |
| agent-portal | @cargonexus/agent-portal | Web | Next.js 14.2.3 |
| rider-app | @cargonexus/rider-app | Mobile | Expo ~51.0.0 |