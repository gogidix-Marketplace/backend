package com.gogidix.shared.courier.pricing.infrastructure.persistence;

import com.gogidix.shared.courier.pricing.domain.entity.PricingRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Custom query repository implementation for Pricing
 * Provides complex queries beyond standard MongoRepository
 */
@Slf4j
@Repository
public class PricingQueryRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Find active rules by tenant with multiple filters
     */
    public List<PricingRule> findActiveRulesByFilters(
            String tenantId,
            String serviceType,
            String vehicleType,
            String ruleType) {

        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("active").is(true));

        if (serviceType != null) {
            query.addCriteria(Criteria.where("serviceType").is(serviceType));
        }
        if (vehicleType != null) {
            query.addCriteria(Criteria.where("vehicleType").is(vehicleType));
        }
        if (ruleType != null) {
            query.addCriteria(Criteria.where("ruleType").is(ruleType));
        }

        query.with(org.springframework.data.domain.Sort.by(
                org.springframework.data.domain.Sort.Order.asc("priority")));

        return mongoTemplate.find(query, PricingRule.class);
    }

    /**
     * Find rules by priority range with pagination
     */
    public Page<PricingRule> findByPriorityRange(
            String tenantId,
            Integer minPriority,
            Integer maxPriority,
            Pageable pageable) {

        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("priority").gte(minPriority).lte(maxPriority));
        query.with(pageable);

        List<PricingRule> results = mongoTemplate.find(query, PricingRule.class);

        long count = mongoTemplate.count(query, PricingRule.class);

        return new PageImpl<>(results, pageable, count);
    }

    /**
     * Find surge pricing rules
     */
    public List<PricingRule> findSurgeRules(String tenantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("ruleType").is("SURGE"));
        query.addCriteria(Criteria.where("active").is(true));

        return mongoTemplate.find(query, PricingRule.class);
    }

    /**
     * Find discount rules
     */
    public List<PricingRule> findDiscountRules(String tenantId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("tenantId").is(tenantId));
        query.addCriteria(Criteria.where("ruleType").is("DISCOUNT"));
        query.addCriteria(Criteria.where("active").is(true));

        return mongoTemplate.find(query, PricingRule.class);
    }
}
