package com.gogidix.finance.currency.infrastructure.persistence.mongo;

import com.gogidix.finance.currency.domain.model.ExchangeRate;
import com.gogidix.finance.currency.domain.repository.ExchangeRateRepository;
import com.gogidix.finance.currency.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Exchange Rate
 * Implements exchange rate persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoExchangeRateRepository implements ExchangeRateRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public ExchangeRate save(ExchangeRate rate) {
        log.debug("Saving exchange rate: {}/{} for tenant: {}",
            rate.getBaseCurrency(), rate.getQuoteCurrency(), rate.getTenantId());
        return mongoTemplate.save(rate);
    }

    @Override
    public Optional<ExchangeRate> findByIdAndTenantId(String id, String tenantId) {
        Query query = Query.query(
            Criteria.where("id").is(id)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, ExchangeRate.class));
    }

    @Override
    public Optional<ExchangeRate> findByTenantIdAndBaseCurrencyAndQuoteCurrencyAndValidToIsNull(
        String tenantId, String baseCurrency, String quoteCurrency) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("baseCurrency").is(baseCurrency)
                .and("quoteCurrency").is(quoteCurrency)
                .and("validTo").is(null)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, ExchangeRate.class));
    }

    @Override
    public Optional<ExchangeRate> findByTenantIdAndCurrencyPairAndValidAt(
        String tenantId, String baseCurrency, String quoteCurrency, Instant at) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("baseCurrency").is(baseCurrency)
                .and("quoteCurrency").is(quoteCurrency)
                .and("validFrom").lte(at)
                .orOperator(
                    Criteria.where("validTo").is(null),
                    Criteria.where("validTo").gt(at)
                )
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, ExchangeRate.class));
    }

    @Override
    public List<ExchangeRate> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, ExchangeRate.class);
    }

    @Override
    public List<ExchangeRate> findByTenantIdAndCurrency(String tenantId, String currency) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .orOperator(
                    Criteria.where("baseCurrency").is(currency),
                    Criteria.where("quoteCurrency").is(currency)
                )
        );
        return mongoTemplate.find(query, ExchangeRate.class);
    }

    @Override
    public List<ExchangeRate> findByTenantIdAndBaseCurrencyAndQuoteCurrency(
        String tenantId, String baseCurrency, String quoteCurrency) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("baseCurrency").is(baseCurrency)
                .and("quoteCurrency").is(quoteCurrency)
        );
        return mongoTemplate.find(query, ExchangeRate.class);
    }

    @Override
    public List<ExchangeRate> findByTenantIdAndUpdatedAtAfter(String tenantId, Instant timestamp) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("updatedAt").gt(timestamp)
        );
        return mongoTemplate.find(query, ExchangeRate.class);
    }

    @Override
    public List<ExchangeRate> findLatestRatesByTenantId(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("validTo").is(null)
        );
        return mongoTemplate.find(query, ExchangeRate.class);
    }

    @Override
    public void deleteByIdAndTenantId(String id, String tenantId) {
        Query query = Query.query(
            Criteria.where("id").is(id)
                .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, ExchangeRate.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, ExchangeRate.class);
    }

    @Override
    public void invalidateCurrentRates(String tenantId, String baseCurrency, String quoteCurrency) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("baseCurrency").is(baseCurrency)
                .and("quoteCurrency").is(quoteCurrency)
                .and("validTo").is(null)
        );
        Update update = Update.update("validTo", Instant.now());
        mongoTemplate.updateMulti(query, update, ExchangeRate.class);
    }
}
