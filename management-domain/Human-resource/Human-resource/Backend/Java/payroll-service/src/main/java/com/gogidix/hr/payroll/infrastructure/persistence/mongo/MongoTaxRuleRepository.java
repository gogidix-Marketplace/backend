package com.gogidix.hr.payroll.infrastructure.persistence.mongo;

import com.gogidix.hr.payroll.domain.model.TaxRule;
import com.gogidix.hr.payroll.domain.enums.TaxType;
import com.gogidix.hr.payroll.domain.repository.TaxRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoTaxRuleRepository implements TaxRuleRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public TaxRule save(TaxRule taxRule) {
        return mongoTemplate.save(taxRule);
    }

    @Override
    public List<TaxRule> saveAll(List<TaxRule> taxRules) {
        return taxRules.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<TaxRule> findById(String id) {
        String tenantId = com.gogidix.hr.payroll.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id).and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, TaxRule.class));
    }

    @Override
    public Optional<TaxRule> findByTaxCode(String taxCode) {
        Query query = Query.query(Criteria.where("taxCode").is(taxCode));
        return Optional.ofNullable(mongoTemplate.findOne(query, TaxRule.class));
    }

    @Override
    public List<TaxRule> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, TaxRule.class);
    }

    @Override
    public List<TaxRule> findByTenantIdAndCountryCode(String tenantId, String countryCode) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("countryCode").is(countryCode)
        );
        return mongoTemplate.find(query, TaxRule.class);
    }

    @Override
    public List<TaxRule> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("isActive").is(isActive)
        );
        return mongoTemplate.find(query, TaxRule.class);
    }

    @Override
    public List<TaxRule> findByTaxType(TaxType taxType) {
        Query query = Query.query(Criteria.where("taxType").is(taxType));
        return mongoTemplate.find(query, TaxRule.class);
    }

    @Override
    public List<TaxRule> findByCountryCodeAndIsActive(String countryCode, Boolean isActive) {
        Query query = Query.query(
                Criteria.where("countryCode").is(countryCode).and("isActive").is(isActive)
        );
        return mongoTemplate.find(query, TaxRule.class);
    }

    @Override
    public List<TaxRule> findByJurisdictionLevel(String jurisdictionLevel) {
        Query query = Query.query(Criteria.where("jurisdictionLevel").is(jurisdictionLevel));
        return mongoTemplate.find(query, TaxRule.class);
    }

    @Override
    public List<TaxRule> findEffectiveRules(String countryCode, LocalDate date) {
        Query query = Query.query(
                Criteria.where("countryCode").is(countryCode)
                        .and("isActive").is(true)
                        .and("effectiveDate").lte(date)
                        .orOperator(
                                Criteria.where("expiryDate").exists(false),
                                Criteria.where("expiryDate").gt(date)
                        )
        );
        return mongoTemplate.find(query, TaxRule.class);
    }

    @Override
    public List<TaxRule> findApplicableRules(String tenantId, String countryCode, LocalDate date) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("countryCode").is(countryCode)
                        .and("isActive").is(true)
                        .and("effectiveDate").lte(date)
                        .orOperator(
                                Criteria.where("expiryDate").exists(false),
                                Criteria.where("expiryDate").gt(date)
                        )
        );
        return mongoTemplate.find(query, TaxRule.class);
    }

    @Override
    public List<TaxRule> findByStateCode(String stateCode) {
        Query query = Query.query(Criteria.where("stateCode").is(stateCode));
        return mongoTemplate.find(query, TaxRule.class);
    }

    @Override
    public List<TaxRule> findFederalTaxes(String countryCode) {
        Query query = Query.query(
                Criteria.where("countryCode").is(countryCode)
                        .and("jurisdictionLevel").is("FEDERAL")
                        .and("isActive").is(true)
        );
        return mongoTemplate.find(query, TaxRule.class);
    }

    @Override
    public List<TaxRule> findStateTaxes(String countryCode, String stateCode) {
        Query query = Query.query(
                Criteria.where("countryCode").is(countryCode)
                        .and("stateCode").is(stateCode)
                        .and("jurisdictionLevel").is("STATE")
                        .and("isActive").is(true)
        );
        return mongoTemplate.find(query, TaxRule.class);
    }

    @Override
    public List<TaxRule> findLocalTaxes(String countryCode, String stateCode, String localCode) {
        Query query = Query.query(
                Criteria.where("countryCode").is(countryCode)
                        .and("stateCode").is(stateCode)
                        .and("localCode").is(localCode)
                        .and("jurisdictionLevel").is("LOCAL")
                        .and("isActive").is(true)
        );
        return mongoTemplate.find(query, TaxRule.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), TaxRule.class);
    }

    @Override
    public List<TaxRule> findAllActive() {
        Query query = Query.query(Criteria.where("isActive").is(true));
        return mongoTemplate.find(query, TaxRule.class);
    }

    @Override
    public boolean existsByTaxCode(String taxCode) {
        Query query = Query.query(Criteria.where("taxCode").is(taxCode));
        return mongoTemplate.exists(query, TaxRule.class);
    }
}
