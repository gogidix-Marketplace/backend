package com.gogidix.globalbusinessmanagement.dashboard.application.mapper;

import com.gogidix.globalbusinessmanagement.dashboard.application.dto.KPIBoardDto;
import com.gogidix.globalbusinessmanagement.dashboard.domain.model.KPIBoard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for KPIBoard domain model and DTO.
 */
@Mapper(
    componentModel = "spring",
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface KPIBoardMapper {

    KPIBoardDto toDto(KPIBoard entity);

    KPIBoard toEntity(KPIBoardDto dto);

    List<KPIBoardDto> toDtoList(List<KPIBoard> entities);

    List<KPIBoard> toEntityList(List<KPIBoardDto> dtos);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(KPIBoardDto dto, @MappingTarget KPIBoard entity);

    default KPIBoardDto.KPIItemDto toKPIItemDto(KPIBoard.KPIItem item) {
        if (item == null) {
            return null;
        }
        return KPIBoardDto.KPIItemDto.builder()
            .kpiId(item.getKpiId())
            .name(item.getName())
            .description(item.getDescription())
            .type(item.getType() != null ? item.getType().name() : null)
            .dataSource(toDataSourceDto(item.getDataSource()))
            .display(toDisplayConfigDto(item.getDisplay()))
            .aggregationType(item.getAggregationType() != null ? item.getAggregationType().name() : null)
            .comparisonType(item.getComparisonType() != null ? item.getComparisonType().name() : null)
            .comparisonPeriod(item.getComparisonPeriod())
            .threshold(toThresholdConfigDto(item.getThreshold()))
            .unit(item.getUnit())
            .decimalPlaces(item.getDecimalPlaces())
            .currentValue(toKPIValueDto(item.getCurrentValue()))
            .previousValue(toKPIValueDto(item.getPreviousValue()))
            .changePercentage(item.getChangePercentage())
            .trend(item.getTrend())
            .historicalValues(toKPIValueDtoList(item.getHistoricalValues()))
            .displayOrder(item.getDisplayOrder())
            .visible(item.getVisible())
            .metadata(item.getMetadata())
            .build();
    }

    default KPIBoard.KPIItem toKPIItem(KPIBoardDto.KPIItemDto dto) {
        if (dto == null) {
            return null;
        }
        return KPIBoard.KPIItem.builder()
            .kpiId(dto.getKpiId())
            .name(dto.getName())
            .description(dto.getDescription())
            .type(dto.getType() != null ? KPIBoard.KPIType.valueOf(dto.getType()) : null)
            .dataSource(toDataSource(dto.getDataSource()))
            .display(toDisplayConfig(dto.getDisplay()))
            .aggregationType(dto.getAggregationType() != null ?
                KPIBoard.AggregationType.valueOf(dto.getAggregationType()) : null)
            .comparisonType(dto.getComparisonType() != null ?
                KPIBoard.ComparisonType.valueOf(dto.getComparisonType()) : null)
            .comparisonPeriod(dto.getComparisonPeriod())
            .threshold(toThresholdConfig(dto.getThreshold()))
            .unit(dto.getUnit())
            .decimalPlaces(dto.getDecimalPlaces())
            .currentValue(toKPIValue(dto.getCurrentValue()))
            .previousValue(toKPIValue(dto.getPreviousValue()))
            .changePercentage(dto.getChangePercentage())
            .trend(dto.getTrend())
            .historicalValues(toKPIValueList(dto.getHistoricalValues()))
            .displayOrder(dto.getDisplayOrder())
            .visible(dto.getVisible())
            .metadata(dto.getMetadata())
            .build();
    }

    default KPIBoardDto.DataSourceDto toDataSourceDto(KPIBoard.KPIItem.DataSource dataSource) {
        if (dataSource == null) {
            return null;
        }
        return KPIBoardDto.DataSourceDto.builder()
            .sourceType(dataSource.getSourceType())
            .endpoint(dataSource.getEndpoint())
            .query(dataSource.getQuery())
            .collection(dataSource.getCollection())
            .parameters(dataSource.getParameters())
            .build();
    }

    default KPIBoard.KPIItem.DataSource toDataSource(KPIBoardDto.DataSourceDto dto) {
        if (dto == null) {
            return null;
        }
        return KPIBoard.KPIItem.DataSource.builder()
            .sourceType(dto.getSourceType())
            .endpoint(dto.getEndpoint())
            .query(dto.getQuery())
            .collection(dto.getCollection())
            .parameters(dto.getParameters())
            .build();
    }

    default KPIBoardDto.DisplayConfigDto toDisplayConfigDto(KPIBoard.KPIItem.DisplayConfig display) {
        if (display == null) {
            return null;
        }
        return KPIBoardDto.DisplayConfigDto.builder()
            .visualizationType(display.getVisualizationType())
            .color(display.getColor())
            .width(display.getWidth())
            .height(display.getHeight())
            .showSparkline(display.getShowSparkline())
            .showTrend(display.getShowTrend())
            .showComparison(display.getShowComparison())
            .chartType(display.getChartType())
            .build();
    }

    default KPIBoard.KPIItem.DisplayConfig toDisplayConfig(KPIBoardDto.DisplayConfigDto dto) {
        if (dto == null) {
            return null;
        }
        return KPIBoard.KPIItem.DisplayConfig.builder()
            .visualizationType(dto.getVisualizationType())
            .color(dto.getColor())
            .width(dto.getWidth())
            .height(dto.getHeight())
            .showSparkline(dto.getShowSparkline())
            .showTrend(dto.getShowTrend())
            .showComparison(dto.getShowComparison())
            .chartType(dto.getChartType())
            .build();
    }

    default KPIBoardDto.ThresholdConfigDto toThresholdConfigDto(KPIBoard.KPIItem.ThresholdConfig threshold) {
        if (threshold == null) {
            return null;
        }
        return KPIBoardDto.ThresholdConfigDto.builder()
            .type(threshold.getType() != null ? threshold.getType().name() : null)
            .warningThreshold(threshold.getWarningThreshold())
            .criticalThreshold(threshold.getCriticalThreshold())
            .targetThreshold(threshold.getTargetThreshold())
            .warningColor(threshold.getWarningColor())
            .criticalColor(threshold.getCriticalColor())
            .targetColor(threshold.getTargetColor())
            .build();
    }

    default KPIBoard.KPIItem.ThresholdConfig toThresholdConfig(KPIBoardDto.ThresholdConfigDto dto) {
        if (dto == null) {
            return null;
        }
        return KPIBoard.KPIItem.ThresholdConfig.builder()
            .type(dto.getType() != null ? KPIBoard.ThresholdType.valueOf(dto.getType()) : null)
            .warningThreshold(dto.getWarningThreshold())
            .criticalThreshold(dto.getCriticalThreshold())
            .targetThreshold(dto.getTargetThreshold())
            .warningColor(dto.getWarningColor())
            .criticalColor(dto.getCriticalColor())
            .targetColor(dto.getTargetColor())
            .build();
    }

    default KPIBoardDto.KPIValueDto toKPIValueDto(KPIBoard.KPIItem.KPIValue value) {
        if (value == null) {
            return null;
        }
        return KPIBoardDto.KPIValueDto.builder()
            .value(value.getValue())
            .timestamp(value.getTimestamp())
            .period(value.getPeriod())
            .attributes(value.getAttributes())
            .build();
    }

    default KPIBoard.KPIItem.KPIValue toKPIValue(KPIBoardDto.KPIValueDto dto) {
        if (dto == null) {
            return null;
        }
        return KPIBoard.KPIItem.KPIValue.builder()
            .value(dto.getValue())
            .timestamp(dto.getTimestamp())
            .period(dto.getPeriod())
            .attributes(dto.getAttributes())
            .build();
    }

    default List<KPIBoardDto.KPIValueDto> toKPIValueDtoList(List<KPIBoard.KPIItem.KPIValue> values) {
        if (values == null) {
            return null;
        }
        return values.stream()
            .map(this::toKPIValueDto)
            .toList();
    }

    default List<KPIBoard.KPIItem.KPIValue> toKPIValueList(List<KPIBoardDto.KPIValueDto> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
            .map(this::toKPIValue)
            .toList();
    }

    default KPIBoardDto.BoardLayoutDto toBoardLayoutDto(KPIBoard.BoardLayout layout) {
        if (layout == null) {
            return null;
        }
        return KPIBoardDto.BoardLayoutDto.builder()
            .layoutType(layout.getLayoutType())
            .columns(layout.getColumns())
            .rows(layout.getRows())
            .items(toLayoutItemDtoList(layout.getItems()))
            .build();
    }

    default KPIBoard.BoardLayout toBoardLayout(KPIBoardDto.BoardLayoutDto dto) {
        if (dto == null) {
            return null;
        }
        return KPIBoard.BoardLayout.builder()
            .layoutType(dto.getLayoutType())
            .columns(dto.getColumns())
            .rows(dto.getRows())
            .items(toLayoutItemList(dto.getItems()))
            .build();
    }

    default KPIBoardDto.LayoutItemDto toLayoutItemDto(KPIBoard.BoardLayout.LayoutItem item) {
        if (item == null) {
            return null;
        }
        return KPIBoardDto.LayoutItemDto.builder()
            .kpiId(item.getKpiId())
            .column(item.getColumn())
            .row(item.getRow())
            .columnSpan(item.getColumnSpan())
            .rowSpan(item.getRowSpan())
            .build();
    }

    default KPIBoard.BoardLayout.LayoutItem toLayoutItem(KPIBoardDto.LayoutItemDto dto) {
        if (dto == null) {
            return null;
        }
        return KPIBoard.BoardLayout.LayoutItem.builder()
            .kpiId(dto.getKpiId())
            .column(dto.getColumn())
            .row(dto.getRow())
            .columnSpan(dto.getColumnSpan())
            .rowSpan(dto.getRowSpan())
            .build();
    }

    default List<KPIBoardDto.LayoutItemDto> toLayoutItemDtoList(List<KPIBoard.BoardLayout.LayoutItem> items) {
        if (items == null) {
            return null;
        }
        return items.stream()
            .map(this::toLayoutItemDto)
            .toList();
    }

    default List<KPIBoard.BoardLayout.LayoutItem> toLayoutItemList(List<KPIBoardDto.LayoutItemDto> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
            .map(this::toLayoutItem)
            .toList();
    }

    default KPIBoardDto.BoardPreferencesDto toBoardPreferencesDto(KPIBoard.BoardPreferences preferences) {
        if (preferences == null) {
            return null;
        }
        return KPIBoardDto.BoardPreferencesDto.builder()
            .theme(preferences.getTheme())
            .autoRefresh(preferences.getAutoRefresh())
            .refreshInterval(preferences.getRefreshInterval())
            .timeZone(preferences.getTimeZone())
            .dateFormat(preferences.getDateFormat())
            .numberFormat(preferences.getNumberFormat())
            .showAnnotations(preferences.getShowAnnotations())
            .enableDrillDown(preferences.getEnableDrillDown())
            .build();
    }

    default KPIBoard.BoardPreferences toBoardPreferences(KPIBoardDto.BoardPreferencesDto dto) {
        if (dto == null) {
            return null;
        }
        return KPIBoard.BoardPreferences.builder()
            .theme(dto.getTheme())
            .autoRefresh(dto.getAutoRefresh())
            .refreshInterval(dto.getRefreshInterval())
            .timeZone(dto.getTimeZone())
            .dateFormat(dto.getDateFormat())
            .numberFormat(dto.getNumberFormat())
            .showAnnotations(dto.getShowAnnotations())
            .enableDrillDown(dto.getEnableDrillDown())
            .build();
    }

    default KPIBoardDto.RefreshScheduleDto toRefreshScheduleDto(KPIBoard.RefreshSchedule schedule) {
        if (schedule == null) {
            return null;
        }
        return KPIBoardDto.RefreshScheduleDto.builder()
            .scheduleType(schedule.getScheduleType())
            .cronExpression(schedule.getCronExpression())
            .intervalMinutes(schedule.getIntervalMinutes())
            .timeZone(schedule.getTimeZone())
            .includeWeekends(schedule.getIncludeWeekends())
            .build();
    }

    default KPIBoard.RefreshSchedule toRefreshSchedule(KPIBoardDto.RefreshScheduleDto dto) {
        if (dto == null) {
            return null;
        }
        return KPIBoard.RefreshSchedule.builder()
            .scheduleType(dto.getScheduleType())
            .cronExpression(dto.getCronExpression())
            .intervalMinutes(dto.getIntervalMinutes())
            .timeZone(dto.getTimeZone())
            .includeWeekends(dto.getIncludeWeekends())
            .build();
    }

    default String mapBoardScope(KPIBoard.BoardScope scope) {
        return scope != null ? scope.name() : null;
    }

    default KPIBoard.BoardScope mapBoardScope(String scope) {
        return scope != null ? KPIBoard.BoardScope.valueOf(scope) : null;
    }

    default String mapBoardStatus(KPIBoard.BoardStatus status) {
        return status != null ? status.name() : null;
    }

    default KPIBoard.BoardStatus mapBoardStatus(String status) {
        return status != null ? KPIBoard.BoardStatus.valueOf(status) : null;
    }

    default List<KPIBoardDto.KPIItemDto> toKPIItemDtoList(List<KPIBoard.KPIItem> items) {
        if (items == null) {
            return null;
        }
        return items.stream()
            .map(this::toKPIItemDto)
            .toList();
    }

    default List<KPIBoard.KPIItem> toKPIItemList(List<KPIBoardDto.KPIItemDto> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream()
            .map(this::toKPIItem)
            .toList();
    }
}
