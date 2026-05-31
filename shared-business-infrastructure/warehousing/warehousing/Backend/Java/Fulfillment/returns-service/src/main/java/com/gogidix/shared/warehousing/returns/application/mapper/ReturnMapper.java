package com.gogidix.shared.warehousing.returns.application.mapper;

import com.gogidix.shared.warehousing.returns.application.command.CreateReturnCommand;
import com.gogidix.shared.warehousing.returns.application.command.UpdateReturnCommand;
import com.gogidix.shared.warehousing.returns.application.dto.ReturnDTO;
import com.gogidix.shared.warehousing.returns.domain.entity.Return;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ReturnMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "rmaNumber", ignore = true)
    @Mapping(target = "requestedDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    Return toEntity(CreateReturnCommand command);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "rmaNumber", ignore = true)
    void updateEntity(@MappingTarget Return ret, UpdateReturnCommand command);

    ReturnDTO toDTO(Return ret);
    List<ReturnDTO> toDTOList(List<Return> returns);
}
