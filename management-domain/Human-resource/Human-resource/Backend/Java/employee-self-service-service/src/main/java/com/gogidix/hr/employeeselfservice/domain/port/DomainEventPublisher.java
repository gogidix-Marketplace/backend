package com.gogidix.hr.employeeselfservice.domain.port;

import com.gogidix.hr.employeeselfservice.domain.event.*;

public interface DomainEventPublisher {
    void publishBankDetailsCreated(BankDetailsCreatedEvent event);
    void publishBankDetailsUpdated(BankDetailsUpdatedEvent event);
    void publishBankDetailsDeleted(BankDetailsDeletedEvent event);
    void publishEmployeeProfileCreated(EmployeeProfileCreatedEvent event);
    void publishEmployeeProfileUpdated(EmployeeProfileUpdatedEvent event);
    void publishEmployeeProfileDeleted(EmployeeProfileDeletedEvent event);
    void publishQuickActionCreated(QuickActionCreatedEvent event);
    void publishQuickActionUpdated(QuickActionUpdatedEvent event);
    void publishQuickActionDeleted(QuickActionDeletedEvent event);
    void publishSelfServiceRequestCreated(SelfServiceRequestCreatedEvent event);
    void publishSelfServiceRequestUpdated(SelfServiceRequestUpdatedEvent event);
    void publishSelfServiceRequestDeleted(SelfServiceRequestDeletedEvent event);
}
