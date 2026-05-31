package com.gogidix.finance.currency.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.gogidix.finance.currency.domain.model.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

/**
 * Response DTO - Currency
 * Response object for currency data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CurrencyResponseDto {

    private String id;
    private String currencyCode;
    private String name;
    private String symbol;
    private int decimalPlaces;
    private String isoNumericCode;
    private CurrencyStatusDto status;
    private Set<String> countryCodes;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant updatedAt;

    public enum CurrencyStatusDto {
        ACTIVE,
        INACTIVE,
        PENDING_ACTIVATION,
        SUSPENDED
    }

    public static CurrencyResponseDto from(Currency currency) {
        return CurrencyResponseDto.builder()
            .id(currency.getId())
            .currencyCode(currency.getCurrencyCode())
            .name(currency.getName())
            .symbol(currency.getSymbol())
            .decimalPlaces(currency.getDecimalPlaces())
            .isoNumericCode(currency.getIsoNumericCode())
            .status(mapStatus(currency.getStatus()))
            .countryCodes(currency.getCountryCodes() != null
                ? Set.copyOf(currency.getCountryCodes()) : null)
            .createdAt(currency.getCreatedAt())
            .updatedAt(currency.getUpdatedAt())
            .build();
    }

    private static CurrencyStatusDto mapStatus(Currency.CurrencyStatus status) {
        return status != null ? CurrencyStatusDto.valueOf(status.name()) : null;
    }
}
