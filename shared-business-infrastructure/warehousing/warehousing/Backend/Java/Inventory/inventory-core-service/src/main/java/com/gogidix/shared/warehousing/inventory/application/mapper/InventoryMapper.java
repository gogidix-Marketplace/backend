package com.gogidix.shared.warehousing.inventory.application.mapper;

import com.gogidix.shared.warehousing.inventory.application.command.CreateInventoryCommand;
import com.gogidix.shared.warehousing.inventory.application.command.UpdateInventoryCommand;
import com.gogidix.shared.warehousing.inventory.application.dto.InventoryDTO;
import com.gogidix.shared.warehousing.inventory.domain.entity.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * MapStruct mapper for Inventory entity/DTO conversions
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface InventoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Inventory toEntity(CreateInventoryCommand command);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "tenantType", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget Inventory inventory, UpdateInventoryCommand command);

    InventoryDTO toDTO(Inventory inventory);

    List<InventoryDTO> toDTOList(List<Inventory> inventories);
}
