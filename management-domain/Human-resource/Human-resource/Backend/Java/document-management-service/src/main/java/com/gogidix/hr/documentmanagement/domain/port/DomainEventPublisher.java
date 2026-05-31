package com.gogidix.hr.documentmanagement.domain.port;

import com.gogidix.hr.documentmanagement.domain.event.*;

public interface DomainEventPublisher {
    void publishAccessActionCreated(AccessActionCreatedEvent event);
    void publishAccessActionUpdated(AccessActionUpdatedEvent event);
    void publishAccessActionDeleted(AccessActionDeletedEvent event);
    void publishDocumentAccessLogCreated(DocumentAccessLogCreatedEvent event);
    void publishDocumentAccessLogUpdated(DocumentAccessLogUpdatedEvent event);
    void publishDocumentAccessLogDeleted(DocumentAccessLogDeletedEvent event);
    void publishDocumentCategoryCreated(DocumentCategoryCreatedEvent event);
    void publishDocumentCategoryUpdated(DocumentCategoryUpdatedEvent event);
    void publishDocumentCategoryDeleted(DocumentCategoryDeletedEvent event);
    void publishDocumentRetentionCreated(DocumentRetentionCreatedEvent event);
    void publishDocumentRetentionUpdated(DocumentRetentionUpdatedEvent event);
    void publishDocumentRetentionDeleted(DocumentRetentionDeletedEvent event);
    void publishDocumentStatusCreated(DocumentStatusCreatedEvent event);
    void publishDocumentStatusUpdated(DocumentStatusUpdatedEvent event);
    void publishDocumentStatusDeleted(DocumentStatusDeletedEvent event);
    void publishDocumentTemplateCreated(DocumentTemplateCreatedEvent event);
    void publishDocumentTemplateUpdated(DocumentTemplateUpdatedEvent event);
    void publishDocumentTemplateDeleted(DocumentTemplateDeletedEvent event);
    void publishDocumentTypeCreated(DocumentTypeCreatedEvent event);
    void publishDocumentTypeUpdated(DocumentTypeUpdatedEvent event);
    void publishDocumentTypeDeleted(DocumentTypeDeletedEvent event);
    void publishHRDocumentCreated(HRDocumentCreatedEvent event);
    void publishHRDocumentUpdated(HRDocumentUpdatedEvent event);
    void publishHRDocumentDeleted(HRDocumentDeletedEvent event);
    void publishRetentionActionCreated(RetentionActionCreatedEvent event);
    void publishRetentionActionUpdated(RetentionActionUpdatedEvent event);
    void publishRetentionActionDeleted(RetentionActionDeletedEvent event);
    void publishStorageProviderCreated(StorageProviderCreatedEvent event);
    void publishStorageProviderUpdated(StorageProviderUpdatedEvent event);
    void publishStorageProviderDeleted(StorageProviderDeletedEvent event);
}
