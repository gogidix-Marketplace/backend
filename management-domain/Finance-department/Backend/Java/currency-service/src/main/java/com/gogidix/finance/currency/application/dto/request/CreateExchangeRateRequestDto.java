package com.gogidix.finance.currency.application.dto.request;

import com.gogidix.finance.currency.domain.model.ExchangeRate;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

/**
 * Request DTO - Create Exchange Rate
 * Validation DTO for creating/updating exchange rates
 */
@Builder
public record CreateExchangeRateRequestDto(
    @NotBlank(message = "Base currency is required")
    String baseCurrency,

    @NotBlank(message = "Quote currency is required")
    String quoteCurrency,

    @NotNull(message = "Rate is required")
    @DecimalMin(value = "0.000001", message = "Rate must be positive")
    BigDecimal rate,

    String source,

    ExchangeRate.RateQuality quality
) {
}
