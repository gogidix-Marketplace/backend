package com.gogidix.shared.warehousing.quality.application.mapper;

import com.gogidix.shared.warehousing.quality.application.command.CreateQualityCheckCommand;
import com.gogidix.shared.warehousing.quality.application.command.UpdateQualityCheckCommand;
import com.gogidix.shared.warehousing.quality.application.dto.QualityCheckDTO;
import com.gogidix.shared.warehousing.quality.domain.entity.QualityCheck;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface QualityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "passed", ignore = true)
    @Mapping(target = "qualityScore", ignore = true)
    @Mapping(target = "inspectionDate", ignore = true)
    QualityCheck toEntity(CreateQualityCheckCommand command);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget QualityCheck check, UpdateQualityCheckCommand command);

    QualityCheckDTO toDTO(QualityCheck check);
    List<QualityCheckDTO> toDTOList(List<QualityCheck> checks);
}
