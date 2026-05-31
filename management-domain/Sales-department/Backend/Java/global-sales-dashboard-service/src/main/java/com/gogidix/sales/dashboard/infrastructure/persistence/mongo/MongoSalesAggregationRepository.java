package com.gogidix.sales.dashboard.infrastructure.persistence.mongo;

import com.gogidix.sales.dashboard.domain.model.SalesAggregation;
import com.gogidix.sales.dashboard.domain.repository.SalesAggregationRepository;
import com.gogidix.sales.dashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Sales Aggregation
 * Implements aggregation persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoSalesAggregationRepository implements SalesAggregationRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public SalesAggregation save(SalesAggregation aggregation) {
        log.debug("Saving aggregation: {} for tenant: {}",
                aggregation.getAggregationId(), aggregation.getTenantId());
        return mongoTemplate.save(aggregation);
    }

    @Override
    public List<SalesAggregation> saveAll(List<SalesAggregation> aggregations) {
        return aggregations.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<SalesAggregation> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, SalesAggregation.class));
    }

    @Override
    public Optional<SalesAggregation> findByAggregationIdAndTenantId(String aggregationId, String tenantId) {
        Query query = Query.query(
                Criteria.where("aggregationId").is(aggregationId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, SalesAggregation.class));
    }

    @Override
    public List<SalesAggregation> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, SalesAggregation.class);
    }

    @Override
    public List<SalesAggregation> findByTenantIdAndAggregationType(String tenantId,
                                                                   SalesAggregation.AggregationType type) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("aggregationType").is(type)
        );
        return mongoTemplate.find(query, SalesAggregation.class);
    }

    @Override
    public List<SalesAggregation> findByTenantIdAndDimension(String tenantId,
                                                             SalesAggregation.AggregationDimension dimension) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("dimension").is(dimension)
        );
        return mongoTemplate.find(query, SalesAggregation.class);
    }

    @Override
    public List<SalesAggregation> findByTenantIdAndDimensionAndValue(String tenantId,
                                                                      SalesAggregation.AggregationDimension dimension,
                                                                      String dimensionValue) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("dimension").is(dimension)
                        .and("dimensionValue").is(dimensionValue)
        );
        return mongoTemplate.find(query, SalesAggregation.class);
    }

    @Override
    public List<SalesAggregation> findByTenantIdAndTimePeriod(String tenantId,
                                                              SalesAggregation.TimePeriod timePeriod) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("timePeriod.startDate").gte(timePeriod.getStartDate())
                        .and("timePeriod.endDate").lte(timePeriod.getEndDate())
        );
        return mongoTemplate.find(query, SalesAggregation.class);
    }

    @Override
    public List<SalesAggregation> findByTenantIdAndAggregationTypeAndTimePeriod(
            String tenantId,
            SalesAggregation.AggregationType type,
            SalesAggregation.TimePeriod timePeriod) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("aggregationType").is(type)
                        .and("timePeriod.startDate").gte(timePeriod.getStartDate())
                        .and("timePeriod.endDate").lte(timePeriod.getEndDate())
        );
        return mongoTemplate.find(query, SalesAggregation.class);
    }

    @Override
    public List<SalesAggregation> findByTenantIdAndDateRange(String tenantId,
                                                             LocalDate startDate,
                                                             LocalDate endDate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("timePeriod.startDate").gte(startDate)
                        .and("timePeriod.endDate").lte(endDate)
        );
        return mongoTemplate.find(query, SalesAggregation.class);
    }

    @Override
    public List<SalesAggregation> findLatestByTenantIdAndType(String tenantId,
                                                              SalesAggregation.AggregationType type,
                                                              int limit) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("aggregationType").is(type)
        ).with(PageRequest.of(0, limit));
        return mongoTemplate.find(query, SalesAggregation.class);
    }

    @Override
    public List<SalesAggregation> findByTenantIdAndIsComplete(String tenantId, Boolean isComplete) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("isComplete").is(isComplete)
        );
        return mongoTemplate.find(query, SalesAggregation.class);
    }

    @Override
    public boolean existsByAggregationIdAndTenantId(String aggregationId, String tenantId) {
        Query query = Query.query(
                Criteria.where("aggregationId").is(aggregationId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, SalesAggregation.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), SalesAggregation.class);
    }

    @Override
    public void deleteByAggregationIdAndTenantId(String aggregationId, String tenantId) {
        Query query = Query.query(
                Criteria.where("aggregationId").is(aggregationId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, SalesAggregation.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, SalesAggregation.class);
    }

    @Override
    public void deleteOldAggregations(String tenantId, LocalDate beforeDate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("timePeriod.endDate").lt(beforeDate)
        );
        mongoTemplate.remove(query, SalesAggregation.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, SalesAggregation.class);
    }

    @Override
    public long countByTenantIdAndAggregationType(String tenantId, SalesAggregation.AggregationType type) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("aggregationType").is(type)
        );
        return mongoTemplate.count(query, SalesAggregation.class);
    }
}
