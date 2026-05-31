package com.gogidix.sysadmin.compliance.infrastructure.messaging;

import com.gogidix.sysadmin.compliance.domain.event.*;
import com.gogidix.sysadmin.compliance.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishComplianceReportCreated(ComplianceReportCreatedEvent event) {
        log.info("ComplianceReport created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishComplianceReportUpdated(ComplianceReportUpdatedEvent event) {
        log.info("ComplianceReport updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishComplianceReportDeleted(ComplianceReportDeletedEvent event) {
        log.info("ComplianceReport deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
