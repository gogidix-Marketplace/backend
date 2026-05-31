# Dispatch-Service Blueprint Summary

**Service**: dispatch-core-service
**Domain**: shared-courier-core
**Date**: 2026-04-11
**Status**: ✅ Blueprint Complete

---

## What Was Created

### Test Templates (5 files)

| Template | File | Purpose | Test Count |
|----------|------|---------|------------|
| Security | `SecurityTestTemplate.java` | JWT, RBAC, validation, multi-tenant | 15 |
| Resilience | `ResilienceTestTemplate.java` | Circuit breaker, retry, fallback | 12 |
| Chaos | `ChaosTestTemplate.java` | Network failures, resource exhaustion | 15 |
| SLO | `SloTestTemplate.java` | Latency, throughput, error rate | 10 |
| Negative | `NegativeTestTemplate.java` | Boundaries, edge cases | 25 |
| Coverage | `CoverageVerificationTest.java` | Verify ≥85% target | 4 |
| **Total** | **7 files** | **All categories covered** | **~81 tests** |

### Supporting Files

| File | Purpose |
|------|---------|
| `README.md` | How to use templates |
| `COVERAGE.md` | Coverage targets and commands |

---

## Fixes Applied to Service

### 1. BaseEntity Inheritance Issue ✅

**Problem**: `DispatchOrder` extended `BaseEntity` causing Lombok @Builder issues

**Solution**: Removed inheritance, added fields directly to `DispatchOrder`

```java
// BEFORE
public class DispatchOrder extends BaseEntity {
    @Id
    private String id;  // Duplicate
    ...
}

// AFTER
public class DispatchOrder {
    @Id
    private String id;

    @Indexed
    private String tenantId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
    ...
}
```

### 2. Repository Mock Fixes ✅

**Problem**: Tests returning `List` instead of `Optional`

**Solution**: Fixed mock return types in `DispatchOrderServiceTest.java`

---

## Test Template Features

### Security Tests

| Test | Description |
|------|-------------|
| `shouldRejectRequestWithoutAuthorizationHeader` | Missing JWT rejection |
| `shouldRejectMalformedJwtToken` | Invalid JWT format |
| `shouldRejectExpiredJwtToken` | Expired token handling |
| `shouldAllowAdminToCancelAnyDispatch` | Admin role access |
| `shouldAllowDriverToViewOnlyOwnDispatches` | Driver scope |
| `shouldDenyCustomerAccessToAdminEndpoints` | Role enforcement |
| `shouldPreventCrossTenantDataAccess` | Multi-tenant isolation |
| `shouldRequireTenantIdHeader` | Header validation |
| `shouldValidateTenantIdFormat` | Format sanitization |
| `shouldPreventSqlInjectionInOrderId` | SQL injection prevention |
| `shouldPreventXssInDeliveryAddress` | XSS prevention |
| `shouldValidateCoordinateRanges` | Geospatial validation |
| `shouldSanitizeHtmlInNotes` | HTML sanitization |
| `shouldMaskSensitiveCustomerData` | PII masking |
| `shouldEncryptSensitiveMetadata` | Encryption at rest |

### Resilience Tests

| Test | Description |
|------|-------------|
| `shouldOpenCircuitBreakerAfterThresholdFailures` | Circuit breaker |
| `shouldTransitionToHalfOpenAfterTimeout` | Recovery state |
| `shouldCloseCircuitBreakerAfterSuccessfulRequest` | Full recovery |
| `shouldRetryOnTransientFailures` | Retry logic |
| `shouldNotRetryOnClientErrors` | No retry on 4xx |
| `shouldRespectMaxRetryLimit` | Retry limit |
| `shouldUseExponentialBackoff` | Backoff strategy |
| `shouldReturnCachedDataOnServiceFailure` | Cache fallback |
| `shouldUseDefaultDriverOnAssignmentFailure` | Default fallback |
| `shouldReturnSafeDefaultsOnDashboardFailure` | Safe defaults |
| `shouldTimeoutSlowRequests` | Timeout handling |
| `shouldCompleteWithinTimeout` | SLA timeout |

### Chaos Tests

| Test | Description |
|------|-------------|
| `shouldHandleConnectionTimeout` | Network timeout |
| `shouldHandleReadTimeout` | Read timeout |
| `shouldHandleConnectionReset` | Connection reset |
| `shouldHandleDnsFailure` | DNS failure |
| `shouldHandleMongoConnectionPoolExhaustion` | DB pool |
| `shouldHandleMongoQueryTimeout` | Query timeout |
| `shouldHandleMongoPrimaryFailover` | Replica failover |
| `shouldHandleDuplicateKeyException` | Duplicate key |
| `shouldHandleKafkaBrokerUnavailable` | Kafka down |
| `shouldHandleKafkaProducerTimeout` | Kafka timeout |
| `shouldHandleKafkaSerializationFailure` | Serialization |
| `shouldHandleMemoryPressure` | Memory pressure |
| `shouldHandleThreadPoolExhaustion` | Thread pool |
| `shouldHandleDiskSpaceExhaustion` | Disk full |
| `shouldFunctionWithPartialServiceFailure` | Partial outage |

### SLO Tests

| Test | Target | Description |
|------|--------|-------------|
| `getOrderShouldMeetP50Latency` | 50ms | Median response time |
| `getOrderShouldMeetP95Latency` | 200ms | 95th percentile |
| `getOrderShouldMeetP99Latency` | 500ms | 99th percentile |
| `nearbyPickupsShouldMeetP95Latency` | 300ms | Geospatial queries |
| `dashboardShouldMeetP95Latency` | 500ms | Aggregation queries |
| `shouldHandleMinimumThroughput` | 100 RPS | Throughput |
| `shouldHandleBurstTraffic` | 80% success | Burst handling |
| `errorRateShouldBeBelowThreshold` | 0.1% | Error rate |
| `healthEndpointShouldRespond` | 200 OK | Availability |
| `memoryUsageShouldBeReasonable` | 80% | Memory limit |

### Negative Tests

| Test | Description |
|------|-------------|
| `shouldRejectNullDispatchId` | Null handling |
| `shouldRejectEmptyDispatchId` | Empty string |
| `shouldRejectBlankDispatchId` | Whitespace only |
| `shouldRejectOversizedDispatchId` | Max length |
| `shouldAcceptMaxValidDispatchId` | Boundary valid |
| `shouldRejectNegativeLatitude` | Invalid latitude |
| `shouldRejectLatitudeAboveMax` | Invalid latitude |
| `shouldAcceptValidLatitudeBoundaries` | ±90 degrees |
| `shouldRejectNegativeDistance` | Negative radius |
| `shouldHandleZeroRadius` | Zero boundary |
| `shouldRejectInvalidPriority` | Invalid enum |
| `shouldRejectNullCustomerId` | Null required field |
| `shouldRejectInvalidStatusTransition` | State machine |
| `shouldRejectReassignmentOfAssignedDispatch` | Business rule |
| `shouldRejectCancellationOfDeliveredDispatch` | State check |
| `shouldHandleSpecialCharactersInAddress` | Special chars |
| `shouldHandleNullMetadata` | Null optional |
| `shouldHandleNullOptionalFields` | Optional fields |
| `shouldHandleConcurrentAssignmentAttempts` | Concurrency |
| `shouldHandleConcurrentCancellationAttempts` | Concurrency |
| `shouldHandleConcurrentStatusUpdates` | Optimistic lock |
| `shouldHandleLargeMetadata` | Large payload |
| `shouldRejectOversizedRequest` | Max size |
| `shouldHandleIdenticalPickupAndDelivery` | Business rule |
| `shouldHandleAntipodalLocations` | Geospatial edge |
| `shouldHandleDateBoundary` | Date boundary |
| `shouldHandleLeapYearDates` | Leap year |

---

## Coverage Targets

| Metric | Target | Current | Gap |
|--------|--------|---------|-----|
| Line Coverage | ≥85% | TBD | - |
| Branch Coverage | ≥75% | TBD | - |
| Mutation Score | ≥60% | TBD | - |

**Note**: Full coverage measurement requires all tests passing. Controller tests need MongoDB Testcontainers configuration.

---

## How to Use This Blueprint

### For Other Courier Services

```bash
# 1. Copy template directory
cp -r dispatch-core-service/src/test/java/com/gogidix/shared/courier/dispatch/test/template \
      <other-service>/src/test/java/com/gogidix/shared/courier/<category>/test/

# 2. Update package names
find <other-service>/src/test -name "*.java" -exec sed -i 's/dispatch/<category>/g' {} \;

# 3. Customize for each service
#    - Update entity names
#    - Update API endpoints
#    - Update service references
#    - Remove irrelevant tests

# 4. Run tests
cd <other-service>
mvn test jacoco:report
```

### For Other Domains

```bash
# Copy entire dispatch-core-service as reference
cp -r shared-courier-core/Backend/Java/Dispatch/dispatch-core-service \
      <domain>/<reference-service>

# Apply same patterns to services in new domain
```

---

## Files Created/Modified

### Created Files

```
dispatch-core-service/
└── src/test/java/com/gogidix/shared/courier/dispatch/
    └── test/
        ├── template/
        │   ├── SecurityTestTemplate.java      (266 lines)
        │   ├── ResilienceTestTemplate.java     (279 lines)
        │   ├── ChaosTestTemplate.java         (331 lines)
        │   ├── SloTestTemplate.java           (319 lines)
        │   ├── NegativeTestTemplate.java      (511 lines)
        │   └── README.md                      (292 lines)
        └── CoverageVerificationTest.java      (155 lines)
```

### Modified Files

```
dispatch-core-service/
├── src/main/java/.../dispatch/domain/entity/
│   ├── DispatchOrder.java                    (Fixed BaseEntity)
│   └── BaseEntity.java                      (No longer extended)
└── src/test/java/.../dispatch/application/service/
    └── DispatchOrderServiceTest.java         (Fixed mocks)
```

---

## Known Issues

### Controller Tests (MongoDB Connection)

**Issue**: `DispatchControllerTest` fails to load ApplicationContext due to MongoDB connection

**Fix Options**:
1. Add `@SpringBootTest` with embedded MongoDB
2. Use `@Testcontainers` with MongoDB
3. Use `@WebMvcTest` with mocked service layer (already done)

**Current Status**: Templates use `@SpringBootTest` for flexibility. For pure unit tests, change to `@WebMvcTest` with `@MockBean`.

---

## Next Steps

### Immediate

1. ✅ Templates created
2. ✅ BaseEntity fixed
3. ✅ Repository mocks fixed
4. ⏳ Fix controller test configuration
5. ⏳ Run full test suite
6. ⏳ Verify ≥85% coverage

### For Replication

1. Copy templates to next service
2. Customize for service specifics
3. Run and verify coverage
4. Repeat for all services in domain

---

## Commands Reference

```bash
# Navigate to service
cd shared-courier-core/Backend/Java/Dispatch/dispatch-core-service

# Run all tests
mvn test jacoco:report

# Run specific test category
mvn test -Dtest=SecurityTest*

# Run only unit tests
mvn test -DskipITs=true

# View coverage report
open target/site/jacoco/index.html
```

---

*Blueprint Version: 1.0*
*Created: 2026-04-11*
*Coverage Target: Financial-Grade (≥85%)*
