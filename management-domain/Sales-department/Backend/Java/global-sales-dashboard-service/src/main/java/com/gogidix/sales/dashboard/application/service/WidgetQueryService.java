package com.gogidix.sales.dashboard.application.service;

import com.gogidix.sales.dashboard.application.dto.response.WidgetResponseDto;
import com.gogidix.sales.dashboard.domain.model.KPIWidget;
import com.gogidix.sales.dashboard.domain.repository.KPIWidgetRepository;
import com.gogidix.sales.dashboard.shared.exception.NotFoundException;
import com.gogidix.sales.dashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Widget Query Service
 * Handles all read operations for KPI widgets
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WidgetQueryService {

    private final KPIWidgetRepository widgetRepository;

    public List<WidgetResponseDto> getAllForTenant() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching all widgets for tenant: {}", tenantId);

        List<KPIWidget> widgets = widgetRepository.findByTenantId(tenantId);
        return widgets.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "widgets", key = "#widgetId")
    public WidgetResponseDto getById(String widgetId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching widget: {} for tenant: {}", widgetId, tenantId);

        KPIWidget widget = widgetRepository.findByWidgetIdAndTenantId(widgetId, tenantId)
                .orElseThrow(() -> new NotFoundException("Widget", widgetId));

        return toDto(widget);
    }

    public List<WidgetResponseDto> getByDashboardId(String dashboardId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching widgets for dashboard: {} for tenant: {}", dashboardId, tenantId);

        List<KPIWidget> widgets = widgetRepository.findByDashboardIdAndTenantId(dashboardId, tenantId);
        return widgets.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<WidgetResponseDto> getByType(KPIWidget.WidgetType type) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching widgets by type: {} for tenant: {}", type, tenantId);

        List<KPIWidget> widgets = widgetRepository.findByTenantIdAndWidgetType(tenantId, type);
        return widgets.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<WidgetResponseDto> getByCategory(KPIWidget.WidgetCategory category) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching widgets by category: {} for tenant: {}", category, tenantId);

        List<KPIWidget> widgets = widgetRepository.findByTenantIdAndCategory(tenantId, category);
        return widgets.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<WidgetResponseDto> getActiveWidgets() {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching active widgets for tenant: {}", tenantId);

        List<KPIWidget> widgets = widgetRepository.findByTenantIdAndIsActive(tenantId, true);
        return widgets.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<WidgetResponseDto> getByOwner(String owner) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching widgets for owner: {} for tenant: {}", owner, tenantId);

        List<KPIWidget> widgets = widgetRepository.findByTenantIdAndOwner(tenantId, owner);
        return widgets.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<WidgetResponseDto> searchByTitle(String searchTerm) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Searching widgets with term: {} for tenant: {}", searchTerm, tenantId);

        List<KPIWidget> widgets = widgetRepository.searchByTitle(tenantId, searchTerm);
        return widgets.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<WidgetResponseDto> getWidgetsNeedingRefresh(int minutesThreshold) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching widgets needing refresh (threshold: {} minutes) for tenant: {}", minutesThreshold, tenantId);

        List<KPIWidget> widgets = widgetRepository.findWidgetsNeedingRefresh(tenantId, minutesThreshold);
        return widgets.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Map<String, Object> checkThreshold(String widgetId) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Checking threshold for widget: {} for tenant: {}", widgetId, tenantId);

        KPIWidget widget = widgetRepository.findByWidgetIdAndTenantId(widgetId, tenantId)
                .orElseThrow(() -> new NotFoundException("Widget", widgetId));

        Map<String, Object> result = new HashMap<>();
        result.put("widgetId", widgetId);
        result.put("withinThreshold", widget.isWithinThreshold());
        result.put("currentThreshold", widget.getCurrentThreshold());

        return result;
    }

    public List<WidgetResponseDto> getByTag(String tag) {
        String tenantId = RequestContextHolder.getTenantId();

        log.debug("Fetching widgets with tag: {} for tenant: {}", tag, tenantId);

        List<KPIWidget> widgets = widgetRepository.findByTenantIdAndTagsContaining(tenantId, tag);
        return widgets.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public WidgetResponseDto toDto(KPIWidget widget) {
        return WidgetResponseDto.builder()
                .id(widget.getId())
                .widgetId(widget.getWidgetId())
                .tenantId(widget.getTenantId())
                .dashboardId(widget.getDashboardId())
                .title(widget.getTitle())
                .description(widget.getDescription())
                .widgetType(widget.getWidgetType().name())
                .category(widget.getCategory().name())
                .value(widget.getCurrentValue() != null ? widget.getCurrentValue().getValue() : null)
                .displayValue(widget.getCurrentValue() != null ? widget.getCurrentValue().getDisplayValue() : null)
                .trendInfo(toTrendInfoDto(widget.getCurrentValue()))
                .targetInfo(toTargetInfoDto(widget.getCurrentValue()))
                .row(widget.getLayout() != null ? widget.getLayout().getRow() : null)
                .column(widget.getLayout() != null ? widget.getLayout().getColumn() : null)
                .rowSpan(widget.getLayout() != null ? widget.getLayout().getRowSpan() : null)
                .columnSpan(widget.getLayout() != null ? widget.getLayout().getColumnSpan() : null)
                .isVisible(widget.getLayout() != null ? widget.getLayout().getIsVisible() : true)
                .isActive(widget.getIsActive())
                .refreshFrequencyMinutes(widget.getRefreshFrequencyMinutes())
                .owner(widget.getOwner())
                .tags(widget.getTags())
                .lastUpdated(widget.getLastUpdated())
                .createdAt(widget.getCreatedAt())
                .updatedAt(widget.getUpdatedAt())
                .build();
    }

    private WidgetResponseDto.TrendInfoDto toTrendInfoDto(KPIWidget.WidgetValue value) {
        if (value == null || value.getTrendInfo() == null) {
            return null;
        }
        return WidgetResponseDto.TrendInfoDto.builder()
                .direction(value.getTrendInfo().getDirection())
                .value(toDouble(value.getTrendInfo().getValue()))
                .percentage(value.getTrendInfo().getPercentage())
                .isPositive(value.getTrendInfo().getIsPositive())
                .build();
    }

    private WidgetResponseDto.TargetInfoDto toTargetInfoDto(KPIWidget.WidgetValue value) {
        if (value == null || value.getTargetInfo() == null) {
            return null;
        }
        return WidgetResponseDto.TargetInfoDto.builder()
                .target(toDouble(value.getTargetInfo().getTarget()))
                .actual(toDouble(value.getTargetInfo().getActual()))
                .achievement(toDouble(value.getTargetInfo().getAchievement()))
                .remaining(toDouble(value.getTargetInfo().getRemaining()))
                .isOnTrack(value.getTargetInfo().getIsOnTrack())
                .build();
    }

    private Double toDouble(java.math.BigDecimal value) {
        return value != null ? value.doubleValue() : null;
    }
}
