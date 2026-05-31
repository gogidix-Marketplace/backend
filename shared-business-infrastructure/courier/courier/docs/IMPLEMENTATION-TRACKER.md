# Courier Domain — Implementation Tracker

## Status Legend
- [ ] Not started
- [~] In progress
- [x] Complete

---

## 1. Infrastructure

| Component | Status | Notes |
|-----------|--------|-------|
| API Gateway (port 8092) | [x] | Scaffolded — Spring Cloud Gateway |
| Docker compose | [x] | `docker-compose-courier.yml` exists |
| CI/CD (.github) | [x] | Workflows configured |
| Root POM | [x] | All 20 modules + api-gateway listed |
| Shared libraries | [x] | `shared-libraries/Backend/Java` |

## 2. Backend Services

| Group | Services | Status | Notes |
|-------|----------|--------|-------|
| Dispatch | dispatch-core, assignment, routing, load-balancing | [~] | Core logic exists |
| Driver | driver-pool, availability, performance | [~] | Structure exists |
| Tracking | gps-tracking, location, eta, notification | [~] | GPS tracking needs Mapbox integration |
| Pricing | pricing-engine, dynamic-pricing, discount | [~] | Dynamic pricing algorithm needed |
| Partner | partner-portal, commission | [~] | Dashboard data aggregation needed |
| Public API | public-booking, public-quote, public-tracking | [~] | API layer over internal services |
| Ecommerce Integration | ecommerce-integration | [~] | Kafka consumer/producer needed |
| Tenant | tenant-config | [~] | Multi-tenant foundation |

## 3. Frontend Applications

| App | Scaffold | Domain Types | UI Implementation | Design Mockup | Status |
|-----|----------|-------------|-------------------|---------------|--------|
| partner-dashboard | [x] | [x] | [ ] | [ ] | Placeholder |
| agent-portal | [x] | [x] | [ ] | [ ] | Placeholder |
| rider-app | [x] | [x] | [ ] | [ ] | Placeholder |

## 4. Cross-Cutting Concerns

| Concern | Status | Notes |
|---------|--------|-------|
| Kafka event schemas | [ ] | No Avro/Proto definitions |
| Mapbox integration | [ ] | API key and SDK setup |
| Offline-first (rider-app) | [ ] | Local storage, sync queue |
| POD capture (OTP, photo, signature) | [ ] | Camera integration |
| GPS tracking optimization | [ ] | Battery-efficient location updates |

## 5. Production Readiness Checklist

- [ ] All services compile and pass unit tests
- [ ] API gateway routes verified
- [ ] Kafka producers/consumers tested
- [ ] Rider app offline mode tested
- [ ] GPS tracking battery benchmark
- [ ] POD capture end-to-end test
- [ ] Docker images built and pushed
- [ ] Security scan passed
- [ ] Load test (100 concurrent riders)