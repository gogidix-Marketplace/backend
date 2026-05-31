package com.gogidix.corporatecms.application.mapper;

import com.gogidix.corporatecms.application.dto.WorkflowDTO;
import com.gogidix.corporatecms.domain.model.Workflow;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Mapper for Workflow entity and DTO.
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    uses = {WorkflowStepMapper.class, WorkflowActionMapper.class}
)
public interface WorkflowMapper {

    WorkflowDTO toDto(Workflow workflow);

    Workflow toEntity(WorkflowDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromDto(WorkflowDTO dto, @MappingTarget Workflow workflow);

    List<WorkflowDTO> toDtoList(List<Workflow> workflowList);
}
