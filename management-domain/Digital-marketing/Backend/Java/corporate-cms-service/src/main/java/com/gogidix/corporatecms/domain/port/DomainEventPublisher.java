package com.gogidix.corporatecms.domain.port;

import com.gogidix.corporatecms.domain.event.*;

public interface DomainEventPublisher {
    void publishContentCreated(ContentCreatedEvent event);
    void publishContentUpdated(ContentUpdatedEvent event);
    void publishContentDeleted(ContentDeletedEvent event);
    void publishJobCreated(JobCreatedEvent event);
    void publishJobUpdated(JobUpdatedEvent event);
    void publishJobDeleted(JobDeletedEvent event);
    void publishLeadCreated(LeadCreatedEvent event);
    void publishLeadUpdated(LeadUpdatedEvent event);
    void publishLeadDeleted(LeadDeletedEvent event);
    void publishMediaCreated(MediaCreatedEvent event);
    void publishMediaUpdated(MediaUpdatedEvent event);
    void publishMediaDeleted(MediaDeletedEvent event);
    void publishProductCreated(ProductCreatedEvent event);
    void publishProductUpdated(ProductUpdatedEvent event);
    void publishProductDeleted(ProductDeletedEvent event);
    void publishUserCreated(UserCreatedEvent event);
    void publishUserUpdated(UserUpdatedEvent event);
    void publishUserDeleted(UserDeletedEvent event);
    void publishWorkflowCreated(WorkflowCreatedEvent event);
    void publishWorkflowUpdated(WorkflowUpdatedEvent event);
    void publishWorkflowDeleted(WorkflowDeletedEvent event);
}
