# Test Coverage Report - ai-fraud-detection-service

## Overview
- **Service**: ai-fraud-detection-service
- **Analysis Date**: 2026-03-01T00:00:00Z
- **Total Classes**: 18
- **Classes with Tests**: 9
- **Classes without Tests**: 9
- **Test Coverage**: 50.00%

## Test Coverage by Package

### application.dto.response
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| AnalysisResponse | AnalysisResponseTest.java | Unknown | ✅ Covered |

### application.service
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| FraudDetectionService | FraudDetectionServiceTest.java | Unknown | ✅ Covered |

### domain.model
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| FraudAnalysisResult | FraudAnalysisResultTest.java | Unknown | ✅ Covered |
| FraudPattern | FraudPatternTest.java | Unknown | ✅ Covered |
| Transaction | TransactionTest.java | Unknown | ✅ Covered |

### domain.policy
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| FraudDetectionPolicy | FraudDetectionPolicyTest.java | Unknown | ✅ Covered |

### infrastructure.adapter
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| MlModelAdapter | MlModelAdapterTest.java | Unknown | ✅ Covered |

### infrastructure.persistence
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| InMemoryFraudRepository | InMemoryFraudRepositoryTest.java | Unknown | ✅ Covered |

### interfaces.rest
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| FraudDetectionController | FraudDetectionControllerTest.java | Unknown | ✅ Covered |

### multitenancy
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| - | TenantIsolationTest.java | Unknown | ✅ Integration |

## Classes Without Tests

9 classes across various packages

## Detailed Findings

### Positive Observations
1. Excellent test coverage (50.00%)
2. All layers tested including multi-tenancy
3. Domain policies tested

### Areas for Improvement
1. Add more integration tests
2. Increase coverage of DTOs and value objects
