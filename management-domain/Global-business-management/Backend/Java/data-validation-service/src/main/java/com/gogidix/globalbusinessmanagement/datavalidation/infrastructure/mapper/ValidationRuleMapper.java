package com.gogidix.globalbusinessmanagement.datavalidation.infrastructure.mapper;

import com.gogidix.globalbusinessmanagement.datavalidation.domain.dto.ValidationRuleDTO;
import com.gogidix.globalbusinessmanagement.datavalidation.domain.model.ValidationRule;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * MapStruct mapper for ValidationRule entity and DTO
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ValidationRuleMapper {

    ValidationRule toEntity(ValidationRuleDTO dto);

    ValidationRuleDTO toDto(ValidationRule entity);

    List<ValidationRule> toEntityList(List<ValidationRuleDTO> dtoList);

    List<ValidationRuleDTO> toDtoList(List<ValidationRule> entityList);

    void updateEntityFromDto(ValidationRuleDTO dto, @MappingTarget ValidationRule entity);
}
