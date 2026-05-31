package com.gogidix.finance.globalfinancedashboard.infrastructure.persistence.mongo;

import com.gogidix.finance.globalfinancedashboard.domain.model.Widget;
import com.gogidix.finance.globalfinancedashboard.domain.port.out.WidgetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - Widget
 * Implements widget persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoWidgetRepository implements WidgetRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Widget save(Widget widget) {
        log.debug("Saving widget: {} for tenant: {}",
                widget.getWidgetId(), widget.getTenantId());
        return mongoTemplate.save(widget);
    }

    @Override
    public Optional<Widget> findById(String tenantId, String widgetId) {
        Query query = Query.query(
                Criteria.where("widgetId").is(widgetId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, Widget.class));
    }

    @Override
    public List<Widget> findByDashboardId(String tenantId, String dashboardId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("dashboardId").is(dashboardId)
        );
        return mongoTemplate.find(query, Widget.class);
    }

    @Override
    public List<Widget> findByType(String tenantId, Widget.WidgetType type) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("type").is(type)
        );
        return mongoTemplate.find(query, Widget.class);
    }

    @Override
    public List<Widget> findByStatus(String tenantId, Widget.WidgetStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.find(query, Widget.class);
    }

    @Override
    public void delete(Widget widget) {
        log.debug("Deleting widget: {} for tenant: {}",
                widget.getWidgetId(), widget.getTenantId());
        mongoTemplate.remove(widget);
    }

    @Override
    public void deleteByDashboardId(String tenantId, String dashboardId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("dashboardId").is(dashboardId)
        );
        mongoTemplate.remove(query, Widget.class);
    }

    public void deleteByWidgetId(String tenantId, String widgetId) {
        Query query = Query.query(
                Criteria.where("widgetId").is(widgetId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, Widget.class);
    }

    public List<Widget> saveAll(List<Widget> widgets) {
        return widgets.stream()
                .map(mongoTemplate::save)
                .toList();
    }

    public List<Widget> findByDashboardIdAndStatus(String tenantId, String dashboardId,
                                                     Widget.WidgetStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("dashboardId").is(dashboardId)
                        .and("status").is(status)
        );
        return mongoTemplate.find(query, Widget.class);
    }

    public void deleteAllByDashboardId(String tenantId, String dashboardId) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("dashboardId").is(dashboardId)
        );
        mongoTemplate.remove(query, Widget.class);
    }
}
