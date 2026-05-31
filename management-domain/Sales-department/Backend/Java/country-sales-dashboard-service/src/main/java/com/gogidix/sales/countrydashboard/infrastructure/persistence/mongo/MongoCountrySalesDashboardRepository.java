package com.gogidix.sales.countrydashboard.infrastructure.persistence.mongo;

import com.gogidix.sales.countrydashboard.domain.model.CountrySalesDashboard;
import com.gogidix.sales.countrydashboard.domain.repository.CountrySalesDashboardRepository;
import com.gogidix.sales.countrydashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Country Sales Dashboard
 * Implements dashboard persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoCountrySalesDashboardRepository implements CountrySalesDashboardRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public CountrySalesDashboard save(CountrySalesDashboard dashboard) {
        log.debug("Saving dashboard: {} for tenant: {}",
                dashboard.getDashboardId(), dashboard.getTenantId());
        return mongoTemplate.save(dashboard);
    }

    @Override
    public List<CountrySalesDashboard> saveAll(List<CountrySalesDashboard> dashboards) {
        return dashboards.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<CountrySalesDashboard> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, CountrySalesDashboard.class));
    }

    @Override
    public Optional<CountrySalesDashboard> findByDashboardIdAndTenantId(String dashboardId, String tenantId) {
        Query query = Query.query(
                Criteria.where("dashboardId").is(dashboardId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, CountrySalesDashboard.class));
    }

    @Override
    public Optional<CountrySalesDashboard> findByCountryCodeAndTenantId(String countryCode, String tenantId) {
        Query query = Query.query(
                Criteria.where("countryCode").is(countryCode)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, CountrySalesDashboard.class));
    }

    @Override
    public List<CountrySalesDashboard> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, CountrySalesDashboard.class);
    }

    @Override
    public Page<CountrySalesDashboard> findByTenantId(String tenantId, Pageable pageable) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        query.with(pageable);

        List<CountrySalesDashboard> results = mongoTemplate.find(query, CountrySalesDashboard.class);

        Query countQuery = Query.query(Criteria.where("tenantId").is(tenantId));
        long total = mongoTemplate.count(countQuery, CountrySalesDashboard.class);

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public List<CountrySalesDashboard> findByTenantIdAndStatus(String tenantId,
                                                                 CountrySalesDashboard.DashboardStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.find(query, CountrySalesDashboard.class);
    }

    @Override
    public List<CountrySalesDashboard> findByTenantIdAndRegion(String tenantId, String region) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("region").is(region)
        );
        return mongoTemplate.find(query, CountrySalesDashboard.class);
    }

    @Override
    public List<CountrySalesDashboard> findByMultipleCountryCodes(String tenantId, List<String> countryCodes) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("countryCode").in(countryCodes)
        );
        return mongoTemplate.find(query, CountrySalesDashboard.class);
    }

    @Override
    public boolean existsByDashboardIdAndTenantId(String dashboardId, String tenantId) {
        Query query = Query.query(
                Criteria.where("dashboardId").is(dashboardId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, CountrySalesDashboard.class);
    }

    @Override
    public boolean existsByCountryCodeAndTenantId(String countryCode, String tenantId) {
        Query query = Query.query(
                Criteria.where("countryCode").is(countryCode)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, CountrySalesDashboard.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), CountrySalesDashboard.class);
    }

    @Override
    public void deleteByDashboardIdAndTenantId(String dashboardId, String tenantId) {
        Query query = Query.query(
                Criteria.where("dashboardId").is(dashboardId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, CountrySalesDashboard.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, CountrySalesDashboard.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, CountrySalesDashboard.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, CountrySalesDashboard.DashboardStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.count(query, CountrySalesDashboard.class);
    }

    @Override
    public List<CountrySalesDashboard> findActiveDashboardsForTenant(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(CountrySalesDashboard.DashboardStatus.ACTIVE)
        );
        return mongoTemplate.find(query, CountrySalesDashboard.class);
    }

    @Override
    public List<CountrySalesDashboard> findByRegionAndTenantId(String region, String tenantId) {
        Query query = Query.query(
                Criteria.where("region").is(region)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, CountrySalesDashboard.class);
    }
}
