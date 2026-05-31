package com.gogidix.finance.currency.domain.aggregate;

import com.gogidix.finance.currency.domain.event.CurrencyCreatedEvent;
import com.gogidix.finance.currency.domain.event.CurrencyDeactivatedEvent;
import com.gogidix.finance.currency.domain.event.CurrencyUpdatedEvent;
import com.gogidix.finance.currency.domain.model.AggregateRoot;
import com.gogidix.finance.currency.domain.model.Currency;

/**
 * Aggregate Root - Currency Aggregate
 * Manages currency entities and their related behaviors
 * Following DDD aggregate pattern
 */
public class CurrencyAggregate extends AggregateRoot {

    private Currency currency;

    public CurrencyAggregate(String tenantId) {
        super(tenantId);
    }

    public CurrencyAggregate(Currency currency) {
        super(currency.getTenantId());
        this.currency = currency;
    }

    public void createCurrency(Currency newCurrency) {
        newCurrency.validateCurrencyCode();
        this.currency = newCurrency;
        registerEvent(new CurrencyCreatedEvent(
            newCurrency.getTenantId(),
            newCurrency.getCurrencyCode(),
            newCurrency.getName()
        ));
    }

    public void updateCurrencyDetails(String name, String symbol, int decimalPlaces, String isoNumericCode) {
        if (currency == null) {
            throw new IllegalStateException("Currency not initialized");
        }
        currency.updateDetails(name, symbol, decimalPlaces, isoNumericCode);
        registerEvent(new CurrencyUpdatedEvent(
            currency.getTenantId(),
            currency.getId(),
            currency.getCurrencyCode()
        ));
    }

    public void deactivateCurrency() {
        if (currency == null) {
            throw new IllegalStateException("Currency not initialized");
        }
        currency.deactivate();
        registerEvent(new CurrencyDeactivatedEvent(
            currency.getTenantId(),
            currency.getId(),
            currency.getCurrencyCode()
        ));
    }

    public void activateCurrency() {
        if (currency == null) {
            throw new IllegalStateException("Currency not initialized");
        }
        currency.activate();
    }

    public Currency getCurrency() {
        return currency;
    }

    public boolean isActive() {
        return currency != null && currency.isActive();
    }
}
