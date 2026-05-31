package com.gogidix.hr.benefitsadministration.infrastructure.persistence.mongo;

import com.gogidix.hr.benefitsadministration.domain.model.BenefitProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB repository implementation for BenefitProvider
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoBenefitProviderRepository {

    private final MongoTemplate mongoTemplate;

    public BenefitProvider save(BenefitProvider provider) {
        return mongoTemplate.save(provider);
    }

    public Optional<BenefitProvider> findById(String providerId, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(providerId).and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, BenefitProvider.class));
    }

    public Optional<BenefitProvider> findByCode(String providerCode, String tenantId) {
        Query query = Query.query(
                Criteria.where("providerCode").is(providerCode).and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, BenefitProvider.class));
    }

    public List<BenefitProvider> findByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("isActive").is(true)
        );
        return mongoTemplate.find(query, BenefitProvider.class);
    }

    public List<BenefitProvider> findActiveByCountry(String tenantId, String countryCode) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("countryCode").is(countryCode)
                        .and("isActive").is(true)
        );
        return mongoTemplate.find(query, BenefitProvider.class);
    }

    public void deleteById(String providerId, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(providerId).and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, BenefitProvider.class);
    }
}
