package com.gogidix.finance.currency.application.service;

import com.gogidix.finance.currency.domain.event.CurrencyConvertedEvent;
import com.gogidix.finance.currency.domain.event.DomainEvent;
import com.gogidix.finance.currency.domain.model.CurrencyConversion;
import com.gogidix.finance.currency.domain.model.ExchangeRate;
import com.gogidix.finance.currency.domain.model.Money;
import com.gogidix.finance.currency.domain.port.in.CurrencyConversionCommand;
import com.gogidix.finance.currency.domain.port.in.ExchangeRateQuery;
import com.gogidix.finance.currency.domain.port.out.EventPublisher;
import com.gogidix.finance.currency.domain.repository.CurrencyConversionRepository;
import com.gogidix.finance.currency.shared.requestcontext.RequestContextHolder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Application Service - Currency Conversion Command Handler
 * Implements currency conversion use cases
 * Following hexagonal architecture principles
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyConversionCommandService implements CurrencyConversionCommand {

    private final CurrencyConversionRepository conversionRepository;
    private final ExchangeRateQuery exchangeRateQuery;
    private final EventPublisher eventPublisher;

    @Override
    @Transactional
    public CurrencyConversion convert(ConvertCommand command) {
        String tenantId = command.tenantId() != null ? command.tenantId() : RequestContextHolder.getTenantId();

        log.debug("Converting currency: {} {} to {} for tenant: {}",
            command.fromCurrency(), command.fromAmount(), command.toCurrency(), tenantId);

        ExchangeRate rate = getExchangeRate(tenantId, command.fromCurrency(), command.toCurrency());

        Money fromMoney = Money.of(command.fromAmount(), command.fromCurrency());
        Money toMoney = fromMoney.convert(command.toCurrency(), rate.getRate());

        CurrencyConversion conversion = CurrencyConversion.builder()
            .tenantId(tenantId)
            .conversionId(UUID.randomUUID().toString())
            .fromCurrency(command.fromCurrency())
            .toCurrency(command.toCurrency())
            .fromAmount(command.fromAmount())
            .toAmount(toMoney.getAmount())
            .exchangeRate(rate.getRate())
            .rateSource(rate.getSource())
            .referenceId(command.referenceId())
            .build();

        CurrencyConversion saved = conversionRepository.save(conversion);

        publishConversionEvent(saved, command.correlationId());

        log.info("Currency converted: {} {} -> {} {} for tenant: {}",
            command.fromCurrency(), command.fromAmount(),
            toMoney.getAmount(), command.toCurrency(), tenantId);

        return saved;
    }

    @Override
    @Transactional
    public CurrencyConversion reverse(ReverseConversionCommand command) {
        String tenantId = command.tenantId() != null ? command.tenantId() : RequestContextHolder.getTenantId();

        log.debug("Reversing conversion: {} for tenant: {}", command.conversionId(), tenantId);

        CurrencyConversion conversion = conversionRepository.findByConversionIdAndTenantId(
            command.conversionId(), tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Conversion not found"));

        conversion.reverse();
        CurrencyConversion saved = conversionRepository.save(conversion);

        log.info("Currency conversion reversed: {} for tenant: {}", command.conversionId(), tenantId);
        return saved;
    }

    @Override
    public Money calculate(CalculateConversionCommand command) {
        String tenantId = command.tenantId() != null ? command.tenantId() : RequestContextHolder.getTenantId();

        log.debug("Calculating conversion: {} {} to {} for tenant: {}",
            command.fromCurrency(), command.amount(), command.toCurrency(), tenantId);

        ExchangeRate rate = getExchangeRate(tenantId, command.fromCurrency(), command.toCurrency());

        Money fromMoney = Money.of(command.amount(), command.fromCurrency());
        return fromMoney.convert(command.toCurrency(), rate.getRate());
    }

    private ExchangeRate getExchangeRate(String tenantId, String fromCurrency, String toCurrency) {
        return exchangeRateQuery.getCurrentRate(
            new ExchangeRateQuery.GetCurrentRateQuery(tenantId, fromCurrency, toCurrency))
            .orElseThrow(() -> new IllegalArgumentException(
                "Exchange rate not available for " + fromCurrency + "/" + toCurrency));
    }

    private void publishConversionEvent(CurrencyConversion conversion, String correlationId) {
        CurrencyConvertedEvent event = new CurrencyConvertedEvent(
            conversion.getTenantId(),
            correlationId,
            conversion.getConversionId(),
            conversion.getFromCurrency(),
            conversion.getToCurrency(),
            conversion.getFromAmount(),
            conversion.getToAmount(),
            conversion.getExchangeRate(),
            conversion.getReferenceId()
        );
        eventPublisher.publish(event);
    }
}
