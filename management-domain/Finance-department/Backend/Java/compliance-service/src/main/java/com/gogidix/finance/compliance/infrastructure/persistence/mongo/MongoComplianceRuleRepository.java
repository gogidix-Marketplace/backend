package com.gogidix.finance.compliance.infrastructure.persistence.mongo;

import com.gogidix.finance.compliance.domain.model.ComplianceRule;
import com.gogidix.finance.compliance.domain.repository.ComplianceRuleRepository;
import com.gogidix.finance.compliance.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Compliance Rule
 * Implements compliance rule persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoComplianceRuleRepository implements ComplianceRuleRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public ComplianceRule save(ComplianceRule rule) {
        log.debug("Saving compliance rule: {} for tenant: {}",
            rule.getRuleId(), rule.getTenantId());
        return mongoTemplate.save(rule);
    }

    @Override
    public List<ComplianceRule> saveAll(List<ComplianceRule> rules) {
        List<ComplianceRule> result = new java.util.ArrayList<>();
        for (ComplianceRule rule : rules) {
            result.add(mongoTemplate.save(rule));
        }
        return result;
    }

    @Override
    public Optional<ComplianceRule> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
            Criteria.where("id").is(id)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, ComplianceRule.class));
    }

    @Override
    public Optional<ComplianceRule> findByRuleIdAndTenantId(String ruleId, String tenantId) {
        Query query = Query.query(
            Criteria.where("ruleId").is(ruleId)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, ComplianceRule.class));
    }

    @Override
    public List<ComplianceRule> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, ComplianceRule.class);
    }

    @Override
    public List<ComplianceRule> findByTenantIdAndStatus(String tenantId, ComplianceRule.RuleStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.find(query, ComplianceRule.class);
    }

    @Override
    public List<ComplianceRule> findByTenantIdAndEnabled(String tenantId, Boolean enabled) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("enabled").is(enabled)
        );
        return mongoTemplate.find(query, ComplianceRule.class);
    }

    @Override
    public List<ComplianceRule> findByTenantIdAndRuleType(String tenantId, ComplianceRule.RuleType ruleType) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("ruleType").is(ruleType)
        );
        return mongoTemplate.find(query, ComplianceRule.class);
    }

    @Override
    public List<ComplianceRule> findByTenantIdAndCategory(String tenantId, ComplianceRule.RuleCategory category) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("category").is(category)
        );
        return mongoTemplate.find(query, ComplianceRule.class);
    }

    @Override
    public List<ComplianceRule> findActiveByTenantId(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(ComplianceRule.RuleStatus.ACTIVE)
                .and("enabled").is(true)
        );
        return mongoTemplate.find(query, ComplianceRule.class);
    }

    @Override
    public List<ComplianceRule> findByTenantIdAndApplicableDepartmentsContaining(String tenantId, String department) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("applicableDepartments").is(department)
        );
        return mongoTemplate.find(query, ComplianceRule.class);
    }

    @Override
    public List<ComplianceRule> findByTenantIdAndApplicableExpenseCategoriesContaining(String tenantId, String category) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("applicableExpenseCategories").is(category)
        );
        return mongoTemplate.find(query, ComplianceRule.class);
    }

    @Override
    public List<ComplianceRule> findByTenantIdAndTagsContaining(String tenantId, String tag) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("tags").is(tag)
        );
        return mongoTemplate.find(query, ComplianceRule.class);
    }

    @Override
    public boolean existsByRuleIdAndTenantId(String ruleId, String tenantId) {
        Query query = Query.query(
            Criteria.where("ruleId").is(ruleId)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, ComplianceRule.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), ComplianceRule.class);
    }

    @Override
    public void deleteByRuleIdAndTenantId(String ruleId, String tenantId) {
        Query query = Query.query(
            Criteria.where("ruleId").is(ruleId)
                .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, ComplianceRule.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, ComplianceRule.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, ComplianceRule.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, ComplianceRule.RuleStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.count(query, ComplianceRule.class);
    }

    @Override
    public List<ComplianceRule> searchByNameContaining(String tenantId, String searchTerm) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("name").regex(searchTerm, "i")
        );
        return mongoTemplate.find(query, ComplianceRule.class);
    }
}
