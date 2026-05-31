# Test Coverage Report - Executive-domain-X

**Generated:** 2026-04-05  
**Target Coverage:** 85%  
**Domain:** Management-domain/Executive-domain-X

---

## Executive Summary

| Service Type | Current Coverage | Target | Gap | Status |
|--------------|------------------|--------|-----|--------|
| Java Services | 13% | 85% | -72% | ❌ Below Target |
| Node.js Services | Pending | 85% | - | ⏳ Dependencies Required |

---

## Java Services Coverage

### Overall Coverage (executive-dashboard-service)

| Metric | Coverage | Missed | Total | % of Target |
|--------|----------|--------|-------|-------------|
| **Instructions** | 13% | 10,685 / 12,350 | 1,665 | 15% |
| **Branches** | 1% | 1,458 / 1,475 | 17 | 1% |
| **Lines** | 32% | 485 / 709 | 224 | 38% |
| **Methods** | 22% | 701 / 901 | 200 | 26% |
| **Classes** | 42% | 44 / 76 | 32 | 49% |

### Package-Level Coverage

| Package | Instruction | Branch | Lines | Methods | Classes |
|---------|-------------|--------|-------|---------|---------|
| `application.command` | 27% | 3% | 34% | 29% | 82% |
| `application.query` | 30% | 3% | 44% | 50% | 71% |
| `application.dto` | 5% | 0% | 7% | 25% | 25% |
| `domain.model` | 8% | 0% | 14% | 18% | 0% |
| `domain.service` | 0% | n/a | 0% | 0% | 0% |
| `infrastructure.config` | 0% | 0% | 0% | 0% | 0% |
| `com.gogidix.management.executive` | 0% | n/a | 0% | 0% | 0% |

### Test Results

| Metric | Value |
|--------|-------|
| Tests Run | 17 |
| Passed | 16 |
| Skipped | 1 |
| Failures | 0 |
| Errors | 0 |

### Test Files

| Test File | Tests | Status |
|-----------|-------|--------|
| DashboardCommandServiceTest.java | 5 | ✅ Passing |
| DashboardQueryServiceTest.java | 6 | ✅ Passing |
| KpiWidgetCommandServiceTest.java | 5 | ✅ Passing |
| ExecutiveDashboardServiceApplicationTests.java | 1 | ⏭️ Skipped |

### Coverage Gaps - Areas Needing Tests

#### High Priority (0% coverage)
- `infrastructure.config` - SecurityConfig, MongoDB config
- `domain.service` - DashboardDomainService
- `application.dto` - DashboardDto, KpiWidgetDto, CreateDashboardRequest

#### Medium Priority (<15% coverage)
- `domain.model` - Dashboard, KpiWidget, BaseEntity
- `application.dto` - All DTOs

#### Low Priority (15-30% coverage)
- `application.command` - Command handlers
- `application.query` - Query handlers

---

## Node.js Services Coverage

### Service Status

| Service | Test Files | Dependencies | Coverage |
|---------|------------|--------------|----------|
| executive-command-service | 2 | ❌ Not installed | Pending |
| executive-query-service | 17 | ❌ Not installed | Pending |
| executive-realtime-service | 14 | ❌ Not installed | Pending |
| kafka-consumer-service | 22 | ❌ Not installed | Pending |
| websocket-service | 22 | ❌ Not installed | Pending |
| **Total** | **77** | **5 services** | **Pending** |

### Test File Inventory

```
executive-command-service/
├── src/models/KPI.test.js
└── src/services/KPICommandService.test.js

executive-query-service/
├── (17 test files across models, services, controllers)

executive-realtime-service/
├── (14 test files for realtime functionality)

kafka-consumer-service/
├── (22 test files for Kafka message handling)

websocket-service/
├── (22 test files for WebSocket operations)
```

---

## Recommendations

### To Achieve 85% Coverage Target

#### Java Services
1. **Add Infrastructure Tests** (0% → 40%)
   - Security configuration tests
   - MongoDB repository integration tests
   - Redis cache tests

2. **Add Domain Model Tests** (8% → 60%)
   - Entity validation tests
   - Builder pattern tests
   - Soft delete functionality tests

3. **Add DTO Tests** (5% → 80%)
   - Serialization/deserialization tests
   - Validation annotation tests

4. **Add Service Layer Tests** (0% → 90%)
   - Business logic tests
   - Domain service tests
   - Edge case handling

#### Node.js Services
1. **Install Dependencies**
   ```bash
   cd Executive-domain-X/Backend/Nodes
   for service in */; do cd "$service" && npm install && cd ..; done
   ```

2. **Run Coverage Report**
   ```bash
   npm test -- --coverage
   ```

3. **Review Coverage Output**
   - Check jest coverage reports in `coverage/` directory
   - Identify untested modules
   - Add tests for critical paths

---

## Commands to Generate Coverage Reports

### Java (JaCoCo)
```bash
cd Executive-domain-X/Backend/Java/executive-dashboard-service
mvn clean test jacoco:report
# Report generated at: target/site/jacoco/index.html
```

### Node.js (Jest)
```bash
cd Executive-domain-X/Backend/Nodes/[service-name]
npm install
npm test -- --coverage
# Report generated at: coverage/lcov-report/index.html
```

---

## Appendix

### Coverage Tool Configuration

#### Java (pom.xml)
```xml
<plugin>
  <groupId>org.jacoco</groupId>
  <artifactId>jacoco-maven-plugin</artifactId>
  <version>0.8.11</version>
</plugin>
```

#### Node.js (package.json)
```json
{
  "jest": {
    "collectCoverageFrom": [
      "src/**/*.js",
      "!src/server.js"
    ],
    "coverageThreshold": {
      "global": {
        "branches": 80,
        "functions": 80,
        "lines": 80,
        "statements": 80
      }
    }
  }
}
```

---

**Report Location:** `Executive-domain-X/TEST_COVERAGE_REPORT.md`

---

*Next Review: After implementing additional tests*
*Owner: Development Team*
*Contact: DevOps Gogidix*
