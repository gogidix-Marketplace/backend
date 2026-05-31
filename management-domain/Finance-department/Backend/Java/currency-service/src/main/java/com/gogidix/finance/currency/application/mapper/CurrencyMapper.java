package com.gogidix.finance.currency.application.mapper;

import com.gogidix.finance.currency.application.dto.response.*;
import com.gogidix.finance.currency.domain.model.*;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper - Currency Domain to DTO
 * Converts domain entities to DTOs
 * Using simple mapping instead of MapStruct for flexibility
 */
@Component
public class CurrencyMapper {

    public CurrencyResponseDto toResponseDto(Currency currency) {
        return CurrencyResponseDto.builder()
            .id(currency.getId())
            .currencyCode(currency.getCurrencyCode())
            .name(currency.getName())
            .symbol(currency.getSymbol())
            .decimalPlaces(currency.getDecimalPlaces())
            .isoNumericCode(currency.getIsoNumericCode())
            .status(currency.getStatus() != null ?
                CurrencyResponseDto.CurrencyStatusDto.valueOf(currency.getStatus().name()) : null)
            .countryCodes(currency.getCountryCodes() != null ?
                Set.copyOf(currency.getCountryCodes()) : Set.of())
            .createdAt(currency.getCreatedAt())
            .updatedAt(currency.getUpdatedAt())
            .build();
    }

    public ExchangeRateResponseDto toResponseDto(ExchangeRate rate) {
        return ExchangeRateResponseDto.builder()
            .id(rate.getId())
            .baseCurrency(rate.getBaseCurrency())
            .quoteCurrency(rate.getQuoteCurrency())
            .rate(rate.getRate())
            .inverseRate(rate.getInverseRate())
            .validFrom(rate.getValidFrom())
            .validTo(rate.getValidTo())
            .source(rate.getSource())
            .quality(rate.getQuality())
            .createdAt(rate.getCreatedAt())
            .updatedAt(rate.getUpdatedAt())
            .build();
    }

    public CurrencyConversionResponseDto toResponseDto(CurrencyConversion conversion) {
        return CurrencyConversionResponseDto.builder()
            .id(conversion.getId())
            .conversionId(conversion.getConversionId())
            .fromCurrency(conversion.getFromCurrency())
            .toCurrency(conversion.getToCurrency())
            .fromAmount(conversion.getFromAmount())
            .toAmount(conversion.getToAmount())
            .exchangeRate(conversion.getExchangeRate())
            .rateSource(conversion.getRateSource())
            .convertedAt(conversion.getConvertedAt())
            .referenceId(conversion.getReferenceId())
            .status(conversion.getStatus())
            .createdAt(conversion.getCreatedAt())
            .updatedAt(conversion.getUpdatedAt())
            .build();
    }

    public Money toMoney(String currency, java.math.BigDecimal amount) {
        return Money.of(amount, currency);
    }

    public CurrencyPair toCurrencyPair(String base, String quote) {
        return CurrencyPair.of(base, quote);
    }
}
