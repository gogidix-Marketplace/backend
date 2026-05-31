package com.gogidix.sales.analytics.infrastructure.persistence.mongo;

import com.gogidix.sales.analytics.domain.model.DashboardWidget;
import com.gogidix.sales.analytics.domain.repository.DashboardWidgetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository implementation for Dashboard Widget
 * Following Hexagonal Architecture - this is the ADAPTER that implements the PORT
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoDashboardWidgetRepository implements DashboardWidgetRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public DashboardWidget save(DashboardWidget widget) {
        return mongoTemplate.save(widget);
    }

    @Override
    public List<DashboardWidget> saveAll(List<DashboardWidget> widgets) {
        return widgets.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<DashboardWidget> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, DashboardWidget.class));
    }

    @Override
    public Optional<DashboardWidget> findByWidgetIdAndTenantId(String widgetId, String tenantId) {
        Query query = Query.query(
                Criteria.where("widgetId").is(widgetId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, DashboardWidget.class));
    }

    @Override
    public List<DashboardWidget> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, DashboardWidget.class);
    }

    @Override
    public List<DashboardWidget> findByTenantIdAndDashboardId(String tenantId, String dashboardId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("dashboardId").is(dashboardId)
        );
        return mongoTemplate.find(query, DashboardWidget.class);
    }

    @Override
    public List<DashboardWidget> findByTenantIdAndDashboardIdOrderByDisplayOrderAsc(String tenantId, String dashboardId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("dashboardId").is(dashboardId)
        ).with(Sort.by(Sort.Direction.ASC, "displayOrder"));
        return mongoTemplate.find(query, DashboardWidget.class);
    }

    @Override
    public List<DashboardWidget> findByTenantIdAndWidgetType(String tenantId, DashboardWidget.WidgetType widgetType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("widgetType").is(widgetType)
        );
        return mongoTemplate.find(query, DashboardWidget.class);
    }

    @Override
    public List<DashboardWidget> findByTenantIdAndIsVisible(String tenantId, Boolean isVisible) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("isVisible").is(isVisible)
        );
        return mongoTemplate.find(query, DashboardWidget.class);
    }

    @Override
    public List<DashboardWidget> findByTenantIdAndCreatedBy(String tenantId, String createdBy) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("createdBy").is(createdBy)
        );
        return mongoTemplate.find(query, DashboardWidget.class);
    }

    @Override
    public boolean existsByWidgetIdAndTenantId(String widgetId, String tenantId) {
        Query query = Query.query(
                Criteria.where("widgetId").is(widgetId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, DashboardWidget.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), DashboardWidget.class);
    }

    @Override
    public void deleteByWidgetIdAndTenantId(String widgetId, String tenantId) {
        Query query = Query.query(
                Criteria.where("widgetId").is(widgetId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, DashboardWidget.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, DashboardWidget.class);
    }

    @Override
    public void deleteAllByDashboardId(String dashboardId) {
        Query query = Query.query(Criteria.where("dashboardId").is(dashboardId));
        mongoTemplate.remove(query, DashboardWidget.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, DashboardWidget.class);
    }

    @Override
    public long countByTenantIdAndDashboardId(String tenantId, String dashboardId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("dashboardId").is(dashboardId)
        );
        return mongoTemplate.count(query, DashboardWidget.class);
    }
}
