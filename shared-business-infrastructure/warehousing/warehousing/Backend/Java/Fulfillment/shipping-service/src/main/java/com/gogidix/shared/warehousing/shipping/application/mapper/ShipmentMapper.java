package com.gogidix.shared.warehousing.shipping.application.mapper;

import com.gogidix.shared.warehousing.shipping.application.command.CreateShipmentCommand;
import com.gogidix.shared.warehousing.shipping.application.command.UpdateShipmentCommand;
import com.gogidix.shared.warehousing.shipping.application.dto.ShipmentDTO;
import com.gogidix.shared.warehousing.shipping.application.dto.TrackingEventDTO;
import com.gogidix.shared.warehousing.shipping.domain.entity.Shipment;
import com.gogidix.shared.warehousing.shipping.domain.entity.TrackingEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * MapStruct mapper for Shipment entity/DTO conversions
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ShipmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "trackingNumber", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "labelUrl", ignore = true)
    @Mapping(target = "shippingCost", ignore = true)
    @Mapping(target = "estimatedDelivery", ignore = true)
    @Mapping(target = "actualDelivery", ignore = true)
    @Mapping(target = "carrierData", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Shipment toEntity(CreateShipmentCommand command);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "orderNumber", ignore = true)
    @Mapping(target = "trackingNumber", ignore = true)
    @Mapping(target = "carrier", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "shipper", ignore = true)
    @Mapping(target = "originAddress", ignore = true)
    @Mapping(target = "labelUrl", ignore = true)
    @Mapping(target = "shippingCost", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget Shipment shipment, UpdateShipmentCommand command);

    ShipmentDTO toDTO(Shipment shipment);

    List<ShipmentDTO> toDTOList(List<Shipment> shipments);

    TrackingEventDTO toTrackingEventDTO(TrackingEvent event);

    List<TrackingEventDTO> toTrackingEventDTOList(List<TrackingEvent> events);
}
