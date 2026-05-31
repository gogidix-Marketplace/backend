package com.gogidix.hr.globalcompliance.infrastructure.persistence.mongo;

import com.gogidix.hr.globalcompliance.domain.model.AuditTrail;
import com.gogidix.hr.globalcompliance.domain.repository.AuditTrailRepository;
import com.gogidix.hr.globalcompliance.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * MongoDB Repository Implementation - AuditTrail
 * Implements audit trail persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoAuditTrailRepository implements AuditTrailRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public AuditTrail save(AuditTrail auditTrail) {
        log.debug("Saving audit: {} for tenant: {}", auditTrail.getAuditId(), auditTrail.getTenantId());
        return mongoTemplate.save(auditTrail);
    }

    @Override
    public List<AuditTrail> saveAll(List<AuditTrail> auditTrails) {
        return auditTrails.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public List<AuditTrail> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId))
                .with(Sort.by(Sort.Direction.DESC, "actionTimestamp"));
        return mongoTemplate.find(query, AuditTrail.class);
    }

    @Override
    public List<AuditTrail> findByTenantIdAndRequirementId(String tenantId, String requirementId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("requirementId").is(requirementId)
        ).with(Sort.by(Sort.Direction.DESC, "actionTimestamp"));
        return mongoTemplate.find(query, AuditTrail.class);
    }

    @Override
    public List<AuditTrail> findByTenantIdAndCheckId(String tenantId, String checkId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("checkId").is(checkId)
        ).with(Sort.by(Sort.Direction.DESC, "actionTimestamp"));
        return mongoTemplate.find(query, AuditTrail.class);
    }

    @Override
    public List<AuditTrail> findByTenantIdAndIssueId(String tenantId, String issueId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("issueId").is(issueId)
        ).with(Sort.by(Sort.Direction.DESC, "actionTimestamp"));
        return mongoTemplate.find(query, AuditTrail.class);
    }

    @Override
    public List<AuditTrail> findByTenantIdAndReportId(String tenantId, String reportId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("reportId").is(reportId)
        ).with(Sort.by(Sort.Direction.DESC, "actionTimestamp"));
        return mongoTemplate.find(query, AuditTrail.class);
    }

    @Override
    public List<AuditTrail> findByTenantIdAndAction(String tenantId, String action) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("action").is(action)
        ).with(Sort.by(Sort.Direction.DESC, "actionTimestamp"));
        return mongoTemplate.find(query, AuditTrail.class);
    }

    @Override
    public List<AuditTrail> findByTenantIdAndActionedBy(String tenantId, String actionedBy) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("actionedBy").is(actionedBy)
        ).with(Sort.by(Sort.Direction.DESC, "actionTimestamp"));
        return mongoTemplate.find(query, AuditTrail.class);
    }

    @Override
    public List<AuditTrail> findByTenantIdAndEntityTypeAndEntityId(String tenantId, String entityType, String entityId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("entityType").is(entityType)
                        .and("entityId").is(entityId)
        ).with(Sort.by(Sort.Direction.DESC, "actionTimestamp"));
        return mongoTemplate.find(query, AuditTrail.class);
    }

    @Override
    public List<AuditTrail> findByTenantIdAndActionDateBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("actionDate").gte(start).lte(end)
        ).with(Sort.by(Sort.Direction.DESC, "actionTimestamp"));
        return mongoTemplate.find(query, AuditTrail.class);
    }

    @Override
    public List<AuditTrail> findByTenantIdAndCountryCode(String tenantId, String countryCode) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("countryCode").is(countryCode)
        ).with(Sort.by(Sort.Direction.DESC, "actionTimestamp"));
        return mongoTemplate.find(query, AuditTrail.class);
    }

    @Override
    public List<AuditTrail> findByTenantIdAndDepartment(String tenantId, String department) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("department").is(department)
        ).with(Sort.by(Sort.Direction.DESC, "actionTimestamp"));
        return mongoTemplate.find(query, AuditTrail.class);
    }

    @Override
    public List<AuditTrail> findRecentAudits(String tenantId, int limit) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
        ).with(Sort.by(Sort.Direction.DESC, "actionTimestamp")).limit(limit);
        return mongoTemplate.find(query, AuditTrail.class);
    }

    @Override
    public List<AuditTrail> findCriticalAudits(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("action").in(List.of("DELETED", "ESCALATED", "REJECTED"))
        ).with(Sort.by(Sort.Direction.DESC, "actionTimestamp"));
        return mongoTemplate.find(query, AuditTrail.class);
    }

    @Override
    public List<AuditTrail> findByTenantIdAndIpAddress(String tenantId, String ipAddress) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("ipAddress").is(ipAddress)
        ).with(Sort.by(Sort.Direction.DESC, "actionTimestamp"));
        return mongoTemplate.find(query, AuditTrail.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), AuditTrail.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, AuditTrail.class);
    }

    @Override
    public void deleteByRequirementId(String requirementId) {
        Query query = Query.query(Criteria.where("requirementId").is(requirementId));
        mongoTemplate.remove(query, AuditTrail.class);
    }

    @Override
    public void deleteByCheckId(String checkId) {
        Query query = Query.query(Criteria.where("checkId").is(checkId));
        mongoTemplate.remove(query, AuditTrail.class);
    }

    @Override
    public void deleteByIssueId(String issueId) {
        Query query = Query.query(Criteria.where("issueId").is(issueId));
        mongoTemplate.remove(query, AuditTrail.class);
    }

    @Override
    public void deleteByReportId(String reportId) {
        Query query = Query.query(Criteria.where("reportId").is(reportId));
        mongoTemplate.remove(query, AuditTrail.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, AuditTrail.class);
    }

    @Override
    public long countByTenantIdAndAction(String tenantId, String action) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("action").is(action)
        );
        return mongoTemplate.count(query, AuditTrail.class);
    }

    @Override
    public long countByTenantIdAndActionedBy(String tenantId, String actionedBy) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("actionedBy").is(actionedBy)
        );
        return mongoTemplate.count(query, AuditTrail.class);
    }

    @Override
    public List<AuditTrail> findAuditsByEntity(String tenantId, String entityType, String entityId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("entityType").is(entityType)
                        .and("entityId").is(entityId)
        ).with(Sort.by(Sort.Direction.DESC, "actionTimestamp"));
        return mongoTemplate.find(query, AuditTrail.class);
    }

    @Override
    public List<AuditTrail> searchByReason(String tenantId, String searchTerm) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .orOperator(
                                Criteria.where("reason").regex(searchTerm, "i"),
                                Criteria.where("changes").regex(searchTerm, "i")
                        )
        ).with(Sort.by(Sort.Direction.DESC, "actionTimestamp"));
        return mongoTemplate.find(query, AuditTrail.class);
    }
}
