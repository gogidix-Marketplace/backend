package com.gogidix.hr.payroll.domain.port.in;

import com.gogidix.hr.payroll.application.dto.request.CreatePayrollRequest;
import com.gogidix.hr.payroll.application.dto.request.UpdatePayrollRequest;
import com.gogidix.hr.payroll.application.dto.response.PayrollResponse;

import java.util.List;

/**
 * Input port for Payroll commands
 */
public interface PayrollCommandPort {

    PayrollResponse createPayroll(CreatePayrollRequest request);

    PayrollResponse updatePayroll(String id, UpdatePayrollRequest request);

    void deletePayroll(String id);

    PayrollResponse submitForApproval(String id);

    PayrollResponse approvePayroll(String id);

    PayrollResponse rejectPayroll(String id, String reason);

    PayrollResponse processPayroll(String id);

    PayrollResponse markAsPaid(String id);

    PayrollResponse lockPayroll(String id);

    PayrollResponse unlockPayroll(String id);

    PayrollResponse cancelPayroll(String id);

    PayrollResponse addEmployeeToPayroll(String payrollId, String employeeId);

    PayrollResponse removeEmployeeFromPayroll(String payrollId, String employeeId);

    List<PayrollResponse> batchProcessPayrolls(List<String> payrollIds);
}
