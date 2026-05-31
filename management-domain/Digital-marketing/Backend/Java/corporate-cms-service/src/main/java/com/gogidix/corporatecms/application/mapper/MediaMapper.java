package com.gogidix.corporatecms.application.mapper;

import com.gogidix.corporatecms.application.dto.MediaDTO;
import com.gogidix.corporatecms.domain.model.Media;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Mapper for Media entity and DTO.
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface MediaMapper {

    MediaDTO toDto(Media media);

    Media toEntity(MediaDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromDto(MediaDTO dto, @MappingTarget Media media);

    List<MediaDTO> toDtoList(List<Media> mediaList);
}
