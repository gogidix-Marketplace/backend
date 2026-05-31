package com.gogidix.finance.globalfinancedashboard.infrastructure.persistence.mongo;

import com.gogidix.finance.globalfinancedashboard.domain.model.DashboardLayout;
import com.gogidix.finance.globalfinancedashboard.domain.repository.DashboardLayoutRepository;
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
 * MongoDB Repository Implementation - Dashboard Layout
 * Implements dashboard layout persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoDashboardLayoutRepository implements DashboardLayoutRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public DashboardLayout save(DashboardLayout layout) {
        log.debug("Saving dashboard layout: {} for tenant: {}",
                layout.getLayoutId(), layout.getTenantId());
        return mongoTemplate.save(layout);
    }

    @Override
    public List<DashboardLayout> saveAll(List<DashboardLayout> layouts) {
        return layouts.stream()
                .map(mongoTemplate::save)
                .toList();
    }

    @Override
    public Optional<DashboardLayout> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, DashboardLayout.class));
    }

    @Override
    public Optional<DashboardLayout> findByTenantIdAndLayoutId(String tenantId, String layoutId) {
        Query query = Query.query(
                Criteria.where("layoutId").is(layoutId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, DashboardLayout.class));
    }

    @Override
    public Optional<DashboardLayout> findByDashboardId(String tenantId, String dashboardId) {
        Query query = Query.query(
                Criteria.where("dashboardId").is(dashboardId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, DashboardLayout.class));
    }

    @Override
    public List<DashboardLayout> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, DashboardLayout.class);
    }

    @Override
    public List<DashboardLayout> findByTenantIdAndLayoutType(String tenantId,
                                                              DashboardLayout.LayoutType layoutType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("layoutType").is(layoutType)
        );
        return mongoTemplate.find(query, DashboardLayout.class);
    }

    @Override
    public Optional<DashboardLayout> findDefaultByDashboardId(String tenantId, String dashboardId) {
        Query query = Query.query(
                Criteria.where("dashboardId").is(dashboardId)
                        .and("tenantId").is(tenantId)
                        .and("isDefault").is(true)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, DashboardLayout.class));
    }

    @Override
    public boolean existsByTenantIdAndLayoutId(String tenantId, String layoutId) {
        Query query = Query.query(
                Criteria.where("layoutId").is(layoutId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, DashboardLayout.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), DashboardLayout.class);
    }

    @Override
    public void deleteByTenantIdAndLayoutId(String tenantId, String layoutId) {
        Query query = Query.query(
                Criteria.where("layoutId").is(layoutId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, DashboardLayout.class);
    }

    @Override
    public void deleteAllByDashboardId(String tenantId, String dashboardId) {
        Query query = Query.query(
                Criteria.where("dashboardId").is(dashboardId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, DashboardLayout.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, DashboardLayout.class);
    }
}
