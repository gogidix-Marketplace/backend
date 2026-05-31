package com.gogidix.corporatecms.application.mapper;

import com.gogidix.corporatecms.application.dto.LeadDTO;
import com.gogidix.corporatecms.domain.model.Lead;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Mapper for Lead entity and DTO.
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface LeadMapper {

    LeadDTO toDto(Lead lead);

    Lead toEntity(LeadDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromDto(LeadDTO dto, @MappingTarget Lead lead);

    List<LeadDTO> toDtoList(List<Lead> leadList);
}
