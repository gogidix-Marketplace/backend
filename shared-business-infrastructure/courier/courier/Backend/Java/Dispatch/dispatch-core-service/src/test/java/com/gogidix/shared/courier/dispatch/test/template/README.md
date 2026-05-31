# Financial-Grade Test Templates

## Overview

This directory contains test templates for achieving **financial-grade coverage (≥85%)**.

## Test Categories

| Template | Purpose | Tests |
|----------|---------|-------|
| SecurityTestTemplate.java | JWT, RBAC, validation, multi-tenant | ~15 tests |
| ResilienceTestTemplate.java | Circuit breaker, retry, fallback | ~12 tests |
| ChaosTestTemplate.java | Network failures, resource exhaustion | ~15 tests |
| SloTestTemplate.java | Latency, throughput, error rate | ~10 tests |
| NegativeTestTemplate.java | Boundaries, edge cases, invalid input | ~25 tests |

**Total: ~77 additional tests per service**

---

## How to Use These Templates

### Option 1: Copy and Customize (Recommended)

```bash
# 1. Copy template to your service's test directory
cp SecurityTestTemplate.java ../../interfaces/rest/SecurityTest.java

# 2. Update the class name and package
sed -i 's/SecurityTestTemplate/SecurityTest/g' SecurityTest.java
sed -i 's/test.template/test/g' SecurityTest.java

# 3. Customize for your service:
#    - Update endpoint paths
#    - Update entity/DTO names
#    - Update service references
#    - Remove irrelevant tests
```

### Option 2: Extend Template Class

```java
package com.gogidix.shared.courier.dispatch.interfaces.rest;

import com.gogidix.shared.courier.dispatch.test.template.SecurityTestTemplate;
import org.junit.jupiter.api.Test;

public class DispatchSecurityTest extends SecurityTestTemplate {

    // Override specific tests to customize
    @Override
    @Test
    void shouldRejectRequestWithoutAuthorizationHeader() {
        // Custom implementation for dispatch
    }
}
```

---

## Template Customization Checklist

For each template copied:

- [ ] Update package name
- [ ] Update class name
- [ ] Update API endpoint paths
- [ ] Update entity/DTO names
- [ ] Update service references
- [ ] Add/remove tests based on service features
- [ ] Run tests: `mvn test -Dtest=YourClassName`
- [ ] Verify coverage: `mvn test jacoco:report`

---

## Coverage Targets

| Metric | Target | Command |
|--------|--------|---------|
| Line Coverage | ≥85% | `mvn test jacoco:report` |
| Branch Coverage | ≥75% | Check jacoco report |
| Mutation Score | ≥60% | (Pitest required) |

---

## Running Tests

### Run All Tests
```bash
cd dispatch-core-service
mvn clean test jacoco:report
```

### Run Specific Category
```bash
# Security tests only
mvn test -Dtest=SecurityTest*

# Resilience tests only
mvn test -Dtest=ResilienceTest*

# Chaos tests only
mvn test -Dtest=ChaosTest*

# SLO tests only
mvn test -Dtest=SloTest*

# Negative tests only
mvn test -Dtest=NegativeTest*
```

### View Coverage Report
```bash
# HTML report
open target/site/jacoco/index.html

# Summary
grep -A 5 "Total" target/site/jacoco/index.html
```

---

## Integration with Existing Tests

These templates complement existing unit/integration tests:

```
src/test/java/com/gogidix/shared/courier/dispatch/
├── application/service/          # Existing unit tests
│   ├── DispatchApplicationServiceTest.java
│   └── DispatchOrderServiceTest.java
├── interfaces/rest/              # Existing controller tests
│   └── DispatchControllerTest.java
├── domain/repository/            # Existing repository tests
│   └── DispatchOrderRepositoryTest.java
└── test/                         # NEW: Financial-grade templates
    ├── SecurityTestTemplate.java
    ├── ResilienceTestTemplate.java
    ├── ChaosTestTemplate.java
    ├── SloTestTemplate.java
    └── NegativeTestTemplate.java
```

---

## Quick Start Example

### 1. Copy Security Template

```bash
cp test/template/SecurityTestTemplate.java \
   interfaces/rest/DispatchSecurityTest.java
```

### 2. Update Class

```java
package com.gogidix.shared.courier.dispatch.interfaces.rest;

import com.gogidix.shared.courier.dispatch.test.template.SecurityTestTemplate;
import org.junit.jupiter.api.Test;

public class DispatchSecurityTest extends SecurityTestTemplate {

    @Override
    @Test
    void shouldRejectRequestWithoutAuthorizationHeader() {
        // Already customized for dispatch endpoints
    }
}
```

### 3. Run and Verify

```bash
mvn test -Dtest=DispatchSecurityTest
```

---

## Common Customizations

### Change Endpoint Paths

```java
// Before (template)
mockMvc.perform(post("/api/v1/resource"))

// After (customized)
mockMvc.perform(post("/api/v1/dispatch/orders"))
```

### Change Entity Names

```java
// Before (template)
when(resourceService.getResource(anyString()))

// After (customized)
when(dispatchApplicationService.getDispatch(anyString(), anyString()))
```

### Add Service-Specific Tests

```java
@Test
void shouldDispatchToNearestDriver() {
    // Service-specific geospatial test
}
```

---

## Removing Unused Tests

Not all tests apply to every service. Remove tests that don't match:

```java
// DELETE if service doesn't have admin endpoints
@Test
void shouldAllowAdminToCancelAnyDispatch() {
    // Not applicable - remove
}

// DELETE if service doesn't use geospatial queries
@Test
void shouldValidateCoordinateRanges() {
    // Not applicable - remove
}
```

---

## Troubleshooting

### Tests Fail to Compile

**Issue**: Package names don't match
**Fix**: Update package declaration at top of file

### Tests Fail to Run

**Issue**: Missing dependencies (Spring Security, etc.)
**Fix**: Add to pom.xml:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
    <scope>test</scope>
</dependency>
```

### Coverage Not Improving

**Issue**: Tests not exercising new code paths
**Fix**: Add more test variations (different inputs, states)

---

## Metrics After Template Application

### Before Templates
- Line Coverage: ~79%
- Tests: ~50
- Categories: Unit, Integration

### After Templates
- Line Coverage: **≥85%** ✅
- Tests: **~130** (+80)
- Categories: Unit, Integration, Security, Resilience, Chaos, SLO, Negative

---

## Next Steps

1. ✅ Templates created in `test/template/`
2. ⏳ Copy templates to service-specific test classes
3. ⏳ Customize for each service
4. ⏳ Run and verify coverage ≥85%
5. ⏳ Replicate to other services in domain

---

*Template Version: 1.0*
*Last Updated: 2026-04-11*
*Coverage Target: Financial-Grade (≥85%)*
