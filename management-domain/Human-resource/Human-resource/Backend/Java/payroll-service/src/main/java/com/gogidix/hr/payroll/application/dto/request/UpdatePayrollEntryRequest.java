package com.gogidix.hr.payroll.application.dto.request;

import com.gogidix.hr.payroll.domain.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Update Payroll Entry Request DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePayrollEntryRequest {

    private BigDecimal overtimeHours;

    private BigDecimal overtimeRate;

    private BigDecimal bonus;

    private BigDecimal commission;

    private BigDecimal allowances;

    private BigDecimal federalTax;

    private BigDecimal stateTax;

    private BigDecimal localTax;

    private BigDecimal socialSecurityTax;

    private BigDecimal medicareTax;

    private BigDecimal otherTaxes;

    private BigDecimal healthInsurance;

    private BigDecimal dentalInsurance;

    private BigDecimal retirement401k;

    private BigDecimal otherDeductions;

    private PaymentMethod paymentMethod;

    private String bankAccountNumber;

    private String bankRoutingNumber;

    private String taxCode;

    private Integer taxExemptions;

    private List<CreatePayrollEntryRequest.DeductionDetailRequest> deductionDetails;

    private List<CreatePayrollEntryRequest.EarningDetailRequest> earningDetails;

    private String notes;
}
