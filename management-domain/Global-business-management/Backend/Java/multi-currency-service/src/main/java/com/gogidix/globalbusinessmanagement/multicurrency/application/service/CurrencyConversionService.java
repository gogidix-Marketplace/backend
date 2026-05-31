package com.gogidix.globalbusinessmanagement.multicurrency.application.service;

import com.gogidix.globalbusinessmanagement.multicurrency.domain.model.Currency;
import com.gogidix.globalbusinessmanagement.multicurrency.domain.model.ExchangeRate;
import com.gogidix.globalbusinessmanagement.multicurrency.domain.repository.CurrencyRepository;
import com.gogidix.globalbusinessmanagement.multicurrency.domain.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for currency conversion operations.
 * Handles currency conversions with rate caching for performance.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CurrencyConversionService {

    private final ExchangeRateRepository exchangeRateRepository;
    private final CurrencyRepository currencyRepository;

    private static final String CACHE_NAME = "exchangeRates";
    private static final String DEFAULT_CURRENCY = "USD";
    private static final int DEFAULT_DECIMAL_PLACES = 6;

    /**
     * Convert amount from one currency to another.
     *
     * @param amount       the amount to convert
     * @param fromCurrency the source currency code
     * @param toCurrency   the target currency code
     * @return the converted amount
     */
    @Cacheable(value = CACHE_NAME, key = "'convert:' + #amount + ':' + #fromCurrency + ':' + #toCurrency")
    public BigDecimal convert(BigDecimal amount, String fromCurrency, String toCurrency) {
        log.debug("Converting {} {} to {}", amount, fromCurrency, toCurrency);

        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }

        BigDecimal rate = getExchangeRate(fromCurrency, toCurrency);
        return amount.multiply(rate).setScale(DEFAULT_DECIMAL_PLACES, RoundingMode.HALF_UP);
    }

    /**
     * Convert amount from one currency to another as of a specific date.
     *
     * @param amount       the amount to convert
     * @param fromCurrency the source currency code
     * @param toCurrency   the target currency code
     * @param asOfDate     the date for which to get the rate
     * @return the converted amount
     */
    public BigDecimal convertAsOfDate(BigDecimal amount, String fromCurrency, String toCurrency, Instant asOfDate) {
        log.debug("Converting {} {} to {} as of {}", amount, fromCurrency, toCurrency, asOfDate);

        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }

        BigDecimal rate = getExchangeRateAsOfDate(fromCurrency, toCurrency, asOfDate);
        return amount.multiply(rate).setScale(DEFAULT_DECIMAL_PLACES, RoundingMode.HALF_UP);
    }

    /**
     * Get the exchange rate between two currencies.
     *
     * @param fromCurrency the source currency code
     * @param toCurrency   the target currency code
     * @return the exchange rate
     */
    @Cacheable(value = CACHE_NAME, key = "'rate:' + #fromCurrency + ':' + #toCurrency")
    public BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        log.debug("Fetching exchange rate from {} to {}", fromCurrency, toCurrency);

        if (fromCurrency.equals(toCurrency)) {
            return BigDecimal.ONE;
        }

        // Try direct rate
        Optional<ExchangeRate> directRate = exchangeRateRepository
            .findFirstByFromCurrencyAndToCurrencyOrderByEffectiveDateDesc(fromCurrency, toCurrency);

        if (directRate.isPresent() && directRate.get().isActive()) {
            return directRate.get().getRate();
        }

        // Try inverse rate
        Optional<ExchangeRate> inverseRate = exchangeRateRepository
            .findFirstByFromCurrencyAndToCurrencyOrderByEffectiveDateDesc(toCurrency, fromCurrency);

        if (inverseRate.isPresent() && inverseRate.get().isActive()) {
            return inverseRate.get().invertRate();
        }

        // Try cross rate through USD
        if (!fromCurrency.equals(DEFAULT_CURRENCY) && !toCurrency.equals(DEFAULT_CURRENCY)) {
            Optional<ExchangeRate> fromToUsd = exchangeRateRepository
                .findFirstByFromCurrencyAndToCurrencyOrderByEffectiveDateDesc(fromCurrency, DEFAULT_CURRENCY);

            Optional<ExchangeRate> usdToTo = exchangeRateRepository
                .findFirstByFromCurrencyAndToCurrencyOrderByEffectiveDateDesc(DEFAULT_CURRENCY, toCurrency);

            if (fromToUsd.isPresent() && fromToUsd.get().isActive() &&
                usdToTo.isPresent() && usdToTo.isPresent()) {

                BigDecimal fromToUsdRate = fromToUsd.get().getRate();
                BigDecimal usdToToRate = usdToTo.get().getRate();
                return fromToUsdRate.multiply(usdToToRate).setScale(DEFAULT_DECIMAL_PLACES, RoundingMode.HALF_UP);
            }
        }

        // If no rate found, throw exception
        throw new IllegalArgumentException(
            String.format("No exchange rate found from %s to %s", fromCurrency, toCurrency));
    }

    /**
     * Get the exchange rate as of a specific date.
     *
     * @param fromCurrency the source currency code
     * @param toCurrency   the target currency code
     * @param asOfDate     the date for which to get the rate
     * @return the exchange rate
     */
    public BigDecimal getExchangeRateAsOfDate(String fromCurrency, String toCurrency, Instant asOfDate) {
        log.debug("Fetching exchange rate from {} to {} as of {}", fromCurrency, toCurrency, asOfDate);

        if (fromCurrency.equals(toCurrency)) {
            return BigDecimal.ONE;
        }

        Optional<ExchangeRate> rate = exchangeRateRepository.findLatestRateForDate(fromCurrency, toCurrency, asOfDate);

        if (rate.isPresent() && rate.get().isValidForDate(asOfDate)) {
            return rate.get().getRate();
        }

        // Fall back to current rate if no historical rate found
        log.warn("No historical rate found for {} to {} as of {}, using current rate", fromCurrency, toCurrency, asOfDate);
        return getExchangeRate(fromCurrency, toCurrency);
    }

    /**
     * Get exchange rates for a currency against all other currencies.
     *
     * @param baseCurrency the base currency code
     * @return map of currency codes to exchange rates
     */
    @Cacheable(value = CACHE_NAME, key = "'allRates:' + #baseCurrency")
    public Map<String, BigDecimal> getAllExchangeRates(String baseCurrency) {
        log.debug("Fetching all exchange rates for {}", baseCurrency);

        List<ExchangeRate> rates = exchangeRateRepository.findByFromCurrencyAndStatusOrderByEffectiveDateDesc(
            baseCurrency, ExchangeRate.RateStatus.ACTIVE);

        Map<String, BigDecimal> rateMap = rates.stream()
            .filter(ExchangeRate::isActive)
            .collect(Collectors.toMap(
                ExchangeRate::getToCurrency,
                ExchangeRate::getRate,
                (r1, r2) -> r1 // Keep first occurrence if duplicates
            ));

        // Add base currency with rate 1.0
        rateMap.put(baseCurrency, BigDecimal.ONE);

        return rateMap;
    }

    /**
     * Get exchange rates for multiple currency pairs in one call.
     *
     * @param currencyPairs list of currency pairs in format "FROM/TO"
     * @return map of currency pairs to exchange rates
     */
    public Map<String, BigDecimal> getBatchExchangeRates(List<String> currencyPairs) {
        log.debug("Fetching batch exchange rates for {} pairs", currencyPairs.size());

        Map<String, BigDecimal> result = new HashMap<>();

        for (String pair : currencyPairs) {
            String[] currencies = pair.split("/");
            if (currencies.length == 2) {
                try {
                    BigDecimal rate = getExchangeRate(currencies[0], currencies[1]);
                    result.put(pair, rate);
                } catch (IllegalArgumentException e) {
                    log.warn("Could not get rate for {}: {}", pair, e.getMessage());
                    result.put(pair, BigDecimal.ZERO);
                }
            }
        }

        return result;
    }

    /**
     * Convert amount to multiple target currencies.
     *
     * @param amount        the amount to convert
     * @param fromCurrency  the source currency code
     * @param toCurrencies  list of target currency codes
     * @return map of currency codes to converted amounts
     */
    public Map<String, BigDecimal> convertToMultiple(BigDecimal amount, String fromCurrency, List<String> toCurrencies) {
        log.debug("Converting {} {} to {} target currencies", amount, fromCurrency, toCurrencies.size());

        Map<String, BigDecimal> rates = getAllExchangeRates(fromCurrency);

        return toCurrencies.stream()
            .collect(Collectors.toMap(
                currency -> currency,
                currency -> {
                    BigDecimal rate = rates.getOrDefault(currency, BigDecimal.ZERO);
                    return amount.multiply(rate).setScale(DEFAULT_DECIMAL_PLACES, RoundingMode.HALF_UP);
                }
            ));
    }

    /**
     * Create or update an exchange rate.
     *
     * @param exchangeRate the exchange rate to save
     * @return the saved exchange rate
     */
    @Transactional
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public ExchangeRate createOrUpdateExchangeRate(ExchangeRate exchangeRate) {
        log.info("Creating/updating exchange rate from {} to {}", exchangeRate.getFromCurrency(), exchangeRate.getToCurrency());

        exchangeRate.setCurrencyPair(exchangeRate.getFromCurrency() + exchangeRate.getToCurrency());
        exchangeRate.setEffectiveDate(exchangeRate.getEffectiveDate() != null ? exchangeRate.getEffectiveDate() : Instant.now());
        exchangeRate.setInverseRate(exchangeRate.invertRate());

        return exchangeRateRepository.save(exchangeRate);
    }

    /**
     * Get the latest exchange rate entity.
     *
     * @param fromCurrency the source currency code
     * @param toCurrency   the target currency code
     * @return the exchange rate entity
     */
    @Cacheable(value = CACHE_NAME, key = "'entity:' + #fromCurrency + ':' + #toCurrency")
    public ExchangeRate getExchangeRateEntity(String fromCurrency, String toCurrency) {
        return exchangeRateRepository
            .findFirstByFromCurrencyAndToCurrencyOrderByEffectiveDateDesc(fromCurrency, toCurrency)
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("No exchange rate found from %s to %s", fromCurrency, toCurrency)));
    }

    /**
     * Calculate cross rate between two currencies using a base currency.
     *
     * @param fromCurrency   the source currency code
     * @param toCurrency     the target currency code
     * @param baseCurrency   the base currency for cross rate calculation
     * @return the cross exchange rate
     */
    public BigDecimal calculateCrossRate(String fromCurrency, String toCurrency, String baseCurrency) {
        log.debug("Calculating cross rate from {} to {} via {}", fromCurrency, toCurrency, baseCurrency);

        BigDecimal fromToBase = getExchangeRate(fromCurrency, baseCurrency);
        BigDecimal baseToTo = getExchangeRate(baseCurrency, toCurrency);

        return fromToBase.multiply(baseToTo).setScale(DEFAULT_DECIMAL_PLACES, RoundingMode.HALF_UP);
    }

    /**
     * Check if a currency is supported.
     *
     * @param currencyCode the currency code
     * @return true if the currency is supported
     */
    public boolean isCurrencySupported(String currencyCode) {
        return currencyRepository.existsByCode(currencyCode);
    }

    /**
     * Get all supported currencies.
     *
     * @return list of supported currencies
     */
    @Cacheable(value = CACHE_NAME, key = "'supportedCurrencies'")
    public List<Currency> getSupportedCurrencies() {
        return currencyRepository.findByStatus(Currency.CurrencyStatus.ACTIVE);
    }

    /**
     * Get historical exchange rates between two currencies.
     *
     * @param fromCurrency the source currency code
     * @param toCurrency   the target currency code
     * @param limit        maximum number of historical rates to return
     * @return list of historical exchange rates
     */
    public List<ExchangeRate> getHistoricalRates(String fromCurrency, String toCurrency, int limit) {
        log.debug("Fetching historical rates from {} to {} (limit: {})", fromCurrency, toCurrency, limit);

        return exchangeRateRepository
            .findByFromCurrencyAndToCurrencyOrderByEffectiveDateDesc(fromCurrency, toCurrency)
            .stream()
            .limit(limit)
            .toList();
    }

    /**
     * Invalidate cache for a specific currency pair.
     *
     * @param fromCurrency the source currency code
     * @param toCurrency   the target currency code
     */
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void invalidateCacheForPair(String fromCurrency, String toCurrency) {
        log.info("Invalidating cache for currency pair: {}/{}", fromCurrency, toCurrency);
    }

    /**
     * Invalidate all exchange rate caches.
     */
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void invalidateAllCache() {
        log.info("Invalidating all exchange rate caches");
    }
}
