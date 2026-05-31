package com.gogidix.hr.globalcompliance.infrastructure.persistence.mongo;

import com.gogidix.hr.globalcompliance.domain.model.NonComplianceIssue;
import com.gogidix.hr.globalcompliance.domain.repository.NonComplianceIssueRepository;
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
 * MongoDB Repository Implementation - NonComplianceIssue
 * Implements non-compliance issue persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoNonComplianceIssueRepository implements NonComplianceIssueRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public NonComplianceIssue save(NonComplianceIssue issue) {
        log.debug("Saving issue: {} for tenant: {}", issue.getIssueId(), issue.getTenantId());
        return mongoTemplate.save(issue);
    }

    @Override
    public List<NonComplianceIssue> saveAll(List<NonComplianceIssue> issues) {
        return issues.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<NonComplianceIssue> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id).and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, NonComplianceIssue.class));
    }

    @Override
    public Optional<NonComplianceIssue> findByIssueIdAndTenantId(String issueId, String tenantId) {
        Query query = Query.query(
                Criteria.where("issueId").is(issueId).and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, NonComplianceIssue.class));
    }

    @Override
    public Optional<NonComplianceIssue> findByIssueNumberAndTenantId(String issueNumber, String tenantId) {
        Query query = Query.query(
                Criteria.where("issueNumber").is(issueNumber).and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, NonComplianceIssue.class));
    }

    @Override
    public List<NonComplianceIssue> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, NonComplianceIssue.class);
    }

    @Override
    public List<NonComplianceIssue> findByTenantIdAndRequirementId(String tenantId, String requirementId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("requirementId").is(requirementId)
        );
        return mongoTemplate.find(query, NonComplianceIssue.class);
    }

    @Override
    public List<NonComplianceIssue> findByTenantIdAndCheckId(String tenantId, String checkId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("checkId").is(checkId)
        );
        return mongoTemplate.find(query, NonComplianceIssue.class);
    }

    @Override
    public List<NonComplianceIssue> findByTenantIdAndCountryCode(String tenantId, String countryCode) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("countryCode").is(countryCode)
        );
        return mongoTemplate.find(query, NonComplianceIssue.class);
    }

    @Override
    public List<NonComplianceIssue> findByTenantIdAndSeverity(String tenantId, String severity) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("severity").is(severity)
        );
        return mongoTemplate.find(query, NonComplianceIssue.class);
    }

    @Override
    public List<NonComplianceIssue> findByTenantIdAndStatus(String tenantId, String status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("status").is(status)
        );
        return mongoTemplate.find(query, NonComplianceIssue.class);
    }

    @Override
    public List<NonComplianceIssue> findByTenantIdAndAssignedTo(String tenantId, String assignedTo) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("assignedTo").is(assignedTo)
        );
        return mongoTemplate.find(query, NonComplianceIssue.class);
    }

    @Override
    public List<NonComplianceIssue> findByTenantIdAndDepartment(String tenantId, String department) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("department").is(department)
        );
        return mongoTemplate.find(query, NonComplianceIssue.class);
    }

    @Override
    public List<NonComplianceIssue> findByTenantIdAndIdentifiedBy(String tenantId, String identifiedBy) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("identifiedBy").is(identifiedBy)
        );
        return mongoTemplate.find(query, NonComplianceIssue.class);
    }

    @Override
    public List<NonComplianceIssue> findByTenantIdAndIdentifiedDateBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("identifiedDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, NonComplianceIssue.class);
    }

    @Override
    public List<NonComplianceIssue> findByTenantIdAndDueDateBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("dueDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, NonComplianceIssue.class);
    }

    @Override
    public List<NonComplianceIssue> findByTenantIdAndDueDateBeforeAndStatusNotIn(String tenantId, LocalDate date, List<String> statuses) {
        Date dateValue = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("dueDate").lt(dateValue)
                        .and("status").nin(statuses)
        );
        return mongoTemplate.find(query, NonComplianceIssue.class);
    }

    @Override
    public List<NonComplianceIssue> findOpenIssues(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("status").is("OPEN")
        );
        return mongoTemplate.find(query, NonComplianceIssue.class);
    }

    @Override
    public List<NonComplianceIssue> findOverdueIssues(String tenantId) {
        LocalDate today = LocalDate.now();
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("dueDate").lt(today)
                        .and("status").nin(List.of("RESOLVED", "CLOSED"))
        );
        return mongoTemplate.find(query, NonComplianceIssue.class);
    }

    @Override
    public List<NonComplianceIssue> findCriticalIssues(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("severity").is("CRITICAL")
                        .and("status").nin(List.of("RESOLVED", "CLOSED"))
        );
        return mongoTemplate.find(query, NonComplianceIssue.class);
    }

    @Override
    public List<NonComplianceIssue> findHighPriorityIssues(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("severity").in(List.of("CRITICAL", "HIGH"))
                        .and("status").nin(List.of("RESOLVED", "CLOSED"))
        );
        return mongoTemplate.find(query, NonComplianceIssue.class);
    }

    @Override
    public List<NonComplianceIssue> findIssuesByAffectedEmployee(String tenantId, String employeeId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("affectedEmployees").is(employeeId)
        );
        return mongoTemplate.find(query, NonComplianceIssue.class);
    }

    @Override
    public List<NonComplianceIssue> findResolvedIssuesBetweenDates(String tenantId, LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("resolvedDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, NonComplianceIssue.class);
    }

    @Override
    public List<NonComplianceIssue> findByTenantIdAndLocation(String tenantId, String location) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("location").is(location)
        );
        return mongoTemplate.find(query, NonComplianceIssue.class);
    }

    @Override
    public List<NonComplianceIssue> searchByTitleOrDescription(String tenantId, String searchTerm) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .orOperator(
                                Criteria.where("title").regex(searchTerm, "i"),
                                Criteria.where("description").regex(searchTerm, "i")
                        )
        );
        return mongoTemplate.find(query, NonComplianceIssue.class);
    }

    @Override
    public boolean existsByIssueNumberAndTenantId(String issueNumber, String tenantId) {
        Query query = Query.query(
                Criteria.where("issueNumber").is(issueNumber).and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, NonComplianceIssue.class);
    }

    @Override
    public boolean existsByIssueIdAndTenantId(String issueId, String tenantId) {
        Query query = Query.query(
                Criteria.where("issueId").is(issueId).and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, NonComplianceIssue.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), NonComplianceIssue.class);
    }

    @Override
    public void deleteByIssueIdAndTenantId(String issueId, String tenantId) {
        Query query = Query.query(
                Criteria.where("issueId").is(issueId).and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, NonComplianceIssue.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, NonComplianceIssue.class);
    }

    @Override
    public void deleteByRequirementId(String requirementId) {
        Query query = Query.query(Criteria.where("requirementId").is(requirementId));
        mongoTemplate.remove(query, NonComplianceIssue.class);
    }

    @Override
    public void deleteByCheckId(String checkId) {
        Query query = Query.query(Criteria.where("checkId").is(checkId));
        mongoTemplate.remove(query, NonComplianceIssue.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, NonComplianceIssue.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, String status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("status").is(status)
        );
        return mongoTemplate.count(query, NonComplianceIssue.class);
    }

    @Override
    public long countByTenantIdAndSeverity(String tenantId, String severity) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("severity").is(severity)
        );
        return mongoTemplate.count(query, NonComplianceIssue.class);
    }

    @Override
    public long countOpenIssues(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").in(List.of("OPEN", "IN_PROGRESS", "ESCALATED"))
        );
        return mongoTemplate.count(query, NonComplianceIssue.class);
    }

    @Override
    public long countOverdueIssues(String tenantId) {
        LocalDate today = LocalDate.now();
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("dueDate").lt(today)
                        .and("status").nin(List.of("RESOLVED", "CLOSED"))
        );
        return mongoTemplate.count(query, NonComplianceIssue.class);
    }

    @Override
    public List<NonComplianceIssue> findIssuesRequiringAttention(String tenantId) {
        LocalDate today = LocalDate.now();
        LocalDate urgentDate = today.plusDays(7);
        Date urgentDateValue = Date.from(urgentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").nin(List.of("RESOLVED", "CLOSED"))
                        .andOperator(
                                Criteria.where("severity").is("CRITICAL"),
                                new Criteria().orOperator(
                                        Criteria.where("dueDate").exists(false),
                                        Criteria.where("dueDate").lte(urgentDateValue)
                                )
                        )
        );
        return mongoTemplate.find(query, NonComplianceIssue.class);
    }
}
