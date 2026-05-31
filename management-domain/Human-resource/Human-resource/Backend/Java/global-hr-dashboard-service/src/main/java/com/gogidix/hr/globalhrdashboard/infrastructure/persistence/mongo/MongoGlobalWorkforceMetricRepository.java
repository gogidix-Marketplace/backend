package com.gogidix.hr.globalhrdashboard.infrastructure.persistence.mongo;

import com.gogidix.hr.globalhrdashboard.domain.model.GlobalWorkforceMetric;
import com.gogidix.hr.globalhrdashboard.domain.model.MetricCategory;
import com.gogidix.hr.globalhrdashboard.domain.model.ExecutiveLevel;
import com.gogidix.hr.globalhrdashboard.domain.model.AggregationLevel;
import com.gogidix.hr.globalhrdashboard.domain.repository.GlobalWorkforceMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository implementation for GlobalWorkforceMetric
 * Following Hexagonal Architecture - this is the ADAPTER that implements the PORT
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoGlobalWorkforceMetricRepository implements GlobalWorkforceMetricRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public GlobalWorkforceMetric save(GlobalWorkforceMetric metric) {
        return mongoTemplate.save(metric);
    }

    @Override
    public Optional<GlobalWorkforceMetric> findByIdAndTenantId(String id, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, GlobalWorkforceMetric.class));
    }

    @Override
    public List<GlobalWorkforceMetric> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, GlobalWorkforceMetric.class);
    }

    @Override
    public List<GlobalWorkforceMetric> findByTenantIdAndMetricCategory(String tenantId, MetricCategory category) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("metricCategory").is(category)
        );
        return mongoTemplate.find(query, GlobalWorkforceMetric.class);
    }

    @Override
    public List<GlobalWorkforceMetric> findByTenantIdAndExecutiveLevel(String tenantId, ExecutiveLevel executiveLevel) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("executiveLevel").is(executiveLevel)
        );
        return mongoTemplate.find(query, GlobalWorkforceMetric.class);
    }

    @Override
    public List<GlobalWorkforceMetric> findByTenantIdAndPeriod(String tenantId, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
        );
        return mongoTemplate.find(query, GlobalWorkforceMetric.class);
    }

    @Override
    public List<GlobalWorkforceMetric> findByTenantIdAndCategoryAndPeriod(String tenantId, MetricCategory category, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("metricCategory").is(category)
                        .and("period").is(period)
        );
        return mongoTemplate.find(query, GlobalWorkforceMetric.class);
    }

    @Override
    public List<GlobalWorkforceMetric> findActiveByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("isActive").is(true)
        );
        return mongoTemplate.find(query, GlobalWorkforceMetric.class);
    }

    @Override
    public List<GlobalWorkforceMetric> findByTenantIdAndMetricName(String tenantId, String metricName) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("metricName").is(metricName)
        );
        return mongoTemplate.find(query, GlobalWorkforceMetric.class);
    }

    @Override
    public List<GlobalWorkforceMetric> findLatestByTenantId(String tenantId, int limit) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
        ).with(Sort.by(Sort.Direction.DESC, "createdAt")).limit(limit);
        return mongoTemplate.find(query, GlobalWorkforceMetric.class);
    }

    @Override
    public List<GlobalWorkforceMetric> findByTenantIdAndAggregationLevel(String tenantId, AggregationLevel aggregationLevel) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("aggregationLevel").is(aggregationLevel)
        );
        return mongoTemplate.find(query, GlobalWorkforceMetric.class);
    }

    @Override
    public List<GlobalWorkforceMetric> findMetricsNeedingAggregation(String tenantId, Instant before) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("lastAggregated").lt(before)
                        .orOperator(
                                Criteria.where("lastAggregated").exists(false)
                        )
        );
        return mongoTemplate.find(query, GlobalWorkforceMetric.class);
    }

    @Override
    public void deleteByIdAndTenantId(String id, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, GlobalWorkforceMetric.class);
    }

    @Override
    public void deleteByTenantIdAndPeriod(String tenantId, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
        );
        mongoTemplate.remove(query, GlobalWorkforceMetric.class);
    }

    @Override
    public boolean existsByIdAndTenantId(String id, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, GlobalWorkforceMetric.class);
    }

    @Override
    public List<GlobalWorkforceMetric> saveAll(List<GlobalWorkforceMetric> metrics) {
        return metrics.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public long countByTenantIdAndMetricCategory(String tenantId, MetricCategory category) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("metricCategory").is(category)
        );
        return mongoTemplate.count(query, GlobalWorkforceMetric.class);
    }

    @Override
    public List<GlobalWorkforceMetric> findByTenantIdAndUpdatedAtAfter(String tenantId, Instant updatedAt) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("updatedAt").gt(updatedAt)
        );
        return mongoTemplate.find(query, GlobalWorkforceMetric.class);
    }
}
