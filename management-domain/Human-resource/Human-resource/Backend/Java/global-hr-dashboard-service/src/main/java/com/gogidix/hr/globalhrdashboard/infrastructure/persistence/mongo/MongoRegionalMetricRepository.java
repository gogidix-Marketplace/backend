package com.gogidix.hr.globalhrdashboard.infrastructure.persistence.mongo;

import com.gogidix.hr.globalhrdashboard.domain.model.RegionalMetric;
import com.gogidix.hr.globalhrdashboard.domain.model.MetricCategory;
import com.gogidix.hr.globalhrdashboard.domain.repository.RegionalMetricRepository;
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
 * MongoDB Repository implementation for RegionalMetric
 * Following Hexagonal Architecture - this is the ADAPTER that implements the PORT
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoRegionalMetricRepository implements RegionalMetricRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public RegionalMetric save(RegionalMetric metric) {
        return mongoTemplate.save(metric);
    }

    @Override
    public Optional<RegionalMetric> findByIdAndTenantId(String id, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, RegionalMetric.class));
    }

    @Override
    public List<RegionalMetric> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, RegionalMetric.class);
    }

    @Override
    public List<RegionalMetric> findByTenantIdAndRegionCode(String tenantId, String regionCode) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("regionCode").is(regionCode)
        );
        return mongoTemplate.find(query, RegionalMetric.class);
    }

    @Override
    public Optional<RegionalMetric> findByTenantIdAndRegionCodeAndMetricNameAndPeriod(
            String tenantId, String regionCode, String metricName, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("regionCode").is(regionCode)
                        .and("metricName").is(metricName)
                        .and("period").is(period)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, RegionalMetric.class));
    }

    @Override
    public List<RegionalMetric> findByTenantIdAndPeriod(String tenantId, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
        );
        return mongoTemplate.find(query, RegionalMetric.class);
    }

    @Override
    public List<RegionalMetric> findByTenantIdAndRegionCodeAndPeriod(String tenantId, String regionCode, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("regionCode").is(regionCode)
                        .and("period").is(period)
        );
        return mongoTemplate.find(query, RegionalMetric.class);
    }

    @Override
    public List<RegionalMetric> findByTenantIdAndMetricCategory(String tenantId, MetricCategory metricCategory) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("metricCategory").is(metricCategory)
        );
        return mongoTemplate.find(query, RegionalMetric.class);
    }

    @Override
    public List<RegionalMetric> findActiveByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("isActive").is(true)
        );
        return mongoTemplate.find(query, RegionalMetric.class);
    }

    @Override
    public List<String> findDistinctRegionsByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
        );
        query.fields().include("regionCode");
        return mongoTemplate.findDistinct(query, "regionCode", RegionalMetric.class, String.class);
    }

    @Override
    public List<RegionalMetric> findByTenantIdAndRegionCodeAndMetricName(String tenantId, String regionCode, String metricName) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("regionCode").is(regionCode)
                        .and("metricName").is(metricName)
        );
        return mongoTemplate.find(query, RegionalMetric.class);
    }

    @Override
    public List<RegionalMetric> findByTenantIdAndLastUpdatedAfter(String tenantId, Instant lastUpdated) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("lastUpdated").gt(lastUpdated)
        );
        return mongoTemplate.find(query, RegionalMetric.class);
    }

    @Override
    public List<RegionalMetric> findMetricsNeedingUpdate(String tenantId, Instant before) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("lastUpdated").lt(before)
                        .orOperator(
                                Criteria.where("lastUpdated").exists(false)
                        )
        );
        return mongoTemplate.find(query, RegionalMetric.class);
    }

    @Override
    public void deleteByIdAndTenantId(String id, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, RegionalMetric.class);
    }

    @Override
    public void deleteByTenantIdAndRegionCodeAndPeriod(String tenantId, String regionCode, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("regionCode").is(regionCode)
                        .and("period").is(period)
        );
        mongoTemplate.remove(query, RegionalMetric.class);
    }

    @Override
    public boolean existsByIdAndTenantId(String id, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, RegionalMetric.class);
    }

    @Override
    public List<RegionalMetric> saveAll(List<RegionalMetric> metrics) {
        return metrics.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public long countByTenantIdAndRegionCode(String tenantId, String regionCode) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("regionCode").is(regionCode)
        );
        return mongoTemplate.count(query, RegionalMetric.class);
    }

    @Override
    public List<RegionalMetric> findTrendData(String tenantId, String regionCode, String metricName,
                                               String startPeriod, String endPeriod) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("regionCode").is(regionCode)
                        .and("metricName").is(metricName)
                        .and("period").gte(startPeriod).lte(endPeriod)
        ).with(Sort.by(Sort.Direction.ASC, "period"));
        return mongoTemplate.find(query, RegionalMetric.class);
    }

    @Override
    public List<RegionalMetric> findLatestByMetricNameAndPeriod(String tenantId, String metricName, String period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("metricName").is(metricName)
                        .and("period").is(period)
        );
        return mongoTemplate.find(query, RegionalMetric.class);
    }
}
