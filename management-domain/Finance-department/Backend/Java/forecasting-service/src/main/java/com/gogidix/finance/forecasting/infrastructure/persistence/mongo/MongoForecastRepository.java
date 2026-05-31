package com.gogidix.finance.forecasting.infrastructure.persistence.mongo;

import com.gogidix.finance.forecasting.domain.model.Forecast;
import com.gogidix.finance.forecasting.domain.repository.ForecastRepository;
import com.gogidix.finance.forecasting.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
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
        List<Forecast> result = new java.util.ArrayList<>();
        for (Forecast forecast : forecasts) {
            result.add(mongoTemplate.save(forecast));
        }
        return result;
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
    public List<Forecast> findByTenantIdAndForecastType(String tenantId, Forecast.ForecastType forecastType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("forecastType").is(forecastType)
        );
        return mongoTemplate.find(query, Forecast.class);
    }

    @Override
    public List<Forecast> findByTenantIdAndForecastHorizon(String tenantId, Forecast.ForecastHorizon forecastHorizon) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("forecastHorizon").is(forecastHorizon)
        );
        return mongoTemplate.find(query, Forecast.class);
    }

    @Override
    public List<Forecast> findByTenantIdAndStartDateBetween(String tenantId, Instant startDate, Instant endDate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("startDate").gte(startDate).lte(endDate)
        );
        return mongoTemplate.find(query, Forecast.class);
    }

    @Override
    public List<Forecast> findByTenantIdAndStatusAndForecastType(String tenantId, Forecast.ForecastStatus status,
                                                                  Forecast.ForecastType forecastType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
                        .and("forecastType").is(forecastType)
        );
        return mongoTemplate.find(query, Forecast.class);
    }

    @Override
    public List<Forecast> findByTenantIdAndDepartment(String tenantId, String department) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("department").is(department)
        );
        return mongoTemplate.find(query, Forecast.class);
    }

    @Override
    public List<Forecast> findByTenantIdAndScenario(String tenantId, String scenario) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("scenario").is(scenario)
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
    public List<Forecast> findPendingApprovalByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(Forecast.ForecastStatus.PENDING_APPROVAL)
        );
        return mongoTemplate.find(query, Forecast.class);
    }

    @Override
    public List<Forecast> findPendingApprovalByTenantIdAndDepartment(String tenantId, String department) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(Forecast.ForecastStatus.PENDING_APPROVAL)
                        .and("department").is(department)
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
    public BigDecimal sumTotalForecastAmountByTenantIdAndStatus(String tenantId, Forecast.ForecastStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );

        List<Forecast> forecasts = mongoTemplate.find(query, Forecast.class);
        return forecasts.stream()
                .map(Forecast::getTotalForecastAmount)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<Forecast> findArchivedByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(Forecast.ForecastStatus.ARCHIVED)
        );
        return mongoTemplate.find(query, Forecast.class);
    }

    @Override
    public List<Forecast> findLatestByTenantId(String tenantId, int limit) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId))
                .with(org.springframework.data.domain.PageRequest.of(0, limit,
                        org.springframework.data.domain.Sort.by(
                                org.springframework.data.domain.Sort.Order.desc("createdAt"))));
        return mongoTemplate.find(query, Forecast.class);
    }

    @Override
    public List<Forecast> searchByTenantId(String tenantId, String searchTerm) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage()
                .matching(searchTerm);

        Query query = TextQuery.queryText(textCriteria)
                .addCriteria(Criteria.where("tenantId").is(tenantId));

        return mongoTemplate.find(query, Forecast.class);
    }

    @Override
    public List<Forecast> findByTenantIdAndForecastTypeAndStartDateBetween(
            String tenantId, Forecast.ForecastType forecastType, Instant startDate, Instant endDate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("forecastType").is(forecastType)
                        .and("startDate").gte(startDate).lte(endDate)
        );
        return mongoTemplate.find(query, Forecast.class);
    }

    @Override
    public List<Forecast> findByTenantIdAndCategory(String tenantId, String category) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("category").is(category)
        );
        return mongoTemplate.find(query, Forecast.class);
    }

    @Override
    public List<Forecast> findByTenantIdAndForecastIdIn(String tenantId, List<String> forecastIds) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("forecastId").in(forecastIds)
        );
        return mongoTemplate.find(query, Forecast.class);
    }

    @Override
    public List<Forecast> findByTenantIdAndConfidenceLevelGreaterThanEqual(String tenantId, Integer confidenceLevel) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("confidenceLevel").gte(confidenceLevel)
        );
        return mongoTemplate.find(query, Forecast.class);
    }
}
