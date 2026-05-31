package com.gogidix.hr.employeeselfservice.infrastructure.messaging;

import com.gogidix.hr.employeeselfservice.domain.event.*;
import com.gogidix.hr.employeeselfservice.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishBankDetailsCreated(BankDetailsCreatedEvent event) {
        log.info("BankDetails created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishBankDetailsUpdated(BankDetailsUpdatedEvent event) {
        log.info("BankDetails updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishBankDetailsDeleted(BankDetailsDeletedEvent event) {
        log.info("BankDetails deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishEmployeeProfileCreated(EmployeeProfileCreatedEvent event) {
        log.info("EmployeeProfile created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishEmployeeProfileUpdated(EmployeeProfileUpdatedEvent event) {
        log.info("EmployeeProfile updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishEmployeeProfileDeleted(EmployeeProfileDeletedEvent event) {
        log.info("EmployeeProfile deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishQuickActionCreated(QuickActionCreatedEvent event) {
        log.info("QuickAction created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishQuickActionUpdated(QuickActionUpdatedEvent event) {
        log.info("QuickAction updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishQuickActionDeleted(QuickActionDeletedEvent event) {
        log.info("QuickAction deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSelfServiceRequestCreated(SelfServiceRequestCreatedEvent event) {
        log.info("SelfServiceRequest created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSelfServiceRequestUpdated(SelfServiceRequestUpdatedEvent event) {
        log.info("SelfServiceRequest updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSelfServiceRequestDeleted(SelfServiceRequestDeletedEvent event) {
        log.info("SelfServiceRequest deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
