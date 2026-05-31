package com.gogidix.finance.currency.domain.port.in;

import com.gogidix.finance.currency.domain.model.Currency;

/**
 * Input Port - Currency Command
 * Defines commands for currency operations
 * Following hexagonal architecture principles
 */
public interface CurrencyCommand {

    /**
     * Create a new currency
     */
    Currency create(CreateCurrencyCommand command);

    /**
     * Update an existing currency
     */
    Currency update(UpdateCurrencyCommand command);

    /**
     * Deactivate a currency
     */
    void deactivate(DeactivateCurrencyCommand command);

    /**
     * Activate a currency
     */
    void activate(ActivateCurrencyCommand command);

    /**
     * Delete a currency
     */
    void delete(DeleteCurrencyCommand command);

    /**
     * Command object for creating a currency
     */
    record CreateCurrencyCommand(
        String tenantId,
        String currencyCode,
        String name,
        String symbol,
        int decimalPlaces,
        String isoNumericCode,
        String[] countryCodes
    ) {}

    /**
     * Command object for updating a currency
     */
    record UpdateCurrencyCommand(
        String tenantId,
        String currencyId,
        String name,
        String symbol,
        int decimalPlaces,
        String isoNumericCode
    ) {}

    /**
     * Command object for deactivating a currency
     */
    record DeactivateCurrencyCommand(
        String tenantId,
        String currencyId
    ) {}

    /**
     * Command object for activating a currency
     */
    record ActivateCurrencyCommand(
        String tenantId,
        String currencyId
    ) {}

    /**
     * Command object for deleting a currency
     */
    record DeleteCurrencyCommand(
        String tenantId,
        String currencyId
    ) {}
}
