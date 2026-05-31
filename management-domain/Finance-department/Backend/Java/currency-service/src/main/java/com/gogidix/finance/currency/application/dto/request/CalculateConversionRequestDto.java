package com.gogidix.finance.currency.application.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

/**
 * Request DTO - Calculate Conversion
 * Validation DTO for calculating currency conversion without persisting
 */
@Builder
public record CalculateConversionRequestDto(
    @NotBlank(message = "From currency is required")
    String fromCurrency,

    @NotBlank(message = "To currency is required")
    String toCurrency,

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be positive")
    BigDecimal amount
) {
}
