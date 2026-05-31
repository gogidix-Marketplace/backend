package com.gogidix.globalbusinessmanagement.multicurrency.infrastructure.messaging;

import com.gogidix.globalbusinessmanagement.multicurrency.domain.event.*;
import com.gogidix.globalbusinessmanagement.multicurrency.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishCurrencyCreated(CurrencyCreatedEvent event) {
        log.info("Currency created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCurrencyUpdated(CurrencyUpdatedEvent event) {
        log.info("Currency updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCurrencyDeleted(CurrencyDeletedEvent event) {
        log.info("Currency deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCurrencyPairCreated(CurrencyPairCreatedEvent event) {
        log.info("CurrencyPair created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCurrencyPairUpdated(CurrencyPairUpdatedEvent event) {
        log.info("CurrencyPair updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCurrencyPairDeleted(CurrencyPairDeletedEvent event) {
        log.info("CurrencyPair deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
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
    public void publishMultiCurrencyAccountCreated(MultiCurrencyAccountCreatedEvent event) {
        log.info("MultiCurrencyAccount created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMultiCurrencyAccountUpdated(MultiCurrencyAccountUpdatedEvent event) {
        log.info("MultiCurrencyAccount updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMultiCurrencyAccountDeleted(MultiCurrencyAccountDeletedEvent event) {
        log.info("MultiCurrencyAccount deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
