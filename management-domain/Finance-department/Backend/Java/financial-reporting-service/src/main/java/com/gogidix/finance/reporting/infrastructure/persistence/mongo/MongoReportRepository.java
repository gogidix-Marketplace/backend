package com.gogidix.finance.reporting.infrastructure.persistence.mongo;

import com.gogidix.finance.reporting.domain.model.Report;
import com.gogidix.finance.reporting.domain.repository.ReportRepository;
import com.gogidix.finance.reporting.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 * MongoDB Repository Implementation - Report
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoReportRepository implements ReportRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Report save(Report report) {
        return mongoTemplate.save(report);
    }

    @Override
    public List<Report> saveAll(List<Report> reports) {
        List<Report> savedReports = new java.util.ArrayList<>();
        for (Report report : reports) {
            savedReports.add(mongoTemplate.save(report));
        }
        return savedReports;
    }

    @Override
    public Optional<Report> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
            Criteria.where("id").is(id)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Report.class));
    }

    @Override
    public Optional<Report> findByReportIdAndTenantId(String reportId, String tenantId) {
        Query query = Query.query(
            Criteria.where("reportId").is(reportId)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Report.class));
    }

    @Override
    public List<Report> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, Report.class);
    }

    @Override
    public List<Report> findByTenantIdAndStatus(String tenantId, Report.ReportStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.find(query, Report.class);
    }

    @Override
    public List<Report> findByTenantIdAndReportType(String tenantId, Report.ReportType reportType) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("reportType").is(reportType)
        );
        return mongoTemplate.find(query, Report.class);
    }

    @Override
    public List<Report> findByTenantIdAndGeneratedBy(String tenantId, String generatedBy) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("generatedBy").is(generatedBy)
        );
        return mongoTemplate.find(query, Report.class);
    }

    @Override
    public List<Report> findByTenantIdAndReportDateBetween(String tenantId, LocalDate startDate,
                                                             LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("reportDate").gte(start).lte(end)
        );
        return mongoTemplate.find(query, Report.class);
    }

    @Override
    public List<Report> findByTenantIdAndCreatedAtBetween(String tenantId, Instant startDate, Instant endDate) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("createdAt").gte(startDate).lte(endDate)
        );
        return mongoTemplate.find(query, Report.class);
    }

    @Override
    public List<Report> findScheduledReportsDueBefore(Instant threshold) {
        Query query = Query.query(
            Criteria.where("isScheduled").is(true)
                .and("status").is(Report.ReportStatus.PENDING)
                .and("startedAt").exists(false)
        );
        return mongoTemplate.find(query, Report.class);
    }

    @Override
    public boolean existsByReportIdAndTenantId(String reportId, String tenantId) {
        Query query = Query.query(
            Criteria.where("reportId").is(reportId)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, Report.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Report.class);
    }

    @Override
    public void deleteByReportIdAndTenantId(String reportId, String tenantId) {
        Query query = Query.query(
            Criteria.where("reportId").is(reportId)
                .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, Report.class);
    }

    @Override
    public void deleteOlderThan(Instant cutoff) {
        Query query = Query.query(Criteria.where("createdAt").lt(cutoff));
        mongoTemplate.remove(query, Report.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, Report.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, Report.ReportStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.count(query, Report.class);
    }

    @Override
    public List<Report> findByTenantIdAndScheduleId(String tenantId, String scheduleId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("scheduleId").is(scheduleId)
        );
        return mongoTemplate.find(query, Report.class);
    }
}
