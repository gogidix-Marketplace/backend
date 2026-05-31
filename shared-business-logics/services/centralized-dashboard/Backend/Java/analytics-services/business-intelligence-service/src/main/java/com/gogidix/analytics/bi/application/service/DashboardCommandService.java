package com.gogidix.analytics.bi.application.service;

import com.gogidix.analytics.bi.domain.model.Dashboard;
import com.gogidix.analytics.bi.domain.model.DashboardWidget;
import com.gogidix.analytics.bi.domain.port.in.CreateDashboardCommand;
import com.gogidix.analytics.bi.domain.repository.DashboardRepository;
import com.gogidix.analytics.bi.domain.repository.DashboardWidgetRepository;
import com.gogidix.shared.audit.service.AuditService;
import com.gogidix.shared.exceptions.NotFoundException;
import com.gogidix.shared.exceptions.ValidationException;
import com.gogidix.shared.security.context.RequestContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardCommandService {

    private final DashboardRepository dashboardRepository;
    private final DashboardWidgetRepository widgetRepository;
    private final AuditService auditService;

    @Transactional
    public Dashboard createDashboard(CreateDashboardCommand command) {
        log.info("Creating dashboard: name={}", command.getName());

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        Dashboard dashboard = Dashboard.builder()
            .name(command.getName())
            .description(command.getDescription())
            .category(command.getCategory())
            .ownerId(command.getOwnerId())
            .tenantId(tenantId)
            .isPublic(command.getIsPublic())
            .isFavorite(false)
            .refreshIntervalSeconds(command.getRefreshIntervalSeconds())
            .layoutConfig(command.getLayoutConfig())
            .theme(command.getTheme())
            .tags(command.getTags())
            .viewCount(0)
            .build();

        Dashboard savedDashboard = dashboardRepository.save(dashboard);

        if (command.getWidgets() != null && !command.getWidgets().isEmpty()) {
            for (var widgetCmd : command.getWidgets()) {
                DashboardWidget widget = DashboardWidget.builder()
                    .widgetName(widgetCmd.getWidgetName())
                    .widgetType(widgetCmd.getWidgetType())
                    .position(widgetCmd.getPosition() != null ? widgetCmd.getPosition() : 0)
                    .rowIndex(widgetCmd.getRowIndex())
                    .columnIndex(widgetCmd.getColumnIndex())
                    .rowSpan(widgetCmd.getRowSpan() != null ? widgetCmd.getRowSpan() : 1)
                    .columnSpan(widgetCmd.getColumnSpan() != null ? widgetCmd.getColumnSpan() : 1)
                    .dataSource(widgetCmd.getDataSource())
                    .visualizationConfig(widgetCmd.getVisualizationConfig())
                    .queryDefinition(widgetCmd.getQueryDefinition())
                    .refreshIntervalSeconds(widgetCmd.getRefreshIntervalSeconds())
                    .enabled(widgetCmd.getEnabled())
                    .build();

                savedDashboard.addWidget(widget);
            }
        }

        savedDashboard = dashboardRepository.save(savedDashboard);

        auditService.logEvent(
            "DASHBOARD_CREATED",
            "Dashboard",
            savedDashboard.getId(),
            "Created dashboard: " + savedDashboard.getName()
        );

        log.info("Dashboard created: id={}, name={}", savedDashboard.getId(), savedDashboard.getName());
        return savedDashboard;
    }

    @Transactional
    public Dashboard updateDashboard(String dashboardId, CreateDashboardCommand command) {
        log.info("Updating dashboard: id={}", dashboardId);

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        Dashboard dashboard = dashboardRepository.findById(dashboardId)
            .orElseThrow(() -> new NotFoundException("Dashboard not found: " + dashboardId));

        if (!dashboard.getTenantId().equals(tenantId)) {
            throw new ValidationException("Access denied: Dashboard belongs to different tenant");
        }

        dashboard.setName(command.getName());
        dashboard.setDescription(command.getDescription());
        dashboard.setCategory(command.getCategory());
        dashboard.setIsPublic(command.getIsPublic());
        dashboard.setRefreshIntervalSeconds(command.getRefreshIntervalSeconds());
        dashboard.setLayoutConfig(command.getLayoutConfig());
        dashboard.setTheme(command.getTheme());
        dashboard.setTags(command.getTags());

        Dashboard savedDashboard = dashboardRepository.save(dashboard);

        auditService.logEvent(
            "DASHBOARD_UPDATED",
            "Dashboard",
            savedDashboard.getId(),
            "Updated dashboard: " + savedDashboard.getName()
        );

        log.info("Dashboard updated: id={}", dashboardId);
        return savedDashboard;
    }

    @Transactional
    public void deleteDashboard(String dashboardId) {
        log.info("Deleting dashboard: id={}", dashboardId);

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        Dashboard dashboard = dashboardRepository.findById(dashboardId)
            .orElseThrow(() -> new NotFoundException("Dashboard not found: " + dashboardId));

        if (!dashboard.getTenantId().equals(tenantId)) {
            throw new ValidationException("Access denied: Dashboard belongs to different tenant");
        }

        widgetRepository.deleteByDashboardId(dashboardId);

        dashboardRepository.delete(dashboard);

        auditService.logEvent(
            "DASHBOARD_DELETED",
            "Dashboard",
            dashboardId,
            "Deleted dashboard: " + dashboard.getName()
        );

        log.info("Dashboard deleted: id={}", dashboardId);
    }

    @Transactional
    public DashboardWidget addWidget(String dashboardId, CreateDashboardCommand.WidgetCommand command) {
        log.info("Adding widget to dashboard: dashboardId={}, widgetName={}",
            dashboardId, command.getWidgetName());

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        Dashboard dashboard = dashboardRepository.findById(dashboardId)
            .orElseThrow(() -> new NotFoundException("Dashboard not found: " + dashboardId));

        if (!dashboard.getTenantId().equals(tenantId)) {
            throw new ValidationException("Access denied: Dashboard belongs to different tenant");
        }

        DashboardWidget widget = DashboardWidget.builder()
            .widgetName(command.getWidgetName())
            .widgetType(command.getWidgetType())
            .position(command.getPosition() != null ? command.getPosition() :
                dashboard.getWidgets().size())
            .rowIndex(command.getRowIndex())
            .columnIndex(command.getColumnIndex())
            .rowSpan(command.getRowSpan() != null ? command.getRowSpan() : 1)
            .columnSpan(command.getColumnSpan() != null ? command.getColumnSpan() : 1)
            .dataSource(command.getDataSource())
            .visualizationConfig(command.getVisualizationConfig())
            .queryDefinition(command.getQueryDefinition())
            .refreshIntervalSeconds(command.getRefreshIntervalSeconds())
            .enabled(command.getEnabled())
            .build();

        dashboard.addWidget(widget);
        dashboardRepository.save(dashboard);

        auditService.logEvent(
            "WIDGET_ADDED",
            "DashboardWidget",
            widget.getId(),
            "Added widget " + widget.getWidgetName() + " to dashboard " + dashboard.getName()
        );

        log.info("Widget added: widgetId={}, dashboardId={}", widget.getId(), dashboardId);
        return widget;
    }

    @Transactional
    public DashboardWidget updateWidget(String dashboardId, String widgetId,
        CreateDashboardCommand.WidgetCommand command) {
        log.info("Updating widget: dashboardId={}, widgetId={}", dashboardId, widgetId);

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        Dashboard dashboard = dashboardRepository.findById(dashboardId)
            .orElseThrow(() -> new NotFoundException("Dashboard not found: " + dashboardId));

        if (!dashboard.getTenantId().equals(tenantId)) {
            throw new ValidationException("Access denied: Dashboard belongs to different tenant");
        }

        DashboardWidget widget = dashboard.getWidgets().stream()
            .filter(w -> w.getId().equals(widgetId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Widget not found: " + widgetId));

        widget.setWidgetName(command.getWidgetName());
        widget.setWidgetType(command.getWidgetType());
        if (command.getPosition() != null) widget.setPosition(command.getPosition());
        if (command.getRowIndex() != null) widget.setRowIndex(command.getRowIndex());
        if (command.getColumnIndex() != null) widget.setColumnIndex(command.getColumnIndex());
        if (command.getRowSpan() != null) widget.setRowSpan(command.getRowSpan());
        if (command.getColumnSpan() != null) widget.setColumnSpan(command.getColumnSpan());
        widget.setDataSource(command.getDataSource());
        widget.setVisualizationConfig(command.getVisualizationConfig());
        widget.setQueryDefinition(command.getQueryDefinition());
        widget.setRefreshIntervalSeconds(command.getRefreshIntervalSeconds());
        widget.setEnabled(command.getEnabled());

        dashboardRepository.save(dashboard);

        auditService.logEvent(
            "WIDGET_UPDATED",
            "DashboardWidget",
            widgetId,
            "Updated widget: " + widget.getWidgetName()
        );

        log.info("Widget updated: widgetId={}", widgetId);
        return widget;
    }

    @Transactional
    public void deleteWidget(String dashboardId, String widgetId) {
        log.info("Deleting widget: dashboardId={}, widgetId={}", dashboardId, widgetId);

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        Dashboard dashboard = dashboardRepository.findById(dashboardId)
            .orElseThrow(() -> new NotFoundException("Dashboard not found: " + dashboardId));

        if (!dashboard.getTenantId().equals(tenantId)) {
            throw new ValidationException("Access denied: Dashboard belongs to different tenant");
        }

        DashboardWidget widget = dashboard.getWidgets().stream()
            .filter(w -> w.getId().equals(widgetId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Widget not found: " + widgetId));

        dashboard.removeWidget(widget);
        dashboardRepository.save(dashboard);

        auditService.logEvent(
            "WIDGET_DELETED",
            "DashboardWidget",
            widgetId,
            "Deleted widget: " + widget.getWidgetName()
        );

        log.info("Widget deleted: widgetId={}", widgetId);
    }

    @Transactional
    public Dashboard toggleFavorite(String dashboardId) {
        log.info("Toggling favorite: dashboardId={}", dashboardId);

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        Dashboard dashboard = dashboardRepository.findById(dashboardId)
            .orElseThrow(() -> new NotFoundException("Dashboard not found: " + dashboardId));

        if (!dashboard.getTenantId().equals(tenantId)) {
            throw new ValidationException("Access denied: Dashboard belongs to different tenant");
        }

        dashboard.setIsFavorite(!dashboard.getIsFavorite());
        Dashboard savedDashboard = dashboardRepository.save(dashboard);

        log.info("Dashboard favorite toggled: id={}, isFavorite={}", dashboardId, savedDashboard.getIsFavorite());
        return savedDashboard;
    }
}
