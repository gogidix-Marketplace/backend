package com.gogidix.sales.analytics.application.service;

import com.gogidix.sales.analytics.domain.model.DashboardWidget;
import com.gogidix.sales.analytics.domain.repository.DashboardWidgetRepository;
import com.gogidix.sales.analytics.shared.exception.NotFoundException;
import com.gogidix.sales.analytics.shared.exception.ValidationException;
import com.gogidix.sales.analytics.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for Dashboard Widget Operations
 * Handles widget creation, configuration, and data refresh
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final DashboardWidgetRepository widgetRepository;

    @Transactional
    public DashboardWidget createWidget(String dashboardId, String name, String description,
                                       DashboardWidget.WidgetType widgetType,
                                       DashboardWidget.WidgetPosition position,
                                       DashboardWidget.WidgetDataSource dataSource) {
        String tenantId = RequestContextHolder.getTenantId();
        String userId = RequestContextHolder.getUserId();

        DashboardWidget widget = DashboardWidget.create(
                tenantId, dashboardId, name, description, widgetType,
                position, dataSource, userId
        );

        widget = widgetRepository.save(widget);
        log.info("Created widget: {} for dashboard: {}", widget.getWidgetId(), dashboardId);

        return widget;
    }

    @Transactional(readOnly = true)
    public DashboardWidget getWidget(String widgetId) {
        String tenantId = RequestContextHolder.getTenantId();
        return widgetRepository.findByWidgetIdAndTenantId(widgetId, tenantId)
                .orElseThrow(() -> new NotFoundException("DashboardWidget", widgetId));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "dashboardWidgets", key = "#dashboardId")
    public List<DashboardWidget> getDashboardWidgets(String dashboardId) {
        String tenantId = RequestContextHolder.getTenantId();
        return widgetRepository.findByTenantIdAndDashboardIdOrderByDisplayOrderAsc(tenantId, dashboardId);
    }

    @Transactional(readOnly = true)
    public List<DashboardWidget> getWidgetsByType(DashboardWidget.WidgetType widgetType) {
        String tenantId = RequestContextHolder.getTenantId();
        return widgetRepository.findByTenantIdAndWidgetType(tenantId, widgetType);
    }

    @Transactional(readOnly = true)
    public List<DashboardWidget> getVisibleWidgets(String dashboardId) {
        String tenantId = RequestContextHolder.getTenantId();
        return widgetRepository.findByTenantIdAndDashboardId(tenantId, dashboardId)
                .stream()
                .filter(w -> w.getIsVisible() != null && w.getIsVisible())
                .collect(Collectors.toList());
    }

    @Transactional
    @CacheEvict(value = "dashboardWidgets", key = "#widgetId")
    public DashboardWidget updateWidgetData(String widgetId, Map<String, Object> data) {
        String tenantId = RequestContextHolder.getTenantId();

        DashboardWidget widget = widgetRepository.findByWidgetIdAndTenantId(widgetId, tenantId)
                .orElseThrow(() -> new NotFoundException("DashboardWidget", widgetId));

        widget.updateData(data);
        return widgetRepository.save(widget);
    }

    @Transactional
    @CacheEvict(value = "dashboardWidgets", key = "#widgetId")
    public DashboardWidget updateWidgetConfig(String widgetId, String key, Object value) {
        String tenantId = RequestContextHolder.getTenantId();

        DashboardWidget widget = widgetRepository.findByWidgetIdAndTenantId(widgetId, tenantId)
                .orElseThrow(() -> new NotFoundException("DashboardWidget", widgetId));

        widget.updateConfig(key, value);
        return widgetRepository.save(widget);
    }

    @Transactional
    @CacheEvict(value = "dashboardWidgets", key = "#widgetId")
    public DashboardWidget updateWidgetPosition(String widgetId, DashboardWidget.WidgetPosition position) {
        String tenantId = RequestContextHolder.getTenantId();

        DashboardWidget widget = widgetRepository.findByWidgetIdAndTenantId(widgetId, tenantId)
                .orElseThrow(() -> new NotFoundException("DashboardWidget", widgetId));

        widget.updatePosition(position);
        return widgetRepository.save(widget);
    }

    @Transactional
    @CacheEvict(value = "dashboardWidgets", key = "#widgetId")
    public DashboardWidget addMetricToWidget(String widgetId, String metricId) {
        String tenantId = RequestContextHolder.getTenantId();

        DashboardWidget widget = widgetRepository.findByWidgetIdAndTenantId(widgetId, tenantId)
                .orElseThrow(() -> new NotFoundException("DashboardWidget", widgetId));

        widget.addMetric(metricId);
        return widgetRepository.save(widget);
    }

    @Transactional
    @CacheEvict(value = "dashboardWidgets", key = "#widgetId")
    public DashboardWidget removeMetricFromWidget(String widgetId, String metricId) {
        String tenantId = RequestContextHolder.getTenantId();

        DashboardWidget widget = widgetRepository.findByWidgetIdAndTenantId(widgetId, tenantId)
                .orElseThrow(() -> new NotFoundException("DashboardWidget", widgetId));

        widget.removeMetric(metricId);
        return widgetRepository.save(widget);
    }

    @Transactional
    @CacheEvict(value = "dashboardWidgets", key = "#widgetId")
    public DashboardWidget setWidgetVisibility(String widgetId, Boolean isVisible) {
        String tenantId = RequestContextHolder.getTenantId();

        DashboardWidget widget = widgetRepository.findByWidgetIdAndTenantId(widgetId, tenantId)
                .orElseThrow(() -> new NotFoundException("DashboardWidget", widgetId));

        widget.setVisibility(isVisible);
        return widgetRepository.save(widget);
    }

    @Transactional
    @CacheEvict(value = "dashboardWidgets", key = "#widgetId")
    public DashboardWidget updateDisplayOrder(String widgetId, Integer displayOrder) {
        String tenantId = RequestContextHolder.getTenantId();

        DashboardWidget widget = widgetRepository.findByWidgetIdAndTenantId(widgetId, tenantId)
                .orElseThrow(() -> new NotFoundException("DashboardWidget", widgetId));

        widget.updateDisplayOrder(displayOrder);
        return widgetRepository.save(widget);
    }

    @Transactional
    @CacheEvict(value = "dashboardWidgets", key = "#widgetId")
    public DashboardWidget addWidgetFilter(String widgetId, String key, Object value) {
        String tenantId = RequestContextHolder.getTenantId();

        DashboardWidget widget = widgetRepository.findByWidgetIdAndTenantId(widgetId, tenantId)
                .orElseThrow(() -> new NotFoundException("DashboardWidget", widgetId));

        widget.addFilter(key, value);
        return widgetRepository.save(widget);
    }

    @Transactional
    @CacheEvict(value = "dashboardWidgets", key = "#widgetId")
    public DashboardWidget removeWidgetFilter(String widgetId, String key) {
        String tenantId = RequestContextHolder.getTenantId();

        DashboardWidget widget = widgetRepository.findByWidgetIdAndTenantId(widgetId, tenantId)
                .orElseThrow(() -> new NotFoundException("DashboardWidget", widgetId));

        widget.removeFilter(key);
        return widgetRepository.save(widget);
    }

    @Transactional
    @CacheEvict(value = "dashboardWidgets", allEntries = true)
    public void refreshWidgetData(String widgetId) {
        String tenantId = RequestContextHolder.getTenantId();

        DashboardWidget widget = widgetRepository.findByWidgetIdAndTenantId(widgetId, tenantId)
                .orElseThrow(() -> new NotFoundException("DashboardWidget", widgetId));

        if (!widget.needsRefresh()) {
            log.debug("Widget {} does not need refresh", widgetId);
            return;
        }

        // Generate new widget data based on type
        Map<String, Object> newData = generateWidgetData(widget);
        widget.updateData(newData);
        widgetRepository.save(widget);

        log.info("Refreshed data for widget: {}", widgetId);
    }

    @Transactional
    public void refreshDashboardWidgets(String dashboardId) {
        List<DashboardWidget> widgets = getDashboardWidgets(dashboardId);

        for (DashboardWidget widget : widgets) {
            if (widget.needsRefresh()) {
                refreshWidgetData(widget.getWidgetId());
            }
        }

        log.info("Refreshed {} widgets for dashboard: {}", widgets.size(), dashboardId);
    }

    @Transactional
    @CacheEvict(value = "dashboardWidgets", key = "#widgetId")
    public void deleteWidget(String widgetId) {
        String tenantId = RequestContextHolder.getTenantId();

        if (!widgetRepository.existsByWidgetIdAndTenantId(widgetId, tenantId)) {
            throw new NotFoundException("DashboardWidget", widgetId);
        }

        widgetRepository.deleteByWidgetIdAndTenantId(widgetId, tenantId);
        log.info("Deleted widget: {} for tenant: {}", widgetId, tenantId);
    }

    @Transactional
    @CacheEvict(value = "dashboardWidgets", allEntries = true)
    public void deleteDashboardWidgets(String dashboardId) {
        String tenantId = RequestContextHolder.getTenantId();
        widgetRepository.deleteAllByDashboardId(dashboardId);
        log.info("Deleted all widgets for dashboard: {}", dashboardId);
    }

    /**
     * Generates widget data based on widget type
     */
    private Map<String, Object> generateWidgetData(DashboardWidget widget) {
        Map<String, Object> data = new HashMap<>();

        switch (widget.getWidgetType()) {
            case LINE_CHART:
            case BAR_CHART:
            case AREA_CHART:
                data.put("labels", List.of("Jan", "Feb", "Mar", "Apr", "May", "Jun"));
                data.put("datasets", List.of(
                    Map.of("label", "Revenue", "data", List.of(10000, 15000, 12000, 18000, 20000, 25000)),
                    Map.of("label", "Target", "data", List.of(12000, 16000, 14000, 19000, 21000, 26000))
                ));
                break;

            case PIE_CHART:
            case DONUT_CHART:
                data.put("labels", List.of("Won", "Lost", "In Progress"));
                data.put("data", List.of(45, 25, 30));
                break;

            case METRIC_CARD:
            case KPI_CARD:
            case NUMBER_CARD:
                data.put("value", Math.random() * 100000);
                data.put("label", "Total Revenue");
                data.put("change", (Math.random() * 20) - 10);
                data.put("period", "This Month");
                break;

            case TABLE:
                data.put("headers", List.of("Name", "Value", "Change"));
                data.put("rows", List.of(
                    List.of("Product A", 50000, 5.2),
                    List.of("Product B", 35000, -2.1),
                    List.of("Product C", 28000, 8.4)
                ));
                break;

            case GAUGE:
                data.put("value", Math.random() * 100);
                data.put("min", 0);
                data.put("max", 100);
                data.put("label", "Quota Achievement");
                break;

            case FUNNEL:
                data.put("stages", List.of(
                    Map.of("name", "Leads", "value", 1000),
                    Map.of("name", "Qualified", "value", 500),
                    Map.of("name", "Proposals", "value", 200),
                    Map.of("name", "Won", "value", 100)
                ));
                break;

            default:
                data.put("message", "Widget data placeholder");
                data.put("timestamp", Instant.now().toString());
        }

        return data;
    }
}
