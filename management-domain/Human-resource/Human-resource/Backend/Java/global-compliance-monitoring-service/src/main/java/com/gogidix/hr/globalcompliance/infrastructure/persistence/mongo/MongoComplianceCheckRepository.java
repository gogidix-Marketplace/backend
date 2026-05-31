package com.gogidix.hr.globalcompliance.infrastructure.persistence.mongo;

import com.gogidix.hr.globalcompliance.domain.model.ComplianceCheck;
import com.gogidix.hr.globalcompliance.domain.repository.ComplianceCheckRepository;
import com.gogidix.hr.globalcompliance.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - ComplianceCheck
 * Implements compliance check persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoComplianceCheckRepository implements ComplianceCheckRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public ComplianceCheck save(ComplianceCheck check) {
        log.debug("Saving check: {} for tenant: {}",
                check.getCheckId(), check.getTenantId());
        return mongoTemplate.save(check);
    }

    @Override
    public List<ComplianceCheck> saveAll(List<ComplianceCheck> checks) {
        return checks.stream().map(mongoTemplate::save).toList();
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
    public Optional<ComplianceCheck> findByCheckNumberAndTenantId(String checkNumber, String tenantId) {
        Query query = Query.query(
                Criteria.where("checkNumber").is(checkNumber)
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
    public List<ComplianceCheck> findByTenantIdAndRequirementId(String tenantId, String requirementId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("requirementId").is(requirementId)
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public List<ComplianceCheck> findByTenantIdAndCountryCode(String tenantId, String countryCode) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("countryCode").is(countryCode)
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public List<ComplianceCheck> findByTenantIdAndStatus(String tenantId, String status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public List<ComplianceCheck> findByTenantIdAndResult(String tenantId, String result) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("result").is(result)
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public List<ComplianceCheck> findByTenantIdAndCheckedBy(String tenantId, String checkedBy) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("checkedBy").is(checkedBy)
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public List<ComplianceCheck> findByTenantIdAndScheduledDateBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("scheduledDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public List<ComplianceCheck> findByTenantIdAndCompletedDateBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("completedDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public List<ComplianceCheck> findByTenantIdAndScheduledDateBeforeAndStatus(String tenantId, LocalDate date, String status) {
        Date dateValue = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("scheduledDate").lt(dateValue)
                        .and("status").is(status)
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public List<ComplianceCheck> findOverdueChecks(String tenantId) {
        LocalDate today = LocalDate.now();
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("scheduledDate").lt(today)
                        .and("status").in(List.of("PENDING", "IN_PROGRESS"))
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public List<ComplianceCheck> findUpcomingChecks(String tenantId, LocalDate fromDate, LocalDate toDate) {
        Date from = Date.from(fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date to = Date.from(toDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("scheduledDate").gte(from).lte(to)
                        .and("status").is("PENDING")
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public List<ComplianceCheck> findPendingChecks(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is("PENDING")
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public List<ComplianceCheck> findCompletedChecks(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").in(List.of("PASSED", "FAILED", "WAIVED", "NOT_APPLICABLE"))
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public List<ComplianceCheck> findFailedChecks(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("result").in(List.of("NON_COMPLIANT", "PARTIALLY_COMPLIANT"))
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public List<ComplianceCheck> findChecksByRequirementAndDateRange(String tenantId, String requirementId, LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("requirementId").is(requirementId)
                        .and("scheduledDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public List<ComplianceCheck> findByTenantIdAndFrequency(String tenantId, String frequency) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("frequency").is(frequency)
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }

    @Override
    public boolean existsByCheckNumberAndTenantId(String checkNumber, String tenantId) {
        Query query = Query.query(
                Criteria.where("checkNumber").is(checkNumber)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, ComplianceCheck.class);
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
    public void deleteByRequirementId(String requirementId) {
        Query query = Query.query(Criteria.where("requirementId").is(requirementId));
        mongoTemplate.remove(query, ComplianceCheck.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, ComplianceCheck.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, String status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.count(query, ComplianceCheck.class);
    }

    @Override
    public long countByTenantIdAndResult(String tenantId, String result) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("result").is(result)
        );
        return mongoTemplate.count(query, ComplianceCheck.class);
    }

    @Override
    public long countByTenantIdAndRequirementId(String tenantId, String requirementId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("requirementId").is(requirementId)
        );
        return mongoTemplate.count(query, ComplianceCheck.class);
    }

    @Override
    public long countOverdueChecks(String tenantId) {
        LocalDate today = LocalDate.now();
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("scheduledDate").lt(today)
                        .and("status").in(List.of("PENDING", "IN_PROGRESS"))
        );
        return mongoTemplate.count(query, ComplianceCheck.class);
    }

    @Override
    public List<ComplianceCheck> searchByFindings(String tenantId, String searchTerm) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .orOperator(
                                Criteria.where("findings").regex(searchTerm, "i"),
                                Criteria.where("comments").regex(searchTerm, "i"),
                                Criteria.where("correctiveAction").regex(searchTerm, "i")
                        )
        );
        return mongoTemplate.find(query, ComplianceCheck.class);
    }
}
