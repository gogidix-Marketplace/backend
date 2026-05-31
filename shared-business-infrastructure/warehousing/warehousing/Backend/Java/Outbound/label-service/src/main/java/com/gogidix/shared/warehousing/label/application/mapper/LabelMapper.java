package com.gogidix.shared.warehousing.label.application.mapper;

import com.gogidix.shared.warehousing.label.application.dto.ShippingLabelDTO;
import com.gogidix.shared.warehousing.label.domain.entity.ShippingLabel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LabelMapper {

    ShippingLabelDTO toDTO(ShippingLabel label);
    List<ShippingLabelDTO> toDTOList(List<ShippingLabel> labels);
}
