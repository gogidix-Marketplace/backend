package com.gogidix.hr.payroll.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Update Payroll Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePayrollRequest {

    private LocalDate payDate;

    private String status;

    @Positive(message = "Total gross pay must be positive")
    private BigDecimal totalGrossPay;

    @Positive(message = "Total net pay must be positive")
    private BigDecimal totalNetPay;

    @Positive(message = "Total deductions must be positive")
    private BigDecimal totalDeductions;

    @Positive(message = "Total taxes must be positive")
    private BigDecimal totalTaxes;

    private String approvedBy;

    private LocalDate approvedDate;

    private String notes;
}
