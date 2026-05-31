package com.gogidix.globalbusinessmanagement.datavalidation.infrastructure.mapper;

import com.gogidix.globalbusinessmanagement.datavalidation.domain.dto.ValidationResultDTO;
import com.gogidix.globalbusinessmanagement.datavalidation.domain.model.ValidationResult;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * MapStruct mapper for ValidationResult entity and DTO
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ValidationResultMapper {

    ValidationResult toEntity(ValidationResultDTO dto);

    ValidationResultDTO toDto(ValidationResult entity);

    List<ValidationResult> toEntityList(List<ValidationResultDTO> dtoList);

    List<ValidationResultDTO> toDtoList(List<ValidationResult> entityList);

    void updateEntityFromDto(ValidationResultDTO dto, @MappingTarget ValidationResult entity);
}
