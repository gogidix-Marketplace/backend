package com.gogidix.hr.globalcompliance.infrastructure.persistence.mongo;

import com.gogidix.hr.globalcompliance.domain.model.ComplianceReport;
import com.gogidix.hr.globalcompliance.domain.repository.ComplianceReportRepository;
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
import java.util.Optional;

/**
 * MongoDB Repository Implementation - ComplianceReport
 * Implements compliance report persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoComplianceReportRepository implements ComplianceReportRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public ComplianceReport save(ComplianceReport report) {
        log.debug("Saving report: {} for tenant: {}", report.getReportId(), report.getTenantId());
        return mongoTemplate.save(report);
    }

    @Override
    public List<ComplianceReport> saveAll(List<ComplianceReport> reports) {
        return reports.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<ComplianceReport> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id).and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, ComplianceReport.class));
    }

    @Override
    public Optional<ComplianceReport> findByReportIdAndTenantId(String reportId, String tenantId) {
        Query query = Query.query(
                Criteria.where("reportId").is(reportId).and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, ComplianceReport.class));
    }

    @Override
    public Optional<ComplianceReport> findByReportNumberAndTenantId(String reportNumber, String tenantId) {
        Query query = Query.query(
                Criteria.where("reportNumber").is(reportNumber).and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, ComplianceReport.class));
    }

    @Override
    public List<ComplianceReport> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId))
                .with(Sort.by(Sort.Direction.DESC, "periodStart"));
        return mongoTemplate.find(query, ComplianceReport.class);
    }

    @Override
    public List<ComplianceReport> findByTenantIdAndCountryCode(String tenantId, String countryCode) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("countryCode").is(countryCode)
        ).with(Sort.by(Sort.Direction.DESC, "periodStart"));
        return mongoTemplate.find(query, ComplianceReport.class);
    }

    @Override
    public List<ComplianceReport> findByTenantIdAndReportType(String tenantId, String reportType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("reportType").is(reportType)
        ).with(Sort.by(Sort.Direction.DESC, "periodStart"));
        return mongoTemplate.find(query, ComplianceReport.class);
    }

    @Override
    public List<ComplianceReport> findByTenantIdAndStatus(String tenantId, String status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("status").is(status)
        ).with(Sort.by(Sort.Direction.DESC, "periodStart"));
        return mongoTemplate.find(query, ComplianceReport.class);
    }

    @Override
    public List<ComplianceReport> findByTenantIdAndPreparedBy(String tenantId, String preparedBy) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("preparedBy").is(preparedBy)
        ).with(Sort.by(Sort.Direction.DESC, "periodStart"));
        return mongoTemplate.find(query, ComplianceReport.class);
    }

    @Override
    public List<ComplianceReport> findByTenantIdAndApprovedBy(String tenantId, String approvedBy) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("approvedBy").is(approvedBy)
        ).with(Sort.by(Sort.Direction.DESC, "periodStart"));
        return mongoTemplate.find(query, ComplianceReport.class);
    }

    @Override
    public List<ComplianceReport> findByTenantIdAndPeriodBetween(String tenantId, LocalDate periodStart, LocalDate periodEnd) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("periodStart").gte(periodStart)
                        .and("periodEnd").lte(periodEnd)
        ).with(Sort.by(Sort.Direction.DESC, "periodStart"));
        return mongoTemplate.find(query, ComplianceReport.class);
    }

    @Override
    public List<ComplianceReport> findByTenantIdAndPeriodStartBetween(String tenantId, LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("periodStart").gte(start).lte(end)
        ).with(Sort.by(Sort.Direction.DESC, "periodStart"));
        return mongoTemplate.find(query, ComplianceReport.class);
    }

    @Override
    public List<ComplianceReport> findByTenantIdAndDepartment(String tenantId, String department) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("department").is(department)
        ).with(Sort.by(Sort.Direction.DESC, "periodStart"));
        return mongoTemplate.find(query, ComplianceReport.class);
    }

    @Override
    public List<ComplianceReport> findDraftReports(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("status").is("DRAFT")
        ).with(Sort.by(Sort.Direction.DESC, "periodStart"));
        return mongoTemplate.find(query, ComplianceReport.class);
    }

    @Override
    public List<ComplianceReport> findSubmittedReports(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("status").is("SUBMITTED")
        ).with(Sort.by(Sort.Direction.DESC, "submittedDate"));
        return mongoTemplate.find(query, ComplianceReport.class);
    }

    @Override
    public List<ComplianceReport> findApprovedReports(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("status").is("APPROVED")
        ).with(Sort.by(Sort.Direction.DESC, "approvedDate"));
        return mongoTemplate.find(query, ComplianceReport.class);
    }

    @Override
    public List<ComplianceReport> findPublishedReports(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("status").is("PUBLISHED")
        ).with(Sort.by(Sort.Direction.DESC, "approvedDate"));
        return mongoTemplate.find(query, ComplianceReport.class);
    }

    @Override
    public List<ComplianceReport> findByTenantIdAndRegion(String tenantId, String region) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("region").is(region)
        ).with(Sort.by(Sort.Direction.DESC, "periodStart"));
        return mongoTemplate.find(query, ComplianceReport.class);
    }

    @Override
    public List<ComplianceReport> findReportsByComplianceScoreRange(String tenantId, Double minScore, Double maxScore) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("complianceScore").gte(minScore).lte(maxScore)
        ).with(Sort.by(Sort.Direction.DESC, "complianceScore"));
        return mongoTemplate.find(query, ComplianceReport.class);
    }

    @Override
    public List<ComplianceReport> findReportsWithCriticalIssues(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("criticalIssues").exists(true)
                        .and("criticalIssues").ne(null)
                        .and("criticalIssues").size(1)
        ).with(Sort.by(Sort.Direction.DESC, "periodStart"));
        return mongoTemplate.find(query, ComplianceReport.class);
    }

    @Override
    public List<ComplianceReport> findRecentReports(String tenantId, int limit) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
        ).with(Sort.by(Sort.Direction.DESC, "createdAt")).limit(limit);
        return mongoTemplate.find(query, ComplianceReport.class);
    }

    @Override
    public boolean existsByReportNumberAndTenantId(String reportNumber, String tenantId) {
        Query query = Query.query(
                Criteria.where("reportNumber").is(reportNumber).and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, ComplianceReport.class);
    }

    @Override
    public boolean existsByReportIdAndTenantId(String reportId, String tenantId) {
        Query query = Query.query(
                Criteria.where("reportId").is(reportId).and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, ComplianceReport.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), ComplianceReport.class);
    }

    @Override
    public void deleteByReportIdAndTenantId(String reportId, String tenantId) {
        Query query = Query.query(
                Criteria.where("reportId").is(reportId).and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, ComplianceReport.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, ComplianceReport.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, ComplianceReport.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, String status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("status").is(status)
        );
        return mongoTemplate.count(query, ComplianceReport.class);
    }

    @Override
    public long countByTenantIdAndReportType(String tenantId, String reportType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId).and("reportType").is(reportType)
        );
        return mongoTemplate.count(query, ComplianceReport.class);
    }

    @Override
    public Double getAverageComplianceScore(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("complianceScore").exists(true)
        );
        List<ComplianceReport> reports = mongoTemplate.find(query, ComplianceReport.class);
        return reports.stream()
                .map(ComplianceReport::getComplianceScore)
                .reduce(0.0, Double::sum) / Math.max(1, reports.size());
    }

    @Override
    public List<ComplianceReport> searchBySummary(String tenantId, String searchTerm) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .orOperator(
                                Criteria.where("summary").regex(searchTerm, "i"),
                                Criteria.where("notes").regex(searchTerm, "i")
                        )
        ).with(Sort.by(Sort.Direction.DESC, "periodStart"));
        return mongoTemplate.find(query, ComplianceReport.class);
    }
}
