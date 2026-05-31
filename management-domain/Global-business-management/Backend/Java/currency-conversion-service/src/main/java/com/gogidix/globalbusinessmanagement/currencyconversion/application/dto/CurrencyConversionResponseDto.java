package com.gogidix.globalbusinessmanagement.currencyconversion.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyConversionResponseDto {
    private String id;
    private String tenantId;
    private String fromCurrency;
    private String toCurrency;
    private String rate;
    private String source;
    private String effectiveDate;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
}
