package com.gogidix.hr.countryhrmanagement.domain.port;

import com.gogidix.hr.countryhrmanagement.domain.event.*;

public interface DomainEventPublisher {
    void publishCountryHRConfigCreated(CountryHRConfigCreatedEvent event);
    void publishCountryHRConfigUpdated(CountryHRConfigUpdatedEvent event);
    void publishCountryHRConfigDeleted(CountryHRConfigDeletedEvent event);
    void publishLaborLawCreated(LaborLawCreatedEvent event);
    void publishLaborLawUpdated(LaborLawUpdatedEvent event);
    void publishLaborLawDeleted(LaborLawDeletedEvent event);
    void publishPayrollConfigCreated(PayrollConfigCreatedEvent event);
    void publishPayrollConfigUpdated(PayrollConfigUpdatedEvent event);
    void publishPayrollConfigDeleted(PayrollConfigDeletedEvent event);
    void publishTaxConfigurationCreated(TaxConfigurationCreatedEvent event);
    void publishTaxConfigurationUpdated(TaxConfigurationUpdatedEvent event);
    void publishTaxConfigurationDeleted(TaxConfigurationDeletedEvent event);
    void publishWorkingHoursConfigCreated(WorkingHoursConfigCreatedEvent event);
    void publishWorkingHoursConfigUpdated(WorkingHoursConfigUpdatedEvent event);
    void publishWorkingHoursConfigDeleted(WorkingHoursConfigDeletedEvent event);
}
