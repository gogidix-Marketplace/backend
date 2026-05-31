package com.gogidix.hr.payroll.domain.repository;

import com.gogidix.hr.payroll.domain.model.Payslip;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Payslip
 */
public interface PayslipRepository {

    Payslip save(Payslip payslip);

    Optional<Payslip> findById(String id);

    Optional<Payslip> findByPayrollIdAndEmployeeId(String payrollId, String employeeId);

    List<Payslip> findByPayrollId(String payrollId);

    List<Payslip> findByEmployeeId(String employeeId);

    List<Payslip> findByTenantId(String tenantId);

    List<Payslip> findByTenantIdAndCountryCode(String tenantId, String countryCode);

    List<Payslip> findByEmployeeIdAndPayPeriod(String employeeId, YearMonth payPeriod);

    List<Payslip> findRecentPayslips(String employeeId, int limit);

    List<Payslip> findYearToDatePayslips(String employeeId, int year);

    List<Payslip> findByPayPeriod(YearMonth payPeriod);

    List<Payslip> findByStatus(String status);

    void deleteById(String id);

    void deleteByPayrollId(String payrollId);

    boolean existsByPayrollIdAndEmployeeId(String payrollId, String employeeId);

    List<Payslip> findByEmployeeIdAndYear(String employeeId, int year);

    List<Payslip> findIssuedPayslips(String tenantId, YearMonth payPeriod);

    List<Payslip> saveAll(List<Payslip> payslips);

    List<Payslip> findByTenantIdAndEmployeeIdAndPeriod(String tenantId, String employeeId, YearMonth period);

    List<Payslip> getByEmployeeIdAndPeriod(String employeeId, YearMonth period);

    Optional<Payslip> findByPayslipIdAndTenantId(String payslipId, String tenantId);

    void deleteByPayslipIdAndTenantId(String payslipId, String tenantId);

    List<Payslip> findByPayrollIdAndTenantId(String payrollId, String tenantId);

    List<Payslip> findByTenantIdAndEmployeeId(String tenantId, String employeeId);
}
