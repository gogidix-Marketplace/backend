package com.gogidix.finance.globalfinancedashboard.application.service;

import com.gogidix.finance.globalfinancedashboard.domain.event.DashboardCreatedEvent;
import com.gogidix.finance.globalfinancedashboard.domain.event.DashboardEvent;
import com.gogidix.finance.globalfinancedashboard.domain.event.DashboardSharedEvent;
import com.gogidix.finance.globalfinancedashboard.domain.event.DashboardUpdatedEvent;
import com.gogidix.finance.globalfinancedashboard.domain.event.MetricCalculatedEvent;
import com.gogidix.finance.globalfinancedashboard.domain.event.WidgetCreatedEvent;
import com.gogidix.finance.globalfinancedashboard.domain.event.WidgetUpdatedEvent;
import com.gogidix.finance.globalfinancedashboard.domain.model.Dashboard;
import com.gogidix.finance.globalfinancedashboard.domain.model.DashboardLayout;
import com.gogidix.finance.globalfinancedashboard.domain.model.DashboardView;
import com.gogidix.finance.globalfinancedashboard.domain.model.DashboardWidget;
import com.gogidix.finance.globalfinancedashboard.domain.model.Widget;
import com.gogidix.finance.globalfinancedashboard.domain.port.out.DashboardRepository;
import com.gogidix.finance.globalfinancedashboard.domain.port.out.EventPublisher;
import com.gogidix.finance.globalfinancedashboard.domain.port.out.WidgetRepository;
import com.gogidix.finance.globalfinancedashboard.domain.repository.DashboardLayoutRepository;
import com.gogidix.finance.globalfinancedashboard.domain.repository.DashboardViewRepository;
import com.gogidix.finance.globalfinancedashboard.domain.repository.DashboardWidgetRepository;
import com.gogidix.finance.globalfinancedashboard.shared.exception.ConflictException;
import com.gogidix.finance.globalfinancedashboard.shared.exception.NotFoundException;
import com.gogidix.finance.globalfinancedashboard.shared.exception.ValidationException;
import com.gogidix.finance.globalfinancedashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Dashboard Command Service
 * Handles all write operations for dashboards
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardCommandService {

    private final DashboardRepository dashboardRepository;
    private final DashboardViewRepository dashboardViewRepository;
    private final DashboardWidgetRepository dashboardWidgetRepository;
    private final DashboardLayoutRepository dashboardLayoutRepository;
    private final WidgetRepository widgetRepository;
    private final EventPublisher eventPublisher;

    /**
     * Creates a new dashboard
     */
    @Transactional
    public Dashboard createDashboard(String tenantId, String name, String description,
                                     String owner, String ownerEmail, Dashboard.DashboardType type) {
        log.info("Creating dashboard: {} for tenant: {}", name, tenantId);

        if (dashboardRepository.existsById(tenantId, name)) {
            throw new ConflictException("Dashboard", name);
        }

        Dashboard dashboard = Dashboard.create(tenantId, name, description, owner, ownerEmail, type);
        Dashboard savedDashboard = dashboardRepository.save(dashboard);
        publishEvents(savedDashboard);

        log.info("Created dashboard: {} for tenant: {}", savedDashboard.getDashboardId(), tenantId);
        return savedDashboard;
    }

    /**
     * Updates dashboard information
     */
    @Transactional
    public Dashboard updateDashboard(String tenantId, String dashboardId, String name,
                                     String description, Dashboard.DashboardType type) {
        log.info("Updating dashboard: {} for tenant: {}", dashboardId, tenantId);

        Dashboard dashboard = dashboardRepository.findById(tenantId, dashboardId)
                .orElseThrow(() -> new NotFoundException("Dashboard", dashboardId));

        dashboard.updateInfo(name, description, type);
        Dashboard savedDashboard = dashboardRepository.save(dashboard);
        publishEvents(savedDashboard);

        log.info("Updated dashboard: {}", dashboardId);
        return savedDashboard;
    }

    /**
     * Activates a dashboard
     */
    @Transactional
    public Dashboard activateDashboard(String tenantId, String dashboardId) {
        log.info("Activating dashboard: {} for tenant: {}", dashboardId, tenantId);

        Dashboard dashboard = dashboardRepository.findById(tenantId, dashboardId)
                .orElseThrow(() -> new NotFoundException("Dashboard", dashboardId));

        dashboard.activate();
        Dashboard savedDashboard = dashboardRepository.save(dashboard);
        publishEvents(savedDashboard);

        log.info("Activated dashboard: {}", dashboardId);
        return savedDashboard;
    }

    /**
     * Archives a dashboard
     */
    @Transactional
    public Dashboard archiveDashboard(String tenantId, String dashboardId) {
        log.info("Archiving dashboard: {} for tenant: {}", dashboardId, tenantId);

        Dashboard dashboard = dashboardRepository.findById(tenantId, dashboardId)
                .orElseThrow(() -> new NotFoundException("Dashboard", dashboardId));

        dashboard.archive();
        Dashboard savedDashboard = dashboardRepository.save(dashboard);
        publishEvents(savedDashboard);

        log.info("Archived dashboard: {}", dashboardId);
        return savedDashboard;
    }

    /**
     * Deletes a dashboard
     */
    @Transactional
    public void deleteDashboard(String tenantId, String dashboardId) {
        log.info("Deleting dashboard: {} for tenant: {}", dashboardId, tenantId);

        Dashboard dashboard = dashboardRepository.findById(tenantId, dashboardId)
                .orElseThrow(() -> new NotFoundException("Dashboard", dashboardId));

        if (dashboard.getStatus() != Dashboard.DashboardStatus.DRAFT) {
            throw new ValidationException("Can only delete draft dashboards");
        }

        // Delete associated widgets
        // TODO: Implement widget cleanup
        // widgetRepository.deleteAllByDashboardId(tenantId, dashboardId);

        dashboardRepository.delete(dashboard);
        log.info("Deleted dashboard: {}", dashboardId);
    }

    /**
     * Sets dashboard as default
     */
    @Transactional
    public Dashboard setAsDefault(String tenantId, String dashboardId) {
        log.info("Setting dashboard as default: {} for tenant: {}", dashboardId, tenantId);

        // Remove default from existing default dashboard
        dashboardRepository.findDefault(tenantId).ifPresent(d -> {
            d.setIsDefault(false);
            dashboardRepository.save(d);
        });

        Dashboard dashboard = dashboardRepository.findById(tenantId, dashboardId)
                .orElseThrow(() -> new NotFoundException("Dashboard", dashboardId));

        dashboard.setAsDefault();
        Dashboard savedDashboard = dashboardRepository.save(dashboard);

        log.info("Set dashboard as default: {}", dashboardId);
        return savedDashboard;
    }

    /**
     * Makes dashboard public
     */
    @Transactional
    public Dashboard makePublic(String tenantId, String dashboardId) {
        log.info("Making dashboard public: {} for tenant: {}", dashboardId, tenantId);

        Dashboard dashboard = dashboardRepository.findById(tenantId, dashboardId)
                .orElseThrow(() -> new NotFoundException("Dashboard", dashboardId));

        dashboard.makePublic();
        Dashboard savedDashboard = dashboardRepository.save(dashboard);
        publishEvents(savedDashboard);

        log.info("Made dashboard public: {}", dashboardId);
        return savedDashboard;
    }

    /**
     * Makes dashboard private
     */
    @Transactional
    public Dashboard makePrivate(String tenantId, String dashboardId) {
        log.info("Making dashboard private: {} for tenant: {}", dashboardId, tenantId);

        Dashboard dashboard = dashboardRepository.findById(tenantId, dashboardId)
                .orElseThrow(() -> new NotFoundException("Dashboard", dashboardId));

        dashboard.makePrivate();
        Dashboard savedDashboard = dashboardRepository.save(dashboard);

        log.info("Made dashboard private: {}", dashboardId);
        return savedDashboard;
    }

    /**
     * Generates share token
     */
    @Transactional
    public String generateShareToken(String tenantId, String dashboardId, int expiryHours) {
        log.info("Generating share token for dashboard: {} for tenant: {}", dashboardId, tenantId);

        Dashboard dashboard = dashboardRepository.findById(tenantId, dashboardId)
                .orElseThrow(() -> new NotFoundException("Dashboard", dashboardId));

        String shareToken = dashboard.generateShareToken(expiryHours);
        Dashboard savedDashboard = dashboardRepository.save(dashboard);
        publishEvents(savedDashboard);

        log.info("Generated share token for dashboard: {}", dashboardId);
        return shareToken;
    }

    /**
     * Shares dashboard with user
     */
    @Transactional
    public void shareWithUser(String tenantId, String dashboardId, String userId) {
        log.info("Sharing dashboard: {} with user: {}", dashboardId, userId);

        Dashboard dashboard = dashboardRepository.findById(tenantId, dashboardId)
                .orElseThrow(() -> new NotFoundException("Dashboard", dashboardId));

        dashboard.shareWith(userId);
        Dashboard savedDashboard = dashboardRepository.save(dashboard);
        publishEvents(savedDashboard);

        log.info("Shared dashboard: {} with user: {}", dashboardId, userId);
    }

    /**
     * Shares dashboard with group
     */
    @Transactional
    public void shareWithGroup(String tenantId, String dashboardId, String groupId) {
        log.info("Sharing dashboard: {} with group: {}", dashboardId, groupId);

        Dashboard dashboard = dashboardRepository.findById(tenantId, dashboardId)
                .orElseThrow(() -> new NotFoundException("Dashboard", dashboardId));

        dashboard.shareWithGroup(groupId);
        Dashboard savedDashboard = dashboardRepository.save(dashboard);
        publishEvents(savedDashboard);

        log.info("Shared dashboard: {} with group: {}", dashboardId, groupId);
    }

    /**
     * Removes share from user
     */
    @Transactional
    public void unshareFromUser(String tenantId, String dashboardId, String userId) {
        log.info("Unsharing dashboard: {} from user: {}", dashboardId, userId);

        Dashboard dashboard = dashboardRepository.findById(tenantId, dashboardId)
                .orElseThrow(() -> new NotFoundException("Dashboard", dashboardId));

        dashboard.unshareFrom(userId);
        dashboardRepository.save(dashboard);

        log.info("Unshared dashboard: {} from user: {}", dashboardId, userId);
    }

    /**
     * Removes share from group
     */
    @Transactional
    public void unshareFromGroup(String tenantId, String dashboardId, String groupId) {
        log.info("Unsharing dashboard: {} from group: {}", dashboardId, groupId);

        Dashboard dashboard = dashboardRepository.findById(tenantId, dashboardId)
                .orElseThrow(() -> new NotFoundException("Dashboard", dashboardId));

        dashboard.unshareFromGroup(groupId);
        dashboardRepository.save(dashboard);

        log.info("Unshared dashboard: {} from group: {}", dashboardId, groupId);
    }

    /**
     * Adds widget to dashboard
     */
    @Transactional
    public Dashboard addWidget(String tenantId, String dashboardId, Widget widget) {
        log.info("Adding widget: {} to dashboard: {}", widget.getWidgetId(), dashboardId);

        Dashboard dashboard = dashboardRepository.findById(tenantId, dashboardId)
                .orElseThrow(() -> new NotFoundException("Dashboard", dashboardId));

        dashboard.addWidget(widget);
        widgetRepository.save(widget);
        Dashboard savedDashboard = dashboardRepository.save(dashboard);
        publishEvents(savedDashboard);

        log.info("Added widget: {} to dashboard: {}", widget.getWidgetId(), dashboardId);
        return savedDashboard;
    }

    /**
     * Removes widget from dashboard
     */
    @Transactional
    public Dashboard removeWidget(String tenantId, String dashboardId, String widgetId) {
        log.info("Removing widget: {} from dashboard: {}", widgetId, dashboardId);

        Dashboard dashboard = dashboardRepository.findById(tenantId, dashboardId)
                .orElseThrow(() -> new NotFoundException("Dashboard", dashboardId));

        dashboard.removeWidget(widgetId);
        // TODO: Implement widget deletion
        // widgetRepository.deleteByWidgetId(tenantId, widgetId);
        Dashboard savedDashboard = dashboardRepository.save(dashboard);
        publishEvents(savedDashboard);

        log.info("Removed widget: {} from dashboard: {}", widgetId, dashboardId);
        return savedDashboard;
    }

    /**
     * Updates widget position
     */
    @Transactional
    public Dashboard updateWidgetPosition(String tenantId, String dashboardId,
                                         String widgetId, int newPosition) {
        log.info("Updating widget position: {} to: {} in dashboard: {}", widgetId, newPosition, dashboardId);

        Dashboard dashboard = dashboardRepository.findById(tenantId, dashboardId)
                .orElseThrow(() -> new NotFoundException("Dashboard", dashboardId));

        dashboard.updateWidgetPosition(widgetId, newPosition);
        Dashboard savedDashboard = dashboardRepository.save(dashboard);

        log.info("Updated widget position: {} in dashboard: {}", widgetId, dashboardId);
        return savedDashboard;
    }

    /**
     * Updates dashboard layout
     */
    @Transactional
    public Dashboard updateLayout(String tenantId, String dashboardId, Dashboard.LayoutConfig layout) {
        log.info("Updating layout for dashboard: {}", dashboardId);

        Dashboard dashboard = dashboardRepository.findById(tenantId, dashboardId)
                .orElseThrow(() -> new NotFoundException("Dashboard", dashboardId));

        dashboard.updateLayout(layout);
        Dashboard savedDashboard = dashboardRepository.save(dashboard);

        log.info("Updated layout for dashboard: {}", dashboardId);
        return savedDashboard;
    }

    /**
     * Updates dashboard theme
     */
    @Transactional
    public Dashboard updateTheme(String tenantId, String dashboardId, Dashboard.ThemeConfig theme) {
        log.info("Updating theme for dashboard: {}", dashboardId);

        Dashboard dashboard = dashboardRepository.findById(tenantId, dashboardId)
                .orElseThrow(() -> new NotFoundException("Dashboard", dashboardId));

        dashboard.updateTheme(theme);
        Dashboard savedDashboard = dashboardRepository.save(dashboard);

        log.info("Updated theme for dashboard: {}", dashboardId);
        return savedDashboard;
    }

    /**
     * Updates refresh interval
     */
    @Transactional
    public Dashboard updateRefreshInterval(String tenantId, String dashboardId,
                                           Dashboard.RefreshInterval interval) {
        log.info("Updating refresh interval for dashboard: {}", dashboardId);

        Dashboard dashboard = dashboardRepository.findById(tenantId, dashboardId)
                .orElseThrow(() -> new NotFoundException("Dashboard", dashboardId));

        dashboard.updateRefreshInterval(interval);
        Dashboard savedDashboard = dashboardRepository.save(dashboard);

        log.info("Updated refresh interval for dashboard: {}", dashboardId);
        return savedDashboard;
    }

    /**
     * Creates a dashboard view
     */
    @Transactional
    public DashboardView createDashboardView(String tenantId, String name, String ownerId) {
        log.info("Creating dashboard view: {} for tenant: {}", name, tenantId);

        DashboardView view = new DashboardView(tenantId, name, ownerId);
        DashboardView savedView = dashboardViewRepository.save(view);

        log.info("Created dashboard view: {}", savedView.getId());
        return savedView;
    }

    /**
     * Adds a widget to a dashboard view
     */
    @Transactional
    public void addWidgetToView(String tenantId, String viewId,
                                DashboardView.WidgetConfig widgetConfig) {
        log.info("Adding widget: {} to view: {}", widgetConfig.widgetId(), viewId);

        DashboardView view = dashboardViewRepository.findByTenantIdAndViewId(tenantId, viewId)
                .orElseThrow(() -> new NotFoundException("DashboardView", viewId));

        view.addWidget(widgetConfig);
        dashboardViewRepository.save(view);

        log.info("Added widget to view: {}", viewId);
    }

    /**
     * Removes a widget from a dashboard view
     */
    @Transactional
    public void removeWidgetFromView(String tenantId, String viewId, String widgetId) {
        log.info("Removing widget: {} from view: {}", widgetId, viewId);

        DashboardView view = dashboardViewRepository.findByTenantIdAndViewId(tenantId, viewId)
                .orElseThrow(() -> new NotFoundException("DashboardView", viewId));

        view.removeWidget(widgetId);
        dashboardViewRepository.save(view);

        log.info("Removed widget from view: {}", viewId);
    }

    /**
     * Creates a dashboard widget
     */
    @Transactional
    public DashboardWidget createDashboardWidget(String tenantId, String dashboardId,
                                                  String name, DashboardWidget.WidgetType type,
                                                  DashboardWidget.DataSource dataSource) {
        log.info("Creating dashboard widget: {} for dashboard: {}", name, dashboardId);

        DashboardWidget widget = DashboardWidget.create(tenantId, dashboardId, name, type, dataSource);
        DashboardWidget savedWidget = dashboardWidgetRepository.save(widget);
        publishWidgetEvents(savedWidget);

        log.info("Created dashboard widget: {}", savedWidget.getWidgetId());
        return savedWidget;
    }

    /**
     * Updates dashboard widget
     */
    @Transactional
    public DashboardWidget updateDashboardWidget(String tenantId, String widgetId,
                                                  String name, String description) {
        log.info("Updating dashboard widget: {}", widgetId);

        DashboardWidget widget = dashboardWidgetRepository.findByTenantIdAndWidgetId(tenantId, widgetId)
                .orElseThrow(() -> new NotFoundException("DashboardWidget", widgetId));

        widget.updateInfo(name, description);
        DashboardWidget savedWidget = dashboardWidgetRepository.save(widget);
        publishWidgetEvents(savedWidget);

        log.info("Updated dashboard widget: {}", widgetId);
        return savedWidget;
    }

    /**
     * Deletes dashboard widget
     */
    @Transactional
    public void deleteDashboardWidget(String tenantId, String widgetId) {
        log.info("Deleting dashboard widget: {}", widgetId);

        DashboardWidget widget = dashboardWidgetRepository.findByTenantIdAndWidgetId(tenantId, widgetId)
                .orElseThrow(() -> new NotFoundException("DashboardWidget", widgetId));

        dashboardWidgetRepository.deleteByTenantIdAndWidgetId(tenantId, widgetId);

        log.info("Deleted dashboard widget: {}", widgetId);
    }

    private void publishEvents(Dashboard dashboard) {
        if (!dashboard.getDomainEvents().isEmpty() && eventPublisher != null) {
            for (DashboardEvent event : dashboard.getDomainEvents()) {
                if (event instanceof DashboardCreatedEvent) {
                    eventPublisher.publish((DashboardCreatedEvent) event);
                } else if (event instanceof DashboardUpdatedEvent) {
                    eventPublisher.publish((DashboardUpdatedEvent) event);
                } else if (event instanceof DashboardSharedEvent) {
                    eventPublisher.publish((DashboardSharedEvent) event);
                } else {
                    eventPublisher.publish("dashboard-event", event);
                }
            }
            dashboard.clearDomainEvents();
        }
    }

    private void publishWidgetEvents(DashboardWidget widget) {
        if (!widget.getDomainEvents().isEmpty() && eventPublisher != null) {
            for (DashboardEvent event : widget.getDomainEvents()) {
                if (event instanceof WidgetCreatedEvent) {
                    eventPublisher.publish((WidgetCreatedEvent) event);
                } else if (event instanceof WidgetUpdatedEvent) {
                    eventPublisher.publish((WidgetUpdatedEvent) event);
                } else if (event instanceof MetricCalculatedEvent) {
                    eventPublisher.publish((MetricCalculatedEvent) event);
                } else {
                    eventPublisher.publish("widget-event", event);
                }
            }
            widget.clearDomainEvents();
        }
    }
}
