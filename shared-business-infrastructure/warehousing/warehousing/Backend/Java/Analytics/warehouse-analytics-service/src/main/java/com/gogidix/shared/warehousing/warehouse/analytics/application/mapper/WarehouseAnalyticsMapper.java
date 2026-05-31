package com.gogidix.shared.warehousing.warehouse.analytics.application.mapper;

import com.gogidix.shared.warehousing.warehouse.analytics.application.command.CreateMetricsCommand;
import com.gogidix.shared.warehousing.warehouse.analytics.application.command.GenerateReportCommand;
import com.gogidix.shared.warehousing.warehouse.analytics.application.dto.PerformanceDataDTO;
import com.gogidix.shared.warehousing.warehouse.analytics.application.dto.UtilizationReportDTO;
import com.gogidix.shared.warehousing.warehouse.analytics.application.dto.WarehouseMetricsDTO;
import com.gogidix.shared.warehousing.warehouse.analytics.domain.entity.PerformanceData;
import com.gogidix.shared.warehousing.warehouse.analytics.domain.entity.UtilizationReport;
import com.gogidix.shared.warehousing.warehouse.analytics.domain.entity.WarehouseMetrics;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.Named;

import java.util.List;

/**
 * MapStruct mapper for Warehouse Analytics entity/DTO conversions
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface WarehouseAnalyticsMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "availableCapacity", ignore = true)
    @Mapping(target = "utilizationPercentage", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    WarehouseMetrics toEntity(CreateMetricsCommand command);

    @Named("warehouseMetricsToDTO")
    WarehouseMetricsDTO toDTO(WarehouseMetrics metrics);

    @Named("warehouseMetricsToList")
    default List<WarehouseMetricsDTO> toMetricsDTOList(List<WarehouseMetrics> metrics) {
        return metrics.stream().map(this::toDTO).toList();
    }

    @Named("utilizationReportToDTO")
    UtilizationReportDTO toReportDTO(UtilizationReport report);

    @Named("utilizationReportToList")
    default List<UtilizationReportDTO> toReportDTOList(List<UtilizationReport> reports) {
        return reports.stream().map(this::toReportDTO).toList();
    }

    @Named("performanceDataToDTO")
    PerformanceDataDTO toPerformanceDTO(PerformanceData performance);

    @Named("performanceDataToList")
    default List<PerformanceDataDTO> toPerformanceDTOList(List<PerformanceData> performance) {
        return performance.stream().map(this::toPerformanceDTO).toList();
    }
}
