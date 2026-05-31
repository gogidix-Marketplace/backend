package com.gogidix.shared.warehousing.availability.application.mapper;

import com.gogidix.shared.warehousing.availability.application.command.CreateCapacityPoolCommand;
import com.gogidix.shared.warehousing.availability.application.dto.*;
import com.gogidix.shared.warehousing.availability.domain.entity.AvailabilitySlot;
import com.gogidix.shared.warehousing.availability.domain.entity.CapacityPool;
import com.gogidix.shared.warehousing.availability.domain.entity.StorageAvailability;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * MapStruct mapper for availability entity/DTO conversions
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE
)
public interface AvailabilityMapper {

    // StorageAvailability mappings
    StorageAvailabilityDTO toStorageAvailabilityDTO(StorageAvailability availability);

    List<StorageAvailabilityDTO> toStorageAvailabilityDTOList(List<StorageAvailability> availabilityList);

    // AvailabilitySlot mappings
    AvailabilitySlotDTO toAvailabilitySlotDTO(AvailabilitySlot slot);

    List<AvailabilitySlotDTO> toAvailabilitySlotDTOList(List<AvailabilitySlot> slots);

// CapacityPool mappings
@Mapping(target = "id", ignore = true)
@Mapping(target = "tenantId", ignore = true)
@Mapping(target = "createdAt", ignore = true)
@Mapping(target = "updatedAt", ignore = true)
@Mapping(target = "availableCapacity", constant = "0")
@Mapping(target = "reservedCapacity", constant = "0")
@Mapping(target = "lastReservedAt", ignore = true)
@Mapping(target = "lastReleasedAt", ignore = true)
CapacityPool toEntity(CreateCapacityPoolCommand command);

    @Mapping(target = "utilizationPercentage", expression = "java(entity.calculateUtilization())")
    CapacityPoolDTO toCapacityPoolDTO(CapacityPool entity);

    List<CapacityPoolDTO> toCapacityPoolDTOList(List<CapacityPool> pools);
}
