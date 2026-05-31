package com.gogidix.finance.currency.infrastructure.persistence.mongo;

import com.gogidix.finance.currency.domain.model.Currency;
import com.gogidix.finance.currency.domain.repository.CurrencyRepository;
import com.gogidix.finance.currency.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Currency
 * Implements currency persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoCurrencyRepository implements CurrencyRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Currency save(Currency currency) {
        log.debug("Saving currency: {} for tenant: {}", currency.getCurrencyCode(), currency.getTenantId());
        return mongoTemplate.save(currency);
    }

    @Override
    public Optional<Currency> findByIdAndTenantId(String id, String tenantId) {
        Query query = Query.query(
            Criteria.where("id").is(id)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Currency.class));
    }

    @Override
    public Optional<Currency> findByCurrencyCodeAndTenantId(String currencyCode, String tenantId) {
        Query query = Query.query(
            Criteria.where("currencyCode").is(currencyCode)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Currency.class));
    }

    @Override
    public List<Currency> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, Currency.class);
    }

    @Override
    public List<Currency> findByTenantIdAndStatus(String tenantId, Currency.CurrencyStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.find(query, Currency.class);
    }

    @Override
    public List<Currency> findByTenantIdAndActive(String tenantId, boolean active) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(active ? Currency.CurrencyStatus.ACTIVE : Currency.CurrencyStatus.INACTIVE)
        );
        return mongoTemplate.find(query, Currency.class);
    }

    @Override
    public boolean existsByCurrencyCodeAndTenantId(String currencyCode, String tenantId) {
        Query query = Query.query(
            Criteria.where("currencyCode").is(currencyCode)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, Currency.class);
    }

    @Override
    public void deleteByIdAndTenantId(String id, String tenantId) {
        Query query = Query.query(
            Criteria.where("id").is(id)
                .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, Currency.class);
    }

    @Override
    public List<Currency> findAll() {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, Currency.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, Currency.class);
    }

    @Override
    public List<Currency> findByTenantIdAndCountryCodesContaining(String tenantId, String countryCode) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("countryCodes").in(countryCode)
        );
        return mongoTemplate.find(query, Currency.class);
    }
}
