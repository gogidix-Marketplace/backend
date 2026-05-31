package com.gogidix.sales.dashboard.infrastructure.persistence.mongo;

import com.gogidix.sales.dashboard.domain.model.KPIWidget;
import com.gogidix.sales.dashboard.domain.repository.KPIWidgetRepository;
import com.gogidix.sales.dashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - KPI Widget
 * Implements widget persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoKPIWidgetRepository implements KPIWidgetRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public KPIWidget save(KPIWidget widget) {
        log.debug("Saving widget: {} for tenant: {}", widget.getWidgetId(), widget.getTenantId());
        return mongoTemplate.save(widget);
    }

    @Override
    public List<KPIWidget> saveAll(List<KPIWidget> widgets) {
        return widgets.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<KPIWidget> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, KPIWidget.class));
    }

    @Override
    public Optional<KPIWidget> findByWidgetIdAndTenantId(String widgetId, String tenantId) {
        Query query = Query.query(
                Criteria.where("widgetId").is(widgetId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, KPIWidget.class));
    }

    @Override
    public List<KPIWidget> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, KPIWidget.class);
    }

    @Override
    public List<KPIWidget> findByDashboardIdAndTenantId(String dashboardId, String tenantId) {
        Query query = Query.query(
                Criteria.where("dashboardId").is(dashboardId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.find(query, KPIWidget.class);
    }

    @Override
    public List<KPIWidget> findByTenantIdAndWidgetType(String tenantId, KPIWidget.WidgetType widgetType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("widgetType").is(widgetType)
        );
        return mongoTemplate.find(query, KPIWidget.class);
    }

    @Override
    public List<KPIWidget> findByTenantIdAndCategory(String tenantId, KPIWidget.WidgetCategory category) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("category").is(category)
        );
        return mongoTemplate.find(query, KPIWidget.class);
    }

    @Override
    public List<KPIWidget> findByTenantIdAndIsActive(String tenantId, Boolean isActive) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("isActive").is(isActive)
        );
        return mongoTemplate.find(query, KPIWidget.class);
    }

    @Override
    public List<KPIWidget> findByTenantIdAndOwner(String tenantId, String owner) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("owner").is(owner)
        );
        return mongoTemplate.find(query, KPIWidget.class);
    }

    @Override
    public List<KPIWidget> findByTenantIdAndTagsContaining(String tenantId, String tag) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("tags").is(tag)
        );
        return mongoTemplate.find(query, KPIWidget.class);
    }

    @Override
    public List<KPIWidget> findByDashboardIdAndTenantIdAndIsActive(String dashboardId,
                                                                     String tenantId,
                                                                     Boolean isActive) {
        Query query = Query.query(
                Criteria.where("dashboardId").is(dashboardId)
                        .and("tenantId").is(tenantId)
                        .and("isActive").is(isActive)
        );
        return mongoTemplate.find(query, KPIWidget.class);
    }

    @Override
    public List<KPIWidget> findByTenantIdAndWidgetTypeAndCategory(String tenantId,
                                                                    KPIWidget.WidgetType widgetType,
                                                                    KPIWidget.WidgetCategory category) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("widgetType").is(widgetType)
                        .and("category").is(category)
        );
        return mongoTemplate.find(query, KPIWidget.class);
    }

    @Override
    public List<KPIWidget> searchByTitle(String tenantId, String searchTerm) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("title").regex(searchTerm, "i")
        );
        return mongoTemplate.find(query, KPIWidget.class);
    }

    @Override
    public boolean existsByWidgetIdAndTenantId(String widgetId, String tenantId) {
        Query query = Query.query(
                Criteria.where("widgetId").is(widgetId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, KPIWidget.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), KPIWidget.class);
    }

    @Override
    public void deleteByWidgetIdAndTenantId(String widgetId, String tenantId) {
        Query query = Query.query(
                Criteria.where("widgetId").is(widgetId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, KPIWidget.class);
    }

    @Override
    public void deleteByDashboardIdAndTenantId(String dashboardId, String tenantId) {
        Query query = Query.query(
                Criteria.where("dashboardId").is(dashboardId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, KPIWidget.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, KPIWidget.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, KPIWidget.class);
    }

    @Override
    public long countByDashboardIdAndTenantId(String dashboardId, String tenantId) {
        Query query = Query.query(
                Criteria.where("dashboardId").is(dashboardId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.count(query, KPIWidget.class);
    }

    @Override
    public long countByTenantIdAndWidgetType(String tenantId, KPIWidget.WidgetType widgetType) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("widgetType").is(widgetType)
        );
        return mongoTemplate.count(query, KPIWidget.class);
    }

    @Override
    public List<KPIWidget> findWidgetsNeedingRefresh(String tenantId, int minutesThreshold) {
        Instant threshold = Instant.now().minusSeconds(minutesThreshold * 60L);

        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("isActive").is(true)
                        .and("lastUpdated").lt(threshold)
        );

        return mongoTemplate.find(query, KPIWidget.class);
    }
}
