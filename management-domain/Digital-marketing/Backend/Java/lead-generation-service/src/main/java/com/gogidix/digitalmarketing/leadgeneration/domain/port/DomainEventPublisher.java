package com.gogidix.digitalmarketing.leadgeneration.domain.port;

import com.gogidix.digitalmarketing.leadgeneration.domain.event.*;

public interface DomainEventPublisher {
    void publishLeadCreated(LeadCreatedEvent event);
    void publishLeadUpdated(LeadUpdatedEvent event);
    void publishLeadDeleted(LeadDeletedEvent event);
    void publishLeadActivityCreated(LeadActivityCreatedEvent event);
    void publishLeadActivityUpdated(LeadActivityUpdatedEvent event);
    void publishLeadActivityDeleted(LeadActivityDeletedEvent event);
    void publishLeadAssignmentCreated(LeadAssignmentCreatedEvent event);
    void publishLeadAssignmentUpdated(LeadAssignmentUpdatedEvent event);
    void publishLeadAssignmentDeleted(LeadAssignmentDeletedEvent event);
    void publishLeadHandoffCreated(LeadHandoffCreatedEvent event);
    void publishLeadHandoffUpdated(LeadHandoffUpdatedEvent event);
    void publishLeadHandoffDeleted(LeadHandoffDeletedEvent event);
    void publishLeadQualificationCreated(LeadQualificationCreatedEvent event);
    void publishLeadQualificationUpdated(LeadQualificationUpdatedEvent event);
    void publishLeadQualificationDeleted(LeadQualificationDeletedEvent event);
    void publishLeadSourceCreated(LeadSourceCreatedEvent event);
    void publishLeadSourceUpdated(LeadSourceUpdatedEvent event);
    void publishLeadSourceDeleted(LeadSourceDeletedEvent event);
}
