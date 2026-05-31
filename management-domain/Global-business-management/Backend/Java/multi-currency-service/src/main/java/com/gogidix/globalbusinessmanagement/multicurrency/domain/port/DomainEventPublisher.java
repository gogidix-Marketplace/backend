package com.gogidix.globalbusinessmanagement.multicurrency.domain.port;

import com.gogidix.globalbusinessmanagement.multicurrency.domain.event.*;

public interface DomainEventPublisher {
    void publishCurrencyCreated(CurrencyCreatedEvent event);
    void publishCurrencyUpdated(CurrencyUpdatedEvent event);
    void publishCurrencyDeleted(CurrencyDeletedEvent event);
    void publishCurrencyPairCreated(CurrencyPairCreatedEvent event);
    void publishCurrencyPairUpdated(CurrencyPairUpdatedEvent event);
    void publishCurrencyPairDeleted(CurrencyPairDeletedEvent event);
    void publishExchangeRateCreated(ExchangeRateCreatedEvent event);
    void publishExchangeRateUpdated(ExchangeRateUpdatedEvent event);
    void publishExchangeRateDeleted(ExchangeRateDeletedEvent event);
    void publishMultiCurrencyAccountCreated(MultiCurrencyAccountCreatedEvent event);
    void publishMultiCurrencyAccountUpdated(MultiCurrencyAccountUpdatedEvent event);
    void publishMultiCurrencyAccountDeleted(MultiCurrencyAccountDeletedEvent event);
}
