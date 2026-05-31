# Test Coverage Report - deal-management-service

## Overview
- **Service**: deal-management-service
- **Sub-Domain**: Sales-department
- **Analysis Date**: 2026-03-06T10:24:22Z
- **Total Source Files**: 37
- **Total Test Files**: 6
- **Source Classes (significant)**: 29
- **Test Classes**: 6
- **Test Methods**: 136
- **Test Coverage**: 20%

## Implementation Gaps Summary
- **Total Gaps**: 0
- **Files with Gaps**: 0
- **TODO**: 0
- **FIXME**: 0
- **XXX**: 0
- **HACK**: 0
- **STUB**: 0
- **PLACEHOLDER**: 0
- **NotImplementedError**: 0
- **UnsupportedOperationException**: 0

## Test Classes Details

### DealCommandServiceTest
- **Path**: `src/test/java/com/gogidix/sales/dealmanagement/application/service/DealCommandServiceTest.java`
- **Test Methods**: 2

### DealTest
- **Path**: `src/test/java/com/gogidix/sales/dealmanagement/domain/model/DealTest.java`
- **Test Methods**: 50
- **Method Names**: testAddActivity, testAddCompetitor, testAddDuplicateTag, testAddDuplicateTeamMember, testAddProduct, testAddTag, testAddTeamMember, testAdvanceStage, testAdvanceStageFromLostThrowsException, testAdvanceStageFromWonThrowsException, testAdvanceStageMultipleTimes, testAdvanceStageToClosedWon, testApprovalStatuses, testApprove, testApproveWhenNotPendingThrowsException, testCheckApprovalNotRequired, testCheckApprovalRequiredWithExecutiveThreshold, testCheckApprovalRequiredWithManagerThreshold, testClearDomainEvents, testCreateDeal, testDealPriorities, testDealStageFromOrder, testDealStagesProbabilities, testDealStatuses, testDomainEventsOnCreate, testDomainEventsOnStageChange, testIsClosedForLostDeal, testIsClosedForOpenDeal, testIsClosedForWonDeal, testIsInPipelineForClosedDeal, testIsInPipelineForOpenDeal, testMarkAsLost, testMarkAsLostWhenAlreadyLostThrowsException, testMarkAsWon, testMarkAsWonWhenAlreadyWonThrowsException, testMarkAsWonWithNullFinalAmount, testRegressStage, testRegressStageWithInvalidTarget, testRejectApproval, testRemoveProduct, testRemoveTag, testRemoveTeamMember, testRequestApproval, testUpdateAmount, testUpdateAmountWithNegativeThrowsException, testUpdateAmountWithZeroThrowsException, testUpdateProbability, testUpdateProbabilityInvalidHigh, testUpdateProbabilityInvalidLow, testWeightedAmountCalculation

### CompetitorTest
- **Path**: `src/test/java/com/gogidix/sales/dealmanagement/unit/domain/CompetitorTest.java`
- **Test Methods**: 15

### DealActivityTest
- **Path**: `src/test/java/com/gogidix/sales/dealmanagement/unit/domain/DealActivityTest.java`
- **Test Methods**: 17

### DealProductTest
- **Path**: `src/test/java/com/gogidix/sales/dealmanagement/unit/domain/DealProductTest.java`
- **Test Methods**: 20

### DealTest
- **Path**: `src/test/java/com/gogidix/sales/dealmanagement/unit/domain/DealTest.java`
- **Test Methods**: 32

## Source Classes (Significant)

- `DealCommandService` - **Tested**: Yes
- `DealQueryService` - **Tested**: No
- `DealManagementServiceApplication` - **Tested**: No
- `DealCreatedEvent` - **Tested**: No
- `DealLostEvent` - **Tested**: No
- `DealStageChangedEvent` - **Tested**: No
- `DealWonEvent` - **Tested**: No
- `BaseEntity` - **Tested**: No
- `Competitor` - **Tested**: Yes
- `Deal` - **Tested**: Yes
- `DealActivity` - **Tested**: Yes
- `DealProduct` - **Tested**: Yes
- `DealCommand` - **Tested**: No
- `EventPublisher` - **Tested**: No
- `CompetitorRepository` - **Tested**: No
- `DealActivityRepository` - **Tested**: No
- `DealProductRepository` - **Tested**: No
- `DealRepository` - **Tested**: No
- `KafkaEventPublisher` - **Tested**: No
- `MongoCompetitorRepository` - **Tested**: No
- `MongoDealActivityRepository` - **Tested**: No
- `MongoDealProductRepository` - **Tested**: No
- `MongoDealRepository` - **Tested**: No
- `SecurityConfig` - **Tested**: No
- `DealController` - **Tested**: No
- `GlobalExceptionHandler` - **Tested**: No
- `BaseEntity` - **Tested**: No
- `RequestContext` - **Tested**: No
- `RequestContextHolder` - **Tested**: No
