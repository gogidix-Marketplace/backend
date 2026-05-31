package com.gogidix.globalbusinessmanagement.currencyconversion.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyConversionRequestDto {
    private String tenantId;
    private String fromCurrency;
    private String toCurrency;
    private String rate;
    private String source;
    private String effectiveDate;
    private String status;
}
