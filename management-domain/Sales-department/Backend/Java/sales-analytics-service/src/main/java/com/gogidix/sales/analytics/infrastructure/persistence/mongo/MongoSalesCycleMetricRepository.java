package com.gogidix.sales.analytics.infrastructure.persistence.mongo;

import com.gogidix.sales.analytics.domain.model.SalesCycleMetric;
import com.gogidix.sales.analytics.domain.repository.SalesCycleMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository implementation for Sales Cycle Metric
 * Following Hexagonal Architecture - this is the ADAPTER that implements the PORT
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoSalesCycleMetricRepository implements SalesCycleMetricRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public SalesCycleMetric save(SalesCycleMetric metric) {
        return mongoTemplate.save(metric);
    }

    @Override
    public List<SalesCycleMetric> saveAll(List<SalesCycleMetric> metrics) {
        return metrics.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<SalesCycleMetric> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, SalesCycleMetric.class));
    }

    @Override
    public Optional<SalesCycleMetric> findBySalesCycleMetricIdAndTenantId(String metricId, String tenantId) {
        Query query = Query.query(
                Criteria.where("salesCycleMetricId").is(metricId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, SalesCycleMetric.class));
    }

    @Override
    public List<SalesCycleMetric> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, SalesCycleMetric.class);
    }

    @Override
    public List<SalesCycleMetric> findByTenantIdAndEntityType(String tenantId, String entityType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("entityType").is(entityType)
        );
        return mongoTemplate.find(query, SalesCycleMetric.class);
    }

    @Override
    public List<SalesCycleMetric> findByTenantIdAndEntityTypeAndEntityId(
            String tenantId, String entityType, String entityId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("entityType").is(entityType)
                        .and("entityId").is(entityId)
        );
        return mongoTemplate.find(query, SalesCycleMetric.class);
    }

    @Override
    public List<SalesCycleMetric> findByTenantIdAndPeriodBetween(
            String tenantId, LocalDate startDate, LocalDate endDate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("periodStartDate").gte(startDate)
                        .and("periodEndDate").lte(endDate)
        );
        return mongoTemplate.find(query, SalesCycleMetric.class);
    }

    @Override
    public List<SalesCycleMetric> findByTenantIdAndPeriod(String tenantId, SalesCycleMetric.MetricPeriod period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
        );
        return mongoTemplate.find(query, SalesCycleMetric.class);
    }

    @Override
    public List<SalesCycleMetric> findByTenantIdAndHealthScore(
            String tenantId, SalesCycleMetric.CycleHealthScore healthScore) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("healthScore").is(healthScore)
        );
        return mongoTemplate.find(query, SalesCycleMetric.class);
    }

    @Override
    public Optional<SalesCycleMetric> findLatestByTenantIdAndEntity(
            String tenantId, String entityType, String entityId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("entityType").is(entityType)
                        .and("entityId").is(entityId)
        ).with(Sort.by(Sort.Direction.DESC, "calculatedAt"))
         .limit(1);
        return Optional.ofNullable(mongoTemplate.findOne(query, SalesCycleMetric.class));
    }

    @Override
    public List<SalesCycleMetric> findSlowestCyclesByTenantId(String tenantId, int limit) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
        ).with(Sort.by(Sort.Direction.DESC, "averageCycleDurationDays"))
         .limit(limit);
        return mongoTemplate.find(query, SalesCycleMetric.class);
    }

    @Override
    public List<SalesCycleMetric> findFastestCyclesByTenantId(String tenantId, int limit) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
        ).with(Sort.by(Sort.Direction.ASC, "averageCycleDurationDays"))
         .limit(limit);
        return mongoTemplate.find(query, SalesCycleMetric.class);
    }

    @Override
    public boolean existsBySalesCycleMetricIdAndTenantId(String metricId, String tenantId) {
        Query query = Query.query(
                Criteria.where("salesCycleMetricId").is(metricId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, SalesCycleMetric.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), SalesCycleMetric.class);
    }

    @Override
    public void deleteBySalesCycleMetricIdAndTenantId(String metricId, String tenantId) {
        Query query = Query.query(
                Criteria.where("salesCycleMetricId").is(metricId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, SalesCycleMetric.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, SalesCycleMetric.class);
    }

    @Override
    public void deleteByTenantIdAndPeriod(String tenantId, SalesCycleMetric.MetricPeriod period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
        );
        mongoTemplate.remove(query, SalesCycleMetric.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, SalesCycleMetric.class);
    }

    @Override
    public long countByTenantIdAndHealthScore(String tenantId, SalesCycleMetric.CycleHealthScore healthScore) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("healthScore").is(healthScore)
        );
        return mongoTemplate.count(query, SalesCycleMetric.class);
    }
}
