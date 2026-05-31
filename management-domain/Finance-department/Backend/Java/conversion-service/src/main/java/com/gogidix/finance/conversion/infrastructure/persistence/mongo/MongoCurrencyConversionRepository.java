package com.gogidix.finance.conversion.infrastructure.persistence.mongo;

import com.gogidix.finance.conversion.domain.model.CurrencyConversion;
import com.gogidix.finance.conversion.domain.repository.CurrencyConversionRepository;
import com.gogidix.finance.conversion.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Currency Conversion
 * Implements conversion persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoCurrencyConversionRepository implements CurrencyConversionRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public CurrencyConversion save(CurrencyConversion conversion) {
        log.debug("Saving conversion: {} for tenant: {}",
                conversion.getConversionId(), conversion.getTenantId());
        return mongoTemplate.save(conversion);
    }

    @Override
    public List<CurrencyConversion> saveAll(List<CurrencyConversion> conversions) {
        return conversions.stream()
                .map(mongoTemplate::save)
                .toList();
    }

    @Override
    public Optional<CurrencyConversion> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, CurrencyConversion.class));
    }

    @Override
    public Optional<CurrencyConversion> findByConversionIdAndTenantId(String conversionId, String tenantId) {
        Query query = Query.query(
                Criteria.where("conversionId").is(conversionId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, CurrencyConversion.class));
    }

    @Override
    public List<CurrencyConversion> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        query.with(Pageable.ofSize(1000));
        return mongoTemplate.find(query, CurrencyConversion.class);
    }

    @Override
    public List<CurrencyConversion> findByTenantIdAndRequestedBy(String tenantId, String requestedBy) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("requestedBy").is(requestedBy)
        );
        query.with(Pageable.ofSize(1000));
        return mongoTemplate.find(query, CurrencyConversion.class);
    }

    @Override
    public List<CurrencyConversion> findByTenantIdAndFromCurrencyAndToCurrency(
            String tenantId, String fromCurrency, String toCurrency) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("fromCurrency").is(fromCurrency)
                        .and("toCurrency").is(toCurrency)
        );
        query.with(Pageable.ofSize(1000));
        return mongoTemplate.find(query, CurrencyConversion.class);
    }

    @Override
    public List<CurrencyConversion> findByTenantIdAndConversionDateBetween(
            String tenantId, Instant startDate, Instant endDate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("conversionDate").gte(startDate).lte(endDate)
        );
        query.with(Pageable.ofSize(1000));
        return mongoTemplate.find(query, CurrencyConversion.class);
    }

    @Override
    public List<CurrencyConversion> findByTenantIdAndStatus(
            String tenantId, CurrencyConversion.ConversionStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        query.with(Pageable.ofSize(1000));
        return mongoTemplate.find(query, CurrencyConversion.class);
    }

    @Override
    public List<CurrencyConversion> findByTenantIdAndStatusIn(
            String tenantId, List<CurrencyConversion.ConversionStatus> statuses) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").in(statuses)
        );
        query.with(Pageable.ofSize(1000));
        return mongoTemplate.find(query, CurrencyConversion.class);
    }

    @Override
    public List<CurrencyConversion> findByTenantIdAndProvider(String tenantId, String provider) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("provider").is(provider)
        );
        query.with(Pageable.ofSize(1000));
        return mongoTemplate.find(query, CurrencyConversion.class);
    }

    @Override
    public boolean existsByConversionIdAndTenantId(String conversionId, String tenantId) {
        Query query = Query.query(
                Criteria.where("conversionId").is(conversionId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, CurrencyConversion.class);
    }

    @Override
    public void deleteById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, CurrencyConversion.class);
    }

    @Override
    public void deleteByConversionIdAndTenantId(String conversionId, String tenantId) {
        Query query = Query.query(
                Criteria.where("conversionId").is(conversionId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, CurrencyConversion.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, CurrencyConversion.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, CurrencyConversion.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, CurrencyConversion.ConversionStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.count(query, CurrencyConversion.class);
    }

    @Override
    public BigDecimal sumAmountByTenantIdAndStatus(
            String tenantId, CurrencyConversion.ConversionStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );

        List<CurrencyConversion> conversions = mongoTemplate.find(query, CurrencyConversion.class);
        return conversions.stream()
                .map(CurrencyConversion::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal sumConvertedAmountByTenantIdAndCurrenciesAndDateBetween(
            String tenantId, String fromCurrency, String toCurrency, Instant startDate, Instant endDate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("fromCurrency").is(fromCurrency)
                        .and("toCurrency").is(toCurrency)
                        .and("conversionDate").gte(startDate).lte(endDate)
                        .and("status").is(CurrencyConversion.ConversionStatus.COMPLETED)
        );

        List<CurrencyConversion> conversions = mongoTemplate.find(query, CurrencyConversion.class);
        return conversions.stream()
                .map(CurrencyConversion::getConvertedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<CurrencyConversion> findByTenantIdAndCorrelationId(String tenantId, String correlationId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("correlationId").is(correlationId)
        );
        return mongoTemplate.find(query, CurrencyConversion.class);
    }

    @Override
    public Optional<CurrencyConversion> findLatestByTenantIdAndCurrencies(
            String tenantId, String fromCurrency, String toCurrency) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("fromCurrency").is(fromCurrency)
                        .and("toCurrency").is(toCurrency)
        );
        query.with(org.springframework.data.domain.PageRequest.of(0, 1,
                org.springframework.data.domain.Sort.by(
                        org.springframework.data.domain.Sort.Order.desc("conversionDate"))));
        return Optional.ofNullable(mongoTemplate.findOne(query, CurrencyConversion.class));
    }
}
