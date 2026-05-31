# Test Coverage Report - territory-management-service

## Overview
- **Service**: territory-management-service
- **Sub-Domain**: Sales-department
- **Analysis Date**: 2026-03-06T10:24:22Z
- **Total Source Files**: 46
- **Total Test Files**: 4
- **Source Classes (significant)**: 34
- **Test Classes**: 4
- **Test Methods**: 36
- **Test Coverage**: 11%

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

### TerritoryQueryServiceTest
- **Path**: `src/test/java/com/gogidix/sales/territory/application/service/TerritoryQueryServiceTest.java`
- **Test Methods**: 3

### TerritoryCommandServiceTest
- **Path**: `src/test/java/com/gogidix/sales/territory/test/unit/application/TerritoryCommandServiceTest.java`
- **Test Methods**: 8
- **Method Names**: testActivateTerritory_NotFound, testActivateTerritory_Success, testCreateTerritory_CodeAlreadyExists, testCreateTerritory_Success, testDeleteTerritory_HasChildTerritories, testDeleteTerritory_Success, testRequestRealignment_Success, testUpdatePerformance_Success

### QuotaTest
- **Path**: `src/test/java/com/gogidix/sales/territory/test/unit/domain/QuotaTest.java`
- **Test Methods**: 12
- **Method Names**: testActivateQuota, testAddBreakdown, testAdjustAmount, testCancelQuota, testCannotActivateNonDraftQuota, testCreateQuota, testGetRemainingAmount, testGetRemainingAmountWhenOverAchieved, testPauseQuota, testResumeQuota, testUpdateAchievement, testUpdateAchievementCompletesQuota

### TerritoryTest
- **Path**: `src/test/java/com/gogidix/sales/territory/test/unit/domain/TerritoryTest.java`
- **Test Methods**: 13
- **Method Names**: testActivateTerritory, testAddChildTerritory, testAddProductCategory, testArchiveTerritory, testCannotActivateArchivedTerritory, testCompleteRealignment, testCreateTerritory, testGeographicOverlapDetection, testIsActive, testRemoveChildTerritory, testRemoveProductCategory, testRequestRealignment, testUpdatePerformance

## Source Classes (Significant)

- `QuotaCommandService` - **Tested**: No
- `QuotaQueryService` - **Tested**: No
- `TerritoryAssignmentCommandService` - **Tested**: No
- `TerritoryAssignmentQueryService` - **Tested**: No
- `TerritoryCommandService` - **Tested**: Yes
- `TerritoryQueryService` - **Tested**: Yes
- `QuotaUpdatedEvent` - **Tested**: No
- `TerritoryAssignedEvent` - **Tested**: No
- `TerritoryCreatedEvent` - **Tested**: No
- `BaseEntity` - **Tested**: No
- `Quota` - **Tested**: Yes
- `Territory` - **Tested**: Yes
- `TerritoryAssignment` - **Tested**: No
- `QuotaCommand` - **Tested**: No
- `TerritoryAssignmentCommand` - **Tested**: No
- `TerritoryCommand` - **Tested**: No
- `EventPublisher` - **Tested**: No
- `OverlapDetectionService` - **Tested**: No
- `QuotaRepository` - **Tested**: No
- `TerritoryAssignmentRepository` - **Tested**: No
- `TerritoryRepository` - **Tested**: No
- `KafkaEventPublisher` - **Tested**: No
- `MongoQuotaRepository` - **Tested**: No
- `MongoTerritoryAssignmentRepository` - **Tested**: No
- `MongoTerritoryRepository` - **Tested**: No
- `SecurityConfig` - **Tested**: No
- `OverlapDetectionServiceImpl` - **Tested**: No
- `GlobalExceptionHandler` - **Tested**: No
- `QuotaController` - **Tested**: No
- `TerritoryAssignmentController` - **Tested**: No
- `BaseEntity` - **Tested**: No
- `RequestContext` - **Tested**: No
- `RequestContextHolder` - **Tested**: No
- `TerritoryManagementApplication` - **Tested**: No
