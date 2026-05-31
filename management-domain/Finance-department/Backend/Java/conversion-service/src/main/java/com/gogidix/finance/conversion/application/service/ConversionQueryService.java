package com.gogidix.finance.conversion.application.service;

import com.gogidix.finance.conversion.domain.model.ConversionRate;
import com.gogidix.finance.conversion.domain.model.CurrencyConversion;
import com.gogidix.finance.conversion.domain.port.in.ConversionQuery;
import com.gogidix.finance.conversion.domain.port.out.ExchangeRateProvider;
import com.gogidix.finance.conversion.domain.repository.ConversionRateRepository;
import com.gogidix.finance.conversion.domain.repository.CurrencyConversionRepository;
import com.gogidix.finance.conversion.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Conversion Query Service
 * Handles all read operations for currency conversions and rates
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ConversionQueryService {

    private final CurrencyConversionRepository conversionRepository;
    private final ConversionRateRepository rateRepository;
    private final ExchangeRateProvider exchangeRateProvider;

    private static final List<String> SUPPORTED_CURRENCIES = List.of(
            "USD", "EUR", "GBP", "JPY", "CHF", "CAD", "AUD", "NZD",
            "CNY", "HKD", "SGD", "KRW", "INR", "BRL", "MXN", "ZAR",
            "RUB", "TRY", "SEK", "NOK", "DKK", "PLN", "THB", "MYR",
            "IDR", "PHP", "VND", "AED", "SAR", "EGP", "NGN", "KES"
    );

    /**
     * Gets a conversion by ID
     */
    public CurrencyConversion getConversionById(String conversionId) {
        String tenantId = com.gogidix.finance.conversion.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching conversion: {} for tenant: {}", conversionId, tenantId);

        return conversionRepository.findByConversionIdAndTenantId(conversionId, tenantId)
                .orElseThrow(() -> new NotFoundException("Conversion", conversionId));
    }

    /**
     * Gets conversions by user
     */
    public Page<CurrencyConversion> getConversionsByUser(String requestedBy,
                                                          int page, int size,
                                                          String sortBy, String sortDirection) {
        String tenantId = com.gogidix.finance.conversion.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching conversions for user: {} in tenant: {}", requestedBy, tenantId);

        List<CurrencyConversion> conversions = conversionRepository
                .findByTenantIdAndRequestedBy(tenantId, requestedBy);

        // Sort the results
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Comparator<CurrencyConversion> comparator = getComparator(sortBy, direction);
        conversions.sort(comparator);

        // Paginate
        int start = page * size;
        int end = Math.min(start + size, conversions.size());
        List<CurrencyConversion> pagedContent = start < conversions.size()
                ? conversions.subList(start, end)
                : List.of();

        return new PageImpl<>(pagedContent, PageRequest.of(page, size), conversions.size());
    }

    /**
     * Gets conversions by date range
     */
    public List<CurrencyConversion> getConversionsByDateRange(Instant startDate, Instant endDate,
                                                               String fromCurrency, String toCurrency) {
        String tenantId = com.gogidix.finance.conversion.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching conversions between {} and {} for tenant: {}", startDate, endDate, tenantId);

        List<CurrencyConversion> conversions = conversionRepository
                .findByTenantIdAndConversionDateBetween(tenantId, startDate, endDate);

        // Filter by currencies if specified
        if (fromCurrency != null && !fromCurrency.isBlank()) {
            conversions = conversions.stream()
                    .filter(c -> c.getFromCurrency().equals(fromCurrency))
                    .toList();
        }
        if (toCurrency != null && !toCurrency.isBlank()) {
            conversions = conversions.stream()
                    .filter(c -> c.getToCurrency().equals(toCurrency))
                    .toList();
        }

        return conversions;
    }

    /**
     * Gets conversions by currency pair
     */
    public Page<CurrencyConversion> getConversionsByCurrencies(String fromCurrency, String toCurrency,
                                                                int page, int size) {
        String tenantId = com.gogidix.finance.conversion.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching conversions for pair: {}/{} in tenant: {}", fromCurrency, toCurrency, tenantId);

        List<CurrencyConversion> conversions = conversionRepository
                .findByTenantIdAndFromCurrencyAndToCurrency(tenantId, fromCurrency, toCurrency);

        // Sort by conversion date descending
        conversions.sort(Comparator.comparing(CurrencyConversion::getConversionDate).reversed());

        // Paginate
        int start = page * size;
        int end = Math.min(start + size, conversions.size());
        List<CurrencyConversion> pagedContent = start < conversions.size()
                ? conversions.subList(start, end)
                : List.of();

        return new PageImpl<>(pagedContent, PageRequest.of(page, size), conversions.size());
    }

    /**
     * Gets the current exchange rate
     */
    @Cacheable(value = "exchangeRates", key = "#fromCurrency + '-' + #toCurrency")
    public ConversionRate getConversionRate(String fromCurrency, String toCurrency, Boolean forceRefresh) {
        log.debug("Fetching rate: {} to {}", fromCurrency, toCurrency);

        if (!isValidCurrency(fromCurrency) || !isValidCurrency(toCurrency)) {
            throw new IllegalArgumentException("Invalid currency code");
        }

        String rateKey = fromCurrency + "-" + toCurrency;

        if (forceRefresh == null || !forceRefresh) {
            Optional<ConversionRate> cachedRate = rateRepository.findByRateKey(rateKey);
            if (cachedRate.isPresent() && cachedRate.get().isValid()) {
                log.debug("Returning cached rate for: {}", rateKey);
                return cachedRate.get();
            }
        }

        // Fetch from provider
        if (!exchangeRateProvider.isAvailable()) {
            throw new IllegalStateException("Exchange rate provider is not available");
        }

        BigDecimal rate = exchangeRateProvider.getExchangeRate(fromCurrency, toCurrency);
        String provider = exchangeRateProvider.getProviderName();

        ConversionRate conversionRate = ConversionRate.create(fromCurrency, toCurrency, rate, provider);
        return rateRepository.save(conversionRate);
    }

    /**
     * Calculates conversion without creating a record
     */
    public CalculationResult calculateConversion(BigDecimal amount, String fromCurrency, String toCurrency) {
        log.debug("Calculating conversion: {} {} to {}", amount, fromCurrency, toCurrency);

        if (!isValidCurrency(fromCurrency) || !isValidCurrency(toCurrency)) {
            throw new IllegalArgumentException("Invalid currency code");
        }

        if (fromCurrency.equals(toCurrency)) {
            return new CalculationResult(amount, BigDecimal.ONE, amount, BigDecimal.ZERO, amount);
        }

        ConversionRate rate = getConversionRate(fromCurrency, toCurrency, false);
        BigDecimal convertedAmount = amount.multiply(rate.getEffectiveRate())
                .setScale(2, RoundingMode.HALF_UP);

        // Calculate fee (1% default, min 0.50)
        BigDecimal feePercentage = new BigDecimal("0.01");
        BigDecimal fee = convertedAmount.multiply(feePercentage).setScale(2, RoundingMode.HALF_UP);
        if (fee.compareTo(new BigDecimal("0.50")) < 0) {
            fee = new BigDecimal("0.50");
        }

        BigDecimal totalAmount = convertedAmount.add(fee);

        return new CalculationResult(
                amount,
                rate.getEffectiveRate(),
                convertedAmount,
                fee,
                totalAmount
        );
    }

    /**
     * Gets conversion statistics
     */
    public ConversionStatistics getStatistics(Instant startDate, Instant endDate,
                                               String fromCurrency, String toCurrency) {
        String tenantId = com.gogidix.finance.conversion.shared.requestcontext.RequestContextHolder.getTenantId();

        log.debug("Fetching statistics for tenant: {} between {} and {}", tenantId, startDate, endDate);

        List<CurrencyConversion> conversions = conversionRepository
                .findByTenantIdAndConversionDateBetween(tenantId, startDate, endDate);

        // Filter by currencies if specified
        if (fromCurrency != null && !fromCurrency.isBlank()) {
            conversions = conversions.stream()
                    .filter(c -> c.getFromCurrency().equals(fromCurrency))
                    .toList();
        }
        if (toCurrency != null && !toCurrency.isBlank()) {
            conversions = conversions.stream()
                    .filter(c -> c.getToCurrency().equals(toCurrency))
                    .toList();
        }

        long totalCount = conversions.size();
        long completedCount = conversions.stream()
                .filter(c -> c.getStatus() == CurrencyConversion.ConversionStatus.COMPLETED)
                .count();
        long failedCount = conversions.stream()
                .filter(c -> c.getStatus() == CurrencyConversion.ConversionStatus.FAILED)
                .count();

        BigDecimal totalAmount = conversions.stream()
                .filter(c -> c.getStatus() == CurrencyConversion.ConversionStatus.COMPLETED)
                .map(CurrencyConversion::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalConvertedAmount = conversions.stream()
                .filter(c -> c.getStatus() == CurrencyConversion.ConversionStatus.COMPLETED)
                .map(CurrencyConversion::getConvertedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ConversionStatistics(totalCount, completedCount, failedCount,
                totalAmount, totalConvertedAmount);
    }

    /**
     * Gets supported currencies
     */
    public List<String> getSupportedCurrencies() {
        return SUPPORTED_CURRENCIES;
    }

    /**
     * Gets multiple rates at once
     */
    public List<ConversionRate> getBatchRates(String baseCurrency, List<String> targetCurrencies) {
        log.debug("Fetching batch rates for base: {}", baseCurrency);

        if (!isValidCurrency(baseCurrency)) {
            throw new IllegalArgumentException("Invalid base currency code");
        }

        // Validate all target currencies
        for (String currency : targetCurrencies) {
            if (!isValidCurrency(currency)) {
                throw new IllegalArgumentException("Invalid currency code: " + currency);
            }
        }

        // Try to fetch from provider (if supported)
        if (exchangeRateProvider.isAvailable()) {
            try {
                var rates = exchangeRateProvider.getExchangeRates(baseCurrency, targetCurrencies);
                List<ConversionRate> conversionRates = new ArrayList<>();
                String provider = exchangeRateProvider.getProviderName();

                for (var entry : rates.entrySet()) {
                    String[] parts = entry.getKey().split("-");
                    if (parts.length == 2) {
                        ConversionRate rate = ConversionRate.create(parts[0], parts[1], entry.getValue(), provider);
                        conversionRates.add(rateRepository.save(rate));
                    }
                }

                if (!conversionRates.isEmpty()) {
                    return conversionRates;
                }
            } catch (Exception e) {
                log.warn("Batch rate fetch failed, falling back to individual requests", e);
            }
        }

        // Fallback to individual requests
        List<ConversionRate> rates = new ArrayList<>();
        for (String targetCurrency : targetCurrencies) {
            if (!targetCurrency.equals(baseCurrency)) {
                try {
                    ConversionRate rate = getConversionRate(baseCurrency, targetCurrency, false);
                    rates.add(rate);
                } catch (Exception e) {
                    log.error("Failed to get rate for: {}/{}", baseCurrency, targetCurrency, e);
                }
            }
        }

        return rates;
    }

    /**
     * Validates a currency code
     */
    private boolean isValidCurrency(String currencyCode) {
        return currencyCode != null
                && currencyCode.length() == 3
                && SUPPORTED_CURRENCIES.contains(currencyCode.toUpperCase());
    }

    /**
     * Gets a comparator for sorting conversions
     */
    private Comparator<CurrencyConversion> getComparator(String sortBy, Sort.Direction direction) {
        Comparator<CurrencyConversion> comparator;
        switch (sortBy != null ? sortBy.toLowerCase() : "conversionDate") {
            case "amount" -> comparator = Comparator.comparing(CurrencyConversion::getAmount);
            case "fromcurrency" -> comparator = Comparator.comparing(CurrencyConversion::getFromCurrency);
            case "tocurrency" -> comparator = Comparator.comparing(CurrencyConversion::getToCurrency);
            case "status" -> comparator = Comparator.comparing(CurrencyConversion::getStatus);
            default -> comparator = Comparator.comparing(CurrencyConversion::getConversionDate);
        }

        return direction == Sort.Direction.DESC ? comparator.reversed() : comparator;
    }

    /**
     * Calculation result holder
     */
    public record CalculationResult(
            BigDecimal originalAmount,
            BigDecimal rate,
            BigDecimal convertedAmount,
            BigDecimal fee,
            BigDecimal totalAmount
    ) {}

    /**
     * Conversion statistics holder
     */
    public record ConversionStatistics(
            long totalCount,
            long completedCount,
            long failedCount,
            BigDecimal totalAmount,
            BigDecimal totalConvertedAmount
    ) {
        public BigDecimal getSuccessRate() {
            if (totalCount == 0) return BigDecimal.ZERO;
            return BigDecimal.valueOf((double) completedCount / totalCount)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);
        }
    }
}
