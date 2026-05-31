package com.gogidix.globalbusinessmanagement.datavalidation.infrastructure.messaging;

import com.gogidix.globalbusinessmanagement.datavalidation.domain.event.*;
import com.gogidix.globalbusinessmanagement.datavalidation.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishDataQualityReportCreated(DataQualityReportCreatedEvent event) {
        log.info("DataQualityReport created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDataQualityReportUpdated(DataQualityReportUpdatedEvent event) {
        log.info("DataQualityReport updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDataQualityReportDeleted(DataQualityReportDeletedEvent event) {
        log.info("DataQualityReport deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishValidationResultCreated(ValidationResultCreatedEvent event) {
        log.info("ValidationResult created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishValidationResultUpdated(ValidationResultUpdatedEvent event) {
        log.info("ValidationResult updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishValidationResultDeleted(ValidationResultDeletedEvent event) {
        log.info("ValidationResult deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishValidationRuleCreated(ValidationRuleCreatedEvent event) {
        log.info("ValidationRule created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishValidationRuleUpdated(ValidationRuleUpdatedEvent event) {
        log.info("ValidationRule updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishValidationRuleDeleted(ValidationRuleDeletedEvent event) {
        log.info("ValidationRule deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
