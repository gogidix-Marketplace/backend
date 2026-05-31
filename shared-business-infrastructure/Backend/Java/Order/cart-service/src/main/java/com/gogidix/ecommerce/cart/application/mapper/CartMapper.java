package com.gogidix.ecommerce.cart.application.mapper;

import com.gogidix.ecommerce.cart.application.dto.*;
import com.gogidix.ecommerce.cart.domain.model.Cart;
import com.gogidix.ecommerce.cart.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    public CartDto toDto(Cart entity) {
        List<CartItemDto> itemDtos = entity.getItems().stream()
            .map(this::toItemDto)
            .collect(Collectors.toList());

        return new CartDto(
            entity.getId(),
            entity.getTenantId(),
            entity.getCustomerId(),
            itemDtos,
            entity.getSubtotal(),
            entity.getTaxAmount(),
            entity.getDiscountAmount(),
            entity.getTotalAmount(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public CartItemDto toItemDto(Cart.CartItem item) {
        return new CartItemDto(
            item.getItemId(),
            item.getProductId(),
            item.getSku(),
            item.getName(),
            item.getImageUrl(),
            item.getUnitPrice(),
            item.getQuantity(),
            item.getLineTotal(),
            item.getVendorId(),
            item.getInStock()
        );
    }

    public Cart.CartItem toCartItem(AddCartItemRequest request) {
        return new Cart.CartItem(
            null,
            request.productId(),
            request.sku(),
            request.name(),
            request.imageUrl(),
            request.unitPrice(),
            request.quantity(),
            null,
            request.vendorId(),
            null
        );
    }

    public CartResponse toResponse(CartDto dto) {
        return CartResponse.from(dto);
    }
}
