package com.gogidix.digitalmarketing.leadgeneration.infrastructure.messaging;

import com.gogidix.digitalmarketing.leadgeneration.domain.event.*;
import com.gogidix.digitalmarketing.leadgeneration.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishLeadCreated(LeadCreatedEvent event) {
        log.info("Lead created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishLeadUpdated(LeadUpdatedEvent event) {
        log.info("Lead updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishLeadDeleted(LeadDeletedEvent event) {
        log.info("Lead deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishLeadActivityCreated(LeadActivityCreatedEvent event) {
        log.info("LeadActivity created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishLeadActivityUpdated(LeadActivityUpdatedEvent event) {
        log.info("LeadActivity updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishLeadActivityDeleted(LeadActivityDeletedEvent event) {
        log.info("LeadActivity deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishLeadAssignmentCreated(LeadAssignmentCreatedEvent event) {
        log.info("LeadAssignment created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishLeadAssignmentUpdated(LeadAssignmentUpdatedEvent event) {
        log.info("LeadAssignment updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishLeadAssignmentDeleted(LeadAssignmentDeletedEvent event) {
        log.info("LeadAssignment deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishLeadHandoffCreated(LeadHandoffCreatedEvent event) {
        log.info("LeadHandoff created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishLeadHandoffUpdated(LeadHandoffUpdatedEvent event) {
        log.info("LeadHandoff updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishLeadHandoffDeleted(LeadHandoffDeletedEvent event) {
        log.info("LeadHandoff deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishLeadQualificationCreated(LeadQualificationCreatedEvent event) {
        log.info("LeadQualification created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishLeadQualificationUpdated(LeadQualificationUpdatedEvent event) {
        log.info("LeadQualification updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishLeadQualificationDeleted(LeadQualificationDeletedEvent event) {
        log.info("LeadQualification deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishLeadSourceCreated(LeadSourceCreatedEvent event) {
        log.info("LeadSource created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishLeadSourceUpdated(LeadSourceUpdatedEvent event) {
        log.info("LeadSource updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishLeadSourceDeleted(LeadSourceDeletedEvent event) {
        log.info("LeadSource deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
