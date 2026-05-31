# Test Coverage Report - crm-service

## Overview
- **Service**: crm-service
- **Sub-Domain**: Sales-department
- **Analysis Date**: 2026-03-06T10:24:22Z
- **Total Source Files**: 47
- **Total Test Files**: 4
- **Source Classes (significant)**: 37
- **Test Classes**: 4
- **Test Methods**: 36
- **Test Coverage**: 10%

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

### CustomerCommandServiceTest
- **Path**: `src/test/java/com/gogidix/sales/crm/application/service/CustomerCommandServiceTest.java`
- **Test Methods**: 3

### CustomerQueryServiceTest
- **Path**: `src/test/java/com/gogidix/sales/crm/application/service/CustomerQueryServiceTest.java`
- **Test Methods**: 5

### CustomerTest
- **Path**: `src/test/java/com/gogidix/sales/crm/domain/model/CustomerTest.java`
- **Test Methods**: 12
- **Method Names**: testAddTag, testAdvanceLifecycleStage, testAssignOwner, testCannotRevertLifecycleStage, testCreateCustomer, testMarkAsChurned, testReactivateCustomer, testRemoveTag, testSetFollowUpDate, testSetParentAccount, testUpdateDealStats, testUpdateLastContactDate

### InteractionTest
- **Path**: `src/test/java/com/gogidix/sales/crm/domain/model/InteractionTest.java`
- **Test Methods**: 16
- **Method Names**: testAddAttachment, testAddParticipant, testAssignTo, testAssociateWithDeal, testCancelInteraction, testCannotCompleteAlreadyCompletedInteraction, testCompleteInteraction, testCreateInteraction, testIsOverdue, testIsUpcoming, testMarkAsHighPriority, testRemoveFollowUp, testRemoveHighPriority, testRescheduleInteraction, testSetFollowUp, testSetNextStep

## Source Classes (Significant)

- `ContactCommandService` - **Tested**: No
- `CustomerCommandService` - **Tested**: Yes
- `CustomerQueryService` - **Tested**: Yes
- `InteractionCommandService` - **Tested**: No
- `InteractionQueryService` - **Tested**: No
- `CrmServiceApplication` - **Tested**: No
- `ContactCreatedEvent` - **Tested**: No
- `CustomerCreatedEvent` - **Tested**: No
- `CustomerUpdatedEvent` - **Tested**: No
- `InteractionLoggedEvent` - **Tested**: No
- `Account` - **Tested**: No
- `BaseEntity` - **Tested**: No
- `Contact` - **Tested**: No
- `Customer` - **Tested**: Yes
- `Interaction` - **Tested**: Yes
- `ContactCommand` - **Tested**: No
- `CustomerCommand` - **Tested**: No
- `CustomerQuery` - **Tested**: No
- `InteractionCommand` - **Tested**: No
- `EventPublisher` - **Tested**: No
- `AccountRepository` - **Tested**: No
- `ContactRepository` - **Tested**: No
- `CustomerRepository` - **Tested**: No
- `InteractionRepository` - **Tested**: No
- `KafkaEventPublisher` - **Tested**: No
- `MongoAccountRepository` - **Tested**: No
- `MongoContactRepository` - **Tested**: No
- `MongoCustomerRepository` - **Tested**: No
- `MongoInteractionRepository` - **Tested**: No
- `SecurityConfig` - **Tested**: No
- `ContactController` - **Tested**: No
- `CustomerController` - **Tested**: No
- `GlobalExceptionHandler` - **Tested**: No
- `InteractionController` - **Tested**: No
- `BaseEntity` - **Tested**: No
- `RequestContext` - **Tested**: No
- `RequestContextHolder` - **Tested**: No
