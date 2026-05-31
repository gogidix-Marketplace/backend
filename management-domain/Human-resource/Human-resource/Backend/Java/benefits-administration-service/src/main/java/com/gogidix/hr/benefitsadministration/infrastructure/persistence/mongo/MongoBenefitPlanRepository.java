package com.gogidix.hr.benefitsadministration.infrastructure.persistence.mongo;

import com.gogidix.hr.benefitsadministration.domain.enums.BenefitStatus;
import com.gogidix.hr.benefitsadministration.domain.enums.BenefitType;
import com.gogidix.hr.benefitsadministration.domain.model.BenefitPlan;
import com.gogidix.hr.benefitsadministration.domain.port.out.BenefitPlanRepositoryPort;
import com.gogidix.hr.benefitsadministration.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB repository implementation for BenefitPlan
 * Implements the hexagonal architecture output port
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoBenefitPlanRepository implements BenefitPlanRepositoryPort {

    private final MongoTemplate mongoTemplate;

    @Override
    public BenefitPlan save(BenefitPlan plan) {
        return mongoTemplate.save(plan);
    }

    @Override
    public Optional<BenefitPlan> findById(String planId, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(planId).and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, BenefitPlan.class));
    }

    @Override
    public List<BenefitPlan> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, BenefitPlan.class);
    }

    @Override
    public List<BenefitPlan> findActiveByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(BenefitStatus.ACTIVE)
                        .and("isActive").is(true)
        );
        return mongoTemplate.find(query, BenefitPlan.class);
    }

    @Override
    public List<BenefitPlan> findByTenantAndCountry(String tenantId, String countryCode) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("countryCode").is(countryCode)
        );
        return mongoTemplate.find(query, BenefitPlan.class);
    }

    @Override
    public List<BenefitPlan> findByType(String tenantId, BenefitType benefitType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("benefitType").is(benefitType)
        );
        return mongoTemplate.find(query, BenefitPlan.class);
    }

    @Override
    public List<BenefitPlan> findEffectiveOn(String tenantId, LocalDate effectiveDate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("effectiveDate").lte(effectiveDate)
                        .and("expiryDate").gte(effectiveDate)
                        .and("status").is(BenefitStatus.ACTIVE)
        );
        return mongoTemplate.find(query, BenefitPlan.class);
    }

    @Override
    public List<BenefitPlan> findExpiringBefore(String tenantId, LocalDate expiryDate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("expiryDate").lte(expiryDate)
                        .and("status").is(BenefitStatus.ACTIVE)
        );
        return mongoTemplate.find(query, BenefitPlan.class);
    }

    @Override
    public Optional<BenefitPlan> findByCode(String planCode, String tenantId) {
        Query query = Query.query(
                Criteria.where("planCode").is(planCode).and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, BenefitPlan.class));
    }

    @Override
    public void deleteById(String planId, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(planId).and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, BenefitPlan.class);
    }

    @Override
    public boolean existsById(String planId, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(planId).and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, BenefitPlan.class);
    }
}
