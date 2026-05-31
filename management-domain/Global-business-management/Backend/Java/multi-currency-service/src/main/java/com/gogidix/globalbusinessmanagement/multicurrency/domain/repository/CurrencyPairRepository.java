package com.gogidix.globalbusinessmanagement.multicurrency.domain.repository;

import com.gogidix.globalbusinessmanagement.multicurrency.domain.model.CurrencyPair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for CurrencyPair domain model.
 */
@Repository
public interface CurrencyPairRepository extends MongoRepository<CurrencyPair, String> {

    Optional<CurrencyPair> findByBaseCurrencyAndQuoteCurrency(String baseCurrency, String quoteCurrency);

    Optional<CurrencyPair> findBySymbol(String symbol);

    List<CurrencyPair> findByBaseCurrency(String baseCurrency);

    List<CurrencyPair> findByQuoteCurrency(String quoteCurrency);

    List<CurrencyPair> findByStatus(CurrencyPair.PairStatus status);

    List<CurrencyPair> findByType(CurrencyPair.PairType type);

    List<CurrencyPair> findByBaseCurrencyAndStatus(String baseCurrency, CurrencyPair.PairStatus status);

    List<CurrencyPair> findByStatusOrderByCurrentRateDesc(CurrencyPair.PairStatus status);

    List<CurrencyPair> findByVolume24hGreaterThanOrderByVolume24hDesc(java.math.BigDecimal minVolume);

    Page<CurrencyPair> findAllByOrderByPopularityRankAsc(Pageable pageable);

    @Query("{ 'baseCurrency': ?0, 'quoteCurrency': { $in: ?1 } }")
    List<CurrencyPair> findByBaseCurrencyAndQuoteCurrencyIn(String baseCurrency, List<String> quoteCurrencies);

    @Query("{ 'quoteCurrency': ?0, 'baseCurrency': { $in: ?1 } }")
    List<CurrencyPair> findByQuoteCurrencyAndBaseCurrencyIn(String quoteCurrency, List<String> baseCurrencies);

    @Query("{ 'isTradable': true, 'status': 'ACTIVE' }")
    List<CurrencyPair> findTradablePairs();

    @Query("{ 'isCrypto': true, 'status': 'ACTIVE' }")
    List<CurrencyPair> findActiveCryptoPairs();

    @Query("{ 'category': ?0, 'status': 'ACTIVE' }")
    List<CurrencyPair> findByCategoryAndStatus(String category);

    boolean existsByBaseCurrencyAndQuoteCurrency(String baseCurrency, String quoteCurrency);

    void deleteByBaseCurrencyAndQuoteCurrency(String baseCurrency, String quoteCurrency);

    @Query("{ 'changePercent24h': { $gt: ?0 } }")
    List<CurrencyPair> findGainers(java.math.BigDecimal minChange);

    @Query("{ 'changePercent24h': { $lt: ?0 } }")
    List<CurrencyPair> findLosers(java.math.BigDecimal maxChange);

    List<CurrencyPair> findByTypeAndStatus(CurrencyPair.PairType type, CurrencyPair.PairStatus status);
}
