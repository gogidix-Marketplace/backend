package com.gogidix.corporatecms.application.mapper;

import com.gogidix.corporatecms.application.dto.ProductDTO;
import com.gogidix.corporatecms.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * Mapper for Product entity and DTO.
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProductMapper {

    ProductDTO toDto(Product product);

    Product toEntity(ProductDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromDto(ProductDTO dto, @MappingTarget Product product);

    List<ProductDTO> toDtoList(List<Product> productList);
}
