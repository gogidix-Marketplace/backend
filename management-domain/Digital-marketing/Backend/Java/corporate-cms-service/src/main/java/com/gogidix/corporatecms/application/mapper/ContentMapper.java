package com.gogidix.corporatecms.application.mapper;

import com.gogidix.corporatecms.application.dto.ContentDTO;
import com.gogidix.corporatecms.domain.model.Content;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Mapper for Content entity and DTO.
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ContentMapper {

    ContentDTO toDto(Content content);

    Content toEntity(ContentDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromDto(ContentDTO dto, @MappingTarget Content content);

    List<ContentDTO> toDtoList(List<Content> contentList);
}
