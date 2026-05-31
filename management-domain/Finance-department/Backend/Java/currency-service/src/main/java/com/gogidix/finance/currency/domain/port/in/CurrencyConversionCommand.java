package com.gogidix.finance.currency.domain.port.in;

import com.gogidix.finance.currency.domain.model.CurrencyConversion;
import com.gogidix.finance.currency.domain.model.Money;

import java.math.BigDecimal;

/**
 * Input Port - Currency Conversion Command
 * Defines commands for currency conversion operations
 * Following hexagonal architecture principles
 */
public interface CurrencyConversionCommand {

    /**
     * Perform a currency conversion
     */
    CurrencyConversion convert(ConvertCommand command);

    /**
     * Reverse a previous conversion
     */
    CurrencyConversion reverse(ReverseConversionCommand command);

    /**
     * Calculate conversion without persisting
     */
    Money calculate(CalculateConversionCommand command);

    /**
     * Command object for conversion
     */
    record ConvertCommand(
        String tenantId,
        String fromCurrency,
        String toCurrency,
        BigDecimal fromAmount,
        String referenceId,
        String correlationId
    ) {}

    /**
     * Command object for reversing conversion
     */
    record ReverseConversionCommand(
        String tenantId,
        String conversionId
    ) {}

    /**
     * Command object for calculating conversion
     */
    record CalculateConversionCommand(
        String tenantId,
        String fromCurrency,
        String toCurrency,
        BigDecimal amount
    ) {}
}
