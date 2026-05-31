package com.gogidix.dashboard.core.application.mapper;

import com.gogidix.dashboard.core.application.dto.request.CreateKPIRequestDto;
import com.gogidix.dashboard.core.application.dto.request.UpdateKPIRequestDto;
import com.gogidix.dashboard.core.application.dto.response.KPIResponseDto;
import com.gogidix.dashboard.core.domain.model.DashboardKPI;
import com.gogidix.dashboard.core.domain.model.KPIValue;
import com.gogidix.dashboard.core.domain.model.KPITarget;
import com.gogidix.dashboard.core.domain.port.in.CreateKPICommand;
import com.gogidix.dashboard.core.domain.port.in.UpdateKPICommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * MapStruct mapper for KPI entities and DTOs.
 */
@Mapper(componentModel = "spring")
public interface KPIMapper {

    DashboardKPI toEntity(CreateKPIRequestDto dto);

    default CreateKPICommand toCreateCommand(CreateKPIRequestDto dto, String tenantId) {
        return CreateKPICommand.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .code(dto.getCode())
                .category(dto.getCategory())
                .tenantId(tenantId)
                .sourceDomain(dto.getSourceDomain())
                .unit(dto.getUnit())
                .dataType(dto.getDataType())
                .aggregationType(dto.getAggregationType())
                .formula(dto.getFormula())
                .isActive(dto.getIsActive())
                .isRealTime(dto.getIsRealTime())
                .refreshIntervalSeconds(dto.getRefreshIntervalSeconds())
                .thresholdWarning(dto.getThresholdWarning())
                .thresholdCritical(dto.getThresholdCritical())
                .targetValue(dto.getTargetValue())
                .build();
    }

    default UpdateKPICommand toUpdateCommand(String kpiId, UpdateKPIRequestDto dto) {
        return UpdateKPICommand.builder()
                .kpiId(kpiId)
                .name(dto.getName())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .unit(dto.getUnit())
                .dataType(dto.getDataType())
                .aggregationType(dto.getAggregationType())
                .formula(dto.getFormula())
                .isActive(dto.getIsActive())
                .isRealTime(dto.getIsRealTime())
                .refreshIntervalSeconds(dto.getRefreshIntervalSeconds())
                .thresholdWarning(dto.getThresholdWarning())
                .thresholdCritical(dto.getThresholdCritical())
                .targetValue(dto.getTargetValue())
                .build();
    }

    @Mapping(target = "sourceDomain", expression = "java(entity.getSourceDomain().name())")
    @Mapping(target = "progressPercentage", ignore = true)
    @Mapping(target = "status", ignore = true)
    KPIResponseDto toDto(DashboardKPI entity);

    List<KPIResponseDto> toDtoList(List<DashboardKPI> entities);

    KPIResponseDto.KPIValueDto toValueDto(KPIValue entity);

    KPIResponseDto.KPITargetDto toTargetDto(KPITarget entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntityFromDto(UpdateKPIRequestDto dto, @MappingTarget DashboardKPI entity);
}
