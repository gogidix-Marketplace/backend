package com.gogidix.finance.currency.domain.port.in;

import com.gogidix.finance.currency.domain.model.CurrencyConversion;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Input Port - Currency Conversion Query
 * Defines queries for currency conversion lookups
 * Following hexagonal architecture principles
 */
public interface CurrencyConversionQuery {

    /**
     * Get conversion by ID
     */
    Optional<CurrencyConversion> getById(GetConversionByIdQuery query);

    /**
     * Get conversion by conversion ID
     */
    Optional<CurrencyConversion> getByConversionId(GetByConversionIdQuery query);

    /**
     * Get all conversions for a tenant
     */
    List<CurrencyConversion> getAllForTenant(GetAllConversionsQuery query);

    /**
     * Get conversions by status
     */
    List<CurrencyConversion> getByStatus(GetByStatusQuery query);

    /**
     * Get conversions by currency pair
     */
    List<CurrencyConversion> getByCurrencyPair(GetByCurrencyPairQuery query);

    /**
     * Get conversions by date range
     */
    List<CurrencyConversion> getByDateRange(GetByDateRangeQuery query);

    /**
     * Get conversions by reference ID
     */
    List<CurrencyConversion> getByReferenceId(GetByReferenceIdQuery query);

    /**
     * Get paginated conversions
     */
    List<CurrencyConversion> getPaginated(GetPaginatedConversionsQuery query);

    /**
     * Query object for getting conversion by ID
     */
    record GetConversionByIdQuery(
        String tenantId,
        String conversionId
    ) {}

    /**
     * Query object for getting conversion by conversion ID
     */
    record GetByConversionIdQuery(
        String tenantId,
        String conversionId
    ) {}

    /**
     * Query object for getting all conversions
     */
    record GetAllConversionsQuery(
        String tenantId
    ) {}

    /**
     * Query object for getting conversions by status
     */
    record GetByStatusQuery(
        String tenantId,
        CurrencyConversion.ConversionStatus status
    ) {}

    /**
     * Query object for getting conversions by currency pair
     */
    record GetByCurrencyPairQuery(
        String tenantId,
        String fromCurrency,
        String toCurrency
    ) {}

    /**
     * Query object for getting conversions by date range
     */
    record GetByDateRangeQuery(
        String tenantId,
        Instant startDate,
        Instant endDate
    ) {}

    /**
     * Query object for getting conversions by reference ID
     */
    record GetByReferenceIdQuery(
        String tenantId,
        String referenceId
    ) {}

    /**
     * Query object for getting paginated conversions
     */
    record GetPaginatedConversionsQuery(
        String tenantId,
        int page,
        int size
    ) {}
}
