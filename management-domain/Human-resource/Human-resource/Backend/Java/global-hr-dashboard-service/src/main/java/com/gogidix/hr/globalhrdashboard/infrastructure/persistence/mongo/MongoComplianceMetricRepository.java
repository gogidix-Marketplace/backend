package com.gogidix.hr.globalhrdashboard.infrastructure.persistence.mongo;

import com.gogidix.hr.globalhrdashboard.domain.model.ComplianceMetric;
import com.gogidix.hr.globalhrdashboard.domain.model.ComplianceStatus;
import com.gogidix.hr.globalhrdashboard.domain.repository.ComplianceMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * MongoDB Repository implementation for ComplianceMetric
 * Following Hexagonal Architecture - this is the ADAPTER that implements the PORT
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoComplianceMetricRepository implements ComplianceMetricRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public ComplianceMetric save(ComplianceMetric metric) {
        return mongoTemplate.save(metric);
    }

    @Override
    public Optional<ComplianceMetric> findByIdAndTenantId(String id, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, ComplianceMetric.class));
    }

    @Override
    public List<ComplianceMetric> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, ComplianceMetric.class);
    }

    @Override
    public List<ComplianceMetric> findByTenantIdAndCountryCode(String tenantId, String countryCode) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("countryCode").is(countryCode.toUpperCase())
        );
        return mongoTemplate.find(query, ComplianceMetric.class);
    }

    @Override
    public Optional<ComplianceMetric> findByTenantIdAndCountryCodeAndPeriod(String tenantId, String countryCode, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("countryCode").is(countryCode.toUpperCase())
                        .and("period").is(period)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, ComplianceMetric.class));
    }

    @Override
    public List<ComplianceMetric> findByTenantIdAndPeriod(String tenantId, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
        );
        return mongoTemplate.find(query, ComplianceMetric.class);
    }

    @Override
    public List<ComplianceMetric> findByTenantIdAndRegionCode(String tenantId, String regionCode) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("regionCode").is(regionCode)
        );
        return mongoTemplate.find(query, ComplianceMetric.class);
    }

    @Override
    public List<ComplianceMetric> findByTenantIdAndStatus(String tenantId, ComplianceStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.find(query, ComplianceMetric.class);
    }

    @Override
    public List<ComplianceMetric> findAtRiskByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(ComplianceStatus.AT_RISK)
        );
        return mongoTemplate.find(query, ComplianceMetric.class);
    }

    @Override
    public List<ComplianceMetric> findNonCompliantByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(ComplianceStatus.NON_COMPLIANT)
        );
        return mongoTemplate.find(query, ComplianceMetric.class);
    }

    @Override
    public List<ComplianceMetric> findWithCriticalIssuesByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("criticalIssues").gt(0)
        );
        return mongoTemplate.find(query, ComplianceMetric.class);
    }

    @Override
    public List<ComplianceMetric> findActiveByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("isActive").is(true)
        );
        return mongoTemplate.find(query, ComplianceMetric.class);
    }

    @Override
    public List<ComplianceMetric> findByTenantIdAndLastAssessedAfter(String tenantId, Instant lastAssessed) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("lastAssessed").gt(lastAssessed)
        );
        return mongoTemplate.find(query, ComplianceMetric.class);
    }

    @Override
    public List<ComplianceMetric> findMetricsNeedingAssessment(String tenantId, Instant before) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("lastAssessed").lt(before)
                        .orOperator(
                                Criteria.where("lastAssessed").exists(false)
                        )
        );
        return mongoTemplate.find(query, ComplianceMetric.class);
    }

    @Override
    public List<ComplianceMetric> findByTenantIdAndComplianceScoreLessThan(String tenantId, Double score) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("complianceScore").lt(score)
        );
        return mongoTemplate.find(query, ComplianceMetric.class);
    }

    @Override
    public List<ComplianceMetric> findByTenantIdAndComplianceScoreGreaterThanEqual(String tenantId, Double score) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("complianceScore").gte(score)
        );
        return mongoTemplate.find(query, ComplianceMetric.class);
    }

    @Override
    public Double getAverageComplianceScore(String tenantId, String period) {
        Aggregation aggregation = newAggregation(
                match(Criteria.where("tenantId").is(tenantId).and("period").is(period)),
                group().avg("complianceScore").as("averageScore")
        );

        AggregationResults<AverageResult> results = mongoTemplate.aggregate(
                aggregation, ComplianceMetric.class, AverageResult.class
        );

        AverageResult result = results.getUniqueMappedResult();
        return result != null ? result.getAverageScore() : 0.0;
    }

    @Override
    public Optional<ComplianceStatus> getGlobalComplianceStatus(String tenantId, String period) {
        Double avgScore = getAverageComplianceScore(tenantId, period);
        return Optional.of(ComplianceStatus.fromScore(avgScore));
    }

    @Override
    public void deleteByIdAndTenantId(String id, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, ComplianceMetric.class);
    }

    @Override
    public void deleteByTenantIdAndPeriod(String tenantId, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
        );
        mongoTemplate.remove(query, ComplianceMetric.class);
    }

    @Override
    public boolean existsByIdAndTenantId(String id, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, ComplianceMetric.class);
    }

    @Override
    public List<ComplianceMetric> saveAll(List<ComplianceMetric> metrics) {
        return metrics.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, ComplianceStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.count(query, ComplianceMetric.class);
    }

    @Override
    public long countWithCriticalIssuesByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("criticalIssues").gt(0)
        );
        return mongoTemplate.count(query, ComplianceMetric.class);
    }

    @Override
    public List<ComplianceMetric> findTrendDataByCountry(String tenantId, String countryCode,
                                                           String startPeriod, String endPeriod) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("countryCode").is(countryCode.toUpperCase())
                        .and("period").gte(startPeriod).lte(endPeriod)
        ).with(Sort.by(Sort.Direction.ASC, "period"));
        return mongoTemplate.find(query, ComplianceMetric.class);
    }

    @Override
    public List<ComplianceMetric> findLatestByPeriod(String tenantId, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
        );
        return mongoTemplate.find(query, ComplianceMetric.class);
    }

    private static class AverageResult {
        private double averageScore;

        public double getAverageScore() {
            return averageScore;
        }

        public void setAverageScore(double averageScore) {
            this.averageScore = averageScore;
        }
    }
}
