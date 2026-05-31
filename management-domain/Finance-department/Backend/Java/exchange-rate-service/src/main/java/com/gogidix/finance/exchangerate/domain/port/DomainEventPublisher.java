package com.gogidix.finance.exchangerate.domain.port;

import com.gogidix.finance.exchangerate.domain.event.*;

public interface DomainEventPublisher {
    void publishExchangeRateCreated(ExchangeRateCreatedEvent event);
    void publishExchangeRateUpdated(ExchangeRateUpdatedEvent event);
    void publishExchangeRateDeleted(ExchangeRateDeletedEvent event);
    void publishHistoricalRateCreated(HistoricalRateCreatedEvent event);
    void publishHistoricalRateUpdated(HistoricalRateUpdatedEvent event);
    void publishHistoricalRateDeleted(HistoricalRateDeletedEvent event);
    void publishRateHistoryCreated(RateHistoryCreatedEvent event);
    void publishRateHistoryUpdated(RateHistoryUpdatedEvent event);
    void publishRateHistoryDeleted(RateHistoryDeletedEvent event);
    void publishRateSourceCreated(RateSourceCreatedEvent event);
    void publishRateSourceUpdated(RateSourceUpdatedEvent event);
    void publishRateSourceDeleted(RateSourceDeletedEvent event);
}
