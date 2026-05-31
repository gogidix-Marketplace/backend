package com.gogidix.sales.dashboard.application.service;

import com.gogidix.sales.dashboard.application.dto.response.WidgetResponseDto;
import com.gogidix.sales.dashboard.domain.event.WidgetUpdatedEvent;
import com.gogidix.sales.dashboard.domain.model.KPIWidget;
import com.gogidix.sales.dashboard.domain.port.out.EventPublisher;
import com.gogidix.sales.dashboard.domain.repository.KPIWidgetRepository;
import com.gogidix.sales.dashboard.interfaces.rest.WidgetController;
import com.gogidix.sales.dashboard.shared.exception.NotFoundException;
import com.gogidix.sales.dashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Widget Command Service
 * Handles all write operations for KPI widgets
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WidgetCommandService {

    private final KPIWidgetRepository widgetRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public KPIWidget create(WidgetController.CreateWidgetRequest request) {
        log.info("Creating widget: {} for dashboard: {}", request.title(), request.dashboardId());

        KPIWidget widget = KPIWidget.create(
                RequestContextHolder.getTenantId(),
                request.dashboardId(),
                request.title(),
                request.widgetType(),
                request.category(),
                RequestContextHolder.getTenantId()
        );

        if (request.description() != null) {
            widget.setDescription(request.description());
        }

        if (request.configuration() != null && !request.configuration().isEmpty()) {
            updateWidgetConfiguration(widget, request.configuration());
        }

        if (request.dataSources() != null && !request.dataSources().isEmpty()) {
            request.dataSources().forEach(ds -> {
                KPIWidget.DataSource dataSource = KPIWidget.DataSource.builder()
                        .sourceId(ds.sourceId())
                        .sourceType(ds.sourceType())
                        .sourceKey(ds.sourceKey())
                        .field(ds.field())
                        .filters(ds.filters())
                        .aggregation(ds.aggregation())
                        .build();
                widget.addDataSource(dataSource);
            });
        }

        if (request.owner() != null) {
            widget.setOwner(request.owner());
        }

        if (request.tags() != null && !request.tags().isEmpty()) {
            request.tags().forEach(widget::addTag);
        }

        if (request.refreshFrequencyMinutes() != null) {
            widget.setRefreshFrequencyMinutes(request.refreshFrequencyMinutes());
        }

        if (request.initialValue() != null) {
            widget.updateValue(request.initialValue(), request.initialValue().toString());
        }

        KPIWidget saved = widgetRepository.save(widget);
        publishEvents(saved);

        log.info("Created widget: {}", saved.getWidgetId());
        return saved;
    }

    @Transactional
    public List<KPIWidget> createBatch(List<WidgetController.CreateWidgetRequest> requests) {
        log.info("Creating batch of {} widgets", requests.size());

        List<KPIWidget> widgets = requests.stream()
                .map(this::create)
                .toList();

        log.info("Created {} widgets in batch", widgets.size());
        return widgets;
    }

    @Transactional
    public void updateValue(String widgetId, WidgetController.UpdateValueRequest request) {
        log.info("Updating value for widget: {}", widgetId);

        KPIWidget widget = findByWidgetId(widgetId);
        widget.updateValue(request.value(), request.displayValue());

        widgetRepository.save(widget);
        publishEvents(widget);

        log.info("Updated value for widget: {}", widgetId);
    }

    @Transactional
    public void updateValueWithTrend(String widgetId, WidgetController.UpdateValueWithTrendRequest request) {
        log.info("Updating value with trend for widget: {}", widgetId);

        KPIWidget widget = findByWidgetId(widgetId);
        widget.updateValueWithTrend(
                request.value(),
                request.displayValue(),
                request.direction(),
                request.changeValue()
        );

        widgetRepository.save(widget);
        publishEvents(widget);

        log.info("Updated value with trend for widget: {}", widgetId);
    }

    @Transactional
    public void updateValueWithTarget(String widgetId, WidgetController.UpdateValueWithTargetRequest request) {
        log.info("Updating value with target for widget: {}", widgetId);

        KPIWidget widget = findByWidgetId(widgetId);
        widget.updateValueWithTarget(
                request.value(),
                request.displayValue(),
                request.target(),
                request.actual()
        );

        widgetRepository.save(widget);
        publishEvents(widget);

        log.info("Updated value with target for widget: {}", widgetId);
    }

    @Transactional
    public KPIWidget update(String widgetId, WidgetController.UpdateWidgetRequest request) {
        log.info("Updating widget: {}", widgetId);

        KPIWidget widget = findByWidgetId(widgetId);

        if (request.title() != null) {
            widget.setTitle(request.title());
        }
        if (request.description() != null) {
            widget.setDescription(request.description());
        }
        if (request.refreshFrequencyMinutes() != null) {
            widget.setRefreshFrequencyMinutes(request.refreshFrequencyMinutes());
        }

        KPIWidget saved = widgetRepository.save(widget);

        log.info("Updated widget: {}", widgetId);
        return saved;
    }

    @Transactional
    public void updateLayout(String widgetId, WidgetController.UpdateLayoutRequest request) {
        log.info("Updating layout for widget: {}", widgetId);

        KPIWidget widget = findByWidgetId(widgetId);

        KPIWidget.LayoutInfo layout = KPIWidget.LayoutInfo.builder()
                .row(request.row() != null ? request.row() : widget.getLayout().getRow())
                .column(request.column() != null ? request.column() : widget.getLayout().getColumn())
                .rowSpan(request.rowSpan() != null ? request.rowSpan() : widget.getLayout().getRowSpan())
                .columnSpan(request.columnSpan() != null ? request.columnSpan() : widget.getLayout().getColumnSpan())
                .zIndex(request.zIndex() != null ? request.zIndex() : widget.getLayout().getZIndex())
                .isVisible(request.isVisible() != null ? request.isVisible() : widget.getLayout().getIsVisible())
                .isCollapsed(request.isCollapsed() != null ? request.isCollapsed() : widget.getLayout().getIsCollapsed())
                .build();

        widget.updateLayout(layout);
        widgetRepository.save(widget);

        log.info("Updated layout for widget: {}", widgetId);
    }

    @Transactional
    public void updateThresholds(String widgetId, WidgetController.UpdateThresholdsRequest request) {
        log.info("Updating thresholds for widget: {}", widgetId);

        KPIWidget widget = findByWidgetId(widgetId);

        List<KPIWidget.Threshold> thresholds = request.thresholds().stream()
                .map(t -> KPIWidget.Threshold.builder()
                        .label(t.label())
                        .minValue(t.minValue())
                        .maxValue(t.maxValue())
                        .color(t.color())
                        .severity(t.severity())
                        .build())
                .toList();

        widget.updateThresholds(thresholds);

        if (request.enableAlerts() != null) {
            if (widget.getThresholdConfig() == null) {
                widget.setThresholdConfig(KPIWidget.ThresholdConfig.builder()
                        .type(KPIWidget.ThresholdType.RANGE)
                        .thresholds(thresholds)
                        .build());
            }
            widget.getThresholdConfig().setEnableAlerts(request.enableAlerts());
        }

        widgetRepository.save(widget);

        log.info("Updated thresholds for widget: {}", widgetId);
    }

    @Transactional
    public void activate(String widgetId) {
        log.info("Activating widget: {}", widgetId);

        KPIWidget widget = findByWidgetId(widgetId);
        widget.activate();
        widgetRepository.save(widget);

        log.info("Activated widget: {}", widgetId);
    }

    @Transactional
    public void deactivate(String widgetId) {
        log.info("Deactivating widget: {}", widgetId);

        KPIWidget widget = findByWidgetId(widgetId);
        widget.deactivate();
        widgetRepository.save(widget);

        log.info("Deactivated widget: {}", widgetId);
    }

    @Transactional
    public void addTag(String widgetId, String tag) {
        log.info("Adding tag {} to widget: {}", tag, widgetId);

        KPIWidget widget = findByWidgetId(widgetId);
        widget.addTag(tag);
        widgetRepository.save(widget);

        log.info("Added tag to widget: {}", widgetId);
    }

    @Transactional
    public void removeTag(String widgetId, String tag) {
        log.info("Removing tag {} from widget: {}", tag, widgetId);

        KPIWidget widget = findByWidgetId(widgetId);
        widget.removeTag(tag);
        widgetRepository.save(widget);

        log.info("Removed tag from widget: {}", widgetId);
    }

    @Transactional
    public void delete(String widgetId) {
        log.info("Deleting widget: {}", widgetId);

        KPIWidget widget = findByWidgetId(widgetId);
        widgetRepository.deleteByWidgetIdAndTenantId(widgetId, widget.getTenantId());

        log.info("Deleted widget: {}", widgetId);
    }

    private KPIWidget findByWidgetId(String widgetId) {
        String tenantId = RequestContextHolder.getTenantId();
        return widgetRepository.findByWidgetIdAndTenantId(widgetId, tenantId)
                .orElseThrow(() -> new NotFoundException("Widget", widgetId));
    }

    private void publishEvents(KPIWidget widget) {
        if (!widget.getDomainEvents().isEmpty() && eventPublisher != null && eventPublisher.isReady()) {
            for (var event : widget.getDomainEvents()) {
                if (event instanceof WidgetUpdatedEvent) {
                    eventPublisher.publishWidgetEvent((WidgetUpdatedEvent) event);
                }
            }
            widget.clearDomainEvents();
        }
    }

    private void updateWidgetConfiguration(KPIWidget widget, Map<String, Object> config) {
        KPIWidget.WidgetConfiguration widgetConfig = KPIWidget.WidgetConfiguration.builder()
                .decimalPlaces((Integer) config.getOrDefault("decimalPlaces", 2))
                .showTrend((Boolean) config.getOrDefault("showTrend", true))
                .showTarget((Boolean) config.getOrDefault("showTarget", true))
                .comparisonPeriod((String) config.getOrDefault("comparisonPeriod", "PREVIOUS_PERIOD"))
                .showSparkline((Boolean) config.getOrDefault("showSparkline", true))
                .sparklinePoints((Integer) config.getOrDefault("sparklinePoints", 30))
                .aggregationType((String) config.getOrDefault("aggregationType", "SUM"))
                .customConfig(config)
                .build();

        widget.setConfiguration(widgetConfig);
    }
}
