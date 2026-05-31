package com.gogidix.finance.conversion.infrastructure.persistence.mongo;

import com.gogidix.finance.conversion.domain.model.ConversionRate;
import com.gogidix.finance.conversion.domain.repository.ConversionRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Conversion Rate
 * Implements rate cache persistence operations
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoConversionRateRepository implements ConversionRateRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public ConversionRate save(ConversionRate rate) {
        log.debug("Saving rate: {} for pair: {}/{}",
                rate.getRateKey(), rate.getFromCurrency(), rate.getToCurrency());
        return mongoTemplate.save(rate);
    }

    @Override
    public List<ConversionRate> saveAll(List<ConversionRate> rates) {
        return rates.stream()
                .map(mongoTemplate::save)
                .toList();
    }

    @Override
    public Optional<ConversionRate> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, ConversionRate.class));
    }

    @Override
    public Optional<ConversionRate> findByRateKey(String rateKey) {
        Query query = Query.query(Criteria.where("rateKey").is(rateKey));
        return Optional.ofNullable(mongoTemplate.findOne(query, ConversionRate.class));
    }

    @Override
    public Optional<ConversionRate> findByFromCurrencyAndToCurrency(String fromCurrency, String toCurrency) {
        String rateKey = fromCurrency + "-" + toCurrency;
        return findByRateKey(rateKey);
    }

    @Override
    public List<ConversionRate> findByFromCurrency(String fromCurrency) {
        Query query = Query.query(Criteria.where("fromCurrency").is(fromCurrency));
        query.with(Pageable.ofSize(1000));
        return mongoTemplate.find(query, ConversionRate.class);
    }

    @Override
    public List<ConversionRate> findByToCurrency(String toCurrency) {
        Query query = Query.query(Criteria.where("toCurrency").is(toCurrency));
        query.with(Pageable.ofSize(1000));
        return mongoTemplate.find(query, ConversionRate.class);
    }

    @Override
    public List<ConversionRate> findByProvider(String provider) {
        Query query = Query.query(Criteria.where("provider").is(provider));
        query.with(Pageable.ofSize(1000));
        return mongoTemplate.find(query, ConversionRate.class);
    }

    @Override
    public List<ConversionRate> findExpiredRates(Instant now) {
        Query query = Query.query(Criteria.where("expiresAt").lt(now));
        query.with(Pageable.ofSize(1000));
        return mongoTemplate.find(query, ConversionRate.class);
    }

    @Override
    public List<ConversionRate> findRatesExpiringBefore(Instant threshold) {
        Query query = Query.query(Criteria.where("expiresAt").lt(threshold));
        query.with(Pageable.ofSize(1000));
        return mongoTemplate.find(query, ConversionRate.class);
    }

    @Override
    public List<ConversionRate> findAll() {
        return mongoTemplate.findAll(ConversionRate.class);
    }

    @Override
    public boolean existsByRateKey(String rateKey) {
        Query query = Query.query(Criteria.where("rateKey").is(rateKey));
        return mongoTemplate.exists(query, ConversionRate.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), ConversionRate.class);
    }

    @Override
    public void deleteByRateKey(String rateKey) {
        Query query = Query.query(Criteria.where("rateKey").is(rateKey));
        mongoTemplate.remove(query, ConversionRate.class);
    }

    @Override
    public void deleteExpiredRates(Instant before) {
        Query query = Query.query(Criteria.where("expiresAt").lt(before));
        mongoTemplate.remove(query, ConversionRate.class);
    }

    @Override
    public void deleteAll() {
        mongoTemplate.remove(new Query(), ConversionRate.class);
    }

    @Override
    public long count() {
        return mongoTemplate.count(new Query(), ConversionRate.class);
    }

    @Override
    public long countByProvider(String provider) {
        Query query = Query.query(Criteria.where("provider").is(provider));
        return mongoTemplate.count(query, ConversionRate.class);
    }

    @Override
    public List<ConversionRate> findByFromCurrencyAndToCurrencyIn(
            String fromCurrency, List<String> toCurrencies) {
        Query query = Query.query(
                Criteria.where("fromCurrency").is(fromCurrency)
                        .and("toCurrency").in(toCurrencies)
        );
        return mongoTemplate.find(query, ConversionRate.class);
    }

    @Override
    public List<ConversionRate> findRatesNeedingRefresh(Instant threshold) {
        Query query = Query.query(Criteria.where("expiresAt").lt(threshold));
        query.with(Pageable.ofSize(1000));
        return mongoTemplate.find(query, ConversionRate.class);
    }

    @Override
    public void incrementHitCount(String rateKey) {
        Query query = Query.query(Criteria.where("rateKey").is(rateKey));
        Update update = new Update().inc("hitCount", 1);
        mongoTemplate.updateFirst(query, update, ConversionRate.class);
    }

    @Override
    public List<ConversionRate> findAllByCurrencyPair(String fromCurrency, String toCurrency) {
        Query query = Query.query(
                Criteria.where("fromCurrency").is(fromCurrency)
                        .and("toCurrency").is(toCurrency)
        );
        query.with(Pageable.ofSize(100));
        return mongoTemplate.find(query, ConversionRate.class);
    }
}
