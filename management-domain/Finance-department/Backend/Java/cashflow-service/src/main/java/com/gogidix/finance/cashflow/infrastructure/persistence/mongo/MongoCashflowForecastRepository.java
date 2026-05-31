package com.gogidix.finance.cashflow.infrastructure.persistence.mongo;

import com.gogidix.finance.cashflow.domain.model.CashflowForecast;
import com.gogidix.finance.cashflow.domain.repository.CashflowForecastRepository;
import com.gogidix.finance.cashflow.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Cashflow Forecast
 * Implements cashflow forecast persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoCashflowForecastRepository implements CashflowForecastRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public CashflowForecast save(CashflowForecast forecast) {
        log.debug("Saving cashflow forecast: {} for tenant: {}",
                forecast.getForecastId(), forecast.getTenantId());
        return mongoTemplate.save(forecast);
    }

    @Override
    public List<CashflowForecast> saveAll(List<CashflowForecast> forecasts) {
        return forecasts.stream()
                .map(mongoTemplate::save)
                .toList();
    }

    @Override
    public Optional<CashflowForecast> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, CashflowForecast.class));
    }

    @Override
    public Optional<CashflowForecast> findByForecastIdAndTenantId(String forecastId, String tenantId) {
        Query query = Query.query(
                Criteria.where("forecastId").is(forecastId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, CashflowForecast.class));
    }

    @Override
    public List<CashflowForecast> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, CashflowForecast.class);
    }

    @Override
    public List<CashflowForecast> findByTenantIdAndScenario(String tenantId, CashflowForecast.ForecastScenario scenario) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("scenario").is(scenario)
        );
        return mongoTemplate.find(query, CashflowForecast.class);
    }

    @Override
    public List<CashflowForecast> findByTenantIdAndStatus(String tenantId, CashflowForecast.ForecastStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.find(query, CashflowForecast.class);
    }

    @Override
    public List<CashflowForecast> findByTenantIdAndDateRange(
            String tenantId, LocalDate startDate, LocalDate endDate) {
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("startDate").lte(end)
                        .and("endDate").gte(start)
        );
        return mongoTemplate.find(query, CashflowForecast.class);
    }

    @Override
    public List<CashflowForecast> findByTenantIdAndIsBaselineTrue(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("isBaseline").is(true)
        );
        return mongoTemplate.find(query, CashflowForecast.class);
    }

    @Override
    public List<CashflowForecast> findByTenantIdAndParentForecastId(String tenantId, String parentForecastId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("parentForecastId").is(parentForecastId)
        );
        return mongoTemplate.find(query, CashflowForecast.class);
    }

    @Override
    public List<CashflowForecast> findByTenantIdOrderByVersionDesc(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId))
                .with(Sort.by(Sort.Direction.DESC, "version"));
        return mongoTemplate.find(query, CashflowForecast.class);
    }

    @Override
    public Optional<CashflowForecast> findLatestByTenantIdAndScenario(
            String tenantId, CashflowForecast.ForecastScenario scenario) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("scenario").is(scenario)
        ).with(Sort.by(Sort.Direction.DESC, "version")).limit(1);

        return Optional.ofNullable(mongoTemplate.findOne(query, CashflowForecast.class));
    }

    @Override
    public List<CashflowForecast> findByTenantIdAndGeneratedBy(String tenantId, String generatedBy) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("generatedBy").is(generatedBy)
        );
        return mongoTemplate.find(query, CashflowForecast.class);
    }

    @Override
    public List<CashflowForecast> findByTenantIdAndStartDateBeforeAndEndDateAfter(
            String tenantId, LocalDate startDate, LocalDate endDate) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("startDate").lte(endDate)
                        .and("endDate").gte(startDate)
        );
        return mongoTemplate.find(query, CashflowForecast.class);
    }

    @Override
    public List<CashflowForecast> findByTenantIdAndScenarioIn(
            String tenantId, List<CashflowForecast.ForecastScenario> scenarios) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("scenario").in(scenarios)
        );
        return mongoTemplate.find(query, CashflowForecast.class);
    }

    @Override
    public List<CashflowForecast> findByTenantIdAndStatusIn(
            String tenantId, List<CashflowForecast.ForecastStatus> statuses) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").in(statuses)
        );
        return mongoTemplate.find(query, CashflowForecast.class);
    }

    @Override
    public boolean existsByForecastIdAndTenantId(String forecastId, String tenantId) {
        Query query = Query.query(
                Criteria.where("forecastId").is(forecastId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, CashflowForecast.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), CashflowForecast.class);
    }

    @Override
    public void deleteByForecastIdAndTenantId(String forecastId, String tenantId) {
        Query query = Query.query(
                Criteria.where("forecastId").is(forecastId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, CashflowForecast.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, CashflowForecast.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, CashflowForecast.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, CashflowForecast.ForecastStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.count(query, CashflowForecast.class);
    }

    @Override
    public long countByTenantIdAndScenario(String tenantId, CashflowForecast.ForecastScenario scenario) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("scenario").is(scenario)
        );
        return mongoTemplate.count(query, CashflowForecast.class);
    }

    @Override
    public List<CashflowForecast> findByTenantIdAndTagsContaining(String tenantId, String tag) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("tags").is(tag)
        );
        return mongoTemplate.find(query, CashflowForecast.class);
    }

    @Override
    public List<CashflowForecast> findActiveForecastsByTenantId(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").in(List.of(
                                CashflowForecast.ForecastStatus.DRAFT,
                                CashflowForecast.ForecastStatus.GENERATING,
                                CashflowForecast.ForecastStatus.GENERATED
                        ))
        );
        return mongoTemplate.find(query, CashflowForecast.class);
    }
}
