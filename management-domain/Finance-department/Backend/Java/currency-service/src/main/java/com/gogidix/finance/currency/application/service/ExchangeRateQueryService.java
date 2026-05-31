package com.gogidix.finance.currency.application.service;

import com.gogidix.finance.currency.domain.model.ExchangeRate;
import com.gogidix.finance.currency.domain.port.in.ExchangeRateQuery;
import com.gogidix.finance.currency.domain.repository.ExchangeRateRepository;
import com.gogidix.finance.currency.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Application Service - Exchange Rate Query Handler
 * Implements exchange rate query use cases
 * Following hexagonal architecture principles
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeRateQueryService implements ExchangeRateQuery {

    private final ExchangeRateRepository exchangeRateRepository;

    @Override
    public Optional<ExchangeRate> getCurrentRate(GetCurrentRateQuery query) {
        String tenantId = query.tenantId() != null ? query.tenantId() : RequestContextHolder.getTenantId();
        log.debug("Getting current exchange rate: {}/{} for tenant: {}",
            query.baseCurrency(), query.quoteCurrency(), tenantId);
        return exchangeRateRepository.findByTenantIdAndBaseCurrencyAndQuoteCurrencyAndValidToIsNull(
            tenantId, query.baseCurrency(), query.quoteCurrency());
    }

    @Override
    public Optional<ExchangeRate> getRateAtTime(GetRateAtTimeQuery query) {
        String tenantId = query.tenantId() != null ? query.tenantId() : RequestContextHolder.getTenantId();
        log.debug("Getting exchange rate at time: {}/{} for tenant: {}",
            query.baseCurrency(), query.quoteCurrency(), tenantId);
        return exchangeRateRepository.findByTenantIdAndCurrencyPairAndValidAt(
            tenantId, query.baseCurrency(), query.quoteCurrency(), query.at());
    }

    @Override
    public List<ExchangeRate> getAllForTenant(GetAllRatesQuery query) {
        String tenantId = query.tenantId() != null ? query.tenantId() : RequestContextHolder.getTenantId();
        log.debug("Getting all exchange rates for tenant: {}", tenantId);
        return exchangeRateRepository.findByTenantId(tenantId);
    }

    @Override
    public List<ExchangeRate> getRatesForCurrency(GetRatesForCurrencyQuery query) {
        String tenantId = query.tenantId() != null ? query.tenantId() : RequestContextHolder.getTenantId();
        log.debug("Getting exchange rates for currency: {} for tenant: {}", query.currency(), tenantId);
        return exchangeRateRepository.findByTenantIdAndCurrency(tenantId, query.currency());
    }

    @Override
    public List<ExchangeRate> getRatesForPair(GetRatesForPairQuery query) {
        String tenantId = query.tenantId() != null ? query.tenantId() : RequestContextHolder.getTenantId();
        log.debug("Getting exchange rates for pair: {}/{} for tenant: {}",
            query.baseCurrency(), query.quoteCurrency(), tenantId);
        return exchangeRateRepository.findByTenantIdAndBaseCurrencyAndQuoteCurrency(
            tenantId, query.baseCurrency(), query.quoteCurrency());
    }

    @Override
    public List<ExchangeRate> getLatestRates(GetLatestRatesQuery query) {
        String tenantId = query.tenantId() != null ? query.tenantId() : RequestContextHolder.getTenantId();
        log.debug("Getting latest exchange rates for tenant: {}", tenantId);
        return exchangeRateRepository.findLatestRatesByTenantId(tenantId);
    }
}
