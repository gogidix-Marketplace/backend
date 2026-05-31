package com.gogidix.globalbusinessmanagement.multicurrency.domain.repository;

import com.gogidix.globalbusinessmanagement.multicurrency.domain.model.ExchangeRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ExchangeRate domain model.
 */
@Repository
public interface ExchangeRateRepository extends MongoRepository<ExchangeRate, String> {

    Optional<ExchangeRate> findFirstByFromCurrencyAndToCurrencyOrderByEffectiveDateDesc(
        String fromCurrency, String toCurrency);

    List<ExchangeRate> findByFromCurrencyAndToCurrencyOrderByEffectiveDateDesc(
        String fromCurrency, String toCurrency);

    List<ExchangeRate> findByCurrencyPairOrderByEffectiveDateDesc(String currencyPair);

    List<ExchangeRate> findByFromCurrencyAndStatusOrderByEffectiveDateDesc(
        String fromCurrency, ExchangeRate.RateStatus status);

    List<ExchangeRate> findByToCurrencyAndStatusOrderByEffectiveDateDesc(
        String toCurrency, ExchangeRate.RateStatus status);

    List<ExchangeRate> findByStatus(ExchangeRate.RateStatus status);

    List<ExchangeRate> findBySource(ExchangeRate.RateSource source);

    List<ExchangeRate> findByEffectiveDateBetween(Instant startDate, Instant endDate);

    Page<ExchangeRate> findAllByOrderByEffectiveDateDesc(Pageable pageable);

    @Query("{ 'fromCurrency': ?0, 'toCurrency': ?1, 'effectiveDate': { $lte: ?2 } }")
    Optional<ExchangeRate> findLatestRateForDate(String fromCurrency, String toCurrency, Instant asOfDate);

    @Query("{ 'fromCurrency': { $in: ?0 }, 'toCurrency': 'USD', 'status': 'ACTIVE' }")
    List<ExchangeRate> findRatesToUSDForCurrencies(List<String> currencies);

    @Query("{ 'fromCurrency': ?0, 'toCurrency': { $in: ?1 }, 'status': 'ACTIVE' }")
    List<ExchangeRate> findRatesFromCurrencyToTargets(String fromCurrency, List<String> targets);

    @Query("{ 'effectiveDate': { $gte: ?0, $lte: ?1 }, 'status': 'ACTIVE' }")
    List<ExchangeRate> findActiveRatesInDateRange(Instant startDate, Instant endDate);

    boolean existsByFromCurrencyAndToCurrencyAndStatus(
        String fromCurrency, String toCurrency, ExchangeRate.RateStatus status);

    void deleteByFromCurrencyAndToCurrency(String fromCurrency, String toCurrency);

    @Query("{ 'volume24h': { $gt: ?0 } }")
    List<ExchangeRate> findHighVolumePairs(BigDecimal minVolume);

    @Query("{ 'volatility': { $gt: ?0 } }")
    List<ExchangeRate> findHighVolatilityPairs(BigDecimal minVolatility);

    List<ExchangeRate> findBySourceAndStatusOrderByEffectiveDateDesc(
        ExchangeRate.RateSource source, ExchangeRate.RateStatus status);
}
