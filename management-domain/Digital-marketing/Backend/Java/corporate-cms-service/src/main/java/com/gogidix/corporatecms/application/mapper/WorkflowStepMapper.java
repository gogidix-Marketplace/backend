package com.gogidix.corporatecms.application.mapper;

import com.gogidix.corporatecms.application.dto.WorkflowDTO.WorkflowStepDTO;
import com.gogidix.corporatecms.domain.model.Workflow.WorkflowStep;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Mapper for WorkflowStep nested class.
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface WorkflowStepMapper {

    WorkflowStepDTO toDto(WorkflowStep step);

    WorkflowStep toEntity(WorkflowStepDTO dto);

    List<WorkflowStepDTO> toDtoList(List<WorkflowStep> steps);
}
