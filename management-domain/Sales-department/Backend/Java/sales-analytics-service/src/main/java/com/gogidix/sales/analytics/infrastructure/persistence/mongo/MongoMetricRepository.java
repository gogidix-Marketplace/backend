package com.gogidix.sales.analytics.infrastructure.persistence.mongo;

import com.gogidix.sales.analytics.domain.model.Metric;
import com.gogidix.sales.analytics.domain.repository.MetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository implementation for Metric
 * Following Hexagonal Architecture - this is the ADAPTER that implements the PORT
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoMetricRepository implements MetricRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Metric save(Metric metric) {
        return mongoTemplate.save(metric);
    }

    @Override
    public List<Metric> saveAll(List<Metric> metrics) {
        return metrics.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<Metric> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, Metric.class));
    }

    @Override
    public Optional<Metric> findByMetricIdAndTenantId(String metricId, String tenantId) {
        Query query = Query.query(
                Criteria.where("metricId").is(metricId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Metric.class));
    }

    @Override
    public List<Metric> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, Metric.class);
    }

    @Override
    public List<Metric> findByTenantIdAndEntityType(String tenantId, String entityType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("entityType").is(entityType)
        );
        return mongoTemplate.find(query, Metric.class);
    }

    @Override
    public List<Metric> findByTenantIdAndEntityTypeAndEntityId(String tenantId, String entityType, String entityId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("entityType").is(entityType)
                        .and("entityId").is(entityId)
        );
        return mongoTemplate.find(query, Metric.class);
    }

    @Override
    public List<Metric> findByTenantIdAndMetricType(String tenantId, Metric.MetricType metricType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("metricType").is(metricType)
        );
        return mongoTemplate.find(query, Metric.class);
    }

    @Override
    public List<Metric> findByTenantIdAndPeriod(String tenantId, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
        );
        return mongoTemplate.find(query, Metric.class);
    }

    @Override
    public List<Metric> findByTenantIdAndDateRange(String tenantId, Instant startDate, Instant endDate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("periodStart").gte(startDate)
                        .and("periodEnd").lte(endDate)
        );
        return mongoTemplate.find(query, Metric.class);
    }

    @Override
    public List<Metric> findLatestMetricsByTenantIdAndEntity(String tenantId, String entityType, String entityId, int limit) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("entityType").is(entityType)
                        .and("entityId").is(entityId)
        ).with(Sort.by(Sort.Direction.DESC, "calculatedAt"))
         .limit(limit);
        return mongoTemplate.find(query, Metric.class);
    }

    @Override
    public List<Metric> findMetricsByTenantIdAndMetricTypes(String tenantId, List<Metric.MetricType> metricTypes) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("metricType").in(metricTypes)
        );
        return mongoTemplate.find(query, Metric.class);
    }

    @Override
    public boolean existsByMetricIdAndTenantId(String metricId, String tenantId) {
        Query query = Query.query(
                Criteria.where("metricId").is(metricId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, Metric.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Metric.class);
    }

    @Override
    public void deleteByMetricIdAndTenantId(String metricId, String tenantId) {
        Query query = Query.query(
                Criteria.where("metricId").is(metricId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, Metric.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, Metric.class);
    }

    @Override
    public void deleteByTenantIdAndPeriod(String tenantId, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
        );
        mongoTemplate.remove(query, Metric.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, Metric.class);
    }

    @Override
    public long countByTenantIdAndMetricType(String tenantId, Metric.MetricType metricType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("metricType").is(metricType)
        );
        return mongoTemplate.count(query, Metric.class);
    }
}
