package com.gogidix.finance.compliance.infrastructure.persistence.mongo;

import com.gogidix.finance.compliance.domain.model.ComplianceCheck;
import com.gogidix.finance.compliance.domain.repository.ComplianceCheckRepository;
import com.gogidix.finance.compliance.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Compliance Check
 * Implements compliance check persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoComplianceCheckRepository implements ComplianceCheckRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public ComplianceCheck save(ComplianceCheck check) {
        log.debug("Saving compliance check: {} for tenant: {}",
            check.getCheckId(), check.getTenantId());
        return mongoTemplate.save(check);
    }

    @Override
    public List<ComplianceCheck> saveAll(List<ComplianceCheck> checks) {
        List<ComplianceCheck> result = new java.util.ArrayList<>();
        for (ComplianceCheck check : checks) {
            result.add(mongoTemplate.save(check));
        }
        return result;
    }

    @Override
    public Optional<ComplianceCheck> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
            Criteria.where("id").is(id)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, ComplianceCheck.class));
    }

    @Override
    public Optional<ComplianceCheck> findByCheckIdAndTenantId(String checkId, String tenantId) {
        Query query = Query.query(
            Criteria.where("checkId").is(checkId)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, ComplianceCheck.class));
    }

    @Override
    public List<ComplianceCheck> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public List<ComplianceCheck> findByTenantIdAndRuleId(String tenantId, String ruleId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("ruleId").is(ruleId)
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public List<ComplianceCheck> findByTenantIdAndEntityTypeAndEntityId(String tenantId, String entityType, String entityId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("entityType").is(entityType)
                .and("entityId").is(entityId)
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public List<ComplianceCheck> findByTenantIdAndResult(String tenantId, ComplianceCheck.CheckResult result) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("result").is(result)
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public List<ComplianceCheck> findByTenantIdAndSeverity(String tenantId, ComplianceCheck.SeverityLevel severity) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("severity").is(severity)
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public List<ComplianceCheck> findByTenantIdAndStatus(String tenantId, ComplianceCheck.CheckStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public List<ComplianceCheck> findByTenantIdAndEvaluatedAtBetween(String tenantId, Instant startDate, Instant endDate) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("evaluatedAt").gte(startDate).lte(endDate)
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public List<ComplianceCheck> findViolationsByTenantId(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("result").in(ComplianceCheck.CheckResult.NON_COMPLIANT, ComplianceCheck.CheckResult.WARNING)
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public List<ComplianceCheck> findViolationsByTenantIdAndSeverityGreaterThanEqual(String tenantId, ComplianceCheck.SeverityLevel severity) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("result").in(ComplianceCheck.CheckResult.NON_COMPLIANT, ComplianceCheck.CheckResult.WARNING)
                .and("severity").gte(severity)
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public List<ComplianceCheck> findPendingRemediationByTenantId(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("remediationRequired").is(true)
                .and("remediationCompletedAt").is(null)
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public List<ComplianceCheck> findPendingRemediationByTenantIdAndAssignedTo(String tenantId, String assignedTo) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("remediationRequired").is(true)
                .and("remediationAssignedTo").is(assignedTo)
                .and("remediationCompletedAt").is(null)
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public List<ComplianceCheck> findByTenantIdAndDepartment(String tenantId, String department) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("department").is(department)
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public List<ComplianceCheck> findByTenantIdAndCostCenter(String tenantId, String costCenter) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("costCenter").is(costCenter)
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public boolean existsByCheckIdAndTenantId(String checkId, String tenantId) {
        Query query = Query.query(
            Criteria.where("checkId").is(checkId)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, ComplianceCheck.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), ComplianceCheck.class);
    }

    @Override
    public void deleteByCheckIdAndTenantId(String checkId, String tenantId) {
        Query query = Query.query(
            Criteria.where("checkId").is(checkId)
                .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, ComplianceCheck.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, ComplianceCheck.class);
    }

    @Override
    public void deleteByTenantIdAndEvaluatedAtBefore(String tenantId, Instant cutoffDate) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("evaluatedAt").lt(cutoffDate)
        );
        mongoTemplate.remove(query, ComplianceCheck.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, ComplianceCheck.class);
    }

    @Override
    public long countByTenantIdAndResult(String tenantId, ComplianceCheck.CheckResult result) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("result").is(result)
        );
        return mongoTemplate.count(query, ComplianceCheck.class);
    }

    @Override
    public long countByTenantIdAndSeverity(String tenantId, ComplianceCheck.SeverityLevel severity) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("severity").is(severity)
        );
        return mongoTemplate.count(query, ComplianceCheck.class);
    }

    @Override
    public long countViolationsByTenantId(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("result").in(ComplianceCheck.CheckResult.NON_COMPLIANT, ComplianceCheck.CheckResult.WARNING)
        );
        return mongoTemplate.count(query, ComplianceCheck.class);
    }

    @Override
    public long countPendingRemediationByTenantId(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("remediationRequired").is(true)
                .and("remediationCompletedAt").is(null)
        );
        return mongoTemplate.count(query, ComplianceCheck.class);
    }
}
