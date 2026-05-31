package com.gogidix.hr.payroll.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

/**
 * Payroll Summary Response DTO
 * Contains summary information for payroll periods
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayrollSummaryResponse {

    private String payrollId;
    private YearMonth payrollPeriod;
    private String status;
    private Integer employeeCount;
    private BigDecimal totalGrossPay;
    private BigDecimal totalNetPay;
    private BigDecimal totalTaxes;
    private BigDecimal totalDeductions;
    private String currency;
    private String paymentDate;
    @Builder.Default
    private List<DepartmentSummary> departmentSummaries = List.of();
    private BigDecimal averageGrossPay;
    private BigDecimal averageNetPay;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartmentSummary {
        private String department;
        private Integer employeeCount;
        private BigDecimal totalGrossPay;
        private BigDecimal totalNetPay;
        private BigDecimal averageGrossPay;
    }
}
