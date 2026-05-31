package com.gogidix.shared.warehousing.stock.application.mapper;

import com.gogidix.shared.warehousing.stock.application.command.CreateStockCommand;
import com.gogidix.shared.warehousing.stock.application.dto.StockLevelDTO;
import com.gogidix.shared.warehousing.stock.application.dto.StockMovementDTO;
import com.gogidix.shared.warehousing.stock.domain.entity.StockLevel;
import com.gogidix.shared.warehousing.stock.domain.entity.StockMovement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * MapStruct mapper for Stock entities/DTOs
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface StockMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "availableQuantity", source = "quantity")
    @Mapping(target = "reservedQuantity", constant = "0")
    @Mapping(target = "allocatedQuantity", constant = "0")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lastMovementAt", ignore = true)
    StockLevel toEntity(CreateStockCommand command);

    @Mapping(target = "totalQuantity", expression = "java(stockLevel.getTotalQuantity())")
    @Mapping(target = "belowReorderPoint", expression = "java(stockLevel.isBelowReorderPoint())")
    StockLevelDTO toDTO(StockLevel stockLevel);

    List<StockLevelDTO> toDTOList(List<StockLevel> stockLevels);

    StockMovementDTO toMovementDTO(StockMovement stockMovement);

    List<StockMovementDTO> toMovementDTOList(List<StockMovement> stockMovements);
}
