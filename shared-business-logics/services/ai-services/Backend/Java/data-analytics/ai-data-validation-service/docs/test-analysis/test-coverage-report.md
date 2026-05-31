# Test Coverage Report - ai-data-validation-service

## Overview
- **Service**: ai-data-validation-service
- **Analysis Date**: 2026-03-01T00:00:00Z
- **Total Classes**: 45
- **Classes with Tests**: 14
- **Classes without Tests**: 31
- **Test Coverage**: 31.11%

## Test Coverage by Package

### application.service
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| ValidationService | ValidationServiceTest.java | Unknown | ✅ Covered |

### domain.aggregate
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| ValidationExecution | ValidationExecutionTest.java | Unknown | ✅ Covered |

### domain.model
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| ValidationResult | ValidationResultTest.java | Unknown | ✅ Covered |
| ValidationRule | ValidationRuleTest.java | Unknown | ✅ Covered |

### infrastructure.adapter
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| DataSourceAdapter | DataSourceAdapterTest.java | Unknown | ✅ Covered |

### infrastructure.persistence
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| ValidationRepositoryImpl | ValidationRepositoryImplTest.java | Unknown | ✅ Covered |

### interfaces.rest
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| ValidationController | ValidationControllerTest.java | Unknown | ✅ Covered |

## Classes Without Tests

31 classes across various packages

## Detailed Findings

### Positive Observations
1. Good test coverage (31.11%)
2. Tests across all layers

### Areas for Improvement
1. Add integration tests
2. Increase coverage of DTOs and value objects
