package com.gogidix.sales.analytics.infrastructure.persistence.mongo;

import com.gogidix.sales.analytics.domain.model.AnalyticsReport;
import com.gogidix.sales.analytics.domain.repository.AnalyticsReportRepository;
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
 * MongoDB Repository implementation for Analytics Report
 * Following Hexagonal Architecture - this is the ADAPTER that implements the PORT
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoAnalyticsReportRepository implements AnalyticsReportRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public AnalyticsReport save(AnalyticsReport report) {
        return mongoTemplate.save(report);
    }

    @Override
    public List<AnalyticsReport> saveAll(List<AnalyticsReport> reports) {
        return reports.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<AnalyticsReport> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, AnalyticsReport.class));
    }

    @Override
    public Optional<AnalyticsReport> findByReportIdAndTenantId(String reportId, String tenantId) {
        Query query = Query.query(
                Criteria.where("reportId").is(reportId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, AnalyticsReport.class));
    }

    @Override
    public List<AnalyticsReport> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, AnalyticsReport.class);
    }

    @Override
    public List<AnalyticsReport> findByTenantIdAndStatus(String tenantId, AnalyticsReport.ReportStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.find(query, AnalyticsReport.class);
    }

    @Override
    public List<AnalyticsReport> findByTenantIdAndReportType(String tenantId, AnalyticsReport.ReportType reportType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("reportType").is(reportType)
        );
        return mongoTemplate.find(query, AnalyticsReport.class);
    }

    @Override
    public List<AnalyticsReport> findByTenantIdAndGeneratedBy(String tenantId, String generatedBy) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("generatedBy").is(generatedBy)
        );
        return mongoTemplate.find(query, AnalyticsReport.class);
    }

    @Override
    public List<AnalyticsReport> findByTenantIdAndDateRange(String tenantId, Instant startDate, Instant endDate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("startDate").gte(startDate)
                        .and("endDate").lte(endDate)
        );
        return mongoTemplate.find(query, AnalyticsReport.class);
    }

    @Override
    public List<AnalyticsReport> findByTenantIdAndScheduleId(String tenantId, String scheduleId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("scheduleId").is(scheduleId)
        );
        return mongoTemplate.find(query, AnalyticsReport.class);
    }

    @Override
    public List<AnalyticsReport> findExpiredReports(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("expiresAt").lt(Instant.now())
        );
        return mongoTemplate.find(query, AnalyticsReport.class);
    }

    @Override
    public List<AnalyticsReport> findByTenantIdAndTagsContaining(String tenantId, String tag) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("tags").in(tag)
        );
        return mongoTemplate.find(query, AnalyticsReport.class);
    }

    @Override
    public boolean existsByReportIdAndTenantId(String reportId, String tenantId) {
        Query query = Query.query(
                Criteria.where("reportId").is(reportId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, AnalyticsReport.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), AnalyticsReport.class);
    }

    @Override
    public void deleteByReportIdAndTenantId(String reportId, String tenantId) {
        Query query = Query.query(
                Criteria.where("reportId").is(reportId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, AnalyticsReport.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, AnalyticsReport.class);
    }

    @Override
    public void deleteExpiredReports(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("expiresAt").lt(Instant.now())
        );
        mongoTemplate.remove(query, AnalyticsReport.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, AnalyticsReport.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, AnalyticsReport.ReportStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.count(query, AnalyticsReport.class);
    }
}
