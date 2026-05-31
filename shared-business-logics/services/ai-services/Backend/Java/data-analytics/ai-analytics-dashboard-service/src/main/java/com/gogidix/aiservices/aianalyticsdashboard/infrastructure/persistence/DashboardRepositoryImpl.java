package com.gogidix.aiservices.aianalyticsdashboard.infrastructure.persistence;

import com.gogidix.aiservices.aianalyticsdashboard.domain.aggregate.Dashboard;
import com.gogidix.aiservices.aianalyticsdashboard.domain.model.Widget;
import com.gogidix.aiservices.aianalyticsdashboard.domain.port.out.DashboardRepository;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Repository
@RequiredArgsConstructor
public class DashboardRepositoryImpl implements DashboardRepository {

    private final DashboardDataSource dataSource;

    @Override
    public Dashboard save(Dashboard dashboard) {
        DashboardEntity entity = toEntity(dashboard);
        DashboardEntity saved = dataSource.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Dashboard> findById(String dashboardId) {
        return dataSource.findById(dashboardId)
                .map(this::toDomain);
    }

    @Override
    public List<Dashboard> findByUserId(String userId) {
        return dataSource.findByUserId(userId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Dashboard> findPublicDashboards() {
        return dataSource.findPublicDashboards().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String dashboardId) {
        dataSource.delete(dashboardId);
    }

    @Override
    public List<Dashboard> findUpdatedAfter(Instant timestamp) {
        return dataSource.findUpdatedAfter(timestamp).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private DashboardEntity toEntity(Dashboard dashboard) {
        DashboardEntity entity = new DashboardEntity();
        entity.setDashboardId(dashboard.getDashboardId());
        entity.setUserId(dashboard.getUserId());
        entity.setName(dashboard.getName());
        entity.setDescription(dashboard.getDescription());
        entity.setRefreshInterval(dashboard.getRefreshInterval());
        entity.setCreatedAt(dashboard.getCreatedAt());
        entity.setUpdatedAt(dashboard.getUpdatedAt());
        entity.setPublic(dashboard.isPublic());
        entity.setTheme(dashboard.getTheme());
        entity.setWidgets(dashboard.getWidgets().stream()
                .map(this::toWidgetEntity)
                .collect(Collectors.toList()));
        return entity;
    }

    private WidgetEntity toWidgetEntity(Widget widget) {
        WidgetEntity entity = new WidgetEntity();
        entity.setWidgetId(widget.getWidgetId());
        entity.setType(widget.getType());
        entity.setTitle(widget.getTitle());
        entity.setDataSource(widget.getDataSource());
        entity.setConfig(widget.getConfig());
        entity.setPosition(widget.getPosition());
        return entity;
    }

    private Dashboard toDomain(DashboardEntity entity) {
        return Dashboard.restore(
                entity.getDashboardId(),
                entity.getUserId(),
                entity.getName(),
                entity.getDescription(),
                entity.getWidgets() != null
                        ? entity.getWidgets().stream()
                                .map(this::toWidgetDomain)
                                .collect(Collectors.toList())
                        : List.of(),
                entity.getRefreshInterval(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.isPublic(),
                entity.getTheme()
        );
    }

    private Widget toWidgetDomain(WidgetEntity entity) {
        return Widget.builder()
                .widgetId(entity.getWidgetId())
                .type(entity.getType())
                .title(entity.getTitle())
                .dataSource(entity.getDataSource())
                .config(entity.getConfig())
                .position(entity.getPosition())
                .build();
    }
}
