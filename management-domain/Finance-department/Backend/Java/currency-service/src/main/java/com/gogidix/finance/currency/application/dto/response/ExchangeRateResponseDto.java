package com.gogidix.finance.currency.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gogidix.finance.currency.domain.model.ExchangeRate;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Response DTO - Exchange Rate
 * Response object for exchange rate data
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ExchangeRateResponseDto(
    String id,
    String baseCurrency,
    String quoteCurrency,
    BigDecimal rate,
    BigDecimal inverseRate,
    Instant validFrom,
    Instant validTo,
    String source,
    ExchangeRate.RateQuality quality,
    Instant createdAt,
    Instant updatedAt
) {
}
