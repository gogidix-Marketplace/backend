package com.gogidix.corporatecms.application.mapper;

import com.gogidix.corporatecms.application.dto.WorkflowDTO.WorkflowActionDTO;
import com.gogidix.corporatecms.domain.model.Workflow.WorkflowAction;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Mapper for WorkflowAction nested class.
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface WorkflowActionMapper {

    WorkflowActionDTO toDto(WorkflowAction action);

    WorkflowAction toEntity(WorkflowActionDTO dto);

    List<WorkflowActionDTO> toDtoList(List<WorkflowAction> actions);
}
