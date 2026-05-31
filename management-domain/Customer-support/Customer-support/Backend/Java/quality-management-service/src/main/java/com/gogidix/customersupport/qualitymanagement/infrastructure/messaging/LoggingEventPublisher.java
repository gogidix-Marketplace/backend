package com.gogidix.customersupport.qualitymanagement.infrastructure.messaging;

import com.gogidix.customersupport.qualitymanagement.domain.event.*;
import com.gogidix.customersupport.qualitymanagement.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishAgentQualityProfileCreated(AgentQualityProfileCreatedEvent event) {
        log.info("AgentQualityProfile created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAgentQualityProfileUpdated(AgentQualityProfileUpdatedEvent event) {
        log.info("AgentQualityProfile updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAgentQualityProfileDeleted(AgentQualityProfileDeletedEvent event) {
        log.info("AgentQualityProfile deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCalibrationSessionCreated(CalibrationSessionCreatedEvent event) {
        log.info("CalibrationSession created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCalibrationSessionUpdated(CalibrationSessionUpdatedEvent event) {
        log.info("CalibrationSession updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCalibrationSessionDeleted(CalibrationSessionDeletedEvent event) {
        log.info("CalibrationSession deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishQaReviewCreated(QaReviewCreatedEvent event) {
        log.info("QaReview created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishQaReviewUpdated(QaReviewUpdatedEvent event) {
        log.info("QaReview updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishQaReviewDeleted(QaReviewDeletedEvent event) {
        log.info("QaReview deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishScorecardTemplateCreated(ScorecardTemplateCreatedEvent event) {
        log.info("ScorecardTemplate created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishScorecardTemplateUpdated(ScorecardTemplateUpdatedEvent event) {
        log.info("ScorecardTemplate updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishScorecardTemplateDeleted(ScorecardTemplateDeletedEvent event) {
        log.info("ScorecardTemplate deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
