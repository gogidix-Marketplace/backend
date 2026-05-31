package com.gogidix.sales.dashboard.infrastructure.persistence.mongo;

import com.gogidix.sales.dashboard.domain.model.MetricRollup;
import com.gogidix.sales.dashboard.domain.repository.MetricRollupRepository;
import com.gogidix.sales.dashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Metric Rollup
 * Implements rollup persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoMetricRollupRepository implements MetricRollupRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public MetricRollup save(MetricRollup rollup) {
        log.debug("Saving rollup: {} for tenant: {}",
                rollup.getRollupId(), rollup.getTenantId());
        return mongoTemplate.save(rollup);
    }

    @Override
    public List<MetricRollup> saveAll(List<MetricRollup> rollups) {
        return rollups.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<MetricRollup> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, MetricRollup.class));
    }

    @Override
    public Optional<MetricRollup> findByRollupIdAndTenantId(String rollupId, String tenantId) {
        Query query = Query.query(
                Criteria.where("rollupId").is(rollupId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, MetricRollup.class));
    }

    @Override
    public List<MetricRollup> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, MetricRollup.class);
    }

    @Override
    public List<MetricRollup> findByTenantIdAndRollupType(String tenantId,
                                                          MetricRollup.RollupType type) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("rollupType").is(type)
        );
        return mongoTemplate.find(query, MetricRollup.class);
    }

    @Override
    public List<MetricRollup> findByTenantIdAndRollupKey(String tenantId, String rollupKey) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("rollupKey").is(rollupKey)
        );
        return mongoTemplate.find(query, MetricRollup.class);
    }

    @Override
    public List<MetricRollup> findByTenantIdAndParentRollupId(String tenantId, String parentRollupId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("parentRollupId").is(parentRollupId)
        );
        return mongoTemplate.find(query, MetricRollup.class);
    }

    @Override
    public List<MetricRollup> findByTenantIdAndTimePeriod(String tenantId,
                                                         MetricRollup.TimePeriod timePeriod) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("timePeriod.startDate").gte(timePeriod.getStartDate())
                        .and("timePeriod.endDate").lte(timePeriod.getEndDate())
        );
        return mongoTemplate.find(query, MetricRollup.class);
    }

    @Override
    public List<MetricRollup> findGlobalRollupByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("rollupType").is(MetricRollup.RollupType.GLOBAL)
        );
        return mongoTemplate.find(query, MetricRollup.class);
    }

    @Override
    public List<MetricRollup> findRegionalRollupsByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("rollupType").is(MetricRollup.RollupType.REGIONAL)
        );
        return mongoTemplate.find(query, MetricRollup.class);
    }

    @Override
    public List<MetricRollup> findByTenantIdAndRollupTypeAndTimePeriod(
            String tenantId,
            MetricRollup.RollupType type,
            MetricRollup.TimePeriod timePeriod) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("rollupType").is(type)
                        .and("timePeriod.startDate").gte(timePeriod.getStartDate())
                        .and("timePeriod.endDate").lte(timePeriod.getEndDate())
        );
        return mongoTemplate.find(query, MetricRollup.class);
    }

    @Override
    public List<MetricRollup> findChildRollups(String parentRollupId, String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("parentRollupId").is(parentRollupId)
        );
        return mongoTemplate.find(query, MetricRollup.class);
    }

    @Override
    public Optional<MetricRollup> findLatestByTypeAndKey(String tenantId,
                                                         MetricRollup.RollupType type,
                                                         String rollupKey) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("rollupType").is(type)
                        .and("rollupKey").is(rollupKey)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, MetricRollup.class));
    }

    @Override
    public List<MetricRollup> findByTenantIdAndDataVersion(String tenantId, Integer dataVersion) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("dataVersion").is(dataVersion)
        );
        return mongoTemplate.find(query, MetricRollup.class);
    }

    @Override
    public boolean existsByRollupIdAndTenantId(String rollupId, String tenantId) {
        Query query = Query.query(
                Criteria.where("rollupId").is(rollupId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, MetricRollup.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), MetricRollup.class);
    }

    @Override
    public void deleteByRollupIdAndTenantId(String rollupId, String tenantId) {
        Query query = Query.query(
                Criteria.where("rollupId").is(rollupId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, MetricRollup.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, MetricRollup.class);
    }

    @Override
    public void deleteOldRollups(String tenantId, LocalDate beforeDate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("timePeriod.endDate").lt(beforeDate)
        );
        mongoTemplate.remove(query, MetricRollup.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, MetricRollup.class);
    }

    @Override
    public long countByTenantIdAndRollupType(String tenantId, MetricRollup.RollupType type) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("rollupType").is(type)
        );
        return mongoTemplate.count(query, MetricRollup.class);
    }

    @Override
    public List<MetricRollup> findRealtimeRollupsByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("isRealtime").is(true)
        );
        return mongoTemplate.find(query, MetricRollup.class);
    }
}
