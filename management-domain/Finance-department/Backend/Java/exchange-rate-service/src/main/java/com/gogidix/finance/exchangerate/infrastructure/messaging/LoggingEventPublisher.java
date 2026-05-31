package com.gogidix.finance.exchangerate.infrastructure.messaging;

import com.gogidix.finance.exchangerate.domain.event.*;
import com.gogidix.finance.exchangerate.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishExchangeRateCreated(ExchangeRateCreatedEvent event) {
        log.info("ExchangeRate created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishExchangeRateUpdated(ExchangeRateUpdatedEvent event) {
        log.info("ExchangeRate updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishExchangeRateDeleted(ExchangeRateDeletedEvent event) {
        log.info("ExchangeRate deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishHistoricalRateCreated(HistoricalRateCreatedEvent event) {
        log.info("HistoricalRate created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishHistoricalRateUpdated(HistoricalRateUpdatedEvent event) {
        log.info("HistoricalRate updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishHistoricalRateDeleted(HistoricalRateDeletedEvent event) {
        log.info("HistoricalRate deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRateHistoryCreated(RateHistoryCreatedEvent event) {
        log.info("RateHistory created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRateHistoryUpdated(RateHistoryUpdatedEvent event) {
        log.info("RateHistory updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRateHistoryDeleted(RateHistoryDeletedEvent event) {
        log.info("RateHistory deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRateSourceCreated(RateSourceCreatedEvent event) {
        log.info("RateSource created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRateSourceUpdated(RateSourceUpdatedEvent event) {
        log.info("RateSource updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishRateSourceDeleted(RateSourceDeletedEvent event) {
        log.info("RateSource deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
