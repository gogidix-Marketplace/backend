package com.gogidix.hr.payroll.domain.port.in;

import com.gogidix.hr.payroll.application.dto.request.CreatePayrollEntryRequest;
import com.gogidix.hr.payroll.application.dto.request.UpdatePayrollEntryRequest;
import com.gogidix.hr.payroll.application.dto.response.PayrollEntryResponse;

import java.util.List;

/**
 * Input port for PayrollEntry commands
 */
public interface PayrollEntryCommandPort {

    PayrollEntryResponse createPayrollEntry(CreatePayrollEntryRequest request);

    PayrollEntryResponse updatePayrollEntry(String id, UpdatePayrollEntryRequest request);

    void deletePayrollEntry(String id);

    PayrollEntryResponse calculateEntry(String id);

    List<PayrollEntryResponse> createBulkEntries(List<CreatePayrollEntryRequest> requests);

    PayrollEntryResponse holdPayment(String id, String reason);

    PayrollEntryResponse releaseHold(String id);

    PayrollEntryResponse markAsPaid(String id);

    List<PayrollEntryResponse> getEntriesByPayroll(String payrollId);
}
