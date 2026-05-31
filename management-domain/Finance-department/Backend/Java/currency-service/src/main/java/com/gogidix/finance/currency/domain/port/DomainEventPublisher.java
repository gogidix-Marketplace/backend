package com.gogidix.finance.currency.domain.port;

import com.gogidix.finance.currency.domain.event.*;

public interface DomainEventPublisher {
    void publishAggregateRootCreated(AggregateRootCreatedEvent event);
    void publishAggregateRootUpdated(AggregateRootUpdatedEvent event);
    void publishAggregateRootDeleted(AggregateRootDeletedEvent event);
    void publishCurrencyCreated(CurrencyCreatedEvent event);
    void publishCurrencyUpdated(CurrencyUpdatedEvent event);
    void publishCurrencyDeleted(CurrencyDeletedEvent event);
    void publishCurrencyConversionCreated(CurrencyConversionCreatedEvent event);
    void publishCurrencyConversionUpdated(CurrencyConversionUpdatedEvent event);
    void publishCurrencyConversionDeleted(CurrencyConversionDeletedEvent event);
    void publishCurrencyPairCreated(CurrencyPairCreatedEvent event);
    void publishCurrencyPairUpdated(CurrencyPairUpdatedEvent event);
    void publishCurrencyPairDeleted(CurrencyPairDeletedEvent event);
    void publishExchangeRateCreated(ExchangeRateCreatedEvent event);
    void publishExchangeRateUpdated(ExchangeRateUpdatedEvent event);
    void publishExchangeRateDeleted(ExchangeRateDeletedEvent event);
    void publishMoneyCreated(MoneyCreatedEvent event);
    void publishMoneyUpdated(MoneyUpdatedEvent event);
    void publishMoneyDeleted(MoneyDeletedEvent event);
}
