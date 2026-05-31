package com.gogidix.aiservices.aianalyticsdashboard.application.service;

import com.gogidix.aiservices.aianalyticsdashboard.application.dto.request.*;
import com.gogidix.aiservices.aianalyticsdashboard.application.dto.response.DashboardResponse;
import com.gogidix.aiservices.aianalyticsdashboard.application.dto.response.WidgetResponse;
import com.gogidix.aiservices.aianalyticsdashboard.domain.aggregate.Dashboard;
import com.gogidix.aiservices.aianalyticsdashboard.domain.model.Widget;
import com.gogidix.aiservices.aianalyticsdashboard.domain.port.out.DashboardRepository;
import com.gogidix.aiservices.aianalyticsdashboard.shared.exception.DashboardNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final DashboardRepository dashboardRepository;

    public DashboardService(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    public DashboardResponse createDashboard(CreateDashboardRequest request, String userId) {
        Dashboard dashboard = Dashboard.create(request.getName(), userId);

        if (request.getDescription() != null) {
            dashboard.setDescription(request.getDescription());
        }
        if (request.getRefreshInterval() != null) {
            dashboard.setRefreshInterval(request.getRefreshInterval());
        }
        if (request.getTheme() != null) {
            dashboard.setTheme(request.getTheme());
        }
        dashboard.setPublic(request.isPublic());

        if (request.getWidgets() != null) {
            for (AddWidgetRequest widgetReq : request.getWidgets()) {
                Widget widget = createWidgetFromRequest(widgetReq);
                dashboard.addWidget(widget);
            }
        }

        Dashboard saved = dashboardRepository.save(dashboard);
        return toResponse(saved);
    }

    public DashboardResponse getDashboard(String dashboardId) {
        Dashboard dashboard = dashboardRepository.findById(dashboardId)
                .orElseThrow(() -> new DashboardNotFoundException(dashboardId));
        return toResponse(dashboard);
    }

    public List<DashboardResponse> getUserDashboards(String userId) {
        return dashboardRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<DashboardResponse> getPublicDashboards() {
        return dashboardRepository.findPublicDashboards().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public DashboardResponse updateDashboard(String dashboardId, UpdateDashboardRequest request) {
        Dashboard dashboard = dashboardRepository.findById(dashboardId)
                .orElseThrow(() -> new DashboardNotFoundException(dashboardId));

        if (request.getName() != null) {
            // Name cannot be updated in current domain model
            // Would need to add setName method to Dashboard
        }
        if (request.getDescription() != null) {
            dashboard.setDescription(request.getDescription());
        }
        if (request.getRefreshInterval() != null) {
            dashboard.setRefreshInterval(request.getRefreshInterval());
        }
        if (request.getIsPublic() != null) {
            dashboard.setPublic(request.getIsPublic());
        }
        if (request.getTheme() != null) {
            dashboard.setTheme(request.getTheme());
        }

        Dashboard saved = dashboardRepository.save(dashboard);
        return toResponse(saved);
    }

    public void setDashboardVisibility(String dashboardId, boolean isPublic) {
        Dashboard dashboard = dashboardRepository.findById(dashboardId)
                .orElseThrow(() -> new DashboardNotFoundException(dashboardId));
        dashboard.setPublic(isPublic);
        dashboardRepository.save(dashboard);
    }

    public WidgetResponse addWidget(String dashboardId, AddWidgetRequest request) {
        Dashboard dashboard = dashboardRepository.findById(dashboardId)
                .orElseThrow(() -> new DashboardNotFoundException(dashboardId));

        Widget widget = createWidgetFromRequest(request);
        dashboard.addWidget(widget);

        dashboardRepository.save(dashboard);
        return toWidgetResponse(widget);
    }

    public void removeWidget(String dashboardId, String widgetId) {
        Dashboard dashboard = dashboardRepository.findById(dashboardId)
                .orElseThrow(() -> new DashboardNotFoundException(dashboardId));
        dashboard.removeWidget(widgetId);
        dashboardRepository.save(dashboard);
    }

    public void deleteDashboard(String dashboardId) {
        Dashboard dashboard = dashboardRepository.findById(dashboardId)
                .orElseThrow(() -> new DashboardNotFoundException(dashboardId));
        dashboardRepository.delete(dashboardId);
    }

    public Map<String, Object> exportDashboard(String dashboardId) {
        Dashboard dashboard = dashboardRepository.findById(dashboardId)
                .orElseThrow(() -> new DashboardNotFoundException(dashboardId));

        return Map.of(
                "name", dashboard.getName(),
                "description", dashboard.getDescription() != null ? dashboard.getDescription() : "",
                "refreshInterval", dashboard.getRefreshInterval(),
                "theme", dashboard.getTheme(),
                "widgets", dashboard.getWidgets().stream()
                        .map(w -> Map.of(
                                "type", w.getType(),
                                "title", w.getTitle(),
                                "dataSource", w.getDataSource(),
                                "config", w.getConfig()
                        )).toList()
        );
    }

    public DashboardResponse importDashboard(Map<String, Object> config, String userId) {
        String name = (String) config.getOrDefault("name", "Imported Dashboard");
        Dashboard dashboard = Dashboard.create(name, userId);

        if (config.containsKey("description")) {
            dashboard.setDescription((String) config.get("description"));
        }
        if (config.containsKey("refreshInterval")) {
            dashboard.setRefreshInterval(((Number) config.get("refreshInterval")).intValue());
        }
        if (config.containsKey("theme")) {
            dashboard.setTheme((String) config.get("theme"));
        }

        Dashboard saved = dashboardRepository.save(dashboard);
        return toResponse(saved);
    }

    private Widget createWidgetFromRequest(AddWidgetRequest request) {
        return Widget.builder()
                .widgetId(UUID.randomUUID().toString())
                .type(request.getType())
                .title(request.getTitle())
                .dataSource(request.getDataSource())
                .config(request.getConfig() != null ? request.getConfig() : Map.of())
                .position(request.getPosition() != null ? request.getPosition() : 0)
                .build();
    }

    private DashboardResponse toResponse(Dashboard dashboard) {
        return DashboardResponse.builder()
                .dashboardId(dashboard.getDashboardId())
                .userId(dashboard.getUserId())
                .name(dashboard.getName())
                .description(dashboard.getDescription())
                .widgets(dashboard.getWidgets().stream()
                        .map(this::toWidgetResponse)
                        .toList())
                .refreshInterval(dashboard.getRefreshInterval())
                .isPublic(dashboard.isPublic())
                .theme(dashboard.getTheme())
                .createdAt(dashboard.getCreatedAt())
                .updatedAt(dashboard.getUpdatedAt())
                .build();
    }

    private WidgetResponse toWidgetResponse(Widget widget) {
        return WidgetResponse.builder()
                .widgetId(widget.getWidgetId())
                .type(widget.getType())
                .title(widget.getTitle())
                .dataSource(widget.getDataSource())
                .config(widget.getConfig())
                .position(widget.getPosition())
                .build();
    }
}
