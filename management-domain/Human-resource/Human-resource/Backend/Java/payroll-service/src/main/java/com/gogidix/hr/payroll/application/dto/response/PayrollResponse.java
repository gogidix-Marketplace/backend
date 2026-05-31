package com.gogidix.hr.payroll.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Payroll Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayrollResponse {

    private String id;
    private String tenantId;
    private String payrollCode;
    private YearMonth payrollPeriod;
    private LocalDate payDate;
    private String countryCode;
    private String currency;
    private BigDecimal totalGrossPay;
    private BigDecimal totalNetPay;
    private BigDecimal totalDeductions;
    private BigDecimal totalTaxes;
    private Integer employeeCount;
    private String status;
    private String processedBy;
    private LocalDate processedDate;
    private String approvedBy;
    private LocalDate approvedDate;
    private String notes;
}
