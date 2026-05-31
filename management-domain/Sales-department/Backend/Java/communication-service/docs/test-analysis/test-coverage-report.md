# Test Coverage Report - communication-service

## Overview
- **Service**: communication-service
- **Sub-Domain**: Sales-department
- **Analysis Date**: 2026-03-06T10:24:22Z
- **Total Source Files**: 54
- **Total Test Files**: 8
- **Source Classes (significant)**: 38
- **Test Classes**: 8
- **Test Methods**: 122
- **Test Coverage**: 21%

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

### ConversationCommandServiceTest
- **Path**: `src/test/java/com/gogidix/sales/communication/application/service/ConversationCommandServiceTest.java`
- **Test Methods**: 17
- **Method Names**: testAddDuplicateTag, testAddParticipant, testAddParticipantWhenMaxParticipantsReached, testAddTag, testArchiveConversation, testAssignConversation, testConversationNotFound, testCreateConversation, testCreateConversationWithRelatedEntity, testMarkAsRead, testRemoveParticipant, testRemoveParticipantWhenOwner, testResolveConversation, testSetSlaDeadline, testUpdateConversation, testUpdateConversationAssignment, testUpdateConversationWithInvalidPriority

### MessageCommandServiceTest
- **Path**: `src/test/java/com/gogidix/sales/communication/application/service/MessageCommandServiceTest.java`
- **Test Methods**: 12
- **Method Names**: testAddAttachment, testCreateMessage, testCreateMessageWithNoRecipients, testCreateMessageWithTooManyRecipients, testDeleteMessage, testDeleteNonDraftMessage, testMarkAsDelivered, testMarkAsFailed, testMarkAsRead, testScheduleMessage, testSendMessage, testSendMessageNotFound

### MessageQueryServiceTest
- **Path**: `src/test/java/com/gogidix/sales/communication/application/service/MessageQueryServiceTest.java`
- **Test Methods**: 21
- **Method Names**: testCountByStatus, testCountUnread, testGetAllForTenant, testGetByChannel, testGetByChannelInvalid, testGetByConversationId, testGetByConversationIdDescending, testGetByConversationIdEmptyResult, testGetById, testGetByIdNotFound, testGetByRelatedEntity, testGetBySenderId, testGetByStatus, testGetByStatusInvalid, testGetByTemplateId, testGetThread, testGetUnreadMessages, testPaginationSecondPage, testSearchMessagesCaseInsensitive, testSearchMessagesWithDateRange, testSearchMessagesWithoutDateRange

### ConversationTest
- **Path**: `src/test/java/com/gogidix/sales/communication/domain/model/ConversationTest.java`
- **Test Methods**: 14
- **Method Names**: testAddAdmin, testAddParticipant, testAddTag, testArchive, testAssignTo, testCheckSlaBreach, testCreateConversation, testIncrementUnreadCount, testIsParticipant, testMarkAsRead, testRemoveParticipant, testResolve, testSetSlaDeadline, testUpdateLastMessage

### MessageTest
- **Path**: `src/test/java/com/gogidix/sales/communication/domain/model/MessageTest.java`
- **Test Methods**: 11
- **Method Names**: testAddAttachment, testCannotSendNonDraftMessage, testCreateMessage, testMarkAsFailed, testMarkAsRead, testMarkAsSent, testMarkRecipientAsRead, testScheduleMessage, testSendMessage, testSetPriority, testSetPriorityInvalid

### EmailMessageSenderTest
- **Path**: `src/test/java/com/gogidix/sales/communication/infrastructure/messaging/EmailMessageSenderTest.java`
- **Test Methods**: 12
- **Method Names**: testCanSendWithEmailChannel, testCanSendWithNonEmailChannel, testIsReady, testSendEmailFailureUpdatesReadyStatus, testSendEmailGeneratesUniqueExternalId, testSendEmailSuccessfully, testSendEmailWithEmptyContent, testSendEmailWithEmptySubject, testSendEmailWithMultipleRecipients, testSendEmailWithNullContent, testSendEmailWithRecipientWithoutEmail, testSendNonEmailMessageThrowsException

### MongoMessageRepositoryTest
- **Path**: `src/test/java/com/gogidix/sales/communication/infrastructure/persistence/mongo/MongoMessageRepositoryTest.java`
- **Test Methods**: 21
- **Method Names**: testCountByTenantIdAndStatus, testCountUnreadByRecipientId, testDeleteById, testDeleteByMessageIdAndTenantId, testFindByConversationIdAndTenantId, testFindById, testFindByIdNotFound, testFindByIsReadFalseAndRecipientIdsContaining, testFindByMessageIdAndTenantId, testFindByMessageIdAndTenantIdNotFound, testFindByRelatedEntityTypeAndRelatedEntityIdAndTenantId, testFindByScheduledAtBeforeAndStatus, testFindBySenderIdAndTenantId, testFindByTemplateIdAndTenantId, testFindByTenantId, testFindByTenantIdAndChannel, testFindByTenantIdAndCreatedAtBetween, testFindByTenantIdAndParentMessageId, testFindByTenantIdAndStatus, testSave, testSearchByContent

### MessageControllerTest
- **Path**: `src/test/java/com/gogidix/sales/communication/interfaces/rest/MessageControllerTest.java`
- **Test Methods**: 14
- **Method Names**: testAddAttachment, testCreateMessage, testDeleteMessage, testGetAllMessages, testGetMessageById, testGetMessagesByConversation, testGetMessagesBySender, testGetMessagesByStatus, testGetMessageThread, testGetUnreadMessages, testMarkAsRead, testScheduleMessage, testSearchMessages, testSendMessage

## Source Classes (Significant)

- `ConversationCommandService` - **Tested**: Yes
- `ConversationQueryService` - **Tested**: No
- `MessageCommandService` - **Tested**: Yes
- `MessageQueryService` - **Tested**: Yes
- `CommunicationServiceApplication` - **Tested**: No
- `ConversationCreatedEvent` - **Tested**: No
- `ConversationUpdatedEvent` - **Tested**: No
- `MessageReadEvent` - **Tested**: No
- `MessageSentEvent` - **Tested**: No
- `BaseEntity` - **Tested**: No
- `CommunicationChannel` - **Tested**: No
- `Conversation` - **Tested**: Yes
- `Message` - **Tested**: Yes
- `MessageTemplate` - **Tested**: No
- `ConversationCommand` - **Tested**: No
- `MessageCommand` - **Tested**: No
- `MessageQuery` - **Tested**: No
- `EventPublisher` - **Tested**: No
- `MessageSender` - **Tested**: No
- `TemplateRenderer` - **Tested**: No
- `CommunicationChannelRepository` - **Tested**: No
- `ConversationRepository` - **Tested**: No
- `MessageRepository` - **Tested**: No
- `MessageTemplateRepository` - **Tested**: No
- `EmailMessageSender` - **Tested**: Yes
- `KafkaEventPublisher` - **Tested**: No
- `MongoCommunicationChannelRepository` - **Tested**: No
- `MongoConversationRepository` - **Tested**: No
- `MongoMessageRepository` - **Tested**: Yes
- `MongoMessageTemplateRepository` - **Tested**: No
- `SecurityConfig` - **Tested**: No
- `ConversationController` - **Tested**: No
- `GlobalExceptionHandler` - **Tested**: No
- `MessageController` - **Tested**: Yes
- `AuditableEntity` - **Tested**: No
- `BaseEntity` - **Tested**: No
- `RequestContext` - **Tested**: No
- `RequestContextHolder` - **Tested**: No
