package com.gogidix.sales.analytics.infrastructure.persistence.mongo;

import com.gogidix.sales.analytics.domain.model.PipelineMetric;
import com.gogidix.sales.analytics.domain.repository.PipelineMetricRepository;
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
 * MongoDB Repository implementation for Pipeline Metric
 * Following Hexagonal Architecture - this is the ADAPTER that implements the PORT
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoPipelineMetricRepository implements PipelineMetricRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public PipelineMetric save(PipelineMetric metric) {
        return mongoTemplate.save(metric);
    }

    @Override
    public List<PipelineMetric> saveAll(List<PipelineMetric> metrics) {
        return metrics.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<PipelineMetric> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, PipelineMetric.class));
    }

    @Override
    public Optional<PipelineMetric> findByPipelineMetricIdAndTenantId(String metricId, String tenantId) {
        Query query = Query.query(
                Criteria.where("pipelineMetricId").is(metricId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, PipelineMetric.class));
    }

    @Override
    public List<PipelineMetric> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, PipelineMetric.class);
    }

    @Override
    public List<PipelineMetric> findByTenantIdAndEntityType(String tenantId, String entityType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("entityType").is(entityType)
        );
        return mongoTemplate.find(query, PipelineMetric.class);
    }

    @Override
    public List<PipelineMetric> findByTenantIdAndEntityTypeAndEntityId(
            String tenantId, String entityType, String entityId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("entityType").is(entityType)
                        .and("entityId").is(entityId)
        );
        return mongoTemplate.find(query, PipelineMetric.class);
    }

    @Override
    public List<PipelineMetric> findByTenantIdAndPeriodBetween(
            String tenantId, LocalDate startDate, LocalDate endDate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("periodStartDate").gte(startDate)
                        .and("periodEndDate").lte(endDate)
        );
        return mongoTemplate.find(query, PipelineMetric.class);
    }

    @Override
    public List<PipelineMetric> findByTenantIdAndPeriod(String tenantId, PipelineMetric.MetricPeriod period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
        );
        return mongoTemplate.find(query, PipelineMetric.class);
    }

    @Override
    public List<PipelineMetric> findByTenantIdAndHealth(String tenantId, PipelineMetric.PipelineHealth health) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("health").is(health)
        );
        return mongoTemplate.find(query, PipelineMetric.class);
    }

    @Override
    public List<PipelineMetric> findByTenantIdAndHealthScoreLessThan(String tenantId, Integer score) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("healthScore").lt(score)
        );
        return mongoTemplate.find(query, PipelineMetric.class);
    }

    @Override
    public Optional<PipelineMetric> findLatestByTenantIdAndEntity(
            String tenantId, String entityType, String entityId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("entityType").is(entityType)
                        .and("entityId").is(entityId)
        ).with(Sort.by(Sort.Direction.DESC, "calculatedAt"))
         .limit(1);
        return Optional.ofNullable(mongoTemplate.findOne(query, PipelineMetric.class));
    }

    @Override
    public List<PipelineMetric> findAtRiskPipelinesByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("health").in(PipelineMetric.PipelineHealth.ATTENTION_NEEDED,
                                          PipelineMetric.PipelineHealth.AT_RISK,
                                          PipelineMetric.PipelineHealth.CRITICAL)
        );
        return mongoTemplate.find(query, PipelineMetric.class);
    }

    @Override
    public List<PipelineMetric> findByTenantIdOrderByHealthScoreAsc(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
        ).with(Sort.by(Sort.Direction.ASC, "healthScore"));
        return mongoTemplate.find(query, PipelineMetric.class);
    }

    @Override
    public boolean existsByPipelineMetricIdAndTenantId(String metricId, String tenantId) {
        Query query = Query.query(
                Criteria.where("pipelineMetricId").is(metricId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, PipelineMetric.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), PipelineMetric.class);
    }

    @Override
    public void deleteByPipelineMetricIdAndTenantId(String metricId, String tenantId) {
        Query query = Query.query(
                Criteria.where("pipelineMetricId").is(metricId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, PipelineMetric.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, PipelineMetric.class);
    }

    @Override
    public void deleteByTenantIdAndPeriod(String tenantId, PipelineMetric.MetricPeriod period) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").is(period)
        );
        mongoTemplate.remove(query, PipelineMetric.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, PipelineMetric.class);
    }

    @Override
    public long countByTenantIdAndHealth(String tenantId, PipelineMetric.PipelineHealth health) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("health").is(health)
        );
        return mongoTemplate.count(query, PipelineMetric.class);
    }
}
