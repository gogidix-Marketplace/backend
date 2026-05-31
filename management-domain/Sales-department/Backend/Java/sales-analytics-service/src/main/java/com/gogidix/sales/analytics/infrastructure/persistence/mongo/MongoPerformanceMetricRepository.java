package com.gogidix.sales.analytics.infrastructure.persistence.mongo;

import com.gogidix.sales.analytics.domain.model.PerformanceMetric;
import com.gogidix.sales.analytics.domain.repository.PerformanceMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository implementation for Performance Metric
 * Following Hexagonal Architecture - this is the ADAPTER that implements the PORT
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoPerformanceMetricRepository implements PerformanceMetricRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public PerformanceMetric save(PerformanceMetric metric) {
        return mongoTemplate.save(metric);
    }

    @Override
    public List<PerformanceMetric> saveAll(List<PerformanceMetric> metrics) {
        return metrics.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<PerformanceMetric> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, PerformanceMetric.class));
    }

    @Override
    public Optional<PerformanceMetric> findByPerformanceMetricIdAndTenantId(String metricId, String tenantId) {
        Query query = Query.query(
                Criteria.where("performanceMetricId").is(metricId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, PerformanceMetric.class));
    }

    @Override
    public List<PerformanceMetric> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, PerformanceMetric.class);
    }

    @Override
    public List<PerformanceMetric> findByTenantIdAndEntityType(String tenantId, String entityType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("entityType").is(entityType)
        );
        return mongoTemplate.find(query, PerformanceMetric.class);
    }

    @Override
    public List<PerformanceMetric> findByTenantIdAndEntityTypeAndEntityId(String tenantId, String entityType, String entityId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("entityType").is(entityType)
                        .and("entityId").is(entityId)
        );
        return mongoTemplate.find(query, PerformanceMetric.class);
    }

    @Override
    public List<PerformanceMetric> findByTenantIdAndEntityTypeAndPeriodBetween(
            String tenantId, String entityType, LocalDate startDate, LocalDate endDate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("entityType").is(entityType)
                        .and("periodStartDate").gte(startDate)
                        .and("periodEndDate").lte(endDate)
        );
        return mongoTemplate.find(query, PerformanceMetric.class);
    }

    @Override
    public List<PerformanceMetric> findByTenantIdAndPeriod(String tenantId, PerformanceMetric.PerformancePeriod period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
        );
        return mongoTemplate.find(query, PerformanceMetric.class);
    }

    @Override
    public List<PerformanceMetric> findTopPerformersByTenantIdAndEntityType(String tenantId, String entityType, int limit) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("entityType").is(entityType)
        ).with(Sort.by(Sort.Direction.DESC, "quotaAchievement"))
         .limit(limit);
        return mongoTemplate.find(query, PerformanceMetric.class);
    }

    @Override
    public List<PerformanceMetric> findByTenantIdAndManagerId(String tenantId, String managerId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("managerId").is(managerId)
        );
        return mongoTemplate.find(query, PerformanceMetric.class);
    }

    @Override
    public List<PerformanceMetric> findByTenantIdAndRegionId(String tenantId, String regionId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("regionId").is(regionId)
        );
        return mongoTemplate.find(query, PerformanceMetric.class);
    }

    @Override
    public Optional<PerformanceMetric> findLatestByTenantIdAndEntity(String tenantId, String entityType, String entityId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("entityType").is(entityType)
                        .and("entityId").is(entityId)
        ).with(Sort.by(Sort.Direction.DESC, "calculatedAt"))
         .limit(1);
        return Optional.ofNullable(mongoTemplate.findOne(query, PerformanceMetric.class));
    }

    @Override
    public List<PerformanceMetric> findRankedMetricsByTenantIdAndEntityType(String tenantId, String entityType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("entityType").is(entityType)
        ).with(Sort.by(Sort.Direction.DESC, "quotaAchievement"));
        return mongoTemplate.find(query, PerformanceMetric.class);
    }

    @Override
    public boolean existsByPerformanceMetricIdAndTenantId(String metricId, String tenantId) {
        Query query = Query.query(
                Criteria.where("performanceMetricId").is(metricId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, PerformanceMetric.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), PerformanceMetric.class);
    }

    @Override
    public void deleteByPerformanceMetricIdAndTenantId(String metricId, String tenantId) {
        Query query = Query.query(
                Criteria.where("performanceMetricId").is(metricId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, PerformanceMetric.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, PerformanceMetric.class);
    }

    @Override
    public void deleteByTenantIdAndPeriod(String tenantId, PerformanceMetric.PerformancePeriod period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
        );
        mongoTemplate.remove(query, PerformanceMetric.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, PerformanceMetric.class);
    }

    @Override
    public long countByTenantIdAndEntityType(String tenantId, String entityType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("entityType").is(entityType)
        );
        return mongoTemplate.count(query, PerformanceMetric.class);
    }
}
