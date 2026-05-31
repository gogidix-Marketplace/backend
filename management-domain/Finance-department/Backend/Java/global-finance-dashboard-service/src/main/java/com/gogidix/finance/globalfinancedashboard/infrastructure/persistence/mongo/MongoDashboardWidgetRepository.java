package com.gogidix.finance.globalfinancedashboard.infrastructure.persistence.mongo;

import com.gogidix.finance.globalfinancedashboard.domain.model.DashboardWidget;
import com.gogidix.finance.globalfinancedashboard.domain.repository.DashboardWidgetRepository;
import com.gogidix.finance.globalfinancedashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Dashboard Widget
 * Implements dashboard widget persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoDashboardWidgetRepository implements DashboardWidgetRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public DashboardWidget save(DashboardWidget widget) {
        log.debug("Saving dashboard widget: {} for tenant: {}",
                widget.getWidgetId(), widget.getTenantId());
        return mongoTemplate.save(widget);
    }

    @Override
    public List<DashboardWidget> saveAll(List<DashboardWidget> widgets) {
        return widgets.stream()
                .map(mongoTemplate::save)
                .toList();
    }

    @Override
    public Optional<DashboardWidget> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, DashboardWidget.class));
    }

    @Override
    public Optional<DashboardWidget> findByTenantIdAndWidgetId(String tenantId, String widgetId) {
        Query query = Query.query(
                Criteria.where("widgetId").is(widgetId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, DashboardWidget.class));
    }

    @Override
    public List<DashboardWidget> findByDashboardId(String tenantId, String dashboardId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("dashboardId").is(dashboardId)
        );
        return mongoTemplate.find(query, DashboardWidget.class);
    }

    @Override
    public List<DashboardWidget> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, DashboardWidget.class);
    }

    @Override
    public List<DashboardWidget> findByTenantIdAndType(String tenantId,
                                                        DashboardWidget.WidgetType type) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("type").is(type)
        );
        return mongoTemplate.find(query, DashboardWidget.class);
    }

    @Override
    public List<DashboardWidget> findByTenantIdAndStatus(String tenantId,
                                                          DashboardWidget.WidgetStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.find(query, DashboardWidget.class);
    }

    @Override
    public List<DashboardWidget> findByDashboardIdAndStatus(String tenantId, String dashboardId,
                                                             DashboardWidget.WidgetStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("dashboardId").is(dashboardId)
                        .and("status").is(status)
        );
        return mongoTemplate.find(query, DashboardWidget.class);
    }

    @Override
    public List<DashboardWidget> findActiveByDashboardId(String tenantId, String dashboardId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("dashboardId").is(dashboardId)
                        .and("status").is(DashboardWidget.WidgetStatus.ACTIVE)
        );
        return mongoTemplate.find(query, DashboardWidget.class);
    }

    @Override
    public List<DashboardWidget> findByDashboardIdAndPositionBetween(String tenantId,
                                                                      String dashboardId,
                                                                      int minPosition,
                                                                      int maxPosition) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("dashboardId").is(dashboardId)
                        .and("position").gte(minPosition).lte(maxPosition)
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
    public boolean existsByTenantIdAndWidgetId(String tenantId, String widgetId) {
        Query query = Query.query(
                Criteria.where("widgetId").is(widgetId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, DashboardWidget.class);
    }

    @Override
    public boolean existsByDashboardIdAndWidgetId(String tenantId, String dashboardId,
                                                   String widgetId) {
        Query query = Query.query(
                Criteria.where("dashboardId").is(dashboardId)
                        .and("widgetId").is(widgetId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, DashboardWidget.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), DashboardWidget.class);
    }

    @Override
    public void deleteByTenantIdAndWidgetId(String tenantId, String widgetId) {
        Query query = Query.query(
                Criteria.where("widgetId").is(widgetId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, DashboardWidget.class);
    }

    @Override
    public void deleteAllByDashboardId(String tenantId, String dashboardId) {
        Query query = Query.query(
                Criteria.where("dashboardId").is(dashboardId)
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
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, DashboardWidget.class);
    }

    @Override
    public long countByDashboardId(String tenantId, String dashboardId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("dashboardId").is(dashboardId)
        );
        return mongoTemplate.count(query, DashboardWidget.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, DashboardWidget.WidgetStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.count(query, DashboardWidget.class);
    }

    @Override
    public List<DashboardWidget> findWidgetsNeedingRefresh(String tenantId,
                                                             Instant beforeTimestamp) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").in(DashboardWidget.WidgetStatus.ACTIVE,
                                          DashboardWidget.WidgetStatus.LOADING)
                        .and("lastRefreshedAt").lt(beforeTimestamp)
        );
        return mongoTemplate.find(query, DashboardWidget.class);
    }

    @Override
    public void updateStatus(String tenantId, String widgetId,
                             DashboardWidget.WidgetStatus status) {
        Query query = Query.query(
                Criteria.where("widgetId").is(widgetId)
                        .and("tenantId").is(tenantId)
        );
        Update update = Update.update("status", status)
                .set("updatedAt", Instant.now());
        mongoTemplate.updateFirst(query, update, DashboardWidget.class);
    }
}
