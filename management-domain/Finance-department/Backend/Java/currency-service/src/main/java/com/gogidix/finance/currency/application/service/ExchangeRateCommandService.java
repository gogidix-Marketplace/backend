package com.gogidix.finance.currency.application.service;

import com.gogidix.finance.currency.domain.event.DomainEvent;
import com.gogidix.finance.currency.domain.event.ExchangeRateUpdatedEvent;
import com.gogidix.finance.currency.domain.model.ExchangeRate;
import com.gogidix.finance.currency.domain.port.in.ExchangeRateCommand;
import com.gogidix.finance.currency.domain.port.out.EventPublisher;
import com.gogidix.finance.currency.domain.policy.ExchangeRatePolicy;
import com.gogidix.finance.currency.domain.repository.ExchangeRateRepository;
import com.gogidix.finance.currency.shared.requestcontext.RequestContextHolder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Application Service - Exchange Rate Command Handler
 * Implements exchange rate command use cases
 * Following hexagonal architecture principles
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateCommandService implements ExchangeRateCommand {

    private final ExchangeRateRepository exchangeRateRepository;
    private final EventPublisher eventPublisher;
    private final ExchangeRatePolicy ratePolicy;

    @Override
    @Transactional
    public ExchangeRate createOrUpdate(CreateOrUpdateRateCommand command) {
        String tenantId = command.tenantId() != null ? command.tenantId() : RequestContextHolder.getTenantId();

        log.debug("Creating/updating exchange rate: {}/{} for tenant: {}",
            command.baseCurrency(), command.quoteCurrency(), tenantId);

        ratePolicy.validateRateValue(command.rate());

        Optional<ExchangeRate> existing = exchangeRateRepository
            .findByTenantIdAndBaseCurrencyAndQuoteCurrencyAndValidToIsNull(
                tenantId, command.baseCurrency(), command.quoteCurrency());

        BigDecimal oldRate = existing.map(ExchangeRate::getRate).orElse(null);

        if (existing.isPresent() && ratePolicy.isRateChangeAcceptable(oldRate, command.rate())) {
            ExchangeRate rate = existing.get();
            rate.setRate(command.rate());
            ExchangeRate saved = exchangeRateRepository.save(rate);
            publishRateUpdatedEvent(tenantId, command, oldRate, command.rate());
            return saved;
        } else if (existing.isEmpty()) {
            ExchangeRate newRate = ExchangeRate.builder()
                .tenantId(tenantId)
                .baseCurrency(command.baseCurrency())
                .quoteCurrency(command.quoteCurrency())
                .rate(command.rate())
                .source(command.source())
                .quality(command.quality())
                .validFrom(Instant.now())
                .build();
            ExchangeRate saved = exchangeRateRepository.save(newRate);
            publishRateUpdatedEvent(tenantId, command, null, command.rate());
            return saved;
        } else {
            throw new IllegalArgumentException("Rate change exceeds acceptable threshold");
        }
    }

    @Override
    @Transactional
    public void invalidate(InvalidateRateCommand command) {
        String tenantId = command.tenantId() != null ? command.tenantId() : RequestContextHolder.getTenantId();

        log.debug("Invalidating exchange rate: {}/{} for tenant: {}",
            command.baseCurrency(), command.quoteCurrency(), tenantId);

        exchangeRateRepository.invalidateCurrentRates(
            tenantId, command.baseCurrency(), command.quoteCurrency());

        log.info("Exchange rate invalidated: {}/{} for tenant: {}",
            command.baseCurrency(), command.quoteCurrency(), tenantId);
    }

    @Override
    @Transactional
    public List<ExchangeRate> bulkImport(BulkImportRatesCommand command) {
        String tenantId = command.tenantId() != null ? command.tenantId() : RequestContextHolder.getTenantId();

        log.debug("Bulk importing {} exchange rates for tenant: {}", command.rates().size(), tenantId);

        List<ExchangeRate> saved = new ArrayList<>();
        for (ExchangeRateCommand.BulkImportRatesCommand.RateData rateData : command.rates()) {
            try {
                ExchangeRate rate = createOrUpdate(new CreateOrUpdateRateCommand(
                    tenantId,
                    rateData.baseCurrency(),
                    rateData.quoteCurrency(),
                    rateData.rate(),
                    command.source(),
                    ExchangeRate.RateQuality.DAILY
                ));
                saved.add(rate);
            } catch (Exception e) {
                log.warn("Failed to import rate {}/{}: {}",
                    rateData.baseCurrency(), rateData.quoteCurrency(), e.getMessage());
            }
        }

        log.info("Bulk imported {} exchange rates for tenant: {}", saved.size(), tenantId);
        return saved;
    }

    private void publishRateUpdatedEvent(String tenantId, CreateOrUpdateRateCommand command,
                                         BigDecimal oldRate, BigDecimal newRate) {
        ExchangeRateUpdatedEvent event = new ExchangeRateUpdatedEvent(
            tenantId,
            command.baseCurrency(),
            command.quoteCurrency(),
            newRate,
            oldRate,
            command.source()
        );
        eventPublisher.publish(event);
    }
}
