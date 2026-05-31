package com.gogidix.hr.documentmanagement.infrastructure.messaging;

import com.gogidix.hr.documentmanagement.domain.event.*;
import com.gogidix.hr.documentmanagement.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishAccessActionCreated(AccessActionCreatedEvent event) {
        log.info("AccessAction created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAccessActionUpdated(AccessActionUpdatedEvent event) {
        log.info("AccessAction updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAccessActionDeleted(AccessActionDeletedEvent event) {
        log.info("AccessAction deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDocumentAccessLogCreated(DocumentAccessLogCreatedEvent event) {
        log.info("DocumentAccessLog created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDocumentAccessLogUpdated(DocumentAccessLogUpdatedEvent event) {
        log.info("DocumentAccessLog updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDocumentAccessLogDeleted(DocumentAccessLogDeletedEvent event) {
        log.info("DocumentAccessLog deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDocumentCategoryCreated(DocumentCategoryCreatedEvent event) {
        log.info("DocumentCategory created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDocumentCategoryUpdated(DocumentCategoryUpdatedEvent event) {
        log.info("DocumentCategory updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDocumentCategoryDeleted(DocumentCategoryDeletedEvent event) {
        log.info("DocumentCategory deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDocumentRetentionCreated(DocumentRetentionCreatedEvent event) {
        log.info("DocumentRetention created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDocumentRetentionUpdated(DocumentRetentionUpdatedEvent event) {
        log.info("DocumentRetention updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDocumentRetentionDeleted(DocumentRetentionDeletedEvent event) {
        log.info("DocumentRetention deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDocumentStatusCreated(DocumentStatusCreatedEvent event) {
        log.info("DocumentStatus created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDocumentStatusUpdated(DocumentStatusUpdatedEvent event) {
        log.info("DocumentStatus updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDocumentStatusDeleted(DocumentStatusDeletedEvent event) {
        log.info("DocumentStatus deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDocumentTemplateCreated(DocumentTemplateCreatedEvent event) {
        log.info("DocumentTemplate created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDocumentTemplateUpdated(DocumentTemplateUpdatedEvent event) {
        log.info("DocumentTemplate updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDocumentTemplateDeleted(DocumentTemplateDeletedEvent event) {
        log.info("DocumentTemplate deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDocumentTypeCreated(DocumentTypeCreatedEvent event) {
        log.info("DocumentType created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDocumentTypeUpdated(DocumentTypeUpdatedEvent event) {
        log.info("DocumentType updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDocumentTypeDeleted(DocumentTypeDeletedEvent event) {
        log.info("DocumentType deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishHRDocumentCreated(HRDocumentCreatedEvent event) {
        log.info("HRDocument created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishHRDocumentUpdated(HRDocumentUpdatedEvent event) {
        log.info("HRDocument updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishHRDocumentDeleted(HRDocumentDeletedEvent event) {
        log.info("HRDocument deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRetentionActionCreated(RetentionActionCreatedEvent event) {
        log.info("RetentionAction created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRetentionActionUpdated(RetentionActionUpdatedEvent event) {
        log.info("RetentionAction updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRetentionActionDeleted(RetentionActionDeletedEvent event) {
        log.info("RetentionAction deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishStorageProviderCreated(StorageProviderCreatedEvent event) {
        log.info("StorageProvider created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishStorageProviderUpdated(StorageProviderUpdatedEvent event) {
        log.info("StorageProvider updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishStorageProviderDeleted(StorageProviderDeletedEvent event) {
        log.info("StorageProvider deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
