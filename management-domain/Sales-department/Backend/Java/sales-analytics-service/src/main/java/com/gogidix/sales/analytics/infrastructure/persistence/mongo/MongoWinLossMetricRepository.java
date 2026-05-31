package com.gogidix.sales.analytics.infrastructure.persistence.mongo;

import com.gogidix.sales.analytics.domain.model.WinLossMetric;
import com.gogidix.sales.analytics.domain.repository.WinLossMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository implementation for Win/Loss Metric
 * Following Hexagonal Architecture - this is the ADAPTER that implements the PORT
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoWinLossMetricRepository implements WinLossMetricRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public WinLossMetric save(WinLossMetric metric) {
        return mongoTemplate.save(metric);
    }

    @Override
    public List<WinLossMetric> saveAll(List<WinLossMetric> metrics) {
        return metrics.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<WinLossMetric> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, WinLossMetric.class));
    }

    @Override
    public Optional<WinLossMetric> findByWinLossMetricIdAndTenantId(String metricId, String tenantId) {
        Query query = Query.query(
                Criteria.where("winLossMetricId").is(metricId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, WinLossMetric.class));
    }

    @Override
    public List<WinLossMetric> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, WinLossMetric.class);
    }

    @Override
    public List<WinLossMetric> findByTenantIdAndEntityType(String tenantId, String entityType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("entityType").is(entityType)
        );
        return mongoTemplate.find(query, WinLossMetric.class);
    }

    @Override
    public List<WinLossMetric> findByTenantIdAndEntityTypeAndEntityId(
            String tenantId, String entityType, String entityId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("entityType").is(entityType)
                        .and("entityId").is(entityId)
        );
        return mongoTemplate.find(query, WinLossMetric.class);
    }

    @Override
    public List<WinLossMetric> findByTenantIdAndPeriodBetween(
            String tenantId, LocalDate startDate, LocalDate endDate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("periodStartDate").gte(startDate)
                        .and("periodEndDate").lte(endDate)
        );
        return mongoTemplate.find(query, WinLossMetric.class);
    }

    @Override
    public List<WinLossMetric> findByTenantIdAndPeriod(String tenantId, WinLossMetric.MetricPeriod period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
        );
        return mongoTemplate.find(query, WinLossMetric.class);
    }

    @Override
    public List<WinLossMetric> findByTenantIdOrderByWinRateDesc(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
        ).with(Sort.by(Sort.Direction.DESC, "winRate"));
        return mongoTemplate.find(query, WinLossMetric.class);
    }

    @Override
    public List<WinLossMetric> findByTenantIdAndWinRateLessThan(String tenantId, BigDecimal winRate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("winRate").lt(winRate)
        );
        return mongoTemplate.find(query, WinLossMetric.class);
    }

    @Override
    public List<WinLossMetric> findByTenantIdAndWinRateGreaterThanEqual(String tenantId, BigDecimal winRate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("winRate").gte(winRate)
        );
        return mongoTemplate.find(query, WinLossMetric.class);
    }

    @Override
    public Optional<WinLossMetric> findLatestByTenantIdAndEntity(
            String tenantId, String entityType, String entityId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("entityType").is(entityType)
                        .and("entityId").is(entityId)
        ).with(Sort.by(Sort.Direction.DESC, "calculatedAt"))
         .limit(1);
        return Optional.ofNullable(mongoTemplate.findOne(query, WinLossMetric.class));
    }

    @Override
    public List<WinLossMetric> findLowestPerformersByTenantIdAndEntityType(
            String tenantId, String entityType, int limit) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("entityType").is(entityType)
        ).with(Sort.by(Sort.Direction.ASC, "winRate"))
         .limit(limit);
        return mongoTemplate.find(query, WinLossMetric.class);
    }

    @Override
    public List<WinLossMetric> findHighestPerformersByTenantIdAndEntityType(
            String tenantId, String entityType, int limit) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("entityType").is(entityType)
        ).with(Sort.by(Sort.Direction.DESC, "winRate"))
         .limit(limit);
        return mongoTemplate.find(query, WinLossMetric.class);
    }

    @Override
    public boolean existsByWinLossMetricIdAndTenantId(String metricId, String tenantId) {
        Query query = Query.query(
                Criteria.where("winLossMetricId").is(metricId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, WinLossMetric.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), WinLossMetric.class);
    }

    @Override
    public void deleteByWinLossMetricIdAndTenantId(String metricId, String tenantId) {
        Query query = Query.query(
                Criteria.where("winLossMetricId").is(metricId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, WinLossMetric.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, WinLossMetric.class);
    }

    @Override
    public void deleteByTenantIdAndPeriod(String tenantId, WinLossMetric.MetricPeriod period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
        );
        mongoTemplate.remove(query, WinLossMetric.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, WinLossMetric.class);
    }

    @Override
    public long countByTenantIdAndEntityType(String tenantId, String entityType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("entityType").is(entityType)
        );
        return mongoTemplate.count(query, WinLossMetric.class);
    }
}
