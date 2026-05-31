package com.gogidix.hr.payroll.domain.port.out;

import com.gogidix.hr.payroll.domain.event.PayrollCreatedEvent;
import com.gogidix.hr.payroll.domain.event.PayrollApprovedEvent;
import com.gogidix.hr.payroll.domain.event.PayrollProcessedEvent;
import com.gogidix.hr.payroll.domain.event.PayrollPaidEvent;
import com.gogidix.hr.payroll.domain.event.PayslipGeneratedEvent;

/**
 * Event Publisher Port
 * Interface for publishing domain events to message brokers
 */
public interface EventPublisher {

    void publishPayrollCreated(PayrollCreatedEvent event);

    void publishPayrollApproved(PayrollApprovedEvent event);

    void publishPayrollProcessed(PayrollProcessedEvent event);

    void publishPayrollPaid(PayrollPaidEvent event);

    void publishPayslipGenerated(PayslipGeneratedEvent event);

    boolean isReady();
}
