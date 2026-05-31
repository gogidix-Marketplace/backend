package com.gogidix.finance.currency.application.service;

import com.gogidix.finance.currency.domain.model.Currency;
import com.gogidix.finance.currency.domain.port.in.CurrencyQuery;
import com.gogidix.finance.currency.domain.repository.CurrencyRepository;
import com.gogidix.finance.currency.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Application Service - Currency Query Handler
 * Implements currency query use cases
 * Following hexagonal architecture principles
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyQueryService implements CurrencyQuery {

    private final CurrencyRepository currencyRepository;

    @Override
    public Optional<Currency> getById(GetCurrencyByIdQuery query) {
        String tenantId = query.tenantId() != null ? query.tenantId() : RequestContextHolder.getTenantId();
        log.debug("Getting currency by ID: {} for tenant: {}", query.currencyId(), tenantId);
        return currencyRepository.findByIdAndTenantId(query.currencyId(), tenantId);
    }

    @Override
    public Optional<Currency> getByCode(GetCurrencyByCodeQuery query) {
        String tenantId = query.tenantId() != null ? query.tenantId() : RequestContextHolder.getTenantId();
        log.debug("Getting currency by code: {} for tenant: {}", query.currencyCode(), tenantId);
        return currencyRepository.findByCurrencyCodeAndTenantId(query.currencyCode(), tenantId);
    }

    @Override
    public List<Currency> getAllForTenant(GetAllCurrenciesQuery query) {
        String tenantId = query.tenantId() != null ? query.tenantId() : RequestContextHolder.getTenantId();
        log.debug("Getting all currencies for tenant: {}", tenantId);
        return currencyRepository.findByTenantId(tenantId);
    }

    @Override
    public List<Currency> getActiveForTenant(GetActiveCurrenciesQuery query) {
        String tenantId = query.tenantId() != null ? query.tenantId() : RequestContextHolder.getTenantId();
        log.debug("Getting active currencies for tenant: {}", tenantId);
        return currencyRepository.findByTenantIdAndStatus(
            tenantId,
            Currency.CurrencyStatus.ACTIVE
        );
    }

    @Override
    public boolean exists(CurrencyExistsQuery query) {
        String tenantId = query.tenantId() != null ? query.tenantId() : RequestContextHolder.getTenantId();
        log.debug("Checking currency existence: {} for tenant: {}", query.currencyCode(), tenantId);
        return currencyRepository.existsByCurrencyCodeAndTenantId(query.currencyCode(), tenantId);
    }
}
