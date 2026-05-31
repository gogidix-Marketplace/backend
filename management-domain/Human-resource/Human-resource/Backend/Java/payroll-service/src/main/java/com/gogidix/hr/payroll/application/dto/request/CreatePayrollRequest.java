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
import java.time.YearMonth;

/**
 * Create Payroll Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePayrollRequest {

    @NotBlank(message = "Country code is required")
    private String countryCode;

    @NotNull(message = "Payroll period is required")
    private YearMonth payrollPeriod;

    @NotNull(message = "Pay date is required")
    private LocalDate payDate;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotNull(message = "Number of employees is required")
    @Positive(message = "Number of employees must be positive")
    private Integer employeeCount;

    private String notes;
}
