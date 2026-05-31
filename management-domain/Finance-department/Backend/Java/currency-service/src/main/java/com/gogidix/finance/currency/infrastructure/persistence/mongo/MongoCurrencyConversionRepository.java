package com.gogidix.finance.currency.infrastructure.persistence.mongo;

import com.gogidix.finance.currency.domain.model.CurrencyConversion;
import com.gogidix.finance.currency.domain.repository.CurrencyConversionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * MongoDB Repository Implementation - Currency Conversion
 * Implements currency conversion persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoCurrencyConversionRepository implements CurrencyConversionRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public CurrencyConversion save(CurrencyConversion conversion) {
        log.debug("Saving currency conversion: {} for tenant: {}",
            conversion.getConversionId(), conversion.getTenantId());
        return mongoTemplate.save(conversion);
    }

    @Override
    public Optional<CurrencyConversion> findByIdAndTenantId(String id, String tenantId) {
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
        return mongoTemplate.find(query, CurrencyConversion.class);
    }

    @Override
    public List<CurrencyConversion> findByTenantIdAndStatus(String tenantId, CurrencyConversion.ConversionStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
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
        return mongoTemplate.find(query, CurrencyConversion.class);
    }

    @Override
    public List<CurrencyConversion> findByTenantIdAndConvertedAtBetween(
        String tenantId, Instant startDate, Instant endDate) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("convertedAt").gte(startDate).lte(endDate)
        );
        return mongoTemplate.find(query, CurrencyConversion.class);
    }

    @Override
    public List<CurrencyConversion> findByReferenceIdAndTenantId(String referenceId, String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("referenceId").is(referenceId)
        );
        return mongoTemplate.find(query, CurrencyConversion.class);
    }

    @Override
    public List<CurrencyConversion> findByTenantIdOrderByConvertedAtDesc(
        String tenantId, int page, int size) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId))
            .with(PageRequest.of(page, size))
            .with(org.springframework.data.domain.Sort.by(
                org.springframework.data.domain.Sort.Order.desc("convertedAt")));
        return mongoTemplate.find(query, CurrencyConversion.class);
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
    public BigDecimal sumConvertedAmountByTenantAndCurrencies(
        String tenantId, String fromCurrency, String toCurrency) {
        Aggregation aggregation = newAggregation(
            match(Criteria.where("tenantId").is(tenantId)
                .and("fromCurrency").is(fromCurrency)
                .and("toCurrency").is(toCurrency)
                .and("status").is(CurrencyConversion.ConversionStatus.COMPLETED)),
            group()
                .sum("toAmount").as("total"),
            project("total").andExclude("_id")
        );

        AggregationResults<AmountSumResult> results = mongoTemplate.aggregate(
            aggregation, "currency_conversions", AmountSumResult.class);

        return results.getUniqueMappedResult() != null ?
            results.getUniqueMappedResult().total : BigDecimal.ZERO;
    }

    private record AmountSumResult(BigDecimal total) {}
}
