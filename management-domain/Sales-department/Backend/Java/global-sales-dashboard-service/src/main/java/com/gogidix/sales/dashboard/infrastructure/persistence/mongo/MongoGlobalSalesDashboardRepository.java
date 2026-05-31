package com.gogidix.sales.dashboard.infrastructure.persistence.mongo;

import com.gogidix.sales.dashboard.domain.model.GlobalSalesDashboard;
import com.gogidix.sales.dashboard.domain.repository.GlobalSalesDashboardRepository;
import com.gogidix.sales.dashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Global Sales Dashboard
 * Implements dashboard persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoGlobalSalesDashboardRepository implements GlobalSalesDashboardRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public GlobalSalesDashboard save(GlobalSalesDashboard dashboard) {
        log.debug("Saving dashboard: {} for tenant: {}",
                dashboard.getDashboardId(), dashboard.getTenantId());
        return mongoTemplate.save(dashboard);
    }

    @Override
    public List<GlobalSalesDashboard> saveAll(List<GlobalSalesDashboard> dashboards) {
        return dashboards.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<GlobalSalesDashboard> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, GlobalSalesDashboard.class));
    }

    @Override
    public Optional<GlobalSalesDashboard> findByDashboardIdAndTenantId(String dashboardId, String tenantId) {
        Query query = Query.query(
                Criteria.where("dashboardId").is(dashboardId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, GlobalSalesDashboard.class));
    }

    @Override
    public List<GlobalSalesDashboard> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, GlobalSalesDashboard.class);
    }

    @Override
    public List<GlobalSalesDashboard> findByTenantIdAndStatus(String tenantId,
                                                                 GlobalSalesDashboard.DashboardStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.find(query, GlobalSalesDashboard.class);
    }

    @Override
    public List<GlobalSalesDashboard> findByTenantIdAndType(String tenantId,
                                                               GlobalSalesDashboard.DashboardType type) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("type").is(type)
        );
        return mongoTemplate.find(query, GlobalSalesDashboard.class);
    }

    @Override
    public List<GlobalSalesDashboard> findByTenantIdAndTypeAndStatus(String tenantId,
                                                                        GlobalSalesDashboard.DashboardType type,
                                                                        GlobalSalesDashboard.DashboardStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("type").is(type)
                        .and("status").is(status)
        );
        return mongoTemplate.find(query, GlobalSalesDashboard.class);
    }

    @Override
    public List<GlobalSalesDashboard> findByTenantIdAndNameContaining(String tenantId, String name) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("name").regex(name, "i")
        );
        return mongoTemplate.find(query, GlobalSalesDashboard.class);
    }

    @Override
    public List<GlobalSalesDashboard> findByTenantIdAndCreatedBy(String tenantId, String userId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("createdBy").is(userId)
        );
        return mongoTemplate.find(query, GlobalSalesDashboard.class);
    }

    @Override
    public boolean existsByDashboardIdAndTenantId(String dashboardId, String tenantId) {
        Query query = Query.query(
                Criteria.where("dashboardId").is(dashboardId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, GlobalSalesDashboard.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), GlobalSalesDashboard.class);
    }

    @Override
    public void deleteByDashboardIdAndTenantId(String dashboardId, String tenantId) {
        Query query = Query.query(
                Criteria.where("dashboardId").is(dashboardId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, GlobalSalesDashboard.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, GlobalSalesDashboard.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, GlobalSalesDashboard.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, GlobalSalesDashboard.DashboardStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.count(query, GlobalSalesDashboard.class);
    }

    @Override
    public List<GlobalSalesDashboard> findActiveDashboardsForTenant(String tenantId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(GlobalSalesDashboard.DashboardStatus.ACTIVE)
        );
        return mongoTemplate.find(query, GlobalSalesDashboard.class);
    }
}
