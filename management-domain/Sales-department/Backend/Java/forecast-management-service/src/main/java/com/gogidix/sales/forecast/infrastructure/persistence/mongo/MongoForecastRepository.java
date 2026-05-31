package com.gogidix.sales.forecast.infrastructure.persistence.mongo;

import com.gogidix.sales.forecast.domain.model.Forecast;
import com.gogidix.sales.forecast.domain.repository.ForecastRepository;
import com.gogidix.sales.forecast.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Forecast
 * Implements forecast persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoForecastRepository implements ForecastRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Forecast save(Forecast forecast) {
        log.debug("Saving forecast: {} for tenant: {}",
            forecast.getForecastId(), forecast.getTenantId());
        return mongoTemplate.save(forecast);
    }

    @Override
    public List<Forecast> saveAll(List<Forecast> forecasts) {
        return forecasts.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<Forecast> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
            Criteria.where("id").is(id)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Forecast.class));
    }

    @Override
    public Optional<Forecast> findByForecastIdAndTenantId(String forecastId, String tenantId) {
        Query query = Query.query(
            Criteria.where("forecastId").is(forecastId)
                .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Forecast.class));
    }

    @Override
    public List<Forecast> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, Forecast.class);
    }

    @Override
    public List<Forecast> findByTenantIdAndStatus(String tenantId, Forecast.ForecastStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.find(query, Forecast.class);
    }

    @Override
    public List<Forecast> findByTenantIdAndPeriod(String tenantId, Forecast.ForecastPeriod period) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("period").is(period)
        );
        return mongoTemplate.find(query, Forecast.class);
    }

    @Override
    public List<Forecast> findByTenantIdAndStartDateBetween(String tenantId, YearMonth startDate,
                                                             YearMonth endDate) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("startDate").gte(startDate)
                .and("endDate").lte(endDate)
        );
        return mongoTemplate.find(query, Forecast.class);
    }

    @Override
    public List<Forecast> findByTenantIdAndRegion(String tenantId, String region) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("region").is(region)
        );
        return mongoTemplate.find(query, Forecast.class);
    }

    @Override
    public List<Forecast> findByTenantIdAndTerritory(String tenantId, String territory) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("territory").is(territory)
        );
        return mongoTemplate.find(query, Forecast.class);
    }

    @Override
    public List<Forecast> findByTenantIdAndBusinessUnit(String tenantId, String businessUnit) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("businessUnit").is(businessUnit)
        );
        return mongoTemplate.find(query, Forecast.class);
    }

    @Override
    public List<Forecast> findByTenantIdAndCreatedBy(String tenantId, String createdBy) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("createdBy").is(createdBy)
        );
        return mongoTemplate.find(query, Forecast.class);
    }

    @Override
    public List<Forecast> findByParentForecastIdAndTenantId(String parentForecastId, String tenantId) {
        Query query = Query.query(
            Criteria.where("parentForecastId").is(parentForecastId)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, Forecast.class);
    }

    @Override
    public List<Forecast> findVersionsByForecastIdAndTenantId(String forecastId, String tenantId) {
        Query query = Query.query(
            Criteria.where("parentForecastId").is(forecastId)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, Forecast.class);
    }

    @Override
    public List<Forecast> findPendingApprovalByTenantIdAndApproverLevel(String tenantId,
                                                                         Forecast.ApprovalLevel approverLevel) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(Forecast.ForecastStatus.PENDING_APPROVAL)
                .and("currentApprovalLevel").is(approverLevel)
        );
        return mongoTemplate.find(query, Forecast.class);
    }

    @Override
    public boolean existsByForecastIdAndTenantId(String forecastId, String tenantId) {
        Query query = Query.query(
            Criteria.where("forecastId").is(forecastId)
                .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, Forecast.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), Forecast.class);
    }

    @Override
    public void deleteByForecastIdAndTenantId(String forecastId, String tenantId) {
        Query query = Query.query(
            Criteria.where("forecastId").is(forecastId)
                .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, Forecast.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, Forecast.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, Forecast.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, Forecast.ForecastStatus status) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(status)
        );
        return mongoTemplate.count(query, Forecast.class);
    }

    @Override
    public List<Forecast> findActiveByTenantId(String tenantId) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").in(List.of(
                    Forecast.ForecastStatus.DRAFT,
                    Forecast.ForecastStatus.SUBMITTED,
                    Forecast.ForecastStatus.PENDING_APPROVAL,
                    Forecast.ForecastStatus.APPROVED,
                    Forecast.ForecastStatus.PUBLISHED
                ))
        );
        return mongoTemplate.find(query, Forecast.class);
    }

    @Override
    public List<Forecast> findPublishedByTenantIdAndDateRange(String tenantId, YearMonth startDate,
                                                                YearMonth endDate) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(Forecast.ForecastStatus.PUBLISHED)
                .and("startDate").gte(startDate)
                .and("endDate").lte(endDate)
        );
        return mongoTemplate.find(query, Forecast.class);
    }

    @Override
    public Optional<Forecast> findLatestPublishedByTenantIdAndPeriod(String tenantId,
                                                                      Forecast.ForecastPeriod period) {
        Query query = Query.query(
            Criteria.where("tenantId").is(tenantId)
                .and("status").is(Forecast.ForecastStatus.PUBLISHED)
                .and("period").is(period)
        ).with(org.springframework.data.domain.Sort.by(
            org.springframework.data.domain.Sort.Direction.DESC, "createdAt"));
        return Optional.ofNullable(mongoTemplate.findOne(query, Forecast.class));
    }
}
