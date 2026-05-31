package com.gogidix.ecommerce.cart.application.mapper;

import com.gogidix.ecommerce.cart.application.dto.AddItemRequest;
import com.gogidix.ecommerce.cart.application.dto.CartItemDto;
import com.gogidix.ecommerce.cart.application.dto.CartResponse;
import com.gogidix.ecommerce.cart.application.dto.CartSummaryDto;
import com.gogidix.ecommerce.cart.domain.model.Cart;
import com.gogidix.ecommerce.cart.domain.model.CartItem;
import com.gogidix.ecommerce.cart.domain.model.CartSummary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    public CartResponse toCartResponse(Cart cart) {
        if (cart == null) return null;

        return new CartResponse(
                cart.getId(),
                cart.getCartId(),
                cart.getCustomerId(),
                cart.getStatus(),
                cart.getCurrency(),
                cart.getChannelId(),
                toCartItemDtoList(cart.getItems()),
                toCartSummaryDto(cart.getSummary()),
                cart.getExpiresAt(),
                cart.getCreatedAt(),
                cart.getUpdatedAt(),
                cart.getVersion()
        );
    }

    public List<CartResponse> toCartResponseList(List<Cart> carts) {
        return carts.stream()
                .map(this::toCartResponse)
                .collect(Collectors.toList());
    }

    private List<CartItemDto> toCartItemDtoList(List<CartItem> items) {
        if (items == null) return List.of();
        return items.stream()
                .map(this::toCartItemDto)
                .collect(Collectors.toList());
    }

    private CartItemDto toCartItemDto(CartItem item) {
        if (item == null) return null;
        return new CartItemDto(
                item.getSku(),
                item.getProductId(),
                item.getProductName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice(),
                item.getCurrency(),
                item.getAttributes(),
                item.getAddedAt(),
                item.getUpdatedAt()
        );
    }

    private CartSummaryDto toCartSummaryDto(CartSummary summary) {
        if (summary == null) return null;
        return new CartSummaryDto(
                summary.getItemCount(),
                summary.getTotalQuantity(),
                summary.getSubtotal(),
                summary.getDiscountAmount(),
                summary.getTaxAmount(),
                summary.getShippingAmount(),
                summary.getTotalAmount()
        );
    }

    public CartItem toCartItem(AddItemRequest request) {
        return CartItem.create(
                request.sku(),
                request.productId(),
                request.productName(),
                request.quantity(),
                request.unitPrice(),
                request.currency() != null ? request.currency() : "USD"
        );
    }
}
