package com.gogidix.finance.currency.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Request DTO - Create Currency
 * Validation DTO for creating a new currency
 */
@Retention(RetentionPolicy.RUNTIME)
@jakarta.validation.Constraint(validatedBy = {})
public @interface CreateCurrencyRequestDto {
    @NotBlank(message = "Currency code is required")
    @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters")
    String currencyCode();

    @NotBlank(message = "Currency name is required")
    @Size(max = 100, message = "Currency name must not exceed 100 characters")
    String name();

    @Size(max = 10, message = "Symbol must not exceed 10 characters")
    String symbol();

    int decimalPlaces() default 2;

    @Size(max = 3, message = "ISO numeric code must not exceed 3 characters")
    String isoNumericCode();

    String[] countryCodes() default {};
}
