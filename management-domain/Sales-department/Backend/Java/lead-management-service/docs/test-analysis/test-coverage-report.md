# Test Coverage Report - lead-management-service

## Overview
- **Service**: lead-management-service
- **Sub-Domain**: Sales-department
- **Analysis Date**: 2026-03-06T10:24:22Z
- **Total Source Files**: 38
- **Total Test Files**: 6
- **Source Classes (significant)**: 29
- **Test Classes**: 6
- **Test Methods**: 99
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

### LeadQueryServiceTest
- **Path**: `src/test/java/com/gogidix/sales/lead/application/service/LeadQueryServiceTest.java`
- **Test Methods**: 4

### LeadCommandServiceTest
- **Path**: `src/test/java/com/gogidix/sales/leadmanagement/application/service/LeadCommandServiceTest.java`
- **Test Methods**: 20
- **Method Names**: testAddActivity, testAddScheduledActivity, testAdvanceStage, testAssignLead, testConvertLead, testCreateLead, testCreateLeadWithAutoAssignment, testCreateLeadWithDuplicate, testDeleteClosedLeadThrowsException, testDeleteLead, testLeadNotFound, testMarkAsLost, testRecordEmailInteraction, testRecordFormSubmission, testRecordWebVisit, testRecycleLead, testRegressStage, testUpdateClosedLeadThrowsException, testUpdateLead, testUpdateScore

### LeadTest
- **Path**: `src/test/java/com/gogidix/sales/leadmanagement/domain/model/LeadTest.java`
- **Test Methods**: 44
- **Method Names**: testAddActivity, testAddDuplicateTag, testAddDuplicateTeamMember, testAddScorePoints, testAddScorePointsClampsToMaximum, testAddScorePointsClampsToMinimum, testAddTag, testAddTeamMember, testAdvanceStage, testAdvanceStageFromConvertedThrowsException, testAdvanceStageMultipleTimes, testAdvanceStageToConverted, testAssignTo, testCalculateBantScore, testClearDomainEvents, testCreateLead, testDetermineQualityFromScore, testDomainEventsOnCreate, testEnrich, testGetFullName, testGetFullNameWithNullFirstName, testGetFullNameWithNullLastName, testIsClosedForActiveLead, testIsClosedForConvertedLead, testIsClosedForLostLead, testLeadStageFromOrder, testLeadStagesProbability, testMarkAsConverted, testMarkAsConvertedTwiceThrowsException, testMarkAsDuplicate, testMarkAsLost, testMarkAsLostWhenConvertedThrowsException, testRecordEmailInteractionClicked, testRecordEmailInteractionOpened, testRecordFormSubmission, testRecordWebVisit, testRecycle, testRegressStage, testRegressStageWithInvalidTarget, testRemoveTag, testRemoveTeamMember, testUpdateScore, testUpdateScoreInvalidHigh, testUpdateScoreInvalidLow

### LeadCommandServiceTest
- **Path**: `src/test/java/com/gogidix/sales/leadmanagement/LeadCommandServiceTest.java`
- **Test Methods**: 10
- **Method Names**: testAdvanceStage, testAssignLead, testConvertLead, testCreateDuplicateLead, testCreateLead, testDeleteLead, testMarkAsLost, testUpdateLead, testUpdateNonExistentLead, testUpdateScore

### LeadDomainTest
- **Path**: `src/test/java/com/gogidix/sales/leadmanagement/LeadDomainTest.java`
- **Test Methods**: 20
- **Method Names**: testAddScorePoints, testAdvanceStage, testAssignLead, testCannotAdvanceConvertedLead, testCapScoreAt100, testCreateLead, testGetFullName, testIsActive, testIsClosed, testMarkAsConverted, testMarkAsDuplicate, testMarkAsLost, testRecordEmailInteraction, testRecordFormSubmission, testRecordWebVisit, testRecycleLead, testRejectInvalidScore, testTagManagement, testTeamMemberManagement, testUpdateScore

### LeadManagementServiceApplicationTest
- **Path**: `src/test/java/com/gogidix/sales/leadmanagement/LeadManagementServiceApplicationTest.java`
- **Test Methods**: 1

## Source Classes (Significant)

- `LeadCommandService` - **Tested**: Yes
- `LeadQueryService` - **Tested**: Yes
- `LeadAssignedEvent` - **Tested**: No
- `LeadConvertedEvent` - **Tested**: No
- `LeadCreatedEvent` - **Tested**: No
- `LeadLostEvent` - **Tested**: No
- `LeadQualifiedEvent` - **Tested**: No
- `LeadStageChangedEvent` - **Tested**: No
- `BaseEntity` - **Tested**: No
- `Lead` - **Tested**: Yes
- `LeadActivity` - **Tested**: No
- `LeadCommand` - **Tested**: No
- `EventPublisher` - **Tested**: No
- `LeadAssigner` - **Tested**: No
- `LeadDuplicateDetector` - **Tested**: No
- `LeadActivityRepository` - **Tested**: No
- `LeadRepository` - **Tested**: No
- `KafkaEventPublisher` - **Tested**: No
- `KafkaLeadDuplicateDetector` - **Tested**: No
- `RoundRobinLeadAssigner` - **Tested**: No
- `MongoLeadActivityRepository` - **Tested**: No
- `MongoLeadRepository` - **Tested**: No
- `SecurityConfig` - **Tested**: No
- `GlobalExceptionHandler` - **Tested**: No
- `LeadController` - **Tested**: No
- `LeadManagementServiceApplication` - **Tested**: Yes
- `BaseEntity` - **Tested**: No
- `RequestContext` - **Tested**: No
- `RequestContextHolder` - **Tested**: No
