package com.gogidix.finance.currency.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Response DTO - Currency Pair
 * Response object for currency pair data
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CurrencyPairResponseDto(
    String id,
    String baseCurrency,
    String quoteCurrency,
    String pair,
    boolean active
) {
    public static CurrencyPairResponseDto from(String baseCurrency, String quoteCurrency) {
        return new CurrencyPairResponseDto(
            null,
            baseCurrency,
            quoteCurrency,
            baseCurrency + "/" + quoteCurrency,
            true
        );
    }
}
