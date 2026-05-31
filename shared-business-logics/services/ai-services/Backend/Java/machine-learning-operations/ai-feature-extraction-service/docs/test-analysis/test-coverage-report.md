# Test Coverage Report - ai-feature-extraction-service

## Overview
- **Service**: ai-feature-extraction-service
- **Analysis Date**: 2026-03-01T00:00:00Z
- **Total Classes**: 20
- **Classes with Tests**: 9
- **Classes without Tests**: 11
- **Test Coverage**: 45.00%

## Test Coverage by Package

### application.dto
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| ExtractFeaturesRequestDto | ExtractFeaturesRequestDtoTest.java | Unknown | ✅ Covered |
| FeatureSetResponseDto | FeatureSetResponseDtoTest.java | Unknown | ✅ Covered |
| PagedResponseDto | PagedResponseDtoTest.java | Unknown | ✅ Covered |

### application.service
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| FeatureExtractionApplicationService | FeatureExtractionApplicationServiceTest.java | Unknown | ✅ Covered |

### architecture
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| - | HexagonalArchitectureTests.java | Unknown | ✅ Architecture |

### domain.model
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| FeatureSet | FeatureSetTest.java | Unknown | ✅ Covered |
| FeatureValue | FeatureValueTest.java | Unknown | ✅ Covered |

### infrastructure.external
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| FeatureStoreServiceClient | FeatureStoreServiceClientTest.java | Unknown | ✅ Covered |

### infrastructure.persistence
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| MongoFeatureSetRepository | MongoFeatureSetRepositoryTest.java | Unknown | ✅ Covered |

### interfaces.rest
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| FeatureExtractionController | FeatureExtractionControllerTest.java | Unknown | ✅ Covered |

## Classes Without Tests

11 classes across various packages

## Detailed Findings

### Positive Observations
1. Good test coverage (45.00%)
2. Architecture tests included
3. All layers tested

### Areas for Improvement
1. Add integration tests
2. Increase coverage of value objects and DTOs
