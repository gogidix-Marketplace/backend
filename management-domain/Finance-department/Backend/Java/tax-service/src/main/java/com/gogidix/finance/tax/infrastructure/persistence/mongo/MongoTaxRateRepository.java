package com.gogidix.finance.tax.infrastructure.persistence.mongo;

import com.gogidix.finance.tax.domain.model.TaxRate;
import com.gogidix.finance.tax.domain.repository.TaxRateRepository;
import com.gogidix.finance.tax.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Tax Rate
 * Implements tax rate persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoTaxRateRepository implements TaxRateRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public TaxRate save(TaxRate taxRate) {
        log.debug("Saving tax rate: {} for tenant: {}",
            taxRate.getTaxRateId(), taxRate.getTenantId());
        return mongoTemplate.save(taxRate);
    }

    @Override
    public List<TaxRate> saveAll(List<TaxRate> taxRates) {
        List<TaxRate> result = new ArrayList<>();
        for (TaxRate taxRate : taxRates) {
            result.add(mongoTemplate.save(taxRate));
        }
        return result;
    }

    @Override
    public Optional<TaxRate> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
            Criteria.where("id").is(id)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, TaxRate.class));
    }

    @Override
    public Optional<TaxRate> findByTaxRateIdAndTenantId(String taxRateId, String tenantId) {
        Query query = Query.query(
            Criteria.where("taxRateId").is(taxRateId)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, TaxRate.class));
    }

    @Override
    public List<TaxRate> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, TaxRate.class);
    }

    @Override
    public List<TaxRate> findByTenantIdAndJurisdiction(String tenantId, TaxRate.Jurisdiction jurisdiction) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("jurisdiction").is(jurisdiction)
        );
        return mongoTemplate.find(query, TaxRate.class);
    }

    @Override
    public List<TaxRate> findByTenantIdAndTaxType(String tenantId, TaxRate.TaxType taxType) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("taxType").is(taxType)
        );
        return mongoTemplate.find(query, TaxRate.class);
    }

    @Override
    public List<TaxRate> findByTenantIdAndJurisdictionAndTaxType(String tenantId, TaxRate.Jurisdiction jurisdiction,
                                                                  TaxRate.TaxType taxType) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("jurisdiction").is(jurisdiction)
                .and("taxType").is(taxType)
        );
        return mongoTemplate.find(query, TaxRate.class);
    }

    @Override
    public Optional<TaxRate> findEffectiveRateForDate(String tenantId, TaxRate.Jurisdiction jurisdiction,
                                                      TaxRate.TaxType taxType, String taxCode, LocalDate date) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("jurisdiction").is(jurisdiction)
                .and("taxType").is(taxType)
                .and("status").is(TaxRate.TaxRateStatus.ACTIVE)
                .and("effectiveDate").lte(date)
        );

        if (taxCode != null) {
            query.addCriteria(Criteria.where("taxCode").is(taxCode));
        }

        query.with(org.springframework.data.domain.PageRequest.of(
            0, 1,
            org.springframework.data.domain.Sort.by(
                org.springframework.data.domain.Sort.Direction.DESC, "effectiveDate"
            )
        ));

        return Optional.ofNullable(mongoTemplate.findOne(query, TaxRate.class));
    }

    @Override
    public List<TaxRate> findByTenantIdAndEffectiveDateBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("effectiveDate").gte(startDate).lte(endDate)
        );
        return mongoTemplate.find(query, TaxRate.class);
    }

    @Override
    public List<TaxRate> findByTenantIdAndStatus(String tenantId, TaxRate.TaxRateStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.find(query, TaxRate.class);
    }

    @Override
    public List<TaxRate> findByTaxCodeAndTenantId(String taxCode, String tenantId) {
        Query query = Query.query(
            Criteria.where("taxCode").is(taxCode)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, TaxRate.class);
    }

    @Override
    public List<TaxRate> findActiveRatesForJurisdiction(String tenantId, TaxRate.Jurisdiction jurisdiction) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("jurisdiction").is(jurisdiction)
                .and("status").is(TaxRate.TaxRateStatus.ACTIVE)
        );
        return mongoTemplate.find(query, TaxRate.class);
    }

    @Override
    public List<TaxRate> findActiveRatesForJurisdictionAndType(String tenantId, TaxRate.Jurisdiction jurisdiction,
                                                              TaxRate.TaxType taxType) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("jurisdiction").is(jurisdiction)
                .and("taxType").is(taxType)
                .and("status").is(TaxRate.TaxRateStatus.ACTIVE)
        );
        return mongoTemplate.find(query, TaxRate.class);
    }

    @Override
    public List<TaxRate> findExpiringBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("expiryDate").gte(startDate).lte(endDate)
                .and("status").ne(TaxRate.TaxRateStatus.EXPIRED)
        );
        return mongoTemplate.find(query, TaxRate.class);
    }

    @Override
    public boolean existsByTaxRateIdAndTenantId(String taxRateId, String tenantId) {
        Query query = Query.query(
            Criteria.where("taxRateId").is(taxRateId)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, TaxRate.class);
    }

    @Override
    public boolean existsByTaxCodeAndJurisdictionAndTenantId(String taxCode, TaxRate.Jurisdiction jurisdiction,
                                                           String tenantId) {
        Query query = Query.query(
            Criteria.where("taxCode").is(taxCode)
                .and("jurisdiction").is(jurisdiction)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, TaxRate.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), TaxRate.class);
    }

    @Override
    public void deleteByTaxRateIdAndTenantId(String taxRateId, String tenantId) {
        Query query = Query.query(
            Criteria.where("taxRateId").is(taxRateId)
                .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, TaxRate.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, TaxRate.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, TaxRate.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, TaxRate.TaxRateStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.count(query, TaxRate.class);
    }

    @Override
    public List<TaxRate> findByTenantIdAndTaxCode(String tenantId, String taxCode) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("taxCode").is(taxCode)
        );
        return mongoTemplate.find(query, TaxRate.class);
    }

    @Override
    public List<TaxRate> findVersionsByTaxCode(String tenantId, String taxCode) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("taxCode").is(taxCode)
        ).with(org.springframework.data.domain.Sort.by(
            org.springframework.data.domain.Sort.Direction.DESC, "version"
        ));
        return mongoTemplate.find(query, TaxRate.class);
    }
}
