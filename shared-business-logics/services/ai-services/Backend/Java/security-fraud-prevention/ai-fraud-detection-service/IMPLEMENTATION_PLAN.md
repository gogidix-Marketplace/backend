# Implementation Plan: Fix Coverage Gap & Smoke Test Issues

## Problem Statement
The Financial-Grade CI pipeline has two issues:
1. **Coverage Gap**: 74% coverage vs 85% requirement (11% gap)
2. **Smoke Test Timeout**: MongoDB connection timeout in CI environment

## Coverage Analysis

### Classes with 0% Coverage (Critical)

| Class | Package | Lines | Priority |
|-------|---------|-------|----------|
| FraudDetectionMetrics.java | infrastructure.metrics | 167 | HIGH |
| ComplianceReportGenerator.java | infrastructure.governance | 297 | HIGH |
| ThresholdValidator.java | infrastructure.governance | 183 | HIGH |
| MongoConfig.java | infrastructure.persistence.mongodb | 66 | MEDIUM |
| MongoFraudRepositoryAdapter.java | infrastructure.persistence.mongodb | 223 | MEDIUM |

### Classes with Low Coverage (<85%)

| Class | Package | Est. Coverage | Lines |
|-------|---------|---------------|-------|
| FraudAnalysisResultEntity | infrastructure.persistence.mongodb | ~60% | ~150 |
| FraudPatternEntity | infrastructure.persistence.mongodb | ~60% | ~120 |
| Various DTOs | application.dto.response | ~0-50% | ~100 |

---

## Phase 1: Add Missing Tests (Coverage Gap Fix)

### Task 1.1: FraudDetectionMetricsTest
**File**: `src/test/java/.../infrastructure/metrics/FraudDetectionMetricsTest.java`

**Test Coverage Required**:
- Constructor initialization with MeterRegistry
- All counter increment methods:
  - `incrementAnalysisTotal()`
  - `incrementAnalysisSuccess()`
  - `incrementAnalysisFailure()`
  - `incrementFraudDetected()`
  - `incrementHighRisk()`
  - `incrementBlocked()`
- All timer methods:
  - `recordAnalysisTime(long durationMs)`
  - `startAnalysisTimer()` / `stopAnalysisTimer(Timer.Sample sample)`
  - `startMlPredictionTimer()` / `stopMlPredictionTimer(Timer.Sample sample)`
  - `startDatabaseSaveTimer()` / `stopDatabaseSaveTimer(Timer.Sample sample)`
- SLO compliance methods:
  - `getAnalysisLatencyP95()`
  - `getAnalysisLatencyP99()`
  - `getMlPredictionLatencyP95()`
  - `getErrorRate()`

**Expected Coverage**: 95%+

---

### Task 1.2: ThresholdValidatorTest
**File**: `src/test/java/.../infrastructure/governance/ThresholdValidatorTest.java`

**Test Coverage Required**:
- Constructor with FraudDetectionMetrics
- `validateAllThresholds()` - returns ComplianceReport with all checks
- `validateThreshold(ThresholdType type)` for each type:
  - P95_LATENCY
  - P99_LATENCY
  - ML_PREDICTION_LATENCY
  - ERROR_RATE
- `isDegraded()` - returns boolean based on compliance
- `getHealthStatus()` - returns HEALTHY/DEGRADED/UNHEALTHY

**Test Scenarios**:
- All thresholds within limits → HEALTHY
- Some thresholds exceeded → DEGRADED
- Most thresholds exceeded → UNHEALTHY

**Expected Coverage**: 95%+

---

### Task 1.3: ComplianceReportGeneratorTest
**File**: `src/test/java/.../infrastructure/governance/ComplianceReportGeneratorTest.java`

**Test Coverage Required**:
- Constructor with FraudDetectionMetrics and ThresholdValidator
- `generateReport()` - returns complete GovernanceReport:
  - Report ID format (GOV-yyyMMdd-HHmmss)
  - SLO compliance section populated
  - Performance metrics section populated
  - Governance status calculated correctly
- `GovernanceReport.toMap()` - converts to Map structure

**Test Scenarios**:
- Generate report with healthy metrics
- Generate report with degraded metrics
- Generate report with unhealthy metrics
- Verify all sections are populated

**Expected Coverage**: 90%+

---

### Task 1.4: MongoConfigTest
**File**: `src/test/java/.../infrastructure/persistence/mongodb/MongoConfigTest.java`

**Test Coverage Required**:
- `getDatabaseName()` - returns database name from property
- `mongoClient()` - creates MongoClient with correct connection string
- `mongoTemplate()` - creates MongoTemplate
- `mongoLoggingEventListener()` - creates LoggingEventListener

**Test Strategy**:
- Unit tests with @ExtendWith(MockitoExtension.class)
- Mock @Value properties or use @TestPropertySource
- Verify MongoClient and MongoTemplate are created

**Expected Coverage**: 85%+

---

### Task 1.5: MongoFraudRepositoryAdapterTest
**File**: `src/test/java/.../infrastructure/persistence/mongodb/MongoFraudRepositoryAdapterTest.java`

**Note**: May already exist - enhance if needed

**Test Coverage Required**:
- `saveAnalysisResult()` - upsert behavior
- `findAnalysisById()` - returns Optional
- `findByUserId()` - with/without limit
- `savePattern()` - increment occurrence
- `getActivePatterns()` - active only
- `getActivePatternsByTenant()` - tenant isolation
- `findByTenantId()` - tenant filtering
- `deletePattern()` - soft delete
- `getTenantStats()` - statistics

**Expected Coverage**: 85%+

---

## Phase 2: Fix Smoke Test MongoDB Timeout

### Root Cause Analysis
The smoke tests use `@SpringBootTest` which:
- Loads full Spring application context
- Requires real MongoDB connection
- Can timeout during service container startup

### Solution Options

#### Option A: Keep Current Tests + Add Better MongoDB Health Check
- Add retry logic to workflow
- Increase MongoDB health-start-period
- Add test retry configuration

#### Option B: Create Lightweight Smoke Tests (RECOMMENDED)
Create true smoke tests that:
- Use `@WebMvcTest` for controller health endpoint
- Don't load full application context
- Don't require MongoDB connection

**New File**: `SmokeTest.java`
```java
@WebMvcTest(HealthController.class)
class SmokeTest {
    @Test
    void healthEndpointReturns200() { ... }
}
```

---

## Implementation Order

1. **Task 1.1**: FraudDetectionMetricsTest (fastest, highest impact)
2. **Task 1.2**: ThresholdValidatorTest (medium complexity)
3. **Task 1.3**: ComplianceReportGeneratorTest (depends on metrics)
4. **Task 1.4**: MongoConfigTest (simple unit test)
5. **Task 1.5**: MongoFraudRepositoryAdapterTest (may exist, enhance)
6. **Phase 2**: Fix smoke test timeout

---

## Success Criteria

### Coverage Goal
- **Before**: 74% line coverage
- **After**: 85%+ line coverage (Financial-Grade requirement)
- All packages must meet 85% threshold

### Smoke Test Goal
- Smoke tests pass consistently in CI
- Tests complete within 2 minutes
- No MongoDB timeout errors

### Validation
```bash
# Run tests with coverage
mvn clean test -Pci

# Verify coverage
mvn jacoco:check

# Run smoke tests
mvn verify -Pci -Dtest=SmokeTest
```

---

## Files to Create

| File | Purpose | Lines (est.) |
|------|---------|--------------|
| FraudDetectionMetricsTest.java | Test metrics class | ~200 |
| ThresholdValidatorTest.java | Test threshold validation | ~150 |
| ComplianceReportGeneratorTest.java | Test report generation | ~180 |
| MongoConfigTest.java | Test MongoDB config | ~80 |
| SmokeTest.java | Lightweight smoke tests | ~50 |

**Total**: ~660 lines of test code

---

## Estimated Timeline

| Task | Duration |
|------|----------|
| Write FraudDetectionMetricsTest | 30 min |
| Write ThresholdValidatorTest | 25 min |
| Write ComplianceReportGeneratorTest | 30 min |
| Write MongoConfigTest | 15 min |
| Enhance MongoFraudRepositoryAdapterTest | 20 min |
| Fix smoke test timeout | 15 min |
| **Total** | **~2 hours** |
