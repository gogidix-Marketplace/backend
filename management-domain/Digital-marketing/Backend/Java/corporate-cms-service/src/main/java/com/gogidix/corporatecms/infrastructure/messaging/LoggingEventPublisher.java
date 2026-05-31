package com.gogidix.corporatecms.infrastructure.messaging;

import com.gogidix.corporatecms.domain.event.*;
import com.gogidix.corporatecms.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishContentCreated(ContentCreatedEvent event) {
        log.info("Content created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishContentUpdated(ContentUpdatedEvent event) {
        log.info("Content updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishContentDeleted(ContentDeletedEvent event) {
        log.info("Content deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishJobCreated(JobCreatedEvent event) {
        log.info("Job created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishJobUpdated(JobUpdatedEvent event) {
        log.info("Job updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishJobDeleted(JobDeletedEvent event) {
        log.info("Job deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
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
    public void publishMediaCreated(MediaCreatedEvent event) {
        log.info("Media created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMediaUpdated(MediaUpdatedEvent event) {
        log.info("Media updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMediaDeleted(MediaDeletedEvent event) {
        log.info("Media deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishProductCreated(ProductCreatedEvent event) {
        log.info("Product created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishProductUpdated(ProductUpdatedEvent event) {
        log.info("Product updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishProductDeleted(ProductDeletedEvent event) {
        log.info("Product deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishUserCreated(UserCreatedEvent event) {
        log.info("User created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishUserUpdated(UserUpdatedEvent event) {
        log.info("User updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishUserDeleted(UserDeletedEvent event) {
        log.info("User deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishWorkflowCreated(WorkflowCreatedEvent event) {
        log.info("Workflow created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishWorkflowUpdated(WorkflowUpdatedEvent event) {
        log.info("Workflow updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishWorkflowDeleted(WorkflowDeletedEvent event) {
        log.info("Workflow deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
