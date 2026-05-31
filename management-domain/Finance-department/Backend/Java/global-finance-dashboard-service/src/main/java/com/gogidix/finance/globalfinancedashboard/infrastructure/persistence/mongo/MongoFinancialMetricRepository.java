package com.gogidix.finance.globalfinancedashboard.infrastructure.persistence.mongo;

import com.gogidix.finance.globalfinancedashboard.domain.model.FinancialMetric;
import com.gogidix.finance.globalfinancedashboard.domain.repository.FinancialMetricRepository;
import com.gogidix.finance.globalfinancedashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository implementation for Financial Metric
 * Following Hexagonal Architecture - this is the ADAPTER that implements the PORT
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoFinancialMetricRepository implements FinancialMetricRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public FinancialMetric save(FinancialMetric metric) {
        return mongoTemplate.save(metric);
    }

    @Override
    public Optional<FinancialMetric> findByIdAndTenantId(String id, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, FinancialMetric.class));
    }

    @Override
    public List<FinancialMetric> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, FinancialMetric.class);
    }

    @Override
    public List<FinancialMetric> findByTenantIdAndMetricType(String tenantId, FinancialMetric.MetricType metricType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("metricType").is(metricType)
        );
        return mongoTemplate.find(query, FinancialMetric.class);
    }

    @Override
    public List<FinancialMetric> findByTenantIdAndPeriodBetween(String tenantId, String startPeriod, String endPeriod) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("period").gte(startPeriod).lte(endPeriod)
        );
        return mongoTemplate.find(query, FinancialMetric.class);
    }

    @Override
    public List<FinancialMetric> findByTenantIdAndRegionAndPeriod(String tenantId, String region, String period) {
        Criteria criteria = Criteria.where("tenantId").is(tenantId)
                .and("period").is(period);

        if (region != null && !region.isBlank()) {
            criteria.and("region").is(region);
        }

        Query query = Query.query(criteria);
        return mongoTemplate.find(query, FinancialMetric.class);
    }

    @Override
    public void deleteByIdAndTenantId(String id, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, FinancialMetric.class);
    }

    @Override
    public boolean existsByIdAndTenantId(String id, String tenantId) {
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, FinancialMetric.class);
    }
}
