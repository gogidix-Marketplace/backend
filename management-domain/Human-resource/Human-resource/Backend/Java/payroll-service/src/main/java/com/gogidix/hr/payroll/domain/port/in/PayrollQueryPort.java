package com.gogidix.hr.payroll.domain.port.in;

import com.gogidix.hr.payroll.application.dto.response.PayrollResponse;
import com.gogidix.hr.payroll.application.dto.response.PayrollSummaryResponse;
import com.gogidix.hr.payroll.domain.enums.PayrollStatus;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Input port for Payroll queries
 */
public interface PayrollQueryPort {

    PayrollResponse getPayrollById(String id);

    List<PayrollResponse> getAllPayrolls();

    List<PayrollResponse> getPayrollsByTenant(String tenantId);

    List<PayrollResponse> getPayrollsByCountry(String tenantId, String countryCode);

    List<PayrollResponse> getPayrollsByStatus(String tenantId, PayrollStatus status);

    PayrollResponse getPayrollByPeriod(String tenantId, YearMonth period);

    List<PayrollResponse> getPayrollsByDateRange(String tenantId, LocalDate startDate, LocalDate endDate);

    List<PayrollResponse> getPendingApprovalPayrolls(String tenantId);

    List<PayrollResponse> getProcessedPayrollsPendingPayment(String tenantId);

    List<PayrollSummaryResponse> getPayrollSummary(String tenantId, int year);

    List<PayrollResponse> getRecentPayrolls(String tenantId, int limit);

    List<PayrollResponse> getActivePayrolls(String tenantId);
}
