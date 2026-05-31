package com.gogidix.sales.onboarding.infrastructure.persistence.mongo;

import com.gogidix.sales.onboarding.domain.model.Onboarding;
import com.gogidix.sales.onboarding.domain.repository.OnboardingRepository;
import com.gogidix.sales.onboarding.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Onboarding
 * Implements onboarding persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoOnboardingRepository implements OnboardingRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Onboarding save(Onboarding onboarding) {
        log.debug("Saving onboarding: {} for tenant: {}",
                onboarding.getOnboardingId(), onboarding.getTenantId());
        return mongoTemplate.save(onboarding);
    }

    @Override
    public List<Onboarding> saveAll(List<Onboarding> onboardings) {
        return onboardings.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<Onboarding> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Onboarding.class));
    }

    @Override
    public Optional<Onboarding> findByOnboardingIdAndTenantId(String onboardingId, String tenantId) {
        Query query = Query.query(
                Criteria.where("onboardingId").is(onboardingId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Onboarding.class));
    }

    @Override
    public List<Onboarding> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, Onboarding.class);
    }

    @Override
    public List<Onboarding> findByTenantIdAndStatus(String tenantId,
                                                      com.gogidix.sales.onboarding.domain.valueobject.OnboardingStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.find(query, Onboarding.class);
    }

    @Override
    public List<Onboarding> findByTenantIdAndCustomerId(String tenantId, String customerId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("customerId").is(customerId)
        );
        return mongoTemplate.find(query, Onboarding.class);
    }

    @Override
    public List<Onboarding> findByTenantIdAndAssignedTo(String tenantId, String assignedTo) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("assignedTo").is(assignedTo)
        );
        return mongoTemplate.find(query, Onboarding.class);
    }

    @Override
    public List<Onboarding> findByTenantIdAndCustomerType(String tenantId,
                                                            com.gogidix.sales.onboarding.domain.valueobject.CustomerType customerType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("customerType").is(customerType)
        );
        return mongoTemplate.find(query, Onboarding.class);
    }

    @Override
    public List<Onboarding> findByTenantIdAndDateRange(String tenantId, LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("createdAt").gte(start).lte(end)
        );
        return mongoTemplate.find(query, Onboarding.class);
    }

    @Override
    public List<Onboarding> findPendingReview(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(com.gogidix.sales.onboarding.domain.valueobject.OnboardingStatus.PENDING_REVIEW)
        );
        return mongoTemplate.find(query, Onboarding.class);
    }

    @Override
    public List<Onboarding> findInProgress(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(com.gogidix.sales.onboarding.domain.valueobject.OnboardingStatus.IN_PROGRESS)
        );
        return mongoTemplate.find(query, Onboarding.class);
    }

    @Override
    public List<Onboarding> findOverdue(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(com.gogidix.sales.onboarding.domain.valueobject.OnboardingStatus.IN_PROGRESS)
                        .and("estimatedCompletionDate").lt(Instant.now())
        );
        return mongoTemplate.find(query, Onboarding.class);
    }

    @Override
    public List<Onboarding> findByTenantIdAndPriority(String tenantId, String priority) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("priority").is(priority)
        );
        return mongoTemplate.find(query, Onboarding.class);
    }

    @Override
    public List<Onboarding> findByTenantIdAndInitiatedBy(String tenantId, String initiatedBy) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("initiatedBy").is(initiatedBy)
        );
        return mongoTemplate.find(query, Onboarding.class);
    }

    @Override
    public boolean existsByOnboardingIdAndTenantId(String onboardingId, String tenantId) {
        Query query = Query.query(
                Criteria.where("onboardingId").is(onboardingId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, Onboarding.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Onboarding.class);
    }

    @Override
    public void deleteByOnboardingIdAndTenantId(String onboardingId, String tenantId) {
        Query query = Query.query(
                Criteria.where("onboardingId").is(onboardingId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, Onboarding.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, Onboarding.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, Onboarding.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId,
                                          com.gogidix.sales.onboarding.domain.valueobject.OnboardingStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.count(query, Onboarding.class);
    }

    @Override
    public List<Onboarding> findByTemplateId(String tenantId, String templateId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("templateId").is(templateId)
        );
        return mongoTemplate.find(query, Onboarding.class);
    }
}
