package com.gogidix.finance.currency.domain.port.in;

import com.gogidix.finance.currency.domain.model.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;

/**
 * Input Port - Exchange Rate Command
 * Defines commands for exchange rate operations
 * Following hexagonal architecture principles
 */
public interface ExchangeRateCommand {

    /**
     * Create or update an exchange rate
     */
    ExchangeRate createOrUpdate(CreateOrUpdateRateCommand command);

    /**
     * Invalidate an exchange rate
     */
    void invalidate(InvalidateRateCommand command);

    /**
     * Bulk import exchange rates
     */
    List<ExchangeRate> bulkImport(BulkImportRatesCommand command);

    /**
     * Command object for creating/updating a rate
     */
    record CreateOrUpdateRateCommand(
        String tenantId,
        String baseCurrency,
        String quoteCurrency,
        BigDecimal rate,
        String source,
        ExchangeRate.RateQuality quality
    ) {}

    /**
     * Command object for invalidating a rate
     */
    record InvalidateRateCommand(
        String tenantId,
        String baseCurrency,
        String quoteCurrency
    ) {}

    /**
     * Command object for bulk importing rates
     */
    record BulkImportRatesCommand(
        String tenantId,
        String source,
        List<RateData> rates
    ) {
        public record RateData(
            String baseCurrency,
            String quoteCurrency,
            BigDecimal rate
        ) {}
    }
}
