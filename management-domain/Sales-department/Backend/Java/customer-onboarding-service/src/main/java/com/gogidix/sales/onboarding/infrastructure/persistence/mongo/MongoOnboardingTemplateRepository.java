package com.gogidix.sales.onboarding.infrastructure.persistence.mongo;

import com.gogidix.sales.onboarding.domain.model.OnboardingTemplate;
import com.gogidix.sales.onboarding.domain.repository.OnboardingTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - OnboardingTemplate
 * Implements template persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoOnboardingTemplateRepository implements OnboardingTemplateRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public OnboardingTemplate save(OnboardingTemplate template) {
        log.debug("Saving template: {} for tenant: {}",
                template.getTemplateId(), template.getTenantId());
        return mongoTemplate.save(template);
    }

    @Override
    public List<OnboardingTemplate> saveAll(List<OnboardingTemplate> templates) {
        return templates.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<OnboardingTemplate> findById(String id) {
        String tenantId = com.gogidix.sales.onboarding.shared.requestcontext.RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, OnboardingTemplate.class));
    }

    @Override
    public Optional<OnboardingTemplate> findByTemplateIdAndTenantId(String templateId, String tenantId) {
        Query query = Query.query(
                Criteria.where("templateId").is(templateId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, OnboardingTemplate.class));
    }

    @Override
    public List<OnboardingTemplate> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, OnboardingTemplate.class);
    }

    @Override
    public List<OnboardingTemplate> findByTenantIdAndCustomerType(String tenantId,
                                                                    com.gogidix.sales.onboarding.domain.valueobject.CustomerType customerType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("customerType").is(customerType)
        );
        return mongoTemplate.find(query, OnboardingTemplate.class);
    }

    @Override
    public List<OnboardingTemplate> findByTenantIdAndActive(String tenantId, Boolean active) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("active").is(active)
        );
        return mongoTemplate.find(query, OnboardingTemplate.class);
    }

    @Override
    public List<OnboardingTemplate> findLatestVersionsByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId))
                .with(Sort.by(Sort.Direction.DESC, "version"));
        return mongoTemplate.find(query, OnboardingTemplate.class);
    }

    @Override
    public Optional<OnboardingTemplate> findDefaultTemplateForCustomerType(String tenantId,
                                                                            com.gogidix.sales.onboarding.domain.valueobject.CustomerType customerType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("customerType").is(customerType)
                        .and("active").is(true)
        ).with(Sort.by(Sort.Direction.DESC, "version")).limit(1);
        return Optional.ofNullable(mongoTemplate.findOne(query, OnboardingTemplate.class));
    }

    @Override
    public boolean existsByTemplateIdAndTenantId(String templateId, String tenantId) {
        Query query = Query.query(
                Criteria.where("templateId").is(templateId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, OnboardingTemplate.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), OnboardingTemplate.class);
    }

    @Override
    public void deleteByTemplateIdAndTenantId(String templateId, String tenantId) {
        Query query = Query.query(
                Criteria.where("templateId").is(templateId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, OnboardingTemplate.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, OnboardingTemplate.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, OnboardingTemplate.class);
    }

    @Override
    public long countByTenantIdAndActive(String tenantId, Boolean active) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("active").is(active)
        );
        return mongoTemplate.count(query, OnboardingTemplate.class);
    }
}
