# service-contract.md
# AI Product Recommendation Service - Service Contract Status: 📝 **DRAFT - PHASE 1**

**Last Updated**: 2025-02-12

---

## Change Log

| Date | Change | Description | Author |
|-------|--------|-------------|----------|
| 2025-02-12 | Initial service contract creation | Claude (Opus 4.6) |

---

## 1. Service Overview

### Purpose
Provide intelligent product recommendations to customers based on:
- Purchase history
- Browsing behavior
- Customer segments
- Inventory availability
- Price elasticity
- Cross-selling opportunities

### Service Responsibilities
- Generate personalized product recommendations
- Rank products by relevance score
- Apply business rules (availability, compatibility)
- Support real-time recommendation queries
- Integrate with customer analytics service
- Publish recommendation events to Kafka

---

## 2. Service Interface Definition

### Input Ports

| Port Name | Direction | Message Format | Description |
|-------------|---------|----------------|-------------|
| `RecommendationRequest` | IN | JSON | Request for product recommendations |
| `ProductQuery` | IN | JSON | Query product details/catalog |
| `RecommendationSync` | IN | JSON | Sync recommendation cache |
| `ProductEvent` | OUT | JSON | Publish product view/interaction events |

### Output Ports

| Port Name | Direction | Message Format | Description |
|-------------|---------|----------------|-------------|
| `RecommendationResponse` | OUT | JSON | Response with ranked products |
| `ProductEvent` | OUT | JSON | Publish recommendation errors |

---

## 3. Domain Model

### Core Entities

| Entity | Status | File |
|---------|--------|-------|
| Product | ✅ Created | `Product.java` |
| ProductRule | ✅ Created | `ProductRule.java` |
| RecommendationRule | ✅ Created | `RecommendationRule.java` |
| RecommendationResult | ✅ Created | `RecommendationResult.java` |
| ProductScore | ✅ Created | `ProductScore.java` |
| RecommendationType | ✅ Created | `RecommendationType.java` |
| RecommendationContext | ✅ Created | `RecommendationContext.java` |

### Domain Services

| Service | Status | File |
|----------|--------|-------|
| ProductRepository | 📝 Pending | `ProductRepository.java` |
| ProductRecommendationService | 📝 Pending | `ProductRecommendationService.java` |

### Domain Layer Status
- [x] Repository port defined
- [ ] Service port defined
- [x] Domain tests created

---

## 4. Application Layer

### Request DTOs

| DTO | Status | File |
|-----|-------|-------|
| RecommendationRequest | ✅ Created | `RecommendationRequestDto.java` |
| RecommendationResponse | ✅ Created | `RecommendationResponseDto.java` |

### Query Objects

| Query | Status | File |
|------|-------|-------|
| ProductQuery | 📝 Pending | `ProductQuery.java` |

### Response DTOs

| DTO | Status | File |
|-----|-------|-------|
| ProductResponse | ✅ Created | `ProductResponseDto.java` |

### Application Services

| Service | Status | File |
|----------|--------|-------|
| ProductRecommendationService | 📝 Pending | `ProductRecommendationService.java` |
| ProductController | 📝 Pending | `ProductController.java` |

### Application Layer Status
- [x] Request DTOs created
- [x] Query objects created
- [x] Response DTOs created
- [ ] Services implemented
- [ ] Tests created

---

## 5. Infrastructure Layer

### Components

| Component | Status | File |
|-----------|--------|-------|
| ProductRepositoryAdapter | 📝 Pending | `ProductRepositoryAdapter.java` |
| ProductDocument | 📝 Pending | `ProductDocument.java` |
| SpringDataProductRepository | 📝 Pending | `SpringDataProductRepository.java` |
| RedisCacheConfig | 📝 Pending | `RedisCacheConfig.java` |
| ProductEventPublisher | 📝 Pending | `ProductEventPublisher.java` |
| ProductController | 📝 Pending | `ProductController.java` |

### Infrastructure Layer Status
- [ ] Persistence components created
- [ ] Caching components created
- [ ] Messaging components created
- [ ] REST controller created
- [ ] Tests created

---

## 6. Test Strategy

### Domain Tests
- [ ] `ProductTest` - Entity validation
- [ ] `ProductRuleTest` - Rule evaluation
- [ ] `RecommendationResultTest` - Result aggregation
- Target: 100% coverage

### Application Tests
- [ ] `RecommendationRequestDtoTest` - Request validation
- [ ] `ProductResponseDtoTest` - Response mapping
- [ ] `ProductQueryTest` - Query validation
- [ ] `ProductRecommendationServiceTest` - Service orchestration
- Target: 100% coverage

### Infrastructure Tests
- [ ] `ProductRepositoryAdapterTest` - MongoDB operations
- [ ] `RedisCacheConfigTest` - Cache configuration
- [ ] `ProductEventPublisherTest` - Event publishing
- [ ] `ProductControllerTest` - REST endpoint validation
- Target: 100% coverage

---

## 7. Implementation Status

### Status: 📝 **PHASE 6 PENDING**

All components are defined in contracts. Implementation must follow strict TDD:
1. Domain tests MUST be written first
2. Then application tests
3. Then infrastructure tests
4. THEN production code

**Current Blocker**: No tests written yet. Must write tests BEFORE implementation.

---

## 8. Technology Stack

| Technology | Version | Purpose |
|-----------|---------|----------|
| Spring Boot | 3.1.5 | Application framework |
| Spring Data MongoDB | 3.1.5 | Persistence framework |
| Spring Data Redis | 3.1.5 | Distributed caching |
| Spring Kafka | 3.1.5 | Event streaming |
| JUnit 5 | 5.10.1 | Testing framework |
| Testcontainers | 1.19.9 | Integration testing |
| Maven | 3.9.11 | Build tool |
| Jacoco | 0.8.11 | Code coverage |

---

## 9. Deployment Configuration

### Application Properties
```yaml
spring:
  application:
    name: AI Product Recommendation Service
  data:
    mongodb:
      host: ${MONGODB_HOST:localhost}
      port: ${MONGODB_PORT:27017}
      database: ai_product_recommendation_db
  redis:
    host: ${REDIS_HOST:localhost}
    port: ${REDIS_PORT:6379}
    cache:
      type: simple
      ttl: 300000
  kafka:
      bootstrap-servers: ${KAFKA_BOOTSTRAP_SERVERS:localhost:9092}
```

### Docker Configuration
- **Base Image**: `eclipse-temurin:17-jre-alpine`
- **Ports**: `8080` (HTTP), `9001` (Actuator)
- **Health Check**: `/actuator/health` returns `{"status":"UP"}`

---

## 10. Status

- [ ] Phase 1 (Service Contract): **COMPLETE**
- [x] Phase 2 (Domain Tests): **PENDING** - Tests not written
- [x] Phase 3 (Application Tests): **PENDING** - Tests not written
- [x] Phase 4 (Interface Tests): **PENDING** - Tests not written
- [x] Phase 5 (Infrastructure Tests): **PENDING** - Tests not written
- [x] Phase 6 (Implementation): **PENDING** - Cannot start without tests
- [x] Phase 7 (Coverage Check): **PENDING** - Cannot check without implementation

---

## Next Steps

1. ✅ **Create domain tests** - Write test files in `src/test/java/domain/`
2. ✅ **Create application tests** - Write test files in `src/test/java/application/`
3. ✅ **Create interface tests** - Write test files in `src/test/java/interfaces/`
4. ✅ **Create infrastructure tests** - Write test files in `src/test/java/infrastructure/`
5. ✅ **Implement service layer** - Write `ProductRecommendationService.java`
6. ✅ **Implement repository adapter** - Write `ProductRepositoryAdapter.java`
7. ✅ **Implement REST controller** - Write `ProductController.java`

**Do NOT proceed to implementation (PHASE 6) until ALL tests (PHASE 2-5) are written.**

---

*Service Contract v1.0.1 - DRAFT PHASE 1*
