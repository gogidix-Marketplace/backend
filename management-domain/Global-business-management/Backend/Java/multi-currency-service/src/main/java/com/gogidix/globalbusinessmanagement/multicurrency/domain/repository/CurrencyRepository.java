package com.gogidix.globalbusinessmanagement.multicurrency.domain.repository;

import com.gogidix.globalbusinessmanagement.multicurrency.domain.model.Currency;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Currency domain model.
 */
@Repository
public interface CurrencyRepository extends MongoRepository<Currency, String> {

    Optional<Currency> findByCode(String code);

    List<Currency> findByStatus(Currency.CurrencyStatus status);

    List<Currency> findByIsDefaultTrue();

    List<Currency> findByRegion(String region);

    List<Currency> findByIsCrypto(Boolean isCrypto);

    boolean existsByCode(String code);

    @Query("{ 'countries': { $in: [?0] } }")
    List<Currency> findByCountry(String countryCode);

    @Query("{ 'status': 'ACTIVE' }")
    List<Currency> findAllActive();

    @Query("{ 'isCrypto': true, 'status': 'ACTIVE' }")
    List<Currency> findActiveCryptocurrencies();

    @Query("{ 'isCrypto': false, 'status': 'ACTIVE' }")
    List<Currency> findActiveFiatCurrencies();
}
