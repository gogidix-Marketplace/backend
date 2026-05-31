package com.gogidix.finance.currency.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gogidix.finance.currency.domain.model.CurrencyConversion;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Response DTO - Currency Conversion
 * Response object for currency conversion data
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CurrencyConversionResponseDto(
    String id,
    String conversionId,
    String fromCurrency,
    String toCurrency,
    BigDecimal fromAmount,
    BigDecimal toAmount,
    BigDecimal exchangeRate,
    String rateSource,
    Instant convertedAt,
    String referenceId,
    CurrencyConversion.ConversionStatus status,
    Instant createdAt,
    Instant updatedAt
) {
}
