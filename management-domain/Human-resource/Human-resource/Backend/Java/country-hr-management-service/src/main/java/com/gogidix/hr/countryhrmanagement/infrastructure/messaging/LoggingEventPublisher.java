package com.gogidix.hr.countryhrmanagement.infrastructure.messaging;

import com.gogidix.hr.countryhrmanagement.domain.event.*;
import com.gogidix.hr.countryhrmanagement.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishCountryHRConfigCreated(CountryHRConfigCreatedEvent event) {
        log.info("CountryHRConfig created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCountryHRConfigUpdated(CountryHRConfigUpdatedEvent event) {
        log.info("CountryHRConfig updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCountryHRConfigDeleted(CountryHRConfigDeletedEvent event) {
        log.info("CountryHRConfig deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishLaborLawCreated(LaborLawCreatedEvent event) {
        log.info("LaborLaw created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishLaborLawUpdated(LaborLawUpdatedEvent event) {
        log.info("LaborLaw updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishLaborLawDeleted(LaborLawDeletedEvent event) {
        log.info("LaborLaw deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPayrollConfigCreated(PayrollConfigCreatedEvent event) {
        log.info("PayrollConfig created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPayrollConfigUpdated(PayrollConfigUpdatedEvent event) {
        log.info("PayrollConfig updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPayrollConfigDeleted(PayrollConfigDeletedEvent event) {
        log.info("PayrollConfig deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishTaxConfigurationCreated(TaxConfigurationCreatedEvent event) {
        log.info("TaxConfiguration created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishTaxConfigurationUpdated(TaxConfigurationUpdatedEvent event) {
        log.info("TaxConfiguration updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishTaxConfigurationDeleted(TaxConfigurationDeletedEvent event) {
        log.info("TaxConfiguration deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishWorkingHoursConfigCreated(WorkingHoursConfigCreatedEvent event) {
        log.info("WorkingHoursConfig created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishWorkingHoursConfigUpdated(WorkingHoursConfigUpdatedEvent event) {
        log.info("WorkingHoursConfig updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishWorkingHoursConfigDeleted(WorkingHoursConfigDeletedEvent event) {
        log.info("WorkingHoursConfig deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
