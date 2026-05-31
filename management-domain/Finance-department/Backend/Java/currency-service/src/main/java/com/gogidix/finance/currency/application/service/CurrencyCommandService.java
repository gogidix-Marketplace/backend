package com.gogidix.finance.currency.application.service;

import com.gogidix.finance.currency.domain.event.DomainEvent;
import com.gogidix.finance.currency.domain.model.Currency;
import com.gogidix.finance.currency.domain.port.in.CurrencyCommand;
import com.gogidix.finance.currency.domain.port.out.EventPublisher;
import com.gogidix.finance.currency.domain.repository.CurrencyRepository;
import com.gogidix.finance.currency.shared.exception.ConflictException;
import com.gogidix.finance.currency.shared.exception.NotFoundException;
import com.gogidix.finance.currency.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Application Service - Currency Command Handler
 * Implements currency command use cases
 * Following hexagonal architecture principles
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyCommandService implements CurrencyCommand {

    private final CurrencyRepository currencyRepository;
    private final EventPublisher eventPublisher;

    @Override
    @Transactional
    public Currency create(CreateCurrencyCommand command) {
        String tenantId = command.tenantId() != null ? command.tenantId() : RequestContextHolder.getTenantId();

        log.info("Creating currency: {} for tenant: {}", command.currencyCode(), tenantId);

        if (currencyRepository.existsByCurrencyCodeAndTenantId(command.currencyCode(), tenantId)) {
            throw new ConflictException("Currency", command.currencyCode());
        }

        Currency currency = Currency.create(
            tenantId,
            command.currencyCode(),
            command.name(),
            command.symbol(),
            command.decimalPlaces(),
            command.isoNumericCode(),
            command.countryCodes() != null ? List.of(command.countryCodes()) : List.of()
        );

        Currency saved = currencyRepository.save(currency);

        publishEvents(saved);

        log.info("Currency created: {} for tenant: {}", command.currencyCode(), tenantId);
        return saved;
    }

    @Override
    @Transactional
    public Currency update(UpdateCurrencyCommand command) {
        String tenantId = command.tenantId() != null ? command.tenantId() : RequestContextHolder.getTenantId();

        log.info("Updating currency: {} for tenant: {}", command.currencyId(), tenantId);

        Currency currency = currencyRepository.findByIdAndTenantId(command.currencyId(), tenantId)
            .orElseThrow(() -> new NotFoundException("Currency not found"));

        currency.updateDetails(
            command.name(),
            command.symbol(),
            command.decimalPlaces(),
            command.isoNumericCode()
        );

        Currency saved = currencyRepository.save(currency);

        publishEvents(saved);

        log.info("Currency updated: {} for tenant: {}", command.currencyId(), tenantId);
        return saved;
    }

    @Override
    @Transactional
    public void deactivate(DeactivateCurrencyCommand command) {
        String tenantId = command.tenantId() != null ? command.tenantId() : RequestContextHolder.getTenantId();

        log.info("Deactivating currency: {} for tenant: {}", command.currencyId(), tenantId);

        Currency currency = currencyRepository.findByIdAndTenantId(command.currencyId(), tenantId)
            .orElseThrow(() -> new NotFoundException("Currency not found"));

        currency.deactivate();

        currencyRepository.save(currency);

        publishEvents(currency);

        log.info("Currency deactivated: {} for tenant: {}", command.currencyId(), tenantId);
    }

    @Override
    @Transactional
    public void activate(ActivateCurrencyCommand command) {
        String tenantId = command.tenantId() != null ? command.tenantId() : RequestContextHolder.getTenantId();

        log.info("Activating currency: {} for tenant: {}", command.currencyId(), tenantId);

        Currency currency = currencyRepository.findByIdAndTenantId(command.currencyId(), tenantId)
            .orElseThrow(() -> new NotFoundException("Currency not found"));

        currency.activate();

        currencyRepository.save(currency);

        publishEvents(currency);

        log.info("Currency activated: {} for tenant: {}", command.currencyId(), tenantId);
    }

    @Override
    @Transactional
    public void delete(DeleteCurrencyCommand command) {
        String tenantId = command.tenantId() != null ? command.tenantId() : RequestContextHolder.getTenantId();

        log.info("Deleting currency: {} for tenant: {}", command.currencyId(), tenantId);

        if (!currencyRepository.findByIdAndTenantId(command.currencyId(), tenantId).isPresent()) {
            throw new NotFoundException("Currency not found");
        }

        currencyRepository.deleteByIdAndTenantId(command.currencyId(), tenantId);

        log.info("Currency deleted: {} for tenant: {}", command.currencyId(), tenantId);
    }

    private void publishEvents(Currency currency) {
        if (currency.hasUncommittedEvents() && eventPublisher.isReady()) {
            List<Object> events = currency.getUncommittedEvents();
            for (Object event : events) {
                if (event instanceof DomainEvent domainEvent) {
                    eventPublisher.publish(domainEvent);
                }
            }
            currency.markEventsAsCommitted();
        }
    }
}
