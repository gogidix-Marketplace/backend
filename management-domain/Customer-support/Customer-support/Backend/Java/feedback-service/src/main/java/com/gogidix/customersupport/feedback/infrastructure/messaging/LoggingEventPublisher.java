package com.gogidix.customersupport.feedback.infrastructure.messaging;

import com.gogidix.customersupport.feedback.domain.event.*;
import com.gogidix.customersupport.feedback.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishCSATSurveyCreated(CSATSurveyCreatedEvent event) {
        log.info("CSATSurvey created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCSATSurveyUpdated(CSATSurveyUpdatedEvent event) {
        log.info("CSATSurvey updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCSATSurveyDeleted(CSATSurveyDeletedEvent event) {
        log.info("CSATSurvey deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishFeedbackCreated(FeedbackCreatedEvent event) {
        log.info("Feedback created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishFeedbackUpdated(FeedbackUpdatedEvent event) {
        log.info("Feedback updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishFeedbackDeleted(FeedbackDeletedEvent event) {
        log.info("Feedback deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishNPSMetricCreated(NPSMetricCreatedEvent event) {
        log.info("NPSMetric created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishNPSMetricUpdated(NPSMetricUpdatedEvent event) {
        log.info("NPSMetric updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishNPSMetricDeleted(NPSMetricDeletedEvent event) {
        log.info("NPSMetric deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
