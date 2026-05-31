# Test Coverage Report - ai-customer-segmentation-service

## Overview
- **Service**: ai-customer-segmentation-service
- **Analysis Date**: 2026-03-01T00:00:00Z
- **Total Classes**: 85
- **Classes with Tests**: 9
- **Classes without Tests**: 76
- **Test Coverage**: 10.59%

## Test Coverage by Package

### application.command
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| AddUsersToProfileCommand | AddUsersToProfileCommandTest.java | 11 | ✅ Covered |
| CreateProfileCommand | CreateProfileCommandTest.java | 14 | ✅ Covered |
| AnalyzeProfileCommand | - | 0 | ❌ No Tests |
| CreateUserProfileCommand | - | 0 | ❌ No Tests |
| DeleteUserProfileCommand | - | 0 | ❌ No Tests |
| DeleteProfileCommand | - | 0 | ❌ No Tests |
| RemoveUsersFromProfileCommand | - | 0 | ❌ No Tests |
| UpdateUserProfileCommand | - | 0 | ❌ No Tests |
| UpdateProfileCommand | - | 0 | ❌ No Tests |

### application.dto
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| PagedResponseDto | PagedResponseDtoTest.java | 4 | ✅ Covered |
| AnalyzeProfileRequestDto | - | 0 | ❌ No Tests |
| CreateProfileRequestDto | - | 0 | ❌ No Tests |
| UserProfileResponseDto | - | 0 | ❌ No Tests |
| ErrorResponseDto | - | 0 | ❌ No Tests |
| ModifyUsersRequestDto | - | 0 | ❌ No Tests |
| ProfileAnalysisResponseDto | - | 0 | ❌ No Tests |
| ProfileSearchRequestDto | - | 0 | ❌ No Tests |
| UpdateProfileRequestDto | - | 0 | ❌ No Tests |

### application.query
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| FindProfilesByTenantQuery | FindProfilesByTenantQueryTest.java | 3 | ✅ Covered |
| FindProfileByIdQuery | - | 0 | ❌ No Tests |

### application.service
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| UserProfileApplicationService | - | 0 (covered by integration tests) | ⚠️ Partial |
| ProfileAnalysisService | - | 0 | ❌ No Tests |

### domain.model
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| UserProfile | UserProfileTest.java | 32 | ✅ Covered |
| UserProfile | - | 0 | ❌ No Tests |
| ProfileCriteria | - | 0 | ❌ No Tests |
| ProfileStatus | - | 0 | ❌ No Tests |
| ProfileType | - | 0 | ❌ No Tests |

### domain.event
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| DomainEvent | - | 0 | ❌ No Tests |
| UsersAddedToProfileEvent | - | 0 | ❌ No Tests |
| ProfileCreatedEvent | - | 0 | ❌ No Tests |
| ProfileDeletedEvent | - | 0 | ❌ No Tests |
| ProfileUpdatedEvent | - | 0 | ❌ No Tests |

### domain.policy
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| MaxProfilesPerTenantPolicy | - | 0 | ❌ No Tests |
| MinimumUserCountPolicy | - | 0 | ❌ No Tests |
| ProfileBusinessPolicy | - | 0 | ❌ No Tests |
| ProfileNameUniquePolicy | - | 0 | ❌ No Tests |

### domain.port.in
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| CreateProfileCommand | - | 0 | ❌ No Tests |
| UserProfileServicePort | - | 0 | ❌ No Tests |
| DeleteProfileCommand | - | 0 | ❌ No Tests |
| GetProfileQuery | - | 0 | ❌ No Tests |
| ListProfilesQuery | - | 0 | ❌ No Tests |
| ProfileAnalysisQuery | - | 0 | ❌ No Tests |
| UpdateProfileCommand | - | 0 | ❌ No Tests |

### domain.port.out
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| UserProfileRepositoryPort | - | 0 | ❌ No Tests |
| ExternalUserDataProviderPort | - | 0 | ❌ No Tests |
| ProfileEventPublisherPort | - | 0 | ❌ No Tests |

### domain.repository
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| UserProfileRepository | - | 0 | ❌ No Tests |

### infrastructure.persistence
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| UserProfileRepositoryAdapter | - | 0 (covered by integration tests) | ⚠️ Partial |
| UserProfileDocument | - | 0 | ❌ No Tests |
| SpringDataUserProfileRepository | - | 0 | ❌ No Tests |

### infrastructure.messaging
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| ProfileEvent | - | 0 | ❌ No Tests |
| ProfileEventPublisher | - | 0 | ❌ No Tests |

### infrastructure.security
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| JwtTokenProvider | JwtTokenProviderTest.java | 11 | ✅ Covered |
| JwtAuthenticationFilter | - | 0 | ❌ No Tests |
| SecurityConfiguration | - | 0 | ❌ No Tests |

### interfaces.rest
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| UserProfileController | - | 0 (covered by integration tests) | ⚠️ Partial |
| GlobalExceptionHandler | - | 0 | ❌ No Tests |
| HealthController | - | 0 | ❌ No Tests |

### application.mapper
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| UserProfileMapper | - | 0 | ❌ No Tests |

### shared.context
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| RequestContext | RequestContextTest.java | 3 | ✅ Covered |
| RequestContextFilter | - | 0 | ❌ No Tests |
| RequestContextHolder | - | 0 | ❌ No Tests |

### shared.exception
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| BaseDomainException | - | 0 | ❌ No Tests |
| BusinessException | - | 0 | ❌ No Tests |
| ConflictException | - | 0 | ❌ No Tests |
| ErrorResponse | - | 0 | ❌ No Tests |
| NotFoundException | - | 0 | ❌ No Tests |
| ValidationException | - | 0 | ❌ No Tests |

### shared.util
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| CorrelationIdGenerator | CorrelationIdGeneratorTest.java | 2 | ✅ Covered |
| TenantIdGenerator | - | 0 | ❌ No Tests |

### shared.requestcontext (duplicate)
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| RequestContext | - | 0 (covered above) | ✅ Covered |
| RequestContextHolder | - | 0 | ❌ No Tests |

### integration
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| - | UserProfileIntegrationTest.java | 8 | ✅ Integration |

### architecture
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| - | HexagonalArchitectureTests.java | 16 | ✅ Architecture |

### smoke
| Source Class | Test File | Test Methods | Coverage Status |
|--------------|-----------|--------------|-----------------|
| - | EmbeddedSmokeTest.java | 2 | ✅ Smoke |

## Classes Without Tests

1. AnalyzeProfileCommand
2. CreateUserProfileCommand
3. DeleteUserProfileCommand
4. DeleteProfileCommand
5. RemoveUsersFromProfileCommand
6. UpdateUserProfileCommand
7. UpdateProfileCommand
8. AnalyzeProfileRequestDto
9. CreateProfileRequestDto
10. UserProfileResponseDto
11. ErrorResponseDto
12. ModifyUsersRequestDto
13. ProfileAnalysisResponseDto
14. ProfileSearchRequestDto
15. UpdateProfileRequestDto
16. FindProfileByIdQuery
17. UserProfileApplicationService (partially covered by integration tests)
18. ProfileAnalysisService
19. UserProfile
20. ProfileCriteria
21. ProfileStatus
22. ProfileType
23. DomainEvent
24. UsersAddedToProfileEvent
25. ProfileCreatedEvent
26. ProfileDeletedEvent
27. ProfileUpdatedEvent
28. MaxProfilesPerTenantPolicy
29. MinimumUserCountPolicy
30. ProfileBusinessPolicy
31. ProfileNameUniquePolicy
32. All domain.port.in classes
33. All domain.port.out classes
34. UserProfileRepository
35. UserProfileRepositoryAdapter (partially covered by integration tests)
36. UserProfileDocument
37. SpringDataUserProfileRepository
38. ProfileEvent
39. ProfileEventPublisher
40. JwtAuthenticationFilter
41. SecurityConfiguration
42. GlobalExceptionHandler
43. HealthController
44. UserProfileMapper
45. RequestContextFilter
46. RequestContextHolder (shared and shared.requestcontext)
47. All shared.exception classes
48. TenantIdGenerator

## Test Method Details

### AddUsersToProfileCommandTest
- **Source Class**: AddUsersToProfileCommand
- **Test Methods**: 11
- **Test Scenarios**:
  1. `shouldCreateWithValidParameters`: Tests creation with valid parameters
  2. `shouldAcceptNullUserId`: Tests acceptance of null userId
  3. `shouldThrowWhenProfileIdIsNull`: Tests validation of null segmentId
  4. `shouldThrowWhenProfileIdIsBlank`: Tests validation of blank segmentId
  5. `shouldThrowWhenTenantIdIsNull`: Tests validation of null tenantId
  6. `shouldThrowWhenTenantIdIsBlank`: Tests validation of blank tenantId
  7. `shouldThrowWhenUserIdsIsNull`: Tests validation of null customerIds
  8. `shouldThrowWhenUserIdsIsEmpty`: Tests validation of empty customerIds
  9. `shouldThrowWhenUserIdsExceedsMaxSize`: Tests validation of max customerIds size (1000)
  10. `shouldAcceptExactly1000Users`: Tests boundary of 1000 customers
  11. `shouldAcceptSingleUser`: Tests single customer acceptance

### CreateProfileCommandTest
- **Source Class**: CreateProfileCommand
- **Test Methods**: 14
- **Test Scenarios**:
  1. `shouldCreateWithAllValidParameters`: Tests creation with all valid parameters
  2. `shouldAcceptNullDescription`: Tests acceptance of null description
  3. `shouldThrowWhenNameIsNull`: Tests validation of null name
  4. `shouldThrowWhenNameIsBlank`: Tests validation of blank name
  5. `shouldThrowWhenNameExceedsMaxLength`: Tests validation of name > 100 chars
  6. `shouldThrowWhenDescriptionExceedsMaxLength`: Tests validation of description > 500 chars
  7. `shouldThrowWhenProfileTypeIsNull`: Tests validation of null segmentType
  8. `shouldThrowWhenCriteriaIsNull`: Tests validation of null criteria
  9. `shouldThrowWhenCriteriaIsEmpty`: Tests validation of empty criteria
  10. `shouldThrowWhenTenantIdIsNull`: Tests validation of null tenantId
  11. `shouldThrowWhenTenantIdIsBlank`: Tests validation of blank tenantId
  12. `shouldAcceptNullUserId`: Tests acceptance of null userId
  13. `shouldAcceptNameWithExactly100Characters`: Tests boundary of 100 char name
  14. `shouldAcceptDescriptionWithExactly500Characters`: Tests boundary of 500 char description

### UserProfileTest
- **Source Class**: UserProfile
- **Test Methods**: 32
- **Test Scenarios**:
  1. `shouldCreateProfileWithValidParameters`: Tests valid segment creation
  2. `shouldThrowWhenTenantIdIsNull`: Tests null tenantId handling
  3. `shouldThrowWhenNameIsNull`: Tests null name handling
  4. `shouldThrowWhenCriteriaIsNull`: Tests null criteria handling
  5. `shouldActivateProfileWhenDraft`: Tests segment activation
  6. `shouldThrowWhenActivatingActiveProfile`: Tests double activation prevention
  7. `shouldThrowWhenActivatingWithoutCriteria`: Tests activation without criteria
  8. `shouldDeactivateActiveProfile`: Tests segment deactivation
  9. `shouldThrowWhenDeactivatingDraftProfile`: Tests draft deactivation prevention
  10. `shouldArchiveProfile`: Tests segment archiving
  11. `shouldThrowWhenArchivingArchivedProfile`: Tests double archiving prevention
  12. `shouldAddUsersToActiveProfile`: Tests adding customers
  13. `shouldNotAddDuplicateUsers`: Tests duplicate prevention
  14. `shouldThrowWhenAddingUsersToDraftProfile`: Tests draft customer addition prevention
  15. `shouldRemoveUsersFromProfile`: Tests customer removal
  16. `shouldUpdateProfileDetails`: Tests detail updates
  17. `shouldThrowWhenUpdatingArchivedProfile`: Tests archived update prevention
  18. `shouldUpdateCriteriaWhenDraft`: Tests criteria update in draft
  19. `shouldThrowWhenUpdatingCriteriaOfActiveProfile`: Tests active criteria update prevention
  20. `shouldValidateValidProfile`: Tests valid segment validation
  21. `shouldThrowWhenTenantIdIsBlank`: Tests blank tenantId validation
  22. `shouldThrowWhenNameIsBlank`: Tests blank name validation
  23. `shouldThrowWhenNameExceedsMaxLength`: Tests name length validation
  24. `shouldThrowWhenCriteriaIsNull`: Tests null criteria validation
  25. `shouldReturnCorrectActiveStatus`: Tests active status flag
  26. `shouldReturnCorrectModifiableStatus`: Tests modifiable status flag
  27. `shouldMarkAsAnalyzed`: Tests marking as analyzed
  28. `shouldSetUserCount`: Tests customer count setting
  29. `shouldBuildProfileUsingBuilder`: Tests builder pattern
  30. `shouldThrowWhenBuildingWithoutTenantId`: Tests builder validation
  31. `shouldThrowWhenBuildingWithoutName`: Tests builder name validation
  32. `shouldThrowWhenBuildingWithoutCriteria`: Tests builder criteria validation

### JwtTokenProviderTest
- **Source Class**: JwtTokenProvider
- **Test Methods**: 11
- **Test Scenarios**:
  1. `shouldGenerateValidToken`: Tests token generation
  2. `shouldValidateCorrectToken`: Tests valid token validation
  3. `shouldRejectInvalidToken`: Tests invalid token rejection
  4. `shouldRejectNullToken`: Tests null token rejection
  5. `shouldRejectEmptyToken`: Tests empty token rejection
  6. `shouldRejectTamperedToken`: Tests tampered token rejection
  7. `shouldExtractUserIdFromToken`: Tests userId extraction
  8. `shouldExtractTenantIdFromToken`: Tests tenantId extraction
  9. `shouldExtractRolesFromToken`: Tests roles extraction
  10. `shouldGetExpirationDateFromToken`: Tests expiration date extraction
  11. `shouldDetectUnexpiredToken`: Tests unexpired token detection

### UserProfileIntegrationTest
- **Source Class**: Integration test covering UserProfileApplicationService
- **Test Methods**: 8
- **Test Scenarios**:
  1. `shouldCreateAndRetrieveProfile`: Tests create and retrieve flow
  2. `shouldListProfilesForTenant`: Tests listing segments
  3. `shouldFilterProfilesByType`: Tests type filtering
  4. `shouldUpdateProfile`: Tests segment update
  5. `shouldDeleteProfile`: Tests segment deletion
  6. `shouldAddUsersToProfile`: Tests customer addition
  7. `shouldIsolateDataByTenant`: Tests tenant isolation
  8. (8th test covered by smoke tests)

### HexagonalArchitectureTests
- **Test Methods**: 16
- Tests architecture compliance including hexagonal architecture rules, package dependencies, and naming conventions.

### EmbeddedSmokeTest (SmokeTest)
- **Test Methods**: 2
- Tests application context loading and package verification.

### PagedResponseDtoTest
- **Source Class**: PagedResponseDto
- **Test Methods**: 4
- Tests pagination DTO functionality.

### FindProfilesByTenantQueryTest
- **Source Class**: FindProfilesByTenantQuery
- **Test Methods**: 3
- Tests query object creation and validation.

### RequestContextTest
- **Source Class**: RequestContext
- **Test Methods**: 3
- Tests request context creation and tenant/user ID handling.

### CorrelationIdGeneratorTest
- **Source Class**: CorrelationIdGenerator
- **Test Methods**: 2
- Tests correlation ID generation.

## Detailed Findings

### Positive Observations
1. Strong domain model testing with 32 tests for UserProfile
2. Comprehensive command validation tests
3. Integration tests cover critical application flows
4. Architecture tests ensure hexagonal architecture compliance
5. Security components (JwtTokenProvider) well tested

### Areas for Improvement
1. Low overall test coverage (10.59%) - many classes lack unit tests
2. Missing tests for domain events
3. Missing tests for domain policies
4. Missing tests for DTOs (except PagedResponseDto)
5. Missing tests for mappers
6. Missing tests for exception handling
7. Missing tests for controllers
8. ProfileAnalysisService completely untested
9. Infrastructure components (publishers, adapters) lack unit tests
