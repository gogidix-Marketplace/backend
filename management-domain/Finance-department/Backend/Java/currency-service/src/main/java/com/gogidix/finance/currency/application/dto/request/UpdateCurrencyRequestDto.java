package com.gogidix.finance.currency.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * Request DTO - Update Currency
 * Validation DTO for updating an existing currency
 */
@Builder
public record UpdateCurrencyRequestDto(
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    String name,

    @Size(max = 10, message = "Symbol must not exceed 10 characters")
    String symbol,

    int decimalPlaces,

    @Size(max = 3, message = "ISO numeric code must not exceed 3 characters")
    String isoNumericCode
) {
}
