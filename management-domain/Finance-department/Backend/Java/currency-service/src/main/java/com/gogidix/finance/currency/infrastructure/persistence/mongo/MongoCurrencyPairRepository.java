package com.gogidix.finance.currency.infrastructure.persistence.mongo;

import com.gogidix.finance.currency.domain.model.CurrencyPair;
import com.gogidix.finance.currency.domain.repository.CurrencyPairRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Currency Pair
 * Implements currency pair persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoCurrencyPairRepository implements CurrencyPairRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public CurrencyPair save(CurrencyPair pair) {
        log.debug("Saving currency pair: {} for tenant: {}", pair.getPair(), "tenant");
        // CurrencyPair is a value object, so we need a document wrapper
        // For now, we'll store it as a document
        return mongoTemplate.save(pair);
    }

    @Override
    public Optional<CurrencyPair> findByIdAndTenantId(String id, String tenantId) {
        Query query = Query.query(
            Criteria.where("id").is(id)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, CurrencyPair.class));
    }

    @Override
    public Optional<CurrencyPair> findByBaseAndQuote(String baseCurrency, String quoteCurrency, String tenantId) {
        Query query = Query.query(
            Criteria.where("baseCurrency").is(baseCurrency)
                .and("quoteCurrency").is(quoteCurrency)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, CurrencyPair.class));
    }

    @Override
    public List<CurrencyPair> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, CurrencyPair.class);
    }

    @Override
    public List<CurrencyPair> findByTenantIdAndCurrency(String tenantId, String currencyCode) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .andOperator(
                    new Criteria().orOperator(
                        Criteria.where("baseCurrency").is(currencyCode),
                        Criteria.where("quoteCurrency").is(currencyCode)
                    )
                )
        );
        return mongoTemplate.find(query, CurrencyPair.class);
    }

    @Override
    public boolean existsByBaseAndQuote(String baseCurrency, String quoteCurrency, String tenantId) {
        Query query = Query.query(
            Criteria.where("baseCurrency").is(baseCurrency)
                .and("quoteCurrency").is(quoteCurrency)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, CurrencyPair.class);
    }

    @Override
    public void deleteByIdAndTenantId(String id, String tenantId) {
        Query query = Query.query(
            Criteria.where("id").is(id)
                .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, CurrencyPair.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, CurrencyPair.class);
    }
}
