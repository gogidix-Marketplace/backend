package com.gogidix.finance.consolidation.infrastructure.messaging;

import com.gogidix.finance.consolidation.domain.event.*;
import com.gogidix.finance.consolidation.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishConsolidatedBalanceCreated(ConsolidatedBalanceCreatedEvent event) {
        log.info("ConsolidatedBalance created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishConsolidatedBalanceUpdated(ConsolidatedBalanceUpdatedEvent event) {
        log.info("ConsolidatedBalance updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishConsolidatedBalanceDeleted(ConsolidatedBalanceDeletedEvent event) {
        log.info("ConsolidatedBalance deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishConsolidationJobCreated(ConsolidationJobCreatedEvent event) {
        log.info("ConsolidationJob created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishConsolidationJobUpdated(ConsolidationJobUpdatedEvent event) {
        log.info("ConsolidationJob updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishConsolidationJobDeleted(ConsolidationJobDeletedEvent event) {
        log.info("ConsolidationJob deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishConsolidationReportCreated(ConsolidationReportCreatedEvent event) {
        log.info("ConsolidationReport created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishConsolidationReportUpdated(ConsolidationReportUpdatedEvent event) {
        log.info("ConsolidationReport updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishConsolidationReportDeleted(ConsolidationReportDeletedEvent event) {
        log.info("ConsolidationReport deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishConsolidationRuleCreated(ConsolidationRuleCreatedEvent event) {
        log.info("ConsolidationRule created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishConsolidationRuleUpdated(ConsolidationRuleUpdatedEvent event) {
        log.info("ConsolidationRule updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishConsolidationRuleDeleted(ConsolidationRuleDeletedEvent event) {
        log.info("ConsolidationRule deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
