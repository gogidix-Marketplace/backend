package com.gogidix.shared.warehousing.carrier.application.mapper;

import com.gogidix.shared.warehousing.carrier.application.dto.CarrierDTO;
import com.gogidix.shared.warehousing.carrier.application.dto.CarrierRateDTO;
import com.gogidix.shared.warehousing.carrier.application.dto.CarrierServiceDTO;
import com.gogidix.shared.warehousing.carrier.domain.entity.Carrier;
import com.gogidix.shared.warehousing.carrier.domain.entity.CarrierRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CarrierMapper {

    @Mapping(target = "carrierType", expression = "java(entity.getCarrierType() != null ? entity.getCarrierType().name() : null)")
    CarrierDTO toDTO(Carrier entity);

    List<CarrierDTO> toDTOList(List<Carrier> entities);

    Carrier toEntity(CarrierDTO dto);

    CarrierRateDTO toRateDTO(CarrierRate entity);

    List<CarrierRateDTO> toRateDTOList(List<CarrierRate> entities);

    CarrierRate toRateEntity(CarrierRateDTO dto);

    CarrierServiceDTO toServiceDTO(Carrier.CarrierService service);

    List<CarrierServiceDTO> toServiceDTOList(List<Carrier.CarrierService> services);
}
