package com.gogidix.shared.warehousing.order.application.mapper;

import com.gogidix.shared.warehousing.order.application.command.CreateOrderCommand;
import com.gogidix.shared.warehousing.order.application.dto.OutboundOrderDTO;
import com.gogidix.shared.warehousing.order.application.dto.OrderLineDTO;
import com.gogidix.shared.warehousing.order.domain.entity.OutboundOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.Named;

import java.util.List;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE
)
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "orderNumber", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "actualShipDate", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "shippingAddress", source = "shippingAddress", qualifiedByName = "stringToShippingAddress")
    OutboundOrder toEntity(CreateOrderCommand command);

    @Mapping(target = "status", expression = "java(entity.getStatus() != null ? entity.getStatus().name() : null)")
    @Mapping(target = "shippingAddress", source = "shippingAddress", qualifiedByName = "shippingAddressToString")
    OutboundOrderDTO toDTO(OutboundOrder entity);

    List<OutboundOrderDTO> toDTOList(List<OutboundOrder> entities);

    OrderLineDTO toLineDTO(OutboundOrder.OrderLine line);

    List<OrderLineDTO> toLineDTOList(List<OutboundOrder.OrderLine> lines);

    @Named("stringToShippingAddress")
    default OutboundOrder.ShippingAddress stringToShippingAddress(String address) {
        if (address == null) return null;
        return OutboundOrder.ShippingAddress.builder()
            .line1(address)
            .build();
    }

    @Named("shippingAddressToString")
    default String shippingAddressToString(OutboundOrder.ShippingAddress address) {
        if (address == null) return null;
        return address.getLine1();
    }
}
