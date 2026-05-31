package com.gogidix.monitoring.alertmanagementservice.infrastructure.messaging;

import com.gogidix.monitoring.alertmanagementservice.domain.event.*;
import com.gogidix.monitoring.alertmanagementservice.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishAlertCreated(AlertCreatedEvent event) {
        log.info("Alert created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAlertUpdated(AlertUpdatedEvent event) {
        log.info("Alert updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAlertDeleted(AlertDeletedEvent event) {
        log.info("Alert deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAlertHistoryCreated(AlertHistoryCreatedEvent event) {
        log.info("AlertHistory created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAlertHistoryUpdated(AlertHistoryUpdatedEvent event) {
        log.info("AlertHistory updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAlertHistoryDeleted(AlertHistoryDeletedEvent event) {
        log.info("AlertHistory deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAlertRuleCreated(AlertRuleCreatedEvent event) {
        log.info("AlertRule created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAlertRuleUpdated(AlertRuleUpdatedEvent event) {
        log.info("AlertRule updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAlertRuleDeleted(AlertRuleDeletedEvent event) {
        log.info("AlertRule deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
