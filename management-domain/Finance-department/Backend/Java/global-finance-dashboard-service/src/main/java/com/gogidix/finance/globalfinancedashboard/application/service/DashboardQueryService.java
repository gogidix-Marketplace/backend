package com.gogidix.finance.globalfinancedashboard.application.service;

import com.gogidix.finance.globalfinancedashboard.domain.event.DashboardViewedEvent;
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
import com.gogidix.finance.globalfinancedashboard.shared.exception.NotFoundException;
import com.gogidix.finance.globalfinancedashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Dashboard Query Service
 * Handles all read operations for dashboards
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardQueryService {

    private final DashboardRepository dashboardRepository;
    private final DashboardViewRepository dashboardViewRepository;
    private final DashboardWidgetRepository dashboardWidgetRepository;
    private final DashboardLayoutRepository dashboardLayoutRepository;
    private final WidgetRepository widgetRepository;
    private final EventPublisher eventPublisher;

    /**
     * Gets a dashboard by ID
     */
    public Dashboard getById(String tenantId, String dashboardId) {
        log.debug("Fetching dashboard: {} for tenant: {}", dashboardId, tenantId);

        Dashboard dashboard = dashboardRepository.findById(tenantId, dashboardId)
                .orElseThrow(() -> new NotFoundException("Dashboard", dashboardId));

        // Record access
        dashboard.recordAccess();
        dashboardRepository.save(dashboard);

        // Publish viewed event
        publishViewedEvent(dashboardId, tenantId);

        return dashboard;
    }

    /**
     * Gets a dashboard by share token
     */
    public Dashboard getByShareToken(String shareToken) {
        log.debug("Fetching dashboard by share token: {}", shareToken);

        Dashboard dashboard = dashboardRepository.findByShareToken(shareToken)
                .orElseThrow(() -> new NotFoundException("Dashboard", "shareToken"));

        if (!dashboard.isShareTokenValid(shareToken)) {
            throw new NotFoundException("Dashboard", "shareToken");
        }

        // Record access
        dashboard.recordAccess();
        dashboardRepository.save(dashboard);

        return dashboard;
    }

    /**
     * Gets all dashboards for a tenant
     */
    public List<Dashboard> getAllForTenant(String tenantId) {
        log.debug("Fetching all dashboards for tenant: {}", tenantId);
        return dashboardRepository.findAll(tenantId);
    }

    /**
     * Gets dashboards by owner
     */
    public List<Dashboard> getByOwner(String tenantId, String owner) {
        log.debug("Fetching dashboards for owner: {} in tenant: {}", owner, tenantId);
        return dashboardRepository.findByOwner(tenantId, owner);
    }

    /**
     * Gets dashboards shared with a user
     */
    public List<Dashboard> getSharedWithUser(String tenantId, String userId) {
        log.debug("Fetching dashboards shared with user: {} in tenant: {}", userId, tenantId);
        return dashboardRepository.findSharedWith(tenantId, userId);
    }

    /**
     * Gets dashboards shared with a group
     */
    public List<Dashboard> getSharedWithGroup(String tenantId, String groupId) {
        log.debug("Fetching dashboards shared with group: {} in tenant: {}", groupId, tenantId);
        return dashboardRepository.findSharedWithGroup(tenantId, groupId);
    }

    /**
     * Gets dashboards accessible by a user
     */
    public List<Dashboard> getAccessibleByUser(String tenantId, String userId, Set<String> userGroupIds) {
        log.debug("Fetching dashboards accessible by user: {} in tenant: {}", userId, tenantId);

        return dashboardRepository.findAll(tenantId).stream()
                .filter(dashboard -> dashboard.hasAccess(userId, userGroupIds))
                .collect(Collectors.toList());
    }

    /**
     * Gets default dashboard
     */
    public Dashboard getDefault(String tenantId) {
        log.debug("Fetching default dashboard for tenant: {}", tenantId);
        return dashboardRepository.findDefault(tenantId)
                .orElseThrow(() -> new NotFoundException("DefaultDashboard", tenantId));
    }

    /**
     * Gets dashboards by type
     */
    public List<Dashboard> getByType(String tenantId, Dashboard.DashboardType type) {
        log.debug("Fetching dashboards by type: {} for tenant: {}", type, tenantId);
        return dashboardRepository.findByType(tenantId, type);
    }

    /**
     * Searches dashboards by name
     */
    public List<Dashboard> searchByName(String tenantId, String namePattern) {
        log.debug("Searching dashboards by name: {} for tenant: {}", namePattern, tenantId);
        return dashboardRepository.searchByName(tenantId, namePattern);
    }

    /**
     * Gets paginated dashboards for a tenant
     */
    public Page<Dashboard> getPaginated(String tenantId, int page, int size,
                                        String sortBy, String sortDirection) {
        log.debug("Fetching paginated dashboards for tenant: {}", tenantId);

        List<Dashboard> dashboards = dashboardRepository.findAll(tenantId);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return new PageImpl<>(dashboards, pageRequest, dashboards.size());
    }

    /**
     * Gets dashboard widgets
     */
    public List<Widget> getWidgetsForDashboard(String tenantId, String dashboardId) {
        log.debug("Fetching widgets for dashboard: {} in tenant: {}", dashboardId, tenantId);
        return widgetRepository.findByDashboardId(tenantId, dashboardId);
    }

    /**
     * Gets active dashboard widgets
     */
    public List<Widget> getActiveWidgetsForDashboard(String tenantId, String dashboardId) {
        log.debug("Fetching active widgets for dashboard: {} in tenant: {}", dashboardId, tenantId);
        return widgetRepository.findByDashboardId(tenantId, dashboardId).stream()
                .filter(w -> w.getStatus() == Widget.WidgetStatus.ACTIVE)
                .toList();
    }

    /**
     * Gets a dashboard view by ID
     */
    public DashboardView getViewById(String tenantId, String viewId) {
        log.debug("Fetching dashboard view: {} for tenant: {}", viewId, tenantId);
        return dashboardViewRepository.findByTenantIdAndViewId(tenantId, viewId)
                .orElseThrow(() -> new NotFoundException("DashboardView", viewId));
    }

    /**
     * Gets all dashboard views for a tenant
     */
    public List<DashboardView> getAllViewsForTenant(String tenantId) {
        log.debug("Fetching all dashboard views for tenant: {}", tenantId);
        return dashboardViewRepository.findByTenantId(tenantId);
    }

    /**
     * Gets dashboard views by owner
     */
    public List<DashboardView> getViewsByOwner(String tenantId, String ownerId) {
        log.debug("Fetching dashboard views for owner: {} in tenant: {}", ownerId, tenantId);
        return dashboardViewRepository.findByTenantIdAndOwnerId(tenantId, ownerId);
    }

    /**
     * Gets public dashboard views
     */
    public List<DashboardView> getPublicViews(String tenantId) {
        log.debug("Fetching public dashboard views for tenant: {}", tenantId);
        return dashboardViewRepository.findByTenantIdAndIsPublic(tenantId, true);
    }

    /**
     * Gets a dashboard widget by ID
     */
    public DashboardWidget getWidgetById(String tenantId, String widgetId) {
        log.debug("Fetching dashboard widget: {} for tenant: {}", widgetId, tenantId);
        return dashboardWidgetRepository.findByTenantIdAndWidgetId(tenantId, widgetId)
                .orElseThrow(() -> new NotFoundException("DashboardWidget", widgetId));
    }

    /**
     * Gets dashboard widgets for a dashboard
     */
    public List<DashboardWidget> getWidgetsByDashboard(String tenantId, String dashboardId) {
        log.debug("Fetching dashboard widgets for dashboard: {} in tenant: {}", dashboardId, tenantId);
        return dashboardWidgetRepository.findByDashboardId(tenantId, dashboardId);
    }

    /**
     * Gets active dashboard widgets for a dashboard
     */
    public List<DashboardWidget> getActiveWidgetsByDashboard(String tenantId, String dashboardId) {
        log.debug("Fetching active dashboard widgets for dashboard: {} in tenant: {}", dashboardId, tenantId);
        return dashboardWidgetRepository.findActiveByDashboardId(tenantId, dashboardId);
    }

    /**
     * Gets dashboard widgets by type
     */
    public List<DashboardWidget> getWidgetsByType(String tenantId, DashboardWidget.WidgetType type) {
        log.debug("Fetching dashboard widgets by type: {} for tenant: {}", type, tenantId);
        return dashboardWidgetRepository.findByTenantIdAndType(tenantId, type);
    }

    /**
     * Gets dashboard layout by dashboard ID
     */
    public DashboardLayout getLayoutByDashboard(String tenantId, String dashboardId) {
        log.debug("Fetching dashboard layout for dashboard: {} in tenant: {}", dashboardId, tenantId);
        return dashboardLayoutRepository.findByDashboardId(tenantId, dashboardId)
                .orElseThrow(() -> new NotFoundException("DashboardLayout", dashboardId));
    }

    /**
     * Gets dashboard layout by layout ID
     */
    public DashboardLayout getLayoutById(String tenantId, String layoutId) {
        log.debug("Fetching dashboard layout: {} for tenant: {}", layoutId, tenantId);
        return dashboardLayoutRepository.findByTenantIdAndLayoutId(tenantId, layoutId)
                .orElseThrow(() -> new NotFoundException("DashboardLayout", layoutId));
    }

    /**
     * Gets dashboard statistics summary
     */
    public DashboardStatistics getStatistics(String tenantId) {
        log.debug("Fetching dashboard statistics for tenant: {}", tenantId);

        List<Dashboard> dashboards = dashboardRepository.findAll(tenantId);

        long totalDashboards = dashboards.size();
        long activeDashboards = dashboards.stream()
                .filter(d -> d.getStatus() == Dashboard.DashboardStatus.ACTIVE)
                .count();
        long publicDashboards = dashboards.stream()
                .filter(Dashboard::getIsPublic)
                .count();

        List<Widget> allWidgets = dashboards.stream()
                .flatMap(d -> widgetRepository.findByDashboardId(tenantId, d.getDashboardId()).stream())
                .toList();

        long totalWidgets = allWidgets.size();
        long activeWidgets = allWidgets.stream()
                .filter(w -> w.getStatus() == Widget.WidgetStatus.ACTIVE)
                .count();

        return new DashboardStatistics(
                totalDashboards,
                activeDashboards,
                publicDashboards,
                totalWidgets,
                activeWidgets
        );
    }

    /**
     * Gets widgets needing refresh
     */
    public List<DashboardWidget> getWidgetsNeedingRefresh(String tenantId, Instant before) {
        log.debug("Fetching widgets needing refresh for tenant: {}", tenantId);
        return dashboardWidgetRepository.findWidgetsNeedingRefresh(tenantId, before);
    }

    private void publishViewedEvent(String dashboardId, String tenantId) {
        if (eventPublisher != null) {
            String userId = RequestContextHolder.getUserId().orElse(null);
            DashboardViewedEvent event = DashboardViewedEvent.create(
                    dashboardId, tenantId, userId, null, null, null);
            eventPublisher.publish("dashboard-viewed", event);
        }
    }

    /**
     * Dashboard statistics record
     */
    public record DashboardStatistics(
            long totalDashboards,
            long activeDashboards,
            long publicDashboards,
            long totalWidgets,
            long activeWidgets
    ) {}
}
