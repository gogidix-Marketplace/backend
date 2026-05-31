package com.gogidix.corporatecms.application.mapper;

import com.gogidix.corporatecms.application.dto.UserDTO;
import com.gogidix.corporatecms.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Mapper for User entity and DTO.
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    @Mapping(target = "locale", ignore = true)
    UserDTO toDto(User user);

    @Mapping(target = "locale", ignore = true)
    User toEntity(UserDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "locale", ignore = true)
    void updateEntityFromDto(UserDTO dto, @MappingTarget User user);

    List<UserDTO> toDtoList(List<User> userList);
}
