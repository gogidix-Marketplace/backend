package com.gogidix.finance.currency.application.service;

import com.gogidix.finance.currency.domain.model.CurrencyConversion;
import com.gogidix.finance.currency.domain.port.in.CurrencyConversionQuery;
import com.gogidix.finance.currency.domain.repository.CurrencyConversionRepository;
import com.gogidix.finance.currency.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Application Service - Currency Conversion Query Handler
 * Implements currency conversion query use cases
 * Following hexagonal architecture principles
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyConversionQueryService implements CurrencyConversionQuery {

    private final CurrencyConversionRepository conversionRepository;

    @Override
    public Optional<CurrencyConversion> getById(GetConversionByIdQuery query) {
        String tenantId = query.tenantId() != null ? query.tenantId() : RequestContextHolder.getTenantId();
        log.debug("Getting conversion by ID: {} for tenant: {}", query.conversionId(), tenantId);
        return conversionRepository.findByIdAndTenantId(query.conversionId(), tenantId);
    }

    @Override
    public Optional<CurrencyConversion> getByConversionId(GetByConversionIdQuery query) {
        String tenantId = query.tenantId() != null ? query.tenantId() : RequestContextHolder.getTenantId();
        log.debug("Getting conversion by conversion ID: {} for tenant: {}", query.conversionId(), tenantId);
        return conversionRepository.findByConversionIdAndTenantId(query.conversionId(), tenantId);
    }

    @Override
    public List<CurrencyConversion> getAllForTenant(GetAllConversionsQuery query) {
        String tenantId = query.tenantId() != null ? query.tenantId() : RequestContextHolder.getTenantId();
        log.debug("Getting all conversions for tenant: {}", tenantId);
        return conversionRepository.findByTenantId(tenantId);
    }

    @Override
    public List<CurrencyConversion> getByStatus(GetByStatusQuery query) {
        String tenantId = query.tenantId() != null ? query.tenantId() : RequestContextHolder.getTenantId();
        log.debug("Getting conversions by status: {} for tenant: {}", query.status(), tenantId);
        return conversionRepository.findByTenantIdAndStatus(tenantId, query.status());
    }

    @Override
    public List<CurrencyConversion> getByCurrencyPair(GetByCurrencyPairQuery query) {
        String tenantId = query.tenantId() != null ? query.tenantId() : RequestContextHolder.getTenantId();
        log.debug("Getting conversions for pair: {}/{} for tenant: {}",
            query.fromCurrency(), query.toCurrency(), tenantId);
        return conversionRepository.findByTenantIdAndFromCurrencyAndToCurrency(
            tenantId, query.fromCurrency(), query.toCurrency());
    }

    @Override
    public List<CurrencyConversion> getByDateRange(GetByDateRangeQuery query) {
        String tenantId = query.tenantId() != null ? query.tenantId() : RequestContextHolder.getTenantId();
        log.debug("Getting conversions between {} and {} for tenant: {}",
            query.startDate(), query.endDate(), tenantId);
        return conversionRepository.findByTenantIdAndConvertedAtBetween(
            tenantId, query.startDate(), query.endDate());
    }

    @Override
    public List<CurrencyConversion> getByReferenceId(GetByReferenceIdQuery query) {
        String tenantId = query.tenantId() != null ? query.tenantId() : RequestContextHolder.getTenantId();
        log.debug("Getting conversions by reference ID: {} for tenant: {}", query.referenceId(), tenantId);
        return conversionRepository.findByReferenceIdAndTenantId(query.referenceId(), tenantId);
    }

    @Override
    public List<CurrencyConversion> getPaginated(GetPaginatedConversionsQuery query) {
        String tenantId = query.tenantId() != null ? query.tenantId() : RequestContextHolder.getTenantId();
        log.debug("Getting paginated conversions for tenant: {}, page: {}, size: {}",
            tenantId, query.page(), query.size());
        return conversionRepository.findByTenantIdOrderByConvertedAtDesc(
            tenantId, query.page(), query.size());
    }
}
