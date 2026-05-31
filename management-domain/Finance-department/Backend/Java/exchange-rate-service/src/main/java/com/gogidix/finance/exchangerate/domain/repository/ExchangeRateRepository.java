package com.gogidix.finance.exchangerate.domain.repository;

import com.gogidix.finance.exchangerate.domain.model.ExchangeRate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExchangeRateRepository extends MongoRepository<ExchangeRate, String> {

    Optional<ExchangeRate> findFirstByBaseCurrencyAndQuoteCurrencyAndTenantIdOrderByUpdatedAtDesc(
            String baseCurrency, String quoteCurrency, String tenantId);

    Optional<ExchangeRate> findByBaseCurrencyAndQuoteCurrencyAndTenantId(
            String baseCurrency, String quoteCurrency, String tenantId);

    List<ExchangeRate> findByBaseCurrencyAndQuoteCurrencyAndTenantIdAndUpdatedAtBetweenOrderByUpdatedAtDesc(
            String baseCurrency, String quoteCurrency, String tenantId, Instant start, Instant end);

    List<ExchangeRate> findBySourceOrderByUpdatedAtDesc(String source);

    void deleteByUpdatedAtBefore(Instant dateTime);
}
