package com.gogidix.finance.currency.infrastructure.messaging;

import com.gogidix.finance.currency.domain.event.*;
import com.gogidix.finance.currency.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {


    @Override
    public void publishAggregateRootCreated(AggregateRootCreatedEvent event) {
        log.info("AggregateRoot created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAggregateRootUpdated(AggregateRootUpdatedEvent event) {
        log.info("AggregateRoot updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAggregateRootDeleted(AggregateRootDeletedEvent event) {
        log.info("AggregateRoot deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
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
    public void publishCurrencyConversionCreated(CurrencyConversionCreatedEvent event) {
        log.info("CurrencyConversion created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCurrencyConversionUpdated(CurrencyConversionUpdatedEvent event) {
        log.info("CurrencyConversion updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishCurrencyConversionDeleted(CurrencyConversionDeletedEvent event) {
        log.info("CurrencyConversion deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
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
    public void publishMoneyCreated(MoneyCreatedEvent event) {
        log.info("Money created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMoneyUpdated(MoneyUpdatedEvent event) {
        log.info("Money updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMoneyDeleted(MoneyDeletedEvent event) {
        log.info("Money deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
}
