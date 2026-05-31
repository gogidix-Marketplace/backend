# Test Coverage Report - lead-generation-ai-service

## Overview
- **Service**: lead-generation-ai-service
- **Analysis Date**: 2026-03-01T00:00:00Z
- **Total Classes**: 40
- **Classes with Tests**: 10
- **Classes without Tests**: 30
- **Test Coverage**: 25.00%

## Test Coverage by Package

### application.dto
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| LeadGenerationDto | LeadGenerationDtoTest.java | Unknown | ✅ Covered |

### application.service
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| LeadGenerationService | LeadGenerationServiceTest.java | Unknown | ✅ Covered |

### domain.aggregate
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| Lead | LeadTest.java | Unknown | ✅ Covered |

### domain.event
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| LeadActivity | LeadActivityTest.java | Unknown | ✅ Covered |

### domain.model
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| ContactInfo | ContactInfoTest.java | Unknown | ✅ Covered |
| LeadScore | LeadScoreTest.java | Unknown | ✅ Covered |
| LeadSource | LeadSourceTest.java | Unknown | ✅ Covered |

### domain.policy
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| LeadScoringPolicy | LeadScoringPolicyTest.java | Unknown | ✅ Covered |

### infrastructure.adapter
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| AiLeadScoringAdapter | AiLeadScoringAdapterTest.java | Unknown | ✅ Covered |

### infrastructure.persistence
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| InMemoryLeadDataSource | InMemoryLeadDataSourceTest.java | Unknown | ✅ Covered |
| LeadRepositoryImpl | LeadRepositoryImplTest.java | Unknown | ✅ Covered |

### interfaces.rest
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| LeadGenerationController | LeadGenerationControllerTest.java | Unknown | ✅ Covered |

## Classes Without Tests

30 classes across various packages

## Test Method Details

- 10 comprehensive test files covering DTOs, services, aggregates, events, models, policies, adapters, repositories, and controllers

## Detailed Findings

### Positive Observations
1. Good layered test coverage
2. Tests cover domain logic, application services, infrastructure, and interfaces
3. Domain policies tested

### Areas for Improvement
1. Add integration tests
2. Increase coverage of value objects and utilities
