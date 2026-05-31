package com.gogidix.globalbusinessmanagement.datavalidation.infrastructure.mapper;

import com.gogidix.globalbusinessmanagement.datavalidation.domain.dto.DataQualityReportDTO;
import com.gogidix.globalbusinessmanagement.datavalidation.domain.model.DataQualityReport;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * MapStruct mapper for DataQualityReport entity and DTO
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DataQualityReportMapper {

    DataQualityReport toEntity(DataQualityReportDTO dto);

    DataQualityReportDTO toDto(DataQualityReport entity);

    List<DataQualityReport> toEntityList(List<DataQualityReportDTO> dtoList);

    List<DataQualityReportDTO> toDtoList(List<DataQualityReport> entityList);

    void updateEntityFromDto(DataQualityReportDTO dto, @MappingTarget DataQualityReport entity);
}
